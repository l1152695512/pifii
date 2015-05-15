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
		<link href="${pageContext.request.contextPath}/portal/mb/auth/css/public.css" rel="stylesheet" type="text/css">
		<link href="${pageContext.request.contextPath}/portal/mb/auth/css/certification_style.css" rel="stylesheet" type="text/css">
		<script src="${pageContext.request.contextPath}/portal/commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
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
		        			<form id="myform" style="margin-top:30px;" action="" method="post">
		                        <div class="renzheng" style="margin-top:10px;">
		                            <input type="text" value="${phone}" type="number" name="phone" onkeyup="value=value.replace(/[^\d.]/g,'')" placeholder="请输入手机号" class="tel" maxlength="11" minlength="11" style="color:gray;"/>
		                        </div>
		                        </br>
		                        <div class="verifyCode">
		                            <input type="text" value="" name="verifyCode" placeholder="请输入验证码" class="password" maxlength="4" minlength="4" style="color:gray;"/>
		                            <input type="button" class="codeButton" onclick="sendPhoneValidateCode()" value="获取验证码"  />
		                        </div>
		                        <div class="home_footer" style="clear:both;">
		                            <a href="javascript:void(0)">认证</a>
		                        </div>
		                        <input type="hidden" name="cmd"/>
		                	</form>
		          			<div style="margin-bottom:10px;clear:both"></div>
		     			</article>
		            </li>
		        	<li style="display:none;">
		            	<article class="picScroll">
		                      <div class="bd">
		                        <ul>
		                          <li> <a href="#"> <img src="${pageContext.request.contextPath}/portal/mb/auth/img/weixin.png"/>
		                            <p>打开微信</p>
		                            </a> </li>
		                          <li style="margin-top:35px;"> <a href="#"> <img src="${pageContext.request.contextPath}/portal/mb/auth/img/guide.png" style="height:20px;width:20px;" />  
		                            </a> </li>
		                          <li> <a href="#"> <img src="${pageContext.request.contextPath}/portal/mb/auth/img/erweima.png"/>
		                            <p>扫描二维码</p>
		                            </a> </li>
		                          <li style="margin-top:35px;"> <a href="#"> <img src="${pageContext.request.contextPath}/portal/mb/auth/img/guide.png" style="height:20px;width:20px;" />
		                            </a> </li>
		                          <li> <a href="#"> <img src="${pageContext.request.contextPath}/portal/mb/auth/img/wifi.png"/>
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
			if("${msg}" != ""){//如果是发送验证码或者认证，如果有错误则显示错误信息
	   			alert("${msg}");
	   		}
			initStyle();
			initEvent();
			checkAuthed($("#myform input[name=phone]").val());//第一次加载时执行校验
			checkTimeLeft();//检查发送验证码的剩余时间
// 			checkAuthResult();
// 			checkVerifyCode();
			changWeixinAuthUrl();
			if("${type}"=="weixin" && "${phone}" == ""){
				$('.px_nav li.weixin').click();
			}
		});
		function initStyle(){
			$(".pxnav_list>li:nth-of-type(2)").hide();
			if(authedPhone.length > 0 && $("#myform input[name=phone]").val() == ''){//如果有已经认证的手机，并且是第一次请求该页面（不是发送验证码或者请求验证）
				$("#myform input[name=phone]").val(authedPhone[0].phone);
			}
		}
		function initEvent(){
			$('.px_nav li').click(function(){
				$(this).addClass('cur').siblings().removeClass('cur');
				$(".pxnav_list li:eq("+$(this).index()+")").fadeIn().siblings(".pxnav_list li").hide().addClass("current");
			});
			$( "#myform .home_footer a" ).click(function(){
				$("#myform input[name='cmd']").val("auth");
	    		if($("form input[name='phone']").val().length != 11){
					alert("手机号码填写有误！");
					return false;
				} 
	    		if($("#myform .verifyCode").eq(0).is(":visible") && 
	    				$("#myform input[name=verifyCode]").eq(0).val().length != 4){
					alert("验证码填写有误！");
					return false;
				}
	    		if(!hasAuthed){
	    			hasAuthed = true;
		    		$("#myform").submit();
	    		}
			});
			$("#myform input[name=phone]").keyup(function(){
				checkAuthed($(this).val());
			});
			$("#myform input[name=phone]").on('input',function(){
	    		checkAuthed($(this).val());
	    	})
		}
		var authedPhone = ${phones};
    	function checkAuthed(phone){
			if(phone.length == 11){
				if(authedPhone.length>0){
					for(var i=0;i<authedPhone.length;i++){
						if(phone == authedPhone[i].phone){
							$("#myform .verifyCode").hide();
							$("#myform .home_footer a").text("一键认证");
							return;
						}
					}
				}
				$("#myform .verifyCode").show();
				$("#myform .home_footer a").text("认证");
			}else{
				$("#myform .verifyCode").hide();
				$("#myform .home_footer a").text("认证");
			}
		}
    	
    	var refreshLaveSecondsInterval;
    	function checkTimeLeft(){
    		if(parseInt("${timeSeconds}") > 0){
    			$("#myform .verifyCode input[type=button]").addClass("clickedButton");
    			$("#myform .verifyCode input[type=button]").attr('disabled',"disabled");
    			$("#myform .verifyCode input[type=button]").val("${timeSeconds}秒后再次获取");
    			try{
					clearInterval(refreshLaveSecondsInterval);
				}catch(e){
				}
				refreshLaveSecondsInterval = setInterval("refreshLaveSeconds();",1000);
    		}
    	}
    	function sendPhoneValidateCode(){//发送验证码
    		$("#myform input[name='cmd']").val("code");
			var laveSeconds = parseInt($("#myform .verifyCode input[type=button]").val());
			if($("#myform input[name=phone]").eq(0).val().length == 11){
				if(isNaN(laveSeconds) || laveSeconds < 1){
					if(!hasGetCode){
						hasGetCode = true;
						$("form").submit();
					}
				}
			}else{
				alert("手机号码不正确！");
			}
		}
		function refreshLaveSeconds(){//定时刷新发送验证码剩余时间
			var laveSeconds = parseInt($("#myform .verifyCode input[type=button]").val())-1;
			if(isNaN(laveSeconds) || laveSeconds < 1){
				$("#myform .verifyCode input[type=button]").val("获取验证码");
				$("#myform .verifyCode input[type=button]").removeClass("clickedButton");
				$("#myform .verifyCode input[type=button]").removeAttr("disabled");
				clearInterval(refreshLaveSecondsInterval);
			}else{
				$("#myform .verifyCode input[type=button]").val(laveSeconds+"秒后再次获取");
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
