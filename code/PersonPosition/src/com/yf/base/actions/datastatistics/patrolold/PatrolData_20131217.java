package com.yf.base.actions.datastatistics.patrolold;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class PatrolData_20131217 extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private int start;
	private int limit;
	private String dir;
	private String sort;
	
	//显示所有的记录，包含字段，姓名、小区、路线、日期、完成度、详情（可查看完成的线路点）
	private String name;
	private String communityId;
	private String routeId;
	private String startDate;
	private String endDate;
	private String isDone;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		StringBuffer dataSql = new StringBuffer();
		dataSql.append("select community_name,map,route_name,person_name,date,person_id,route_id,if(min(is_done) = 1,1,0) is_done ");
		StringBuffer commonSql = new StringBuffer();
		commonSql.append("from (select pe.name person_name,c.name community_name,c.map,r.name route_name,po.name point_name,DATE(lh.date) date,rp.person_id,rp.route_id ");
		commonSql.append(",if(TIME(MIN(lh.date)) > po.effective_start_time and TIME(MAX(lh.date)) < po.effective_end_time and min(sqrt(pow(po.location_x-lh.locationX,2)+pow(po.location_y-lh.locationY,2)))<=po.effective_range,1,0) is_done ");
		commonSql.append("from bp_route_person_assign_tbl rp ");
		commonSql.append("join bp_fine_route_tbl r on (");
		if(!StringUtils.isBlank(routeId)){
			commonSql.append("r.id = '"+routeId+"' and ");
		}
		commonSql.append("r.is_used and r.id = rp.route_id) ");
		commonSql.append("join bp_community_tbl c on (");
		if(!StringUtils.isBlank(communityId)){
			commonSql.append("c.id = '"+communityId+"' and ");
		}
		commonSql.append("c.id = r.community_id) ");
		commonSql.append("join bp_fine_route_point_tbl po on (po.route_id = rp.route_id) ");
		commonSql.append("join bp_person_tbl pe on (");
		if(!StringUtils.isBlank(name)){
			commonSql.append("pe.name LIKE '"+DBHelper.wrapFuzzyQuery(name)+"' and ");
		}
		commonSql.append("pe.id = rp.person_id) ");
		commonSql.append("left join bp_location_history_tbl lh on (");
		if(StringUtils.isNotBlank(startDate)){
			commonSql.append("date(lh.date) >= '"+startDate+"' and ");
		}
		if(StringUtils.isNotBlank(endDate)){
			commonSql.append("date(lh.date) <= '"+endDate+"' and ");
		}
		commonSql.append("rp.person_id = lh.personId) ");
		
		commonSql.append("group by rp.person_id,po.id,DATE(lh.date) ");
		commonSql.append(") a ");
		commonSql.append("group by person_id,route_id,date ");
		if(!StringUtils.isBlank(isDone)){
			commonSql.append("having (min(is_done) "+("1".equals(isDone)?"=":"<>")+" 1) ");
		}
		dataSql.append(commonSql.toString());
		if(null == sort || "".equals(sort) || null == dir || "".equals(dir)){
			dataSql.append("ORDER BY date desc,community_name,route_id,person_id" +" LIMIT "+start+","+limit);
		}else{
			dataSql.append("ORDER BY "+sort+" " +dir+" LIMIT "+start+","+limit);
		}
		System.err.println(dataSql.toString());
		List<?> dataList = dbhelper.getMapListBySql(dataSql.toString());
		
		StringBuffer countSql = new StringBuffer();
		countSql.append("select count(*) count from (select * ");
		countSql.append(commonSql.toString());
		countSql.append(") b");
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

	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getEndDate() {
		return endDate;
	}
	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	public String getStartDate() {
		return startDate;
	}
	
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getIsDone() {
		return isDone;
	}

	public void setIsDone(String isDone) {
		this.isDone = isDone;
	}

}
