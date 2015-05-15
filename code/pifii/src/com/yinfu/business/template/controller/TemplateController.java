package com.yinfu.business.template.controller;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.yinfu.business.template.model.Template;
import com.yinfu.jbase.jfinal.ext.Controller;

@ControllerBind(controllerKey = "/business/template", viewPath = "/")
public class TemplateController extends Controller<Object>{
//	private static final String PREVIEW_PATH = "images"+File.separator+"template"+File.separator;
//	private static final String FILE_PATH = "file"+File.separator+"template"+File.separator;
	
	public void showPreview() {
//		StringBuffer sql = new StringBuffer();
//		sql.append("select t.cloud_url ");
//		sql.append("from bp_shop_page sp join bp_temp t on (sp.shop_id=? and sp.template_id=t.id) ");
//		Record rec = Db.findFirst(sql.toString(),new Object[]{getPara("shopId")});
		redirect("/portal/mb/index?shopId="+getPara("shopId"));
//		Record rec = new PageUtils().getPagePath(getPara("shopId"),true);
//		if(null != rec){
//			String pagePath = rec.getStr("page_path");
//			if(StringUtils.isNotBlank(pagePath)){
//				renderJsp(pagePath);
//				return;
//			}
//		}
//		renderNull();//可改为推广图片或网址
	}
	
	@Override
	public void list(){
		StringBuffer strB = new StringBuffer();
		strB.append("select t.id,t.name,t.preview_img,IF(sp.id is null,'0','1') is_used,IF(t.is_used='1','1','0') can_used ");
		strB.append("from bp_temp t left join bp_shop_page sp on (sp.shop_id=? and t.id = sp.template_id) ");
		strB.append("where t.delete_date is null");
		renderJson(Db.find(strB.toString(), new Object[]{getPara("shopId")}));
	}
	@Override
	public void delete(){
		renderJson(Template.dao.delete(getPara("id")));
	}
//	@Override
//	public void add(){
//		String imgPath = saveImg();
//		String filePath = saveFile();
//		renderJson(Template.dao.add(getPara("name"),imgPath,filePath,getPara("isUsed")));
//	}
//	@Override
//	public void edit(){
//		String imgPath = saveImg();
//		if(null == imgPath){
//			imgPath = getPara("imgPath");
//		}
//		String filePath = saveFile();
//		if(null == filePath){
//			filePath = getPara("filePath");
//		}
//		renderJson(Template.dao.edit(getPara("id"),getPara("name"),imgPath,filePath,getPara("isUsed")));
//	}
//	
//	private String saveImg(){
//		UploadFile file = getFile("previewImg", PathKit.getWebRootPath()+PREVIEW_PATH);
//		if(null != file){
//			return PREVIEW_PATH+file.getFile().getName();
//		}else{
//			return null;
//		}
//	}
//	
//	private String saveFile(){
//		UploadFile file = getFile("file", PathKit.getWebRootPath()+FILE_PATH);
//		if(null != file){
//			return FILE_PATH+file.getFile().getName();
//		}else{
//			return null;
//		}
//	}
}
