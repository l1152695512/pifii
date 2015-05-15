<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
	#change_template{
		width: 990px;
/* 		height: 620px; */
/* 		margin:0 auto; */
/* 		position: relative; */
		font-family: "微软雅黑";
	}
	#change_template>.phone_preview{
		height: 620px;
/* 		width: 260px; */
/* 		padding-top: 94px; */
/* 		padding-bottom: 80px; */
		padding-left: 36px;
		padding-right: 20px;
/* 		position: absolute; */
/* 		z-index: 2; */
/* 		left: -50px; */
		background: url(images/business/moreniphone.png) no-repeat;
	}
/* 	#change_template>.phone_preview img{ */
/* 		width: 100%; */
/* 	} */
	#change_template .show_template{
/* 		height: 100%; */
/* 		position: relative; */
		padding-left: 20px;
	}
	#change_template .guide_step_title{
		height:88px;
		width: 653px;
		color: #444d5e;
		font-size: 28px;
		line-height: 122px;
		border-bottom: 1px solid #C4C9D7;
	}
	#change_template iframe{
		background-color:#fff;
		filter:chroma(color=black);
		height: 444px;
		width: 260px;
		margin-top: 97px;
	}
</style>

<div id="change_template">
	<div class="phone_preview fl">
		<iframe  src="" framespacing="0" name="page_preview" 
			frameborder="0" scrolling="auto" allowtrancparency="true"></iframe>
<!-- 		<img src="images/business/moreniphone.png"/> -->
	</div><!--向导指引  左边手机界面-->
	<div class="fr show_template">
		<div class="guide_step_title"><span>选择样式</span></div>
	</div>
	<div class="cl"></div>
</div>
<script type="text/javascript">
	$(function(){
		var iframeSrc = "business/template/showPreview?shopId="+getSelectedShopId();
		$("#change_template>.phone_preview>iframe").attr("src",iframeSrc);
		
		loadPageWithCallback("page/business/guide/step_template.jsp",{},function(html){
			$("#change_template .show_template").append(html);
// 			$("#guide_templates .guide_step_title").find("img").remove();
		});
// 		loadPage("page/business/guide/step_template.jsp",{},$("#change_template .show_template"));
	});
	
</script>
			