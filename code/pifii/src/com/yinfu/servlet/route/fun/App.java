package com.yinfu.servlet.route.fun;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.util.PropertyUtils;

public class App {
	private String routersn;
	private String clientMac;
	private String appId;
	
	public App(String routersn,String clientMac) {
		this.routersn = routersn;
		this.clientMac = clientMac;
	}
	
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		appId = request.getParameter("id");
		
		Record adv = Db.findFirst("select link from bp_app where id = ? and delete_date is null ", 
				new Object[]{appId});
		if(null != adv){
			try{
				Commons.addAccessData(routersn, clientMac, appId, PropertyUtils.getProperty("route.upload.type.app"));
			}catch(Exception e){}
			response.sendRedirect(adv.getStr("link"));
		}else{
			response.sendRedirect(PropertyUtils.getProperty("router.auth.errorPage"));
		}
		System.err.println("-----------app");
	}
}
