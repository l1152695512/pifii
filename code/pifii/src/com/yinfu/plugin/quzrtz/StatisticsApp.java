
package com.yinfu.plugin.quzrtz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import com.jfinal.ext.DbExt;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.util.PropertyUtils;

/**
 * 任务调度
 * 每天0、10、16、23点的1、58分执行
 * @author Administrator
 */
public class StatisticsApp implements Job {
	private static Logger logger = Logger.getLogger(StatisticsApp.class);
//	private static final String START_DATE = "2014-08-01";
//	private static final int DAYS = 3;
	
	@Override
	public void execute(JobExecutionContext arg0){
		logger.warn(">>>>>>>>>>>>>>>>开始插入应用统计数据");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar yesterdayCalendar = Calendar.getInstance();
		yesterdayCalendar.setTime(new Date());
		yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -1);
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(getMaxDate());
		startCalendar.add(Calendar.DAY_OF_MONTH, 0-PropertyUtils.getPropertyToInt("statistics.app.checkDays", 3));
		while(!startCalendar.after(yesterdayCalendar)){
			final String insertDate = sdf.format(startCalendar.getTime());
			startCalendar.add(Calendar.DAY_OF_MONTH, 1);
			
			StringBuffer sql = new StringBuffer();
			sql.append("select sp.shop_id,a.id app_id,COUNT(sa.id) num,ifnull(sap.id,'') id,ifnull(sap.access_num,0) access_num ");
			sql.append("from bp_shop_page_app spa ");
			sql.append("join bp_shop_page sp on (spa.page_id=sp.id) ");
			sql.append("join bp_device d on (sp.shop_id=d.shop_id) ");
			sql.append("join bp_app a on (spa.app_id = a.id) ");
			sql.append("join bp_statistics_all sa on (sa.statistics_type='app' and DATE(sa.access_date)='"+insertDate+"' and sa.statistics_id=a.id and d.router_sn=sa.router_sn) ");
			sql.append("left join bp_statistics_app sap on (sap.access_date='"+insertDate+"' and sp.shop_id=sap.shop_id and a.id=sap.app_id) ");
			sql.append("group by DATE(sa.access_date),sp.shop_id,a.id ");
			sql.append("ORDER BY sp.shop_id,a.id ");
			List<Record> dataList = Db.find(sql.toString());
			if(dataList.size() > 0){
				List<Object[]> updateList = new ArrayList<Object[]>();
				List<Object[]> insertList = new ArrayList<Object[]>();
				for(int i=0;i<dataList.size();i++){
					Record row = dataList.get(i);
					String id = row.getStr("id");
					if(StringUtils.isBlank(id)){
						Object[] o = new Object[]{UUID.randomUUID().toString(),insertDate,
								row.get("shop_id"),row.get("app_id"),row.get("num")};
						insertList.add(o);
					}else{
						long thisNum = row.getLong("num");
						long oldNum = row.getLong("access_num");
						if(thisNum!=oldNum){
							Object[] o = new Object[]{thisNum,id};
							updateList.add(o);
						}
					}
				}
				//新加入了每次统计数据时，会向前检查几天，所以不用开启事物，即使失败也会有几次检查机会（一般出现数据未插入是因为服务器挂掉了）
				if(updateList.size() > 0 || insertList.size() > 0){
//					boolean success = Db.tx(new IAtom(){public boolean run() throws SQLException {
						if(updateList.size() > 0){
							int[] changeRows = DbExt.batch("update bp_statistics_app set access_num=? where id=?", listTo2Array(updateList,2));
//							for(int i=0;i<changeRows.length;i++){
//								if(changeRows[i] < 1){
//									logger.warn(">>>>>>>>>>>>>>>>"+insertDate+"更新数据失败，下次再次更新。");
//									return false;
//								}
//							}
						}
						if(insertList.size() > 0){
							int[] changeRows = DbExt.batch("insert into bp_statistics_app(id,access_date,shop_id,app_id,access_num) "
									+ "values(?,?,?,?,?)", listTo2Array(insertList,5));
//							for(int i=0;i<changeRows.length;i++){
//								if(changeRows[i] < 1){
//									logger.warn(">>>>>>>>>>>>>>>>"+insertDate+"插入数据失败，下次再次插入。");
//									return false;
//								}
//							}
						}
//						return true;
//					}});
//					if(!success){//插入错误就不再插入后面的日期
//						return;
//					}
				}
			}
		}
		logger.warn(">>>>>>>>>>>>>>>>结束插入应用统计数据");
	}
	private Date getMaxDate(){
		Record rec = Db.findFirst("select max(access_date) max_date from bp_statistics_app");
		if(null != rec && null != rec.get("max_date") && StringUtils.isNotBlank(rec.get("max_date").toString())){
			return rec.getDate("max_date");
		}
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(PropertyUtils.getProperty("statistics.startDate", "2014-08-01"));
		} catch (ParseException e) {
			e.printStackTrace();
			Calendar yesterdayCalendar = Calendar.getInstance();
			yesterdayCalendar.setTime(new Date());
			yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -2);
			return yesterdayCalendar.getTime();
		}
	}
	
	private Object[][] listTo2Array(List<Object[]> list,int size){
		Object[][] paramsArr = new Object[list.size()][size];
		for(int i=0;i<list.size();i++){
			paramsArr[i] = list.get(i);
		}
		return paramsArr;
	}
}
