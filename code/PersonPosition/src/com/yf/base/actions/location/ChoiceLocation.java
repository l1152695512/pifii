package com.yf.base.actions.location;

import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class ChoiceLocation extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String mapPath;
	private String type;
	private String personId;
	private String communityId;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		String sql = "select communityId from bp_person_tbl where id='"+personId+"' ";
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
		if(dataList.size()>0){
			Map<String,Object> data = dataList.get(0);
			communityId = null==data.get("communityId")?"":data.get("communityId").toString();
			return super.execute();
		}else{
			return "failure";
		}
	}
	
	public String getMapPath() {
		return mapPath;
	}
	
	public void setMapPath(String mapPath) {
		this.mapPath = mapPath;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getPersonId() {
		return personId;
	}
	
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
	public String getCommunityId() {
		return communityId;
	}
	
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	
}
