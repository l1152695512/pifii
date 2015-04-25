package com.yf.interfaces.commons;

import java.sql.Date;
import java.text.SimpleDateFormat;

import com.yf.util.baidupush.BaiduPush;

public class Test {
	public static void main1(String[] args) {

//		URL url = null;
//		HttpURLConnection url_con = null;
//		String urlStr = "https://www.bitstamp.net/api/ticker";
////		String urlStr = "http://192.168.1.228:8088/GreenEnergySys/phoneServlet";
//		try {
//			url = new URL(urlStr);
//			//*********************封装参数*****************
//			JSONObject dataJson = new JSONObject();
//			dataJson.put("ACCOUNT", "admin111");
//			dataJson.put("PASSWORD", "1234");
//			
//			JSONObject json = new JSONObject();
////			json.put("tradeCode", 11013);
////			json.put("data", dataJson);
//			//*********************封装参数end*****************
//			
//			
//			String param = json.toString();
//			url_con = (HttpURLConnection) url.openConnection();
//			url_con.setRequestMethod("GET");
//			url_con.setDoOutput(true);
//			url_con.setDoInput(true);
//			url_con.setUseCaches(false);
//			url_con.setConnectTimeout(10 * 1000);
//			url_con.setRequestProperty("Content-Type","text/json; charset=UTF-8");
//			byte[] entity = param.getBytes("UTF-8");
//			//url_con.setRequestProperty("Content-Length", String.valueOf(entity.length));
//			url_con.setRequestProperty("aaa", "wecome");
//			url_con.getOutputStream().write(entity);
//			url_con.getOutputStream().flush();
//			url_con.getOutputStream().close();
//			//*********************接收返回*****************
//			InputStream in = url_con.getInputStream();
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			byte[] buffer = new byte[1024];
//			int len = -1;
//			while ((len = in.read(buffer)) != -1) {
//				out.write(buffer, 0, len);
//			}
//			out.close();
//			in.close();
//			
//			byte[] data = out.toByteArray();
//			System.out.println("返回数据："+new String(data, "UTF-8"));
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (url_con != null){
//				url_con.disconnect();
//			}
//		}
		
		String USERID = "889145612219660799";
		String CHANNELID = "3622979773711745527";

		String API_KEY = "7ODGhUAWz3iqbOQxTcafeKM1";
		String SECRIT_KEY = "uGSYwP8gTGHjtyVsaOen9s92R0MSGfPL";
		
		BaiduPush baiduPush = new BaiduPush(BaiduPush.HTTP_METHOD_POST, SECRIT_KEY, API_KEY);
		System.out.println(baiduPush.VerifyBind(USERID, null));
//		baiduPush.PushNotify("Hello", "hello 1111world", USERID);
		baiduPush.PushMessage("hello world", USERID);
		System.out.println(baiduPush.QueryBindlist(USERID, CHANNELID));
		baiduPush.SetTag("tag3",USERID);
		System.out.println(baiduPush.QueryUserTag(USERID));
//		baiduPush.FetchTag();
	}
	
	
	public static void main(String[] args) throws Exception {
////		String urlString = "https://www.bitstamp.net/api/ticker";
////		HttpRequester hr = new HttpRequester();
////		HttpRespons ttt = hr.sendGet(urlString);
////		System.out.println(ttt.getContentEncoding());
//		
//		String USERID = "889145612219660799";
//		String CHANNELID = "3622979773711745527";
//
//		String API_KEY = "7ODGhUAWz3iqbOQxTcafeKM1";
//		String SECRIT_KEY = "uGSYwP8gTGHjtyVsaOen9s92R0MSGfPL";
//		
//		BaiduPush baiduPush = new BaiduPush(BaiduPush.HTTP_METHOD_POST, SECRIT_KEY, API_KEY);
////		System.out.println(baiduPush.VerifyBind(USERID, null));
////		baiduPush.PushNotify("Hello", "hello 1111world", USERID);
//		baiduPush.PushMessage("hello world", USERID);
//		System.out.println(baiduPush.QueryBindlist(USERID, CHANNELID));
////		baiduPush.SetTag("tag3",USERID);
////		System.out.println(baiduPush.QueryUserTag(USERID));
////		baiduPush.FetchTag();
		
		
		 Date date = new Date(Long.valueOf("1371105039755497"));  
		    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");  
		    String time = format.format(date);  
		    System.out.println(time);
	}
}