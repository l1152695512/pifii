package com.yf.base.actions.mapposition.routemanage.routeassign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class RouteTimes extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static Map<String,String> nameToIndex = new HashMap<String,String>();
	private String jsonString;
	private int start;
	private int limit;
	private String dir;
	private String sort;
	
	private String routeId;
	
	static{
		nameToIndex.put("route_name", "r.name");
		nameToIndex.put("start_time", "t.start_time");
		nameToIndex.put("end_time", "t.end_time");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		StringBuffer dataSql = new StringBuffer();
		dataSql.append("select r.name as route_name,t.route_id,t.id,t.name,t.start_time,t.end_time,t.is_used ");
		StringBuffer commonSql = new StringBuffer();
		commonSql.append("from bp_coarse_route_time_tbl t join bp_coarse_route_tbl r on (t.route_id = r.id) ");
		if(!"0".equals(routeId)){
			commonSql.append("where t.route_id = '");
			commonSql.append(routeId);
			commonSql.append("' ");
		}
		dataSql.append(commonSql.toString());
		dataSql.append("ORDER BY "+nameToIndex.get(sort)+" " +dir+" LIMIT "+start+","+limit);
		List<?> dataList = dbhelper.getMapListBySql(dataSql.toString());
		
		StringBuffer countSql = new StringBuffer();
		countSql.append("select count(*) count ");
		countSql.append(commonSql.toString());
		List<Map<String,Object>> countList = (List<Map<String, Object>>) dbhelper.getMapListBySql(countSql.toString());
		long rowCount = (Long)countList.get(0).get("count");
		
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("list", dataList.toArray());
		resultMap.put("totalRecord", rowCount);
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

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	
}
