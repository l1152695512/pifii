package com.yf.util.dbhelper;

public class DBResourceBean {

	private String name;
	private String type;
	private String driver;
	private String url;
	private String username;
	private String password;
	private String maxconn;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMaxconn() {
		return maxconn;
	}

	public void setMaxconn(String maxconn) {
		this.maxconn = maxconn;
	}

}
