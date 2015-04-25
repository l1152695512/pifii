package com.yf.base.actions.commons;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.yf.base.service.YwCompanyService;
import com.yf.data.base.TransactionalCallBack;
import com.yf.ext.base.BaseAction;
import com.yf.service.IService;

public class TreeInitializationModel extends BaseAction {

	private String jsonString;
	public String getJsonString() {
		return jsonString;
	}
	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}
	@Override
	public String execute() throws Exception {
		Object result = executeDoing();
		this.jsonString = result.toString();		
		return "data";
	}
	public Object executeDoing (){
		Map<String,String> map = new HashMap<String,String>();
		map.put("专业维度数据", "1");
		map.put("机房维度数据", "2");
		map.put("能耗监测数据", "7");
		map.put("管理用房数据", "3");
		map.put("采购数据", "8");
		map.put("能耗预测数据", "9");
		map.put("电子渠道数据", "4");
		
		return buildTree(map);
	}
	
	private JSONArray buildTree(Map<String,String> map){
         
		JSONArray array = new JSONArray();	
		Set<String> set = map.keySet();
		for(Iterator<String> iter = set.iterator();iter.hasNext();)
		{
			String text = iter.next();
			JSONObject obj = new JSONObject();
			obj.put("text", text);
			obj.put("id", map.get(text));
			obj.put("leaf", true);
			array.add(obj);
		}
		return array;
	}
}
