<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="content-type" content="text/html;charset=utf-8" />
		<meta name="mark" content="this is mark for login page,do not delete it" />
		<link rel="Shortcut Icon" href="images/favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" type="text/css" href="css/loginPage.css">
		<script src="${pageContext.request.contextPath}/js/security.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/jsbase.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.ba-resize.js" type="text/javascript"></script>
		
		<title>PiFii商户平台</title>
	</head>
	<body>
		 <div class="header">
			<div class="logo fl"><a href="#"><img src="images/login/pifiiLogo_delault.png"></a></div>
			<ul class="nav fr">
				<li><a href="http://www.pifii.com/" target="_blank">进入官网</a></li>
			</ul>
		</div>
		<div class="content">
			<form id="login" class="denglu fl" action="${pageContext.request.contextPath}/login" method="post">
				<div class="title">登录，更多精彩</div>
				<div class="floor floor01">
					<input id="name" type="text" placeholder="Username" value="${name}" maxlength="16" minlength="5" autofocus required />
				</div>
				<div class="floor floor02">
					<input id="pwd" type="password" placeholder="Password" maxlength="18" minlength="6" required/>
					<input id="modulus" type="hidden" value="${modulus}" />
					<input id="exponent" type="hidden" value="${exponent}" />
				</div>
				<div class="yangzhengma">
					<input type="text" maxlength="4" minlength="4" name="validateCode" required/>
					<a href="javascript:void(0);" onclick="changeCode()">
						<img src="servlet/captchaCode?_sed=<%=new java.util.Date().getTime()%>"/>
						<span>换一张</span>
					</a>
				</div>
				<div class="errorMsg">${msg}</div>
				<div class="floor03">
					<input type="button" value="登&nbsp;&nbsp;&nbsp;录" onclick="go()"/>
				</div>
				<input type="hidden" name="key" id="key"></input>
			</form>
		</div>
<!-- 		<div class="footer"> -->
<!-- 			<p>ICP备案号:粤ICP备08122927号-7</p> -->
<!-- 			<p>广州派联信息科技有限公司 版权所有&copy;CopyRight 2014-2015</p> -->
<!-- 		</div>  -->
		<script type="text/javascript">
			var NewHeight = $(window).height();
			if(NewHeight>=730){
				$(".denglu").css("top","10%");
			}
			$(document).keyup(function(e) {//监听enter事件
	        	if(e.keyCode == 13){
	        		go();
				}
	        });
			function go() {
				var userName = $('#name').val();
		  		if(userName==undefined || userName==''){
		  			$('.errorMsg').text('用户名不能为空！');
		  			$('#name').focus();
		  			return;
		  		}
		  		var password = $('#pwd').val();
		  		if(password==undefined || password==''){
		  			$('.errorMsg').text('密码不能为空！');
		  			$('#pwd').focus();
		  			return;
		  		}
				if($("input[name=validateCode]").val().length == 0){
					$('.errorMsg').text('验证码不能为空！');
					$('input[name=validateCode]').focus();
					return;
				}
				if($("input[name=validateCode]").val().length != 4){
					$('.errorMsg').text('验证码填写有误！');
					$('input[name=validateCode]').focus();
					return;
				}
				var key = RSAUtils.getKeyPair('${exponent}', '', '${modulus}');
				var key2 = "name=" + $('#name').val() + "&pwd=" + $('#pwd').val();
		
				$('#key').val(RSAUtils.encryptedString(key, key2));
				$('#login').submit();
			}
			function changeCode() {
				$(".yangzhengma img").attr("src", "servlet/captchaCode?"+Math.floor(Math.random()*100));
			}
		</script>
	</body>
</html>