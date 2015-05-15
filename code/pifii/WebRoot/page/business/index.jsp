<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="zh">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
		<script type="text/javascript" src="js/jquery.ba-resize.js"></script>
		<script type="text/javascript" src="js/business/common.js"></script>
		<script type="text/javascript" src="js/business/dataFormate.js"></script>
		<link rel="Shortcut Icon" href="images/favicon.ico" type="image/x-icon" />
		<link rel="stylesheet" type="text/css" href="css/business/main.css"/>
<!-- 		<link rel="stylesheet" type="text/css" href="css/business/index.css"/> -->
		
<!-- 		<link rel="stylesheet" type="text/css" href="js/lightbox/themes/default/jquery.lightbox.css" /> -->
<!-- 		<script type="text/javascript" src="js/lightbox/jquery.lightbox.min.js"></script> -->
		
		<script type="text/javascript" src="js/jquery.circliful.js"></script><!-- Tab圆形的js -->
		<script type="text/javascript" src="js/RGraph.bar.js" ></script><!-- Tab柱形的js -->
		<script type="text/javascript" src="js/RGraph.common.core.js" ></script><!-- Tab柱形的js -->
		<script type="text/javascript" src="js/RGraph.common.key.js" ></script><!-- Tab柱形的js -->
		
		<script type="text/javascript" src="js/My97DatePicker/WdatePicker.js" ></script><!-- 日期选择控件js -->
		
		<link rel="stylesheet" href="js/jquery-simple-modal/assets/css/simplemodal.css" type="text/css" media="screen" title="no title" charset="utf-8">
		<script src="js/jquery-simple-modal/simple-modal.js" type="text/javascript" charset="utf-8"></script>
		
		<link href="js/tipsy/tipsy.css" rel="stylesheet" type="text/css" media="screen" title="no title" charset="utf-8">
		<script src="js/tipsy/jquery.tipsy.js" type="text/javascript" charset="utf-8"></script>
		
		<!-- 分页插件jPage -->
		<script type="text/javascript" src="js/jPage/js/jPages.js" ></script>
		<link rel="stylesheet" href="js/jPage/css/jPages.css" type="text/css" media="screen" title="no title" charset="utf-8"/>
<!-- 		<script type="text/javascript" src="js/bootstrap/bootstrap.min.js"></script> -->
<!-- 		<script type="text/javascript" src="js/bootstrap/datepicker/moment.js"></script> -->
<!-- 		<script type="text/javascript" src="js/bootstrap/datepicker/daterangepicker.js"></script> -->

<!-- 		<link href="js/bootstrap/datepicker/font-awesome.min.css" rel="stylesheet"> -->
<!-- 		<link href="js/bootstrap/bootstrap.min.css" rel="stylesheet"> -->
<!-- 		<link rel="stylesheet" type="text/css" media="all" href="js/bootstrap/datepicker/daterangepicker-bs3.css"/> -->

		<link rel="stylesheet" type="text/css" href="js/fusionChartsJs/prettify.css"/>
		<script type="text/javascript" src="js/fusionChartsJs/FusionCharts.js"></script>
		<script type="text/javascript" src="js/fusionChartsJs/FusionChartsExportComponent.js"></script>
		<script type="text/javascript" src="js/fusionChartsJs/FusionCharts.js"></script>
		<script type="text/javascript" src="js/business/uploadImagePreview.js"></script>
		<script type="text/javascript" src="js/zclip/jquery.zclip.js"></script>
		<script type="text/javascript" src="js/jquery.actual.min.js"></script>
		<script type="text/javascript" src="js/business/scrollText.js"></script>
		<script type="text/javascript">
			var cxt = "${cxt}";
		</script>
		<title>PiFii商户平台</title>
	</head>
	<body>
		<div class="header">
			<div id="mainPageHeader" class="headerJuzhong">
				<div class="headerLeft fl">
					<div class="logo select fl"><img src="" onerror="this.src='images/business/userPic/morentouxiang.png'"/></div><!-- 商户图标 -->
<!-- 					<div class="name fl"><select></select></div>商户的商铺 -->
					<div class="selectname fl">
						<div class="selected_shop">
							<p></p>
							<img src="images/business/indexCons.png"/><!-- 显示选择后的商铺 -->
  						</div>
  						<div class="shop_list"><ul></ul></div><!-- 显示选择商铺列表 -->
					</div>
				</div>
				<div class="headerRight fr">
					<span>修改密码</span>&nbsp;&nbsp;<b>|</b>&nbsp;&nbsp;<span>退出</span>
				 </div>
			</div>
		</div><!--header结束-->
		<div class="main_page_center">
			<%@ include file="menuLeft.jsp" %> 
				<!--主页的index 部分结束-->
				<!--设备的内容-->
				<div id="main_contents" class="main_content fl"></div>
			<div class="cl"></div>
		</div><!--8.4号修改部分-->
		<!-- lightbox弹窗设置路由页面 -->
<!-- 		<a id="start_guide_link" href="page/business/guide/index.jsp?lightbox[iframe]=false&lightbox[width]=950&lightbox[height]=550"></a> -->
<!-- 		<footer class="footer"> -->
<!-- 			<div class="copyright">ICP备案号：粤ICP备08122927号-7</div> -->
<!-- 			<div >广州因孚网络科技有限公司 版权所有@CopyRight 2014-2015 </div> -->
<!-- 		</footer> -->
	</body>
	<script type="text/javascript" src="js/business/commonData.js"></script>
	<script type="text/javascript" src="js/business/commonPop.js"></script>
	<script type="text/javascript" src="js/jquery.form.js"></script>
	<script>
		refreshPageLayout();
		$(function(){
			$("#mainPageHeader").css("background","url(${user.icon}) no-repeat center");//设置用户logo
			
			initEvent();
			initUserData();
			loadShop();
		});
		function initEvent(){
			//动态更改左边菜单栏的高度及footer的位置
			$("#main_contents").resize(function(){
				refreshPageLayout();
			});
			$(window).resize(function(){
				refreshPageLayout();
			});
			//更改用户图像
			$("#mainPageHeader").find(".logo").click(function(){
				$.fn.SimpleModal({
// 					model: 'modal-ajax',
					title: '更改商铺信息',
					width: 550,
// 					hideFooter: true,
					param: {
						url: "business/shop/editInfo",
						data:{shopId:getSelectedShopId()}
					}
				}).showModal();
			});
			addShopMouseEvent($("#mainPageHeader .headerLeft>.logo"),{
				enter:{'margin':'5px','width':'75px','height':'75px'},
				leave:{'margin':'10px','width':'65px','height':'65px'}
			});
// 			$("#mainPageHeader").find(".logo").mouseenter(function() {
// 				$(this).animate({'width':'75px','height':'75px'},{
// 	            	duration: 100,
// 					//easing: "linear",
// 					start: function() {
// 						$(this).stop();//防止上次触发的事件还未完成又触发造成的延迟执行
// 					}
// 				});
// // 				$(this).addClass("logo_mouse_over");
// 			}).mouseleave(function() {
// 				$(this).animate({'width':'65px','height':'65px'},{
// 	            	duration: 100,
// 					//easing: "linear",
// 					start: function() {
// 						$(this).stop();//防止上次触发的事件还未完成又触发造成的延迟执行
// 					}
// 				});
// // 				$(this).removeClass("logo_mouse_over");
// 			});
			//选择商铺事件
			$("#mainPageHeader .selectname div").first().click(function() {
// 				$(this).next().show("normal");
// 				$(this).next().fadeIn();
				$(this).next().slideDown();
				return false;
			});
			$("body").click(function(){
// 				$("#mainPageHeader .selectname div").eq(1).hide("normal");
// 				$("#mainPageHeader .selectname div").eq(1).fadeOut();
				$("#mainPageHeader .selectname div").eq(1).slideUp();
			});
			//修改密码
			$("#mainPageHeader .headerRight").find("span").eq(0).click(function(){
				$.fn.SimpleModal({
// 					model: 'modal-ajax',
					title: '修改用户密码',
					width: 550,
// 					hideFooter: true,
					param: {
						url: "page/business/changeUserPwd.jsp"
					}
				}).showModal();
			});
			//退出
			$("#mainPageHeader .headerRight").find("span").eq(1).click(function(){
				var bb="bbbb";
				myConfirm("确定要退出系统？",function(){
					 window.location.href="loginOut";
				});
			});
		}
		function initUserData(){
// 			$("#mainPageHeader").find(".logo").eq(0).find("img").attr("src","${user.icon}");
		}
		function loadShop(){
			$.ajax({
				type: "POST",
				url: "business/shop/list",
				success: function(data,status,xhr){
					if("success" == status){
						showShop(data);
					}
				}
			});
		}
		//显示店铺信息
		function showShop(shops){
			if(shops.length > 0){
				$("#mainPageHeader .selectname p")
						.text(shops[0].name)
						.data("shopId",shops[0].id);
				$("#mainPageHeader .headerLeft .logo img").attr("src",shops[0].icon);
				var shopsSelectHtml ="";
				for(var i=0;i<shops.length;i++){
					if("" == shops[i].icon){//兼容火狐，火狐浏览器中如果src为空字符串时，是不会执行onerror的
						shops[i].icon = "aa.jpg";
					}
					shopsSelectHtml += 
						'<li data-shop-id="'+shops[i].id+'">'+
							'<div class="logo fl">'+
								'<img src="'+shops[i].icon+'" onerror="this.src=\'images/business/userPic/morentouxiang.png\'">'+
							'</div>'+
							'<p>'+shops[i].name+'</p>'+
						'</li>';
				}
				$("#mainPageHeader .selectname ul").html(shopsSelectHtml);
				$("#mainPageHeader .selectname ul").width($("#mainPageHeader .selectname ul li").eq(0).actual('width') * $("#mainPageHeader .selectname ul li").length);
				var shop_list_element = $("#mainPageHeader .selectname .shop_list");
				if(shop_list_element.actual('width') > $("#mainPageHeader").width()){
					shop_list_element.width($("#mainPageHeader").width());
				}
				addShopMouseEvent($("#mainPageHeader .selectname ul li .logo"),{
					enter:{'margin':'10px','width':'75px','height':'75px'},
					leave:{'margin':'15px','width':'65px','height':'65px'}
				});
// 				$("#mainPageHeader .selectname ul li").attr("");
				$("#mainPageHeader .selectname ul li").click(function(){
					if($(this).data("shopId") != getSelectedShopId()){
						$(this).parent().parent().parent().find(".selected_shop p")
								.text($(this).children("p").text())
								.data("shopId",$(this).data("shopId"));
						$("#mainPageHeader .headerLeft>.logo img").attr("src",$(this).find("img").attr("src"));
						loadShopPageInfo();
					}
					$(this).parent().parent().hide();
				});
				loadShopPageInfo();
			}else{
				alert("未检测到你的商铺，请重新登录！");
				window.location.href="loginOut";
			}
		}
		function addShopMouseEvent(element,cls){
			element.mouseenter(function() {
				$(this).animate(cls.enter,{
	            	duration: 100,
					//easing: "linear",
					start: function() {
						$(this).stop();//防止上次触发的事件还未完成又触发造成的延迟执行
					}
				});
// 				$(this).addClass("logo_mouse_over");
			}).mouseleave(function() {
				$(this).animate(cls.leave,{
	            	duration: 100,
					//easing: "linear",
					start: function() {
						$(this).stop();//防止上次触发的事件还未完成又触发造成的延迟执行
					}
				});
// 				$(this).removeClass("logo_mouse_over");
			});
		}
		function loadShopPageInfo(){
			$.ajax({
				type: "POST",
				url: "business/page/findByShopId",
				data: {shopId:getSelectedShopId()},
				success: function(data,status,xhr){
					shopPageInfo = data;
					if(null == shopPageInfo){
						shopPageInfo = {id:"",step:0};
					}
					if(shopPageInfo.is_publish != "1"){
						$.fn.SimpleModal({
// 							model: 'modal-ajax',
							title: '主页创建向导',
// 							keyEsc:true,
// 							width: 1050,
							closeButton: false,
// 							hideFooter: true,
							param: {
								url: 'page/business/guide/index.jsp'
							}
						}).showModal();
					}
					//ajaxContent("/userContent");
					$("#main_leftmenu>nav>ul>li").eq(0).children("div").click();
				}
			});
		}
		//更新footer的位置
		function refreshPageLayout(){
			//刷新左边菜单的高度
			var contentHeight = $("#main_contents").height();
			var navHeight = $("#main_leftmenu nav ul").height();
			var height1 = navHeight>contentHeight?navHeight:contentHeight;
			var pageContentHeight = $(window).height()-$("body>.header").height()-$("body>footer").height();
			var height = pageContentHeight>height1?pageContentHeight:height1;
			$("#main_leftmenu nav").height(height);
			//刷新footer的高度
			$("body>footer").css("position","static");
			$(document).scrollTop(300);
			if($(document).scrollTop() == 0){
				$("body>footer").css({"position":"fixed","bottom":"0"});
			}
			$(document).scrollTop(0);
		}
	</script>
</html>
