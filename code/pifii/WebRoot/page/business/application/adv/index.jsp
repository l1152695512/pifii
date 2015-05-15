<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
	#manage_adv{
		width: 950px;
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
		width: 630px;
		height: 435px;
		margin-top: 79px;
		overflow: auto;
	}
/* 	#manage_adv .adv_setting ul{ */
/* 		height: 410px; */
/* 		overflow: hidden; */
/* 	} */
	#manage_adv .adv_setting ul li {
		float: left;
		margin-right: 20px;
		margin-bottom: 20px;
/* 		display:none; */
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
	#manage_adv .adv_setting .wenzi span.delete {
/* 		display:none; */
		right: 0;
		background:
			url(images/business/adv/create.png)
			no-repeat 0 0;
	}
	#manage_adv .adv_setting .wenzi span.editor {
/* 		display:none; */
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
				<div class="wenzi">01 <span class="editor"></span><span class="delete"></span></div>
			</li>
			<li class="evenRow">
				<div class="pic"><img id="pic_02" src="" onerror="this.src='images/business/ad-1.jpg'" /></div>
				<div class="wenzi">02 <span class="editor"></span><span class="delete"></span></div>
			</li>
		</ul>
	</div>
	<div class="cl"></div>
</div>
<script type="text/javascript" >
	$(document).ready(function(){
		$("#manage_adv>.phone_emulator>iframe").attr("src","business/template/showPreview?shopId="+getSelectedShopId());
// 		loadPage("${pageContext.request.contextPath}/business/app/adv/right",{},$("#contentAD .ADright"));//得到广告right 插入广告HTML
		reloadAdvs();
	});
	function reloadAdvs(){
		$.ajax({
			type : "POST",
			url : 'business/app/adv/getAdvs',
			data : {shopId:getSelectedShopId()},
			success : function(data) {
				var advsHtml = "";
				for (var i = 0; i < data.length; i++) {
					var evenRow = "";
					if(i%2 == 1){
						evenRow = ' class="evenRow"';
					}
					if("" == data[i].image){//兼容火狐，火狐浏览器中如果src为空字符串时，是不会执行onerror的
						data[i].image = "aa.jpg";
					}
					advsHtml += '<li'+evenRow+'>'+
									'<div class="pic"><img onerror="this.src=\'images/business/ad-1.jpg\'" src="'+data[i].image+'"></div>'+
									'<div class="wenzi" data-id="'+data[i].id+'" data-adv-spaces-id="'+data[i].adv_spaces_id+'" '+
											'data-adv-content-id="'+data[i].adv_content_id+'" data-img-width="'+data[i].img_width+'" data-img-height="'+data[i].img_height+'">'+
										data[i].name;
					if(data[i].adv_spaces_id != ""){
						advsHtml += '<span class="editor" title="编辑广告"></span>';
						if(data[i].id != ""){
							advsHtml += '<span class="delete" title="投放广告"></span>';
						}
					}
					advsHtml += '</div></li>';
				}
				if(advsHtml == ""){
					$("#manage_adv .adv_setting>ul").html("暂无可配置的广告！");
				}else{
					$("#manage_adv .adv_setting>ul").html(advsHtml+'<li class="cl"></li>');
				}
				initAdvsEvent();
			}
		});
		refreshPhonePagePreview("adv");
	}
	function initAdvsEvent(){
		$("#manage_adv .adv_setting>ul>li .editor").unbind("click");
		$("#manage_adv .adv_setting>ul>li .editor").click(function(){
			var data = {
					id:$(this).parent().data("id"),
					advSpacesId:$(this).parent().data("advSpacesId"),
					advContentId:$(this).parent().data("advContentId"),
					imgWidth:$(this).parent().data("imgWidth"),
					imgHeight:$(this).parent().data("imgHeight")
			};
			changeAdv(data);
		});
		$("#manage_adv .adv_setting>ul>li .delete").unbind("click");
		$("#manage_adv .adv_setting>ul>li .delete").click(function(){
			var data = {
					id:$(this).parent().data("id"),
					advSpacesId:$(this).parent().data("advSpacesId"),
					imgWidth:$(this).parent().data("imgWidth"),
					imgHeight:$(this).parent().data("imgHeight")
			};
			changeAdv(data);
// 			var id = $(this).parent().data("id");
// 			if("" != id){
// 				myConfirm("确定删除该广告？",function(){
// 					$.ajax({
// 			            type: "POST",
// 			            url: 'business/app/adv/delete',
// 			            data:{id:id},
// 			            success: function(data) {
// 			            	reloadAdvs();
// 			            }
// 			        });
// 				});
// 			}
		});
	}
	function changeAdv(data){
		$.fn.SimpleModal({
			title: '广告配置',
			param: {
				url: 'business/app/adv/addOrEditInfo',
				data:data
			}
		}).showModal();
	}
</script>
