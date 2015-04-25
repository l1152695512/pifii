package com.yf.base.actions.warnmanage.warnareaassign;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class AreaTimes extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static Map<String,String> nameToIndex = new HashMap<String,String>();
	private String jsonString;
	private int start;
	private int limit;
	private String dir = "desc";
	private String sort = "add_date";
	
	private String id;
	private String isArea;
	
	static{
		nameToIndex.put("community_name", "c.name");
		nameToIndex.put("area_name", "a.name");
		nameToIndex.put("start_time", "t.start_time");
		nameToIndex.put("end_time", "t.end_time");
		nameToIndex.put("area_type", "t.area_type");
		nameToIndex.put("add_date", "t.add_date");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		StringBuffer dataSql = new StringBuffer();
		dataSql.append("select c.name as community_name,a.name as area_name,a.community_id,t.id,t.name,t.area_type,t.start_time,t.end_time,t.is_used ");
		StringBuffer commonSql = new StringBuffer();
		commonSql.append("from bp_fine_area_time_tbl t join bp_fine_area_tbl a on (t.area_id = a.id) ");
		commonSql.append("join bp_community_tbl c on (");
		String userId = (String) ActionContext.getContext().getSession().get("loginUserId");
		if(!(Boolean) ActionContext.getContext().getSession().get("isAdmin")){
			commonSql.append("c.user_id = '"+userId+"' and ");
		}
		commonSql.append("c.id = a.community_id) ");
		
		if(!"0".equals(id)){
			if("1".equals(isArea)){
				commonSql.append("where t.area_id = '"+id+"' ");
			}else{
				commonSql.append("where a.community_id = '"+id+"' ");
			}
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

	public String getIsArea() {
		return isArea;
	}

	public void setIsArea(String isArea) {
		this.isArea = isArea;
	}

}
