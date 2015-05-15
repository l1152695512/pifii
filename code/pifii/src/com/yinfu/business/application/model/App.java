package com.yinfu.business.application.model;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.plugin.tablebind.TableBind;
import com.yinfu.Consts;
import com.yinfu.business.shop.model.Shop;
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.jfinal.ext.ShiroExt;
import com.yinfu.system.model.User;

@TableBind(tableName = "bp_app")
public class App extends Model<App>
{
	private static final long serialVersionUID = 1813462134625829854L;
	public static App dao = new App();
	

	/**
	 *  获取所有可用的应用
	 */
	public List getAll()
	{
		StringBuffer sql = new StringBuffer("select ");
		sql.append("a.id,a.edit_url,a.classify,b.name as className,a.name,a.icon,a.link,a.des,a.create_date ");
		sql.append("from bp_app a ");
		sql.append("left join bp_app_class b ");
		sql.append("on a.classify = b.id ");
		sql.append("where a.status = 1 ");
		
		List list = dao.find(sql.toString());
		return list;
	}
	
	/**
	 * 获取所有应用类型
	 */
	public List getAppType()
	{
		 List list = dao.find("select id,name from bp_app_class");
		 return list;
	}
	
	public JSONObject add(String name, String logo, String des) {
		App app = new App();
		app.set("", "");
		JSONObject json = new JSONObject();
		json.put("success", app.save());
		return json;
	}

	//@formatter:off 
	/**
	 * Title: queryInstalledApp
	 * Description:获得已安装的app应用
	 * Created On: 2014年8月6日 下午6:59:28
	 * @author JiaYongChao
	 * <p>
	 * @param shopId
	 * @return 
	 */
	//@formatter:on
	public List<App> queryInstalledApp(String shopId) {
		String sql =" select b.* from bp_shop_page_app a join bp_app b on (b.status=1 and (b.show is null or b.show !='0') and a.app_id=b.id and b.template_id=?) left JOIN bp_shop_page c on c.id = a.page_id where c.shop_id=?";
		List<App> list = dao.find(sql,new Object[]{PageUtil.getTemplateId(shopId),shopId});
		return list;
	}
}
