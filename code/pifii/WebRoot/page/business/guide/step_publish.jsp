<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
	#guide_publish{
		width: 650px;
	}
	#guide_publish ul{
		width: 480px;
		height: 320px;
		margin:0 auto;
		margin-top: 30px;
	}
	#guide_publish ul>li{
		width: 235px;
		height: 60px;
		float: left;
/* 		margin-right: 75px; */
		margin-bottom: 18px;
		overflow: hidden;
	}
	#guide_publish ul>li.none_mr{
		margin-right: 0;
	}
	
	#guide_publish ul>li>div.router_info{
		width: 170px;
		white-space: nowrap;
	}
	
	#guide_publish ul>li span{
		color: #5b646f;
		font-size: 16px;
		height: 25px;
		line-height: 25px;
		overflow: hidden;
	}
</style>

<div id="guide_publish">
	<ul>
<!-- 		<li> -->
<!-- 			<div class="pic fl"><img src="images/business/wifi2.png"/></div> -->
<!-- 			<div class="fl"> -->
<!-- 				<p>小肥羊1店路由器</p> -->
<!-- 				<p>YINFU-wifi</p> -->
<!-- 			</div> -->
<!-- 		</li> -->
	</ul>
	<div class="cl"></div>
<!-- 	<div class="step_button"> -->
<!-- 		<span>保存</span> -->
<!-- 		<span>保存并发布</span> -->
<!-- 	</div> -->
</div>
<script type="text/javascript">
	$(function(){
		$.ajax({
			type: "POST",
			url: "business/device/getShopDevice",
			data:{shopId:getSelectedShopId()},
			success: function(data,status,xhr){
				if("success" == status){
					var deviceHtml = "";
					for(var i=0;i<data.length;i++){
						var cls = "";
						if(i%2 == 1){
							cls = 'class="none_mr"';
						}
						deviceHtml += '<li '+cls+'>' +
											'<div class="fl"><img src="images/business/wifi2.png"/></div>' +
											'<div class="fl router_info">' +
												'<span>'+data[i].shop_name+'</span>' +
												'<br>'+
												'<span>'+data[i].device_name+'</span>' +
											'</div>' +
										'</li>';
					}
					$("#guide_publish").find("ul").eq(0).html(deviceHtml);
					//名称太长时，滚动
					$("#guide_publish ul li .router_info span").each(function(){
						scrollMe($(this),$(this).parent().width());
					});
				}
			}
		});
	});
</script>
