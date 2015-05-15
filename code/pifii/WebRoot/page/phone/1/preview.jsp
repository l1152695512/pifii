<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0,user-scalable=no">
		<meta name="MobileOptimized" content="320">
		<script src="${pageContext.request.contextPath}/js/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/phone/commons.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/phone/TouchSlide.1.1.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/jquery.ba-resize.js" type="text/javascript"></script>
		<title>欢迎光临妈妈餐厅</title>
		<style type="text/css">
			/* css 重置 */
			body, p, input, h1, h2, h3, h4, h5, h6, ul, li{ margin: 0; padding: 0; list-style: none; vertical-align: middle;}
			body{ background-color: transparent;}
			
			img { border:0; margin: 0; padding: 0;}
			body { color: #000; -webkit-user-select: none; -webkit-text-size-adjust: none; font:normal 14px/200% "微软雅黑", helvetica, arial; text-align:left;   }
/* 			header, section, footer { display: block; margin: 0; padding: 0 } */
			a{text-decoration:none;color:#000;}
			.fl{float:left}
			.fr{float:right;}
			.cl{clear:both;}
			.line3{width: 100%;height:3px;background:#E3E3E3;}
			h3{
				font-size: 16px;
				font-weight: normal;
				height: 18px;
				line-height: 18px;
			}
			.cl{
				clear:both;
			}
			/*公共样式*/
			#content{background:#fff;}
			.path{text-align: center;height: 35px;font-size:18px;line-height: 35px;background:rgb(22, 171, 93);color:#fff;}
			/*标题信息*/

			/*ad广告信息*/
			.ad{

				width: 100%;
				height: 165px;
				overflow: hidden;
			}		
			.ad img{
				width: 100%;
			}
			.ad-bj{
				width: 100%;
				height: 4px;
				background: url(../../images/phone/1/ad-bt-bj.png) repeat-x;
			}

			/*banner轮播代码*/

			/* 本例子css -------------------------------------- */
			.slideBox{ position:relative; overflow:hidden; margin:0 auto;height: 134px;max-width:560px;/* 设置焦点图最大宽度 */ }
			.slideBox .hd{ position:absolute; height:28px; line-height:28px; bottom:0; right:0; z-index:1; }
			.slideBox .hd li{ display:inline-block; width:5px; height:5px; -webkit-border-radius:5px; -moz-border-radius:5px; border-radius:5px; background:#333; text-indent:-9999px; overflow:hidden; margin:0 6px;   }
			.slideBox .hd li.on{ background:#fff;  }
			.slideBox .bd{ position:relative; z-index:0; }
			.slideBox .bd li{ position:relative; text-align:center;  }
			.slideBox .bd li img{ background:url(../../images/phone/1/loading.gif) center center no-repeat;  vertical-align:top; width: 260px;height: 134px;/* 图片宽度100%，达到自适应效果 */}
			.slideBox .bd li a{ -webkit-tap-highlight-color:rgba(0,0,0,0);  }  /* 去掉链接触摸高亮 */
			.slideBox .bd li .tit{ display:block; width:100%;  position:absolute; bottom:0; text-indent:10px; height:28px; line-height:28px; background:url(images/focusBg.png) repeat-x; color:#7E9500;  text-align:left;  }

			/*queing-box信息*/
			.queing-box{
				width: 100%;
				height: 57px;
				overflow: hidden;
			}
			.queing-box .left{
				width: 47%;
				overflow: hidden;
			}
			.queing-logo{
				width: 42%;
				overflow: hidden;
				height: 50px;
			}
			.queing-logo a{
				display: block;
			}
			.queing-logo a img{
				width: 100%;
			}
			/*queing-info 排队信息右边内容*/

			/* guest  顾客显示的代码 */
			.queing-info{
				width: 52%;
				height: 45px;
				text-align: center;
				line-height: 45px;
				font-size:14px;
			}/* guest 顾客显示的代码 公用的样式*/
			.queing-info span{
				font-size: 14px;
				color: #F69E95;
				font-family: "arial";
			}
			.guest{
				
			}
				
			/* guest 顾客显示的代码结束*/


			/*Manager 管理人员显示的代码*/
			.manager{
				display: none;
				border-left:1px solid #EAEAEA;
			}
			.queing-info .right{
				width: 99%;
				height: 50px;
				padding-left: 2%;
				overflow: hidden;
			}
			.m-info{
				width: 38%;
				height: 50px;
				overflow: hidden;
			}
			.m-info a{
				display: block;
				height: 50px;
			}
			.m-info a img{
				width: 99%;
			}
			.queing-info .right p{
				width: 53%;
				margin-top:17px;
			}
			.paidui{
				width: 53%;
				height: 14px;
				line-height: 12px;
				color: #363636;
				font-size: 14px;
				margin-top: 17px;
			}

			/*manager排队信息结束*/
			.modular{
				width:100%;
			}
			.modular ul{
				padding:12px 2%;
			}
			.modular ul li{
				float: left;
				margin-right: 5%;
				margin-bottom: 10px;
				width: 21%;
			}
			.modular ul li.four_children{
				margin-right:0;
			}
			.modular .pic{
				display: block;
				width: 100%;
				overflow: hidden;
			}
			.modular .pic img{
				width: 100%;
			}
			.modular p{
				width: 100%;
				height: 14px;
				line-height: 14px;
				text-align: center;
				font-size: 12px;
				color: #222222;
			}
			/*模板结束*/

			.shangjia{
				width: 100%;
				height: 64px;
				padding-top:5px;
				overflow: hidden;
			}
			.shangjia .inner{
				width:92%;
				height: 100%;
				margin:0 auto;
			}
			.sj-logo{
				width:27%;
				 margin-right:2%;
			}
			.sj-info{
				width: 70%;
				height: 100%;
			}
			.sj-logo img{
				width: 60px;
				height: 60px;
			}
			.shangjia p{
				width: 100%;
				color: #666;
				font-size: 10px;
				height: 20px;
				line-height: 20px;
				text-indent: 22px;
				overflow: hidden;
			}
			.shangjia p.shop_phone{
				background: url(../../images/phone/1/tel_small.png) no-repeat 0 1px;
				background-size: 11%;
			}
			.shangjia p.shop_address{
				background: url(../../images/phone/1/address_small.png) no-repeat 0 1px;
				background-size: 13%;
			}
			.footer{
				width: 100%;
				height: 26px;
				text-align: center;
				line-height: 26px;
				font-size: 12px;
				color: #666;
				position: static;
			}
			.footer a:link,.footer a:visited{
				text-decoration: none;
				color:#000;
			}
			.footer a:hover{
				text-decoration: none;
				color:#000;
			}
		</style>
	</head>
	<body>
		<div id="content">
			<div id="slideBox" class="slideBox"><!-- 轮播图 -->
				<div class="bd">
					<ul></ul>
				</div>
				<div class="hd">
					<ul></ul>
				</div>
			</div>
			<div class="line3"></div><!--分割线-->
			<div class="shangjia"><!-- 商家信息 -->
				<div class="inner">
					<div class="sj-logo fl"><img src="" onerror="this.src='${pageContext.request.contextPath}/images/business/userPic/morentouxiang.png'"/></div>
					<div class="sj-info fl">
						<h3></h3>
						<p class="shop_phone"></p>
						<p class="shop_address"></p>
					</div>
					<div class="cl"></div>
				</div>
			</div>
			<div class="line3"></div><!--分割线-->
			<div class="modular"><!--模板展示-->
				<ul></ul>
				<div class="cl"></div>
				<input id="app_shopId" type="hidden" value="<%=request.getParameter("shopId")%>">
			</div>
			<div class="cl"></div>
		</div><!--大盒子over-->
		<div class="footer">CopyRight @派路由&nbsp;&nbsp;&nbsp;<a href="javascript:void(0);">www.pifii.com</a></div>
		<script type="text/javascript">
		$(function(){
			$("#content").resize(function(){
				refreshFooterPosition();
			});
// 		 $("#backgroudImage").show(function(){
				var shopId = $("#app_shopId").attr("value");
				$.ajax({
		            type: "POST",
		            url: '${pageContext.request.contextPath}/business/page/loadBackImg',
		            data: {shopId:shopId},
		            success: function(data) {
		            	if(data.length > 0){
		            		var picturesHtml = "";
		        			for(var i = 0;i < data.length;i++){
		        				picturesHtml += "<li>"+
		        									"<a class='pic' href='javascript:void(0);'>"+
		        										"<img src='${pageContext.request.contextPath}/"+data[i].image+"' onerror=\"this.src='${pageContext.request.contextPath}/images/business/ad-1.jpg'\"/>"+
		        									"</a>"+
		        								"</li>";
		        			}
		        			$("#slideBox>.bd>ul").html(picturesHtml);
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
		            	}else{
		            		$("#slideBox>.bd>ul").hide();
		            	}
		            }
		        });
// 			});
// 		 $("#app_info").show(function(){
// 				var shopId = $("#app_shopId").attr("value");
				$.ajax({
		            type: "POST",
		            url: '${pageContext.request.contextPath}/business/page/loadAppInfo',
		            data: {shopId:shopId},
		            success: function(data) {
		            	var appsHtml = "";
		    			for(var i = 0;i < data.length;i++){
		    				var cls = "";
		    				if(i%4 == 3){
		    					cls = 'class="four_children"';
		    				}
		    				appsHtml += '<li '+cls+'>'+
		    								 "<a href='javascript:void(0);' class='pic' >"+
		    								 	"<img src='${pageContext.request.contextPath}/"+data[i].icon+"'/>"+
		    								 "</a>"+
		    								 "<p>"+data[i].name+"</p>"+
		    							 "</li>";
		    			}
		    			$("#content .modular ul").html(appsHtml);
		            }
		        });
// 			});

			//加载商家信息
			$.ajax({
	            type: "POST",
	            url: '${pageContext.request.contextPath}/business/shop/getShopInfo',
	            data: {shopId:shopId},
	            success: function(data) {
	            	if('' !=data.icon && null != data.icon){
		            	$("#content .shangjia .sj-logo img").attr("src","${pageContext.request.contextPath}/"+data.icon);
	            	}
	            	$("#content .shangjia .sj-info h3").text(data.name);
	            	$("#content .shangjia .sj-info p").eq(0).text(data.tel);
	            	$("#content .shangjia .sj-info p").eq(1).text(data.addr);
	            }
	        });
		});
		
		//更新footer的位置
		function refreshFooterPosition(){
			$(".footer").css("position","static");
			$(document).scrollTop(300);
			if($(document).scrollTop() == 0){
				$(".footer").css({"position":"fixed","bottom":"0"});
			}
			$(document).scrollTop(0);
		}
		</script>
	</body>
</html>