package com.yf.base.actions.mapposition.routemanage.routeassign;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class GetRoutePoints extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String routeTimeId;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("select rp.id,rp.previous_point_id,rp.next_point_id,rp.location_x,rp.location_y,rpt.id as route_point_time_id,");
		sql.append("IFNULL(rpt.start_time,rt.start_time) start_time,IFNULL(rpt.end_time,rt.end_time) end_time ");
		sql.append("from bp_coarse_route_point_tbl rp join bp_coarse_route_time_tbl rt on (rt.id='"+routeTimeId+"' and rt.route_id=rp.route_id) ");
		sql.append("left join bp_coarse_route_point_time_tbl rpt on (rpt.route_time_id=rt.id and rpt.route_point_id=rp.id) ");
		sql.append("order by rp.previous_point_id ");
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql.toString());
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
	
	public String getRouteTimeId() {
		return routeTimeId;
	}
	
	public void setRouteTimeId(String routeTimeId) {
		this.routeTimeId = routeTimeId;
	}
}
