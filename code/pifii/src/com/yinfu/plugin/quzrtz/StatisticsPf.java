
package com.yinfu.plugin.quzrtz;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;

/**
 * 任务调度
 * 
 * @author Administrator
 */
public class StatisticsPf implements Job {
	private static Logger logger = Logger.getLogger(StatisticsPf_old.class);
	
	private static final String START_DATE = "2014-08-01";//最开始统计的开始日期
	private static final int checkDays = 20;//每次开启任务会向前检查的天数（防止前面有插入错误或者未插入的数据）
	/**
	 * 插入客流统计数据
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.warn(">>>>>>>>>>>>>>>>开始插入客流统计数据");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date now = new Date();
		
		Calendar yesterdayCalendar = Calendar.getInstance();
		yesterdayCalendar.setTime(now);
		yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -1);
//		yesterdayCalendar.set(Calendar.HOUR_OF_DAY, 0);
//		yesterdayCalendar.set(Calendar.SECOND,0);
//		yesterdayCalendar.set(Calendar.MINUTE,0);
		
		Calendar startCheckCalendar = Calendar.getInstance();
		startCheckCalendar.setTime(now);
		startCheckCalendar.add(Calendar.DAY_OF_MONTH, 0-checkDays);
		
		Date startDate = null;
		try {
			startDate = sdf.parse(START_DATE);
		} catch (ParseException e) {
			startDate = yesterdayCalendar.getTime();
		}
		if(startCheckCalendar.getTime().before(startDate)){
			startCheckCalendar.setTime(startDate);
		}
		Map<String,List<String>> staticDay = hasStaticDay(sdf.format(startCheckCalendar.getTime()));
		while(!startCheckCalendar.after(yesterdayCalendar)){
			final String curentDate = sdf.format(startCheckCalendar.getTime());
			List<Record> list = Db.find("select sn,count(distinct(mac)) passenger_flow from bp_report where date(create_date)=? group by sn ",
					new Object[]{curentDate});
			List<Record> insertData = new ArrayList<Record>();
			List<String> insertedSN = staticDay.get(curentDate);
			if(null != insertedSN){
				Iterator<Record> ite = list.iterator();
				while(ite.hasNext()){
					Record rowData = ite.next();
					String routerSN = rowData.getStr("sn");
					if(!insertedSN.contains(routerSN)){
						insertData.add(rowData);
						logger.warn(">>>>>>插入客流统计数据："+curentDate+":"+routerSN+":"+rowData.get("passenger_flow").toString());
					}
				}
			}else{
				insertData = list;
			}
			final Object[][] paramsArr = new Object[insertData.size()][3];
			for(int i=0;i<insertData.size();i++){
				Record rd = insertData.get(i);
				paramsArr[i][0] = rd.getStr("sn");
				paramsArr[i][1] = rd.getLong("passenger_flow");
				paramsArr[i][2] = curentDate;
			}
			if(insertData.size() > 0){
				Db.tx(new IAtom(){public boolean run() throws SQLException {
					int[] changeRows = Db.batch("insert into bp_statistics_pf(router_sn,counts,date) values(?,?,?) ", paramsArr, paramsArr.length);
					for(int i=0;i<changeRows.length;i++){
						if(changeRows[i] < 1){
							logger.warn(">>>>>>>>>>>>>>>>"+curentDate+"插入客流统计数据失败，下次再次插入。");
							return false;
						}
					}
					return true;
				}});
			}
			startCheckCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
//		Db.update("delete from bp_report where to_days(now()) - to_days( create_date) > ? ", new Object[]{checkDays});//删除之前的数据
		logger.warn(">>>>>>>>>>>>>>>>客流统计数据插入结束。");
	}
	
//	private List<String> hasStaticDay(String startDate){
//		List<String> returnData = new ArrayList<String>();
//		
//		List<Record> dates = Db.find("select DISTINCT(date_format(date,'%Y-%m-%d')) date from bp_statistics_pf where date >= ? ", new Object[]{startDate});
//		Iterator<Record> ite = dates.iterator();
//		while(ite.hasNext()){
//			Record rowData = ite.next();
//			returnData.add(rowData.getStr("date"));
//		}
//		return returnData;
//	}
	
	private Map<String,List<String>> hasStaticDay(String startDate){
		Map<String,List<String>> returnData = new HashMap<String,List<String>>();
		List<Record> dates = Db.find("select distinct date_format(date,'%Y-%m-%d') date,router_sn from bp_statistics_pf where date >= ? ", new Object[]{startDate});
		Iterator<Record> ite = dates.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			String date = rowData.getStr("date");
			String routerSN = rowData.getStr("router_sn");
			List<String> routerSNList = returnData.get(date);
			if(null == routerSNList){
				routerSNList = new ArrayList<String>();
				returnData.put(date, routerSNList);
			}
			routerSNList.add(routerSN);
		}
		return returnData;
	}
	
}
