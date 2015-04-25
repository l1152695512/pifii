package com.yf.base.actions.location;

import org.springframework.security.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.auth.SystemUser;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class DealWarnEvent extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String communityId;
	private String id;
	private String isDeal;
	private String description;

	@Override
	public String execute() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("update bp_warn_event_tbl set deal_user_id=?,is_deal=?,description=? where id=? ");
		SystemUser sysuser = (SystemUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Object[] params = new Object[4];
		params[0] = sysuser.getUserId();
		params[1] = isDeal;
		params[2] = description.replaceAll("\n", "<br>");
		params[3] = id;
		if(dbhelper.update(sql.toString(),params)){
			return super.execute();
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

	public String getIsDeal() {
		return isDeal;
	}

	public void setIsDeal(String isDeal) {
		this.isDeal = isDeal;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
