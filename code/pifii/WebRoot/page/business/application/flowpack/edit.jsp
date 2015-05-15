<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style type="text/css">
	#flow_pack_edit .row{padding:0;margin-bottom:10px;}
	#flow_pack_edit .row span{float: left;width:100px;height:30px;line-height:30px;color:gray;font-size:15px;font-weight:bold;text-align:right;display:inline-block;}
	#flow_pack_edit .row input{ border:1px solid #b4c0ce; color:#666;margin-left:20px; width:300px; height:30px; line-height:30px;border-radius:3px;}
	#flow_pack_edit .row select{outline:none;width:304px;color: rgb(105, 100, 100);height:30px;margin-left: 20px;line-height:30px;border: 1px solid #B4C0CE;font-weight:bold;border-radius:4px;}
	#flow_pack_edit .row textarea{ border:1px solid #b4c0ce;color:#666;width:298px;margin-left: 20px; height:60px;border-radius:3px;overflow:hidden;}
	#flow_pack_edit .row .tupian{width:140px;height:80px;overflow: hidden;margin-left: 20px;}
 	#flow_pack_edit .row .tupian img{width:100%;height:100%;}
/* 	#flow_pack_edit .row .line_height_30{height:30px;line-height:30px;} */
	#flow_pack_edit .row .line_height_60{height:60px;line-height:60px;}
	#flow_pack_edit .row .line_height_80{height:80px;line-height:80px;}
	
	#flow_pack_edit .button_upload{position:relative;height:40px;line-height:40px;width:157px; text-align:center;color:#FFF; background:#31a3ff;border-radius:4px; font-weight:bold;top:40px;left:7px;overflow: hidden;margin:0;padding:0;}
	#flow_pack_edit .button_upload:hover{background:#52B0FC;}
	#flow_pack_edit .button_upload input{width:3774px;height: 285px;position:absolute;opacity:0;filter: alpha(opacity=0);right:0;top:0;cursor:pointer;font-size: 40px;}
	
	#flow_pack_edit .content_btn1{margin-top:20px;margin-left:120px;float:left;}
	#flow_pack_edit .content_btn1 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
	#flow_pack_edit .content_btn2{float:left;margin-top:20px;margin-left:10px;}
	#flow_pack_edit .content_btn2 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
</style>
<div id="flow_pack_edit">
	<form action="business/app/flowpack/save" method="post" enctype="multipart/form-data">
		<div>
			<div class="row">
				<span>标题：</span>
				<input type="text" name="pageData.title"  value="${pageData.title}"/>
			</div>
			<div class="row">
				<span class="line_height_80">图片：</span> 
				<div class="tupian fl">
					<img src="<c:if test='${empty pageData.pic}'>aa.jpg</c:if><c:if test='${not empty pageData.pic}'>${pageData.pic}</c:if>" onerror="this.src='images/business/ad-1.jpg'" />
				</div>
				<div class="button_upload">
					<input type="file" name="upload"/>本地上传
				</div>
				<div class="cl"></div>
			</div>
			<div class="row">
				<span class="line_height_60">描述：</span>
				<textarea name="pageData.des" cols="5" rows="5">${pageData.des}</textarea>
			</div>
			<div class="content_btn1">
				<!-- <input type="submit"  value="确定">  -->
				<a href="javascript:void(0)" onclick="submitPage();"><span>确定</span></a>
			</div>
			<div class="content_btn2">
				<a href="javascript:void(0)" onclick="closePop();"><span>取消</span></a>
			</div>
		</div>
		<input name="pageData.id" type="hidden" value="${pageData.id}" />
		<input name="pageData.shop_id" type="hidden" value="${pageData.shop_id}" />
	</form>
</div>
<script type="text/javascript">
	$(function() {
		var thisShopId = '${pageData.shop_id}';
		if(thisShopId == ''){//为添加
			var shopId = getSelectedShopId();
			$("#flow_pack_edit form input[name='pageData.shop_id']").attr("value",shopId);
		}
		$("#flow_pack_edit form input[name=upload]").uploadPreview({ Img: $("#flow_pack_edit form .tupian").children("img")});
	});
	function submitPage(){
		var title = $("#flow_pack_edit form input[name='pageData.title']").attr("value");
		if(title.length==0){
			myAlert("标题不能为空！");
			return;
		}
		var localImgPath = $("#flow_pack_edit form input[name=upload]").val();
		if("${pageData.id}"=="" && localImgPath==""){
			myAlert("请上传图片！");
			return;
		}
		 $("#flow_pack_edit form").ajaxSubmit({
		       success: function(resp){
		    	   if(resp.msg){
			    		myAlert(resp.msg);
			    	}else{
			    		closePop();
		    			getFlowPackList();
			    	}
		       }
		  	});
	 }
	</script>
