<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
	#manage_adv{
		width: 920px;
		height: 620px;
		position: relative;
	}
	#manage_adv>.phone_emulator{
		height: 620px;
		padding-left: 36px;
		padding-right: 36px;
		position: absolute;
		z-index: 2;
		left: -20px;
		background: url(images/business/moreniphone.png) no-repeat;
	}
	#manage_adv>.phone_emulator>iframe{
		background-color:#fff;
		height: 444px;
		width: 260px;
		margin-top: 97px;
	}
	#manage_adv .adv_setting{
		width: 605px;
		padding-top: 79px;
		overflow: hidden;
	}
	#manage_adv .adv_setting ul li {
		float: left;
		display:none;
	}
	#manage_adv .adv_setting .pic{
		width:290px;
		height:150px;
		overflow:hidden;
		border: 1px solid #C0C0C0;
	}
	#manage_adv .adv_setting .pic img{
		width:100%;
		height: 100%;
	}
	#manage_adv .adv_setting ul li.evenRow {
		margin-right: 0;
		float: right;
	}
	#manage_adv .adv_setting .wenzi {
		text-align: center;
		width: 292px;
		height: 33px;
		line-height: 33px;
		color: #5b646f;
		position: relative;
		font-size: 16px;
		font-family: "宋体";
	}
	#manage_adv .adv_setting .wenzi span {
		display: block;
		float: left;
		position: absolute;
		width: 20px;
		height: 20px;
		top: 5px;
		cursor: pointer;
	}
	#manage_adv .adv_setting .wenzi span.delate {
		display:none;
		right: 0;
		background:
			url(images/business/adv/delate.png)
			no-repeat 0 0;
	}
	#manage_adv .adv_setting .wenzi span.editor {
		display:none;
		right: 35px;
		background:
			url(images/business/adv/editor.png)
			no-repeat 0 0;
	}
</style>
<div id="manage_adv">
	<div class="phone_emulator fl">
		<iframe frameborder="0" allowtrancparency="true" scrolling="auto" name="page_preview" framespacing="0" src=""></iframe>
	</div>
	<div class="adv_setting fr">
		<ul>
			<li>
				<div class="pic"><img src="" onerror="this.src='images/business/ad-1.jpg'" /></div>
				<div class="wenzi">01 <span class="editor"></span><span class="delate"></span></div>
			</li>
			<li class="evenRow">
				<div class="pic"><img id="pic_02" src="images/business/ad-1.jpg" onerror="this.src='images/business/ad-1.jpg'" /></div>
				<div class="wenzi">02 <span class="editor"></span><span class="delate"></span></div>
			</li>
			<li>
				<div class="pic"><img id="pic_03" src="images/business/ad-1.jpg"  onerror="this.src='images/business/ad-1.jpg'" /></div>
				<div class="wenzi">03 <span  class="editor"></span><span class="delate"></span></div>
			</li>
			<li class="evenRow">
				<div class="pic"><img id="pic_04" src="images/business/ad-1.jpg" onerror="this.src='images/business/ad-1.jpg'" /></div>
				<div class="wenzi">04 <span class="editor"></span><span class="delate"></span></div>
			</li>
		</ul>
		<div class="cl"></div>
	</div>
	<div class="cl"></div>
</div>
<script type="text/javascript" >
	$(document).ready(function(){
		$("#manage_adv>.phone_emulator>iframe").attr("src","business/template/showPreview?shopId="+getSelectedShopId());
// 		loadPage("${pageContext.request.contextPath}/business/app/adv/right",{},$("#contentAD .ADright"));//得到广告right 插入广告HTML
		reloadAdvs();
		$("#manage_adv .adv_setting>ul>li .editor").click(function(){
			var id = $(this).parent().data("id");
// 			if(id && id != ''){
				var serial = $(this).parent().data("serial");
				var link = $(this).parent().data("link");
				var des = $(this).parent().data("des");
				var img = $(this).parent().data("img");
				$.fn.SimpleModal({
					title: '广告配置',
					param: {
						url: 'business/app/adv/addOrEditInfo',
						data:{id:id,serial:serial,link:link,des:des,img:img}
					}
				}).showModal();
// 			}else{
// 				myAlert("无权限编辑！");
// 			}
		});
// 		$("#manage_adv .adv_setting>ul>li .delate").click(function(){
// 			var id = $(this).parent().data("id");
// 			myConfirm("确定重置该广告？",function(){
// 				if(undefined == id || "" == id){
// 					myAlert("没有权限删除该广告！");
// 				}else{
// 					$.ajax({
// 			            type: "POST",
// 			            url: 'business/app/adv/delete?id='+id,
// 			            success: function(data) {
// 			            	reloadAdvs();
// 			            }
// 			        });
// 				}
// 			});
// 		});
	});
	function reloadAdvs(){
		$("#manage_adv .adv_setting>ul>li").each(function(){
			$(this).children(".wenzi").removeData("id");
			$(this).children(".wenzi").removeData("serial");
			$(this).children(".wenzi").find(".editor").hide();
//			$(this).children(".wenzi").find(".delate").hide();
		});
		$.ajax({
			type : "POST",
			url : 'business/app/adv/getAdvs',
			data : {shopId:getSelectedShopId()},
			success : function(data) {
				for (var i = 0; i < data.length; i++) {
					var thisAdv = $("#manage_adv .adv_setting>ul>li").eq(parseInt(data[i].serial)-1);
					thisAdv.show();
					if(data[i].image != ""){
						thisAdv.children(".pic").children("img").attr("src", data[i].image);
					}
					if(data[i].access == "1"){
						thisAdv.children(".wenzi").data("id",data[i].id);
						thisAdv.children(".wenzi").data("serial",data[i].serial);
						thisAdv.children(".wenzi").data("link",data[i].link);
						thisAdv.children(".wenzi").data("des",data[i].des);
						thisAdv.children(".wenzi").data("img",data[i].image);
						thisAdv.children(".wenzi").find(".editor").show();
					}
				}
			}
		});
		refreshPhonePagePreview("adv");
	}
</script>
