
package com.yinfu.routersyn.report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.setting.SynWhitelist;

@ControllerBind(controllerKey = "/router/report/passlist", viewPath = "/")
public class PassList extends Controller {
	private static Logger logger = Logger.getLogger(PassList.class);
	
//	@Before(POST.class)
	public void index() {
		try{
			String routersn = getPara("routersn");
			Map<String,Object> json = new HashMap<String,Object>();
			List<String> macArray = new ArrayList<String>();
			List<String> auth = new ArrayList<String>();
			List<String> group = new ArrayList<String>();
			List<String> passlist = new ArrayList<String>();
			if(StringUtils.isNotEmpty(routersn)){
				//查询开启群组功能的白名单
//				String mac_sql = "select mac from bp_pass_list where sn= ? group by mac";
				StringBuffer sql = new StringBuffer();
				sql.append("select a.mac from bp_pass_list a ");
				sql.append("left join bp_device b ");
				sql.append("on a.sn=b.router_sn ");
				sql.append("left join bp_shop c ");
				sql.append("on b.shop_id=c.id ");
				sql.append("where c.gstatus=1 and a.sn=? group by a.mac");
				List<Record> macList = Db.find(sql.toString(), new Object[]{routersn});
				for(Record rd : macList){
					macArray.add(rd.getStr("mac"));
				}
//				new SynWhitelist().addPasslistMac(routersn, macArray);//添加永久性的白名单mac
				
				List<Record> macWhitelist = SynWhitelist.getMacWhitelist(routersn);
				addPasslistMac(macWhitelist, macArray);//添加永久性的白名单mac
				passlist = listToArray(macWhitelist);
				StringBuffer sqlMac = new StringBuffer();
				sqlMac.append("select a.mac,a.is_me ");
				sqlMac.append("from bp_pass_list a ");
				sqlMac.append("left join bp_device b on a.sn=b.router_sn ");
				sqlMac.append("left join bp_shop c on (b.shop_id=c.id) ");
				sqlMac.append("where a.sn=? and (a.is_me or c.gstatus=1) ");
				sqlMac.append("group by a.mac ");
				List<Record> newMacList = Db.find(sqlMac.toString(), new Object[]{routersn});
				auth = getMac(newMacList,"1");
				group = getMac(newMacList,"0");
			}
			json.put("passlist", passlist);
			json.put("auth", auth);
			json.put("group", group);
			json.put("member", macArray);
			json.put("success", "true");
			renderJson(json);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("盒子获取mac地址白名单异常！",e);
			renderJson("success", "false");
		}
	}
	
	private List<String> getMac(List<Record> macList,String joinmark){
		List<String> macArray = new ArrayList<String>();
		Iterator<Record> ite = macList.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			if(joinmark.equals(rec.get("is_me").toString())){
				macArray.add(rec.getStr("mac"));
			}
		}
		return macArray;
	}
	
	public void addPasslistMac(List<Record> settingMacs,List<String> macs){
		Iterator<Record> ite = settingMacs.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			if(!macs.contains(rec.get("content").toString())){
				macs.add(rec.get("content").toString());
			}
		}
	}

	private List<String> listToArray(List<Record> settingMacs){
		List<String> macs = new ArrayList<String>();
		Iterator<Record> ite = settingMacs.iterator();
		while(ite.hasNext()){
			Record rec = ite.next();
			macs.add(rec.getStr("content"));
		}
		return macs;
	}
}
