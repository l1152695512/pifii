
package com.yinfu;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.JFinal;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.ext.plugin.quartz.QuartzPlugin;
import com.jfinal.ext.plugin.shiro.ShiroInterceptor;
import com.jfinal.ext.plugin.shiro.ShiroPlugin;
import com.jfinal.ext.plugin.sqlinxml.SqlInXmlPlugin;
import com.jfinal.ext.plugin.tablebind.AutoTableBindPlugin;
import com.jfinal.ext.route.AutoBindRoutes;
import com.jfinal.plugin.activerecord.SqlReporter;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.plugin.ehcache.EhCachePlugin;
import com.jfinal.render.ViewType;
import com.yinfu.jbase.jfinal.ext.xss.XssHandler;
import com.yinfu.shiro.SessionHandler;
import com.yinfu.shiro.ShopInterceptor;

/**
 * API引导式配置
 */
public class MyConfig extends JFinalConfig {
	private Routes routes;
	
	/**
	 * 配置常量
	 */
	public void configConstant(Constants me) {//这个方法会最先执行，所以加载配置文件要放在这里
		loadPropertyFile("classes/db.properties");
		
		me.setError404View("/page/error/404.html");
		me.setError401View("/page/error/401.html");
		me.setError403View("/page/error/403.html");
		me.setError500View("/page/error/500.html");
		
		me.setDevMode(getPropertyToBoolean("devMode"));
		me.setViewType(ViewType.JSP); // 设置视图类型为Jsp，否则默认为FreeMarker
	}
	/**
	 * 配置路由
	 */
	public void configRoute(Routes me) {
		this.routes = me;
		// 自动扫描 建议用注解
		me.add(new AutoBindRoutes(false));
		/* me.add("/hello",CommonController.class); */
	}
	
	/**
	 * 配置插件
	 */
	public void configPlugin(Plugins me) {
		C3p0Plugin dbPlugin = new C3p0Plugin(getProperty("jdbcUrl"), getProperty("user"), getProperty("password"));
		me.add(dbPlugin);
		// add EhCache
		me.add(new EhCachePlugin());
		// add sql xml plugin
		me.add(new SqlInXmlPlugin());
		// add shrio
		if (Consts.OPEN_SHIRO)
			me.add(new ShiroPlugin(this.routes));
		
		// 配置AutoTableBindPlugin插件
		AutoTableBindPlugin atbp = new AutoTableBindPlugin(dbPlugin);
		atbp.setShowSql(getPropertyToBoolean("devMode"));
		atbp.autoScan(false);
		me.add(atbp);
		// sql记录
		SqlReporter.setLogger(true);
		//配置quartz任务调度插件
		QuartzPlugin quartzPlugin =  new QuartzPlugin("job.properties");
        me.add(quartzPlugin);
		
	}
	
	/**
	 * 配置全局拦截器
	 */
	public void configInterceptor(Interceptors me) {
		// shiro权限拦截器配置
		if (Consts.OPEN_SHIRO)
			me.add(new ShiroInterceptor());
		if (Consts.OPEN_SHIRO)
			me.add(new com.yinfu.shiro.ShiroInterceptor());
		// 让 模版 可以使用session
		me.add(new SessionInViewInterceptor());
//		me.add(new ShopInterceptor());
	}
	
	/**
	 * 配置处理器
	 */
	public void configHandler(Handlers me) {
		// 计算每个page 运行时间
		// me.add(new RenderingTimeHandler());
		
		// xss 过滤
		me.add(new XssHandler("s"));
		// 伪静态处理
		//me.add(new FakeStaticHandler());
		// 去掉 jsessionid 防止找不到action
		me.add(new SessionHandler());
		me.add(new UrlSkipHandler(".*/servlet.*", false));
		me.add(new UrlSkipHandler(".*/advServlet", false));
		me.add(new UrlSkipHandler(".*/authorizeAccess", false));
		me.add(new UrlSkipHandler(".*/routerPassList", false));
		// me.add(new DruidStatViewHandler("/druid"));
		
		me.add(new ContextPathHandler());
	}
	
	@Override
	public void afterJFinalStart() {
		super.afterJFinalStart();
//		ListenDataFileChange.listenFile();
	}
	
	@Override
	public void beforeJFinalStop() {
		super.beforeJFinalStop();
		
	}
	/**
	 * 运行此 main 方法可以启动项目，此main方法可以放置在任意的Class类定义中，不一定要放于此
	 */
	public static void main(String[] args) {
		JFinal.start("WebRoot", 4444, "/", 5);
	}
	
}
