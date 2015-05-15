//当前第一次调用ajax时触发，如果同时有多个ajax请求或者在调用ajax前已有ajax未完成，只有第一次ajax会触发该事件
$( document ).ajaxStart(function() {//用于弹出遮罩层显示加载中
	$.fn.SimpleModal({
		model: 'loading',
		width: 200,
		zindexAdd:100,//加载提示的zindex比普通的弹窗大100
		closeButton: false,
//		hideHeader: false,
		keyEsc:true,
		animate:false
//		hideFooter: false
	}).showModal();
//}).ajaxSend(function( event, jqXHR, ajaxOptions) {//每次ajax调用之前会触发该事件
}).ajaxError(function(event, jqXHR, ajaxSettings, thrownError){
	if(jqXHR.status == 0){
		myAlert("无法访问服务器！");
	}else if(jqXHR.status == 500){
		myAlert("服务器请求出现错误，请联系管理员！");
	}else if(jqXHR.status == 401){
		myAlert("无权限操作！");
	}else{
		myAlert("请求失败，稍后请重试！");
	}
}).ajaxComplete(function(event, jqXHR, ajaxOptions) {//每次ajax调用完成后会触发该事件
	var responseText = jqXHR.responseText;
//	if(jqXHR.readyState != 4 || (jqXHR.status-200 < 0 && jqXHR.status-200 >= 100 )){
//		myAlert("加载失败，稍后请重试！");
//	}else 
	if(isLoginPage(responseText)){//授权失效
		//弹出授权框
		myAlert("登录过时，请重新登录！",function(e){
			var url=encodeURI(encodeURI(cxt + "/loginView"));
			window.location.href=url;
		});
	}
}).ajaxStop(function() {//一批或者一个ajax调用完成后触发一次，用于所有请求完成后隐藏遮罩层
	closePop();
});

if (!Array.prototype.indexOf){
	Array.prototype.indexOf = function(elt /*, from*/)
	{
		var len = this.length >>> 0;
		var from = Number(arguments[1]) || 0;
		from = (from < 0)
		? Math.ceil(from)
				: Math.floor(from);
		if (from < 0)
			from += len;
		for (; from < len; from++)
		{
			if (from in this &&
					this[from] === elt)
				return from;
		}
		return -1;
	};
}
function isLoginPage(responseText){
	if(responseText.indexOf("html") != -1 && 
			responseText.indexOf("this is mark for login page,do not delete it") != -1){
		return true;
	}else{
		return false;
	}
}


//$.ajaxSetup({
//	beforeSend:function(jqXHR, settings){
//	},
//	complete:function(jqXHR, status){
//	},
//	error:function(){
//	},
//	statusCode: {
//	    404: function() {
//	    }
//	}
//});

function loadPage(url,params,$object){
//	$object.load(url,params,function(responseText,textStatus,jqXHR){});
	$.ajax({
		type: "POST",
		dataType: 'html',
		data : params,
		url: url,
		async: false,
		success: function(responseText,textStatus,jqXHR){
			if(!isLoginPage(responseText)){
				$object.html(responseText);
			}
		}
	});
}

function loadPageWithCallback(url,params,callback){
	$.ajax({
		type: "GET",
		dataType: 'html',
		data : params,
		url: url,
		success: function(responseText,textStatus,jqXHR){
			if(!isLoginPage(responseText)){
				callback(responseText);
			}
		}
	});
}
function refreshPhonePagePreview(type){
//	var templateMarker = getShopPageInfo().marker;
//	if("tem" == type || "info" == type || "template1" == templateMarker || "" == templateMarker){
		$("iframe[name='page_preview']").each(function(){
			var thisSrc = $(this).attr("src");
			var index = thisSrc.indexOf("?");
			if(index > 0){
				$(this).attr("src",thisSrc+"&date="+new Date());
			}else{
				$(this).attr("src",thisSrc+"?date="+new Date());
			}
			
		});
//	}
}
function ajaxContent(url, data){
	var shopId = getSelectedShopId();
	$.ajax({
		type : "post",
		url : encodeURI(encodeURI(cxt + url+"?shopId="+shopId)),
		data : data,
		dataType : "html",
		contentType: "application/x-www-form-urlencoded; charset=UTF-8",
		cache: false,
		success:function(responseText,textStatus,jqXHR){
			if(!isLoginPage(responseText)){
				$("#main_contents").html(responseText);
			}
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) { 
			// 这个方法有三个参数：XMLHttpRequest 对象，错误信息，（可能）捕获的错误对象。
			// 通常情况下textStatus和errorThown只有其中一个有值
            // alert(XMLHttpRequest.status);
            // alert(XMLHttpRequest.readyState);
            // alert(textStatus);
//			myAlert("请求出现错误！");
        },
        complete: function(XMLHttpRequest, textStatus) { 
        	// 请求完成后回调函数 (请求成功或失败时均调用)。参数： XMLHttpRequest 对象，成功信息字符串。
            // 调用本次AJAX请求时传递的options参数
			$('#main_contens').fadeIn();
        }
	});
}

function generRandomCharacters(characterLength){
	characterLength = characterLength || 5;
	var chars = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
	var randomCharacters = "";
    for(var i = 0; i < characterLength ; i ++) {
        var index = Math.floor(Math.random()*(chars.length));
        randomCharacters += chars[index];
    }
    return randomCharacters;
}
//点击app展示时标识出该app为点击状态，用于在app详情页面修改app图标或者名称时，能够更新其他地方显示的app
function markerClickApp(appId){
	$(".clickAppMarkerClass").removeClass("clickAppMarkerClass");
	$(".appInfo").parent().each(function(){
		if($(this).data("appId") == appId){
			$(this).addClass("clickAppMarkerClass");
		}
	});
}
function changeAppShowName(name){
	$(".clickAppMarkerClass p").text(name);
	$(".clickAppMarkerClass p").attr("title",name);
}
function changeAppShowIcon(src){
	$(".clickAppMarkerClass img").attr("src",src);
}

function IsURL(str_url){
	return true;
	
//    var strRegex = "^((https|http|ftp|rtsp|mms)?://)"
//    + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@
//    + "(([0-9]{1,3}\.){3}[0-9]{1,3}" // IP形式的URL- 199.194.52.184
//    + "|" // 允许IP和DOMAIN（域名）
//    + "([0-9a-z_!~*'()-]+\.)*" // 域名- www.
//    + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\." // 二级域名
//    + "[a-z]{2,6})" // first level domain- .com or .museum
//    + "(:[0-9]{1,4})?" // 端口- :80
//    + "((/?)|" // a slash isn't required if there is no file name
//    + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
//    var re=new RegExp(strRegex);
//    //re.test()
//    if (re.test(str_url)){
//        return (true);
//    }else{
//        return (false);
//    }
}