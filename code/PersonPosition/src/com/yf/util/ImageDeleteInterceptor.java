package com.yf.util;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;


public class ImageDeleteInterceptor extends EmptyInterceptor{
	
	
	private FileStore fileStore;
	
	private List imageProperties = new ArrayList();
	private List imageClassNames = new ArrayList();
	
	public List getImageClassNames() {
		return imageClassNames;
	}

	public void setImageClassNames(List imageClassNames) {
		this.imageClassNames = imageClassNames;
	}

	public List getImageProperties() {
		return imageProperties;
	}

	public void setImageProperties(List imageProperties) {
		this.imageProperties = imageProperties;
	}

	public FileStore getFileStore() {
		return fileStore;
	}

	public void setFileStore(FileStore fileStore) {
		this.fileStore = fileStore;
	}

 	@Override
	public void onDelete(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) {
		//super.onDelete(entity, id, state, propertyNames, types);
		if(entity != null && imageClassNames.contains(entity.getClass().getName())){
			File parentPath = fileStore.getRootFile();
			for ( int i=0; i < propertyNames.length; i++ ) {
	            if ( imageProperties.contains( propertyNames[i])) {
	            	if(state[i] != null && StringUtils.isNotBlank(state[i].toString())){
	//	            		Debug.println("delete img:"+parentPath+"/"+state[i].toString());
	            		new File(parentPath,state[i].toString()).delete();
	            	}
	            }
	        }
		}
	}

}
