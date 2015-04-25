package com.yf.base.actions.commons;

import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.util.Debug;
import com.yf.util.PinyinDictionary;

public class Pinyin extends ActionSupport {

	private String pinyinCode;
	private String jsonString;

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	public String getPinyinCode() {
		return pinyinCode;
	}

	public void setPinyinCode(String pinyinCode) {
		this.pinyinCode = pinyinCode;
	}

	@Override
	public String execute() throws Exception {
		
	

		if(this.pinyinCode!=null&&!this.pinyinCode.equals("")){
			List<String> l = PinyinDictionary.translate2ShouPin(this.pinyinCode);
			JSONArray r =new JSONArray();
			JSONObject  o=null;
			if(l!=null){	
				for(int i=0;i<l.size();i++){
					o=new JSONObject();
					o.put("text", l.get(i).toString());
					r.add(o);
				}
			}
			
			this.jsonString = r.toString();
			
		

			
		}
		Debug.println("r=222=="+this.jsonString);
	
	
		return super.execute();
	}

}
