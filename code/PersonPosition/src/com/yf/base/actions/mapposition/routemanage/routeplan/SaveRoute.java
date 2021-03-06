package com.yf.base.actions.mapposition.routemanage.routeplan;

import java.util.UUID;



import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SaveRoute extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String id;
	private String name;
	private String effectiveRange;
	private String description;

	@Override
	public String execute() throws Exception {
		description = description.replaceAll("\n", "<br>");
		StringBuffer sql = new StringBuffer();
		if(StringUtils.isNotBlank(id)){
			sql.append("update bp_coarse_route_tbl set name=?,effective_range=?,description=? where id=? ");
			Object[] params = new Object[4];
			params[0] = name;
			params[1] = effectiveRange;
			params[2] = description;
			params[3] = id;
			if(dbhelper.update(sql.toString(),params)){
				return super.execute();
			}
		}else{
			sql.append("insert into bp_coarse_route_tbl(id,name,effective_range,description,add_date) ");
			sql.append("VALUES(?,?,?,?,now()) ");
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			Object[] params = new Object[4];
			params[0] = id;
			params[1] = name;
			params[2] = effectiveRange;
			params[3] = description;
			if(dbhelper.insert(sql.toString(),params)){
				return super.execute();
			}
		}
		return "failure";
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getEffectiveRange() {
		return effectiveRange;
	}
	
	public void setEffectiveRange(String effectiveRange) {
		this.effectiveRange = effectiveRange;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
}
