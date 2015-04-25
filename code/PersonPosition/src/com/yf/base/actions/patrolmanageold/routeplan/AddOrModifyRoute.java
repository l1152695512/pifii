package com.yf.base.actions.patrolmanageold.routeplan;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class AddOrModifyRoute extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String communityId;
	private String id;
	private String name;
	private String isUsed;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		if(StringUtils.isNotBlank(id)){
			String sql = "select name,is_used from bp_fine_route_tbl where id='"+id+"' ";
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
			if(dataList.size()>0){
				Map<String,Object> data = dataList.get(0);
				name = null==data.get("name")?"":data.get("name").toString();
				isUsed = null==data.get("is_used")?"":data.get("is_used").toString();
			}else{
				return "failure";
			}
		}
		return super.execute();
	}
	
	public String getCommunityId() {
		return communityId;
	}
	
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
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
	
	public String getIsUsed() {
		return isUsed;
	}
	
	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}
}
