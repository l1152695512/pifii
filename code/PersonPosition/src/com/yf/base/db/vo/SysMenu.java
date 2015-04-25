package com.yf.base.db.vo;

import java.util.List;
import java.util.*;

/**
 * SysMenu entity. @author MyEclipse Persistence Tools
 */

public class SysMenu implements java.io.Serializable {

	// Fields

	private String systemMenuId;
	private SysMenu sysMenu;
	private String menuText;
	private String iconClass;
	private String actionPath;
	private String extId;
	private String layoutAttr;
	private Boolean leaf;
	private Boolean expanded;
	private Short indexValue;
	private Boolean enabled;
	private List sysMenus = new ArrayList(0);
	private List sysUsergroupMenuss = new ArrayList(0);
	
	private Boolean isGrey;
	private Boolean shortcuts;
	// Constructors


	/** default constructor */
	public SysMenu() {
	}

	/** full constructor */
	public SysMenu(SysMenu sysMenu, String menuText, String iconClass,
			String actionPath, String extId, String layoutAttr, Boolean leaf,
			Boolean expanded, Short indexValue, Boolean enabled, List sysMenus,
			List sysUsergroupMenuss,Boolean isGrey,Boolean shortcuts) {
		this.sysMenu = sysMenu;
		this.menuText = menuText;
		this.iconClass = iconClass;
		this.actionPath = actionPath;
		this.extId = extId;
		this.layoutAttr = layoutAttr;
		this.leaf = leaf;
		this.expanded = expanded;
		this.indexValue = indexValue;
		this.enabled = enabled;
		this.sysMenus = sysMenus;
		this.sysUsergroupMenuss = sysUsergroupMenuss;
		this.isGrey = isGrey;
		this.shortcuts = shortcuts;
	}

	// Property accessors

	public String getSystemMenuId() {
		return this.systemMenuId;
	}

	public void setSystemMenuId(String systemMenuId) {
		this.systemMenuId = systemMenuId;
	}

	public SysMenu getSysMenu() {
		return this.sysMenu;
	}

	public void setSysMenu(SysMenu sysMenu) {
		this.sysMenu = sysMenu;
	}

	public String getMenuText() {
		return this.menuText;
	}

	public void setMenuText(String menuText) {
		this.menuText = menuText;
	}

	public String getIconClass() {
		return this.iconClass;
	}

	public void setIconClass(String iconClass) {
		this.iconClass = iconClass;
	}

	public String getActionPath() {
		return this.actionPath;
	}

	public void setActionPath(String actionPath) {
		this.actionPath = actionPath;
	}

	public String getExtId() {
		return this.extId;
	}

	public void setExtId(String extId) {
		this.extId = extId;
	}

	public String getLayoutAttr() {
		return this.layoutAttr;
	}

	public void setLayoutAttr(String layoutAttr) {
		this.layoutAttr = layoutAttr;
	}

	public Boolean getLeaf() {
		return this.leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	public Boolean getExpanded() {
		return this.expanded;
	}

	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}

	public Short getIndexValue() {
		return this.indexValue;
	}

	public void setIndexValue(Short indexValue) {
		this.indexValue = indexValue;
	}

	public Boolean getEnabled() {
		return this.enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List getSysMenus() {
		return this.sysMenus;
	}

	public void setSysMenus(List sysMenus) {
		this.sysMenus = sysMenus;
	}

	public List getSysUsergroupMenuss() {
		return this.sysUsergroupMenuss;
	}

	public void setSysUsergroupMenuss(List sysUsergroupMenuss) {
		this.sysUsergroupMenuss = sysUsergroupMenuss;
	}

	public Boolean getIsGrey()
	{
		return isGrey;
	}

	public void setIsGrey(Boolean isGrey)
	{
		this.isGrey = isGrey;
	}
	
	public Boolean getShortcuts() {
		return shortcuts;
	}

	public void setShortcuts(Boolean shortcuts) {
		this.shortcuts = shortcuts;
	}

}
