package com.yinfu.jbase.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class MS_FeieAPI {
	
	//CLIENT_CODE：终端编号9位,查看机身贴纸
	//KEY：在网站上注册，并绑定打印机即可生成
	
	public static final String CLIENT_CODE = "814070813";
	public static final String KEY = "0TAT2Na3";

	public static final String HOST = "http://115.28.225.82:80";
	
	//执行main函数即可测试
	public static void main(String[] args) throws Exception{
		
		
		 JSONArray array = new JSONArray();
		 JSONObject son = new JSONObject();
		 son.put("name", "炒粉");
		 son.put("money", "15.0");
		 son.put("amount", "1");
		 son.put("cost", "15.0");
		 array.add(son);
		 
		 JSONObject json = new JSONObject();
		 json.put("title", "都城");
		 json.put("list", array);
		 json.put("remark", "快点送到");
		 json.put("total", "15.0");
		 json.put("address", "天河北路光大银行2403");
		 json.put("phone", "13854545252");
		 json.put("orderTime", "2014-05-01 08:08:08");
		 json.put("qrcode", "www.feieyun.com");
		sendDefaultFormatOrderInfo(CLIENT_CODE,KEY,"1",json);
		
		//===========打印订单有如下两种方式，根据需要，任选其一即可=============
		//***返回的状态有如下几种***
		//{"reslutCode":0,"msg":"success"};
		//{"reslutCode":1,"msg":"终端编号错误"};
		//{"reslutCode":2,"msg":"订单保存失败"};
		//{"reslutCode":3,"msg":"订单内容太长"};
		
//		//1.发送默认格式订单(只需要把数据填写完毕即可，服务器会按照默认的格式将订单打印)
//			String result = sendDefaultFormatOrderInfo(CLIENT_CODE,KEY,"1");//第三个参数为打印次（联）数,默认为1
//			System.out.println(result);
		
		//2.发送自定义格式订单(订单格式和内容都由使用者拼凑好，服务器只管负责打印)-------推荐使用自定义格式
		
//		    String result = sendSelfFormatOrderInfo(CLIENT_CODE,KEY,"1");//第三个参数为打印次（联）数,默认为1
//			System.out.println(result);
		    
		
		//===========查询订单的信息============================================
		//***返回的状态有如下几种*** (print:已打印,waiting:未打印)
	 	//{"resultCode":0,"print":"xx","waiting":"xx"};
		//{"resultCode":1,"info":"请求参数错误"};
		
		//3.查询指定终端编号某天打印的订单详情(第一个参数为终端编号,第二个参数为时间),注意时间格式
//		String result = queryOrderNumbersByTime(CLIENT_CODE,"2014-08-11");
//		System.out.println(result);
		
		//===========查询打印机状态=============================================
		//***返回的状态有如下几种***
		//{"status":"clientCode错误"};
	    //{"status":"离线"};
	    //{"status":"在线,工作状态正常"};
	    //{"status":"在线,工作状态不正常"};
		
		//4.查询打印机状态（是否在线,工作状态是否正常）
//		String result = queryPrinterStatus(CLIENT_CODE);
//		System.out.println(result);
		
	}

	
	
	
	//=====================以下是函数实现部分================================================
	//查询打印机状态
	public static String queryPrinterStatus(String clientCode){
		   HttpGet get = new HttpGet(HOST+"/FeieServer/queryprinterstatus?clientCode="+clientCode);
	       HttpClient client = new DefaultHttpClient();
	       client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000); 
	       client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
	       InputStream is = null;
	       try{
	    	   HttpResponse response = client.execute(get);
	           int statecode = response.getStatusLine().getStatusCode();
	           if(statecode == 200){
	        	   HttpEntity httpentity = response.getEntity(); 
	 	           String strentity = null;
	 	            if(httpentity != null){
	 	            	is = httpentity.getContent();
	 	            	byte[] b = new byte[1024]; 
	 	            	int length = 0;
	 	            	StringBuilder sb = new StringBuilder();
	 	            	while((length=is.read(b))!= -1){
	 	            		sb.append(new String(b,0,length));
	 	            	}
	 	            	strentity = new String(sb.toString().getBytes(),"utf-8");
	 	            	 
	 	            	return strentity;
	 	            }
	 	            else{
	 	            	 return null;
	 	            }
	           }
	           else{
	        	   return null;
	           }
	       }
	       catch (Exception e) {
	    	   e.printStackTrace();
	       }
	       finally{
	    	    if(is!=null){
	    	    	try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
	    	    }
				if(get!=null){
					get.abort();
				}
		   }
	       return null;
	       
	}

	
	//按指定时间去查询指定终端编号的打印的订单情况,注意时间格式
	public static String queryOrderNumbersByTime(String clientCode,String strdate){
		   //通过POST请求，发送打印信息到服务器
		   HttpPost post = new HttpPost(HOST+"/FeieServer/queryorderinfo");
	       HttpClient client = new DefaultHttpClient();
	       client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000); 
	       client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
	       
	       List<NameValuePair> nvps = new ArrayList<NameValuePair>();
	       nvps.add(new BasicNameValuePair("clientCode",clientCode));
		   nvps.add(new BasicNameValuePair("date",strdate));
		   InputStream is = null;
	       try
	       {
	    	   post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
	       	   HttpResponse response = client.execute(post);
	           int statecode = response.getStatusLine().getStatusCode();
	           if(statecode == 200){
		           HttpEntity httpentity = response.getEntity(); 
	 	           String strentity = null;
	 	            if (httpentity != null){
	 	            	is = httpentity.getContent();
	 	            	byte[] b = new byte[1024]; 
	 	            	int length = 0;
	 	            	StringBuilder sb = new StringBuilder();
	 	            	while((length=is.read(b))!= -1){
	 	            		sb.append(new String(b,0,length));
	 	            	}
	 	            	strentity = sb.toString();
	 	            	is.close(); 
	 	            	return strentity;
	 	            }
	 	            else{
	 	            	 return null;
	 	            }
	             }else{
	            	 return null;
	             }
	           
	       }
	       catch (Exception e)
	       {
	    	   e.printStackTrace();
	       }
	       finally{
	    	   if(is!=null){
	    		   try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	   }
	    	   if(post !=null){
	    		   post.abort();
	    	   }
	       }
	       return null;
	}
	
	
	//发送默认格式订单信息
	public static String sendDefaultFormatOrderInfo(String clientCode,String strkey,String times,JSONObject orderInfo) {
		//将打印内容封装成JOSN字符串
		//例子：{"title":"大西豪餐饮连锁","list":[{"name:炒粉","money":"15.0","amount":"1","cost":"15.0"},{"name:鸡蛋汤","money":"12.0","amount":"2","cost":"24.0"}],"remark":"快点送到","total":"39.0元","address":"广州市天河区","phone":"133123123123","orderTime":"2014-05-01 08:08:08"}
		//字段解析
		//title:店名,list:菜单对象的数组(name:菜名,money:单价,amount:数量,cost：金额)
		//remark:备注,total:合计,address:送餐地址,phone:联系电话,orderTime:订餐时间
		//qrcode:二维码,如不需要二维码,填空字符“”即可
		
		//测试简体
//		String orderInfo = "{\"title\":\"大西豪餐饮连锁\",\"list\":[{\"name\":\"炒粉\",\"money\":\"15.0\",\"amount\":\"1\",\"cost\":\"15.0\"},{\"name\":\"鸡蛋汤\",\"money\":\"12.0\",\"amount\":\"2\",\"cost\":\"24.0\"}],\"remark\":\"快点送到\",\"total\":\"39.0元\",\"address\":\"广州市天河区\",\"phone\":\"133123123123\",\"orderTime\":\"2014-05-01 08:08:08\",\"qrcode\":\"www.feieyun.com\"}";
		
//		 JSONArray array = new JSONArray();
//		 JSONObject son = new JSONObject();
//		 son.put("name", "炒粉");
//		 son.put("money", "15.0");
//		 son.put("amount", "1");
//		 son.put("cost", "15.0");
//		 array.add(son);
//		 
//		 JSONObject json = new JSONObject();
//		 json.put("title", "都城");
//		 json.put("list", array);
//		 json.put("remark", "快点送到");
//		 json.put("total", "15.0");
//		 json.put("address", "天河北路光大银行2403");
//		 json.put("phone", "13854545252");
//		 json.put("orderTime", "2014-05-01 08:08:08");
//		 json.put("qrcode", "www.feieyun.com");
		
		
		
		//测试繁体
		//String orderInfo = "{\"title\":\"大西豪餐飲連鎖\",\"list\":[{\"name\":\"炒粉\",\"money\":\"15.0\",\"amount\":\"1\",\"cost\":\"15.0\"},{\"name\":\"雞蛋湯\",\"money\":\"12.0\",\"amount\":\"2\",\"cost\":\"24.0\"}],\"remark\":\"快點送到\",\"total\":\"39.0元\",\"address\":\"廣州市天河區\",\"phone\":\"133123123123\",\"orderTime\":\"2014-05-01 08:08:08\",\"qrcode\":\"www.feieyun.com\"}";
		
	    //通过POST请求，发送打印信息到服务器
	    HttpPost post = new HttpPost(HOST+"/FeieServer/printDefalutFormatOrder");
        HttpClient client = new DefaultHttpClient();
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000); 
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
       
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("clientCode",clientCode));
		nvps.add(new BasicNameValuePair("printInfo",orderInfo.toString()));
		nvps.add(new BasicNameValuePair("key",strkey));
		nvps.add(new BasicNameValuePair("printTimes",times));
		
	   InputStream is = null;
       try
       {
    	   post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
       	   HttpResponse response = client.execute(post);
           int statecode = response.getStatusLine().getStatusCode();
           if(statecode == 200){
	           HttpEntity httpentity = response.getEntity(); 
 	           String strentity = null;
 	            if (httpentity != null){
 	            	is = httpentity.getContent();
 	            	byte[] b = new byte[1024]; 
 	            	int length = 0;
 	            	StringBuilder sb = new StringBuilder();
 	            	while((length=is.read(b))!= -1){
 	            		sb.append(new String(b,0,length));
 	            	}
 	            	strentity = sb.toString();
 	            	is.close(); 
 	            	return strentity;
 	            }
 	            else{
 	            	 return null;
 	            }
             }else{
            	 return null;
             }
           
       }
       catch (Exception e)
       {
    	   e.printStackTrace();
       }
       finally{
    	   if(is!=null){
    		   try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	   }
    	   if(post !=null){
    		   post.abort();
    	   }
    	   
       }
       return null;
	  
	}

	//发送自定义格式订单信息
	public static String sendSelfFormatOrderInfo(String clientCode,String strkey,String times){
		//标签说明："<BR>"为换行符，"<CB></CB>"为居中放大，"<B></B>"为放大, "<QR></QR>"为二维码标签, "<L></L>"倍高
		
		//测试简体=============
		String orderInfo;
		orderInfo = "<CB>大西豪</CB><BR>";//标题字体如需居中放大,就需要用标签套上
		orderInfo += "名称　　　　　 单价  数量 金额<BR>";
		orderInfo += "--------------------------------<BR>";
		orderInfo += "番　　　　　　 1.0    1   1.0<BR>";
		orderInfo += "番茄　　　　　 10.0   10  10.0<BR>";
		orderInfo += "番茄炒　　　　 10.0   100 100.0<BR>";
		orderInfo += "番茄炒粉　　　 100.0  100 100.0<BR>";
		orderInfo += "番茄炒粉粉　　 1000.0 1   100.0<BR>";
		orderInfo += "番茄炒粉粉粉粉 100.0  100 100.0<BR>";
		orderInfo += "番茄炒粉粉粉粉 15.0   1   15.0<BR>";
		orderInfo += "备注：快点送到xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx<BR>";
		orderInfo += "--------------------------------<BR>";
		orderInfo += "合计：xx.0元<BR>";
		orderInfo += "送货地点：xxxxxxxxxxxxxxxxx<BR>";
		orderInfo += "联系电话：138000000000<BR>";
		orderInfo += "订餐时间：2011-01-06 19:30:10<BR>";
		orderInfo += "----------请扫描二维码----------";
		orderInfo += "<QR>www.feieyun.com</QR>";//这是二维码,把内容用标签套上即可
		orderInfo += "<BR>";
		orderInfo += "<BR>";

//测试繁体中文==================		
//		String orderInfo;
//		orderInfo = "<CB>大西豪</CB><BR>";//标题字体如需居中加粗,就需要用标签套上
//		orderInfo += "名稱　　　　　 單價  數量 金額<BR>";
//		orderInfo += "--------------------------------<BR>";
//		orderInfo += "番　　　　　　 1.0    1   1.0<BR>";
//		orderInfo += "番茄　　　　　 10.0   10  10.0<BR>";
//		orderInfo += "番茄炒　　　　 10.0   100 100.0<BR>";
//		orderInfo += "番茄炒粉　　　 100.0  100 100.0<BR>";
//		orderInfo += "番茄炒粉粉　　 1000.0 1   100.0<BR>";
//		orderInfo += "番茄炒粉粉粉粉 100.0  100 100.0<BR>";
//		orderInfo += "番茄炒粉粉粉粉 15.0   1   15.0<BR>";
//		orderInfo += "備註：快點送到xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx<BR>";
//		orderInfo += "--------------------------------<BR>";
//		orderInfo += "郃計：xx.0元<BR>";
//		orderInfo += "送貨地點：xxxxxxxxxxxxxxxxx<BR>";
//		orderInfo += "聯係電話：138000000000<BR>";
//		orderInfo += "訂餐時間：2011-01-06 19:30:10<BR>";
//		orderInfo += "----------請掃描二維碼----------";
//		orderInfo += "<QR>www.feieyun.com</QR>";//这是二维码,把内容用标签套上即可
//		orderInfo += "<BR>";

//测试外文=======================		
//		String orderInfo;
//		orderInfo = "<CB>中文日文英文</CB><BR>";//标题字体如需居中放大,就需要用标签套上
//		orderInfo += "名稱　　　　　 單價  數量 金額<BR>";
//		orderInfo += "--------------------------------<BR>";
//		orderInfo += "雨が降っていますね。傘を<BR>";
//		orderInfo += "じゃあ、私の　お貸しましょうか<BR>";
//		orderInfo += "じゃあ、私の　お貸しましょうか<BR>";
//		orderInfo += "abcdefghijklmnopqrstuvwxyz<BR>";
//		orderInfo += "<BR>";
		
	   //通过POST请求，发送打印信息到服务器
	   HttpPost post = new HttpPost(HOST+"/FeieServer/printSelfFormatOrder");
       HttpClient client = new DefaultHttpClient();
       client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000); 
       client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
       
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("clientCode",clientCode));
		nvps.add(new BasicNameValuePair("printInfo",orderInfo));
		nvps.add(new BasicNameValuePair("key",strkey));
		nvps.add(new BasicNameValuePair("printTimes",times));
		
	    InputStream is = null;
       try
       {
    	   post.setEntity(new UrlEncodedFormEntity(nvps,"utf-8"));
       	   HttpResponse response = client.execute(post);
           int statecode = response.getStatusLine().getStatusCode();
           if(statecode == 200){
	           HttpEntity httpentity = response.getEntity(); 
 	           String strentity = null;
 	            if (httpentity != null){
 	            	is = httpentity.getContent();
 	            	byte[] b = new byte[1024]; 
 	            	int length = 0;
 	            	StringBuilder sb = new StringBuilder();
 	            	while((length=is.read(b))!= -1){
 	            		sb.append(new String(b,0,length));
 	            	}
 	            	strentity = sb.toString();
 	            	is.close(); 
 	            	return strentity;
 	            }
 	            else{
 	            	 return null;
 	            }
             }else{
            	 return null;
             }
           
       }
       catch (Exception e)
       {
    	   e.printStackTrace();
       }
       finally{
    	   if(is!=null){
    		   try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
    	   }
    	   if(post !=null){
    		   post.abort();
    	   }
    	   
       }
       return null;
	  
	}

	
}