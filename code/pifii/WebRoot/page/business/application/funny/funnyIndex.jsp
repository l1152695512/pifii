<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
	#funny_show .title{width:550px;}
	#funny_show .title h3{width:90px;color: #444d5e;font-size:16px;font-weight: normal;float:left;line-height:32px;margin-top: 0px; }/*标题信息*/
	#funny_show .title .search{background:#fff; text-indent:10px;float:left;padding:0;margin:0; border-radius:4px;}
	#funny_show .title .search input{ border:1px solid #b4c0ce; color:#c6c6c6; width:150px; float:left; height:30px; line-height:30px;}
	#funny_show .title .search a{ float:left;display:inline-block; border:1px solid #b4c0ce;border-left:0;width:30px;height:30px; line-height:30px; text-align:center;overflow:hidden;}
	#funny_show .title .search a img{ width:16px;height:16px; vertical-align:middle;margin-left:-10px;}
	#funny_show .delete,.edit,.add{ float:right;display:block;width:30px;height:30px; line-height:30px; text-align:center;overflow:hidden;}
	#funny_show .delete img,.edit img,.add img{ width:16px;height:16px; vertical-align:middle;}
	
/* 	#funny_show .data_list .ft{width: 100%;} */
	#funny_show .data_list .ft>ul li{float:left;height: 43px;border-bottom: 2px solid #c0c4d1;line-height: 43px;text-align: center;font-weight: bold;font-size: 16px;overflow: hidden;}
	#funny_show .data_list .ft .data ul li{float: left;height: 80px;border-bottom: 1px solid #dee2ed;text-align: center;line-height: 80px;overflow: hidden;}
	#funny_show .data_list .ft .data ul li.radio_cls{*line-height: 0px;*height: 51px;*padding-top: 29px;}
	#funny_show .data_list .ft .data ul li img{height:80px;}
</style>
<div id="funny_show">
	<div class="title">
		<h3>搜索：</h3>
		<div class="search fl">
			<input type="text" value="" placeholder="输入关键字"/>
			<a href="javascript:void(0)">
				<img src="images/business/ordering/dc_icon01.png">
			</a>
		</div>
		<a href="javascript:void(0)" class="delete fr">
			<img src="images/business/ordering/delete_icon.png">
		</a>
		<a href="javascript:void(0)" class="edit fr">
			<img src="images/business/ordering/edit_icon.png">
		</a>
		<a href="javascript:void(0)" class="add fr">
			<img src="images/business/ordering/add_icon.png">
		</a>
		<div class="cl"></div>
	</div>
	<div class="data_list">
		<div class="ft">
			<ul>
				<li style="width: 20px;"></li>
				<li style="width: 30px;"></li>
				<li style="width: 150px;">标题</li>
				<li style="width: 150px;">图片</li>
				<li style="width: 200px;">内容</li>
			</ul>
			<div class="cl"></div>
			<div class="data" id="funny_show_list_data"></div>
		</div>
		<div class="holder"></div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		initFlowPackEvent();
		getFlowPackList();
	});
	function getFlowPackList(){
		var searchText = $("#funny_show .title .search input").first().val();
		$.ajax({
			type : "POST",
			url : "business/app/funny/listData",
			data : {searchText:searchText,shopId:getSelectedShopId()},
			success : function(data) {
				var recHtml = "";
				if(data.length > 0){
					var list_header = $("#funny_show .ft>ul>li");
					for(var i=0;i<data.length;i++){
						if("" == data[i].img){//兼容火狐，火狐浏览器中如果src为空字符串时，是不会执行onerror的
							data[i].img = "aa.jpg";
						}
						recHtml += '<ul>'+
											'<li style="width: '+list_header.eq(0).width()+'px;">'+(i+1)+'</li>'+
											'<li style="width: '+list_header.eq(1).width()+'px;" class="radio_cls"><input type="radio" name="radio_funnyList" value="'+data[i].id+'" /></li>'+
											'<li style="width: '+list_header.eq(2).width()+'px;overflow: hidden;"><span title="'+data[i].title+'">'+data[i].title+'</span></li>'+
											'<li style="width: '+list_header.eq(3).width()+'px;overflow: hidden;"><img src="'+data[i].img+'" onerror="this.src=\'images/business/ad-1.jpg\'" /></li>'+
											'<li style="width: '+list_header.eq(4).width()+'px; overflow: hidden;"><span title="'+data[i].txt+'">'+data[i].txt+'</span></li>'+
											'<li style="width: 0px;float: none;"></li>'+
									'</ul>';
					}
				}else{
					recHtml = '<div><font color="red">没有记录！</font></div>';
				}
				$("#funny_show .data_list .data").html(recHtml);
				generFlowPackPager();
			}
		});
	}
	function generFlowPackPager(){
		$("#funny_show_list_data").css("min-height","0px");
		$("#funny_show .data_list .holder").jPages({
			containerID: "funny_show_list_data",
			first:'首页',
			last:'尾页',
			previous:'上页',
			next :'下页',
			perPage : 5,
			keyBrowse : true
		});
	}
	function initFlowPackEvent(){
		$("#funny_show .title .add,.edit").click(function(e){
			var title = "";
			var params = {};
			if($(this).hasClass("add")){
				title = "添加数据";
				params = {};
			}else if($(this).hasClass("edit")){
				title = "修改数据";
				var id = getSelectRecordId();
				if(id != ''){
					params = {id:id};
				}else{
					return;
				}
			}
			$.fn.SimpleModal({
// 				model : 'modal-ajax',
				title : title,
				width : 550,
// 				hideFooter : true,
				param : {
					url : 'business/app/funny/update',
					data : params
				}
			}).showModal();
		});
		$("#funny_show .title .delete").click(function(e){
			var id = getSelectRecordId();
			if(id != ''){
				var params = {id:id};
				myConfirm("确定删除此记录吗？",function(){
					$.ajax({
						type : "POST",
						url : 'business/app/funny/delete',
						data : params,
						success : function(resp) {
							myAlert("删除成功",function() {
										getFlowPackList();
									});
						}
					});
				});
			}
		});
		$("#funny_show .title .search a").click(function(){
			getFlowPackList();
		});
	}
	function getSelectRecordId(){
		var checkRadio = $('#funny_show_list_data input[name="radio_funnyList"]:checked');
		if(checkRadio.length == 0){
			myAlert("请选择记录！");
			return '';
		}else{
			return checkRadio.first().val();
		}
	}
</script>