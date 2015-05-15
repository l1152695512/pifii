<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style type="text/css">
	#weixin_auth_setting{width: 970px;font-size: 15px;}
	#weixin_auth_setting > img{width: 100%;}
	#weixin_auth_setting .weixin_info > div{margin-bottom: 20px;}
	#weixin_auth_setting .field{width: 300px;display: inline-block;text-align: right;}
	#weixin_auth_setting .weixin_input{position: relative;}
	#weixin_auth_setting .weixin_input>input{width: 200px;height: 25px;line-height: 25px;margin-right: 20px;}
	#weixin_auth_setting .copy_auth_url{display: inline-block;color: rgb(0, 31, 255);}
</style>

<div id="weixin_auth_setting">
	<img src="images/business/weixin/weixin_auth_flow.png"/>
	<div class="weixin_info">
		<div class="weixin_input">
			<span class="field">输入微信公众号：</span>
			<input type="text"/>
			<span class="copy_auth_url">复制地址</span>
		</div>
		<div class="weixin_auth">
			<span class="field">请复制至微信公众平台：</span>
			<span class="auth_url">${baseUrl}</span>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		$("#weixin_auth_setting .weixin_input input").keyup(function(){
			$("#weixin_auth_setting .weixin_auth .auth_url").text("${baseUrl}"+$(this).val());
		});
		$("#weixin_auth_setting .copy_auth_url").zclip({
			path: "js/zclip/ZeroClipboard.swf",
			copy: function(){
				return $("#weixin_auth_setting .weixin_auth .auth_url").text();
			},
			afterCopy:function(){
				if($("#weixin_auth_setting .weixin_input input").val() == ""){
					myAlert("请先填写微信号！");
				}else{
					myAlert("地址已复制到剪切板。");
				}
			}
		});
		$("#weixin_auth_setting .zclip").css({"left": "532px","top": "0px"});//兼容IE7
// 		$("#weixin_auth_setting .copy_auth_url").click(function(){
// 			console.debug(window.clipboardData);
// 		});
	});
</script>