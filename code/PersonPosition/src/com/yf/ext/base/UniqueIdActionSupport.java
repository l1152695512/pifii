package com.yf.ext.base;

import com.opensymphony.xwork2.ActionSupport;

public class UniqueIdActionSupport extends ActionSupport {

	protected static int idSn = 0;
	public static synchronized int generate(){
		if(idSn>10000)idSn=0;
		return idSn++;
	}

	protected int uniqueId;

	public UniqueIdActionSupport() {
		super();
		uniqueId = generate();
	}

	public int getUniqueId() {
		return uniqueId;
	}

}
