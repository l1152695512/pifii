package com.yf.base.db.vo;

import java.util.*;

/**
 * SysUsergroup entity. @author MyEclipse Persistence Tools
 */

public class SysUsergroup implements java.io.Serializable {

	// Fields

	private String sugid;
	private YwCompany ywCompany;
	private Date createTime;
	private String name;
	private String remards;
	private Boolean deleted;
	private Date deletedDate;
	private List sysUsergroupUsers = new ArrayList(0);
	private List sysUsergroupMenuss = new ArrayList(0);
	private List sysUsergroupActions = new ArrayList(0);

	// Constructors

	/** default constructor */
	public SysUsergroup() {
	}

	/** full constructor */
	public SysUsergroup(YwCompany ywCompany, Date createTime, String name,
			String remards, Boolean deleted, Date deletedDate,
			List sysUsergroupUsers, List sysUsergroupMenuss,List sysUsergroupActions) {
		this.ywCompany = ywCompany;
		this.createTime = createTime;
		this.name = name;
		this.remards = remards;
		this.deleted = deleted;
		this.deletedDate = deletedDate;
		this.sysUsergroupUsers = sysUsergroupUsers;
		this.sysUsergroupMenuss = sysUsergroupMenuss;
		this.sysUsergroupActions = sysUsergroupActions;
	}

	// Property accessors

	public String getSugid() {
		return this.sugid;
	}

	public void setSugid(String sugid) {
		this.sugid = sugid;
	}

	public YwCompany getYwCompany() {
		return this.ywCompany;
	}

	public void setYwCompany(YwCompany ywCompany) {
		this.ywCompany = ywCompany;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemards() {
		return this.remards;
	}

	public void setRemards(String remards) {
		this.remards = remards;
	}

	public Boolean getDeleted() {
		return this.deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Date getDeletedDate() {
		return this.deletedDate;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}

	public List getSysUsergroupUsers() {
		return this.sysUsergroupUsers;
	}

	public void setSysUsergroupUsers(List sysUsergroupUsers) {
		this.sysUsergroupUsers = sysUsergroupUsers;
	}

	public List getSysUsergroupMenuss() {
		return this.sysUsergroupMenuss;
	}

	public void setSysUsergroupMenuss(List sysUsergroupMenuss) {
		this.sysUsergroupMenuss = sysUsergroupMenuss;
	}

	public List getSysUsergroupActions() {
		return this.sysUsergroupActions;
	}

	public void setSysUsergroupActions(List sysUsergroupActions) {
		this.sysUsergroupActions = sysUsergroupActions;
	}

}
