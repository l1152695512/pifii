package com.yinfu.servlet.route.fun;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class Feedback {
	private String routersn;
	private String clientMac;
	
	public Feedback(String routersn,String clientMac) {
		this.routersn = routersn;
		this.clientMac = clientMac;
	}
	
	public void index(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		String rid = request.getParameter("rid");
		if(StringUtils.isNotBlank(rid)){
			int index = rid.lastIndexOf("_");
			Commons.addAccessData(routersn,clientMac,rid.substring(index+1),rid.substring(0, index));
		}
		response.addHeader("Cache-Control", "no-store, must-revalidate"); 
		response.addHeader("Expires", "Thu, 01 Jan 1970 00:00:01 GMT");
		request.getRequestDispatcher("/page/routerapp/feedback/index.jsp").forward(request,response);
	}
	
	public void save(String opinion){
		try{
			StringBuffer sql = new StringBuffer();
			sql.append("select count(*) count ");
			sql.append("from bp_device d1 join bp_device d2 on (d1.router_sn=? and d1.shop_id=d2.shop_id) ");
			sql.append("join bp_feedback f on (f.mac=? and d2.router_sn=f.router_sn) ");
			Record rec = Db.findFirst(sql.toString(), new Object[]{routersn,clientMac});
			if(0 == rec.getLong("count")){
				Db.update("insert into bp_feedback(router_sn,mac,opinion,create_time) values(?,?,?,now())", 
						new Object[]{routersn,clientMac,opinion});
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
