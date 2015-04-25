package com.yf.base.actions;

import java.io.InputStream;
import java.io.StringBufferInputStream;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;

import com.opensymphony.xwork2.ActionSupport;
/**
 * 实用价值不大，虽然不用写结果jsp。
 * @author Administrator
 *
 */
public class TextResult extends ActionSupport {
	private InputStream inputStream;

	public InputStream getInputStream() {
		return inputStream;
	}

	@Action(results = { @Result(type = "stream", params = { "inputName",
			"inputStream", "contentType", "text/html", "allowCaching", "false",
			"contentCharSet", "charset=UTF-8" }) })
	public String execute() throws Exception {
		inputStream = new StringBufferInputStream(
				"Hello World! This is a text string response from a Struts 2 Action.");
		return SUCCESS;
	}
}
