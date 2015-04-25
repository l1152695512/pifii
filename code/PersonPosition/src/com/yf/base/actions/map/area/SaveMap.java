package com.yf.base.actions.map.area;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.interceptor.ServletRequestAware;

import com.opensymphony.xwork2.ActionSupport;
import com.yf.tradecontrol.GlobalVar;
import com.yf.util.dbhelper.DBHelper;

public class SaveMap extends ActionSupport implements ServletRequestAware {
	private static final long serialVersionUID = 1L;
	private DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
	//上传文件的基本变量
	protected HttpServletRequest request;
	protected File upload;//与前台的name属性对应，下面两个属性是直接在该属性的基础上加字符串的
	protected String uploadContentType;
	protected String uploadFileName;
	protected String msg;
	
	private String communityId;
//	private String mapImage;
	@Override
	@Action("image-upload-input")
	public String input() throws Exception {
		return SUCCESS;
	}
	
	/**
	 * "allowedTypes","image/png,image/gif,image/jpeg,image/pjpeg,image/jpg,image/x-png",
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Action(interceptorRefs={
				@InterceptorRef (value="fileUpload",params={"allowedExtensions","png,gif,jpg,jpeg","maximumSize","5120000"}), 
				@InterceptorRef ("extStack")
			})
	public String execute() throws Exception {
		File uploadFile = saveFile();
		if(null != uploadFile){
			List<Map<String,String>> dataList = (List<Map<String, String>>) dbhelper.getMapListBySql(
					"select map from bp_community_tbl where id=? ",new Object[]{communityId});
			if(dataList.size()>0){
				File oldMapFile = new File(GlobalVar.TOOLSPATH+"/"+dataList.get(0).get("map"));
				boolean success = dbhelper.update("update bp_community_tbl set map=? where id=?", new Object[]{this.msg,communityId});
				if(success){
					oldMapFile.delete();
					return super.execute();
				}else{
					uploadFile.delete();
				}
			}else{
				uploadFile.delete();
			}
		}
		return "failure";
	}

	private File saveFile() throws IOException{
		if (uploadFileName == null) {
			this.msg = "不支持的文件类型或大小超过限制";
			return null;
		}
		if (uploadFileName.lastIndexOf(".") == -1) {
			this.msg = "非法的文件后缀";
			return null;
		}
		String suffix = uploadFileName.substring(uploadFileName.lastIndexOf("."));
		
		String mapSaveName = RandomStringUtils.random(32, true, true) + suffix;
		String mapRelativePath = "maps/"+mapSaveName;
//		String str = SaveMap.class.getClassLoader().getResource("").getPath();
//		String localPath = str.substring(1, str.indexOf("WEB-INF"));
		String localPath = GlobalVar.TOOLSPATH+"/";
		String mapAbsolutePath = localPath+mapRelativePath;
		File storeFile = new File(mapAbsolutePath);
		upload = this.beforeSave(upload);
		FileUtils.copyFile(upload, storeFile);
		this.msg = mapRelativePath;
		return storeFile;
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
	
	public String getCommunityId() {
		return communityId;
	}
	
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	
//	public String getMapImage() {
//		return mapImage;
//	}
//	
//	public void setMapImage(String mapImage) {
//		this.mapImage = mapImage;
//	}
}
