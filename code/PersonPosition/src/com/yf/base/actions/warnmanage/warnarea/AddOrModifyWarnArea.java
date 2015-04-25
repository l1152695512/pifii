package com.yf.base.actions.warnmanage.warnarea;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class AddOrModifyWarnArea extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String communityId;
	private String id;
	private String name;
//	private String type;
//	private String effectiveStartTime;
//	private String effectiveEndTime;
//	private String isUsed;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		if(StringUtils.isNotBlank(id)){
			String sql = "select name from bp_fine_area_tbl where id='"+id+"' ";
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
			if(dataList.size()>0){
				Map<String,Object> data = dataList.get(0);
				name = null==data.get("name")?"":data.get("name").toString();
//				type = null==data.get("type")?"":data.get("type").toString();
//				effectiveStartTime = null==data.get("effective_start_time")?"":data.get("effective_start_time").toString();
//				effectiveEndTime = null==data.get("effective_end_time")?"":data.get("effective_end_time").toString();
//				isUsed = null==data.get("is_used")?"":data.get("is_used").toString();
			}else{
				return "failure";
			}
		}else{
//			effectiveStartTime = "08:00:00";
//			effectiveEndTime = "18:00:00";
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

}
