package com.yf.interfaces.phone.device;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.yf.tradecontrol.GlobalVar;
import com.yf.util.HibernateUUId;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;
import com.yf.util.dbhelper.PrivateUtil;

public class HouseDevice {
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static Logger logger = Logger.getLogger(HouseDevice.class);
	
	public JSONObject addHouseDevice(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		Map packParam = JsonUtils.parseJSON2Map(json.getJSONObject("data"));
		packParam.put("D_ID", new HibernateUUId().generate().toString());
		boolean bool=false;
		try {
			packParam.put("START_TIME",PrivateUtil.str2sqlDate(packParam.get("START_TIME").toString()));
			String inserSql = PrivateUtil.ctIstSl("BP_DEVICE_TBL", packParam);
			bool = dbhelper.insert(inserSql, PrivateUtil.delNullArray(packParam.values().toArray()));
		}catch (Exception e1) {
			logger.error("房屋区域编号："+json.getJSONObject("data").getString("H_ID")+"添加设备失败", e1);
		} 
		if(bool){
			resultObject.put("returnCode", 200);
			JSONObject obj=new JSONObject();
			obj.put("D_ID", packParam.get("D_ID"));
			resultObject.put("data", obj);
			resultObject.put("desc", "客户端操作成功");
		}else{
			resultObject.put("returnCode", 402);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端创建数据失败");
		}
		return resultObject;
	}
	
	public JSONObject deleteHouseDevice(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		JSONArray array=json.getJSONArray("data");
		List<String> sqlList=new ArrayList<String>();
		for(int i=0;i<array.size();i++){
			String dId=array.getJSONObject(i).getString("D_ID");
			String sql="delete from BP_DEVICE_TBL where D_ID='"+dId+"'";
			sqlList.add(sql);
		}
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
	
	public JSONObject updateHouseDevice(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		Map packParam = JsonUtils.parseJSON2Map(json.getJSONObject("data"));
		boolean bool=false;
		try {
			String str = PrivateUtil.ctutdSl("BP_DEVICE_TBL", packParam);	
			String updateSql = str+"where D_ID='"+json.getJSONObject("data").getString("D_ID")+"'";
			bool = dbhelper.update(updateSql, PrivateUtil.delNullArray(packParam.values().toArray()));
		} catch (Exception e) {
			logger.error("房屋区域设备："+json.getJSONObject("data").getString("D_ID")+"修改失败", e);
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
		HouseDevice houseDevice=new HouseDevice();
		JSONObject json=new JSONObject();
		json.put("tradeCode", 2301);
		JSONObject jObject=new JSONObject();
		jObject.put("H_ID", "222");
		jObject.put("D_TYPE", "dt001");
		jObject.put("D_NAME", "我的空调");
		jObject.put("STATUS", "1");
		jObject.put("START_TIME", "2013-05-20");
		jObject.put("IS_OPEN", "1");
		jObject.put("REMARK", "备注");
		jObject.put("PHOTO_URL", "C:/images");
		json.put("data", jObject);
		json.put("authCode", "admin");
		JSONObject obj=houseDevice.addHouseDevice(json);
		System.out.println(json);
		System.out.println(obj);
		System.out.println(obj.getJSONObject("data"));
		System.out.println(obj.getJSONObject("data").getString("D_ID"));
		
		JSONObject json1=new JSONObject();
		json1.put("tradeCode", 2302);
		JSONArray array=new JSONArray();
		JSONObject jObject1=new JSONObject();
		jObject1.put("D_ID", "402881ee3f120898013f120908600001");
		array.add(jObject1);
		JSONObject jObject2=new JSONObject();
		jObject2.put("D_ID", "402881ee3f120898013f1208980c0000");
		array.add(jObject2);
		json1.put("data", array);
		json1.put("authCode", "admin");
		System.out.println(json1);
		System.out.println(houseDevice.deleteHouseDevice(json1));
		System.out.println(houseDevice.deleteHouseDevice(json1).getJSONObject("data"));
		
		JSONObject json2=new JSONObject();
		json2.put("tradeCode", 2303);
		JSONObject jObject3=new JSONObject();
		jObject3.put("D_ID", "402881353ecadf71013ecadf71440000");
		jObject3.put("D_NAME", "我的设备");
		jObject3.put("D_TYPE", "dt001");
		jObject3.put("STATUS", "1");
		jObject3.put("REMARK", "大厅区域");
		jObject3.put("PHOTO_URL", "C:/images/");
		jObject3.put("H_ID", "111");
		json2.put("data", jObject3);
		json2.put("authCode", "admin");
		JSONObject jObject4=houseDevice.updateHouseDevice(json2);
		System.out.println(json2);
		System.out.println(jObject4);
		System.out.println(jObject4.getJSONObject("data"));
	}
}
