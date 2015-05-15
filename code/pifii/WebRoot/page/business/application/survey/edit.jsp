<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style type="text/css">
	#survey_edit{width:500px;}
	#survey_edit .row{padding:0;margin-bottom:10px;}
	#survey_edit .row>span{float: left;width:60px;height:30px;line-height:30px;color:gray;font-size:15px;font-weight:bold;text-align:right;display:inline-block;}
	#survey_edit .row>label{margin-right: 80px;margin-left: 20px;line-height: 25px;}
	#survey_edit .row .actionDiv{height: 25px;padding-top: 5px;}
	#survey_edit .row .addAction{margin-left: 18px;}
	#survey_edit .row .addAction span{font-size: 15px;font-weight: bold;color: #738AA4;}
	#survey_edit .row .options{float: right;width: 420px;padding-bottom: 10px;overflow-y: auto;overflow-x: hidden;border: 1px solid #b4c0ce;color: #666;border-radius: 3px;}
	#survey_edit .row .options>textarea{ border:1px solid #b4c0ce;font-size: 16px;color:#666;width:298px;padding-left: 10px;border-radius:3px;overflow:hidden;display: none;}
	#survey_edit .row .options>div{font-size: 16px;margin-top: 10px;padding-bottom: 10px;margin-left: 10px;border-bottom: 1px solid #dee2ed;}
	#survey_edit .row .options>div span{overflow: hidden;width: 335px;display: block;}
	#survey_edit .row .options .deleteOptionAction{float:right;margin-right: 20px;margin-left: 20px;}
	#survey_edit .row .options .editOptionAction{float:right;}
/* 	#survey_edit .row input{ border:1px solid #b4c0ce; color:#666;margin-left:20px; width:300px; height:30px; line-height:30px;border-radius:3px;} */
/* 	#survey_edit .row select{outline:none;width:304px;color: rgb(105, 100, 100);height:30px;margin-left: 20px;line-height:30px;border: 1px solid #B4C0CE;font-weight:bold;border-radius:4px;} */
	#survey_edit .row>textarea{ border:1px solid #b4c0ce;color:#666;width:413px;margin-left: 20px; height:60px;border-radius:3px;overflow:hidden;}
/* 	#survey_edit .row img{width:140px;height:80px;overflow:hidden;margin-left: 20px;} */
/* 	#survey_edit .row .line_height_30{height:30px;line-height:30px;} */
	#survey_edit .row .line_height_60{height:60px;line-height:60px;}
/* 	#survey_edit .row .line_height_80{height:80px;line-height:80px;} */
	
/* 	#survey_edit .button_upload{position:relative;filter:alpha(opacity=100);apacity:1;height:30px;line-height:30px;width:157px; text-align:center;color:#FFF; background:#31a3ff;display:inline-block;border-radius:4px; font-weight:bold;top:-10px;left:0;} */
/* 	#survey_edit .button_upload:hover{background:#52B0FC;} */
/* 	#survey_edit .button_upload input{width: 230px;height:30px;position:absolute;opacity:0;right:0;top:0;cursor:pointer;} */
	
	#survey_edit .content_btn1{margin-top:20px;margin-left:120px;float:left;}
	#survey_edit .content_btn1 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
	#survey_edit .content_btn2{float:left;margin-top:20px;margin-left:10px;}
	#survey_edit .content_btn2 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
</style>
<div id="survey_edit">
	<div>
		<div class="row">
			<span>类型：</span>
			<label><input checked="checked" type="radio" name="type" value="1">&nbsp;单选</label>
			<label><input <c:if test="${type==2}">checked="checked" </c:if> type="radio" name="type" value="2">&nbsp;多选</label>
		</div>
		<div class="row">
			<span class="line_height_60">问题：</span>
			<textarea name="question" placeholder="1-100字" cols="5" rows="5">${question}</textarea>
		</div>
		<div class="row">
			<span>选项：</span>
			<div class="actionDiv">
				<a href="javascript:void(0)" class="addAction" title="添加选项">
					<img src="images/business/ordering/add_icon.png">
					<span>添加选项</span>
				</a>
			</div>
			<div class="options">
				<textarea name="option" placeholder="1-100字" cols="5" rows="1"></textarea>
				<c:forEach items="${options}" var="row" varStatus="stat">
					<div data-id="${row.id}">
						<a href="javascript:void(0)" title="删除选项" class="deleteOptionAction">
							<img src="images/business/ordering/delete_icon.png">
						</a>
						<a href="javascript:void(0)" class="editOptionAction" title="修改选项">
							<img src="images/business/ordering/edit_icon.png">
						</a>
						<span title="${row.option}">${row.option}</span>
					</div>
				</c:forEach>
			</div>
			<div class="cl"></div>
		</div>
<!-- 		<div> -->
<!-- 			<form action="business/app/coupon/save" id="coupon_add_form" method="post" > -->
<!-- 				<input type="text" name="a"/> -->
<!-- 				<input type="text" name="a"/> -->
<!-- 				<input type="text" name="a"/> -->
<!-- 			</form> -->
<!-- 		</div> -->
		<div class="content_btn1">
			<a href="javascript:void(0)" onclick="submitPage();" >
				<span>确定</span>
			</a>
		</div>
		<div class="content_btn2">
			<a href="javascript:void(0)" onclick="closePop();">
				<span>取消</span>
			</a>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		checkTips();
		initSurveyEditEvent();
		addOptionsEvent();
	});
	function initSurveyEditEvent(){
		$("#survey_edit a.addAction").click(function(){
// 			$(this).hide();
			var option = $('<div>' +
								'<a href="javascript:void(0)" title="删除选项" class="deleteOptionAction">' +
									'<img src="images/business/ordering/delete_icon.png">' +
								'</a>' +
								'<a href="javascript:void(0)" class="editOptionAction" title="修改选项">' +
									'<img src="images/business/ordering/edit_icon.png">' +
								'</a>' +
								'<span></span>'+
							'</div>').appendTo($("#survey_edit .options"));
			$("#survey_edit .options>span").remove();
			editOption(option);
		});
		$("#survey_edit .options textarea").focusout(function(){
			var val = $(this).val();
			if(val != $(this).prev("div").children("span").text() && $(this).prev("div").data("id") != ''){
				$(this).prev("div").data('edit','1');
			}
			var isDelete = false;
			if(val != ''){
				$(this).prev("div").children("span").text(val);
				$(this).prev("div").children("span").attr("title",val);
			}else{
				if(undefined == $(this).prev("div").data("id")){
					$(this).prev("div").remove();
					isDelete = true;
				}else{
					$(this).prev("div").children("span").text('');
				}
			}
			if(!isDelete){
				$(this).prev("div").show();
			}
// 			$("#survey_edit a.addAction").show();
			$(this).hide();
			addOptionsEvent();
		});
	}
	function checkTips(){
		if($("#survey_edit .options>div:visible").length==0 && $("#survey_edit .options>span").length==0){
			$("#survey_edit .options").append('<span>请点击“添加选项”</span>');
		}
	}
	function addOptionsEvent(){
// 		$("#survey_edit .options div").unbind("click");
// 		$("#survey_edit .options div").click(function(){
			//editOption($(this));
// 		});
		$("#survey_edit .options .deleteOptionAction").unbind("click");
		$("#survey_edit .options .deleteOptionAction").click(function(e){
			var $this = $(this);
			myConfirm("确定要删除该选项？",function(){
				if(undefined == $this.parent().data("id")){
					$this.parent().remove();
				}else{
					$this.parent().data("delete","1");
					$this.parent().hide();
				}
				checkTips();
			});
			return false;//禁止该事件冒泡
		});
		$("#survey_edit .options .editOptionAction").unbind("click");
		$("#survey_edit .options .editOptionAction").click(function(e){
			editOption($(this).parent());
		});
		checkTips();
	}
	function editOption($option){
		$option.after($("#survey_edit .options textarea"));
		$("#survey_edit .options textarea").val($option.children("span").text());
		$("#survey_edit .options textarea").css({'width':'96%','height':'20px'});
		$option.hide();
		$("#survey_edit .options textarea").show();
		$("#survey_edit .options textarea").focus();
	}
	function submitPage(){
// 		if("${id}" == ''){//为添加
// 			var shopId = getSelectedShopId();
// 			$("#survey_edit form input[name='coupon.shopId']").attr("value",shopId);
// 		}
		var type= $('#survey_edit input[name="type"]:checked').first().val();
		if('' == type){
			myAlert("请选择类型！");
			return;
		}
		var question=$('#survey_edit textarea[name="question"]').first().val();
		if(question.length>100 || question.length<1){
			myAlert("问题的字数在1-100字之间！");
			return;
		}
		var addOptions = [];
		var deleteOptions = [];
		var editOptions = [];
		var hasError = false;
		$("#survey_edit .options div").each(function(){
			if(undefined == $(this).data("id") || $(this).data("id")==''){
				if($(this).children("span").text().length>100 || $(this).children("span").text().length<1){
					hasError = true;
					return false;
				}
				addOptions.push($(this).children("span").text());
			}else if($(this).data("delete")==1 || $(this).children("span").text()==''){
				deleteOptions.push($(this).data("id"));
			}else{
				if($(this).children("span").text().length>100 || $(this).children("span").text().length<1){
					hasError = true;
					return false;
				}
				editOptions.push($(this).data("id")+":"+$(this).children("span").text());
			}
		});
		if(hasError){
			myAlert("选项的字数在1-100字之间！");
			return;
		}
		var data = $.param({id:"${id}",shopId:getSelectedShopId(),type:type,question:question,
			addOptions:addOptions,deleteOptions:deleteOptions,editOptions:editOptions},true)
		$.ajax({
			type : "POST",
			url : "business/survey/save",
			data : data,
			success : function(data) {
				closePop();
				getSurveyList();
			}
		});
	 }
</script>
