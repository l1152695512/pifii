package com.yf.base.actions.patrolmanageold.routeplan;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.sf.json.JSONArray;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class GetTreeNodes extends ActionSupport{
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	private String jsonString;
	private String nodeId;
	
	@Override
	public String execute() throws Exception {
		JSONArray jsonArray = null;
		if("0".equals(nodeId)){
			jsonArray = getCommunities();
		}else{
			jsonArray = getRoutes();
		}
		this.jsonString = jsonArray.toString();
		return "data";
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray getCommunities(){
		String sql = "select id,name,map from bp_community_tbl";
		List<?> dataList = dbhelper.getMapListBySql(sql);
		Iterator<?> ite = dataList.iterator();
		JSONArray jsonArray = new JSONArray();
		while(ite.hasNext()){
			Map<String,Object> rowData = (Map<String,Object>)ite.next();
			RouteTreeNode jsonTreeNode = new RouteTreeNode();
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
	private JSONArray getRoutes(){
		String sql = "select id,name from bp_fine_route_tbl where community_id = '"+nodeId+"' order by add_date ";
		List<?> dataList = dbhelper.getMapListBySql(sql);
		Iterator<?> ite = dataList.iterator();
		JSONArray jsonArray = new JSONArray();
		while(ite.hasNext()){
			Map<String,Object> rowData = (Map<String,Object>)ite.next();
			RouteTreeNode jsonTreeNode = new RouteTreeNode();
			jsonTreeNode.setId(null==rowData.get("id")?"":rowData.get("id").toString());
			jsonTreeNode.setText(null==rowData.get("name")?"":rowData.get("name").toString());
//			String map = null==rowData.get("map")?"":rowData.get("map").toString();
//			jsonTreeNode.setMap(map);
			
//			String path = GetTreeNodes.class.getClassLoader().getResource("").getPath();
//			String mapPath = path.substring(1, path.indexOf("WEB-INF"))+map;
////			String mapPath = GlobalVar.WORKPATH.replaceAll("WEB-INF", map.substring(0, map.indexOf("/"))) + File.separator + map.substring(map.indexOf("/")+1);
//			File file = new File(mapPath);
//			BufferedImage bi = null;
//			try {
//				bi = ImageIO.read(file);
//				jsonTreeNode.setMapWidthPixel(bi.getWidth()+""); // 像素
//				jsonTreeNode.setMapHeightPixel(bi.getHeight()+""); // 像素
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
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
	
	public String getNodeId() {
		return nodeId;
	}
	
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	
}
