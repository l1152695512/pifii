<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
	#guide_app_data{
	}
	#guide_app_data>ul{
		width: 640px;
/* 		height: 281px; */
		margin:0 auto;
/* 		margin-bottom: 39px; */
		margin-top: 30px;
	}
	#guide_app_data>ul>li{
		width: 60px;
		height: 130px;
		position: relative;
		float: left;
		margin-left: 40px;
/* 		margin-bottom: 25px; */
	}
	#guide_app_data>ul>li .show_icon img{
		width: 60px;
		height: 60px;
	}
	#guide_app_data>ul>li .appInfo p{
		font-size:14px;
		font-weight: normal;
		text-align: center;
		overflow: hidden;
		white-space: nowrap;
	}
	#guide_app_data>ul>li .setting{
/* 		width: 60px; */
/* 		height: 24px; */
		width:58px;
		height: 22px;
/* 		border:1px solid #b4c0ce; */
		border-radius: 2px;
		margin:0 auto;
		font-family: "宋体";
		font-size: 12px;
		color: #fff;
		text-align: center;
		line-height: 24px;
		cursor: pointer;
		background: rgb(111, 206, 139);
		border: 1px solid rgb(107, 190, 49);
	}
	#guide_app_data .appInfo{
		cursor: pointer;
	}
</style>

<div id="guide_app_data">
	<ul>
<!-- 		<li> -->
<!-- 			<div class="pic"><img src="images/business/zhuang/3.png"/></div> -->
<!-- 			<p>小说</p> -->
<!-- 		</li> -->
<!-- 		<li> -->
<!-- 			<div class="pic"><img src="images/business/zhuang/10.png"/></div> -->
<!-- 			<p>认证</p> -->
<!-- 			<div class="zhuang">配置</div> -->
<!-- 		</li> -->
	</ul>
<!-- 	<div class="cl"></div> -->
<!-- 	<div class="step_button"> -->
<!-- 		<span>上一步</span> -->
<!-- 		<span>下一步</span> -->
<!-- 	</div> -->
</div>
<script type="text/javascript">
// 	var thisStep = 3;
	$(function(){
		$.ajax({
			type: "POST",
			url: "business/page/listInstalledApp",
			data:{pageId:getPageId()},
			success: function(data,status,xhr){
				var appHtml = "";
				for(var i=0;i<data.length;i++){
					appHtml += '<li data-app-id="'+data[i].id+'" data-edit-url="'+data[i].edit_url+'">' +
									'<div class="appInfo">'+
										'<div class="show_icon"><img src="'+data[i].icon+'"/></div>' +
										'<p title="'+data[i].name+'">'+data[i].name+'</p>'+
									'</div>';
					if(data[i].edit_url != ""){
						appHtml += '<div class="setting">配置</div>';
					}
					appHtml += '</li>';
				}
				$("#guide_app_data").find("ul").eq(0).html(appHtml);
				initSettingEvent();
			}
		});
	});
	function initSettingEvent(){
		$("#guide_app_data .appInfo").click(function(){
// 			var des = $(this).parents("li").first().data("des");
// 			var appIcon = $(this).parents("li").find(".appInfo img").attr("src");
// 			var appName = $(this).parents("li").find(".appInfo p").text();
			var appId = $(this).parents("li").first().data("appId");
			//处理页面上所有当前点击app的样式，保证在更改该应用图标或者名称的时候可以整体更新页面上app的名称及icon
			markerClickApp(appId);//方法写在common.js中
			$.fn.SimpleModal({
				title: '应用详细信息',
				keyEsc:true,
				buttons: [{
		    		text:'关闭',
		    		classe:'btn primary'
		    	}],
				param: {
					url: 'business/app/appInfo',
					data: {appId:appId,shopId:getSelectedShopId()}
				}
			}).showModal();
		});
// 		$("#guide_app_data li .appInfo").tipsy({fade: true,delayIn: 500,gravity:'s'});//,gravity:$.fn.tipsy.autoWE
		$("#guide_app_data .setting").click(function(){
			var editUrl = $(this).parents("li").first().data("editUrl");
			var title = $(this).parents("li").first().find(".appInfo p").text()+"配置";
			if(editUrl != '1'){
				$.fn.SimpleModal({
// 					model: 'modal-ajax',
					title: title,
// 					width: 1000,
// 					height: 555,
// 					hideFooter: true,
					param: {
						url: editUrl
					}
				}).showModal();
			}else{
				myAlert('<h3>应用数据录入正在开发，请稍后！</h3>');
			}
		});
		
// 		each(function(){
// 			if($(this).children().length == 3){
// 				$(this).children().eq(3).click(function(){
// 					myAlert("功能正在更新中");
// 				});
// 			}
// 		});
	}
</script>