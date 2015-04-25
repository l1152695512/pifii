package com.yf.base.actions.mjgl;


import com.opensymphony.xwork2.ActionSupport;

public class PhotoShow extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
