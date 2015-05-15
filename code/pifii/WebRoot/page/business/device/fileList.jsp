<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#file_list .ft{
		width: 100%;
		-moz-user-select:none;
/* 		border-top:2px solid #dae0e7; */
/* 		border-bottom:2px solid #c0c4d1; */
/* 		margin-bottom: 65px; */
	}
	#file_list .absolutePath{
		font-size: 18px;
		border-bottom: 2px solid #c0c4d1;
		padding-bottom: 15px;
	}
	
	#file_list .absolutePath>div{
		float:left;
		margin:0 5px;
	}
	#file_list .absolutePath>span{
		float:left;
		margin:0 5px;
	}
	#file_list .absolutePath>div{
		cursor: pointer;
	}
	#file_list .ft >ul li{
		float:left;
		height: 43px;
		border-bottom: 2px solid #c0c4d1;
		line-height: 43px;
		text-align: center;
		font-weight: bold;
		font-size: 16px;
	}
	#file_list .ft .data ul:hover{
		background:rgb(218, 235, 247);
	}
	#file_list .ft .data ul li{
		float: left;
		width: 192px;
		height: 48px;
		border-bottom: 1px solid #dee2ed;
		text-align: center;
		line-height: 48px;
	}
</style>

<div id="file_list" onselectstart="return false">
	<!-- 这里可以放搜索框 以后放 -->
	<div class="absolutePath">
<!-- 		<div>aa</div> -->
<!-- 		<span>></span> -->
<!-- 		<div>bb</div> -->
		<div class="cl" style="float: none;"></div>
	</div>
	<div class="ft">
<!-- 		<ul> -->
<!-- 			<li style="width: 70px;"></li> -->
<!-- 			<li style="width: 210px;text-align: left;">名称</li> -->
<!-- 			<li style="width: 100px;">大小</li> -->
<!-- 			<li style="width: 200px;">修改时间</li> -->
<!-- 		</ul> -->
<!-- 		<div class="cl"></div> -->
		<div class="data" id="file_list_data"></div>
	</div>
	<div class="holder"></div>
</div>
<script type="text/javascript">
	$(function() {
		getFloderContent("/",true);
	});
	function getFloderContent(folder,closeParent){
		$.ajax({
			type : "POST",
			url : 'business/device/listFile',
			data : {sn:"${sn}",path:folder},
			success : function(data) {
				if(undefined != data.msg){
					if(closeParent){
						myAlertClosePop(data.msg);
					}else{
						myAlert(data.msg);
					}
				}else{
					generAbsolutePath(folder);
					var recHtml = "";
					if(data.length > 0){
						var list_header = $("#file_list .ft>ul>li");
						for(var i=0;i<data.length;i++){
							recHtml += '<ul data-path="'+data[i].path+'">'+
											'<li style="width: 70px;">'+//'+list_header.eq(0).width()+'
												'<img src="'+data[i].icon+'">'+
											'</li>'+
											'<li style="text-align: left;width: 210px;">'+data[i].name+'</li>'+
											'<li style="width: 100px;">'+data[i].size+'</li>'+
											'<li style="width: 200px;">'+data[i].modified+'</li>'+
											'<li style="width: 0px;float: none;"></li>'+
										'</ul>';
						}
					}else{
						recHtml = '<div><font color="red">没有文件！</font></div>';
					}
					$("#file_list .data").html(recHtml);
					initEvent();
					generPager();
				}
			}
		});
	}
	function initEvent(){
		$("#file_list .ft .data ul").dblclick(function(){
			getFloderContent($(this).data("path"),false);
			return false;
		});
	}
	function generAbsolutePath(path){
		var paths = path.split("/");
		var pathsHtml = "<div data-path='/'>/</div>";
		var basePath = "";
		for(var i=0;i<paths.length;i++){
			if("" != paths[i]){
				basePath += "/"+paths[i];
				pathsHtml += "<span>></span><div data-path='"+basePath+"'>"+paths[i]+"</div>";
			}
		}
		pathsHtml += '<div class="cl" style="float: none;"></div>';
		$("#file_list .absolutePath").html(pathsHtml);
		$("#file_list .absolutePath>div").click(function(){
			getFloderContent($(this).data("path"),false);
		});
	}
	function generPager(){
		$("#file_list .holder").jPages({
			containerID:"file_list_data",
			first:'首页',
			last:'尾页',
			previous:'上页',
			next:'下页',
			perPage : 5,
			keyBrowse : true
		});
	}
</script>