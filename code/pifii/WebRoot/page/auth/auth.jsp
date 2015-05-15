<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
	<head>
<!-- 		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> -->
<!-- 		<meta http-equiv="pragma" content="no-cache">  -->
<!-- 		<meta http-equiv="cache-control" content="no-cache">  -->
<!-- 		<meta http-equiv="expires" content="0"> -->
		<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" name="viewport" />
		<meta name="apple-touch-fullscreen" content="yes">
		<meta content="yes" name="apple-mobile-web-app-capable" />
		<meta content="telephone=no" name="format-detection" />
		<meta content="email=no" name="format-detection" />
		<title>授权上网</title>
		<link href="css/auth/public.css" rel="stylesheet" type="text/css">
		<link href="css/auth/certification_style.css" rel="stylesheet" type="text/css">
		<script src="js/jquery-1.8.3.min.js" type="text/javascript"></script>
	</head>
	<body class="hasfootnav">
		<section>
			<nav class="px_nav ">
		    	<ul>
		            <li class="cur"><a href="#">手机认证</a></li>
		            <li><a href="#">微信认证</a></li>
		        </ul>
		    </nav>
		    <article class="pxnav_box">
		    	<div class="pxnav_list ">
		        	<li class="current">
		            	<article class="picScroll">
		        			<form id="myform" style="margin-top:30px;">
		                        <div class="renzheng" style="margin-top:10px;">
		                            <input type="text" value="${phone}" name="phone" placeholder="请输入手机号" class="tel" maxlength="11" minlength="11" style="color:gray;"/>
		                        </div>
		                        </br>
		                        <div class="verifyCode">
		                            <input type="text" value="" name="verifyCode" placeholder="请输入验证码" class="password" minlength="4" style="color:gray;"/>
		                            <input type="button" class="codeButton" onclick="sendPhoneValidateCode()" value="获取验证码"  />
		                        </div>
		                        <div class="home_footer" style="clear:both;">
		                            <a href="javascript:void(0)">认证</a>
		                        </div>
		                	</form>
		          			<div style="margin-bottom:10px;clear:both"></div>
		     			</article>
		            </li>
		        	<li style="display:none;">
		            	<article class="picScroll">
		                    <div>
		                      <div class="bd">
		                        <ul>
		                          <li> <a href="#"> <img src="images/auth/weixin.png" src="images/auth/blank.png" />
		                            <p>打开微信</p>
		                            </a> </li>
		                          <li style="margin-top:35px;"> <a href="#"> <img src="images/auth/guide.png" src="images/auth/blank.png" style="height:20px;width:20px;" />  
		                            </a> </li>
		                          <li> <a href="#"> <img src="images/auth/erweima.png" src="images/auth/blank.png" />
		                            <p>扫描二维码</p>
		                            </a> </li>
		                          <li style="margin-top:35px;"> <a href="#"> <img src="images/auth/guide.png" src="images/auth/blank.png" src="images/auth/blank.png" style="height:20px;width:20px;" />
		                            </a> </li>
		                          <li> <a href="#"> <img src="images/auth/wifi.png" src="images/auth/blank.png" />
		                            <p>点击上网</p>
		                            </a> </li>
		                        </ul>	
		                        <div class="home_footer auth_weixin" style="clear:both;margin-top:10px;">
		                            <a href="weixin:open">打开微信</a>
		                        </div>
		                      </div>
		                      <div style="margin-bottom:10px;clear:both"></div>
		            	</article>
		            </li>
		        </div>
		    </article>
		</section>
		<p class="wenzi_header">CopyRight @ 派路由 www.pifii.com</p>
	</body>
	<script type="text/javascript">
		var hasGetCode = false;//防止在网络慢时，连续点击
		var hasAuthed = false;//防止在网络慢时，连续点击
		$(document).ready(function() {
			initStyle();
			initEvent();
			checkAuthResult();
			checkVerifyCode();
			
			changWeixinAuthUrl();
		});
		function initStyle(){
			var bodyHeight = $(document.body).height();	
			var tabboxHeight = $('#tabbox').parent().height();
			$('#tabbox').height(tabboxHeight);
			$('.choos_list').height(bodyHeight);
			$(".pxnav_list>li:nth-of-type(2)").hide();
		}
		function initEvent(){
			$('.px_nav li').click(function(){
				$(this).addClass('cur').siblings().removeClass('cur');
				var fadeto = $('.pxnav_list').animate({opacity: '0'},50);
				if(fadeto){
					$('.pxnav_list').animate({opacity: '1'},50);
				} 
				$(".pxnav_list li:eq("+$(this).index()+")").show().siblings(".pxnav_list li").hide().addClass("current");
			});
			$( "#myform .home_footer a" ).click(function(){
				if($("#myform .renzheng").eq(0).is(":visible") && $("#myform input[name=phone]").eq(0).val().length != 11){
					alert("手机号码填写有误！");
					return false;
				} 
				if($("#myform .verifyCode").eq(0).is(":visible") && $("#myform input[name=verifyCode]").eq(0).val().length != 4){
					alert("验证码填写有误！");
					return false;
				}
				if(!hasAuthed){
					hasAuthed = true;
	//				window.location.href="authorizeAccess?phone="+$("#myform input[name=phone]").eq(0).val()+"&verifyCode="+$("#myform input[name=verifyCode]").eq(0).val();
					window.location.href="authorizeAccess?mac=${mac}&routersn=${routersn}&phone="+$("#myform input[name=phone]").eq(0).val()+"&verifyCode="+$("#myform input[name=verifyCode]").eq(0).val(); 
				}
			});
		}
		function checkAuthResult(){
			if("${hasAuthed}" == "true"){
				$( "#myform .home_footer" ).siblings().hide();
				$( "#myform .home_footer a" ).text("一键认证");
			}else{
				$( "#myform .home_footer" ).siblings().show();
			}
			if("${routeAuthSuccess}" != ''){
				if("${routeAuthSuccess}" != "true"){
					alert('${routeAuthMsg}');
				}
			}
		}
		function checkVerifyCode(){
			if("${waitSendVerifyCodeTime}" != "" && "${waitSendVerifyCodeTime}" != "null"){
				var laveSeconds = parseInt("${waitSendVerifyCodeTime}");
				if(laveSeconds>0){
					$("#myform .verifyCode").find("input[type=button]").val(laveSeconds+"秒后再次获取");
					$("#myform .verifyCode").find("input[type=button]").addClass("clickedButton");
					try{
						clearInterval(refreshLaveSecondsInterval);
					}catch(e){
					}
					refreshLaveSecondsInterval = setInterval("refreshLaveSeconds();",1000);
				}
			}else if("${sendVerifyCode}" == "true"){
				if("${sendVerifyCodeSuccess}" == "true"){
					$("#myform .verifyCode").find("input[type=button]").val("60秒后再次获取");
					$("#myform .verifyCode").find("input[type=button]").addClass("clickedButton");
					try{
						clearInterval(refreshLaveSecondsInterval);
					}catch(e){
					}
					refreshLaveSecondsInterval = setInterval("refreshLaveSeconds();",1000);
				}else{
					alert("${sendVerifyCodeMsg}");
				}
			}
		}
		var refreshLaveSecondsInterval;
		function sendPhoneValidateCode(){
			var laveSeconds = parseInt($("#myform .verifyCode").find("input[type=button]").val());
			if($("#myform input[name=phone]").eq(0).val().length == 11){
				if(isNaN(laveSeconds) || laveSeconds < 1){
					if(!hasGetCode){
						hasGetCode = true;
	//					window.location.href="sendPhoneValidateCode?phone="+$("#myform input[name=phone]").eq(0).val();
						window.location.href="advServlet?cmd=auth_validateCode&mac=${mac}&routersn=${routersn}&phone="+$("#myform input[name=phone]").eq(0).val(); 
					}
				}
			}else{
				alert("手机号码不正确！");
			}
		}
		function refreshLaveSeconds(){
			var laveSeconds = parseInt($("#myform .verifyCode").find("input[type=button]").val())-1;
			if(isNaN(laveSeconds) || laveSeconds < 1){
				$("#myform .verifyCode").find("input[type=button]").val("获取验证码");
				$("#myform .verifyCode").find("input[type=button]").removeClass("clickedButton");
				clearInterval(refreshLaveSecondsInterval);
			}else{
				$("#myform .verifyCode").find("input[type=button]").val(laveSeconds+"秒后再次获取");
			}
		}
		function changWeixinAuthUrl(){
			var userAgent = navigator.userAgent.toLowerCase();
			if(userAgent.indexOf("iphone os") == -1){
				$(".auth_weixin").find("a").attr('href',
					"http://weixin.qq.com/cgi-bin/readtemplate?uin=&stype=&fr=m.baidu.com&lang=zh_CN&check=false&t=w_down");
			}else{
<%--					$(".yijianrenzheng").find("a").click(function() {--%>
<%--						setTimeout(function(){--%>
<%--							window.location = "https://itunes.apple.com/cn/app/id414478124";--%>
<%--						},500);--%>
<%--					});--%>
			}
    	}
	</script>
</html>
