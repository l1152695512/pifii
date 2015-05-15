
package com.yinfu.business.application.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.application.model.Coupon;
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.remote.DataSynUtil;
import com.yinfu.routersyn.task.IntroduceTask;
import com.yinfu.routersyn.util.SynUtils;

/**
 * @author JiaYongChao 优惠券
 */

@ControllerBind(controllerKey = "/business/app/introduce", viewPath = "/page/business/application/introduce")
public class IntroduceController extends Controller<Coupon> {
	private static final String FILE_PATH = "upload" + File.separator + "introduce" + File.separator;
	private static final String ACCEPT_EXT = ".mp4";

	public void settingIndex(){
		Record rec = Db.findFirst("select id,shop_id,file_path,des from bp_introduce where shop_id=? order by create_date desc", new Object[]{getPara("shopId")});
		if(null != rec){
			setAttr("id", rec.get("id"));
			setAttr("shopId", rec.get("shop_id"));
			setAttr("filePath", rec.get("file_path"));
			setAttr("des", rec.get("des"));
		}
		renderJsp("setting.jsp");
	}
	public void save() {
		final Record actionRec = new Record();
		final Map<String,String> errorMsg = new HashMap<String,String>();
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			UploadFile file = null;
			try{
				file = getFile("upload", PathKit.getWebRootPath() + "/" + FILE_PATH,20971520);
			}catch(Exception e){
				e.printStackTrace();
				errorMsg.put("msg","文件大小必须在20M以内。");
				return false;
			}
			String filePath = "";
			if(file!=null){
				String name = String.valueOf(System.currentTimeMillis());
				ImageKit.renameFile(file, name);
				File src = file.getFile();
				String extension = ImageKit.getFileExtension(src.getName());
				if(Arrays.asList(ACCEPT_EXT.split(",")).contains(extension)){
					filePath = FILE_PATH.replaceAll(File.separator+File.separator, "/") + name + extension;
				}
			}
			boolean success = false;
			if (StringUtils.isBlank(getPara("id"))) {//新增
				int changeRows = Db.update("insert into bp_introduce(shop_id,file_path,des,create_date) values(?,?,?,now())", 
						new Object[]{getPara("shopId"),filePath,getPara("des")});
				if(changeRows == 1){
					success = synFile(deleteRes,null,"添加了");
				}
				actionRec.set("operate", "1");
			}else{//修改
				if(StringUtils.isBlank(filePath)){
					Db.update("update bp_introduce set des=? where id=? ",new Object[]{getPara("des"),getPara("id")});
					success = synFile(deleteRes,null,"修改了");
				}else{
					List<String> routerDeleteRes = getOldImg(deleteRes,getPara("id"));
					int changeRows = Db.update("update bp_introduce set file_path=?,des=? where id=? ",new Object[]{filePath,getPara("des"),getPara("id")});
					if(changeRows == 1){
						success = synFile(deleteRes,routerDeleteRes,"修改了");
					}
				}
				actionRec.set("operate", "2");
			}
			return success;
		}});
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));
			try{
				Record rec =Db.findFirst("select id from bp_introduce where shop_id=? order by create_date desc", new Object[]{getPara("shopId")});
				boolean result = DataSynUtil.addTask(getPara("shopId"),"introduce", rec.get("id").toString(), "1");
//				if(result){//这里不能基于succes判断是否执行下面代码，坑
					PageUtil.changPageLog(getPara("shopId"), "introduce", rec.get("id").toString(), actionRec.getStr("operate"));
//				}
			}catch(Exception e){
				e.printStackTrace();
			}
			renderJsonResult(true);
		}else{
			SynUtils.deleteRes(deleteRes.get("fail"));
			if(errorMsg.size() > 0){
				renderJson(errorMsg);
			}else{
				renderJsonResult(false);
			}
		}
	}
	
	private List<String> getOldImg(Map<String,List<File>> deleteRes,Object id){
		List<String> routerDeleteRes = new ArrayList<String>();
		try{
			Record oldRec = Db.findFirst("select file_path from bp_introduce where id=?",new Object[]{id});
			if(null != oldRec && StringUtils.isNotBlank(oldRec.getStr("file_path"))){
				String fileName = oldRec.getStr("file_path").substring(oldRec.getStr("file_path").lastIndexOf("/")+1);
				routerDeleteRes.add("/storageroot/Data/"+IntroduceTask.IMAGE_FOLDER+"/"+fileName);
				
				List<File> files = new ArrayList<File>();
				files.add(new File(PathKit.getWebRootPath()+File.separator+oldRec.getStr("file_path")));
				SynUtils.putFiles(deleteRes,files,"success");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return routerDeleteRes;
	}
	
	private boolean synFile(Map<String,List<File>> deleteRes,List<String> routerDeleteRes,String taskDesc){
		List<String> sqls = new ArrayList<String>();
		Record taskInfo = new Record().set("task_desc", taskDesc+SynUtils.getAppName("introduce"));
		Map<String,List<File>> res = IntroduceTask.synRes(getPara("shopId"), sqls, taskInfo,routerDeleteRes,null);
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
