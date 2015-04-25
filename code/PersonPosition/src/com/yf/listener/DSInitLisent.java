package com.yf.listener;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.yf.tradecontrol.GlobalVar;

/**
 * 初始化DS
 *
 */
public class DSInitLisent implements ServletContextListener {
	/** 日志 */
	private static Logger logger = Logger.getLogger(DSInitLisent.class);
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.info("关闭系统");
	}
	
	/** 初始化系统参数,web容器启动的时候自动加载需要的系统参数文件*/
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ServletContext sc=servletContextEvent.getServletContext();
		String root=sc.getRealPath("");
		root=root.replace("\\","/");
		GlobalVar.setToolsPath(root);
		GlobalVar.setWorkPath(root+"/WEB-INF/");
		String webroot = sc.getInitParameter("webAppRootKey");
		System.setProperty(webroot, root);	
		GlobalVar.initGlobalVar();
		logger.info("初始化系统参数完成....");
	}
}
