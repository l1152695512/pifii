package com.yf.base.actions.sys.user.usergroup;

import com.opensymphony.xwork2.ActionSupport;

public class SelectView extends ActionSupport {
	private String userId;;// 用户主键

	private String accountName;// 用户名称

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

}
