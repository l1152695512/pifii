package com.cnvp.paladin.pifii.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogU {
	public static final Logger log=LoggerFactory.getLogger(LogU.class);
	
	private LogU(){
		//
	}
	/**
	 * 
	* @Title: get 
	* @Description: TODO(日志自动 返回当前方法) 
	* @param @return    设定文件  null
	* @return Logger    返回类型  Logger
	* @throws
	 */
	
	public static Logger get(){
		final StackTraceElement[]	 th=	Thread.currentThread().getStackTrace();
		return LoggerFactory.getLogger(th[2].getClassName());
	}
	

}
