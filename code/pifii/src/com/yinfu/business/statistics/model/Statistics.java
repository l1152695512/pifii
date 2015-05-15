
package com.yinfu.business.statistics.model;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.util.ChartUtil;
import com.yinfu.business.util.PageUtil;
import com.yinfu.jbase.jfinal.ext.Model;
import com.yinfu.jbase.util.DateUtil;
import com.yinfu.jbase.util.ImageKit;
import com.yinfu.jbase.util.PropertyUtils;


@SuppressWarnings({ "rawtypes", "unchecked" })
public class Statistics extends Model<Statistics> {
	private static Logger logger = Logger.getLogger(Statistics.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static Statistics dao = new Statistics();
	/**
	 * 
	 */
	public int getOnLineTotal(String shopId) {
		int sum = 0;
		int interval = PropertyUtils.getPropertyToInt("route.uploadInterval", 600);// 路由上报数据的时间间隔，要考虑网络延迟
		String sql = "select ifnull(sum(online_num),0) online_num from bp_device where shop_id = ? and date_add(report_date, interval ? second) > now()";
		Record rd = Db.findFirst(sql, new Object[] { shopId, interval });
		sum = Integer.parseInt(rd.get("online_num").toString());
		return sum;
	}
	
	public int getDeviceAccessNum(String shopId) {
		int sum = 0;
		int maxAccessNum = PropertyUtils.getPropertyToInt("router.maxAccessNum", 20);
		String sql = "select sum("+maxAccessNum+") num from bp_device where shop_id = ? ";
		List<Record> list = Db.find(sql, new Object[] { shopId });
		if (list.size() > 0) {
			try {
				Record record = list.get(0);
				sum = null == record.getBigDecimal("num") ? 0 : record.getBigDecimal("num").intValue();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sum;
	}
	
	//@formatter:off 
	/**
	 * Title: getClientTotal
	 * Description:首页客流统计
	 * Created On: 2014年8月19日 下午1:52:56
	 * @author JiaYongChao
	 * <p>
	 * @param shopId商铺ID
	 * @return 
	 */
	//@formatter:on
	public JSONObject getClientTotal(String shopId) {
		JSONObject json = new JSONObject();
		int y_total = 0;
		int total = 0;
		try {
			StringBuffer sqlToday = new StringBuffer();
			/*
			 * 从基础数据表中查询
			 * select count(DISTINCT date(a.access_date),a.client_mac) as total 
			 * from bp_statistics_all a join bp_device b on (b.shop_id=? and a.router_sn =  b.router_sn) 
			 * 
			 * where to_days(now()) - to_days( a.access_date) = 1 
			 */
			sqlToday.append("select count(distinct(r.mac)) total ");
			sqlToday.append("from bp_report r join bp_device d on (d.shop_id=? and r.sn=d.router_sn) ");
			sqlToday.append("where date(r.create_date)=date(now()) group by r.sn ");
			Record rd = Db.findFirst(sqlToday.toString(),new Object[]{shopId});
			if(null != rd){
				total = Integer.parseInt(rd.get("total").toString());
			}
			StringBuffer sqlHistory = new StringBuffer();
			sqlHistory.append("select IFNULL(sum(IFNULL(sp.counts,0)),0) as total ");
			sqlHistory.append("from bp_statistics_pf sp join bp_device d on (d.shop_id=? and sp.router_sn = d.router_sn) ");
			Record historyData = Db.findFirst(sqlHistory.toString(),new Object[]{shopId});
			total += Integer.parseInt(historyData.get("total").toString());
			
			StringBuffer sqlYestoday = new StringBuffer();
			sqlYestoday.append("select IFNULL(sum(IFNULL(sp.counts,0)),0) as total ");
			sqlYestoday.append("from bp_statistics_pf sp join bp_device d on (d.shop_id=? and sp.router_sn = d.router_sn) ");
			sqlYestoday.append("where to_days(now()) - to_days(sp.date) = 1 ");
			Record yrd = Db.findFirst(sqlYestoday.toString(),new Object[]{shopId});
			y_total = 0;
			if(null != yrd){
				y_total = Integer.parseInt(yrd.get("total").toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		json.put("y_total", y_total);
		json.put("total", total);
		return json;
	}
	
	public int getAdvClick(String shopId) {
		int sum = 0;
		StringBuffer sql = new StringBuffer(" select ");
		sql.append(" ifnull(sum(case when to_days(now()) - to_days( a.create_date) = 1 then 1 else 0 end),0) as y_total ");
		sql.append(" from bp_adv_log a ");
		sql.append(" left join bp_device b ");
		sql.append(" on a.dev_mac = b.mac ");
		sql.append(" where b.shop_id  = ? ");
		
		List list = Db.find(sql.toString(), new Object[] { shopId });
		Map map = (Map) list.get(0);
		sum = Integer.parseInt(map.get("y_total").toString());
		
		return sum;
	}
	
	//@formatter:off 
	/**
	 * Title: getAdvShow
	 * Description:近七日广告展示统计
	 * Created On: 2014年8月15日 上午10:39:13
	 * @author JiaYongChao
	 * <p>
	 * @param shopId
	 * @return 
	 */
	//@formatter:on
	public String  getAdvShow(String shopId) {
		StringBuffer sql = new StringBuffer("select ");
		sql.append("ifnull(sum(case when (to_days(now()) - to_days( a.access_date) = 0) then 1 else 0 end),0) as t1, ");
		sql.append("ifnull(sum(case when (to_days(now()) - to_days( a.access_date) = 1) then 1 else 0 end),0) as t2, ");
		sql.append("ifnull(sum(case when (to_days(now()) - to_days( a.access_date) = 2) then 1 else 0 end),0) as t3, ");
		sql.append("ifnull(sum(case when (to_days(now()) - to_days( a.access_date) = 3) then 1 else 0 end),0) as t4, ");
		sql.append("ifnull(sum(case when (to_days(now()) - to_days( a.access_date) = 4) then 1 else 0 end),0) as t5, ");
		sql.append("ifnull(sum(case when (to_days(now()) - to_days( a.access_date) = 5) then 1 else 0 end),0) as t6, ");
		sql.append("ifnull(sum(case when (to_days(now()) - to_days( a.access_date) = 6) then 1 else 0 end),0) as t7 ");
		sql.append("from bp_statistics_all a join bp_device b on (b.shop_id  = ? and a.router_sn = b.router_sn) ");
		sql.append("where a.statistics_id='-1' ");
		Record rd = Db.findFirst(sql.toString(), new Object[] { shopId });
		Map map = new HashMap();
		String[] str = new String[]{"一","二","三","四","五","六","七"};
		for(int i=0;i<rd.getColumns().size();i++){
			String num =  rd.getColumns().get("t"+Integer.valueOf(((i+1)))).toString();
			map.put(str[i], num);
		}
		StringBuffer xmlData = new StringBuffer();
		String caption =  "近7日广告展示数";
		xmlData.append("<chart  caption='"+ caption+ "' palette='1'   numberSuffix='次' divLineColor ='cacaca'   rotatevalues='1' placevaluesinside='1' legendshadow='0' legendborderalpha='0' canvasBgColor='EBEBEB'  showborder='0'>");
		
		xmlData.append("<categories>");
		for(int i =0;i<rd.getColumns().size();i++){//循环7次
			xmlData.append("<category label='" + str[i] + "' />");
		}
		xmlData.append("</categories>");
		xmlData.append("<dataset  color='077ec2' showvalues='1'>");
		for(int i =0;i<rd.getColumns().size();i++){//循环7次
			xmlData.append("<set value='" + map.get(str[i]) + "' />");
		}
		xmlData.append("</dataset>");
		xmlData.append("</chart>");
		return xmlData.toString();
	}
	
	public int getSmsTotal(String shopId) {
		int sum = 0;
		String sql = "select ifnull(sum(send_num),0)  as sms_total from bp_sms where status = 1 and shop_id = ?";
		List list = Db.find(sql, new Object[] { shopId });
		Map map = (Map) list.get(0);
		sum = Integer.parseInt(map.get("sms_total").toString());
		
		return sum;
	}
	
	//@formatter:off 
	/**
	 * Title: getAdCharts
	 * Description:获得广告统计图表
	 * Created On: 2014年8月12日 上午11:03:40
	 * @author JiaYongChao
	 * <p>
	 * @param shopId商铺ID
	 * @param firstDay开始日期
	 * @param endDay结束日期
	 * @return 
	 */
	//@formatter:on
	public String getAdCharts(String shopId, String firstDay, String endDay) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String startDate = firstDay;// 开始时间
		String endDate = endDay;// 结束时间
		if (firstDay == null || firstDay.equals("")) {
			c.add(Calendar.MONTH, 0);
			c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			startDate = format.format(c.getTime());// 当前年月的第一天
			firstDay = startDate;
		} else if (endDay == null || endDay.equals("")) {
			endDate = DateUtil.getNow();
			endDay = endDate;
		}
		String[] days = DateUtil.scopeTime(startDate, endDate);// 获得天数
		/*
		 * String[] dateArr = years.split("-"); String monthString = dateArr[1]; if (monthString.length() == 1) {
		 * monthString = "0" + monthString; } String yearAndMonth = dateArr[0] + "-" + monthString; int maxDay
		 * =getMaxDay(Integer.parseInt(dateArr[0]), Integer.parseInt(dateArr[1]));
		 */
		
		StringBuffer zsSql = new StringBuffer("SELECT ");
		zsSql.append(" DATE_FORMAT(a.access_date,'%c-%e') AS day_num,COUNT(*) AS zs");
		zsSql.append(" FROM bp_statistics_all a  ");
		zsSql.append(" LEFT JOIN bp_device d ON a.router_sn = d.router_sn ");
		zsSql.append(" WHERE  d.shop_id ="+shopId+" and DATE_FORMAT(a.access_date, '%Y-%m-%d') BETWEEN '"+firstDay+"' AND '"+endDay+"'  AND a.`statistics_id` IS NOT NULL AND a.`statistics_id`='-1' ");
		zsSql.append(" GROUP BY  DATE_FORMAT(a.access_date, '%Y-%m-%d') ");
		List<Record> zslist = Db.find(zsSql.toString());
		Map zsMap = new HashMap();
		for (int i = 0; i < days.length; i++) {
			boolean flag = true;
			for (int f = 0; f < zslist.size(); f++) {
				Record zsRd = zslist.get(f);
				String nva = zsRd.getStr("day_num");
				if (nva.equals(days[i])) {
					zsMap.put(days[i], zsRd.get("zs"));
					flag = false;
					break;
				}
			}
			if (flag) {
				zsMap.put(days[i], 0);
			}
		}
		
		StringBuffer djSql = new StringBuffer(" SELECT DATE_FORMAT(a.access_date,'%c-%e') AS day_num,COUNT(*) AS dj  ");
		djSql.append("FROM bp_statistics_all a  ");
		djSql.append("LEFT JOIN bp_device d ON a.router_sn = d.router_sn ");
		djSql.append(" WHERE a.`statistics_type`='"+PropertyUtils.getProperty("route.upload.type.adv")+"' AND   d.shop_id ="+shopId+" AND DATE_FORMAT(a.access_date, '%Y-%m-%d') BETWEEN '"+firstDay+"' AND '"+endDay+"' and a.`statistics_id` IS NOT NULL  ");
		djSql.append(" GROUP BY  DATE_FORMAT(a.access_date, '%Y-%m-%d') ");
		List<Record> djlist = Db.find(djSql.toString());
		Map djMap = new HashMap();
		for (int i = 0; i < days.length; i++) {
			boolean flag = true;
			for (int ii = 0; ii < djlist.size(); ii++) {
				Record djRd = djlist.get(ii);
				String nva = djRd.getStr("day_num");
				if (nva.equals(days[i])) {
					int clickNum = Integer.valueOf(djRd.get("dj").toString());
					int showNum = Integer.parseInt(zsMap.get(days[i]).toString());
					if(showNum < clickNum){
						clickNum = showNum;
					}
					djMap.put(days[i], clickNum);
					flag = false;
					break;
				}
			}
			if (flag) {
				djMap.put(days[i], 0);
			}
		}
		
		String caption = startDate + "至" + endDate + "广告分日统计表";
		StringBuffer chartXmlData = new StringBuffer();
		chartXmlData
				.append("<chart palette='1' caption='"
						+ caption
						+ "' xAxisName='日期（号）' showlabels='1' showvalues='0' numbersuffix='次' snumbersuffix='%' syaxisvaluesdecimals='2' connectnulldata='0' pyaxisname='展示/点击次数' syaxisname='点击率' numdivlines='4' formatnumberscale='0' hoverCapSepChar='/'>");
		chartXmlData.append("<categories>");
		for (int i = 0; i < days.length; i++) {
			chartXmlData.append("<category label='" + days[i] + "' />");
		}
		chartXmlData.append("</categories>");
		
		chartXmlData.append("<dataset seriesname='展示次数' color='AFD8F8' showvalues='0'>");
		for (int i = 0; i < days.length; i++) {
			chartXmlData.append("<set value='" + zsMap.get(days[i]) + "' />");
		}
		chartXmlData.append("</dataset>");
		
		chartXmlData.append("<dataset seriesname='点击次数' color='F6BD0F' showvalues='0'>");
		for (int i = 0; i < days.length; i++) {
			chartXmlData.append("<set value='" + djMap.get(days[i]) + "' />");
		}
		chartXmlData.append("</dataset>");
		
		chartXmlData.append("<dataset seriesname='点击率' color='8BBA00' showvalues='0' parentyaxis='S' renderas='Line'>");
		for (int i = 0; i < days.length; i++) {
			String rate = "0";
			int zs = Integer.parseInt(zsMap.get(days[i]).toString());
			int dj = Integer.parseInt(djMap.get(days[i]).toString());
			if (zs > 0) {
				float num = (float) dj / zs;
				DecimalFormat df = new DecimalFormat("0.00");
				rate = df.format(num * 100);
			}
			chartXmlData.append("<set value='" + rate + "' />");
		}
		chartXmlData.append("</dataset>");
		
		chartXmlData.append("</chart>");
		
		return chartXmlData.toString();
	}
	
	//@formatter:off 
	/**
	 * Title: getPvStatistics
	 * Description:访问量统计
	 * Created On: 2014年8月12日 下午5:07:47
	 * @author JiaYongChao
	 * <p>
	 * @param type 显示的类型adv,app等
	 * @param shopId商铺ID
	 * @param firstDay开始时间
	 * @param endDay结束时间
	 * @return 
	 */
	//@formatter:on
	public String getPvStatistics(String shopId, String firstDay, String endDay, String type) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String startDate = firstDay;// 开始时间
		String endDate = endDay;// 结束时间
		if (firstDay == null || firstDay.equals("")) {
			c.add(Calendar.MONTH, 0);
			c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			startDate = format.format(c.getTime());// 当前年月的第一天
			firstDay = startDate;
		} else if (endDay == null || endDay.equals("")) {
			endDate = DateUtil.getNow();
			endDay = endDate;
		}
		String[] days = DateUtil.scopeTime(startDate, endDate);// 获得天数
		StringBuffer sql = new StringBuffer(" select ");
		sql.append(" DATE_FORMAT(a.access_date,'%c-%e') as day_num, ");
		sql.append(" count(*) AS cs,a.statistics_type as type ");
		sql.append(" from bp_statistics_all a ");
		sql.append(" LEFT JOIN bp_device b ON b.router_sn = a.router_sn ");
		sql.append(" LEFT JOIN bp_shop s ON b.shop_id = s.id ");
		sql.append(" WHERE DATE_FORMAT(a.access_date, '%Y-%m-%d') < DATE_FORMAT(now(), '%Y-%m-%d') and b.shop_id=? ");
		List<Record> zslist = Db.find(sql.toString(), new Object[] { shopId });
		Map zsMap = new HashMap();
		for (int i = 0; i < days.length; i++) {
			boolean flag = true;
			for (int f = 0; f < zslist.size(); f++) {
				Record zsRd = zslist.get(f);
				String nva = zsRd.get("day_num");
				if (nva.equals(days[i])) {
					zsMap.put(days[i], zsRd.get("cs"));
					flag = false;
					break;
				}
			}
			if (flag) {
				zsMap.put(days[i], 0);
			}
		}
		StringBuffer advSql = new StringBuffer("select ");
		advSql.append(" DATE_FORMAT(a.access_date,'%c-%e') as day_num, ");
		advSql.append(" count(*) AS cs,a.statistics_type as type ");
		advSql.append(" from bp_statistics_all a ");
		advSql.append(" LEFT JOIN bp_device b ON b.router_sn = a.router_sn ");
		advSql.append(" LEFT JOIN bp_shop s ON b.shop_id = s.id ");
		advSql.append(" WHERE DATE_FORMAT(a.access_date, '%Y-%m-%d') < DATE_FORMAT(now(), '%Y-%m-%d') and a.statistics_type='"+PropertyUtils.getProperty("route.upload.type.adv")+"' and b.shop_id=? ");
		Map advMap = new HashMap();
		List<Record> advList = Db.find(advSql.toString(),new Object[] {shopId });
		for (int i = 0; i < days.length; i++) {
			boolean flag = true;
			for (int f = 0; f < advList.size(); f++) {
				Record advRd = advList.get(f);
				String nva = advRd.get("day_num");
				if (nva.equals(days[i])) {
					advMap.put(days[i], advRd.get("cs"));
					flag = false;
					break;
				}
			}
			if (flag) {
				advMap.put(days[i], 0);
			}
		}
		
		StringBuffer appSql = new StringBuffer("select ");
		appSql.append(" DATE_FORMAT(a.access_date,'%c-%e') as day_num, ");
		appSql.append(" count(*) AS cs,a.statistics_type as type ");
		appSql.append(" from bp_statistics_all a ");
		appSql.append(" LEFT JOIN bp_device b ON b.router_sn = a.router_sn ");
		appSql.append(" LEFT JOIN bp_shop s ON b.shop_id = s.id ");
		appSql.append(" WHERE DATE_FORMAT(a.access_date, '%Y-%m-%d') < DATE_FORMAT(now(), '%Y-%m-%d') and a.statistics_type='"+PropertyUtils.getProperty("route.upload.type.app")+"' and b.shop_id=? ");
		Map appMap = new HashMap();
		List<Record> appList = Db.find(appSql.toString(),new Object[] {shopId});
		for (int i = 0; i < days.length; i++) {
			boolean flag = true;
			for (int f = 0; f < appList.size(); f++) {
				Record appRd = appList.get(f);
				String nva = appRd.get("day_num");
				if (nva.equals(days[i])) {
					appMap.put(days[i], appRd.get("cs"));
					flag = false;
					break;
				}
			}
			if (flag) {
				appMap.put(days[i], 0);
			}
		}
		
		String caption = startDate + "至" + endDate + "访问量统计表";
		StringBuffer chartXmlData = new StringBuffer();
		chartXmlData.append("<chart caption='"+ caption
						+ "'  xAxisName='日期（号）' numbersuffix='次' linethickness='1' formatnumberscale='0' showvalues='0' anchorradius='2' divlinecolor='666666'  divlinealpha='30' divlineisdashed='1' labelstep='2' bgcolor='FFFFFF' showalternatehgridcolor='0' labelpadding='10' canvasborderthickness='1' legendiconscale='1.5' legendshadow='0' legendborderalpha='0' legendposition='right' canvasborderalpha='50' numvdivlines='5' vdivlinealpha='20' showborder='0'>");
		chartXmlData.append("<categories>");
		for (int i = 0; i < days.length; i++) {
			chartXmlData.append("<category label='" + days[i] + "' />");
		}
		chartXmlData.append("</categories>");
		chartXmlData.append("<dataset seriesname='总访问量' color='1D8BD1' anchorbordercolor='1D8BD1' anchorbgcolor='1D8BD1'>");
		for (int i = 0; i < days.length; i++) {
			chartXmlData.append("<set value='" + zsMap.get(days[i]) + "' />");
		}
		chartXmlData.append("</dataset>");
		
		chartXmlData.append("<dataset seriesname='广告访问量' color='F1683C' anchorbordercolor='F1683C' anchorbgcolor='F1683C'>");
		for (int i = 0; i < days.length; i++) {
			chartXmlData.append("<set value='" + advMap.get(days[i]) + "' />");
		}
		chartXmlData.append("</dataset>");
		chartXmlData.append("<dataset seriesname='App访问量' color='2AD62A' anchorbordercolor='2AD62A' anchorbgcolor='2AD62A'>");
		for (int i = 0; i < days.length; i++) {
			chartXmlData.append("<set value='" + appMap.get(days[i]) + "' />");
		}
		chartXmlData.append("</dataset>");
		chartXmlData.append("</chart>");
		return chartXmlData.toString();
	}

	//@formatter:off 
	/**
	 * Title: getPfStatistics
	 * Description:获得客流统计信息
	 * Created On: 2014年8月18日 下午4:49:33
	 * @author JiaYongChao
	 * <p>
	 * @param shopId商铺ID
	 * @param firstDay
	 * @param endDay
	 * @param type
	 * @return 
	 */
	//@formatter:on
	public String getPfStatistics(String shopId, String startDate, String endDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String[] searchDay = checkSearch(startDate,endDate);
		startDate = searchDay[0];
		endDate = searchDay[1];
		StringBuffer chartXmlData = new StringBuffer();
		try {
			Calendar c = Calendar.getInstance();
			c.setTime(format.parse(startDate));
			c.add(Calendar.YEAR, 1);
			if(!c.getTime().after(format.parse(endDate))){//开始时间和结束时间的区间不能超过一年
				c.setTime(format.parse(endDate));
				c.add(Calendar.YEAR, -1);
				startDate = format.format(c.getTime());
			}
			chartXmlData.append("<chart caption='"+ startDate + "至" + endDate + "客流量统计表' xAxisName='日期（号）' numbersuffix='人' "
					+ "decimals='0' divlinecolor='666666'   bgcolor='FFFFFF'  linethickness='1'  showvalues='0'   >");
			Calendar startC = Calendar.getInstance(); 
			startC.setTime(format.parse(startDate));
			Calendar endC = Calendar.getInstance(); 
			endC.setTime(format.parse(endDate));
			List<String> dates = new ArrayList<String>();
			SimpleDateFormat formatLabel = new SimpleDateFormat("M-d");
			while(!startC.after(endC)){
				dates.add(formatLabel.format(startC.getTime()));
				startC.add(Calendar.DAY_OF_MONTH, 1);
			}
			chartXmlData.append(ChartUtil.generCategories(dates));
			
			List<Record> devices = getShopDevice(shopId);
			Map<String,Map<String,Record>> dataList = getDataList(shopId, startDate, endDate);
			if(endDate.equals(format.format(new Date()))){
				dataList.put(formatLabel.format(new Date()), getTodayDataList(shopId));
			}
			
			Iterator<String> iteDate = dates.iterator();
			while(iteDate.hasNext()){
				String dateLabel = iteDate.next();
				for(int i=0;i<devices.size();i++){
					String sn = devices.get(i).getStr("router_sn");
					Object count = (null!=dataList.get(dateLabel))?
							null!=dataList.get(dateLabel).get(sn)?
									dataList.get(dateLabel).get(sn).get("counts"):0:0;
					((StringBuffer)(devices.get(i).get("dataset"))).append("<set value='" + count + "' />");
				}
			}
			Iterator<Record> devicesIte = devices.iterator();
			while(devicesIte.hasNext()){
				Record deviceInfo = devicesIte.next();
				chartXmlData.append(deviceInfo.get("dataset").toString());
				chartXmlData.append("</dataset>");
			}
			chartXmlData.append("</chart>");
		} catch (ParseException e) {
		}
		return chartXmlData.toString();
	}
	private List<Record> getShopDevice(String shopId){
		List<Record> devices = Db.find("SELECT router_sn,name FROM bp_device WHERE shop_id=? ",new Object[]{shopId});
		Iterator<Record> ite = devices.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			String color = ImageKit.randColor();
			StringBuffer buf = new StringBuffer("<dataset seriesname='"+rowData.getStr("name")+"' color='"+color+"' anchorbordercolor='"+color+"' anchorbgcolor='"+color+"'>");
			rowData.set("dataset", buf);
		}
		return devices;
	}
	private Map<String,Map<String,Record>> getDataList(String shopId, String startDate, String endDate){
		Map<String,Map<String,Record>> returnData = new HashMap<String,Map<String,Record>>();
		StringBuffer sql = new StringBuffer("SELECT DATE_FORMAT(sp.date,'%c-%e') AS date,sp.router_sn,sp.counts ");
		sql.append("FROM bp_statistics_pf sp ");
		sql.append("JOIN bp_device d ON (sp.router_sn=d.router_sn) ");
		sql.append("WHERE d.shop_id=? AND sp.date BETWEEN ? AND ? ");
		sql.append("ORDER BY sp.date ");
		List<Record> dataList = Db.find(sql.toString(),new Object[]{shopId,startDate,endDate});
		Iterator<Record> ite = dataList.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			String date = rowData.getStr("date");
			String thisSN = rowData.getStr("router_sn");
			Map<String,Record> dateMap =  returnData.get(date);
			if(null == dateMap){
				dateMap = new HashMap<String,Record>();
				returnData.put(date, dateMap);
			}
			dateMap.put(thisSN, rowData);
		}
		return returnData;
	}
	
	private Map<String,Record> getTodayDataList(String shopId){
		Map<String,Record> returnData = new HashMap<String,Record>();
		StringBuffer sql = new StringBuffer("select r.sn,count(distinct(r.mac)) counts ");
		sql.append("from bp_report r join bp_device d on (d.shop_id=? and r.sn=d.router_sn) ");
		sql.append("where date(r.create_date)=date(now()) group by r.sn ");
		List<Record> dataList = Db.find(sql.toString(),new Object[]{shopId});
		Iterator<Record> ite = dataList.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			String thisSN = rowData.getStr("sn");
			returnData.put(thisSN, rowData);
		}
		return returnData;
	}
	
	//@formatter:off 
	/**
	 * Title: findPvTypeList
	 * Description:查询访问量统计的统计分类
	 * Created On: 2014年8月20日 上午10:07:53
	 * @author JiaYongChao
	 * <p>
	 * @param firstDay
	 * @param endDay
	 * @param shopId
	 * @return 
	 */
	//@formatter:on
	public List<Record> findPvTypeList(String firstDay, String endDay, String shopId) {
//		StringBuffer sql = new StringBuffer("SELECT DISTINCT d.id,d.`name` from bp_shop_page_app a  ");
//		sql.append(" left join  bp_shop_page b on a.page_id = b.id ");
//		sql.append("  left join bp_shop c  on c.id = b.shop_id ");
//		sql.append(" left join bp_app  d  on a.app_id = d.id ");
//		sql.append(" where c.id="+shopId +" order by d.id");
		StringBuffer sql = new StringBuffer();
		sql.append("select DISTINCT a.id,ifnull(sac.name,a.name) name ");
		sql.append("from bp_app a ");
		sql.append("left join bp_shop_page_app spa on (spa.page_id=? and spa.app_id = a.id) ");
		sql.append("left join bp_shop_page sp on (spa.page_id = sp.id) ");
		sql.append("left join bp_temp_app_icon tai on (sp.template_id=tai.template_id and a.id=tai.app_id) ");
		sql.append("left join bp_shop_app_custom sac on (sac.shop_id=? and sp.template_id=sac.template_id and sac.app_id=a.id) ");
		sql.append("where spa.id is not null and a.template_id=? ");
		sql.append("order by a.id");
		List<Record> list  = Db.find(sql.toString(),new Object[]{PageUtil.getPageIdByShopId(shopId),
			shopId,PageUtil.getTemplateId(shopId)});//表头list
		return list;
	}
	
	//@formatter:off 
	/**
	 * Title: findPvList
	 * Description:查询访问量统计
	 * Created On: 2014年8月20日 上午11:29:07
	 * @author JiaYongChao
	 * <p>
	 * @param firstDay
	 * @param endDay
	 * @param shopId
	 * @return 
	 * @throws ParseException 
	 */
	//@formatter:on
	public Map<String,List<Integer>>  findPvList(String firstDay, String endDay, String shopId) {
		Map<String,List<Integer>> tempMap = new LinkedHashMap<String, List<Integer>>();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String nowDay = format.format(new Date());
		String[] searchDay = checkSearch(firstDay,endDay);
		firstDay = searchDay[0];
		endDay = searchDay[1];
		boolean getTodayData = false;
		boolean getHistoryData = false;
		if(!firstDay.equals(nowDay)){
			getHistoryData = true;
		}
		if(endDay.equals(nowDay)){
			getTodayData = true;
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			c.add(Calendar.DAY_OF_MONTH, -1);
			endDay = format.format(c.getTime());
		}
		StringBuffer sql = new StringBuffer();
		if(getHistoryData){
			sql.append("SELECT a.id,sd.search_date AS days,a.name,IFNULL(sa.access_num,0) num ");
			sql.append("FROM bp_shop_page_app spa JOIN bp_shop_page sp ON (sp.shop_id='"+shopId+"' AND spa.page_id=sp.id) ");
			sql.append("JOIN bp_app a ON (spa.app_id = a.id and a.template_id='"+PageUtil.getTemplateId(shopId)+"') ");
			sql.append("JOIN bp_search_day sd ON (sd.search_date>='"+firstDay+"' AND sd.search_date<='"+endDay+"') ");
			sql.append("LEFT JOIN bp_statistics_app sa ON (sa.shop_id='"+shopId+"' AND sa.access_date>='"+firstDay+"' AND sa.access_date<='"+endDay+"' ");
			sql.append("AND sa.app_id=a.id AND sa.access_date=sd.search_date) ");
			sql.append("GROUP BY sd.search_date,a.id ");
			if(getTodayData){
				sql.append("UNION ");
			}
		}
		if(getTodayData){
			sql.append("SELECT a.id,'"+nowDay+"' AS days,a.name,COUNT(sa.id) num ");
			sql.append("FROM bp_shop_page_app spa ");
			sql.append("JOIN bp_shop_page sp ON (sp.shop_id='"+shopId+"' AND spa.page_id=sp.id) ");
			sql.append("JOIN bp_device d on (sp.shop_id=d.shop_id) ");
			sql.append("JOIN bp_app a ON (spa.app_id = a.id and a.template_id='"+PageUtil.getTemplateId(shopId)+"') ");
			sql.append("LEFT JOIN bp_statistics_all sa ON (sa.statistics_type='app' AND DATE(sa.access_date)='"+nowDay+"' AND sa.statistics_id=a.id AND d.router_sn=sa.router_sn) ");
			sql.append("GROUP BY a.id ");
			if(getHistoryData){
				sql.append("ORDER BY days DESC,id ");
			}
		}else{
			sql.append("ORDER BY sd.search_date DESC,a.id ");
		}
		List<Record> list = Db.find(sql.toString());
		List<Integer> numList = new ArrayList<Integer>();
		String currentDay = "";
		Iterator<Record> ite = list.iterator();
		while(ite.hasNext()){
			Record rowData = ite.next();
			String thisDay = rowData.get("days").toString();
			if(!currentDay.equals(thisDay)){
				numList = new ArrayList<Integer>();
				tempMap.put(thisDay, numList);
				currentDay = thisDay;
			}
			numList.add(Integer.parseInt(rowData.get("num").toString()));
		}
		return tempMap;
	}

	//@formatter:off 
	/**
	 * Title: timerStatisticsPf
	 * Description:定时客流统计接口
	 * Created On: 2014年8月28日 下午2:11:59
	 * @author JiaYongChao
	 * <p>
	 * @param 
	 */
	//@formatter:on
	public void timerStatisticsPf() {
		logger.warn(">>>>>>>>>>>>>>>>开始插入客流统计数据");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar yesterdayCalendar = Calendar.getInstance();
		yesterdayCalendar.setTime(new Date());
		yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -1);
		Calendar startCalendar = Calendar.getInstance();
		startCalendar.setTime(getMaxDate());
		startCalendar.add(Calendar.DAY_OF_MONTH, 1);
		while(!startCalendar.after(yesterdayCalendar)){
			final String insertDate = sdf.format(startCalendar.getTime());
			startCalendar.add(Calendar.DAY_OF_MONTH, 1);
			List<String> list = new ArrayList<String>();
			StringBuffer sql = new StringBuffer(" INSERT INTO bp_statistics_pf (DATE,router_sn,counts) SELECT * FROM ( ");
			sql.append(" SELECT  STR_TO_DATE(IFNULL(aa.days,'"+insertDate+"'),'%Y-%m-%d') AS DATE,ds.router_sn,IFNULL(aa.counts,0) AS counts  ");
			sql.append(" FROM bp_device AS   ds LEFT JOIN ( ");
			sql.append(" SELECT dd.*,COUNT(*) AS counts FROM ( ");
			sql.append(" SELECT DISTINCT DATE_FORMAT(s.access_date,'%Y-%m-%d') AS days,s.router_sn,s.`client_mac` ");
			sql.append(" FROM bp_statistics_all AS s ");
			sql.append(" WHERE  DATE_FORMAT(access_date,'%Y-%m-%d')='"+insertDate+"' AND s.`client_mac` !='') dd  ");
			sql.append(" GROUP BY dd.router_sn,dd.days) AS aa ON aa.router_sn = ds.router_sn) AS cd ");
			list.add(sql.toString());
			Db.batch(list, list.size());
		}
		logger.warn(">>>>>>>>>>>>>>>>结束插入客流统计数据");
	}
	private Date getMaxDate(){
		Record rec = Db.findFirst("select max(date) max_date from bp_statistics_pf");
		if(null != rec && null != rec.get("max_date") && StringUtils.isNotBlank(rec.get("max_date").toString())){
			return rec.getDate("max_date");
		}
		return new Date();
	}
	//@formatter:off 
	/**
	 * Title: insertData
	 * Description:pf表初始化
	 * Created On: 2014年8月29日 上午11:12:02
	 * @author JiaYongChao
	 * <p>
	 * @param firstDay
	 * @param endDay 
	 */
	//@formatter:on
	public void insertData(String firstDay, String endDay){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String startDate = "2014-01-31";// 开始时间
		String endDate = "2014-10-01";// 结束时间 
		if (firstDay == null || firstDay.equals("")) {
			c.add(Calendar.MONTH, 0);
			c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			startDate = format.format(c.getTime());// 当前年月的第一天
			firstDay = startDate;
		}
		if (endDay == null || endDay.equals("")) {
			endDate = DateUtil.getSpecifiedDayBefore(DateUtil.getNow());
			endDay = endDate;
		}
		String[] days = DateUtil.scopeTimes(startDate, endDate);// 获得天数
		for(int i=0;i<days.length;i++){
			List<String> list = new ArrayList<String>();
			StringBuffer sql = new StringBuffer(" INSERT INTO bp_statistics_pf (DATE,router_sn,counts) SELECT * FROM ( ");
			sql.append(" SELECT  STR_TO_DATE(IFNULL(aa.days,'"+days[i]+"'),'%Y-%m-%d') AS DATE,ds.router_sn,IFNULL(aa.counts,0) AS counts  ");
			sql.append(" FROM bp_device AS   ds LEFT JOIN ( ");
			sql.append(" SELECT dd.*,COUNT(*) AS counts FROM ( ");
			sql.append(" SELECT DISTINCT DATE_FORMAT(s.access_date,'%Y-%m-%d') AS days,s.router_sn,s.`client_mac` ");
			sql.append(" FROM bp_statistics_all AS s ");
			sql.append(" WHERE  DATE_FORMAT(access_date,'%Y-%m-%d')='"+days[i]+"' AND s.`client_mac` !='') dd  ");
			sql.append(" GROUP BY dd.router_sn,dd.days) AS aa ON aa.router_sn = ds.router_sn) AS cd ");
			list.add(sql.toString());
			Db.batch(list, list.size());
		}
	}

	//@formatter:off 
	/**
	 * Title: certifiedList
	 * Description:认证信息统计
	 * Created On: 2014年9月13日 下午3:18:44
	 * @author JiaYongChao
	 * <p>
	 * @param shopId
	 * @param firstDay
	 * @param endDay
	 * @return 
	 */
	//@formatter:on
	public Page<Record> certifiedList(String shopId, String firstDay, String endDay,int pageNum,int pageSize) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String startDate = firstDay;// 开始时间
		String endDate = endDay;// 结束时间
		if (firstDay == null || firstDay.equals("")) {
			c.add(Calendar.MONTH, 0);
			c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			startDate = format.format(c.getTime());// 当前年月的第一天
			firstDay = startDate;
		} else if (endDay == null || endDay.equals("")) {
			endDate = DateUtil.getNow();
			endDay = endDate;
		}
		StringBuffer sqlFrom = new StringBuffer();
		sqlFrom.append(" FROM  bp_auth s  LEFT JOIN bp_device d ON s.`router_sn` = d.`router_sn` ");
		sqlFrom.append(" LEFT JOIN bp_shop h  ON h.`id` = d.`shop_id` ");
		sqlFrom.append(" WHERE s.`auth_type`='phone' AND S.`client_mac` !='' AND S.`client_mac` IS NOT NULL  ");
		if(shopId!=null){
			sqlFrom.append(" and h.id ="+shopId);
		}
		if(StringUtils.isNotEmpty(startDate) && !startDate.equals("") && StringUtils.isNotEmpty(endDate) && !endDate.equals("")){
			sqlFrom.append(" AND s.`auth_date` >= '"+startDate+"' AND s.`auth_date` <=  '"+endDate+"'" );
		}
		sqlFrom.append(" order by s.auth_date desc");
		StringBuffer sqlSelect = new StringBuffer();
		sqlSelect.append("select IF(s.`auth_type`='phone','手机','其他') AS auth_type,");
		sqlSelect.append("concat(left(S.`tag`,6),'*****') AS info,");
		sqlSelect.append("concat(left(s.`client_mac`,3),'****',substring(s.`client_mac`,15)) AS mac,");
		sqlSelect.append("s.address,s.cardtype,s.`auth_date`,d.`name`,IFNULL(s.`address`,'未知') AS address,");
		sqlSelect.append("IFNULL(s.`cardtype`,'未知') AS cardtype,IFNULL(s.`ftutype`,'未知')  AS ftutype ");
		Page<Record> returnData = Db.paginate(pageNum, pageSize, sqlSelect.toString(), 
				sqlFrom.toString());
//		List<Record> list = Db.find(sql.toString());
		return returnData;
	}
	/**
	 * 统计查询时使用，如果startDay不符合要求则，设置该值为当月的第一天,如果endDay不符合要求则，设置该值为昨天的日期
	 * @param startDay
	 * @return
	 */
	private static String[] checkSearch(String startDay,String endDay){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		Date startDate = null;
		try{
			startDate = format.parse(startDay);
			if(startDate.after(new Date())){
				startDate = new Date();
			}
		}catch(Exception e){//格式错误处理
			c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			startDate = c.getTime();
		}
		Date endDate = null;
		try{
			endDate = format.parse(endDay);
			if(endDate.after(new Date())){
				endDate = new Date();
			}
		}catch(Exception e){//格式错误处理
			c.setTime(new Date());
			c.add(Calendar.DAY_OF_MONTH, -1);
			endDate = c.getTime();
		}
		if(startDate.after(endDate)){
			Date temp = startDate;
			startDate = endDate;
			endDate = temp;
		}
		startDay = format.format(startDate);
		endDay = format.format(endDate);
		return new String[]{startDay,endDay};
	}
	
	public Page<Record> logList(String shopId, String firstDay, String endDay,int pageNum,int pageSize) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		String startDate = firstDay;// 开始时间
		String endDate = endDay;// 结束时间
		if (firstDay == null || firstDay.equals("")) {
			c.add(Calendar.MONTH, 0);
			c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
			startDate = format.format(c.getTime());// 当前年月的第一天
			firstDay = startDate;
		} else if (endDay == null || endDay.equals("")) {
			endDate = DateUtil.getNow();
			endDay = endDate;
		}
		StringBuffer sqlFrom = new StringBuffer();
		sqlFrom.append(" from bpbaselogtbl a join bp_device b on a.device_no=b.router_sn ");
		sqlFrom.append(" join bp_shop c on b.shop_id=c.id ");
		sqlFrom.append(" join bp_auth d ON a.input_mac=d.client_mac and d.auth_type='phone' and a.device_no=d.router_sn ");
		sqlFrom.append("  where 1=1 ");
		if(shopId!=null){
			sqlFrom.append(" and c.id ="+shopId);
		}
		if(StringUtils.isNotEmpty(startDate) && !startDate.equals("") && StringUtils.isNotEmpty(endDate) && !endDate.equals("")){
			sqlFrom.append(" and a.create_date >= '"+startDate+"' and a.create_date <=  '"+endDate+"'" );
		}
		sqlFrom.append(" group by a.link order by a.create_date desc");
		Page<Record> returnData = Db.paginate(pageNum, pageSize, "select b.name,a.input_mac,a.link,a.create_date,ifnull(d.tag,'') tag ", 
				sqlFrom.toString());
		return returnData;
	}
	
	public static void main(String[] args) {
		String[] days = checkSearch("2014-09-22","2014-09-21");
		System.err.println(days[0]+"-------"+days[1]);
	}
}
