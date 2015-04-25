package com.yf.base.actions.sys.dictionary;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import com.yf.base.db.vo.SysDictionary;
import com.yf.base.service.SysDictionaryService;
import com.yf.ext.base.BaseAction;

public class SaveForm extends   BaseAction {

	public SysDictionaryService getSysDictionaryService() {
		return sysDictionaryService;
	}

	public void setSysDictionaryService(SysDictionaryService sysDictionaryService) {
		this.sysDictionaryService = sysDictionaryService;
	}

	private SysDictionaryService sysDictionaryService;

	private String dicId;
	private String keyName;
	private String typeName;
	private String keyValue;
	private String remark;
	private String pinyinCode;


	@Override
	public String execute() throws Exception {
		SysDictionary sysDictionary = null;
		if (!StringUtils.isBlank(dicId)) {
			sysDictionary = this.sysDictionaryService.findById(this.dicId);

			if (sysDictionary == null) {
				this.msg= "保存失败,ID为" + this.dicId + "基础数据不存在";
				return "failure";
			}

			BeanUtils.copyProperties(sysDictionary, this);
			this.sysDictionaryService.update(sysDictionary);
		} else {
			sysDictionary = new SysDictionary();
			SysDictionary d = new SysDictionary();
			d.setKeyName(keyName);
			d.setTypeName(typeName);
			;
			if (this.sysDictionaryService.findByExample(d) != null
					&& this.sysDictionaryService.findByExample(d).size() > 0) {

				this.msg = "保存失败,数据重复";
				return "failure";
			}
			BeanUtils.copyProperties(sysDictionary, this);
			this.sysDictionaryService.add(sysDictionary);
		}
		return super.execute();
	}



	public String getDicId() {
		return dicId;
	}

	public void setDicId(String dicId) {
		this.dicId = dicId;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPinyinCode() {
		return pinyinCode;
	}

	public void setPinyinCode(String pinyinCode) {
		this.pinyinCode = pinyinCode;
	}

}
