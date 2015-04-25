package com.yf.base.actions.meeting.book;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.sql.RowSet;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.HibernateUUId;
import com.yf.util.dbhelper.DBHelper;

public class SaveForm extends ActionSupport {
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	@Override
	public String execute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String startTime=request.getParameter("START_TIME");
		String endTime=request.getParameter("END_TIME");
		String mRoom=request.getParameter("M_ROOM").substring(0, 1);
		StringBuffer sb=new StringBuffer();
		sb.append("select M_NAME from BP_MEETING_TBL where ((START_TIME>=to_date('"+startTime+"','yyyy-MM-dd HH24:mi:ss') and END_TIME<=to_date('"+endTime+"','yyyy-MM-dd HH24:mi:ss'))  ");
		sb.append("or (START_TIME<to_date('"+endTime+"','yyyy-MM-dd HH24:mi:ss') and END_TIME>to_date('"+endTime+"','yyyy-MM-dd HH24:mi:ss'))  ");
		sb.append("or (START_TIME<to_date('"+startTime+"','yyyy-MM-dd HH24:mi:ss') and END_TIME>to_date('"+startTime+"','yyyy-MM-dd HH24:mi:ss'))) and M_ROOM='"+mRoom+"'");
		RowSet rs=dbhelper.select(sb.toString());
		if(rs.next()){
			return "failure";
		}
		else{
			String strType=request.getParameter("M_TYPE");
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String orderTime=df.format(new Date());
			String mId=new HibernateUUId().generate().toString();
			String mName=request.getParameter("M_NAME");
			String mStatus="预约";
			String charge=request.getParameter("CHARGE");
			String type="";
			String sql="";
			if("其他".equals(strType)){
				type="2";
				sql="insert into BP_MEETING_TBL (M_ID,M_NAME,M_STATUS,CHARGE,START_TIME,END_TIME,ORDER_TIME,M_ROOM,M_TYPE) values('"+mId+"','"+mName+"','"+mStatus+"','"+charge+"',to_date('"+startTime+"','yyyy-MM-dd HH24:mi:ss'),to_date('"+endTime+"','yyyy-MM-dd HH24:mi:ss'),to_date('"+orderTime+"','yyyy-MM-dd HH24:mi:ss'),'"+mRoom+"','"+type+"')";
			}else {
				type="1";
				String pro=request.getParameter("PROJECT");
				String com=request.getParameter("COMPANY");
				String join=request.getParameter("JOIN");
				String wjoin=request.getParameter("W_JOIN");
				String memo=request.getParameter("MEMO");
				sql="insert into BP_MEETING_TBL values('"+mId+"','"+mName+"','"+mStatus+"','"+charge+"',to_date('"+startTime+"','yyyy-MM-dd HH24:mi:ss'),to_date('"+endTime+"','yyyy-MM-dd HH24:mi:ss'),to_date('"+orderTime+"','yyyy-MM-dd HH24:mi:ss'),'"+mRoom+"','"+type+"','"+pro+"','"+com+"','"+join+"','"+wjoin+"','"+memo+"')";
			}
			boolean boo=dbhelper.insert(sql);
			if(boo){
				return super.execute();
			}else{
				return "failure";
			}
		}
	}
}
