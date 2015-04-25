package com.yf.base.actions.datastatistics.patrolold;


import com.opensymphony.xwork2.ActionSupport;

public class PatrolInfoView extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String personName;
	private String routeName;
	private String date;
	private String personId;
	private String routeId;
	private String mapPath;
	
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getMapPath() {
		return mapPath;
	}
	public void setMapPath(String mapPath) {
		this.mapPath = mapPath;
	}
}
