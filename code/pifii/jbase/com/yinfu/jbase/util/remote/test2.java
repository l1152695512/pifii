package com.yinfu.jbase.util.remote;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class test2 {
	public static ExecutorService threadPool = Executors.newCachedThreadPool();
	
//	public static void main(String[] args) throws Exception{
//		Logger.shutdown();
//		SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String thisStr = "";
//		while(true){
//			HttpClient httpClient = new HttpClient();
//			httpClient.getParams().setContentCharset("UTF-8");
//			String url = "http://223.82.251.49/jxsqt/routerPassList?routersn=0100860000004967";
//			GetMethod getMethod=new GetMethod(url);
//			httpClient.executeMethod(getMethod);
//			String res = getMethod.getResponseBodyAsString();
//			if(!thisStr.equals(res)){
//				System.err.println(fm.format(new Date())+":"+res);
//				thisStr = res;
//			}
//			Thread.sleep(1000);
//		}
//	}
	
	
	 public static void main(String[] args) throws Exception{
//		 String imgDir = "logo";
//		 String[] img = imgDir.split("/");
//	     imgDir = img[img.length-1];
//	     System.out.println(imgDir);
//		 getToken("12345678");
//		 deviceinfo("12345678");
//		 systat_get("12345678");
//		 geturl("de61ad4e1312ef13@pifii.com","12345678");
//		 router_def_location_get("f128b86eeddb7029@pifii.com","88888888");
//		 router_def_location_set("de61ad4e1312ef13@pifii.com","12345678");
//		 wifi_client_list("e702dd54f3f6c947@pifii.com","12345678");
//		 setURL("88888888");//设置上报地址
//		 authurl_get("88888888");//设置认证地址
//{ "ad": "http://m.pifii.com/ifidc/pifii.html", 
		 //"auth": "http://www.pifii.com:8090/home/authorizeAccess" }
//		 authurl_set("88888888");
//		 
//		 routerHelper("82af2788db9e081e@pifii.com","88888888");
//		 logserver_get("88888888");
//		 logserver_set("12345678");
//		 def_location_get("88888888");
		// def_location_set("12345678");
//		 timeout_get("12345678");
//		 timeout_set("12345678");
//		 getWhitelist("f128b86eeddb7029@pifii.com","88888888");
//		 getWhitelist("f6c4a2dfd76fbcbc@pifii.com","88888888");
//		 getWhitelist("226473120f290bee@pifii.com","12345678");
//		 getPasslist("12345678");
//		 routerHelper("f6c4a2dfd76fbcbc@pifii.com","88888888");
//		 deviceInfo("b404c23006ccd974@pifii.com","12345678");
//		 passlist_set("b404c23006ccd974@pifii.com","12345678");
		 //38:bc:1a:00:6b:f5
		 //e8:bb:a8:70:ae:ef
//		 threadPool.execute(new AddWhiteList("88:e3:ab:b8:8b:d9",180,"cmcc4@pifii.com","123456"));
//		 sysData2("12345678","http://www.pifii.com/app/PiFiiHome.apk","/storageroot/data/mb/img");
//		 status("12345678");
//		 mac();
//		 fileList("0100860000004307@pifii.com","88888888","/");
		 
		 String token = RouterHelper.routerToken("0100860000002811@pifii.com","88888888");//2014@pifii.com-yinfu
//		 System.err.println(RouterHelper.whitelistGet(token)); 
		 
//		 NameValuePair[] params = { new NameValuePair("token", token),
//					new NameValuePair("domain", "weixin.qq.com"),new NameValuePair("domain", "www.pifii.com"),
//					new NameValuePair("domain", "api.map.baidu.com"),new NameValuePair("ip", "113.106.98.233")};
////		 NameValuePair[] params = { new NameValuePair("token", token)};
//		 System.err.println(RouterHelper.whitelistSet(params));
		 System.err.println(RouterHelper.whitelistGet(token));
		 
//		 System.err.println(RouterHelper.sys_hostname_set("jx-e-post",token));
//		 System.err.println(RouterHelper. passlistGet(token));
		 
//		 System.err.println(RouterHelper. setPrivoxy(token,"n"));
//		 System.err.println(RouterHelper. setQos(token,false,100,100));
		 
//		 JSONObject obj = JSONObject.parseObject(RouterHelper. setQos(token,false,100,100));
//		 System.err.println(obj.get("result").toString());
		 
//		 System.err.println(RouterHelper. getPrivoxy(token));
//		 System.err.println(RouterHelper. getQos(token));
	 }
	 
	public static void getURL(String pass){
			String token = getToken(pass);
			if(!"".equals(token)){
				HttpRequester http = new HttpRequester();
				String url = "http://192.168.10.1/cgi-bin/luci/api/0/pifiibox/getURL?token="+token;
				try {
					HttpRespons hr = http.sendPost(url);
					System.out.println(hr.getContent());
				} catch (IOException e) {
				}
			}else{
				System.out.println("查询失败");
			}
		}
	
	public static void setURL(String pass){
		String token = getToken(pass);
		if(!"".equals(token)){
			HttpRequester http = new HttpRequester();
			Map param = new HashMap();
			param.put("seturl", "http://113.106.98.233/pifii");
			
			String url = "http://192.168.10.1/cgi-bin/luci/api/0/pifiibox/setURL?token="+token;
			try {
				HttpRespons hr = http.sendGet(url,param);
				System.out.println(hr.getContent());
			} catch (IOException e) {
			}
		}else{
			System.out.println("查询失败");
		}
	}
	 
	 public static void router_def_location_get(String email,String pass){
		 String token = RouterHelper.routerToken(email, pass);
		 String backData = RouterHelper.def_location_get(token);
		 System.out.println(backData);
	}
	 
	 public static void router_def_location_set(String email,String pass){
		 String token = RouterHelper.routerToken(email, pass);
		 String backData = RouterHelper.def_location_set(token,"http://m.pifii.com/ifidc/pifii.html");
		 System.out.println(backData);
	}
	 
	 public static void geturl(String email,String pass){
		 String token = RouterHelper.routerToken(email, pass);
		 String backData = RouterHelper.authurlGet(token);
		 System.out.println(backData);
	}
	 
	 public static void wifi_client_list(String email,String pass){
		 String token = RouterHelper.routerToken(email, pass);
		 String backData = RouterHelper.wifiClientList(token);
		 System.out.println(backData);
	}
	 
	 public static void authurl_get(String email,String pass){
		 String token = RouterHelper.routerToken(email, pass);
		 String backData = RouterHelper.authurlGet(token);
		 System.out.println(backData);
	}
	 
	 public static void mac(){
		 HttpRequester http = new HttpRequester();
		 HttpRespons hr;
		 String urlString = "http://mac.51240.com/54-ae-27-7c-4c-65__mac/";
		try {
			hr = http.sendGet(urlString);
			 System.out.println(hr.getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
	 
	 public static void getPasslist(String pass){
		 String token = getToken(pass);
		 String urlString = "http://192.168.10.1/cgi-bin/luci/api/0/ifidc/passlist_get?token="+token;
		 HttpRequester http = new HttpRequester();
		 HttpRespons hr;
		try {
			hr = http.sendPost(urlString);
			 System.out.println(hr.getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		 
	 }
	 
	 public static void status(String pass){
		 String token = getToken(pass);
		 JSONObject setJson = new JSONObject();
		 setJson.put("cmd", "status");
		 JSONObject option = new JSONObject();

		 setJson.put("param", option);
		 Map params = new HashMap();
		 params.put("json", setJson.toString());
		 
		 String urlString = "http://192.168.10.1/cgi-bin/luci/api/0/extension/ext_download?token="+token;
		 HttpRequester http = new HttpRequester();
		 HttpRespons hr;
		try {
			hr = http.sendPost(urlString, params);
			 System.out.println(hr.getContent());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		 
	 }
	 
	 public static void passlist_set(String email,String pass){
		String token = RouterHelper.routerToken(email, pass);
		String getData = RouterHelper.passlistGet(token);
		JSONObject json = JSONObject.parseObject(getData);
		JSONArray array = null;
		if (json.containsKey("member")) {
			getData = json.getString("member");
			array = JSONArray.parseArray(getData);
		} else {
			array = new JSONArray();
		}
		//00:e0:4c:19:25:84 
		array = new JSONArray();
		array.add("24:0a:64:1e:3b:6c");
		array.add("00:e0:4c:19:25:84");
		NameValuePair[] params = new NameValuePair[array.size() + 1];
		for (int i = 0; i < array.size(); i++) {
			params[i] = new NameValuePair("member", array.get(i).toString());
		}
		params[array.size()] = new NameValuePair("token", token);
		RouterHelper.passlistSet(token, params);
	 }
	 
	 public static void systat_get(String pass){
		 String token = getToken(pass);
			if(!"".equals(token)){
				HttpRequester http = new HttpRequester();
				String url = "http://192.168.1.1/cgi-bin/luci/api/0/module/systat_get?token="+token;
				try {
					HttpRespons hr = http.sendPost(url);
					System.out.println(hr.getContent());
				} catch (IOException e) {
				}
			}else{
				System.out.println("查询失败");
			}
	 }
	 
	 public static void deviceinfo(String pass){
		 String token = getToken(pass);
			if(!"".equals(token)){
				HttpRequester http = new HttpRequester();
				String url = "http://192.168.10.1/cgi-bin/luci/api/0/common/deviceinfo?token="+token;
				try {
					HttpRespons hr = http.sendPost(url);
					System.out.println(hr.getContent());
				} catch (IOException e) {
				}
			}else{
				System.out.println("查询失败");
			}
	 }
	
	public boolean sysData(String userName,String password,String sourcePath,String savePath){
		boolean flag = false;
		String token = RouterHelper.routerToken(userName, password);
		
		JSONObject setJson = new JSONObject();
		setJson.put("cmd", "setdownopt");

		JSONObject setObj = new JSONObject();
		setObj.put("down-speed", "0");
		setObj.put("errcode", "0");
		setObj.put("up-speed", "51200");
		setObj.put("seed-time", "60");
		setObj.put("up-each", "20480");
		setObj.put("max-down", "3");
		setObj.put("down-dir", savePath);
		setObj.put("down-each", "0");
		
		JSONObject option = new JSONObject();
		option.put("option", setObj);

		setJson.put("param", option);
		String backSetData = RouterHelper.download(setJson.toString(),token);
		JSONObject bkSetJson = JSONObject.parseObject(backSetData);
		
		if (bkSetJson.containsKey("errcode") && "0".equals(bkSetJson.getString("errcode"))) {
			JSONObject json = new JSONObject();
			json.put("cmd", "add");
			String[] array = new String[] { URLEncoder.encode(sourcePath) };
			JSONObject obj = new JSONObject();
			obj.put("link", array);
			obj.put("islocal", false);
			json.put("param", obj);
			String backData = RouterHelper.download(json.toString(), token);
			JSONObject bkJson = JSONObject.parseObject(backData);
			if (bkJson.containsKey("errcode") && "0".equals(bkJson.getString("errcode"))) {
				flag = true;
			}
		}
		
		return flag;
	}
	
	
	public static boolean sysData2(String password,String sourcePath,String savePath) throws Exception{
		boolean flag = false;
		String token = getToken(password);
		
		JSONObject setJson = new JSONObject();
		setJson.put("cmd", "setdownopt");

		JSONObject setObj = new JSONObject();
		setObj.put("down-speed", "0");
		setObj.put("errcode", "0");
		setObj.put("up-speed", "51200");
		setObj.put("seed-time", "60");
		setObj.put("up-each", "20480");
		setObj.put("max-down", "3");
		setObj.put("down-dir", savePath);
		setObj.put("down-each", "0");
		
		JSONObject option = new JSONObject();
		option.put("option", setObj);

		setJson.put("param", option);
		HttpRequester http = new HttpRequester();
		String url = "http://192.168.10.1/cgi-bin/luci/api/0/extension/ext_download?token="+token;
		Map map = new HashMap();
		map.put("json", setJson.toString());
		HttpRespons hr = http.sendPost(url, map);
		System.out.println(hr.getUrlString());	
		System.out.println("========="+hr.getContent());
		String backSetData = hr.getContent();
		JSONObject bkSetJson = JSONObject.parseObject(backSetData);
		
		if (bkSetJson.containsKey("errcode") && "0".equals(bkSetJson.getString("errcode"))) {
			JSONObject json = new JSONObject();
			json.put("cmd", "add");
			String[] array = new String[] { URLEncoder.encode(sourcePath) };
			JSONObject obj = new JSONObject();
			obj.put("link", array);
			obj.put("islocal", false);
			json.put("param", obj);
			
			Map map2 = new HashMap();
			map2.put("json", json.toString());
			String url2 = "http://192.168.10.1/cgi-bin/luci/api/0/extension/ext_download?token="+token;
			hr = http.sendPost(url2, map2);
			System.out.println("**********"+hr.getContent());
			System.out.println(hr.getUrlString());	
			JSONObject bkJson = JSONObject.parseObject(hr.getContent());
			if (bkJson.containsKey("errcode") && "0".equals(bkJson.getString("errcode"))) {
				flag = true;
			}
		}
		
		return flag;
	}
	
	
	public static String getToken(String pass){
		String token = "";
		HttpRequester http = new HttpRequester();
		Map map = new HashMap();
		map.put("pass", pass);
		try {
			HttpRespons hr = http.sendPost("http://192.168.10.1/cgi-bin/luci/api/0/account/login", map);
			System.out.println(hr.getContent());
			JSONObject json = JSONObject.parseObject(hr.getContent());
			if(json.containsKey("status") && "0".equals(json.getString("status")) && json.containsKey("token")){
				token = json.getString("token");
			}
		} catch (IOException e) {
		}
		return token;
	}
	
	public static void authurl_get(String pass){
		String token = getToken(pass);
		if(!"".equals(token)){
			HttpRequester http = new HttpRequester();
			String url = "http://192.168.10.1/cgi-bin/luci/api/0/ifidc/authurl_get?token="+token;
			try {
				HttpRespons hr = http.sendPost(url);
				System.out.println(hr.getContent());
			} catch (IOException e) {
			}
		}else{
			System.out.println("查询失败");
		}
	}
	
	public static void authurl_set(String pass){
		String token = getToken(pass);
		if(!"".equals(token)){
			HttpRequester http = new HttpRequester();
			Map param = new HashMap();
			param.put("auth", "http://113.106.98.233:8090/pifii/authorizeAccess");
			param.put("ad", "http://m.pifii.com/ifidc/pifii.html");
			//param.put("ad", "http://123.pifii.com/ifidc/pifii/pifii.html");
			
			String url = "http://192.168.10.1/cgi-bin/luci/api/0/ifidc/authurl_set?token="+token;
			try {
				HttpRespons hr = http.sendPost(url,param);
				System.out.println(hr.getContent());
			} catch (IOException e) {
			}
		}else{
			System.out.println("查询失败");
		}
		
	}
	
	public static void logserver_get(String pass){
		String token = getToken(pass);
		if(!"".equals(token)){
			HttpRequester http = new HttpRequester();
			String url = "http://192.168.10.1/cgi-bin/luci/api/0/ifidc/logserver_get?token="+token;
			try {
				HttpRespons hr = http.sendPost(url);
				System.out.println(hr.getContent());
			} catch (IOException e) {
			}
		}else{
			System.out.println("查询失败");
		}
		
	}
	
	public static void logserver_set(String pass){
		String token = getToken(pass);
		if(!"".equals(token)){
			HttpRequester http = new HttpRequester();
			Map param = new HashMap();
			param.put("port", "10000");
			param.put("ip", "113.106.98.60");

			String url = "http://192.168.10.1/cgi-bin/luci/api/0/ifidc/logserver_set?token="+token;
			try {
				HttpRespons hr = http.sendPost(url,param);
				System.out.println(hr.getContent());
			} catch (IOException e) {
			}
		}else{
			System.out.println("查询失败");
		}
		
	}
	
	
	public static void def_location_get(String pass){
		String token = getToken(pass);
		if(!"".equals(token)){
			HttpRequester http = new HttpRequester();
			String url = "http://192.168.10.1/cgi-bin/luci/api/0/ifidc/def_location_get?token="+token;
			try {
				HttpRespons hr = http.sendPost(url);
				System.out.println(hr.getContent());
			} catch (IOException e) {
			}
		}else{
			System.out.println("查询失败");
		}
		
	}
	
	public static void def_location_set(String pass){
		String token = getToken(pass);
		if(!"".equals(token)){
			HttpRequester http = new HttpRequester();
			Map param = new HashMap();
			param.put("url", "http://m.pifii.com/ifidc/pifii.html");

			String url = "http://192.168.10.1/cgi-bin/luci/api/0/ifidc/def_location_set?token="+token;
			try {
				HttpRespons hr = http.sendPost(url,param);
				System.out.println(hr.getContent());
			} catch (IOException e) {
			}
		}else{
			System.out.println("查询失败");
		}
		
	}
	
	public static void timeout_get(String pass){
		String token = getToken(pass);
		if(!"".equals(token)){
			HttpRequester http = new HttpRequester();
			String url = "http://192.168.10.1/cgi-bin/luci/api/0/ifidc/timeout_get?token="+token;
			try {
				HttpRespons hr = http.sendPost(url);
				System.out.println(hr.getContent());
			} catch (IOException e) {
			}
		}else{
			System.out.println("查询失败");
		}
		
	}
	
	public static void timeout_set(String pass){
		String token = getToken(pass);
		if(!"".equals(token)){
			HttpRequester http = new HttpRequester();
			Map param = new HashMap();
			param.put("timeout", "300");

			String url = "http://192.168.10.1/cgi-bin/luci/api/0/ifidc/timeout_set?token="+token;
			try {
				HttpRespons hr = http.sendPost(url,param);
				System.out.println(hr.getContent());
			} catch (IOException e) {
			}
		}else{
			System.out.println("查询失败");
		}
		
	}
	
	
	public static void senMsm(String phone,String code){
		HttpRequester http = new HttpRequester();
		Map<String,String> param = new HashMap<String,String>();
		param.put("account", "cf_gzyf");
		param.put("password", "2ZGH2v");
		param.put("mobile", phone);
		String verifyCode = code;
		param.put("content", "您的验证码是："+verifyCode+"。请不要把验证码泄露给其他人。");
		try {
			HttpRespons hr = http.sendPost("http://106.ihuyi.cn/webservice/sms.php?method=Submit",param);
			System.out.println(hr.getContent());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getWhitelist(String email,String pass){
		 String token = RouterHelper.routerToken(email, pass);
		 String backData = RouterHelper.whitelistGet(token);
		 System.out.println(backData);
	}
	
	
	public static void routerHelper(String email,String pass){
		 String token = RouterHelper.routerToken(email, pass);
		 String backData = RouterHelper.passlistGet(token);
		 System.out.println(backData);
	}
	
	public static void deviceInfo(String email,String pass){
		 String token = RouterHelper.routerToken(email, pass);
		 String backData = RouterHelper.deviceInfo(token);
		 System.out.println(backData);
	}
	
	public static void fileList(String email,String pass,String path){
//		List<Record> returnFiles = new ArrayList<Record>();
		String token = RouterHelper.routerToken(email, pass);
		String backData = RouterHelper.fileList(path,token);
		System.err.println(backData);
//		JSONObject obj = JSONObject.parseObject(backData);
//		if(null != obj && null!=obj.getJSONArray("contents")){
//			JSONArray files = obj.getJSONArray("contents");
//			Iterator<Object> ite = files.iterator();
//			while(ite.hasNext()){
//				JSONObject file = JSONObject.parseObject(ite.next().toString());
//				System.err.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(file.getLong("modified_lts")*1000)));
//				System.err.println(file.get("modified"));
//				System.err.println(file.getBooleanValue("is_dir"));
//				
//				String thisPath = file.get("path").toString();
//				returnFiles.add(new Record().set("icon", "").set("path", thisPath).set("name", thisPath.substring(thisPath.lastIndexOf("/")+1))
//						.set("size", file.get("size")).set("modified", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(file.getLong("modified_lts")*1000))));
//			}
//		}
	}
	
	 public static void main2(String[] args) { 
//		 getToken("12345678");
//		 
//		 authurl_get("12345678");
//		 authurl_set("12345678");
//		 
//		 logserver_get("12345678");
//		 logserver_set("12345678");
//		 
//		 def_location_get("12345678");
//		 def_location_set("12345678");
		 
		 
		 
		 
		 
		 
		 //http://192.168.2.1/cgi-bin/luci/syncboxlite/0/files/syncbox/data/download/030002130052302C42836E055EEB3EF64210D5-1A32-2326-51E7-D643D5856A7C.flv?dl=1&token=ab0031udfflgpttu
		 HttpRequester http = new HttpRequester();
		 try {
//			 Map<String,String> param = new HashMap<String,String>();
//			 param.put("mac", "EC:85:2F:6D:47:2E");
//			// param.put("option", "-");
//			 String urlString = "http://192.168.10.1/cgi-bin/luci/api/0/module/lan_kick_get?token=fe3459vzue0gaa6z";
//			 HttpRespons hr = http.sendGet(urlString);
//			 System.out.println(hr.getContent());
			 
			 //一个YFHttpClient操作实例
//			 YFHttpClient client = new YFHttpClient();
//			 
//			 NameValuePair[] params={new NameValuePair("token","fe39a6djbvqerngo")};
//			 //调用一个接口
//			 client.httpRouterGet(APIDefine.AUTHURL_GET, params);
//			 //调用另一个接口
//			 client.httpRouterGet(APIDefine.LAN_KICK_GET, params);
			 
			 
			 
			 
//			 Map<String,String> param = new HashMap<String,String>();
////			 param.put("mac", "08:57:00:0F:66:73");
////			 param.put("option", "-");
//			 String urlString = "http://192.168.10.1/cgi-bin/luci/syncboxlite/0/metadata/syncbox/data/music?token=ab0031h8mv3ssvlx&list=true";
//			 HttpRespons hr = http.sendGet(urlString);
//			 System.out.println(hr.getContent());
//			 
//				String token = RouterHelper.routerToken("cmcc1@pifii.com", "123456");
//				RouterHelper.passlistGet(token);
			 
			 //threadPool.execute(new AddWhiteList("08:57:00:0F:66:73","cmcc3@pifii.com", "123456"));
				
//				String markInfo = "60:d9:c7:82:d3:87";
//				
//				String getData = RouterHelper.passlistGet(token);
//				JSONObject json = JSONObject.fromObject(getData);
//				JSONArray array = null;
//				if(json.containsKey("member")){
//					getData = json.getString("member");
//					array  = JSONArray.fromObject(getData);
//				}else{
//					array = new JSONArray();
//				}
//				
//				array.add(markInfo);
//				NameValuePair[] params=new NameValuePair[array.size()+1];
//		        for (int i = 0; i < array.size(); i++) {
//					 params[i]=new NameValuePair("member",array.get(i).toString());
//				}
//		        params[array.size()]=new NameValuePair("token",token);
//		       
//				String setData = RouterHelper.passlistSet(token, params);
				
				
				//RouterHelper.passlistGet(token);
				
				//RouterHelper.log(token,"113.106.98.60","10000");
//				YFHttpClient client = new YFHttpClient();
//				RouterInfo info = client.routerInfo("cmcc1@pifii.com", "123456");
//				String token = info.getToken();

//				 JSONObject json=new JSONObject();
//				 json.put("cmd", "status");
//				 JSONObject obj=new JSONObject();
//				 json.put("param", obj);
//				 String backData = RouterHelper.download(json.toString(),token);
//				 JSONObject backJson = JSONObject.fromObject(backData);
//				 if(backJson.containsKey("errcode") && "0".equals(backJson.getString("errcode"))){
//					 if(backJson.containsKey("taskinfo")){
//						 JSONObject taskJson = backJson.getJSONObject("taskinfo");
//						 if(taskJson.containsKey("73ae3c5c1b16f2f4")){
//							 JSONObject bkJson = taskJson.getJSONObject("73ae3c5c1b16f2f4");
//							 System.out.println(bkJson.toString());
//						 }
//					 }
//				 }
				
				JSONObject setJson = new JSONObject();
				setJson.put("cmd", "setdownopt");

				JSONObject setObj = new JSONObject();
				setObj.put("down-speed", "0");
				setObj.put("errcode", "0");
				setObj.put("up-speed", "51200");
				setObj.put("seed-time", "60");
				setObj.put("up-each", "20480");
				setObj.put("max-down", "3");
				setObj.put("down-dir", "/storageroot/data/doc");
				setObj.put("down-each", "0");
				
				JSONObject option = new JSONObject();
				option.put("option", setObj);

				setJson.put("param", option);
				//String backSetData = RouterHelper.download(setJson.toString(),token);
				//JSONObject bkSetJson = JSONObject.fromObject(backSetData);
				
				
				//RouterHelper.wifi_macl_list_del("cfg077767",token);
				//RouterHelper.wifi_macl_basic_set("1", "0", token);
				//String getData = RouterHelper.wifi_macl_list_set("74:E5:0B:F4:C8:D0",token);
//				getData = JSONObject.fromObject(getData).getString("member");
//				JSONArray array = JSONArray.fromObject(getData);
//				array.add("D8:B0:4C:F3:47:49");
//				NameValuePair[] params=new NameValuePair[array.size()+1];
//		        for (int i = 0; i < array.size(); i++) {
//					 params[i]=new NameValuePair("member",array.get(i).toString());
//				}
//		        params[array.size()]=new NameValuePair("token",token);
//		       
//				String setData = RouterHelper.passlistSet(token, params);
			 
			 
			 
			 
			 
//			 String url = "http://192.168.10.1/cgi-bin/luci/api/0/extension/ext_download?token=ab0031h8mv3ssvlx";
//			 
//			 JSONObject json=new JSONObject();
//			 json.put("cmd", "add");
//			 String [] array=new String[]{"http://v.youku.com/v_show/id_XNjgyMzU1NzY4.html"};
//			 JSONObject obj=new JSONObject();
//			obj.put("link", array);
//			obj.put("islocal", false);
//			json.put("param", obj);
//			// String jsons = "{\\\"cmd\\\":\\\"add\\\",\\\"param\\\":{\\\"link\\\":[\\\"http://v.youku.com/v_show/id_XNjgzNTAyMzA4.html\\\"],\\\"islocal\\\":false}}";
//			 Map map = new HashMap();
//			 System.out.println(json.toString());
//			 map.put("json", json.toString());
//			 HttpRespons hrr = http.sendPost(url,map);
//			 System.out.println(hrr.getContent());
//			 
			
			 
			 
//			 String url = "http://192.168.2.1/cgi-bin/luci/syncboxlite/0/metadata/syncbox/data/download?token=ab0031udfflgpttu&list=true";
//			 HttpRespons hrr = http.sendGet(url);
//			 System.out.println(hrr.getContent());
			 //https://openapi.youku.com/v2/searches/show/top_unite.json?client_id=47cf22b310cb6c23&category=电影&genre=2004
			 
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
