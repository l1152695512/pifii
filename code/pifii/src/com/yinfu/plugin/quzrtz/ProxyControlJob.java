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
import com.yinfu.jbase.util.remote.ProxyControlThread;
import com.yinfu.jbase.util.remote.RebootDeviceThread;

public class ProxyControlJob implements Job {
	private static Logger logger = Logger.getLogger(ProxyControlJob.class);
	private static ExecutorService threadPool = Executors.newCachedThreadPool();
	private static int interval = 600;
	private static int hour = 23;
	private static int size = 10;
	private static int proxyStatus = 0;
	
	static{
		interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);
		hour = PropertyUtils.getPropertyToInt("route.proxyStatus.endtime", 23);
		size = PropertyUtils.getPropertyToInt("route.reboot.threadsize", 10);
		proxyStatus = PropertyUtils.getPropertyToInt("route.proxyStatus", 0);
	}
	/**
	 * proxy开关控制，0：关;1:开
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.warn("proxy开关控制工作开始...0：关;1:开,当前操作："+this.proxyStatus);
		try{
			while(true){
				List<String> deviceList = RebootDeviceThread.deviceList;
				if(deviceList.size()<this.size){//有空闲线程
					StringBuffer sql = new StringBuffer();
					sql.append("select a.router_sn,ifnull(a.remote_account,concat(a.router_sn,'@pifii.com')) remote_account,ifnull(a.remote_pass,'88888888') remote_pass ");
					sql.append("from bp_device a left join bp_device_setting b on a.router_sn = b.sn ");
					sql.append("where a.type=1 and date_add(a.report_date, interval ? second) > now() and (b.proxy_status is null or b.proxy_status<>?) and (b.binding is null or b.binding=1) ");
					if(deviceList.size()>0){
						sql.append("and a.router_sn not in("+listToSqlIn(deviceList)+") ");
					}
					sql.append("limit ?");
					List<Record> list = Db.find(sql.toString(),this.interval,this.proxyStatus,this.size-deviceList.size());
					for(Record rd : list){
						String sn = rd.getStr("router_sn");
						String email = rd.getStr("remote_account");
						String pass = rd.getStr("remote_pass");
						deviceList.add(sn);
						threadPool.execute(new ProxyControlThread(sn,email,pass,proxyStatus));
					}
				}
				
				int nowHour = DateUtil.getHour(new Date());
				if(nowHour >= hour){//检查结束时间
					logger.warn("Proxy开关控制工作结束...0：关;1:开,当前操作："+this.proxyStatus);
					break;
				}
				Thread.sleep(30*1000);
			}
		}catch(Exception e){
			logger.error("Proxy开关控制工作失败...0：关;1:开,当前操作："+this.proxyStatus,e);
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