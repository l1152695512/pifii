package com.yf.util.dbhelper;

public class ProcdureParmeter {
	public static final int IN=1;
	public static final int OUT=2;
	public static final int IO=3;
	private int index;
	private int type;
	private Object value;
	private Class outType;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Class getOutType() {
		return outType;
	}
	public void setOutType(Class outType) {
		this.outType = outType;
	}
	
	
}
