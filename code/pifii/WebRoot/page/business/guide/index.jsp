<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<style type="text/css">
	#guide_index{
		width: 990px;
		height: 620px;
/* 		margin:0 auto; */
/* 		position: relative; */
		font-family: "微软雅黑";
	}
	#guide_index>.guide_phone_preview{
		height: 620px;
/* 		width:332px; */
/* 		display: inline-block; */
		padding-left: 36px;
		padding-right: 36px;
/* 		position: absolute; */
/* 		z-index: 2; */
/* 		left: -50px; */
		background: url(images/business/moreniphone.png) no-repeat;
	}
/* 	#guide_index>.guide_phone_preview img{ */
/* 		width: 100%; */
/* 	} */
	#guide_index>.guide_phone_preview iframe{
		background-color:#fff;
		filter:chroma(color=black);
		height: 444px;
		width: 260px;
		margin-top: 97px;
	}
	#guide_index .fr{
		height: 100%;
	}
	#guide_index .guide_step_title{
		height:88px;
		width: 653px;
		color: #444d5e;
		font-size: 28px;
		line-height: 122px;
		border-bottom: 1px solid #C4C9D7;
		position: relative;
	}
	#guide_index .guide_step_title img{
		position: absolute;
		right: 0;
		top: 35px;
/* 		float: right; */
/* 		margin-top: 36px; */
	}
	#guide_index .start_guide{
		float:right;
		margin-right: 53px;
		color: #fff;
		text-align: center;
		font-size: 18px;
		width: 140px;
		height: 40px;
		line-height: 40px;
		background: #1A9AFF;
		cursor: pointer;
	}
	#guide_index .step_content{
		height:520px;
	}
	#guide_index .step_button {
		width: 290px;
		margin:0 auto;
		height: 50px;
	}
	#guide_index .step_button span{
/* 		display: block; */
/* 		float: left; */
		width: 137px;
		height: 35px;
		border:1px solid #0284e4;
		border-radius: 2px;
		margin-right: 10px;
		font-size: 16px;
		color: #fff;
		font-weight: bold;
		background: #1a9aff;
		font-family: "宋体";
		font-size: 16px;
		text-align: center;
		line-height: 35px;
		font-weight: bold;
		cursor: pointer;
	}
	#guide_index .step_button .previous_step{
		float:left;
	}
	#guide_index .step_button .next_step{
		margin-right: 0;
		float:right;
	}
	#guide_index .this_step_content{
		height: 420px;
		overflow: auto;
	}
</style>

<div id="guide_index">
	<div class="guide_phone_preview fl">
		<iframe  src="" framespacing="0" name="page_preview" 
			frameborder="0" scrolling="auto" allowtrancparency="true"></iframe>
<!-- 		<img src="images/business/moreniphone.png"/> -->
	</div><!--向导指引  左边手机界面-->
	<div class="fr">
		<div class="step_content">
			<div class="guide_step_title">向导指引</div>
			<div class="this_step_content"></div>
		</div>
		<div>
			<div class="start_guide">开启向导</div>
			<div class="step_button">
				<span class="previous_step">上一步</span>
				<span class="next_step">下一步</span>
			</div>
		</div>
	</div><!--空盒子为了装 两个guideRight（首次+非首次）-->
</div>
<script type="text/javascript">
	var previousStep = 0;
	var currentShowStep = 0;//0即为向导内容
	var $contentPage;
	$(function(){
// 		var src = $("iframe[name='iframe']").attr("src");
// 		var newSrc = src+"?shopId="+getSelectedShopId();
		$("#guide_index>.guide_phone_preview>iframe").attr("src","business/template/showPreview?shopId="+getSelectedShopId());
		$("#guide_index .step_button").hide();
		$("#guide_index .start_guide").click(function(){
			showStep(getShopPageInfo().step);
		});
		$("#guide_index .step_button").find("span").eq(0).click(function(){
// 			if(currentShowStep == 4){
// 				$.ajax({
// 					type: "POST",
// 					url: "business/page/changePageStep",
// 					data:{pageId:getPageId(),step:currentShowStep},
// 					success: function(data,status,xhr){
// 						if("success" == status){
// 							if(data.success = true){
// 								myAlert("数据已保存。");
// 							}else{
// 								myAlert("保存失败，稍后请重试！");
// 							}
// 						}
// 					}
// 				});
// 			}else{
				showStep(currentShowStep-1);
// 			}
		});
		$("#guide_index .step_button").find("span").eq(1).click(function(){
			if(currentShowStep == 1){
				var choiceTemplate = false;
				$("#guide_templates").find("li").each(function(){
					if($(this).children().last().css("cursor") == "auto"){
						choiceTemplate = true;
						return false;
					}
				});
				if(choiceTemplate){
					showStep(currentShowStep+1);
				}else{
					myAlert("请先选择风格！");
				}
			}else if(currentShowStep == 2 || currentShowStep == 3){
				if(currentShowStep > getShopPageInfo().step){
					$.ajax({
						type: "POST",
// 						async: false,
						url: "business/page/changePageStep",
						data:{pageId:getPageId(),step:currentShowStep},
						success: function(data,status,xhr){
							setShopPageInfoStep(currentShowStep);
							showStep(currentShowStep+1);
						}
					});
				}else{
					showStep(currentShowStep+1);
				}
			}else if(currentShowStep == 4){
				$.ajax({
					type: "POST",
					url: "business/page/publishPage",
					data:{pageId:getPageId(),step:currentShowStep},
					success: function(data,status,xhr){
						myAlert("数据已保存。",function(){
							location.reload();
						});
					}
				});
			}
		});
		//得到向导指引的--右边向导指引步骤--HTML
		$contentPage = $("#guide_index .this_step_content");
		loadPage("page/business/guide/steps.jsp",{},$contentPage);
// 		loadPageWithCallback("page/business/guide/steps.jsp",{},function(html){
// 			$contentPage.append(html);
// 		});
	});
	//更新右边的内容为当前步骤
	function showStep(step){
		if(step > 0 && currentShowStep == step){
			return;
		}
		if(step > getShopPageInfo().step+1){
			myAlert("请按照步骤创建！");
		}else{
			if(step < 1){
				step = 1;
			}
			$("#guide_index .start_guide").hide();
			$("#guide_index .step_button").find("span").eq(0).show();
			$("#guide_index .step_button").find("span").eq(1).text("下一步");
			if(1 == step){
				$("#guide_index .guide_step_title").html('<span>选择样式</span><img src="images/business/guide/steps/1.png"></img>');
				$("#guide_index .step_button").find("span").eq(0).hide();
				loadPage("page/business/guide/step_template.jsp",{},$contentPage);
// 				loadPageWithCallback("page/business/guide/step_template.jsp",{},function(html){
// 					$contentPage.append(html);
// 				});
			}else if(2 == step){
				$("#guide_index .guide_step_title").html('<span>安装应用</span><img src="images/business/guide/steps/2.png"></img>');
				loadPage("page/business/guide/step_app.jsp",{},$contentPage);
// 				loadPageWithCallback("page/business/guide/step_app.jsp",{},function(html){
// 					$contentPage.append(html);
// 				});
			}else if(3 == step){
				$("#guide_index .guide_step_title").html('<span>配置应用数据</span><img src="images/business/guide/steps/3.png"></img>');
				loadPage("page/business/guide/step_app_data.jsp",{},$contentPage);
// 				loadPageWithCallback("page/business/guide/step_app_data.jsp",{},function(html){
// 					$contentPage.append(html);
// 				});
			}else{
				$("#guide_index .guide_step_title").html('<span>内容发布</span><img src="images/business/guide/steps/4.png"></img>');
				step = 4;
// 				$("#guide_index .step_button").find("span").eq(0).hide();
// 				$("#guide_index .step_button").find("span").eq(0).text("保存");
				$("#guide_index .step_button").find("span").eq(1).text("保存并发布");
				loadPage("page/business/guide/step_publish.jsp",{},$contentPage);
// 				loadPageWithCallback("page/business/guide/step_publish.jsp",{},function(html){
// 					$contentPage.append(html);
// 				});
			}
			$("#guide_index .step_button").show();
			previousStep = currentShowStep;
			currentShowStep = step;
		}
	}
</script>
			