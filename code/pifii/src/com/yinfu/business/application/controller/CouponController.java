
package com.yinfu.business.application.controller;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import com.yinfu.business.application.model.Coupon;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.DbUtil;
import com.yinfu.jbase.util.ImageKit;

/**
 * @author JiaYongChao 优惠券
 */

@ControllerBind(controllerKey = "/business/app/coupon", viewPath = "/page/business/application/coupon")
public class CouponController extends Controller<Coupon> {
	private static final String PREVIEW_PATH = "upload" + File.separator + "image" + File.separator + "coupon" + File.separator;

	public void show(){
		renderJsp("index.jsp");
	}
	public void listData(){
		StringBuffer sql = new StringBuffer();
		sql.append("select c.id,c.companyname,c.shopname,ct.name type,c.description,c.icon,c.storename,c.validity ");
		sql.append("from bp_coupon c join bp_coupon_type ct on (c.type=ct.id) ");
		sql.append("where c.delete_date is null and c.shopId = ? ");
		if(StringUtils.isNotBlank(getPara("searchText"))){
			sql.append("and LOCATE('"+DbUtil.replaceSqlStr(getPara("searchText"))+"',CONCAT(c.companyname,c.shopname,c.description,c.storename,ct.name)) > 0 ");
		}
		System.err.println(sql.toString());
		renderJson(Db.find(sql.toString(), new Object[]{getPara("shopId")}));
	}
	public void update(){
		List<Record> types = Db.find("select id,name from bp_coupon_type");
		setAttr("types", types);
		if(StringUtils.isNotBlank(getPara("id"))){
			Coupon coupon = Coupon.dao.findById(getPara("id"));
			setAttr("coupon", coupon);
		}
		renderJsp("edit.jsp");
	}
	@Override
	public void delete(){
		Coupon coupon = Coupon.dao.findById(getPara("id"));
		renderJsonResult(coupon.set("delete_date", new Date()).update());
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
		Coupon coupon = getModel();
		if (coupon.getId() == null) {//新增
			renderJsonResult(coupon.set("icon", image).set("create_date",new Date()).save());
		}else{//修改
			if(!image.equals("")){
				renderJsonResult(coupon.set("icon", image).set("update_date",new Date()).update());
			}else{
				renderJsonResult(coupon.set("update_date",new Date()).update());
			}
		}
	}
}
