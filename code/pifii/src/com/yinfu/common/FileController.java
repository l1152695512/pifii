package com.yinfu.common;

import java.io.IOException;

import com.yinfu.jbase.jfinal.ext.Controller;
import com.yinfu.jbase.util.upload.FlashUpload;
import com.yinfu.jbase.util.upload.KindEditor;
import com.jfinal.ext.route.ControllerBind;

@ControllerBind(controllerKey = "/common/file")
public class FileController extends Controller
{
	
	
	public void flashUpload() throws IOException{
		
		renderJson(FlashUpload.flashUpload(getRequest()));
		
	}
	
	public void upload()
	{
		renderJson(KindEditor.upload(this));
	}

	public void fileManage()
	{
		   renderJson(KindEditor.fileManage(getRequest()));
	}
	

	

}
