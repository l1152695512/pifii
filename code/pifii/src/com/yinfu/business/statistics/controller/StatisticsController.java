
package com.yinfu.business.statistics.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.yinfu.business.application.model.SmsFlow;
import com.yinfu.business.statistics.model.Statistics;
import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.DateUtil;

@ControllerBind(controllerKey = "/business/statistics", viewPath = "")
public class StatisticsController extends Controller<Statistics> {
	
	//@formatter:off 
	/**
	 * Title: toIndex
	 * Description:跳转广告统计首页
	 * Created On: 2014年8月12日 上午10:29:14
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void toIndex() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 获取当前月第一天：
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String firstDay = format.format(c.getTime());// 当前年月的第一天
		// 获取当前日期
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.DAY_OF_MONTH, -1);
		String nowDay = format.format(c1.getTime());
		setAttr("firstDay", firstDay);
		setAttr("nowDay", nowDay);
		render("/page/business/statistic/countAD.jsp");
	}
	
	public void getOnLineTotal() {
		String shopId = getPara("shopId");// 获得商铺Id
		Statistics statistics = new Statistics();
		JSONObject data = new JSONObject();
		data.put("onlineNum", statistics.getOnLineTotal(shopId));
		data.put("totalNum", statistics.getDeviceAccessNum(shopId));
		renderJson(data);
	}
	
	//@formatter:off 
	/**
	 * Title: getClientTotal
	 * Description:首页客流统计
	 * Created On: 2014年8月19日 下午1:52:43
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void getClientTotal() {
		String shopId = getPara("shopId");// 获得商铺Id
		Statistics statistics = new Statistics();
		renderJson(statistics.getClientTotal(shopId));
	}
	
	public void getAdvClick() {
		String shopId = getPara("shopId");// 获得商铺Id
		Statistics statistics = new Statistics();
		renderText(String.valueOf(statistics.getAdvClick(shopId)));
	}
	
	public void getAdvShow() {
		String shopId = getPara("shopId");// 获得商铺Id
		Statistics statistics = new Statistics();
		renderJson(statistics.getAdvShow(shopId));
	}
	
	public void getSmsTotal() {
		String shopId = getPara("shopId");// 获得商铺Id
		Statistics statistics = new Statistics();
		renderText(String.valueOf(statistics.getSmsTotal(shopId)));
	}
	
	//@formatter:off 
	/**
	 * Title: getAdCharts
	 * Description:广告统计
	 * Created On: 2014年8月12日 上午10:18:27
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void getAdCharts() {
		String shopId = getPara("shopId");// 获得商铺Id
		String firstDay = getPara("firstDay");// 获得选择的开始日期
		String endDay = getPara("endDay");// 获得选择的结束日期
		Statistics statistics = new Statistics();
		renderText(statistics.getAdCharts(shopId, firstDay, endDay));
	}
	
	//@formatter:off 
	/**
	 * Title: getPvStatistics
	 * Description:访问量统计
	 * Created On: 2014年8月12日 下午5:06:08
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void getPvStatistics() {
		String shopId = getPara("shopId");// 获得商铺Id
		String firstDay = getPara("firstDay");// 获得选择的开始日期
		String endDay = getPara("endDay");// 获得选择的结束日期
		String type = getPara("type");// 显示类别
		Statistics statistics = new Statistics();
		renderText(statistics.getPvStatistics(shopId, firstDay, endDay, type));
	}
	
	//@formatter:off 
	/**
	 * Title: pvStatisticsIndex
	 * Description:访问量统计首页
	 * Created On: 2014年8月12日 下午4:33:41
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void pvStatisticsIndex() {
		String shopId = getPara("shopId");// 获得商铺Id
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 获取当前月第一天：
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String firstDay = format.format(c.getTime());// 当前年月的第一天
		// 获取当前日期
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.DAY_OF_MONTH, -1);
		String nowDay = format.format(c1.getTime());
		setAttr("firstDay", firstDay);
		setAttr("nowDay", nowDay);
		
		List<Record> typeList = Statistics.dao.findPvTypeList(firstDay, nowDay, shopId);
		setAttr("typeList", typeList);
		render("/page/business/statistic/pv/countPVIndex.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: pvStatisticsList
	 * Description:访问量统计列表
	 * Created On: 2014年8月20日 上午10:06:19
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void pvStatisticsList() {
		String shopId = getPara("shopId");// 获得商铺Id
		String firstDay = getPara("startDate");// 获得选择的开始日期
		String endDay = getPara("endDate");// 获得选择的结束日期
		Map<String, List<Integer>> map = Statistics.dao.findPvList(firstDay, endDay, shopId);
		renderJson(map);
	}
	
//@formatter:off 
	/**
	 * Title: pfStatisticsIndex
	 * Description:客流统计首页
	 * Created On: 2014年8月18日 下午4:34:36
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void pfStatisticsIndex() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		// 获取当前月第一天：
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 0);
		c.set(Calendar.DAY_OF_MONTH, 1);// 设置为1号,当前日期既为本月第一天
		String firstDay = format.format(c.getTime());// 当前年月的第一天
		if(firstDay.equals(DateUtil.getNow())){
			Calendar cale = Calendar.getInstance();   
	        cale.set(Calendar.DAY_OF_MONTH,0);//设置为1号,当前日期既为本月第一天 
	        String lastDay = format.format(cale.getTime());
	        firstDay=lastDay;
		}
		// 获取当前日期
		Calendar c1 = Calendar.getInstance();
		c1.add(Calendar.DAY_OF_MONTH, -1);
		String nowDay = format.format(c1.getTime());
		setAttr("firstDay", firstDay);
		setAttr("nowDay", nowDay);
		render("/page/business/statistic/countPF.jsp");
	}
	
	//@formatter:off 
	/**
	 * Title: getPfStatistics
	 * Description:获得客流统计信息
	 * Created On: 2014年8月18日 下午4:48:20
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void getPfStatistics() {
		String shopId = getPara("shopId");// 获得商铺Id
		String firstDay = getPara("firstDay");// 获得选择的开始日期
		String endDay = getPara("endDay");// 获得选择的结束日期
		Statistics statistics = new Statistics();
		/*statistics.insertData(firstDay,endDay);*/
		renderText(statistics.getPfStatistics(shopId, firstDay, endDay));
	}
	//@formatter:off 
	/**
	 * Title: certifiedStatisticsIndex
	 * Description:认证信息统计首页
	 * Created On: 2014年9月13日 下午3:08:13
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void certifiedIndex(){
		render("/page/business/statistic/certified.jsp");
	}
	//@formatter:off 
	/**
	 * Title: getCertifiedInfo
	 * Description:认证信息统计
	 * Created On: 2014年9月13日 下午3:09:58
	 * @author JiaYongChao
	 * <p> 
	 */
	//@formatter:on
	public void getCertifiedInfo(){
		String shopId = getPara("shopId");// 获得商铺Id
		String firstDay = getPara("startDate");// 获得选择的开始日期
		String endDay = getPara("endDate");// 获得选择的结束日期
		int pageNum = 1;
		int pageSize = 10;
		try{
			pageNum = getParaToInt("pageNum");
			pageSize = getParaToInt("pageSize");
		}catch(Exception e){
		}
		Page<Record> list = Statistics.dao.certifiedList(shopId,firstDay,endDay,pageNum,pageSize);
		renderJson(list);
	}
	
	public void logIndex(){
		render("/page/business/statistic/logIndex.jsp");
	}
	
	public void getLogInfo(){
		String shopId = getPara("shopId");// 获得商铺Id
		String firstDay = getPara("startDate");// 获得选择的开始日期
		String endDay = getPara("endDate");// 获得选择的结束日期
		int pageNum = 1;
		int pageSize = 10;
		try{
			pageNum = getParaToInt("pageNum");
			pageSize = getParaToInt("pageSize");
		}catch(Exception e){
		}
		Page<Record> list = Statistics.dao.logList(shopId,firstDay,endDay,pageNum,pageSize);
		renderJson(list);
	}
	
	/**
	 * 跳转短信统计页面
	 */
	public void smsStatistics(){
		render("/page/business/statistic/smsFlowStatic.jsp");
	}
	
	/**
	 * 提取短信统计信息 并将查询结果转发至页面
	 */
	public void getSmsStatisInfo(){
		String shopId = getPara("shopId");// 获得商铺Id
		String startDate = getPara("firstDay");// 获得选择的开始日期
		String endDate = getPara("endDay");// 获得选择的结束日期
		String monthnums = getPara("monthnums");// 获得选择的结束日期
		renderText(SmsFlow.dao.getSmsStatisInfoXml(startDate, endDate,monthnums));
		
	}
}
