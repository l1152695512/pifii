<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
	#micro_shop .title{width: 670px;}
	#micro_shop .title h3{width:90px;color: #444d5e;font-size:16px;font-weight: normal;float:left;line-height:32px;margin-top: 0px; }/*标题信息*/
	#micro_shop .title .search{background:#fff; text-indent:10px;float:left;padding:0;margin:0; border-radius:4px;}
	#micro_shop .title .search input{ border:1px solid #b4c0ce; color:#c6c6c6; width:150px; float:left; height:30px; line-height:30px;}
	#micro_shop .title .search a{ float:left;display:inline-block; border:1px solid #b4c0ce;border-left:0;width:30px;height:30px; line-height:30px; text-align:center;overflow:hidden;}
	#micro_shop .title .search a img{ width:16px;height:16px; vertical-align:middle;margin-left:-10px;}
	#micro_shop .delete,.edit,.add{ float:right;display:block;width:30px;height:30px; line-height:30px; text-align:center;overflow:hidden;}
	#micro_shop .delete img,.edit img,.add img{ width:16px;height:16px; vertical-align:middle;}
	
/* 	#micro_shop .data_list .ft{width: 100%;} */
	#micro_shop .data_list .ft>ul li{float:left;height: 43px;border-bottom: 2px solid #c0c4d1;line-height: 43px;text-align: center;font-weight: bold;font-size: 16px;overflow: hidden;}
	#micro_shop .data_list .ft .data ul li{float: left;height: 80px;border-bottom: 1px solid #dee2ed;text-align: center;line-height: 80px;overflow: hidden;}
	#micro_shop .data_list .ft .data ul li.radio_cls{*line-height: 0px;*height: 51px;*padding-top: 29px;}
	#micro_shop .data_list .ft .data ul li img{height:80px;}
</style>
<div id="micro_shop">
	<div class="title">
		<h3>标题：</h3>
		<div class="search fl">
			<input type="text" value="" placeholder="搜索标题"/>
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
			<li style="width: 120px;">标题</li>
			<li style="width: 100px;">图片</li>
			<li style="width: 85px;">原价</li>
			<li style="width: 85px;">现价</li>
			<li style="width: 90px;">类型</li>
			<li style="width: 120px;">更新时间</li>
		</ul>
			<div class="cl"></div>
			<div class="data" id="micro_shop_list_data"></div>
		</div>
		<div class="holder"></div>
	</div>
</div>
<script type="text/javascript">
	var startPage_microShop = 1;
	$(function() {
		initMicroShopEvent();
		generMicroContentPager();
	});
	function generMicroContentPager(){
		var searchText = $("#micro_shop .title .search input").first().val();
		$("#micro_shop_list_data").css("min-height","0px");
		$("#micro_shop .data_list .holder").jPages({
			containerID: "micro_shop_list_data",
			first:'首页',
			last:'尾页',
			previous:'上页',
			next:'下页',
			perPage : 5,
			startPage:startPage_microShop,
			realPagination:true,
	        serverParams:{
	        	url:"business/app/microshop/dataList",
	        	data:{title:searchText,shopId:getSelectedShopId()},
	        	generDataHtml:generMicroContentData
	        }
		});
	}
	function generMicroContentData(data,searchParams){
		var recHtml = "";
		if(data.length > 0){
			var list_header = $("#micro_shop .ft>ul>li");
			for(var i=0;i<data.length;i++){
				if("" == data[i].img){//兼容火狐，火狐浏览器中如果src为空字符串时，是不会执行onerror的
					data[i].img = "aa.jpg";
				}
				recHtml += '<ul>'+
								'<li style="width: '+list_header.eq(0).width()+'px;">'+(i+1)+'</li>'+
								'<li style="width: '+list_header.eq(1).width()+'px;" class="radio_cls"><input type="radio" name="radio_micro_shop" value="'+data[i].id+'" /></li>'+
								'<li style="width: '+list_header.eq(2).width()+'px;">'+data[i].title+'</li>'+
								'<li style="width: '+list_header.eq(3).width()+'px;"><img src="'+data[i].img+'" onerror="this.src=\'images/business/ad-1.jpg\'"/></li>'+
								'<li style="width: '+list_header.eq(4).width()+'px;">'+data[i].old_price+'</li>'+
								'<li style="width: '+list_header.eq(5).width()+'px;">'+data[i].new_price+'</li>'+
								'<li style="width: '+list_header.eq(6).width()+'px;">'+data[i].type_name+'</li>'+
								'<li style="width: '+list_header.eq(7).width()+'px;">'+data[i].create_date+'</li>'+
								'<li style="width: 0px;float: none;"></li>'+
							'</ul>';
			}
		}else{
			recHtml = '<div><font color="red">没有记录！</font></div>';
		}
		$("#micro_shop .data_list .data").html(recHtml);
		startPage_microShop = searchParams.pageNum;
	}
	
	function initMicroShopEvent(){
		$("#micro_shop .title .add,.edit").click(function(e){
			var title = "";
			var params = {shopId:getSelectedShopId()};
			if($(this).hasClass("add")){
				title = "添加数据";
			}else if($(this).hasClass("edit")){
				title = "修改数据";
				var id = getSelectMicroShopId();
				if(id != ''){
					params.id=id;
				}else{
					return;
				}
			}
			$.fn.SimpleModal({
// 				model : 'modal-ajax',
				title : title,
				width : 500,
// 				hideFooter : true,
				param : {
					url : 'business/app/microshop/editMicroShopInfo',
					data : params
				}
			}).showModal();
		});
		$("#micro_shop .title .delete").click(function(e){
			var id = getSelectMicroShopId();
			if(id != ''){
				var params = {id:id};
				myConfirm("确定删除此记录吗？",function(){
					$.ajax({
						type : "POST",
						url : 'business/app/microshop/deleteMicroShopInfo',
						data : params,
						success : function(resp) {
							myAlert("删除成功",function() {
								generMicroContentPager();
							});
						}
					});
				});
			}
		});
		$("#micro_shop .title .search a").click(function(){
			generMicroContentPager();
		});
	}
	function getSelectMicroShopId(){
		var checkRadio = $('#micro_shop_list_data input[name="radio_micro_shop"]:checked');
		if(checkRadio.length == 0){
			myAlert("请选择记录！");
			return '';
		}else{
			return checkRadio.first().val();
		}
	}
</script>