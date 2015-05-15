<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
	/*更改用户头像  弹出头像*/
	#change_user_picture_form>div.first_children{
/* 		width: 375px;	 */
/* 		height: 228px; */
		padding-left:85px;
		left: 37%;
		top:220px;
		z-index: 21;/*比透明层高11  透明层是10 */
	}
	#change_user_picture_form .row{
		font-size: 14px;
		color: #2d343b;
		margin-bottom: 20px;
	}
	#change_user_picture_form .row>span{
		text-align: right;
		width: 75px;
		float: left;
		line-height: 30px;
	}
	#change_user_picture_form .user_pic{
		line-height: 75px !important;
	}
	#change_user_picture_form input[type=text]{
		height:28px;
		width: 230px;
		border: 1px solid #CDCDCD;
		*line-height:30px;
	}
	#change_user_picture_form .wenzi{
		font-size: 14px;
		color: #2d343b;
		width: 71px;
		height:83px;
		line-height: 83px;
		overflow: hidden;
	}
	#change_user_picture_form .tupian{
		width: 65px;
		height: 65px;
		border:1px solid #c1c1c1;
		overflow: hidden;
		margin-top: 8px;
	}
	#change_user_picture_form .tupian img{
		width: 100%;
		height: 100%;
	}
	#change_user_picture_form .shangchuan{
		width: 154px;
		padding: 16px 10px 0px;
		font-size: 14px;
		color: #5b646f;
	}
	#change_user_picture_form .shangchuan input{
		width: 154px;
		height: 40px;
	}
	#change_user_picture_form .shangchuan .button_test{
		position:relative;
		line-height:40px;
	 	text-align:center;
		color:#FFF;
		background:#1a9aff;
		border-radius:5px;
		font-weight:bold;
		cursor: pointer;
		overflow: hidden;
	}
	#change_user_picture_form .shangchuan .button_test:hover{
		background:#54B4EB;
	}
	#change_user_picture_form .shangchuan .button_test input{
		width:3774px;
		height: 285px;
		position:absolute;
		opacity:0;
		filter: alpha(opacity=0);
		right:0;
		top:0;
		cursor:pointer;
		font-size: 30px;
	}
	#change_user_picture_form .foot{
		width: 100%;
		height: 40px;
		margin-top:40px;
	}
	#change_user_picture_form .foot input{
		width: 138px;
		height: 35px;
		border-radius: 3px;
		border:1px solid #0284e4;
		margin-right: 10px;
		background: #1a9aff;
		font-size: 16px;
		font-weight: bold;
		color: #fff;
		cursor: pointer;
	}
	
</style>
<form id="change_user_picture_form" action="${pageContext.request.contextPath}/business/shop/saveShopInfo" method="post" enctype="multipart/form-data">
	<div class="first_children">
		<div class="row">
			<span class="user_pic">图片：</span>
			<div class="fl">
				<div class="tupian fl">
					<img id="userImg"
						src="<c:if test='${empty shop.icon}'>aa.jpg</c:if><c:if test='${not empty shop.icon}'>${shop.icon}</c:if>"
						onerror="this.src='images/business/userPic/morentouxiang.png'" />
				</div>
				<div class="shangchuan fl">
					<span>建议尺寸：65*65</span>
					<div class="button_test">
						<input type="file" name="upload" id="userImage"/>本地上传
					</div>
				</div>
			</div>
			<div class="cl"></div>
		</div>
		<div class="row">
			<span>商铺名称：</span>
			<input type="text" name="shop.name" value="${shop.name}" />
		</div>
		<div class="row">
			<span>商铺地址：</span>
			<input type="text" name="shop.addr" value="${shop.addr}" />
		</div>
		<div class="row">
			<span>联系电话：</span>
			<input type="text" name="shop.tel" value="${shop.tel}" />
		</div>
		<div class="cl"></div>
	</div>
	<div class="foot" align="center" >
		<input style="text-align: center;" onclick="submitInfo()"  value="确定" />
	</div>
	<input type="hidden"  name="shop.id" value="${shop.id}" />
</form>
<script type="text/javascript">
	function submitInfo(){
		var name = $("#change_user_picture_form input[name='shop.name']").val();
		if(name.length > 100){
			myAlert("商铺名称的字符长度不能超过100！");
			return;
		}
		var address = $("#change_user_picture_form input[name='shop.addr']").val();
		if(address.length > 100){
			myAlert("商铺地址的字符长度不能超过100！");
			return;
		}
		var tel = $("#change_user_picture_form input[name='shop.tel']").val();
		if(tel.length > 20){
			myAlert("联系电话的字符长度不能超过20！");
			return;
		}
		$("#change_user_picture_form").ajaxSubmit({
		       success: function(resp){
	    			closePop();
	    			if(typeof resp === "string"){
	    				resp = $.parseJSON(resp);
	    			}
	    			if(resp.icon != undefined){
	    				$("#mainPageHeader .headerLeft .logo.select img").attr("src",resp.icon);
	    			}
	    			if(resp.name!= undefined){
	    				$("#mainPageHeader .selectname .selected_shop p").text(resp.name).data("shopId",resp.id);
	    			}
	    			$("#mainPageHeader .headerLeft .shop_list ul li").each(function(){
	    				if(resp.id == $(this).data("shopId")){
	    					if(resp.icon != undefined){
	    						$(this).find("img").attr("src",resp.icon);
	    	    			}
	    	    			if(resp.name!= undefined){
	    	    				$(this).children("p").text(resp.name);
	    	    			}
	    				}
	    			});
	    			refreshPhonePagePreview("info");
		       }
		  	});
	}
// 	$("#userImage").on("change", function() {
// 		var files = !!this.files ? this.files : [];
// 		if (!files.length || !window.FileReader)
// 			return;
// 		if (/^image/.test(files[0].type)) {
// 			var reader = new FileReader();
// 			reader.readAsDataURL(files[0]);
// 			reader.onloadend = function() {
// 				$("#userImg").attr("src", this.result);
// 			};
// 		}
// 	});
	$("#userImage").uploadPreview({ Img: $("#userImg")});
</script>