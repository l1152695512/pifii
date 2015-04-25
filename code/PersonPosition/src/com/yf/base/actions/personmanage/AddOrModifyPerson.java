package com.yf.base.actions.personmanage;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class AddOrModifyPerson extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String isRead = "no";
	private String id;
	private String name;
	private String communityId;
	private String dictionaryId;
	private String age;
	private String phone;
	private String description;
	private String photo;

	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		if(StringUtils.isNotBlank(id)){
			String sql = "select communityId,name,age,dictionaryId,phone,description,photo from bp_person_tbl where id='"+id+"' ";
			List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sql);
			if(dataList.size()>0){
				Map<String,Object> data = dataList.get(0);
				name = null==data.get("name")?"":data.get("name").toString();
				communityId = null==data.get("communityId")?"":data.get("communityId").toString();
				dictionaryId = null==data.get("dictionaryId")?"":data.get("dictionaryId").toString();
				age = null==data.get("age")?"":data.get("age").toString();
				phone = null==data.get("phone")?"":data.get("phone").toString();
				description = null==data.get("description")?"":data.get("description").toString();
				photo = null==data.get("photo")?"":data.get("photo").toString();
			}else{
				return "failure";
			}
		}
		return super.execute();
	}

	public String getIsRead() {
		return isRead;
	}
	
	public void setIsRead(String isRead) {
		this.isRead = isRead;
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

	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	public String getDictionaryId() {
		return dictionaryId;
	}

	public void setDictionaryId(String dictionaryId) {
		this.dictionaryId = dictionaryId;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPhoto() {
		return photo;
	}
	
	public void setPhoto(String photo) {
		this.photo = photo;
	}
}
