package com.yf.base.actions.devicemanage;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class AddOrModifyStation extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String id;
	private String communityId;
	private String cardMark;
	private String name;
	private String locationX;
	private String locationY;
	private String description;
	private String addDate;

	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		if(StringUtils.isNotBlank(id)){
			String sql = "select card_mark,name,locationX,locationY,description,DATE_FORMAT(addDate,'%Y-%m-%d %H:%i:%s') addDate from bp_card_tbl where id='"+id+"' ";
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
			if(dataList.size()>0){
				Map<String,Object> data = dataList.get(0);
				cardMark = null==data.get("card_mark")?"":data.get("card_mark").toString();
				name = null==data.get("name")?"":data.get("name").toString();
				locationX = null==data.get("locationX")?"":data.get("locationX").toString();
				addDate = null==data.get("addDate")?"":data.get("addDate").toString();
				locationY = null==data.get("locationY")?"":data.get("locationY").toString();
				description = null==data.get("description")?"":data.get("description").toString();
			}else{
				return "failure";
			}
		}
		return super.execute();
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
