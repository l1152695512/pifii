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

public class EmipWorkLast {
	private static final Logger log = LogManager.getLogger(TroubleDealLast.class);
	public HttpServletRequest request = ServletActionContext.getRequest ();

	DBUtil dbu = new DBUtil();

	public String getStrXML() {
		try {
			String dateyear = request.getParameter("dateyear");
			String adjustType = request.getParameter("adjustType");
			dbu.getConnection();
			String sql="";
			if(adjustType!=null && adjustType.equals("工程施工"))
			{
				sql = "select round(sum(cast(baa.adjustDuration as float))/count(*),2) as adjustLast, substring(eo.DispatchTime,0,8) as shortDispatchTime, count(*) as cou from EMOSOrder as eo, bizAdjustAnalysis as baa,bizAdjustProcess as bap " +
						"where eo.id=baa.emosOrderID and eo.filing='已归档' and bap.feedbackStage like '%工程施工%' and DATEPART(year, eo.DispatchTime)=DATEPART(year, getdate()) group by substring(eo.DispatchTime,0,8)";
				if(dateyear!=null && dateyear!="" )
				{
					sql = "select round(sum(cast(baa.adjustDuration as float))/count(*),2) as adjustLast, substring(eo.DispatchTime,0,8) as shortDispatchTime, count(*) as cou from EMOSOrder as eo, bizAdjustAnalysis as baa,bizAdjustProcess as bap " +
					"where eo.id=baa.emosOrderID and eo.filing='已归档' and bap.feedbackStage like '%工程施工%' and substring(eo.DispatchTime,0,12)>='"+ dateyear +"-01-01' and substring(eo.DispatchTime,0,12)<='"+ dateyear +"-12-31' group by substring(eo.DispatchTime,0,8)";
				}
			}
			else
			{
				sql = "select round(sum(cast(baa.adjustDuration as float))/count(*),2) as adjustLast, substring(eo.DispatchTime,0,8) as shortDispatchTime, count(*) as cou from EMOSOrder as eo, bizAdjustAnalysis as baa,bizAdjustProcess as bap " +
				"where eo.id=baa.emosOrderID and eo.id=bap.emosOrderID and eo.filing='已归档' and charindex('工程施工',bap.feedbackStage)<=0 and DATEPART(year, eo.DispatchTime)=DATEPART(year, getdate()) group by substring(eo.DispatchTime,0,8)";
				if(dateyear!=null && dateyear!="" )
				{
					sql = "select round(sum(cast(baa.adjustDuration as float))/count(*),2) as adjustLast, substring(eo.DispatchTime,0,8) as shortDispatchTime, count(*) as cou from EMOSOrder as eo, bizAdjustAnalysis as baa,bizAdjustProcess as bap " +
					"where eo.id=baa.emosOrderID and eo.id=bap.emosOrderID and eo.filing='已归档' and charindex('工程施工',bap.feedbackStage)<=0 and substring(eo.DispatchTime,0,12)>='"+ dateyear +"-01-01' and substring(eo.DispatchTime,0,12)<='"+ dateyear +"-12-31' group by substring(eo.DispatchTime,0,8)";
				}
			}
			List lMap = dbu.getQRunner()
					.query(
							dbu.getConn(),
							sql,
					new MapListHandler());			
			
			String strXML = "";

			strXML = "<chart decimals='2' baseFontSize='12' caption='平均业务调整时长'  bgColor='E0F6FB,B2E9F6'  borderColor='B2F0FF' shownames='1' numberPrefix='' rotateYAxisName='0' xAxisName='' yAxisName='小时'>";

			//String strCategories = "<categories>";

			//String strDataRev = "<dataset seriesName='平均调整时长' color='AFD8F8'>";
			for (int i = 0; i < lMap.size(); i++) {
				Map vals = (Map) lMap.get(i);
				//strCategories += "<category label='" + vals.get("shortDispatchTime") + "' />";
				//strDataRev += "<set value='" + vals.get("adjustLast") + "' />";
				strXML += "<set label='" + vals.get("shortDispatchTime") + "' value='" + vals.get("adjustLast") + "'/>";
			}

			//strCategories += "</categories>";

			//strDataRev += "</dataset>";

			//strXML += strCategories + strDataRev +  "</chart>";
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
