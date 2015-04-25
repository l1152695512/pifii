package com.yf.base.actions.map.area;

import javax.sql.RowSet;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;


public class ViewMap extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String id;
	private String communityMapPath;

	@Override
	public String execute() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT map ");
		sql.append("FROM bp_community_tbl ");
		sql.append("WHERE id = '"+id+"'");
		RowSet rs = dbhelper.select(sql.toString());
		if(rs.next()){
			communityMapPath = rs.getString("map");
		}else{
			return "failure";
		}
		return super.execute();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCommunityMapPath() {
		return communityMapPath;
	}
	
	public void setCommunityMapPath(String communityMapPath) {
		this.communityMapPath = communityMapPath;
	}

}
