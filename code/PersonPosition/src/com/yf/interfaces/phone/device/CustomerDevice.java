package com.yf.interfaces.phone.device;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.yf.tradecontrol.GlobalVar;
import com.yf.tradecontrol.JDomHandler;
import com.yf.util.dbhelper.DBHelper;

public class CustomerDevice{
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private JDomHandler domHandler = new JDomHandler();
	private static final String xmlpath = GlobalVar.WORKPATH + File.separator
			+ "config" + File.separator + "dsSystemConfig.xml";
	
	public JSONObject getHouseArea(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		domHandler.loadXmlByPath(xmlpath);
		String sql = "select H_ID,H_NAME,REMARK,PHOTO_URL from BP_HOUSE_TBL where ACCOUNT='"+json.getJSONObject("data").getString("ACCOUNT")+"'";
		List list = dbhelper.getMapListBySql(sql);
		if(null != list){
			JSONArray jsonArray = new JSONArray();
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				JSONObject obj = new JSONObject();
				String loadPath="";
				try {
					loadPath = domHandler.getNodeValue("/ds-config/service/serveradd");
				} catch (Exception e) {
					e.printStackTrace();
				}
				String url=map.get("PHOTO_URL")==null?"":loadPath.trim()+map.get("PHOTO_URL").toString().trim();
				obj.put("PHOTO_URL", url);
				obj.put("H_ID", map.get("H_ID")==null?"":map.get("H_ID").toString());
				obj.put("H_NAME", map.get("H_NAME")==null?"":map.get("H_NAME").toString());
				obj.put("REMARK", map.get("REMARK")==null?"":map.get("REMARK").toString());
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

	public JSONObject getHouseDevice(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		domHandler.loadXmlByPath(xmlpath);
		StringBuffer sql = new StringBuffer();
		sql.append("select d.D_ID,d.D_TYPE,d.D_NAME,d.STATUS,d.START_TIME,d.IS_OPEN,d.PHOTO_URL,dt.TYPE,dt.NAME,dt.VOLTAGE,dt.POWER,dt.ECURRENT  ");
		sql.append("from BP_DEVICE_TBL d left join BP_DEVICE_TYPE_TBL dt on d.D_TYPE=dt.DT_ID where d.H_ID='"+json.getJSONObject("data").getString("H_ID")+"'") ; 
		List list = dbhelper.getMapListBySql(sql.toString());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(null != list){
			JSONArray jsonArray = new JSONArray();
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				JSONObject obj = new JSONObject();
				String loadPath="";
				try {
					loadPath = domHandler.getNodeValue("/ds-config/service/serveradd");
				} catch (Exception e) {
					e.printStackTrace();
				}
				String url=map.get("PHOTO_URL")==null?"":loadPath.trim()+map.get("PHOTO_URL").toString().trim();
				obj.put("PHOTO_URL", url);
				obj.put("D_ID", map.get("D_ID")==null?"":map.get("D_ID").toString());
				obj.put("D_TYPE", map.get("D_TYPE")==null?"":map.get("D_TYPE").toString());
				obj.put("D_NAME", map.get("D_NAME")==null?"":map.get("D_NAME").toString());
				obj.put("STATUS", map.get("STATUS")==null?"0":map.get("STATUS").toString());
				try {
					obj.put("START_TIME", sdf.format(sdf.parse(map.get("START_TIME").toString())));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				obj.put("IS_OPEN", map.get("IS_OPEN")==null?"0":map.get("IS_OPEN").toString());
				obj.put("TYPE", map.get("TYPE")==null?"0":map.get("TYPE").toString());
				obj.put("NAME", map.get("NAME")==null?"":map.get("NAME").toString());
				obj.put("VOLTAGE", map.get("VOLTAGE")==null?"0":map.get("VOLTAGE").toString());
				obj.put("POWER", map.get("POWER")==null?"0":map.get("POWER").toString());
				obj.put("ECURRENT", map.get("ECURRENT")==null?"0":map.get("ECURRENT").toString());
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
	
	public JSONObject getCustomerDevice(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		domHandler.loadXmlByPath(xmlpath);
		String loadPath="";
		try {
			loadPath = domHandler.getNodeValue("/ds-config/service/serveradd");
		} catch (Exception e) {
			e.printStackTrace();
		}
		String sql = "select H_ID,H_NAME,REMARK,PHOTO_URL from BP_HOUSE_TBL where ACCOUNT='"+json.getJSONObject("data").getString("ACCOUNT")+"'";
		List list = dbhelper.getMapListBySql(sql);
		if(null != list){
			JSONArray jsonArray = new JSONArray();
			for(int i=0;i<list.size();i++){
				Map map = (Map)list.get(i);
				JSONObject obj = new JSONObject();
				String hurl=map.get("PHOTO_URL")==null?"":loadPath.trim()+map.get("PHOTO_URL").toString().trim();;
				obj.put("PHOTO_URL", hurl);
				obj.put("H_ID", map.get("H_ID")==null?"":map.get("H_ID").toString());
				obj.put("H_NAME", map.get("H_NAME")==null?"":map.get("H_NAME").toString());
				obj.put("REMARK", map.get("REMARK")==null?"":map.get("REMARK").toString());
				StringBuffer sqlString = new StringBuffer();
				sqlString.append("select d.D_ID,d.D_TYPE,d.D_NAME,d.STATUS,d.START_TIME,d.IS_OPEN,d.PHOTO_URL,dt.TYPE,dt.NAME,dt.VOLTAGE,dt.POWER,dt.ECURRENT  ");
				sqlString.append("from BP_DEVICE_TBL d left join BP_DEVICE_TYPE_TBL dt on d.D_TYPE=dt.DT_ID where d.H_ID='"+map.get("H_ID").toString()+"'") ; 
				List device = dbhelper.getMapListBySql(sqlString.toString());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(null != device){
					JSONArray array = new JSONArray();
					int power=0;
					for(int j=0;j<device.size();j++){
						Map mapDevice = (Map)device.get(j);
						JSONObject object = new JSONObject();
						String durl=mapDevice.get("PHOTO_URL")==null?"":loadPath.trim()+map.get("PHOTO_URL").toString().trim();;
						obj.put("PHOTO_URL", durl);
						object.put("D_ID", mapDevice.get("D_ID")==null?"":mapDevice.get("D_ID").toString());
						object.put("D_TYPE", mapDevice.get("D_TYPE")==null?"":mapDevice.get("D_TYPE").toString());
						object.put("D_NAME", mapDevice.get("D_NAME")==null?"":mapDevice.get("D_NAME").toString());
						object.put("STATUS", mapDevice.get("STATUS")==null?"0":mapDevice.get("STATUS").toString());
						try {
							object.put("START_TIME", sdf.format(sdf.parse(mapDevice.get("START_TIME").toString())));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						object.put("IS_OPEN", mapDevice.get("IS_OPEN")==null?"0":mapDevice.get("IS_OPEN").toString());
						object.put("TYPE", mapDevice.get("TYPE")==null?"0":mapDevice.get("TYPE").toString());
						object.put("NAME", mapDevice.get("NAME")==null?"":mapDevice.get("NAME").toString());
						object.put("VOLTAGE", mapDevice.get("VOLTAGE")==null?"0":mapDevice.get("VOLTAGE").toString());
						object.put("POWER", mapDevice.get("POWER")==null?"0":mapDevice.get("POWER").toString());
						object.put("ECURRENT", mapDevice.get("ECURRENT")==null?"0":mapDevice.get("ECURRENT").toString());
						power+=Integer.parseInt(object.getString("POWER"));
						array.add(object);
					}
					obj.put("DEVICE", array);
					obj.put("TOTAL_POWER",String.valueOf(power)+"w");
				}
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
	
	public static void main(String[] args) {
		CustomerDevice customerDevice=new CustomerDevice();
		JSONObject json = new JSONObject();
		json.put("tradeCode", 2101);
		JSONObject jObject=new JSONObject();
		jObject.put("ACCOUNT", "admin");
		json.put("data", jObject);
		json.put("authCode", "admin");
		System.out.println(json);
		System.out.println(customerDevice.getHouseArea(json));
		System.out.println(customerDevice.getHouseArea(json).getJSONArray("data"));
		for(int i=0;i<customerDevice.getHouseArea(json).getJSONArray("data").size();i++){
			System.out.println(customerDevice.getHouseArea(json).getJSONArray("data").getJSONObject(i));
		}
		
		JSONObject json1 = new JSONObject();
		json1.put("tradeCode", 2102);
		JSONObject jObject1=new JSONObject();
		jObject1.put("H_ID", "111");
		json1.put("data", jObject1);
		json1.put("authCode", "admin");
		System.out.println(json1);
		System.out.println(customerDevice.getHouseDevice(json1));
		System.out.println(customerDevice.getHouseDevice(json1).getJSONArray("data"));
		for(int i=0;i<customerDevice.getHouseDevice(json1).getJSONArray("data").size();i++){
			System.out.println(customerDevice.getHouseDevice(json1).getJSONArray("data").getJSONObject(i));
		}
		
		JSONObject json2 = new JSONObject();
		json2.put("tradeCode", 2103);
		JSONObject jObject2=new JSONObject();
		jObject2.put("ACCOUNT", "admin");
		json2.put("data", jObject2);
		json2.put("authCode", "admin");
		System.out.println(json2);
		System.out.println(customerDevice.getCustomerDevice(json2));
		System.out.println(customerDevice.getCustomerDevice(json2).getJSONArray("data"));
	}
}
