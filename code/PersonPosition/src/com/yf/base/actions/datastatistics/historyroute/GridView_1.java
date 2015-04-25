package com.yf.base.actions.datastatistics.historyroute;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class GridView_1 extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String msg;
	
	private String personId;
	private String communityId;
	private String map;
	private String mapWidthPixel;
	private String mapHeightPixel;
	private String photo;
	private String type;
	private String name;
	private String age;
	private String phone;
	private String description;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
//		String sql = "select name,map,DATE_FORMAT(createDate,'%Y-%m-%d %H:%i:%s') createDate from bp_community_tbl where id = '"+id+"' ";
		StringBuffer sql = new StringBuffer();
		sql.append("select p.communityId,p.photo,p.name,p.age,p.phone,p.description,c.map,d.KEY_VALUE ");
		sql.append("from bp_person_tbl p join bp_community_tbl c on (p.communityId = c.id) ");
		sql.append("join sys_dictionary_tbl d on (p.dictionaryId = d.DIC_ID) ");
		sql.append("where p.id = '");
		sql.append(personId);
		sql.append("'");
		List<?> dataList = dbhelper.getMapListBySql(sql.toString());
		if(dataList.size()>0){
			Map<String,Object> data = (Map<String,Object>)dataList.get(0);
			communityId = null!=data.get("communityId")?data.get("communityId").toString():"";
			map = null!=data.get("map")?data.get("map").toString():"";
//			String path = GridView_1.class.getClassLoader().getResource("").getPath();
//			String mapPath = path.substring(1, path.indexOf("WEB-INF"))+map;
			String mapPath = GlobalVar.TOOLSPATH+"/"+map;
//			String mapPath = GlobalVar.WORKPATH.replaceAll("WEB-INF", map.substring(0, map.indexOf("/"))) + File.separator + map.substring(map.indexOf("/")+1);
			File file = new File(mapPath);
			if(file.exists()){
				try {
					BufferedImage bi = ImageIO.read(file);
					mapWidthPixel = bi.getWidth()+""; // 像素
					mapHeightPixel = bi.getHeight()+""; // 像素
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			photo = null!=data.get("photo")?data.get("photo").toString():"";
			type = null!=data.get("KEY_VALUE")?data.get("KEY_VALUE").toString():"";
			name = null!=data.get("name")?data.get("name").toString():"";
			age = null!=data.get("age")?data.get("age").toString():"";
			phone = null!=data.get("phone")?data.get("phone").toString():"";
			description = null!=data.get("description")?data.get("description").toString():"";
			return super.execute();
		}else{
			this.msg = "该人员不存在！";
		}
		return "failure";
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public String getMapHeightPixel() {
		return mapHeightPixel;
	}
	
	public void setMapHeightPixel(String mapHeightPixel) {
		this.mapHeightPixel = mapHeightPixel;
	}
	
	public String getMapWidthPixel() {
		return mapWidthPixel;
	}
	
	public void setMapWidthPixel(String mapWidthPixel) {
		this.mapWidthPixel = mapWidthPixel;
	}
	
	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	
}
