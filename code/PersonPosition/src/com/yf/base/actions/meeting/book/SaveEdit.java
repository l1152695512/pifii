package com.yf.base.actions.meeting.book;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SaveEdit extends ActionSupport {
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	@Override
	public String execute() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		String mId=request.getParameter("M_ID");
		String strDate=request.getParameter("M_DATE");
		String start=request.getParameter("START");
		String end=request.getParameter("END");
		String strStart=strDate+" "+start;
		String strEnd=strDate+" "+end;
		String sql="update BP_MEETING_TBL set START_TIME=to_date('"+strStart+"','yyyy-MM-dd HH24:mi:ss'),END_TIME=to_date('"+strEnd+"','yyyy-MM-dd HH24:mi:ss') where M_ID='"+mId+"'";
		boolean boo=dbhelper.update(sql);
		if(boo){
			return super.execute();
		}else{
			return "failure";
		}
	}
}
