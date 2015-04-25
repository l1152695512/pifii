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


public class ProjectStageToType {
	private static final Logger log = LogManager.getLogger(ProjectStageToType.class);
	public HttpServletRequest request = ServletActionContext.getRequest ();

	DBUtil dbu = new DBUtil();

	public String getStrXML() {
		try {
			Debug.println("the startDate is " + request.getParameter("startDate"));
			Debug.println("the endDate is " + request.getParameter("endDate"));
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			dbu.getConnection();

			String sqlA = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '互联网数据专线' and actualCompleteTime=''";
			String sqlB = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '传输租赁' and actualCompleteTime=''";
			String sqlC = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = 'IP VPN' and actualCompleteTime=''";
			String sqlD = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '语音专线（含综V）' and actualCompleteTime=''";
			String sqlE = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '短信专线' and actualCompleteTime=''";
			String sqlF = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = 'GPRS行业应用' and actualCompleteTime=''";
			
			String sqlA1 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '互联网数据专线' and substring(actualCompleteTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			String sqlB1 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '传输租赁' and substring(actualCompleteTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			String sqlC1 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = 'IP VPN' and substring(actualCompleteTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			String sqlD1 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '语音专线（含综V）' and substring(actualCompleteTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			String sqlE1 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '短信专线' and substring(actualCompleteTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			String sqlF1 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = 'GPRS行业应用' and substring(actualCompleteTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			
			String sqlA2 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '互联网数据专线' and substring(lauchTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			String sqlB2 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '传输租赁' and substring(lauchTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			String sqlC2 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = 'IP VPN' and substring(lauchTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			String sqlD2 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '语音专线（含综V）' and substring(lauchTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			String sqlE2 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '短信专线' and substring(lauchTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			String sqlF2 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = 'GPRS行业应用' and substring(lauchTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			
			if(startDate!=null && endDate!=null && startDate!="" && endDate!= "")
			{
				sqlA = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '互联网数据专线' and (actualCompleteTime='' or actualCompleteTime>'"+ endDate +"')";
				sqlB = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '传输租赁' and (actualCompleteTime='' or actualCompleteTime>'"+ endDate +"')";
				sqlC = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = 'IP VPN' and (actualCompleteTime='' or actualCompleteTime>'"+ endDate +"')";
				sqlD = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '语音专线（含综V）' and (actualCompleteTime='' or actualCompleteTime>'"+ endDate +"')";
				sqlE = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '短信专线' and (actualCompleteTime='' or actualCompleteTime>'"+ endDate +"')";
				sqlF = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = 'GPRS行业应用' and (actualCompleteTime='' or actualCompleteTime>'"+ endDate +"')";
				
				sqlA1 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '互联网数据专线' and actualCompleteTime>='"+ startDate +"' and actualCompleteTime<='"+ endDate +"'";
				sqlB1 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '传输租赁' and actualCompleteTime>='"+ startDate +"' and actualCompleteTime<='"+ endDate +"'";
				sqlC1 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = 'IP VPN' and actualCompleteTime>='"+ startDate +"' and actualCompleteTime<='"+ endDate +"'";
				sqlD1 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '语音专线（含综V）' and actualCompleteTime>='"+ startDate +"' and actualCompleteTime<='"+ endDate +"'";
				sqlE1 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '短信专线' and actualCompleteTime>='"+ startDate +"' and actualCompleteTime<='"+ endDate +"'";
				sqlF1 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = 'GPRS行业应用' and actualCompleteTime>='"+ startDate +"' and actualCompleteTime<='"+ endDate +"'";
				
				sqlA2 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '互联网数据专线' and lauchTime>='"+ startDate +"' and lauchTime<='"+ endDate +"'";
				sqlB2 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '传输租赁' and lauchTime>='"+ startDate +"' and lauchTime<='"+ endDate +"'";
				sqlC2 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = 'IP VPN' and lauchTime>='"+ startDate +"' and lauchTime<='"+ endDate +"'";
				sqlD2 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '语音专线（含综V）' and lauchTime>='"+ startDate +"' and lauchTime<='"+ endDate +"'";
				sqlE2 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = '短信专线' and lauchTime>='"+ startDate +"' and lauchTime<='"+ endDate +"'";
				sqlF2 = "select count(*) as cou from ProjectScheduleMgr where bizRequirements = 'GPRS行业应用' and lauchTime>='"+ startDate +"' and lauchTime<='"+ endDate +"'";
			}
			
			List lMapA = (List) dbu.getQRunner().query(dbu.getConn(),sqlA,new MapListHandler());
			List lMapB = (List) dbu.getQRunner().query(dbu.getConn(),sqlB,new MapListHandler());
			List lMapC = (List) dbu.getQRunner().query(dbu.getConn(),sqlC,new MapListHandler());
			List lMapD = (List) dbu.getQRunner().query(dbu.getConn(),sqlD,new MapListHandler());
			List lMapE = (List) dbu.getQRunner().query(dbu.getConn(),sqlE,new MapListHandler());
			List lMapF = (List) dbu.getQRunner().query(dbu.getConn(),sqlF,new MapListHandler());
			
			List lMapA1 = (List) dbu.getQRunner().query(dbu.getConn(),sqlA1,new MapListHandler());
			List lMapB1 = (List) dbu.getQRunner().query(dbu.getConn(),sqlB1,new MapListHandler());
			List lMapC1 = (List) dbu.getQRunner().query(dbu.getConn(),sqlC1,new MapListHandler());
			List lMapD1 = (List) dbu.getQRunner().query(dbu.getConn(),sqlD1,new MapListHandler());
			List lMapE1 = (List) dbu.getQRunner().query(dbu.getConn(),sqlE1,new MapListHandler());
			List lMapF1 = (List) dbu.getQRunner().query(dbu.getConn(),sqlF1,new MapListHandler());
			
			List lMapA2 = (List) dbu.getQRunner().query(dbu.getConn(),sqlA2,new MapListHandler());
			List lMapB2 = (List) dbu.getQRunner().query(dbu.getConn(),sqlB2,new MapListHandler());
			List lMapC2 = (List) dbu.getQRunner().query(dbu.getConn(),sqlC2,new MapListHandler());
			List lMapD2 = (List) dbu.getQRunner().query(dbu.getConn(),sqlD2,new MapListHandler());
			List lMapE2 = (List) dbu.getQRunner().query(dbu.getConn(),sqlE2,new MapListHandler());
			List lMapF2 = (List) dbu.getQRunner().query(dbu.getConn(),sqlF2,new MapListHandler());
			
			String strXML = "";

			strXML = "<chart baseFontSize='12' caption='施工中站点数量（按业务类型分）' bgColor='E0F6FB,B2E9F6'  borderColor='B2F0FF' shownames='1' decimals='0' numberPrefix=''>";
			strXML +="<categories><category label='互联网数据专线'/><category label='传输租赁'/><category label='IP VPN'/><category label='语音专线（含综V）'/><category label='短信专线'/><category label='GPRS行业应用'/></categories>";
			strXML +="<dataset seriesName='施工中数量' color='AFD8F8'>";
			for (int i = 0; i < lMapA.size(); i++) {
				Map valsA = (Map) lMapA.get(i);
				Map valsB = (Map) lMapB.get(i);
				Map valsC = (Map) lMapC.get(i);
				Map valsD = (Map) lMapD.get(i);
				Map valsE = (Map) lMapE.get(i);
				Map valsF = (Map) lMapF.get(i);
				strXML += "<set value='" + valsA.get("cou") + "'/>";
				strXML += "<set value='" + valsB.get("cou") + "'/>";
				strXML += "<set value='" + valsC.get("cou") + "'/>";
				strXML += "<set value='" + valsD.get("cou") + "'/>";
				strXML += "<set value='" + valsE.get("cou") + "'/>";
				strXML += "<set value='" + valsF.get("cou") + "'/>";
			}
			strXML +="</dataset>";
			
			strXML +="<dataset seriesName='完成数量' color='F6BD0F'>";
			for (int i = 0; i < lMapA.size(); i++) {
				Map valsA1 = (Map) lMapA1.get(i);
				Map valsB1 = (Map) lMapB1.get(i);
				Map valsC1 = (Map) lMapC1.get(i);
				Map valsD1 = (Map) lMapD1.get(i);
				Map valsE1 = (Map) lMapE1.get(i);
				Map valsF1 = (Map) lMapF1.get(i);
				strXML += "<set value='" + valsA1.get("cou") + "'/>";
				strXML += "<set value='" + valsB1.get("cou") + "'/>";
				strXML += "<set value='" + valsC1.get("cou") + "'/>";
				strXML += "<set value='" + valsD1.get("cou") + "'/>";
				strXML += "<set value='" + valsE1.get("cou") + "'/>";
				strXML += "<set value='" + valsF1.get("cou") + "'/>";
			}
			strXML +="</dataset>";
			
			strXML +="<dataset seriesName='启动数量' color='8BBA00'>";
			for (int i = 0; i < lMapA.size(); i++) {
				Map valsA2 = (Map) lMapA2.get(i);
				Map valsB2 = (Map) lMapB2.get(i);
				Map valsC2 = (Map) lMapC2.get(i);
				Map valsD2 = (Map) lMapD2.get(i);
				Map valsE2 = (Map) lMapE2.get(i);
				Map valsF2 = (Map) lMapF2.get(i);
				strXML += "<set value='" + valsA2.get("cou") + "'/>";
				strXML += "<set value='" + valsB2.get("cou") + "'/>";
				strXML += "<set value='" + valsC2.get("cou") + "'/>";
				strXML += "<set value='" + valsD2.get("cou") + "'/>";
				strXML += "<set value='" + valsE2.get("cou") + "'/>";
				strXML += "<set value='" + valsF2.get("cou") + "'/>";
			}
			strXML +="</dataset>";
			
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
