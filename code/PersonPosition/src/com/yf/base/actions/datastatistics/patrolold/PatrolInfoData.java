package com.yf.base.actions.datastatistics.patrolold;

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
	private String routeId;
	private String date;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		StringBuffer dataSql = new StringBuffer();
		dataSql.append("select r.name route_name,DATE(lh.date) date,po.id,po.previous_point_id,po.next_point_id,po.name point_name,po.location_x,po.location_y,po.effective_range,po.effective_start_time,po.effective_end_time");
		dataSql.append(",if(TIME(MIN(lh.date)) > po.effective_start_time and TIME(MAX(lh.date)) < po.effective_end_time and min(sqrt(pow(po.location_x-lh.locationX,2)+pow(po.location_y-lh.locationY,2)))<=po.effective_range,1,0) is_done ");
		dataSql.append("from bp_route_person_assign_tbl rp ");
		dataSql.append("join bp_fine_route_tbl r on (r.is_used and r.id = rp.route_id) ");
		dataSql.append("join bp_fine_route_point_tbl po on (po.route_id = rp.route_id) ");
		dataSql.append("left join bp_location_history_tbl lh on (date(lh.date) = '"+date+"' and rp.person_id = lh.personId) ");
		dataSql.append("where rp.person_id = '"+personId+"' and rp.route_id = '"+routeId+"' ");
		dataSql.append("group by rp.person_id,po.id,DATE(lh.date) ");
		dataSql.append("order by po.previous_point_id ");
		System.err.println(dataSql.toString());
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

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
