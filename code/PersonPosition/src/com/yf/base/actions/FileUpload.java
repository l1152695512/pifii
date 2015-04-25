package com.yf.base.actions;

import java.io.File;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;

import com.yf.ext.base.StrutsUploadAction;

public class FileUpload extends StrutsUploadAction {
	
	private String imgId;
	private String inputId;


	public String getImgId() {
		return imgId;
	}

	public void setImgId(String imgId) {
		this.imgId = imgId;
	}

	public String getInputId() {
		return inputId;
	}

	public void setInputId(String inputId) {
		this.inputId = inputId;
	}

	@Override
	@Action("file-upload-input")
	public String input() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * "allowedTypes","image/png,image/gif,image/jpeg,image/pjpeg,image/jpg,image/x-png",
	 */
//	@Override
//	@Action(interceptorRefs={
//				@InterceptorRef (value="fileUpload",params={"allowedExtensions","doc,xls,xlsx,pdf","maximumSize","2100000"}), 
//				@InterceptorRef ("extStack")
//				
//			})	
	   @Action(interceptorRefs={  
			@InterceptorRef (value="fileUpload",params={"maximumSize","204800000"}), 
			@InterceptorRef ("extStack")	
		})
	public String execute() throws Exception {
//		if(uploadFileName == null){
//			this.msg = "只支持doc,xls,xlsx,pdf文件，文件大小不能超过2M";
//			return "failure";
//		}
		if(uploadFileName == null){
			this.msg = "请检查上传文件格式及大小！";
			return "failure";
		}
		return super.execute();
	}

	@Override
	protected File beforeSave(File file) {
		String path = file.getAbsolutePath();
		//ImageUtils.reduceImg(path, 170, true);
		//ImageUtils.reduceImg(path, 190, false);
		return new File(path);
	}
	
	
}
