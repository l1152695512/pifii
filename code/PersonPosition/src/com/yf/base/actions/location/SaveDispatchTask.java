package com.yf.base.actions.location;

import java.util.UUID;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SaveDispatchTask extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String personId;
	private String name;
	private String type;
	private String location;
	private String description;
	private String helpPersonId;
	
	@Override
	public String execute() throws Exception {
		description = description.replaceAll("\n", "<br>");
		String taskId = UUID.randomUUID().toString().replaceAll("-", "");
		StringBuffer sql = new StringBuffer();
		sql.append("insert into bp_dispatch_task_tbl(id,person_id,dispatch_type_id,description,location_x,location_y,help_person_id,task_date) values(?,?,?,?,?,?,?,now()) ");
		Object[] params = new Object[7];
		params[0] = taskId;
		params[1] = personId;
		params[2] = type;
		params[3] = description;
		Double locationX = null;
		Double locationY = null;
		if(!type.equals("8")){//类型不为发送信息
			try{
				String[] locationXY = location.split(",");
				locationX = Double.parseDouble(locationXY[0]);
				locationY = Double.parseDouble(locationXY[1]);
			}catch(Exception e){
				return "failure";
			}
		}
		params[4] = locationX;
		params[5] = locationY;
		if(type.equals("5")){//类型为响应救助
			params[6] = helpPersonId;
		}else{
			params[6] = "";
		}
		if(dbhelper.insert(sql.toString(),params)){
			return super.execute();
		}
		return "failure";
	}
	
	public String getPersonId() {
		return personId;
	}
	
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getHelpPersonId() {
		return helpPersonId;
	}
	
	public void setHelpPersonId(String helpPersonId) {
		this.helpPersonId = helpPersonId;
	}
	
}
