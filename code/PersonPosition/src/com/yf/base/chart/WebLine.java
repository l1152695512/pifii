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
import com.yf.util.Debug;



public class WebLine {
	private static final Logger log = LogManager.getLogger(WebLine.class);
	public HttpServletRequest request = ServletActionContext.getRequest ();

	DBUtil dbu = new DBUtil();

	public String getStrXML() {
		try {

			Debug.println("the startDate is " + request.getParameter("startDate"));
			Debug.println("the endDate is " + request.getParameter("endDate"));
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			dbu.getConnection();

			//String sql = "select substring(startTime,0,8) as shortStartTime,count(*) as cou from acceptBusinessInfo where businessDescriction='互联网数据专线' group by substring(startTime,0,8)";
			//String sql1 = "select substring(startTime,0,8) as shortStartTime,count(clientName) as cou from acceptBusinessInfo where businessDescriction='互联网数据专线' and id in (select Min(id) from acceptBusinessInfo where businessDescriction='互联网数据专线' group by clientName) group by substring(startTime,0,8)";
			String sql = "select substring(dataLaunchedDate,0,8) as shortStartTime,count(*) as cou from GroupCustomerInfo where isDelete='False' and dataLaunchedDate!='' and DATEPART(year, dataLaunchedDate)=DATEPART(year, getdate()) group by substring(dataLaunchedDate,0,8)";
			String sql1 = "select substring(dataLaunchedDate,0,8) as shortStartTime,count(CustomerName) as cou from GroupCustomerInfo where isDelete='False' and dataLaunchedDate!='' and DATEPART(year, dataLaunchedDate)=DATEPART(year, getdate()) and id in (select Min(id) from GroupCustomerInfo where isDelete='False' and dataLaunchedDate!='' and DATEPART(year, dataLaunchedDate)=DATEPART(year, getdate()) group by CustomerName) group by substring(dataLaunchedDate,0,8)";
			
			if(startDate!=null && endDate!=null && startDate!="" && endDate!= "")
			{
				//sql = "select substring(startTime,0,8) as shortStartTime,count(*) as cou from acceptBusinessInfo where businessDescriction='互联网数据专线' and startTime>='"+ startDate +"' and startTime<='"+ endDate +"'  group by substring(startTime,0,8)";
				//sql1 = "select substring(startTime,0,8) as shortStartTime,count(clientName) as cou from acceptBusinessInfo where businessDescriction='互联网数据专线' and startTime>='"+ startDate +"' and startTime<='"+ endDate +"' and id in (select Min(id) from acceptBusinessInfo where businessDescriction='互联网数据专线' and startTime>='"+ startDate +"' and startTime<='"+ endDate +"' group by clientName) group by substring(startTime,0,8)";
				sql="select substring(dataLaunchedDate,0,8) as shortStartTime,count(*) as cou from GroupCustomerInfo where isDelete='False' and dataLaunchedDate!='' and dataLaunchedDate>='"+ startDate +"' and dataLaunchedDate<='"+ endDate +"' group by substring(dataLaunchedDate,0,8)";
				sql1="select substring(dataLaunchedDate,0,8) as shortStartTime,count(CustomerName) as cou from GroupCustomerInfo where isDelete='False' and dataLaunchedDate!='' and dataLaunchedDate>='"+ startDate +"' and dataLaunchedDate<='"+ endDate +"' and id in (select Min(id) from GroupCustomerInfo where isDelete='False' and dataLaunchedDate!='' and dataLaunchedDate>='"+ startDate +"' and dataLaunchedDate<='"+ endDate +"' group by CustomerName) group by substring(dataLaunchedDate,0,8)";			
			}

			List lMap = dbu.getQRunner()
					.query(
							dbu.getConn(),
							sql,
					new MapListHandler());

			List lMap1 = dbu.getQRunner()
			.query(
					dbu.getConn(),
					sql1,
			new MapListHandler());
			
			
			
			String strXML = "";

			strXML = "<chart baseFontSize='12' caption='互联网数据专线客户和业务数量'  bgColor='E0F6FB,B2E9F6'  borderColor='B2F0FF' shownames='1' decimals='0' numberPrefix=''>";

			String strCategories = "<categories>";

			String strDataRev = "<dataset seriesName='集团客户数量' color='AFD8F8'>";
			String strDataQty = "<dataset seriesName='业务数量（专线数量）' color='F6BD0F'>";
			int cous = 0;
			int cous1 = 0;
			for (int i = 0; i < lMap.size(); i++) {
				Map vals = (Map) lMap.get(i);
				Map vals1 = (Map) lMap1.get(i);
				cous += Integer.parseInt(vals.get("cou").toString());
				cous1 += Integer.parseInt(vals1.get("cou").toString());
				strCategories += "<category label='" + vals.get("shortStartTime") + "' />";
				strDataRev += "<set value='" + cous1 + "' />";
				strDataQty = strDataQty + "<set value='" + cous + "' />";
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

}
