<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
	#food_order_info .data_list .ft{width: 100%;}
	#food_order_info .data_list .ft>ul li{float:left;height: 43px;border-bottom: 2px solid #c0c4d1;line-height: 43px;text-align: center;font-weight: bold;font-size: 16px;overflow: hidden;}
	#food_order_info .data_list .ft .data ul li{float: left;height: 80px;border-bottom: 1px solid #dee2ed;text-align: center;line-height: 80px;overflow: hidden;}
	#food_order_info .data_list .ft .data ul li img{height:80px;}
</style>
<div id="food_order_info">
	<div class="data_list">
		<div class="ft">
			<ul>
			<li style="width: 30px;"></li>
			<li style="width: 120px;">菜名</li>
			<li style="width: 110px;">图片</li>
			<li style="width: 70px;">数量</li>
			<li style="width: 80px;">原价(元)</li>
			<li style="width: 80px;">现价(元)</li>
			<li style="width: 100px;">菜类</li>
			<li style="width: 100px;">口味</li>
		</ul>
			<div class="cl"></div>
			<div class="data" id="food_order_info_list_data"></div>
		</div>
		<div class="holder"></div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		$.ajax({
			type : "POST",
			url : "business/food/orderInfo",
			data : {orderId:'${orderId}'},
			success : function(data) {
				var recHtml = "";
				if(data.length > 0){
					var list_header = $("#food_order_info .ft>ul>li");
					for(var i=0;i<data.length;i++){
						if("" == data[i].icon){//兼容火狐，火狐浏览器中如果src为空字符串时，是不会执行onerror的
							data[i].icon = "aa.jpg";
						}
						recHtml += '<ul>'+
										'<li style="width: '+list_header.eq(0).width()+'px;">'+(i+1)+'</li>' +
										'<li style="width: '+list_header.eq(1).width()+'px;">'+data[i].name+'</li>'+
										'<li style="width: '+list_header.eq(2).width()+'px;"><img src="'+data[i].icon+'" onerror="this.src=\'images/business/ordering/favorable_pic.png\'" /></li>'+
										'<li style="width: '+list_header.eq(3).width()+'px;">'+data[i].food_num+'</li>'+
										'<li style="width: '+list_header.eq(4).width()+'px;">'+data[i].old_price+'</li>'+
										'<li style="width: '+list_header.eq(5).width()+'px;">'+data[i].new_price+'</li>'+
										'<li style="width: '+list_header.eq(6).width()+'px;">'+data[i].food_type_name+'</li>'+
										'<li style="width: '+list_header.eq(7).width()+'px;float: none;">'+data[i].taste+'</li>'+
									'</ul>';
					}
				}else{
					recHtml = '<div><font color="red">没有记录！</font></div>';
				}
				$("#food_order_info .data_list .data").html(recHtml);
				generFoodInfoPager();
			}
		});
	});
	function generFoodInfoPager(){
		$("#food_order_info .holder").jPages({
			containerID:"food_order_info_list_data",
			first:'首页',
			last:'尾页',
			previous:'上页',
			next:'下页',
			perPage : 5,
			keyBrowse : true
		});
	}
</script>