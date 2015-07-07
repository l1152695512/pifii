package com.cnvp.paladin.pifii.job;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;

import com.cnvp.paladin.pifii.util.LogU;

public class JobTest implements org.quartz.Job
 {
	public static Logger log=LogU.get();
	int count=0;
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		count++;
		log.debug("init Server Start"+count);
		// 判断类型分表  把带天类型的base表转换为  类型表  
		
		
		
		
		
		
	}

}
