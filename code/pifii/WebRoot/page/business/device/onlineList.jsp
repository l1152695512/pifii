<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#online_list .ft{
		width: 100%;
/* 		border-top:2px solid #dae0e7; */
/* 		border-bottom:2px solid #c0c4d1; */
/* 		margin-bottom: 65px; */
	}
	#online_list .ft >ul li{
		float:left;
		height: 43px;
		border-bottom: 2px solid #c0c4d1;
		line-height: 43px;
		text-align: center;
		font-weight: bold;
		font-size: 16px;
	}
	#online_list .ft .data ul li{
		float: left;
		width: 192px;
		height: 48px;
		border-bottom: 1px solid #dee2ed;
		text-align: center;
		line-height: 48px;
	}
</style>

<div id="online_list">
	<!-- 这里可以放搜索框 以后放 -->
	<div class="ft">
		<ul>
			<li style="width: 50px;">序号</li>
			<li style="width: 200px;">名称</li>
			<li style="width: 150px;">mac</li>
			<li style="width: 150px;">ip</li>
			<li style="width: 100px;">连接时长</li>
<!-- 			<li style="width: 0px;"></li> -->
		</ul>
		<div class="cl"></div>
		<div class="data" id="online_list_data"></div>
	</div>
	<div class="holder"></div>
	<input type="hidden" name="routersn" value="<%=request.getParameter("routersn") %>"/>
	<input type="hidden" name="limit" value="<%=request.getParameter("limit") %>"/>
</div>
<script type="text/javascript">
	$(function() {
		var sn = $("#online_list input[name=routersn]").attr("value");
		var limit = $("#online_list input[name=limit]").attr("value");
		$.ajax({
			type : "POST",
			url : 'business/device/getOnlinePerson',
			data : {routersn:sn,limit:limit},
			success : function(data) {
				var recHtml = "";
				if(data.length > 0){
					var list_header = $("#online_list .ft>ul>li");
					for(var i=0;i<data.length;i++){
						recHtml += '<ul>'+
										'<li style="width: '+list_header.eq(0).width()+'px;">'+(i+1)+'</li>'+
										'<li style="width: '+list_header.eq(1).width()+'px;">'+data[i].host+'</li>'+
										'<li style="width: '+list_header.eq(2).width()+'px;">'+data[i].mac+'</li>'+
										'<li style="width: '+list_header.eq(3).width()+'px;">'+data[i].ip+'</li>'+
										'<li style="width: '+list_header.eq(4).width()+'px;">'+data[i].time_length+'</li>'+
										'<li style="width: 0px;float: none;"></li>'+
									'</ul>';
					}
				}else{
					recHtml = '<div><font color="red">没有记录！</font></div>';
				}
				$("#online_list .data").append(recHtml);
				generPager();
			}
		});
	});
	function generPager(){
		$("#online_list .holder").jPages({
			containerID:"online_list_data",
			first:'首页',
			last:'尾页',
			previous:'上页',
			next:'下页',
			perPage : 5,
			keyBrowse : true
		});
	}
</script>