package com.yf.base.db.vo;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * SysDictionary entity. @author MyEclipse Persistence Tools
 */

public class SysDictionary implements java.io.Serializable {

	// Fields

	private String dicId;
	private String keyName;
	private String pinyinCode;
	private String typeName;
	private String keyValue;
	private String remark;
	private Boolean authority;
	private SysDictionary sysDictionary;
	private Boolean deleted;
	private Date deletedDate;
	private String ghtype;
	private List sysDictionarys = new ArrayList(0);
	
	
	public SysDictionary() {
		super();
	}

	public SysDictionary(String dicId, String keyName, String pinyinCode,
			String typeName, String keyValue, String remark, Boolean authority,
			SysDictionary sysDictionary, Boolean deleted,
			Date deletedDate, String ghtype, List sysDictionarys) {
		super();
		this.dicId = dicId;
		this.keyName = keyName;
		this.pinyinCode = pinyinCode;
		this.typeName = typeName;
		this.keyValue = keyValue;
		this.remark = remark;
		this.authority = authority;
		this.sysDictionary = sysDictionary;
		this.deleted = deleted;
		this.deletedDate = deletedDate;
		this.ghtype = ghtype;
		this.sysDictionarys = sysDictionarys;
	}

	public String getDicId() {
		return dicId;
	}

	public void setDicId(String dicId) {
		this.dicId = dicId;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getPinyinCode() {
		return pinyinCode;
	}

	public void setPinyinCode(String pinyinCode) {
		this.pinyinCode = pinyinCode;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getAuthority() {
		return authority;
	}

	public void setAuthority(Boolean authority) {
		this.authority = authority;
	}

	public SysDictionary getSysDictionary() {
		return sysDictionary;
	}

	public void setSysDictionary(SysDictionary sysDictionary) {
		this.sysDictionary = sysDictionary;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Date getDeletedDate() {
		return deletedDate;
	}

	public void setDeletedDate(Date deletedDate) {
		this.deletedDate = deletedDate;
	}

	public String getGhtype() {
		return ghtype;
	}

	public void setGhtype(String ghtype) {
		this.ghtype = ghtype;
	}

	public List getSysDictionarys() {
		return sysDictionarys;
	}

	public void setSysDictionarys(List sysDictionarys) {
		this.sysDictionarys = sysDictionarys;
	}

}
