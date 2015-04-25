package com.yf.remote.service;

import java.util.Date;

public class DataBaseDTO implements java.io.Serializable{
	private String db_id;
	private String d_no;
	private String d_type;
	private String c_id;
	private String c_type;
	private String province;
	private String city;
	private String area;
	private Date create_time;
	private Double current;
	private Double voltage;
	private Double power;
	private String remark;
	
	
	public String getDb_id() {
		return db_id;
	}
	public void setDb_id(String dbId) {
		db_id = dbId;
	}
	public String getD_no() {
		return d_no;
	}
	public void setD_no(String dNo) {
		d_no = dNo;
	}
	public String getD_type() {
		return d_type;
	}
	public void setD_type(String dType) {
		d_type = dType;
	}
	public String getC_id() {
		return c_id;
	}
	public void setC_id(String cId) {
		c_id = cId;
	}
	public String getC_type() {
		return c_type;
	}
	public void setC_type(String cType) {
		c_type = cType;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date createTime) {
		create_time = createTime;
	}
	public Double getCurrent() {
		return current;
	}
	public void setCurrent(Double current) {
		this.current = current;
	}
	public Double getVoltage() {
		return voltage;
	}
	public void setVoltage(Double voltage) {
		this.voltage = voltage;
	}
	public Double getPower() {
		return power;
	}
	public void setPower(Double power) {
		this.power = power;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

}
