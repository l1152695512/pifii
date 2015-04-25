package com.yf.base.db.vo;

/**
 * SysUsergroupAction entity. @author MyEclipse Persistence Tools
 */

public class SysUsergroupAction implements java.io.Serializable {

	// Fields

	private String sugiad;
	private SysUsergroup sysUsergroup;
	private SysAction sysAction;

	// Constructors

	/** default constructor */
	public SysUsergroupAction() {
	}

	/** full constructor */
	public SysUsergroupAction(SysUsergroup sysUsergroup, SysAction sysAction) {
		this.sysUsergroup = sysUsergroup;
		this.sysAction = sysAction;
	}

	// Property accessors

	public String getSugiad() {
		return this.sugiad;
	}

	public void setSugiad(String sugiad) {
		this.sugiad = sugiad;
	}

	public SysUsergroup getSysUsergroup() {
		return this.sysUsergroup;
	}

	public void setSysUsergroup(SysUsergroup sysUsergroup) {
		this.sysUsergroup = sysUsergroup;
	}

	public SysAction getSysAction() {
		return this.sysAction;
	}

	public void setSysAction(SysAction sysAction) {
		this.sysAction = sysAction;
	}

}
