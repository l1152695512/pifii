package com.yf.ext.base;

import com.opensymphony.xwork2.ActionSupport;

public class BaseEditForm<T> extends ActionSupport {
	
	protected static int idSn = 0;
	public static synchronized int generate(){
		if(idSn>10000)idSn=0;
		return idSn++;
	}

	protected int uniqueId;

	

	public int getUniqueId() {
		return uniqueId;
	}

	public BaseEditForm(){
		super();

		uniqueId = generate();
		
	}
	private static final long serialVersionUID = 7687461255900792698L;
	protected String formTitle;
	protected T editId;
	
	
	public String getFormTitle() {
		return formTitle;
	}

	public void setFormTitle(String formTitle) {
		this.formTitle = formTitle;
	}

	public T getEditId() {
		return editId;
	}

	public void setEditId(T editId) {
		this.editId = editId;
	}

	@Override
	public String execute() throws Exception { 
		return SUCCESS;
	}
}
