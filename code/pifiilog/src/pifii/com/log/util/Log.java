package pifii.com.log.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {
	static final StackTraceElement[] th=Thread.currentThread().getStackTrace();
	static final Logger log=LoggerFactory.getLogger(th[1].getMethodName());
	
	/**
	 * 
	* @Title: logn 
	* @Description: TODO(日志：自动判断日志的发出者：不用写object.class的方式) 
	* @param @return    Logger   StackTraceElement[] th=Thread.currentThread().getStackTrace(); 获取正在运行的方法名
	* @throws
	 */
	public static Logger logn(){
		
		return LoggerFactory.getLogger(th[1].getMethodName());
	}
	
	
	
	
	
}
