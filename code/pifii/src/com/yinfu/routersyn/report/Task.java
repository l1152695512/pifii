
package com.yinfu.routersyn.report;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.core.Controller;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.kit.JsonKit;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.task.DataTask;
import com.yinfu.routersyn.util.SynUtils;
import com.yinfu.routersyn.util.VerifyUtil;

@ControllerBind(controllerKey = "/router/report/task", viewPath = "/")
public class Task extends Controller {
	private static Logger logger = Logger.getLogger(Task.class);
	private static Map<String,JSONObject> requestUpdateClient = new HashMap<String,JSONObject>();
	
	private String[] taskTypePriority = new String[]{"lua","data","publish"};//标识任务类型的优先级，必须前面的升级完成后才能升级后面的
	
	/**
	 * 此处要添加过滤，同一个盒子的请求只能有一次
	 */
//	@Before(POST.class)
	public void index(){//如果想让某个资源重新下载需要修改bp_res_task表的step为1，is_done为0,progress为0
		String sn = getPara("routersn");
		List<Record> resData = new ArrayList<Record>();
		if(canGetRes(sn)){//同一个盒子同一时间只能访问一次
			try{
				Record device = Db.findFirst("select ifnull(script_version,'0') script_version,ifnull(data_version,'0') data_version from bp_device where router_sn=? ",new Object[]{sn});
				if(null != device){//以下任务分顺序下载，有脚本文件升级先升级脚本文件，升级完成后如果有Data文件升级则升级Data文件，升级完成后下载更新任务
					checkScript(device.getStr("script_version"));//检查并生成脚本升级任务，场景：发布脚本升级包时不会生成盒子的更新任务（如果在发布盒子处对盒子下发升级任务的话，会出现后来加的盒子不能取到升级包），所以需要在盒子请求下载任务时判断是否需要升级
					checkData(device.getStr("data_version"));//检查并生成Data升级任务
					
					//下面的sql不能加and related_task_id is null ，因为related_task_id不为null时，需要让盒子删除该下载任务，并上报，所以在上报盒子任务完成时不能加related_task_id is null
					List<Record> unfinishedResData = Db.find("select id,res_type,res_url,local_path,md5,file_size,cmd,task_type from bp_res_task where router_sn=? and !is_done order by res_type,operate_date",
							new Object[]{sn});
					Map<String,List<Record>> tasks = new HashMap<String,List<Record>>();
					tasks.put("other", new ArrayList<Record>());
					for(int i=0;i<taskTypePriority.length;i++){
						tasks.put(taskTypePriority[i], new ArrayList<Record>());
					}
					Iterator<Record> ite = unfinishedResData.iterator();
					while(ite.hasNext()){
						Record rec = ite.next();
						String taskType = rec.getStr("task_type");
						String type = taskType.split("_")[0];//lua下载任务的task_type=lua_脚本升级包的id，data下载任务的task_type=data_data升级包的id
						List<Record> thisTaskTypeTask = tasks.get(type);
						if(null == thisTaskTypeTask){
							thisTaskTypeTask = tasks.get("other");
						}
						thisTaskTypeTask.add(rec);
					}
					boolean founded = false;
					for(int i=0;i<taskTypePriority.length;i++){
						List<Record> thisTaskTypeTask = tasks.get(taskTypePriority[i]);
						if(thisTaskTypeTask.size() > 0){
							resData = thisTaskTypeTask;
							founded = true;
							break;
						}
					}
					if(!founded){
						resData = tasks.get("other");
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				logger.warn("获取盒子同步文件异常！", e);
			}finally{
				requestUpdateClient.remove(sn);
			}
		}
		renderJson(JsonKit.toJson(resData));
	}
	
	/**
	 * 检查脚本文件是否有更新
	 */
	private void checkScript(String scriptVersion){
		Record newVersion = new Record();
		if(needUpdate(scriptVersion,newVersion,"script")){
			final String resourcePath = newVersion.getStr("res_url");
			final String scriptId = newVersion.getStr("id");
			List<Record> scriptTask = Db.find("select id from bp_res_task where router_sn=? and task_type=? ", new Object[]{getPara("routersn"),"lua_"+scriptId});
			if(scriptTask.size() == 0){//如果最新更新包下载任务不存在
				Db.tx(new IAtom(){public boolean run() throws SQLException {
					boolean success  = false;
					String id = UUID.randomUUID().toString();
					String filePath = PathKit.getWebRootPath() + File.separator+(resourcePath.replaceAll("/", File.separator+File.separator));
					File file = new File(filePath);
					if(file.exists()){
						//拷贝一份文件作为盒子的更新包，使用原始更新包的原因是：如果盒子有多个脚本更新包时，这里会删除旧的更新包，不再让盒子下载，只用下载最新的更新包即可
						File downLoadFolder = new File(PathKit.getWebRootPath()+File.separator+"upload"+File.separator+"routerSyn");
						try {
							FileUtils.copyFileToDirectory(file, downLoadFolder);
							File newFile = new File(downLoadFolder.getAbsolutePath()+File.separator+file.getName());
							if(newFile.exists()){
								String newFileMd5 = VerifyUtil.getMD5(newFile);
								List<Record> oldScriptUpdate = Db.find("select id,res_url from bp_res_task where router_sn=? and left(task_type,4)=? and step!=2", new Object[]{getPara("routersn"),"lua_"});
								long file_size = newFile.length();
								int changeRows = Db.update("insert into bp_res_task(id,router_sn,res_type,task_type,res_url,md5,file_size,step,progress,is_show,delete_when_done,task_desc,gen_res_date,operate_date) "
										+ "values(?,?,'lua',?,?,?,?,0,0,1,0,'更新盒子脚本文件',now(),now())", new Object[]{id,getPara("routersn"),"lua_"+scriptId,resourcePath,newFileMd5,file_size});
								if(changeRows > 0){
									success = true;
									if(oldScriptUpdate.size() > 0){
										Db.update("update bp_res_task set cmd='d',related_task_id='"+id+"' where id in ("+recordListToIdIn(oldScriptUpdate)+")");
										//删除旧的脚本升级包，避免盒子继续下载旧的升级包
										Iterator<Record> ite = oldScriptUpdate.iterator();
										while(ite.hasNext()){
											Record rec = ite.next();
											File oldScript = new File(PathKit.getWebRootPath()+File.separator+rec.getStr("res_url"));
											if(oldScript.exists()){
												FileUtils.forceDelete(oldScript);
											}
										}
									}
								}else{
									success = false;
									logger.warn("插入脚本更新包失败！");
								}
							}else{
								logger.warn("复制的脚本文件不存在！");
							}
						} catch (IOException e) {
							e.printStackTrace();
							logger.warn("复制升级的脚本文件异常！",e);
						}
					}else{
						logger.warn("要更新的脚本文件不存在！");
					}
					return success;
				}});
			}
		}
	}
	
	private void checkData(String version){
		Record versionRec = new Record();
		if(needUpdate(version,versionRec,"data")){
			//判断是否有最新的Data更新包在下载
			List<Record> dataTask = Db.find("select id from bp_res_task where router_sn=? and task_type=? ", new Object[]{getPara("routersn"),"data_"+versionRec.getStr("id")});
			if(dataTask.size() == 0){
				List<String> sqls = new ArrayList<String>();
				List<Record> publishDevices = new ArrayList<Record>();
				Record publishDevice = new Record();
				publishDevice.set("router_sn", getPara("routersn"));
				publishDevices.add(publishDevice);
				File latestData = new File(PathKit.getWebRootPath() + File.separator+(versionRec.getStr("res_url").replaceAll("/", File.separator+File.separator)));
				Record taskInfo = new Record().set("task_type", DataTask.marker+"_"+versionRec.getStr("id")).set("task_desc", "更新盒子Data包");
				Map<String,List<File>> res = DataTask.synRes(sqls, taskInfo, null, publishDevices, latestData);
				if(null != res){
					if(sqls.size()>0){
						Db.batch(sqls,sqls.size()+1);
						SynUtils.deleteRes(res.get("success"));
					}
				}
			}
		}
	}
	
	private boolean needUpdate(String version,Record versionRec,String fileType){
		StringBuffer sql = new StringBuffer();
		sql.append("select v.id,ifnull(v.res_url,'') res_url,ifnull(v.version,'') version ");
		sql.append("from router_script_update_role r join router_script_version v on (v.file_type=? and r.version_id=v.id) ");
		sql.append("left join bp_device d on (d.router_sn=?) ");
		sql.append("where r.related_id is null or r.related_id=? or r.related_id=d.shop_id ");
		sql.append("order by v.create_date desc ");
		Record newVersion = Db.findFirst(sql.toString(), new Object[]{fileType,getPara("routersn"),getPara("routersn")});
		if(null == newVersion){//如果没有指定该盒子的升级，则使用最新发布的针对全部盒子的版本
			newVersion = Db.findFirst("select id,ifnull(res_url,'') res_url,ifnull(version,'') version from router_script_version where file_type=? and type = 1 order by create_date desc ",
					new Object[]{fileType});
		}
		if(null != newVersion){//有要更新的脚本文件
			double thisVersion = StringUtils.isBlank(version)?0:versionToNum(version);//最开始这个字段是没有值的，这种情况盒子会去更新脚本文件
			double updateVersion = versionToNum(newVersion.getStr("version"));
			if(updateVersion !=-1 && thisVersion != -1 && updateVersion>thisVersion){
				versionRec.set("id", newVersion.get("id"));
				versionRec.set("res_url", newVersion.get("res_url"));
				return true;
			}
		}
		return false;
	}
	
	private double versionToNum(String version){//version格式1.4
		double versionNum = -1;
		try{
			versionNum = Double.parseDouble(version);
		}catch(Exception e){
			e.printStackTrace();
			logger.warn("版本转数字异常！",e);
		}
		return versionNum;
	}
	
	private String recordListToIdIn(List<Record> resData){
		StringBuffer sqlIn = new StringBuffer("'");
		Iterator<Record> ite = resData.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			sqlIn.append(rowData.get("id")+"','");
		}
		sqlIn.append("'");
		return sqlIn.toString();
	}
	

	private synchronized boolean canGetRes(String sn){
		JSONObject clientInfo = requestUpdateClient.get(sn);
		if(null == clientInfo){
			clientInfo = new JSONObject();
			clientInfo.put("requestDate", new Date());
			requestUpdateClient.put(sn, clientInfo);
			return true;
		}else{
			return false;
		}
	}
}
