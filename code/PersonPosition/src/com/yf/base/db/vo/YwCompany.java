package com.yf.base.db.vo;

import java.util.Date;
import java.util.List;
import java.util.*;

/**
 * YwCompany entity. @author MyEclipse Persistence Tools
 */

public class YwCompany implements java.io.Serializable {

	// Fields

	private String cid;
	private YwRegion ywRegion;
	private String cname;
	private String category;
	private Boolean deleted;
	private Date deletedDate;
	private Long fixedAssets;
	private List sysUsers = new ArrayList(0);
	private List sysUsergroups = new ArrayList(0);
	private String deptNo;

	// Constructors

	/** default constructor */
	public YwCompany() {
	}

	/** full constructor */
	public YwCompany(YwRegion ywRegion, String cname, String category,
			Boolean deleted, Date deletedDate, Long fixedAssets,
			List sysUsers,List sysUsergroups,String deptNo) {
		this.ywRegion = ywRegion;
		this.cname = cname;
		this.category = category;
		this.deleted = deleted;
		this.deletedDate = deletedDate;
		this.fixedAssets = fixedAssets;
		this.sysUsers = sysUsers;
		this.sysUsergroups = sysUsergroups;
		this.deptNo = deptNo;
	}

	// Property accessors

	public String getDeptNo() {
		return deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	public String getCid() {
		return this.cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public YwRegion getYwRegion() {
		return this.ywRegion;
	}

	public void setYwRegion(YwRegion ywRegion) {
		this.ywRegion = ywRegion;
	}

	public String getCname() {
		return this.cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
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

	public Long getFixedAssets() {
		return this.fixedAssets;
	}

	public void setFixedAssets(Long fixedAssets) {
		this.fixedAssets = fixedAssets;
	}

	public List getSysUsers() {
		return this.sysUsers;
	}

	public void setSysUsers(List sysUsers) {
		this.sysUsers = sysUsers;
	}

	public List getSysUsergroups() {
		return this.sysUsergroups;
	}

	public void setSysUsergroups(List sysUsergroups) {
		this.sysUsergroups = sysUsergroups;
	}

}
