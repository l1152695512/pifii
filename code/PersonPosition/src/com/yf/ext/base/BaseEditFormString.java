package com.yf.ext.base;

import com.opensymphony.xwork2.ActionSupport;

public class BaseEditFormString extends ActionSupport {
	
	private static final long serialVersionUID = 7687461255900792698L;
	protected String formTitle;
	protected String editId;
	
	
	public String getFormTitle() {
		return formTitle;
	}

	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}



	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	@Override
	public String execute() throws Exception { 
		return SUCCESS;
	}
}
