package com.yf.base.actions.patrolmanage.routeassign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
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
	private String dir = "";
	private String sort = "community_name";
	
	private String id;
	
	static{
		nameToIndex.put("community_name", "c.name");
		nameToIndex.put("route_name", "r.name");
		nameToIndex.put("route_time_name", "rt.name");
		nameToIndex.put("start_time", "rt.start_time");
		nameToIndex.put("end_time", "rt.end_time");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		StringBuffer dataSql = new StringBuffer();
		dataSql.append("select rt.id,rt.name route_time_name,rt.start_time,rt.end_time,rt.is_used,r.id route_id,r.name route_name,c.id community_id,c.name community_name,c.map ");
		StringBuffer commonSql = new StringBuffer();
		commonSql.append("from bp_fine_route_time_tbl rt join bp_fine_route_tbl r on (rt.route_id = r.id) join bp_community_tbl c on (");
		String userId = (String) ActionContext.getContext().getSession().get("loginUserId");
		if(!(Boolean) ActionContext.getContext().getSession().get("isAdmin")){
			commonSql.append("c.user_id = '"+userId+"' and ");
		}
		commonSql.append("c.id = r.community_id) ");
		
		if(!"0".equals(id)){
			commonSql.append("where r.id = '"+id+"' or c.id = '"+id+"' ");
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
