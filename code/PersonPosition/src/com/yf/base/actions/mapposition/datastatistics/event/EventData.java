package com.yf.base.actions.mapposition.datastatistics.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class EventData extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static Map<String,String> routeNameToIndex = new HashMap<String,String>();
	private static Map<String,String> areaNameToIndex = new HashMap<String,String>();
	private String jsonString;
	private int start;
	private int limit;
	private String dir;
	private String sort;
	
	private String personName;
	private String type;
//	private String eventType;
	private String startDate;
	private String endDate;
	
	static{
		routeNameToIndex.put("person_name", "p.name");
		routeNameToIndex.put("name", "r.name");
		routeNameToIndex.put("time_name", "rt.name");
		routeNameToIndex.put("add_date", "ce.add_date");
		routeNameToIndex.put("event_type", "ce.type");
		
		areaNameToIndex.put("person_name", "p.name");
		areaNameToIndex.put("name", "ca.name");
		areaNameToIndex.put("time_name", "cat.name");
		areaNameToIndex.put("add_date", "ce.add_date");
		areaNameToIndex.put("event_type", "ce.type");
	}
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		String[] sqls;
		if(type.equals("1")){//route事件
			sqls = getRouteSqls();
		}else{
			sqls = getAreaSqls();
		}
		List<?> dataList = dbhelper.getMapListBySql(sqls[0]);
		List<Map<String,Object>> countList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sqls[1]);
		long rowCount = (Long)countList.get(0).get("count");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("totalRecord", rowCount);
		resultMap.put("list", dataList.toArray());
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		return "data";
	}

	private String[] getRouteSqls(){
		String[] sqls = new String[2];
		StringBuffer dataSql = new StringBuffer();
		dataSql.append("select ce.type event_type,p.name person_name,r.name name,rt.name time_name,DATE_FORMAT(ce.add_date,'%Y-%m-%d %H:%i:%s') add_date ");
		StringBuffer commonSql = new StringBuffer();
		commonSql.append("from bp_coarse_event_tbl ce ");
		commonSql.append("join bp_person_tbl p on (");
		if(StringUtils.isNotBlank(personName)){
			commonSql.append("p.name LIKE '"+DBHelper.wrapFuzzyQuery(personName)+"' and ");
		}
		commonSql.append("ce.person_id = p.id) ");
		commonSql.append("join bp_coarse_route_time_tbl rt on (ce.time_id = rt.id) ");
		commonSql.append("join bp_coarse_route_tbl r on (rt.route_id = r.id) ");
		commonSql.append("where 1=1 ");
//		if(StringUtils.isNotBlank(eventType)){
//			commonSql.append(" and ce.type = '"+eventType+"' ");
//		}
		if(StringUtils.isNotBlank(startDate)){
			commonSql.append(" and ce.add_date >= '"+startDate+"' ");
		}
		if(StringUtils.isNotBlank(endDate)){
			commonSql.append(" and ce.add_date <= '"+endDate+"' ");
		}
		dataSql.append(commonSql.toString());
		dataSql.append(" ORDER BY "+routeNameToIndex.get(sort)+" " +dir+" LIMIT "+start+","+limit);
		sqls[0] = dataSql.toString();
		StringBuffer countSql = new StringBuffer();
		countSql.append("select count(*) count ");
		countSql.append(commonSql.toString());
		sqls[1] = countSql.toString();
		return sqls;
	}
	
	private String[] getAreaSqls(){
		String[] sqls = new String[2];
		StringBuffer dataSql = new StringBuffer();
		dataSql.append("select ce.type event_type,p.name person_name,ca.name name,cat.name time_name,DATE_FORMAT(ce.add_date,'%Y-%m-%d %H:%i:%s') add_date ");
		StringBuffer commonSql = new StringBuffer();
		commonSql.append("from bp_coarse_event_tbl ce ");
		commonSql.append("join bp_person_tbl p on (");
		if(StringUtils.isNotBlank(personName)){
			commonSql.append("p.name LIKE '"+DBHelper.wrapFuzzyQuery(personName)+"' and ");
		}
		commonSql.append("ce.person_id = p.id) ");
		commonSql.append("join bp_coarse_area_time_tbl cat on (ce.time_id = cat.id) ");
		commonSql.append("join bp_coarse_area_tbl ca on (cat.area_id = ca.id) ");
		commonSql.append("where 1=1 ");
//		if(StringUtils.isNotBlank(eventType)){
//			commonSql.append(" and ce.type = '"+eventType+"' ");
//		}
		if(StringUtils.isNotBlank(startDate)){
			commonSql.append(" and ce.add_date >= '"+startDate+"' ");
		}
		if(StringUtils.isNotBlank(endDate)){
			commonSql.append(" and ce.add_date <= '"+endDate+"' ");
		}
		dataSql.append(commonSql.toString());
		dataSql.append(" ORDER BY "+areaNameToIndex.get(sort)+" " +dir+" LIMIT "+start+","+limit);
		sqls[0] = dataSql.toString();
		StringBuffer countSql = new StringBuffer();
		countSql.append("select count(*) count ");
		countSql.append(commonSql.toString());
		sqls[1] = countSql.toString();
		return sqls;
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

	public String getSort() {
		return sort;
	}
	
	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public String getDir() {
		return dir;
	}
	
	public void setDir(String dir) {
		this.dir = dir;
	}
	
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

//	public String getEventType() {
//		return eventType;
//	}
//
//	public void setEventType(String eventType) {
//		this.eventType = eventType;
//	}

	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}
