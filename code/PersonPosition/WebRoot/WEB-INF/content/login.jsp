<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>中国电信翼调度人员精定位平台--用户登录</title>
		<style>
body {
	padding: 0;
	margin: 0 auto;
	font-size: 12px;
}

.area {
	position: absolute;
	top: 20%;
	width: 100%;
}

.loginbg {
	
}

.loginarea {
	float: right;
	height: 160px;
	padding-top: 115px;;
	padding-right: 65px;
	display: block;
}

.loginbtn {
	background: url(<c:url value='/images/loginbtnbg.gif'/>);
	width: 67px;
	height: 28px;
	border: none;
	background-repeat: no-repeat;
	color: #FFFFFF;
}

.logintxt {
	width: 150px;
	font-size: 12px;
	line-height: 38px;
	text-align: right;
}

.logintxtarea {
	width: 130px;
	height: 18px;
	border: 1px solid #97b1c0;
}

.logintxtarea2 {
	width: 90px;
	height: 18px;
	border: 1px solid #97b1c0;
}

.copyright {
	padding: 10px 0 0 10px;
	padding: 30px 0 0 10px !important;
}
.otherinfo {
	padding: 5px 0 0  5px;
	padding: 10px 0 0 10px !important;
}
</style>
		<script type="text/javascript">
function onfocus() {
	document.getElementById('username').focus();
}
function loginindex() {
	location.href = "index.jsp";
}
function checkPassword(){
	
	if(document.LoginForm.j_username.value==""){
		alert("用户不能为空");
		return false;
	}
	
	if(document.LoginForm.j_password.value==""){
		alert("密码不能为空");
		return false;
	}
}
</script>
	</head>

	<body onload="onfocus()">
		<div class="area">
			<div class="loginbg" style="background-image: url(<c:url value='/images/loginbg.jpg'/>);width: 955px;height: 361px;background-repeat: no-repeat;margin: 0 auto;clear: both;">
				<c:if test="${not empty param.login_error}">
					<font color="red"> <script type="text/javascript">

</script> </font>
				</c:if>
				<form name="LoginForm"
					action="<c:url value='/j_spring_security_check'/>" method="post" onSubmit="return checkPassword()">
					<table width="100%" cellpadding="0" cellspacing="0">
						<tr>
							<td class="loginarea">
								<table width="270" align="right" cellpadding="0" cellspacing="0">
									<tr>
										<td class="logintxt">
											用&nbsp;户&nbsp;名：
										</td>
										<td>
											<label>
												<input type="text" id="username" name="j_username"
													value="${ACEGI_SECURITY_LAST_USERNAME}"
													
													class="logintxtarea" />
											</label>
										</td>
									</tr>
									<tr>
										<td class="logintxt">
											密&nbsp;&nbsp;&nbsp;&nbsp;码：
										</td>
										<td>
											<label>
												<input type="password" name="j_password" value=""
													class="logintxtarea" />
											</label>
										</td>
									</tr>
									<tr>
										<td>
											&nbsp;
										</td>
										<td>
											<table width="150" cellspacing="0" cellpadding="0" >
												<tr>
													<td>
														<label>
															<input name="submit" type="submit" class="loginbtn"
																value="确定" style="cursor: pointer;" />
														</label>
													</td>
													<td>
														<input name="reset" type="reset" class="loginbtn"
															value="重置" style="cursor: pointer;" />
													</td>
												</tr>
											</table>
										</td>
									</tr>
									<tr><td height="35" colspan="3" align="center"><font color='red'>${msg}</font></td></tr>
								</table>
							</td>
						</tr>
							<tr><td>
								&nbsp;&nbsp;
							</td></tr>
						<tr>
							<td class="copyright">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;版本信息：广州因孚网络科技有限公司 技术支持
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>

	</body>
</html>
