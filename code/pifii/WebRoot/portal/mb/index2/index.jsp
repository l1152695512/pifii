<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
<!--     	<meta http-equiv="pragma" content="no-cache">  -->
<!-- 		<meta http-equiv="cache-control" content="no-cache">  -->
<!-- 		<meta http-equiv="expires" content="0"> -->
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
        <meta name="apple-mobile-web-app-capable" content="yes">
        <meta name="apple-mobile-web-app-status-bar-style" content="black">
        <meta name="format-detection" content="telephone=no">
        <title>${shopInfo.name}</title>
        <script src="../commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
        <script src="index2/js/TouchSlide.1.1.js" type="text/javascript"></script>
        <script src="index1/js/scrollText.js" type="text/javascript"></script>
        <link rel="stylesheet" type="text/css" href="index2/css/common.css" />
        <link rel="stylesheet" type="text/css" href="index2/css/cer.css" />
        <style type="text/css">
			.footer img{width: 16px;vertical-align: middle;padding-bottom: 4px;padding-right: 2px;}
		</style>
    </head>
    <body>
    	<header>
        	<h2>认证上网</h2>
        </header>
        <!--<div class="pic_area"></div>-->
        <div id="slideBox" class="slideBox">
            <div class="bd">
                <ul>
                	<c:forEach items="${banner_advs}" var="row">
						<li>
	                        <a class="pic" href="${row.link}"><img src="${row.image}" onerror="this.src='index1/img/ad-1.jpg'" ></a>
	                    </li>
					</c:forEach>
<!--                     <li> -->
<!--                         <a class="pic" href="http://www.yixun.com"><img src="images/banner_01.jpg"  onerror="this.src='index1/img/ad-1.jpg'" style="height:10rem;min-height:10rem;"/></a> -->
<!--                     </li> -->
<!--                     <li> -->
<!--                         <a class="pic" href="http://www.yixun.com"><img src="images/banner_02.jpg"  onerror="this.src='index1/img/ad-1.jpg'" style="height:10rem;min-height:10rem;"/></a> -->
<!--                     </li> -->
<!--                     <li> -->
<!--                         <a class="pic" href="http://www.yixun.com"><img src="images/banner_03.jpg"  onerror="this.src='index1/img/ad-1.jpg'" style="height:10rem;min-height:10rem;"/></a> -->
<!--                     </li> -->
                </ul>
            </div>
            <div class="hd">
                <ul></ul>
            </div>
            <script type="text/javascript">
            	var bannerImgWidth = $(document.body).width();
            	var bannerImgHeight = 150/290*bannerImgWidth;
            	$("#slideBox .bd img").css({"width":bannerImgWidth,"height":bannerImgHeight});
                try{
                    TouchSlide({
                        slideCell:"#slideBox",
                        titCell:".hd ul", //开启自动分页 autoPage:true ，此时设置 titCell 为导航元素包裹层
                        mainCell:".bd ul", 
                        effect:"leftLoop", 
                        autoPage:true,//自动分页
                        autoPlay:true //自动播放
                    });
                }catch(e){
                }
            </script>
        </div>
        <div class="business">
        	<div class="business_logo">
       		<c:if test="${shopInfo.icon != ''}">
       			<img src="${shopInfo.icon}" onerror="this.src='index2/images/youzheng.jpg'"/>
       		</c:if>
        	</div>
            <div class="business_detail">
            	<span title="${shopInfo.name}">${shopInfo.name}</span>
            	<div title="${shopInfo.tel}">
					<span class="this_title">电话:</span>
					<span class="content">${shopInfo.tel}</span>
				</div>
				<div title="${shopInfo.addr}">
					<span class="this_title">地址:</span>
					<span class="content">${shopInfo.addr}</span>
				</div>
<%--                 <p title="${shopInfo.tel}"><a href="tel:${shopInfo.tel}">电话：${shopInfo.tel}</a></p> --%>
<%--                 <h3 title="${shopInfo.addr}">地址：${shopInfo.addr}</h3> --%>
                
                
                
<%--                 <span class="shopName animate2 rollIn" title="${shopInfo.name}">${shopInfo.name}</span> --%>
<%-- 				<div class="animate3 rollIn" title="${shopInfo.tel}"> --%>
<!-- 					<span>电话:</span> -->
<%-- 					<span class="content">${shopInfo.tel}</span> --%>
<!-- 				</div> -->
<%-- 				<div class="animate4 rollIn" title="${shopInfo.addr}"> --%>
<!-- 					<span>地址:</span> -->
<%-- 					<span class="content">${shopInfo.addr}</span> --%>
<!-- 				</div> -->
            </div>
            <div class="cl"></div>
        </div>
        <form class="cer_form" action="" method="post">
            <div class="renzheng">
                <input type="text" placeholder="请输入手机号码" type="number" value="${phone}" name="phone" class="tel" maxlength="11" minlength="11" />
            </div>
            <div class="cer_code">
                <input type="text" placeholder="请输入验证码" name="verifyCode" class="cer_num" maxlength="4" minlength="4" />
                <input type="button" class="get_cer" onclick="sendPhoneValidateCode();" value="获取验证码"  />
            </div>
            <div class="home_footer cl">
                <a href="javascript:void(0);" onclick="authClient();">认证</a>
            </div>
            <input type="hidden" name="cmd"/>
        </form>
        <div class="login_box">
            <ul>
                <li><a href="auth?type=weixin"><img src="index2/images/weixin.png" ><p>微信登录</p></a></li>
                <li><a href="javascript:void(0);"><img src="index2/images/weibo02.png" ><p>微博登录</p></a></li>
                <li><a href="javascript:void(0);"><img src="index2/images/qq02.png" ><p>QQ登录</p></a></li>
            </ul>
        	<div class="cl"></div>
        </div>
        <div class="footer"><a href="help" >CopyRight &copy; 派路由&nbsp;&nbsp;&nbsp;<img src="index1/img/hm_bz.png">帮助中心</a></div>
    </body>
    <script type="text/javascript">
	    var hasGetCode = false;//防止在网络慢时，连续点击
		var hasAuthed = false;//防止在网络慢时，连续点击
	    $(function(){
	    	if("${msg}" != ""){//如果是发送验证码或者认证，如果有错误则显示错误信息
	   			alert("${msg}");
	   		}
			initEvent();
			if(authedPhone.length > 0 && $("form input[name='phone']").val() == ''){//如果有已经认证的手机，并且是第一次请求该页面（不是发送验证码或者请求验证）
				$("form input[name='phone']").val(authedPhone[0].phone);
			}
			checkAuthed($("form input[name='phone']").val());//第一次加载时执行校验
			dealShow();
			'<c:if test="${isShow != 1}">'
			checkTimeLeft();//检查发送验证码的剩余时间
			'</c:if>'
		});
	    function initEvent(){
	    	$("form input[name='phone']").keyup(function(){
				checkAuthed($(this).val());
			});
	    	$("form input[name='phone']").on('input',function(){
	    		checkAuthed($(this).val());
	    	})
	    }
	    function isPC(){
	    	var system ={
				win : false,
				mac : false,
				xll : false
			};
			var p = navigator.platform;
			system.win = p.indexOf("Win") == 0;
			system.mac = p.indexOf("Mac") == 0;
			system.x11 = (p == "X11") || (p.indexOf("Linux") == 0);
			if(system.win||system.mac||system.xll){
				return true;
			}else{
				return false;
			}
	    }
	    
	    function dealShow(){
	    	
			if("${isShow}" == "1"){//平台端展示预览
				$("a").each(function () {
	                $(this).css("cursor", "default");
	                $(this).attr('href', '#');
	                $(this).click(function (event) {
	                    event.preventDefault();
	                });
	                $(this).removeAttr("onclick"); 
	                $("input").attr("disabled","disabled");
				});
				$("input[type=button]").removeAttr("onclick");
				$("form input[name='phone']").val("");
				if(isPC()){
// 					$(".slideBox .bd img").removeClass("banner_adv");
// 					$(".business_detail").css({"width":"60%"});
// 					$(".slideBox .bd img").css("width","260px");
// 					$(".slideBox .bd img").css("height","9em");
// 					$(".slideBox").css("height","135px");
					
					$(".login_box").css("font-size","14px");
				}
			}
			$(".business_detail").width($(".business").width()-$(".business_logo").width()
					-getMarginPaddingBorderWidth($(".business_logo"))
					-getMarginPaddingBorderWidth($(".business_detail")));
			scrollMe($(".business_detail>span"));//滚动商铺名称
			$(".business_detail span.content").each(function(){//滚动电话和地址
				var parentWidth = $(this).parent().width();
				scrollMe($(this),parentWidth-$(this).parent().children("span").eq(0).width());
			});
		}
    	var authedPhone = ${phones};
    	function checkAuthed(phone){
			if(phone.length == 11){
				if(authedPhone.length>0){
					for(var i=0;i<authedPhone.length;i++){
						if(phone == authedPhone[i].phone){
							$("form .cer_code").hide();
							$("form .home_footer a").text("一键认证");
							return;
						}
					}
				}
				$("form .cer_code").show();
				$("form .home_footer a").text("认证");
			}else{
				$("form .cer_code").hide();
				$("form .home_footer a").text("认证");
			}
		}
	    '<c:if test="${isShow != 1}">'
    	var refreshLaveSecondsInterval;
    	function checkTimeLeft(){
    		if(parseInt("${timeSeconds}") > 0){
    			$("form .cer_code input[type=button]").attr('disabled',"disabled");
    			$("form .cer_code input[type=button]").val("${timeSeconds}秒后再次获取");
    			try{
					clearInterval(refreshLaveSecondsInterval);
				}catch(e){
				}
				refreshLaveSecondsInterval = setInterval("refreshLaveSeconds();",1000);
    		}
    	}
		function sendPhoneValidateCode(){//发送验证码
			$("form input[name='cmd']").val("code");
			var laveSeconds = parseInt($("form .cer_code input[type=button]").val());
			if($("form .renzheng input[name=phone]").eq(0).val().length == 11){
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
			var laveSeconds = parseInt($("form .cer_code input[type=button]").val())-1;
			if(isNaN(laveSeconds) || laveSeconds < 1){
				$("form .cer_code input[type=button]").val("获取验证码");
				$("form .cer_code input[type=button]").removeAttr("disabled");
				clearInterval(refreshLaveSecondsInterval);
			}else{
				$("form .cer_code input[type=button]").val(laveSeconds+"秒后再次获取");
			}
		}
		function authClient(){
    		$("form input[name='cmd']").val("auth");
    		if($("form input[name='phone']").val().length != 11){
				alert("手机号码填写有误！");
				return false;
			} 
    		if($("form .cer_code").is(":visible") && 
    				$("form input[name='verifyCode']").val().length != 4){
				alert("验证码填写有误！");
				return false;
			}
    		if(!hasAuthed){
    			hasAuthed = true;
    			$("form").submit();
    		}
    	}
	    '</c:if>'
    </script>
</html>
