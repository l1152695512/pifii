package com.yf.interfaces.phone.main;

import java.io.File;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;

import com.yf.tradecontrol.GlobalVar;
import com.yf.tradecontrol.JDomHandler;
import com.yf.tradecontrol.JDomHandlerException;
import com.yf.util.dbhelper.DBHelper;

public class FirstPage {

	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static Logger logger = Logger.getLogger(FirstPage.class);
	private JDomHandler domHandler = new JDomHandler();
	private static final String xmlpath = GlobalVar.WORKPATH + File.separator
			+ "config" + File.separator + "dsSystemConfig.xml";
	
	public JSONObject getCustomerJSON(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		domHandler.loadXmlByPath(xmlpath);
		String sql = "select ACCOUNT,PASSWORD,C_NAME,USER_IMG,USER_GRADE,FAMILY_IMG,FAMILY_ADDRESS,USER_SIGN from BP_CUSTOMER_TBL where ACCOUNT='"+json.getJSONObject("data").getString("ACCOUNT")+"'";
		List list = dbhelper.getMapListBySql(sql);
		if(list.isEmpty()){
			resultObject.put("returnCode", 401);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端请求数据失败");
		}
		else{
			Map map = (Map)list.get(0);
			JSONObject obj = new JSONObject();
			String loadPath="";
			try {
				loadPath = domHandler.getNodeValue("/ds-config/service/serveradd");
			} catch (Exception e) {
				e.printStackTrace();
			}
			String userUrl=map.get("USER_IMG")==null?"":loadPath.trim()+map.get("USER_IMG").toString().trim();
			String familyUrl=map.get("FAMILY_IMG")==null?"":loadPath.trim()+map.get("FAMILY_IMG").toString().trim();
			obj.put("USER_IMG", userUrl);
			obj.put("FAMILY_IMG", familyUrl);
			obj.put("ACCOUNT", map.get("ACCOUNT")==null?"":map.get("ACCOUNT").toString());
			obj.put("PASSWORD", map.get("PASSWORD")==null?"":map.get("PASSWORD").toString());
			obj.put("C_NAME", map.get("C_NAME")==null?"":map.get("C_NAME").toString());
			obj.put("USER_GRADE", map.get("USER_GRADE")==null?"":map.get("USER_GRADE").toString());
			obj.put("FAMILY_ADDRESS", map.get("FAMILY_ADDRESS")==null?"":map.get("FAMILY_ADDRESS").toString());
			obj.put("USER_SIGN", map.get("USER_SIGN")==null?"":map.get("USER_SIGN").toString());
			JSONArray array=new JSONArray();
			JSONObject obj1=new JSONObject();			
			obj1.put("ID", 101);
			obj1.put("DETAIL", "完成了一天用电少于30度的任务");
			obj1.put("TIME", "2013-05-21");
			array.add(obj1);
			JSONObject obj2=new JSONObject();
			obj2.put("ID", 102);
			obj2.put("DETAIL", "生活状态达到仙界水平");
			obj2.put("TIME", "2013-05-21");
			array.add(obj2);
			JSONObject obj3=new JSONObject();
			obj3.put("ID", 103);
			obj3.put("DETAIL", "刚刚提交了5条远程控制指令");
			obj3.put("TIME", "2013-05-21");
			array.add(obj3);
			JSONObject obj4=new JSONObject();
			obj4.put("ID", 104);
			obj4.put("DETAIL", "刚刚下载了新的生活及环保评价");
			obj4.put("TIME", "2013-05-21");
			array.add(obj4);
			obj.put("ITEMS", array);
			resultObject.put("returnCode", 200);
			resultObject.put("data", obj);
			resultObject.put("desc", "客户端操作成功");
		}
		return resultObject;
	}
	
	public static void main(String[] args) {
		FirstPage firstPage=new FirstPage();
		JSONObject json = new JSONObject();
		json.put("tradeCode", 4101);
		JSONObject jObject=new JSONObject();
		jObject.put("ACCOUNT", "admin");
		json.put("data", jObject);
		json.put("authCode", "admin");
		System.out.println(json);
		System.out.println(firstPage.getCustomerJSON(json));
		System.out.println(firstPage.getCustomerJSON(json).getJSONObject("data"));
		System.out.println(firstPage.getCustomerJSON(json).getJSONObject("data").getJSONArray("ITEMS"));
	}
}
