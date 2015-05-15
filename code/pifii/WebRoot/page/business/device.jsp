<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
	#device_status_list .title{
		margin-top: 30px;
		margin-bottom: 20px;
		border-bottom: 1px solid #d4d8e2;
	}
	#device_status_list h2{
		height:50px;
		font-size: 30px;
		font-family: "微软雅黑",Helvetica,Arial,sans-serif;
		font-weight: 500;
		line-height: 1.1;
		color: rgb(139, 139, 139);
	}
	#device_status_list ul{
		width: 970px;
		overflow: hidden;
	}
	#device_status_list li{
		float:left;
		margin-right:31px;
		margin-bottom: 15px;
		width: 200px;
		height: 200px;
		overflow: hidden;
	}
	#device_status_list li.four_children{
		margin-right:0;
	}
	
	#device_status_list li .dianMing{
		width: 100%;
/* 		height:106px; */
		background: #2079d4;
		border-top-left-radius: 4px;
		border-top-right-radius: 4px;
		color: white;
	}
	#device_status_list li .dianMing .router_title{
		overflow: hidden;
		height: 46px;
	}
	#device_status_list li .dianMing .router_title>img{
		margin: 10px 5px;
		width: 27px;
	}
	#device_status_list li .dianMing .router_title a{
		float: right;
		position: relative;
		padding: 10px;
		top: -40px;
	}
	#device_status_list li .dianMing .router_title .router_name{
		float: left;
		width: 65%;
		line-height: 45px;
		overflow: hidden;
		white-space: nowrap;
	}
	#device_status_list li .dianMing .router_sn{
		text-indent: 10px;
		border-top: 1px dashed #94c7ff;
	}
	#device_status_list li .dianMing .router_name_mark{
		text-indent: 10px;
		border-top: 1px dashed #94c7ff;
	}
	#device_status_list li .dianMing .router_name_mark a{
		float: right;
		padding-right: 10px;
		padding-top: 5px;
	}
	#device_status_list li .dianMingOut{
		background: #70adec;
	}
	#device_status_list li .dianMing img{
		float:left;
	}
	#device_status_list li .dianMing .action{
		height:40px;
		line-height: 40px;
		font-size: 16px;
		text-align: center;
		font-weight: normal;
		border-top:1px dashed #94c7ff;
		padding-left: 50px;
	}
	#device_status_list li .dianMing .action a img{
		margin: 12px 10px;
	}
	#device_status_list li .linkNumb{
	/* 	width: 198px; */
		color: #2079d4;
		height: 42px;
		line-height: 42px;
		text-indent: 2em;
		border-left:1px solid #2079d4;
		border-right:1px solid #2079d4;
		border-top:2px solid #2079d4;
		border-bottom:4px solid #2079d4;
		border-radius: 5px;
		border-top-left-radius: 0;
		border-top-right-radius: 0;
		background:url(images/business/person.png) no-repeat 5px center;
		position: relative;
	}
	#device_status_list li .linkNumbOut{
		color: #7090b3 !important;
		border-left:1px solid #70adec !important;
		border-right:1px solid #70adec !important;
		border-top:2px solid #70adec !important;
		border-bottom:4px solid #70adec !important;
		background:url(images/business/personOut.png) no-repeat 5px center !important;
	}
	#device_status_list li .linkNumb span.online_type{
		position: absolute;
		right: 3px;
	}
	#device_status_list .online_person{
		color:#2079d4;
	}
	#device_status_list .outline_person{
		color:#70adec;
	}
/* 	#device_status_list .online_person:hover{ */
/* 		text-decoration: underline; */
/* 	} */
</style>

<div id="device_status_list">
	<div class="title"><h2>设备信息</h2></div>
		<div id="device_group" class="fl"><input type="checkbox" id="device_group_ck" value="" onclick="" />&nbsp;&nbsp;开启群组</div>
		<div class="cl"></div>
<!-- 	<div class="smallTitle"> -->
<!-- 		<p class="fl">小肥羊石牌店</p> -->
<!-- 		<!--<div class="fr">更新时间:<span>2014-01-24&nbsp;13:13:13</span></div></div>非首次登陆状态显示路由器  更新时间 -->
<!-- 		<div class="cl"></div> -->
<!-- 	</div> -->
	<ul>
<!-- 		<li> -->
<!-- 			<div class="dianMing"> -->
<!-- 				<h4>YINfu-wifi</h4> -->
<!-- 				<h5 class="">小肥羊1店路由器</h5> -->
<!-- 			</div> -->
<!-- 			<div class="linkNumb"> -->
<!-- 				链接人数 ：<span>15</span>人  <span>脱网</span> -->
<!-- 			</div> -->
<!-- 		</li> -->
<!-- 		<li> -->
<!-- 			<div class="dianMing"> -->
<!-- 				<h4>YINfu-wifi</h4> -->
<!-- 				<h5 class="">小肥羊2店路由器</h5> -->
<!-- 			</div> -->
<!-- 			<div class="linkNumb"> -->
<!-- 				链接人数 ：<span>15</span>人  <span>脱网</span> -->
<!-- 			</div> -->
<!-- 		</li> -->
	</ul>
	<div class="cl"></div>
</div>
<script type="text/javascript">
	var refreshInterval = 10000;//5秒一刷新
	var deviceStatusTimeOut;
	$(function(){
		 $.ajax({
			type: "POST",
			url: "business/shop/getShopInfo",
			data:{shopId:getSelectedShopId()},
			success : function(data) {
				$("#device_group_ck").attr("checked",data.gstatus=='1'?true:false);
			}
		});
		refreshStatus(true);
		$("#device_group_ck").click(function(){
			 if ($(this).is(':checked') ==true) {//开启
				 $.ajax({
					 	type: "POST",
						url: "business/shop/changeGroupStatus",
						data:{shopId:getSelectedShopId()},
						success: function(data){
							if(eval(data).state=='error'){
								myAlert("开启失败！");
							}else{
								myAlert("开启成功！");
							}
						}
					});
				 
			 }else{//取消
				 $.ajax({
						type: "POST",
						url: "business/shop/changeGroupStatus",
						data:{shopId:getSelectedShopId()},
						success : function(data) {
							if(eval(data).state=='error'){
								myAlert("取消失败！");
							}else{
								myAlert("取消成功！");
							}
						}
					});
			 }
		});
	});
	function refreshStatus(isFirst){
		if(undefined != $('#device_status_list').html()){//用户停留在该页面
			var index = $.fn._maxZIndexOptionIndex();
			if(index == -1 || isFirst){//无弹出页面时，才会定时刷新
				var global = false;
				if(isFirst){//第一次加载会出现加载提示
					global = true;
				}
				$.ajax({
					type: "POST",
					global: global,
					url: "business/device/getShopDeviceWithStatus",
					data:{shopId:getSelectedShopId()},
					success: function(data,status,xhr){
						if(isLoginPage(data)){//当global为false时，不会检查是否登录超时，所以这里需要自己去检查授权失效
							//弹出授权框
							myAlert("登录过时，请重新登录！",function(e){
								var url=encodeURI(encodeURI(cxt + "/loginView"));
								window.location.href=url;
							});
						}else{
							if(data.length > 0){
								var dataHtml = "";
								for(var i=0;i<data.length;i++){
									var cls = "";
									if(i%4 == 3){
										cls='class="four_children"';
									}
									dataHtml += '<li '+cls+' data-sn="'+data[i].router_sn+'" data-online-num="'+data[i].online_num+'">';
									var online_num = data[i].online_num;
									var isOnline = true;
									if(online_num == -1){
										online_num = 0;
										isOnline = false;
									}
									if(isOnline){
										dataHtml += '<div class="dianMing">'+
														'<div class="router_title">'+
														'<img src="images/business/wifi.png">';
									}else{
										dataHtml += '<div class="dianMing dianMingOut">'+
														'<div class="router_title">'+
														'<img src="images/business/outwifi.png">';
									}
									dataHtml += '<div class="router_name"><span>'+data[i].name+'</span></div>'+
												'<a href="javascript:void(0)" title="修改名称">'+
													'<img src="images/business/edit_icon_white.png">'+
												'</a>'+
											'</div>'+
											'<div class="router_sn">&nbsp;&nbsp;SN：'+data[i].router_sn+'</div>'+
											'<div class="router_name_mark">'+
												'<span>昵称：'+data[i].marker+'</span>'+
												'<a href="javascript:void(0)" title="修改昵称">'+
													'<img src="images/business/edit_icon_white.png">'+
												'</a>'+
											'</div>'+
											'<div class="action">'+
												'<a href="javascript:void(0)" title="在线用户">'+
													'<img class="linkedPerson" src="images/business/device/online_person_list.png">'+
												'</a>'+
												'<a href="javascript:void(0)" title="重启">'+
													'<img class="restart" src="images/business/device/restart.png">'+
												'</a>'+
// 												'<a href="javascript:void(0)" title="文件列表">'+
// 													'<img class="fileList" src="images/business/device/router_file_list.png">'+
// 												'</a>'+
												'<a href="javascript:void(0)" title="重置盒子文件">'+
													'<img class="reloadDataFile" src="images/business/device/router_file_list.png">'+
												'</a>'+
											'</div>'+
										'</div>';
									if(isOnline){
										dataHtml += '<div class="linkNumb">';
									}else{
										dataHtml += '<div class="linkNumb linkNumbOut">';
									}
									if(isOnline){
										dataHtml += '<span class="online_person">人数 ：<span>'+online_num+'</span>人</span>';
										dataHtml += '<span class="online_person online_type">'+data[i].online_type+'</span>';
									}else{
										dataHtml += '<span class="outline_person">人数 ：<span>'+online_num+'</span>人</span>';
										dataHtml += '<span class="outline_person online_type">脱网</span>';
									}
									dataHtml += '</div></li>';
								}
								$("#device_status_list").find("ul").eq(0).html(dataHtml);
							}else{
								$("#device_status_list").find("ul").eq(0).html('<span>你还没有设备！</span>');
							}
							initEvent();
							var parentDiv = $("#device_status_list ul .router_name");
							$("#device_status_list ul .router_name").each(function(){
								scrollMe($(this).find("span"),$(this).width());
							});
							if(undefined != deviceStatusTimeOut){//避免多次点击”我的设备“菜单造成定时器执行多次
								clearTimeout(deviceStatusTimeOut);
							}
							deviceStatusTimeOut = setTimeout('refreshStatus()',refreshInterval);
						}
//	 				},
//	 				complete:function(){
					}
				});
			}else{
				deviceStatusTimeOut = setTimeout('refreshStatus()',refreshInterval);
			}
		}
	}
	function initEvent(){
		$("#device_status_list li .action .linkedPerson").click(function(){
			var onlineNum = $(this).parent().parent().parent().parent().data("onlineNum");
			if(onlineNum != '-1'){
				if(onlineNum <= 0){
					myAlert("该路由器当前无连接用户！");
				}else{
					var routersn = $(this).parent().parent().parent().parent().data("sn");
					$.fn.SimpleModal({
// 						model: 'modal-ajax',
						title: '设备连接详情',
						width: 710,
// 						hideFooter: true,
						param: {
							url: 'page/business/device/onlineList.jsp',
							data: {routersn:routersn,limit:onlineNum}
						}
					}).showModal();
				}
			}else{
				myAlert("该路由器处于离线状态，不能查看连接详情！");
			}
		});
		
		$("#device_status_list li .action .restart").click(function(){
			var onlineNum = $(this).parent().parent().parent().parent().data("onlineNum");
			if(onlineNum != '-1'){
				var routersn = $(this).parent().parent().parent().parent().data("sn");
				myConfirm("确定要重启盒子？",function(){
					$.ajax({
						type : "POST",
						url : 'business/device/restart',
						data : {sn:routersn},
						success : function(resp) {
							if(resp.msg){
					    		myAlert(resp.msg);
					    	}else{
// 					    		closePop();
								refreshStatus(true);
					    	}
						}
					});
				});
			}else{
				myAlert("该路由器处于离线状态，不能查看连接详情！");
			}
		});
		
		$("#device_status_list li .action .fileList").click(function(){
			var onlineNum = $(this).parent().parent().parent().parent().data("onlineNum");
			if(onlineNum != '-1'){
				var routersn = $(this).parent().parent().parent().parent().data("sn");
				$.fn.SimpleModal({
					title: '设备本地文件信息',
					width: 610,
					param: {
						url: 'business/device/listFilePage',
						data: {sn:routersn}
					}
				}).showModal();
			}else{
				myAlert("该路由器处于离线状态，不能查看连接详情！");
			}
		});
		
		$("#device_status_list li .action .reloadDataFile").click(function(){
			var routersn = $(this).parent().parent().parent().parent().data("sn");
			myConfirm("确定要重置盒子文件（仅新ROM的盒子有效）？",function(){
				$.ajax({
					type : "POST",
					url : 'business/device/reloadDataFile',
					data : {sn:routersn,shopId:getSelectedShopId()},
					success : function(resp) {
						myAlert("任务已成功下发。");
// 						if(resp.msg){
// 				    		myAlert(resp.msg);
// 				    	}else{
// 				    		closePop();
// 							refreshStatus(true);
// 				    	}
					}
				});
			});
		});
		
// 		$("#device_status_list li").click(function(){
// 			var onlineNum = $(this).data("onlineNum");
// 			if(onlineNum != '-1'){
// 				if(onlineNum <= 0){
// 					myAlert("该路由器当前无连接用户！");
// 				}else{
// 					var routersn = $(this).data("sn");
// 					$.fn.SimpleModal({
// // 						model: 'modal-ajax',
// 						title: '设备连接详情',
// 						width: 710,
// // 						hideFooter: true,
// 						param: {
// 							url: 'page/business/device/onlineList.jsp',
// 							data: {routersn:routersn,limit:onlineNum}
// 						}
// 					}).showModal();
// 				}
// 			}else{
// 				myAlert("该路由器处于离线状态，不能查看连接详情！");
// 			}
// 		});
		$("#device_status_list li .dianMing .router_title a").click(function(){
			var onlineNum = $(this).parent().parent().parent().data("onlineNum");
			if(onlineNum != '-1'){
				var sn = $(this).parent().parent().parent().data("sn");
				modifyRouterName(sn);
			}else{
				myAlert("该路由器处于离线状态，暂时不能修改名称！");
			}
			return false;
		});
		$("#device_status_list li .dianMing .router_name_mark a").click(function(){
			var sn = $(this).parent().parent().parent().data("sn");
			modifyRouterMarker(sn);
			return false;
		});
	}
	function modifyRouterName(sn){
		$.fn.SimpleModal({
			title: "盒子名称修改",
			width: 400,
			param: {
				url: 'business/device/modifyName',
				data: {sn:sn}
			},
			buttons: [{
	    		text:'确定',
	    		classe:'btn primary btn-margin',
	    		clickEvent:function() {
	    			saveRouterName();
	            }
	    	},{
	    		text:'取消',
	    		classe:'btn secondary'
	    	}]
		}).showModal();
	}
	function modifyRouterMarker(sn){
		$.fn.SimpleModal({
			title: "盒子昵称修改",
			width: 350,
			param: {
				url: 'business/device/modifyMarker',
				data: {sn:sn}
			},
			buttons: [{
	    		text:'确定',
	    		classe:'btn primary btn-margin',
	    		clickEvent:function() {
	    			saveRouterMarker();
	            }
	    	},{
	    		text:'取消',
	    		classe:'btn secondary'
	    	}]
		}).showModal();
	}
</script>
		