package com.yf.base.actions.warnmanage.warnareaassign;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SaveAssignPerson_1 extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String[] personIds;
	private String areaId;
	
	@Override
	public String execute() throws Exception {
		List<String> sqls = new ArrayList<String>();
		for(int i=0;i<personIds.length;i++){
			if(!"".equals(personIds[i])){
				String id = UUID.randomUUID().toString().replaceAll("-", "");
				sqls.add("insert into bp_warn_area_person_assign_tbl(id,warn_area_id,person_id,add_date) values('"+id+"','"+areaId+"','"+personIds[i]+"',now()) ");
			}
		}
		if(dbhelper.executeFor(sqls)){
			return super.execute();
		}
		return "failure";
	}
	
	public String[] getPersonIds() {
		return personIds;
	}
	
	public void setPersonIds(String[] personIds) {
		this.personIds = personIds;
	}
	
	public String getAreaId() {
		return areaId;
	}
	
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}
	
}
