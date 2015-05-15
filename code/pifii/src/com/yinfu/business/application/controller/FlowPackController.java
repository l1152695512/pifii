package com.yinfu.business.application.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.remote.DataSynUtil;
import com.yinfu.routersyn.task.FlowPackTask;
import com.yinfu.routersyn.util.SynUtils;

@ControllerBind(controllerKey = "/business/app/flowpack", viewPath = "/page/business/application/flowpack")
public class FlowPackController extends Controller<Record>{
	private static final String IMAGE_PATH = "upload" + File.separator + "image" + File.separator + "flowpack" + File.separator;
	
	public void show(){
		renderJsp("index.jsp");
	}
	public void listData(){
		StringBuffer sql = new StringBuffer();
		sql.append("select id,title,pic,ifnull(des,'') des from bp_flow_pack where shop_id=? and delete_date is null ");
		if(StringUtils.isNotBlank(getPara("searchText"))){
			sql.append("and LOCATE('"+DbUtil.replaceSqlStr(getPara("searchText"))+"',CONCAT(title,des)) > 0 ");
		}
		renderJson(Db.find(sql.toString(), new Object[]{getPara("shopId")}));
	}
	public void update(){
		if(StringUtils.isNotBlank(getPara("id"))){
			Record rec = Db.findFirst("select id,title,pic,des from bp_flow_pack where id=? ", 
					new Object[]{getPara("id")});
			setAttr("pageData", rec);
		}
		renderJsp("edit.jsp");
	}
	public void delete(){
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			List<String> routerDeleteRes = getOldImg(deleteRes,getPara("id"));
			return Db.update("update bp_flow_pack set delete_date=now() where id=? ", new Object[]{getPara("id")})>=1
					&& synFile(deleteRes,routerDeleteRes,"删除了",getPara("id"));
			
//			List<String> sqls = new ArrayList<String>();
//			Record rec = Db.findFirst("select shop_id,ifnull(title,'') title from bp_flow_pack where id=? ", new Object[]{getPara("id")});
//			String name = rec.getStr("title");
//			if(StringUtils.isNotBlank(name)){
//				name = "【"+name+"】";
//			}
//			Map<String,List<File>> res = FlowPackTask.synRes(rec.get("shop_id"), "flowPack", sqls, "删除了包子铺"+name,routerDeleteRes);
//			if(null != res){
//				deleteRes.putAll(res);
//				DbExt.batch(sqls);
//				return true;
//			}
//			return false;
		}});
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));
			try{
				Record rec = Db.findFirst("select shop_id from bp_flow_pack where id=? ", new Object[]{getPara("id")});
				boolean success = DataSynUtil.addTask(rec.get("shop_id").toString(), "flowpack", getPara("id"), "2");
//				if(success){//这里不能基于succes判断是否执行下面代码，坑
					PageUtil.changPageLog(rec.get("shop_id").toString(), "flowpack", getPara("id"), "3");//记录更新日志
//				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			SynUtils.deleteRes(deleteRes.get("fail"));
		}
		renderJsonResult(isSuccess);
	}
	public void save(){
		UploadFile file = getFile("upload", PathKit.getWebRootPath() + "/" + IMAGE_PATH);
		String image = "";
		if(file!=null){
			String name = String.valueOf(System.currentTimeMillis());
			ImageKit.renameFile(file, name);
			File src = file.getFile();
			image = IMAGE_PATH.replaceAll(File.separator+File.separator, "/") + name + ImageKit.getFileExtension(src.getName());
		}
		final Record rec = getModel();
		if(StringUtils.isNotBlank(image)){
			rec.set("pic", image);
		}
		final Record actionRec = new Record();
		final Map<String,String> returnMap = new HashMap<String,String>();
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			boolean success = false;
			String id = rec.get("id");
			if (rec.get("id") == null) {//新增
				if(StringUtils.isNotBlank(rec.getStr("pic"))){
					id = UUID.randomUUID().toString();
					success = Db.save("bp_flow_pack", rec.set("id", id).set("create_date",new Date()))
							&& synFile(deleteRes,null,"添加了",id);
				}else{
					returnMap.put("msg","请上传图片！");
				}
				actionRec.set("operate", "1");
				actionRec.set("id", id);
			}else{//修改
				List<String> routerDeleteRes = null;
				if(null != rec.get("pic")){//更改了图片
					routerDeleteRes = getOldImg(deleteRes,rec.get("id"));
				}
				success = Db.update("bp_flow_pack", rec);
				if(success){
					success = synFile(deleteRes,routerDeleteRes,"修改了",rec.get("id"));
				}else{
					success = true;//防止用户在不修改任何内容的情况下提交时出现保存失败
				}
				actionRec.set("operate", "2");
				actionRec.set("id", rec.get("id"));
			}
			return success;
		}});
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));
			try{
				boolean result = DataSynUtil.addTask(rec.get("shop_id").toString(),"flowpack", actionRec.get("id").toString(), "1");
//				if(result){//这里不能基于succes判断是否执行下面代码，坑
					PageUtil.changPageLog(rec.get("shop_id").toString(), "flowpack", actionRec.get("id").toString(), actionRec.getStr("operate"));
//				}
			}catch(Exception e){
				e.printStackTrace();
			}
			renderJsonResult(true);
		}else{
			SynUtils.deleteRes(deleteRes.get("fail"));
			if(returnMap.size()>0){
				renderHtml(JsonKit.toJson(returnMap));
			}else{
				renderJsonResult(false);
			}
		}
	}
	
	private List<String> getOldImg(Map<String,List<File>> deleteRes,Object id){
		List<String> routerDeleteRes = new ArrayList<String>();
		try{
			Record oldRec = Db.findFirst("select pic from bp_flow_pack where id=?",new Object[]{id});
			if(null != oldRec && StringUtils.isNotBlank(oldRec.getStr("pic"))){
				String fileName = oldRec.getStr("pic").substring(oldRec.getStr("pic").lastIndexOf("/")+1);
				routerDeleteRes.add("/storageroot/Data/"+FlowPackTask.IMAGE_FOLDER+"/"+fileName);
				
				List<File> files = new ArrayList<File>();
				files.add(new File(PathKit.getWebRootPath()+File.separator+oldRec.getStr("pic")));
				SynUtils.putFiles(deleteRes,files,"success");
			}
		}catch(Exception e){
		}
		return routerDeleteRes;
	}
	
	private boolean synFile(Map<String,List<File>> deleteRes,List<String> routerDeleteRes,String taskDesc,Object id){
		List<String> sqls = new ArrayList<String>();
		Record rec = Db.findFirst("select shop_id,ifnull(title,'') title from bp_flow_pack where id=? ", new Object[]{id});
		String name = rec.getStr("title");
		if(StringUtils.isNotBlank(name)){
			name = "【"+name+"】";
		}
		Record taskInfo = new Record().set("task_desc", taskDesc+SynUtils.getAppName("flowpack")+name);
		Map<String,List<File>> res = FlowPackTask.synRes(rec.get("shop_id"), sqls, taskInfo,routerDeleteRes,null);
		if(null != res){
			SynUtils.putAllFiles(deleteRes, res);
			if(sqls.size() > 0){
				DbExt.batch(sqls);
			}
			return true;
		}else{
			return false;
		}
	}
}
