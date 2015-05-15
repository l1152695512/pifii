package com.yinfu.cloudportal.model;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class Feedback {
	private String routersn;
	private String clientMac;
	
	public Feedback(String routersn,String clientMac) {
		this.routersn = routersn;
		this.clientMac = clientMac;
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
