package com.yf.base.actions.meeting.book;

import java.util.ArrayList;
import java.util.List;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class DeleteData extends ActionSupport {

	private String[] ids;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);

	@Override
	public String execute() throws Exception {
		List<String> sqlList=new ArrayList<String>();
		for (int i=0;i<ids.length;i++) {
			String sql="delete from BP_MEETING_TBL where M_ID='"+ids[i]+"'";
			sqlList.add(sql);
		}
		boolean bool=dbhelper.executeFor(sqlList);
		if(bool){
			return super.execute();
		}else{
			return "failure";
		}
	}
	
	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}
	
}
