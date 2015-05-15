<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
<!--     	<meta http-equiv="pragma" content="no-cache">  -->
<!-- 		<meta http-equiv="cache-control" content="no-cache">  -->
<!-- 		<meta http-equiv="expires" content="0"> -->
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <meta name="apple-mobile-web-app-status-bar-style" content="black" />
        <meta name="format-detection" content="telephone=no" />
        <title>${shopInfo.name}</title>     
        <link rel="stylesheet" type="text/css" href="index3/css/common.css" />
        <link rel="stylesheet" type="text/css" href="index3/css/template.css" />
        <script src="../commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
        <script src="index3/js/TouchSlide.1.1.js" type="text/javascript"></script>
        <script src="index1/js/scrollText.js" type="text/javascript"></script>
        <style type="text/css">
	        .cer_form{background:#fff;margin-top:5px;padding-top:2%;}
			.cer_form input[type="text"]{height: 100%;text-indent: 5px;margin: 0 auto;border:1px solid #999;-webkit-border-radius:3px;border-radius:3px;}
			.cer_form .tel{width: 100%;}
			.renzheng,.cer_code{width: 80%;height: 35px;margin: 0 auto;margin-bottom:10px;line-height: 35px;}
			.renzheng input{cursor: pointer;word-spacing: 1px;display: block;width: 100%;height: 100%;vertical-align: middle;font-size: 16px;}
			.cer_form .cer_num{width: 50%;font-size: 16px;border:1px solid #999;}
			.cer_form .get_cer{width:45%;height: 35px;line-height: 35px;font-size: 16px;text-align:center;padding:0;}
	       	.home_footer{padding-top:3px;padding-bottom:5px;}
			.home_footer a{width:80%;color:#ffffff;background-color: #e34f14;display:block;margin:0 auto;text-align:center;padding:13px 0;border-radius:5px;font-size:15px;font-weight:bold;}
        	.footer img{width: 16px;vertical-align: middle;padding-bottom: 4px;padding-right: 2px;}
        </style>
    </head>
    <body>
        <div id="slideBox" class="slideBox">
            <div class="bd">
                <ul>
                <c:forEach items="${banner_advs}" var="row">
					<li>
                        <a class="pic" href="${row.link}"><img src="${row.image}" onerror="this.src='index1/img/ad-1.jpg'"></a>
                    </li>
				</c:forEach>
<!--                         <li> -->
<!--                             <a class="pic" href="javascript:void(0)"><img src="images/7day_b_01.png"  style="height:13rem;min-height:12rem;"/></a> -->
<!--                         </li> -->
<!--                         <li> -->
<!--                             <a class="pic" href="javascript:void(0)"><img src="images/7day_b_01.png"  style="height:13rem;min-height:12rem;"/></a> -->
<!--                         </li> -->
<!--                         <li> -->
<!--                             <a class="pic" href="javascript:void(0)"><img src="images/7day_b_01.png"  style="height:13rem;min-height:12rem;"/></a> -->
<!--                         </li> -->
<!--                         <li> -->
<!--                             <a class="pic" href="javascript:void(0)"><img src="images/7day_b_01.png"  style="height:13rem;min-height:12rem;"/></a> -->
<!--                         </li> -->
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
        <div class="article">
        	<div class="d_l">
       			<img src="${shopInfo.icon}" onerror="this.src='index1/img/morentouxiang.png'"/>
        	</div>
            <div class="d_r">
            	<span title="${shopInfo.name}">${shopInfo.name}</span>
            	<div title="${shopInfo.tel}">
					<span class="this_title">电话:</span>
					<span class="content">${shopInfo.tel}</span>
				</div>
				<div title="${shopInfo.addr}">
					<span class="this_title">地址:</span>
					<span class="content">${shopInfo.addr}</span>
				</div>
<%--             	<h2>${shopInfo.name}</h2> --%>
<%--                 <p>电话：<a href="tel:${shopInfo.tel}">${shopInfo.tel}</a></p> --%>
<%--                 <p>地址：${shopInfo.addr}</p> --%>
            </div>
            <div class="cl"></div>
        </div>
<!--         <aside> -->
<!--         	<div class="online"><a href="javascript:void(0)" class="qiday">我要上网</a></div> -->
<!--         </aside> -->
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
        <div class="article ar_2">
        	<h1>附近热门商家</h1>
             <ul>
                <li><a href="javascript:void(0);"><img src="index3/images/business_1.png" ><p>友联菜馆</p></a></li>
                <li><a href="javascript:void(0);"><img src="index3/images/business_2.png" ><p>荣华楼</p></a></li>
                <li><a href="javascript:void(0);"><img src="index3/images/business_3.png" ><p>唐荔园</p></a></li>
                <li><a href="javascript:void(0);"><img src="index3/images/business_4.png" ><p>银座餐厅</p></a></li>
            </ul>
        	<div class="cl"></div>
        </div>
        <div class="footer">
        	<a href="help">CopyRight &copy; 派路由&nbsp;&nbsp;&nbsp;<img src="index1/img/hm_bz.png">帮助中心</a>
        </div>
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
// 				$(".slideBox").css("height","150px");
// 				$(".business_detail").css("width","60%");
				if(isPC()){
// 					$(".slideBox .bd img").css("height","9em");
// 					$(".ar_2 li a img").css({"width":"50px","height":"50px"});
// 					$(".ar_2 h1").css("font-size","14px");
// 					$(".article .d_r p").css("font-size","12px");
// 					$(".ar_2 li a p").css("font-size","12px");
// 					$(".footer p").css("font-size","12px");

// 					$(".slideBox").css("height","135px");
// 					$(".slideBox .bd img").removeClass("banner_adv");
// 					$(".slideBox .bd img").css("width","260px");
					
					$(".ar_2 li a img").css({"width":"50px","height":"50px"});
					$(".ar_2 li a p").css("font-size","12px");
					$(".ar_2 h1").css("font-size","14px");
					$(".footer p").css("font-size","12px");
				}
			}
			$(".article .d_r").width($(".article").width()-$(".article .d_l").width()
					-getMarginPaddingBorderWidth($(".article .d_l"))
					-getMarginPaddingBorderWidth($(".article .d_r")));
			
			scrollMe($(".article .d_r>span"));//滚动商铺名称
			$(".article .d_r span.content").each(function(){//滚动电话和地址
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
