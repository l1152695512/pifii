package com.yf.base.actions.setting.region;

import com.opensymphony.xwork2.ActionSupport;

public class NewForm extends ActionSupport {
	private String pid;//父类主键
	
	private String pname;

	public String getPname() {
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

}
