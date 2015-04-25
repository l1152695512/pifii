package com.yf.base.actions.mapposition.phoneseeting;

import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class UploadSeeting extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String id;
	private String startWidthSystem = "1";
	private String startTime = "08:00";
	private String endTime = "18:00";
	private String isBasestation = "1";
	private String isReadOnly = "0";
	private String period = "300";
	private String sensibility = "0";
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		String sql = "select id,start_width_system,start_time,end_time,is_basestation,is_read_only,period,sensibility from bp_phone_upload_seeting_tbl limit 1 ";
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
		if(dataList.size()>0){
			Map<String,Object> data = dataList.get(0);
			id = data.get("id").toString();
			startWidthSystem = null==data.get("start_width_system")?startWidthSystem:data.get("start_width_system").toString();
			startTime = null==data.get("start_time")?startTime:data.get("start_time").toString().substring(0, 5);
			endTime = null==data.get("end_time")?endTime:data.get("end_time").toString().substring(0, 5);
			isBasestation = null==data.get("is_basestation")?isBasestation:data.get("is_basestation").toString();
			isReadOnly = null==data.get("is_read_only")?isReadOnly:data.get("is_read_only").toString();
			period = null==data.get("period")?period:data.get("period").toString();
			sensibility = null==data.get("sensibility")?sensibility:data.get("sensibility").toString();
		}else{
			return "failure";
		}
		return super.execute();
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
