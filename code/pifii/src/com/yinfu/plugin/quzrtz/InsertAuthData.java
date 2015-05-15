package com.yinfu.plugin.quzrtz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.InitDemoDbConfig;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.jbase.util.NetWorkUtil;

/**
 * 定时更新认证数据
 * @author JiaYongChao
 *
 */
public class InsertAuthData implements Job {
	private static Logger logger = Logger.getLogger(InsertAuthData.class);
	private static final String INSERT_DATE =  DateUtil.formatDateByFormat(new Date(),"yyyy-MM-dd");
	private static boolean flag = true;
	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException  {
		if (flag) {
			try {
				logger.info(">>>>>>>>>>>>>>>>定时更新认证数据开始");
				flag = false;
				execute1();
				execute2();
				execute1();
				logger.info(">>>>>>>>>>>>>>>>结束定时更新认证数据");
			} catch (Exception e) {
				logger.info(">>>>>>>>>>>>>>>>结束定时更新认证数据");
			} finally {
				flag = true;
			}
		}
	}
	

	public  void execute1(){
		try {
			List<String> sqlList = new ArrayList<String>();
			//填充带有相同tag号码的address和cardtype
			StringBuffer  updateAddressCardtype =  new StringBuffer();
			updateAddressCardtype.append(" UPDATE bp_auth a1 JOIN ");
			updateAddressCardtype.append(" (SELECT tag,address,cardtype FROM bp_auth WHERE auth_type='phone' ");
			updateAddressCardtype.append(" AND address IS NOT NULL AND cardtype IS NOT NULL GROUP BY tag) a2");
			updateAddressCardtype.append(" ON (a1.auth_type='phone' AND a1.tag=a2.tag)");
			updateAddressCardtype.append(" SET a1.address=a2.address,a1.cardtype=a2.cardtype");
			sqlList.add(updateAddressCardtype.toString());
			//填充带有相同client_mac的ftutype
			StringBuffer updateFtutype = new StringBuffer();
			updateFtutype.append(" UPDATE bp_auth a1 JOIN ");
			updateFtutype.append(" (SELECT client_mac,ftutype FROM bp_auth ");
			updateFtutype.append(" WHERE client_mac IS NOT NULL AND ftutype IS NOT NULL GROUP BY client_mac) a2");
			updateFtutype.append(" ON (a1.client_mac=a2.client_mac)");
			updateFtutype.append(" SET a1.ftutype=a2.ftutype");
			sqlList.add(updateFtutype.toString());
			Db.batch(sqlList, sqlList.size());
		} catch (Exception e) {
		}
	}
	
	
	//填充新的tag号码的address和cardtype和填充新的client_mac的ftutype
	public   void execute2() {
		try {
			// TODO Auto-generated method stub
			/**String sql =" SELECT * FROM bp_auth  s WHERE s.`client_mac` IS NOT NULL  AND s.`client_mac` !='' AND (s.`cardtype` IS NULL OR s.`ftutype` IS NULL OR s.`address` IS NULL )  AND auth_type='phone' ";**/
			//String sql =" SELECT * FROM bp_auth  WHERE client_mac IS NOT NULL  AND client_mac !='' AND (cardtype IS NULL OR ftutype IS NULL OR address IS NULL )";
			String sql_a  = "SELECT * FROM bp_auth WHERE cardtype IS NULL  OR address IS NULL GROUP BY TAG  ORDER BY ID ASC" ;
			List<Record> dataList_a = Db.find(sql_a.toString());
			List<String> sqlList = new ArrayList<String>();
			for(Record r:dataList_a){
				String address = r.getStr("address");//归属地
				String cardType = r.getStr("cardtype");//卡类型
				String authType = r.getStr("auth_type");//认证类型
				if(authType!=null && !authType.isEmpty()){
					if(authType.equals("phone")){//如果是手机认证
						String mobile = r.getStr("tag");//手机号码
						if(address==null || address.isEmpty()){
							address = NetWorkUtil.byPhoneGetAddress(mobile);
							String updateSql = " update bp_auth set address='"+address+"' where tag='"+mobile+"'";
							sqlList.add(updateSql);
						}
						if(cardType==null || cardType.isEmpty()){
							cardType = NetWorkUtil.byPhoneGetCardType(mobile);
							String updateSql = " update bp_auth set cardtype='"+cardType+"' where tag='"+mobile+"'";
							sqlList.add(updateSql);
						}
					}
				}
			}
//			if(mac!=null && StringUtils.isNotEmpty(mac)){
//				ftuType = NetWorkUtil.byMacGetInfo(mac);
//				String updateSql = " update bp_auth set ftutype='"+ftuType+"' where client_mac='"+mac+"'";
//				sqlList.add(updateSql);
//			}
			String sql_b = "SELECT * FROM bp_auth WHERE  client_mac IS NOT NULL  AND client_mac !='' AND ftutype IS NULL  GROUP BY client_mac ORDER BY ID ";
			List<Record> dataList_b = Db.find(sql_b.toString());
			for(Record r:dataList_b){
				String mac = r.getStr("client_mac");//mac地址
				String ftuType  = r.getStr("ftutype");//终端类型
				  if(mac!=null && StringUtils.isNotEmpty(mac)){
					ftuType = NetWorkUtil.byMacGetInfo(mac);
					String updateSql = " update bp_auth set ftutype='"+ftuType+"' where client_mac='"+mac+"'";
					sqlList.add(updateSql);
				}
			}
			Db.batch(sqlList, sqlList.size());
		} catch (Exception e) {
		}
	}
}
