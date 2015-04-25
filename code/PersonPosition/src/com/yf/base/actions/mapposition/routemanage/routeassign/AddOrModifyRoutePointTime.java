package com.yf.base.actions.mapposition.routemanage.routeassign;


import com.opensymphony.xwork2.ActionSupport;

public class AddOrModifyRoutePointTime extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String routePointId;
	private String routeTimeId;
	private String startTime;
	private String endTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRoutePointId() {
		return routePointId;
	}
	public void setRoutePointId(String routePointId) {
		this.routePointId = routePointId;
	}
	public String getRouteTimeId() {
		return routeTimeId;
	}
	public void setRouteTimeId(String routeTimeId) {
		this.routeTimeId = routeTimeId;
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
	
}
