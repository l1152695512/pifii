<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta content="text/html; charset=utf-8" http-equiv="Content-Type">
        <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
        <meta http-equiv="Content-Type" content"application/xhtml+xml;charset=UTF-8">
<!--         <meta http-equiv="Cache-Control" content="no-cache,must-revalidate"> -->
<!--         <meta http-equiv="pragma" content="no-cache"> -->
<!--         <meta http-equiv="expires" content="0"> -->
        <meta name="format-detection" content="telephone=no, address=no">
        <meta content="yes" name="apple-mobile-web-app-capable">
        <meta content="black-translucent" name="apple-mobile-web-app-status-bar-style">
        <title>设备激活</title>
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/portal/mb/workOrder/css/common.css">
        <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/portal/mb/workOrder/css/work_number.css">
        <script src="${pageContext.request.contextPath}/portal/commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=XpGabbd4W3nxxzOi4WCu03yt"></script>
		<script type="text/javascript" src="http://developer.baidu.com/map/jsdemo/demo/convertor.js"></script> 
        <script type="text/javascript" src="${pageContext.request.contextPath}/portal/mb/workOrder/js/google_ga.js"></script>
        <script type="text/javascript" src="${pageContext.request.contextPath}/portal/mb/workOrder/js/position.js"></script>
    </head>
    <body>
    	<h1 class="tit">设备激活</h1>
    	<div class="num">
        	<span>设备SN：</span>
            <p>${workOrderInfo.router_sn}</p>
        </div>
        <div class="all_type device_status">
        	<span>设备状态：</span>
            <p>未激活</p>
        </div>
        <div class="all_type location_address">
        	<span>定位地址：</span>
            <p id="add_position" data-position-success="0">定位中....</p>
            <div class="cl"></div>
        </div>
        <div class="num">
        	<span>工单编号：</span><input type="text" name="work_order_id" value="${workOrderInfo.wo_id}" /><div class="find" onClick="findWorkOrder();">查找</div>
        </div>
        <div class="all_type">
        	<span>所属组织：</span>
            <p id="org_name">${workOrderInfo.org_name}</p>
            <div class="cl"></div>
        </div>
        <div class="all_type">
        	<span>行业：</span>
            <p id="trde">${workOrderInfo.trde}</p>
            <div class="cl"></div>
        </div>
        <div class="all_type">
        	<span>类型：</span>
            <p id="trde_type">${workOrderInfo.trde_type}</p>
            <div class="cl"></div>
        </div>
<!--         <div class="all_type"> -->
<!--         	<span>商铺编号：</span> -->
<%--             <p id="shop_sn">${workOrderInfo.shop_sn}</p> --%>
<!--             <div class="cl"></div> -->
<!--         </div> -->
        <div class="all_type">
        	<span>商铺名称：</span>
            <p id="shop_name">${workOrderInfo.shop_name}</p>
            <div class="cl"></div>
        </div>
        <div class="all_type">
        	<span>联系方式：</span>
            <p id="tel">${workOrderInfo.tel}</p>
            <div class="cl"></div>
        </div>
        <div class="all_type">
        	<span>商铺地址：</span>
            <p id="location">${workOrderInfo.addr}</p>
            <div class="cl"></div>
        </div>
        <div class="all_type">
        	<span>工单状态：</span>
            <p id="state">${workOrderInfo.state}</p>
            <div class="cl"></div>
        </div>
<!--         <div class="address"> -->
<!--            <span>点亮地址：</span> -->
<!--            <ul> -->
<!--               <li class="shangpu a_icon" data-use-me="0"> -->
<!--                  <a href="javascript:void(0)"> -->
<!--                     <div class="addre_box"> -->
<!--                        <h2 class="fl">商铺地址</h2> -->
<!--                        <div class="cl"></div> -->
<!--                     </div> -->
<!--                  </a> -->
<!--               </li> -->
<!--               <li class="dingwei" data-use-me="1"> -->
<!--                  <a href="javascript:void(0)"> -->
<!--                     <div class="addre_box"> -->
<!--                        <h2 class="fl">定位地址</h2> -->
<!--                        <div class="cl"></div> -->
<!--                     </div> -->
<!--                  </a> -->
<!--               </li> -->
<!--           </ul> -->
<!--         </div> -->
        <input type="hidden" name="lng">
        <input type="hidden" name="lat">
        <div class="cl"></div>
        <div class="confirm" onClick="doneWorkOrder();">
        	<a href="javascript:void(0)">完成</a>
        </div>
    </body>
    <script type="text/javascript">
//     	var trdes = ["其他","餐饮","休闲娱乐","酒店","银行","商厦","美容美发"];
//     	$("#trde").text(trdes[parseInt("${workOrderInfo.trde}")]);
		$(function(){
			$(".location_address").show();
			$(".device_status").show();
			if("${workOrderInfo.wo_id}" != ""){
				$(".device_status p").text("已激活");
				$(".all_type,.address").show();
				$("input[name='work_order_id']").attr('disabled',"true");
				$(".confirm").remove();
				$(".find").remove();
				$(".address").remove();
				$("#add_position").text("${workOrderInfo.device_location}");
				$("#add_position").parent().children("span").first().text("盒子地址：");
			}else{
				getLocation();
			}
		});
		function findWorkOrder(){
			var workOrderId = $("input[name='work_order_id']").val();
			if("" != workOrderId){
				$.ajax({
					type: "POST",
					url: "${pageContext.request.contextPath}/portal/mb/workorder/getWorkOrderInfo",
					data: {workOrderId:workOrderId},
					success: function(data,status,xhr){
						if(data.workOrderInfo != ""){
							$("#org_name").text(data.workOrderInfo.org_name);
// 							$("#trde").text(trdes[parseInt(data.workOrderInfo.trde)]);
							$("#trde").text(data.workOrderInfo.trde);
// 							$("#shop_sn").text(data.workOrderInfo.shop_sn);
							$("#tel").text(data.workOrderInfo.tel);
							$("#location").text(data.workOrderInfo.addr);
							$("#shop_name").text(data.workOrderInfo.shop_name);
							$("#state").text(data.workOrderInfo.state);
							$("#trde_type").text(data.workOrderInfo.trde_type);
							$(".all_type,.address").show();
							if(data.workOrderInfo.wo_state == "2"){
								$(".confirm").hide();
							}else{
								$(".confirm").show();
							}
						}else{
							alert("该工单不存在！");
						}
					}
				});
			}else{
				alert("请先填写工单号！");
			}
		}
		function doneWorkOrder(){
			var workOrderId = $("input[name='work_order_id']").val();
			if("" != workOrderId){
// 				if($("#add_position").data("positionSuccess")=="0"){
// 					alert("未定位到当前的位置信息！");
// 				}else{
					var data = {workOrderId:workOrderId,sn:'${workOrderInfo.router_sn}'};
					var positionSuccess = $("#add_position").data("positionSuccess");
					if(positionSuccess == "1"){//定位成功
// 						var useMe = $(".address .a_icon").data("useMe");
						data.useMe = "1";
						data.location = $("#add_position").text();
						data.lng = $("input[name='lng']").val();
						data.lat = $("input[name='lat']").val();
// 						data = {useMe:"1",location:location,lng:lng,lat:lat};
					}else{
						data.useMe = "0";
// 						data = {useMe:"0"};
					}
					$.ajax({
						type: "POST",
						url: "${pageContext.request.contextPath}/portal/mb/workorder/doneWorkOrder",
						async:false,
						data: data,
						success: function(data,status,xhr){
							if(data.success == "true"){
								alert("成功激活。");
								window.location.reload();
							}else{
								alert(data.msg);
								if("refresh" == data.cmd){
									window.location.reload();
								}
							}
						}
					});
// 				}
			}else{
				alert("请先填写工单号！");
			}
		}
       </script>
</html>