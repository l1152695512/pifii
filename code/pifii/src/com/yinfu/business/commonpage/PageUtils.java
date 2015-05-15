package com.yinfu.business.commonpage;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.PageUtil;

/**
 * 这个类中的方法是查询手机中（手机模拟器预览、手机请求的主页）显示的数据
 * 解决的问题：所有手机的数据显示保持一致，做改动也只用改一份
 * @author l
 *
 */
public class PageUtils {
	public List<Record> getPageApp(Object shopId){//这个类用于之前做的云端模板页面app，已废弃
		StringBuffer sqlApps = new StringBuffer();
		sqlApps.append("select a.name,IFNULL(tai.icon,a.icon) icon,concat('advServlet?cmd=app&id=',a.id) link ");
		sqlApps.append("from bp_shop_page_app spa ");
		sqlApps.append("join bp_app a on (a.status=1 and (a.show is null or a.show !='0') and a.delete_date is null and spa.app_id = a.id) ");
		sqlApps.append("join bp_shop_page sp on (sp.shop_id=? and spa.page_id = sp.id) ");
		sqlApps.append("join bp_temp t on (t.id=sp.template_id) ");
		sqlApps.append("left join bp_temp_app_icon tai on (t.id = tai.template_id and tai.app_id = a.id) ");
		return Db.find(sqlApps.toString(), new Object[]{shopId});
	}
	
	public List<Record> getPageAdvPicture(Object shopId){
		StringBuffer sql = new StringBuffer();
		sql.append("select bas.content_id id,ifnull(batr.res_url,'') image ");
		sql.append("from bp_adv_shop bas join bp_adv_spaces basp on (bas.shop_id=? and basp.adv_type='adv' and basp.id=bas.adv_spaces) ");
		sql.append("join bp_adv_type bat on (bat.template_id=? and bat.adv_spaces=basp.id) ");
		sql.append("join bp_adv_type_res batr on (batr.adv_type_id=bat.id and batr.content_id=bas.content_id) ");
		sql.append("order by basp.adv_index ");
//		sql.append("select bas.content_id id,ifnull(batr.res_url,'') image ");
//		sql.append("from bp_adv_shop bas join bp_adv_type bat on (bas.shop_id=? and bat.template_id=? and bat.id=bas.adv_type_id) ");
//		sql.append("join bp_adv_spaces basp on (basp.adv_type='adv' and basp.id=bat.adv_spaces) ");
//		sql.append("join bp_adv_type_res batr on (batr.adv_type_id=bat.id and batr.content_id=bas.content_id) ");
//		sql.append("order by basp.adv_index ");
		Object templateId = PageUtil.getTemplateId(shopId);
		return Db.find(sql.toString(), new Object[]{shopId,templateId});
	}
	
	public Record getPageShopInfo(Object shopId){
		StringBuffer sqlShopInfo = new StringBuffer();
		sqlShopInfo.append("select ifnull(name,'') name,ifnull(addr,'') addr,ifnull(tel,'') tel,ifnull(icon,'images/business/userPic/morentouxiang.png') icon ");
		sqlShopInfo.append("from bp_shop ");
		sqlShopInfo.append("where id=? ");
		return Db.findFirst(sqlShopInfo.toString(), new Object[]{shopId});
	}
	
	public Record getPagePath(Object shopId,boolean isPreview){
		String fileName = "show.jsp";
		if(isPreview){
			fileName = "preview.jsp";
		}
		StringBuffer strB = new StringBuffer();
		strB.append("select concat(t.page_path,'"+fileName+"') page_path ");
		strB.append("from bp_temp t join bp_shop_page sp on (sp.shop_id=? and sp.template_id = t.id) ");
		return Db.findFirst(strB.toString(), new Object[]{shopId});
	}
	
}
