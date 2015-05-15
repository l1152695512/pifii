package com.yinfu.jbase.util.remote;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class RebootDeviceThread implements Runnable {
	private static Logger logger = Logger.getLogger(RebootDeviceThread.class);
	private String sn;
	private String email;
	private String pass;
	public static List<String> deviceList = new ArrayList<String>();

	public RebootDeviceThread(String sn, String email, String pass) {
		this.sn = sn;
		this.email = email;
		this.pass = pass;
	}

	@Override
	public void run() {
		try{
			YFHttpClient client = new YFHttpClient();
			client.setShowLog(false);
			String xsrf = client.serverInfo();
			String loginResult = client.login(this.email, this.pass, xsrf);
			if("".equals(loginResult)){
				if(this.pass.equals("88888888")){
					this.pass = "2014@pifii.com-yinfu";
				}else{
					this.pass = "88888888";
				}
				loginResult = client.login(this.email, this.pass, xsrf);
			}
			if("".equals(loginResult)){
				failure(this.sn);
			}else{
				JSONObject obj = JSONObject.parseObject(loginResult);
				JSONArray states = (JSONArray) obj.get("router_states");
				if (states.size() > 0) {
					JSONObject router = (JSONObject) states.get(0);
					String token = router.getString("token");
					if(!"".equals(token)){
						NameValuePair[] paramsGet = { new NameValuePair("token", token) };
						String getData = client.httpRouterGet(APIDefine.SYS_REBOOT, paramsGet);
						JSONObject json = JSONObject.parseObject(getData);
						if(json.containsKey("status") && ("0".equals(json.get("status").toString()) || "1".equals(json.get("status").toString()))){
							successful(this.sn,this.email,this.pass);
						}
					}
				}
			}
		}catch(Exception es){
			logger.error("开始尝试重启设备失败,sn:"+this.sn,es);
		}
		
		deviceList.remove(this.sn);
	}
	
	private static void successful(String sn,String email,String pass){
		Db.update("update bp_device set reboot_date=now(),remote_account=?,remote_pass=? where router_sn=?",new Object[]{email,pass,sn});
		Record rd = Db.findFirst("select 1 from bp_device_setting where sn=?",new Object[]{sn});
		if(rd != null){
			Db.update("update bp_device_setting set binding=1 where sn=?",new Object[]{sn});
		}else{
			Db.update("insert into bp_device_setting(sn,binding) values(?,1)",new Object[]{sn});
		}
	}
	
	//binding=0未绑定远程帐号；1 绑定
	private static void failure(String sn){
		Record rd = Db.findFirst("select 1 from bp_device_setting where sn=?",new Object[]{sn});
		if(rd != null){
			Db.update("update bp_device_setting set binding=0 where sn=?",new Object[]{sn});
		}else{
			Db.update("insert into bp_device_setting(sn,binding) values(?,0)",new Object[]{sn});
		}
	}
	
}
