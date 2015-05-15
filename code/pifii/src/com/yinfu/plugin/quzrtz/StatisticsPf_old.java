package com.yinfu.plugin.quzrtz;

import java.sql.SQLException;
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
import org.quartz.JobExecutionException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
/***
 * 客流统计任务调度
 * @author JiaYongChao
 *
 */
public class StatisticsPf_old implements Job {
	private static Logger logger = Logger.getLogger(StatisticsPf_old.class);
	private static final String START_DATE = "2014-08-01";
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.warn(">>>>>>>>>>>>>>>>开始插入统计数据");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar yesterdayCalendar = Calendar.getInstance();
		yesterdayCalendar.setTime(new Date());
		yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -1);
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(getMaxDate());
		startCalendar.add(Calendar.DAY_OF_MONTH, 1);
		while(!startCalendar.after(yesterdayCalendar)){
			final String insertDate = sdf.format(startCalendar.getTime());
			startCalendar.add(Calendar.DAY_OF_MONTH, 1);
			List<String> list = new ArrayList<String>();
			StringBuffer sql = new StringBuffer(" INSERT INTO bp_statistics_pf (DATE,router_sn,counts) SELECT * FROM ( ");
			sql.append(" SELECT  STR_TO_DATE(IFNULL(aa.days,'"+insertDate+"'),'%Y-%m-%d') AS DATE,ds.router_sn,IFNULL(aa.counts,0) AS counts  ");
			sql.append(" FROM bp_device AS   ds LEFT JOIN ( ");
			sql.append(" SELECT dd.*,COUNT(*) AS counts FROM ( ");
			sql.append(" SELECT DISTINCT DATE_FORMAT(s.access_date,'%Y-%m-%d') AS days,s.router_sn,s.`client_mac` ");
			sql.append(" FROM bp_statistics_all AS s ");
			sql.append(" WHERE  DATE_FORMAT(access_date,'%Y-%m-%d')='"+insertDate+"' AND s.`client_mac` !='') dd  ");
			sql.append(" GROUP BY dd.router_sn,dd.days) AS aa ON aa.router_sn = ds.router_sn) AS cd ");
			list.add(sql.toString());
			Db.batch(list, list.size());
		}
		logger.warn(">>>>>>>>>>>>>>>>结束插入应用统计数据");
	}
	private Date getMaxDate(){
		Record rec = Db.findFirst("select max(date) max_date from bp_statistics_pf");
		if(null != rec && null != rec.get("max_date") && StringUtils.isNotBlank(rec.get("max_date").toString())){
			return rec.getDate("max_date");
		}
		try {
			return new SimpleDateFormat("yyyy-MM-dd").parse(START_DATE);
		} catch (ParseException e) {
			e.printStackTrace();
			Calendar yesterdayCalendar = Calendar.getInstance();
			yesterdayCalendar.setTime(new Date());
			yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -2);
			return yesterdayCalendar.getTime();
		}
	}
}
