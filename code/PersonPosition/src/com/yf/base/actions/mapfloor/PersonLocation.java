package com.yf.base.actions.mapfloor;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.jdom.JDOMException;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.actions.commons.CommunityUtils;
import com.yf.base.actions.datastatistics.historyroute.MinRouteUtils;
import com.yf.base.actions.datastatistics.warn.Commons;
import com.yf.tradecontrol.GlobalVar;
import com.yf.tradecontrol.JDomHandler;
import com.yf.tradecontrol.JDomHandlerException;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class PersonLocation extends ActionSupport {
	private static final long serialVersionUID = 1L;
	public JDomHandler domHandler = new JDomHandler();
	public static final String xmlpath = GlobalVar.WORKPATH + File.separator + "config" + File.separator + "dsSystemConfig.xml";
	
	private static final int GAP = 20;//设置偏离距离
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private MinRouteUtils utils;
	private String jsonString;
	private String communityId;
	private String previousLoadDate;
//	private String maxSecondForHiddenPerson;
	
	private String nextLoadDate;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(getRFIDSql());
		nextLoadDate = previousLoadDate;
		if(dataList.size() > 0){
			Floor floor = new Floor();
			floor.generFloorData(dataList,communityId);//生成页面的楼层数据
			
			utils = new MinRouteUtils(communityId);
			Map<String,String> previousLocation = updateSessionData();//这个map中的数据保存了人员前一次获取到位置点的信息（某个人在某个rfid卡上）
			
			nextLoadDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
			SimpleDateFormat formate = new SimpleDateFormat("HH:mm:ss");
			Map<String,Map<String,List<Map<String,Object>>>> personCheckRoutePoints = getPersonCheckRoutePoints();
			Map<String,Map<String,List<JSONObject>>> personCheckWarnAreas = getPersonCheckWarnAreas();
			Map<String,List<Map<String,Object>>> warnAreas = Commons.getWarnAreas(dbhelper);
			Map<String,String> eventTypeIds = getWarnEventTypeId("WARN_EVENT_TYPE");
			
			Iterator<Map<String,Object>> ite = dataList.iterator();
			while(ite.hasNext()){
				Map<String,Object> rowData = ite.next();
				String personId = rowData.get("id").toString();
				generAdjacentPoints(previousLocation.get(personId),rowData);
				previousLocation.put(personId, rowData.get("card_mark").toString());
				
				Double locationX = (Double)rowData.get("locationX");
				Double locationY = (Double)rowData.get("locationY");
				Date locationDate = formate.parse(rowData.get("date").toString().substring(11));
				
				JSONArray events = new JSONArray();
				
				if(!personId.endsWith(Floor.ADD_STR)){//如果当前人员没有进行事件检测，则检测违规事件（主要针对floor.generFloorData(dataList)产生的数据）
					//check points
					//5-29更新了判定违规的规则(可能其他地方也要做改动,如数据统计中的考勤,违规记录等)
					//路线违规判定规则:假如某个人分配了多个路线时间段,如果当前位置在任何一个分配的时间段的路线上是非法的,如果路线不重复则记录违法事件
					Map<String,List<Map<String,Object>>> routeTimes = personCheckRoutePoints.get(personId);
					Set<String> illegalRoute = new HashSet<String>();
					if(null != routeTimes){//这个人分配了巡更路线
						Iterator<String> iteTimes = routeTimes.keySet().iterator();
						while(iteTimes.hasNext()){//遍历每一个分配的时间段
							String routeTimeId = iteTimes.next();
							List<Map<String,Object>> points = routeTimes.get(routeTimeId);
							try {
								//获取这个人当前时间在该分配时间段的合法的位置点(可能有多个)
								List<Map<String,Object>> stayPointsList = getPointIndex(points,locationDate);
								if(stayPointsList.size() > 0){//找到对应时间段的路线点
									Iterator<Map<String,Object>> stayPointsIte = stayPointsList.iterator();
									boolean inPointLegalRange = false;
									Map<String,Object> point = null;
									//5-29更新了判定违规的规则:只要当前位置在任何一个合法的路线点上,则位置合法(可能其他地方也要做改动,如数据统计中的考勤,违规记录等)
									while(!inPointLegalRange && stayPointsIte.hasNext()){
										point = stayPointsIte.next();
										if(Math.sqrt(Math.pow((locationX-(Double)point.get("location_x")), 2)+Math.pow((locationY-(Double)point.get("location_y")), 2)) 
												<= (Double)point.get("effective_range")){//在有效范围内
											inPointLegalRange = true;
										}
									}
									//在当前需要检查的时间段的路线内,没有合法的位置
									if(!inPointLegalRange && !illegalRoute.contains(point.get("route_name").toString())){
										JSONObject event = new JSONObject();
										event.put("eventType", "3");//值对应于字典表中TYPE_NAME = WARN_EVENT_TYPE 的KEY_NAME值
										event.put("eventName", point.get("route_name"));
										String eventId = writeWarnEvent(locationX,locationY,eventTypeIds.get("3"),rowData.get("date").toString(),personId);//记录非法事件
										if(null != eventId){
											event.put("eventId", eventId);
											events.add(event);
											illegalRoute.add(point.get("route_name").toString());
										}
									}
								}
							} catch (ParseException e) {
								e.printStackTrace();
								break;
							}
						}
					}
					//check areas
					Map<String,List<JSONObject>> areas = personCheckWarnAreas.get(personId);
					if(null != areas){//该人员分配了区域
						Iterator<String> iteAreas = areas.keySet().iterator();
						while(iteAreas.hasNext()){//迭代人员区域指定的区域
							String areaId = iteAreas.next();
							//人员当前位置处于当前迭代的区域的那个位置(1==区域内,-1==区域外,0==区域的线上)
							int pointPosition = Commons.checkPointInWarnArea(warnAreas.get(areaId),locationX,locationY);
							List<JSONObject> timesAndTypes = areas.get(areaId);
							Iterator<JSONObject> iteTimesAndTypes = timesAndTypes.iterator();
							while(iteTimesAndTypes.hasNext()){//迭代区域中考核的时间区间和类型
								JSONObject timeAndType = iteTimesAndTypes.next();
								String areaType = timeAndType.getString("areaType");
								Date startTime = formate.parse(timeAndType.getString("startTime"));
								Date endTime = formate.parse(timeAndType.getString("endTime"));
								if(locationDate.after(startTime) && locationDate.before(endTime)){//该人员当前有分配的区域
									//9==禁止进入,10==禁止离开
									String eventType = ("9".equals(areaType) && 1 == pointPosition)//进入了禁止进入的区域
												?"2":
													(("10".equals(areaType) && -1 == pointPosition)//离开了规定的区域
															?"1":"");
									if(!"".equals(eventType)){//当前时间段的区域不合法
										JSONObject event = new JSONObject();
										event.put("eventType", eventType);//值对应于字典表中TYPE_NAME = WARN_EVENT_TYPE 的KEY_NAME值
										event.put("eventName", timeAndType.getString("areaName"));
										String eventId = writeWarnEvent(locationX,locationY,eventTypeIds.get(eventType),rowData.get("date").toString(),personId);//记录非法事件
										if(null != eventId){
											event.put("eventId", eventId);
											events.add(event);
										}
										break;
									}
								}
							}
						}
					}
					System.err.println("-----------------------"+events.toString());
					rowData.put("wareEvents", events.toString());
//					floor.addFloorwareEvents(dataList, personId, events);//将对应于floor.generFloorData(dataList)产生的数据的人员的wareEvents也设置为该警告事件
				}
				
				//随机偏移人员的位置，避免人员在地图上图标重叠
				double locationModifyX = locationX-GAP/2+Math.round(Math.random()*GAP);
				double locationModifyY = locationY-GAP/2+Math.round(Math.random()*GAP);
				rowData.put("locationX", locationModifyX);
				rowData.put("locationY", locationModifyY);
				Map<String,Object> thisPoint = new HashMap<String,Object>();
				thisPoint.put("locationX", locationModifyX);
				thisPoint.put("locationY", locationModifyY);
				((List<Map<String,Object>>)rowData.get("adjacentPoints")).add(thisPoint);//把当前点也加入进去
			}
		}
		System.err.println(">>>>>>>>>>>>>>>>>>"+dataList.size());
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("list", dataList.toArray());
		resultMap.put("nextLoadDate", nextLoadDate);
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		return "data";
	}
	
	private void generAdjacentPoints(String start_card_mark,Map<String,Object> point){
		List<Map<String,Object>> points = new ArrayList<Map<String,Object>>();
		if(StringUtils.isNotBlank(start_card_mark) && StringUtils.isNotBlank(point.get("card_mark").toString())){
			Map<String,JSONObject> adjacentPoints = utils.getAdjacentPoints(start_card_mark,point.get("card_mark").toString());
			Iterator<String> ite = adjacentPoints.keySet().iterator();
			while(ite.hasNext()){
				Map<String,Object> rowData = new HashMap<String,Object>();
				JSONObject thisPoint = adjacentPoints.get(ite.next());
				rowData.put("locationX", thisPoint.get("locationX"));
				rowData.put("locationY", thisPoint.get("locationY"));
				points.add(rowData);
			}
		}
//		Map<String,Object> thisPoint = new HashMap<String,Object>();
//		thisPoint.put("locationX", point.get("locationX"));
//		thisPoint.put("locationY", point.get("locationY"));
//		points.add(thisPoint);//把当前点也加入进去
		point.put("adjacentPoints", points);
	}
	@SuppressWarnings("unchecked")
	private Map<String,String> updateSessionData(){
		Map<String,Object> session = ActionContext.getContext().getSession();
		Object position = session.get("personPreviousLocation");
		Map<String,String> positionMap = null;
		if(position == null){
			positionMap = new HashMap<String,String>();
		}else{
			positionMap = (Map<String, String>) position;
		}
		session.put("personPreviousLocation", positionMap);
		return positionMap;
	}
	
	private String getRFIDSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (");
		sql.append("select c.card_mark,c.communityId,p.id,DATE_FORMAT(prl.upload_time,'%Y-%m-%d %H:%i:%s') date,");
		sql.append("c.locationX,c.locationY,if(prl.insert_date >= '"+previousLoadDate+"','1','0') is_new ");
		sql.append("from bp_phone_rfid_location_tbl prl ");
		sql.append("join bp_person_tbl p on (p.communityId='"+communityId+"' and p.phone = prl.phone) ");
		sql.append("join bp_card_tbl c on (c.card_mark = prl.card_id) ");
		String hidePersonInterval = "60";
		try {
			domHandler.loadXmlByPath(xmlpath);
			hidePersonInterval = domHandler.getNodeValue("/ds-config/location/hidePersonInterval");
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (JDomHandlerException e) {
			e.printStackTrace();
		}
		sql.append("where TIMESTAMPDIFF(SECOND,prl.insert_date,now()) < "+hidePersonInterval+" ");
//		if(StringUtils.isNotBlank(previousLoadDate)){
//			sql.append("where prl.insert_date > '"+previousLoadDate+"' ");
//		}
		sql.append("order by prl.upload_time desc) a ");
		sql.append("group by id");
		System.err.println(sql.toString());
		return sql.toString();
	}
	/**
	 * 检测人员的当前时间,是否在某个规定的路线时间段中(与路线上每个点的时间段对比),如果是,则返回所有匹配的路线上的位置点
	 * @param points
	 * @param locationDate
	 * @return
	 * @throws ParseException
	 */
	private List<Map<String,Object>> getPointIndex(List<Map<String,Object>> points,Date locationDate) throws ParseException{
		List<Map<String,Object>> stayPoints = new ArrayList<Map<String,Object>>();
		SimpleDateFormat formate = new SimpleDateFormat("HH:mm:ss");
		for(int i = 0;i<points.size();i++){
			Map<String,Object> point = points.get(i);
			Date startTime = formate.parse(point.get("start_time").toString());
			Date endTime = formate.parse(point.get("end_time").toString());
			Date locationTime = formate.parse(formate.format(locationDate));
			if(startTime.before(locationTime) && endTime.after(locationTime)){
				stayPoints.add(point);
			}
		}
		return stayPoints;
	}
	
	private String writeWarnEvent(double locationX,double locationY,String eventTypeId,String eventTime,String personId){
		String personCommunity = CommunityUtils.getParentCommunity(communityId);
		StringBuffer sql1 = new StringBuffer();
		sql1.append("select id from bp_warn_event_tbl ");
		sql1.append("where warn_person_id=? and event_type=? and ");
		sql1.append("event_time=? and location_x=? and location_y=? and community_id=? ");
		List<?> dataList = dbhelper.getMapListBySql(sql1.toString(),new Object[]{personId,eventTypeId,eventTime,locationX,locationY,personCommunity});
		if(dataList.size() == 0){
			StringBuffer sql = new StringBuffer();
			sql.append("insert into bp_warn_event_tbl(id,warn_person_id,event_type,event_time,community_id,location_x,location_y,add_date) ");
			sql.append("VALUES(?,?,?,?,?,?,?,now()) ");
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			Object[] params = new Object[7];
			params[0] = id;
			params[1] = personId;
			params[2] = eventTypeId;
			params[3] = eventTime;
			params[4] = personCommunity;
			params[5] = locationX;
			params[6] = locationY;
			if(dbhelper.insert(sql.toString(),params)){
				return id;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private Map<String,String> getWarnEventTypeId(String eventType){
		Map<String,String> eventTypeIds = new HashMap<String,String>();
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select dic_id,key_name from sys_dictionary_tbl where type_name = '"+eventType+"'");
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(getListSql.toString());
		Iterator<Map<String,Object>> ite = dataList.iterator();
		while(ite.hasNext()){
			Map<String,Object> rowData = ite.next();
			eventTypeIds.put(rowData.get("key_name").toString(), rowData.get("dic_id").toString());
		}
		return eventTypeIds;
	}
	
	/** 获取当前小区所有人员对应的所有路线上路线点的信息
	 *  如当前小区有5个人,则返回的外层map的长度就是5;
	 *  	某个人分配了3个时间段的路线,则这个人的第二层map长度就是3;
	 *  	有一个时间段上的路线上有5个点,则list长度为5
	 */
	@SuppressWarnings("unchecked")
	private Map<String,Map<String,List<Map<String,Object>>>> getPersonCheckRoutePoints(){//Map<人员ID,Map<路线时间ID,List<分配的路线时间和类型>>>
		Map<String,Map<String,List<Map<String,Object>>>> personCheckRoutes = new HashMap<String,Map<String,List<Map<String,Object>>>>();
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select rta.person_id,r.effective_range,r.name route_name,rt.id route_time_id,");
		getListSql.append("rp.id route_point_id,rp.location_x,rp.location_y,rpt.start_time,rpt.end_time ");
		getListSql.append("from bp_fine_route_time_assign_tbl rta ");
		getListSql.append("join bp_fine_route_time_tbl rt on (rta.route_time_id = rt.id) ");
		getListSql.append("join bp_fine_route_tbl r on (rt.route_id = r.id) ");//r.community_id = '"+communityId+"' and 
		getListSql.append("join bp_fine_route_point_tbl rp on (r.id = rp.route_id) ");
		getListSql.append("join bp_fine_route_point_time_tbl rpt on (rt.id = rpt.route_time_id and rp.id = rpt.route_point_id) ");
		getListSql.append("order by rta.person_id,r.id,rt.id,rpt.start_time ");
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(getListSql.toString());
		Iterator<Map<String,Object>> ite = dataList.iterator();
		String ignoreRouteTimeId = "";
		while(ite.hasNext()){
			Map<String,Object> rowData = ite.next();
			String personId = rowData.get("person_id").toString();
			String routeTimeId = rowData.get("route_time_id").toString();
			if(ignoreRouteTimeId.equals(routeTimeId)){//忽略没有设置详细路线点时间段的路线时间
				continue;
			}
			Map<String,List<Map<String,Object>>> routes = personCheckRoutes.get(personId);
			if(null == routes){
				routes = new HashMap<String,List<Map<String,Object>>>();
				personCheckRoutes.put(personId, routes);
			}
			Object startTime = rowData.get("start_time");
			Object endTime = rowData.get("end_time");
			if(null == startTime || null == endTime){
				ignoreRouteTimeId = routeTimeId;
				routes.remove(routeTimeId);//删除未设置路线点时间段的路线
				continue;
			}
			List<Map<String,Object>> points = routes.get(routeTimeId);
			if(null == points){
				points = new ArrayList<Map<String,Object>>();
				routes.put(routeTimeId, points);
			}
			points.add(rowData);
		}
		return personCheckRoutes;
	}
	
	/** 获取所有人员分配的区域及时间段
	 *  如当前小区有5个人,则返回的外层map的长度就是5;
	 *  	某个人分配了3个时间段,3个时间段包含了两个区域(有一个区域分配了两个时间段),则这个人的第二层map长度就是2(区域的个数);
	 *  	在这个人分配的区域中有3个时间段,则这个区域对应的list的长度为3;
	 */
	@SuppressWarnings("unchecked")
	private Map<String,Map<String,List<JSONObject>>> getPersonCheckWarnAreas(){//Map<人员ID,Map<区域ID,List<分配的区域时间和类型>>>
		Map<String,Map<String,List<JSONObject>>> personCheckWarnAreas = new HashMap<String,Map<String,List<JSONObject>>>();
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select ta.person_id,t.area_type,DATE_FORMAT(t.start_time,'%H:%i:%s') start_time,DATE_FORMAT(t.end_time,'%H:%i:%s') end_time,t.area_id,a.name ");
		getListSql.append("from bp_fine_area_time_assign_tbl ta join bp_fine_area_time_tbl t on (t.is_used and t.id = ta.area_time_id) ");
		getListSql.append("join bp_fine_area_tbl a on (a.id = t.area_id) ");//a.community_id = '"+communityId+"' and 
		getListSql.append("order by ta.person_id ");
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(getListSql.toString());
		Iterator<Map<String,Object>> ite = dataList.iterator();
		while(ite.hasNext()){
			Map<String,Object> rowData = ite.next();
			String personId = rowData.get("person_id").toString();
			String areaId = rowData.get("area_id").toString();
			JSONObject timeAndType = new JSONObject();
			timeAndType.put("areaName", rowData.get("name"));
			timeAndType.put("areaType", rowData.get("area_type"));
			timeAndType.put("startTime", rowData.get("start_time"));
			timeAndType.put("endTime", rowData.get("end_time"));
			Map<String,List<JSONObject>> areas = personCheckWarnAreas.get(personId);
			if(null == areas){
				areas = new HashMap<String,List<JSONObject>>();
				personCheckWarnAreas.put(personId, areas);
			}
			List<JSONObject> timesAndTypes = areas.get(areaId);
			if(null == timesAndTypes){
				timesAndTypes = new ArrayList<JSONObject>();
				areas.put(areaId, timesAndTypes);
			}
			timesAndTypes.add(timeAndType);
		}
		return personCheckWarnAreas;
	}
	
	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	public String getCommunityId() {
		return communityId;
	}
	
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	
	public String getPreviousLoadDate() {
		return previousLoadDate;
	}
	
	public void setPreviousLoadDate(String previousLoadDate) {
		this.previousLoadDate = previousLoadDate;
	}
	
//	public String getMaxSecondForHiddenPerson() {
//		return maxSecondForHiddenPerson;
//	}
//	
//	public void setMaxSecondForHiddenPerson(String maxSecondForHiddenPerson) {
//		this.maxSecondForHiddenPerson = maxSecondForHiddenPerson;
//	}
	
}
