package com.yf.base.actions.datastatistics.historyroute;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.JsonUtils;
import com.yf.util.dbhelper.DBHelper;

public class GetMaps extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		String sql = "select id,map from bp_community_tbl";
		List<Map<String,String>> dataList = (List<Map<String, String>>) dbhelper.getMapListBySql(sql);
		Iterator<Map<String,String>> ite = dataList.iterator();
		while(ite.hasNext()){
			Map<String,String> rowData = ite.next();
			String map = rowData.get("map");
//			String path = GetMaps.class.getClassLoader().getResource("").getPath();
//			String mapPath = path.substring(1, path.indexOf("WEB-INF"))+map;
			String mapPath = GlobalVar.TOOLSPATH+"/"+map;
			File file = new File(mapPath);
			if(file.exists()){
				try {
					BufferedImage bi = ImageIO.read(file);
					rowData.put("mapWidthPixel", bi.getWidth()+"");// 像素
					rowData.put("mapHeightPixel", bi.getHeight()+"");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("list", dataList.toArray());
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		return "data";
	}

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
}
