<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	/*修改用户密码*/
	#change_password_form>div{
/* 		width:327px; */
		padding: 2px 95px;
		background: #fff;
		top:220px;
		left: 35%; 
		z-index: 12;
		text-align: right;
		color: #2d343b;
		font-size: 14px;
		font-family: "宋体";
	}
	#change_password_form>div>div{
		padding:20px 0;
	}
	#change_password_form>div>div.actions{
		padding-top:10px;
	}
	#change_password_form>div>div.actions input{
		width: 137px;
		height: 35px;
		background: #2ca1ff;
		border:1px solid #0284e4;
		border-radius: 3px;
		color: #fff;
		font-size: 16px;
		font-weight: bold;
		margin-right: 12px;
		cursor:pointer;
	}
	#change_password_form>div input[type="password"]{
		width: 245px;
		height: 36px;
		border:1px solid #abadb3;
	}
	#change_password_form .resultMsg{
		text-align: center;
		color: red;
		margin-top: 20px;
	}
	/*修改用户密码结束*/
</style>
<form id="change_password_form" action="" method="post">
	<div>
		<div>
			原始密码：<input type="password" value="" name="oldPass" />
		</div>
		<div>
			新密码：<input type="password" value="" name="newPass" />
		</div>
		<div>
			重复密码：<input type="password" value="" name="repeatNewPass" />
		</div>
		<div align="center" class="actions">
			<input type="button" value="确定" />
			<div class="resultMsg"></div>
		</div>
	</div>
</form>
<script type="text/javascript">
	$(function(){
		$("#change_password_form input:button").click(function(){
			$("#change_password_form .resultMsg").text("");
			var oldPass = $("#change_password_form input[name='oldPass']").attr("value");
			var newPass = $("#change_password_form input[name='newPass']").attr("value");
			var repeatNewPass = $("#change_password_form input[name='repeatNewPass']").attr("value");
			
			if(oldPass == "" || newPass == "" || repeatNewPass == ""){
				$("#change_password_form .resultMsg").text("输入框不能为空！");
				return;
			}
			if(newPass != repeatNewPass){
				$("#change_password_form .resultMsg").text("新密码两次输入不同！");
				return;
			}
			$.ajax({
				type: "POST",
				url: "system/user/changePassword",
				data: {oldPass:oldPass,newPass:newPass,repeatNewPass:repeatNewPass},
				success: function(data,status,xhr){
					if("success" == status){
						if(data.msg != ""){
							$("#change_password_form .resultMsg").text(data.msg);
						}else{
							myAlert("密码修改成功!",function(){
								closePop();
							});
							
						}
					}
				}
			});
		});
	});
</script>