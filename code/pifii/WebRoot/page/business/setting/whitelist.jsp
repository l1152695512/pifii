<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#whitelist{color: gray;}
	#whitelist .title{border-bottom: 1px solid #C0C4D1;padding: 14px 0px;font-weight: bold;font-size: 18px;text-align: center;}
	#whitelist .add{ float:right;display:block;width:30px;height:30px; line-height:30px; text-align:center;overflow:hidden;margin-right: 68px;}
	#whitelist .add img{ width:16px;height:16px; vertical-align:middle;}
	#whitelist .ft{width: 970px;}
	#whitelist .ft >ul li{float:left;height: 43px;line-height: 43px;text-align: center;font-weight: bold;font-size: 16px;background: #E3E8EE;}
	#whitelist .ft .data ul li{float: left;width: 192px;height: 48px;border-top: 1px solid #dee2ed;text-align: center;line-height: 48px;overflow: hidden;}
	#whitelist .operate{display: inline-block;width: 53px;height: 30px;line-height: 30px;color: #fff;border-radius: 2px;cursor: pointer;margin-right: 10px;background: rgb(223, 163, 161);}
	#whitelist .operate:hover{background: rgb(211, 70, 65);}
</style>

<div id="whitelist">
	<div class="title">
		白名单列表
		<a href="javascript:void(0)" class="add fr">
			<img src="images/business/ordering/add_icon.png" title="添加">
		</a>
		<div class="cl"></div>
	</div>
	<div class="ft">
		<ul>
			<li style="width:80px">序号</li>
			<li style="width:250px">类型</li>
			<li style="width:250px">内容</li>
			<li style="width:240px">备注</li>
			<li style="width:150px">操作</li>
			<li style="float: none;width: 0px;"></li>
		</ul>
		<div class="cl"></div>
		<div class="data" id="list_data_whitelist"></div>
	</div>
	<div class="holder"></div>
</div>
<script type="text/javascript">
	$(function() {
		getWhitelistData();
		$("#whitelist .title .add img").click(function(){
			$.fn.SimpleModal({
				title : "白名单添加",
				width : 500,
				param : {
					url : 'business/setting/addWhitelist'
				}
			}).showModal();
		});
	});
	function getWhitelistData(){
		$("#whitelist .holder").jPages({
			containerID:"list_data_whitelist",
			first:'首页',
			last:'尾页',
			previous:'上页',
			next:'下页',
			perPage : 10,
			startPage:1,
			keyBrowse : true,
			realPagination:true,
	        serverParams:{
	        	url:"business/setting/getWhiteList",
	        	data:{shopId:getSelectedShopId()},
	        	generDataHtml:generWhitelistData
	        }
		});
	}
	
	function generWhitelistData(data,searchParams){
		var recHtml = "";
		if(data.length > 0){
			var list_header = $("#whitelist .ft>ul>li");
			for(var i=0;i<data.length;i++){
				recHtml += '<ul data-id="'+data[i].id+'">'+
								'<li style="width: '+list_header.eq(0).width()+'px;">'+(searchParams.startRowNum+i)+'</li>'+
								'<li style="width: '+list_header.eq(1).width()+'px;">';
				if(data[i].type == 'mac'){
					recHtml += 'MAC地址';
				}else if(data[i].type == 'domain'){
					recHtml += '域名';
				}else if(data[i].type == 'ip'){
					recHtml += 'IP';
				}
				recHtml += '</li>'+
							'<li style="width: '+list_header.eq(2).width()+'px;">'+data[i].content+'</li>'+
							'<li style="width: '+list_header.eq(3).width()+'px;" title="'+data[i].marker+'">'+data[i].marker+'</li>'+
							'<li style="width: '+list_header.eq(4).width()+'px;"><span class="operate">删除</span></li>'+
							'<li style="float: none;width: 0px;"></li>'+
						'</ul>';
			}
		}else{
			recHtml = '<div><font color="red">没有记录！</font></div>';
		}
		$("#whitelist .data").html(recHtml);
		$("#whitelist .operate").click(function(){
			var id=$(this).parent().parent().data("id");
			myConfirm("确定要删除该白名单？",function(){
				$.ajax({
					type : "POST",
					url : "business/setting/deleteWhitelist",
					data : {id:id,shopId:getSelectedShopId()},
					success : function(data) {
						getWhitelistData();
					}
				});
			});
		});
	}
</script>