package com.yinfu.business.setting.blacklist;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.jfinal.ext.Controller;

@ControllerBind(controllerKey = "/business/setting/blacklist", viewPath = "/page/business/setting/blacklist/")
public class BlackListController extends Controller<Record> {
	public void index(){
		render("index.jsp");
	}
	
	public void getList(){
		Page<Record> returnData = Db.paginate(getParaToInt("pageNum"), getParaToInt("pageSize"), 
				"select id,type,tag,ifnull(marker,'') marker ", "from bp_shop_blacklist where shop_id=? order by create_date desc ",new Object[]{getPara("shopId")});
		renderJson(returnData);
	}
	
	public void add(){
		render("add.jsp");
	}
	
	public void save(){
		List<Record> list = Db.find("select id from bp_shop_blacklist where shop_id=? and type=? and tag=? ", 
				new Object[]{getPara("shopId"),getPara("type"),getPara("tag")});
		if(list.size() > 0){
			renderJson("error","repeat");
		}else{
			String content = getPara("tag");
			if("mac".equals(getPara("type"))){//将mac地址转小写
				content = content.toLowerCase();
			}
			Record rec = new Record().set("id", UUID.randomUUID().toString()).set("shop_id", getPara("shopId"))
					.set("type", getPara("type")).set("tag", content).set("marker", getPara("marker"))
					.set("create_date", new Timestamp(System.currentTimeMillis()));;
			renderJsonResult(Db.save("bp_shop_blacklist", rec));
		}
	}
	
	public void delete(){
		Db.update("delete from bp_shop_blacklist where id=? ", new Object[]{getPara("id")});
		renderJsonResult(true);
	}
	
}
