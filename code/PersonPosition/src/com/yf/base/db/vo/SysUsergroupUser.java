package com.yf.base.db.vo;

/**
 * SysUsergroupUser entity. @author MyEclipse Persistence Tools
 */

public class SysUsergroupUser implements java.io.Serializable {

	// Fields

	private String suuid;
	private SysUsergroup sysUsergroup;
	private SysUser sysUser;

	// Constructors

	/** default constructor */
	public SysUsergroupUser() {
	}

	/** full constructor */
	public SysUsergroupUser(SysUsergroup sysUsergroup, SysUser sysUser) {
		this.sysUsergroup = sysUsergroup;
		this.sysUser = sysUser;
	}

	// Property accessors

	public String getSuuid() {
		return this.suuid;
	}

	public void setSuuid(String suuid) {
		this.suuid = suuid;
	}

	public SysUsergroup getSysUsergroup() {
		return this.sysUsergroup;
	}

	public void setSysUsergroup(SysUsergroup sysUsergroup) {
		this.sysUsergroup = sysUsergroup;
	}

	public SysUser getSysUser() {
		return this.sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

}
