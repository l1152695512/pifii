package com.yinfu.plugin.quzrtz;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.plugin.activerecord.Record;
import com.yinfu.servlet.route.RouteAuth;

/**
 * 定时清除认证成功的信息，这些信息会放在RouteAuth的authKey变量中，担心会出现有时候认证完成后认证信息没有清除，导致内存占用
 * @author liuzhaozhao
 *
 */
public class AuthKeyClear implements Job {
	private static Logger logger = Logger.getLogger(AuthKeyClear.class);
	private static final long AUTH_INFO_MAX_TIME = 2*60*1000;//2分钟
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			Map<String,Record> authMap = RouteAuth.getAuthKey();
			Set<String> authSet = new HashSet<String>();
			authSet.addAll(authMap.keySet());
			Iterator<String> ite = authSet.iterator();
			while(ite.hasNext()){
				String thisKey = ite.next();
				Record authInfo = authMap.get(thisKey);
				if(null != authInfo && 
						(System.currentTimeMillis()-authInfo.getDate("authDate").getTime())>AUTH_INFO_MAX_TIME){
					authMap.remove(thisKey);
				}
			}
		} catch (Exception e) {
			logger.warn("清除认证信息异常！", e);
		}
	}
	
}
