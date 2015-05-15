var checkLogin = false;
//根据QueryString参数名称获取值
//根据传入的name获取对应的value
function getQueryStringByName(name){
	var result = location.search.match(new RegExp("[\?\&]" + name+ "=([^\&]+)","i"));
	if(result == null || result.length < 1){
		return "";
	}
	return result[1];
}
var token = $.cookie("token");//获取cookie中的token
if(getQueryStringByName("token")!=""){//避免浏览器不支持cookie导致无法获取token
	token = getQueryStringByName("token");
}
if(undefined != token && "" != token){
	$.ajax({
		type: "GET", 
		async :true,
		url: 'http://'+window.location.host+'/cgi-bin/luci/api/0/ifidc/timeout_get?token='+token,
		success: function(data,status){
			if(data.timeout == undefined){
				gotoLogin();
			}else{
				if(location.href.indexOf("login.html") != -1){
					gotoIndex(token);
				}
			}
		},
		error: function(jqXHR, textStatus,errorThrown){
			alert("请求失败，稍后请重试！");
		},
		complete: function(){
			$("body").show();
			checkLogin = true;
		}
	});
}else{
	checkLogin = true;
	gotoLogin();
}

function gotoLogin(){
	//判断当前页面是不是login页面，不是则跳转
	if(location.href.indexOf("login.html") == -1){
		window.location.href="login.html";
	}
}
function gotoIndex(token){
	//判断当前页面是不是index页面不是则跳转
	if(location.href.indexOf("index.html") == -1){
		if(undefined != token && "" != token){
			window.location.href="index.html?token="+token;
		}else{
			window.location.href="index.html";
		}
	}
}
