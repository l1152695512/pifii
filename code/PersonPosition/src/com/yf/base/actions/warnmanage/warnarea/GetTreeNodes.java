package com.yf.base.actions.warnmanage.warnarea;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sf.json.JSONArray;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.actions.commons.CommunityUtils;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class GetTreeNodes extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String node;
	
	@Override
	public String execute() throws Exception {
		JSONArray jsonArray = null;
		if("0".equals(node)){
			jsonArray = getCommunities();
		}else{
			jsonArray = getWarnArea();
		}
		this.jsonString = jsonArray.toString();
		return "data";
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray getCommunities(){
		List<?> dataList = CommunityUtils.getCommunityComboWidthoutFloor("id,name,map");
		Iterator<?> ite = dataList.iterator();
		JSONArray jsonArray = new JSONArray();
		while(ite.hasNext()){
			Map<String,Object> rowData = (Map<String,Object>)ite.next();
			WarnAreaTreeNode jsonTreeNode = new WarnAreaTreeNode();
			jsonTreeNode.setId(null==rowData.get("id")?"":rowData.get("id").toString());
			jsonTreeNode.setText(null==rowData.get("name")?"":rowData.get("name").toString());
			String map = null==rowData.get("map")?"":rowData.get("map").toString();
			jsonTreeNode.setMap(map);
			
//			String path = GetTreeNodes.class.getClassLoader().getResource("").getPath();
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
			jsonTreeNode.setLeaf(false);
			jsonTreeNode.setIconCls("");//文件图标css
			jsonArray.add(jsonTreeNode);
		}
		return jsonArray;
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray getWarnArea(){
		String sql = "select id,name from bp_fine_area_tbl where community_id = '"+node+"' order by add_date ";
		List<?> dataList = dbhelper.getMapListBySql(sql);
		Iterator<?> ite = dataList.iterator();
		JSONArray jsonArray = new JSONArray();
		while(ite.hasNext()){
			Map<String,Object> rowData = (Map<String,Object>)ite.next();
			WarnAreaTreeNode jsonTreeNode = new WarnAreaTreeNode();
			jsonTreeNode.setId(null==rowData.get("id")?"":rowData.get("id").toString());
			jsonTreeNode.setText(null==rowData.get("name")?"":rowData.get("name").toString());
			jsonTreeNode.setLeaf(true);
			jsonTreeNode.setIconCls("");//文件图标css
			jsonArray.add(jsonTreeNode);
		}
		return jsonArray;
	}
	
	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	
	public String getNode() {
		return node;
	}
	
	public void setNode(String node) {
		this.node = node;
	}
}
