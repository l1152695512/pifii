package com.yf.base.db.vo;

import java.util.Date;

/**
 * SysActionItem entity. @author MyEclipse Persistence Tools
 */

public class SysActionItem implements java.io.Serializable {

	// Fields

	private String saiid;
	private SysAction sysAction;
	private Date createTime;
	private String path;
	private String name;
	private Boolean deleted;
	private Date deletedDate;

	// Constructors

	/** default constructor */
	public SysActionItem() {
	}

	/** full constructor */
	public SysActionItem(SysAction sysAction, Date createTime,
			String path, String name, Boolean deleted, Date deletedDate) {
		this.sysAction = sysAction;
		this.createTime = createTime;
		this.path = path;
		this.name = name;
		this.deleted = deleted;
		this.deletedDate = deletedDate;
	}

	// Property accessors

	public String getSaiid() {
		return this.saiid;
	}

	public void setSaiid(String saiid) {
		this.saiid = saiid;
	}

	public SysAction getSysAction() {
		return this.sysAction;
	}

	public void setSysAction(SysAction sysAction) {
		this.sysAction = sysAction;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
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

}
