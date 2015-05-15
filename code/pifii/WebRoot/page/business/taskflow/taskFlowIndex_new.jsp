<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#taskFlowList{
		color: gray;
	}
	#taskFlowList .search{
		border-bottom: 1px solid #C0C4D1;
		padding-bottom: 30px;
		margin-top:20px;
	}
	#taskFlowList .search>div{
		margin-bottom:10px;
		height:35px;
	}
	#taskFlowList .search>div.first_search{
		float:left;
	}
	#taskFlowList .search>div>span{
		float: left;
/* 		width:150px; */
		font-size:15px;
		font-weight:bold;
		text-align:right;
		display:inline-block;
		height:25px;
		line-height:30px;
	}
	#taskFlowList .search>div>select{
		width:150px;
		height:25px;
		line-height:25px;
	}
	#taskFlowList .search input[type="button"] {
		width: 115px;
		height: 30px;
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
	}
	#taskFlowList .ft{
		width: 970px;
/* 		width:100%; */
/* 		border-top:2px solid #dae0e7; */
/* 		border-bottom:2px solid #c0c4d1; */
/* 		margin-bottom: 65px; */
	}
	#taskFlowList .ft >ul li{
		float:left;
		height: 43px;
/* 		border-bottom: 2px solid #c0c4d1; */
		line-height: 43px;
		text-align: center;
		font-weight: bold;
		font-size: 16px;
	}
	#taskFlowList .ft .data ul li{
		float: left;
		width: 192px;
		height: 48px;
		border-top: 1px solid #dee2ed;
		text-align: center;
		line-height: 48px;
		overflow: hidden;
	}
	#taskFlowList .ft .data ul li .bars{
		float: left;
		margin-left: 15%;
		width: 85%;
	}
	#taskFlowList .ft .data ul li .bars .bar{
		float: left;
		margin-top: 17px;
		height: 15px;
		width: 75%;
		background: #E3E3E3;
	}
	#taskFlowList .ft .data ul li .bars .bar .precent{
		width: 45.9%;
		height: 100%;
		background: #1E7CD4;
	}
	#taskFlowList .ft .data ul li .bars .precentNum{
		text-align: right;
	}
	
</style>

<div id="taskFlowList">
	<div class="search">
		<div class="first_search">
			<span>智能盒子：</span>
			<select name="deviceList">
<!-- 				<option selected="selected" value="">全部</option> -->
			</select>
		</div>
		<div>
			<span style="width:100px;">日期：</span>
			<input type="text" id="task_flow_start" class="Wdate" onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'task_flow_end\')}'})" />
			&nbsp;至&nbsp;&nbsp;<input type="text" id="task_flow_end" class="Wdate" onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'task_flow_start\')}',maxDate:'#F{$dp.$D(\'task_flow_start\',{d:+30})}'})" /> 
		</div>
		<input type="button" onclick="generPageChangeLogPager(true);" value="查询"/>
	</div>
	<div class="ft">
		<ul>
			<li style="width:80px">序号</li>
<!-- 			<li style="width:250px">路由名称</li> -->
			<li style="width:150px">时间</li>
			<li style="width:250px">任务描述</li>
			<li style="width:150px">内容大小</li>
			<li style="width:140px">盒子状态</li>
			<li style="width:200px">任务状态</li>
			<li style="float: none;width: 0px;"></li>
		</ul>
		<div class="cl"></div>
		<div class="data" id="list_data_task_flow"></div>
	</div>
	<div class="holder"></div>
</div>
<script type="text/javascript">
	//下面定义的变量是解决点击查询时如果搜索条件没有发生变化，则直接刷新当前页的数据
	var previousSearchParams = {};
	
	$(function() {
		var now = new Date();
		now.setDate(now.getDate()-3); 
		$("#task_flow_start").val(now.Format("yyyy-MM-dd"));
		$("#task_flow_end").val((new Date()).Format("yyyy-MM-dd"));
		loadShopDevice();
// 		$("#taskFlowList search input[type=button]").click(function(){
// 			generPageChangeLogPager();
// 		});
	});
	function loadShopDevice(){
		$.ajax({
			type : "POST",
			url : 'business/device/getDeviceSelect',
			data : {shopId:getSelectedShopId()},
			success : function(data) {
				var selectHtml="";
				for(var i=0;i<data.length;i++){
					if(i==0){
						selectHtml += '<option selected="selected" value="'+data[i].id+'">'+data[i].name+'</option>';
					}else{
						selectHtml += '<option value="'+data[i].id+'">'+data[i].name+'</option>';
					}
				}
				$("#taskFlowList select[name=deviceList]").append(selectHtml);
				generPageChangeLogPager(false);
			}
		});
	}
// 	function searchPageChangeLog(){
// 		var selectDevice = $("#taskFlowList select[name=deviceList] option:selected").val();
// // 		var routerName = $("#taskFlowList input[name=deviceId]").val();
// 		var startDate = $("#task_flow_start").val();
// 		var endDate = $("#task_flow_end").val();
// 		$.ajax({
// 			type : "POST",
// 			url : 'business/page/getPageChangeLog',
// 			data : {shopId:getSelectedShopId(),deviceId:selectDevice,startDate:startDate,endDate:endDate},
// 			success : function(data) {
// 				var recHtml = "";
// 				if(data.apps.length > 0){
// 					for(var i=0;i<data.apps.length;i++){
// 						recHtml += '<ul>'+
// 										'<li style="width: '+$("#taskFlowList .ft>ul>li").eq(0).width()+'px;">'+(i+1)+'</li>'+
// 										'<li style="width: '+$("#taskFlowList .ft>ul>li").eq(1).width()+'px;">'+data.apps[i].routerName+'</li>'+
// 										'<li style="width: '+$("#taskFlowList .ft>ul>li").eq(2).width()+'px;">'+data.apps[i].operate_date+'</li>'+
// 										'<li style="width: '+$("#taskFlowList .ft>ul>li").eq(3).width()+'px;" title="'+data.apps[i].operateDes+'">'+data.apps[i].operateDes+'</li>'+
// 										'<li style="width: '+$("#taskFlowList .ft>ul>li").eq(4).width()+'px;">';
// 						if(data.apps[i].status == 0){
// 							recHtml += '<span style="color:#FF0000;">未同步';
// 							if(data.isOnline == 1){
// 								recHtml += '【5分钟后同步完成】';
// 							}else if(data.isOnline == 0){
// 								recHtml += '【设备离线】';
// 							}
// 							recHtml += '</span>';
// 						}else{
// 							recHtml += '<span style="color:#5783E0;">已同步</span>';
// 						}
// 						recHtml += '</li>'+
// 									'<li style="float: none;width: 0px;"></li>'+
// 								'</ul>';
// 					}
// 				}else{
// 					recHtml = '<div><font color="red">没有记录！</font></div>';
// 				}
// 				$("#taskFlowList .data").html(recHtml);
// 				generPager();
// 			}
// 		});
// 	}
	function generPageChangeLogPager(isSearch){
		var selectDevice = $("#taskFlowList select[name=deviceList] option:selected").val();
		var startDate = $("#task_flow_start").val();
		var endDate = $("#task_flow_end").val();
		var data={shopId:getSelectedShopId(),deviceId:selectDevice,startDate:startDate,endDate:endDate};
		var startPage = 1;
		if(isSearch && previousSearchParams.deviceId==data.deviceId 
				&& previousSearchParams.startDate==data.startDate && previousSearchParams.endDate==data.endDate){
			startPage = previousSearchParams.pageNum;
		}
		$("#taskFlowList .holder").jPages({
			containerID:"list_data_task_flow",
			first:'首页',
			last:'尾页',
			previous:'上页',
			next:'下页',
			perPage : 10,
			startPage:startPage,
			keyBrowse : true,
			realPagination:true,
	        serverParams:{
	        	url:"business/page/getPageChangeLog",
	        	data:data,
	        	generDataHtml:generPageChangeLogData
	        }
		});
	}
	function generPageChangeLogData(data,searchParams){
		var recHtml = "";
		if(data.length > 0){
			var list_header = $("#taskFlowList .ft>ul>li");
			for(var i=0;i<data.length;i++){
				recHtml += '<ul>'+
								'<li style="width: '+list_header.eq(0).width()+'px;">'+(searchParams.startRowNum+i)+'</li>'+
								'<li style="width: '+list_header.eq(1).width()+'px;">'+data[i].operate_date+'</li>'+
								'<li style="width: '+list_header.eq(2).width()+'px;" title="'+data[i].task_desc+'">'+data[i].task_desc+'</li>'+
								'<li style="width: '+list_header.eq(3).width()+'px;">'+data[i].file_size+'</li>'+
								'<li style="width: '+list_header.eq(4).width()+'px;">';
				if(data[i].isOnline == 1){
					recHtml += '在线';
				}else if(data[i].isOnline == 0){
					recHtml += '<span style="color:#FF0000;">离线</span></li>';
				}				
				recHtml += '<li style="width: '+list_header.eq(5).width()+'px;">';	
				if(data[i].step == -1){
					recHtml += '<span style="color:#FF0000;" title="'+data[i].error_msg+'">任务同步失败【'+data[i].error_msg+'】</span>';
				}else if(data[i].step == 2){
					recHtml += '<span style="color:#5783E0;">任务已同步</span>';
				}else{//data[i].progress
					recHtml += '<div class="bars">' +
									'<div class="bar"><div class="precent" style="width: '+data[i].progress+';"></div></div>' +
									'<div class="precentNum">'+data[i].progress+'</div>' +
								'</div>';
				}
				recHtml += '</li>'+
							'<li style="float: none;width: 0px;"></li>'+
						'</ul>';
			}
		}else{
			recHtml = '<div><font color="red">没有记录！</font></div>';
		}
		$("#taskFlowList .data").html(recHtml);
		previousSearchParams = searchParams;
	}
</script>