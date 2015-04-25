package com.yf.base.actions.warnmanageold.warnareaassign;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class GetCommunityPerson extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String communityId;
	
	@Override
	public String execute() throws Exception {
		if("0".equals(communityId)){
			jsonString = "[]";
		}else{
			JSONArray array = new JSONArray();
			StringBuffer strB = new StringBuffer();
			strB.append("select id,name,description from bp_person_tbl where communityId = '"+communityId+"' ");
			List<?> dataList = dbhelper.getMapListBySql(strB.toString());
			processTreeNode(array,dataList);
			jsonString = array.toString();
		}
		return "data";
	}
	
	@SuppressWarnings("unchecked")
	private void processTreeNode(JSONArray array , List<?> depts){
		for(int i=0;i<depts.size();i++){
			Map<String,Object> map = (Map<String,Object>)depts.get(i);
			JSONObject json = new JSONObject();
			json.put("id", map.get("id"));
			json.put("text", map.get("name"));
			json.put("qtip", null==map.get("description")?"":map.get("description"));
			json.put("checked", false);
			json.put("icon", "hr/img/person_photo.png");
			json.put("leaf", true);
			array.add(json);
		}
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
