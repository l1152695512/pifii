
package com.yinfu.plugin.quzrtz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.servlet.route.RouteReportInterface;

/**
 * 任务调度
 * 每月1,10,20,25号1点执行，多次执行避免服务器关闭，导致数据没有插入
 * @author Administrator
 */
public class SearchDateJob implements Job {
	private static Logger logger = Logger.getLogger(RouteReportInterface.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.warn("开始执行插入下个月搜索数据。");
		Calendar end = Calendar.getInstance();
		end.add(Calendar.MONTH, 1);
		end.set(Calendar.DAY_OF_MONTH,1);//设置为1号
//		Date endDate = end.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Record maxDate = Db.findFirst("select max(search_date) max_date from bp_search_day");
        Date startDate = maxDate.getDate("max_date");
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);
        start.add(Calendar.DATE, 1);
        final List<Object[]> dateSearch = new ArrayList<Object[]>();
        while(start.getTime().before(end.getTime())){
        	dateSearch.add(new Object[]{df.format(start.getTime())});
        	start.add(Calendar.DATE, 1);
        }
        if(dateSearch.size() > 0){
        	final Object[][] paramsArr = new Object[dateSearch.size()][1];
        	for(int i=0;i<dateSearch.size();i++){
        		paramsArr[i] = dateSearch.get(i);
        	}
        	int[] changeRows = Db.batch("insert into bp_search_day(search_date) values(?)", paramsArr, paramsArr.length);
        	for(int i=0;i<changeRows.length;i++){
        		if(changeRows[i] < 1){
        			logger.warn("开始执行插入下个月搜索数据---------有未插入的数据（可能是重复的）。");
        			return;
        		}
        	}
        	logger.warn("开始执行插入下个月搜索数据---------数据插入正常。");
        }
	}
	
}
