package com.yinfu.servlet.route;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


public class RouteUpgrade extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(RouteUpgrade.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json = new JSONObject();
		response.setCharacterEncoding("UTF-8");
		String fn = request.getParameter("fn");
		String routerSn = request.getParameter("sn");
		logger.info("routerSn="+routerSn+",fn="+fn);
		if(StringUtils.isNotEmpty(fn) && StringUtils.isNotEmpty(routerSn)){
			if("version".equals(fn)){//盒子脚本升级
				System.err.println("请求脚本升级包。");
//				json.put("version", "1.2");
//				json.put("url", "upload/file/pifiibox_file_all.tar.gz");
//				json.put("dir", "/storageroot/Data");
//				if("true".equals(PropertyUtils.getProperty("version.up","false"))){
//					//查询最新版本
//					StringBuffer sql = new StringBuffer("select c.version,c.url,c.dir ");
//					sql.append("from bp_platform a left join bp_warehouse b on a.plat_no=b.plat_no ");
//					sql.append("left join bp_version c on a.plat_no=c.plat_no and b.type=c.type ");
//					sql.append("where b.sn=? order by c.id desc limit 1");
//					Record rd = Db.findFirst(sql.toString(),new Object[]{routerSn});
//					if(rd != null){
//						json.put("version", rd.getStr("version"));
//						json.put("url", rd.getStr("url"));
//						json.put("dir", rd.getStr("dir"));
//					}
//				}
			}else if("init".equals(fn)){//盒子data数据更新
				//查询最新版本
				Record rec = Db.findFirst("select shop_id from bp_device where router_sn=? ",new Object[]{routerSn});
				if(rec != null){
					if(null != rec.get("shop_id")){
						Record initData = Db.findFirst("select url,dir,version,IFNULL(md5,'') md5 from bp_init where status=1 order by create_date desc");
						if(initData != null){
							String filePath = PathKit.getWebRootPath() + File.separator+(initData.getStr("url").replaceAll("/", File.separator+File.separator));
							File downloadFile = new File(filePath);
							if(downloadFile.exists()){
								json.put("url", initData.get("url"));
								json.put("dir", initData.get("dir"));
								json.put("version", initData.get("version"));
								json.put("md5", initData.get("md5"));
								json.put("file_size", (downloadFile.length()*10/1024/1024/10.0)+"M");
								Db.update("update bp_device set init_step=1 where router_sn=? ",new Object[]{routerSn});
							}
						}
					}
				}
			}else if("init_done".equals(fn)){//初始化完成上报
				json.put("success", "false");
				Record step = Db.findFirst("select init_step from bp_device where router_sn=? ", new Object[]{routerSn});
//				if(null != step && step.getInt("init_step")>0){
					if(step.getInt("init_step") == 2){
						json.put("success", "true");
					}else{
						int changeRow = Db.update("update bp_device set init_step=2 where router_sn=? ", new Object[]{routerSn});
						if(changeRow > 0){
							json.put("success", "true");
						}
					}
//				}
			}
		}
		
		response.getWriter().print(json.toJSONString());
	}
	
}
