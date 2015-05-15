package com.yinfu.cloudportal.controller.template;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.DataOrgUtil;
import com.yinfu.cloudportal.RouterInterceptor;
import com.yinfu.cloudportal.interceptor.PageViewInterceptor;
import com.yinfu.cloudportal.utils.CommsRoute;
import com.yinfu.cloudportal.utils.SessionParams;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.ContextUtil;

/**
 * 这里要注意图片资源路径，认证平台上是没有图片资源的，需要加载商户平台上的资源
 * @author l
 *
 */
@Before({RouterInterceptor. class,PageViewInterceptor.class})
@ControllerBind(controllerKey = "/portal/mb/postal", viewPath = "/portal")
public class Postal extends Controller<Record>{
	
	public void index(){
		Record rd = DataOrgUtil.getUserSetting(ContextUtil.getCurrentUserId(),"isroot");
		
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		setAttr("appsT1", getPageApps(1,rd.getInt("id")));
		setAttr("appsT2", getPageApps(2,rd.getInt("id")));
		setAttr("banner_advs", CommsRoute.getBannerAvds(device.get("shop_id")));
		setAttr("shopInfo", CommsRoute.getPageShopInfo(device.get("shop_id")));
		render("mb/index1/index.jsp");
	}
	
	private List<Record> getPageApps(int type,int rootId){
		List<Record> apps = Db.find("select title,url,logo,des from bp_nav where status=1 and type=? and org_root_id=?",type,rootId);
//		apps.add(new Record().set("name", "便民缴费").set("link", "#").set("icon", Portal.serverpath+"/images/app/postal/payment.png"));
//		apps.add(new Record().set("name", "车险自邮一族").set("link", "#").set("icon", Portal.serverpath+"/images/app/postal/insurance.png"));
//		apps.add(new Record().set("name", "惠民优选").set("link", "http://cpvip.ule.com").set("icon", Portal.serverpath+"/images/app/postal/huimin.png"));
//		apps.add(new Record().set("name", "金融连接").set("link", "http://www.psbc.com/portal/zh_CN/index.html").set("icon", Portal.serverpath+"/images/app/postal/financial.png"));
//		apps.add(new Record().set("name", "票务").set("link", "#").set("icon", Portal.serverpath+"/images/app/postal/ticketing.png"));
//		apps.add(new Record().set("name", "邮乐半价").set("link", "http://www.ule.com/tejia.html?srcid=ule2014_hp_topslide_001&uspm=1.1.1_V2014.100016.1.1").set("icon", Portal.serverpath+"/images/app/postal/halfprice.png"));
//		apps.add(new Record().set("name", "邮乐网").set("link", "http://www.ule.com").set("icon", Portal.serverpath+"/images/app/postal/youlewang.png"));
//		apps.add(new Record().set("name", "网上营业厅").set("link", "http://11185.cn/").set("icon", Portal.serverpath+"/images/app/postal/businessOffice.png"));
		return apps;
	}
	
	
}
