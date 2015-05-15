<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
	#ad_picture_edit{
		width:620px;
	}
	#ad_picture_edit .shangchuan .button_test{
		position:relative;
		width: 170px;
		line-height:40px;
	 	text-align:center;
		color:#FFF;
		background:#1a9aff;
		border-radius:5px;
		font-weight:bold;
		cursor: pointer;
		overflow: hidden;
		display: inline-block;
	}
	#ad_picture_edit .button_test:hover{
		background:#54B4EB;
	}
	#ad_picture_edit .button_test input{
		width:3774px;
		height: 285px;
		position:absolute;
		opacity:0;
		filter: alpha(opacity=0);
		right:0;
		top:0;
		cursor:pointer;
		font-size: 40px;
	}
	/*上传按钮后来改的样式结束*/

	#ad_picture_edit .yilou{
		width: 100%;
		height: 95px;
	}
	#ad_picture_edit .yilou .wenzi{
		width: 72px;
		height: 100%;
		line-height: 75px;
		text-align: right;
		font-size:14px;
		color: #2d343b;
	}
	#ad_picture_edit .yilou .pic{
		width: 150px;
		height: 78px;
		margin-left: 17px;
/* 		margin:0 15px; */
		overflow:hidden;
	}
	#ad_picture_edit .yilou span{
		margin-top: 59px;
		margin-left: 10px;
		display: inline-block;
	}
	#ad_picture_edit .yilou .pic img{
		width: 100%;
		height: 100%;
	}
	#ad_picture_edit .yilou .shangchuan{
/* 		width: 170px; */
/* 		height: 60px; */
		padding-top: 38px;
		padding-left: 20px;
	}
	#ad_picture_edit .erlou{
		width: 100%;
		height: 65px;
	}
	#ad_picture_edit .erlou .wenzi{
		width: 72px;
		height: 100%;
		line-height:40px;
		font-size:14px;
		color: #2d343b;
		text-align: right;
	}
	#ad_picture_edit .erlou .kuang{
/* 		width: 588px; */
		height: 100%;
		align:left;
		margin-left:2px;
		text-align: left;
/* 		line-height: 65px; */
	}
	#ad_picture_edit .erlou .kuang input{
		width: 510px;
		height: 38px;
		border:1px solid #abadb3;
		line-height: 30px;
		font-size: 16px;
/* 		text-indent: 1em; */
		margin-left: 16px;
	}
	/*erlou 结束*/
	#ad_picture_edit .sanlou{
		width: 100%;
		height: 155px;
	}
	#ad_picture_edit .sanlou .shuoming{
/* 		width:588px; */
		height: 100%;
	}
	#ad_picture_edit .sanlou .wenzi{
		width: 72px;
		height: 100%;
		line-height:95px;
		font-size:14px;
		color: #2d343b;
		text-align: right;
	}
	#ad_picture_edit .sanlou .shuoming textarea{
		width: 510px;
		font-size: 17px;
/* 		text-indent: 1em; */
		text-align: left;
		margin-left: 16px;
	}/*三楼结束*/
	#ad_picture_edit .silou{
		width: 100%;
		height: 60px;
		text-align: center;
	}
	#ad_picture_edit .silou input{
		width: 138px;
		height: 35px;
		border-radius: 3px;
		border: 1px solid #0080e3;
		background: #2da2ff;
		cursor: pointer;
		text-align: center;
		color: #fff;
		font-weight: bold;
		font-size: 14px;
		margin-right: 15px;
	}
	#ad_picture_edit .silou input:hover{
		background:#54B4EB;
	}
</style>	
<div id="ad_picture_edit">
	<form id="adv_form" action="business/app/adv/add" method="post" enctype="multipart/form-data">
			<div class="erlou">
				<div class="wenzi fl">名称：</div>
				<div class="kuang f1"><input type="text" name="name" value="${name}"/></div>
				<div class="cl"></div>
			</div>
			<div class="yilou">
				<div class="wenzi fl">图片：</div>
				<div class="pic fl"><img id ="adv_img" src="<c:if test='${empty img}'>aa.jpg</c:if><c:if test='${not empty img}'>${img}</c:if>" onerror="this.src='images/business/ad-1.jpg'" /></div>
				<div class="shangchuan fl">
					<div class="button_test"><input type="file" width="640" height="330"  id="uploadImage"  name="upload" />本地上传</div>
				</div>
				<span>建议尺寸：${imgWidth}px*${imgHeight}px</span>
				<div class="cl"></div>
			</div>
			<input type="hidden" name="id" value="${id}" >
			<input type="hidden" name="shopId">
			<input type="hidden" name="advSpacesId" value="${advSpacesId}">
			<input type="hidden" name="advContentId" value="${advContentId}">
			<input type="hidden" name="img" value="${img}">
<!-- 			<input type="hidden" name="saveImgType" id="saveImgType" value="0"> -->
			<div class="erlou">
				<div class="wenzi fl">链接地址：</div>
				<div class="kuang f1"><input type="text" name="link" value="${link}"/></div>
				<div class="cl"></div>
			</div>
			<div class="sanlou">
				<div class="wenzi fl">说明：</div>
				<div class="shuoming f1">
					<textarea rows="6" cols="" name="des" >${des}</textarea>
				</div>
				<div class="cl"></div>
			</div>
			<div class="silou">
				<input type="button" onclick="submitInfo()" value="确定"/>
				<input type="button" onclick="closePop();" value="取消">
			</div>
	</form>
</div>
<script type="text/javascript">
	$("#ad_picture_edit .yilou .pic").css("height",'${imgHeight}'/'${imgWidth}'*$("#ad_picture_edit .yilou .pic").width());
	$("#uploadImage").uploadPreview({ Img: $("#adv_img")});
	function submitInfo(){
		var name = $("#ad_picture_edit form input[name='name']").val();
		if(name == ""){
			myAlert("名称必须填写！");
			return;
		}
		var localImgPath = $("#ad_picture_edit form input[name=upload]").val();
		if("${id}"=="" && localImgPath==""){
			myAlert("请上传图片！");
			return;
		}
		var link = $("#ad_picture_edit form input[name='link']").val();
		if(link && link.length > 255){
			myAlert("链接地址字符太长！");
			return;
		}
//         if (link!="" && !IsURL(link)){
//         	myAlert("网址格式不正确！");
// 			return;
//         }
        $("#adv_form input[name='id']").val('${id}');
        $("#adv_form input[name='shopId']").val(getSelectedShopId());
        $("#adv_form input[name='advSpacesId']").val('${advSpacesId}');
        $("#adv_form input[name='advContentId']").val('${advContentId}');
		$("#adv_form").ajaxSubmit({
			success: function(resp){
				closePop();
				reloadAdvs();
			}
		});
	}
</script>