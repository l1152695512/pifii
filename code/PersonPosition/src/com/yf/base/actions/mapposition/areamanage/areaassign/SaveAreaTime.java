package com.yf.base.actions.mapposition.areamanage.areaassign;

import java.util.UUID;

import org.apache.commons.lang.xwork.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SaveAreaTime extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String areaId;
	private String id;
	private String name;
	private String areaType;
	private String startTime;
	private String endTime;
	private String isUsed;

	@Override
	public String execute() throws Exception {
		StringBuffer sql = new StringBuffer();
		if(StringUtils.isNotBlank(id)){
			sql.append("update bp_coarse_area_time_tbl set name=?,area_type=?,start_time=?,end_time=?,is_used=? where id=? ");
			Object[] params = new Object[6];
			params[0] = name;
			params[1] = areaType;
			params[2] = startTime;
			params[3] = endTime;
			params[4] = isUsed;
			params[5] = id;
			if(dbhelper.update(sql.toString(),params)){
				return super.execute();
			}
		}else{
			sql.append("insert into bp_coarse_area_time_tbl(id,area_id,name,area_type,start_time,end_time,is_used,add_date) ");
			sql.append("VALUES(?,?,?,?,?,?,?,now()) ");
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			Object[] params = new Object[7];
			params[0] = id;
			params[1] = areaId;
			params[2] = name;
			params[3] = areaType;
			params[4] = startTime;
			params[5] = endTime;
			params[6] = isUsed;
			if(dbhelper.insert(sql.toString(),params)){
				return super.execute();
			}
		}
		return "failure";
	}
	
	public String getAreaId() {
		return areaId;
	}
	
	public void setAreaId(String areaId) {
		this.areaId = areaId;
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

	public String getAreaType() {
		return areaType;
	}
	
	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	public String getIsUsed() {
		return isUsed;
	}
	
	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}
	
}
