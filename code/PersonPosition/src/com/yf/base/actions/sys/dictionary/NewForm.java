package com.yf.base.actions.sys.dictionary;

import java.util.Calendar;

import com.opensymphony.xwork2.ActionSupport;

public class NewForm extends ActionSupport {
	
  private String prefixTitle;

	public String getPrefixTitle() {
		return prefixTitle;
	}
	
	public void setPrefixTitle(String prefixTitle) {
		this.prefixTitle = prefixTitle;
	}

	@Override
	public String execute() throws Exception {
		Calendar cal = Calendar.getInstance();
	    this.prefixTitle = cal.get(Calendar.YEAR)+"年";
		return super.execute();
	}
	
	
}
