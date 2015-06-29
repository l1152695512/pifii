package com.yf.base.actions.patrolmanageold.routeplan;

import java.util.UUID;



import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SaveRoute extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String communityId;
	private String id;
	private String name;
	private String isUsed;

	@Override
	public String execute() throws Exception {
		StringBuffer sql = new StringBuffer();
		if(StringUtils.isNotBlank(id)){
			sql.append("update bp_fine_route_tbl set name=?,is_used=? where id=? ");
			Object[] params = new Object[3];
			params[0] = name;
			params[1] = "1".equals(isUsed);
			params[2] = id;
			if(dbhelper.update(sql.toString(),params)){
				return super.execute();
			}
		}else{
			sql.append("insert into bp_fine_route_tbl(id,community_id,name,is_used,add_date) ");
			sql.append("VALUES(?,?,?,?,now()) ");
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			Object[] params = new Object[4];
			params[0] = id;
			params[1] = communityId;
			params[2] = name;
			params[3] = "1".equals(isUsed);
			if(dbhelper.insert(sql.toString(),params)){
				return super.execute();
			}
		}
		return "failure";
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
