package com.yf.interfaces.phone.setting;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class QuickSetting {
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static Logger logger = Logger.getLogger(QuickSetting.class);
	
	public JSONObject checkUpdate(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		JSONObject obj=new JSONObject();
		obj.put("VERSION", "翼能管家-手机版v1.1");
		obj.put("DESC", "新版本添加了XXX功能");
		obj.put("URL", "http://www.ifidc.com/yngj");
		resultObject.put("returnCode", 200);
		resultObject.put("data", obj);
		resultObject.put("desc", "客户端操作成功");
		return resultObject;
	}
	
	public JSONObject pushMessage(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		JSONObject obj=new JSONObject();
		obj.put("TITLE", "冰箱节能小技巧");
		obj.put("CONTENT", "冰箱是日常家电中耗电比较大的电器。。。");
		obj.put("TIME", sdf.format(new Date()));
		resultObject.put("returnCode", 200);
		resultObject.put("data", obj);
		resultObject.put("desc", "客户端操作成功");
		return resultObject;
	}

	public static void main(String[] args) {
		QuickSetting quickSetting=new QuickSetting();
		JSONObject json = new JSONObject();
		json.put("tradeCode", 6101);
		json.put("data", new JSONObject());
		json.put("authCode", "admin");
		System.out.println(json);
		System.out.println(quickSetting.checkUpdate(json));
		
		JSONObject json1 = new JSONObject();
		json1.put("tradeCode", 6102);
		json1.put("data", new JSONObject());
		json1.put("authCode", "admin");
		System.out.println(json1);
		System.out.println(quickSetting.pushMessage(json1));
	}
}
