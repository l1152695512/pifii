package com.yf.base.actions.sys.dictionary;

import net.sf.json.JSONObject;

import com.yf.base.db.vo.SysDictionary;
import com.yf.base.service.SysDictionaryService;
import com.yf.ext.base.BaseAction;
import com.yf.util.JsonUtils;

public class EditFormData extends BaseAction {


	private SysDictionaryService sysDictionaryService;

	private String dicId;

	private String jsonString;

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}

	@Override
	public String execute() throws Exception {
		SysDictionary sysDictionary = this.sysDictionaryService.findById(this.dicId);

		if (sysDictionary == null){
			this.msg="该数据字典不存在!";
			return "formData";
		}
		

		JSONObject record = JsonUtils.getJsonHelper().excludeForeignObject(
				SysDictionary.class).setAllBooleanToString()

		.toJSONObject(sysDictionary);

		jsonString = record.toString();
		return "formData";
	}

	public String getDicId() {
		return dicId;
	}

	public void setDicId(String dicId) {
		this.dicId = dicId;
	}

	public SysDictionaryService getSysDictionaryService() {
		return sysDictionaryService;
	}

	public void setSysDictionaryService(SysDictionaryService sysDictionaryService) {
		this.sysDictionaryService = sysDictionaryService;
	}



}
