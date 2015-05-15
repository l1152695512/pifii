/*勾选*/
$(document).ready(function(){
	$(".address ul > li").click(function(){
		if($(this).hasClass("dingwei") && $("#add_position").data("positionSuccess")=="0"){
			alert("未定位到当前的位置信息！");
			return;
		}
		$(this).addClass('a_icon').siblings().removeClass('a_icon');
	});
//	var lng = $.cookie("lng");
//	if(undefined != lng && "" != lng){
//		var baiduPoint = new BMap.Point($.cookie("lng"), $.cookie("lat"));
//		translateCallback(baiduPoint);
//	}else{
//		getLocation();
//	}
});
function getLocation(){
	if(navigator.geolocation){
		navigator.geolocation.getCurrentPosition(getPositionSuccess,getPositionError,{
			timeout: 30000,
			maximumAge: 5000,
			enableHighAccuracy: true
		});
	}else{
	   alert("您的浏览器不支持地理定位");
	}
}
function getPositionError(){
	$("#add_position").html("定位失败！");
	alert("地理位置定位失败！");
}
function getPositionSuccess(position){
	lat=position.coords.latitude;
	lon=position.coords.longitude;
	var gpsPoint = new BMap.Point(lon, lat); 
	BMap.Convertor.translate(gpsPoint, 0, translateCallback); //转换坐标 
}

function translateCallback(point){
//	$.cookie("lng", point.lng);
//	$.cookie("lat", point.lat);
	$("input[name='lng']").val(point.lng);
	$("input[name='lat']").val(point.lat);
	var gc = new BMap.Geocoder();    
	gc.getLocation(point, function(rs){
	   var addComp = rs.addressComponents;
	   document.getElementById("add_position").innerHTML = addComp.province + addComp.city + addComp.district + addComp.street + addComp.streetNumber;
	   $("#add_position").data("positionSuccess","1");
	   //alert(addComp.province + ", " + addComp.city + ", " + addComp.district + ", " + addComp.street);
	});
}
