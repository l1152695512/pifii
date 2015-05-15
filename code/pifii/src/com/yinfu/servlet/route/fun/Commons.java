package com.yinfu.servlet.route.fun;

import org.apache.commons.lang.StringUtils;

import com.jfinal.plugin.activerecord.Db;

public class Commons {
	public static void addAccessData(String routerSN,String clientMac,String id,String type){
		if(StringUtils.isNotBlank(id)){
			try{
				if(StringUtils.isBlank(routerSN)){
					routerSN = "";
				}
				if(StringUtils.isBlank(clientMac)){
					clientMac = "";
				}
				Db.update("insert into bp_statistics_all(router_sn,client_mac,statistics_id,statistics_type,access_date,create_date) "
						+ "values(?,?,?,?,now(),now()) ", new Object[]{routerSN,clientMac,id,type});
			}catch(Exception e){
			}
		}
		
	}
}
