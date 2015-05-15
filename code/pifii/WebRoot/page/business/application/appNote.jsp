<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
	#app_note .icon{padding-top: 20px;width: 150px;border-right: 1px solid #ccc;float: left;}
	#app_note .icon h2{text-align: center;margin-top: 10px;line-height: 40px;}
	#app_note .icon .iconDiv{width: 100px;height: 100px;margin-left: 15%;position: relative;overflow: hidden;}
	#app_note .icon .iconDiv .appIcon{width: 100px;height: 100px;}
	#app_note .icon .iconDiv .changeIconAction{
		background: rgb(73, 73, 73);
		opacity: 0.8;
		filter: alpha(opacity=80);
		-moz-opacity: 0.8;
		height: 30px;
		width: 100%;
		position:relative;
		top: -34px;
		cursor: pointer;
		border-bottom-left-radius: 7px;
		border-bottom-right-radius: 7px;
		overflow: hidden;
		display: none;
	}
	#app_note .icon .iconDiv .changeIconAction .editIcon{cursor: pointer;position: absolute;left: 7%;top: 6px;}
	#app_note .icon .iconDiv .changeIconAction span{cursor: pointer;position: absolute;left: 27%;top: 6px;}
	#app_note .icon .iconDiv .changeIconAction input{width:3774px;height: 285px;position:absolute;opacity:0;filter: alpha(opacity=0);right:0;top:0;cursor:pointer;font-size: 30px;}
	
	#app_note .info{float: left;padding-left: 20px;}
	#app_note .info .name{width: 300px;height: 28px;margin-top: 10px;}
	#app_note .info .name h3{float: left;line-height: 35px;}
	#app_note .info .name .name_input{float: left;}
	#app_note .info .name .name_input input{display:none;margin-left: 15px;height: 28px;*line-height:28px;width: 160px;border: 1px solid #CDCDCD;}
	#app_note .info .name .name_input span{margin-left: 15px;line-height: 29px;font-size: 18px;}
	#app_note .info .name .action{float: right;margin-top: 5px;}
	#app_note .info .name .action .undo{display: none;}
	#app_note .info .name .action a{margin-right: 30px;}
	
	#app_note .info .desc{margin-top: 15px;}
	#app_note .info .name .delete{float:right;margin-right: 20px;margin-left: 20px;}
	#app_note .info .name .edit{float:right;}
	#app_note .info .detail{
		height: 69px;
		width: 300px;
		text-indent: 2em;
		overflow-y: auto;
		overflow-x: hidden;
		word-wrap: break-word;
		margin-top: 10px;
	}
</style>

<div id="app_note" data-id="${id}" data-custom-id="${custom_app_id}">
	<div class="icon">
		<form action="business/app/saveCustomAppIcon" method="post" enctype="multipart/form-data">
			<div class="iconDiv">
				<img class="appIcon" src="${icon}"></img>
				<div class="changeIconAction" title="编辑">
					<img class="editIcon" src="images/business/ordering/edit_icon.png">
					<span>60px * 60px</span>
					<input type="file" name="upload"/>
				</div>
			</div>
			<input type="hidden" name="shopId"/>
			<input type="hidden" name="appId"/>
			<input type="hidden" name="custom_app_id"/>
		</form>
		<h2>${name}</h2>
	</div>
	<div class="info">
		<div class="name">
			<h3>名称</h3>
			<div class="name_input">
				<span>${name}</span>
				<input name="appName"/>
			</div>
			<div class="action">
<!-- 				<a href="javascript:void(0)" title="删除" class="undo"> -->
<!-- 					<img src="images/business/ordering/delete_icon.png"> -->
<!-- 				</a> -->
				<a href="javascript:void(0)" title="修改" class="edit">
					<img src="images/business/ordering/edit_icon.png">
				</a>
			</div>
		</div>
		<div class="desc">
			<h3>描述</h3>
			<div class="detail">
				${des}
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		initAppNoteEditEvent();
	});
	function initAppNoteEditEvent(){
		$("#app_note .info .name .action .edit").click(function(){
			showEdit(true);
		});
		$("#app_note .info .name .name_input input").blur(function(){
			var oldAppName = $("#app_note .info .name .name_input span").text();
			var appName = $("#app_note .info .name .name_input input").val();
			if(oldAppName == appName){
				showEdit(false);
			}else{
				$.ajax({
					type : "POST",
					url : 'business/app/saveCustomAppName',
					data : {shopId:getSelectedShopId(),appId:"${id}",custom_app_id:"${custom_app_id}",name:appName},
					success : function(data) {
						showEdit(false);
						changeAppShowName(appName);
						refreshPhonePagePreview("app");
					}
				});
			}
		});
		$("#app_note .icon .iconDiv").mouseenter(function() {
			$(this).children(".changeIconAction").fadeIn();
		}).mouseleave(function() {
			$(this).children(".changeIconAction").fadeOut();
		});
		$("#app_note .icon .iconDiv .changeIconAction input").uploadPreview({ Img: $("#app_note .icon .iconDiv .appIcon")});
		$("#app_note .icon .iconDiv .changeIconAction input").on("change", function() {
			$("#app_note form input[name='shopId']").val(getSelectedShopId());
			$("#app_note form input[name='appId']").val("${id}");
			$("#app_note form input[name='custom_app_id']").val("${custom_app_id}");
			$("#app_note form").ajaxSubmit({
				success: function(resp){
					if(typeof resp === "string"){
	    				resp = $.parseJSON(resp);
	    			}
	    			if(resp.success == true){
	    				changeAppShowIcon(resp.src);
	    				$("#app_note .icon .iconDiv .appIcon").attr("src",resp.src);
	    				$("#app_note .icon .iconDiv .appIcon").show();
	    				refreshPhonePagePreview("app");
	    			}else{
	    				myAlert(resp.msg);
	    			}
				}
			});
	 	});
	}
	function showEdit(show){
		var showNameSpan = $("#app_note .info .name .name_input span");
		var showNameInput = $("#app_note .info .name .name_input input");
		var editImg = $("#app_note .info .name .action .edit");
		
		if(show){
			showNameInput.val(showNameSpan.text());
			showNameSpan.hide();
			showNameInput.show();
			editImg.hide();
			showNameInput.focus();
		}else{
			showNameSpan.text(showNameInput.val());
			$("#app_note .icon h2").text(showNameInput.val());
			showNameInput.hide();
			showNameSpan.show();
			editImg.show();
		}
	}
</script>
			