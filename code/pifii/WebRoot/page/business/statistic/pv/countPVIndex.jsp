<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
	#countPVList{
		color: gray;
	}
	#countPVList .search{
		border-bottom: 1px solid #C0C4D1;
		padding-bottom: 30px;
		margin-top:20px;
	}
	#countPVList .search>div{
		margin-bottom:10px;
		height:35px;
		margin-left:100px;
	}
	#countPVList .search>div.data_search{
		float:left;
	}
	#countPVList .search>div>span{
		float: left;
/* 		width:150px; */
		font-size:15px;
		font-weight:bold;
		text-align:right;
		display:inline-block;
		height:25px;
		line-height:30px;
	}
	#countPVList .search>div>input[type="text"]{
		width:150px;
		height:25px;
		line-height:25px;
	}
	#countPVList .search input[type="button"] {
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
	#countPVList .ft{
/* 		width: 900px; */
		overflow-y: hidden;
		overflow-x: auto;
/* 		width:100%; */
/* 		border-top:2px solid #dae0e7; */
/* 		border-bottom:2px solid #c0c4d1; */
/* 		margin-bottom: 65px; */
	}
	#countPVList .ft >ul{
		border-bottom: 1px solid #dee2ed;
	}
	#countPVList .ft >ul li{
		float:left;
		height: 43px;
/* 		border-bottom: 2px solid #c0c4d1; */
		line-height: 43px;
		text-align: center;
		font-weight: bold;
		font-size: 16px;
		margin-right: 15px;
	}
	#countPVList .ft >ul li:first-child{
		border-right: 1px solid #dee2ed;
	}
	#countPVList .ft .data ul{
		margin:0;
		padding:0;
		border-bottom: 1px solid #dee2ed;
	}
	#countPVList .ft .data ul li{
		float: left;
/* 		width: 192px; */
		height: 48px;
		text-align: center;
		line-height: 48px;
		margin:0;
		padding:0;
		margin-right: 15px;
	}
	#countPVList .ft .data ul li:first-child{
		font-weight: bold;
		border-right: 1px solid #dee2ed;
	}
</style>

<div id="countPVList">
	<div class="search">
		<div class="data_search">
			<span style="width:100px;">日期：</span>
			<input type="text" id="countPV_start" class="Wdate" value="${firstDay}" onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'countPV_end\')}'})" />
			&nbsp;至&nbsp;&nbsp;<input type="text" id="countPV_end" class="Wdate" value="${nowDay}" onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'countPV_start\')}',maxDate:'%y-%M-%d'})" /> 
		</div>
		<input type="button" onclick="searchPageChangeLog();" value="查询"/>
	</div>
	<div class="ft">
		<ul>
			<li style="width: 100px;">日期</li>
			<c:forEach items="${typeList}" var="type">	
				<li>${type.name}</li>
			</c:forEach>
			<div class="cl"></div>
		</ul>
		<div class="data" id="list_data_task_flow"></div>
<!-- 		<div class="cl"></div> -->
	</div>
	<div class="holder"></div>
</div>
<script type="text/javascript">
	$(function() {
// 		$("#countPV_end").val((new Date()).Format("yyyy-MM-dd"));
		changeTitleWidth();
	});
	function changeTitleWidth(){
		var maxWidth = $("#countPVList .ft").width();
		var totalWidth = 0;
		$("#countPVList .ft>ul>li").each(function(){
			totalWidth += $(this).width()
							+parseInt($(this).css("margin-right"))+parseInt($(this).css("margin-left"))
							+parseInt($(this).css("padding-right"))+parseInt($(this).css("padding-left"))
							+(parseInt($(this).css("borderRightWidth"))||0)+(parseInt($(this).css("borderLeftWidth"))||0)+2;
		});
		if(totalWidth > maxWidth){
			$("#countPVList .ft>ul").css("width",totalWidth);
		}else{
			$("#countPVList .ft>ul").css("width",maxWidth);
			var titleColumnNum = $("#countPVList .ft>ul>li").length;
			var addWidth = (maxWidth - totalWidth)/(titleColumnNum-1);
			$("#countPVList .ft>ul>li").each(function(index){
				if(index > 0){
					var thisWidth = $(this).width();
					$(this).css("width",parseInt(thisWidth+addWidth));
				}
			});
		}
		searchPageChangeLog();
	}
	function searchPageChangeLog(){
		var startDate = $("#countPV_start").val();
		var endDate = $("#countPV_end").val();
		$.ajax({
			type : "POST",
			url : 'business/statistics/pvStatisticsList',
			data : {shopId:getSelectedShopId(),startDate:startDate,endDate:endDate},
			success : function(data) {
				var recHtml = "";
				if(data!= null){
					var titleLi = $("#countPVList .ft>ul>li");
					for(var p in data){
						recHtml += '<ul> <li style="width: '+titleLi.first().width()+'px;">'+p+'</li>';
						var value = data[p];
						for(var i=0;i<value.length;i++){
							recHtml +='<li style="width:'+titleLi.eq(i+1).width()+'px;">'+value[i]+'</li>';
						}
						recHtml +='<li style="width:0px;float: none;"></li>';
						recHtml +='</ul>';
					}
				}else{
					recHtml = '<div><font color="red">没有记录！</font></div>';
				}
				$("#countPVList .data").html(recHtml); 
				$("#countPVList .data ul").css("width",$("#countPVList .ft>ul").first().width());
				generPager();
			}
		});
	}
	function generPager(){
		$("#countPVList .holder").jPages({
			containerID:"list_data_task_flow",
			first:'首页',
			last:'尾页',
			previous:'上页',
			next:'下页',
			perPage : 10,
			keyBrowse : true
		});
		$("#list_data_task_flow").append('<div class="cl"></div>');
	}
</script>