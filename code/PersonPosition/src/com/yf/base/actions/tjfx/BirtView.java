package com.yf.base.actions.tjfx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.opensymphony.xwork2.ActionSupport;

public class BirtView extends  ActionSupport implements ServletRequestAware, ServletResponseAware{



	
	private HttpServletRequest request;
	private HttpServletResponse response;


	@Override
	public String execute() throws Exception {
		request.getRequestDispatcher("/frameset?__report=reports/composite.rptdesign&__format=HTML").forward(request, response);
		return  null;
		
		
	}

	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request=request;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setServletResponse(HttpServletResponse response) {
		// TODO Auto-generated method stub
		this.response=response;
	}




}
