<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
/* 	#introduce{width:500px;} */
	#introduce .row{margin-bottom: 20px;}
	#introduce .row>span{float: left;width:60px;height:30px;line-height:30px;color:gray;font-size:15px;font-weight:bold;text-align:right;display:inline-block;}
	#introduce .row>textarea{ border:1px solid #b4c0ce;color:#666;width:413px;margin-left: 20px; height:80px;border-radius:3px;overflow:hidden;}
	
	#introduce .row .tupian{overflow: hidden;margin-left: 20px;display: none;}
 	#introduce .row .tupian img{width:50px;height:50px;}
/* 	#introduce .row img{width:50px;height:50px;overflow:hidden;margin-left: 20px;display: none;position: relative;top: 12px;} */
	#introduce .row .line_height_60{height:60px;line-height:60px;}
	#introduce .row .line_height_80{height:80px;line-height:80px;}
	
	#introduce .row .tips{margin-top: 28px;display: inline-block;}
	
	#introduce .button_upload{overflow: hidden;position:relative;height:40px;top: 8px;line-height:40px;width:157px; text-align:center;color:#FFF; background:#31a3ff;border-radius:4px; font-weight:bold;margin-left: 20px;}
	#introduce .button_upload:hover{background:#52B0FC;}
	#introduce .button_upload input{width:3774px;height: 285px;filter: alpha(opacity=0);font-size: 40px;position:absolute;opacity:0;right:0;top:0;cursor:pointer;}
	
	#introduce .content_btn1{margin-top:20px;margin-left:120px;float:left;}
	#introduce .content_btn1 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
	#introduce .content_btn2{float:left;margin-top:20px;margin-left:10px;}
	#introduce .content_btn2 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
</style>

<div id="introduce">
	<form action="${cxt}/business/app/introduce/save"  method="post" enctype="multipart/form-data">
		<div class="row">
       		<span class="line_height_60">文件：</span>
       		<div>
       			<div class="tupian fl">
       				<img src="images/business/videoPreview.png"/>
       			</div>
	            <div class="button_upload fl">
	            	<input name="upload" type="file" accept=".mp4"/>本地上传
	            </div>
	            <span class="tips">&nbsp;&nbsp;&nbsp;*必须为20M以内的mp4视频</span>
	            <div class="cl"></div>
       		</div>
        </div>
		<div class="row">
			<span class="line_height_80">描述：</span>
			<textarea name="des" placeholder="500字以内" cols="5" rows="8">${des}</textarea>
		</div>
		<div class="content_btn1">
			<a href="javascript:void(0)" onclick="submitInfo()"><span>确定</span></a>
		</div>
		<div class="content_btn2">
			<a href="javascript:void(0)" onclick="closePop()"><span>取消</span></a>
		</div>
        <input name="id" type="hidden" value="" />
		<input name="shopId" type="hidden" value=""/>
	</form>
</div>
<script type="text/javascript">
	if("${id}" != "" && "${filePath}" != ""){
		$("#introduce .row .button_upload").prev("div").show();
	}
	$("#introduce form input[name=upload]").change(function() {
		$("#introduce .row .button_upload").prev("div").show();
	});
	function submitInfo(){
		$("#introduce input[name=id]").val("${id}");
		if("${shopId}" != ""){
			$("#introduce input[name=shopId]").val("${shopId}");
		}else{
			$("#introduce input[name=shopId]").val(getSelectedShopId());
		}
		$("#introduce form").ajaxSubmit({
			success: function(resp){
		    	if(resp.msg){
		    		myAlert(resp.msg);
		    	}else{
		    		closePop();
		    	}
			}
		});
	 }
</script>