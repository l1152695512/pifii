package com.yf.base.actions.datastatistics.historyroute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class GetHistoryRoute extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String communityId;
	private String personId;
	private String startDateTime;
	private String endDateTime;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select c.card_mark,DATE_FORMAT(upload_time,'%Y-%m-%d %H:%i:%s') date,c.locationX,c.locationY ");
		getListSql.append("from bp_person_tbl p join bp_phone_rfid_location_tbl prl ");
		getListSql.append("on (upload_time > ? and upload_time < ? and p.phone = prl.phone) ");
		getListSql.append("join bp_card_tbl c on (c.communityId = ? and c.card_mark = prl.card_id) ");
		getListSql.append("join bp_community_tbl co on (c.communityId=co.id) ");//这个连接主要确保查出的位置点为当前小区的位置点，防止两个区域内都有该人员的位置点，造成轨迹错乱
		getListSql.append("where p.id = ? order by upload_time ");
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(getListSql.toString(),new Object[]{startDateTime,endDateTime,communityId,personId});
		List<Map<String,Object>> newDataList = new ArrayList<Map<String,Object>>();
		if(dataList.size() > 0){
			MinRouteUtils utils = new MinRouteUtils(communityId);
			newDataList.add(dataList.get(0));
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			for(int i=1;i<dataList.size();i++){
				Map<String,Object> previousPoint = dataList.get(i-1);
				Map<String,Object> thisPoint = dataList.get(i);
				String previousPointMark = previousPoint.get("card_mark").toString();
				String thisPointMark = thisPoint.get("card_mark").toString();
				System.err.println("previousPointMark="+previousPointMark+",thisPointMark="+thisPointMark);
				Map<String,JSONObject> adjacentPoints = utils.getAdjacentPoints(previousPointMark,thisPointMark);
				System.err.println(adjacentPoints.keySet().toArray().length);
				Iterator<String> ite = adjacentPoints.keySet().iterator();
//				Date startDate = df.parse(previousPoint.get("date").toString());
//				Date endDate = df.parse(thisPoint.get("date").toString());
//				long averageSecond = (startDate.getTime() - endDate.getTime())/(adjacentPoints.size()+1);
				while(ite.hasNext()){
					Map<String,Object> rowData = new HashMap<String,Object>();
//					Date date = new Date(startDate.getTime() + averageSecond*j);
//					rowData.put("date", df.format(date));
					JSONObject point = adjacentPoints.get(ite.next());
					rowData.put("locationX", point.get("locationX"));
					rowData.put("locationY", point.get("locationY"));
					newDataList.add(rowData);
				}
				newDataList.add(dataList.get(i));
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("list", newDataList.toArray());
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		return "data";
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
	
	public String getPersonId() {
		return personId;
	}
	
	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getStartDateTime() {
		return startDateTime;
	}
	
	public void setStartDateTime(String startDateTime) {
		this.startDateTime = startDateTime;
	}
	
	public String getEndDateTime() {
		return endDateTime;
	}
	
	public void setEndDateTime(String endDateTime) {
		this.endDateTime = endDateTime;
	}
}
