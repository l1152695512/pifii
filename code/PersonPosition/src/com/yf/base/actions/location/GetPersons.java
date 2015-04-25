package com.yf.base.actions.location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.actions.commons.CommunityUtils;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class GetPersons extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String communityId;
	
	@Override
	public String execute() throws Exception {
		communityId = CommunityUtils.getParentCommunity(communityId);
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select p.id,p.name,p.age,p.phone,p.photo,p.description,p.dictionaryId,d.KEY_VALUE ");
		getListSql.append("from bp_person_tbl p join sys_dictionary_tbl d on (p.dictionaryId = d.DIC_ID) ");
		getListSql.append("where p.communityId='"+communityId+"' ");
		List<?> dataList = dbhelper.getMapListBySql(getListSql.toString());
		Map<String,Object> resultMap = new HashMap<String,Object>();
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
	
	public String getCommunityId() {
		return communityId;
	}
	
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
}
