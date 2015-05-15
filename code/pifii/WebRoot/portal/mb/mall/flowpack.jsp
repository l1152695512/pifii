<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html manifest="">
	<head>
		<title>${name}</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0"/>
		<meta name="apple-mobile-web-app-capable" content="yes"/>
		<meta content="telephone=no" name="format-detection" />
		
		<script src="${pageContext.request.contextPath}/portal/commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/commonjs/jquery.cookie.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/commonjs/commons.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" media="all" href="${pageContext.request.contextPath}/portal/mb/mall/css/global_style.css"/>
		<link rel="stylesheet" type="text/css" media="all" href="${pageContext.request.contextPath}/portal/mb/mall/css/current_page.css"/>
		<link rel="stylesheet" type="text/css" media="all" href="${pageContext.request.contextPath}/portal/mb/mall/css/stc.css"/>
	</head>
	<body id="touch-screen-ctzt">
		<div class="container">
			<header id="header" class="lge page-header">
				<article class="row pd_small">
					<h1 class="tc f_24 se-f">${name}<a href="javascript:void(1);" class="fl re-back">返回</a></h1>
					<a href="javascript:void(1);" id="mune" class="fr title_cd"></a>
				</article>
			</header>
			<section id="main" class="row mt_10" style="margin-top:45px;">
				<div class="main_content">
				  	<ul class="cz_ll_list">
						<c:forEach items="${rows}" var="row">
		                <li style="width:100%;">
							<a href="#">
								<div><img src="${row.pic}"/></div>
								<p>${row.title}</p>
								<span>${row.des}</span>
							</a>
		                </li>
		              	</c:forEach>
					</ul>
					<div class="clear"></div>
				</div>
			</section>
		</div>
	</body>
</html>