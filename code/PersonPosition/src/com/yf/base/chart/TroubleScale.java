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


public class TroubleScale {
	private static final Logger log = LogManager.getLogger(TroubleScale.class);
	public HttpServletRequest request = ServletActionContext.getRequest ();
	DBUtil dbu = new DBUtil();

	public String getStrXML() {
		try {
			Debug.println("the startDate is " + request.getParameter("startDate"));
			Debug.println("the endDate is " + request.getParameter("endDate"));
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			dbu.getConnection();

			String sql = "select bizRequirements,count(*) as cou from troubleProcess where substring(occurredTime,0,8)= substring(convert(char(10),getdate(),120),0,8) group by bizRequirements";
			
			if(startDate!=null && endDate!=null && startDate!="" && endDate!= "")
			{
				sql = "select bizRequirements,count(*) as cou from troubleProcess where substring(occurredTime,0,12)>='"+ startDate +"' and substring(occurredTime,0,12)<='"+ endDate +"' group by bizRequirements";
			}
			
			List lMap = dbu.getQRunner().query(dbu.getConn(),sql,new MapListHandler());
			
			String strXML = "";

			strXML = "<chart baseFontSize='12' caption='故障投诉比例' bgColor='E0F6FB,B2E9F6'  borderColor='B2F0FF' palette='4' decimals='0' enableSmartLabels='1' enableRotation='0' bgColor='99CCFF,FFFFFF' bgAlpha='40,100' bgRatio='0,100' bgAngle='360' showBorder='1' startingAngle='70'>";

			for (int i = 0; i < lMap.size(); i++) {
				Map vals = (Map) lMap.get(i);
				strXML += "<set label='" + vals.get("bizRequirements") + "' value='" + vals.get("cou") + "' isSliced='1'/>";
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

