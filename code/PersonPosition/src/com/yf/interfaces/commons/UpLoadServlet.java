package com.yf.interfaces.commons;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.yf.tradecontrol.GlobalVar;
import com.yf.tradecontrol.JDomHandler;
import com.yf.util.dbhelper.DBHelper;

public class UpLoadServlet extends HttpServlet {
	private static Logger logger = Logger.getLogger(UpLoadServlet.class);
	private JDomHandler domHandler = new JDomHandler();
	private static final String xmlpath = GlobalVar.WORKPATH + File.separator
			+ "config" + File.separator + "dsSystemConfig.xml";
	File outdir = null;  
	File outfile = null; 
    FileOutputStream fos = null;  
    BufferedInputStream bis = null;  
    byte[] bs = new byte[1024];  
  
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		this.doPost(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		domHandler.loadXmlByPath(xmlpath);
		JSONObject resultObject = new JSONObject();
		String fName = "";
		String type = "";
		String keyWord = "";
		try {
			String loadPath = domHandler.getNodeValue("/ds-config/uploadPath/upLoadImagePath");
			String savePath = request.getSession().getServletContext().getRealPath("/")+loadPath;
			outdir = new File(savePath);  
			fName = request.getHeader("fileName");//文件名
			type = request.getHeader("type");//类型("1".用户头像;"2".家庭图片,"3".家庭区域图片;"4".家电图片:"5".其它)
			keyWord = request.getHeader("keyWord");//关键ID(帐号/用户ID/区域ID/家电ID/其它ID)
			String url = savePath+getNewName(fName,keyWord,type);
			String url2 = loadPath+getNewName(fName,keyWord,type);
			outfile = new File(url);
			bis = new BufferedInputStream(request.getInputStream());
			uploadFile(type,keyWord,url2);
			resultObject.put("returnCode", 200);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "文件上传成功!");
			logger.info("用户："+keyWord+"上传文件类型："+type+"成功!");
		} catch (Exception e) {
			resultObject.put("returnCode", 500);
			resultObject.put("data", new JSONObject());
			resultObject.put("desc", "文件上传失败!");
			logger.error("用户："+keyWord+"上传文件类型："+type+"失败!"+e);
		} finally {
			if (null != bis){
				bis.close();
			}
			if (null != fos){
				fos.close();
			}
		}
		response.getWriter().print(resultObject.toString());
	}

	private void uploadFile(String type,String keyWord,String url) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug("outdir:" + outdir.getPath());  
			logger.debug("outfile:" + outfile.getPath());
		}
		if (!outdir.exists()){
			outdir.mkdirs();
		}
		if (!outfile.exists()){
			outfile.createNewFile();
		}
		fos = new FileOutputStream(outfile);
		int i;
		while ((i = bis.read(bs)) != -1) {
			fos.write(bs, 0, i);
		}
		
		String sql = "";
		if(type.equals("1")){//用户头像
			sql = "update BP_CUSTOMER_TBL set user_img='"+url+"' where c_id='"+keyWord+"'";
		}else if(type.equals("2")){//家庭图片
			sql = "update BP_CUSTOMER_TBL set family_img='"+url+"' where c_id='"+keyWord+"'";
		}else if(type.equals("3")){//家庭区域图片
			sql = "update BP_HOUSE_TBL set photo_url='"+url+"' where h_id='"+keyWord+"'";
		}else if(type.equals("4")){//家电图片
			sql = "update BP_DEVICE_TBL set photo_url='"+url+"' where d_id='"+keyWord+"'";
		}
		if(!isEmpty(sql)){
			DBHelper dbhelper = new DBHelper(GlobalVar.POOLNAME_YFBIZDB);
			dbhelper.update(sql);
		}
	}

	public static String getNewName(String fname,String keyWord,String type) {
		StringBuffer result = new StringBuffer();
		int idx = fname.lastIndexOf('.');
		if (idx != -1) {
			result.append(keyWord+"_"+type);
			result.append(fname.substring(idx));
		} else {
			result.append(keyWord+type);
		}

		return result.toString();
	}

	public static boolean isEmpty(String str) {
		return ((str == null) || (str.length() == 0));
	}

}
