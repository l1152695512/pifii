var serverPath = "http://auth.pifii.com:8090/pifii";
var serverUrl = serverPath+"/advServlet?1=1";//手机端及PC端都要使用

//格式化CST日期的字串
function formatCSTDate(strDate,format){
	return formatDate(new Date(strDate),format);
}
//格式化日期,
function formatDate(date,format){
	var paddNum = function(num){
	  num += "";
	  return num.replace(/^(\d)$/,"0$1");
	}
	//指定格式字符
	var cfg = {
	   yyyy : date.getFullYear() //年 : 4位
	  ,yy : date.getFullYear().toString().substring(2)//年 : 2位
	  ,M  : date.getMonth() + 1  //月 : 如果1位的时候不补0
	  ,MM : paddNum(date.getMonth() + 1) //月 : 如果1位的时候补0
	  ,d  : date.getDate()   //日 : 如果1位的时候不补0
	  ,dd : paddNum(date.getDate())//日 : 如果1位的时候补0
	  ,hh : date.getHours()  //时
	  ,mm : date.getMinutes() //分
	  ,ss : date.getSeconds() //秒
	}
	format || (format = "yyyy-MM-dd hh:mm:ss");
	return format.replace(/([a-z])(\1)*/ig,function(m){return cfg[m];});
}
//根据QueryString参数名称获取值
//根据传入的name获取对应的value
function getQueryStringByName(name){
	var result = location.search.match(new RegExp("[\?\&]" + name+ "=([^\&]+)","i"));
	if(result == null || result.length < 1){
		return "";
	}
	return result[1];
}
var routersn = getQueryStringByName("routersn");
var mac = getQueryStringByName("mac");
function initQueryParams(){//如果cookie中有信息则使用cookie中的
	var cookieRoutersn = $.cookie("routersn");
	if(undefined != cookieRoutersn && "" != cookieRoutersn){
		routersn = cookieRoutersn;
	}
	
	var cookieMac = $.cookie("mac");
	if(undefined != cookieMac && "" != cookieMac){
		mac = cookieMac;
	}
}
initQueryParams();

function insertClickData(){
	var thisHref = window.location.href;
	var rid = getQueryStringByName("rid");
	var regExp = new RegExp("mb/\\w+/\\w+\.html");
	if(thisHref.indexOf("mb/index.html") || regExp.test(thisHref)){
		try{
			$.getJSON("/cgi-bin/luci/api/0/pifiibox/logIn?jsonpCallback=?&mac="+mac+"&rid="+rid,function(data){});
		}catch(e){
		}
	}
}

function checkFileExist(filePath,callback){
	$.ajax({
		type: "GET",
		//async :false,
		url: filePath,
		complete: function(xhr,status){
			if(xhr.status == 404){//文件不存在
				callback(false);
			}else{
				callback(true);
			}
		}
	});
}

function changHref(){
	try{
		$("a").each(function(){
			var $this = $(this);
			var href = $this.attr("href");
			if(href.indexOf("http://") == -1){
				checkFileExist(href,function(exist){
					if(!exist){
						$this.attr("href","javascript:void(0);");
						$this.click(function(){
							 window.location.href=serverUrl+"&cmd=notFound&mac="+mac+"&routersn="+routersn;
							//alert("访问的页面不存在,页面正在更新中！");
						});
					}
				});
			}
		});
	}catch(e){
	}
}

function readXML(filePath,callback){
	//var $xmlData;
	$.ajax({
		type: "GET",
		async :false,
		dataType: 'xml',
		url: filePath,
		success: function(data,status,xhr){
			callback(data);
			//$xmlData = $(data);
			//console.debug($(data).find("template").children("path").text());
		},
		error: function(xhr,status,error){
			//alert("加载配置文件失败！");
		}
	});
	//return $xmlData;
}
