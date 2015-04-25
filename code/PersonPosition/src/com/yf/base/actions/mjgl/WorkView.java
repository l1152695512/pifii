package com.yf.base.actions.mjgl;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class WorkView extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String id;
	private String map;
	private String mapWidthPixel;
	private String mapHeightPixel;
	private String name;
	private String createDate;
	private String msg;
	private String title;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		String sql = "select name,map,DATE_FORMAT(createDate,'%Y-%m-%d %H:%i:%s') createDate from bp_community_tbl where id = '"+id+"' ";
		List<?> dataList = dbhelper.getMapListBySql(sql);
		if(dataList.size()>0){
			Map<String,String> data = (Map<String,String>)dataList.get(0);
			map = data.get("map");
//			String mapPath = GlobalVar.WORKPATH.replaceAll("WEB-INF", map.substring(0, map.indexOf("/"))) + File.separator + map.substring(map.indexOf("/")+1);
//			String path = WorkView.class.getClassLoader().getResource("").getPath();
//			String mapPath = path.substring(1, path.indexOf("WEB-INF"))+map;
			String mapPath = GlobalVar.TOOLSPATH+"/"+map;
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
			name = data.get("name");
			createDate = data.get("createDate");
			return super.execute();
		}else{
			this.msg = "该小区不存在！";
		}
		return "failure";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
}
