package com.yf.interfaces.phone.device;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.yf.tradecontrol.GlobalVar;
import com.yf.util.HibernateUUId;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;
import com.yf.util.dbhelper.PrivateUtil;

public class HouseArea {
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static Logger logger = Logger.getLogger(HouseArea.class);
	
	public JSONObject addHouseArea(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		Map packParam = JsonUtils.parseJSON2Map(json.getJSONObject("data"));
		packParam.put("H_ID", new HibernateUUId().generate().toString());
		boolean bool=false;
		try {
			String inserSql = PrivateUtil.ctIstSl("BP_HOUSE_TBL", packParam);
			bool = dbhelper.insert(inserSql, PrivateUtil.delNullArray(packParam.values().toArray()));
		} catch (Exception e) {
			logger.error("客户账号："+json.getJSONObject("data").getString("H_ID")+"添加房屋区域失败", e);
		}
		if(bool){
			resultObject.put("returnCode", 200);
			JSONObject obj=new JSONObject();
			obj.put("H_ID", packParam.get("H_ID"));
			resultObject.put("data", obj);
			resultObject.put("desc", "客户端操作成功");
		}else{
			resultObject.put("returnCode", 402);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端创建数据失败");
		}
		return resultObject;
	}
	
	public JSONObject deleteHouseArea(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		List<String> sqlList=new ArrayList<String>();
		sqlList.add("delete from BP_HOUSE_TBL where H_ID='"+json.getJSONObject("data").getString("H_ID")+"'");
		sqlList.add("delete from BP_DEVICE_TBL where H_ID='"+json.getJSONObject("data").getString("H_ID")+"'");
		boolean bool=dbhelper.executeFor(sqlList);		
		if(bool){
			resultObject.put("returnCode", 200);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端操作成功");
		}else{
			resultObject.put("returnCode", 404);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端删除数据失败");
		}
		return resultObject;
	}
	
	public JSONObject updateHouseArea(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		Map packParam = JsonUtils.parseJSON2Map(json.getJSONObject("data"));
		boolean bool=false;
		try {
			String str = PrivateUtil.ctutdSl("BP_HOUSE_TBL", packParam);	
			String updateSql = str+"where H_ID='"+json.getJSONObject("data").getString("H_ID")+"'";
			bool = dbhelper.update(updateSql, PrivateUtil.delNullArray(packParam.values().toArray()));
		} catch (Exception e) {
			logger.error("房屋区域："+json.getJSONObject("data").getString("H_ID")+"修改失败", e);
		}
		if(bool){
			resultObject.put("returnCode", 200);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端操作成功");
		}else{
			resultObject.put("returnCode", 403);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端修改数据失败");
		}
		return resultObject;
	}
	
	public static void main(String[] args) {
		HouseArea houseArea=new HouseArea();
		JSONObject json=new JSONObject();
		json.put("tradeCode", 2201);
		JSONObject jObject=new JSONObject();
		jObject.put("ACCOUNT", "admin");
		jObject.put("H_NAME", "我的卧室");
		jObject.put("REMARK", "备注");
		jObject.put("PHOTO_URL", "C:/images/");
		json.put("data", jObject);
		json.put("authCode", "admin");
		JSONObject obj=houseArea.addHouseArea(json);
		System.out.println(json);
		System.out.println(obj);
		System.out.println(obj.getJSONObject("data"));
		System.out.println(obj.getJSONObject("data").getString("H_ID"));
		
		JSONObject json1=new JSONObject();
		json1.put("tradeCode", 2202);
		JSONObject jObject1=new JSONObject();
		jObject1.put("H_ID", "402881ee3f096357013f0963573f0000");
		json1.put("data", jObject1);
		json1.put("authCode", "admin");
		System.out.println(json1);
		System.out.println(houseArea.deleteHouseArea(json1));
		System.out.println(houseArea.deleteHouseArea(json1).getJSONObject("data"));
		
		JSONObject json2=new JSONObject();
		json2.put("tradeCode", 2203);
		JSONObject jObject2=new JSONObject();
		jObject2.put("H_ID", "402881353eca4650013eca46507a0000");
		jObject2.put("H_NAME", "大厅");
		jObject2.put("REMARK", "大厅区域");
		jObject2.put("PHOTO_URL", "C:/images/");
		json2.put("data", jObject2);
		json2.put("authCode", "admin");
		JSONObject jObject3=houseArea.updateHouseArea(json2);
		System.out.println(json2);
		System.out.println(jObject3);
		System.out.println(jObject3.getJSONObject("data"));
	}
}
