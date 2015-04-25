package com.yf.base.actions.patrolmanageold.routeplan;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class AddOrModifyRoutePoint extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String id;
	private String routeId;
	private String name;
	private String locationX;
	private String locationY;
	private String previousPoint;
	private String nextPoint;
	private String effectiveRange;
	private String effectiveStartTime;
	private String effectiveEndTime;

	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		if(StringUtils.isNotBlank(id)){
			String sql = "select name,location_x,location_y,previous_point_id,next_point_id,effective_range," +
					"effective_start_time,effective_end_time " +
					"from bp_fine_route_point_tbl " +
					"where id='"+id+"' ";
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
			if(dataList.size()>0){
				Map<String,Object> data = dataList.get(0);
				name = null==data.get("name")?"":data.get("name").toString();
				locationX = null==data.get("location_x")?"":data.get("location_x").toString();
				locationY = null==data.get("location_y")?"":data.get("location_y").toString();
				previousPoint = null==data.get("previous_point_id")?"":data.get("previous_point_id").toString();
				nextPoint = null==data.get("next_point_id")?"":data.get("next_point_id").toString();
				effectiveRange = null==data.get("effective_range")?"":data.get("effective_range").toString();
				effectiveStartTime = null==data.get("effective_start_time")?"":data.get("effective_start_time").toString();
				effectiveEndTime = null==data.get("effective_end_time")?"":data.get("effective_end_time").toString();
			}else{
				return "failure";
			}
		}else{
			String sql = "select id from bp_fine_route_point_tbl where route_id='"+routeId+"' and (next_point_id is null or trim(next_point_id) = '') ";
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
			if(dataList.size()>0){
				Map<String,Object> data = dataList.get(0);
				previousPoint = null==data.get("id")?"":data.get("id").toString();
			}
			effectiveRange = "50";
			effectiveStartTime = "08:00:00";
			effectiveEndTime = "18:00:00";
		}
		return super.execute();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocationX() {
		return locationX;
	}

	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}

	public String getLocationY() {
		return locationY;
	}

	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}

	public String getPreviousPoint() {
		return previousPoint;
	}

	public void setPreviousPoint(String previousPoint) {
		this.previousPoint = previousPoint;
	}

	public String getNextPoint() {
		return nextPoint;
	}

	public void setNextPoint(String nextPoint) {
		this.nextPoint = nextPoint;
	}

	public String getEffectiveRange() {
		return effectiveRange;
	}

	public void setEffectiveRange(String effectiveRange) {
		this.effectiveRange = effectiveRange;
	}

	public String getEffectiveStartTime() {
		return effectiveStartTime;
	}

	public void setEffectiveStartTime(String effectiveStartTime) {
		this.effectiveStartTime = effectiveStartTime;
	}

	public String getEffectiveEndTime() {
		return effectiveEndTime;
	}

	public void setEffectiveEndTime(String effectiveEndTime) {
		this.effectiveEndTime = effectiveEndTime;
	}

}
