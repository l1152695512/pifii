/*
 * Copyright (c) 2008 Gteam, All Rights Reserved.
 */
package com.yf.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ContractService {

	public static String ClientLoginURL = "http://localhost:8088/gcgl?a=1&b=2";
	public static String AuthTokenParams = "accountType=HOSTED_OR_GOOGLE&Email=android.c2dm.demo@gmail.com&Passwd=androidc2dmdemo&service=ac2dm&source=bupt-c2dmdemo-1.0";
	public static String C2DMServerURL = "http://android.apis.google.com/c2dm/send";
	public static String Registration_ID = "APA91bGUBoSvt3G5Ny9t0IGLmIKAKYX6G6VHwSQHh3tP2fqcaQ0N4GPdKh5B3RDUHFCFF06YwT8ifOP";

	public static String getAuthToken(String url, String params)
			throws IOException {
		String auth = null;
		// 要POST的数据
		byte[] postData = params.getBytes();
		// 构造POST请求
		URL requestUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) requestUrl
				.openConnection();
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");
		connection.setRequestProperty("Content-Length", Integer
				.toString(postData.length));
		// 写入POST数据
		OutputStream out = connection.getOutputStream();
		out.write(postData);
		out.flush();
		out.close();
		// 获取并处理请求返回的数据
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String responseLine;
		StringBuilder responseDataBuidler = new StringBuilder();
		while ((responseLine = reader.readLine()) != null) {
			responseDataBuidler.append(responseLine);
		}
		int responseCode = connection.getResponseCode();
		System.out.println("auth responseCode = " + responseCode);
		if (responseCode == 200) {
			// 如果请求成功，提取Auth值
			int authStartIndex = responseDataBuidler.indexOf("Auth=");
			auth = responseDataBuidler.substring(authStartIndex + 5);
			// System.out.println(auth);
		} else {
			// 如果失败，打印出失败的结果
			System.out.println(responseDataBuidler);
		}
		return auth;
	}

	public static boolean sendPushMessage(String url, String registration_id,
			String collapse_key, String auth, Map<String, String> data)
			throws IOException {
		boolean flag = false;
		// 构造POST的数据
		StringBuilder postDataBuidler = new StringBuilder();
		postDataBuidler.append("registration_id").append("=").append(
				registration_id);
		postDataBuidler.append("&").append("collapse_key").append("=").append(
				collapse_key);
		for (Object keyObject : data.keySet()) {
			String key = (String) keyObject;
			if (key.startsWith("data.")) {
				String value = data.get(key);
				postDataBuidler.append("&").append(key).append("=").append(
						value);
			}

		}
		byte[] postData = postDataBuidler.toString().getBytes();
		// 构造POST请求
		URL requestUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) requestUrl
				.openConnection();
		connection.setDoOutput(true);
		connection.setUseCaches(false);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");
		connection.setRequestProperty("Content-Length", Integer
				.toString(postData.length));
		connection.setRequestProperty("Authorization", "GoogleLogin auth="
				+ auth);
		// 写入POST数据
		OutputStream out = connection.getOutputStream();
		out.write(postData);
		out.flush();
		out.close();
		// 获取并处理请求返回的数据
		int responseCode = connection.getResponseCode();
		System.out.println("c2dm responseCode = " + responseCode);
		if (responseCode == 200) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String responseLine;
			StringBuilder responseDataBuidler = new StringBuilder();
			while ((responseLine = reader.readLine()) != null) {
				responseDataBuidler.append(responseLine);
			}
			if (responseDataBuidler.toString().startsWith("id=")) {
				flag = true;
			}
			System.out.println(responseDataBuidler);
		} else if (responseCode == 503) {
			System.out
					.println("The server is temporarily unavailable, please try later");
		} else if (responseCode == 401) {
			System.out
					.println(" the ClientLogin AUTH_TOKEN used to validate the sender is invalid");
		}
		return flag;
	}

	public static void main(String[] args) {

		try {
			String authToken = getAuthToken(ClientLoginURL, AuthTokenParams);
			if (authToken == null) {
				System.out.println("Can not get the Auth");
				return;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));
			while (true) {
				System.out
						.println("Please input the message to push:(Exit with q)");
				String message = reader.readLine();
				if (message.equalsIgnoreCase("q")) {
					break;
				}
				Map<String, String> data = new HashMap<String, String>();
				data.put("data.msg", message);// 这里和客户端代码统一，使用的key为msg，并且只push一组数据
				if (sendPushMessage(C2DMServerURL, Registration_ID, "1",
						authToken, data)) {
					System.out.println("Send Successfully");
				} else {
					System.out.println("Send Failed");
				}

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void test(){
		System.out.println("---------------服务端测试中");
	}
}
