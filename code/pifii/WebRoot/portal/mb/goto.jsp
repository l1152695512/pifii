<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0,user-scalable=no"/>
		<meta name="MobileOptimized" content="320"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/portal/commonjs/jquery-1.8.3.min.js"></script>
		<title>${start_adv.shopName}</title>
        <style type="text/css">
        	body{
        		margin: 0;
        		padding:0;
        	}
       		.result{
				display:none;
				position:absolute;
				width:100%;
				height:100%;
			}
        </style>
	</head>
	<body><!-- ${start_adv.link} -->
		<a href="javascript:void(0)"><img class="result" src="${start_adv.image}"  onerror="this.src='${pageContext.request.contextPath}/portal/mb/index1/img/transition.png'"/></a>
    	<noscript>
			该浏览器不能执行javascript！请检查是否有禁用浏览器！
		</noscript>
		<script type="text/javascript">
			$(window).load(function(){
				$(".result").fadeIn(2000);
				setInterval(gotoIndex,4000);
	        });
// 			$(function(){
				
// 			});
			function gotoIndex(){
				$(".result").fadeOut(2000,function() {
					window.location.href = "index?routersn="+getQueryStringByName("routersn")+"&mac="+getQueryStringByName("mac");
				});
			}
			function getQueryStringByName(name){
				var result = location.search.match(new RegExp("[\?\&]" + name+ "=([^\&]+)","i"));
				if(result == null || result.length < 1){
					return "";
				}
				return result[1];
			}
        </script>
	</body>
</html>