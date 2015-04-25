package com.yf.base.actions.datastatistics;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.yf.util.dbhelper.DBHelper;

public class CommonSearch {
	
	public static boolean initSearchTable(DBHelper dbhelper,String startDate,String endDate){
//		dbhelper.delete("delete from bp_search_date_tbl");
		List<String> dates = getAllSearchDate(startDate,endDate);
		System.err.println(dates.size());
		List<String> sqls = new ArrayList<String>();
		sqls.add("delete from bp_search_date_tbl");
		Iterator<String> ite = dates.iterator();
		while(ite.hasNext()){
			String date = ite.next();
			sqls.add("insert into bp_search_date_tbl values('"+date+"')");
		}
		return dbhelper.executeFor(sqls);
	}
	
	private static List<String> getAllSearchDate(String startDate,String endDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<String> dates = new ArrayList<String>();
		try {
			Date start = sdf.parse(startDate);
			Date end = sdf.parse(endDate);
			Date currentDate = new Date(start.getTime());;
			Calendar calendar=Calendar.getInstance(); 
			while(currentDate.before(end)){
				System.err.println(sdf.format(currentDate));
				dates.add(sdf.format(currentDate));
				calendar.setTime(currentDate); 
				calendar.add(Calendar.DATE,1);
				currentDate = calendar.getTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return dates;
	}
	
	public static boolean searchTable(DBHelper dbhelper,String startDateTime,String endDateTime){
//		dbhelper.delete("delete from bp_search_date_tbl");
		List<String> dates = getSearchDate(startDateTime,endDateTime);
		System.err.println(dates.size());
		List<String> sqls = new ArrayList<String>();
		sqls.add("delete from bp_search_date_tbl");
		Iterator<String> ite = dates.iterator();
		while(ite.hasNext()){
			String date = ite.next();
			sqls.add("insert into bp_search_date_tbl values('"+date+"')");
		}
		return dbhelper.executeFor(sqls);
	}
	
	private static List<String> getSearchDate(String startDateTime,String endDateTime){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> dates = new ArrayList<String>();
		try {
			Date start = sdf.parse(startDateTime);
			Date end = sdf.parse(endDateTime);
			Date currentDate = new Date(start.getTime());
			Calendar calendar=Calendar.getInstance(); 
			while(currentDate.before(end)){
				System.err.println(sdf.format(currentDate));
				dates.add(sdf.format(currentDate));
				calendar.setTime(currentDate); 
				calendar.add(Calendar.DATE,1);
				currentDate = calendar.getTime();
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return dates;
	}
}
