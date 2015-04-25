package com.yf.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PhoneInformationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
		System.err.println("----------------------doget");
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.err.println("----------------------dopost");
		resp.getWriter().print(getJSONBean(req));
	}

	public String getJSONBean(HttpServletRequest request) {
		InputStream in = null;
		ByteArrayOutputStream read = null;
		String bkStr = "";
		try {
			in = request.getInputStream();
			read = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = in.read(buffer)) != -1) {
				read.write(buffer, 0, len);
			}
			byte[] data = read.toByteArray();
			bkStr = new String(data, "UTF-8").intern();
			return bkStr;
		} catch (Exception e) {
			e.printStackTrace();
			return bkStr;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (read != null) {
					read.close();
				}
			} catch (IOException e) {
			}
		}
	}

}
