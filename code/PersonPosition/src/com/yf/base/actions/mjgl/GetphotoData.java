package com.yf.base.actions.mjgl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.xwork.StringUtils;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class GetphotoData extends ActionSupport{
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static Map<String,String> nameToIndex = new HashMap<String,String>();
	private String jsonString;
	private int limit;
	private int start;	
	private String dir;
	private String sort;
	private String name;
	private String id;
	private String communityId;

	static{
		nameToIndex.put("communityName", "c.name");
		nameToIndex.put("name", "per.name");
		nameToIndex.put("phone", "per.phone");
		nameToIndex.put("type", "dic.KEY_VALUE");
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		StringBuffer dataSql = new StringBuffer();
		dataSql.append ("select c.name communityName,per.id,per.name,per.phone,dic.KEY_VALUE type ");
		StringBuffer commonSql = new StringBuffer();
		commonSql.append ("from bp_community_tbl c join bp_person_tbl per on (per.communityId = c.id) ");
		commonSql.append ("join sys_dictionary_tbl dic on (per.dictionaryId=dic.DIC_ID) ");
		commonSql.append ("where 1=1 ");
		String userId = (String) ActionContext.getContext().getSession().get("loginUserId");
		if(!(Boolean) ActionContext.getContext().getSession().get("isAdmin")){
			commonSql.append(" and c.user_id = '"+userId+"' ");
		}
		if(!StringUtils.isBlank(communityId)){
			commonSql.append(" and per.communityId = '"+communityId+"' ");
		}
		if(!StringUtils.isBlank(name)){
			commonSql.append(" and per.name LIKE '"+DBHelper.wrapFuzzyQuery(name)+"' ");
		}
		if(!StringUtils.isBlank(id)){
			commonSql.append(" and dic.DIC_ID = '"+id+"' ");
		}
		dataSql.append(commonSql.toString());
		dataSql.append(" ORDER BY "+nameToIndex.get(sort)+" " +dir+" LIMIT "+start+","+limit);
		
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
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
	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
	public String getJsonString() {
		return jsonString;
	}
	
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
}

