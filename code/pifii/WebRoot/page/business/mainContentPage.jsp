<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- 设备信息显示 -->
<!-- 		<div id="devices" class="device"></div> -->
<div id="main_page_statistics" class="statistics"></div>

<!-- 路由首页创建步骤 -->
<div id="guide" class="guide"></div>
<!--guide安装更新结束-->
<!-- 应用中心显示 -->
<!-- 		<div id="applications" class="application"></div> -->
<!-- 		<div class="show_template"></div> -->
<script>
	$(function() {
		//获取统计信息
		loadPage("page/business/statistics.jsp", {}, $("#main_page_statistics"));

		//得到设备信息的HTML
		//loadPage("page/business/device.jsp",{},$(".device"));
		// 		loadPageWithCallback("page/business/device.jsp",{},function(html){
		// 			$(html).appendTo($("#devices"));
		// 			var title = $("#devices").children(".title");
		// 			if(title!=undefined && title!=""){
		// 				$("#devices").before(title);
		// 			}
		// 		});
		//得到向导指引的信息HTML
		//	loadPage("page/business/guide.jsp",{},$("#page"));
		loadPageWithCallback("page/business/guide.jsp", {}, function(html) {
			$(html).appendTo($("#guide"));
			var title = $("#guide").children(".title");
			if (title != undefined && title != "") {
				$("#guide").before(title);
			}
		});

		//得到向导指引的信息HTML
		//loadPage("page/business/apps.jsp",{},$(".application"));
		// 		loadPageWithCallback("page/business/showTemplate.jsp",{},function(html){
		// 			$(html).appendTo($("body>.main_content>.show_template"));
		// 			var title = $("body>.main_content>.show_template").children(".title");
		// 			if(title!=undefined && title!=""){
		// 				$("body>.main_content>.show_template").before(title);
		// 			}
		// 		});
	});
</script>