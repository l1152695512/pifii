<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- 配置文件 -->
<script type="text/javascript" src="js/ueditor/ueditor.config.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" src="js/ueditor/ueditor.all.js"></script>

<style type="text/css">
	#preferentialAdd .row{padding:0;margin-bottom:10px;}
	#preferentialAdd .row span{float: left;width:100px;height:30px;line-height:30px;color:gray;font-size:15px;font-weight:bold;text-align:right;display:inline-block;}
	#preferentialAdd .row input{ border:1px solid #b4c0ce; color:#666;margin-left:20px; width:300px; height:30px; line-height:30px;border-radius:3px;}
	#preferentialAdd .row select{outline:none;width:304px;color: rgb(105, 100, 100);height:30px;margin-left: 20px;line-height:30px;border: 1px solid #B4C0CE;font-weight:bold;border-radius:4px;}
	#preferentialAdd .row textarea{ border:1px solid #b4c0ce;color:#666;width:298px;margin-left: 20px; height:60px;border-radius:3px;overflow:hidden;}
/* 	#preferentialAdd .row img{width:140px;height:80px;overflow:hidden;margin-left: 20px;} */
	#preferentialAdd .row .tupian{width:140px;height:80px;overflow: hidden;margin-left: 20px;}
 	#preferentialAdd .row .tupian img{width:100%;height:100%;}
	#preferentialAdd .row .line_height_30{height:30px;line-height:30px;}
	#preferentialAdd .row .line_height_60{height:60px;line-height:60px;}
	#preferentialAdd .row .line_height_80{height:80px;line-height:80px;}
	
	#preferentialAdd .button_upload{overflow: hidden;position:relative;height:40px;line-height:40px;width:157px; text-align:center;color:#FFF; background:#31a3ff;display:inline-block;border-radius:4px; font-weight:bold;top:40px;left:5px;}
	#preferentialAdd .button_upload:hover{background:#52B0FC;}
	#preferentialAdd .button_upload input{width:3774px;height: 285px;filter: alpha(opacity=0);font-size: 40px;position:absolute;opacity:0;right:0;top:0;cursor:pointer;}
	
	#preferentialAdd .content_btn1{margin-top:20px;margin-left:120px;float:left;}
	#preferentialAdd .content_btn1 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
	#preferentialAdd .content_btn2{float:left;margin-top:20px;margin-left:10px;}
	#preferentialAdd .content_btn2 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
</style>
<div id="preferentialAdd">
	<form action="business/app/preferential/save" method="post" enctype="multipart/form-data">
		<div class="tanchuceng_content" id="content_popup">
			<div class="row">
					<span class="line_height_30">标题：</span>
					<input type="text" name="preferential.title"  value="${preferential.title}" placeholder="输入标题" />
			</div>
			<div class="row">
				<span class="line_height_80">图片：</span>
				<div class="tupian fl">
					<img src="<c:if test='${empty preferential.img}'>aa.jpg</c:if><c:if test='${not empty preferential.img}'>${cxt}/${preferential.img}</c:if>" onerror="this.src='images/business/ad-1.jpg'"/>
				</div>
				<div class="button_upload">
					<input type="file" name="upload"/>本地上传
				</div>
				<div class="cl"></div>
			</div>
			<div class="row">
<!-- 				<span class="line_height_60">描述：</span> -->
				<textarea id="container" name="preferential.txt">${preferential.txt}</textarea>
			</div>
			<div class="content_btn1">
			 <a href="javascript:void(0)" id="sva_info"  onclick="submitInfo()" ><span>确定</span>
			</div>
			<div class="content_btn2">
				<a href="javascript:void(0)" onclick="closePop();"> <span>取消</span></a>
			</div>
		</div>
		<input id="preferentialAdd_id" name="preferential.id" type="hidden" value="${preferential.id}" />
		<input id="preferentialAdd_shopId" name="preferential.shop_id" type="hidden" value="" />
	</form>
</div>
<script type="text/javascript">
	UE.getEditor('container');
	$(function() {
		var thisShopId = '${preferentialAdd_id}';
		if(thisShopId == ''){//为添加
			var shopId = getSelectedShopId();
			$("#preferentialAdd form input[name='preferential.shop_id']").attr("value",shopId);
		}
		$("#preferentialAdd form input[name=upload]").uploadPreview({ Img: $("#preferentialAdd form .tupian").children("img")});
		setTimeout("$.fn._display();",200);
	});
	function submitInfo(){
		var titleName = $("#preferentialAdd form input[name='preferential.title']").attr("value");
		if(titleName.length == 0){
			myAlert("标题不能为空！");
			return;
		}
		var localImgPath = $("#preferentialAdd form input[name=upload]").val();
		if("${preferential.id}"=="" && localImgPath==""){
			myAlert("请上传图片！");
			return;
		}
		 $("#preferentialAdd form").ajaxSubmit({
		       success: function(resp){
	    			closePop();
	    			getFlowPackList();
		       }
		  	});
	 }
	</script>
