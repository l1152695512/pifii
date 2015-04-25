package com.yf.base.actions.location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class GetForHelpPerson extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String communityId;
	
	@Override
	public String execute() throws Exception {
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select p.id,p.name,p.age,p.phone,p.photo,p.description,p.dictionaryId,d.KEY_VALUE,DATE_FORMAT(l.date,'%Y-%m-%d %H:%i:%s') date,l.locationX,l.locationY ");
		getListSql.append("from bp_location_current_tbl l join bp_person_tbl p on (p.communityId='"+communityId+"' and l.personId = p.id) ");
		getListSql.append("join sys_dictionary_tbl d on (d.DIC_ID=p.dictionaryId) join test_help_event h on (p.id = h.person_id) ");
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
