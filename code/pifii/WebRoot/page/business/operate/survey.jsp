<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#survey_page{
		color: gray;
		width: 970px;
		margin-top: 10px;
		overflow: auto;
	}
	#survey_page>li{
		margin-bottom: 10px;
	}
	#survey_page ul{
		margin-top: 13px;
		margin-left: 70px;
	}
	#survey_page ul li{
		margin-bottom: 5px;
	}
	#survey_page ul .option{
		float: left;
		width: 30%;
	}
	#survey_page ul .bars{
		float: left;
		width: 50%;
	}
	#survey_page ul .bar{
		float: left;
		margin-top: 5px;
		height: 20px;
		width: 85%;
		background: #E3E3E3;
	}
	#survey_page ul .precent{
		width: 45.9%;
		height: 100%;
		background: #F29149;
	}
	#survey_page ul .precentNum{
		text-align: right;
	}
	
	#survey_page ul .conter{
		text-align: right;
		padding-right: 30px;
	}
	#survey_page .gotoAddQuestion{
		color:rgb(0, 112, 255);
		font-size: 18px;
		cursor: pointer;
	}
</style>

<ul id="survey_page">
<!-- 	<li> -->
<!-- 		<h3>1.菜品种类及合胃口程度:</h3> -->
<!-- 		<ul> -->
<!-- 			<li> -->
<!-- 				<div class="option">勒布朗-詹姆斯</div> -->
<!-- 				<div class="bars"> -->
<!-- 					<div class="bar"><div class="precent" style="width: 45.9%;"></div></div> -->
<!-- 					<div class="precentNum">45.9%</div> -->
<!-- 				</div> -->
<!-- 				<div class="conter">22,587票</div> -->
<!-- 			</li> -->
<!-- 			<li> -->
<!-- 				<div class="option">勒布朗-詹姆斯</div> -->
<!-- 				<div class="bars"> -->
<!-- 					<div class="bar"><div class="precent" style="width: 45.9%;"></div></div> -->
<!-- 					<div class="precentNum">45.9%</div> -->
<!-- 				</div> -->
<!-- 				<div class="conter">22,587票</div> -->
<!-- 			</li> -->
<!-- 			<li> -->
<!-- 				<div class="option">勒布朗-詹姆斯</div> -->
<!-- 				<div class="bars"> -->
<!-- 					<div class="bar"><div class="precent" style="width: 45.9%;"></div></div> -->
<!-- 					<div class="precentNum">45.9%</div> -->
<!-- 				</div> -->
<!-- 				<div class="conter">22,587票</div> -->
<!-- 			</li> -->
<!-- 		</ul> -->
<!-- 	</li> -->
</ul>
<script type="text/javascript">
	$(function() {
		getQuestions();
	});
	function getQuestions(){
		$.ajax({
			type : "POST",
			url : 'business/survey/list',
			data : {shopId:getSelectedShopId()},
			success : function(data) {
				var recHtml = "";
				if(data.length > 0){
					var currentSurveyId = "";
					var surveyNum = 0;
					for(var i=0;i<data.length;i++){
						if(data[i].survey_id != currentSurveyId){
							surveyNum++;
							currentSurveyId = data[i].survey_id;
							if(currentSurveyId != ""){
								recHtml += '</ul></li>';
							}
							recHtml += '<li>' +
											'<h3>'+surveyNum+'.'+data[i].question+':</h3>' +
											'<ul>';
						}
						recHtml += '<li>' +
										'<div class="option">'+data[i].option+'</div>' +
										'<div class="bars">' +
											'<div class="bar"><div class="precent" style="width: '+data[i].precent+';"></div></div>' +
											'<div class="precentNum">'+data[i].precent+'</div>' +
										'</div>' +
										'<div class="conter">'+data[i].option_count+'票</div>' +
									'</li>';
					}
					recHtml += '</ul></li>';
				}else{
					recHtml = '<div>还没有添加调查问卷的题目，<span class="gotoAddQuestion">去添加</span></div>';
				}
				$("#survey_page").html(recHtml);
				$("#survey_page .gotoAddQuestion").click(function(){
					$.fn.SimpleModal({
						title: "调查问卷问题添加",
						param: {
							url:'business/survey/settingIndex'
						},
						afterClose:function(){
							getQuestions();
						}
					}).showModal();
				});
			}
		});
	}
	$(window).resize(function(){
		var pageContentHeight = $(window).height()-$("body>.header").height()
					-parseInt($("#survey_page").css("margin-top"))
					-parseInt($("#survey_page").css("margin-bottom"))
					-parseInt($("#survey_page").css("padding-top"))
					-parseInt($("#survey_page").css("padding-bottom"));
		$("#survey_page").css("height",pageContentHeight-10);
	});
</script>