package com.yf.base.actions.datastatistics.historyroute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.actions.commons.CommunityUtils;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class ComboPerson extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String communityId;
	
	@Override
	public String execute() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("select id,name from bp_person_tbl ");
//		if(StringUtils.isNotBlank(communityId)){
			sql.append("where communityId='"+CommunityUtils.getParentCommunity(communityId)+"' ");
//		}
		List<?> dataList = dbhelper.getMapListBySql(sql.toString());
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("values", dataList.toArray());
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
