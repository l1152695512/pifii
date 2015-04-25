package com.yf.base.db.vo;

import java.util.Date;
import java.util.List;
import java.util.*;

/**
 * SysUser entity. @author MyEclipse Persistence Tools
 */

public class SysUser implements java.io.Serializable {

	// Fields

	private String userId;
	private YwCompany ywCompany;
	private String accountName;
	private String serialNum;
	private Boolean disabled;
	private String password;
	private String pinyinCode;
	private String userName;
	private String telNum;
	private String cellPhone;
	private String email;
	private String description;
	private Date lastLogin;
	private Date createTime;
	private Date updateTime;
	private Integer userPriority;
	private Boolean deleted;
	private Date deletedDate;
	private Integer loginCount;
	private List sysUsergroupUsers = new ArrayList(0);

	// Constructors

	/** default constructor */
	public SysUser() {
	}

	/** minimal constructor */
	public SysUser(String accountName, String password) {
		this.accountName = accountName;
		this.password = password;
	}

	/** full constructor */
	public SysUser(YwCompany ywCompany, String accountName, String serialNum,
			Boolean disabled, String password, String pinyinCode,
			String userName, String telNum, String cellPhone, String email,
			String description, Date lastLogin, Date createTime,
			Date updateTime, Integer userPriority, Boolean deleted,
			Date deletedDate, Integer loginCount, List sysUsergroupUsers) {
		this.ywCompany = ywCompany;
		this.accountName = accountName;
		this.serialNum = serialNum;
		this.disabled = disabled;
		this.password = password;
		this.pinyinCode = pinyinCode;
		this.userName = userName;
		this.telNum = telNum;
		this.cellPhone = cellPhone;
		this.email = email;
		this.description = description;
		this.lastLogin = lastLogin;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.userPriority = userPriority;
		this.deleted = deleted;
		this.deletedDate = deletedDate;
		this.loginCount = loginCount;
		this.sysUsergroupUsers = sysUsergroupUsers;
	}

	// Property accessors

	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public YwCompany getYwCompany() {
		return this.ywCompany;
	}

	public void setYwCompany(YwCompany ywCompany) {
		this.ywCompany = ywCompany;
	}

	public String getAccountName() {
		return this.accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getSerialNum() {
		return this.serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}

	public Boolean getDisabled() {
		return this.disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPinyinCode() {
		return this.pinyinCode;
	}

	public void setPinyinCode(String pinyinCode) {
		this.pinyinCode = pinyinCode;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTelNum() {
		return this.telNum;
	}

	public void setTelNum(String telNum) {
		this.telNum = telNum;
	}

	public String getCellPhone() {
		return this.cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getLastLogin() {
		return this.lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getUserPriority() {
		return this.userPriority;
	}

	public void setUserPriority(Integer userPriority) {
		this.userPriority = userPriority;
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

	public Integer getLoginCount() {
		return this.loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	public List getSysUsergroupUsers() {
		return this.sysUsergroupUsers;
	}

	public void setSysUsergroupUsers(List sysUsergroupUsers) {
		this.sysUsergroupUsers = sysUsergroupUsers;
	}

}
