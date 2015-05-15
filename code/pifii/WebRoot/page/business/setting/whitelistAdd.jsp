<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style type="text/css">
	#whitelist_add{width:500px;}
	#whitelist_add .row{padding:0;margin-bottom:20px;}
	#whitelist_add .row>span{float: left;width:100px;height:30px;line-height:30px;color:gray;font-size:15px;font-weight:bold;text-align:right;display:inline-block;}
	#whitelist_add .row>label{margin-right: 40px;margin-left: 20px;line-height: 25px;}
	#whitelist_add .row>input{ border:1px solid #b4c0ce; color:#666;margin-left:20px; width:300px; height:30px; line-height:30px;border-radius:3px;}
	#whitelist_add .row>textarea{ border:1px solid #b4c0ce;color:#666;width:300px;margin-left: 20px; height:60px;border-radius:3px;overflow:hidden;}
	#whitelist_add .row .line_height_60{height:60px;line-height:60px;}
	#whitelist_add .row p{margin-left: 120px;margin-top: 10px;color: red;}
	
	#whitelist_add .content_btn1{margin-top:20px;margin-left:120px;float:left;}
	#whitelist_add .content_btn1 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
	#whitelist_add .content_btn2{float:left;margin-top:20px;margin-left:10px;}
	#whitelist_add .content_btn2 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
</style>
<div id="whitelist_add">
	<div>
		<div class="row">
			<span>类型：</span>
			<label><input checked="checked" type="radio" name="type" value="mac">&nbsp;MAC</label>
			<label><input type="radio" name="type" value="domain">&nbsp;域名</label>
			<label><input type="radio" name="type" value="ip">&nbsp;IP</label>
		</div>
		<div class="row content">
			<span>MAC地址：</span>
			<input name="content" type="text" placeholder="38:bc:1a:00:6b:f1" ></input>
			<p>示例：38:bc:1a:00:6b:f1</p>
		</div>
		<div class="row">
			<span class="line_height_60">备注：</span>
			<textarea name="marker" placeholder="1-50字" cols="5" rows="5"></textarea>
		</div>
		<div class="cl"></div>
		<div class="content_btn1">
			<a href="javascript:void(0)" onclick="submitPage();" >
				<span>确定</span>
			</a>
		</div>
		<div class="content_btn2">
			<a href="javascript:void(0)" onclick="closePop();">
				<span>取消</span>
			</a>
		</div>
	</div>
</div>
<script type="text/javascript">
	$(function() {
		initWhitelistEvent();
	});
	function initWhitelistEvent(){
		$("#whitelist_add .row input[name='type']").change(function(){
			var type= $('#whitelist_add input[name="type"]:checked').first().val();
			if("mac" == type){
				$("#whitelist_add .row.content span").text("MAC地址：");
				$("#whitelist_add .row.content input").attr("placeholder","38:bc:1a:00:6b:f1");
				$("#whitelist_add .row.content p").text("示例：38:bc:1a:00:6b:f1");
			}else if("domain" == type){
				$("#whitelist_add .row.content span").text("域名：");
				$("#whitelist_add .row.content input").attr("placeholder","www.pifii.com");
				$("#whitelist_add .row.content p").text("示例：www.pifii.com");
			}else if("ip" == type){
				$("#whitelist_add .row.content span").text("IP：");
				$("#whitelist_add .row.content input").attr("placeholder","192.168.10.1");
				$("#whitelist_add .row.content p").text("示例：192.168.10.1");
			}
		});
	}
	function submitPage(){
		var type= $('#whitelist_add input[name="type"]:checked').first().val();
		var content = $('#whitelist_add input[name="content"]').val();
		if("mac" == type){
			if(content.length != 17){
				myAlert("MAC地址填写有误(正确格式：38:bc:1a:00:6b:f1)！");
				return;
			}
			var macSplit = content.split(":");
			if(macSplit.length != 6){
				myAlert("MAC地址填写有误(正确格式：38:bc:1a:00:6b:f1)！");
				return;
			}
			for(var i=0;i<macSplit.length;i++){
				if(macSplit[i].length != 2){
					myAlert("MAC地址填写有误(正确格式：38:bc:1a:00:6b:f1)！");
					return;
				}
			}
		}else if("domain" == type){
			if(content.toLowerCase().indexOf("http:") == 0){
				myAlert("域名不能以http:开头！");
				return;
			}
			if(content.indexOf("/") != -1){
				myAlert("域名不能包含 / ！");
				return;
			}
			if(content.length < 5){
				myAlert("域名的长度必须大于5！");
				return;
			}
			if(content.substr(-4) != ".com"){
				myAlert("域名必须以 .com 结尾！");
				return;
			}
			if(content.length > 50){
				myAlert("域名的长度不能大于50个字符！");
				return;
			}
		}else if("ip" == type){
			var ipSplit = content.split(".");
			if(ipSplit.length != 4){
				myAlert("IP地址填写有误(正确格式：192.168.10.1)！");
				return;
			}
			for(var i=0;i<ipSplit.length;i++){
				var ipInt = parseInt(ipSplit[i]);
				if(ipInt<=0 || ipInt>255 || isNaN(ipInt) || ipInt.toString().length!=ipSplit[i].length){
					myAlert("IP地址填写有误(正确格式：192.168.10.1)！");
					return;
				}
			}
		}else{
			myAlert("类型选择有误！");
			return;
		}
		var data = {type:type,content:content,marker:$('#whitelist_add textarea[name="marker"]').val(),shopId:getSelectedShopId()};
		$.ajax({
			type : "POST",
			url : "business/setting/saveWhitelist",
			data : data,
			success : function(data) {
				if(undefined != data.error){
					if("repeat" == data.error){
						var tips = "";
						if("mac" == type){
							tips = "MAC地址";
						}else if("domain" == type){
							tips = "域名";
						}else if("ip" == type){
							tips = "IP";
						}
						myAlert("该"+tips+"已存在！");
					}else if("notAccess" == data.error){
						var tips = "";
						if("ip" == type){
							tips = "IP";
						}else if("domain" == type){
							tips = "域名";
						}
						myAlert("该"+tips+"禁止添加！");
					}
				}else{
					closePop();
					getWhitelistData();
				}
			}
		});
	 }
</script>
