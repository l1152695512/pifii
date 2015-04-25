package com.yf.base.chart;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.yf.util.DBUtil;



public class TroubleDealLast {
	private static final Logger log = LogManager.getLogger(TroubleDealLast.class);
	public HttpServletRequest request = ServletActionContext.getRequest ();

	DBUtil dbu = new DBUtil();

	public String getStrXML() {
		try {
			String dateyear = request.getParameter("dateyear");
			String bizRequirements = request.getParameter("bizRequirements");
			dbu.getConnection();
			String sql="";
			if(bizRequirements!=null && bizRequirements!=""){
				sql = "select round(sum(cast(ta.durationProcess as float))/count(*),2) as dealLast, round(sum(cast(ta.troubleDuration as float))/count(*),2) as troubleLast,substring(tp.resumedTime,0,8) as shortResumedTime " +
						"from troubleAnalysis as ta, troubleProcess as tp where ta.emosOrderID=tp.emosOrderID and tp.bizRequirements='"+ bizRequirements +"' and DATEPART(year, tp.resumedTime)=DATEPART(year, getdate()) group by substring(tp.resumedTime,0,8)";
				if(dateyear!=null && dateyear!="" )
				{
					sql = "select round(sum(cast(ta.durationProcess as float))/count(*),2) as dealLast, round(sum(cast(ta.troubleDuration as float))/count(*),2) as troubleLast,substring(tp.resumedTime,0,8) as shortResumedTime " +
					"from troubleAnalysis as ta, troubleProcess as tp where ta.emosOrderID=tp.emosOrderID and tp.bizRequirements='"+ bizRequirements +"' and substring(tp.resumedTime,0,12)>='"+ dateyear +"-01-01' and substring(tp.resumedTime,0,12)<='"+ dateyear +"-12-31' group by substring(tp.resumedTime,0,8)";
				}
			}else{
				sql = "select round(sum(cast(ta.durationProcess as float))/count(*),2) as dealLast, round(sum(cast(ta.troubleDuration as float))/count(*),2) as troubleLast,substring(tp.resumedTime,0,8) as shortResumedTime " +
				"from troubleAnalysis as ta, troubleProcess as tp where ta.emosOrderID=tp.emosOrderID and DATEPART(year, tp.resumedTime)=DATEPART(year, getdate()) group by substring(tp.resumedTime,0,8)";
				if(dateyear!=null && dateyear!="" )
				{
					sql = "select round(sum(cast(ta.durationProcess as float))/count(*),2) as dealLast, round(sum(cast(ta.troubleDuration as float))/count(*),2) as troubleLast,substring(tp.resumedTime,0,8) as shortResumedTime " +
					"from troubleAnalysis as ta, troubleProcess as tp where ta.emosOrderID=tp.emosOrderID and substring(tp.resumedTime,0,12)>='"+ dateyear +"-01-01' and substring(tp.resumedTime,0,12)<='"+ dateyear +"-12-31' group by substring(tp.resumedTime,0,8)";
				}
			}
			
			List lMap = dbu.getQRunner()
					.query(
							dbu.getConn(),
							sql,
					new MapListHandler());			
			
			String strXML = "";

			strXML = "<chart decimals='2' baseFontSize='12' caption='故障处理时长'  bgColor='E0F6FB,B2E9F6'  borderColor='B2F0FF' shownames='1' numberPrefix='' xAxisName='' yAxisName='小时' rotateYAxisName='0' >";

			String strCategories = "<categories>";

			String strDataRev = "<dataset seriesName='平均处理时长' color='AFD8F8'>";
			String strDataQty = "<dataset seriesName='平均故障历时' color='F6BD0F'>";
			int cous = 0;
			for (int i = 0; i < lMap.size(); i++) {
				Map vals = (Map) lMap.get(i);
				strCategories += "<category label='" + vals.get("shortResumedTime") + "' />";
				strDataRev += "<set value='" + vals.get("dealLast") + "' />";
				strDataQty = strDataQty + "<set value='" + vals.get("troubleLast") + "' />";
			}

			strCategories += "</categories>";

			strDataRev += "</dataset>";
			strDataQty += "</dataset>";

			strXML += strCategories + strDataRev + strDataQty + "</chart>";

			return strXML;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(dbu.getConn());
		}
		
		return "";
	}
	
	//平均上门时间
	public String getAvVisitTimeXML() {
		try {
			String dateyear = request.getParameter("dateyear");
			dbu.getConnection();
			
			String sql = "select sum(DATEDIFF(minute,tp.informTime,tp.arrivedSceneTime))/count(*) as minsum,substring(tp.informTime,0,8) as subMonth " +
					"from EMOSOrder as eo, troubleProcess as tp where eo.id=tp.emosOrderID and tp.informTime!='' and tp.arrivedSceneTime!='' and DATEPART(year, tp.informTime)=DATEPART(year, getdate()) and DATEDIFF(minute,informTime,arrivedSceneTime)>0 and DATEDIFF(minute,informTime,arrivedSceneTime)<500 " +
					"group by substring(tp.informTime,0,8)";

			if(dateyear!=null && dateyear!="" )
			{
				sql = "select sum(DATEDIFF(minute,tp.informTime,tp.arrivedSceneTime))/count(*) as minsum,substring(tp.informTime,0,8) as subMonth " +
					"from EMOSOrder as eo, troubleProcess as tp where eo.id=tp.emosOrderID and tp.informTime!='' and tp.arrivedSceneTime!='' and tp.informTime>='"+ dateyear +"-01-01' and tp.informTime<='"+ dateyear +"-12-31' " +
					" and DATEDIFF(minute,informTime,arrivedSceneTime)>0 and DATEDIFF(minute,informTime,arrivedSceneTime)<500 group by substring(tp.informTime,0,8)";
			}
			
			List lMap = dbu.getQRunner()
					.query(
							dbu.getConn(),
							sql,
					new MapListHandler());			
			
			String strXML = "";

			strXML = "<chart decimals='2' baseFontSize='12' caption='平均上门时间'  bgColor='E0F6FB,B2E9F6'  borderColor='B2F0FF' shownames='1' numberPrefix='' rotateYAxisName='0' xAxisName='' yAxisName='分钟'>";

			for (int i = 0; i < lMap.size(); i++) {
				Map vals = (Map) lMap.get(i);
				strXML += "<set label='" + vals.get("subMonth") + "' value='" + vals.get("minsum") + "'/>";
			}
			
			strXML += "</chart>";
			return strXML;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(dbu.getConn());
		}
		
		return "";
	}
	
	
	//业务中断修复时限
	public String getTroubleRevertTimeXML() {
		try {
			String dateyear = request.getParameter("dateyear");
			dbu.getConnection();
			
			String sql = "select sum(DATEDIFF(hour,tp.complaintTime,tp.resumedTime))/count(*) as hoursum,substring(tp.complaintTime,0,8) as subMonth " +
					"from EMOSOrder as eo, troubleProcess as tp where eo.id=tp.emosOrderID and tp.complaintTime!='' and tp.resumedTime!='' and DATEPART(year, tp.complaintTime)=DATEPART(year, getdate()) " +
					"group by substring(tp.complaintTime,0,8)";

			if(dateyear!=null && dateyear!="" )
			{
				sql = "select sum(DATEDIFF(hour,tp.complaintTime,tp.resumedTime))/count(*) as hoursum,substring(tp.complaintTime,0,8) as subMonth " +
					"from EMOSOrder as eo, troubleProcess as tp where eo.id=tp.emosOrderID and tp.complaintTime!='' and tp.resumedTime!='' and tp.complaintTime>='"+ dateyear +"-01-01' and tp.complaintTime<='"+ dateyear +"-12-31' " +
					"group by substring(tp.complaintTime,0,8)";
			}
			
			List lMap = dbu.getQRunner()
					.query(
							dbu.getConn(),
							sql,
					new MapListHandler());			
			
			String strXML = "";

			strXML = "<chart decimals='2' baseFontSize='12' caption='业务中断修复时限'  bgColor='E0F6FB,B2E9F6'  borderColor='B2F0FF' shownames='1' numberPrefix='' rotateYAxisName='0' xAxisName='' yAxisName='小时'>";

			for (int i = 0; i < lMap.size(); i++) {
				Map vals = (Map) lMap.get(i);
				strXML += "<set label='" + vals.get("subMonth") + "' value='" + vals.get("hoursum") + "'/>";
			}
			
			strXML += "</chart>";
			return strXML;

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(dbu.getConn());
		}
		
		return "";
	}

}
