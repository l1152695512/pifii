
package com.yinfu.routersyn.report;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;

/**
 * 仅支持POST请求
 * 
 * 上报盒子的状态，包含：盒子的wifii名称、客户端列表、上网类型及ip，
 */
@ControllerBind(controllerKey = "/router/report/online", viewPath = "/")
public class Online extends Controller {
	private static Logger logger = Logger.getLogger(Online.class);
	
//	@Before(POST.class)
	public void index(){//上传的版本号没有使用，盒子脚本的版本是通过更新成功之后设置的，不通过上传设置
		try{
			String routersn = getPara("routersn");
			logger.info(getPara("info").replaceAll("&quot;", "\""));
			JSONObject infoJson = JSONObject.parseObject(getPara("info").replaceAll("&quot;", "\""));
			String wifiname = "";
			String sys_version = "";
			String version_code_all = "";
			String version_tfcard = "";
			
			if(infoJson.containsKey("wifiname")){
				wifiname = infoJson.getString("wifiname");
			}
			if(infoJson.containsKey("sys_version")){
				sys_version = infoJson.getString("sys_version");
			}
			if(infoJson.containsKey("version_code_all")){
				version_code_all = infoJson.getString("version_code_all");
			}
			if(infoJson.containsKey("version_tfcard")){
				version_tfcard = infoJson.getString("version_tfcard");
			}
			
			int num = 0;
			if(infoJson.containsKey("clientList")){
				List<String> sqlList = new ArrayList<String>();
				JSONArray jsonArray = infoJson.getJSONArray("clientList");
				
				if(jsonArray.size() > 0 && jsonArray.getJSONObject(0).size() > 0){
					num = jsonArray.size();
					for(int i=0;i<jsonArray.size();i++){
						JSONObject sonJson = jsonArray.getJSONObject(i);
						String host = sonJson.getString("host");
						String mac = sonJson.getString("mac");
						String ip = sonJson.getString("ip");
						
						sqlList.add("insert into bp_report(sn,host,mac,ip,create_date) values('"+routersn+"','"+host+"','"+mac+"','"+ip+"',now())");
					}
					try{
						Db.batch(sqlList, sqlList.size());
					}catch(Exception e){
						e.printStackTrace();
						logger.error("盒子上报在线状态,插入客户端列表异常！",e);
						logger.error(getPara("info").replaceAll("&quot;", "\""));
					}
				}
			}
			//上面的Db.batch与下面的Db.update不需要在一个事务中，
			String sql = "update bp_device set name=?,online_num=?,online_type=?,ip=?,report_date=now(),router_version=?,script_version=?,data_version=? where router_sn=?";
			Db.update(sql, new Object[]{wifiname,num,infoJson.get("wantype"),infoJson.get("ip"),sys_version,version_code_all,version_tfcard,routersn});
			
			renderJson("success", "true");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("盒子上报在线状态异常！",e);
			logger.error(getPara("info").replaceAll("&quot;", "\""));
			renderJson("success", "false");
		}
	}
	
}
