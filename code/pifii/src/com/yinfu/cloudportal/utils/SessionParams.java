package com.yinfu.cloudportal.utils;

/**
 * 这里的所有变量都是session中存放的key值，为了避免冲突，统一写在这个类中
 * @author l
 *
 */
public class SessionParams {
	public static final String ACCESS_DEVICE = "accessDevice";//访问portal页面的设备信息
	public static final String CLIENT_INFO = "clientInfo";//访问portal页面的设备信息
	
	public static final String CODE_SEND = "sendVerifyCode";//发送的验证码
	public static final String CODE_SEND_DATE = "sendVerifyCodeDate";//发送验证码的时间
	public static final String CODE_SEND_PHONE = "sendVerifyCodePhone";//发送验证码的手机号码
	
	public static final String AUTH_SUCCESS_URL = "authSuccessUrl";//认证成功后的跳转页面
	
	public static final String COOKIE_ROUTER_SN = "routersn";//cookie中的盒子sn
	public static final String COOKIE_CLIENT_MAC = "mac";//cookie中的客户端mac
	
	public static final String MY_SHOP_LIST = "myShopList";//cookie中的客户端mac
}
