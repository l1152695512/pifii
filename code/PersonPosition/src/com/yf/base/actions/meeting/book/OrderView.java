package com.yf.base.actions.meeting.book;

import com.opensymphony.xwork2.ActionSupport;

public class OrderView extends ActionSupport {
	
	private String userName;

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

}