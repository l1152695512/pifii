<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#feed_back_page{
		color: gray;
	}
	#feed_back_page .search{
		border-bottom: 1px solid #C0C4D1;
		padding-bottom: 30px;
		margin-top:20px;
	}
	#feed_back_page .search>div{
		margin-bottom:10px;
		height:35px;
		margin-left:100px;
	}
	#feed_back_page .search>div.data_search{
		float:left;
	}
	#feed_back_page .search>div>span{
		float: left;
/* 		width:150px; */
		font-size:15px;
		font-weight:bold;
		text-align:right;
		display:inline-block;
		height:25px;
		line-height:30px;
	}
	#feed_back_page .search>div>input[type="text"]{
		width:150px;
		height:25px;
		line-height:25px;
	}
	#feed_back_page .search input[type="button"] {
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
		margin-left: 5%;
	}
/* 	#feed_back_page .search{ */
/* 		border-bottom: 1px solid #C0C4D1; */
/* 		padding-bottom: 30px; */
/* 		margin-top:20px; */
/* 	} */
/* 	#feed_back_page .search>div>span{ */
/* 		float: left; */
/* /* 		width:150px; */ */
/* 		font-size:15px; */
/* 		font-weight:bold; */
/* 		text-align:right; */
/* 		display:inline-block; */
/* 		height:30px; */
/* 		line-height:30px; */
/* 	} */
/* 	#feed_back_page .search>div select,input[type="text"]{ */
/* 		width:180px; */
/* 		height:28px; */
/* 		line-height:28px; */
/* 		border: 1px solid #b4beca; */
/* 		outline: none; */
/* 	} */
/* 	#feed_back_page .search input[type="button"] { */
/* 		width: 133px; */
/* 		height: 38px; */
/* 		line-height: 30px; */
/* 		background: #14a1f9; */
/* 		color: #fff; */
/* 		font-size: 16px; */
/* 		font-weight: bold; */
/* 		border: 0; */
/* 		outline: none; */
/* 		border-radius: 5px; */
/* 		cursor: pointer; */
/* 		margin-left: 40%; */
/* 		margin-top: 20px; */
/* 	} */
	#feed_back_page .ft{
		width: 970px;
	}
	#feed_back_page .ft .data ul{
		border-bottom: 1px solid #dee2ed;
	}
	
	#feed_back_page .ft .data ul li{
		float: left;
		width: 192px;
		height: 48px;
/* 		text-align: center; */
		line-height: 48px;
		overflow: hidden;
	}
</style>

<div id="feed_back_page">
<!-- 	<div class="search"> -->
<!-- 		<div> -->
<!-- 			<span style="width:100px;">日期：</span> -->
<!-- 			<input type="text" id="start_date" class="Wdate" onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'end_date\')}'})" /> -->
<!-- 			&nbsp;至&nbsp;&nbsp; -->
<!-- 			<input type="text" id="end_date" class="Wdate" onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'start_date\')}',maxDate:'%y-%M-%d 23:59:59'})" />  -->
<!-- 		</div> -->
<!-- 		<input type="button" onclick="loadDataList(true);" value="查询"/> -->
<!-- 	</div> -->
	<div class="search">
		<div class="data_search">
			<span style="width:100px;">日期：</span>
			<input type="text" id="start_date" class="Wdate" onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'#F{$dp.$D(\'end_date\')}'})" />
			&nbsp;至&nbsp;&nbsp;<input type="text" id="end_date" class="Wdate" onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'start_date\')}',maxDate:'%y-%M-%d 23:59:59'})" />  
		</div>
		<input type="button" onclick="loadDataList(true);" value="查询"/>
	</div>
	<div class="ft">
		<div class="data" id="feedback_list_data"></div>
	</div>
	<div class="holder"></div>
</div>
<script type="text/javascript">
	$(function() {
		var now = new Date();
		now.setDate(now.getDate()-7); 
		$("#start_date").val(now.Format("yyyy-MM-dd")+" 00:00:00");
		$("#end_date").val((new Date()).Format("yyyy-MM-dd")+" 23:59:59");
		loadDataList();
	});
	function loadDataList(){
		var startDate = $("#start_date").val();
		var endDate = $("#end_date").val();
		$.ajax({
			type : "POST",
			url : 'business/feedback/list',
			data : {shopId:getSelectedShopId(),startDate:startDate,endDate:endDate},
			success : function(data) {
				var recHtml = "";
				if(data.length > 0){
					for(var i=0;i<data.length;i++){
						recHtml += '<ul>'+
// 										'<li style="width: 50px;">'+(i+1)+'</li>' +
										'<li style="width: 150px;">'+data[i].create_time+'</li>'+
										'<li style="width: 820px;"><span title="'+data[i].opinion+'">'+data[i].opinion+'</span></li>'+
										'<div class="cl"></div>' +
									'</ul>';
					}
				}else{
					recHtml = '<div><font color="red">没有记录！</font></div>';
				}
				$("#feed_back_page .data").html(recHtml);
				generPager();
// 				$("#feed_back_page .tips").tipsy({fade: true,delayIn: 100,gravity:$.fn.tipsy.autoNS});//gravity:'s',
			}
		});
	}
	
	function generPager(){
		$("#feed_back_page .holder").jPages({
			containerID:"feedback_list_data",
			first:'首页',
			last:'尾页',
			previous:'上页',
			next:'下页',
			perPage : 10,
			keyBrowse : true
		});
	}
</script>