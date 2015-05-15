<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style type="text/css">
	#blacklist_add{width:500px;}
	#blacklist_add .row{padding:0;margin-bottom:25px;}
	#blacklist_add .row>span{float: left;width:100px;height:30px;line-height:30px;color:gray;font-size:15px;font-weight:bold;text-align:right;display:inline-block;}
	#blacklist_add .row>label{margin-right: 40px;margin-left: 20px;line-height: 25px;}
	#blacklist_add .row>input{ border:1px solid #b4c0ce; color:#666;margin-left:20px; width:300px; height:30px; line-height:30px;border-radius:3px;}
	#blacklist_add .row>textarea{ border:1px solid #b4c0ce;color:#666;width:300px;margin-left: 20px; height:60px;border-radius:3px;overflow:hidden;}
	#blacklist_add .row .line_height_60{height:60px;line-height:60px;}
	
	#blacklist_add .content_btn1{margin-top:20px;margin-left:120px;float:left;}
	#blacklist_add .content_btn1 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
	#blacklist_add .content_btn2{float:left;margin-top:20px;margin-left:10px;}
	#blacklist_add .content_btn2 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
</style>
<div id="blacklist_add">
	<div>
		<div class="row">
			<span>类型：</span>
			<label><input checked="checked" type="radio" name="type" value="mac">&nbsp;MAC</label>
			<label><input type="radio" name="type" value="phone">&nbsp;手机号码</label>
		</div>
		<div class="row content">
			<span>MAC地址：</span>
			<input name="tag" type="text" placeholder="XX:XX:XX:XX:XX:XX" ></input>
		</div>
		<div class="row">
			<span class="line_height_60">备注：</span>
			<textarea name="marker" placeholder="1-50字" cols="5" rows="5"></textarea>
		</div>
		<div class="cl"></div>
		<div class="content_btn1">
			<a href="javascript:void(0)" onclick="submitBlacklist();" >
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
		initBlacklistEvent();
	});
	function initBlacklistEvent(){
		$("#blacklist_add .row input[name='type']").change(function(){
			var type= $('#blacklist_add input[name="type"]:checked').first().val();
			if("mac" == type){
				$("#blacklist_add .row.content span").text("MAC地址：");
				$("#blacklist_add .row.content input").attr("placeholder","XX:XX:XX:XX:XX:XX");
			}else if("phone" == type){
				$("#blacklist_add .row.content span").text("手机号码：");
				$("#blacklist_add .row.content input").attr("placeholder","13811111111");
			}
		});
	}
	function submitBlacklist(){
		var type= $('#blacklist_add input[name="type"]:checked').first().val();
		var content = $('#blacklist_add input[name="tag"]').val();
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
		}else if("phone" == type){
			if(content.length != 11){
				myAlert("手机号码填写有误！");
				return;
			}
		}else{
			myAlert("类型选择有误！");
			return;
		}
		var data = {type:type,tag:content,marker:$('#blacklist_add textarea[name="marker"]').val(),shopId:getSelectedShopId()};
		$.ajax({
			type : "POST",
			url : "business/setting/blacklist/save",
			data : data,
			success : function(data) {
				if(undefined != data.error){
					if("repeat" == data.error){
						var tips = "";
						if("mac" == type){
							tips = "MAC地址";
						}else if("phone" == type){
							tips = "手机号码";
						}
						myAlert("该"+tips+"已存在！");
					}
				}else{
					closePop();
					getBlacklistData();
				}
			}
		});
	 }
</script>
