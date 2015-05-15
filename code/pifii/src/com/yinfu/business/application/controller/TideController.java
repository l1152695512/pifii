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
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.application.model.Tide;
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.remote.DataSynUtil;
import com.yinfu.routersyn.task.TideTask;
import com.yinfu.routersyn.util.SynUtils;
/**
 * @author JiaYongChao
 *潮机推荐
 */
@ControllerBind(controllerKey = "/business/app/tide", viewPath = "/page/business/application/tide")
public class TideController extends Controller<Record> {
	private static final String PREVIEW_PATH = "upload" + File.separator + "image" + File.separator + "tide" + File.separator;//图片保存路径
	//@formatter:off 
	/**
	 * Title: show
	 * Description:最新优惠页面
	 * Created On: 2014年8月21日 下午8:04:26
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void show(){
		render("tideIndex.jsp");
	}
	//@formatter:off 
	/**
	 * Title: listData
	 * Description:最新优惠列表
	 * Created On: 2014年8月21日 下午8:04:57
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void listData(){
		String shopId = getPara("shopId");//商铺Id
		String name =getPara("searchText");//查询参数
		renderJson(Tide.dao.tideList(shopId, name));
	}
	//@formatter:off 
	/**
	 * Title: update
	 * Description:更新
	 * Created On: 2014年8月21日 下午8:05:38
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void update(){
		String id = getPara("id");
		Tide tide = Tide.dao.findById(id);
		setAttr("tide", tide);
		renderJsp("tideAdd.jsp");
	}
	
	public void delete(){
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			List<String> routerDeleteRes = getOldImg(deleteRes,getPara("id"));
			return Db.update("update bp_tide set delete_date=now() where id=? ", new Object[]{getPara("id")})>=1
					&& synFile(deleteRes,routerDeleteRes,"删除了",getPara("id"));
		}});
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));
			
			try{
				Record rec = Db.findFirst("select shop_id from bp_tide where id=? ", new Object[]{getPara("id")});
				boolean success = DataSynUtil.addTask(rec.get("shop_id").toString(), "tide", getPara("id"), "2");
//				if(success){
					PageUtil.changPageLog(rec.get("shop_id").toString(), "tide", getPara("id"), "3");//记录更新日志
//				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			SynUtils.deleteRes(deleteRes.get("fail"));
		}
		renderJsonResult(isSuccess);
	}

	public void save() {
		UploadFile file = getFile("upload", PathKit.getWebRootPath() + "/" + PREVIEW_PATH);
		String image = "";
		if(file!=null){
			String name = String.valueOf(System.currentTimeMillis());
			ImageKit.renameFile(file, name);
			File src = file.getFile();
			image = PREVIEW_PATH.replaceAll(File.separator+File.separator, "/") + name + ImageKit.getFileExtension(src.getName());
		}
		final Record rec = getModel();
		if(StringUtils.isNotBlank(image)){
			rec.set("img", image);
		}
		final Record actionRec = new Record();
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			boolean success = false;
			if (rec.get("id") == null) {//新增
				Object id = UUID.randomUUID().toString();
				success = Db.save("bp_tide", rec.set("id", id).set("create_date",new Date()))
						&& synFile(deleteRes,null,"添加了",id);
				actionRec.set("operate","1");
				actionRec.set("id", id);
			}else{//修改
				List<String> routerDeleteRes = null;
				if(null != rec.get("img")){//更改了图片
					routerDeleteRes = getOldImg(deleteRes,rec.get("id"));
				}
				success = Db.update("bp_tide", rec)
							&& synFile(deleteRes,routerDeleteRes,"修改了",rec.get("id"));
				actionRec.set("operate","2");
				actionRec.set("id", rec.get("id"));
			}
			return success;
		}});
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));
			try{
				boolean success = DataSynUtil.addTask(rec.get("shop_id").toString(), "tide", actionRec.get("id").toString(), "1");
//				if(success){
					PageUtil.changPageLog(rec.get("shop_id").toString(), "tide", actionRec.get("id").toString(), actionRec.getStr("operate"));//记录更新日志
//				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			SynUtils.deleteRes(deleteRes.get("fail"));
		}
		renderJsonResult(isSuccess);
	}
	
	private List<String> getOldImg(Map<String,List<File>> deleteRes,Object id){
		List<String> routerDeleteRes = new ArrayList<String>();
		try{
			Record oldRec = Db.findFirst("select img from bp_tide where id=?",new Object[]{id});
			if(null != oldRec && StringUtils.isNotBlank(oldRec.getStr("img"))){
				String fileName = oldRec.getStr("img").substring(oldRec.getStr("img").lastIndexOf("/")+1);
				routerDeleteRes.add("/storageroot/Data/"+TideTask.IMAGE_FOLDER+"/"+fileName);
				
				List<File> files = new ArrayList<File>();
				files.add(new File(PathKit.getWebRootPath()+File.separator+oldRec.getStr("img")));
				SynUtils.putFiles(deleteRes,files,"success");
			}
		}catch(Exception e){
		}
		return routerDeleteRes;
	}
	
	private boolean synFile(Map<String,List<File>> deleteRes,List<String> routerDeleteRes,String taskDesc,Object id){
		List<String> sqls = new ArrayList<String>();
		Record rec = Db.findFirst("select shop_id,ifnull(name,'') name from bp_tide where id=? ", new Object[]{id});
		String name = rec.getStr("name");
		if(StringUtils.isNotBlank(name)){
			name = "【"+name+"】";
		}
		Record taskInfo = new Record().set("task_desc", taskDesc+SynUtils.getAppName("tide")+name);
		Map<String,List<File>> res = TideTask.synRes(rec.get("shop_id"), sqls, taskInfo,routerDeleteRes,null);
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
