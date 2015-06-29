package com.yf.base.actions.datastatistics.event;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class EventData extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static Map<String,String> nameToIndex = new HashMap<String,String>();
	private String jsonString;
	private int start;
	private int limit;
	private String dir;
	private String sort;
	
	private String personName;
	private String eventType;
	private String eventResult;
	private String startDate;
	private String endDate;
	
	static{
		nameToIndex.put("personName", "p.name");
		nameToIndex.put("key_value", "d.key_value");
		nameToIndex.put("userName", "u.user_name");
		nameToIndex.put("event_time", "we.event_time");
	}
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		StringBuffer dataSql = new StringBuffer();
		dataSql.append("select c.name community_name,p.name person_name,u.user_name,d.key_value,DATE_FORMAT(we.event_time,'%Y-%m-%d %H:%i:%s') event_time,we.is_deal,we.description ");
		StringBuffer commonSql = new StringBuffer();
		commonSql.append("from bp_warn_event_tbl we ");
		commonSql.append("join bp_person_tbl p on (");
		if(StringUtils.isNotBlank(personName)){
			commonSql.append("p.name LIKE '"+DBHelper.wrapFuzzyQuery(personName)+"' and ");
		}
		commonSql.append("p.id = we.warn_person_id) ");
		commonSql.append("join bp_community_tbl c on (");
		String userId = (String) ActionContext.getContext().getSession().get("loginUserId");
		if(!(Boolean) ActionContext.getContext().getSession().get("isAdmin")){
			commonSql.append("c.user_id = '"+userId+"' and ");
		}
		commonSql.append("we.community_id=c.id) ");
		
		commonSql.append("left join sys_user_tbl u on (u.user_id = we.deal_user_id) ");
		commonSql.append("join sys_dictionary_tbl d on (");
		if(StringUtils.isNotBlank(eventType)){
			commonSql.append("d.dic_id = '"+eventType+"' and ");
		}
		commonSql.append("d.DIC_ID = we.event_type) ");
		commonSql.append("where 1=1 ");
		if(StringUtils.isNotBlank(eventResult)){
			if("1".equals(eventResult)){
				commonSql.append(" and !we.is_deal ");
			}else if("2".equals(eventResult)){
				commonSql.append(" and we.is_deal ");
			}else{
				commonSql.append(" and we.is_deal is null ");
			}
		}
		if(StringUtils.isNotBlank(startDate)){
			commonSql.append(" and we.event_time >= '"+startDate+"' ");
		}
		if(StringUtils.isNotBlank(endDate)){
			commonSql.append(" and we.event_time <= '"+endDate+"' ");
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
	
	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventResult() {
		return eventResult;
	}

	public void setEventResult(String eventResult) {
		this.eventResult = eventResult;
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
