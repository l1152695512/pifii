package com.yinfu.servlet.route.fun;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.commonpage.PageUtils;
import com.yinfu.jbase.util.PropertyUtils;

public class CloudPage {
	private Logger logger = Logger.getLogger(CloudPage.class);
	private String routersn;
	private String clientMac;
	
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		routersn = (String) request.getSession().getAttribute("session_deviceUniqueCode");
		clientMac = (String) request.getSession().getAttribute("session_clientMac");
		PageUtils utils = new PageUtils();
		request.setAttribute("apps", JsonKit.toJson(utils.getPageApp(getShopId())));
		request.setAttribute("pictures", JsonKit.toJson(utils.getPageAdvPicture(getShopId())));
		request.setAttribute("shopInfo", JsonKit.toJson(utils.getPageShopInfo(getShopId())));
		Record style = utils.getPagePath(getShopId(),false);
		if(null != style){
			try{
				Commons.addAccessData(routersn, clientMac, "-1", "");
			}catch(Exception e){}
			//根据终端类型去跳转页面
			if(clientIsPC(request)){
				String url = "advServlet?cmd=pcAuth&routersn="+routersn+"&mac="+clientMac;
				response.sendRedirect(url);
//				request.getRequestDispatcher(style.getStr("page_path")).forward(request,response);
			}else{
				request.getRequestDispatcher(style.getStr("page_path")).forward(request,response);
			}
		}else{
			response.sendRedirect(PropertyUtils.getProperty("router.auth.errorPage"));
		}
		logger.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>load page");
	}
	
	private String getShopId(){
		Record rec = Db.findFirst("select shop_id from bp_device where router_sn=? ", new Object[]{routersn});
		if(null != rec){
			return rec.getInt("shop_id")+"";
		}
		return "";
	}
	
	//判断客户端类型
	public static boolean clientIsPC(HttpServletRequest request){
		String clientInfo = (null != request.getHeader("USER-AGENT"))?request.getHeader("USER-AGENT").toLowerCase():"";
		System.err.println("clientInfo="+clientInfo);
		if(clientInfo.indexOf("windows") > 0
				|| (clientInfo.indexOf("linux") > 0 
						&& clientInfo.indexOf("android") == -1
						&& clientInfo.indexOf("mobile") == -1 )
				|| clientInfo.indexOf("macintosh") > 0){
			return true;
		}else{
			return false;
		}
	}
}
