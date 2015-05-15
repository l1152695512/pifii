<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#certifiedList{
		color: gray;
	}
	#certifiedList .search{
		border-bottom: 1px solid #C0C4D1;
		padding-bottom: 30px;
		margin-top:20px;
	}
	#certifiedList .search>div{
		margin-bottom:10px;
		height:35px;
		margin-left:100px;
	}
	#certifiedList .search>div.data_search{
		float:left;
	}
	#certifiedList .search>div>span{
		float: left;
/* 		width:150px; */
		font-size:15px;
		font-weight:bold;
		text-align:right;
		display:inline-block;
		height:25px;
		line-height:30px;
	}
	#certifiedList .search>div>input[type="text"]{
		width:150px;
		height:25px;
		line-height:25px;
	}
	#certifiedList .search input[type="button"] {
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
	#certifiedList .ft{
/* 		width: 900px; */
		overflow: auto;
	}
	#certifiedList .ft >ul li{
		float:left;
		height: 43px;
		line-height: 43px;
		text-align: center;
		font-weight: bold;
		font-size: 16px;
	}
	#certifiedList .ft .data ul li{
		float: left;
		width: 192px;
		height: 48px;
		border-top: 1px solid #dee2ed;
		text-align: center;
		line-height: 48px;
		overflow: hidden;
	}
</style>

<div id="certifiedList">
<!-- 	<div class="search"> -->
<!-- 		<div class="date_search"> -->
<!-- 			<span style="width:100px;">日期：</span> -->
<!-- 			<input type="text" id="certified_start" class="Wdate" onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" /> -->
<!-- 			&nbsp;至&nbsp;&nbsp;<input type="text" id="certified_end" class="Wdate" onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'certified_start\')}',maxDate:'%y-%M-%d'})" />  -->
<!-- 		</div> -->
<!-- 		<input type="button" onclick="searchPageChangeLog();" value="查询"/> -->
<!-- 	</div> -->
	
	<div class="search">
		<div class="data_search">
			<span style="width:100px;">日期：</span>
			<input type="text" id="certified_start" class="Wdate" onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d'})" />
			&nbsp;至&nbsp;&nbsp;<input type="text" id="certified_end" class="Wdate" onClick="WdatePicker({skin:'whyGreen',dateFmt:'yyyy-MM-dd HH:mm:ss',minDate:'#F{$dp.$D(\'certified_start\')}',maxDate:'%y-%M-%d'})" /> 
		</div>
		<input type="button" onclick="generPageCertifiedPager();" value="查询"/>
	</div>
	<div class="ft">
		<ul>
			<li style="width: 50px;">序号</li>
			<li style="width: 70px;">认证类型</li>
			<li style="width: 150px;">用户</li>
			<li style="width: 150px;">归属地</li>
			<li style="width: 150px;">卡类型</li>
			<li style="width: 150px;">MAC地址</li>
			<li style="width: 100px;">终端信息</li>
			<li style="width: 150px;">认证时间</li>
		</ul>
		<div class="cl"></div>
		<div class="data" id="list_data_certified"></div>
	</div>
	<div class="holder"></div>
</div>
<script type="text/javascript">
	$(function() {
		generPageCertifiedPager();
	});
	function generPageCertifiedPager(){
		var startDate = $("#certified_start").val();
		var endDate = $("#certified_end").val();
		var data={shopId:getSelectedShopId(),startDate:startDate,endDate:endDate};
		$("#certifiedList .holder").jPages({
			containerID:"list_data_certified",
			first:'首页',
			last:'尾页',
			previous:'上页',
			next:'下页',
			perPage : 10,
			startPage:1,
			keyBrowse : true,
			realPagination:true,
	        serverParams:{
	        	url:"business/statistics/getCertifiedInfo",
	        	data:data,
	        	generDataHtml:generPageCertifiedData
	        }
		});
	}
	function generPageCertifiedData(data,searchParams){
		var recHtml = "";
		if(data.length > 0){
			var list_header = $("#certifiedList .ft>ul>li");
			for(var i=0;i<data.length;i++){
				recHtml += '<ul>'+
								'<li style="width: '+list_header.eq(0).width()+'px;">'+(searchParams.startRowNum+i)+'</li>'+
								'<li style="width: '+list_header.eq(1).width()+'px;">'+data[i].auth_type+'</li>'+
								'<li style="width: '+list_header.eq(2).width()+'px;">'+data[i].info+'</li>'+
								'<li style="width: '+list_header.eq(3).width()+'px;">'+data[i].address+'</li>'+
								'<li style="width: '+list_header.eq(4).width()+'px;">'+data[i].cardtype+'</li>'+
								'<li style="width: '+list_header.eq(5).width()+'px;">'+data[i].mac+'</li>'+
								'<li style="width: '+list_header.eq(6).width()+'px;">'+data[i].ftutype+'</li>'+
								'<li style="width: '+list_header.eq(7).width()+'px;">'+data[i].auth_date+'</li>'+
								'<li style="width:0px;float: none;"></li>'
							'</ul>';
			}
		}else{
			recHtml = '<div><font color="red">没有记录！</font></div>';
		}
		$("#certifiedList .data").html(recHtml);
	}
</script>