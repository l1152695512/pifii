var serverUrl = "http://113.106.98.60:8080/ttopyd/advServlet?deviceId=1";//deviceId���ݾ�����豸������
//var serverUrl = "http://192.168.10.188:8080/ttopyd/advServlet?deviceId=1";
//var serverUrl = "http://58.67.196.187:8083/ttopyd/advServlet?deviceId=1";
//��ʽ��CST���ڵ��ִ�
function formatCSTDate(strDate,format){
	return formatDate(new Date(strDate),format);
}
//��ʽ������,
function formatDate(date,format){
	var paddNum = function(num){
	  num += "";
	  return num.replace(/^(\d)$/,"0$1");
	}
	//ָ����ʽ�ַ�
	var cfg = {
	   yyyy : date.getFullYear() //�� : 4λ
	  ,yy : date.getFullYear().toString().substring(2)//�� : 2λ
	  ,M  : date.getMonth() + 1  //�� : ���1λ��ʱ�򲻲�0
	  ,MM : paddNum(date.getMonth() + 1) //�� : ���1λ��ʱ��0
	  ,d  : date.getDate()   //�� : ���1λ��ʱ�򲻲�0
	  ,dd : paddNum(date.getDate())//�� : ���1λ��ʱ��0
	  ,hh : date.getHours()  //ʱ
	  ,mm : date.getMinutes() //��
	  ,ss : date.getSeconds() //��
	}
	format || (format = "yyyy-MM-dd hh:mm:ss");
	return format.replace(/([a-z])(\1)*/ig,function(m){return cfg[m];});
}
//���QueryString������ƻ�ȡֵ
//��ݴ����name��ȡ��Ӧ��value
function getQueryStringByName(name){
	var result = location.search.match(new RegExp("[\?\&]" + name+ "=([^\&]+)","i"));
	if(result == null || result.length < 1){
		return "";
	}
	return result[1];
}

var routeToken = "";
function routeLogin(async){
	async = async == false?false:true;
	$.ajax({
		type: "GET", 
		async :async,
		url: 'http://192.168.10.1/cgi-bin/luci/api/0/account/login',
		data:{pass:"12345678"},
		complete: function(xhr,status){
			var data = eval('('+xhr.responseText+')');
			routeToken=data.token;
		},
		error: function(jqXHR, textStatus,errorThrown){
//			console.debug(jqXHR.status);
//			console.debug(jqXHR.statusText);
//			console.debug(jqXHR.responseText);
//			console.debug(textStatus);
//			console.debug(errorThrown);
		}
	});
}

function execRouteInterface(type,url,data,needLogin,callback){
	if(needLogin){
		if(routeToken == ""){
			routeLogin(false);
		}
		data.token = routeToken;
	}
	$.ajax({
		type: type, 
		url: url,
		data:data,
		complete: function(xhr,status){
			var data = eval('('+xhr.responseText+')');
			callback(data);
		}
	});
}
//����ļ��Ƿ����:һ��Ӧ����չʾĳ��ģ������ǰ�����ģ�������Ƿ���ڣ�������������
function checkFileExist(filePath){
	var fileExist = true;
	$.ajax({
		type: "GET",
		async :false,
		url: filePath,
		complete: function(xhr,status){
			if(xhr.status == 404){//�ļ�������
				fileExist = false;
			}
		},
		error: function(jqXHR, textStatus,errorThrown){
		}
	});
	return fileExist;
}

function readXML(filePath){
	var $xmlData;
	$.ajax({
		type: "GET",
		async :false,
		dataType: 'xml',
		url: filePath,
		success: function(data,status,xhr){
			$xmlData = $(data);
			//console.debug($(data).find("template").children("path").text());
		},
		error: function(xhr,status,error){
			alert("get config data error!");
			//alert("���������ļ�ʧ�ܣ�");
		}
	});
	return $xmlData;
}