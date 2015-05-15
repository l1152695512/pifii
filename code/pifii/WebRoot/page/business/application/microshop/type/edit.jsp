<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
	#microShopTypeInfo .content_list{padding:0;margin-bottom:10px;overflow: hidden;white-space: nowrap;}
	#microShopTypeInfo .content_list>span{float: left;width:60px;height:30px;line-height:30px;color:gray;font-size:15px;font-weight:bold;text-align:right;display:inline-block;}
	#microShopTypeInfo .content_list input{ border:1px solid #b4c0ce; color:#666;margin-left:20px; width:300px; height:30px; line-height:30px;border-radius:3px;}
	#microShopTypeInfo .content_list select{outline:none;width:304px;color: rgb(105, 100, 100);height:30px;margin-left: 20px;line-height:30px;border: 1px solid #B4C0CE;font-weight:bold;border-radius:4px;}
	#microShopTypeInfo .content_list textarea{ border:1px solid #b4c0ce;color:#666;width:298px;margin-left: 20px; height:60px;border-radius:3px;overflow:hidden;}
/* 	#microShopTypeInfo .content_list img{width:140px;height:80px;overflow:hidden;margin-left: 20px;} */
	#microShopTypeInfo .content_list .tupian{overflow: hidden;margin-left: 20px;}
 	#microShopTypeInfo .content_list .tupian img{width:110px;height:100px;}
 	#microShopTypeInfo .content_list .add_icon{width: 40px;position: relative;top: 13px;left: 10px;cursor: pointer;}
 	#microShopTypeInfo .content_list .info_type{height: 100px;max-width: 303px;display: inline-block;margin-left: 20px;overflow-x: auto;}
 	#microShopTypeInfo .content_list .info_type li{display: inline-block;text-align: center;margin-right: 10px;cursor: pointer;border-radius: 3px;}
 	#microShopTypeInfo .content_list .info_type li:hover{background:#52B0FC;color: white;}
 	#microShopTypeInfo .content_list .info_type li.selected{background:#52B0FC;color: white;}
 	#microShopTypeInfo .content_list .info_type li img{width: 60px;height: 60px;}
 	
	#microShopTypeInfo .content_list .line_height_30{height:30px;line-height:30px;}
/* 	#microShopTypeInfo .content_list .line_height_60{height:60px;line-height:60px;} */
	#microShopTypeInfo .content_list .line_height_80{height:80px;line-height:80px;}
	
	#microShopTypeInfo .upload_div{position: relative;display: inline-block;}
	#microShopTypeInfo .upload_div span{position: absolute;top: 33px;left:4px;}
	#microShopTypeInfo .button_upload{overflow: hidden;position:relative;height:40px;line-height:40px; text-align:center;color:#FFF; background:#31a3ff;display:inline-block;border-radius:4px; font-weight:bold;top:60px;left:12px;cursor: pointer;}
	#microShopTypeInfo .button_upload:hover{background:#52B0FC;}
	#microShopTypeInfo .button_upload.upload_input{width:100px;}
	#microShopTypeInfo .button_upload.choice{width:70px;}
	
	#microShopTypeInfo .button_upload input{width:3774px;height: 285px;filter: alpha(opacity=0);font-size: 40px;position:absolute;opacity:0;right:0;top:0;cursor:pointer;}
	
	#microShopTypeInfo .content_btn1{margin-top:20px;margin-left:120px;float:left;}
	#microShopTypeInfo .content_btn1 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
	#microShopTypeInfo .content_btn2{float:left;margin-top:20px;margin-left:10px;}
	#microShopTypeInfo .content_btn2 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
</style>

<div id="microShopTypeInfo">
	<form action="${cxt}/business/app/microshop/saveMicroShopType"  method="post" enctype="multipart/form-data">
        <div class="tanchuceng_content">
        	<div class="close"></div>
        	<div class="content_list">
            	<span class="line_height_30">名称：</span><input type="text" name="microShopType.name" value="${info.name}" />
            </div>
            <div class="content_list">
            	<span class="line_height_80">图标：</span>
            	<div>
	            	<div class="tupian fl">
		            	<img src="<c:if test='${empty info.img}'>aa.jpg</c:if><c:if test='${not empty info.img}'>${info.img}</c:if>" onerror="this.src='images/business/ad-1.jpg'"/>
	            	</div>
	            	<div class="upload_div">
	            		<span>&nbsp;&nbsp;建议尺寸：110px * 100px</span>
	                	<div class="button_upload upload_input"><input name="upload" type="file" />本地上传</div>
	                	<div class="button_upload choice" onclick="choice_icon();">选择</div>
	                </div>
            	</div>
                <div class="cl"></div>
            </div>
            <div class="content_list">
            	<span class="line_height_30">链接：</span>
            	<input type="text" name="microShopType.link" value="${info.link}" />
                <div class="cl"></div>
            </div>
            <div class="content_btn1">
            	 <a href="javascript:void(0)" id="sva_info"  onclick="submitMicroShopTypeInfoInfo()" ><span>确定</span>
            </div>
            <div class="content_btn2">
            	<a href="javascript:void(0)" onclick="closePop();">
            	<span>取消</span>
                </a>
            </div>
        </div>
        <input name="microShopType.id" type="hidden" value="${info.id}" />
        <input name="microShopType.icon" type="hidden" value="${info.img}" />
		<input name="microShopType.shop_id" type="hidden" value="" />
	</form>
</div>
<script type="text/javascript">
	$(function() {
		$("#microShopTypeInfo form input[name='microShopType.shop_id']").val(getSelectedShopId());
		$("#microShopTypeInfo form input[name=upload]").uploadPreview({ Img: $("#microShopTypeInfo form .tupian").children("img")});
	});
	function choice_icon(){
		$.fn.SimpleModal({
			title : "图标库",
			width : 500,
			param : {
				url : 'business/app/microshop/getSystemIcon'
			}
		}).showModal();
	}
	function submitMicroShopTypeInfoInfo(){
		var name = $("#microShopTypeInfo form input[name='microShopType.name']").val();
		if(name.length==0){
			myAlert("名称不能为空！");
			return;
		}
		var localImgPath = $("#microShopTypeInfo form input[name=upload]").val();
		var thisImg = $("#microShopTypeInfo form input[name='microShopType.icon']").val();
		if(localImgPath=="" && thisImg==''){
			myAlert("请上传图片！");
			return;
		}
		var link = $("#microShopTypeInfo form input[name='microShopType.link']").val();
        if (link!="" && !IsURL(link)){
        	myAlert("网址格式不正确！");
			return;
        }
		$("#microShopTypeInfo form input[name='microShopType.shop_id']").val(getSelectedShopId());
		$("#microShopTypeInfo form").ajaxSubmit({
			success: function(resp){
    			closePop();
    			getMicroShopTypes();
			}
	  	});
	 }
</script>