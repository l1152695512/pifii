<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
/* 	#page{ */
/* 		width: 100%; */
/* 		height:418px; */
/* 		margin-bottom:125px; */
/* 	} */
	#main_page_app{
		width: 962px;
/* 		margin:0 auto; */
/* 		position: relative; */
	}
	#main_page_app>div.first_children{
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
/* 		background-size: 98%; */
	}
	#main_page_app .app_title{
		position: relative;
		width: 630px;
		height: 68px;
		border-bottom: 1px solid #C4C9D7;
		overflow: hidden;
		margin-top: 15px;
	}
	#main_page_app .app_title span.app_center{
		font-size: 28px;
		color: #444d5e;
		line-height: 40px;
	}
	#main_page_app .app_title span.change_template{
		position: absolute;
		right: 32px;
		top: 25px;
		font-size: 18px;
		color: rgb(88, 145, 253);
		font-style: italic;
		cursor: pointer;
	}
	#main_page_app .app_title span.template_preview{
		position: absolute;
		right: 120px;
		top: 25px;
		font-size: 18px;
		color: rgb(88, 145, 253);
		font-style: italic;
		cursor: pointer;template_preview
	}
	#main_page_app iframe{
		background-color:#fff;
		filter:chroma(color=black);
		height: 444px;
		width: 260px;
		margin-top: 97px;
	}
</style>

<div class="title"></div>
<div id="main_page_app">
	<div class="fl first_children">
		<iframe src="" framespacing="0" name="page_preview" 
			frameborder="0" scrolling="auto" allowtrancparency="true"></iframe>
	</div><!--向导指引  左边手机界面-->
	<div class="fr" style="width: 630px;">
		<div class="app_title" style="text-indent: 2em">
			<span class="app_center">应用中心</span>
			<span class="template_preview">手机端预览</span>
			<span class="change_template">更改模板</span>
		</div>
	</div><!--空盒子为了装 两个guideRight（首次+非首次）-->
	<div class="cl"></div>
</div>

<script type="text/javascript">
	$(function(){
		var iframeSrc = "business/template/showPreview?shopId="+getSelectedShopId();
		$("#main_page_app>.fl>iframe").attr("src",iframeSrc);
		//得到向导指引的--左边手机界面--的HTML
// 		loadPage("page/business/guideLeft.jsp",{shopId:getSelectedShopId()},$("#main_page_app .fl"));
		//得到向导指引的--右边向导指引步骤--HTML
		loadPageWithCallback("page/business/showApp.jsp",{shopId:getSelectedShopId()},function(html){
			$("#main_page_app .fr").append(html);
		});
		$("#main_page_app .app_title").find("span").eq(1).click(function(){
			$.fn.SimpleModal({
// 				model: 'modal-ajax',
				title: '模板预览',
				width: 750,
// 				hideFooter: true,
				param: {
					url: 'page/business/preViewTemplate.jsp'
				}
			}).showModal();
		});
		$("#main_page_app .app_title").find("span").eq(2).click(function(){
			$.fn.SimpleModal({
// 				model: 'modal-ajax',
				title: '更改模板',
// 				width: 1050,
// 				hideFooter: true,
				param: {
					url: 'page/business/showTemplate.jsp'
				}
			}).showModal();
		});
	});
</script>
			