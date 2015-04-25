package com.yf.base.actions.meeting.book;


import org.springframework.security.context.SecurityContextHolder;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.auth.SystemUser;

public class GridView extends ActionSupport {
	
	private String userName = "";
	
	@Override
	public String execute() throws Exception {
		SystemUser sysuser = (SystemUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		userName = sysuser.getRealUserName();
		return SUCCESS;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
