<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <meta name="apple-mobile-web-app-status-bar-style" content="black" />
        <meta name="format-detection" content="telephone=no" />
        <title>${name}</title>     
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/portal/mb/microshop/css/common.css" />
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/portal/mb/microshop/css/weidian.css" />
        <script src="${pageContext.request.contextPath}/portal/commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/mb/microshop/js/weidian.js" type="text/javascript"></script>
    </head>
    <body>
    	<header>
        	<h3 class="weidian_tit">${name}</h3>
        </header>
        <article class="ar_2">
            <ul>
            <c:forEach items="${types}" var="row" varStatus="status">
				<li><a href="javascript:void(0);"><img src="${row.icon}" ><p>${row.name}</p></a></li>
			</c:forEach>
            </ul>
        	<div class="cl"></div>
            <div class="down_list"></div>
        </article>
        <article class="ar_3">
        	<h1>新品速递</h1>
            <ul>
           	<c:forEach items="${products}" var="row" varStatus="status">
				<li>
                	<a href="${row.link}">
                    	<img src="${row.img}">
                        <p title="${row.title}">${row.title}</p>
                        <h3><span>￥${row.new_price}</span><del>￥${row.old_price}</del></h3>
                    </a>
           		</li>
			</c:forEach>
            </ul>
        	<div class="cl"></div>
        </article>
        <footer>
        	<P>MO派智慧WiFi@<a href="#">mofi139.com</a></P>
        </footer>
    </body>
</html>
