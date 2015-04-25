package com.yf.base.actions;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;

import com.yf.ext.base.StrutsUploadAction;
import com.yf.util.ImageUtils;

public class ImageUpload extends StrutsUploadAction {
	
	private String imgId;
	private String inputId;
	private HttpServletRequest request;

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
	@Action("image-upload-input")
	public String input() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * "allowedTypes","image/png,image/gif,image/jpeg,image/pjpeg,image/jpg,image/x-png",
	 */
	@Override
	@Action(interceptorRefs={
				@InterceptorRef (value="fileUpload",params={"allowedExtensions","png,gif,jpg,jpeg","maximumSize","2048000"}), 
				@InterceptorRef ("extStack")
			})
	public String execute() throws Exception {
		if(uploadFileName == null){
			this.msg = "只支持png、gif、jpg文件，文件大小不能超过200K";
			return "failure";
		}
		return super.execute();
	}

	@Override
	protected File beforeSave(File file) {
		String path = file.getAbsolutePath();
		return new File(path);
	}
	
	
}
