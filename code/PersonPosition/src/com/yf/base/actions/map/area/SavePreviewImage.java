package com.yf.base.actions.map.area;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SavePreviewImage extends ActionSupport implements ServletRequestAware {
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	//上传文件的基本变量
	protected HttpServletRequest request;
	protected File upload;//与前台的name属性对应，下面两个属性是直接在该属性的基础上加字符串的
	protected String uploadContentType;
	protected String uploadFileName;
	protected String msg;
	
	private String id;
	
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
				@InterceptorRef (value="fileUpload",params={"allowedExtensions","png,gif,jpg,jpeg","maximumSize","5120000"}), 
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
		
		String saveName = RandomStringUtils.random(32, true, true) + suffix;
		String relativePath = "maps/previewImages/"+saveName;
//		String str = SavePreviewImage.class.getClassLoader().getResource("").getPath();
//		String localPath = str.substring(1, str.indexOf("WEB-INF"));
		String localPath = GlobalVar.TOOLSPATH+"/";
		String absolutePath = localPath+relativePath;
		File storeFile = new File(absolutePath);
		upload = this.beforeSave(upload);
		FileUtils.copyFile(upload, storeFile);
		if(changePreviewImage(localPath,relativePath)){
			this.msg = relativePath;
			return super.execute();
		}else{
			this.msg = "操作失败，稍后请重试！";
			return "failure";
		}
	}

	@SuppressWarnings("unchecked")
	private boolean changePreviewImage(String localPath,String previewImageRelativePath){
		String sqlSelect = "select preview_image from bp_community_tbl where id='"+id+"'";
		List<Map<String,Object>> dataList = (List<Map<String, Object>>) dbhelper.getMapListBySql(sqlSelect);
		if(dataList.size()>0){
			Map<String,Object> data = dataList.get(0);
			String previewImagePath = null==data.get("preview_image")?"":data.get("preview_image").toString();
			String sqlUpdate = "update bp_community_tbl set preview_image='"+previewImageRelativePath+"' where id='"+id+"'";
			if(dbhelper.update(sqlUpdate)){
				if(StringUtils.isNotBlank(previewImagePath)){//如果有以前的图片，就删除
					deleteFile(localPath+previewImagePath);
				}
				return true;
			}else{
				deleteFile(localPath+previewImageRelativePath);
			}
		}else{
			deleteFile(localPath+previewImageRelativePath);
		}
		return false;
	}
	
	private boolean deleteFile(String fileAbsolutePath){
		File previewImageFile = new File(fileAbsolutePath);
		if(previewImageFile.exists() && previewImageFile.delete()){
			return true;
		}
		return false;
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
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
}
