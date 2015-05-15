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
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.application.model.Menu;
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.remote.DataSynUtil;
import com.yinfu.routersyn.task.RestaurantTask;
import com.yinfu.routersyn.util.SynUtils;

@ControllerBind(controllerKey = "/business/app/menu", viewPath = "/")
public class MenuController extends Controller<Menu>
{
	private static final String PREVIEW_PATH = "upload" + File.separator + "image" + File.separator + "menu" + File.separator;
	
	public void index()
	{
		render("/page/business/application/adv/adv.jsp");
		
	}

	/**
	 * 根据shopId获取所有菜单
	 */
	public void getListByShopId()
	{	
		// 分页基本变量（jqGrid自带）
		int pageNum = getParaToInt("pageNum");
		int pageSize = getParaToInt("pageSize");
		String shopId = getPara("shopId");
		Page<Menu> page = Menu.dao.getListByShopId(pageNum, pageSize, shopId);
		renderJson(page);
	}
	
	/**
	 * 根据shopId获取所有菜单类型
	 */
	public void getTypeByShopId()
	{
		renderJson(Menu.dao.getTypeByShopId());
	}
	
	public void add()
	{
		UploadFile file = getFile("upload", PathKit.getWebRootPath()+"/"+PREVIEW_PATH);
		if(null != file){
			String icon =  PREVIEW_PATH+file.getFile().getName();
			getModel().set("icon", icon);
			getModel().saveAndDate();
		}else{
			renderJson("");
		}
	}

	@Override
	public void edit()
	{
		renderJsonResult(getModel().updateAndModifyDate());
	}
	
	/**
	 * 新增菜单类型
	 */
	public void addType(){
		String name = getPara("name");
		String shop_id = getPara("shop_id");
		
		renderJson(Menu.dao.addType(name, shop_id));
	}
	
	/**
	 * 修改菜单类型
	 */
	public void editMenuType(){
		String name = getPara("name");
		String id = getPara("id");
		
		renderJson(Menu.dao.editType(name, id));
	}
	
	/**
	 * 保存图片
	 */
	public void saveImg(){
		UploadFile file = getFile("upload", PathKit.getWebRootPath()+"/"+PREVIEW_PATH);
		if(null != file){
			renderJson( PREVIEW_PATH+file.getFile().getName());
		}else{
			renderJson("");
		}
	}
	
	//@formatter:off 
	/**
	 * Title: orderingList
	 * Description:点餐菜单列表显示
	 * Created On: 2014年8月9日 下午5:18:48
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void orderingList(){
		String shopId = getPara("shopId");
		String name = getPara("searchText");
		List<Menu> orderingList = Menu.dao.orderingList(shopId,name);
		renderJson(orderingList);
	}
	
	//@formatter:off 
	/**
	 * Title: save
	 * Description:保存菜单信息
	 * Created On: 2014年8月9日 下午6:21:00
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void save(){
		UploadFile file = getFile("upload", PathKit.getWebRootPath() + "/" + PREVIEW_PATH);
		String image = "";
		if(file!=null){
			String name = String.valueOf(System.currentTimeMillis());
			ImageKit.renameFile(file, name);
			File src = file.getFile();
			image = "upload/image/menu/" + name + ImageKit.getFileExtension(src.getName());
		}
		final Menu rec = getModel();
		if(StringUtils.isNotBlank(image)){
			rec.set("icon", image);
		}
		final Record actionRec = new Record();
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			boolean success = false;
			if (rec.get("id") == null) {//新增
				Object uid = UUID.randomUUID().toString();
				success = rec.set("uid", uid).set("create_date",new Date()).save();
				if(success){
					Record addRec = Db.findFirst("select id from bp_menu where uid=? ", new Object[]{uid});
					success = synFile(deleteRes,null,"添加了",addRec.get("id"));
				}
				actionRec.set("operate","1");
			}else{//修改
				List<String> routerDeleteRes = null;
				if(null != rec.get("icon")){//更改了图片
					routerDeleteRes = getOldImg(deleteRes,rec.get("id"));
				}
				success = rec.set("update_date",new Date()).update()
							&& synFile(deleteRes,routerDeleteRes,"修改了",rec.get("id"));
				actionRec.set("operate","2");
			}
			return success;
		}});
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));
			try{
				String sql = "select id from bp_menu where uid=?";
				Record rd = Db.findFirst(sql, new Object[]{rec.getStr("uid")});
				boolean success = DataSynUtil.addTask(rec.get("shopId")+"", "menu", rd.getInt("id")+"", "1");
//				if(success){//这里不能基于succes判断是否执行下面代码，坑
					PageUtil.changPageLog(rec.get("shopId")+"", "menu", rd.getInt("id")+"", actionRec.getStr("operate"));//记录更新日志
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
	 * Title: menuEdit
	 * Description:菜单修改
	 * Created On: 2014年8月9日 下午6:24:41
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void menuEdit(){
		String id = getPara("id");
		Menu menu = Menu.dao.findById(id);
		setAttr("menu", menu);
		render("page/business/application/menu/menuAdd.jsp");
	}
	
	//@formatter:off 
	/**(non-Javadoc)
	 * Title: delete
	 * Description: 删除
	 * Created On: 2014年8月9日 下午6:27:08
	 * @author JiaYongChao
	 * <p>
	 * @see com.yinfu.jbase.jfinal.ext.Controller#delete()
	 */
	//@formatter:on
	public void delete() {
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			List<String> routerDeleteRes = getOldImg(deleteRes,getPara("id"));
			return Db.update("update bp_menu set delete_date=now() where id=? ", new Object[]{getPara("id")})>=1
					&& synFile(deleteRes,routerDeleteRes,"删除了",getPara("id"));
		}});
		if(isSuccess){
			SynUtils.deleteRes(deleteRes.get("success"));
			try{
				Record menu = Db.findFirst("select shopId from bp_menu where id=?", new Object[]{getPara("id")});
				boolean success = DataSynUtil.addTask(menu.get("shopId")+"", "menu", getPara("id"), "2");
//				if(success){//这里不能基于succes判断是否执行下面代码，坑
					PageUtil.changPageLog(menu.get("shopId")+"", "menu", getPara("id"), "3");//记录更新日志
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
			Record oldRec = Db.findFirst("select icon from bp_menu where id=?",new Object[]{id});
			if(null != oldRec && StringUtils.isNotBlank(oldRec.getStr("icon"))){
				String fileName = oldRec.getStr("icon").substring(oldRec.getStr("icon").lastIndexOf("/")+1);
				routerDeleteRes.add("/storageroot/Data/"+RestaurantTask.IMAGE_FOLDER+"/"+fileName);
				
				List<File> files = new ArrayList<File>();
				files.add(new File(PathKit.getWebRootPath()+File.separator+oldRec.getStr("icon")));
				SynUtils.putFiles(deleteRes,files,"success");
			}
		}catch(Exception e){
		}
		return routerDeleteRes;
	}
	
	private boolean synFile(Map<String,List<File>> deleteRes,List<String> routerDeleteRes,String taskDesc,Object id){
		List<String> sqls = new ArrayList<String>();
		Record rec = Db.findFirst("select shopId,ifnull(name,'') name from bp_menu where id=? ", new Object[]{id});
		String name = rec.getStr("name");
		if(StringUtils.isNotBlank(name)){
			name = "【"+name+"】";
		}
		Record taskInfo = new Record().set("task_desc", taskDesc+"菜品"+name);
		Map<String,List<File>> res = RestaurantTask.synRes(rec.get("shopId"), sqls, taskInfo,routerDeleteRes,null);
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
