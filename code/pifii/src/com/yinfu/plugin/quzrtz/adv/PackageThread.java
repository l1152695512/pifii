package com.yinfu.plugin.quzrtz.adv;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.jfinal.ext.DbExt;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.routersyn.task.GotoTask;
import com.yinfu.routersyn.task.IndexTask;
import com.yinfu.routersyn.util.SynUtils;

public class PackageThread extends Thread{
	private static Logger logger = Logger.getLogger(PackageThread.class);
	
	private static int MAX_TRY_TIMES = 5;
	private static int SLEEP_TIME = 200;
	private Map<String,List<Record>> advs;
	private String shopId;
	
	public PackageThread(String shopId,Map<String,List<Record>> advs) {
		this.shopId = shopId;
		this.advs = advs;
	}
	
	@Override
	public void run() {
		try{
			int currentTryTimes = 0;
			while(true){//解决开启事务后，批量更新报死锁的问题
				final Map<String,List<File>> deleteRes = new HashMap<String,List<File>>();
				try{
					boolean isSuccess = Db.tx(new IAtom(){public boolean run() throws SQLException {//这里用事务使用多线程时会出现死锁的情况
						List<Record> updateAdvs = new ArrayList<Record>();
						Iterator<String> ite = advs.keySet().iterator();
						while(ite.hasNext()){
							String key = ite.next();
							Iterator<Record> iteAdv = advs.get(key).iterator();
							while(iteAdv.hasNext()){
								Record rec = iteAdv.next();
								updateAdvs.add(rec);
							}
						}
						if(updateAdvs.size() > 0){
							Object[][] params = new Object[updateAdvs.size()][3];
							for(int i=0;i<updateAdvs.size();i++){
								params[i] = new Object[]{updateAdvs.get(i).get("adv_content_id"),shopId,updateAdvs.get(i).get("adv_space")};
							}
							DbExt.batch("update bp_adv_shop set content_id=? where shop_id=? and adv_spaces=? ", params);
							
							List<String> sqls = new ArrayList<String>();
							if(advs.containsKey("adv")){
								Record taskInfo = new Record().set("task_desc", "定时更新广告").set("is_show", "0");
								Map<String,List<File>> res = IndexTask.synRes(shopId, sqls, taskInfo,null,null);
								if(null != res){
									SynUtils.putAllFiles(deleteRes, res);
								}else{
									return false;
								}
							}
							if(advs.containsKey("adv_start")){
								Record taskInfo = new Record().set("task_desc", "定时更新广告").set("is_show", "0");
								Map<String,List<File>> res = GotoTask.synRes(shopId, sqls, taskInfo,null,null);
								if(null != res){
									SynUtils.putAllFiles(deleteRes, res);
								}else{
									return false;
								}
							}
							if(sqls.size() > 0){
								DbExt.batch(sqls);
							}
						}
						return true;
					}});
					if(isSuccess){
						SynUtils.deleteRes(deleteRes.get("success"));
					}else{
						SynUtils.deleteRes(deleteRes.get("fail"));
					}
					break;
				}catch(Exception e){
					SynUtils.deleteRes(deleteRes.get("fail"));
					logger.warn("定时更新广告异常！已尝试次数："+currentTryTimes, e);
					Thread.sleep((int)(SLEEP_TIME*Math.random()));
					if(currentTryTimes < MAX_TRY_TIMES){
						currentTryTimes++;
					}else{
						break;
					}
				}
			}
		}catch(Exception e){
			logger.warn("定时更新广告异常！", e);
		}finally{
			UpdatePutinAdvJob.updateThreadNum(-1);
		}
	}
}
