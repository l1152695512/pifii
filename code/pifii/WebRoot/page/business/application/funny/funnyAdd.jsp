<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- 配置文件 -->
<script type="text/javascript" src="js/ueditor/ueditor.config.js"></script>
<!-- 编辑器源码文件 -->
<script type="text/javascript" src="js/ueditor/ueditor.all.js"></script>

<style type="text/css">
	#funnyAdd .row{padding:0;margin-bottom:10px;}
	#funnyAdd .row span{float: left;width:100px;height:30px;line-height:30px;color:gray;font-size:15px;font-weight:bold;text-align:right;display:inline-block;}
	#funnyAdd .row input{ border:1px solid #b4c0ce; color:#666;margin-left:20px; width:300px; height:30px; line-height:30px;border-radius:3px;}
	#funnyAdd .row select{outline:none;width:304px;color: rgb(105, 100, 100);height:30px;margin-left: 20px;line-height:30px;border: 1px solid #B4C0CE;font-weight:bold;border-radius:4px;}
	#funnyAdd .row textarea{ border:1px solid #b4c0ce;color:#666;width:298px;margin-left: 20px; height:60px;border-radius:3px;overflow:hidden;}
	
	#funnyAdd .row .tupian{width:140px;height:80px;overflow: hidden;margin-left: 20px;}
 	#funnyAdd .row .tupian img{width:100%;height:100%;}
	#funnyAdd .row .line_height_30{height:30px;line-height:30px;}
	#funnyAdd .row .line_height_60{height:60px;line-height:60px;}
	#funnyAdd .row .line_height_80{height:80px;line-height:80px;}
	
	#funnyAdd .button_upload{position:relative;height:40px;line-height:40px;width:157px; text-align:center;color:#FFF; background:#31a3ff;display:inline-block;border-radius:4px; font-weight:bold;top:40px;left:7px;overflow: hidden;}
	#funnyAdd .button_upload:hover{background:#52B0FC;}
	#funnyAdd .button_upload input{width:3774px;height: 285px;position:absolute;opacity:0;filter: alpha(opacity=0);right:0;top:0;cursor:pointer;font-size: 40px;}
	
	#funnyAdd .content_btn1{margin-top:20px;margin-left:120px;float:left;}
	#funnyAdd .content_btn1 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
	#funnyAdd .content_btn2{float:left;margin-top:20px;margin-left:10px;}
	#funnyAdd .content_btn2 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
</style>
<div id="funnyAdd">
	<form action="business/app/funny/save" method="post" enctype="multipart/form-data">
		<div class="tanchuceng_content" id="content_popup">
			<div class="row">
					<span class="line_height_30">标题：</span>
					<input type="text" name="funny.title"  value="${funny.title}" placeholder="输入标题" />
			</div>
			<div class="row">
				<span class="line_height_80">图片：</span>
				<div class="tupian fl">
					<img id="img1" src="<c:if test='${empty funny.img}'>aa.jpg</c:if><c:if test='${not empty funny.img}'>${cxt}/${funny.img}</c:if>" onerror="this.src='images/business/ad-1.jpg'"/>
				</div>
				<div class="button_upload">
					<input type="file" id="uploadImage" name="upload"  />本地上传
				</div>
				<div class="cl"></div>
			</div>
			<div class="row">
<!-- 				<span class="line_height_60">描述：</span> -->
				<textarea id="container" name="funny.txt">${funny.txt}</textarea>
			</div>
			<div class="content_btn1">
			 <a href="javascript:void(0)" id="sva_info"  onclick="submitInfo()" ><span>确定</span>
			</div>
			<div class="content_btn2">
				<a href="javascript:void(0)" id="closeWin" onclick="closePop();"> <span>取消</span>
				</a>
			</div>
		</div>
		<input id="funnyAdd_id" name="funny.id" type="hidden" value="${funny.id}" />
		<input id="funnyAdd_shopId" name="funny.shop_id" type="hidden" value="" />
	</form>
</div>
<script type="text/javascript">
	UE.getEditor('container');
	$(function() {
		var thisShopId = '${funnyAdd_id}';
		if(thisShopId == ''){//为添加
			var shopId = getSelectedShopId();
			$("#funnyAdd form input[name='funny.shop_id']").attr("value",shopId);
		}
		$("#funnyAdd form input[name=upload]").uploadPreview({ Img: $("#funnyAdd form .tupian").children("img")});
		$.fn._display();
	});
	function submitInfo(){
		var titleName = $("#funnyAdd form input[name='funny.title']").attr("value");
		if(titleName.length == 0){
			myAlert("标题不能为空！");
			return;
		}
		var localImgPath = $("#funnyAdd form input[name=upload]").val();
		if("${funny.id}" =="" && localImgPath==""){
			myAlert("请上传图片！");
			return;
		}
		 $("#funnyAdd form").ajaxSubmit({
		       success: function(resp){
	    			closePop();
	    			getFlowPackList();
		       }
		  	});
	 }
	</script>
