package com.yf.base.actions.warnmanage.warnarea;

public class WarnAreaTreeNode {
	private String id;            //ID
	private String text;          //节点显示
	private String iconCls;           //图标
	private boolean leaf;         //是否叶子
	private String nodeType = "async";
//    private String href;          //链接
//    private String hrefTarget;    //链接指向
    private String map;
    private String mapWidthPixel;
	private String mapHeightPixel;
    
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public boolean isLeaf() {
		return leaf;
	}
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
//	public String getHref() {
//		return href;
//	}
//	public void setHref(String href) {
//		this.href = href;
//	}
//	public String getHrefTarget() {
//		return hrefTarget;
//	}
//	public void setHrefTarget(String hrefTarget) {
//		this.hrefTarget = hrefTarget;
//	}
	public String getMap() {
		return map;
	}
	
	public void setMap(String map) {
		this.map = map;
	}
	public void setMapHeightPixel(String mapHeightPixel) {
		this.mapHeightPixel = mapHeightPixel;
	}
	public String getMapHeightPixel() {
		return mapHeightPixel;
	}
	public String getMapWidthPixel() {
		return mapWidthPixel;
	}
	public void setMapWidthPixel(String mapWidthPixel) {
		this.mapWidthPixel = mapWidthPixel;
	}
}
