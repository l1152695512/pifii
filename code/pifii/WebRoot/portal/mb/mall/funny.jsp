<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
	<head>
		<title>${name}</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/portal/mb/mall/css/jokeGlobal.css"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/portal/mb/mall/css/joke.css"/>
		<script src="${pageContext.request.contextPath}/portal/commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/commonjs/jquery.cookie.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/commonjs/commons.js" type="text/javascript"></script>
	</head>
	<body>
		<div class="topTitle"><h1>${name}</h1>
			<div><img src="${pageContext.request.contextPath}/portal/mb/mall/img/refresh.png"/></div>
		</div>
		<div class="kg40"></div>
		<div id="wrap">
			<ul>
			<c:forEach items="${rows}" var="row">
				<li>
					<div class="contentTitle">
						<h3 class="fl">${row.title}</h3>
						<div class="fr time">${row.create_date}</div>
						<div class="cl"></div>
					</div>
					<div class="detail">
						${row.txt}
					</div>
					<div class="pic">
						<div><img src="${row.img}"/></div>
					</div>
				</li>
			</c:forEach>
				<div class="cl"></div>
			</ul>
		</div>
	</body>
	<script type="text/javascript">
// 		$(document).ready(function(){
// 			$(".pic").find("img")[0].onload=function(){
// 				var imgHeight = parseInt($(".pic").find("img").height());
// 				var imgWidth = parseInt($(".pic").find("img").width());
// 				$(".pic").find("div").height(imgHeight+"px");
// 				$(".pic").find("div").width(imgWidth+"px");
// 			};
// 		});
	</script>
</html>