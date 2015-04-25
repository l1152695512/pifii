package com.yf.base.actions.sys.action.item;


import com.opensymphony.xwork2.ActionSupport;

public class GridView extends ActionSupport {
	private String said;//权限ID
	private String name;



	public String getSaid() {
		return said;
	}

	public void setSaid(String said) {
		this.said = said;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
