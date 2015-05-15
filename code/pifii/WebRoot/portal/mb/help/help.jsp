<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0,user-scalable=no"/>
		<meta name="MobileOptimized" content="320"/>
		<script src="${pageContext.request.contextPath}/portal/commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
		<title>帮助中心</title>
        <style type="text/css">
        	body{width:100%;height:100%;margin: 0;padding: 0;font-family: Microsoft YaHei;background:url(${pageContext.request.contextPath}/portal/mb/help/images/hm_ditu.png) no-repeat; background-size:100% 100%;}
        	ul,li{list-style: none;}
			ul,li,img{ border:0; margin: 0; padding: 0;}
			.title{text-align: center;padding: 15% 0;}
			.title img{width: 35%;}
			.content ul{font-size: 17px;margin-left: 5%;margin-right: 5%;color: white;}
			.content li{height: 50px;text-align: center;background: #057CDA;-webkit-border-radius: 3px;border-radius: 3px;margin-top: 10%;cursor: pointer;}
			.content li img{height: 86%;margin: 4px 5px;}
			.content li span{position: relative;top: -18px;}
			.footer{width: 100%;text-align: center;position: absolute;bottom: 0;color: white;padding-bottom: 10px;}
        </style>
	</head>
	<body>
		<div class="title">
			<img alt="" src="${pageContext.request.contextPath}/portal/mb/help/images/hm_logo.png">
		</div>
		<div class="content">
			<ul>
				<c:forEach items="${functions}" var="row" varStatus="status">
					<li data-url="${row.url}">
						<img src="${pageContext.request.contextPath}${row.src}"/>
						<span>${row.name}</span>
					</li>
				</c:forEach>
			</ul>
		</div>
		<div class="footer">江西移动&nbsp;&nbsp;&nbsp;&nbsp;MO派</div>
		<script type="text/javascript">
			$(function(){
				$("body").height($(window).height());
				$(".content li").click(function(){
					if(undefined!=$(this).data("url") && $(this).data("url") != ""){
						window.location.href=$(this).data("url");
					}else{
						alert("功能正在研发中！");
					}
				});
			});
        </script>
	</body>
</html>