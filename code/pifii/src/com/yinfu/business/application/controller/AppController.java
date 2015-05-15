package com.yinfu.business.application.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.application.model.App;
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.remote.DataSynUtil;
import com.yinfu.routersyn.task.IndexTask;
import com.yinfu.routersyn.util.SynUtils;

@ControllerBind(controllerKey = "/business/app", viewPath = "/page/business/application")
public class AppController extends Controller<App>
{
	private static final String APP_CUSTOM_PATH = "upload" + File.separator + "image" + File.separator + "app" + File.separator;
	
	public void list()
	{
		renderJson(App.dao.getAll());
	}
	
	public void getAppType()
	{
		renderJson(App.dao.getAppType());
	}
	
	public void listAppWithInstallStatus(){
		StringBuffer sql = new StringBuffer();
//		sql.append("select a.id,ifnull(a.edit_url,'') edit_url,a.classify,a.name,a.icon,if(spa.id is null,'0','1') is_install,a.show,ifnull(a.des,'') des ");
//		sql.append("from bp_app a left join bp_shop_page_app spa on (spa.page_id=? and a.id = spa.app_id) ");
//		sql.append("where a.delete_date is null and a.status=1 ");
//		sql.append("order by a.create_date ");
//		List<Record> apps = Db.find(sql.toString(), new Object[]{getPara("pageId")});
		
		sql.append("select a.id,ifnull(a.edit_url,'') edit_url,a.classify,");
		sql.append("ifnull(sac.icon,ifnull(tai.icon,a.icon)) icon,ifnull(sac.name,a.name) name,");
		sql.append("if(spa.id is null,'0','1') is_install,a.show,ifnull(a.des,'') des ");
		sql.append("from bp_app a left join bp_shop_page_app spa on (spa.page_id=? and a.id = spa.app_id) ");
		sql.append("left join bp_shop_page sp on (spa.page_id = sp.id) ");
		sql.append("left join bp_temp_app_icon tai on (tai.template_id=? and a.id=tai.app_id) ");
		sql.append("left join bp_shop_app_custom sac on (sac.shop_id=? and sac.template_id=? and sac.app_id=a.id) ");
		sql.append("where a.delete_date is null and a.status=1 and a.template_id=? ");
		sql.append("order by a.create_date ");
		Object templateId = PageUtil.getTemplateIdByPageId(getPara("pageId"));
		List<Record> apps = Db.find(sql.toString(), new Object[]{getPara("pageId"),templateId,
			PageUtil.getShopId(getPara("pageId")),templateId,templateId});
		renderJson(apps);
	}
	
	public void appInfo(){
		Object pageId = PageUtil.getPageIdByShopId(getPara("shopId"));
		StringBuffer sql = new StringBuffer();
		sql.append("select a.id,if(a.des is null or a.des ='','暂无描述',a.des) des,");
		sql.append("ifnull(sac.icon,ifnull(tai.icon,a.icon)) icon,ifnull(sac.name,a.name) name,ifnull(sac.id,'') custom_app_id ");
		sql.append("from bp_app a left join bp_shop_page_app spa on (a.id=spa.app_id and spa.page_id=?) ");
		sql.append("left join bp_shop_page sp on (spa.page_id = sp.id) ");
		sql.append("left join bp_temp_app_icon tai on (tai.template_id=? and a.id=tai.app_id) ");
		sql.append("left join bp_shop_app_custom sac on (sac.shop_id=? and sac.template_id=? and sac.app_id=a.id)");
		sql.append("where a.id=?");
		Object templateId = PageUtil.getTemplateIdByPageId(pageId);
		Record rec = Db.findFirst(sql.toString(), new Object[]{pageId,templateId,getPara("shopId"),templateId,getPara("appId")});
		if(null != rec){
			setAttr("id", rec.get("id"));
			setAttr("icon", rec.get("icon"));
			setAttr("name", rec.get("name"));
			setAttr("des", rec.get("des"));
			setAttr("custom_app_id", rec.get("custom_app_id"));
		}
		render("appNote.jsp");
	}
	public void saveAppInfo(){
		setAttr("appIcon", getPara("appIcon"));
		setAttr("appName", getPara("appName"));
		String des = getPara("des");
		if(StringUtils.isBlank(des)){
			des = "暂无描述";
		}
		setAttr("des", des);
		render("appNote.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: toPageByAppId
	 * Description:根据应用ID跳转页面
	 * Created On: 2014年7月30日 下午1:42:37
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void toPageByAppId(){
	    int id = getParaToInt("id");
		App app = App.dao.findById(id);
		/*List<Shop> shopList = Shop.dao.list();*/
		if(app.get("name").equals("点餐")){
			setAttr("shopId", "1");
			render("");
		}
		if(app.get("name").equals("优惠券")){
			setAttr("shopId", "1");
			render("/page/business/application/coupon/couponList.jsp");
		}
	}
	
	public void saveCustomAppName(){
		final Record changeApp = getChangeAppInfo();
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			boolean success = true;
			String customAppId = getPara("custom_app_id");
			if(StringUtils.isBlank(customAppId)){
				Object templateId = PageUtil.getTemplateId(getPara("shopId"));
				Record rec = Db.findFirst("select id from bp_shop_app_custom where shop_id=? and template_id=? and app_id=?", 
						new Object[]{getPara("shopId"),templateId,getPara("appId")});
				if(null == rec){
					String id = UUID.randomUUID().toString();
					success = Db.update("insert into bp_shop_app_custom(id,shop_id,template_id,app_id,name,create_date) values(?,?,?,?,?,now())", 
							new Object[]{id,getPara("shopId"),templateId,getPara("appId"), getPara("name")}) > 0;
				}else{
					customAppId = rec.getStr("id");
					Db.update("update bp_shop_app_custom set name=? where id=? ", new Object[]{getPara("name"),customAppId});
				}
			}else{
				Db.update("update bp_shop_app_custom set name=? where id=? ", new Object[]{getPara("name"),customAppId});
			}
			if(success && null != changeApp.get("id")){//同步已安装的应用
				List<String> sqls = new ArrayList<String>();
				Record taskInfo = new Record().set("task_desc", "修改了应用名称【"+getPara("name")+"】");
				Map<String,List<File>> res = IndexTask.synRes(getPara("shopId"), sqls, taskInfo,null,null);
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
			return success;
		}});
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));//执行成功后删除的资源文件，例如之前的旧文件
			if(null != changeApp.get("id")){//该应用已安装
				try{
					boolean success = DataSynUtil.addTask(getPara("shopId"), "index_app", getPara("appId"), "1");
//				if(success){
					PageUtil.changPageLog(getPara("shopId"), "index_app", getPara("appId"), "2");//记录更新日志
//				}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}else{
			SynUtils.deleteRes(deleteRes.get("fail"));//执行失败后删除的资源文件，例如新生成的下载包
		}
		renderJsonResult(isSuccess);
	}
	
	public void saveCustomAppIcon(){
		final Record appIconRec = new Record();
		UploadFile file = getFile("upload", PathKit.getWebRootPath() + "/" + APP_CUSTOM_PATH);
		if (file != null) {
			String name = String.valueOf(System.currentTimeMillis());
			ImageKit.renameFile(file, name);
			File src = file.getFile();
			appIconRec.set("icon", APP_CUSTOM_PATH.replaceAll(File.separator+File.separator, "/") + name + ImageKit.getFileExtension(src.getName()));
		}else{
			String jsonStr = JsonKit.toJson(new Record().set("success", false).set("msg", "请上传应用图标！"));
			renderHtml(jsonStr);//上传文件的form表单的返回值为json时会出现浏览器提示下载
			return;
		}
		final Record changeApp = getChangeAppInfo();
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			String customAppId = getPara("custom_app_id");
			Object templateId = PageUtil.getTemplateId(getPara("shopId"));
			if(StringUtils.isBlank(customAppId)){//查找是否存在已配置的app
				Record rec = Db.findFirst("select id from bp_shop_app_custom where shop_id=? and template_id=? and app_id=?", 
						new Object[]{getPara("shopId"),templateId,getPara("appId")});
				if(null != rec){
					customAppId = rec.getStr("id");
				}
			}
			List<String> routerDeleteRes = new ArrayList<String>();
			boolean success = true;
			if(StringUtils.isBlank(customAppId)){//新增
				String id = UUID.randomUUID().toString();
				success = Db.update("insert into bp_shop_app_custom(id,shop_id,template_id,app_id,icon,create_date) values(?,?,?,?,?,now())", 
						new Object[]{id,getPara("shopId"),templateId,getPara("appId"), appIconRec.get("icon")}) > 0;
			}else{
				//查找之前的应用图标，加到删除列表中
				Record oldRec = Db.findFirst("select icon from bp_shop_app_custom where id=?",new Object[]{customAppId});
				if(null != oldRec && null!= oldRec.get("icon") && 
						StringUtils.isNotBlank(oldRec.getStr("icon")) && oldRec.getStr("icon").startsWith("upload")){
					String fileName = oldRec.getStr("icon").substring(oldRec.getStr("icon").lastIndexOf("/")+1);
					routerDeleteRes.add("/storageroot/Data/"+IndexTask.IMAGE_FOLDER+"/"+fileName);
					List<File> files = new ArrayList<File>();
					files.add(new File(PathKit.getWebRootPath()+File.separator+oldRec.getStr("icon")));
					SynUtils.putFiles(deleteRes,files,"success");
				}
				Db.update("update bp_shop_app_custom set icon=? where id=? ", new Object[]{appIconRec.get("icon"),customAppId});
			}
			if(success && null != changeApp.get("id")){
				List<String> sqls = new ArrayList<String>();
				Record taskInfo = new Record().set("task_desc", "更改了应用图标");
				Map<String,List<File>> res = IndexTask.synRes(getPara("shopId"), sqls, taskInfo,routerDeleteRes,null);
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
			return success;
		}});
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));//执行成功后删除的资源文件，例如之前的旧文件
			if(null != changeApp.get("id")){
				try{
					boolean success = DataSynUtil.addTask(getPara("shopId"), "index_app", getPara("appId"), "1");
//					if(success){
						PageUtil.changPageLog(getPara("shopId"), "index_app", getPara("appId"), "2");//记录更新日志
//					}
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}else{
			SynUtils.deleteRes(deleteRes.get("fail"));//执行失败后删除的资源文件，例如新生成的下载包
		}
		String jsonStr = "";
		if(isSuccess){
			jsonStr = JsonKit.toJson(new Record().set("success", true).set("src", appIconRec.get("icon")));
		}else{
			jsonStr = JsonKit.toJson(new Record().set("success", false).set("msg", "操作失败稍后请重试！"));
		}
		renderHtml(jsonStr);//上传文件的form表单的返回值为json时会出现浏览器提示下载
	}
	
	private Record getChangeAppInfo(){
		StringBuffer sql = new StringBuffer();
		sql.append("select spa.id ");
		sql.append("from bp_app a left join bp_shop_page_app spa on (a.id=spa.app_id and spa.page_id=?) ");
		sql.append("where a.id=? ");
		return Db.findFirst(sql.toString(), 
				new Object[]{PageUtil.getPageIdByShopId(getPara("shopId")),getPara("appId")});
	}
}
