
package com.yinfu.routersyn.report;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

/**
 * 仅支持POST请求
 * 
 * 上报盒子本地应用的点击量
 */
@ControllerBind(controllerKey = "/router/report/statistics", viewPath = "/")
public class AppAccessStatistics extends Controller {
	private static Logger logger = Logger.getLogger(AppAccessStatistics.class);
	
//	@Before(POST.class)
	public void index(){
		try{
			String routersn = getPara("routersn");
			JSONObject infoJson = JSONObject.parseObject(getPara("info").replaceAll("&quot;", "\""));
			final JSONArray jsonArray = infoJson.getJSONArray("userLog");
			boolean isSuccess = true;
			if(jsonArray.size() > 0){
				final Object[][] params = new Object[jsonArray.size()][5];
				for(int i=0;i<jsonArray.size();i++){
					JSONObject sonJson = jsonArray.getJSONObject(i);
					Date accessDate = new SimpleDateFormat("yyyyMMddHHmmss").parse(sonJson.getString("dates"));
					int index = sonJson.getString("rid").lastIndexOf("_");
					String type = "";
					int id = -1;
					if(index != -1){
						type = sonJson.getString("rid").substring(0, index);
						try{
							id = Integer.parseInt(sonJson.getString("rid").substring(index+1));
						}catch(Exception e){
						}
					}else{
						type = sonJson.getString("rid");
					}
					params[i] = new Object[]{routersn,sonJson.getString("mac"),id,type,new Timestamp(accessDate.getTime())};
				}
				isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {
					int[] changeRows = Db.batch("insert into bp_statistics_all(router_sn,client_mac,statistics_id,statistics_type,access_date,create_date) "
							+ "values(?,?,?,?,?,now()) ", params, jsonArray.size());
					for(int i=0;i<changeRows.length;i++){
						if(changeRows[i] < 1){
							return false;
						}
					}
					return true;
				}});
			}
			renderJson("success", isSuccess?"true":"false");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("盒子上报应用访问量异常！",e);
			renderJson("success", "false");
		}
	}
	
}
