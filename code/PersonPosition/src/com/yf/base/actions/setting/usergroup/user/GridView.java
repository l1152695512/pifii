package com.yf.base.actions.setting.usergroup.user;


import com.opensymphony.xwork2.ActionSupport;

public class GridView extends ActionSupport {
	private String sugid;//用户组主键
	private String name;//用户组名称
	public String getSugid() {
		return sugid;
	}
	public void setSugid(String sugid) {
		this.sugid = sugid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
