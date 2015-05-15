package com.yinfu.cloudportal.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.jfinal.aop.Before;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.cloudportal.RouterInterceptor;
import com.yinfu.cloudportal.interceptor.BlackListMacInterceptor;
import com.yinfu.cloudportal.interceptor.BlackListPhoneInterceptor;
import com.yinfu.cloudportal.utils.CommsPhoneCode;
import com.yinfu.cloudportal.utils.CommsRoute;
import com.yinfu.cloudportal.utils.SessionParams;
import com.yinfu.jbase.jfinal.ext.Controller;

@Before({RouterInterceptor.class,BlackListMacInterceptor.class,BlackListPhoneInterceptor.class})
@ControllerBind(controllerKey = "/portal/mb/auth", viewPath = "/portal/mb/auth")
public class Phone extends Controller<Record>{
	
	public void index(){
		Record client = getSessionAttr(SessionParams.CLIENT_INFO);
		Record device = getSessionAttr(SessionParams.ACCESS_DEVICE);
		StringBuffer sql = new StringBuffer();
		sql.append("select bat.name,bat.url,bat.marker,if(bas.id is null,0,1) used ");
		sql.append("from bp_auth_type bat left join bp_auth_setting bas on (bas.shop_id=? and bat.id=bas.auth_type_id) ");
		sql.append("where bat.is_used = 1 ");
		sql.append("group by bat.id ");
		List<Record> authTypes = Db.find(sql.toString(), new Object[]{device.get("shop_id")});//所有的认证方式
		
		String defaultAuthType = "";//用户默认选择的认证方式
		if(StringUtils.isNotBlank(getPara("type"))){
			defaultAuthType = getPara("type");
		}else{
			defaultAuthType = "phone";
		}
		String useAuthType = "";//必须使用的认证方式
		String cmd = getPara("cmd");
		if("code".equals(cmd) || "auth".equals(cmd)){//手机认证
			useAuthType = "phone";
		}
		if("code".equals(cmd)){//发送验证码
//			setAttr("cmd", "code");
			JSONObject json = CommsPhoneCode.getCode(getRequest());
			String success = json.getString("success");
			setAttr("success", success);
			if(!"1".equals(success)){
				setAttr("msg", json.get("msg"));
			}
		}else if("auth".equals(cmd)){//手机认证
//			setAttr("cmd", "auth");
			JSONObject json = CommsPhoneCode.auth(getRequest(), device.getStr("router_sn"), client.getStr("client_mac"));
			String success = json.getString("success");
			if(!"1".equals(success)){//认证失败
				setAttr("msg", json.get("msg"));
			}else{
				redirect("/authorizeAccess?key="+json.getString("key"));//认证成功
				return;
			}
		}
		List<Record> showAuthTypes = new ArrayList<Record>();
		Iterator<Record> ite = authTypes.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			if(useAuthType.equals(rec.get("marker").toString())//必须使用的认证方式，即使没有配置也要加到认证方式列表中
					|| "1".equals(rec.get("used").toString())){//商户配置了该认证方式
				showAuthTypes.add(rec);
				if(StringUtils.isBlank(useAuthType) && defaultAuthType.equals(rec.get("marker").toString())){
					useAuthType = rec.get("marker").toString();
				}
			}
		}
		if(StringUtils.isBlank(useAuthType) && showAuthTypes.size() > 0){
			useAuthType = showAuthTypes.get(0).get("marker").toString();
		}
		String pageUrl = "";
		if(StringUtils.isBlank(useAuthType)){
			Iterator<Record> iteAt = authTypes.iterator();
			while(iteAt.hasNext()){
				Record rec = iteAt.next();
				if(useAuthType.equals(rec.get("marker").toString())){
					pageUrl = rec.get("url").toString();
				}
			}
		}
		if("phone".equals(useAuthType)){
			setAttr("phones", JsonKit.toJson(CommsRoute.authedPhones(client.get("client_mac"))));
			setAttr("timeSeconds", CommsPhoneCode.getPhoneCodeTimeleft(getRequest()));//设置获取验证码的剩余时长
		}
		setAttr("useAuthType", useAuthType);
		setAttr("pageUrl", pageUrl);
		setAttr("showAuthTypes", showAuthTypes);
		render("index.jsp");
	}
	
//	private String getLocalMainPage(String routerSn,String mac){
//		String indexPage = PropertyUtils.getProperty("router.mainPage", "http://m.pifii.com/ifidc/pifii.html");
//		if(indexPage.indexOf("?") == -1){
//			indexPage += "?";
//		}else{
//			indexPage += "&";
//		}
//		indexPage += "routersn="+routerSn+"&mac="+mac;
//		return indexPage;
//	}
}

