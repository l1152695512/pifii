package com.yf.base.actions.datastatistics.patrol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.actions.datastatistics.CommonSearch;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class PatrolData extends ActionSupport {
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
		long rowCount = 0;
		List<?> dataList = new ArrayList<Object>();
		if(CommonSearch.initSearchTable(dbhelper, startDate, endDate)){
			StringBuffer dataSql = new StringBuffer();
			dataSql.append("select date,person_name,community_name,map,route_name,max(position_date) position_date,");
			dataSql.append("person_id,phone,route_time_id,min(start_time) as start_time,max(end_time) as end_time,");
			dataSql.append("if(min(is_done) = 1,1,0) is_done ");
			StringBuffer commonSql = new StringBuffer();
			commonSql.append("from (select sd.date,p.name as person_name,c.name as community_name,c.map,r.name as route_name,");
			commonSql.append("prl.upload_time as position_date,p.id as person_id,p.phone,r.id as route_id,rt.id as route_time_id,");
			commonSql.append("IFNULL(rpt.start_time,rt.start_time) as start_time,IFNULL(rpt.end_time,rt.end_time) as end_time,");
			commonSql.append("IF(min(sqrt(pow(rp.location_x-bct.locationX,2)+pow(rp.location_y-bct.locationY,2))) < r.effective_range,1,0) is_done ");
			commonSql.append("from bp_fine_route_time_assign_tbl rta ");
			commonSql.append("join bp_fine_route_time_tbl rt on (rt.id=rta.route_time_id) ");
			commonSql.append("join bp_fine_route_tbl r on (");
			if(!StringUtils.isBlank(routeId)){
				commonSql.append("r.id = '"+routeId+"' and ");
			}
			commonSql.append("r.id = rt.route_id) ");
			commonSql.append("join bp_community_tbl c on (");
			if(!StringUtils.isBlank(communityId)){
				commonSql.append("c.id = '"+communityId+"' and ");
			}
			String userId = (String) ActionContext.getContext().getSession().get("loginUserId");
			if(!(Boolean) ActionContext.getContext().getSession().get("isAdmin")){
				commonSql.append("c.user_id = '"+userId+"' and ");
			}
			commonSql.append("c.id = r.community_id) ");
			commonSql.append("join bp_fine_route_point_tbl rp on (rp.route_id = r.id) ");
			commonSql.append("left join bp_fine_route_point_time_tbl rpt on (rpt.route_time_id = rt.id and rpt.route_point_id = rp.id) ");
			commonSql.append("join bp_person_tbl p on (");
			if(!StringUtils.isBlank(name)){
				commonSql.append("p.name LIKE '"+DBHelper.wrapFuzzyQuery(name)+"' and ");
			}
			commonSql.append("p.id = rta.person_id) ");
			commonSql.append("join bp_search_date_tbl sd ");
			
			commonSql.append("left join bp_phone_rfid_location_tbl prl on (");
			if(StringUtils.isNotBlank(startDate)){
				commonSql.append("date(prl.upload_time) >= '"+startDate+"' and ");
			}
			if(StringUtils.isNotBlank(endDate)){
				commonSql.append("date(prl.upload_time) < '"+endDate+"' and ");
			}
			commonSql.append("date(prl.upload_time) = sd.date and p.phone = prl.phone and TIME(prl.upload_time)>IFNULL(rpt.start_time,rt.start_time) and TIME(prl.upload_time)<IFNULL(rpt.end_time,rt.end_time)) ");
			commonSql.append("left join bp_card_tbl bct on (bct.card_mark = prl.card_id) ");
			commonSql.append("group by sd.date,r.id,p.id,rt.id,rp.id ) a ");
			commonSql.append("group by date,route_id,person_id,route_time_id ");
			if(!StringUtils.isBlank(isDone)){
				commonSql.append("having (min(is_done) "+("1".equals(isDone)?"=":"<>")+" 1) ");
			}
			dataSql.append(commonSql.toString());
			if(null == sort || "".equals(sort) || null == dir || "".equals(dir)){
				dataSql.append("ORDER BY date desc,community_name,route_id,person_id" +" LIMIT "+start+","+limit);
			}else{
				dataSql.append("ORDER BY "+sort+" " +dir+" LIMIT "+start+","+limit);
			}
			dataList = dbhelper.getMapListBySql(dataSql.toString());
			
			StringBuffer countSql = new StringBuffer();
			countSql.append("select count(*) count from (select * ");
			countSql.append(commonSql.toString());
			countSql.append(") b");
			List<Map<String,Object>> countList = (List<Map<String, Object>>) dbhelper.getMapListBySql(countSql.toString());
			rowCount = (Long)countList.get(0).get("count");
		}
		
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
