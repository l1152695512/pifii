package com.yf.base.actions.personmanage;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;

public class SavePersonPhoto extends ActionSupport implements ServletRequestAware {
	private static final long serialVersionUID = 1L;
	//上传文件的基本变量
	protected HttpServletRequest request;
	protected File upload;//与前台的name属性对应，下面两个属性是直接在该属性的基础上加字符串的
	protected String uploadContentType;
	protected String uploadFileName;
	protected String msg;
	
	private String photoImage;
	
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
				@InterceptorRef (value="fileUpload",params={"allowedExtensions","png,gif,jpg,jpeg","maximumSize","512000"}), 
				@InterceptorRef ("extStack")
			})
	public String execute() throws Exception {
		if (uploadFileName == null) {
			this.msg = "不支持的文件类型或大小超过限制";
			return "failure";
		}
		if (uploadFileName.lastIndexOf(".") == -1) {
			this.msg = "非法的文件后缀";
			return "failure";
		}
		String suffix = uploadFileName.substring(uploadFileName.lastIndexOf("."));
		
		String photoSaveName = RandomStringUtils.random(32, true, true) + suffix;
		String photoRelativePath = "photos/"+photoSaveName;
//		String str = SavePersonPhoto.class.getClassLoader().getResource("").getPath();
//		String localPath = str.substring(1, str.indexOf("WEB-INF"));
		String localPath = GlobalVar.TOOLSPATH+"/";
		String photoAbsolutePath = localPath+photoRelativePath;
		File storeFile = new File(photoAbsolutePath);
		upload = this.beforeSave(upload);
		FileUtils.copyFile(upload, storeFile);
		this.msg = photoRelativePath;
		//如果存在上一个上传并且没有保存到数据库的图片，就删除
		if(StringUtils.isNotBlank(photoImage)){
			String previousPhotoAbsolutePath = localPath + photoImage;
			File previousPhotoFile = new File(previousPhotoAbsolutePath);
			if(previousPhotoFile.exists()){
				previousPhotoFile.delete();
			}
		}
		return super.execute();
	}

	protected File beforeSave(File file) {
		String path = file.getAbsolutePath();
		return new File(path);
	}
	
	@Override
	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}
	
	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getPhotoImage() {
		return photoImage;
	}
	
	public void setPhotoImage(String photoImage) {
		this.photoImage = photoImage;
	}
}
