<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
	#guide_apps{
		
	}
	#guide_apps ul{
		width: 640px;
/* 		height: 134px; */
		margin:0 auto;
		margin-top: 30px;
	}
	#guide_apps ul>li{
		width: 60px;
		height: 130px;
		position: relative;
		float: left;
		margin-left: 40px;
/* 		margin-top: 30px; */
/* 		margin-bottom: 25px; */
	}
	#guide_apps .install_status{
		width:58px;
		height: 22px;
		border: 1px solid rgb(49, 189, 222);
		background: rgb(78, 199, 227);
		border-radius: 2px;
		margin:0 auto;
		font-family: "宋体";
		font-size: 12px;
		color: #fff;
		text-align: center;
		line-height: 24px;
		cursor: pointer;
	}
	#guide_apps .uninstall_app{
		background: rgb(226, 165, 0);
		border: 1px solid rgb(200, 175, 1);
	}
	#guide_apps ul>li .appInfo p{
		font-size:14px;
		font-weight: normal;
		text-align: center;
		overflow: hidden;
		white-space: nowrap;
	}
	#guide_apps .show_icon img{
		width: 60px;
		height: 60px;
	}
	#guide_apps .installed{
		width: 45px;
		height: 43px;
		background:url(images/business/yizhuang.png) no-repeat;
		position: absolute;
		top:0;
		right: 0;
	}
	#guide_apps .appInfo{
		cursor: pointer;
	}
</style>

<div id="guide_apps">
<!-- 	<div class="apps_content"> -->
<!-- 		<p class="guide_app_title">基础应用</p> -->
		<ul>
	<!-- 		<li> -->
	<!-- 			<div class="show_icon"><img src="images/business/zhuang/1.png"/></div> -->
	<!-- 			<p>视频</p> -->
	<!-- 			<div class="install_status">安装</div> -->
	<!-- 		</li> -->
	<!-- 		<li> -->
	<!-- 			<div class="installed"></div> -->
	<!-- 			<div class="show_icon"><img src="images/business/zhuang/3.png"/></div> -->
	<!-- 			<p>小说</p> -->
	<!-- 			<div class="install_status">卸载</div> -->
	<!-- 		</li> -->
		</ul>
<!-- 		<div class="cl"></div> -->
<!-- 		<p class="guide_app_title">餐饮</p> -->
<!-- 		<ul> -->
	<!-- 		<li> -->
	<!-- 			<div class="show_icon"><img src="images/business/zhuang/9.png"/></div> -->
	<!-- 			<p>排队</p> -->
	<!-- 			<div class="install_status">安装</div> -->
		
	<!-- 		</li> -->
	<!-- 		<li> -->
	<!-- 			<div class="installed"></div> -->
	<!-- 			<div class="show_icon"><img src="images/business/zhuang/10.png"/></div> -->
	<!-- 			<p>优惠券</p> -->
	<!-- 			<div class="install_status">卸载</div> -->
		
	<!-- 		</li> -->
<!-- 		</ul> -->
<!-- 		<div class="cl"></div> -->
<!-- 	</div> -->
<!-- 	<div class="step_button"> -->
<!-- 		<span>上一步</span> -->
<!-- 		<span>下一步</span> -->
<!-- 	</div> -->
</div>
<script type="text/javascript">
	$(function(){
		$.ajax({
			type: "POST",
			url: "business/app/listAppWithInstallStatus",
			data:{pageId:getPageId()},
			success: function(data,status,xhr){
				var appsHtml = "";
				for(var i=0;i<data.length;i++){
					if(data[i].show != '0'){//广告不显示
						var appHtml = '<li data-app-id="'+data[i].id+'" data-des="'+data[i].des+'">';
						if(data[i].is_install == "1"){
							appHtml += '<div class="appInfo">'+
											'<div class="installed"></div>'+
											'<div class="show_icon"><img src="'+data[i].icon+'"/></div>'+
											'<p title="'+data[i].name+'">'+data[i].name+'</p>'+
										'</div>'+
										'<div class="install_status uninstall_app">卸载</div>';
						}else{
							appHtml += '<div class="appInfo" original-title="'+data[i].des+'">'+
											'<div class="show_icon"><img src="'+data[i].icon+'"/></div>'+
											'<p>'+data[i].name+'</p>'+
										'</div>'+
										'<div class="install_status">安装</div>';
						}
						appHtml += '</li>';
						appsHtml += appHtml;
					}
				}
				if(appsHtml == ''){
					if(previousStep > currentShowStep){
						$("#guide_index .step_button").find("span").eq(0).click();//如果没有应用直接跳到下一步
					}else{
						$("#guide_index .step_button").find("span").eq(1).click();//如果没有应用直接跳到下一步
					}
				}else{
					$("#guide_apps").find("ul").eq(0).html(appsHtml);
					initEvent();
				}
			}
		});
	});
	function initEvent(){
		$("#guide_apps .appInfo").click(function(){
// 			var des = $(this).parents("li").first().data("des");
// 			var appIcon = $(this).parents("li").find(".appInfo img").attr("src");
// 			var appName = $(this).parents("li").find(".appInfo p").text();
			var appId = $(this).parents("li").first().data("appId");
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
		
// 		$("#guide_apps li .appInfo").tipsy({fade: true,delayIn: 500,gravity:'s'});//,gravity:$.fn.tipsy.autoWE
		$("#guide_apps li .install_status").unbind("click");
// 		if(shopPageInfo.marker == "template2"){
// 			$("#guide_apps li .install_status").css({"border":"1px solid rgb(187, 187, 187)","background":"rgb(187, 187, 187)"});
// 			$("#guide_apps li .install_status").click(function(){
// 				myAlert("该模板不支持安装此应用！");
// 			});
// 		}else{
			$("#guide_apps li .install_status").click(function(){
				var status = "1";//安装
				if($(this).parents("li").first().find(".installed").length > 0){//已安装
					status = "0";
				}
				var appId = $(this).parents("li").first().data("appId");
				markerClickApp(appId);//方法写在common.js中
				var $this = $(this);
				if(status == "1"){//安装
// 					var des = $(this).parents("li").first().data("des");
// 					var appIcon = $(this).parents("li").find(".appInfo img").attr("src");
// 					var appName = $(this).parents("li").find(".appInfo p").text();
					$.fn.SimpleModal({
						title: '应用安装提示',
						keyEsc:true,
						buttons: [{
				    		text:'安装',
				    		classe:'btn primary2 btn-margin',
				    		clickEvent:function() {
				    			changeAppStatus(appId,status,$this);
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
						changeAppStatus(appId,status,$this);
					});
				}
			});
// 		}
	}
	function changeAppStatus(appId,thisStatus,appObj){
		$.ajax({
			type: "POST",
			url: "business/page/changeInstallApp",
			data:{appId:appId,status:thisStatus,pageId:getPageId()},
			success: function(data,status,xhr){
				if(data.success = true){
					changeAppStatusStyle(appObj);
					if(thisStatus == "1"){
						closePop();
					}
				}else{
					myAlert("安装应用失败，稍后请重试！");
				}
			}
		});
	}
	function changeAppStatusStyle($this){
		if($this.parents("li").first().find(".installed").length > 0){//已安装
			$this.parents("li").first().find(".installed").remove();
			$this.parents("li").first().find(".install_status").text("安装");
			$this.parents("li").first().find(".install_status").removeClass("uninstall_app");
		}else{
			$this.parent().prepend('<div class="installed"></div>');
			$this.parents("li").first().find(".install_status").text("卸载");
			$this.parents("li").first().find(".install_status").addClass("uninstall_app");
		}
		refreshPhonePagePreview("app");
	}
</script>