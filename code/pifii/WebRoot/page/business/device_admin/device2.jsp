<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
#device2 .hd {
	width: 765px;
	height: 152px;
	padding: 45px 0 20px 0;
	color: #2d333a;
	font-size: 14px;
	font-family: "宋体";
	margin-bottom: 25px;
}

#device2 .hd .companyInfo {
	margin-bottom: 20px;
}

#device2 .hd .companyInfo .city {
	height: 35px;
	width: 176px;
	margin-right: 10px;
	text-align: right;
}

#device2 .hd .companyInfo select {
	outline: none;
	width: 118px;
	height: 28px;
	border: 1px solid #b4beca;
	color: #888888;
}

#device2 .hd .companyInfo .companyName {
	width: 267px;
	height: 35px;
	margin-right: 15px;
}

#device2 .hd .companyInfo .companyName input {
	outline: none;
	width: 183px;
	height: 28px;
	border: 1px solid #b4beca;
	color: #888888;
	font-size: 14px;
	text-indent: 3px;
}

#device2 .hd .companyInfo .adress input {
	outline: none;
	border: 1px solid #b4beca;
	width: 250px;
	height: 28px;
	color: #888888;
} /*.companyInfo 公司信息结束*/
#device2 .hd .deviceInfo {
	margin-bottom: 26px;
}

#device2 .hd .deviceInfo .deviceGroup {
	width: 276px;
	height: 35px;
	margin-right: 10px;
}

#device2 .hd .deviceInfo .deviceGroup select {
	width: 212px;
	height: 28px;
	outline: none;
	border: 1px solid #b4beca;
	color: #888888;
}

#device2 .hd .deviceInfo .deviceNumb {
	margin-right: 10px;
}

#device2 .hd .deviceInfo .deviceNumb input {
	width: 163px;
	height: 28px;
	border: 1px solid #b4beca;
	outline: none;
	color: #888888;
}

#device2 .hd .deviceInfo .deviceState select {
	width: 132px;
	height: 28px;
	border: 1px solid #b4beca;
	color: #888888;
	outline: none;
}

#device2 .hd .query {
	width: 116px;
	height: 25px;
	margin: 0 auto;
}

#device2 .hd .query input {
	width: 115px;
	height: 35px;
	border-radius: 5px;
	background: #14a1f9;
	border: 0;
	outline: none;
	color: #fff;
	font-weight: bold;
	text-align: center;
	font-size: 14px;
	cursor: pointer;
} /*deviceInfo end*/
#device2 .bd {
	font-family: "宋体";
	font-size: 14px;
}

#device2 .bd .deviceLeftNav {
	width: 174px;
	height: 520px;
	border-right: 1px solid #c0c4d1;
	margin-right: 15px;
}

#device2 .bd .deviceLeftNav .group {
	margin-bottom: 23px;
}

#device2 .bd .deviceLeftNav ul li {
	width: 166px;
	height: 30px;
	color: #2d333a;
}

#device2 .bd .group .deviceTitle {
	width: 165px;
	border-left: 3px solid #45bf1a;
	height: 38px;
	background: #c2e4b5;
	text-indent: 3px;
	line-height: 38px;
	color: #288607;
	font-size: 16px;
	margin-bottom: 15px;
	position: relative;
}

#device2 .bd .group:nth-child(2) .deviceTitle {
	border-left: 3px solid #ee5364;
	height: 38px;
	background: #f5c9ce;
	color: #ee5364;
	d8cceb
}

#device2 .bd .group:nth-child(3) .deviceTitle {
	border-left: 3px solid #a071eb;
	height: 38px;
	background: #d8cceb;
	color: #7144b8;
}

#device2 .bd .group .deviceTitle span:nth-child(1) {
	width: 80px;
	display: block;
	float: left;
}

#device2 .bd .group .deviceTitle span+span {
	width: 20px;
	height: 20px;
	display: block;
	float: left;
	position: absolute;
	right: 0;
	cursor: pointer;
}

#device2 .bd .group .deviceTitle span:nth-child(2) {
	right: 48px;
	top: 15px;
	background: url(images/business/small.png) no-repeat -34px 2px;
}

#device2 .bd .group .deviceTitle span:nth-child(3) {
	right: 25px;
	top: 15px;
	background: url(images/business/small.png) no-repeat -34px -26px;
}

#device2 .bd .group .deviceTitle span:nth-child(4) {
	right: 3px;
	top: 15px;
	background: url(images/business/small.png) no-repeat -34px -52px;
}
/*设备信息的bd 的左边导航部分end*/
#device2 .bd .deviceInfo .online {
	margin-bottom: 30px;
}

#device2 .bd .deviceInfo .online .deviceInfoHd {
	width: 585px;
	height: 37px;
	border-bottom: 3px solid #dae0e7;
	position: relative;
}

#device2 .bd .deviceInfo .outline .deviceInfoHd {
	width: 585px;
	height: 37px;
	border-bottom: 3px solid #dae0e7;
	position: relative;
}

#device2 .bd .deviceInfo .online .deviceInfoHd h4 {
	width: 90px;
	height: 38px;
	text-indent: 3em;
	background: url(images/business/nav.png) no-repeat -24px -1066px;
	font-weight: normal;
	color: #183356;
}

#device2 .bd .deviceInfo .outline .deviceInfoHd h4 {
	width: 90px;
	height: 38px;
	text-indent: 3em;
	background: url(images/business/nav.png) no-repeat -24px -1112px;
	font-weight: normal;
	color: #69788b;
} /*脱网状态*/
#device2 .bd .deviceInfo .online .deviceInfoHd span {
	display: block;
	float: left;
	width: 30px;
	height: 24px;
	line-height: 24px;
	overflow: hidden;
	position: absolute;
	cursor: pointer;
	bottom: 2px;
	font-size: 14px;
	color: #657b95;
}

#device2 .bd .deviceInfo .outline .deviceInfoHd span {
	display: block;
	float: left;
	width: 30px;
	height: 24px;
	line-height: 24px;
	overflow: hidden;
	position: absolute;
	cursor: pointer;
	bottom: 2px;
	font-size: 14px;
	color: #657b95;
}

#device2 .bd .deviceInfo .online .deviceInfoHd span:nth-child(2) {
	right: 77px;
}

#device2 .bd .deviceInfo .outline .deviceInfoHd span:nth-child(2) {
	right: 77px;
}

#device2 .bd .deviceInfo .online .deviceInfoHd span:nth-child(3) {
	right: 38px;
}

#device2 .bd .deviceInfo .outline .deviceInfoHd span:nth-child(3) {
	right: 38px;
}

#device2 .bd .deviceInfo .online .deviceInfoHd span:nth-child(4) {
	right: 0;
}

#device2 .bd .deviceInfo .outline .deviceInfoHd span:nth-child(4) {
	right: 0;
}

#device2 .bd .deviceInfo .online .deviceInfoBd {
	width: 585px;
	height: 44px;
	border-bottom: 1px solid #c0c4d1;
	color: #4a4e53;
	font-size: 14px;
}

#device2 .bd .deviceInfo .outline .deviceInfoBd {
	width: 585px;
	height: 44px;
	border-bottom: 1px solid #c0c4d1;
	color: #4a4e53;
	font-size: 14px;
}

#device2 .bd .deviceInfo .online .deviceInfoBd ul li {
	float: left;
	width: 117px;
	height: 44px;
	text-align: center;
	line-height: 45px;
}

#device2 .bd .deviceInfo .outline .deviceInfoBd ul li {
	float: left;
	width: 146px;
	height: 44px;
	text-align: center;
	line-height: 45px;
}

#device2 .bd .deviceInfo .online .deviceInfoBd ul li:last-child {
	text-align: right;
}

#device2 .bd .deviceInfo .outline .deviceInfoBd ul li:last-child {
	text-align: right;
}

#device2 .bd .deviceInfo .online .deviceInfoFt {
	padding: 10px 0;
	color: #4a4e53;
	border-bottom: 3px solid #dae0e7;;
}

#device2 .bd .deviceInfo .outline .deviceInfoFt {
	padding: 10px 0;
	color: #4a4e53;
	border-bottom: 3px solid #dae0e7;;
}

#device2 .bd .deviceInfo .online .deviceInfoFt ul:nth-child(1) li {
	width: 140px;
	height: 34px;
	line-height: 34px;
	font-size: 12px;
	overflow: hidden;
}

#device2 .bd .deviceInfo .outline .deviceInfoFt ul:nth-child(1) li {
	width: 160px;
	height: 34px;
	line-height: 34px;
	font-size: 12px;
	overflow: hidden;
}

#device2 .bd .deviceInfo .online .deviceInfoFt ul:nth-child(2) li {
	width: 113px;
	height: 34px;
	line-height: 34px;
	text-indent: 2em;
}

#device2 .bd .deviceInfo .outline .deviceInfoFt ul:nth-child(2) li {
	width: 130px;
	height: 34px;
	line-height: 34px;
	text-align: center;
}

#device2 .bd .deviceInfo .online .deviceInfoFt ul:nth-child(3) li {
	width: 92px;
	height: 34px;
	line-height: 34px;
	text-align: center;
}

#device2 .bd .deviceInfo .outline .deviceInfoFt ul:nth-child(3) li {
	width: 165px;
	height: 34px;
	line-height: 34px;
	text-align: center;
}

#device2 .bd .deviceInfo .online .deviceInfoFt ul:nth-child(4) li {
	width: 120px;
	height: 34px;
	line-height: 34px;
	text-align: center;
}

#device2 .bd .deviceInfo .outline .deviceInfoFt ul:nth-child(4) li {
	width: 120px;
	height: 34px;
	line-height: 34px;
	text-align: right;
}

#device2 .bd .deviceInfo .online .deviceInfoFt ul:nth-child(5) li {
	width: 114px;
	height: 34px;
	line-height: 34px;
	text-align: right;
}
</style>

<script type="text/javascript" src="js/icheck.js"></script>
<!--复选按钮的js-->
<link rel="stylesheet" type="text/css"
	href="css/business/minimal/green.css" />
<link rel="stylesheet" type="text/css"
	href="css/business/minimal/pink.css" />
<link rel="stylesheet" type="text/css"
	href="css/business/minimal/purple.css" />
<link rel="stylesheet" type="text/css"
	href="css/business/minimal/blue.css" />
<div id="device2">
	<div class="hd">
		<div class="companyInfo">
			<div class="city fl">
				地方: <select>
					<option>惠州</option>
					<option>广州</option>
					<option>长沙</option>
				</select>
			</div>
			<div class="companyName fl">
				公司/店名：<input type="text" />
			</div>
			<div class="adress fr">
				地址：<input type="text" />
			</div>
			<div class="cl"></div>
		</div>
		<!--公司信息end-->
		<div class="deviceInfo">
			<div class="deviceGroup fl">
				设备组： <select>
					<option>云南移动大楼</option>
					<option>广州</option>
					<option>长沙</option>
				</select>
			</div>
			<div class="deviceNumb fl">
				设备编号：<input type="text" />
			</div>
			<div class="deviceState fr">
				鉴权状态： <select>
					<option>直通</option>
					<option>禁通</option>
					<option>鉴权</option>
				</select>
			</div>
			<div class="cl"></div>
		</div>
		<div class="query">
			<input type="submit" value="查询" />
		</div>
	</div>
	<div class="bd">
		<div class="deviceLeftNav fl">
			<div class="group">
				<div class="deviceTitle">云南移动</div>
				<ul>
					<li><input type="checkbox" checked />&nbsp;办公大楼1楼</li>
					<li><input type="checkbox" />&nbsp;办公大楼2楼</li>
				</ul>
			</div>

			<div class="group">
				<div class="deviceTitle">
					<span>wifi名</span><span></span><span></span><span></span>
				</div>
				<ul>
					<li><input type="checkbox" checked>&nbsp;云南移动办公大楼1楼</li>
					<li><input type="checkbox">&nbsp;云南移动办公大楼2楼</li>
				</ul>
			</div>

			<div class="group">
				<div class="deviceTitle">
					<span>设备组</span><span></span><span></span><span></span>
				</div>
				<ul>
					<li><input type="checkbox">&nbsp;未分组</li>
					<li><input type="checkbox" checked>&nbsp;云南移动办公大楼1楼</li>
					<li><input type="checkbox">&nbsp;云南移动办公大楼2楼</li>
				</ul>
			</div>
		</div>
		<div id="device_info" class="deviceInfo fr">
		</div>
		<div class="cl"></div>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$("#device_info").show(function(){
 				var shopId = getSelectedShopId();
				$.ajax({
		            type: "POST",
		            url: 'business/device/loadList?shopId='+shopId+'&type=1',
		            success: function(data) {
		            	var divshow = $("#app_info");
		            	 divshow.html(data); // 添加Html内容，不能用Text 或 Val
		            }
		        });
		});
		});
		$('.deviceLeftNav').find(".group").eq(0).find("input").iCheck({
			checkboxClass : 'icheckbox_minimal-green',
			increaseArea : '20%' // optional
		});

		$('.deviceInfoFt').find("input").iCheck({
			radioClass : 'iradio_minimal-blue',
			increaseArea : '20%' // optional
		});

		$('.deviceLeftNav').find(".group").eq(1).find("input").iCheck({
			checkboxClass : 'icheckbox_minimal-pink',
			increaseArea : '20%' // optional
		});
		$('.deviceLeftNav').find(".group").eq(2).find("input").iCheck({
			checkboxClass : 'icheckbox_minimal-purple',
			increaseArea : '20%' // optional
		});
		
	});
</script>
