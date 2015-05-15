<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#main_leftmenu{
		width: 220px;
		float: left;
		margin-right: 20px;
	}
	#main_leftmenu nav{
		width: 220px;
		background: #e3e8ee;
	}
	#main_leftmenu nav>ul>li>div{
		width: 175px;
		height: 55px;
		line-height: 55px;
		cursor: pointer;
		color: #69788b;
		font-size: 16px;
		overflow: hidden;
		text-indent: 62px;
		position: relative;
		text-indent: 88px;
		white-space: nowrap;
	}
	#main_leftmenu nav>ul>li>ul{
/* 		position: relative; */
		display: none;
	}
	#main_leftmenu nav>ul>li>ul>li{
		background: #d5dde6;
		color:#69788b;
		font-size: 14px;
		text-align: center;
		border-bottom: 1px solid #c8d0d9;
		height: 45px;
		line-height: 45px;
		cursor: pointer;
	}
	#main_leftmenu nav>ul>li.main_page div{
		background:#e3e8ee url(images/business/nav.png) 20px -55px no-repeat;
	}
	#main_leftmenu nav>ul>li.my_device div{
		background:#e3e8ee url(images/business/nav.png) 20px -185px no-repeat;
	}
	#main_leftmenu nav>ul>li.operation_analysis div{
		background:#e3e8ee url(images/business/nav.png) 20px -732px no-repeat;
	}
	#main_leftmenu nav>ul>li.business_operation div{
		background:#e3e8ee url(images/business/nav.png) 20px -993px no-repeat;
	}
	#main_leftmenu nav>ul>li.task_tracking div{
		background:#e3e8ee url(images/business/nav.png) 20px -1064px no-repeat;
	}
	#main_leftmenu nav>ul>li.app_shop div{
		background:#e3e8ee url(images/business/nav.png) 20px -1130px no-repeat;
	}
	#main_leftmenu nav>ul>li.my_setting div{
		background:#e3e8ee url(images/business/nav.png) 20px -875px no-repeat;
	}
	#main_leftmenu nav>ul>li.help>div{
		background:#e3e8ee url(images/business/nav.png) 20px -1199px no-repeat;
	}
	#main_leftmenu nav>ul>li.help li a{
		display:block;
		color:#69788b;
	}
	
	#main_leftmenu .bigWrapNavCur{
		width: 217px !important;
		border-left:3px solid #14a1f9;
		color: #183356 !important;
	}
	#main_leftmenu .bigWrapNavCurSec{
		background: #ABC6E4 !important;
		color: #FEFEFE !important; 
	}
</style>

<div id="main_leftmenu">
	<nav class="fl">
		<ul>
			<li class="main_page"><div data-click-url="/userContent">首页</div></li>
			<li class="my_device"><div data-click-url="/business/device/toIndex">我的设备</div></li>
			<li class="operation_analysis">
				<div >运营分析</div>
				<ul>
					<li data-click-url="/business/statistics/toIndex">广告</li>
					<li data-click-url="/business/statistics/pvStatisticsIndex">应用</li>
					<li data-click-url="/business/statistics/pfStatisticsIndex">客流</li>
					<li data-click-url="/business/statistics/certifiedIndex">客户管理</li>
					<li data-click-url="/business/statistics/smsStatistics">短信统计</li>
<!-- 					<li data-click-url="/business/statistics/logIndex">客户行为轨迹</li> -->
				</ul>
			</li>
			<li class="business_operation">
				<div >业务运营</div>
				<ul>
					<li data-click-url="/business/food">餐饮通</li>
					<li data-click-url="/business/queue">排队</li>
					<li data-click-url="/business/survey">调查问卷</li>
					<li data-click-url="/business/feedback">意见反馈</li>
					<li data-click-url="/business/weixin">微信认证</li>
				</ul>
			</li>
			<li class="task_tracking"><div data-click-url="/page/business/taskflow/taskFlowIndex.jsp">任务跟踪</div></li>
			<li class="task_tracking"><div data-click-url="/page/business/taskflow/taskFlowIndex_new.jsp">同步进度</div></li>
			<li class="app_shop"><div data-click-url="/page/business/showApp.jsp">应用商店</div></li>
			<li class="my_setting">
				<div>我的设置</div>
				<ul>
					<li data-click-url="/business/setting">白名单</li>
					<li data-click-url="/business/setting/blacklist">黑名单</li>
				</ul>
			</li>
			<li class="help">
				<div>帮助中心</div>
				<ul>
					<li><a href="page/help/help.html" target="_blank"><div>平台介绍</div></a></li>
					<li><a href="file/help/help.pdf" target="_blank"><div>帮助文档</div></a></li>
					<li><a href="file/help/merchant.mp4" target="_blank"><div>视频教程</div></a></li>
				</ul>
			</li>
		</ul>
	</nav>
</div>
<script type="text/javascript">
$(function(){
	//ajaxify menus
// 	$("#main_leftmenu>nav>ul>li>div").click(function(){
// 		$(this).next().show();
// 	});
	$("#main_leftmenu>nav>ul>li").eq(0).children("div").addClass("bigWrapNavCur");
	$("#main_leftmenu>nav>ul>li>ul>li").click(function(){
		if($(this).parent().is(":hidden")){
			$(this).parent().slideDown();
		}
		$(this).parent().prev().addClass("bigWrapNavCur").parent().siblings().children("div").removeClass("bigWrapNavCur");/*点击一级导航的一级菜单 显示样式*/
		$(this).addClass("bigWrapNavCurSec").siblings().removeClass("bigWrapNavCurSec");
		var url = $(this).attr("data-click-url");
		if(url && url != ""){
			ajaxContent(url);
		}
	});
	$("#main_leftmenu>nav>ul>li>div").click(function(){
		if($(this).next("ul").length > 0){//有子菜单
			if($(this).next("ul").is(":hidden")){
				$(this).next("ul").slideDown();
			}else{
				$(this).next("ul").slideUp();
			}
		}
		$(this).addClass("bigWrapNavCur").parent().siblings().children("div").removeClass("bigWrapNavCur");/*点击一级导航的一级菜单 显示样式*/
		$(this).parent().siblings().children("ul").children().removeClass("bigWrapNavCurSec").parent().slideUp();
		var url = $(this).attr("data-click-url");
		if(url==null){
			return ;
		}else{
			ajaxContent(url);
		}
	});
});
function accessMenu(url){
	$("#main_leftmenu li").each(function(){
		var isDiv = false;
		var clickUrl = $(this).data("clickUrl");
		if(clickUrl == undefined){
			if($(this).children("div").length > 0){
				clickUrl = $(this).children("div").eq(0).data("clickUrl");
				isDiv = true;
			}
		}
		if(clickUrl == url){
			if(isDiv){
				$(this).children("div").click();
			}else{
				$(this).click();
			}
			return false;
		}
	});
}
</script>