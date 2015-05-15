package com.yinfu.jbase.util.remote;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class QosControlThread implements Runnable {
	private static Logger logger = Logger.getLogger(QosControlThread.class);
	private String sn;
	private String email;
	private String pass;
	private int qosStatus;//0:关；1:开

	public QosControlThread(String sn, String email, String pass,int qosStatus) {
		this.sn = sn;
		this.email = email;
		this.pass = pass;
		this.qosStatus = qosStatus;
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
						NameValuePair[] paramsGet = { new NameValuePair("token", token),new NameValuePair("enabled", this.qosStatus == 0 ? "0":"1"),new NameValuePair("upload", "100"),new NameValuePair("download", "100")};
						String getData = client.httpRouterGet(APIDefine.SET_QOS, paramsGet);
						JSONObject json = JSONObject.parseObject(getData);
						if(json.containsKey("result") && ("true".equals(json.get("result").toString()))){
							successful(this.qosStatus,this.sn);
						}
					}
				}
			}
		}catch(Exception es){
			logger.error("开始尝试Qos开关控制工作失败...0：关;1:开,当前操作："+this.qosStatus+",sn:"+this.sn,es);
		}
		
		RebootDeviceThread.deviceList.remove(this.sn);
	}
	
	private static void successful(int qosStatus,String sn){
		Record rd = Db.findFirst("select 1 from bp_device_setting where sn=?",new Object[]{sn});
		if(rd != null){
			Db.update("update bp_device_setting set qos_last_date=now(),qos_status=?,binding=1 where sn=?",new Object[]{qosStatus,sn});
		}else{
			Db.update("insert into bp_device_setting(sn,qos_status,qos_last_date,binding) values(?,?,now(),1)",new Object[]{sn,qosStatus});
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
