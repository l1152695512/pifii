<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

(function(){
	var mapObject;
	var animateFinish = false;
	var walkPointToPointTimes = 1000;//从上一个轨迹移动到新轨迹使用的时间，时间越大移动的越慢，理论上如果该值等于refreshTime的话，人的移动会比较有连贯性。
	var maxRoutePointDistance = 1.0;//决定画路线时点的密度，值越大，密度越小
	//历史轨迹查询
	function getHistoryRoute(){
		Ext.Msg.wait('正在加载路线数据....', '提示');
	   	Ext.Ajax.request({
       		url : 'historyroute/get-history-route.action',
       		params: {personId:'${personId}',startTime:dateStart.value,endTime:dateEnd.value},
            success : function(xhr) {
            	Ext.Msg.hide(); 
            	var responseText = Ext.decode(xhr.responseText);
            	var dataList = responseText.list;
            	if(dataList.length > 0){
            		$('#history_route_${personId}').animate(
            			{top:dataList[0].locationY,left:dataList[0].locationX},
            			{duration: 0,
							easing:'linear',
					    	complete:function(animation){
					    		addRouteTime(dataList[0]);
					    		if(dataList.length > 1){
				            		drowToNextPoint(dataList,1);
				            	}
					    	}
			  			});
            	}
           	},
		    failure: function(xhr){
		    	Ext.Msg.hide(); 
		    	Ext.Msg.alert("温馨提醒","获取数据信息失败，稍后请重试！");
		    }
        })
	};
	function drowToNextPoint(points,index){
		if(index>=points.length){
			animateFinish = true;
			return;
		}
		$('#history_route_${personId}').animate(
			{top:points[index].locationY,left:points[index].locationX},
			{duration: walkPointToPointTimes,
				easing:'linear',
		    	start:function(animation){
	      			var left = parseFloat($(this).position().left);
					var top = parseFloat($(this).position().top);
	      			var currentPointDistance = Math.sqrt(Math.pow((left-points[index].locationX),2)+Math.pow((top-points[index].locationY),2));
					var drowRoutePointTimes = walkPointToPointTimes*maxRoutePointDistance/currentPointDistance;
					drowLineTask.delay(0,drowLine,this,[$(this),points[index],drowRoutePointTimes]);
		    	},
		    	complete:function(animation){
		    		addRouteTime(points[index]);
	      			drowToNextPoint(points,++index);
		    	}
  		});
	}
	var drowLineTask = new Ext.util.DelayedTask();
	function drowLine(personLocation,endPoint,drowRoutePointTimes){
		if($('#history_route_img_${personId}').html() != undefined){//如果该窗口没有关闭
			var left = parseFloat(personLocation.position().left);
			var top = parseFloat(personLocation.position().top);
			if(left!=endPoint.locationX || top!=endPoint.locationY){
				drowLineTask.delay(drowRoutePointTimes,drowLine,this,[personLocation,endPoint,drowRoutePointTimes]);
			}
			var route_point = '<div class="route" style="top: ' + top + 'px; left: ' + left +
					'px;"></div>';
			$('#history_route_locations_${personId}').append(route_point);
		}
	}
	function addRouteTime(routePoint){
		movePositionToMapCenter(routePoint.locationX,routePoint.locationY);
		$('#route_time_div_${personId}').append('<div style="padding:2px 0px 2px 10px;cursor: pointer;" data-location-x="'+routePoint.locationX+'" data-location-y="'+routePoint.locationY+'">' 
						+routePoint.date +'</div>');
		var lastChild = $('#route_time_div_${personId} div:last-child');
		lastChild.on('click',function(){
			if(animateFinish){
				$('#route_time_div_${personId}').children().removeClass("history-route-point-click"); 
				$(this).addClass("history-route-point-click"); 
				$('#history_route_${personId}').animate({top:$(this).data("locationY")+'px',left:$(this).data("locationX")+'px'}); 
	//			$('#history_route_click_${personId}').show();
				movePositionToMapCenter($(this).data("locationX"),$(this).data("locationY"));
			}
		});
		lastChild.on('mouseover',function(){
			$(this).addClass("history-route-point-mouseover"); 
		});
		lastChild.on('mouseout',function(){
			$(this).removeClass("history-route-point-mouseover"); 
//			$(this).css("background","#DFEEF6"); 
		});
	}
	function movePositionToMapCenter(targetX,targetY){
    	//可视窗口的大小
    	var mapHeight = parseFloat($('#history_route_map_${personId}').parent().height());
    	var mapWidth = parseFloat($('#history_route_map_${personId}').parent().width());
    	//地图相对于centerPanel的偏移量
    	var position = $('#history_route_map_${personId}').position(); 
    	try{
	    	var positionLeft = parseFloat(position.left);//为负值
	    	var positionTop = parseFloat(position.top);//为负值
	    	
	    	var centerX = Math.abs(positionLeft)+mapWidth/2;
	    	var centerY = Math.abs(positionTop)+mapHeight/2;
	    	
	    	var newLeft = positionLeft + (centerX - targetX);
	    	var newTop = positionTop + (centerY - targetY);
	    	
	    	var maxOffsetLeft = parseFloat(mapObject.width) - mapWidth;
	    	var maxOffsetTop = parseFloat(mapObject.height) - mapHeight;
	    	if(Math.abs(newLeft) > maxOffsetLeft){
	    		newLeft = 0-maxOffsetLeft;
	    	}
	    	if(newLeft > 0){
	    		newLeft = 0;
	    	}
	    	if(Math.abs(newTop) > maxOffsetTop){
	    		newTop = 0-maxOffsetTop;
	    	}
	    	if(newTop > 0){
	    		newTop = 0;
	    	}
	        $('#history_route_map_${personId}').animate({top:newTop+'px',left:newLeft+'px'},300); 
    	}catch(e){
    	}
    }
	//加载信号收集站点,成功后加载人员信息
	function loadInformations(){
		Ext.Msg.wait('正在加载设备信息....', '提示');
		Ext.Ajax.request({
			url : 'devicemanage/get-devices.action',
			params : {communityId : '${communityId}'},
			success : function(form,action){
				Ext.Msg.hide(); 
				var responseText = Ext.decode(form.responseText);
            	var dataList = responseText.list;
            	var deviceHtml = '';
            	for(var i=0;i<dataList.length;i++){
            		deviceHtml=deviceHtml+'<div class="ip_tooltip ip_img32" ' +
            				'style="top: '+dataList[i].locationY+'px; left: '+ dataList[i].locationX+
            				'px;" data-tooltipbg="bgwhite" data-round="roundBgW" data-button="device" data-animationtype="ltr-slide">'+
            				'<p>设备信息<hr>' +
            				'名称：'+dataList[i].name +
            				'<br>添加时间：'+dataList[i].addDate +
            				'<br>描述：'+dataList[i].description +
            				'</p></div>';
				}
				$('#history_route_devices_${personId}').html(deviceHtml);
				$("#history_route_map_${personId}").iPicture({id:"history_route_devices_${personId}"});
				showSearchPerson();
			},
			failure:function(form,action){
				Ext.Msg.hide(); 
				Ext.Msg.alert("温馨提醒","获取设备信息失败，稍后请重试！");
			}
		});
	}
	
	function showSearchPerson(){
    	var personLocationHtml = '<div id="history_route_${personId}" class="ip_tooltip ip_img32" style="top: -100px; left: -100px;" ' +
				'data-tooltipbg="bgwhite" data-round="roundBgY" data-button="person" data-animationtype="ltr-slide">'+
				'<p>人员信息<hr>' +
					'<img src="${photo}" class="person-image" style="width:60px;height:70px;"/>' +
					'类型：${type}'+
					'<br>姓名：${name}'+
					'<br>年龄：${age}'+
					'<br>电话：${phone}'+
					'<br>其他：'+
					'${description}'.replace(new RegExp('<br>', 'g'), '\n')+
				'</p></div>';
		$('#history_route_person_${personId}').html(personLocationHtml);
		$("#history_route_map_${personId}").iPicture({id:"history_route_person_${personId}"});
	}
	var dataLabel = new Ext.form.Label({
		width : '50',
		html : '时间区间：'
	});
	var dateStart = new Ext.ux.form.DateTimeField({
		name : 'startDate',
		format : 'Y-m-d H:i:s', 
		editable:false,
		width : 150
	});
	var label = new Ext.form.Label({
		width : '50',
		html : '&nbsp;&nbsp;&nbsp;-&nbsp;&nbsp;&nbsp;'
	});
	var dateEnd = new Ext.ux.form.DateTimeField({
		name : 'endDate',
		format : 'Y-m-d H:i:s',
		editable:false,
		width : 150
	});
	var query = new Ext.Action({
		text : '查询',
		iconCls : 'icon-find',
		handler : function() {
			var start = dateStart.value;
			var end = dateEnd.value;
			if(start == undefined || end == undefined){
				Ext.Msg.alert("温馨提醒",'开始时间和结束时间必须填写！');
				return;
			}
			if(start > end){
				Ext.Msg.alert("温馨提醒",'开始时间不能大于结束时间！');
				return;
			}
			try{
				drowLineTask.cancel();
			}catch(e){
			}
			$('#history_route_${personId}').stop();
			$('#route_time_div_${personId}').html("");
			$('#history_route_locations_${personId}').html("");
			animateFinish = false;
//			$('#history_route_click_${personId}').hide();
			getHistoryRoute();
		}
	});
	var mapPanel=new Ext.Panel({
		header : false,
		region:'center',
		layout : 'fit',
		border : false,
		tbar: [dataLabel,dateStart,label,dateEnd,'-',query],
        html:'<div id="history_route_map_${personId}" data-interaction="hover">' +
        		'<div class="ip_slide">' +//下面的元素注意放的位置，防止不合理的遮挡，下面的元素会遮挡上面的元素
        			'<img id="history_route_img_${personId}" class="ip_tooltipImg" src="${map}"/>' +
        			'<div id="history_route_devices_${personId}" class="devices">' +
	        		'</div>' +
//	        		'<div id="history_route_click_${personId}" class="ip_tooltip ip_img32">' +
//	        			'<div class="roundBgY"></div>' +
//	        			'<div class="roundBgYIn"></div>' +
//	        			'<div class="roundBgYInner"></div>' +
//	        		'</div>' +
        			'<div id="history_route_locations_${personId}" class="locations">' +
	        		'</div>' +
	        		'<div id="history_route_person_${personId}" class="locations">' +
	        		'</div>' +
        		'</div>' +
        	'</div>',
        listeners:{
        	"afterrender":function(panel){
//        		$('#history_route_click_${personId}').hide();
		        //给地图添加拖动效果
        		mapObject = new SpryMap({id : "history_route_map_${personId}",
                         height: parseFloat('${mapHeightPixel}'),
                         width: parseFloat('${mapWidthPixel}'),
                         startX: 0,
                         startY: 0,
                         cssClass: ""});
                changMapSize();
                loadInformations();
          	},
          	"bodyresize":function(){
          		changMapSize();
          		refreshMapPosition();
			}
        }
    });
    function changMapSize(){//当窗口改变时，更改地图的显示尺寸，保证图片可以拖动显示全部
    	var divHeight = $('#history_route_map_${personId}').parent().parent().height();
    	var divWidth = $('#history_route_map_${personId}').parent().parent().width();
    	$('#history_route_map_${personId}').parent().height(divHeight);
    	$('#history_route_map_${personId}').parent().width(divWidth);
    }
    //解决当地图显示区域为右下角时，再拉大浏览器窗口时会出现地图无法全部填充centerPanel的情况
    //要考虑的问题：1.地图可填充满centerPanel,但未填充满；2.地图已近展示到最大了还是无法展示全部的地图
    function refreshMapPosition(){
    	//可视窗口的大小
    	var mapHeight = parseFloat($('#history_route_map_${personId}').parent().height());
    	var mapWidth = parseFloat($('#history_route_map_${personId}').parent().width());
    	//地图相对于centerPanel的偏移量
    	var position = $('#history_route_map_${personId}').position(); 
    	try{
	    	var positionLeft = parseFloat(position.left);//为负值
	    	var positionTop = parseFloat(position.top);//为负值
	    	if(Math.abs(positionLeft)+mapWidth > mapObject.width){
	    		positionLeft = mapWidth-mapObject.width;
	    		if(positionLeft > 0){//解决问题2
	    			positionLeft = 0;
	    		}
	    	}
	    	if(Math.abs(positionTop)+mapHeight > mapObject.height){
	    		positionTop = mapHeight-mapObject.height;
	    		if(positionTop > 0){//解决问题2
	    			positionTop = 0;
	    		}
	    	}
	    	$('#history_route_map_${personId}').animate({top:positionTop+'px',left:positionLeft+'px'},0); 
    	}catch(e){
    	}
    }
    var routeTimePanel = new Ext.Panel({
//    	header : false,
		title : '位置详细信息',
//    	id:"persons_information_panel_${id}",
    	region : 'west',
    	width: 160,
//    	bodyStyle:'padding:10px 10px',
    	collapsible:true,
    	collapseMode:'mini',
//    	border:false,
		autoScroll : true,
		layout:'fit',
//		tbar: [clear_route],
		html : '<div id="route_time_div_${personId}"></div>',
		listeners:{
        	"afterrender":function(panel){
//        		 loadInformations();
			}
		}
    });
    var mainPanel = new Ext.Panel({
		autoWidth:true,
    	header : false,
		title : '历史轨迹查询',
		border : false,
		layout : 'border',
		items : [routeTimePanel,mapPanel],
		listeners: {  
             
        }  
	});
	return mainPanel;
})();