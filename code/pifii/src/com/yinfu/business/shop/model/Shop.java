
package com.yinfu.business.shop.model;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.yinfu.Consts;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.jfinal.ext.ShiroExt;
import com.yinfu.system.model.User;

@TableBind(tableName = "bp_shop")
public class Shop extends Model<Shop> {
	private static final long serialVersionUID = -128801010211787215L;
	
	public static Shop dao = new Shop();
	
	public List<Shop> list() {
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		if(null == user || null == user.getId()){
			return new ArrayList<Shop>();
		}else{
			return dao.find("select id,name,icon from bp_shop where delete_date is null and owner = ? ", new Object[] { user.getId() });
		}
//		StringBuffer sql = new StringBuffer();
//		sql.append("select distinct s.id,s.name,s.icon ");
//		sql.append("from system_user u join system_user_role sur on (u.id = ? and u.id=sur.user_id) ");
//		sql.append("join system_role sr on (sur.role_id=sr.id and u.org_id=sr.org_id) ");
//		sql.append("join system_role_shop srr on (sr.id=srr.role_id) ");
//		sql.append("join bp_shop s on (srr.shop_id=s.id) ");
//		return dao.find(sql.toString(), new Object[]{user.getId()});
	}
	
	public JSONObject delete(String id) {
		JSONObject json = new JSONObject();
		json.put("success", dao.deleteById(id));
		return json;
	}
	
	public JSONObject add(String name,String des) {
		User user = ShiroExt.getSessionAttr(Consts.SESSION_USER);
		Shop shop = new Shop().set("owner", user.getId()).set("name", name).set("des", des);
		JSONObject json = new JSONObject();
		json.put("success", shop.save());
		return json;
	}
	
	public JSONObject edit(String id, String name, String des) {
		Shop shop = new Shop().set("id", id).set("name", name).set("des", des);
		JSONObject json = new JSONObject();
		json.put("success", shop.update());
		return json;
	}
	
}
