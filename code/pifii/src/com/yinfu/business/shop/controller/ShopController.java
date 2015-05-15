
package com.yinfu.business.shop.controller;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yinfu.UrlConfig;
import com.yinfu.business.shop.model.Shop;
import com.yinfu.business.util.PageUtil;
import com.yinfu.cloudportal.utils.SessionParams;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.remote.DataSynUtil;
import com.yinfu.routersyn.task.IndexTask;
import com.yinfu.routersyn.util.SynUtils;

@ControllerBind(controllerKey = "/business/shop", viewPath = UrlConfig.BUSINESS + "/shop")
public class ShopController extends Controller<Shop> {
	private static final String PREVIEW_PATH = "upload" + File.separator + "image" + File.separator + "shop" + File.separator;
	
	@Override
	public void list() {
		List<Shop> shops = Shop.dao.list();
		List<Object> sessionShops = new ArrayList<Object>();
		Iterator<Shop> ite = shops.iterator();
		while(ite.hasNext()){//将拥有查看权限的商铺id放入session中，以便于后续请求中做权限拦截
			Shop shop = ite.next();
			sessionShops.add(shop.get("id").toString());
		}
		setSessionAttr(SessionParams.MY_SHOP_LIST, sessionShops);
		renderJson(shops);
	}
	
	@Override
	public void delete() {
		renderJsonResult(Shop.dao.deleteById(getPara("id")));
	}
	
	@Override
	public void add() {
		renderJson(Shop.dao.add(getPara("name"), getPara("des")));
	}
	
	@Override
	public void edit() {
		renderJson(Shop.dao.edit(getPara("id"), getPara("name"), getPara("des")));
	}
	
	public void getShopInfo() {
		Shop shop = Shop.dao.findById(getPara("shopId"), "name,addr,tel,icon,gstatus");
		if(null == shop){
			shop = new Shop();
		}
		renderJson(shop);
	}
	
	//@formatter:off 
	/**
	 * Title: editInfo
	 * Description:商铺图标修改
	 * Created On: 2014年8月15日 下午4:09:56
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void editInfo() {
		String shopId = getPara("shopId");
		Shop shop = Shop.dao.findById(shopId);
		setAttr("shop", shop);
		render("/page/business/changeUserImage.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: saveShopInfo
	 * Description:保存商铺信息
	 * Created On: 2014年8月15日 下午4:13:26
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void saveShopInfo() {
		UploadFile file = getFile("upload", PathKit.getWebRootPath() + "/" + PREVIEW_PATH);
		final Shop shop = getModel();
		String image = "";
		if (file != null) {
			String name = String.valueOf(System.currentTimeMillis());
			ImageKit.renameFile(file, name);
			File src = file.getFile();
			image = "upload/image/shop/" + name + ImageKit.getFileExtension(src.getName());
		}
		if (!image.equals("")) {
			shop.set("icon", image);
		}
		final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			List<String> routerDeleteRes = new ArrayList<String>();
			try{
				Record oldShop = Db.findFirst("select icon from bp_shop where id=?",new Object[]{shop.getId()});
				//如果是修改了商铺的图标则删除旧图标
				if(null != shop.get("icon") && null != oldShop && StringUtils.isNotBlank(oldShop.getStr("icon"))){
					String fileName = oldShop.getStr("icon").substring(oldShop.getStr("icon").lastIndexOf("/")+1);
					routerDeleteRes.add("/storageroot/Data/"+IndexTask.IMAGE_FOLDER+"/"+fileName);
					
					List<File> files = new ArrayList<File>();
					files.add(new File(PathKit.getWebRootPath()+File.separator+oldShop.getStr("icon")));
					SynUtils.putFiles(deleteRes,files,"success");
				}
			}catch(Exception e){
			}
			boolean success = shop.update();
			if(success){
				List<String> sqls = new ArrayList<String>();
				Record taskInfo = new Record().set("task_desc", "更改了商铺信息");
				Map<String,List<File>> res = IndexTask.synRes(shop.getId(), sqls, taskInfo,routerDeleteRes,null);
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
			SynUtils.deleteRes(deleteRes.get("success"));
			try {
				boolean success = DataSynUtil.addTask(shop.getId().toString(), "index_shop", shop.getId().toString(), "1");
//				if(success){
					PageUtil.changPageLog(shop.getId().toString(), "index_shop", shop.getId().toString(), "2");// 记录更新日志
//				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			SynUtils.deleteRes(deleteRes.get("fail"));
		}
		String jsonStr = JsonKit.toJson(shop);
		renderHtml(jsonStr);
		// renderJson(shop);
	}
	
	//@formatter:off 
			/**
			 * Title: changeGroupStatus
			 * Description:改变开启群组状态
			 * Created On: 2014年10月8日 下午6:31:43
			 * @author JiaYongChao
			 * <p> 
			 */
			//@formatter:on
	public void changeGroupStatus() {
		String shopId = getPara("shopId");
		Shop shop = Shop.dao.findById(shopId);
		JSONObject returnData = new JSONObject();
		if (shopId != null) {
			if (shop.getInt("gstatus") == 0) {// 未开启(发布)
				if (shop.set("gstatus", 1).update()) {
					returnData.put("state", "success");
					renderJson(returnData);
				} else {
					returnData.put("state", "error");
					renderJson(returnData);
				}
			} else {// 已发布(取消发布)
				if (shop.set("gstatus", 0).update()) {
					returnData.put("state", "success");
					renderJson(returnData);
				} else {
					returnData.put("state", "error");
					renderJson(returnData);
				}
			}
			
		} else {
			returnData.put("state", "error");
			renderJson(returnData);
		}
	}
}
