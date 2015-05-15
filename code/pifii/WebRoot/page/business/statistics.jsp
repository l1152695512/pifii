<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#statistics{
/* 		margin: 0 auto; */
		margin-top: 30px;
		margin-bottom: 30px;
		width: 960px;
		overflow: hidden;
	}
	#statistics li{
		float: left;
		width: 258px;
		height: 175px;
		border: 1px solid #cdd4dc;
		margin-right: 20px;
		border-radius: 3px;
		overflow: hidden;
	}
	#statistics .biaoti{
		width: 100%;
		height: 30px;
		border-bottom:1px solid #cdd4dc;
		background: #e7eaee;
	}
	#statistics .biaoti div.network_status{
		font-size: 14px;
		color: #40546e;
		line-height: 35px;
		width: 95px;
		text-indent: 5px;
		height: 100%;
	}
	#statistics .biaoti div.client_info{
		color: #8591a1;
		font-size: 12px;
		line-height: 35px;
		text-align: left;
		width: 65px;
		height: 100%;
		cursor: pointer;
	}

	#statistics .stathalfStyle {
		position: relative;
		width:183px;
		display:block;
		margin:0 auto;
		margin-top:30px;
	}
 	#stathalf2 >span.circle-text-half {
 		line-height: 109px !important;
 	}
	#statistics .stathalfStyle .circle-text-half, .circle-info-half {
	    width: 100%;
	    position: absolute;
	    text-align: center;
	    display: inline-block;
	}

	#statistics .stathalfStyle .circle-info-half {
		color: #999;
	}

	#statistics li.client_statistics>div.total_clients{
		width: 100%;
		height: 79px;
		font-size: 16px;
		color: #434d59;
		line-height: 79px;
		*line-height: 110px;
	}
	#statistics li.client_statistics>div.total_clients p{
		font-size: 40px;
		color: #37a8ea;
		width:60%;
		text-align: right;
		height: 79px;
		line-height: 79px;
	}
	#statistics li.client_statistics>div.yesterday_clients{
		width: 100%;
		height:30px;
		text-align: center;
	}
	#statistics li.client_statistics>div.yesterday_clients span{
		font-size: 14px;
		color: #37a8ea;
		line-height: 30px;
	}/*第2个li*/
	#statistics li.adv_statistics{
		width: 375px;
	}
	#statistics li.adv_statistics>div.chart_adv{
		width: 100%;
		height:20px;
		color: #434d59;
		text-align: center;
		line-height: 25px;
	}/*第3个li end*/

	.wrapper{width:218px; height:148px;}
	#statistics .network_ie_7{
		display: none;
		text-align: center;
		line-height: 124px;
		font-size: 16px;
	}
	#statistics .network_ie_7 span{
		font-size: 59px;
		color: #37a8ea;
	}
</style>

<ul id="statistics">
	<li>
		<div class="biaoti">
			<div class="fl network_status">网络状态</div>
			<div class="fr client_info" id="netSta_view">查看详情</div>
			<div class="cl"></div>
		</div>
		<div class="network_ie_7">
			当前在线<span>?</span>人
		</div>
		<div id="stathalf1" class="stathalfStyle" data-dimension="183" data-text="0" data-info="人" data-width="30" data-fontsize="38" data-percent="0" data-fgcolor="#7ea568" data-bgcolor="#eee" data-type="half" data-fill="#ddd" style="width: 160px;margin-top: 34px;"></div>
		<div id="stathalf2" class="stathalfStyle" data-dimension="183" data-text="0" data-info="人" data-width="30" data-fontsize="38" data-percent="0" data-fgcolor="#7ea568" data-bgcolor="#eee" data-type="half" data-fill="#ddd" style="width: 160px;margin-top: 34px;"></div>
	</li>
	<li class="client_statistics">
		<div class="biaoti">
			<div class="fl network_status">客流统计</div>
			<div class="fr client_info" id="flowSta_view">查看详情</div>
			<div class="cl"></div>
		</div>
		<div class="total_clients"><p class="fl"  id="idx_stat_t">0</p>人次</div>
		<div class="yesterday_clients">昨日总共连接<span id="idx_stat_yt">0</span>人次</div>
	</li>
	<li class="adv_statistics">
		<div class="biaoti">
			<div class="fl network_status">广告推广</div>
			<div class="fr client_info" id="adSta_view">查看详情</div>
			<div class="cl"></div>
		</div>
		<div class="chart_adv">
			<div id="chartdivad"></div>
		</div>
	</li>
<!-- 	<li> -->
<!-- 		<div class="biaoti"> -->
<!-- 			<div class="fl">短信推广</div> -->
<!-- 			<div class="fr" id="smsSta_view">查看详情</div> -->
<!-- 			<div class="cl"></div> -->
<!-- 		</div> -->
<!-- 		<div>已发送&nbsp;<span>600</span>&nbsp;条</div> -->
<!-- 		<div class="message"> -->
<!-- 			<div class="inner"></div> -->
<!-- 		</div> -->
<!-- 		<div>剩&nbsp;<span>780</span>&nbsp;条</div> -->
<!-- 	</li> -->
	<div class="cl"></div>
</ul>
<script>
	$( document ).ready(function() {
		try{
			$('#stathalf1').circliful();
		}catch(e){
		}
		$('#stathalf2').hide();
		$.ajax({
			type: "POST",
			global: false,
			url: "business/statistics/getOnLineTotal",
			data: {shopId:getSelectedShopId()},
			success: function(data,status,xhr){
				if("success" == status){
					$('#stathalf1').remove();
					$('#stathalf2').data("text",data.onlineNum);
					$('#stathalf2').data("percent",parseInt(data.onlineNum)/parseInt(data.onlineNum));
					try{
						$('#stathalf2').circliful();
						$('#stathalf2').show();
					}catch(e){
						$("#statistics .network_ie_7").show();
						$("#statistics .network_ie_7 span").text(data.onlineNum);
					}
				}
			}
		});
		
		$.ajax({
			type: "POST",
			url: "business/statistics/getClientTotal",
			data: {shopId:getSelectedShopId()},
			success: function(data,status,xhr){
				$('#idx_stat_yt').text(data.y_total);
				$('#idx_stat_t').text(data.total);
			}
		});
		var myChart = new FusionCharts('file/charts/MSColumnLine3D.swf', "index_page_chart_"+generRandomCharacters(10), 370, 160);
		$.ajax({
    		type: "POST",
    		global: false,
    		dataType: 'text',
    		data:{shopId:getSelectedShopId()},
    		url: "business/statistics/getAdvShow",
    		success: function(data,status,xhr){
   				myChart.setDataXML(data);
				myChart.render("chartdivad");
    		}
    	});
		$("#adSta_view").click(function(){
// 			ajaxContent("/business/statistics/toIndex");
			accessMenu("/business/statistics/toIndex");
		});
		$("#netSta_view").click(function(){
// 			ajaxContent("/business/device/toIndex");
			accessMenu("/business/device/toIndex");
		});
		$("#flowSta_view").click(function(){
			accessMenu("/business/statistics/pfStatisticsIndex");
		});
    });
</script>
