<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<title>${shopInfo.name}</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0,user-scalable=no"/>
		<meta name="MobileOptimized" content="320"/>
		<link rel="stylesheet" href="index1/css/jquery.scrollintro.css"/>
		<link rel="stylesheet" href="index1/css/animate.css"/>
        <link type="text/css" href="index1/css/jquery.mobile-1.4.2.min.css" rel="stylesheet" />
        <link type="text/css" href="index1/css/mama_style.css" rel="stylesheet" />
		<script src="../commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="../commonjs/jquery.cookie.js" type="text/javascript"></script>
		<script src="../commonjs/TouchSlide.1.1.js" type="text/javascript"></script>
		<script src="../commonjs/commons.js" type="text/javascript"></script>
		
        <script src="index1/js/jquery.media.js" type="text/javascript"></script> 
		<script src="index1/js/modernizr.custom.49511.js" type="text/javascript"></script>
		<script src="index1/js/jquery.scrollintro.min.js" type="text/javascript"></script>
<!-- 		<script src="index1/js/imagesloaded.pkgd.min.js" type="text/javascript"></script> -->
		<script src="index1/js/scrollText.js" type="text/javascript"></script>
		<style type="text/css">
			.ad{height:35px;background:url(${bottom_adv.image}) no-repeat center center;-moz-background-size:cover;-webkit-background-size:cover;-ms-background-size:cover;background-size:cover;position:fixed;bottom:0;}
			.footer img{width: 16px;vertical-align: middle;padding-bottom: 4px;padding-right: 2px;}
		</style>
	</head>
	<body>
		<div id="content">
			<div id="slideBox" class="slideBox">
				<div class="bd">
					<ul data-easein="rollIn">
					<c:forEach items="${banner_advs}" var="row" varStatus="status">
						<li class="animate${status.count} fadeInRight">
							<a class="pic" href="${row.link}">
								<img src="${row.image}" onerror="this.src='index1/img/ad-1.jpg'"/>
							</a>
						</li>
					</c:forEach>
					</ul>
				</div>
				<div class="animate5 fadeIn hd">
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
			<div class="ad-bj"></div>
			<div class="shangjia">
				<div class="animate1 fadeInLeft sj-logo fl">
					<img src="${shopInfo.icon}" onerror="this.src='index1/img/morentouxiang.png'"/>
				</div>
				<div class="inner animate1">
					<span class="shopName animate2 fadeInRight" title="${shopInfo.name}">${shopInfo.name}</span>
					<div class="animate3 fadeInRight" title="${shopInfo.tel}">
						<span>电话:</span>
						<span class="content">${shopInfo.tel}</span>
					</div>
					<div class="animate4 fadeInRight" title="${shopInfo.addr}">
						<span>地址:</span>
						<span class="content">${shopInfo.addr}</span>
					</div>
				</div>
			</div>
		  	<div class="line3"></div><!--万能分割线-->
            <div class="animate0 fadeInUp app_list" data-easein="fadeInUp">
<!--             	<p class="animate1 fadeInUp fl">应用</p> -->
                <div class="cl"></div>
            </div>
			<div class="modular" id="update_data"><!--模板展示-->
				<ul data-easein="fadeInUp">
				<c:forEach items="${apps}" var="row" varStatus="status">
					<li class="animate${status.count} fadeInUp <c:if test='${status.count%4==0}'>four_app</c:if>">
						<a href="${row.link}" class="pic" ><img src="${row.icon}"/></a>
						<p class="animate${status.count} fadeInUp">${row.name}</p>
					</li>
				</c:forEach>
				</ul>
				<div class="cl"></div>
			</div>
		</div><!--大盒子over-->
		<div class="animate1 fadeInUp footer"><a href="help" >CopyRight &copy; 派路由&nbsp;&nbsp;&nbsp;<img src="index1/img/hm_bz.png">帮助中心</a></div>
<!-- 		<div class="ad_wrapper">  -->
<%--             <a href="${bottom_adv.link}"><div class="ad"></div></a> --%>
<!--             <div class="close"></div> -->
<!--         </div> -->
		<script type="text/javascript">
			$(window).load(function(){
// 	            $(".close").click(function(){
// 	                $(".ad_wrapper").hide();
// 	            });
// 	            changeSpanLineHeight();
	        });
// 			$('.sj-logo img').imagesLoaded().done(function(instance){
// 				changeSpanLineHeight();
// 			});
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
		    
			$(function(){
				if("${isShow}" == "1"){//平台端展示预览
					$("a").each(function () {
		                $(this).css("cursor", "default");
		                $(this).attr('href', '#');
		                $(this).click(function (event) {
		                    event.preventDefault();
		                });
		                $(this).removeAttr("onclick");
					});
					 $(".close").parent().remove();
					 if(isPC()){
// 						 $(".slideBox").css("height","135px");
	// 					 $(".slideBox .bd .pic img").css("height","158px");
// 						 $(".slideBox .bd .pic img").css("width","260px");
// 						 $(".inner .shopName").css("font-size","18px");
// 						 $(".inner div span").css("font-size","15px");
					 }
				}
				$(".shangjia .inner").width($(".shangjia").width()-$(".shangjia .sj-logo").width()
						-getMarginPaddingBorderWidth($(".shangjia .sj-logo"))
						-getMarginPaddingBorderWidth($(".shangjia .inner")));
				scrollMe($(".inner>span"));//滚动商铺名称
				$(".inner span.content").each(function(){//滚动电话和地址
					var parentWidth = $(this).parent().width();
					scrollMe($(this),parentWidth-$(this).parent().children("span").eq(0).width());
				});
				
				//更改应用图标的宽和高
				$(".modular li img").each(function(){
					var thisWidth = $(this).width();
					$(this).css("height",thisWidth);
				});
			});
// 			function changeSpanLineHeight(){
// 				$(".inner span").css("line-height",$(".shangjia").height()/3+"px");
// 			}
		</script>
	</body>
</html>