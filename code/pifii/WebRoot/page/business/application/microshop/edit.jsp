<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
	#microShopInfo .content_list{padding:0;margin-bottom:10px;overflow: hidden;white-space: nowrap;}
	#microShopInfo .content_list>span{float: left;width:60px;height:30px;line-height:30px;color:gray;font-size:15px;font-weight:bold;text-align:right;display:inline-block;}
	#microShopInfo .content_list input{ border:1px solid #b4c0ce; color:#666;margin-left:20px; width:300px; height:30px; line-height:30px;border-radius:3px;}
	#microShopInfo .content_list select{outline:none;width:304px;color: rgb(105, 100, 100);height:30px;margin-left: 20px;line-height:30px;border: 1px solid #B4C0CE;font-weight:bold;border-radius:4px;}
	#microShopInfo .content_list textarea{ border:1px solid #b4c0ce;color:#666;width:298px;margin-left: 20px; height:60px;border-radius:3px;overflow:hidden;}
/* 	#microShopInfo .content_list img{width:140px;height:80px;overflow:hidden;margin-left: 20px;} */
	#microShopInfo .content_list.type_content{position: relative;}
	#microShopInfo .content_list .tupian{margin-left: 20px;}
 	#microShopInfo .content_list .tupian img{width:150px;height:120px;}
 	#microShopInfo .content_list .add_icon{width: 63px;position: absolute;top: 20px;cursor: pointer;margin-left: 15px;}
 	#microShopInfo .content_list .info_type{height: 100px;max-width: 227px;display: inline-block;margin-left: 20px;overflow-x: auto;}
 	#microShopInfo .content_list .info_type li{display: inline-block;text-align: center;margin-right: 10px;cursor: pointer;border-radius: 3px;}
 	#microShopInfo .content_list .info_type li:hover{background:#52B0FC;color: white;}
 	#microShopInfo .content_list .info_type li.selected{background:#52B0FC;color: white;}
 	#microShopInfo .content_list .info_type li img{width: 60px;height: 60px;}
 	
	#microShopInfo .content_list .line_height_30{height:30px;line-height:30px;}
/* 	#microShopInfo .content_list .line_height_60{height:60px;line-height:60px;} */
	#microShopInfo .content_list .line_height_80{height:80px;line-height:80px;}
	#microShopInfo .content_list .line_height_100{height:100px;line-height:100px;}
	
	#microShopInfo .upload_div{position: relative;}
	#microShopInfo .upload_div span{position: absolute;top: 50px;}
	#microShopInfo .button_upload{overflow: hidden;position:absolute;height:40px;line-height:40px;width:150px; text-align:center;color:#FFF; background:#31a3ff;display:inline-block;border-radius:4px; font-weight:bold;top:78px;left: 235px;}
	#microShopInfo .button_upload:hover{background:#52B0FC;}
	#microShopInfo .button_upload input{width:3774px;height: 285px;filter: alpha(opacity=0);font-size: 40px;position:absolute;opacity:0;right:0;top:0;cursor:pointer;}
	
	#microShopInfo .content_btn1{margin-top:20px;margin-left:120px;float:left;}
	#microShopInfo .content_btn1 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
	#microShopInfo .content_btn2{float:left;margin-top:20px;margin-left:10px;}
	#microShopInfo .content_btn2 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
</style>

<div id="microShopInfo">
	<form action="${cxt}/business/app/microshop/saveMicroShopInfo"  method="post" enctype="multipart/form-data">
        <div class="tanchuceng_content">
        	<div class="close"></div>
        	<div class="content_list">
            	<span class="line_height_30">标题：</span><input type="text" name="microShop.title" value="${info.title}" />
            </div>
            <div class="content_list type_content">
            	<span class="line_height_80">类型：</span>
            	<div class="info_type">
            		<ul></ul>
            	</div>
            	<img class="add_icon" title="添加类型" onclick="addMicroShopType();" alt="" src="images/app/microShop/add_micro_shop_type.png">
            </div>
            <div class="content_list">
            	<span class="line_height_100">图片：</span>
            	<div>
	            	<div class="tupian fl">
		            	<img src="<c:if test='${empty info.img}'>aa.jpg</c:if><c:if test='${not empty info.img}'>${info.img}</c:if>" onerror="this.src='images/business/ad-1.jpg'"/>
	            	</div>
	            	<div class="upload_div">
	            		<span>&nbsp;&nbsp;建议尺寸：150px * 120px</span>
	                	<div class="button_upload"><input name="upload" type="file" />本地上传</div>
	                </div>
            	</div>
                <div class="cl"></div>
            </div>
            <div class="content_list">
            	<span class="line_height_30">原价：</span><input type="text" onkeyup="value=value.replace(/[^\d.]/g,'')" name="microShop.old_price" value="${info.old_price}" /><b>元</b>
            </div>
            <div class="content_list">
            	<span class="line_height_30">现价：</span><input type="text" onkeyup="value=value.replace(/[^\d.]/g,'')" name="microShop.new_price" value="${info.new_price}"/><b>元</b>
            </div>
             <div class="content_list">
            	<span class="line_height_30">链接：</span><input type="text" name="microShop.link" value="${info.link}"/>
            </div>
            <div class="content_list">
				<span class="line_height_80">描述：</span>
				<textarea name="microShop.remark" placeholder="500字以内" cols="5" rows="8">${info.remark}</textarea>
			</div>
            <div class="content_btn1">
            	 <a href="javascript:void(0)" id="sva_info"  onclick="submitInfo()" ><span>确定</span>
            </div>
            <div class="content_btn2">
            	<a href="javascript:void(0)" onclick="closePop();">
            	<span>取消</span>
                </a>
            </div>
        </div>
        <input name="microShop.id" type="hidden" value="${info.id}" />
		<input name="microShop.shop_id" type="hidden" value="" />
		<input name="microShop.type" type="hidden" value="${info.type}" />
	</form>
</div>
<script type="text/javascript">
	$(function() {
		getMicroShopTypes();
		$("#microShopInfo form input[name='microShop.shop_id']").val(getSelectedShopId());
		$("#microShopInfo form input[name=upload]").uploadPreview({ Img: $("#microShopInfo form .tupian").children("img")});
	});
	function addMicroShopType(){
		$.fn.SimpleModal({
			title : "产品类型添加",
			width : 500,
			param : {
				url : 'business/app/microshop/editMicroShopType'
			}
		}).showModal();
	}
	function getMicroShopTypes(){
		$.ajax({
    		type: "POST",
    		data:{shopId:getSelectedShopId()},
    		url: "business/app/microshop/getMicroShopType",
    		success: function(data,status,xhr){
				var selectTypes = 0;
    			var typesHtml = "";
    			for(var i=0;i<data.length;i++){
    				if(data[i].id == "${info.type}"){
    					selectTypes = i;
    				}
    				typesHtml += '<li data-id="'+data[i].id+'">' +
				    				'<div><img alt="" src="'+data[i].icon+'"></div>' +
				    				'<span>'+data[i].name+'</span>' +
				    			'</li>';
    			}
				$("#microShopInfo .content_list .info_type ul").html(typesHtml);
				$("#microShopInfo .content_list .info_type li").click(function(){
					$(this).addClass("selected").siblings().removeClass("selected");
					$("#microShopInfo form input[name='microShop.type']").val($(this).data("id"));
				});
				if(data.length > 0){
					$("#microShopInfo .content_list .info_type li").eq(selectTypes).click();
				}
    		}
    	});
	}
	function submitInfo(){
		var name = $("#microShopInfo form input[name='microShop.title']").val();
		if(name.length==0){
			myAlert("标题不能为空！");
			return;
		}
		var type = $("#microShopInfo form input[name='microShop.type']").val();
		if(type==0){
			myAlert("请选择类型！");
			return;
		}
		var localImgPath = $("#microShopInfo form input[name=upload]").val();
		if("${info.id}"=="" && localImgPath==""){
			myAlert("请上传图片！");
			return;
		}
		var oldPrice = $("#microShopInfo form input[name='microShop.old_price']").val();
		if(oldPrice.length==0){
			myAlert("原价不能为空！");
			return;
		}else if(parseFloat(oldPrice)<0){
			myAlert("原价不能小于0！");
			return;
		}
		var newPrice = $("#microShopInfo form input[name='microShop.new_price']").val();
		if(newPrice.length==0){
			myAlert("现价不能为空！");
			return;
		}else if(parseFloat(newPrice)<0){
			myAlert("现价不能小于0！");
			return;
		}
		var link = $("#microShopInfo form input[name='microShop.link']").val();
        if (link!="" && !IsURL(link)){
        	myAlert("网址格式不正确！");
			return;
        }
        var remark = $("#microShopInfo form input[name='microShop.remark']").val();
        if (remark && remark.length > 1000){
        	myAlert("描述不能超过1000个字符！");
			return;
        }
		$("#microShopInfo form input[name='microShop.shop_id']").val(getSelectedShopId());
		$("#microShopInfo form").ajaxSubmit({
			success: function(resp){
    			closePop();
    			generMicroContentPager();
			}
	  	});
	 }
</script>