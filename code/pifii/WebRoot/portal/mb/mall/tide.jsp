<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<title>${name}</title>
		<meta content="text/html; charset=utf-8" http-equiv="Content-Type" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0;" />
		<meta name="apple-touch-fullscreen" content="yes"/>
		<meta content="yes" name="apple-mobile-web-app-capable" />
		<link href="${pageContext.request.contextPath}/portal/mb/mall/css/gwk_touch.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/portal/mb/mall/css/public.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/portal/mb/mall/css/jquery.scrollintro.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/portal/mb/mall/css/animate.css" rel="stylesheet" type="text/css" />
		<link href="${pageContext.request.contextPath}/portal/mb/mall/css/font_face.css" rel="stylesheet" type="text/css">
		<script src="${pageContext.request.contextPath}/portal/commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/commonjs/jquery.cookie.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/commonjs/commons.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/mb/mall/js/pagination.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/mb/mall/js/script.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/mb/mall/js/jquery.scrollintro.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/mb/mall/js/modernizr.custom.49511.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/mb/mall/js/jquery.easing.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/mb/mall/js/huaping.js" type="text/javascript"language="javascript"></script>
	</head>
	<body style="background:silver;">
		<a name="top"></a>
		<div id="content">
			<div class="scroll">
				<div class="animate-block animate0 fadeInDown row_con">
					<h2 class="animate-block animate1 fadeInDown">${name}</h2>
				</div>	
				<div class="slide_01" id="slide_01">
					<div>
						<div>
						<c:forEach items="${rows}" var="row">
							<div class="mod_01">
								<a href="javascript:void(0)"><img src="${row.img}" class="animate-block animate0 rollIn" alt="${row.picdes}"> 
                                <h2 class="animate-block animate1 fadeInDown">${row.name}</h2>
                                <h3 class="animate-block animate2 rollIn">优惠价：${row.preprice}￥<del>原价：${row.price}￥</del></h3>
                                <p class="animate-block animate3 fadeInUp">${row.des}</p>
                                </a>
							</div>
						</c:forEach>
						</div>
					</div>`
				</div>
				<div class="animate-block animate3 fadeInUp dotModule_new">
					<div id="slide_01_dot" class="animate-block animate4 fadeInUp"></div>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		var _scroller = $('#content').scrollIntro();
		$('.selected').on('click', function(event){
			_scroller.scrollTo($('.mod_01').eq(1));
		});
		if (document.getElementById("slide_01")) {
			var slide_01 = new ScrollPic();
			slide_01.scrollContId = "slide_01"; //内容容器ID
			slide_01.dotListId = "slide_01_dot";//点列表ID
			slide_01.dotOnClassName = "selected";
			slide_01.arrLeftId = "sl_left"; //左箭头ID
			slide_01.arrRightId = "sl_right";//右箭头ID
			slide_01.frameWidth = 320;
			slide_01.pageWidth = 320;
			slide_01.upright = false;
			slide_01.autoPlay = true;
			slide_01.speed = 10;
			slide_01.space = 30;
			slide_01.initialize(); //初始化
		}
	</script>
</html>
