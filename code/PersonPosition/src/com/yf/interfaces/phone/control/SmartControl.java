package com.yf.interfaces.phone.control;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SmartControl {
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static Logger logger = Logger.getLogger(SmartControl.class);
	
	public JSONObject getDeviceJSON(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		StringBuffer sql = new StringBuffer();
		sql.append("select c.ACCOUNT,h.H_ID,h.H_NAME,h.PHOTO_URL HOUSEIMG,d.D_ID,d.D_NAME,d.START_TIME,d.IS_OPEN  ");
		sql.append("from BP_CUSTOMER_TBL c left join ( BP_HOUSE_TBL h left join BP_DEVICE_TBL d on h.H_ID=d.H_ID)  ");
		sql.append("on c.ACCOUNT=h.ACCOUNT where c.ACCOUNT='"+json.getJSONObject("data").getString("ACCOUNT")+"'") ; 
		List list = dbhelper.getMapListBySql(sql.toString());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(null != list){
			JSONArray jsonArray = new JSONArray();
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				JSONObject obj = new JSONObject();
				obj.put("ACCOUNT", map.get("ACCOUNT")==null?"":map.get("ACCOUNT").toString());
				obj.put("H_ID", map.get("H_ID")==null?"":map.get("H_ID").toString());
				obj.put("H_NAME", map.get("H_NAME")==null?"":map.get("H_NAME").toString());
				obj.put("HOUSEIMG", map.get("HOUSEIMG")==null?"":map.get("HOUSEIMG").toString());
				obj.put("D_ID", map.get("D_ID")==null?"":map.get("D_ID").toString());
				obj.put("D_NAME", map.get("D_NAME")==null?"":map.get("D_NAME").toString());
				try {
					obj.put("START_TIME", sdf.format(sdf.parse(map.get("START_TIME").toString())));
				} catch (ParseException e) {
					logger.error("日期转换出错", e);
				}
				obj.put("IS_OPEN", map.get("IS_OPEN")==null?"0":map.get("IS_OPEN").toString());
				jsonArray.add(obj);
			}
			resultObject.put("returnCode", 200);
			resultObject.put("data", jsonArray);
			resultObject.put("desc", "客户端操作成功");
		}else{
			resultObject.put("returnCode", 401);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端请求数据失败");
		}
		return resultObject;
	}
	
	public JSONObject controlDevice(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		String sql="update BP_DEVICE_TBL set IS_OPEN='"+json.getJSONObject("data").getString("IS_OPEN")+"' where D_ID='"+json.getJSONObject("data").getString("D_ID")+"'";
		boolean bool=dbhelper.update(sql);
		if(bool){
			JSONObject obj=new JSONObject();
			obj.put("IS_OPEN", json.getJSONObject("data").getString("IS_OPEN"));
			resultObject.put("returnCode", 200);
			resultObject.put("data", obj);
			resultObject.put("desc", "客户端操作成功");
		}else{
			resultObject.put("returnCode", 403);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端修改数据失败");
		}
		return resultObject;
	}
	
	public static void main(String[] args) {
		SmartControl smartControl=new SmartControl();
		JSONObject json = new JSONObject();
		json.put("tradeCode", 3101);
		JSONObject jObject=new JSONObject();
		jObject.put("ACCOUNT", "admin");
		json.put("data", jObject);
		json.put("authCode", "admin");
		System.out.println(json);
		System.out.println(smartControl.getDeviceJSON(json));
		System.out.println(smartControl.getDeviceJSON(json).getJSONArray("data"));
		for(int i=0;i<smartControl.getDeviceJSON(json).getJSONArray("data").size();i++){
			System.out.println(smartControl.getDeviceJSON(json).getJSONArray("data").getJSONObject(i));
		}
		
		JSONObject json1 = new JSONObject();
		json1.put("tradeCode", 3201);
		JSONObject jObject1=new JSONObject();
		jObject1.put("D_ID", "d001");
		jObject1.put("IS_OPEN", "2");
		json1.put("data", jObject1);
		json.put("authCode", "admin");
		System.out.println(json1);
		System.out.println(smartControl.controlDevice(json1));
		System.out.println(smartControl.controlDevice(json1).getJSONObject("data"));
	}
}
