package com.yf.base.actions.datastatistics.patrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class PatrolInfoData extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String personId;
	private String phone;
	private String routeTimeId;
	private String date;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		StringBuffer dataSql = new StringBuffer();
		dataSql.append("select rp.id,rp.previous_point_id,rp.next_point_id,rp.location_x,rp.location_y,");
		dataSql.append("IFNULL(rpt.start_time,rt.start_time) start_time,IFNULL(rpt.end_time,rt.end_time) end_time,");
		dataSql.append("IF(min(sqrt(pow(rp.location_x-bct.locationX,2)+pow(rp.location_y-bct.locationY,2))) < r.effective_range,1,0) is_done ");
		dataSql.append("from bp_fine_route_point_tbl rp ");
		dataSql.append("join bp_fine_route_time_tbl rt on (rt.id = '"+routeTimeId+"' and rt.route_id = rp.route_id) ");
		dataSql.append("join bp_fine_route_tbl r on (r.id = rt.route_id) ");
		dataSql.append("left join bp_fine_route_point_time_tbl rpt on (rpt.route_time_id = rt.id and rpt.route_point_id = rp.id) ");
		dataSql.append("left join bp_phone_rfid_location_tbl prl on (");
		dataSql.append("date(prl.upload_time) = '"+date+"' and prl.phone = '"+phone+"' ");
		dataSql.append("and TIME(prl.upload_time)>IFNULL(rpt.start_time,rt.start_time) and TIME(prl.upload_time)<IFNULL(rpt.end_time,rt.end_time)) ");
		dataSql.append("left join bp_card_tbl bct on (bct.card_mark = prl.card_id) ");
		dataSql.append("group by rp.id ");
		dataSql.append("order by rp.previous_point_id ");
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(dataSql.toString());
		//排序
		List<Map<String,Object>> sortDataList = new ArrayList<Map<String,Object>>();
		if(dataList.size()>0){
			sortDataList.add(dataList.get(0));
			dataList.remove(0);
		}
		while(dataList.size()>0){
			String nextPointId = sortDataList.get(sortDataList.size()-1).get("next_point_id").toString();
			if(StringUtils.isBlank(nextPointId)){
				break;
			}
			Map<String,Object> nextPoint = findPoint(dataList,nextPointId);
			if(null == nextPoint){
				break;
			}
			sortDataList.add(nextPoint);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("list", sortDataList.toArray());
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		return "data";
	}
	
	private Map<String,Object> findPoint(List<Map<String,Object>> dataList,String pointId){
		Map<String,Object> findData = null;
		for(int i=0;i<dataList.size();i++){
			if(dataList.get(i).get("id").equals(pointId)){
				findData = dataList.get(i);
				dataList.remove(i);
				return findData;
			}
		}
		return findData;
	}

	public String getJsonString() {
		return jsonString;
	}
	
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getPersonId() {
		return personId;
	}
	
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRouteTimeId() {
		return routeTimeId;
	}
	
	public void setRouteTimeId(String routeTimeId) {
		this.routeTimeId = routeTimeId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
