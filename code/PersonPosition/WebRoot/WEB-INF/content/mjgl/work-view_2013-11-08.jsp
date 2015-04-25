<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


(function(){
	var map_type = '';
	var refreshTime = 15000;
	//实时定位查询
	var positionTask = new Ext.util.DelayedTask(function(){
		if(map_type == 'position'){
		   	Ext.Ajax.request({
	       		url : 'location/person-location.action',
	       		params : {communityId : '${id}'},
	            success : function(xhr) {
	        		var responseText = Ext.decode(xhr.responseText);
	            	var dataList = responseText.list;
	            	var dataHtml = '';
	            	for(var i=0;i<dataList.length;i++){
	            		var dataRound = 'roundBgB';
	//            		if(dataList[i].locationY>250 || dataList[i].locationX>250){
	//            			dataRound = '';
	//            		}else if(dataList[i].locationY>150 || dataList[i].locationX>150){
	//            			dataRound = 'roundBgB';
	//            		}
	            		dataHtml=dataHtml+'<div class="ip_tooltip ip_img32" style="top: '+dataList[i].locationY+'px; left: '+ dataList[i].locationX+
	            				'px;" data-tooltipbg="bgwhite" data-round="'+dataRound+'" data-button="person" data-animationtype="ltr-slide">'+
	            				'<p>人员信息<hr>' +
	            				'<img src="'+dataList[i].photo+'" class="person-image" style="width:60px;height:70px;"/>' +
	            				'类型：'+dataList[i].KEY_VALUE+
	            				'<br>姓名：'+dataList[i].name+
	            				'<br>年龄：'+dataList[i].age+
	            				'<br>电话：'+dataList[i].phone+
	            				'<br>时间：'+dataList[i].date+
	            				'</p></div>';
					}
					if(dataHtml!=''){
						$('#locations_${id}').html(dataHtml);
						$("#map_${id}").iPicture({id:"locations_${id}"});
					}
	                positionTask.delay(refreshTime);
	           	},
			    failure: function(xhr){
			    	positionTask.delay(refreshTime);
			    }
	        })
		}
	});
//	Ext.realTimeLocation = function(){
//		map_type = 'position';
//		positionTask.delay(0);
//	}
	
	//历史轨迹查询
	function getHistoryRoute(){
	   	Ext.Ajax.request({
       		url : 'location/history-route.action',
       		params: searchRoutePanel.getForm().getValues(),
            success : function(xhr) {
            	var responseText = Ext.decode(xhr.responseText);
            	var dataList = responseText.list;
            	var startPoint = '';
            	if(dataList.length > 0){
            		startPoint='<div class="ip_tooltip ip_img32" style="top: '+dataList[0].locationY+'px; left: '+ dataList[0].locationX+
            				'px;" data-tooltipbg="bgwhite" data-round="" data-button="person" data-animationtype="ltr-slide">'+
            				'<p>人员信息(起点)<hr>' +
            				'类型：'+dataList[0].KEY_VALUE+
            				'<br>姓名：'+dataList[0].name+
            				'<br>电话：'+dataList[0].phone+
            				'<br>邮箱：'+dataList[0].email+
            				'<br>时间：'+dataList[0].date+
            				'</p></div>';
	            	$('#locations_${id}').append(startPoint);
//	            	document.getElementById("locations_${id}").innerHTML=startPoint;
	            	$("#map_${id}").iPictureInsert($('#locations_${id}').find('.ip_tooltip:last'));
//					jQuery( "#iPicture_location" ).iPicture();
            	}
            	drowNext(dataList,0);
           	},
		    failure: function(xhr){
		    }
        })
	};
	function drowNext(locations,index){
		if(index+1<locations.length){
			drowHistoryRoute(locations,index);
		}else{
			var routeEndPoint = locations[locations.length-1];
			var tooltips_add = '<div class="ip_tooltip ip_img32" style="top: ' + routeEndPoint.locationY + 'px; left: ' + routeEndPoint.locationX +
				'px;" data-tooltipbg="bgwhite" data-round="" data-button="person" data-animationtype="ltr-slide">'+
				'<p>人员信息(终点)<hr>' +
				'类型：'+routeEndPoint.KEY_VALUE+
				'<br>姓名：'+routeEndPoint.name+
				'<br>年龄：'+routeEndPoint.age+
				'<br>电话：'+routeEndPoint.phone+
				'<br>时间：'+routeEndPoint.date+
				'</p>'+
				'</div>';
			$('#locations_${id}').append(tooltips_add);
			$("#map_${id}").iPictureInsert($('#locations_${id}').find('.ip_tooltip:last'));
		}
	}
	var locationRadius = 2;
	var historyRouteTask;
	function drowHistoryRoute(locations,index){
		var startPoint = locations[index];
		var endPoint = locations[index+1];
		var times = 1;
		var lengthXY = Math.sqrt(Math.pow(startPoint.locationX - endPoint.locationX,2)+Math.pow(startPoint.locationY - endPoint.locationY,2))
		historyRouteTask = new Ext.util.DelayedTask(function(){
			if(map_type == 'historyRoute'){
				var currentLengthXY = times*locationRadius;
				var positionX = Number(startPoint.locationX)+currentLengthXY*(endPoint.locationX - startPoint.locationX)/lengthXY;
				var positionY = Number(startPoint.locationY)+currentLengthXY*(endPoint.locationY - startPoint.locationY)/lengthXY;
				var currentXY = Math.sqrt(Math.pow(positionX,2)+Math.pow(positionY,2));
				var dataTooltipbg = "";
				var dataButton = "person-location-route";
				var dataAnimationtype = "";
				var tipsDescription = "";
				if(currentLengthXY >= lengthXY){
					positionX = endPoint.locationX;
					positionY = endPoint.locationY;
					dataTooltipbg = "bgwhite";
					dataButton = "person";
					dataAnimationtype = "ltr-slide";
					tipsDescription = '<p>' +
								'时间：'+endPoint.date+
	            				'</p>';
				}
				var tooltips_add = '<div class="ip_tooltip ip_img32" style="top: ' + positionY + 'px; left: ' + positionX +
					'px;" data-tooltipbg="'+dataTooltipbg+'" data-round="" data-button="'+dataButton+'" data-animationtype="'+dataAnimationtype+'">' +
					tipsDescription +
					'</div>';
				$('#locations_${id}').append(tooltips_add);
				$("#map_${id}").iPictureInsert($('#locations_${id}').find('.ip_tooltip:last'));
				times = times+1;
				if(currentLengthXY < lengthXY){
					historyRouteTask.delay(0);
				}else{
					drowNext(locations,index+1);
				}
			}
		});
		historyRouteTask.delay(0);
	}

	//实时轨迹查询
	var previousPosition = '';
	var currentPositionTask = new Ext.util.DelayedTask(function(){
		if(map_type == 'currentRoute'){
			Ext.Ajax.request({
	       		url : 'location/current-route.action',
	       		params: searchRoutePanel.getForm().getValues(),
//	       		params: {name:searchRoutePanel.getForm().getValues().name},
	            success : function(xhr) {
	            	var responseText = Ext.decode(xhr.responseText);
	            	var dataList = responseText.list;
			    	if(dataList.length > 0){
			    		if(previousPosition==''){
				    		endPointHtml='<div class="ip_tooltip ip_img32" style="top: '+dataList[0].locationY+'px; left: '+ dataList[0].locationX+
				    				'px;" data-tooltipbg="bgwhite" data-round="roundBgB" data-button="person" data-animationtype="ltr-slide">'+
				    				'<p>人员信息<hr>' +
				    				'类型：'+dataList[0].KEY_VALUE+
				    				'<br>姓名：'+dataList[0].name+
				    				'<br>电话：'+dataList[0].phone+
				    				'<br>邮箱：'+dataList[0].email+
				    				'<br>时间：'+dataList[0].date+
				    				'</p></div>';
				        	$('#locations_${id}').append(endPointHtml);
				        	$("#map_${id}").iPictureInsert($('#locations_${id}').find('.ip_tooltip:last'));
				        	previousPosition = dataList[0];
			    		}else{
			    			drowCurrentRoute(previousPosition,dataList[0]);
			    			previousPosition = dataList[0];
			    		}
	            	}
	            	currentPositionTask.delay(refreshTime);
	           	},
			    failure: function(xhr){
			    	currentPositionTask.delay(refreshTime);
			    }
	        })
		}else{
			previousPosition = '';
		}
	});
	var currentRouteTask;
	function drowCurrentRoute(startPoint,endPoint){
		if(Number(startPoint.locationX) == Number(endPoint.locationX) 
				&& Number(startPoint.locationY) == Number(endPoint.locationY)){
			return;
		}
		$('#locations_${id}').find('.ip_tooltip:last .roundBgB').remove();
		$('#locations_${id}').find('.ip_tooltip:last .roundBgBIn').remove();
		$('#locations_${id}').find('.ip_tooltip:last .roundBgBInner').remove();
		var times = 1;
		var lengthXY = Math.sqrt(Math.pow(startPoint.locationX - endPoint.locationX,2)+Math.pow(startPoint.locationY - endPoint.locationY,2))
		currentRouteTask = new Ext.util.DelayedTask(function(){
			if(map_type == 'currentRoute'){
				var dataTooltipbg = "";
				var dataRound = "";
				var dataButton = "person-location-route";
				var tipsDescription = "";
				var animationtype = "";
				var currentLengthXY = times*locationRadius;
				var positionX = Number(startPoint.locationX)+currentLengthXY*(endPoint.locationX - startPoint.locationX)/lengthXY;
				var positionY = Number(startPoint.locationY)+currentLengthXY*(endPoint.locationY - startPoint.locationY)/lengthXY;
				var currentXY = Math.sqrt(Math.pow(positionX,2)+Math.pow(positionY,2));
				if(currentLengthXY >= lengthXY){
					positionX = endPoint.locationX;
					positionY = endPoint.locationY;
					dataTooltipbg = "bgwhite";
					dataRound = "roundBgB";
					dataButton = "person";
					animationtype = "ltr-slide";
					tipsDescription = '<p>人员信息<hr>' +
								'类型：'+endPoint.KEY_VALUE+
								'<br>姓名：'+endPoint.name+
	            				'<br>年龄：'+endPoint.age+
	            				'<br>电话：'+endPoint.phone+
	            				'<br>时间：'+endPoint.date+
	            				'</p>';
				}
				var tooltips_add = '<div class="ip_tooltip ip_img32" style="top: ' + positionY + 'px; left: ' + positionX +
					'px;" data-tooltipbg="'+dataTooltipbg+'" data-round="'+dataRound+
					'" data-button="'+dataButton+'" data-animationtype="'+animationtype+'">'+
					tipsDescription+
					'</div>';
				$('#locations_${id}').append(tooltips_add);
				$("#map_${id}").iPictureInsert($('#locations_${id}').find('.ip_tooltip:last'));
				times = times+1;
				if(currentLengthXY < lengthXY){
					currentRouteTask.delay(0);
				}
			}
		});
		currentRouteTask.delay(0);
	}
	
	//全屏功能
	//	var all_html;
	var full_screen_win = '';
	//north-region     title-bar     ext-gen13
//	Ext.full_screen_location = function(){
//		console.debug(Ext.getCmp('north-region'));
//		console.debug(Ext.getCmp('title-bar'));
//		console.debug(Ext.getCmp('ext-gen13'));
//		
//		Ext.getCmp('north-region').collapse(false);
		
//		Ext.getCmp('north-region').hide();
//		Ext.getCmp('north-region').doLayout();
//		Ext.getCmp('north-region').adjustBodyHeight();
//		Ext.getCmp('north-region').adjustBodyWidth();
//		Ext.getCmp('north-region').adjustPosition();
//		Ext.getCmp('north-region').adjustSize();
		
//		$('#north-region').fadeOut();
//		$('#north-region').hide();
//		$('#title-bar').hide();
//		$('#ext-gen13').hide();
//		if (window.screen) {              //判断浏览器是否支持window.screen判断浏览器是否支持screen
////			alert("----------");
//		    var width = window.screen.availWidth;   //定义一个myw，接受到当前全屏的宽
//		    var height = window.screen.availHeight;  //定义一个myw，接受到当前全屏的高
//		    window.moveTo(0, 0);           //把window放在左上脚
//		    window.resizeTo(width, height);     //把当前窗体的长宽跳转为myw和myh
//    	}
//		mainPanel.adjustSize();
//		console.debug(mainPanel);
//		console.debug(Ext.getBody());
//		Ext.getBody().resize();
//		searchRoutePanel.collapse();
		
		
		
//		alert("clientWidth="+document.body.clientWidth+"\n"+
//			"clientHeight="+document.body.clientHeight+"\n"+
//			"offsetWidth="+document.body.offsetWidth+"\n"+
//			"offsetHeight="+document.body.offsetHeight);
		
//		all_html = $('body').html();
//		var full_screen_object = $('#location_full_screen');
//		console.debug(full_screen_object);
//		$('body').html('');
//		$('body').append(full_screen_object);
//		$('body').height('710px');
//		searchRoutePanel.doLayout();
//		mainPanel.doLayout();
//		console.debug($('body'));
//		centerPanel.setHeight(document.body.clientHeight);
//		centerPanel.setWidth(document.body.clientWidth);
//		mainPanel.setHeight(document.body.clientHeight);
//		mainPanel.setWidth(document.body.clientWidth);
		
		//1100.710
		
//		var all_html_object = $('body');
//		var full_screen_div = $('<div id="full_screen_div" style="position: absolute;z-index:100"></div>');
//		full_screen_div.append(full_screen_object);
//		$('body').html('');
//		$('body').append(full_screen_object);
//		$('#location_full_screen').remove();
//		$('#full_screen_div').append(full_screen_object);
		
		
//		alert(Ext.getCmp('full_screen_div').body.dom.innerHTML);
//		alert($('#location_full_screen').html());
//		console.debug(Ext.getCmp('leftPanel1'));
//		Ext.getCmp('leftPanel1').collapse();
		
//		var full_screen_object = $('#location_full_screen').html();
//		$('#location_full_screen').html('');
//		Ext.getCmp('full_screen_div').addClass('full-screen-location');
//		Ext.getCmp('full_screen_div').body.dom.innerHTML = full_screen_object;
		
		
//	}
//	Ext.EventManager.onWindowResize(function(w,h){ 
//		Ext.getCmp('north-region').doLayout();
//	});
//	Ext.full_screen_location_exit = function(){
//		alert(all_html_object.html());
//		$('body').html('');
//		$('body').html(all_html);
//		$('body').append($(all_html));
		
		
//		$('#north-region').show();
//		$('#title-bar').show();
//		$('#ext-gen13').show();
//		
//	}
	
	//更改地图功能
//	Ext.changeMap = function(map_path,map_id){
//		Ext.Msg.confirm('确认', '你确定要使用该图片作为地图？',function(btn, text){
//			if (btn == 'yes') {		
//				Ext.Msg.wait('正在更换地图....', '提示');
//				Ext.Ajax.request({
//					url : 'location/change-map.action',
//					params : {mapId : map_id},
//					success : function(form,action){
//						Ext.Msg.hide(); 
//						$("#${id}").attr("src", map_path);
//						searchRoutePanel.collapse(false);
//						$("#preview_div_${id}").remove();
//						map_type = '';
// 					},
// 					failure:function(form,action){
// 						Ext.Msg.hide(); 
// 						Ext.Msg.alert("温馨提醒","更换地图失败，稍后请重试！");
// 					}
//				});
//			}
//		});
//	}
//	Ext.getAllMaps = function(){
//		if(map_type != 'changeMap'){
//			changeFunction('changeMap');
//			Ext.Msg.wait('正在获取所有地图....', '提示');
//			Ext.Ajax.request({
//				url : 'location/get-all-maps.action',
//				success : function(form,action){
//					Ext.Msg.hide(); 
//					var picturePreviewDiv = '<div id="preview_div_${id}" class="picture-preview">' +
//								'<ul id="preview_${id}" class="jcarousel jcarousel-skin-tango">';
//					var dataList = Ext.decode(form.responseText).list;
//					for(var i=0;i<dataList.length;i++){
//						picturePreviewDiv = picturePreviewDiv+
//							'<li><div align="center">'+dataList[i].name+'</div><img src="'+dataList[i].path+'" onDblClick=\'Ext.changeMap("'+dataList[i].path+'","'+dataList[i].id+'")\' width="200" height="100" alt=""/></li>';
//					}
//					picturePreviewDiv = picturePreviewDiv+'</ul></div>';
//					$('#search_panel_${id}').append(picturePreviewDiv);
//					$('#preview_${id}').jcarousel({
//				        vertical: true,
//				        scroll: 2
//				    });
//				    
//				    var picturePreviewHeight = $('#west_panel_${id}').height() - $('#navigation_bar_${id}').height();
//				    if(picturePreviewHeight < 180){
//				    	picturePreviewHeight = 180;
//				    }
//				    $('#preview_div_${id} .jcarousel-skin-tango .jcarousel-container-vertical').height(picturePreviewHeight-140);
//				    $('#preview_div_${id} .jcarousel-skin-tango .jcarousel-clip-vertical').height(picturePreviewHeight-140);
//				    searchRoutePanel.expand(false);
//				},
//				failure:function(form,action){
//					Ext.Msg.hide(); 
//					Ext.Msg.alert("温馨提醒","获取地图失败，稍后请重试！");
//				}
//			});
//		}
//	}
	
	//历史轨迹搜索
//	Ext.historyRoute = function(){
////		alert(searchRoutePanel.isVisible());
//		changeFunction('historyRoute');
////		alert(searchRoutePanel.isVisible());
//		$("#start_date_${id}").parent().parent().parent().show();
//		$("#end_date_${id}").parent().parent().parent().show();
//		searchRoutePanel.expand(false);
//	}
	
	//实时轨迹搜索
//	Ext.currentRoute = function(){
////		alert(searchRoutePanel.isVisible());
//		changeFunction('currentRoute');
////		alert(searchRoutePanel.isVisible());
////		console.debug(Ext.getCmp("start_date_${id}"));
////		Ext.getCmp("start_date_${id}").doHide();
//		$("#start_date_${id}").parent().parent().parent().hide();
//		$("#end_date_${id}").parent().parent().parent().hide();
////		$("#start_date_${id}").hide();
//		searchRoutePanel.expand(false);
//	}
	
	//加载信号收集站点
//	function getAllDevices(){
//		Ext.Msg.wait('正加载信号收集站点....', '提示');
//		Ext.Ajax.request({
//			url : 'location/get-all-devices.action',
//			success : function(form,action){
//				Ext.Msg.hide(); 
//				var responseText = Ext.decode(form.responseText);
//            	var dataList = responseText.list;
//            	var deviceHtml = '';
//            	for(var i=0;i<dataList.length;i++){
//            		deviceHtml=deviceHtml+'<div id="'+dataList[i].id+'" class="ip_tooltip ip_img32" ' +
//            				'style="top: '+dataList[i].locationY+'px; left: '+ dataList[i].locationX+
//            				'px;" data-tooltipbg="bgwhite" data-round="roundBgW" data-button="device" data-animationtype="ltr-slide">'+
//            				'<p>设备信息<hr>添加时间：'+dataList[i].addDate +
//            				'<br>X坐标：'+dataList[i].locationX +
//            				'<br>Y坐标：'+dataList[i].locationY +
//            				'</p></div>';
//				}
//				$('#devices_${id}').html(deviceHtml);
//				$("#map_${id}").iPicture({id:"devices_${id}"});
//				//onDblClick=\'Ext.removeDevice(event,\"'+dataList[i].id+'\");\' 
//				$('#devices_${id}').find('.ip_tooltip .button').on('dblclick',function(event){
//					var locationX = event.layerX;
//					var locationY = event.layerY;
//					alert("locationX="+locationX+"=====locationY="+locationY);
//					removeDevice($(this).parent().attr('id'));
//				});
//			},
//			failure:function(form,action){
//				Ext.Msg.hide(); 
//				Ext.Msg.alert("温馨提醒","获取站点信息失败，稍后请重试！");
//			}
//		});
//	}
	
	//加载信号收集站点
	function loadDevices(){
		Ext.Msg.wait('正在加载地图信息....', '提示');
		Ext.Ajax.request({
			url : 'mapseeting/devicemanage/get-devices.action',
			params : {communityId : '${id}'},
			success : function(form,action){
				Ext.Msg.hide(); 
				var responseText = Ext.decode(form.responseText);
            	var dataList = responseText.list;
            	var deviceHtml = '';
            	for(var i=0;i<dataList.length;i++){
            		deviceHtml=deviceHtml+'<div id="'+dataList[i].id+'" class="ip_tooltip ip_img32" ' +
            				'style="top: '+dataList[i].locationY+'px; left: '+ dataList[i].locationX+
            				'px;" data-tooltipbg="bgwhite" data-round="roundBgW" data-button="device" data-animationtype="ltr-slide">'+
            				'<p>设备信息<hr>' +
            				'名称：'+dataList[i].name +
            				'<br>添加时间：'+dataList[i].addDate +
            				'<br>描述：'+dataList[i].description +
            				'</p></div>';
				}
				$('#devices_${id}').html(deviceHtml);
				$("#map_${id}").iPicture({id:"devices_${id}"});
			},
			failure:function(form,action){
				Ext.Msg.hide(); 
				Ext.Msg.alert("温馨提醒","获取站点信息失败，稍后请重试！");
			}
		});
	}
	function loadPersons(){
	   	Ext.Ajax.request({
       		url : 'location/get-persons.action',
       		params : {communityId : '${id}'},
            success : function(xhr) {
        		var responseText = Ext.decode(xhr.responseText);
            	var dataList = responseText.list;
            	var dataHtml = '';
            	for(var i=0;i<dataList.length;i++){
//            		var dataRound = 'roundBgB';
            		dataHtml=dataHtml+'<div id="'+dataList[i].id+'" class="ip_tooltip ip_img32" style="top: 0px; left: 0px;" ' +
            				'data-tooltipbg="bgwhite" data-round="roundBgB" data-button="person" data-animationtype="ltr-slide">'+
            				'<p>人员信息<hr>' +
	            				'<img src="'+dataList[i].photo+'" class="person-image" style="width:60px;height:70px;"/>' +
	            				'类型：'+dataList[i].KEY_VALUE+
	            				'<br>姓名：'+dataList[i].name+
	            				'<br>年龄：'+dataList[i].age+
	            				'<br>电话：'+dataList[i].phone+
	            				'<br>其他：'+dataList[i].description+
            				'</p></div>';
				}
				if(dataHtml!=''){
					$('#locations_${id}').html(dataHtml);
					$("#map_${id}").iPicture({id:"locations_${id}"});
				}
                positionTask.delay(refreshTime);
           	},
		    failure: function(xhr){
		    	positionTask.delay(refreshTime);
		    }
        })
	}
	function changeFunction(current_map_type,isHideSearch){
		if(isHideSearch){
			searchRoutePanel.collapse(false);
		}
		$("#preview_div_${id}").remove();//删除更改地图的html
		map_type = current_map_type;//功能切换
		//实时轨迹查询时的前一个位置
		$('#locations_${id}').html('');//删除位置信息
		try{
			positionTask.cancel();
		}catch(e){
		}
		try{
			historyRouteTask.cancel();
		}catch(e){
		}
		try{
			currentRouteTask.cancel();
		}catch(e){
		}
		try{
			currentPositionTask.cancel();
		}catch(e){
		}
	}
	
	var centerPanel=new Ext.Panel({
		header : false,
		region:'center',
		layout : 'fit',
		border : false,
//		autoHeight:true,
//		autoWidth:true,
//		autoScroll : true,
        html:'<div id="map_${id}" data-interaction="hover">' +
        		'<div class="ip_slide">' +
        			'<img id="${id}" class="ip_tooltipImg"  src="${map}"/>' +
        			'<div id="devices_${id}" class="devices">' +
	        		'</div>' +
        			'<div id="locations_${id}" class="locations">' +
	        		'</div>' +
        		'</div>' +
        	'</div>',
        listeners:{
        	"afterrender":function(panel){
		        //加载信号收集站点
        		var map = new SpryMap({id : "map_${id}",
                         height: 300,
                         width: 300,
                         startX: 0,
                         startY: 0,
                         cssClass: ""});
                changMapSize()
		        loadDevices();
		        loadPersons();
          	},
          	"bodyresize":function(){
          		changMapSize();
//				leftPanel.setWidth(mainPanel.getWidth()*0.2);
			}
        }
    });
    function changMapSize(){
    	var divHeight = $('#map_${id}').parent().parent().height();
    	var divWidth = $('#map_${id}').parent().parent().width();
    	$('#map_${id}').parent().height(divHeight);
    	$('#map_${id}').parent().width(divWidth);
    }
    
    //功能按钮
    var navigationBar = new Ext.Panel({
    	header : false,
    	id:"navigation_bar_${id}",
    	region : 'north',
    	bodyStyle:'padding:10px 10px 10px',
//    	border:false,
//		title : '导航栏',
		collapsible: true,
		autoScroll : true,
//		autoHeight:true,
		height:95,
		layout:'fit',
		align : 'center',
		html : '<a href="#" title="实时定位" class="location-button">' +
					'<img style="border-radius: 10px;" src="hr/img/icons/real-time-location.png"/></a>' +
		        '<a href="#" title="实时轨迹查询" class="location-button">' +
					'<img style="border-radius: 10px;" src="hr/img/icons/current_route.png"/></a>'+
		        '<a href="#" title="历史轨迹查询" class="location-button">' +
		        	'<img style="border-radius: 10px;" src="hr/img/icons/history_route.png"/></a>',
//		        	+
//		        '<a onclick="Ext.getAllMaps();" title="地图切换" class="location-button">' +
//		        	'<img style="border-radius: 10px;" src="hr/img/icons/chang_map.png"/></a>' 
//		        	+
//				'<a id="full_screen" href="#" onclick="Ext.full_screen_location();" title="全屏" class="location-button">' +
//		        	'<img style="border-radius: 10px;" src="hr/img/icons/full_screen.png"/></a>'
		listeners:{
        	"afterrender":function(panel){
        		$($('#navigation_bar_${id} a img').get(0)).on('click',function(){
//					Ext.realTimeLocation();
        			changeFunction('position',true);
//					map_type = 'position';
					positionTask.delay(0);
				});
				$($('#navigation_bar_${id} a img').get(1)).on('click',function(){
//					Ext.currentRoute();
					changeFunction('currentRoute',true);
					$("#start_date_${id}").parent().parent().parent().hide();
					$("#end_date_${id}").parent().parent().parent().hide();
					searchRoutePanel.expand(false);
				});
				$($('#navigation_bar_${id} a img').get(2)).on('click',function(){
//					Ext.historyRoute();
					changeFunction('historyRoute',true);
					$("#start_date_${id}").parent().parent().parent().show();
					$("#end_date_${id}").parent().parent().parent().show();
					searchRoutePanel.expand(false);
				});
          	}
		}
    });
    var searchRoutePanel = new Ext.FormPanel({
    	id:'search_panel_${id}',
//    	header : false,
    	title : '详情',
    	region : 'center',
      	labelWidth: 65, 
        frame:false,
      	bodyStyle:'padding:10px 10px',
//        width: 250,
      	defaults: {
//      		width: 100,
      		anchor: '100%'
      	},
      	autoHeight:true,
//		autoWidth:true,
      	border:false,
      	defaultType: 'textfield',
        buttonAlign :'center',
        collapsible:false,
        items: [{
        	xtype:'hidden',
        	name:'communityId',
        	value:'${id}'
        },{
        	fieldLabel: '姓名',
            name: 'name',
            maxLength:200
        },{
        	id:'start_date_${id}',
            xtype : 'datetimefield',
            name : 'startDate',
            editable:false,
            format :'Y-m-d H:i:s', 
            fieldLabel : '开始时间',
            border:false
//            value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-6),'Y-m-d')  
        },{
        	id:'end_date_${id}',
            xtype : 'datetimefield',
            name : 'endDate',
            editable:false,
            format : 'Y-m-d H:i:s', 
            fieldLabel : '结束时间',
            border:false
//            value:Ext.util.Format.date(new Date(),'Y-m-d')
        }],
        buttons:[{
         	text:'查询',
         	handler: function () {
         		changeFunction(map_type,false);
         		if(map_type == 'historyRoute'){
         			getHistoryRoute();
         		}else if(map_type = 'currentRoute'){
         			previousPosition = '';
         			currentPositionTask.delay(0);
         		}
		    }
        },{
         	text:'重置',
         	handler: function () {
		      	searchRoutePanel.getForm().reset();
		    }
        }],
        listeners:{
        	"afterrender":function(panel){
        		leftPanel.doLayout();
		        searchRoutePanel.collapse(false);
          	}
		}
    });
    var leftPanel = new Ext.Panel({
		title : '导航栏',
		id:"west_panel_${id}",
		region : 'west',
		width: 250,
		collapsible:true,
        collapseMode:'mini',
		layout : 'border',
		items :[navigationBar,searchRoutePanel],
		listeners:{
            "bodyresize":function(){
            	try{
            		var picturePreviewHeight = $('#west_panel_${id}').height() - $('#navigation_bar_${id}').height();
	            	var picture_preview_html = $('#preview_div_${id} .jcarousel-skin-tango .jcarousel-container-vertical').html();
	            	if(picturePreviewHeight > 280 && picture_preview_html != undefined && picture_preview_html != ""){
	            		$('#preview_div_${id} .jcarousel-skin-tango .jcarousel-container-vertical').height(picturePreviewHeight-140);
				    	$('#preview_div_${id} .jcarousel-skin-tango .jcarousel-clip-vertical').height(picturePreviewHeight-140);
	            	}
            	}catch(e){
            	}
			}
		}
		
    });
    
    var mainPanel = new Ext.Panel({
		autoWidth:true,
    	header : false,
		title : '首页',
		border : false,
		layout : 'border',
		items : [leftPanel,centerPanel]
	});
    
	return mainPanel;
})();