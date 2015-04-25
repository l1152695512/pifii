package com.yf.base.actions.patrolmanage.routeassign;


import com.opensymphony.xwork2.ActionSupport;

public class ShowRoutePoints extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String routeTimeId;
	private String mapPath;

	
	public String getRouteTimeId() {
		return routeTimeId;
	}
	
	public void setRouteTimeId(String routeTimeId) {
		this.routeTimeId = routeTimeId;
	}
	
	public String getMapPath() {
		return mapPath;
	}
	
	public void setMapPath(String mapPath) {
		this.mapPath = mapPath;
	}
	
}
