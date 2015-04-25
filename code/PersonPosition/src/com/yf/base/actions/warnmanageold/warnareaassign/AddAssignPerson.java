package com.yf.base.actions.warnmanageold.warnareaassign;

import com.opensymphony.xwork2.ActionSupport;

public class AddAssignPerson extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private String areaId;
	
	public String getAreaId() {
		return areaId;
	}
	
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
}
