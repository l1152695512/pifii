package com.yf.base.actions.patrolmanageold.routeassign;

import com.opensymphony.xwork2.ActionSupport;

public class AddAssignPerson extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private String routeId;
	
	public String getRouteId() {
		return routeId;
	}
	
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	
}
