<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#countPF{
		width: 970px;
		color: gray;
/* 		font-size: 14px; */
/* 		color: #4a4e53; */
/* 		font-family: "宋体"; */
	}
	#countPF .search{
		border-bottom: 1px solid #C0C4D1;
		padding-bottom: 30px;
		margin-top:20px;
	}
	#countPF .search>div{
		margin-bottom:10px;
		height:35px;
		margin-left:100px;
	}
	#countPF .search>div.data_search{
		float:left;
	}
	#countPF .search>div>span{
		float: left;
/* 		width:150px; */
		font-size:15px;
		font-weight:bold;
		text-align:right;
		display:inline-block;
		height:25px;
		line-height:30px;
	}
	#countPF .search>div>input[type="text"]{
		width:150px;
		height:25px;
		line-height:25px;
	}
	#countPF .search input[type="button"] {
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
	#countPF .ft{
		width: 100%;
		border-top:2px solid #dae0e7;
		border-bottom:2px solid #dae0e7;
		margin-bottom: 65px;
	}
	#countPF .hd .form-control{
		 display:inline !important;
	}
</style>

<div id="countPF">
<!-- 	<div class="hd"> -->
<!-- 		<div>日期：&nbsp; -->
<%-- 		<input type="text" id="pf_firstDay"  class="Wdate" value="${firstDay}"  onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'pf_nowDay\')}'})" />&nbsp; --%>
<!-- 		至 -->
<%-- 		<input type="text" id="pf_nowDay"   class="Wdate end_date" value="${nowDay}"  onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'pf_firstDay\')}',maxDate:'%y-%M-%d'})" />&nbsp;&nbsp;&nbsp;&nbsp; --%>
<!-- 			<input type="button"  onclick="getPFCharts()" value="查询"/> -->
		
<!-- 		</div> -->
	
<!-- 	</div> -->
	<div class="search">
		<div class="data_search">
			<span style="width:100px;">日期：</span>
			<input type="text" id="pf_firstDay"  class="Wdate" value="${firstDay}"  onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',maxDate:'#F{$dp.$D(\'pf_nowDay\')}'})" />
			&nbsp;至&nbsp;&nbsp;<input type="text" id="pf_nowDay"   class="Wdate end_date" value="${nowDay}"  onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd',minDate:'#F{$dp.$D(\'pf_firstDay\')}',maxDate:'%y-%M-%d'})" />
		</div>
		<input type="button"  onclick="getPFCharts()" value="查询"/>
	</div>
	<div class="line1"></div>
	<div >
		<div id="chartdivv"></div>
	</div>
	<div class="ft"></div>
</div>
<script type="text/javascript">
		$(document).ready(function() {
			$("#pf_nowDay").val((new Date()).Format("yyyy-MM-dd"));
			getPFCharts();
		});
		
		function getPFCharts(){
			var myChart = new FusionCharts('file/charts/MSColumn3DLineDY.swf', 'pf_chart_'+generRandomCharacters(10), "100%", 410);
			var firstDay = $("#pf_firstDay").val();
			var nowDay = $("#pf_nowDay").val();
			if(firstDay == ""){
				myAlert("日期不能为空");
			}else{
				$.ajax({
		    		type: "POST",
		    		dataType: 'text',
		    		data:{shopId:getSelectedShopId(),firstDay:firstDay,endDay:nowDay},
		    		url: "business/statistics/getPfStatistics",
		    		success: function(data,status,xhr){
		    			if(status == "success"){
		    				myChart.setDataXML(data);
							myChart.render("chartdivv");
		    			}
		    		}
		    	});
			}
		};
</script>