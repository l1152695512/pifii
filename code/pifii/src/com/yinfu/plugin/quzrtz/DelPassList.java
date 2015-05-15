package com.yinfu.plugin.quzrtz;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.plugin.activerecord.Db;

/**
 * 定时更新白名单
 * @author JiaYongChao
 *
 */
public class DelPassList implements Job {
	private static Logger logger = Logger.getLogger(DelPassList.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			List<String> sqlList = new ArrayList<String>();
			sqlList.add("delete from bp_pass_list where end_time<=NOW()");
			Db.batch(sqlList, sqlList.size());
		} catch (Exception e) {
			
		}
		logger.info("定时更新白名单");
	}
	
}
