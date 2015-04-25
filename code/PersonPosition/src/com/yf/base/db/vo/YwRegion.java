package com.yf.base.db.vo;

import java.util.Date;
import java.util.List;
import java.util.*;

/**
 * YwRegion entity. @author MyEclipse Persistence Tools
 */

public class YwRegion implements java.io.Serializable {

	// Fields

	private String rid;
	private YwRegion ywRegion;
	private String name;
	private Boolean deleted;
	private Date deletedDate;
	private String code;
	private List ywRegions = new ArrayList(0);
	private List ywCompanys = new ArrayList(0);

	// Constructors

	/** default constructor */
	public YwRegion() {
	}

	/** full constructor */
	public YwRegion(YwRegion ywRegion, String name, Boolean deleted,
			Date deletedDate, String code, List ywRegions, List ywCompanys) {
		this.ywRegion = ywRegion;
		this.name = name;
		this.deleted = deleted;
		this.deletedDate = deletedDate;
		this.code = code;
		this.ywRegions = ywRegions;
		this.ywCompanys = ywCompanys;
	}

	// Property accessors

	public String getRid() {
		return this.rid;
	}

	public void setRid(String rid) {
		this.rid = rid;
	}

	public YwRegion getYwRegion() {
		return this.ywRegion;
	}

	public void setYwRegion(YwRegion ywRegion) {
		this.ywRegion = ywRegion;
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

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List getYwRegions() {
		return this.ywRegions;
	}

	public void setYwRegions(List ywRegions) {
		this.ywRegions = ywRegions;
	}

	public List getYwCompanys() {
		return this.ywCompanys;
	}

	public void setYwCompanys(List ywCompanys) {
		this.ywCompanys = ywCompanys;
	}


}
