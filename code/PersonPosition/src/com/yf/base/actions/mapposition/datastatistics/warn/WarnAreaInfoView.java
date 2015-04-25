package com.yf.base.actions.mapposition.datastatistics.warn;


import com.opensymphony.xwork2.ActionSupport;

public class WarnAreaInfoView extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private String personName;
	private String areaName;
	private String date;
	private String phone;
	private String areaId;
	private String areaTimeId;
	private String areaType;
	
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
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
	public String getAreaId() {
		return areaId;
	}
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	public String getAreaTimeId() {
		return areaTimeId;
	}
	public void setAreaTimeId(String areaTimeId) {
		this.areaTimeId = areaTimeId;
	}
	public String getAreaType() {
		return areaType;
	}
	public void setAreaType(String areaType) {
		this.areaType = areaType;
	}
}
