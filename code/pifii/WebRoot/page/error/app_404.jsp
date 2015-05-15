<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0,user-scalable=no">
		<meta name="MobileOptimized" content="320">
		<script src="js/jquery-1.8.3.min.js" type="text/javascript"></script>
		<title>页面正在同步中,请稍后</title>
	    <style>
	    	*{
			    padding: 0;
			    margin: 0;
			}
			img{.
				border:0;
			}
			body{
			    font-family:"微软雅黑","Helvetica","Arial";
			    font-size:16px;
			    letter-spacing: 1px;
			    overflow: hidden;
			}
			.background_img{
				position:absolute;
				top: -10%;
				width:100%;
				height:120%;
			}
			.button{
				position: fixed;
				left: 28%;
				width: 45%;
				cursor: pointer;
			}
			.back{
				bottom: 15%;
			}
			.auth{
				bottom: 5%;
			}
		</style>
		<script type="text/javascript">
			$(document).ready(function(){
				
			});
			function backEvent(){
				if("${main_page}" != ""){
					window.location.href="${main_page}";
				}else if(window.history.length > 1){
					history.back();
				}
			}
			function gotoAuth(){
				if("${auth_url}"!=""){
					window.location.href="${auth_url}";
				}else{
					alert("未配置认证地址！");
				}
			}
	    </script>
	</head>
	<body>
		<img class="background_img" src="images/error/app_not_found.jpg"/>
		<img class="button back" src="images/error/return_button.png" onclick="backEvent()"/>
		<img class="button auth" src="images/error/auth_button.png" onclick="gotoAuth()"/>
	</body>
</html>