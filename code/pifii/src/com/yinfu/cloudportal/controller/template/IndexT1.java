package com.yinfu.cloudportal.controller.template;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.PageUtil;
import com.yinfu.cloudportal.Portal;
import com.yinfu.cloudportal.RouterInterceptor;
import com.yinfu.cloudportal.interceptor.PageViewInterceptor;
import com.yinfu.cloudportal.utils.CommsRoute;
import com.yinfu.cloudportal.utils.SessionParams;
import com.yinfu.jbase.jfinal.ext.Controller;

/**
 * 这里要注意图片资源路径，认证平台上是没有图片资源的，需要加载商户平台上的资源
 * @author l
 *
 */
@Before({RouterInterceptor. class,PageViewInterceptor.class})
@ControllerBind(controllerKey = "/portal/mb/temp1", viewPath = "/portal")
public class IndexT1 extends Controller<Record>{
	
	public void index(){
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		setAttr("apps", getPageApps(device.get("shop_id")));
		setAttr("banner_advs", CommsRoute.getBannerAvds(device.get("shop_id")));
		setAttr("shopInfo", CommsRoute.getPageShopInfo(device.get("shop_id")));
//		request.setAttribute("bottom_adv", getBottomAdv(shopId));//模板不再需要底部广告条
		render("mb/index1/index.jsp");
	}
	
	private List<Record> getPageApps(Object shopId){//要考虑没有识别出商户的情况
//		List<Record> apps = Db.find("select name,concat('"+Portal.serverpath+"','/',icon) icon,concat('"+getRequest().getContextPath()+"','/',cloud_url,'/app-',id) link from bp_app where marker in ('video','audio','book','game2','apk2') ");
		//加载指定的应用，显示bp_shop_page_app表中page_id为null的应用
		List<Record> apps = new ArrayList<Record>();
		apps.add(new Record().set("name", "帮助中心").set("icon",Portal.serverpath+"/images/business/app/help.png").set("link","help"));
		StringBuffer sql = new StringBuffer();
		sql.append("select a.name,concat('"+Portal.serverpath+"','/',a.icon) icon,concat('"+getRequest().getContextPath()+"','/',a.cloud_url,'/app-',a.id) link ");
		sql.append("from bp_shop_page_app pa join bp_app a on (pa.page_id is null and pa.app_id=a.id) ");
		apps.addAll(Db.find(sql.toString()));
		if(null != shopId && StringUtils.isNotBlank(shopId.toString())){
			StringBuffer sqlApps = new StringBuffer();
//			sqlApps.append("select a.name,concat('"+Portal.serverpath+"','/',IFNULL(tai.icon,a.icon)) icon,");
//			sqlApps.append("IF(a.cloud_url is null,a.link,concat('"+contextPath+"','/',a.cloud_url,'/app-',a.id)) link ");
//			sqlApps.append("from bp_shop_page_app spa ");
//			sqlApps.append("join bp_app a on (a.status=1 and (a.show is null or a.show !='0') and a.delete_date is null and spa.app_id = a.id) ");
//			sqlApps.append("join bp_shop_page sp on (sp.shop_id=? and spa.page_id = sp.id) ");
//			sqlApps.append("join bp_temp t on (t.id=sp.template_id) ");
//			sqlApps.append("left join bp_temp_app_icon tai on (t.id = tai.template_id and tai.app_id = a.id)");
//			List<Record> thisApps = Db.find(sqlApps.toString(), new Object[]{shopId});
			sqlApps.append("select IF(a.cloud_url is null,a.link,concat('"+getRequest().getContextPath()+"','/',a.cloud_url,'/app-',a.id)) link,");
			sqlApps.append("concat('"+Portal.serverpath+"','/',ifnull(sac.icon,ifnull(tai.icon,a.icon))) icon,");
			sqlApps.append("ifnull(sac.name,a.name) name ");
			sqlApps.append("from bp_app a ");
			sqlApps.append("left join bp_shop_page_app spa on (spa.page_id=? and spa.app_id = a.id) ");
			sqlApps.append("left join bp_shop_page sp on (spa.page_id = sp.id) ");
			sqlApps.append("left join bp_temp_app_icon tai on (sp.template_id=tai.template_id and a.id=tai.app_id) ");
			sqlApps.append("left join bp_shop_app_custom sac on (sac.shop_id=? and sp.template_id=sac.template_id and sac.app_id=a.id) ");
			sqlApps.append("where spa.id is not null and a.template_id=? ");
			Object templateId = PageUtil.getTemplateId(shopId);
			List<Record> thisApps = Db.find(sqlApps.toString(), new Object[]{PageUtil.getPageIdByShopId(shopId),
				shopId,templateId});
//			if(!thisApps.isEmpty()){
			if(null != templateId && StringUtils.isNotBlank(templateId.toString())){
				apps = thisApps;
			}
		}
		return apps;
	}
	
	private Record getBottomAdv(Object shopId){
		String defaultImage = "index1/img/ad.png";
		String defaultLink = "http://www.pifii.com/";
		Record bottomAdv = new Record().set("image", defaultImage).set("link", defaultLink);
		if(null != shopId && StringUtils.isNotBlank(shopId.toString())){
			StringBuffer sql = new StringBuffer();
			sql.append("select sgr.image,sgr.link ");
			sql.append("from bp_shop_group_role sgr join bp_shop s on (s.id=? and sgr.shop_group_id=s.group_id) ");
			sql.append("join bp_adv_type bat on (sgr.adv_type_id=bat.id) ");
			sql.append("join bp_adv_spaces basp on (basp.adv_type='adv_bottom' and basp.id=bat.adv_spaces) ");
			sql.append("order by sgr.create_date desc ");
			Record thisBottomAdv = Db.findFirst(sql.toString(), new Object[]{shopId});
			if(null != thisBottomAdv){
				String image = thisBottomAdv.get("image");
				if(StringUtils.isBlank(image)){
					thisBottomAdv.set("image", defaultImage);
				}else{
					thisBottomAdv.set("image", Portal.serverpath+"/"+image);
				}
				
				String link = thisBottomAdv.get("link");
				if(StringUtils.isBlank(link)){
					thisBottomAdv.set("link", defaultLink);
				}
				bottomAdv = thisBottomAdv;
			}
		}
		return bottomAdv;
	}
	
}
