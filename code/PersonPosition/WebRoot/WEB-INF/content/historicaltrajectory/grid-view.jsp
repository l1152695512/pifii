<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


//全屏显示：尝试设置height去实现
(function(){
	function getRoute(){
	   	Ext.Ajax.request({
       		url : 'historicaltrajectory/person-route.action',
       		params: searchRoutePanel.getForm().getValues(),
            success : function(xhr) {
            	var responseText = Ext.decode(xhr.responseText);
            	var dataList = responseText.list;
            	var startPoint = '';
            	if(dataList.length > 0){
            		startPoint='<div class="ip_tooltip ip_img32" style="top: '+dataList[0].locationY+'px; left: '+ dataList[0].locationX+
            				'px;" data-tooltipbg="bgwhite" data-round="roundBgB" data-button="person-location-route" data-animationtype="ltr-slide">'+
            				'<p>姓名：'+dataList[0].name+
            				'<br>电话：'+dataList[0].phone+
            				'<br>邮箱：'+dataList[0].email+
            				'</p></div>';
	            	$('#routes').append(startPoint);
//	            	document.getElementById("routes").innerHTML=startPoint;
	            	$("#iPicture_route").iPictureInsert($('#routes').find('.ip_tooltip:last'));
//					jQuery( "#iPicture_route" ).iPicture();
            	}
            	drowNext(dataList,0);
           	},
		    failure: function(xhr){
		    }
        })
	};
	function drowNext(locations,index){
		if(index+1<locations.length){
			drowRoute(locations,index);
		}
	}
	var locationRadius = 2;
	function drowRoute(locations,index){
		var startPoint = locations[index];
		var endPoint = locations[index+1];
		var times = 1;
		var lengthXY = Math.sqrt(Math.pow(startPoint.locationX - endPoint.locationX,2)+Math.pow(startPoint.locationY - endPoint.locationY,2))
//		var startXY = Math.sqrt(Math.pow(startPoint.locationX,2)+Math.pow(startPoint.locationY,2));
//		var endXY = Math.sqrt(Math.pow(endPoint.locationX,2)+Math.pow(endPoint.locationY,2));
		var routeTask = new Ext.util.DelayedTask(function(){
			var dataTooltipbg = "";
			var dataRound = "";
			var tipsDescription = "";
			var animationtype = "";
			var currentLengthXY = times*locationRadius;
			var positionX = Number(startPoint.locationX)+currentLengthXY*(endPoint.locationX - startPoint.locationX)/lengthXY;
			var positionY = Number(startPoint.locationY)+currentLengthXY*(endPoint.locationY - startPoint.locationY)/lengthXY;
			var currentXY = Math.sqrt(Math.pow(positionX,2)+Math.pow(positionY,2));
//			if(currentXY < Math.min(startXY,endXY) || currentXY > Math.max(startXY,endXY)){
			if(currentLengthXY >= lengthXY){
				positionX = endPoint.locationX;
				positionY = endPoint.locationY;
				dataTooltipbg = "bgwhite";
				dataRound = "roundBgB";
				animationtype = "ltr-slide";
				tipsDescription = '<p>姓名：'+endPoint.name+
            				'<br>年龄：'+endPoint.age+
            				'<br>电话：'+endPoint.phone+
            				'<br>时间：'+endPoint.date+
            				'</p>';
			}
			var tooltips_add = '<div class="ip_tooltip ip_img32" style="top: ' + positionY + 'px; left: ' + positionX +
				'px;" data-tooltipbg="'+dataTooltipbg+'" data-round="'+dataRound+
				'" data-button="person-location-route" data-animationtype="'+animationtype+'">'+
				tipsDescription+
				'</div>';
			$('#routes').append(tooltips_add);
			$("#iPicture_route").iPictureInsert($('#routes').find('.ip_tooltip:last'));
			times = times+1;
//			if(currentXY >= Math.min(startXY,endXY) && currentXY <= Math.max(startXY,endXY)){
			if(currentLengthXY < lengthXY){
				routeTask.delay(0);
			}else{
				drowNext(locations,index+1);
			}
			
		});
		routeTask.delay(0);
	}

	//	var all_html;
	var full_screen_win = '';
	//north-region     title-bar     ext-gen13
	Ext.full_screen_location = function(){
//		console.debug(Ext.getCmp('north-region'));
//		console.debug(Ext.getCmp('title-bar'));
//		console.debug(Ext.getCmp('ext-gen13'));
		
//		Ext.getCmp('north-region').hide();
//		Ext.getCmp('north-region').doLayout();
//		Ext.getCmp('north-region').adjustBodyHeight();
//		Ext.getCmp('north-region').adjustBodyWidth();
//		Ext.getCmp('north-region').adjustPosition();
//		Ext.getCmp('north-region').adjustSize();
		
//		$('#north-region').fadeOut();
		$('#north-region').hide();
		$('#title-bar').hide();
		$('#ext-gen13').hide();
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
		
		
	}
	Ext.EventManager.onWindowResize(function(w,h){ 
//		Ext.getCmp('north-region').doLayout();
	});
	Ext.full_screen_location_exit = function(){
//		alert(all_html_object.html());
//		$('body').html('');
//		$('body').html(all_html);
//		$('body').append($(all_html));
		
		
		$('#north-region').show();
		$('#title-bar').show();
		$('#ext-gen13').show();
		
	}
	
	Ext.changeMap = function(map_path,map_id){
		Ext.Msg.confirm('确认', '你确定要使用该图片作为地图？',function(btn, text){
			if (btn == 'yes') {		
				Ext.Msg.wait('正在更换地图....', '提示');
				Ext.Ajax.request({
					url : 'location/add-device.action',
					params : {mapId : map_id},
					success : function(form,action){
						Ext.Msg.hide(); 
						$("#location_map").attr("src", map_path);
						searchRoutePanel.collapse();
						$("#picture-preview-div").remove();
 					},
 					failure:function(form,action){
 						Ext.Msg.hide(); 
 						Ext.Msg.alert("温馨提醒","更换地图失败，稍后请重试！");
 					}
				});
			}
		});
	}
	Ext.getAllMaps = function(){
		Ext.Msg.wait('正在获取所有地图....', '提示');
		Ext.Ajax.request({
			url : 'location/get-all-maps.action',
			success : function(form,action){
				Ext.Msg.hide(); 
				var picturePreviewDiv = '<div id="picture-preview-div" class="picture-preview">' +
							'<ul id="picture-preview-id" class="jcarousel jcarousel-skin-tango">';
				var dataList = Ext.decode(form.responseText).list;
				for(var i=0;i<dataList.length;i++){
					picturePreviewDiv = picturePreviewDiv+
						'<li><img src="'+dataList[i].path+'" onDblClick=\'Ext.changeMap("'+dataList[i].path+'","'+dataList[i].id+'")\' width="200" height="100" alt=""/></li>';
				}
				picturePreviewDiv = picturePreviewDiv+'</ul></div>';
				$('#location-search-panel').append(picturePreviewDiv);
				$('#picture-preview-id').jcarousel({
			        vertical: true,
			        scroll: 2
			    });
			    var picturePreviewHeight = $('#location-search-panel').parent().parent().height();
			    $('#picture-preview-div .jcarousel-skin-tango .jcarousel-container-vertical').height(picturePreviewHeight-80);
			    $('#picture-preview-div .jcarousel-skin-tango .jcarousel-clip-vertical').height(picturePreviewHeight-80);
			    searchRoutePanel.expand();
			},
			failure:function(form,action){
				Ext.Msg.hide(); 
				Ext.Msg.alert("温馨提醒","获取地图失败，稍后请重试！");
			}
		});
	}
	
	Ext.addDevicePoint = function(event){
		var locationX = event.layerX;
		var locationY = event.layerY;
		Ext.Msg.confirm('确认', '确定要在此处添加信号收集器？',function(btn, text){
			if (btn == 'yes') {		
					Ext.Msg.wait('添加数据中....', '提示');
					Ext.Ajax.request({
//						async : false,
						url : 'location/add-device.action',
						params : {locationX : locationX,locationY:locationY},
						success : function(form,action){
							Ext.Msg.hide(); 
							var obj = Ext.decode(form.responseText);
							var station_add = '<div id="'+obj.msg+'" onDblClick=\'Ext.removeDevice(event,\"'+obj.msg+'\");\' class="ip_tooltip ip_img32" style="top: ' + locationY + 'px; left: ' + locationX +
								'px;" data-tooltipbg="bgwhite" data-round="roundBgB" data-button="person-location-route" data-animationtype="ltr-slide">'+
//								tipsDescription+
								'</div>';
							$('#routes').append(station_add);
							$("#iPicture_route").iPictureInsert($('#routes').find('.ip_tooltip:last'));
	 					},
	 					failure:function(form,action){
	 						Ext.Msg.hide(); 
	 						Ext.Msg.alert("温馨提醒","添加失败，稍后请重试！");
	 					}
					});
				}
		});
	}
	Ext.removeDevice = function(event,deviceId){
		event.stopPropagation();
		Ext.Msg.confirm('确认', '确定要删除此信号收集器？',function(btn, text){
			if (btn == 'yes') {
				Ext.Msg.wait('删除数据中....', '提示');
				Ext.Ajax.request({
					url : 'location/delete-device.action',
					params : {deviceId : deviceId},
					success : function(form,action){
						Ext.Msg.hide();
						$('#'+deviceId).remove();
 					},
 					failure:function(form,action){
 						Ext.Msg.hide(); 
 						Ext.Msg.alert("温馨提醒","删除失败，稍后请重试！");
 					}
				});
			}
		});
	}
	
	var centerPanel=new Ext.Panel({
		id:'map_panel',
		header : false,
		region:'center',
		layout : 'fit',
		border : false,
//		autoHeight:true,
//		autoWidth:true,
		autoScroll : true,
        html:'<div id="iPicture_route" data-interaction="hover">' +
        		'<div class="ip_slide">' +
        			'<img id="location_map" class="ip_tooltipImg" onDblClick="Ext.addDevicePoint(event);" src="${defaultMapPath}">' +
        			'<div id="routes">' +
	        		'</div>' +
        		'</div>' +
        	'</div>',
        listeners:{
        	"afterrender":function(panel){
				$('<a id="pause_location" href="#" onclick="Ext.getAllMaps();" title="暂停定位" class="location-button" style="left:290px;">' +
					'<img style="border-radius: 10px;" src="hr/img/icons/stop_location.png"/></a>' +
		        '<a id="full_screen" href="#" onclick="Ext.full_screen_location();" title="全屏" class="location-button" style="left:340px;">' +
		        	'<img style="border-radius: 10px;" src="hr/img/icons/full_screen.png"/></a>' +
		        '<a id="search_route" href="#" onclick="Ext.full_screen_location_exit();" title="搜索" class="location-button" style="left:390px;">' +
		        	'<img style="border-radius: 10px;" src="hr/img/icons/search_route.png"/></a>').insertAfter($('#location-search-panel'));
          	},
          	"bodyresize":function(){
//				leftPanel.setWidth(mainPanel.getWidth()*0.2);
			}
        }
    });
    var searchRoutePanel = new Ext.FormPanel({
    	id:'location-search-panel',
		title : '搜索',
		region : 'west',
//      	margins:'3',
      	labelWidth: 65, 
//        frame:true,
      	bodyStyle:'padding:20px 5px 20px',
        width: 250,
      	defaults: {
      		width: 100,
      		anchor: '100%'
      	},
      	autoHeight:true,
//		autoWidth:true,
      	border:false,
      	defaultType: 'textfield',
        buttonAlign :'center',
//        split:true,
        collapsible:true,
        collapseMode:'mini',
        items: [{
        	fieldLabel: '姓名',
            name: 'name',
            maxLength:200
        },{
            xtype : 'datetimefield',
            name : 'startDate',
            editable:false,
            format :'Y-m-d H:i:s', 
            fieldLabel : '开始时间',
            border:false
//            value:Ext.util.Format.date(Ext.Date.add(new Date(),Ext.Date.MONTH,-6),'Y-m-d')  
        },{
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
			 	getRoute();
		    }
        },{
         	text:'重置',
         	handler: function () {
		      	searchRoutePanel.getForm().reset();
		    }
        }],
        listeners:{
			"collapse":function(){
				$('#pause_location').attr("style", "left:40px;");
				$('#full_screen').attr("style", "left:90px;");
				$('#search_route').attr("style", "left:140px;");
          	},
          	"beforeexpand":function(){
				$('#pause_location').attr("style", "left:290px;");
				$('#full_screen').attr("style", "left:340px;");
				$('#search_route').attr("style", "left:390px;");
          	}
		}
    });
    var mainPanel = new Ext.Panel({
    	id:'location_full_screen',
//    	layout : 'fit',
//    	autoHeight:true,
		autoWidth:true,
//		autoScroll : true,
    	header : false,
		title : '轨迹路线',
		border : false,
		layout : 'border',
		items : [searchRoutePanel,centerPanel],
		listeners:{
            "bodyresize":function(){
            	var picturePreviewHeight = $('#location-search-panel').parent().parent().height();
            	var container = $('#picture-preview-div .jcarousel-skin-tango .jcarousel-container-vertical');
            	if(picturePreviewHeight > 180 && container != undefined && container != ""){
            		$('#picture-preview-div .jcarousel-skin-tango .jcarousel-container-vertical').height(picturePreviewHeight-80);
			    	$('#picture-preview-div .jcarousel-skin-tango .jcarousel-clip-vertical').height(picturePreviewHeight-80);
            	}
            	
//				leftPanel.setWidth(mainPanel.getWidth()*0.2);
			},
			"beforerender":function(panel){
				//showAlarm(panel);
          	}
		}
	});
    
	return mainPanel;
})();