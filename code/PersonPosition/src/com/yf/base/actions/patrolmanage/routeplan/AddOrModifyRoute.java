package com.yf.base.actions.patrolmanage.routeplan;

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
	private String effectiveRange = "10";
	private String description;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		if(StringUtils.isNotBlank(id)){
			String sql = "select name,effective_range,description from bp_fine_route_tbl where id='"+id+"' ";
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
			if(dataList.size()>0){
				Map<String,Object> data = dataList.get(0);
				name = null==data.get("name")?"":data.get("name").toString();
				effectiveRange = null==data.get("effective_range")?"10":data.get("effective_range").toString();
				description = null==data.get("description")?"":data.get("description").toString();
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
	
	public String getEffectiveRange() {
		return effectiveRange;
	}
	
	public void setEffectiveRange(String effectiveRange) {
		this.effectiveRange = effectiveRange;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
}
