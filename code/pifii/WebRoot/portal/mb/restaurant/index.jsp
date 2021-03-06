<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<!DOCTYPE html>
<html>
	<head>
		<title>${name}</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0,user-scalable=no"/>
		<meta name="MobileOptimized" content="320"/>
		<script src="${pageContext.request.contextPath}/portal/commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/commonjs/jquery.cookie.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/commonjs/jquery.jsonp.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/commonjs/commons.js" type="text/javascript"></script>
		<style type="text/css">
			/* css 重置 */
			body, p, input, h1, h2, h3, h4, h5, h6, ul, li{ margin: 0; padding: 0; list-style: none; vertical-align: middle;}
			img { border:0; margin: 0; padding: 0;}
			body { color: #000; -webkit-user-select: none; -webkit-text-size-adjust: none; font:normal 14px/200% "微软雅黑", helvetica, arial; text-align:left;   }
			header, section, footer { display: block; margin: 0; padding: 0 }
			a{text-decoration:none;color:#000;}
			.fl{float:left}
			.fr{float:right;}
			.cl{clear:both;}
			h3{font-size:16px;color:#252525;height: 22px;line-height: 22px;}
			/*公共样式*/

			/* 尾部 */
			footer{ text-align:center; padding:10px 0; line-height:18px; font-size:14px; }
			/* 内容 */
			#content{background:#fff;  }
			.path{text-align: center;height: 35px;font-size:18px;line-height: 35px;}

			/* 本例子css -------------------------------------- */
			.hd{ height:40px; line-height:40px; font-size:20px; overflow:hidden; background:#D5E2ED; border-top:1px solid #ccc;}
			.hd h3{ float:left; font-size:24px; }
			.hd h3 span{color:#649AC9; font-family:Georgia; margin-left:10px;  }
			.hd ul{ float:right;  width:100%;}

			.hd ul li{ float:left;vertical-align:top; width:50%;text-align: center;font-size: 16px;color: #649AC9}
			.hd ul li a{display: block;}
			.hd .cur{background: #027EDE !important;color:#fff !important;}

			/*content*/
			.content{
				width:100%;
			}

			/*左边 left-nav 菜式导航菜单开始*/
			.left-nav{
				width: 30%;
				height: 100%;
				background: url(${pageContext.request.contextPath}/portal/mb/restaurant/img/leftli-cur.png) repeat-y right 0;
			}
			.left-nav ul li{
				height: 85px;
				width: 100%;
				text-align: center;
				line-height: 85px;
				font-size: 14px;
				font-weight: bold;
				color: #000;
			}
			.left-nav-licur{
				background: url(${pageContext.request.contextPath}/portal/mb/restaurant/img/left-cur.png) no-repeat right center;
				color:#027EDE;
			}/*左边 left-nav 菜式导航菜单结束*/

			/*右边 right-caishi 菜品展示开始*/
			.right-caishi{
				width: 70%;
				overflow: auto;
			}
			.right-caishi ul li{
				padding:15px 0 0 15px;
				border-bottom:1px solid #DADADA;
				width:92%;
				height: 70px;
				line-height: 43px;
			}
			.right-caishi .pic{
				width: 80px;
				height: 54px;
				margin-right: 4px;
			}
			.right-caishi .pic img{
				width: 100%;
				height: 100%;
			}

			.intro-cur {
				background: url(${pageContext.request.contextPath}/portal/mb/restaurant/img/selected.png) no-repeat right center;
			}
			.intro p.biaoti{
				width: 110px;
				height: 16px;
				line-height: 16px;
				color:#121212;
				font-size: 16px;
				
				position: relative;
			}
			.intro p.biaoti span{
				display: block;
				float:right;
				width: 30px;
				height: 12px;
				background: url(${pageContext.request.contextPath}/portal/mb/restaurant/img/hot.png) no-repeat 0 0;
				background-size:30px 12px;
				overflow: hidden;
				position: absolute;
				left:66px;
				top:-5px;
			}
			.sale{
				text-decoration: line-through;
			}
			.intro p{
				width: 90px;
				height: 15px;
				font-size: 12px;
				color: #121212;
				line-height: 15px;
			}
			.intro p.discount{
				color:#F17424;
			}

			/*我的菜单 myorder 开始*/
			.myorder{
				width: 100%;
				height: 300px;
			}
			.myorder{
				width: 100%;
				height: 83px;
			}
			
			.caishi{
				border-bottom:1px solid #D2D2D2;
				margin-bottom: 3px;
			}
			.myorder .cainame{
				width: 30%;
				height: 100%;
				line-height: 83px;
				font-size: 16px;
				color: #838182;
				text-align: center;
			}
			.myorder .shuliang{
				width: 70%;
				height: 85px;
				 display: -webkit-box;
				 -webkit-box-orient: horizontal;
				 -webkit-box-pack: center;
				 -webkit-box-align: center;
				  display: -moz-box;
				 -moz-box-orient: horizontal;
				 -moz-box-pack: center;
				 -moz-box-align: center;
				 display: -o-box;
				 -o-box-orient: horizontal;
				 -o-box-pack: center;
				 -o-box-align: center;
				 display: -ms-box;
				 -ms-box-orient: horizontal;
				 -ms-box-pack: center;
				 -ms-box-align: center;
				 display: box;
				 box-orient: horizontal;
				 box-pack: center;
				 box-align: center;
			}
			.shuliang .inner{
				width: 100%;
				height: 26px;
			}
			.inner .small{
				float:left;
				-moz-box-flex:2.0; /* Firefox */
				-webkit-box-flex:1.0; /* Safari and Chrome */
				box-flex:1.0;
				width: 33%;
				color: #666465;
				font-size: 14px;
				height: 100%;
			}
			.inner .small:nth-child(3){
				width: 30%;
				padding-left:5px;
			}
			.inner .small span{
				display: block;
				float: left;
			}
			.inner .small:nth-child(1){
				text-align: right;
			}
			.inner .small:nth-child(3) span:nth-child(2){
				width: 75%;
				text-indent: 2px;
			}
			.inner .small:nth-child(3) span:nth-child(1){
				text-align: right;
			}

			.inner .small:nth-child(1) span:nth-child(1),.inner .small:nth-child(1) span:nth-child(2){
				font-size: 14px;
				color:#A2A0A1;
				text-decoration: line-through;
			}
			.inner .small:nth-child(1) span:nth-child(2){
				margin-right: 2%;
			}
			.inner .small:nth-child(1) span:nth-child(3),.inner .small:nth-child(1) span:nth-child(4){
				font-size: 14px;
				color: #EE5D00;
			}
			.inner .small:nth-child(2){
				position: relative;
			}
			.inner .small:nth-child(2) input{
				width: 24px;
				font-size: 12px;
				border:0;
				outline: none;
				text-align: center;
				background: none;
			}
			.inner .small:nth-child(2) span:nth-child(2){
				width:25px;
				height: 25px;
				border:1px solid #A4A2A3;
				position: absolute;
				left: 27%;
				
			}
			.inner .small:nth-child(2) span:nth-child(1){
				width: 16px;
				height: 16px;
				background: url(${pageContext.request.contextPath}/portal/mb/restaurant/img/jian.png) no-repeat 0 0;
				position: absolute;
				left: 0;
				top:5px;
			}
			.inner .small:nth-child(2) span:nth-child(3){
				width: 16px;
				height: 16px;
				background: url(${pageContext.request.contextPath}/portal/mb/restaurant/img/add.png) no-repeat 0 0;
				position: absolute;
				left: 75%;
				top:5px;
			}
			/*.xiadan 下单信息*/
			.xiadan{
				width: 100%;
				margin-bottom:40px;
			}
			 .hujiao{
				font-size: 16px;
				height: 20px;
				width: 100%;
				text-indent: 2em;
				line-height: 20px;
				color: #7D797A;
			}
			.xiadan .jiage{
				font-size: 16px;
				color: #848283;
			}
			.jiage p{
				height: 25px;
				position: relative;
			}
			.jiage p span{
				display: block;
				position: absolute;
				right: 20%;
				top:-5px;
			}
			.shifu{
				width: 130px;
				margin-bottom: 2%;
				padding-right: 5%;
			}
			
			.shifu span{
				color: #F05D00;
				font-size: 20px;
			}
			.yuanjia{
				width: 140px;
				margin-bottom: 2%;
				padding-right: 5%;
			}
			.yuanjia span{
				color: #666465;
				font-size: 18px;
				top:0 !important;
			}
			/*右边 right-caishi 菜品展示结束*/
			#submitFood{
				background-color:#EE8300;
				height:50px;
				width:94%;
				margin:0 auto;
				color: white;
				line-height: 50px;
				text-align: center;
				font-size: 20px;
				cursor: pointer;
			}
			#submitFood span{
				color: rgb(56, 85, 36);
				font-size: 26px;
				font-weight: bold;
			}
		</style>
	</head>
	<body>
		<div id="content">
			<div class="path">${name}</div>
			<div class="hd">
				<h3></h3>
				<ul>
					<li class="cur">全部菜品<span>(${fn:length(menus)})</span></li>
					<li>我的菜单<span>(0)</span></li>
				</ul>
			</div>
			<div class="content">
				<div class="allcai">
					<!--左边菜的种类-->
					<nav class="left-nav fl">
						<ul>
						<c:forEach items="${types}" var="row">
							<li><a href="javascript:void(0)">${row.name}</a></li>
						</c:forEach>
						</ul>
					</nav>
					<!--全部菜品-->
					<div class="right-caishi fr">
					<c:forEach items="${types}" var="type">
						<ul>
						<c:forEach items="${menus}" var="menu">
						<c:if test="${type.id==menu.type}">
							<li data-id="${menu.id}">
								<div class="pic fl"><img src="${menu.icon}" /></div>
								<div class="intro fl">
									<p class="biaoti">${menu.name}</p>
									<p class="sale">￥<span>${menu.old_price}</span>/份</p>
									<p class="discount">￥<span>${menu.new_price}</span>/份</p>
									<p class="nub"><span>${menu.times}</span>人点过</p>
								</div>
							</li>
						</c:if>
						</c:forEach>
						</ul>
					</c:forEach>
					</div>
				</div>
				<div class="myorder"><!--myorder开始-->
					<div id="mycai"></div>
					<div class="xiadan">
						<p class="hujiao">亲，请叫服务员下单</p>
						<div class="jiage">
							<div>
								<p class="shifu fr">实付：<span id="totalNewPrice">￥0</span></p>
								<div class="cl"></div>
							</div>

							<div>
								<p class="yuanjia fr">原价：<span id="totalOldPrice">￥0</span></p>
								<div class="cl"></div>
							</div>
							<div id="submitFood">提交</div>
						</div>
					</div>
				</div>
			</div><!--content内容开始-->
		</div>
	</body>
	<script type="text/javascript">
		$(document).ready(function(){
			initPage();
		});
		
		function initPage(){
			//设置默认显示的菜
			//第一个种类的菜显示
			$(".left-nav ul li").eq(0).addClass("left-nav-licur");
			$(".allcai").show().siblings().hide();
			$(".right-caishi ul").eq(0).css("display","block").siblings().css("display","none");

			//调整菜单的高度
			var contentHeight = $(window).height()-parseInt($("#content").children(".path").css("height"))
					-parseInt($("#content").children(".hd").css("height"))-1;
			$(".allcai").css("height",contentHeight);
			$(".right-caishi").css("height",contentHeight);
			//alert($(".content").css("height",$(document).height()));
			
			//切换全部菜单及我的菜单
			$(".hd ul li").click(function(){
				$(this).addClass("cur").siblings().removeClass("cur");
				var index = $(this).index();
				$(".content").children().eq(index).show().siblings().hide();
			});
			
			//菜品种类切换事件
			$(".left-nav ul li").click(function(){
				$(this).addClass("left-nav-licur").siblings().removeClass("left-nav-licur");
				/*需要添加的是左侧导航，右侧相应的显示*/
				var index = $(this).index();
				$(".right-caishi").children("ul").eq(index).show().siblings().hide();
			});

			//添加我的菜单及选中或取消我的菜单
			$(".right-caishi ul li").click(function(){
				//alert($(document).height());
				//$(this).children(".intro").addClass("intro-cur").parent().siblings().children(".intro").removeClass("intro-cur");
				if($(this).children(".intro").hasClass("intro-cur")){
					$(this).children(".intro").removeClass("intro-cur");
					$("#"+$(this).data("id")).remove();
					refreshFoodPrice();
				}else{
					$(this).children(".intro").addClass("intro-cur");
					addFood($(this));
				}
			});
			checkHistoryOrder();
		}
		
		
		function addSubmitEvent(){
			//提交菜单事件
			$("#submitFood").click(function(){
				var foodsParams = [];
				$("#mycai").children().each(function(){
					var foodId = $(this).attr("id");
					var foodNum = parseInt($(this).find(".small").eq(1).find("input").attr("value"));
					foodsParams.push(foodId+":"+foodNum);
				});
				if(foodsParams.length > 0){
					$("#submitFood").html("正在下单...");
					$.ajax({
					 	type: "POST",
						url: "${pageContext.request.contextPath}/portal/mb/restaurant/save",
						async:false,
						data:{orderId:$.cookie("orderId"),food:foodsParams},
						traditional:true,//用于处理data中包含数组时，传递到后台是key中包含[]
						success: function(data) {
							if(data.success){
								$.cookie("orderId", data.orderId,{expires:10});//历史数据存放10天
								$.cookie("orderNum", data.orderNum,{expires:10});//历史数据存放10天
								$.cookie("orderDate", data.orderDate,{expires:10});//历史数据存放10天
								$.cookie("orderFoods", foodsParams,{expires:10});//历史数据存放10天
								setOrderNumStatus(data.orderNum);
								alert("已下单，你的号牌为"+data.orderNum);
							}else{
								if(data.msg == "refresh"){
									alert("你的订单已存在，点击“确定”查看已有订单。");
									refreshPage();
								}else{
									alert(data.msg);
								}
								$("#submitFood").html("提交");
							}
						},
						error: function(){
							alert("下单失败，稍后请重试！");
							$("#submitFood").html("提交");
						}
					});
				}else{
					alert("请先点餐！");
				}
			});
		}
		
		function refreshPage(){
			var newLocation = window.location.href;
			if(newLocation.indexOf('?') != -1){
				newLocation += '&';
			}else{
				newLocation += '?';
			}
			newLocation += 'date='+new Date();
			location=newLocation;
		}
		
		function checkHistoryOrder(){
			var historyOrderId = $.cookie("orderId");
			$.ajax({
			 	type: "POST",
				url: "${pageContext.request.contextPath}/portal/mb/restaurant/get",
				data:{orderId:historyOrderId},
				success: function(data) {
					$.cookie("orderId", data.orderId,{expires:10});//历史数据存放10天
					$.cookie("orderNum", data.orderNum,{expires:10});//历史数据存放10天
					$.cookie("orderDate", data.orderDate,{expires:10});//历史数据存放10天
					$.cookie("orderFoods", data.foods,{expires:10});//历史数据存放10天
					setHistoryData(data.foods,data.orderNum);
				},
				error: function(){
// 					setHistoryData();
				}
			});
		}
		function setHistoryData(foods,orderNum){
			var historyFoods = foods || $.cookie("orderFoods") || '';
			if(historyFoods && ''!=historyFoods){//设置以前下过单的菜
				var foods = historyFoods.split("&");
				for(var i=0;i<foods.length;i++){
					if(foods[i].indexOf(":") != -1){
						var info = foods[i].split("=");
						if(info.length == 2){
							var foodInfo = info[1].split(":");
							if(foodInfo.length == 2){
								var thisElement = getFoodEmelent(foodInfo[0]);
								if("" != thisElement){
									thisElement.click();//添加菜
									var addedFoodId = thisElement.data("id");
									var num = parseInt(foodInfo[1]);
									var addFoodElement = $("#"+addedFoodId).find(".small").eq(1).children().eq(2);
									changeMyFood(addFoodElement,'set',num);
									//for(var i=1;i<num;i++){
										//foodElement.click();//添加数量
									//}
								}
							}
						}
					}
				}
				$(".hd ul li").eq(1).click();//将页面切换到我的菜单
			}
			orderNum = orderNum || $.cookie("orderNum");
			setOrderNumStatus(orderNum);
		}
		
		function setOrderNumStatus(orderNumParam){
			$("#submitFood").unbind("click");
			var orderNum = orderNumParam || $.cookie("orderNum") || '';
			if(orderNum != ''){
				$("#submitFood").css("cursor","default");
				$("#submitFood").html("你的号牌为：<span>"+orderNum+"</span>");
				$(".right-caishi ul li").unbind("click");
				$("#mycai").children(".caishi").each(function(){
					$(this).find(".small").eq(1).children().eq(0).unbind("click");
					$(this).find(".small").eq(1).children().eq(2).unbind("click");
				});
			}else{
				addSubmitEvent();
			}
		}
		
		function getFoodEmelent(foodId){
			var element = "";
			$(".right-caishi ul li").each(function(){
				var thisFoodId = $(this).data("id");
				if(thisFoodId == foodId){
					element = $(this);
					return false;
				}
			});
			return element;
		}
		
		//当选中某个菜时，将该菜添加到我的菜单中
		function addFood(thisFood){
			var caiMing = thisFood.find(".biaoti").text();
			var oldPrice = parseInt(thisFood.find(".sale").find("span").text());
			var newPrice = parseInt(thisFood.find(".discount").find("span").text());
			var id = thisFood.data("id");
			var addCaiHtml = '<div class="caishi" id="'+id+'">'+
								'<div class="cainame fl">'+caiMing+'</div>'+
								'<div class="shuliang fl">'+
									'<div class="inner">'+
										'<div class="small">'+
											'<span>￥</span>'+
											'<span>'+oldPrice+'</span>'+
											'<span>￥</span>'+
											'<span>'+newPrice+'</span>'+
											'<div class="cl"></div>'+
										'</div>'+
										'<div class="small">'+
											'<span></span>'+
											'<span><input type="text" maxlength="2" value="1" readonly="readonly"/></span>'+
											'<span></span>'+
										'</div>'+
										'<div class="small">'+
											'<span>￥</span>'+
											'<span>'+newPrice+'</span>'+
										'</div>'+
									'</div>'+
								'</div>'+
								'<div class="cl"></div>'+
							'</div>';
			$("#mycai").append(addCaiHtml);
			refreshFoodPrice();
			//减少点的菜的数量
			$("#"+id).find(".small").eq(1).children().eq(0).click(function(){
				var $foodNumObj = $(this).next().find("input");
				var currentNum = parseInt($foodNumObj.attr("value"));
				currentNum --;
				if(currentNum < 1){
					$(this).parent().parent().parent().parent().remove();
				}else{
					$foodNumObj.attr("value",""+currentNum);
				}
				var thisFoodTotalPrice = parseInt($(this).parent().parent().find(".small").eq(0).children().eq(3).html())*currentNum;
				$(this).parent().parent().find(".small").eq(2).children().eq(1).html(thisFoodTotalPrice);
				refreshFoodPrice();
			});
			//增加点的菜的数量
			$("#"+id).find(".small").eq(1).children().eq(2).click(function(){
				changeMyFood($(this),'add',1);
			});
		}
		
		function changeMyFood($this,type,num){
			var $foodNumObj = $this.prev().find("input");
			var thisNum = num;
			if('add' == type){
				thisNum = parseInt($foodNumObj.attr("value"));
				thisNum ++;
			}
			if(thisNum > 20){
				thisNum = 20;
			}
			$foodNumObj.attr("value",""+thisNum);
			var thisFoodTotalPrice = parseInt($this.parent().parent().find(".small").eq(0).children().eq(3).html())*thisNum;
			$this.parent().parent().find(".small").eq(2).children().eq(1).html(thisFoodTotalPrice);
			refreshFoodPrice();
		}
		
		//更新菜的个数及总价
		function refreshFoodPrice(){
			//更新我的菜单中，菜的个数
			$(".hd ul li:eq(1)").find("span").html("("+$("#mycai").children().length+")");
			
			//更新总价钱
			var totalNewPrice = 0;
			var totalOldPrice = 0;
			$("#mycai").children().each(function(){
				var foodNum = parseInt($(this).find(".small").eq(1).find("input").attr("value"));
				totalOldPrice += parseInt($(this).find(".small").eq(0).children().eq(1).html())*foodNum;
				totalNewPrice += parseInt($(this).find(".small").eq(0).children().eq(3).html())*foodNum;
			});
			$("#totalNewPrice").html("￥"+totalNewPrice);
			$("#totalOldPrice").html("￥"+totalOldPrice);
		}
	</script>
</html>
