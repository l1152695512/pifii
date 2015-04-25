package com.yf.base.db.vo;

/**
 * SysUsergroupMenus entity. @author MyEclipse Persistence Tools
 */

public class SysUsergroupMenus implements java.io.Serializable {

	// Fields

	private String sumId;
	private SysUsergroup sysUsergroup;
	private SysMenu sysMenu;

	// Constructors

	/** default constructor */
	public SysUsergroupMenus() {
	}

	/** full constructor */
	public SysUsergroupMenus(SysUsergroup sysUsergroup, SysMenu sysMenu) {
		this.sysUsergroup = sysUsergroup;
		this.sysMenu = sysMenu;
	}

	// Property accessors

	public String getSumId() {
		return this.sumId;
	}

	public void setSumId(String sumId) {
		this.sumId = sumId;
	}

	public SysUsergroup getSysUsergroup() {
		return this.sysUsergroup;
	}

	public void setSysUsergroup(SysUsergroup sysUsergroup) {
		this.sysUsergroup = sysUsergroup;
	}

	public SysMenu getSysMenu() {
		return this.sysMenu;
	}

	public void setSysMenu(SysMenu sysMenu) {
		this.sysMenu = sysMenu;
	}

}
