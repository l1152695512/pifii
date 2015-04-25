package com.yf.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;

import com.yf.util.Debug;
import com.yf.util.FileStore;
import com.yf.util.MimeTypeDictionary;

public class FileDownloadServlet extends HttpServlet {

	FileStore store;
	boolean sessionPath;
	String fileNotFoundDefault;
	ServletConfig config;

	@Override
	public void init(ServletConfig config) throws ServletException {
		String beanId = config.getInitParameter("fileStoreBeanId");
		String sessionPathStr = config.getInitParameter("sessionPath");
		fileNotFoundDefault = config.getInitParameter("fileNotFoundDefault");
		if ("true".equals(sessionPathStr))
			sessionPath = true;
		if (beanId == null)
			beanId = "fileStore";
		// Debug.println(WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext()));
		this.config = config;
		store = (FileStore) WebApplicationContextUtils
				.getRequiredWebApplicationContext(config.getServletContext())
				.getBean(beanId);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String url = req.getRequestURI();
		// String fileName = url.substring(url.lastIndexOf("/")+1);
		Debug.println("111...fileName=" + url);
		String fileName = "";
		if (url.contains("file/"))
			fileName = url.substring(url.lastIndexOf("file/") + 5);
		else
			fileName = url.substring(url.lastIndexOf("/") + 1);
		String contentType = MimeTypeDictionary.getMimeType(fileName);
		File file = null;
		if (sessionPath) {
			File userDir = store.getFile(req.getSession().getId());
			if (!userDir.exists()) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			} else {
				file = new File(userDir, fileName);
			}
		} else {
			Debug.println("save......==" + fileName);
			file = store.getFile(fileName);
		}
		if (file == null || !file.exists()) {
			if (fileNotFoundDefault == null) {
				resp.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			} else {
				// Debug.println(this.config.getServletContext());
				file = new File(this.config.getServletContext().getRealPath(
						fileNotFoundDefault));
			}
		}
		resp.addHeader("Content-Type", contentType);
		resp.addDateHeader("Last-Modified", file.lastModified());
		// file.lastModified();
		resp.addHeader("Content-Length", String.valueOf(file.length()));
		resp.flushBuffer();
		FileInputStream fi = new FileInputStream(file);
		BufferedInputStream bi = new BufferedInputStream(fi);
		byte[] buffer = new byte[4096];
		int read = 0;
		while ((read = bi.read(buffer)) != -1) {
			resp.getOutputStream().write(buffer, 0, read);
		}
	}

}
