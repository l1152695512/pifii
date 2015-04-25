package com.yf.tradecontrol;


public class TradeBean {

	private String tradeCode; 
	private String xscriptID; //与后置的交易码
	private String description; //描述
	private ReflctBean authMethod;
	private ReflctBean acsMethod;
	public String getTradeCode() {
		return tradeCode;
	}
	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ReflctBean getAuthMethod() {
		return authMethod;
	}
	public void setAuthMethod(ReflctBean authMethod) {
		this.authMethod = authMethod;
	}
	public ReflctBean getAcsMethod() {
		return acsMethod;
	}
	public void setAcsMethod(ReflctBean acsMethod) {
		this.acsMethod = acsMethod;
	}
	public String getXscriptID() {
		return xscriptID;
	}
	public void setXscriptID(String xscriptID) {
		this.xscriptID = xscriptID;
	}
	
}
