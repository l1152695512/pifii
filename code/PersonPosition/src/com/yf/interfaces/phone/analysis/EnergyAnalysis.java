package com.yf.interfaces.phone.analysis;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.RowSet;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.log4j.Logger;

import com.yf.tradecontrol.GlobalVar;
import com.yf.util.DateUtils;
import com.yf.util.dbhelper.DBHelper;

public class EnergyAnalysis {
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private static Logger logger = Logger.getLogger(EnergyAnalysis.class);
	
	public JSONObject getEnergyJSON(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		boolean bool=true;
		JSONArray array=new JSONArray();
		//数据库没有数据，所以先写静态数据
		JSONObject obj1=new JSONObject();
		obj1.put("CONSUMPTION", "41.5度");
		obj1.put("GRADE", "刚刚及格，低空飘过~");
		obj1.put("HEALTH", "80.15");
		obj1.put("ENVIRONMENT", "91.78");
		obj1.put("REMIND", "超过11点还在看电视，要注意休息哦~");
		obj1.put("DATE", "05-08");
		array.add(obj1);
		JSONObject obj2=new JSONObject();
		obj2.put("CONSUMPTION", "35.2度");
		obj2.put("GRADE", "经济的一天，但不太舒适~");
		obj2.put("HEALTH", "75.35");
		obj2.put("ENVIRONMENT", "95.01");
		obj2.put("REMIND", "使用电脑超过12个小时，要注意休息哦~");
		obj2.put("DATE", "05-07");
		array.add(obj2);
		if(bool){
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
	
	public Double getDoubleValue(String account,String date) {
		Double result = 0.0;
		try{
			String table=account.substring(0,6);
			String sqlString=null;
			if(account.length()>6){
				sqlString="select count(*) row_num from (select distinct d_id from bp_database_"+table+"_tbl where account='"+account+"')";
			}else{
				sqlString="select count(*) row_num from (select distinct d_id from bp_database_"+table+"_tbl)";
			}
			RowSet rs=dbhelper.select(sqlString);
			int rownum=0;
			if(rs.next()){
				rownum=rs.getInt("row_num");
			}
			if(rownum!=0){
				StringBuffer sql = new StringBuffer();
				if(account.length()>6){
					sql.append("select * from (select kwh from bp_database_"+table+"_tbl where create_time<=to_date('"+date+"',");
					sql.append("'yyyy-MM-dd hh24:mi:ss') and account='"+account+"' order by create_time desc) where rownum<="+rownum);
				}else{
					sql.append("select * from (select kwh from bp_database_"+table+"_tbl where create_time<=to_date('"+date+"',");
					sql.append("'yyyy-MM-dd hh24:mi:ss') order by create_time desc) where rownum<="+rownum);
				}				
				RowSet rss=dbhelper.select(sql.toString());
				while(rss.next()){
					result+=rss.getDouble("kwh");
				}
			}
		}catch (Exception e) {
			logger.error("账户："+account+", 查询表：bp_database_"+account.substring(0,6)+"_tbl出错", e);
		}
		return result;
	}
	
	public List<String> getDateList(String date) {
		List <String> result=new ArrayList<String>();
		Calendar cal = Calendar.getInstance();
		int nowYear=cal.get(Calendar.YEAR);
		int nowMonth=cal.get(Calendar.MONTH)+1;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		int year=Integer.parseInt(date.substring(0,4));
		int month=Integer.parseInt(date.substring(5,7));
		if(year==nowYear){
			if(month>nowMonth){
				return result;
			}else if(month==nowMonth){
				cal.add(Calendar.DAY_OF_MONTH, +1);
				String str=df.format(cal.getTime())+" 00:00:00";
				result.add(str);
				for(int i=0;i<=Calendar.DAY_OF_MONTH+1;i++){
					cal = Calendar.getInstance();
					cal.add(Calendar.DAY_OF_MONTH, -i);
					str=df.format(cal.getTime())+" 00:00:00";
					result.add(str);
				}
			}else{
				cal.set(Calendar.YEAR, year);
				cal.set(Calendar.MONTH, month);
				cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
				String str=df.format(cal.getTime())+" 00:00:00";
				result.add(str);
				str=DateUtils.getLastDayOfMonth(year, month);
				for(int i=Integer.parseInt(str.substring(8));i>0;i--){
					String string="";
					if(i<10){
						string=str.substring(0,8)+"0"+i+" 00:00:00";
					}else{
						string=str.substring(0,8)+i+" 00:00:00";
					}	
					result.add(string);
				}
			}
		}else if(year<nowYear){
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);
			cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
			String str=df.format(cal.getTime())+" 00:00:00";
			result.add(str);
			str=DateUtils.getLastDayOfMonth(year, month);
			for(int i=Integer.parseInt(str.substring(8));i>0;i--){
				String string="";
				if(i<10){
					string=str.substring(0,8)+"0"+i+" 00:00:00";
				}else{
					string=str.substring(0,8)+i+" 00:00:00";
				}	
				result.add(string);
			}
		}else{
			return result;
		}
		return result;
	}
	
	public JSONArray getConsumption(String account,String date) {
		JSONArray array=new JSONArray();
		List<String> list=getDateList(date);
		if(list.size()<1){
			return array;
		}
		for(int i=0;i<list.size()-1;i++){
			JSONObject object=new JSONObject();
			String str=list.get(i+1).substring(8,10);
			object.put("DATE", str);
			Double dou=getDoubleValue(account, list.get(i))-getDoubleValue(account, list.get(i+1));
			object.put("VALUE", dou);
			array.add(object);
		}
		return array;		
	}
	
	public List<Double> getAverageList(String account,JSONArray array) {
		List<Double> result=new ArrayList<Double>();
		try{
			String sql="select count(*) row_num from (select distinct account from bp_database_"+account+"_tbl)";
			RowSet rs=dbhelper.select(sql);
			int rownum=0;
			if(rs.next()){
				rownum=rs.getInt("row_num");
			}
			if(rownum!=0){
				for(int i=0;i<array.size();i++){
					Double double1=array.getJSONObject(i).getDouble("VALUE");
					Double double2=double1/rownum;
					result.add(double2);
				}
			}
		}catch (Exception e) {
			logger.error("统计区域："+account+", 平均用电量出错", e);
		}
		return result;
	}
	
	public JSONArray getEconomy(List<Double> list,JSONArray jsonArray) {
		JSONArray array=new JSONArray();
		for(int i=0;i<list.size();i++){
			JSONObject object=new JSONObject();
			String date=jsonArray.getJSONObject(i).getString("DATE");
			Double average=list.get(i);
			Double consunmption=jsonArray.getJSONObject(i).getDouble("VALUE");
			Double result=0.0;
			if(consunmption<1.0){
				result=20.0;
			}else{
				result=average/consunmption;
			}
			object.put("DATE", date);
			object.put("VALUE", result);
			array.add(object);
		}
		return array;
	}
	
	public JSONArray getEnvironment(JSONArray jsonArray) {
		JSONArray array=new JSONArray();
		for(int i=0;i<jsonArray.size();i++){
			JSONObject object=new JSONObject();
			String date=jsonArray.getJSONObject(i).getString("DATE");
			Double dou=jsonArray.getJSONObject(i).getDouble("VALUE");
			Double result=0.0;
			if(dou==20){
				result=20.0;
			}else{
				result=dou*10;
			}			
			object.put("DATE", date);
			object.put("VALUE", result);
			array.add(object);
		}
		return array;
	}
	
	public JSONArray getComfort(JSONArray jsonArray1,JSONArray jsonArray2,JSONArray jsonArray3) {
		JSONArray array=new JSONArray();
		for(int i=0;i<jsonArray1.size();i++){
			JSONObject object=new JSONObject();
			String date=jsonArray1.getJSONObject(i).getString("DATE");
			Double double1=jsonArray1.getJSONObject(i).getDouble("VALUE")*0.4;
			Double double2=jsonArray2.getJSONObject(i).getDouble("VALUE")*0.3;
			Double double3=jsonArray3.getJSONObject(i).getDouble("VALUE")*0.3;
			Double result=double1*double2*double3;
			object.put("DATE", date);
			object.put("VALUE", result);
			array.add(object);
		}
		return array;
	}
	
	public JSONObject getAnalysisJSON(JSONObject json) {
		JSONObject resultObject=new JSONObject();
		boolean bool=true;
		JSONObject	object=new JSONObject();
		String account=json.getJSONObject("data").getString("ACCOUNT");
		String date=json.getJSONObject("data").getString("DATE");
		JSONArray consumption=getConsumption(account, date);
		if(consumption.size()<1){
			bool=false;
		}else{
			object.put("CONSUMPTION", consumption);
			
			String table=account.substring(0,6);
			JSONArray count=getConsumption(table, date);
			List<Double> average=getAverageList(table,count);
			JSONArray economy=getEconomy(average, consumption);
			object.put("ECONOMY", economy);
			
			JSONArray environment=getEnvironment(economy);
			object.put("ENVIRONMENT", environment);
			
			JSONArray comfort=getComfort(consumption, economy, environment);
			object.put("COMFORT", comfort);
		}
		if(bool){
			resultObject.put("returnCode", 200);
			resultObject.put("data", object);
			resultObject.put("desc", "客户端操作成功");
		}else{
			resultObject.put("returnCode", 401);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "客户端请求数据失败");
		}
		return resultObject;
	}
	
	public static void main(String[] args) {
		EnergyAnalysis energyAnalysis=new EnergyAnalysis();
		JSONObject json=new JSONObject();
		json.put("tradeCode", 5101);
		JSONObject obj=new JSONObject();
		obj.put("ACCOUNT", "admin");
		obj.put("DATE", "2013-05");
		obj.put("START", 0);
		obj.put("LIMIT", 4);
		json.put("data", obj);
		json.put("authCode", "admin");
		System.out.println(json);
		System.out.println(energyAnalysis.getEnergyJSON(json));
		System.out.println(energyAnalysis.getEnergyJSON(json).getJSONArray("data"));
		for(int i=0;i<energyAnalysis.getEnergyJSON(json).getJSONArray("data").size();i++){
			System.out.println(energyAnalysis.getEnergyJSON(json).getJSONArray("data").getJSONObject(i));
		}
		
		JSONObject json1=new JSONObject();
		json1.put("tradeCode", 5201);
		JSONObject obj1=new JSONObject();
		obj1.put("ACCOUNT", "0101052000010");
		obj1.put("DATE", "2013-06");
		json1.put("data", obj1);
		json1.put("authCode", "admin");
		System.out.println(json1);
		System.out.println(energyAnalysis.getAnalysisJSON(json1));
		System.out.println(energyAnalysis.getAnalysisJSON(json1).getJSONObject("data"));
		System.out.println(energyAnalysis.getAnalysisJSON(json1).getJSONObject("data").getJSONArray("CONSUMPTION"));
		System.out.println(energyAnalysis.getAnalysisJSON(json1).getJSONObject("data").getJSONArray("COMFORT"));
		System.out.println(energyAnalysis.getAnalysisJSON(json1).getJSONObject("data").getJSONArray("ECONOMY"));
		System.out.println(energyAnalysis.getAnalysisJSON(json1).getJSONObject("data").getJSONArray("ENVIRONMENT"));
	}
}
