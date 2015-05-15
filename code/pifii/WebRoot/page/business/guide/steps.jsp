<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
	#guide_step{
		position:relative;
	}
	#guide_step>ul{
		margin-top:60px;
		height: 280px;
	}
	#guide_step>ul>li{
		float:left;
		width: 160px;
		height: 150px;
		position: relative;
	}
	#guide_step>ul>li>div.first_children{
		cursor: pointer;
	}
	#guide_step>ul>li>div.second_children{
		width: 33px;
		height: 102px;
		position: absolute;
		top:0;
		right: 0;
	}
	#guide_step>ul>li>div.third_children{
		width: 100%;
		height: 40px;
/* 		color: #fff; */
		font-family: "宋体";
		font-size: 16px;
		text-align: center;
		font-weight: bold;
	}
	#guide_step>ul>li.last_li>div.second_children{
		display: none;
	}
</style>
<!--向导指引 右边步骤内容-->
<div id="guide_step"><!--向导指引  右边显示操作步骤-->
	<ul>
		<li>
			<div class="first_children"><img src="images/business/guide/chooseOut.png"/></div>
			<div class="second_children"></div><!--为了让向后箭头不被选中可点击-->
			<div class="third_children">选择样式</div>
		</li>
		<li>
			<div class="first_children"><img src="images/business/guide/installOut.png"/></div>
			<div class="second_children"></div><!--为了让向后箭头不被选中可点击-->
			<div class="third_children">安装应用</div>
		</li>
		<li>
			<div class="first_children"><img src="images/business/guide/UpdateOut.png"/></div>
			<div class="second_children"></div><!--为了让向后箭头不被选中可点击-->
			<div class="third_children">更新内容</div>
		</li>
		<li class="last_li">
			<div class="first_children"><img src="images/business/guide/previewOut.png"/></div>
			<div class="second_children"></div><!-- 隐藏 -->
			<div class="third_children">预览发布</div>
		</li>
		<div class="cl"></div>
	</ul>
</div>
<script type="text/javascript">
	$(function(){
		$("#guide_step>ul>li>div:first-child").click(function(){
			var step = $(this).parent().index();
			showStep(step+1);
		});
// 		updateShopPageInfo(function(data){
			setSetedStepStatus(parseInt(getShopPageInfo().step));
// 		});
// 		$("#guide_step>.start_guide").click(function(){
// 			showStep(getShopPageInfo().step);
// 		});
	});
// 	//更新步骤的图标为已完成
// 	function refreshStepStatus(step){
// 		if(step > currentStep){
// 			currentStep = step;
// 			setStepStatus(step);
// 		}
// 	}
	//加载页面时调用，初始化已完成步骤的图标
	function setSetedStepStatus(maxStep){
		for(var i=0;i<maxStep;i++){
			setStepStatus(i+1);
		}
	}
	//更改图标
	function setStepStatus(step){
		if($("#guide_step").find("li").length >= step){
			var img = $("#guide_step").find("li").eq(step-1).find("img");
			var imgSrc = "";
			if(img.attr("src") != ""){
				imgSrc = img.attr("src").substring(0,img.attr("src").length-7)+".png";
			}
			img.attr("src",imgSrc);
		}
	}
	
</script>