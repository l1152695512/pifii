package com.yf.base.actions.mapposition.phoneseeting;

import java.util.UUID;

import org.apache.commons.lang.xwork.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SaveUploadSeeting extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String id;
	private String startWidthSystem;
	private String startTime;
	private String endTime;
	private String isBasestation;
	private String isReadOnly;
	private String period;
	private String sensibility;

	@Override
	public String execute() throws Exception {
		StringBuffer sql = new StringBuffer();
		if(StringUtils.isNotBlank(id)){
			sql.append("update bp_phone_upload_seeting_tbl set start_width_system=?,start_time=?,end_time=?,is_basestation=?,is_read_only=?,period=?,sensibility=? where id=? ");
			Object[] params = new Object[8];
			params[0] = startWidthSystem;
			params[1] = startTime;
			params[2] = endTime;
			params[3] = isBasestation;
			params[4] = isReadOnly;
			params[5] = period;
			params[6] = sensibility;
			params[7] = id;
			if(dbhelper.update(sql.toString(),params)){
				return super.execute();
			}
		}else{
			sql.append("insert into bp_phone_upload_seeting_tbl(id,start_width_system,start_time,end_time,is_basestation,is_read_only,period,sensibility,update_date) ");
			sql.append("VALUES(?,?,?,?,?,?,?,?,now()) ");
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			Object[] params = new Object[8];
			params[0] = id;
			params[1] = startWidthSystem;
			params[2] = startTime;
			params[3] = endTime;
			params[4] = isBasestation;
			params[5] = isReadOnly;
			params[6] = period;
			params[7] = sensibility;
			if(dbhelper.insert(sql.toString(),params)){
				return super.execute();
			}
		}
		return "failure";
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public String getStartWidthSystem() {
		return startWidthSystem;
	}

	public void setStartWidthSystem(String startWidthSystem) {
		this.startWidthSystem = startWidthSystem;
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

	public String getIsBasestation() {
		return isBasestation;
	}

	public void setIsBasestation(String isBasestation) {
		this.isBasestation = isBasestation;
	}

	public String getIsReadOnly() {
		return isReadOnly;
	}

	public void setIsReadOnly(String isReadOnly) {
		this.isReadOnly = isReadOnly;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public String getSensibility() {
		return sensibility;
	}

	public void setSensibility(String sensibility) {
		this.sensibility = sensibility;
	}
}
