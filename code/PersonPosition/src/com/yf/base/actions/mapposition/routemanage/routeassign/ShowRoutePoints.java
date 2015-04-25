package com.yf.base.actions.mapposition.routemanage.routeassign;


import com.opensymphony.xwork2.ActionSupport;

public class ShowRoutePoints extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String routeTimeId;

	
	public String getRouteTimeId() {
		return routeTimeId;
	}
	
	public void setRouteTimeId(String routeTimeId) {
		this.routeTimeId = routeTimeId;
	}
	
}
