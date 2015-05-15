
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
import com.yinfu.business.application.model.Preferential;
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.remote.DataSynUtil;
import com.yinfu.routersyn.task.PreferentialTask;
import com.yinfu.routersyn.util.SynUtils;


/**
 * @author JiaYongChao
 *最新优惠
 */
@ControllerBind(controllerKey = "/business/app/preferential", viewPath = "/page/business/application/preferential")
public class PreferentialController extends Controller<Record> {
	private static final String PREVIEW_PATH = "upload" + File.separator + "image" + File.separator + "preferential" + File.separator;//图片保存路径
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
		render("preferentialIndex.jsp");
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
		renderJson(Preferential.dao.preferentialList(shopId, name));
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
		Preferential preferential = Preferential.dao.findById(id);
		setAttr("preferential", preferential);
		renderJsp("preferentialAdd.jsp");
	}
	//@formatter:off 
	/**(non-Javadoc)
	 * Title: delete
	 * Description: 删除
	 * Created On: 2014年8月21日 下午8:06:21
	 * @author JiaYongChao
	 * <p>
	 * @see com.yinfu.jbase.jfinal.ext.Controller#delete()
	 */
	//@formatter:on
	public void delete(){
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			List<String> routerDeleteRes = getOldImg(deleteRes,getPara("id"));
			return Db.update("update bp_preferential set delete_date=now() where id=? ", new Object[]{getPara("id")})>=1
					&& synFile(deleteRes,routerDeleteRes,"删除了",getPara("id"));
		}});
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));
			
			try{
				Record rec = Db.findFirst("select shop_id from bp_preferential where id=? ", new Object[]{getPara("id")});
				boolean success = DataSynUtil.addTask(rec.get("shop_id").toString(), "preferential", getPara("id"), "2");
//				if(success){//这里不能基于succes判断是否执行下面代码，坑
					PageUtil.changPageLog(rec.get("shop_id").toString(), "preferential", getPara("id"), "3");//记录更新日志
//				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			SynUtils.deleteRes(deleteRes.get("fail"));
		}
		renderJsonResult(isSuccess);
	}

	//@formatter:off 
	/**
	 * Title: save
	 * Description:保存
	 * Created On: 2014年8月21日 下午8:25:42
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
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
				success = Db.save("bp_preferential", rec.set("id", id).set("create_date",new Date()))
						&& synFile(deleteRes,null,"添加了",id);
				actionRec.set("operate", "1");
				actionRec.set("id", id);
			}else{//修改
				List<String> routerDeleteRes = null;
				if(null != rec.get("img")){//更改了图片
					routerDeleteRes = getOldImg(deleteRes,rec.get("id"));
				}
				success = Db.update("bp_preferential", rec)
							&& synFile(deleteRes,routerDeleteRes,"修改了",rec.get("id"));
				actionRec.set("operate", "2");
				actionRec.set("id", rec.get("id"));
			}
			return success;
		}});
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));
			try{
				boolean success = DataSynUtil.addTask(rec.get("shop_id").toString(), "preferential", actionRec.get("id").toString(), "1");
//				if(success){//这里不能基于succes判断是否执行下面代码，坑
					PageUtil.changPageLog(rec.get("shop_id").toString(), "preferential", actionRec.get("id").toString(), actionRec.get("operate").toString());//记录更新日志
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
			Record oldRec = Db.findFirst("select img from bp_preferential where id=?",new Object[]{id});
			if(null != oldRec && StringUtils.isNotBlank(oldRec.getStr("img"))){
				String fileName = oldRec.getStr("img").substring(oldRec.getStr("img").lastIndexOf("/")+1);
				routerDeleteRes.add("/storageroot/Data/"+PreferentialTask.IMAGE_FOLDER+"/"+fileName);
				
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
		Record rec = Db.findFirst("select shop_id,ifnull(title,'') title from bp_preferential where id=? ", new Object[]{id});
		String name = rec.getStr("title");
		if(StringUtils.isNotBlank(name)){
			name = "【"+name+"】";
		}
		Record taskInfo = new Record().set("task_desc", taskDesc+SynUtils.getAppName("preferenti")+name);
		Map<String,List<File>> res = PreferentialTask.synRes(rec.get("shop_id"), sqls, taskInfo,routerDeleteRes,null);
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
