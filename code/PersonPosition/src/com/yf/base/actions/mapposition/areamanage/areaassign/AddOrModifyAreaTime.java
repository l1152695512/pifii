package com.yf.base.actions.mapposition.areamanage.areaassign;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class AddOrModifyAreaTime extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String areaId;
	private String id;
	private String name;
	private String areaType = "10";
	private String startTime = "08:00:00";
	private String endTime = "18:00:00";
	private String isUsed = "1";
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		if(StringUtils.isNotBlank(id)){
			String sql = "select name,area_type,start_time,end_time,is_used from bp_coarse_area_time_tbl where id='"+id+"' ";
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
			if(dataList.size()>0){
				Map<String,Object> data = dataList.get(0);
				name = null==data.get("name")?"":data.get("name").toString();
				areaType = null==data.get("area_type")?"10":data.get("area_type").toString();
				startTime = null==data.get("start_time")?"08:00:00":data.get("start_time").toString();
				endTime = null==data.get("end_time")?"18:00:00":data.get("end_time").toString();
				isUsed = null==data.get("is_used")?"1":data.get("is_used").toString();
			}else{
				return "failure";
			}
		}
		return super.execute();
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
