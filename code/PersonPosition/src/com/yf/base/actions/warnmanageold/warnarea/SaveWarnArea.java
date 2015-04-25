package com.yf.base.actions.warnmanageold.warnarea;

import java.util.UUID;

import org.apache.commons.lang.xwork.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SaveWarnArea extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String communityId;
	private String id;
	private String name;
//	private String type;
//	private String effectiveStartTime;
//	private String effectiveEndTime;
//	private String isUsed;

	@Override
	public String execute() throws Exception {
		StringBuffer sql = new StringBuffer();
		if(StringUtils.isNotBlank(id)){
//			sql.append("update bp_fine_area_tbl set name=?,type=?,effective_start_time=?,effective_end_time=?,is_used=? where id=? ");
			sql.append("update bp_fine_area_tbl set name=? where id=? ");
			Object[] params = new Object[2];
			params[0] = name;
			params[1] = id;
//			params[1] = type;
//			params[2] = effectiveStartTime;
//			params[3] = effectiveEndTime;
//			params[4] = isUsed;
//			params[5] = id;
			if(dbhelper.update(sql.toString(),params)){
				return super.execute();
			}
		}else{
			sql.append("insert into bp_fine_area_tbl(id,community_id,name,add_date) ");
			sql.append("VALUES(?,?,?,now()) ");
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			Object[] params = new Object[3];
			params[0] = id;
			params[1] = communityId;
			params[2] = name;
//			params[3] = type;
//			params[4] = effectiveStartTime;
//			params[5] = effectiveEndTime;
//			params[6] = isUsed;
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

//	public String getType() {
//		return type;
//	}
//
//	public void setType(String type) {
//		this.type = type;
//	}
//
//	public String getEffectiveStartTime() {
//		return effectiveStartTime;
//	}
//
//	public void setEffectiveStartTime(String effectiveStartTime) {
//		this.effectiveStartTime = effectiveStartTime;
//	}
//
//	public String getEffectiveEndTime() {
//		return effectiveEndTime;
//	}
//
//	public void setEffectiveEndTime(String effectiveEndTime) {
//		this.effectiveEndTime = effectiveEndTime;
//	}
//
//	public String getIsUsed() {
//		return isUsed;
//	}
//
//	public void setIsUsed(String isUsed) {
//		this.isUsed = isUsed;
//	}
//	
}
