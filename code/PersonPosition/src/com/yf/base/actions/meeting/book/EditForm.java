package com.yf.base.actions.meeting.book;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.opensymphony.xwork2.ActionSupport;

public class EditForm extends ActionSupport {

	private String editId;
	private String mName;
	private String start;
	private String end;
	private Date bDate;

	@Override
	public String execute() throws Exception {
		String str=start.substring(0,10);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d=sdf.parse(str);
		setBDate(d);
		String str1="";
		if("0".equals(start.substring(11, 12))){
			str1=start.substring(12);
		}else{
			str1=start.substring(11);
		}
		setStart(str1);
		String str2="";
		if("0".equals(end.substring(11, 12))){
			str2=end.substring(12);
		}else{
			str2=end.substring(11);
		}
		setEnd(str2);		
		return super.execute();
	}
	
	public String getEditId() {
		return editId;
	}

	public void setEditId(String editId) {
		this.editId = editId;
	}

	public String getMName() {
		return mName;
	}

	public void setMName(String name) {
		mName = name;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public Date getBDate() {
		return bDate;
	}

	public void setBDate(Date date) {
		bDate = date;
	}

}
