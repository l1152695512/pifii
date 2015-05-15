<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
	#change_template{
		width: 990px;
		font-family: "微软雅黑";
	}
	#change_template>.phone_preview{
		height: 620px;
		padding-left: 36px;
		padding-right: 20px;
		background: url(images/business/moreniphone.png) no-repeat;
	}
	#change_template .show_template{
		padding-left: 20px;
	}
	#change_template .show_template . code_span{
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
	</div><!--向导指引  左边手机界面-->
	<div class="fr show_template">
		<div class="guide_step_title"><span>二维码</span></div>
		<img id="codeImge" align="middle"  />
		<div >
			<span style="font: bold;color: red;font-size: 21px" >请使用手机扫描上方二维码即可预览</span>  
		</div>
	</div>
	<div class="cl">
	</div>
</div>
<script type="text/javascript">
	$(function(){
		var iframeSrc = "business/template/showPreview?shopId="+getSelectedShopId();
		$("#change_template>.phone_preview>iframe").attr("src",iframeSrc);
		$("#codeImge").attr("src",cxt+"/servlet/qrcode?shopId="+getSelectedShopId());
	});
	
</script>
			