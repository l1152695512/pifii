package com.yf.base.actions.map.area;

import javax.sql.RowSet;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;


public class PreviewImage extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String id;
	private String path;

	@Override
	public String execute() throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT preview_image ");
		sql.append("FROM bp_community_tbl ");
		sql.append("WHERE id = '"+id+"'");
		RowSet rs = dbhelper.select(sql.toString());
		if(rs.next()){
			path = rs.getString("preview_image");
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
