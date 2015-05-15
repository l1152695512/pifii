package com.yinfu.jbase.util.remote;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class ProxyControlThread implements Runnable {
	private static Logger logger = Logger.getLogger(ProxyControlThread.class);
	private String sn;
	private String email;
	private String pass;
	private int proxyStatus;//0:关；1:开

	public ProxyControlThread(String sn, String email, String pass,int proxyStatus) {
		this.sn = sn;
		this.email = email;
		this.pass = pass;
		this.proxyStatus = proxyStatus;
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
						NameValuePair[] paramsGet = { new NameValuePair("token", token),new NameValuePair("status", this.proxyStatus == 0 ? "n":"y")};
						String getData = client.httpRouterGet(APIDefine.SET_PRIVOXY, paramsGet);
						JSONObject json = JSONObject.parseObject(getData);
						if(json.containsKey("result") && ("true".equals(json.get("result").toString()))){
							successful(this.proxyStatus,this.sn);
						}
					}
				}
			}
		}catch(Exception es){
			logger.error("开始尝试proxy开关控制工作失败...0：关;1:开,当前操作："+this.proxyStatus+",sn:"+this.sn,es);
		}
		
		RebootDeviceThread.deviceList.remove(this.sn);
	}
	
	private static void successful(int proxyStatus,String sn){
		Record rd = Db.findFirst("select 1 from bp_device_setting where sn=?",new Object[]{sn});
		if(rd != null){
			Db.update("update bp_device_setting set proxy_last_date=now(),proxy_status=?,binding=1 where sn=?",new Object[]{proxyStatus,sn});
		}else{
			Db.update("insert into bp_device_setting(sn,proxy_status,proxy_last_date,binding) values(?,?,now(),1)",new Object[]{sn,proxyStatus});
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
