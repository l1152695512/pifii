package com.yinfu.plugin.quzrtz;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * 
 * 秒（0–59）    分钟（0–59）  小时（0–23）  月份中的日期（1–31）    月份（1–12或JAN–DEC） 星期中的日期（1–7或SUN–SAT）    年份（1970–2099）
 * 0 45 5 ? ? ?
 * @author l
 *
 */
public class DelTaskRes implements Job{
	private static Logger logger = Logger.getLogger(InsertAuthData.class);
	private static boolean flag = true;
	
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException  {
		if (flag) {
			try {
				flag = false;
				StringBuffer sbr = new StringBuffer();
				sbr.append(" SELECT sum(1) as total,sum(if(step =2 AND is_done = 1,1,0)) done_total,res_url  FROM bp_res_task WHERE 1=1");
				sbr.append(" AND res_url  like ('upload/routerSyn/%')");
				sbr.append(" AND delete_when_done = 0");
				sbr.append(" GROUP BY res_url having (sum(1)=sum(if(step =2 AND is_done = 1,1,0)))");
				sbr.append(" order by id asc");
				List<Record> record  = Db.find(sbr.toString());
				Object[][] params = new Object[record.size()][1];
				for(int i=0;i<record.size();i++){
					Record rec = record.get(i);
					String res_url = rec.getStr("res_url");
					File file = new File(PathKit.getWebRootPath()+File.separator+res_url);
					if(file.exists()){
						file.delete();
					}
					params[i] = new Object[]{res_url};
				}
				if(params.length > 0){
					Db.batch("update bp_res_task set delete_when_done = 1 where res_url = ?", params, params.length);
				}
				//删除routerSyn目录下的空文件夹，暂时没查出是哪里生成的临时空文件夹，是不用的
				File file = new File(PathKit.getWebRootPath()+File.separator+"upload"+File.separator+"routerSyn");
				File[] childrens = file.listFiles();
				for(int i=0;i<childrens.length;i++){
					if(childrens[i].isDirectory()){//如果该文件是文件夹则删除
						FileUtils.forceDelete(childrens[i]);
					}
				}
			} catch (Exception e) {
				logger.warn("定时删除已完成的同步任务文件异常！",e);
			} finally {
				flag = true;
			}
		}
	}

}
