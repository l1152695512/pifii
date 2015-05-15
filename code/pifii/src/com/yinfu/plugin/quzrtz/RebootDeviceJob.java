package com.yinfu.plugin.quzrtz;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.jbase.util.remote.RebootDeviceThread;

public class RebootDeviceJob implements Job {
	private static Logger logger = Logger.getLogger(RebootDeviceJob.class);
	public static ExecutorService threadPool = Executors.newCachedThreadPool();
	private static int interval = 600;
	private static int hour = 6;
	private static int size = 10;
	
	static{
		interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);
		hour = PropertyUtils.getPropertyToInt("route.reboot.endtime", 6);
		size = PropertyUtils.getPropertyToInt("route.reboot.threadsize", 10);
	}
	/**
	 * 重启空闲的设备
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.warn("重启空闲设备工作开始...");
		try{
			while(true){
				List<String> deviceList = RebootDeviceThread.deviceList;
				if(deviceList.size()<this.size){//有空闲线程
					StringBuffer sql = new StringBuffer();
					sql.append("select a.router_sn,ifnull(a.remote_account,concat(a.router_sn,'@pifii.com')) remote_account,ifnull(a.remote_pass,'88888888') remote_pass ");
					sql.append("from bp_device a left join bp_device_setting b on a.router_sn = b.sn ");
					sql.append("where a.online_num=0 and (date(a.reboot_date)<>curdate() or a.reboot_date is null) and date_add(a.report_date, interval ? second) > now() and (b.binding is null or b.binding=1) ");
					if(deviceList.size()>0){
						sql.append("and a.router_sn not in("+listToSqlIn(deviceList)+") ");
					}
					sql.append("order by a.reboot_date limit ?");
					List<Record> list = Db.find(sql.toString(),this.interval,this.size-deviceList.size());
					for(Record rd : list){
						String sn = rd.getStr("router_sn");
						String email = rd.getStr("remote_account");
						String pass = rd.getStr("remote_pass");
						deviceList.add(sn);
						threadPool.execute(new RebootDeviceThread(sn,email,pass));
					}
				}
				
				int nowHour = DateUtil.getHour(new Date());
				if(nowHour >= hour){//检查结束时间
					logger.warn("重启空闲设备工作结束...");
					break;
				}
				Thread.sleep(30*1000);
			}
		}catch(Exception e){
			logger.error("重启设备工作失败",e);
		}
	}
	
	public String listToSqlIn(List<String> list){
		StringBuffer sqlIn = new StringBuffer();
		Iterator<String> ite = list.iterator();
		int i = 0;
		while(ite.hasNext()){
			if(i==0){
				sqlIn.append("'"+ite.next()+"'");
			}else{
				sqlIn.append(",'"+ite.next()+"'");
			}
			i++;
		}
		return sqlIn.toString();
	}
	public static void main(String[] args) {
		
	}
}
