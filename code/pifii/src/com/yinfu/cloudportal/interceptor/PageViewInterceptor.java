package com.yinfu.cloudportal.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.cloudportal.interceptor.thread.PageViewThread;
import com.yinfu.cloudportal.utils.SessionParams;

/**
 * 页面访问统计，这里对页面访问记录不是每次都会插入，因为有些页面的访问会参数不同，这样的要过滤掉，例如：访问的是模板2的页面，可能情况有几种：
 * 		1.访问模板页
 * 		2.模板页获取验证码（cmd=code）
 * 		3.模板也认证（cmd=auth）
 * 	这三种请求都是访问模板2，url也一样，但是功能不一样，所以应该只是1为页面访问，其他两个可以过滤掉，
 * 
 * 这里针对那些页面需要添加页面访问统计没有放到数据库里，是因为上面的原因，每个页面的情况可能不一样
 * 
 * @author liuzhao
 *
 */
public class PageViewInterceptor implements Interceptor{
//	private static Logger logger = Logger.getLogger(BlackListMacInterceptor.class);
	
	@Override
	public void intercept(ActionInvocation ai) {
		Controller c = ai.getController();
		Record device = c.getSessionAttr(SessionParams.ACCESS_DEVICE);
		Record client = c.getSessionAttr(SessionParams.CLIENT_INFO);
		//使用多线程插入访问记录，避免影响用户访问速度
		new PageViewThread(ai.getActionKey(),client.get("client_mac").toString(),device.get("router_sn").toString(),c.getParaMap()).start();
		ai.invoke();
	}
	
}
