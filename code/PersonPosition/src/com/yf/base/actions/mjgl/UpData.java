package com.yf.base.actions.mjgl;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.util.DBUtil;

public class UpData extends ActionSupport {

	private String jsonString;
	

	@Override
	public String execute() throws Exception {
		try{
			String sql = "update aaa set tstats='0' where tstats='1'";
			new DBUtil().executeFor(sql);
			this.jsonString = "ok";
		}catch(Exception e){
			e.printStackTrace();
		}
	
		return "data";
	}
}
