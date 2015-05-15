<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0,user-scalable=no">
		<meta name="MobileOptimized" content="320">
		<title>问卷调查</title>
		<script src="js/jquery-1.8.3.min.js"></script>
		<style>
			*{padding: 0; margin: 0;}
			img{border:0;}
			.fl{float:left;}
			.fr{float: right;}
			body{
			    font-family:"微软雅黑","Helvetica","Arial";
			    font-size:16px;
			    letter-spacing: 1px;
/* 				background-image:-webkit-linear-gradient(to bottom,#fff,#999); */
/* 				background-image:-moz-linear-gradient(to bottom,#fff,#999); */
/* 				background-image:linear-gradient(to bottom,#fff,#999); */
			}
			.cl{clear:both;}
			.hello{width:90%;margin:0 auto;margin-top:15px;padding:5px;margin-bottom:3%;font-size:14px;color:gray;background:#F7F7F7;border: 1px dashed #CCC;}
			.listStyle{border-bottom:1px solid #e3ebeb;padding:5px; width: 90%;margin: 0 auto;margin-bottom: 20px;}
			.listStyle label{display: block;}
			.selected{background-color:#dff7ff;}
			#box1{width: 100%;}
			label{font-size:15px;color:#666;margin-left: 20px;}
			.sub{width: 90%; height: 50px;margin: 0 auto;margin-bottom: 10%;}
			.sub input{
			    width: 100%;
			    height: 100%;
			    background:#0da9e6;
			    border-radius: 3px;
			    border: 0;
			    outline: none;
			    text-align: center;
			    line-height: 100%;
			    color: #fff;
			    font-size: 20px;
			    font-weight: bold;
			    cursor: pointer;
			}
			.banquan{
			    width: 100%;
			    height: 25px;
			    line-height:25px;
			    padding-top:1px;
			    text-align: center;
			    font-size:14px;
			    color: #666;
/* 			    background: #ccc; */
/* 			    position:fixed; */
/* 			    bottom:0; */
			}
			.banquan a:link{text-decoration: none;color: #666;}
			.banquan a:hover,.banquan a:visited{text-decoration: underline;}
		</style>
	</head>
	<body>
		<h3 class="hello">您好！感谢您百忙之中填写问卷，我们将为您所填的一切信息保密，请放心填写。感谢您对我们的关注与支持！</h3>
		<div id="box1">
			<form>
<!-- 				<div class="listStyle selected"> -->
<!-- 					<p>1.菜品种类及合胃口程度.</p> -->
<!-- 						<input type="radio" checked="checked" name="taste" value="1" id="vg"><label for="vg">&nbsp;很好</label>&nbsp; -->
<!-- 						<input type="radio"  name="taste" value="2" id="g"><label for="g">&nbsp;好</label>&nbsp; -->
<!-- 						<input type="radio"  name="taste" value="3" id="general"><label for="general">&nbsp;一般</label>&nbsp; -->
<!-- 						<input type="radio"  name="taste" value="4" id="bad"><label for="bad">&nbsp;差</label>&nbsp; -->
<!-- 				</div> -->
			</form>
		    <div class="sub">
		        <input type="button" onclick="submit();" value="提交">
		    </div>
		    <div class="banquan">
		        <p>Copyright &copy;派路由&nbsp;&nbsp;
		        	<a href="http://www.pifii.com">www.pifii.com</a>
		        </p>
		    </div>
		</div>
		<script type="text/javascript">
			var questions = eval('${questions}');
			var currentQuestionId = "";
			var questionNum = 0;
			var questionsHtml = "";
			for(var i=0;i<questions.length;i++){
				if(currentQuestionId != questions[i].survey_id){
					if(currentQuestionId != ""){
						questionsHtml += '</div>';
					}
					questionNum++;
					currentQuestionId = questions[i].survey_id;
					questionsHtml += '<div class="listStyle">' +
										'<p>'+questionNum+'.'+questions[i].question+'.</p>';
				}
				var type="radio";
				if(questions[i].type=='2'){
					type="checkbox";
				}
				questionsHtml += '<label><input type="'+type+'" name="'+questions[i].survey_id+'" value="'+questions[i].option_id+'">&nbsp;&nbsp;&nbsp;&nbsp;'+questions[i].option+'</label>';
			}
			if(currentQuestionId != ''){
				questionsHtml += '</div>';
			}
			$("form").html(questionsHtml);
			$("form>div.listStyle").each(function(){
				$(this).find("input:first").attr("checked","checked");
			});
			$("form>div").click(function(){
				$(this).addClass("selected").siblings(".listStyle").removeClass("selected");
			});
			function submit(){
				$.ajax({
					type : "POST",
					url : 'advServlet',
					async:true,
					data : {cmd:'survey_save',options:$("form").serialize()},
					success : function(data) {
						var system ={  
								win : false,  
								mac : false,  
								xll : false  
							};  
						var p = navigator.platform;    
						system.win = p.indexOf("Win") == 0;  
						system.mac = p.indexOf("Mac") == 0;  
						system.x11 = (p == "X11") || (p.indexOf("Linux") == 0);  
						if(system.win||system.mac||system.xll){
							$("body").html("<h1 style='color: rgb(124, 156, 255);text-align: center;margin-top: 20%;'>多谢您的参与。</h1>");
							//window.opener=null;
							//window.open('','_self');
							//window.close();
						}else{
							alert("多谢您的参与。");
							window.location.href=data.main_page;
						}
					}
				});
			}
		</script>
	</body>
</html>