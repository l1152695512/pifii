<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
	#customApp .content_list{padding:0;margin-bottom:10px;overflow: hidden;white-space: nowrap;}
	#customApp .content_list>span{float: left;width:60px;height:30px;line-height:30px;color:gray;font-size:15px;font-weight:bold;text-align:right;display:inline-block;}
	#customApp .content_list input{ border:1px solid #b4c0ce; color:#666;margin-left:20px; width:300px; height:30px; line-height:30px;border-radius:3px;}
	#customApp .content_list .line_height_30{height:30px;line-height:30px;}
	
	#customApp .content_btn1{margin-top:20px;margin-left:65px;float:left;}
	#customApp .content_btn1 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
	#customApp .content_btn2{float:left;margin-top:20px;margin-left:10px;}
	#customApp .content_btn2 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
</style>

<div id="customApp">
	<form action="${cxt}/business/app/customapp/save" method="post">
        <div>
        	<div class="content_list">
            	<span class="line_height_30">链接地址：</span>
            	<input type="text" name="customApp.url" value="${customApp.url}" placeholder="http://www.pifii.com/"/>
            </div>
            <div class="content_btn1">
            	 <a href="javascript:void(0)" id="sva_info"  onclick="submitCustomAppInfo()" ><span>确定</span>
            </div>
            <div class="content_btn2">
            	<a href="javascript:void(0)" onclick="closePop();">
            	<span>取消</span>
                </a>
            </div>
        </div>
		<input name="customApp.shop_id" type="hidden" value="" />
		<input name="customApp.id" type="hidden" value="${customApp.id}" />
	</form>
</div>
<script type="text/javascript">
	$(function() {
		$("#customApp form input[name='customApp.shop_id']").val(getSelectedShopId());
	});
	function submitCustomAppInfo(){
		var link = $("#customApp form input[name='customApp.link']").val();
        if (link!="" && !IsURL(link)){
        	myAlert("网址格式不正确！");
			return;
        }
		$("#customApp form input[name='customApp.shop_id']").val(getSelectedShopId());
		$("#customApp form").ajaxSubmit({
			success: function(resp){
    			closePop();
			}
	  	});
	 }
</script>