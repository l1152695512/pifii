package com.yf.base.db.vo;

import java.util.Date;
import java.util.List;
import java.util.*;

/**
 * SysAction entity. @author MyEclipse Persistence Tools
 */

public class SysAction implements java.io.Serializable {

	// Fields

	private String said;
	private Date createTime;
	private String remards;
	private String name;
	private Boolean deleted;
	private Date deletedDate;
	private List sysUsergroupActions = new ArrayList(0);
	private List sysActionItems = new ArrayList(0);

	// Constructors

	/** default constructor */
	public SysAction() {
	}

	/** full constructor */
	public SysAction(Date createTime, String remards, String name,
			Boolean deleted, Date deletedDate, List sysUsergroupActions,
			List sysActionItems) {
		this.createTime = createTime;
		this.remards = remards;
		this.name = name;
		this.deleted = deleted;
		this.deletedDate = deletedDate;
		this.sysUsergroupActions = sysUsergroupActions;
		this.sysActionItems = sysActionItems;
	}

	// Property accessors

	public String getSaid() {
		return this.said;
	}

	public void setSaid(String said) {
		this.said = said;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemards() {
		return this.remards;
	}

	public void setRemards(String remards) {
		this.remards = remards;
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

	public List getSysUsergroupActions() {
		return this.sysUsergroupActions;
	}

	public void setSysUsergroupActions(List sysUsergroupActions) {
		this.sysUsergroupActions = sysUsergroupActions;
	}

	public List getSysActionItems() {
		return this.sysActionItems;
	}

	public void setSysActionItems(List sysActionItems) {
		this.sysActionItems = sysActionItems;
	}

}
