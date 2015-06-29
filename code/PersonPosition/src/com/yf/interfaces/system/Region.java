package com.yf.interfaces.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class Region {
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	
	public JSONObject getProvinceJSON(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		JSONArray array=new JSONArray();
		Map map = new HashMap();
		String sql = "select RID,NAME from YW_REGION_TBL where PID is null";
		List list = dbhelper.getMapListBySql(sql);
		if(list.size() != 0){
			for (int i=0;i<list.size();i++) {
				JSONObject o=new JSONObject();
				map = (Map)list.get(i);
	    		o.put("PID",map.get("RID"));
	    		o.put("NAME",map.get("NAME"));
	    		array.add(o);	
			}
			resultObject.put("returnCode", 200);
			resultObject.put("data", array);
			resultObject.put("desc", "客户端操作成功");
		}else{
			resultObject.put("returnCode", 401);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端请求数据失败");
		}
		return resultObject;
	}
	
	public JSONObject getCityJSON(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		JSONArray array=new JSONArray();
		Map map = new HashMap();
		String sql = "select RID,NAME from YW_REGION_TBL where PID is not null";
		String proId=json.getJSONObject("data").getString("PROID");
		if(!StringUtils.isBlank(proId)){
			sql += " and PID='"+proId+"'";
		}
		List list = dbhelper.getMapListBySql(sql);
		if(list.size() != 0){
			for (int i=0;i<list.size();i++) {
				JSONObject o=new JSONObject();
				map = (Map)list.get(i);
	    		o.put("CID",map.get("RID"));
	    		o.put("NAME",map.get("NAME"));
	    		array.add(o);	
			}
			resultObject.put("returnCode", 200);
			resultObject.put("data", array);
			resultObject.put("desc", "客户端操作成功");
		}else{
			resultObject.put("returnCode", 401);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端请求数据失败");
		}
		return resultObject;
	}
	
	public JSONObject getAreaJSON(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		JSONArray array=new JSONArray();
		Map map = new HashMap();
		String sql = "select RID,NAME from YW_REGION_TBL where PID is not null";
		String cityId=json.getJSONObject("data").getString("CITYID");
		if(!StringUtils.isBlank(cityId)){
			sql += " and PID='"+cityId+"'";
		}
		List list = dbhelper.getMapListBySql(sql);
		if(list.size() != 0){
			for (int i=0;i<list.size();i++) {
				JSONObject o=new JSONObject();
				map = (Map)list.get(i);
	    		o.put("AID",map.get("RID"));
	    		o.put("NAME",map.get("NAME"));
	    		array.add(o);	
			}
			resultObject.put("returnCode", 200);
			resultObject.put("data", array);
			resultObject.put("desc", "客户端操作成功");
		}else{
			resultObject.put("returnCode", 401);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端请求数据失败");
		}
		return resultObject;
	}

}
