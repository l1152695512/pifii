package com.yinfu.business.setting;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.jfinal.ext.DbExt;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.PropertyUtils;

@ControllerBind(controllerKey = "/business/setting", viewPath = "/page/business/setting/")
public class Whitelist extends Controller<Record> {
	public void index(){
		render("whitelist.jsp");
	}
	
	public void getWhiteList(){
		int pageNum = 1;
		int pageSize = 5;
		if(null != getPara("pageNum")){
			pageNum = getParaToInt("pageNum");
		}
		if(null != getPara("pageSize")){
			pageSize = getParaToInt("pageSize");
		}
		Page<Record> returnData = Db.paginate(pageNum, pageSize, 
				"select id,type,content,ifnull(marker,'') marker ", "from bp_shop_whitelist where shop_id=? order by create_date desc ",new Object[]{getPara("shopId")});
		renderJson(returnData);
	}
	
	public void addWhitelist(){
		render("whitelistAdd.jsp");
	}
	
	public void saveWhitelist(){
		List<Record> list = Db.find("select id from bp_shop_whitelist where shop_id=? and content=? ", 
				new Object[]{getPara("shopId"),getPara("content")});
		if(list.size() > 0){
			renderJson("error","repeat");
		}else{
			boolean access = true;
			if("domain".equals(getPara("type")) || "ip".equals(getPara("type"))){
				access = checkHost(getPara("type"),getPara("content"));
			}
			if(!access){
				renderJson("error","notAccess");
			}else{
				boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
					String content = getPara("content");
					if("mac".equals(getPara("type"))){//将mac地址转小写
						content = content.toLowerCase();
					}
					Record rec = new Record().set("id", UUID.randomUUID().toString()).set("shop_id", getPara("shopId"))
							.set("type", getPara("type")).set("content", content).set("marker", getPara("marker"))
							.set("create_date", new Timestamp(System.currentTimeMillis()));;
							boolean success = Db.save("bp_shop_whitelist", rec);
							if(success && ("ip".equals(getPara("type")) || "domain".equals(getPara("type")))){
								setSynStatus(getPara("shopId"),getPara("type"));
							}
							return success;
				}});
				renderJsonResult(isSuccess);
			}
		}
	}
	private boolean checkHost(String checkType,String checkContent){
		String notAllowedHost = PropertyUtils.getProperty("route.notAllowed.whitelist","");
		String[] notAllowedHostsStr = notAllowedHost.split("\\|");
		for(int i=0;i<notAllowedHostsStr.length;i++){
			String type = notAllowedHostsStr[i].substring(0, notAllowedHostsStr[i].indexOf(":"));
			String content = notAllowedHostsStr[i].substring(notAllowedHostsStr[i].indexOf(":")+1);
			if(null != checkType && checkType.equals(type) && checkContent.equals(content)){//该host不能添加
				return false;
			}
		}
		return true;
	}
	
	public void deleteWhitelist(){
		boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
			Record rec = Db.findFirst("select type,shop_id from bp_shop_whitelist where id=?", new Object[]{getPara("id")});
			if(null != rec){
				int changeRow = Db.update("delete from bp_shop_whitelist where id=? ", new Object[]{getPara("id")});
				if(changeRow > 0 && ("ip".equals(rec.get("type").toString()) || "domain".equals(rec.get("type").toString()))){
					setSynStatus(rec.get("shop_id"),rec.get("type").toString());
				}
			}
			return true;
		}});
		renderJsonResult(isSuccess);
	}
	
	/**
	 * 添加该商铺中需同步白名单的盒子
	 * @param shopId
	 */
	private void setSynStatus(Object shopId,String type){
		Db.update("delete from bp_device_whitelist_status where type=? and sn in (select router_sn from bp_device where shop_id=?)",
				new Object[]{type,shopId});
		List<Record> devices = Db.find("select router_sn from bp_device where shop_id=? ", new Object[]{shopId});
		if(devices.size() > 0){
			List<Record> synDevices = new ArrayList<Record>();
			Object[][] params = new Object[devices.size()][3];
			for(int i=0;i<devices.size();i++){
				String id = UUID.randomUUID().toString();
				params[i][0] = id;
				params[i][1] = devices.get(i).get("router_sn");
				params[i][2] = type;
				synDevices.add(new Record().set("id", id).set("sn", devices.get(i).get("router_sn")));
			}
			DbExt.batch("insert into bp_device_whitelist_status(id,sn,type) values(?,?,?)", params);
			new SynWhitelistThread(synDevices,false).start();//多线程执行
		}
	}
	
}
