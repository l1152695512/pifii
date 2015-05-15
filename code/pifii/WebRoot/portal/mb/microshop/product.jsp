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
        <title>${detail.title}</title>     
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/portal/mb/microshop/css/common.css" />
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/portal/mb/microshop/css/product.css" />
        <script src="${pageContext.request.contextPath}/portal/commonjs/TouchSlide.1.1.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/portal/commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/portal/mb/microshop/js/weidian.js" type="text/javascript"></script>
    </head>
    <body>
    	<header>
            <a href="javascript:history.back()" class="back">返回</a>
            <div class="info">商品详情</div>
        </header>
        <div class="album">
        	<img src="${detail.img}">
            <div class="desc">点击图片查看</div>
        </div>
        <div class="buy-box">
            <div class="price">¥</div>
            <div class="price sum">${detail.new_price}</div>
            <div class="past Fix">
                <div class="t">
                    <span class="o-price">¥${detail.old_price}</span>
                </div>
            </div>
<!--             <div class="last"> -->
<!--             	<a href="#" class="buy-btn ">立即购买</a> -->
<!--             </div> -->
        </div>
        <div class="intro">
            <h3>${detail.title}</h3>
            <p>${detail.remark}</p>
        </div>
    </body>
</html>
