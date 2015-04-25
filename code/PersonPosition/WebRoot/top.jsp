<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>无标题文档</title>
		<link href="css/style.css" rel="stylesheet" type="text/css" />
		<script language="JavaScript">
//定义一个tick函数，以获取系统的时间
function tick() {
	var day = "";
	var month = "";
	var ampm = "";
	var ampmhour = "";
	var myweekday = "";
	var year = "";
	mydate = new Date();
	myweekday = mydate.getDay();
	mymonth = mydate.getMonth() + 1;
	myday = mydate.getDate();
	myyear = mydate.getYear();
	year = (myyear > 200) ? myyear : 1900 + myyear;
	if (myweekday == 0)
		weekday = " 星期日 ";
	else if (myweekday == 1)
		weekday = " 星期一 ";
	else if (myweekday == 2)
		weekday = " 星期二 ";
	else if (myweekday == 3)
		weekday = " 星期三 ";
	else if (myweekday == 4)
		weekday = " 星期四 ";
	else if (myweekday == 5)
		weekday = " 星期五 ";
	else if (myweekday == 6)
		weekday = " 星期六 ";

	var datea = year + "年" + mymonth + "月" + myday + "日 " + weekday;

	var year, month, day, hours, minutes, seconds, ap;
	var intYear, intMonth, intDay, intHours, intMinutes, intSeconds;
	var today;
	today = new Date();
	intYear = today.getYear();
	intMonth = today.getMonth() + 1;
	intDay = today.getDate();
	intHours = today.getHours();
	intMinutes = today.getMinutes();
	intSeconds = today.getSeconds();
	//获取系统时间的小时数
	if (intHours == 0) {
		hours = intHours + ":";
		ap = "凌晨";
	} else if (intHours < 12) {
		hours = intHours + ":";
		ap = "早晨";
	} else if (intHours == 12) {
		hours = intHours + ":";
		ap = "中午";
	} else {
		intHours = intHours - 12;
		hours = intHours + ":";
		ap = "下午";
	}
	//获取系统时间的分数
	if (intMinutes < 10) {
		minutes = "0" + intMinutes + ":";
	} else
		minutes = intMinutes + ":";
	//获取系统时间的秒数
	if (intSeconds < 10)
		seconds = "0" + intSeconds + " ";
	else
		seconds = intSeconds + " ";
	timeString = hours + minutes + seconds + ap;
	var username = document.getElementById('name').value;
	Clock.innerHTML = username + "，欢迎光临；当前日期：" + datea + timeString;
	//每隔0.1秒钟执行一次tick函数
	window.setTimeout("tick()", 100);
}
window.onload = tick;
</script>
	</head>

	<body>
		<table width="100%" cellspacing="0" cellpadding="0">
			<tr>
				<td class="top_bg">
					<table width="100%" cellspacing="0" cellpadding="0">
						<tr>
							<td class="top_banner">
								<div style="float: right; padding-right: 10px;">
									
									
										<a href="content_index.jsp" target="mainFrame"><img
												src="images/house_32x32.png" border="0" alt="主页"
												style="cursor: pointer;" />
										</a>&nbsp;&nbsp;
				 
									<a
										href="modules/userGroupManage/userGroupList.jsp?username=${ACEGI_SECURITY_LAST_USERNAME}"
										target="mainFrame"><img src="images/userGroup.png"
											border="0" alt="群组管理" style="cursor: pointer;" />
									</a>&nbsp;&nbsp;
									<a
										href="modifyPassword.jsp?username=${ACEGI_SECURITY_LAST_USERNAME}"
										target="mainFrame"><img src="images/password.png"
											border="0" alt="修改密码" style="cursor: pointer;" />
									</a>&nbsp;&nbsp;
									<a href="j_acegi_security_logout" target="_top"><img
											src="images/btn_hd_exit.gif" border="0" alt="退出"
											style="cursor: pointer;" />
									</a>&nbsp;&nbsp;
								</div>
							</td>
						</tr>
					</table>

				</td>
			</tr>
		</table>
		<table width="100%" cellspacing="0" cellpadding="0">
			<input type="hidden" name="userName" id="name"
				value="管理员" />
			<tr>
				<td class="login_message_bg">
					<table width="100%" cellspacing="0" cellpadding="0">
						<tr>
							<td>
								<div id="Clock"></div>
							</td>
							<td>
								总访问量：&nbsp;&nbsp;&nbsp;&nbsp;今日访问：</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</body>
</html>
