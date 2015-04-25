package com.yf.base.actions.personmanage;


import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class PersonView extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private String userId;
	
	@Override
	public String execute() throws Exception {
		userId=(String) ActionContext.getContext().getSession().get("loginUserId");
		return super.execute();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
