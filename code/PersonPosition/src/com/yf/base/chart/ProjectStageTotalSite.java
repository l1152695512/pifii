package com.yf.base.chart;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.yf.util.DBUtil;


@SuppressWarnings("unchecked")
public class ProjectStageTotalSite {
	
	HttpServletRequest request = ServletActionContext.getRequest ();
	String startDate = request.getParameter("startDate");
	String endDate = request.getParameter("endDate");
	/**
	 * 累计站点数量按分公司统计 柱状图
	 * @return
	 */
	public String groupByBranchColumn() {
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

		String totalHearSql = "select count(*) as num,lauchedFirm from ProjectScheduleMgr where DATEPART(year, lauchTime)=DATEPART(year, getdate())";
		String totalFinishSql = "select count(*) as num,lauchedFirm from ProjectScheduleMgr where remainDay='已完成' and DATEPART(year, lauchTime)=DATEPART(year, getdate())";
		String groupBy = " group by lauchedFirm";
		
		if(startDate!=null && endDate!=null && startDate!="" && endDate!= "")
		{
			totalHearSql = "select count(*) as num,lauchedFirm from ProjectScheduleMgr where lauchTime>='"+ startDate +"' and lauchTime<='"+ endDate +"'";
			totalFinishSql = "select count(*) as num,lauchedFirm from ProjectScheduleMgr where remainDay='已完成' and lauchTime>='"+ startDate +"' and lauchTime<='"+ endDate +"'";
		}
		
		// Initialize <chart> element
		String strXML = "<chart caption='累计站点数量(按区域分)' bgColor='E0F6FB,B2E9F6'  borderColor='B2F0FF' formatNumberScale='0' placeValuesInside='1' decimals='0' baseFontSize='12'>";
		// Initialize <categories> element - necessary to generate a
		// multi-series chart
		String strCategories = "<categories>";

		// Initiate <dataset> elements
		String strDataCurr = "<dataset seriesName='累计受理'>";
		String strDataPrev = "<dataset seriesName='累计完成'>";
		int totalHearSum = 0;
		int totalFinishSum = 0;
		Iterator it = dsitrictSet.iterator();
		
		// Iterate through the data
		while (it.hasNext()) {
			String district = (String)it.next();
			
			// Append <category name='...' /> to strCategories
			strCategories = strCategories + "<category name='" + district + "' />";
			
			// Add <set value='...' /> to both the datasets
			Object[] monthList = dbUtil.statisticsBySql(totalHearSql + " and lauchedFirm like '" + district + "%'" + groupBy);
			int value = Integer.parseInt(monthList==null?"0":monthList[0].toString());
			Object[] aggregate = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='累计受理' and groupBy='分公司' and district='" + district +"' and DATEPART(year, getdate())<='2010'");
			if(startDate!=null && endDate!=null && startDate!="" && endDate!= ""&& startDate.compareTo("2010-09-01")>0)
			{
				aggregate = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='累计受理' and groupBy='分公司' and district='" + district +"' and '"+ startDate +"'<='2010-09-01'");
			}
			value += Integer.parseInt(aggregate==null?"0":aggregate[0].toString());
			strDataCurr = strDataCurr + "<set value='" + value + "' />";
			totalHearSum += value;
			
			Object[] yearList = dbUtil.statisticsBySql(totalFinishSql + " and lauchedFirm like '" + district + "%'" + groupBy);
			int value1 = Integer.parseInt(yearList==null?"0":yearList[0].toString());
			Object[] aggregate1 = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='累计完成' and groupBy='分公司' and district='" + district +"' and DATEPART(year, getdate())<='2010'");
			if(startDate!=null && endDate!=null && startDate!="" && endDate!= ""&& startDate.compareTo("2010-09-01")>0)
			{
				aggregate1 = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='累计完成' and groupBy='分公司' and district='" + district +"' and '"+ startDate +"'<='2010-09-01'");
			}
			value1 += Integer.parseInt(aggregate1==null?"0":aggregate1[0].toString());
			strDataPrev = strDataPrev + "<set value='" + value1 + "' />";
			totalFinishSum += value1;
		}
		
		// Close <categories> element
		strCategories = strCategories + "<category name='合计'/></categories>";

		// Close <dataset> elements
		strDataCurr = strDataCurr + "<set value='"+totalHearSum+"' /></dataset>";
		strDataPrev = strDataPrev + "<set value='"+totalFinishSum+"' /></dataset>";

		// Assemble the entire XML now
		strXML = strXML + strCategories + strDataCurr + strDataPrev
				+ "</chart>";

		// Create the chart - MS Column 3D Chart with data contained in strXML
		return fcc.createChart("../../Charts/MSColumn3D.swf", "", strXML,
				"groupByBranchColumn", 600, 300, false, false);

	}

	/**
	 * 累计站点数量按业务类型统计 柱状图
	 * @return
	 */
	public String groupByTypeColumn() {
		FusionChartsCreator fcc = new FusionChartsCreator();
		DBUtil dbUtil = new DBUtil();
		
		Set<String> dsitrictSet = new HashSet<String>();
		dsitrictSet.add("互联网数据专线");
		dsitrictSet.add("传输租赁");
		dsitrictSet.add("IP VPN");
		dsitrictSet.add("语音专线（含综V)");
		dsitrictSet.add("短信专线");
		dsitrictSet.add("GPRS行业应用");

		String totalHearSql = "select count(*) as num,bizRequirements from ProjectScheduleMgr where DATEPART(year, lauchTime)=DATEPART(year, getdate())";
		String totalFinishSql = "select count(*) as num,bizRequirements from ProjectScheduleMgr where remainDay='已完成' and DATEPART(year, lauchTime)=DATEPART(year, getdate())";
		String groupBy = " group by bizRequirements";
		
		if(startDate!=null && endDate!=null && startDate!="" && endDate!= "")
		{
			totalHearSql = "select count(*) as num,bizRequirements from ProjectScheduleMgr where lauchTime>='"+ startDate +"' and lauchTime<='"+ endDate +"'";
			totalFinishSql = "select count(*) as num,bizRequirements from ProjectScheduleMgr where remainDay='已完成' and lauchTime>='"+ startDate +"' and lauchTime<='"+ endDate +"'";
		}
		
		// Initialize <chart> element
		String strXML = "<chart caption='累计站点数量(按业务类型分)' bgColor='E0F6FB,B2E9F6'  borderColor='B2F0FF' formatNumberScale='0' placeValuesInside='1' decimals='0' baseFontSize='12'>";
		// Initialize <categories> element - necessary to generate a
		// multi-series chart
		String strCategories = "<categories>";

		// Initiate <dataset> elements
		String strDataCurr = "<dataset seriesName='累计受理'>";
		String strDataPrev = "<dataset seriesName='累计完成'>";

		Iterator it = dsitrictSet.iterator();
		
		// Iterate through the data
		while (it.hasNext()) {
			String district = (String)it.next();
			
			// Append <category name='...' /> to strCategories
			strCategories = strCategories + "<category name='" + district + "' />";
			
			// Add <set value='...' /> to both the datasets
			Object[] monthList = dbUtil.statisticsBySql(totalHearSql + " and bizRequirements = '" + district + "'" + groupBy);
			int value = Integer.parseInt(monthList==null?"0":monthList[0].toString());
			Object[] aggregate = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='累计受理' and groupBy='业务需求类型' and district='" + district +"' and DATEPART(year, getdate())<='2010'");
			if(startDate!=null && endDate!=null && startDate!="" && endDate!= ""&& startDate.compareTo("2010-09-01")>0)
			{
				aggregate = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='累计受理' and groupBy='业务需求类型' and district='" + district +"' and '"+ startDate +"'<='2010-09-01'");
			}
			value += Integer.parseInt(aggregate==null?"0":aggregate[0].toString());
			strDataCurr = strDataCurr + "<set value='" + value + "' />";
			
			Object[] yearList = dbUtil.statisticsBySql(totalFinishSql + " and bizRequirements = '" + district + "'" + groupBy);
			int value1 = Integer.parseInt(yearList==null?"0":yearList[0].toString());
			Object[] aggregate1 = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='累计完成' and groupBy='业务需求类型' and district='" + district +"' and DATEPART(year, getdate())<='2010'");
			if(startDate!=null && endDate!=null && startDate!="" && endDate!= ""&& startDate.compareTo("2010-09-01")>0)
			{
				aggregate1 = dbUtil.statisticsBySql("select aggregateValue from aggregate where serviceType='累计完成' and groupBy='业务需求类型' and district='" + district +"' and '"+ startDate +"'<='2010-09-01'");
			}
			value1 += Integer.parseInt(aggregate1==null?"0":aggregate1[0].toString());
			strDataPrev = strDataPrev + "<set value='" + value1 + "' />";
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
				"groupByTypeColumn", 600, 300, false, false);

	}
	
}
