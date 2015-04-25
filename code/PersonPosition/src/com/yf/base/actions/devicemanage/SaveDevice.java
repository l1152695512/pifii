package com.yf.base.actions.devicemanage;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SaveDevice extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	
	private String msg;
	private String id;
	private String communityId;
	private String cardMark;
	private String name;
	private String locationX;
	private String locationY;
	private String description;
	private String addDate;

	@Override
	public String execute() throws Exception {
		StringBuffer sql = new StringBuffer();
		if(StringUtils.isNotBlank(id)){
			sql.append("update bp_card_tbl ");
			sql.append("set name = ?,description = ? ");
			sql.append("where id = ? ");
			Object[] params = new Object[3];
			params[0] = name;
			params[1] = description;
			params[2] = id;
			if(dbhelper.update(sql.toString(),params)){
				return super.execute();
			}
		}else{
			//and communityId=?
			if(dbhelper.getMapListBySql("select id from bp_card_tbl where card_mark=? ", new Object[]{cardMark}).size() > 0){
				msg = "cardMarkError";
				return "failure";
			}
			sql.append("insert into bp_card_tbl(id,communityId,card_mark,name,locationX,locationY,description,addDate) ");
			sql.append("VALUES(?,?,?,?,?,?,?,now()) ");
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			Object[] params = new Object[7];
			params[0] = id;
			params[1] = communityId;
			params[2] = cardMark;
			params[3] = name;
			params[4] = locationX;
			params[5] = locationY;
			params[6] = description;
			if(dbhelper.insert(sql.toString(),params)){
				return super.execute();
			}
		}
		return "failure";
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getCommunityId() {
		return communityId;
	}
	
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	
	public String getCardMark() {
		return cardMark;
	}
	
	public void setCardMark(String cardMark) {
		this.cardMark = cardMark;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocationX() {
		return locationX;
	}

	public void setLocationX(String locationX) {
		this.locationX = locationX;
	}

	public String getLocationY() {
		return locationY;
	}

	public void setLocationY(String locationY) {
		this.locationY = locationY;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getAddDate() {
		return addDate;
	}
	
	public void setAddDate(String addDate) {
		this.addDate = addDate;
	}
	
}
