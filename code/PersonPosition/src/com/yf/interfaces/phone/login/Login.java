package com.yf.interfaces.phone.login;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.yf.interfaces.phone.device.CustomerDevice;
import com.yf.interfaces.phone.main.FirstPage;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class Login {
	
	private static Logger logger = Logger.getLogger(Login.class);
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);

	public JSONObject validateUser(JSONObject json) {
		JSONObject resultObject = new JSONObject();
		String account = json.getJSONObject("data").getString("ACCOUNT");
		if ("".equals(account) || account == null) {
			resultObject.put("returnCode", 400);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "帐号不能为空");
			return resultObject;
		}
		String password = json.getJSONObject("data").getString("PASSWORD");
		if ("".equals(password) || password == null) {
			resultObject.put("returnCode", 400);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "密码不能为空");
			return resultObject;
		}

		Object params[] = new Object[2];
		params[0] = account;    //帐号
		params[1] = password;	//密码

		String sql = "select ACCOUNT from BP_CUSTOMER_TBL where ACCOUNT= ? and PASSWORD = ?";
		List list = dbhelper.getMapListBySql(sql, params);
		if (list.isEmpty()) {
			resultObject.put("returnCode", 400);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "帐号或密码错误");
			logger.info("登陆用户名[" + account + "]或密码[" + password + "]错误!");
		} else {
			resultObject.put("returnCode", 200);
			//只返回登录账号
			//resultObject.put("data", list.get(0));
			//返回用户信息、设备信息、设置信息
			JSONObject object=new JSONObject();
			JSONObject object1=new JSONObject();
			object1.put("data", list.get(0));
			FirstPage fp=new FirstPage();
			object.put("USER_INFO", fp.getCustomerJSON(object1).getJSONObject("data"));
			CustomerDevice cd=new CustomerDevice();
			object.put("DEVICE_INFO", cd.getCustomerDevice(object1).getJSONArray("data"));
			object.put("SETTING_INFO", new JSONObject());
			resultObject.put("data", object);
			resultObject.put("desc", "帐号[" + account + "]登陆验证成功!");
			logger.info("帐号[" + account + "]登陆验证成功!");
		}
		return resultObject;
	}

	public static void main(String[] args) {
		Login login = new Login();
		JSONObject json = new JSONObject();
		json.put("tradeCode", 1101);
		JSONObject jObject = new JSONObject();
		jObject.put("ACCOUNT", "admin");
		jObject.put("PASSWORD", "1234");
		json.put("data", jObject);
		json.put("authCode", "admin");
		System.out.println(json);
		System.out.println(login.validateUser(json).toString());
	}
}
