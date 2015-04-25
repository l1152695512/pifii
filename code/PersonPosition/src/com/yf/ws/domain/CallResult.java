package com.yf.ws.domain;

public class CallResult {
	private int returnCode;
	private String returnMessage;

	public CallResult() {
	}
	
	public CallResult(int returnCode, String returnMessage) {
		this.returnCode = returnCode;
		this.returnMessage = returnMessage;
	}


	public int getReturnCode() {
		return returnCode;
	}
	

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMessage() {
		return returnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

}
