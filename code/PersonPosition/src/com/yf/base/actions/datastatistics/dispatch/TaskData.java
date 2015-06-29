package com.yf.base.actions.datastatistics.dispatch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class TaskData extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static Map<String,String> nameToIndex = new HashMap<String,String>();
	private String jsonString;
	private int start;
	private int limit;
	private String dir;
	private String sort;
	
	private String name;
	private String helpPersonName;
	private String type;
//	private String description;
	private String startDate;
	private String endDate;
	
	static{
		nameToIndex.put("type", "d.key_value");
		nameToIndex.put("name", "p.name");
		nameToIndex.put("help_name", "p1.name");
		nameToIndex.put("task_date", "dt.task_date");
	}
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		StringBuffer dataSql = new StringBuffer();
		dataSql.append("select d.key_value type,p.name,p1.name help_name,dt.description,DATE_FORMAT(dt.task_date,'%Y-%m-%d %H:%i:%s') task_date ");
		StringBuffer commonSql = new StringBuffer();
		commonSql.append("from bp_dispatch_task_tbl dt ");
		commonSql.append("join bp_person_tbl p on (p.id = dt.person_id) ");
		commonSql.append("join sys_dictionary_tbl d on (d.DIC_ID = dt.dispatch_type_id) ");
		commonSql.append("left join bp_person_tbl p1 on (p1.id = dt.help_person_id) ");
		commonSql.append("where 1=1 ");
		if(!StringUtils.isBlank(name)){
			commonSql.append(" and p.name LIKE '"+DBHelper.wrapFuzzyQuery(name)+"' ");
		}
		if(!StringUtils.isBlank(helpPersonName)){
			commonSql.append(" and p1.name LIKE '"+DBHelper.wrapFuzzyQuery(helpPersonName)+"' ");
		}
		if(!StringUtils.isBlank(type)){
			commonSql.append(" and dt.dispatch_type_id = '"+type+"' ");
		}
//		if(!StringUtils.isBlank(description)){
//			commonSql.append(" and dt.description LIKE '"+DBHelper.wrapFuzzyQuery(description)+"' ");
//		}
		if(!StringUtils.isBlank(startDate)){
			commonSql.append(" and dt.task_date > '"+startDate+"' ");
		}
		if(!StringUtils.isBlank(endDate)){
			commonSql.append(" and dt.task_date < '"+endDate+"' ");
		}
		dataSql.append(commonSql.toString());
		dataSql.append(" ORDER BY "+nameToIndex.get(sort)+" " +dir+" LIMIT "+start+","+limit);
		System.err.println(dataSql.toString());
		List<?> dataList = dbhelper.getMapListBySql(dataSql.toString());
		
		StringBuffer countSql = new StringBuffer();
		countSql.append("select count(*) count ");
		countSql.append(commonSql.toString());
		List<Map<String,Object>> countList = (List<Map<String, Object>>) dbhelper.getMapListBySql(countSql.toString());
		long rowCount = (Long)countList.get(0).get("count");
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("totalRecord", rowCount);
		resultMap.put("list", dataList.toArray());
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHelpPersonName() {
		return helpPersonName;
	}

	public void setHelpPersonName(String helpPersonName) {
		this.helpPersonName = helpPersonName;
	}

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

//	public String getDescription() {
//		return description;
//	}
//
//	public void setDescription(String description) {
//		this.description = description;
//	}
	
}
