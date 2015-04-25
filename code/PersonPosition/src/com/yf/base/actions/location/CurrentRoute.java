package com.yf.base.actions.location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class CurrentRoute extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String communityId;
	private String name;
	
	@Override
	public String execute() throws Exception {
//		String groupId = ActionContext.getContext().getSession().get("userGroupId").toString();
		StringBuffer getListSql = new StringBuffer();
		getListSql.append("select p.name,p.age,p.phone,d.KEY_VALUE,DATE_FORMAT(l.date,'%Y-%m-%d %H:%i:%s') date,l.locationX,l.locationY ");
		getListSql.append("from bp_location_current_tbl l ");
		getListSql.append("join bp_person_tbl p on (p.communityId='"+communityId+"' and l.personId = p.id) ");
		getListSql.append("join sys_dictionary_tbl d on (d.DIC_ID=p.dictionaryId) ");
		getListSql.append("where 1=1 ");
		if(StringUtils.isNotBlank(name)){
			getListSql.append("and p.name like '");
			getListSql.append(DBHelper.wrapFuzzyQuery(name));
			getListSql.append("' ");
		}
		getListSql.append("order by l.date desc limit 1 ");
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
