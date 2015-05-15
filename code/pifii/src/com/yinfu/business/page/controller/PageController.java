package com.yinfu.business.page.controller;


import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.UrlConfig;
import com.yinfu.business.commonpage.PageUtils;
import com.yinfu.business.page.model.Page;
import com.yinfu.business.page.model.PageApp;
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.PropertyUtils;
import com.yinfu.jbase.util.remote.DataSynUtil;
import com.yinfu.routersyn.task.GotoTask;
import com.yinfu.routersyn.task.IndexTask;
import com.yinfu.routersyn.task.SynAllTask;
import com.yinfu.routersyn.util.SynUtils;

@ControllerBind(controllerKey = "/business/page", viewPath = "")
public class PageController extends Controller<Object>{
//	private static Logger logger = Logger.getLogger(PageController.class);
	
	@Override
	public void index() {
		setAttr("page", Page.dao.findById(getPara("shopId"), "id,template_id,step,is_publish,update_date"));
		render(UrlConfig.BUSINESS + "/guideRight.jsp");
	}
	public void findById(){
		JSONObject returnData = new JSONObject();
		returnData.put("page", Page.dao.findById(getPara("shopId"), "is_publish"));
		renderJson(returnData);
	}
	public void findByShopId(){
		Record pageInfo = getPageInfo(getPara("shopId"));
		if(null == pageInfo){
			renderJson("");
		}else{
			renderJson(pageInfo);
		}
	}
	
	private Record getPageInfo(String shopId){
		StringBuffer sql = new StringBuffer();
		sql.append("select bsp.id,bsp.template_id,bsp.step,bsp.is_publish,bsp.update_date,bt.marker ");
		sql.append("from bp_shop_page bsp join bp_temp bt on (bsp.template_id = bt.id) ");
		sql.append("where bsp.shop_id=? ");
		Record pageInfo = Db.findFirst(sql.toString(), new Object[]{shopId});
		return pageInfo;
	}
	
	public void saveTemplate(){
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			boolean success = true;
			boolean isPageExist = false;
			String pageId = getPara("id");
			int step = -1;
			if(StringUtils.isNotBlank(pageId)){
				Page thisPage = Page.dao.findById(pageId, "step");
				if(null != thisPage){
					isPageExist = true;
					try{
						step = thisPage.getInt("step");
					}catch(Exception e){
					}
				}
			}
			Page page = new Page().set("template_id", getPara("templateId")).setDate("update_date");
			if(!isPageExist){
				success = page.setDate("create_date").set("step", 1).set("shop_id", getPara("shopId")).save();
			}else{
				page.set("id", pageId);
				if(step < 1){
					page.set("step", 1);
				}
				page.update();
			}
			if(success){
				List<String> sqls = new ArrayList<String>();
				String tips = "应用了模板";
				Record template = Db.findFirst("select name from bp_temp where id=? ", new Object[]{getPara("templateId")});
				if(null != template && null != template.get("name")){
					tips += "【"+template.getStr("name")+"】";
				}
				Record taskInfo = new Record().set("task_desc", tips);
				Map<String,List<File>> res = GotoTask.synRes(getPara("shopId"), sqls, taskInfo,null,null);
				if(null != res){
					SynUtils.putAllFiles(deleteRes, res);
				}else{
					return false;
				}
				if(sqls.size() > 0){
					DbExt.batch(sqls);
				}
			}
			return success;
		}});
		JSONObject returnData = new JSONObject();
		returnData.put("success", isSuccess);
		if(isSuccess){
			returnData.put("pageInfo", getPageInfo(getPara("shopId")));
			try{//兼容旧的盒子同步资源文件代码
				DataSynUtil.addTask(getPara("shopId"), "index_temp", getPara("templateId"), "1");
				PageUtil.changPageLog(getPara("shopId"), "index_temp", getPara("templateId"), "2");//记录更新日志
			}catch(Exception e){
				e.printStackTrace();
			}
//			if(isPageExist){
//				PageUtil.changPageLog(getPara("shopId"), "index_temp", getPara("templateId"), "2");//记录更新日志
//			}
		}
		renderJson(returnData);
	}
	public void changeInstallApp(){
		final Record actionRec = new Record();
		actionRec.set("action", "1");
		actionRec.set("operate", "1");
		
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			boolean success = false;
			StringBuffer sql = new StringBuffer();
			sql.append("select a.id appId,a.name,ifnull(spa.id,'') id ");
			sql.append("from bp_app a left join bp_shop_page_app spa on (spa.page_id=? and a.id=spa.app_id) ");
			sql.append("where a.id=? and a.delete_date is null ");
			Record app =Db.findFirst(sql.toString(), new Object[]{getPara("pageId"),getPara("appId")});
			if(null != app){
				String operateDesc = "";
				if("1".equals(getPara("status")) && StringUtils.isBlank(app.get("id").toString())){//安装应用
					operateDesc = "安装了应用【"+app.getStr("name")+"】";
					success =  new PageApp().set("page_id", getPara("pageId")).set("app_id", getPara("appId")).saveAndCreateDate();
				}else if("0".equals(getPara("status")) && StringUtils.isNotBlank(app.get("id").toString())){//删除应用
					operateDesc = "删除了应用【"+app.getStr("name")+"】";
					success = PageApp.dao.deleteById(app.get("id"));
					actionRec.set("action", "2");
					actionRec.set("operate", "3");
				}
				if(success){
					Record record= Db.findFirst("select shop_id from bp_shop_page where id=? ", new Object[]{getPara("pageId")});
					if(null != record){
						List<String> sqls = new ArrayList<String>();
						Record taskInfo = new Record().set("task_desc", operateDesc);
						Map<String,List<File>> res = IndexTask.synRes(record.get("shop_id"), sqls, taskInfo,null,null);
						if(null != res){
							SynUtils.putAllFiles(deleteRes, res);
							if(sqls.size() > 0){
								DbExt.batch(sqls);
							}
							success = true;
						}else{
							success = false;
						}
					}
				}
			}
			return success;
		}});
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));
			try{
				Record record= Db.findFirst("select shop_id from bp_shop_page where id=? ", new Object[]{getPara("pageId")});
				if(null != record){
					boolean success = DataSynUtil.addTask(record.get("shop_id").toString(), "index_app", getPara("appId"), actionRec.getStr("action"));
//					if(success){
						PageUtil.changPageLog(PageUtil.getShopId(getPara("pageId")), "index_app", getPara("appId"), actionRec.getStr("operate"));//记录更新日志
//					}
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}else{
			SynUtils.deleteRes(deleteRes.get("fail"));
		}
		renderJsonResult(isSuccess);
	}
	public void changePageStep(){
		int step = -1;
		String pageId = getPara("pageId");
		Page thisPage = Page.dao.findById(pageId, "step");
		try{
			step = thisPage.getInt("step");
		}catch(Exception e){
		}
		boolean success = false;
		if(step < getParaToInt("step")){
			success = new Page().set("id", pageId).set("step", getPara("step")).update();
		}else{
			success = true;
		}
		renderJsonResult(success);
	}
	public void listInstalledApp(){
		StringBuffer sql = new StringBuffer();
//		sql.append("select a.id,ifnull(a.edit_url,'') edit_url,a.name,a.icon,ifnull(a.des,'') des ");
//		sql.append("from bp_app a left join bp_shop_page_app spa on (spa.page_id=? and spa.app_id = a.id) ");
//		sql.append("where spa.id is not null or a.show = '0' order by a.edit_url desc ");
//		List<Record> apps = Db.find(sql.toString(), new Object[]{getPara("pageId")});
		
		sql.append("select a.id,ifnull(a.edit_url,'') edit_url,");
		sql.append("ifnull(sac.icon,ifnull(tai.icon,a.icon)) icon,ifnull(sac.name,a.name) name,");
		sql.append("ifnull(a.des,'') des ");
		sql.append("from bp_app a left join bp_shop_page_app spa on (spa.page_id=? and spa.app_id = a.id) ");
		sql.append("left join bp_shop_page sp on (spa.page_id = sp.id) ");
		sql.append("left join bp_temp_app_icon tai on (sp.template_id=tai.template_id and a.id=tai.app_id) ");
		sql.append("left join bp_shop_app_custom sac on (sac.shop_id=? and sp.template_id=sac.template_id and sac.app_id=a.id) ");
		sql.append("where (spa.id is not null or a.show = '0') and a.template_id=? order by a.edit_url desc ");
		List<Record> apps = Db.find(sql.toString(), new Object[]{getPara("pageId"),
			PageUtil.getShopId(getPara("pageId")),PageUtil.getTemplateIdByPageId(getPara("pageId"))});
		renderJson(apps);
	}
	public void publishPage(){
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			boolean success = new Page().set("id", getPara("pageId")).set("step", getPara("step")).set("is_publish", "1").update();
			if(success){
				List<String> sqls = new ArrayList<String>();
				Record rec = Db.findFirst("select shop_id from bp_shop_page where id=? ", new Object[]{getPara("pageId")});
				sqls.add("delete brt from bp_res_task brt join bp_device d on (d.shop_id='"+rec.get("shop_id").toString()+"' and brt.router_sn=d.router_sn)");
				Record taskInfo = new Record().set("task_desc", "发布Portal页面");
				Map<String,List<File>> res = SynAllTask.synRes(rec.get("shop_id"), sqls, taskInfo,null,null);
				if(null != res){
					SynUtils.putAllFiles(deleteRes, res);
					if(sqls.size()>0){
						DbExt.batch(sqls);
					}
					success = true;
				}else{
					success = false;
				}
			}
			return success;
		}});
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));
			try{
				Record record= Db.findFirst("select shop_id from bp_shop_page where id=? ", new Object[]{getPara("pageId")});
				if(null != record){
					DataSynUtil.publish(record.getInt("shop_id"));
				}
//				PageUtil.changPageLog(PageUtil.getShopId(getPara("pageId")), "index_page", getPara("pageId"), "4");//记录更新日志
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}else{
			SynUtils.deleteRes(deleteRes.get("fail"));
		}
		renderJsonResult(isSuccess);
	}
	//@formatter:off 
	/**
	 * Title: loadBackImg
	 * Description:加载模拟器的背景图
	 * Created On: 2014年8月6日 下午4:46:25
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void loadBackImg(){
		renderJson(new PageUtils().getPageAdvPicture(getPara("shopId")));
		
//		String shopId = getPara("shopId");
//		String content= "";
//		List<Adv> list = Adv.dao.getListByShopId(shopId);
//		String cxt = Fs.getContextAllPath(getRequest());
//		for(Adv adv :list){
//			if(StringUtils.isNotBlank(adv.get("image").toString())){
//				content += "<li>"+
//							"	<a class='pic' href='javascript:void(0);'><img src="+cxt+"/"+adv.get("image")+" onerror=\"this.src='"+cxt+"/images/business/ad-1.jpg'\" /></a>"+
//							"</li>";
//			}
//		}
//        renderHtml(content);
	}
	//@formatter:off 
	/**
	 * Title: loadAppInfo
	 * Description:加载模拟器app应用信息显示
	 * Created On: 2014年8月6日 下午6:42:59
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void loadAppInfo(){
		renderJson(new PageUtils().getPageApp(getPara("shopId")));
		
//		String shopId = getPara("shopId");
//		List<App> list = App.dao.queryInstalledApp(shopId);
//		String content= "<ul>";
//		String cxt = Fs.getContextAllPath(getRequest());
//		for(App app :list){
//			content += "<li>"+
//						 "<a href='javascript:void(0);' class='pic' ><img src='"+cxt+"/"+app.get("icon")+"'/></a>"+
//						 "<p>"+app.get("name")+"</p>"+
//						"</li>";
//		}
//		content += "</ul>";
//        renderHtml(content);
	}
	
	public void getPageChangeLog(){
		String isOnline = "-1";
		String routerSn = "";
		if(StringUtils.isNotBlank(getPara("deviceId"))){
			int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);//路由上报数据的时间间隔,要考虑网络延迟
			Record rec = Db.findFirst("select IF(date_add(report_date, interval "+interval+" second) > now(),1,0) is_online,router_sn from bp_device where id=? ", 
					new Object[]{getPara("deviceId")});
			if(null != rec){
				isOnline = rec.getLong("is_online")+"";
				routerSn = rec.getStr("router_sn");
			}
		}
		StringBuffer sqlSelect = new StringBuffer();
		sqlSelect.append("select t.file_size,t.step,t.progress,t.task_desc,t.operate_date,"+isOnline+" isOnline,");
		sqlSelect.append("substring_index(GROUP_CONCAT(ifnull(rte.error_msg,'') Order BY rte.error_date desc),',',1) error_msg ");
		StringBuffer sqlFrom = new StringBuffer("from bp_res_task t left join bp_res_task_error rte on (t.id=rte.task_id) ");
		sqlFrom.append("where t.is_show and t.router_sn=? ");
		if(StringUtils.isNotBlank(getPara("startDate"))){
			sqlFrom.append("and t.operate_date > '"+getPara("startDate")+"' ");
		}
		if(StringUtils.isNotBlank(getPara("endDate"))){
			sqlFrom.append("and date(t.operate_date) <= '"+getPara("endDate")+"' ");
		}
		sqlFrom.append("group by t.id ");
		sqlFrom.append("order by operate_date desc ");
		com.jfinal.plugin.activerecord.Page<Record> returnData = Db.paginate(getParaToInt("pageNum"), getParaToInt("pageSize"), 
				sqlSelect.toString(), sqlFrom.toString(),new Object[]{routerSn});
		Iterator<Record> ite = returnData.getList().iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			rec.set("file_size", changeSizeShow(rec.getInt("file_size")));
			rec.set("progress", decimalToPrecent(rec.getFloat("progress")));
		}
		renderJson(returnData);
	}
	
	private String changeSizeShow(long byteSize){
		String show = "";
		if(byteSize/1024 < 1){
			show = byteSize+"   byte";
		}else if(byteSize/1024/1024 < 1){
			show = byteSize/1024+"   KB";
		}else if(byteSize/1024/1024/1024 < 1){
			int size = (int) (byteSize/1024/1024.0*100);
			show = size/100.0+"   MB";
		}else{
			int size = (int) (byteSize/1024/1024.0*100);
			show = size/100.0+"   GB";
		}
		return show;
	}
	
	private String decimalToPrecent(float precent){
		int newPrecent = (int)(precent*100);
		return newPrecent+"%";
	}
	
	public void getPageChangeLog_old(){
		String isOnline = "-1";
		if(StringUtils.isNotBlank(getPara("deviceId"))){
			int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);//路由上报数据的时间间隔,要考虑网络延迟
			Record rec = Db.findFirst("select IF(date_add(report_date, interval "+interval+" second) > now(),1,0) is_online from bp_device where id=? ", 
					new Object[]{getPara("deviceId")});
			if(null != rec){
				isOnline = rec.getLong("is_online")+"";
			}
		}
		StringBuffer sql = new StringBuffer();
//		sql.append(getBaseSql("index_page","发布了主页"));
//		sql.append("UNION ");
		sql.append(getDataSql("index_shop","","","商铺信息"));
		sql.append("UNION ");
		sql.append(getTemplateSql());
		sql.append("UNION ");
		sql.append(getDataSql("index_app","bp_app","name","应用"));
		sql.append("UNION ");
//		sql.append(getDataSql("index_adv","bp_adv","des","广告"));
		sql.append(getDataSql("index_adv","bp_adv_content","name","广告"));
		sql.append("UNION ");
		sql.append(getDataSql("flowpack","bp_flow_pack","title","优惠包"));
		sql.append("UNION ");
		sql.append(getDataSql("funny","bp_funny","title","搞笑段子"));
		sql.append("UNION ");
		sql.append(getDataSql("menu","bp_menu","name","菜单"));
		sql.append("UNION ");
		sql.append(getDataSql("preferential","bp_preferential","title","最新优惠"));
		sql.append("UNION ");
		sql.append(getDataSql("tide","bp_tide","name","潮机推荐"));
		sql.append("UNION ");
		sql.append(getDataSql("introduce","","","商铺介绍信息"));
		
		sql.append("order by operate_date desc ");
//		List<Record> apps = Db.find(sql.toString());
//		Page<Record> dataList = Db.paginate(pageNumber, pageSize, select, sqlExceptSelect, paras);
		
		com.jfinal.plugin.activerecord.Page<Record> returnData = Db.paginate(getParaToInt("pageNum"), getParaToInt("pageSize"), "select a.*,"+isOnline+" isOnline", 
				"from ("+sql.toString()+") a ");
		renderJson(returnData);
	}
	private String getDataSql(String dataType,String tableName,String fieldName,String description){
		StringBuffer sql = new StringBuffer();
		sql.append("select d.name routerName,po.operate_date,if(po.status = 1,1,0) status,po.type,IFNULL(TIMESTAMPDIFF(SECOND, po.download_date, now()),-1) downloadSeconds, ");
		sql.append("concat(case po.operate_type when '1' then '添加了' when '2' then '修改了' when '3' then '删除了' else '' end,'"+description+"' ");
		if(StringUtils.isNotBlank(fieldName)){
			sql.append(",IF(a."+fieldName+" is null,'',concat('【',a."+fieldName+",'】'))");
		}
		sql.append(") operateDes ");
		sql.append("from bp_page_operate po join bp_device d on (");
		if(StringUtils.isNotBlank(getPara("deviceId"))){
			sql.append("d.id = '"+getPara("deviceId")+"' and ");
		}
		sql.append("d.shop_id='"+getPara("shopId")+"' and po.router_sn = d.router_sn) ");
		if(StringUtils.isNotBlank(fieldName)){
			sql.append("join "+tableName+" a on (po.key_id = a.id) ");
		}
		sql.append("where po.type='"+dataType+"' ");
		if(StringUtils.isNotBlank(getPara("startDate"))){
			sql.append("and po.operate_date > '"+getPara("startDate")+"' ");
		}
		if(StringUtils.isNotBlank(getPara("endDate"))){
			sql.append("and date(po.operate_date) <= '"+getPara("endDate")+"' ");
		}
		return sql.toString();
	}
	private String getTemplateSql(){
		StringBuffer sql = new StringBuffer();
		sql.append("select d.name routerName,po.operate_date,if(po.status = 1,1,0) status,po.type,IFNULL(TIMESTAMPDIFF(SECOND, po.download_date, now()),-1) downloadSeconds, ");
		sql.append("concat('应用了','样式【',IFNULL(t.name,''),'】') operateDes ");
		sql.append("from bp_page_operate po join bp_device d on (");
		if(StringUtils.isNotBlank(getPara("deviceId"))){
			sql.append("d.id = '"+getPara("deviceId")+"' and ");
		}
		sql.append("d.shop_id='"+getPara("shopId")+"' and po.router_sn = d.router_sn) ");
		sql.append("join bp_temp t on (po.key_id = t.id) ");
		sql.append("where po.type='index_temp' ");
		if(StringUtils.isNotBlank(getPara("startDate"))){
			sql.append("and po.operate_date > '"+getPara("startDate")+"' ");
		}
		if(StringUtils.isNotBlank(getPara("endDate"))){
			sql.append("and date(po.operate_date) <= '"+getPara("endDate")+"' ");
		}
		return sql.toString();
	}
}
