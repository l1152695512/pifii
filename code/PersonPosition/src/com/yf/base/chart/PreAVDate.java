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



public class PreAVDate {
	private static final Logger log = LogManager.getLogger(PreAVDate.class);
	public HttpServletRequest request = ServletActionContext.getRequest ();
	DBUtil dbu = new DBUtil();
	/**
	 * 勘察设计平均时长
	 * @return
	 */
	public String getPreAVDate() {
		try {
			String dateyear = request.getParameter("dateyear");
			dbu.getConnection();
			
			//签收时间
			//String sql = "select count(*),sum(DATEDIFF(day,CONVERT(varchar(12), ps.CreateDate, 111), CONVERT(varchar(12), pa.CreateDate, 111)))/count(*) as daysum, CONVERT(varchar(7), pa.CreateDate, 120) as submonth " +
			//		"from preSignfor ps, preApproved pa where ps.ModuleInfoId=pa.ModuleInfoId and DATEPART(year, pa.CreateDate)=DATEPART(year, getdate()) group by CONVERT(varchar(7), pa.CreateDate, 120)";
			//申请时间
			String sql = "select count(*),sum(DATEDIFF(day,CONVERT(varchar(12), lr.lauchTime, 111), CONVERT(varchar(12), pa.CreateDate, 111)))/count(*) as daysum, CONVERT(varchar(7), pa.CreateDate, 120) as submonth " +
			"from LauchRequirements lr, preApproved pa where lr.id=pa.ModuleInfoId and DATEPART(year, pa.CreateDate)=DATEPART(year, getdate()) group by CONVERT(varchar(7), pa.CreateDate, 120)";

			if(dateyear!=null && dateyear!="" )
			{
				//sql = "select sum(DATEDIFF(day,lauchTime,actualCompleteTime))/count(*) as daysum,substring(actualCompleteTime,0,8) as submonth " +
				//		"from ProjectScheduleMgr where actualCompleteTime!='' and actualCompleteTime>='"+ dateyear +"-01-01' and actualCompleteTime<='"+ dateyear +"-12-31' " +
				//				"group by substring(actualCompleteTime,0,8)";
				//签收时间
				//sql = "select count(*),sum(DATEDIFF(day,CONVERT(varchar(12), ps.CreateDate, 111), CONVERT(varchar(12), pa.CreateDate, 111)))/count(*) as daysum, CONVERT(varchar(7), pa.CreateDate, 120) as submonth " +
				//"from preSignfor ps, preApproved pa where ps.ModuleInfoId=pa.ModuleInfoId and CONVERT(varchar(10), pa.CreateDate, 120)>='"+ dateyear +"-01-01' and CONVERT(varchar(10), pa.CreateDate, 120)<='"+ dateyear +"-12-31'  group by CONVERT(varchar(7), pa.CreateDate, 120)";
				//申请时间
				sql = "select count(*),sum(DATEDIFF(day,CONVERT(varchar(12), lr.lauchTime, 111), CONVERT(varchar(12), pa.CreateDate, 111)))/count(*) as daysum, CONVERT(varchar(7), pa.CreateDate, 120) as submonth " +
				"from LauchRequirements lr, preApproved pa where lr.id=pa.ModuleInfoId and CONVERT(varchar(10), pa.CreateDate, 120)>='"+ dateyear +"-01-01' and CONVERT(varchar(10), pa.CreateDate, 120)<='"+ dateyear +"-12-31'  group by CONVERT(varchar(7), pa.CreateDate, 120)";				
			}
			
			List lMap = dbu.getQRunner()
					.query(
							dbu.getConn(),
							sql,
					new MapListHandler());			
			
			String strXML = "";

			strXML = "<chart decimals='2' baseFontSize='12' caption='勘察设计平均时长'  bgColor='E0F6FB,B2E9F6'  borderColor='B2F0FF' shownames='1' numberPrefix='' rotateYAxisName='0' xAxisName='' yAxisName='天'>";

			for (int i = 0; i < lMap.size(); i++) {
				Map vals = (Map) lMap.get(i);
				strXML += "<set label='" + vals.get("submonth") + "' value='" + vals.get("daysum") + "'/>";
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
