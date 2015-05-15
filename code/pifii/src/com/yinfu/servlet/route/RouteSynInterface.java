package com.yinfu.servlet.route;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


public class RouteSynInterface extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(RouteSynInterface.class);
	
	private String routersn;//设备标识
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
		response.setCharacterEncoding("UTF-8");
		routersn = request.getParameter("routersn");
		JSONArray taskArray = new JSONArray();
		if(StringUtils.isNotEmpty(routersn)){
			//查询未执行的任务
			String task_sql = "select id,uid,type,key_id from bp_task where status=0 and is_publish=1 and router_sn= ?";
			List<Record> taskList = Db.find(task_sql, new Object[]{routersn});
			
			//查询未执行的命令
			StringBuffer cmd_sql = new StringBuffer();
			cmd_sql.append("select id,uid,type,url,dir ");
			cmd_sql.append("from bp_cmd ");
			cmd_sql.append("where status=0 and uid in( ");
			cmd_sql.append("select uid from bp_task where status=0 and is_publish=1 and router_sn= ?");
			cmd_sql.append(") ");
			List<Record> cmdList = Db.find(cmd_sql.toString(), new Object[]{routersn});
			
			//组装数据
			for(int i=0;i<taskList.size();i++){
				Record task = taskList.get(i);
				int taskId = task.getInt("id");
				String taskType = task.getStr("type");
				
				JSONObject taskObj = new JSONObject();
				taskObj.put("task_id", taskId);
				taskObj.put("task_type", taskType);
				
				JSONArray cmdArray = new JSONArray();
				for(int j=0;j<cmdList.size();j++){
					Record cmd = cmdList.get(j);
					if(task.getStr("uid").equals(cmd.getStr("uid"))){
						JSONObject cmdObj = new JSONObject();
						cmdObj.put("cmd_id", cmd.getInt("id"));
						cmdObj.put("cmd_type", Integer.parseInt(cmd.getStr("type")));
						cmdObj.put("url", cmd.getStr("url"));
						cmdObj.put("dir", cmd.getStr("dir"));
						
						cmdArray.add(cmdObj);
					}
				}
				taskObj.put("cmd", cmdArray);
				taskArray.add(taskObj);
			}
		}
		
		response.getWriter().print(taskArray.toJSONString());
	}
	
}
