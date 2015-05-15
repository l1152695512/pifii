<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#queueList{
		color: gray;
	}
	#queueList .search{
		border-bottom:1px solid #d0d0d0;
		margin-top: 20px;
	}
	#queueList .search .title{
		width:150px;
		height: 130px;
		color: #0083e8;
		font-size: 24px;
		font-weight: bold;
		text-align: center;
		line-height: 120px;
	}
	#queueList .search .numb{
		width:630px;
		height: 120px;
		overflow: hidden;
	}
	#queueList .search .numb li{
		width: 110px;
		height: 40px;
		float: left;
		margin-right: 5px;
		text-align: center;
		line-height: 40px;
		color: #027ede;
		font-size: 18px;
		cursor: pointer;
		margin-bottom: 27px;
	}
	#queueList .search .numb li:hover{
		background: #38a4f7;
		color: #fff !important;
		border-radius: 5px;
	}
	#queueList .search .liClick{
		background: #38a4f7;
		color: #fff !important;
		border-radius: 5px;
	}
	#queueList .ft{
		width: 970px;
	}
	#queueList .ft .data{
		position:relative;
	}
	#queueList .ft .data .waiting{
		width: 40px;
		height: 60px;
		background-image: url(images/business/waiting.png);
		position: absolute;
		left: 80px;
		top: -16px;
	}
	#queueList .ft .data ul{
		border-bottom: 1px solid #dee2ed;
	}
	#queueList .ft .data ul li{
		float: left;
		width: 192px;
		height: 48px;
		text-align: center;
		line-height: 48px;
		overflow: hidden;
	}
	
	#queueList .done{
		display: inline-block;
		width: 53px;
		height: 30px;
		line-height: 30px;
		color: #fff;
		border-radius: 2px;
		cursor: pointer;
		margin-right: 10px;
		background: rgb(107, 197, 253);
	}
	#queueList .done:hover{
		background: #14a1f9;
	}
</style>
<div id="queueList">
	<div class="search">
		<div class="title fl">座位号:</div>
		<ul class="numb fl">
			<li data-num="1">1人</li>
			<li data-num="2">2人</li>
			<li data-num="3">3人</li>
			<li data-num="4">4人</li>
			<li data-num="6">6人</li>
			<li data-num="8">8人</li>
			<li data-num="10">10人</li>
			<li data-num="12">12人</li>
			<li data-num="14">12人以上</li>
			<li data-num="" class="liClick">全部</li>
		</ul>
		<div class="cl"></div>
	</div>
	<div class="ft">
		<div class="data" id="list_data_queue"></div>
	</div>
	<div class="holder"></div>
</div>
<script type="text/javascript">
	var previousData = "";
	var refreshInterval = 5000;//5秒一刷新
	var thisTimeout;
	$(function() {
		$("#queueList .search li").click(function(){
			$(this).addClass('liClick').siblings().removeClass('liClick');
			clearTimeout(thisTimeout); 
			loadDataList(true);
		});
		loadDataList(true);
	});
	function loadDataList(isClick){
		if(undefined != $('#queueList').html()){//用户停留在该页面
			var index = $.fn._maxZIndexOptionIndex();
			if(index == -1 || isClick){//无弹出页面
				var global = false;
				if(isClick){
					global = true;
					previousData = "-1";
				}
				var tablePersonNum = $("#queueList .search li.liClick").data("num");
				$.ajax({
					type : "POST",
					global: global,
					url : 'business/queue/list',
					data : {shopId:getSelectedShopId(),personNum:tablePersonNum,previousData:previousData},
					success : function(data) {
						if(data.thisData != previousData || isClick){
							previousData = data.thisData;
							var recHtml = "";
							if(data.dataList.length > 0){
								for(var i=0;i<data.dataList.length;i++){
									recHtml += '<ul data-id="'+data.dataList[i].id+'">'+
													'<li style="width: 150px;clear:both;text-align: right;">'+(i+1)+'</li>' +
													'<li style="width: 150px;">排队号：'+data.dataList[i].queue_num+'</li>'+
													'<li style="width: 150px;">人数：'+data.dataList[i].person_num+'人</li>'+
													'<li style="width: 300px;">'+data.dataList[i].date+'</li>'+
													'<li style="width: 200px;" data-num="'+data.dataList[i].queue_num+'"><span class="done">完成</span></li>'+
													'<div class="cl"></div>' +
												'</ul>';
								}
								recHtml += '<div class="cl"></div>'+
											'<div class="waiting"></div>';
							}else{
								recHtml = '<div><font color="red">没有记录！</font></div>';
							}
							$("#queueList .data").html(recHtml);
							generPager();
							addOperateEvent();
						}
	 				},
	 				complete:function(){
	 					clearTimeout(thisTimeout); 
	 					thisTimeout = setTimeout('loadDataList()',refreshInterval);
					}
				});
			}else{
				clearTimeout(thisTimeout); 
				thisTimeout = setTimeout('loadDataList()',refreshInterval);
			}
		}
	}
	function addOperateEvent(){
		$("#queueList .data li .done").click(function(){
			var id = $(this).parent().parent().data("id");
			var num = $(this).parent().data("num");
			myConfirm("确定排队号【"+num+"】已完成？",function(){
				$.ajax({
					type : "POST",
					url : 'business/queue/removeNum',
					data : {id:id},
					success : function(data) {
						loadDataList(true);
					}
				});
			});
		});
	}
	function generPager(){
		$("#queueList .holder").jPages({
			containerID:"list_data_queue",
			first:'首页',
			last:'尾页',
			previous:'上页',
			next:'下页',
			perPage : 10,
			keyBrowse : true
		});
	}
</script>