package com.yf.base.actions.devicemanage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sf.json.JSONArray;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class GetAllCommunity extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private static DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	
	@SuppressWarnings("unchecked")
	@Override
	public String execute() throws Exception {
		String userId = (String) ActionContext.getContext().getSession().get("loginUserId");
		StringBuffer sql = new StringBuffer();
		sql.append("select id,name,map from bp_community_tbl where 1=1 ");
		if(!(Boolean) ActionContext.getContext().getSession().get("isAdmin")){
			sql.append("and user_id = '"+userId+"' ");
		}
		sql.append("order by name ");
		List<?> dataList = dbhelper.getMapListBySql(sql.toString());
		Iterator<?> ite = dataList.iterator();
		JSONArray jsonArray = new JSONArray();
		while(ite.hasNext()){
			Map<String,Object> rowData = (Map<String,Object>)ite.next();
			JSONTreeNode jsonTreeNode = new JSONTreeNode();
			jsonTreeNode.setId(null==rowData.get("id")?"":rowData.get("id").toString());
			jsonTreeNode.setText(null==rowData.get("name")?"":rowData.get("name").toString());
			String map = null==rowData.get("map")?"":rowData.get("map").toString();
			jsonTreeNode.setMap(map);
			
//			String path = GetAllCommunity.class.getClassLoader().getResource("").getPath();
//			String mapPath = path.substring(1, path.indexOf("WEB-INF"))+map;
			String mapPath = GlobalVar.TOOLSPATH+"/"+map;
//			String mapPath = GlobalVar.WORKPATH.replaceAll("WEB-INF", map.substring(0, map.indexOf("/"))) + File.separator + map.substring(map.indexOf("/")+1);
			File file = new File(mapPath);
			if(file.exists()){
				try {
					BufferedImage bi = ImageIO.read(file);
					jsonTreeNode.setMapWidthPixel(bi.getWidth()+""); // 像素
					jsonTreeNode.setMapHeightPixel(bi.getHeight()+""); // 像素
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			jsonTreeNode.setLeaf(true);
			jsonTreeNode.setIconCls("");//文件图标css
			jsonArray.add(jsonTreeNode);
		}
		this.jsonString = jsonArray.toString();
		return "data";
	}
	
	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
}
