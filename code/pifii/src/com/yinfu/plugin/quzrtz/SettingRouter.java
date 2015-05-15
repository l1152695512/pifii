package com.yinfu.plugin.quzrtz;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.util.PropertyUtils;

/**
 * 定时关闭盒子的代理和QOS功能
 * @author 刘召召
 *
 */
public class SettingRouter implements Job {
	private static Logger logger = Logger.getLogger(SettingRouter.class);
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);//路由上报数据的时间间隔,要考虑网络延迟
			StringBuffer sql = new StringBuffer();
			sql.append("select d.router_sn,d.remote_account,d.remote_pass,ds.proxy_status,ds.qos_status ");
			sql.append("from bp_device d left join bp_device_setting ds on ((ds.proxy_status or ds.qos_status) and d.router_sn=ds.sn) ");
			sql.append("where date_add(report_date, interval "+interval+" second) > now() limit 10");
			
		} catch (Exception e) {
		}
	}
	
}
