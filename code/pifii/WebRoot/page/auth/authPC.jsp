<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html >
<html >
		<head>
			<title>认证页面</title>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 			<meta http-equiv="pragma" content="no-cache">  -->
<!-- 			<meta http-equiv="cache-control" content="no-cache">  -->
<!-- 			<meta http-equiv="expires" content="0"> -->
			<link rel="stylesheet" href="css/auth/global.css"/>
			<link rel="stylesheet" href="css/auth/pifii_style.css"/>
			<script src="js/jquery-1.8.3.min.js" type="text/javascript"></script>
			<script type="text/javascript" src="js/jquery.placeholder.js"></script>
			<link rel="stylesheet" href="js/jquery-simple-modal/assets/css/simplemodal.css" type="text/css" media="screen" title="no title" charset="utf-8">
			<script src="js/jquery-simple-modal/simple-modal.js" type="text/javascript" charset="utf-8"></script>
			<script type="text/javascript" src="js/business/common.js"></script>
			<script type="text/javascript" src="js/business/commonPop.js"></script>
		</head>
	<body>
		<div class="header"><div id="logo"></div></div>
		<div id="login">
			<div class="tab">
				<div class="ka ON fl">手机认证</div>
<!-- 				<div class="ka OFF fl">微信认证</div> -->
				<div class="cl"></div>
			</div>
			<div class="box">
				<div class="inner">
					<div class="flo"><input type="text" placeholder="请输入手机号码" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="11" minlength="11"/></div>
					<div class="flo flo01"><input type="text" placeholder="请输入验证码" onkeyup="value=value.replace(/[^\d.]/g,'')" maxlength="4" minlength="4" class="numb"/><span>获取验证码</span></div>
					<div class="flo"><input type="button" value="认证"/></div>
				</div>
				<div class="inner02">
					<div class="weixin"><img src="images/auth/weixinAuthFlow.png"/></div>
					<div><input type="button" value="打开微信"></div>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			var authedPhone = $.parseJSON('${phones}');
			$(function(){
				$("#login .box .inner .flo01").hide();
				if(authedPhone.length > 0){
					$("#login .box .inner input").first().val(authedPhone[0].phone);
					checkAuthed(authedPhone[0].phone);
				}
				initEvent();
			});
			
			function initEvent(){
				$('input').placeholder();
				
				$("#login .tab .ka").eq(0).click(function(){
					$(".box .inner").show().siblings().hide();
					$(this).addClass("ON").removeClass("OFF").next().removeClass("ON").addClass("OFF");
				});
				
// 				$("#login .tab .ka").eq(1).click(function(){
// 					$(".box .inner02").show().siblings().hide();
// 					$(this).addClass("ON").removeClass("OFF").prev().removeClass("ON").addClass("OFF");
// 				});
				
				$("#login .box .inner input").first().keyup(function(){
					checkAuthed($(this).val());
				});
				
				$("#login .box .inner input[type='button']").click(function(){
					gotoAuth();
				});
				
				$("#login .box .inner span").click(function(){
					getCode();
				});
			}
			
			function gotoAuth(){
				var phone = $("#login .box .inner input").first().val();
				var code = $("#login .box .inner input").eq(1).val();
				if(phone.length != 11){
					myAlert("手机号码填写有误！");
				}else if($("#login .box .inner .flo01").is(":visible") && code.length != 4){
					myAlert("验证码填写有误！");
				}else{
					$.ajax({
						type: "GET",
						url: "authorizeAccess",
						data: {cmd:'pcAuth',phone:phone,code:code,routersn:'${routersn}',mac:'${mac}'},
						success: function(data,status,xhr){
							if(data.success == "1"){
								myAlert("认证成功!",function(){
									window.location.href=data.url; 
								});
							}else{
								myAlert(data.msg);
							}
						}
					});
				}
			}
			
			function checkAuthed(phone){
				if(phone.length == 11){
					if(authedPhone.length>0){
						for(var i=0;i<authedPhone.length;i++){
							if(phone == authedPhone[i].phone){
								$("#login .box .inner .flo01").hide();
								$("#login .box .inner input[type='button']").val("一键认证");
								return;
							}
						}
					}
					$("#login .box .inner .flo01").show();
					$("#login .box .inner input[type='button']").val("认证");
				}else{
					$("#login .box .inner .flo01").hide();
					$("#login .box .inner input[type='button']").val("认证");
				}
			}
			
			function getCode(){
				var phone = $("#login .box .inner input").first().val();
				if(phone.length != 11){
					myAlert("手机号码填写有误！");
				}else{
					$.ajax({
						type: "GET",
						url: "advServlet",
						async:false,
						data: {cmd:'pcAuth_code',phone:phone,routersn:'${routersn}',mac:'${mac}'},
						success: function(data,status,xhr){
							if(data.success == "1"){
								$("#login .box .inner span").text("60秒后可再次获取");
								//$("#login .box .inner span").css("background","rgb(139, 139, 139)");
								$("#login .box .inner span").unbind("click");
								refreshLaveSecondsInterval = setInterval("refreshLaveSeconds();",1000);
							}else{
								myAlert(data.msg);
							}
						}
					});
				}
			}
			var refreshLaveSecondsInterval;
			function refreshLaveSeconds(){
				var laveSeconds = parseInt($("#login .box .inner span").text())-1;
				if(isNaN(laveSeconds) || laveSeconds < 1){
					$("#login .box .inner span").text("获取验证码");
					//$("#login .box .inner span").css("background","#2BB5C9");
					clearInterval(refreshLaveSecondsInterval);
					$("#login .box .inner span").click(function(){
						getCode();
					});
				}else{
					$("#login .box .inner span").text(laveSeconds+"秒后可再次获取");
				}
			}
			$(document).keyup(function(e) {//监听enter事件
				var index = $.fn._maxZIndexOptionIndex();
	        	if(e.keyCode == 13 && index == -1){
	        		gotoAuth();
				}
	        });
		</script>
	</body>
</html>