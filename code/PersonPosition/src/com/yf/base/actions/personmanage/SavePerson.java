package com.yf.base.actions.personmanage;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SavePerson extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String id;
	private String communityId;
	private String dictionaryId;
	private String name;
	private String age;
	private String phone;
	private String description;
	private String photo;

	@Override
	public String execute() throws Exception {
		StringBuffer sql = new StringBuffer();
		description = description.replaceAll("\n", "<br>");
		if(StringUtils.isNotBlank(id)){
			updatePhoto();
			sql.append("update bp_person_tbl ");
			sql.append("set name = ?,age = ?,phone = ?,description = ?,dictionaryId = ?,photo = ? ");
			sql.append("where id = ? ");
			Object[] params = new Object[7];
			params[0] = name;
			params[1] = age;
			params[2] = phone;
			params[3] = description;
			params[4] = dictionaryId;
			params[5] = photo;
			params[6] = id;
			if(dbhelper.update(sql.toString(),params)){
				return SUCCESS;
			}
		}else{
			sql.append("insert into bp_person_tbl(id,name,age,phone,description,communityId,dictionaryId,photo,add_date) ");
			sql.append("VALUES(?,?,?,?,?,?,?,?,now()) ");
			Object[] params = new Object[8];
			params[0] = UUID.randomUUID().toString().replaceAll("-", "");
			params[1] = name;
			params[2] = age;
			params[3] = phone;
			params[4] = description;
			params[5] = communityId;
			params[6] = dictionaryId;
			params[7] = photo;
			if(dbhelper.insert(sql.toString(),params)){
				return SUCCESS;
			}
		}
		return "failure";
	}

	@SuppressWarnings("unchecked")
	private void updatePhoto(){
		String sql = "select photo from bp_person_tbl where id='"+id+"' ";
		List<Map<String,String>> dataList = (List<Map<String, String>>) dbhelper.getMapListBySql(sql);
		if(dataList.size()>0){
			Map<String,String> data = dataList.get(0);
			String oldPhoto = null==data.get("photo")?"":data.get("photo").toString();
			if(StringUtils.isNotBlank(oldPhoto) && !oldPhoto.equals(photo)){
//				String str = SavePerson.class.getClassLoader().getResource("").getPath();
//				String localPath = str.substring(1, str.indexOf("WEB-INF"));
				String localPath = GlobalVar.TOOLSPATH+"/";
				String photoAbsolutePath = localPath+oldPhoto;
				File photoFile = new File(photoAbsolutePath);
				if(photoFile.exists()){
					photoFile.delete();
				}
			}
		}
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

	public String getDictionaryId() {
		return dictionaryId;
	}

	public void setDictionaryId(String dictionaryId) {
		this.dictionaryId = dictionaryId;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
