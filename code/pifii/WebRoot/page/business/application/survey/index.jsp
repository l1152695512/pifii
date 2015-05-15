<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
/* 	#survey_show .title{margin:0 auto;margin-top:10px;} */
	#survey_show .title{width: 580px;border-bottom: 2px solid #dee2ed;}
/* 	#survey_show .title h3{width:90px;color: #444d5e;font-size:16px;font-weight: normal;float:left;line-height:32px;margin-top: 0px; } */
/* 	#survey_show .title .search{background:#fff; text-indent:10px;float:left;padding:0;margin:0; border-radius:4px;} */
/* 	#survey_show .title .search input{ border:1px solid #b4c0ce; color:#c6c6c6; width:150px; float:left; height:30px; line-height:30px;} */
/* 	#survey_show .title .search a{ float:left;display:inline-block; border:1px solid #b4c0ce;border-left:0;width:30px;height:30px; line-height:30px; text-align:center;overflow:hidden;} */
/* 	#survey_show .title .search a img{ width:16px;height:16px; vertical-align:middle;margin-left:-10px;} */
	#survey_show .delete,.edit,.add{ float:right;display:block;width:30px;height:30px; line-height:30px; text-align:center;overflow:hidden;}
	#survey_show .delete img,.edit img,.add img{ width:16px;height:16px; vertical-align:middle;}
	
/* 	#survey_show .data_list .ft{width: 100%;} */
/* 	#survey_show .data_list .ft>ul li{float:left;height: 43px;border-bottom: 2px solid #c0c4d1;line-height: 43px;text-align: center;font-weight: bold;font-size: 16px;overflow: hidden;} */
	#survey_show .data_list .ft .data ul li{padding-left: 20px;float: left;height: 60px;border-bottom: 1px solid #dee2ed;line-height: 60px;overflow: hidden;}
	#survey_show .data_list .ft .data ul li.radio_cls{*line-height: 0px;*height: 40px;*padding-top: 20px;}
/* 	#survey_show .data_list .ft .data ul li img{height:80px;} */
</style>
<div id="survey_show">
	<div class="title">
<!-- 		<h3>搜索：</h3> -->
<!-- 		<div class="search fl"> -->
<!-- 			<input type="text" value="" placeholder="输入关键字"/> -->
<!-- 			<a href="javascript:void(0)"> -->
<!-- 				<img src="images/business/ordering/dc_icon01.png"> -->
<!-- 			</a> -->
<!-- 		</div> -->
		<a href="javascript:void(0)" class="delete" title="删除">
			<img src="images/business/ordering/delete_icon.png">
		</a>
		<a href="javascript:void(0)" class="edit" title="修改">
			<img src="images/business/ordering/edit_icon.png">
		</a>
		<a href="javascript:void(0)" class="add" title="添加">
			<img src="images/business/ordering/add_icon.png">
		</a>
		<div class="cl"></div>
	</div>
	<div class="data_list">
		<div class="ft">
<!-- 			<ul> -->
<!-- 				<li style="width: 20px;"></li> -->
<!-- 				<li style="width: 30px;"></li> -->
<!-- 				<li style="width: 300px;">题目</li> -->
<!-- 			</ul> -->
<!-- 			<div class="cl"></div> -->
			<div class="data" id="survey_list_data"></div>
		</div>
		<div class="holder"></div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		initSurveyEvent();
		getSurveyList();
	});
	function getSurveyList(){
// 		var searchText = $("#survey_show .title .search input").first().val();
		$.ajax({
			type : "POST",
			url : "business/survey/listQuestions",
			data : {shopId:getSelectedShopId()},
			success : function(data) {
				var recHtml = "";
				if(data.length > 0){
					for(var i=0;i<data.length;i++){
						recHtml += '<ul>'+
										'<li style="width: 20px;" class="radio_cls"><input type="radio" name="check_row_data" value="'+data[i].id+'" /></li>'+
										'<li style="width: 500px;" title="'+data[i].question+'">'+(i+1)+'.'+data[i].question+'</li>'+
										'<li style="width: 0px;float: none;"></li>'+
									'</ul>';
					}
				}else{
					recHtml = '<div><font color="red">没有记录！</font></div>';
				}
				$("#survey_show .data_list .data").html(recHtml);
				generCouponPager();
			}
		});
	}
	function generCouponPager(){
		$("#survey_list_data").css("min-height","0px");//如果是弹窗显示列表时，如果要动态改变弹窗的高度（随记录的个数,如第二页记录数只有1条，切换到第二条时，高度会变化），需加上这行代码
		$("#survey_show .data_list .holder").jPages({
			containerID: "survey_list_data",
			first:'首页',
			last:'尾页',
			previous:'上页',
			next :'下页',
			perPage : 5,
			keyBrowse : true
		});
	}
	function initSurveyEvent(){
		$("#survey_show .title .add,.edit").click(function(e){
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
// 				width : 500,
// 				hideFooter : true,
				param : {
					url : 'business/survey/update',
					data : params
				}
			}).showModal();
		});
		$("#survey_show .title .delete").click(function(e){
			var id = getSelectRecordId();
			if(id != ''){
				var params = {id:id};
				myConfirm("确定删除此记录吗？",function(){
					$.ajax({
						type : "POST",
						url : 'business/survey/delete',
						data : params,
						success : function(resp) {
							getSurveyList();
						}
					});
				});
			}
		});
// 		$("#survey_show .title .search a").click(function(){
// 			getSurveyList();
// 		});
	}
	function getSelectRecordId(){
		var checkRadio = $('#survey_list_data input[name="check_row_data"]:checked');
		if(checkRadio.length == 0){
			myAlert("请选择一条记录！");
			return '';
		}else{
			return checkRadio.first().val();
		}
	}
</script>