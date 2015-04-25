package com.yf.base.actions.meeting;

import com.opensymphony.xwork2.ActionSupport;

public class DetailView extends ActionSupport {
	
	private String userName;
	private String date1;
	private String date2;

	@Override
	public String execute() throws Exception {
		return super.execute();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDate1() {
		return date1;
	}

	public void setDate1(String date1) {
		this.date1 = date1;
	}

	public String getDate2() {
		return date2;
	}

	public void setDate2(String date2) {
		this.date2 = date2;
	}

}