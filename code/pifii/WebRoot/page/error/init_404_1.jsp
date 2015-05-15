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
		<title>授权结果</title>
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
			}
			.result{
				position:absolute;
				width:100%;
				height:100%;
			}
			.back{
				position:absolute;
				top: 20px;
				left: 20px;
				width:18%;
				cursor: pointer;
			}
		</style>
		<script type="text/javascript">
			$(document).ready(function(){
				if("${result}" != "true"){
					$(".result").attr("src","images/auth/authFail.jpg");
				}
				if('${back}'==true){
					setTimeout("backEvent()", 3000);
				}
			});
			function backEvent(){
				if("${main_page}" != ""){
					window.location.href="${main_page}";
				}else if(window.history.length > 1){
					history.back();
				}
			}
	    </script>
	</head>
	<body>
		<img class="result" src="images/auth/authSuccess.jpg"/>
		<img class="back" src="images/auth/back.png" onclick="backEvent()"/>
	</body>
</html>