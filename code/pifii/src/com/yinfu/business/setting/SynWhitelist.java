package com.yinfu.business.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.Utils;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.jbase.util.remote.RouterHelper;

public class SynWhitelist {
	private static Logger logger = Logger.getLogger(SynWhitelist.class);
	
	private static boolean is_syn = false;
	private static List<String> devices = new ArrayList<String>();
	private static Map<String,Record> synHosts = new HashMap<String,Record>();
	
	private void putHost(List<Record> synDevice,boolean removeAll){
		if(removeAll){
			devices.clear();
			synHosts.clear();
		}
		Iterator<Record> ite = synDevice.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			String sn = rec.getStr("sn");
			if(!devices.contains(sn)){
				devices.add(sn);
			}
			synHosts.put(sn, rec);
			logger.info("更新了"+sn+"的host设置。");
		}
	}
	
	public void synHost(List<Record> synDevice,boolean removeAll){
		if(null != synDevice){
			putHost(synDevice,removeAll);
		}
		if(!is_syn){
			logger.info(">>>>>>>>>>>>>>>>>>开始执行设置host，当前需要设置的盒子数量为"+devices.size());
			is_syn = true;
			try{
				for(int i=devices.size()-1;i>=0;i--){
					String thisSn = devices.get(i);
					if(null != thisSn && deviceIsOnline(thisSn)){
						try{
							Object deviceWhitelistId = synHosts.get(thisSn).get("id");
							List<String> hosts = getHostWhitelist(thisSn);
							NameValuePair[] params = new NameValuePair[hosts.size()+1];
							Record account = Utils.getAccount(thisSn);
							params[0] = new NameValuePair("token", RouterHelper.routerToken(account.getStr("remote_account"),account.getStr("remote_pass")));
							for(int j=0;j<hosts.size();j++){
								String thisHost = hosts.get(j);
								int index = thisHost.indexOf(":");
								params[j+1] = new NameValuePair(thisHost.substring(0, index), thisHost.substring(index+1));
							}
							JSONObject obj = JSONObject.parseObject(RouterHelper.whitelistSet(params));
							if(null != obj.get("status") && 
									("0".equals(obj.get("status").toString()) || 
									"1".equals(obj.get("status").toString()))){
								Db.update("delete from bp_device_whitelist_status where id=? ", new Object[]{deviceWhitelistId});
								devices.remove(thisSn);
								synHosts.remove(thisSn);
								logger.info(">>>>>>>>>>>>>>>>>>"+thisSn+"已同步");
							}
						}catch(Exception e){
							e.printStackTrace();
							logger.warn("设置白名单host失败！",e);
						}
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				is_syn = false;
			}
			logger.info(">>>>>>>>>>>>>>>>>>结束执行设置host");
		}else{
			logger.info("正在执行设置，等待下一次执行");
		}
	}
	
	private boolean deviceIsOnline(String thisSn){
		int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);//路由上报数据的时间间隔,要考虑网络延迟
		StringBuffer sql = new StringBuffer();
		sql.append("select IF(report_date is null,'0',IF(date_add(report_date, interval "+interval+" second) > now(),'1','0')) is_online ");
		sql.append("from bp_device where router_sn = ? ");
		Record device = Db.findFirst(sql.toString(), new Object[]{thisSn});
		if(null != device && "1".equals(device.get("is_online"))){
			return true;
		}
		return false;
	}
	
	public static List<Record> getMacWhitelist(String sn){
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct content ");
		sql.append("from bp_shop_whitelist sw join bp_device d on (d.router_sn=? and sw.shop_id=d.shop_id) ");
		sql.append("where sw.type='mac' ");
		List<Record> whitelist = Db.find(sql.toString(),new Object[]{sn});
		return whitelist;
	}
	
	private static List<String> getHostWhitelist(String sn){
		List<String> hosts = new ArrayList<String>();
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct CONCAT(sw.type,':',sw.content) content ");
		sql.append("from bp_shop_whitelist sw join bp_device d on (d.router_sn=? and sw.shop_id=d.shop_id) ");
		sql.append("where sw.type='ip' or sw.type='domain' ");
		List<Record> whitelist = Db.find(sql.toString(),new Object[]{sn});
		Iterator<Record> ite = whitelist.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			hosts.add(rowData.getStr("content"));
		}
		//加入默认的白名单域名及IP
		String defaultHost = PropertyUtils.getProperty("route.default.whitelist","domain:weixin.qq.com|domain:api.map.baidu.com");
		String[] hostsStr = defaultHost.split("\\|");
		for(int i=0;i<hostsStr.length;i++){
			String host = hostsStr[i];
			if(!hosts.contains(host)){
				hosts.add(host);
			}
		}
		//去掉不能加入的白名单域名及IP
		String notAllowedHost = PropertyUtils.getProperty("route.notAllowed.whitelist","");
		String[] notAllowedHostsStr = notAllowedHost.split("\\|");
		for(int i=0;i<notAllowedHostsStr.length;i++){
			String type = notAllowedHostsStr[i].substring(0, notAllowedHostsStr[i].indexOf(":"));
			String content = notAllowedHostsStr[i].substring(notAllowedHostsStr[i].indexOf(":")+1);
			for(int j=0;j<hosts.size();j++){
				String thisHost = hosts.get(j);
				String thisType = thisHost.substring(0, thisHost.indexOf(":"));
				String thisContent = "";
				if(thisHost.length() > thisHost.indexOf(":")){
					thisContent = thisHost.substring(thisHost.indexOf(":")+1);
				}
				if(thisType.equals(type) && thisContent.equals(content)){
					hosts.remove(thisHost);
					break;
				}
			}
		}
		return hosts;
	}
	
}
