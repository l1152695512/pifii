
package com.yinfu.plugin.quzrtz;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.plugin.activerecord.Db;


public class MyJob implements Job {
	private static Logger logger = Logger.getLogger(MyJob.class);
	/**
	 * 删除已同步完成的任务
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.warn("开始删除已完成地 任务。");
		List<String> delList = new ArrayList<String>();
		delList.add("delete from bp_cmd where status=1");
		delList.add("delete from bp_task where status=1");
		Db.batch(delList, delList.size());
		logger.warn("结束删除已完成地 任务。");
	}
	
}
