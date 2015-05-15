<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- <link rel="stylesheet" type="text/css" href="css/business/showApp.css"/> -->

<style type="text/css">
	#show_apps{
/* 		height:550px; */
		height: 520px;
		overflow: auto;
	}
/* 	#show_apps .guide_app_title{ */
/* 		height: 36px; */
/* 		line-height: 36px; */
/* 		width: 595px; */
/* 		margin:0 auto; */
/* 		font-size: 18px; */
/* 		color: #344477; */
/* 		font-family: "微软雅黑"; */
/* 	} */
	
	#show_apps .yingyong{
	/* 	width: 640px; */
		height: 180px;
		margin:0 auto;
	}
	#show_apps .yingyong li{
		width: 60px;
		height: 134px;
		margin-left: 27px;
		margin-top: 30px;
		position: relative;
		float: left;
	}
/* 	#show_apps .close{ */
/* 		top: 7px; */
/* 	} */
	#show_apps .yingyong li .pic img{
		width: 60px;
		height: 60px;
	}
	#show_apps .yingyong li p{
		font-size:14px;
		font-weight: normal;
		text-align: center;
		overflow: hidden;
		white-space: nowrap;
	}
	#show_apps .yizhuang{
		width: 45px;
		height: 43px;
		background:url(images/business/yizhuang.png) no-repeat;
		position: absolute;
		top:0;
		right: 0;
	}
	#show_apps .app_action{
		width:58px;
		height: 22px;
		border: 1px solid rgb(49, 189, 222);
		border-radius: 2px;
		margin:0 auto;
		font-family: "宋体";
		font-size: 12px;
		color: #fff;
		text-align: center;
		line-height: 22px;
		cursor: pointer;
		margin-bottom: 10px;
		background: rgb(78, 199, 227);
	}
	#show_apps .uninstall_app{
		background: rgb(226, 165, 0);
		border: 1px solid rgb(200, 175, 1);
	}
	#show_apps .setting_app{
		background: rgb(111, 206, 139);
		border: 1px solid rgb(107, 190, 49);
	}
	#show_apps .appInfo{
		cursor: pointer;
	}
</style>

<div id="show_apps">
<!-- 	<p class="guide_app_title">基础应用</p> -->
	<ul class="yingyong"></ul>
<!-- 	<div class="cl"></div> -->
<!-- 	<p class="guide_app_title">餐饮</p> -->
<!-- 	<ul class="yingyong"></ul> -->
<!-- 	<div class="cl"></div> -->
</div>
<script type="text/javascript">
	$(function(){
		loadMainPageAppData();
	});
	function loadMainPageAppData(){
		$.ajax({
			type: "POST",
			url: "business/app/listAppWithInstallStatus",
			data:{pageId:getPageId()},
			success: function(data,status,xhr){
				var appsHtml = "";
				for(var i=0;i<data.length;i++){
					var appHtml = '<li data-app-id="'+data[i].id+'" data-edit-url="'+data[i].edit_url+'">';
					appHtml += '<div class="appInfo">';
					if(data[i].is_install == "1" || data[i].show == '0'){
						appHtml += '<div class="yizhuang"></div>';
					}
					appHtml += '<div class="pic"><img src="'+data[i].icon+'"/></div>'+
								'<p title="'+data[i].name+'">'+data[i].name+'</p>'+
							'</div>'+
							'<div class="actions">';
					if(data[i].is_install == "1" || data[i].show == '0'){
						if(data[i].show != '0'){//show为0时代表该应用为基础应用不可卸载的
							appHtml += '<div class="app_action app_status uninstall_app">卸载</div>';
						}
						if(data[i].edit_url != ""){
							appHtml += '<div class="app_action setting_app">配置</div>';
						}
					}else{
						appHtml += '<div class="app_action app_status">安装</div>';
					}
					
					appHtml += '</div>'+
						'</li>';
					appsHtml += appHtml;
				}
				$("#show_apps").find("ul").eq(0).html(appsHtml);
				initMainPageAppEvent();
			}
		});
	}
	function initMainPageAppEvent(){
		$("#show_apps .appInfo").click(function(){//更新页面上显示的app图标或者名称
			var appId = $(this).parents("li").first().data("appId");
			markerClickApp(appId);//方法写在common.js中
			$.fn.SimpleModal({
				title: '应用详细信息',
				keyEsc:true,
				width:503,
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
// 		$("#show_apps li .appInfo").tipsy({fade: true,delayIn: 500,gravity:'s'});//,gravity:$.fn.tipsy.autoWE
		$("#show_apps li .app_action").unbind("click");
// 		if(shopPageInfo.marker == "template2"){
// 			$("#show_apps li .app_status").css({"border":"1px solid rgb(187, 187, 187)","background":"rgb(187, 187, 187)"});
// 			$("#show_apps li .app_status").click(function(){
// 				myAlert("该模板不支持安装此应用！");
// 			});
// 		}else{
			$("#show_apps li .app_status").click(function(){
				var status = "1";//安装
				if($(this).parents("li").first().find(".yizhuang").length > 0){//已安装
					status = "0";
				}
				var appId = $(this).parents("li").first().data("appId");
				markerClickApp(appId);//方法写在common.js中
				var $this = $(this);
				if(status == "1"){//安装
// 					var appId = $(this).parents("li").first().data("appId");
					$.fn.SimpleModal({
						title: '应用安装提示',
						keyEsc:true,
	// 					width: 1050,
	// 					closeButton: false,
						buttons: [{
				    		text:'安装',
				    		classe:'btn primary2 btn-margin',
				    		clickEvent:function() {
				    			changeMainPageAppStatus(appId,status,$this);
				            }
				    	},{
				    		text:'取消',
				    		classe:'btn secondary'
				    	}],
						param: {
							url: 'business/app/appInfo',
							data: {appId:appId,shopId:getSelectedShopId()}
						}
					}).showModal();
				}else{//卸载
					myConfirm("确定要卸载该应用？",function(){
						changeMainPageAppStatus(appId,status,$this);
					});
				}
			});
// 		}
		$("#show_apps li .setting_app").click(function(){
			var title = $(this).parents("li").first().find(".appInfo").find("p").text()+"配置";
			settingMainPageAppData($(this).parents("li").first().data("editUrl"),title);
		});
	}
	function changeMainPageAppStatus(appId,status,appObj){
		$.ajax({
			type: "POST",
			url: "business/page/changeInstallApp",
			data:{appId:appId,status:status,pageId:getPageId()},
			success: function(data,status,xhr){
				if(data.success = true){
					changeMainPageAppStatusStyle(appObj);
					closePop();
				}else{
					myAlert("安装应用失败，稍后请重试！");
				}
			}
		});
	}
	function settingMainPageAppData(editUrl,title){
		if(editUrl != '1'){
			$.fn.SimpleModal({
// 				model: 'modal-ajax',
				title: title,
// 				width: 1000,
// 				height: 555,
// 				hideFooter: true,
				param: {
					url: editUrl,
					data:{shopId:getSelectedShopId()}
				}
			}).showModal();
		}else{
			myAlert('<h3>应用数据录入正在开发，请稍后！</h3>');
		}
	}
	function changeMainPageAppStatusStyle($this){
		if($this.parents("li").first().find(".yizhuang").length > 0){//已安装
			$this.parents("li").first().find(".yizhuang").remove();
			$this.parents("li").first().find(".setting_app").remove();
			$this.parents("li").first().find(".app_status").text("安装");
			$this.parents("li").first().find(".app_status").removeClass("uninstall_app");
		}else{
			$this.parents("li").first().find(".appInfo").prepend('<div class="yizhuang"></div>');
			if($this.parents("li").first().data("editUrl") != ""){
				var settingHtml = $('<div class="app_action setting_app">配置</div>').appendTo($this.parent());
				settingHtml.click(function(){
					settingMainPageAppData($(this).parents("li").first().data("editUrl"));
				});
			}
			$this.parents("li").first().find(".app_status").text("卸载");
			$this.parents("li").first().find(".app_status").addClass("uninstall_app");
		}
		refreshPhonePagePreview("app");
	}
</script>