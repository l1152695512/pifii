package com.yf.interfaces;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		int port = 443; // default https port
		String host = "https://www.bitstamp.net/api/ticker";

		try {
			SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory
					.getDefault();

			SSLSocket socket = (SSLSocket) factory.createSocket(host, port);

			// enable all the suites
			String[] supported = socket.getSupportedCipherSuites();
			socket.setEnabledCipherSuites(supported);

			Writer out = new OutputStreamWriter(socket.getOutputStream());
			// https requires the full URL in the GET line
			out.write("GET http://" + host + "/ HTTP/1.1\r\n");
			out.write("Host: " + host + "\r\n");
			out.write("\r\n");
			out.flush();

			InputStream in = socket.getInputStream();
			ByteArrayOutputStream out1 = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = in.read(buffer)) != -1) {
				out1.write(buffer, 0, len);
			}
			out.close();
			in.close();
			
			byte[] data = out1.toByteArray();
			System.out.println("返回数据："+new String(data, "UTF-8"));

			socket.close();

		} catch (Exception ex) {
			System.err.println(ex);
		}

	}

}
