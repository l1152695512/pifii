<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style type="text/css">
	#tideAdd .row{padding:0;margin-bottom:10px;}
	#tideAdd .row span{float: left;width:100px;height:30px;line-height:30px;color:gray;font-size:15px;font-weight:bold;text-align:right;display:inline-block;}
	#tideAdd .row input{ border:1px solid #b4c0ce; color:#666;margin-left:20px; width:300px; height:30px; line-height:30px;border-radius:3px;}
	#tideAdd .row select{outline:none;width:304px;color: rgb(105, 100, 100);height:30px;margin-left: 20px;line-height:30px;border: 1px solid #B4C0CE;font-weight:bold;border-radius:4px;}
	#tideAdd .row textarea{ border:1px solid #b4c0ce;color:#666;width:298px;margin-left: 20px; height:60px;border-radius:3px;overflow:hidden;}
/* 	#tideAdd .row img{width:140px;height:80px;overflow:hidden;margin-left: 20px;} */
	#tideAdd .row .tupian{width:140px;height:80px;overflow: hidden;margin-left: 20px;}
 	#tideAdd .row .tupian img{width:100%;height:100%;}
	#tideAdd .row .line_height_30{height:30px;line-height:30px;}
	#tideAdd .row .line_height_60{height:60px;line-height:60px;}
	#tideAdd .row .line_height_80{height:80px;line-height:80px;}
	
	#tideAdd .button_upload{overflow: hidden;position:relative;height:40px;line-height:40px;width:157px; text-align:center;color:#FFF; background:#31a3ff;display:inline-block;border-radius:4px; font-weight:bold;top:40px;left:5px;}
	#tideAdd .button_upload:hover{background:#52B0FC;}
	#tideAdd .button_upload input{width:3774px;height: 285px;filter: alpha(opacity=0);font-size: 40px;position:absolute;opacity:0;right:0;top:0;cursor:pointer;}
	
	#tideAdd .content_btn1{margin-top:20px;margin-left:120px;float:left;}
	#tideAdd .content_btn1 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
	#tideAdd .content_btn2{float:left;margin-top:20px;margin-left:10px;}
	#tideAdd .content_btn2 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
</style>
<div id="tideAdd">
	<form action="business/app/tide/save" method="post" enctype="multipart/form-data">
		<div class="tanchuceng_content" id="content_popup">
			<div class="row">
					<span class="line_height_30">名称：</span>
					<input type="text" name="tide.name"  value="${tide.name}" placeholder="名称" />
			</div>
			<div class="row">
				<span class="line_height_80">图片：</span>
				<div class="tupian fl">
					<img onerror="this.src='images/business/ad-1.jpg'" src="<c:if test='${empty tide.img}'>aa.jpg</c:if><c:if test='${not empty tide.img}'>${cxt}/${tide.img}</c:if>" />
				</div>
				<div class="button_upload">
					<input type="file" name="upload"  />本地上传
				</div>
				<div class="cl"></div>
			</div>
			<div class="row">
					<span class="line_height_30">图片描述：</span>
					<input type="text" name="tide.picdes"  value="${tide.picdes}" placeholder="图片描述" />
			</div>
			<div class="row">
					<span class="line_height_30">优惠价￥：</span>
					<input type="text" name="tide.preprice" onkeyup="value=value.replace(/[^\d.]/g,'')"  value="${tide.preprice}" placeholder="价格（请输入正数或者小数）" />
			</div>
			<div class="row">
					<span class="line_height_30">原价格￥：</span>
					<input type="text" name="tide.price" onkeyup="value=value.replace(/[^\d.]/g,'')"  value="${tide.price}" placeholder="价格（请输入正数或者小数）" />
			</div>
			<div class="row">
				<span class="line_height_60">描述：</span>
				<textarea id="container" name="tide.des">${tide.des}</textarea>
			</div>
			<div class="content_btn1">
			 <a href="javascript:void(0)" id="sva_info"  onclick="submitInfo()" ><span>确定</span>
			</div>
			<div class="content_btn2">
				<a href="javascript:void(0)" id="closeWin" onclick="closePop();"> <span>取消</span>
				</a>
			</div>
		</div>
		<input id="tideAdd_id" name="tide.id" type="hidden" value="${tide.id}" />
		<input id="tideAdd_shopId" name="tide.shop_id" type="hidden" value="" />
	</form>
</div>
<script type="text/javascript">
	$(function() {
		var thisShopId = '${tideAdd_id}';
		if(thisShopId == ''){//为添加
			var shopId = getSelectedShopId();
			$("#tideAdd form input[name='tide.shop_id']").attr("value",shopId);
		}
		$("#tideAdd form input[name=upload]").uploadPreview({ Img: $("#tideAdd form .tupian").children("img")});
	});
	function submitInfo(){
		var name = $("#tideAdd form input[name='tide.name']").attr("value");
		if(name.length==0){
			myAlert("名称不能为空！");
			return;
		}
		var localImgPath = $("#tideAdd form input[name=upload]").val();
		if("${tide.id}"=="" && localImgPath==""){
			myAlert("请上传图片！");
			return;
		}
		var preprice = $("#tideAdd form input[name='tide.preprice']").attr("value");
		if(preprice.length==0){
			myAlert("优惠价不能为空！");
			return;
		}else if(parseFloat(preprice)<=0){
			myAlert("优惠价必须大于0！");
			return;
		}
		var price = $("#tideAdd form input[name='tide.price']").attr("value");
		if(price.length==0){
			myAlert("原价不能为空！");
			return;
		}else if(parseFloat(price)<=0){
			myAlert("原价必须大于0！");
			return;
		}
		if(parseFloat(price)<parseFloat(preprice)){
			myAlert("优惠价不能大于原价！");
			return;
		}
		 $("#tideAdd form").ajaxSubmit({
		      success: function(resp){
	    			closePop();
	    			getFlowPackList();
		       }
		  	});
	 }
	</script>
