package com.yf.base.actions.mapposition.datastatistics.patrol;


import com.opensymphony.xwork2.ActionSupport;

public class PatrolInfoView extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String personName;
	private String routeName;
	private String date;
	private String phone;
	private String routeTimeId;
	
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getRouteTimeId() {
		return routeTimeId;
	}
	public void setRouteTimeId(String routeTimeId) {
		this.routeTimeId = routeTimeId;
	}
}
