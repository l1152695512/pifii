package com.yinfu.business.util;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class Utils {
	public static Record getAccount(String sn){
		Record rec = Db.findFirst("select name,ifnull(remote_account,'') remote_account,ifnull(remote_pass,'') remote_pass from bp_device where router_sn=? ", new Object[]{sn});
		if(null == rec){
			rec = new Record();
		}
		if(null == rec.get("remote_account") || StringUtils.isBlank(rec.getStr("remote_account"))){
			rec.set("remote_account", sn+"@pifii.com");
		}
		
		if(null == rec.get("remote_pass") || StringUtils.isBlank(rec.getStr("remote_pass"))){
			rec.set("remote_pass", "88888888");
		}
		return rec;
	}
	
	public static String queryLike(String srcStr) {
		//适用于sqlserver
//		result = StringUtils.replace(result, "[", "[[]");
//		result = StringUtils.replace(result, "_", "[_]");
//		result = StringUtils.replace(result, "%", "[%]");
//		result = StringUtils.replace(result, "^", "[^]");
		//适用于mysql
		srcStr = StringUtils.replace(srcStr, "\\", "\\\\");
		srcStr = StringUtils.replace(srcStr, "'", "\\'");
		srcStr = StringUtils.replace(srcStr, "_", "\\_");
		srcStr = StringUtils.replace(srcStr, "%", "\\%");
		
		return "%" + srcStr + "%";
	}
	
}
