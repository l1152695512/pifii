package com.yf.util;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * 
 * @author llade
 * 
 */
public class LocalFileStore implements InitializingBean, FileStore {

	String rootFilePath = "D:/project/file/";
	String imagePath ="D:/project/file/image";
	File rootFile;
	boolean emptyRoot;

	public boolean isEmptyRoot() {
		return emptyRoot;
	}

	public void setEmptyRoot(boolean emptyRoot) {
		this.emptyRoot = emptyRoot;
	}

	public String getRootFilePath() {
		return rootFilePath;
	}

	public void setRootFilePath(String rootFilePath) {
		this.rootFilePath = rootFilePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.westwind.util.FileStore#getRootFile()
	 */
	public File getRootFile() {
		return this.rootFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.westwind.util.FileStore#getFile(java.lang.String)
	 */
	public File getFile(String fileName) {
		return new File(this.rootFile, fileName);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.rootFilePath);
		this.rootFile = new File(this.rootFilePath);
		if (!this.rootFile.exists()) {
			this.rootFile.mkdirs();
		} else if (emptyRoot) {
			FileUtils.cleanDirectory(this.rootFile);
		}

	}
	

	

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

}
