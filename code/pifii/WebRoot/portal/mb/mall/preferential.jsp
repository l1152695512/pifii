<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">
	<head>
		<title>${name}</title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/portal/mb/mall/css/global.css"/>
		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/portal/mb/mall/css/index.css"/>
		<script src="${pageContext.request.contextPath}/portal/commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/commonjs/jquery.cookie.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/commonjs/commons.js" type="text/javascript"></script>
	</head>
	<body>
		<div id="wrap">
			<ul>
			<c:forEach items="${rows}" var="row">
				<li>
					<p class="title">${row.title}</p>
					<div class="pic"><img src="${row.img}"/></div>
					<div class="detail">
						<div>${row.txt}</div>
					</div>
				</li>
			</c:forEach>
			</ul>
			<div class="cl"></div>
		</div>
	</body>
</html>