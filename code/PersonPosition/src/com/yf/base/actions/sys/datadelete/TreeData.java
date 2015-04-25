package com.yf.base.actions.sys.datadelete;

import java.util.HashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;

public class TreeData extends ActionSupport {


	private String jsonString;
	public String getJsonString() {
		return jsonString;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	@Override
	public String execute() throws Exception {

		this.jsonString =buildTree(Constants.getHashMap()).toString();	
		return "data";
	}
	
	private JSONArray buildTree(HashMap map){
		JSONArray array = new JSONArray();	
		int size=map.size();
		for(int i=0;i<size;i++){
			JSONObject object = new JSONObject();
			object.put("leaf", true);
			object.put("checked", false);
			object.put("expanded", true);
			object.put("text", map.get(i));
			object.put("id", i);	
			array.add(object);
		}
		return array;
	}



	
}
