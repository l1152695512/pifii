
package com.yinfu.plugin.quzrtz;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.jbase.util.PropertyUtils;

public class StatisticsAdv implements Job {
	private static Logger logger = Logger.getLogger(StatisticsPf_old.class);
	/**
	 * 插入广告统计数据
	 */
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
			logger.warn(">>>>>>>>>>>>>>>>开始插入广告统计数据");
			final String curentDate = DateUtil.getSpecifiedDayBefore(DateUtil.getNow());
			StringBuilder formSqlSb = new StringBuilder(" ");  
			formSqlSb.append(" INSERT INTO bp_statistics_adv(shopId,zss,dates,djs) SELECT s.`id` AS shopId,IFNULL(advzs.advs,0) AS zss,sd.search_date AS dates,IFNULL(djs.advdj,0) AS djs");
			formSqlSb.append(" FROM bp_shop s ");
			formSqlSb.append(" LEFT JOIN sys_org org ON s.`org_id` = org.`id` ");
			formSqlSb.append(" JOIN bp_search_day sd ON (sd.search_date>='"+curentDate+"' AND sd.search_date<='"+curentDate+"')  ");
			formSqlSb.append(" LEFT JOIN (  ");
			formSqlSb.append(" SELECT CAST(COUNT(*) AS SIGNED)*5 AS advs,sp.`id` AS shopid,sd.search_date  AS dates  ");
			formSqlSb.append(" FROM bp_statistics_all advs ");
			formSqlSb.append(" LEFT JOIN bp_search_day sd ON sd.search_date=DATE_FORMAT(advs.access_date,'%Y-%m-%d')   ");
			formSqlSb.append(" LEFT JOIN bp_device dv ON  dv.`router_sn` = advs.`router_sn` AND dv.delete_date IS NULL   ");
			formSqlSb.append(" LEFT JOIN bp_shop sp ON dv.`shop_id` = sp.`id` AND  sp.delete_date IS NULL  ");
			formSqlSb.append("  WHERE DATE_FORMAT(advs.access_date,'%Y-%m-%d')>='"+curentDate+"' AND DATE_FORMAT(advs.access_date,'%Y-%m-%d')<='"+curentDate+"' AND advs.`statistics_id`=-1 AND  advs.router_sn !=''   ");
			formSqlSb.append("  GROUP BY sp.`id`,sd.search_date  ");
			formSqlSb.append(" ) AS advzs ON advzs.shopid = s.`id` AND advzs.dates=sd.search_date ");
			formSqlSb.append(" LEFT JOIN ( ");
			formSqlSb.append("  SELECT COUNT(*) AS advdj,s.`id` AS shopid,sd.search_date  AS dates   ");
			formSqlSb.append("  FROM bp_statistics_all adv   ");
			formSqlSb.append(" LEFT JOIN bp_search_day sd ON sd.search_date=DATE_FORMAT(adv.access_date,'%Y-%m-%d')   ");
			formSqlSb.append(" LEFT JOIN bp_device  dv ON adv.`router_sn` = dv.`router_sn` AND dv.delete_date IS NULL  ");
			formSqlSb.append(" LEFT JOIN bp_shop s ON s.`id` = dv.`shop_id`   AND  s.delete_date IS NULL ");
			formSqlSb.append("  WHERE  DATE_FORMAT(adv.access_date,'%Y-%m-%d')>='"+curentDate+"' AND DATE_FORMAT(adv.access_date,'%Y-%m-%d')<='"+curentDate+"' AND  adv.`statistics_type`='"+PropertyUtils.getProperty("route.upload.type.adv")+"'    ");
			formSqlSb.append(" GROUP BY s.`id`,sd.search_date   ");
			formSqlSb.append(" ) AS djs ON  djs.shopid= s.`id`   AND djs.dates = sd.search_date  ");
			formSqlSb.append(" WHERE s.`delete_date` IS NULL  ");
			Db.update(formSqlSb.toString());
	}
}
