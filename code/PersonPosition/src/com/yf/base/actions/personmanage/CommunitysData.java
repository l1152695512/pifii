package com.yf.base.actions.personmanage;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.base.actions.commons.CommunityUtils;
import com.yf.util.JsonUtils;

public class CommunitysData extends ActionSupport {
	private static final long serialVersionUID = 1L;
	private String jsonString;
	
	@Override
	public String execute() throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("values", CommunityUtils.getCommunityComboWidthFloor("id,name").toArray());
		String result=JsonUtils.map2json(resultMap);
		this.jsonString = result;
		return "data";
	}

	public String getJsonString() {
		return jsonString;
	}
	
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
}
