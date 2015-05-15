package com.yinfu.business.application.controller;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.util.Utils;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.routersyn.util.SynUtils;

@ControllerBind(controllerKey = "/business/app/microshop", viewPath = "/page/business/application/microshop")
public class MicroShopController extends Controller<Record>{
	private static final String RES_PATH = "upload" + File.separator + "image" + File.separator + "microShop" + File.separator;//图片保存路径
	
	public void index(){
		render("index.jsp");
	}

	public void dataList(){
		StringBuffer sql = new StringBuffer();
		sql.append("from bp_micro_shop ms join bp_micro_shop_type mst on (ms.type=mst.id) ");
		sql.append("where ms.shop_id=? ");
		if(StringUtils.isNotBlank(getPara("title"))){
			sql.append("and ms.title like '"+Utils.queryLike(getPara("title"))+"' ");
		}
		Page<Record> returnData = Db.paginate(getParaToInt("pageNum"), getParaToInt("pageSize"), 
				"select ms.id,ms.title,ms.img,ms.old_price,ms.new_price,ms.create_date,mst.name type_name ", sql.toString(),new Object[]{getPara("shopId")});
		renderJson(returnData);
	}
	
	public void editMicroShopInfo(){
		if(StringUtils.isNotBlank(getPara("id"))){
			Record info = Db.findFirst("select id,title,img,old_price,new_price,type,remark,link from bp_micro_shop where id=? ", 
					new Object[]{getPara("id")});
			setAttr("info", info);
		}
		render("edit.jsp");
	}
	public void getMicroShopType(){
		List<Record> types = Db.find("select id,icon,name from bp_micro_shop_type where shop_id=? order by create_date desc ", new Object[]{getPara("shopId")});
		renderJson(types);
	}
	
	public void editMicroShopType(){
		render("type/edit.jsp");
	}
	
	public void deleteMicroShopInfo(){
		Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		getOldImg(deleteRes,getPara("id"));
		Db.update("delete from bp_micro_shop where id=? ", new Object[]{getPara("id")});
		SynUtils.deleteRes(deleteRes.get("success"));
		renderJsonResult(true);
	}
	
	public void saveMicroShopInfo(){
		UploadFile file = getFile("upload", PathKit.getWebRootPath() + "/" + RES_PATH);
		String image = "";
		if(file!=null){
			String name = String.valueOf(System.currentTimeMillis());
			ImageKit.renameFile(file, name);
			File src = file.getFile();
			image = RES_PATH.replaceAll(File.separator+File.separator, "/") + name + ImageKit.getFileExtension(src.getName());
		}
		Record rec = getModel();
		if(StringUtils.isNotBlank(image)){
			rec.set("img", image);
		}
		Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = false;
		if (rec.get("id") == null) {//新增
			Object id = UUID.randomUUID().toString();
			isSuccess = Db.save("bp_micro_shop", rec.set("id", id)
					.set("create_date",new Timestamp(System.currentTimeMillis()))
					.set("update_date", new Timestamp(System.currentTimeMillis())));
		}else{//修改
			if(null != rec.get("img")){//更改了图片
				getOldImg(deleteRes,rec.get("id"));
			}
			isSuccess = Db.update("bp_micro_shop", rec.set("update_date", new Timestamp(System.currentTimeMillis())));
		}
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));
		}else{
			SynUtils.deleteRes(deleteRes.get("fail"));
		}
		renderJsonResult(isSuccess);
	}
	
	public void saveMicroShopType(){
		UploadFile file = getFile("upload", PathKit.getWebRootPath() + "/" + RES_PATH);
		String image = "";
		if(file!=null){
			String name = String.valueOf(System.currentTimeMillis());
			ImageKit.renameFile(file, name);
			File src = file.getFile();
			image = RES_PATH.replaceAll(File.separator+File.separator, "/") + name + ImageKit.getFileExtension(src.getName());
		}
		Record rec = getModel();
		if(StringUtils.isNotBlank(image)){
			rec.set("icon", image);
		}
		Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = false;
		if (rec.get("id") == null) {//新增
			Object id = UUID.randomUUID().toString();
			isSuccess = Db.save("bp_micro_shop_type", rec.set("id", id)
					.set("create_date",new Timestamp(System.currentTimeMillis())));
		}else{//修改
			if(null != rec.get("icon")){//更改了图片
				getOldTypeIcon(deleteRes,rec.get("id"));
			}
			isSuccess = Db.update("bp_micro_shop_type", rec);
		}
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));
		}else{
			SynUtils.deleteRes(deleteRes.get("fail"));
		}
		renderJsonResult(isSuccess);
	}
	
	public void getSystemIcon(){
		List<Record> icons = Db.find("select name,icon from bp_micro_shop_icon_lib");
		setAttr("icons", icons);
		render("type/choiceIcon.jsp");
	}
	
	private void getOldImg(Map<String,List<File>> deleteRes,Object id){
		try{
			Record oldRec = Db.findFirst("select img from bp_micro_shop where id=?",new Object[]{id});
			if(null != oldRec && StringUtils.isNotBlank(oldRec.getStr("img"))){
				List<File> files = new ArrayList<File>();
				files.add(new File(PathKit.getWebRootPath()+File.separator+oldRec.getStr("img")));
				SynUtils.putFiles(deleteRes,files,"success");
			}
		}catch(Exception e){
		}
	}
	
	private void getOldTypeIcon(Map<String,List<File>> deleteRes,Object id){
		try{
			Record oldRec = Db.findFirst("select icon from bp_micro_shop_type where id=?",new Object[]{id});
			if(null != oldRec && null!=oldRec.get("iocn") && StringUtils.isNotBlank(oldRec.getStr("iocn"))){
				List<File> files = new ArrayList<File>();
				if(oldRec.getStr("iocn").startsWith("upload")){
					files.add(new File(PathKit.getWebRootPath()+File.separator+oldRec.getStr("icon")));
					SynUtils.putFiles(deleteRes,files,"success");
				}
			}
		}catch(Exception e){
		}
	}
}
