<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#foodOrderList{
		color: gray;
	}
	#foodOrderList .search{
		border-bottom: 1px solid #C0C4D1;
		padding-bottom: 30px;
		margin-top:20px;
	}
	#foodOrderList .search>div.status_style{
		float:left;
	}
	#foodOrderList .search>div>span{
		float: left;
/* 		width:150px; */
		font-size:15px;
		font-weight:bold;
		text-align:right;
		display:inline-block;
		height:30px;
		line-height:30px;
	}
	#foodOrderList .search>div select,input[type="text"]{
		width:180px;
		height:28px;
		line-height:28px;
		border: 1px solid #b4beca;
		outline: none;
	}
	#foodOrderList .search input[type="button"] {
		width: 133px;
		height: 38px;
		line-height: 30px;
		background: #14a1f9;
		color: #fff;
		font-size: 16px;
		font-weight: bold;
		border: 0;
		outline: none;
		border-radius: 5px;
		cursor: pointer;
		margin-left: 40%;
		margin-top: 20px;
	}
	#foodOrderList .ft{
		width: 970px;
	}
	#foodOrderList .ft >ul li{
		float:left;
		height: 43px;
/* 		border-bottom: 2px solid #c0c4d1; */
		line-height: 43px;
		text-align: center;
		font-weight: bold;
		font-size: 16px;
	}
	#foodOrderList .ft .data ul li{
		float: left;
		width: 192px;
		height: 48px;
		border-top: 1px solid #dee2ed;
		text-align: center;
		line-height: 48px;
		overflow: hidden;
	}
	#foodOrderList .operate{
		display: inline-block;
		width: 53px;
		height: 30px;
		line-height: 30px;
		color: #fff;
		border-radius: 2px;
		cursor: pointer;
		margin-right: 10px;
	}
	
	#foodOrderList .done{
		background: rgb(107, 197, 253);
	}
	#foodOrderList .done:hover{
		background: #14a1f9;
	}
	#foodOrderList .cancel{
		background: rgb(223, 163, 161);
	}
	#foodOrderList .cancel:hover{
		background: rgb(211, 70, 65);
	}
	
	#foodOrderList .print{
		background: rgb(173, 171, 171);
	}
	#foodOrderList .print:hover{
		background: rgb(102, 102, 102);
	}
	#foodOrderList .foods{
		background: rgb(104, 163, 250);
	}
	#foodOrderList .foods:hover{
		background: rgb(43, 128, 253);
	}
</style>

<div id="foodOrderList">
	<div class="search">
		<div class="status_style">
			<span>状态：</span>
			<select name="foorOrderStatus">
				<option selected="selected" value="1">正在进行</option>
				<option value="2">完成</option>
				<option value="0">撤销</option>
			</select>
		</div>
		<div>
			<span style="width:100px;">日期：</span>
			<input type="text" id="start_date" class="Wdate" onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'end_date\')}'})" />
			&nbsp;至&nbsp;&nbsp;
			<input type="text" id="end_date" class="Wdate" onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'start_date\')}',maxDate:'%y-%M-%d 23:59:59'})" /> 
		</div>
		<input type="button" onclick="loadDataList(true);" value="查询"/>
	</div>
	<div class="ft">
		<ul>
			<li style="width:50px">序号</li>
			<li style="width:70px">号牌</li>
			<li style="width:80px">菜品数量</li>
			<li style="width:100px">原价(元)</li>
			<li style="width:100px">折后价(元)</li>
			<li style="width:110px">状态</li>
			<li style="width:150px">下单时间</li>
			<li style="width:310px">操作</li>
			<li style="float: none;width: 0px;"></li>
		</ul>
		<div class="cl"></div>
		<div class="data" id="list_data_food_order"></div>
	</div>
	<div class="holder"></div>
</div>
<script type="text/javascript">
	var previousData = "";
	var refreshInterval = 5000;//5秒一刷新
	var foodOrderTimeout;
	$(function() {
		var now = new Date();
		now.setDate(now.getDate()-7); 
		$("#start_date").val(now.Format("yyyy-MM-dd")+" 00:00:00");
		$("#end_date").val(new Date().Format("yyyy-MM-dd")+" 23:59:59");
		loadDataList(true);
	});
	function loadDataList(isClick){
		if(undefined != $('#foodOrderList').html()){//用户停留在该页面
			var index = $.fn._maxZIndexOptionIndex();
			if(index == -1 || isClick){//无弹出页面
				var global = false;
				if(isClick){
					global = true;
					previousData = "-1";
				}
				var foorOrderStatus = $("#foodOrderList .search select[name=foorOrderStatus] option:selected").val();
				var startDate = $("#start_date").val();
				var endDate = $("#end_date").val();
				$.ajax({
					type : "POST",
					global: global,
					url : 'business/food/list',
					data : {shopId:getSelectedShopId(),status:foorOrderStatus,startDate:startDate,endDate:endDate,previousData:previousData},
					success : function(data) {
						if(data.thisData != previousData || isClick){
							previousData = data.thisData;
							var recHtml = "";
							if(data.dataList.length > 0){
								var list_header = $("#foodOrderList .ft>ul>li");
								for(var i=0;i<data.dataList.length;i++){
									var operateHtml = '<span class="operate foods">详情</span>';
									var statusDesc = "";
									if(data.dataList[i].status == '1'){
										 statusDesc = "正在进行";
										 operateHtml = '<span class="operate done">完成</span><span class="operate cancel">撤销</span><span class="operate print">打印</span>'+operateHtml;
									}else if(data.dataList[i].status == '2'){
										statusDesc = "已完成";
									}else if(data.dataList[i].status == '0'){
										statusDesc = "已撤销";
									}
									recHtml += '<ul data-id="'+data.dataList[i].id+'">'+
													'<li style="width: '+list_header.eq(0).width()+'px;">'+(i+1)+'</li>' +
													'<li style="width: '+list_header.eq(1).width()+'px;"><span style="font-weight: bold;">'+data.dataList[i].order_num+'</span></li>'+
													'<li style="width: '+list_header.eq(2).width()+'px;">'+data.dataList[i].food_num+'</li>'+
													'<li style="width: '+list_header.eq(3).width()+'px;">'+data.dataList[i].old_price+'</li>'+
													'<li style="width: '+list_header.eq(4).width()+'px;">'+data.dataList[i].new_price+'</li>'+
													'<li style="width: '+list_header.eq(5).width()+'px;">'+statusDesc+'</li>'+
													'<li style="width: '+list_header.eq(6).width()+'px;">'+data.dataList[i].insert_date+'</li>'+
													'<li style="width: '+list_header.eq(7).width()+'px;">'+operateHtml+'</li>'+
													'<li style="float: none;width: 0px;"></li>'+
												'</ul>';
								}
							}else{
								recHtml = '<div><font color="red">没有记录！</font></div>';
							}
							$("#foodOrderList .data").html(recHtml);
							generPager();
							addOperateEvent();
						}
	 				},
	 				complete:function(){
	 					clearTimeout(foodOrderTimeout); 
	 					foodOrderTimeout = setTimeout('loadDataList()',refreshInterval);
					}
				});
			}else{
				clearTimeout(foodOrderTimeout); 
				foodOrderTimeout = setTimeout('loadDataList()',refreshInterval);
			}
		}
	}
	function addOperateEvent(){
		$("#foodOrderList .data li .print").click(function(){
			var orderId = $(this).parent().parent().data("id");
			var orderNum = $(this).parent().parent().children("li").eq(1).find("span").text();
			print("确定打印【"+orderNum+"】号牌的订单？",getSelectedShopId(),orderId);
		});
		$("#foodOrderList .data li .done").click(function(){
			var orderId = $(this).parent().parent().data("id");
			var orderNum = $(this).parent().parent().children("li").eq(1).find("span").text();
			changeStatus("确定号牌【"+orderNum+"】的订单已完成？",2,orderId);
		});
		$("#foodOrderList .data li .cancel").click(function(){
			var orderId = $(this).parent().parent().data("id");
			var orderNum = $(this).parent().parent().children("li").eq(1).find("span").text();
			changeStatus("确定要取消号牌【"+orderNum+"】的订单？",0,orderId);
		});
		$("#foodOrderList .data li .foods").click(function(){
			var orderId = $(this).parent().parent().data("id");
			$.fn.SimpleModal({
// 				model: 'modal-ajax',
				title: '订单详情',
// 				width: 1000,
// 				height: 555,
// 				hideFooter: true,
				param: {
					url: 'business/food/orderInfoIndex',
					data:{orderId:orderId}
				}
			}).showModal();
		});
	}
	
	function print(tips,shopId,orderId){
		myConfirm(tips,function(){
			$.ajax({
				type : "POST",
				url : 'business/food/print',
				data : {orderId:orderId,shopId:shopId},
				success : function(data) {
					if(data.reslutCode ==  0){
						myAlert("打印成功");
					}else{
						myAlert("打印失败");
					}
				}
			});
		});
	}
	function changeStatus(tips,status,orderId){
		myConfirm(tips,function(){
			$.ajax({
				type : "POST",
				url : 'business/food/changeStatus',
				data : {id:orderId,status:status},
				success : function(data) {
					loadDataList(true);
				}
			});
		});
	}
	function generPager(){
		$("#foodOrderList .holder").jPages({
			containerID:"list_data_food_order",
			first:'首页',
			last:'尾页',
			previous:'上页',
			next:'下页',
			perPage : 10,
			keyBrowse : true
		});
	}
</script>