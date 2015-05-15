<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Cache-Control" content="no-cache">
		<meta http-equiv="Cache-Control" content="no-store, must-revalidate"> 
		<meta http-equiv="expires" content="Wed, 26 Feb 1997 08:21:57 GMT"> 
		<meta http-equiv="expires" content="0"> 
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0,user-scalable=no">
		<meta name="MobileOptimized" content="320">
		<title>反馈</title>
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
			#box2{ margin-top:20px;}
			.wenzi{width: 90%;margin: 0 auto;color: #0f0f0f;font-size: 16px;margin-bottom: 3%;}
			.jianyi{width: 90%;height: 40%;border-radius: 5px;border:1px solid #7e7e7e;margin: 0 auto;padding:1%; margin-bottom: 10%;}
			textarea{width:98%; border: 0;outline: none; color: #7e7e7e;}
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
	<div id="box2"><!--反馈信息开始-->
        <p class="wenzi">
            欢迎您提出宝贵意见和建议，您留下的每个字都将改善我们的服务.
        </p>
        <div class="jianyi">
        	<form>
	        	<textarea name="des" placeholder="请输入您的反馈意见(字数5-500)" cols="5" rows="8">${des}</textarea>
        	</form>
        </div>
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
		function submit(){
			var opinion = $("textarea").val();
			if(opinion.length>4 && opinion.length<501){
				$.ajax({
					type : "POST",
					url : 'advServlet',
					async:true,
					data : {cmd:'feedback_save',opinion:opinion},
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
							$("body").html("<h1 style='color: rgb(124, 156, 255);text-align: center;margin-top: 20%;'>多谢您的意见。</h1>");
							//window.opener=null;
							//window.open('','_self');
							//window.close();
						}else{
							alert("多谢您的意见。");
							window.location.href=data.main_page;
						}
					}
				});
			}else{
				alert("字数必须在5到500之间！");
			}
		}
	</script>
	</body>
</html>