<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="css/business/appStore.css"/>

<div id="appStroe">
	<ul class="yingyong"></ul>
	<div class="cl">
	</div>
</div>
<script type="text/javascript">
	$(function(){
		loadData();
	});
	function loadData(){
		$.ajax({
			type: "POST",
			url: "business/app/listAppWithInstallStatus",
			data:{pageId:getPageId()},
			success: function(data,status,xhr){
				if("success" == status){
					var appsHtml = "";
					for(var i=0;i<data.length;i++){
						var appHtml = '<li data-app-id="'+data[i].id+'" data-edit-url="'+data[i].edit_url+'">';
						if(data[i].is_install == "1" || data[i].show == '0'){
							appHtml += '<div class="yizhuang"></div>'+
										'<div class="pic"><img src="'+data[i].icon+'"/></div>'+
										'<p title="'+data[i].name+'">'+data[i].name+'</p>';
							if(data[i].show != '0'){
								appHtml += '<div class="app_action app_status uninstall_app">卸载</div>';
							}
							if(data[i].edit_url != ""){
								appHtml += '<div class="app_action setting_app">配置</div>';
							}
						}else{
							appHtml += '<div class="pic"><img src="'+data[i].icon+'"/></div>'+
										'<p>'+data[i].name+'</p>'+
										'<div class="app_action app_status">安装</div>';
						}
						appHtml += '</li>';
						appsHtml += appHtml;
					}
					$("#appStroe").find("ul").eq(0).html(appsHtml);
					initEvent();
				}
			}
		});
	}
	function initEvent(){
		$("#appStroe li .app_action").unbind("click");
// 		if(shopPageInfo.marker == "template2"){
// 			$("#appStroe li .app_status").css({"border":"1px solid rgb(187, 187, 187)","background":"rgb(187, 187, 187)"});
// 			$("#appStroe li .app_status").click(function(){
// 				myAlert("该模板不支持安装此应用！");
// 			});
// 		}else{
			$("#appStroe").find("li").find(".app_status").click(function(){
				var status = "1";//安装
				if($(this).parent().children(".yizhuang").length > 0){//已安装
					status = "0";
				}
				var appId = $(this).parent().data("appId");
				var $this = $(this);
				$.ajax({
					type: "POST",
					url: "business/page/changeInstallApp",
					data:{appId:appId,status:status,pageId:getPageId()},
					success: function(data,status,xhr){
						if("success" == status){
							if(data.success = true){
								changeAppStatus($this);
							}else{
								myAlert("安装应用失败，稍后请重试！");
							}
						}
					}
				});
			});
// 		}
		$("#appStroe").find("li").find(".setting_app").click(function(){
			var title = $(this).parent().children("p").text()+"配置";
			settingAppData($(this).parent().data("editUrl"),title);
		});
	}
	function settingAppData(editUrl,title){
		if(editUrl != '1'){
			$.fn.SimpleModal({
// 				model: 'modal-ajax',
				title: title,
//				width: 1000,
// 				hideFooter: true,
				param: {
					url: editUrl
				}
			}).showModal();
		}else{
			myAlert('<h3>应用数据录入正在开发，请稍后！</h3>');
		}
	}
	function changeAppStatus($this){
		if($this.parent().children(".yizhuang").length > 0){//已安装
			$this.parent().children(".yizhuang").remove();
			$this.parent().children(".setting_app").remove();
			$this.parent().children(".app_status").text("安装");
			$this.parent().children(".app_status").removeClass("uninstall_app");
		}else{
			$this.parent().prepend('<div class="yizhuang"></div>');
			if($this.parent().data("editUrl") != ""){
				var settingHtml = $('<div class="app_action setting_app">配置</div>').appendTo($this.parent());
				settingHtml.click(function(){
					settingAppData($(this).parent().data("editUrl"));
				});
			}
			$this.parent().children(".app_status").text("卸载");
			$this.parent().children(".app_status").addClass("uninstall_app");
		}
		refreshPhonePagePreview("app");
	}
</script>