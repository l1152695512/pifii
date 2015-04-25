package com.yf.base.actions.datastatistics.patrol;


import com.opensymphony.xwork2.ActionSupport;

public class PatrolInfoView extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String personName;
	private String routeName;
	private String communityName;
	private String date;
	private String personId;
	private String phone;
	private String routeTimeId;
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
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getCommunityName() {
		return communityName;
	}
	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
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
