<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- 		<meta http-equiv="pragma" content="no-cache">  -->
<!-- 		<meta http-equiv="cache-control" content="no-cache">  -->
<!-- 		<meta http-equiv="expires" content="0"> -->
		<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
		<meta name="apple-touch-fullscreen" content="yes">
		<meta content="yes" name="apple-mobile-web-app-capable" />
		<meta content="telephone=no" name="format-detection" />
		<meta content="email=no" name="format-detection" />
		<title>${name}</title>
		<link href="${pageContext.request.contextPath}/portal/mb/auth/css/public.css" rel="stylesheet" type="text/css">
		<link href="${pageContext.request.contextPath}/portal/mb/auth/css/certification_style.css" rel="stylesheet" type="text/css">
		<script src="${pageContext.request.contextPath}/portal/commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
	</head>
	<body class="hasfootnav">
		<section>
			<nav class="px_nav">
		    	<ul>
	    		<c:forEach items="${showAuthTypes}" var="row">
					<li <c:if test='${row.marker==useAuthType}'>class="cur"</c:if>><a href="../auth?type=${row.marker}">${row.name}</a></li>
				</c:forEach>
		        </ul>
		    </nav>
		    <article class="pxnav_box">
		    	<div class="pxnav_list ">
		        	<li class="current">
		            	<article class="picScroll">
		            		<%@ include file="phone.jsp" %>
		            		<%@ include file="weixin.jsp" %>
							<div style="margin-bottom:10px;clear:both"></div>
		     			</article>
		            </li>
		        </div>
		    </article>
		</section>
	</body>
	<script type="text/javascript">
		$(function() {
			if($(".px_nav li").length == 0){
				alert("没有开放的认证方式！");
				history.back();
			}else{
				var navWidth = 100/$(".px_nav li").length;
				$(".px_nav li").css("width",navWidth+"%");
			}
// 			$('.px_nav li').click(function(){
// 				$(this).addClass('cur').siblings().removeClass('cur');
// 				$(".pxnav_list li:eq("+$(this).index()+")").fadeIn().siblings(".pxnav_list li").hide().addClass("current");
// 			});
		});
	</script>
</html>
