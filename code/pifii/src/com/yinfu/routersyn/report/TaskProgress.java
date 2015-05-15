
package com.yinfu.routersyn.report;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.task.FlowPackTask;
import com.yinfu.routersyn.task.FunnyTask;
import com.yinfu.routersyn.task.GotoTask;
import com.yinfu.routersyn.task.IndexTask;
import com.yinfu.routersyn.task.IntroduceTask;
import com.yinfu.routersyn.task.PreferentialTask;
import com.yinfu.routersyn.task.RestaurantTask;
import com.yinfu.routersyn.task.SynAllTask;
import com.yinfu.routersyn.task.TideTask;
import com.yinfu.routersyn.util.SynUtils;

/**
 * 仅支持POST请求
 * 
 * 上报盒子下载资源的进度及状态：
 * 		进度：0-1之间的小数
 * 		状态：
 * 				0 - 初始值；（盒子不会上传这个状态值）
 *  			1-正在下载；
 *  			2-完成；（包含解压）
 *  			-1 - 失败；
 */
@ControllerBind(controllerKey = "/router/report/progress", viewPath = "/")
public class TaskProgress extends Controller {
	private static Logger logger = Logger.getLogger(TaskProgress.class);
	private static int retryTimes = 3;
	
//	@Before(POST.class)
	public void index() {
		Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
		try{
			String routersn = getPara("routersn");
			String info = getPara("info").replaceAll("&quot;", "\"");
			System.err.println(info);
			if(StringUtils.isNotEmpty(info)){
				JSONArray infoJson = JSONArray.parseArray(info);
				if(infoJson.size() > 0){
					List<String> sqls = new ArrayList<String>();
					for(int i=0;i<infoJson.size();i++){
						JSONObject sonJson = infoJson.getJSONObject(i);
						String taskId = sonJson.getString("taskId");
						int step = sonJson.getIntValue("step");
//						if(step == 0){
//							step = 1;
//						}
						double progress = sonJson.getDoubleValue("progress");
						
						//此处需注意要要传递任务的gen_res_date，通过该字段可判断上报进度任务的数据是否已过期，
						//场景：假如一个task_type为index_app的任务已经开始下载并在上报进度了，这时平台又安装或者删除了app，
						//		这个任务的res_url、md5、file_size、step、progress、cmd、gen_res_date、operate_date字段会被更新，
						//		cmd=da,会重新下载该任务，但是在更新该任务到重新下载任务的过程中以前的任务会来上报下载进度，此时就应该使用gen_res_date字段的值来标识数据是否已过期
						
						//场景变换：有多个未完成的task的task_type一样（旧的task不能被新的任务覆盖，这样会在商铺平台看任务进度时，发现一些进度丢失），
						//		则旧的task任务应该被取消，而且他们的进度应该依赖于最新的同task_type的任务，所以取消上面的场景，
						
						Record thisTask = Db.findFirst("select task_type,ifnull(related_task_id,'') related_task_id,task_desc from bp_res_task where id=? ", new Object[]{taskId});
						if(null != thisTask){
							if(-1 == step){//任务下载失败，记录失败日志
								String errorMsg = sonJson.getString("errorMsg");
								String id = UUID.randomUUID().toString();
								sqls.add("insert into bp_res_task_error(id,task_id,error_msg,error_date) values('"+id+"','"+taskId+"','"+errorMsg+"',now())");
								if(StringUtils.isBlank(thisTask.get("related_task_id").toString())){//这里需要判断该任务是否依赖于其他任务，如果依赖于其他任务，不能将step设值为-1
									//任务失败后主动尝试3次，有很多时候盒子判断md5时，出现错误（实际文件是对的），这是需要再次尝试下发任务
									//		如果下发任务为文件则直接更新该任务的is_done=0、step=0、progress=0；
									//		如果是压缩包就需要重新生成更新包，因为压缩包会有文件先后顺序的，假如发布的压缩包更新失败，后续又有广告和商铺信息的修改，并且这些任务更新成功，
									//			这时如果只是更新发布任务的状态，重新下发，会出现旧的index.html覆盖新的index.html文件，所以需要重新生成该任务的压缩包
									int errorTimes = getTaskFailTimes(taskId);
									if(errorTimes >= retryTimes){//失败次数已经大于尝试次数,提示任务失败
										sqls.add("update bp_res_task set step=-1,is_done=1 where id='"+taskId+"'");
									}else{
										reloadTask(taskId,thisTask.get("task_type").toString(),routersn,sqls,deleteRes,thisTask.get("task_desc").toString());
									}
								}else{
									sqls.add("update bp_res_task set is_done=1 where id='"+taskId+"'");
								}
							}else if(2 == step){//已完成的任务，如果任务标识中delete_when_done=1，则删除该任务的文件，此处应该修改为定时器去删除
//								Record rec = Db.findFirst("select res_url from bp_res_task where id=?",new Object[]{taskId});
//								if(null != rec){
//									File file = new File(PathKit.getWebRootPath()+File.separator+rec.getStr("res_url"));
//									if(file.exists()){
//										file.delete();
//									}
//								}
								//如果该任务为脚本文件更新或者Data文件更新则需要记录盒子的脚本及Data文件的版本号
//								Record rec = Db.findFirst("select task_type,ifnull(related_task_id,'') related_task_id from bp_res_task where id=? ",new Object[]{taskId});
								if(StringUtils.isBlank(thisTask.get("related_task_id").toString())){
									String task_type = thisTask.getStr("task_type");
									if(task_type.startsWith("lua") || task_type.startsWith("data")){
										Object versionNumId = task_type.split("_")[1];
										//这里已商量过，脚本更新和data文件更新使用的是一张表
										Record versionRec = Db.findFirst("select version from router_script_version where id=? ", new Object[]{versionNumId});
										if(null != versionRec){
											if(task_type.startsWith("lua")){
												sqls.add("update bp_device set script_version='"+versionRec.getStr("version")+"' where router_sn='"+routersn+"'");
											}else if(task_type.startsWith("data")){
												sqls.add("update bp_device set data_version='"+versionRec.getStr("version")+"' where router_sn='"+routersn+"'");
											}
										}
									}
									//更新任务的状态is_done为1,将同类型的资源都设置为已完成，并将依赖于该资源的资源状态也设置为完成，这里不能去掉related_task_id的条件，因为data文件和脚本文件更新的task_type包含ID
									sqls.add("update bp_res_task set step=2,is_done=1 where router_sn='"+routersn+"' and (task_type='"+task_type+"' or related_task_id='"+taskId+"')");
								}else{//如果该任务是依赖于其他任务（删除性的任务），则只更新该任务的is_done,下次不需要再拿到该任务
									sqls.add("update bp_res_task set is_done=1 where id='"+taskId+"'");
								}
							}else{//如果是上报下载中的任务
								if(StringUtils.isBlank(thisTask.get("related_task_id").toString())){//如果该任务不依赖于其他的任务，则更新任务状态
									if(progress > 1){
										progress = 1;
									}
//									sqls.add("update bp_res_task set step="+step+",progress="+progress+" where id='"+taskId+"' or (related_task_id='"+taskId+"' and progress<="+progress+")");
//									sqls.add("update bp_res_task set step="+step+",progress="+progress+" where id='"+taskId+"' and gen_res_date='"+sonJson.getString("gen_res_date")+"'");
									//更新同类型的所有任务进度
									if(null != thisTask.get("task_type") && StringUtils.isNotBlank(thisTask.getStr("task_type"))){
										//这个sql中必须加上step!=2，因为这里会改变同类型下载资源的step和progress，如果是完成的任务不应该更改这些信息
										sqls.add("update bp_res_task set step=1,progress="+progress+" where router_sn='"+routersn+"' and (task_type='"+thisTask.getStr("task_type")+"' or related_task_id='"+taskId+"') and step!=2");
									}
								}
							}
						}
					}
					if(sqls.size() > 0){
						Db.batch(sqls, sqls.size()+1);
					}
					SynUtils.deleteRes(deleteRes.get("success"));
				}
			}
			renderJson("success", "true");
		}catch(Exception e){
			e.printStackTrace();
			logger.error("任务进度上报异常！",e);
			renderJson("success", "false");
			SynUtils.deleteRes(deleteRes.get("fail"));
		}
	}
	
	private int getTaskFailTimes(Object taskId){
		Record errorInfo = Db.findFirst("select count(*) count from bp_res_task_error where task_id=? ", new Object[]{taskId});
		int times = 5;
		try{
			times = Integer.parseInt(errorInfo.get("count").toString());
		}catch(Exception e){
			e.printStackTrace();
		}
		return times;
	}
	
	private void reloadTask(String taskId,String taskType,String routerSn,List<String> sqls,Map<String,List<File>> deleteRes,String taskDesc){
		List<Record> synDevice = new ArrayList<Record>();
		synDevice.add(new Record().set("router_sn", routerSn));
		Record taskInfo = new Record().set("id", taskId);
		Record rec = Db.findFirst("select shop_id from bp_device where router_sn=? ", new Object[]{routerSn});
		if(null != rec){
			Map<String,List<File>> res = null;
			if(FlowPackTask.marker.equals(taskType)){
				res = FlowPackTask.synRes(rec.get("shop_id"), sqls, taskInfo,null,synDevice);
			}else if(FunnyTask.marker.equals(taskType)){
				res = FunnyTask.synRes(rec.get("shop_id"), sqls, taskInfo,null,synDevice);
			}else if(GotoTask.marker.equals(taskType)){
				res = GotoTask.synRes(rec.get("shop_id"), sqls, taskInfo,null,synDevice);
			}else if(IndexTask.marker.equals(taskType)){
				res = IndexTask.synRes(rec.get("shop_id"), sqls, taskInfo,null,synDevice);
			}else if(IntroduceTask.marker.equals(taskType)){
				res = IntroduceTask.synRes(rec.get("shop_id"), sqls, taskInfo,null,synDevice);
			}else if(PreferentialTask.marker.equals(taskType)){
				res = PreferentialTask.synRes(rec.get("shop_id"), sqls, taskInfo,null,synDevice);
			}else if(RestaurantTask.marker.equals(taskType)){
				res = RestaurantTask.synRes(rec.get("shop_id"), sqls, taskInfo,null,synDevice);
			}else if(TideTask.marker.equals(taskType)){
				res = TideTask.synRes(rec.get("shop_id"), sqls, taskInfo,null,synDevice);
			}else if(SynAllTask.marker.equals(taskType)){
				res = SynAllTask.synRes(rec.get("shop_id"), sqls, taskInfo,null,synDevice);
			}
			if(null != res){
				SynUtils.putAllFiles(deleteRes, res);
			}
		}
	}
}
