package com.yf.base.chart;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.yf.util.DBUtil;



@SuppressWarnings("unchecked")
public class PreSalesCharts {
	
	HttpServletRequest request = ServletActionContext.getRequest ();
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
	/**
	 * 售前按分公司统计 柱状图
	 * @return
	 */
	public String groupBySubFirmColumn() {
		FusionChartsCreator fcc = new FusionChartsCreator();
		DBUtil dbUtil = new DBUtil();
		
		Set<String> dsitrictSet = new HashSet<String>();
		dsitrictSet.add("省级");
		dsitrictSet.add("市属");
		dsitrictSet.add("禅城");
		dsitrictSet.add("南海");
		dsitrictSet.add("顺德");
		dsitrictSet.add("三水");
		dsitrictSet.add("高明");

		String currentMonthSql = "select count(*) as num,lauchedFirm from LauchRequirements where DATEPART(month, lauchTime)=DATEPART(month, getdate())";
		String currentYearSql = "select count(*) as num,lauchedFirm from LauchRequirements where DATEPART(year, lauchTime)=DATEPART(year, getdate())";
		String groupBy = " group by lauchedFirm";
		
		if(startDate!=null && endDate!=null && startDate!="" && endDate!= "")
		{
			currentYearSql = "select count(*) as num,lauchedFirm from LauchRequirements where lauchTime>='"+ startDate +"' and lauchTime<='"+ endDate +"'";
		}
		
		// Initialize <chart> element
		String strXML = "<chart caption='售前支撑(按区域分)' bgColor='E0F6FB,B2E9F6'  borderColor='B2F0FF' formatNumberScale='0' placeValuesInside='1' decimals='0'  baseFont='宋体' baseFontSize='12'>";
		// Initialize <categories> element - necessary to generate a
		// multi-series chart
		String strCategories = "<categories>";

		// Initiate <dataset> elements
		String strDataCurr = "<dataset seriesName='本月'>";
		String strDataPrev = "<dataset seriesName='累计'>";

		Iterator it = dsitrictSet.iterator();
		
		// Iterate through the data
		while (it.hasNext()) {
			String district = (String)it.next();
			
			// Append <category name='...' /> to strCategories
			strCategories = strCategories + "<category name='" + district + "' />";
			
			// Add <set value='...' /> to both the datasets
			Object[] monthList = dbUtil.statisticsBySql(currentMonthSql + " and lauchedFirm like '" + district + "%'" + groupBy);
			int value = Integer.parseInt(monthList==null?"0":monthList[0].toString());
			strDataCurr = strDataCurr + "<set value='" + value + "' />";
			Object[] yearList = dbUtil.statisticsBySql(currentYearSql + " and lauchedFirm like '" + district + "%'" + groupBy);
			value = Integer.parseInt(yearList==null?"0":yearList[0].toString());
			Object[] aggregate = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='售前支撑' and groupBy='分公司' and district='" + district +"' and DATEPART(year, getdate())<='2010'");
			if(startDate!=null && endDate!=null && startDate!="" && endDate!= ""&& startDate.compareTo("2010-09-01")>0)
			{
				aggregate = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='售前支撑' and groupBy='分公司' and district='" + district +"' and '"+ startDate +"'<='2010-09-01'");
			}
			value += Integer.parseInt(aggregate==null?"0":aggregate[0].toString());
			strDataPrev = strDataPrev + "<set value='" + value + "' />";
		}
		
		// Close <categories> element
		strCategories = strCategories + "</categories>";

		// Close <dataset> elements
		strDataCurr = strDataCurr + "</dataset>";
		strDataPrev = strDataPrev + "</dataset>";

		// Assemble the entire XML now
		strXML = strXML + strCategories + strDataCurr + strDataPrev
				+ "</chart>";

		// Create the chart - MS Column 3D Chart with data contained in strXML
		return fcc.createChart("../../Charts/MSColumn3D.swf", "", strXML,
				"groupBySubFirmColumn", 500, 300, false, false);

	}
	
	/**
	 * 售前按分公司统计 饼图
	 * @return
	 */
	public String groupBySubFirmPie() {
		FusionChartsCreator fcc = new FusionChartsCreator();
		DBUtil dbUtil = new DBUtil();
		
		Set<String> dsitrictSet = new HashSet<String>();
		dsitrictSet.add("省级");
		dsitrictSet.add("市属");
		dsitrictSet.add("禅城");
		dsitrictSet.add("南海");
		dsitrictSet.add("顺德");
		dsitrictSet.add("三水");
		dsitrictSet.add("高明");

		String currentMonthSql = "select count(*) as num,lauchedFirm from LauchRequirements where DATEPART(month, lauchTime)=DATEPART(month, getdate())";
		String groupBy = " group by lauchedFirm";
		
		if(startDate!=null && endDate!=null && startDate!="" && endDate!= "")
		{
			currentMonthSql = "select count(*) as num,lauchedFirm from LauchRequirements where lauchTime>='"+ startDate +"' and lauchTime<='"+ endDate +"'";
		}
		
		// Initialize <chart> element
		String strXML = "<chart  baseFont='宋体' baseFontSize='12' bgColor='E0F6FB,B2E9F6' borderColor='B2F0FF' caption='售前支撑(按区域分)'  formatNumberScale='0'  palette='4' decimals='0' enableSmartLabels='1' bgColor='99CCFF,FFFFFF' bgAlpha='40,100' bgRatio='0,100' bgAngle='360' showBorder='1' startingAngle='70' >";

		Iterator it = dsitrictSet.iterator();
		
		// Iterate through the data
		while (it.hasNext()) {
			String district = (String)it.next();
			Object[] monthList = dbUtil.statisticsBySql(currentMonthSql + " and lauchedFirm like '" + district + "%'" + groupBy);
			int value = Integer.parseInt(monthList==null?"0":monthList[0].toString());
			Object[] aggregate = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='售前支撑' and groupBy='分公司' and district='" + district +"' and DATEPART(year, getdate())<='2010'");
			if(startDate!=null && endDate!=null && startDate!="" && endDate!= ""&& startDate.compareTo("2010-09-01")>0)
			{
				aggregate = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='售前支撑' and groupBy='分公司' and district='" + district +"' and '"+ startDate +"'<='2010-09-01'");
			}
			value += Integer.parseInt(aggregate==null?"0":aggregate[0].toString());
			// Add <set value='...' /> to both the datasets
			strXML += "<set label='" + district + "' value='" +value+ "'/>";
		}
		// Assemble the entire XML now
		strXML = strXML + "</chart>";

		// Create the chart - MS Column 3D Chart with data contained in strXML
		return fcc.createChart("../../Charts/Pie3D.swf", "", strXML,
				"groupBySubFirmPie", 500, 300, false, false);
	}
	
	/**
	 * 售前按业务属性统计 柱状图
	 * @return
	 */
	public String groupByBizPropertyColumn() {
		FusionChartsCreator fcc = new FusionChartsCreator();
		DBUtil dbUtil = new DBUtil();
		
		Set<String> dsitrictSet = new HashSet<String>();
		dsitrictSet.add("本地区内");
		dsitrictSet.add("本地区间");
		dsitrictSet.add("跨市跨省");

		String currentMonthSql = "select count(*) as num,bizProperty from LauchRequirements where DATEPART(month, lauchTime)=DATEPART(month, getdate())";
		String currentYearSql = "select count(*) as num,bizProperty from LauchRequirements where DATEPART(year, lauchTime)=DATEPART(year, getdate())";
		
		if(startDate!=null && endDate!=null && startDate!="" && endDate!= "")
		{
			currentYearSql = "select count(*) as num,bizProperty from LauchRequirements where lauchTime>='"+ startDate +"' and lauchTime<='"+ endDate +"'";
		}
		
		String groupBy = " group by bizProperty";
		
		// Initialize <chart> element
		String strXML = "<chart caption='售前支撑(按业务属性分)' bgColor='E0F6FB,B2E9F6' borderColor='B2F0FF' formatNumberScale='0' placeValuesInside='1' decimals='0'  baseFont='宋体' baseFontSize='12'>";
		// Initialize <categories> element - necessary to generate a
		// multi-series chart
		String strCategories = "<categories>";

		// Initiate <dataset> elements
		String strDataCurr = "<dataset seriesName='本月'>";
		String strDataPrev = "<dataset seriesName='累计'>";

		Iterator it = dsitrictSet.iterator();
		
		// Iterate through the data
		while (it.hasNext()) {
			String district = (String)it.next();
			
			// Append <category name='...' /> to strCategories
			strCategories = strCategories + "<category name='" + district + "' />";
			
			// Add <set value='...' /> to both the datasets
			Object[] monthList = dbUtil.statisticsBySql(currentMonthSql + " and bizProperty = '" + district + "'" + groupBy);
			int value = Integer.parseInt(monthList==null?"0":monthList[0].toString());
			strDataCurr = strDataCurr + "<set value='" + value + "' />";
			Object[] yearList = dbUtil.statisticsBySql(currentYearSql + " and bizProperty = '" + district + "'" + groupBy);
			value = Integer.parseInt(yearList==null?"0":yearList[0].toString());
			Object[] aggregate = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='售前支撑' and groupBy='业务属性' and district='" + district +"' and DATEPART(year, getdate())<='2010'");
			if(startDate!=null && endDate!=null && startDate!="" && endDate!= ""&& startDate.compareTo("2010-09-01")>0)
			{
				aggregate = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='售前支撑' and groupBy='业务属性' and district='" + district +"' and '"+ startDate +"'<='2010-09-01'");
			}
			value += Integer.parseInt(aggregate==null?"0":aggregate[0].toString());
			strDataPrev = strDataPrev + "<set value='" + value + "' />";
		}
		
		// Close <categories> element
		strCategories = strCategories + "</categories>";

		// Close <dataset> elements
		strDataCurr = strDataCurr + "</dataset>";
		strDataPrev = strDataPrev + "</dataset>";

		// Assemble the entire XML now
		strXML = strXML + strCategories + strDataCurr + strDataPrev
				+ "</chart>";

		// Create the chart - MS Column 3D Chart with data contained in strXML
		return fcc.createChart("../../Charts/MSColumn3D.swf", "", strXML,
				"groupByBizPropertyColumn", 500, 300, false, false);

	}
	
	/**
	 * 售前按业务属性统计 饼图
	 * @return
	 */
	public String groupByBizPropertyPie() {
		FusionChartsCreator fcc = new FusionChartsCreator();
		DBUtil dbUtil = new DBUtil();
		
		Set<String> dsitrictSet = new HashSet<String>();
		dsitrictSet.add("本地区内");
		dsitrictSet.add("本地区间");
		dsitrictSet.add("跨市跨省");

		String currentMonthSql = "select count(*) as num,bizProperty from LauchRequirements where DATEPART(month, lauchTime)=DATEPART(month, getdate())";
		String groupBy = " group by bizProperty";
		
		if(startDate!=null && endDate!=null && startDate!="" && endDate!= "")
		{
			currentMonthSql = "select count(*) as num,bizProperty from LauchRequirements where lauchTime>='"+ startDate +"' and lauchTime<='"+ endDate +"'";
		}
		
		// Initialize <chart> element
		String strXML = "<chart  baseFont='宋体' bgColor='E0F6FB,B2E9F6' borderColor='B2F0FF' baseFontSize='12' caption='售前支撑(按业务属性分)' formatNumberScale='0'  palette='4' decimals='0' enableSmartLabels='1' enableRotation='1' bgColor='99CCFF,FFFFFF' bgAlpha='40,100' bgRatio='0,100' bgAngle='360' showBorder='1' startingAngle='70' >";

		Iterator it = dsitrictSet.iterator();
		
		// Iterate through the data
		while (it.hasNext()) {
			String district = (String)it.next();
			Object[] monthList = dbUtil.statisticsBySql(currentMonthSql + " and bizProperty = '" + district + "'" + groupBy);
			int value = Integer.parseInt(monthList==null?"0":monthList[0].toString());
			Object[] aggregate = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='售前支撑' and groupBy='业务属性' and district='" + district +"' and DATEPART(year, getdate())<='2010'");
			if(startDate!=null && endDate!=null && startDate!="" && endDate!= ""&& startDate.compareTo("2010-09-01")>0)
			{
				aggregate = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='售前支撑' and groupBy='业务属性' and district='" + district +"' and '"+ startDate +"'<='2010-09-01'");
			}
			value += Integer.parseInt(aggregate==null?"0":aggregate[0].toString());
			// Add <set value='...' /> to both the datasets
			strXML += "<set label='" + district + "' value='" +value+ "'/>";
		}
		// Assemble the entire XML now
		strXML = strXML + "</chart>";

		// Create the chart - MS Column 3D Chart with data contained in strXML
		return fcc.createChart("../../Charts/Pie3D.swf", "", strXML,
				"groupByBizPropertyPie", 500, 300, false, false);
	}
	
	/**
	 * 售前按业务需求类型统计 柱状图
	 * @return
	 */
	public String groupByRequireTypeColumn() {
		FusionChartsCreator fcc = new FusionChartsCreator();
		DBUtil dbUtil = new DBUtil();
		
		Set<String> dsitrictSet = new HashSet<String>();
		dsitrictSet.add("互联网数据专线");
		dsitrictSet.add("传输租赁");
		dsitrictSet.add("IP VPN");
		dsitrictSet.add("语音专线（含综V）");
		dsitrictSet.add("短信专线");
		dsitrictSet.add("GPRS行业应用");

		String currentMonthSql = "select count(*) as num from LauchRequirements where DATEPART(month, lauchTime)=DATEPART(month, getdate())";
		String currentYearSql = "select count(*) as num from LauchRequirements where DATEPART(year, lauchTime)=DATEPART(year, getdate())";
		
		if(startDate!=null && endDate!=null && startDate!="" && endDate!= "")
		{
			currentYearSql = "select count(*) as num from LauchRequirements where lauchTime>='"+ startDate +"' and lauchTime<='"+ endDate +"'";
		}
		
		// Initialize <chart> element
		String strXML = "<chart caption='售前支撑(按产品分)' bgColor='E0F6FB,B2E9F6' borderColor='B2F0FF' formatNumberScale='0' placeValuesInside='1' decimals='0'  baseFont='宋体' baseFontSize='12'>";
		// Initialize <categories> element - necessary to generate a
		// multi-series chart
		String strCategories = "<categories>";

		// Initiate <dataset> elements
		String strDataCurr = "<dataset seriesName='本月'>";
		String strDataPrev = "<dataset seriesName='累计'>";

		Iterator it = dsitrictSet.iterator();
		
		// Iterate through the data
		while (it.hasNext()) {
			String district = (String)it.next();
			
			// Append <category name='...' /> to strCategories
			strCategories = strCategories + "<category name='" + district + "' />";
			
			// Add <set value='...' /> to both the datasets
			Object[] monthList = dbUtil.statisticsBySql(currentMonthSql + " and (requireType1='"+district+"' or requireType2='"+district+"' or requireType3='"+district+"' or requireType4='"+district+"' or requireType5='"+district+"' or requireType6='"+district+"') ");
			int value = Integer.parseInt(monthList==null?"0":monthList[0].toString());
			strDataCurr = strDataCurr + "<set value='" + value + "' />";
			Object[] yearList = dbUtil.statisticsBySql(currentYearSql + " and (requireType1='"+district+"' or requireType2='"+district+"' or requireType3='"+district+"' or requireType4='"+district+"' or requireType5='"+district+"' or requireType6='"+district+"') ");
			value = Integer.parseInt(yearList==null?"0":yearList[0].toString());
			Object[] aggregate = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='售前支撑' and groupBy='业务需求类型' and district='" + district +"' and DATEPART(year, getdate())<='2010'");
			if(startDate!=null && endDate!=null && startDate!="" && endDate!= ""&& startDate.compareTo("2010-09-01")>0)
			{
				aggregate = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='售前支撑' and groupBy='业务需求类型' and district='" + district +"' and '"+ startDate +"'<='2010-09-01'");
			}
			value += Integer.parseInt(aggregate==null?"0":aggregate[0].toString());
			strDataPrev = strDataPrev + "<set value='" + value + "' />";
		}
		
		// Close <categories> element
		strCategories = strCategories + "</categories>";

		// Close <dataset> elements
		strDataCurr = strDataCurr + "</dataset>";
		strDataPrev = strDataPrev + "</dataset>";

		// Assemble the entire XML now
		strXML = strXML + strCategories + strDataCurr + strDataPrev
				+ "</chart>";

		// Create the chart - MS Column 3D Chart with data contained in strXML
		return fcc.createChart("../../Charts/MSColumn3D.swf", "", strXML,
				"groupByRequireTypeColumn", 500, 300, false, false);

	}
	
	/**
	 * 售前按业务需求类型统计 饼图
	 * @return
	 */
	public String groupByRequireTypePie() {
		FusionChartsCreator fcc = new FusionChartsCreator();
		DBUtil dbUtil = new DBUtil();
		
		Set<String> dsitrictSet = new HashSet<String>();
		dsitrictSet.add("互联网数据专线");
		dsitrictSet.add("传输租赁");
		dsitrictSet.add("IP VPN");
		dsitrictSet.add("语音专线（含综V）");
		dsitrictSet.add("短信专线");
		dsitrictSet.add("GPRS行业应用");
		
		String currentMonthSql = "select count(*) as num from LauchRequirements where DATEPART(month, lauchTime)=DATEPART(month, getdate())";
		
		if(startDate!=null && endDate!=null && startDate!="" && endDate!= "")
		{
			currentMonthSql = "select count(*) as num from LauchRequirements where lauchTime>='"+ startDate +"' and lauchTime<='"+ endDate +"'";
		}
		
		// Initialize <chart> element
		String strXML = "<chart  baseFont='宋体' baseFontSize='12' bgColor='E0F6FB,B2E9F6'  borderColor='B2F0FF' caption='售前支撑(按产品分)' formatNumberScale='0'  palette='4' decimals='0' enableSmartLabels='1' enableRotation='1' bgColor='99CCFF,FFFFFF' bgAlpha='40,100' bgRatio='0,100' bgAngle='360' showBorder='1' startingAngle='70' >";

		Iterator it = dsitrictSet.iterator();
		
		// Iterate through the data
		while (it.hasNext()) {
			String district = (String)it.next();
			Object[] monthList = dbUtil.statisticsBySql(currentMonthSql + " and (requireType1='"+district+"' or requireType2='"+district+"' or requireType3='"+district+"' or requireType4='"+district+"' or requireType5='"+district+"' or requireType6='"+district+"') ");
			int value = Integer.parseInt(monthList==null?"0":monthList[0].toString());
			Object[] aggregate = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='售前支撑' and groupBy='业务需求类型' and district='" + district +"' and DATEPART(year, getdate())<='2010'");
			if(startDate!=null && endDate!=null && startDate!="" && endDate!= ""&& startDate.compareTo("2010-09-01")>0)
			{
				aggregate = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='售前支撑' and groupBy='业务需求类型' and district='" + district +"' and '"+ startDate +"'<='2010-09-01'");
			}
			value += Integer.parseInt(aggregate==null?"0":aggregate[0].toString());
			// Add <set value='...' /> to both the datasets
			strXML += "<set label='" + district + "' value='" +value+ "'/>";
		}
		// Assemble the entire XML now
		strXML = strXML + "</chart>";

		// Create the chart - MS Column 3D Chart with data contained in strXML
		return fcc.createChart("../../Charts/Pie3D.swf", "", strXML,
				"groupByRequireTypePie", 500, 300, false, false);
	}
	
}
