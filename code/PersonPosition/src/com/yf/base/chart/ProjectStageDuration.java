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



public class ProjectStageDuration {
	private static final Logger log = LogManager.getLogger(ProjectStageDuration.class);
	public HttpServletRequest request = ServletActionContext.getRequest ();

	DBUtil dbu = new DBUtil();

	public String getStrXML() {
		try {
			String startDate = request.getParameter("startDate");
			String endDate = request.getParameter("endDate");
			dbu.getConnection();

			//总工期
			String sqlA = "select sum(DATEDIFF(day,lauchTime,actualCompleteTime))/count(*) as daysum from ProjectScheduleMgr where actualCompleteTime!='' and substring(actualCompleteTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			//需求分析和方案设计
			String sqlB = "select sum(DATEDIFF(day,analysisStartTime,analysisEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where analysisEndTime!='' and analysisEndTime!='不需要' and substring(actualCompleteTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			//资源预占
			String sqlC = "select sum(DATEDIFF(day,resourceCampOnStartTime,resourceCampOnEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where resourceCampOnEndTime!='' and resourceCampOnEndTime!='不需要' and substring(actualCompleteTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			//管线工程
			String sqlD = "select sum(DATEDIFF(day,utilitiesPipelineStartTime,utilitiesPipelineEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where utilitiesPipelineEndTime!='' and utilitiesPipelineEndTime!='不需要' and substring(actualCompleteTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			//局数据
			String sqlE = "select sum(DATEDIFF(day,juDataStartTime,juDataEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where juDataEndTime!='' and juDataEndTime!='不需要' and substring(actualCompleteTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			//设备安装
			String sqlF = "select sum(DATEDIFF(day,deviceInstallStartTime,deviceInstallEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where deviceInstallEndTime!='' and deviceInstallEndTime!='不需要' and substring(actualCompleteTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			//电/光路调度
			String sqlG = "select sum(DATEDIFF(day,dispatchcircuitStartTime,dispatchcircuitEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where dispatchcircuitEndTime!='' and dispatchcircuitEndTime!='不需要' and substring(actualCompleteTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			//综合布线
			String sqlH = "select sum(DATEDIFF(day,genericCablingStartTime,genericCablingEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where genericCablingEndTime!='' and genericCablingEndTime!='不需要' and substring(actualCompleteTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			//业务调测
			String sqlI = "select sum(DATEDIFF(day,bizCommissioningTestStartTime,bizCommissioningTestEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where bizCommissioningTestEndTime!='' and bizCommissioningTestEndTime!='不需要' and substring(actualCompleteTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			//业务开通
			String sqlJ = "select sum(DATEDIFF(day,bizLauchStartTime,bizLauchEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where bizLauchEndTime!='' and bizLauchEndTime!='不需要' and substring(actualCompleteTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";
			//验收交付
			String sqlK = "select sum(DATEDIFF(day,bizAcceptanceStartTime,bizAcceptanceEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where bizAcceptanceEndTime!='' and bizAcceptanceEndTime!='不需要' and substring(actualCompleteTime,0,8)= substring(convert(char(10),getdate(),120),0,8)";

			if(startDate!=null && endDate!=null && startDate!="" && endDate!= "")
			{
				sqlA = "select sum(DATEDIFF(day,lauchTime,actualCompleteTime))/count(*) as daysum from ProjectScheduleMgr where actualCompleteTime!='' and actualCompleteTime>='"+ startDate +"' and actualCompleteTime<='"+ endDate +"'";
				sqlB = "select sum(DATEDIFF(day,analysisStartTime,analysisEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where analysisEndTime!='' and analysisEndTime!='不需要' and analysisEndTime>='"+ startDate +"' and analysisEndTime<='"+ endDate +"'";
				sqlC = "select sum(DATEDIFF(day,resourceCampOnStartTime,resourceCampOnEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where resourceCampOnEndTime!='' and resourceCampOnEndTime!='不需要' and resourceCampOnEndTime>='"+ startDate +"' and resourceCampOnEndTime<='"+ endDate +"'";
				sqlD = "select sum(DATEDIFF(day,utilitiesPipelineStartTime,utilitiesPipelineEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where utilitiesPipelineEndTime!='' and utilitiesPipelineEndTime!='不需要' and utilitiesPipelineEndTime>='"+ startDate +"' and utilitiesPipelineEndTime<='"+ endDate +"'";
				sqlE = "select sum(DATEDIFF(day,juDataStartTime,juDataEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where juDataEndTime!='' and juDataEndTime!='不需要' and juDataEndTime>='"+ startDate +"' and juDataEndTime<='"+ endDate +"'";
				sqlF = "select sum(DATEDIFF(day,deviceInstallStartTime,deviceInstallEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where deviceInstallEndTime!='' and deviceInstallEndTime!='不需要' and deviceInstallEndTime>='"+ startDate +"' and deviceInstallEndTime<='"+ endDate +"'";
				sqlG = "select sum(DATEDIFF(day,dispatchcircuitStartTime,dispatchcircuitEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where dispatchcircuitEndTime!='' and dispatchcircuitEndTime!='不需要' and dispatchcircuitEndTime>='"+ startDate +"' and dispatchcircuitEndTime<='"+ endDate +"'";
				sqlH = "select sum(DATEDIFF(day,genericCablingStartTime,genericCablingEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where genericCablingEndTime!='' and genericCablingEndTime!='不需要' and genericCablingEndTime>='"+ startDate +"' and genericCablingEndTime<='"+ endDate +"'";
				sqlI = "select sum(DATEDIFF(day,bizCommissioningTestStartTime,bizCommissioningTestEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where bizCommissioningTestEndTime!='' and bizCommissioningTestEndTime!='不需要' and bizCommissioningTestEndTime>='"+ startDate +"' and bizCommissioningTestEndTime<='"+ endDate +"'";
				sqlJ = "select sum(DATEDIFF(day,bizLauchStartTime,bizLauchEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where bizLauchEndTime!='' and bizLauchEndTime!='不需要' and bizLauchEndTime>='"+ startDate +"' and bizLauchEndTime<='"+ endDate +"'";
				sqlK = "select sum(DATEDIFF(day,bizAcceptanceStartTime,bizAcceptanceEndTime)+1)/count(*) as daysum from ProjectScheduleMgr where bizAcceptanceEndTime!='' and bizAcceptanceEndTime!='不需要' and bizAcceptanceEndTime>='"+ startDate +"' and bizAcceptanceEndTime<='"+ endDate +"'";
			}
			
			List lMapA = (List) dbu.getQRunner().query(dbu.getConn(),sqlA,new MapListHandler());
			List lMapB = (List) dbu.getQRunner().query(dbu.getConn(),sqlB,new MapListHandler());
			List lMapC = (List) dbu.getQRunner().query(dbu.getConn(),sqlC,new MapListHandler());
			List lMapD = (List) dbu.getQRunner().query(dbu.getConn(),sqlD,new MapListHandler());
			List lMapE = (List) dbu.getQRunner().query(dbu.getConn(),sqlE,new MapListHandler());
			List lMapF = (List) dbu.getQRunner().query(dbu.getConn(),sqlF,new MapListHandler());
			List lMapG = (List) dbu.getQRunner().query(dbu.getConn(),sqlG,new MapListHandler());
			List lMapH = (List) dbu.getQRunner().query(dbu.getConn(),sqlH,new MapListHandler());
			List lMapI = (List) dbu.getQRunner().query(dbu.getConn(),sqlI,new MapListHandler());
			List lMapJ = (List) dbu.getQRunner().query(dbu.getConn(),sqlJ,new MapListHandler());
			List lMapK = (List) dbu.getQRunner().query(dbu.getConn(),sqlK,new MapListHandler());
			
		
			String strXML = "";

			strXML = "<chart baseFontSize='12' caption='工期统计' bgColor='E0F6FB,B2E9F6'  borderColor='B2F0FF' rotateYAxisName='0' xAxisName='' yAxisName='工期' decimals='2' formatNumberScale='0' >";
			for (int i = 0; i < lMapA.size(); i++) {
				Map valsA = (Map) lMapA.get(i);
				Map valsB = (Map) lMapB.get(i);
				Map valsC = (Map) lMapC.get(i);
				Map valsD = (Map) lMapD.get(i);
				Map valsE = (Map) lMapE.get(i);
				Map valsF = (Map) lMapF.get(i);
				Map valsG = (Map) lMapG.get(i);
				Map valsH = (Map) lMapH.get(i);
				Map valsI = (Map) lMapI.get(i);
				Map valsJ = (Map) lMapJ.get(i);
				Map valsK = (Map) lMapK.get(i);
				strXML += "<set label='总工期' value='" + valsA.get("daysum") + "'/>";
				strXML += "<set label='需求分析和方案设计' value='" + valsB.get("daysum") + "'/>";
				strXML += "<set label='资源预占' value='" + valsC.get("daysum") + "'/>";
				strXML += "<set label='管线工程' value='" + valsD.get("daysum") + "'/>";
				strXML += "<set label='局数据' value='" + valsE.get("daysum") + "'/>";
				strXML += "<set label='设备安装' value='" + valsF.get("daysum") + "'/>";
				strXML += "<set label='电/光路调度' value='" + valsG.get("daysum") + "'/>";
				strXML += "<set label='综合布线' value='" + valsH.get("daysum") + "'/>";
				strXML += "<set label='业务调测' value='" + valsI.get("daysum") + "'/>";
				strXML += "<set label='业务开通' value='" + valsJ.get("daysum") + "'/>";
				strXML += "<set label='验收交付' value='" + valsK.get("daysum") + "'/>";
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
	
	//平均工期
	public String getAvTimeXML() {
		try {
			String dateyear = request.getParameter("dateyear");
			dbu.getConnection();
			
			String sql = "select sum(DATEDIFF(day,lauchTime,actualCompleteTime))/count(*) as daysum,substring(actualCompleteTime,0,8) as submonth " +
					"from ProjectScheduleMgr where actualCompleteTime!='' and DATEPART(year, actualCompleteTime)=DATEPART(year, getdate()) " +
					"group by substring(actualCompleteTime,0,8)";

			if(dateyear!=null && dateyear!="" )
			{
				sql = "select sum(DATEDIFF(day,lauchTime,actualCompleteTime))/count(*) as daysum,substring(actualCompleteTime,0,8) as submonth " +
						"from ProjectScheduleMgr where actualCompleteTime!='' and actualCompleteTime>='"+ dateyear +"-01-01' and actualCompleteTime<='"+ dateyear +"-12-31' " +
								"group by substring(actualCompleteTime,0,8)";
			}
			
			List lMap = (List) dbu.getQRunner()
					.query(
							dbu.getConn(),
							sql,
					new MapListHandler());			
			
			String strXML = "";

			strXML = "<chart decimals='2' baseFontSize='12' caption='平均工期'  bgColor='E0F6FB,B2E9F6'  borderColor='B2F0FF' shownames='1' numberPrefix='' rotateYAxisName='0' xAxisName='' yAxisName='天'>";

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
