package com.yf.base.actions.location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.actions.datastatistics.warn.Commons;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class PersonLocation_05_29 extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String communityId;
	private String previousLoadDate;
	
	private String nextLoadDate;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(getRFIDSql());
		nextLoadDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		if(dataList.size() > 0){
			SimpleDateFormat formate = new SimpleDateFormat("HH:mm:ss");
			Map<String,Map<String,List<Map<String,Object>>>> personCheckRoutePoints = getPersonCheckRoutePoints();
			Map<String,Map<String,List<JSONObject>>> personCheckWarnAreas = getPersonCheckWarnAreas();
			Map<String,List<Map<String,Object>>> warnAreas = Commons.getWarnAreas(dbhelper);
			Map<String,String> eventTypeIds = getWarnEventTypeId("WARN_EVENT_TYPE");
			
			Iterator<Map<String,Object>> ite = dataList.iterator();
			while(ite.hasNext()){
				Map<String,Object> rowData = ite.next();
				String personId = rowData.get("id").toString();
				Double locationX = (Double)rowData.get("locationX");
				Double locationY = (Double)rowData.get("locationY");
				Date locationDate = formate.parse(rowData.get("date").toString().substring(11));
				
				JSONArray events = new JSONArray();
				//check points
//				List<JSONObject> points = personCheckRoutePoints.get(personId);
//				if(null != points){
//					Iterator<JSONObject> itePoints = points.iterator();
//					while(itePoints.hasNext()){
//						JSONObject point = itePoints.next();
//						Date startTime = formate.parse(point.getString("effective_start_time"));
//						Date endTime = formate.parse(point.getString("effective_end_time"));
//						if(locationDate.after(startTime) && locationDate.before(endTime)){
//							Double pointLocationX = point.getDouble("location_x");
//							Double pointLocationY = point.getDouble("location_y");
//							Double effectiveRange = point.getDouble("effective_range");
//							if(Math.pow((locationX - pointLocationX), 2)+Math.pow((locationY - pointLocationY), 2) > Math.pow(effectiveRange,2)){
//								JSONObject event = new JSONObject();
//								event.put("eventType", "3");//值对应于字典表中TYPE_NAME = WARN_EVENT_TYPE 的KEY_NAME值
//								event.put("eventName", point.getString("routeName"));
//								String eventId = writeWarnEvent(locationX,locationY,eventTypeIds.get("3"),rowData.get("date").toString(),personId);//记录非法事件
//								if(null != eventId){
//									event.put("eventId", eventId);
//									events.add(event);
//								}
//								break;//这里当当前位置在某个路线的某个点处违法了就不再监测其他的点是否违法，
//							}
//						}
//					}
//				}
				Map<String,List<Map<String,Object>>> routeTimes = personCheckRoutePoints.get(personId);
				if(null != routeTimes){
					Iterator<String> iteTimes = routeTimes.keySet().iterator();
					while(iteTimes.hasNext()){//遍历每一个时间段
						String routeTimeId = iteTimes.next();
						List<Map<String,Object>> points = routeTimes.get(routeTimeId);
						try {
							int pointIdex = getPointIndex(points);
							if(pointIdex == -1){//没找到对应时间段的路线点
//								int nearestPointIndex = getNearestPointIndex(points);
//								if(-1 != nearestPointIndex){//得到最近一个时间段的点
//									Map<String,Object> point = points.get(nearestPointIndex);
//									if(getCrossPointNumber(personId,point) == 0){//之前没有经过该点
//										if(!existLog(point.get("person_id").toString(),point.get("route_time_id").toString(),point.get("point_id").toString(),"2")){//"2"代表违规
//											writeLog(point.get("person_id").toString(),point.get("route_time_id").toString(),point.get("point_id").toString(),"2");
//										}
//									}
//								}
							}else{//找到对应时间段的路线点
								Map<String,Object> point = points.get(pointIdex);
								if(Math.sqrt(Math.pow((locationX-(Double)point.get("location_x")), 2)+Math.pow((locationY-(Double)point.get("location_y")), 2)) 
										> (Double)point.get("effective_range")){//不在有效范围内
									if(getCrossPointNumber(personId,point) == 0){//之前没有经过该点
										JSONObject event = new JSONObject();
										event.put("eventType", "3");//值对应于字典表中TYPE_NAME = WARN_EVENT_TYPE 的KEY_NAME值
										event.put("eventName", point.get("route_name"));
										String eventId = writeWarnEvent(locationX,locationY,eventTypeIds.get("3"),rowData.get("date").toString(),personId);//记录非法事件
										if(null != eventId){
											event.put("eventId", eventId);
											events.add(event);
										}
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
				if(null != areas){
					Iterator<String> iteAreas = areas.keySet().iterator();
					while(iteAreas.hasNext()){//迭代人员区域指定的区域
						String areaId = iteAreas.next();
						int pointPosition = Commons.checkPointInWarnArea(warnAreas.get(areaId),locationX,locationY);
						List<JSONObject> timesAndTypes = areas.get(areaId);
						Iterator<JSONObject> iteTimesAndTypes = timesAndTypes.iterator();
						while(iteTimesAndTypes.hasNext()){//迭代区域中考核的时间区间和类型
							JSONObject timeAndType = iteTimesAndTypes.next();
							String areaType = timeAndType.getString("areaType");
							Date startTime = formate.parse(timeAndType.getString("startTime"));
							Date endTime = formate.parse(timeAndType.getString("endTime"));
							if(locationDate.after(startTime) && locationDate.before(endTime)){
								String eventType = ("9".equals(areaType) && 1 == pointPosition)//进入了禁止进入的区域
											?"2":
												(("10".equals(areaType) && -1 == pointPosition)//离开了规定的区域
														?"1":"");
								if(!"".equals(eventType)){
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
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("list", dataList.toArray());
		resultMap.put("nextLoadDate", nextLoadDate);
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		return "data";
	}
	
	private String getGPSSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("select p.id,DATE_FORMAT(l.date,'%Y-%m-%d %H:%i:%s') date,l.locationX,l.locationY ");
		sql.append("from bp_location_current_tbl l ");
		sql.append("join bp_person_tbl p on (p.communityId='"+communityId+"' and l.personId = p.id) ");
		return sql.toString();
	}
	
	private String getRFIDSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (");
		sql.append("select p.id,DATE_FORMAT(prl.upload_time,'%Y-%m-%d %H:%i:%s') date,c.locationX,c.locationY ");
		sql.append("from bp_phone_rfid_location_tbl prl ");
		sql.append("join bp_person_tbl p on (p.communityId='"+communityId+"' and p.phone = prl.phone) ");
		sql.append("join bp_card_tbl c on (c.card_mark = prl.card_id) ");
		if(StringUtils.isNotBlank(previousLoadDate)){
			sql.append("where prl.insert_date > '"+previousLoadDate+"' ");
		}
		sql.append("order by prl.upload_time desc) a ");
		sql.append("group by id");
		return sql.toString();
	}
	
	private int getPointIndex(List<Map<String,Object>> points) throws ParseException{//数据正确的情况下只会有一个point符合条件
		SimpleDateFormat formate = new SimpleDateFormat("HH:mm:ss");
		for(int i = 0;i<points.size();i++){
			Map<String,Object> point = points.get(i);
			Date startTime = formate.parse(point.get("start_time").toString());
			Date endTime = formate.parse(point.get("end_time").toString());
			Date now = formate.parse(formate.format(new Date()));
			if(startTime.before(now) && endTime.after(now)){
				return i;
			}
		}
		return -1;
	}
	
	private int getNearestPointIndex(List<Map<String,Object>> points) throws ParseException{
		SimpleDateFormat formate = new SimpleDateFormat("HH:mm:ss");
		for(int i = points.size()-1;i>=0;i--){
			Map<String,Object> point = points.get(i);
			Date endTime = formate.parse(point.get("end_time").toString());
			Date now = formate.parse(formate.format(new Date()));
			if(endTime.before(now)){
				return i;
			}
		}
		return -1;
	}
	
	@SuppressWarnings("unchecked")
	private long getCrossPointNumber(String personId,Map<String,Object> point){
		String pointId = point.get("route_point_id").toString();
		String startTime = point.get("start_time").toString();
		String endTime = point.get("end_time").toString();
		String sql = "select count(*) count from bp_fine_route_tbl r " +
				"join bp_fine_route_point_tbl rp on (rp.id = '"+pointId+"' and r.id = rp.route_id) " +
				"join bp_location_history_tbl lh on (lh.personId = '"+personId+"' and lh.date > DATE(now()) " +
				"and TIME(lh.date) > '"+startTime+"' and TIME(lh.date) < '"+endTime+"' and " +
				"sqrt(pow(rp.location_x-lh.locationX,2)+pow(rp.location_y-lh.locationY,2)) < r.effective_range)";
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
		return (Long)dataList.get(0).get("count");
	}
	
	private String writeWarnEvent(double locationX,double locationY,String eventTypeId,String eventTime,String personId){
		StringBuffer sql = new StringBuffer();
		sql.append("insert into bp_warn_event_tbl(id,warn_person_id,event_type,event_time,location_x,location_y,add_date) ");
		sql.append("VALUES(?,?,?,?,?,?,now()) ");
		String id = UUID.randomUUID().toString().replaceAll("-", "");
		Object[] params = new Object[6];
		params[0] = id;
		params[1] = personId;
		params[2] = eventTypeId;
		params[3] = eventTime;
		params[4] = locationX;
		params[5] = locationY;
		if(dbhelper.insert(sql.toString(),params)){
			return id;
		}
		return null;
	}
//	private String writeWarnEvent(double locationX,double locationY,String eventTime,String personId,JSONArray events){
//		Map<String,String> eventTypeIds = getWarnEventTypeId("WARN_EVENT_TYPE");
//		List<String> sqls = new ArrayList<String>();
//		for(int i=0;i<events.size();i++){
//			JSONObject event = (JSONObject)events.get(i);
//			String eventTypeId = eventTypeIds.get(event.get("eventType"));
//			String id = UUID.randomUUID().toString().replaceAll("-", "");
//			sqls.add("insert into bp_warn_event_tbl(id,warn_person_id,event_type,event_time,location_x,location_y,add_date) VALUES('"+id+"','"+personId+"','"+eventTypeId+"','"+eventTime+"','"+locationX+"','"+locationY+"',now()) ");
//		}
//		if(dbhelper.executeFor(sqls)){
//			return id;
//		}
//		return null;
//	}
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
		getListSql.append("join bp_fine_route_tbl r on (r.community_id = '"+communityId+"' and rt.route_id = r.id) ");
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
	
	
//	@SuppressWarnings("unchecked")//需更改，表已做改动
//	private Map<String,List<JSONObject>> getPersonCheckRoutePoints(){
//		Map<String,List<JSONObject>> personCheckRoutePoints = new HashMap<String,List<JSONObject>>();
//		StringBuffer getListSql = new StringBuffer();
//		getListSql.append("select rpa.person_id,rp.name,rpp.location_x,rpp.location_y,rpp.effective_range,DATE_FORMAT(rpp.effective_start_time,'%H:%i:%s') effective_start_time,DATE_FORMAT(rpp.effective_end_time,'%H:%i:%s') effective_end_time ");
//		getListSql.append("from bp_fine_route_tbl rp join bp_route_person_assign_tbl rpa on (rp.community_id = '"+communityId+"' and rpa.route_id = rp.id) ");
//		getListSql.append("join bp_fine_route_point_tbl rpp on (rpa.route_id = rpp.route_id) ");
//		getListSql.append("order by rpa.person_id,rpp.id ");
//		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(getListSql.toString());
//		Iterator<Map<String,Object>> ite = dataList.iterator();
//		while(ite.hasNext()){
//			Map<String,Object> rowData = ite.next();
//			String personId = rowData.get("person_id").toString();
//			List<JSONObject> points = personCheckRoutePoints.get(personId);
//			if(null == points){
//				points = new ArrayList<JSONObject>();
//				personCheckRoutePoints.put(personId, points);
//			}
//			JSONObject point = new JSONObject();
////			point.put("id", rowData.get("id"));
//			point.put("routeName", rowData.get("name"));
//			point.put("location_x", rowData.get("location_x"));
//			point.put("location_y", rowData.get("location_y"));
//			point.put("effective_range", rowData.get("effective_range"));
//			point.put("effective_start_time", rowData.get("effective_start_time"));
//			point.put("effective_end_time", rowData.get("effective_end_time"));
//			points.add(point);
//		}
//		return personCheckRoutePoints;
//	}
	/** 获取所有人员对应的区域
	 * 
	 */
	@SuppressWarnings("unchecked")
	private Map<String,Map<String,List<JSONObject>>> getPersonCheckWarnAreas(){//Map<人员ID,Map<区域ID,List<分配的区域时间和类型>>>
		Map<String,Map<String,List<JSONObject>>> personCheckWarnAreas = new HashMap<String,Map<String,List<JSONObject>>>();
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select ta.person_id,t.area_type,DATE_FORMAT(t.start_time,'%H:%i:%s') start_time,DATE_FORMAT(t.end_time,'%H:%i:%s') end_time,t.area_id,a.name ");
		getListSql.append("from bp_fine_area_time_assign_tbl ta join bp_fine_area_time_tbl t on (t.is_used and t.id = ta.area_time_id) ");
		getListSql.append("join bp_fine_area_tbl a on (a.community_id = '"+communityId+"' and a.id = t.area_id) ");
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
//	@SuppressWarnings("unchecked")
//	private Map<String,List<JSONObject>> getPersonCheckWarnAreas(){
//		Map<String,List<JSONObject>> personCheckWarnAreas = new HashMap<String,List<JSONObject>>();
//		StringBuffer getListSql = new StringBuffer();
//		getListSql.append("select pa.person_id,pa.warn_area_id,wa.type ");
//		getListSql.append("from bp_warn_area_person_assign_tbl pa join bp_fine_area_tbl wa on (wa.community_id = '"+communityId+"' and pa.warn_area_id = wa.id) ");
//		getListSql.append("order by pa.person_id ");
//		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(getListSql.toString());
//		Iterator<Map<String,Object>> ite = dataList.iterator();
//		while(ite.hasNext()){
//			Map<String,Object> rowData = ite.next();
//			String personId = rowData.get("person_id").toString();
//			List<JSONObject> areas = personCheckWarnAreas.get(personId);
//			if(null == areas){
//				areas = new ArrayList<JSONObject>();
//				personCheckWarnAreas.put(personId, areas);
//			}
//			JSONObject area = new JSONObject();
//			area.put("id", rowData.get("warn_area_id"));
//			area.put("type", rowData.get("type"));
//			areas.add(area);
//		}
//		return personCheckWarnAreas;
//	}
//	@SuppressWarnings("unchecked")
//	private Map<String,String> routesInCommunity(){
//		Map<String,String> routesInCommunity = new HashMap<String,String>();
//		StringBuffer getListSql = new StringBuffer();
//		getListSql.append("select id,name from bp_fine_route_tbl where community_id = '"+communityId+"'");
//		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(getListSql.toString());
//		Iterator<Map<String,Object>> ite = dataList.iterator();
//		while(ite.hasNext()){
//			Map<String,Object> rowData = ite.next();
//			String routeId = rowData.get("id").toString();
//			String name = rowData.get("name").toString();
//			routesInCommunity.put(routeId, name);
//		}
//		return routesInCommunity;
//	}
//	@SuppressWarnings("unchecked")
//	private Map<String,String> areasInCommunity(){
//		Map<String,String> areasInCommunity = new HashMap<String,String>();
//		StringBuffer getListSql = new StringBuffer();
//		getListSql.append("select id,name from bp_fine_area_tbl where community_id = '"+communityId+"'");
//		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(getListSql.toString());
//		Iterator<Map<String,Object>> ite = dataList.iterator();
//		while(ite.hasNext()){
//			Map<String,Object> rowData = ite.next();
//			String areaId = rowData.get("id").toString();
//			String name = rowData.get("name").toString();
//			areasInCommunity.put(areaId, name);
//		}
//		return areasInCommunity;
//	}
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
	
}
