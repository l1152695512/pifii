<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<c:if test='${"weixin"==useAuthType}'>
<div class="bd">
	<ul>
		<li>
			<a href="#"><img src="${pageContext.request.contextPath}/portal/mb/auth/img/weixin.png"/><p>打开微信</p></a>
		</li>
		<li style="margin-top:35px;">
			<a href="#"><img src="${pageContext.request.contextPath}/portal/mb/auth/img/guide.png" style="height:20px;width:20px;"/></a>
		</li>
		<li>
			<a href="#"><img src="${pageContext.request.contextPath}/portal/mb/auth/img/erweima.png"/><p>扫描二维码</p></a>
		</li>
		<li style="margin-top:35px;">
			<a href="#"><img src="${pageContext.request.contextPath}/portal/mb/auth/img/guide.png" style="height:20px;width:20px;"/></a>
		</li>
		<li>
			<a href="#"><img src="${pageContext.request.contextPath}/portal/mb/auth/img/wifi.png"/><p>点击上网</p></a>
		</li>
	</ul>
	<div class="home_footer auth_weixin" style="clear:both;margin-top:10px;">
		<a href="weixin:open">打开微信</a>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function() {
		$(".auth_weixin a").click(function(){
			setTimeout(function(){
				alert("如果微信没打开，请手动打开！");
			},1000);
		});
		
// 		var userAgent = navigator.userAgent.toLowerCase();
// 		if(userAgent.indexOf("iphone os") == -1){
// 			$(".auth_weixin").find("a").attr('href',
// 				"http://weixin.qq.com/cgi-bin/readtemplate?uin=&stype=&fr=m.baidu.com&lang=zh_CN&check=false&t=w_down");
// 		}else{
// 			$(".yijianrenzheng").find("a").click(function() {
// 				setTimeout(function(){
// 					window.location = "https://itunes.apple.com/cn/app/id414478124";
// 				},500);
// 			});
// 		}
	});
</script>
</c:if>