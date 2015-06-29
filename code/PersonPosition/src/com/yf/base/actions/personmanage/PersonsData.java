package com.yf.base.actions.personmanage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class PersonsData extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static Map<String,String> nameToIndex = new HashMap<String,String>();
	private String jsonString;
	private int start;
	private int limit;
	private String dir;
	private String sort;
	
	private String name;
	private String communityId;
	
	static{
		nameToIndex.put("communityName", "c.name");
		nameToIndex.put("name", "p.name");
		nameToIndex.put("age", "p.age");
		nameToIndex.put("add_date", "p.add_date");
	}
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		StringBuffer dataSql = new StringBuffer();
		dataSql.append("select c.name communityName,p.id,p.name,p.age,p.phone,d.KEY_VALUE type,p.description,date_format(p.add_date,'%Y-%m-%d %H:%i:%s') add_date ");
		StringBuffer commonSql = new StringBuffer();
		commonSql.append("from bp_community_tbl c ");
		commonSql.append("join bp_person_tbl p on (p.communityId=c.id) ");
		commonSql.append("left join sys_dictionary_tbl d on (d.DIC_ID=p.dictionaryId) ");
		commonSql.append("where 1=1 ");
		String userId = (String) ActionContext.getContext().getSession().get("loginUserId");
		if(!(Boolean) ActionContext.getContext().getSession().get("isAdmin")){
			commonSql.append(" and c.user_id = '"+userId+"' ");
		}
		if(!StringUtils.isBlank(name)){
			commonSql.append(" and p.name LIKE '"+DBHelper.wrapFuzzyQuery(name)+"' ");
		}
		if(!StringUtils.isBlank(communityId)){
			commonSql.append(" and p.communityId = '"+communityId+"' ");
		}
		dataSql.append(commonSql.toString());
		dataSql.append(" ORDER BY "+nameToIndex.get(sort)+" " +dir+" LIMIT "+start+","+limit);
		System.err.println(dataSql.toString());
		List<?> dataList = dbhelper.getMapListBySql(dataSql.toString());
		
		StringBuffer countSql = new StringBuffer();
		countSql.append("select count(*) count ");
		countSql.append(commonSql.toString());
		System.out.println(countSql);
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
	
}
