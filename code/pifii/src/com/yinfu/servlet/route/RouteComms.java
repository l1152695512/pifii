package com.yinfu.servlet.route;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.util.PropertyUtils;

public class RouteComms {
	public String isClientAuthed(String code,String mac){
		int phoneAuthExpireLength = PropertyUtils.getPropertyToInt("auth.phone.expire.length", 30);
		StringBuffer strB = new StringBuffer();
		strB.append("select ba.tag ");
		strB.append("from bp_auth ba left join bp_device bd on (bd.router_sn = ? and ba.router_sn = bd.router_sn) ");
		strB.append("where ba.client_mac = ? and ba.auth_type = 'phone' and length(ba.tag) = 11 and To_Days(now())-To_Days(ba.auth_date) < ? ");
		Record record = Db.findFirst(strB.toString(),new Object[]{code,mac,phoneAuthExpireLength});
		if(null != record){
			return record.get("tag");
		}
		return null;
	}
	
	public static boolean isPhoneAuthed(String mac,String phone){
		int phoneAuthExpireLength = PropertyUtils.getPropertyToInt("auth.phone.expire.length", 30);
		Record record = Db.findFirst("select id from bp_auth where auth_type='phone' and client_mac=? and tag=? and To_Days(now())-To_Days(auth_date) < ? ",
				new Object[]{mac,phone,phoneAuthExpireLength});
		if(null != record){
			return true;
		}
		return false;
	}
}
