package com.yf.base.actions.initialization;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.opensymphony.xwork2.ActionSupport;

public class GridView extends ActionSupport {
	private String year;

	public String getYear() {
		
		SimpleDateFormat sd=new SimpleDateFormat("yyyy");
		this.year=sd.format(new Date());
		
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}





	
}
