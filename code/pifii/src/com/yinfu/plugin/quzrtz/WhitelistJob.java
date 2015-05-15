
package com.yinfu.plugin.quzrtz;

import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.setting.SynWhitelistThread;

/**
 * 任务调度
 * 每月1,10,20,25号1点执行，多次执行避免服务器关闭，导致数据没有插入
 * @author Administrator
 */
public class WhitelistJob implements Job {
	private static Logger logger = Logger.getLogger(WhitelistJob.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("开始执行定时设置host白名单任务");
		List<Record> synDevice = Db.find("select id,sn from bp_device_whitelist_status where type='ip' or type='domain' ");
		new SynWhitelistThread(synDevice,true).start();
	}
	
}
