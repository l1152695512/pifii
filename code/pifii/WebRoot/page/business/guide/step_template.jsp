<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
	#guide_templates{
		position: relative;
	}
	#guide_templates>ul{
		width: 590px;
		height: 340px;
		margin:0 auto;
		overflow: hidden;
		margin-left: 58px;
		margin-top: 30px;
	}
	#guide_templates>ul>li{
		/** width: 118px;
		height: 238px;**/
		margin-right: 18px;
		float: left;
		margin-bottom: 40px;
	}
	#guide_templates>ul>li .previewImg{
		height:258px;
	}
	#guide_templates>ul>li>p{
		height:40px;
		width: 169px;
		line-height: 40px;
		font-size: 20px;
		text-align: center;
		color: #344477;
	}
	#guide_templates>ul>li img{
		width: 169px;
/* 		height: 222px; */
	}
	#guide_templates>ul>li>div.install_action{
		width: 60px;
		height: 24px;
		border:1px solid #b4c0ce;
		border-radius: 2px;
/* 		margin:0 auto; */
		margin-left: 55px;
		font-family: "宋体";
		font-size: 12px;
		color: #8a8a8a;
		text-align: center;
		line-height: 24px;
		cursor: pointer;
	}
	#guide_templates>ul>li:last-child{
		margin-right: 0
	}
	#guide_templates .left_template{
		width: 30px;
		height: 51px;
		position: absolute;
		top:32%;
		left: 5px;
		cursor: pointer;
		background: url(images/business/template/left_template.png) no-repeat;
	}
	#guide_templates .right_template{
		width: 30px;
		height: 51px;
		position: absolute;
		top:32%;
		right: 0;
		background: url(images/business/template/right_template.png) no-repeat;
		cursor: pointer;
	}
</style>

<div id="guide_templates">
	<ul>
<!-- 		<li> -->
<!-- 			<div class="pic"><img src="images/business/guide/style/style1.jpg"/></div> -->
<!-- 			<p>风格一</p> -->
<!-- 			<div class="zhuang">安装</div> -->
<!-- 		</li> -->
	</ul>
	<div class="left_template"></div>
	<div class="right_template"></div>
<!-- 	<div class="next_step">下一步</div> -->
</div>
<script type="text/javascript">
	$(function(){
		$.ajax({
			type: "POST",
			url: "business/template/list",
			data:{shopId:getSelectedShopId()},
			success: function(data,status,xhr){
				var templateHtml = "";
				var installedIndex = -1;
				for(var i=0;i<data.length;i++){
					if(data[i].is_used == "1"){
						installedIndex = i;
					}
					templateHtml += '<li data-id="'+data[i].id+'" data-can-used="'+data[i].can_used+'">' +
										'<div class="previewImg"><img src="'+data[i].preview_img+'"/></div>' +
										'<p>'+data[i].name+'</p>' +
										'<div class="install_action">安装</div>' +
									'</li>';
				}
				$("#guide_templates").find("ul").html(templateHtml);
				initEvent();
				if(installedIndex != -1){
					setInstallTemplate($("#guide_templates").find("ul").children().eq(installedIndex).children().last());
				}
			}
		});
	});
	
	function setInstallTemplate(button){
		button.text("已安装");
		button.css({"cursor":"auto"});
		button.unbind("click");
		button.parent().siblings().each(function(){
			var thisInstallButton = $(this).children().last();
			if(!thisInstallButton.data("events") || !thisInstallButton.data("events")["click"]){
				thisInstallButton.text("安装");
				thisInstallButton.css({"cursor":"pointer"});
				addInstallEvent(thisInstallButton);
			}
		});
	}
	
	function initEvent(){
		$("#guide_templates").find(".left_template").click(function(){
			var hideLi = $("#guide_templates").find("li:hidden");
			if(hideLi.length > 0){
				hideLi.eq(hideLi.length-1).show("slow");
			}
		});
		$("#guide_templates").find(".right_template").click(function(){
			if(isLiOverflow()){
				var visibleLi = $("#guide_templates").find("li:visible");
				if(visibleLi.length > 1){
					visibleLi.eq(0).hide("slow");
				}
			}
		});
		addInstallEvent($("#guide_templates").find("li>div:last-child"));
	}
	
	function beforeNextStep(){
		var choiceTemplate = false;
		$("#guide_templates").find("li").each(function(){
			if($(this).children().last().css("cursor") == "auto"){
				choiceTemplate = true;
				return false;
			}
		});
		if(!choiceTemplate){
			myAlert("请先选择风格！");
		}
		return choiceTemplate;
	}
	
	function addInstallEvent($element){
		$element.unbind("click");
		$element.click(function(){
			if("1" == $(this).parent().data("canUsed")){
				var templateId = $(this).parent().data("id");
				var $this = $(this);
				$.ajax({
					type: "POST",
					url: "business/page/saveTemplate",
					data: {id:getPageId(),shopId:getSelectedShopId(),templateId:templateId},
					success: function(data,status,xhr){
						if("success" == status){
							if(data.success = true){
								shopPageInfo = data.pageInfo;
//	 							setShopPageInfoStep(1);//更新前台保存的页面数据
								setInstallTemplate($this);
								loadMainPageAppData();//重新加载模板对应的app
								refreshPhonePagePreview("tem");
							}else{
								myAlert("更换模板失败稍后请重试！");
							}
						}
					}
				});
			}else{
				myAlert("主子，我们正在玩命上线中，敬请期待！");
			}
		});
	}
	
	function isLiOverflow(){
		var ulWidth = parseInt($("#guide_templates").find("ul").css("width"));
		var visibleLiWidth = 0;
		$("#guide_templates").find("li:visible").each(function(){
			visibleLiWidth +=  parseFloat($(this).css("width")) + 
					parseFloat($(this).css("padding-left")) + parseFloat($(this).css("padding-right")) + 
					parseFloat($(this).css("margin-left")) + parseFloat($(this).css("margin-right"));
		});
		if(visibleLiWidth > ulWidth){
			return true;
		}else{
			return false;
		}
	}
</script>