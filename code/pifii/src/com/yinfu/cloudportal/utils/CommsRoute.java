package com.yinfu.cloudportal.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.PageUtil;
import com.yinfu.cloudportal.Portal;
import com.yinfu.jbase.util.PropertyUtils;

public class CommsRoute {
	private static Logger logger = Logger.getLogger(CommsRoute.class);
	private static final String DEFAULT_SHOP_NAME = "尊敬的商户";
	
	public static boolean isPhoneAuthed(String mac,String phone){
		if(StringUtils.isBlank(mac)){
			return false;
		}
		int phoneAuthExpireLength = PropertyUtils.getPropertyToInt("auth.phone.expire.length", 30);
		Record record = Db.findFirst("select id from bp_auth where auth_type='phone' and client_mac=? and tag=? and To_Days(now())-To_Days(auth_date) < ? ",
				new Object[]{mac,phone,phoneAuthExpireLength});
		if(null != record){
			return true;
		}
		return false;
	}
	
	public static List<Record> authedPhones(Object clientMac){
		if(null == clientMac || StringUtils.isBlank(clientMac.toString())){
			return new ArrayList<Record>();
		}
		int phoneAuthExpireLength = PropertyUtils.getPropertyToInt("auth.phone.expire.length", 30);
		return Db.find("select distinct tag phone from bp_auth where auth_type='phone' and client_mac=? and To_Days(now())-To_Days(auth_date) < ? order by auth_date desc", 
				new Object[]{clientMac,phoneAuthExpireLength});
	}
	
	public static int getOnlineTime(String sn){
		int timeOut = PropertyUtils.getPropertyToInt("router.auth.onlineTime");
		try{
			Record record = Db.findFirst("select time_out from bp_device where router_sn=? ", new Object[]{sn});
			if(record != null){
				timeOut = record.getInt("time_out")*60;
				return timeOut;
			}
		}catch(Exception e){
		}
		return timeOut;
	}
	
	public static void addAuthLog(String sn,String mac,String tag,String authType,int timeLength){
		try {
			Db.update("insert into bp_auth(tag,router_sn,auth_type,client_mac,auth_date) values(?,?,?,?,now())", 
					new Object[]{tag,sn,authType,mac});
			if(StringUtils.isNotBlank(sn) && StringUtils.isNotBlank(mac)){
				groupPassList(sn,mac,timeLength);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("插入验证日志失败:", e);
		}
	}

	public static List<Record> getBannerAvds(Object shopId){
		List<Record> bannerAdvs = new ArrayList<Record>();
		//添加默认的广告
//		bannerAdvs.add(new Record().set("image", Portal.serverpath+"/images/business/ad-1.jpg").set("link", "#"));
		bannerAdvs.add(new Record().set("image", Portal.serverpath+"/images/business/adv/default01.jpg").set("link", "#"));
		bannerAdvs.add(new Record().set("image", Portal.serverpath+"/images/business/adv/default02.jpg").set("link", "#"));
		if(null != shopId && StringUtils.isNotBlank(shopId.toString())){
			Record shop = Db.findFirst("select id from bp_shop where id=?", new Object[]{shopId});
			if(null != shop){
				StringBuffer sql = new StringBuffer();
				sql.append("select concat('"+Portal.serverpath+"','/',ifnull(batr.res_url,'images/business/ad-1.jpg')) image,concat('','adv?id=',bac.id) link ");
				sql.append("from bp_adv_shop bas join bp_adv_spaces basp on (bas.shop_id=? and basp.adv_type='adv' and basp.id=bas.adv_spaces) ");
				sql.append("join bp_adv_type bat on (bat.template_id=? and basp.id=bat.adv_spaces) ");
				sql.append("join bp_adv_content bac on (bas.content_id=bac.id) ");
				sql.append("join bp_adv_type_res batr on (batr.adv_type_id=bat.id and batr.content_id=bac.id) ");
				sql.append("order by basp.adv_index ");
				List<Record> thisBannerAdvs = Db.find(sql.toString(), new Object[]{shopId,PageUtil.getTemplateId(shopId)});
				if(!thisBannerAdvs.isEmpty()){
					bannerAdvs = thisBannerAdvs;
				}else{
					bannerAdvs.clear();
					bannerAdvs.add(new Record().set("image", Portal.serverpath+"/images/business/ad-1.jpg").set("link", "#"));
				}
			}
		}
		return bannerAdvs;
	}
	
	public static Record getStartAdv(Object shopId){
		String defaultLink = "http://www.pifii.com/";
		String defaultImg = "index1/img/transition.png";
		Record returnRec = new Record().set("image", defaultImg).set("link", defaultLink);
		if(null != shopId && StringUtils.isNotBlank(shopId.toString())){
			StringBuffer sql = new StringBuffer();
			sql.append("select ifnull(bac.name,'') name,if(batr.res_url is null or batr.res_url ='','',concat('"+Portal.serverpath+"','/',batr.res_url)) image,ifnull(bac.link,'') link ");
			sql.append("from bp_adv_shop bas join bp_adv_spaces basp on (bas.shop_id=? and basp.adv_type='adv_start' and basp.id=bas.adv_spaces) ");
			sql.append("join bp_adv_type bat on (basp.id=bat.adv_spaces) ");
			sql.append("join bp_adv_content bac on (bas.content_id=bac.id) ");
			sql.append("join bp_adv_type_res batr on (batr.adv_type_id=bat.id and batr.content_id=bac.id) ");
			sql.append("order by bac.update_date desc ");
			Record thisRec = Db.findFirst(sql.toString(), new Object[]{shopId});
			if(null != thisRec){
				returnRec = thisRec;
				String link = returnRec.get("link");
				if(StringUtils.isBlank(link)){
					returnRec.set("link", defaultLink);
				}
				String img = returnRec.get("image");
				if(StringUtils.isBlank(img)){
					returnRec.set("image", defaultImg);
				}
			}
		}
		Record rec = Db.findFirst("select ifnull(name,'') name from bp_shop where id=? ", new Object[]{shopId});
		if(null != rec && StringUtils.isNotBlank(rec.get("name").toString())){
			returnRec.set("shopName",rec.get("name").toString());
		}else{
			returnRec.set("shopName",DEFAULT_SHOP_NAME);
		}
		return returnRec;
	}
	
	public static Record getPageShopInfo(Object shopId){
//		String defaultIcon = "index1/img/morentouxiang.png";
		String defaultIcon = "index1/img/default_logo.jpg";
		String defaultTel = "(您的电话)";
		String defaultAddr = "(您的地址)";
		Record shopInfo = new Record().set("name",DEFAULT_SHOP_NAME).set("addr",defaultAddr).set("tel",defaultTel).set("icon",defaultIcon);
		if(null != shopId && StringUtils.isNotBlank(shopId.toString())){
			StringBuffer sqlShopInfo = new StringBuffer();
			sqlShopInfo.append("select ifnull(name,'"+DEFAULT_SHOP_NAME+"') name,ifnull(addr,'"+defaultAddr+"') addr,ifnull(tel,'"+defaultTel+"') tel,");
//			sqlShopInfo.append("concat('"+Portal.serverpath+"','/',ifnull(icon,'')) icon ");
			sqlShopInfo.append("IF(icon is null,'"+defaultIcon+"' ,concat('"+Portal.serverpath+"','/',icon)) icon ");
			sqlShopInfo.append("from bp_shop ");
			sqlShopInfo.append("where id=? ");
			Record thisShopInfo = Db.findFirst(sqlShopInfo.toString(), new Object[]{shopId});
			if(null!= thisShopInfo){
				shopInfo = thisShopInfo;
			}
		}
		return shopInfo;
	}
	
	public static void addAccessData(String routerSN,String clientMac,String id,String type){
		if(StringUtils.isNotBlank(id)){
			try{
				if(StringUtils.isBlank(routerSN)){
					routerSN = "";
				}
				if(StringUtils.isBlank(clientMac)){
					clientMac = "";
				}
				Db.update("insert into bp_statistics_all(router_sn,client_mac,statistics_id,statistics_type,access_date,create_date) "
						+ "values(?,?,?,?,now(),now()) ", new Object[]{routerSN,clientMac,id,type});
			}catch(Exception e){
			}
		}
		
	}
	
	/***
	 * 群组功能
	 * @param sn
	 * @param mac
	 */
	public static void groupPassList(String sn,String mac,int timeLength){
		try{
			StringBuffer sql = new StringBuffer();
			sql.append("select d2.router_sn,if(d2.router_sn=?,1,0) is_me ");
			sql.append("from bp_device d1 join bp_device d2 on (d1.router_sn=? and d1.shop_id=d2.shop_id) ");
//			String sql = "select router_sn from bp_device where shop_id =(select shop_id from bp_device where router_sn= ?)";
			List<Record> list = Db.find(sql.toString(), new Object[]{sn,sn});
			List<String> sqlList = new ArrayList<String>();
//			sqlList.add("delete from bp_pass_list where mac='"+mac+"'");//如果有之前认证的时间信息则删除，避免一个mac出现多个认证信息
			for(Record rd : list){
				sqlList.add("insert into bp_pass_list(sn,mac,begin_time,end_time,is_me) values('"+rd.getStr("router_sn")+"','"+mac+"',now(),DATE_ADD(NOW(),INTERVAL "+timeLength+" SECOND),"+rd.get("is_me").toString()+")");
			}
			if(sqlList.size() > 0){
				Db.batch(sqlList, sqlList.size());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public static String getAppCustomName(Object appId,Object shopId){
		StringBuffer sql = new StringBuffer();
		sql.append("select ifnull(sac.name,a.name) name ");
		sql.append("from bp_app a ");
		sql.append("left join bp_shop_app_custom sac on (sac.shop_id=? and a.template_id=sac.template_id and sac.app_id=a.id) ");
		sql.append("where a.id=? ");
		Record app = Db.findFirst(sql.toString(), new Object[]{shopId,appId});
		String appName = "";
		if(null != app && null != app.get("name")){
			appName = app.get("name").toString();
		}
		return appName;
	}
}
