<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


//问题：1.历史轨迹功能里的时间框，换成可以显示时分秒的组件；2.当当前tab关闭时结束所有的task
(function(){
	var currentRoutePersonId = '';//临时数据
	
	var mapObject;
	var map_type = '';
//	var needDrawLine = false;//标识是实时定位还是实时轨迹
//	var isFirstPoint = true;//标识画实时轨迹时第一个点不用画线
	
	var refreshTime = 15000;//实时定位和实时轨迹的数据刷新间隔（毫秒），最好小于等于后台数据插入的间隔，以保证每一个轨迹点都能在地图上呈现
	var walkPointToPointTimes = 2000;//从上一个轨迹移动到新轨迹使用的时间，时间越大移动的越慢，理论上如果该值等于refreshTime的话，人的移动会比较有连贯性。
	var maxRoutePointDistance = 1.5;//决定画路线时点的密度，值越大，密度越小
	//实时定位查询或实时轨迹查询
	var positionTask = new Ext.util.DelayedTask(function(){
//		alert($('#${id}').html());
		if($('#${id}').html() != undefined){//如果定位窗口没有关闭
		   	Ext.Ajax.request({
	       		url : 'location/person-location.action',
	       		params : {communityId : '${id}'},
	            success : function(xhr) {
	        		var responseText = Ext.decode(xhr.responseText);
	            	var dataList = responseText.list;
	            	var dataHtml = '';
	            	for(var i=0;i<dataList.length;i++){
	            		$('#'+dataList[i].id).animate(
	            			{top:dataList[i].locationY,left:dataList[i].locationX},
	            			{duration: walkPointToPointTimes,
	            				easing:'linear',
						    	start:function(animation){
						    		var left = parseFloat($(this).position().left);
									var top = parseFloat($(this).position().top);
						      		if(dataList[i].id==currentRoutePersonId){
						      			var currentPointDistance = Math.sqrt(Math.pow((left-dataList[i].locationX),2)+Math.pow((top-dataList[i].locationY),2));
										var drowRoutePointTimes = walkPointToPointTimes*maxRoutePointDistance/currentPointDistance;
										drowLineTask.delay(0,drowLine,this,[$(this),dataList[i], drowRoutePointTimes]);
	            					}
						    	}
					  		});
					}
	                positionTask.delay(refreshTime);
	           	},
			    failure: function(xhr){
			    	positionTask.delay(refreshTime);
			    }
	        })
		}
	});
	var drowLineTask = new Ext.util.DelayedTask();
	function drowLine(personLocation,endPoint,drowRoutePointTimes){
		if($('#${id}').html() != undefined){//如果定位窗口没有关闭
			var left = parseFloat(personLocation.position().left);
			var top = parseFloat(personLocation.position().top);
//			console.debug("left="+left+"----top="+top);
			if(left!=endPoint.locationX || top!=endPoint.locationY){
				drowLineTask.delay(drowRoutePointTimes,drowLine,this,[personLocation,endPoint,drowRoutePointTimes]);
			}
			var tooltips_add = '<div class="route" style="top: ' + top + 'px; left: ' + left +
					'px;"></div>';//<div class="route person-location-route"></div>
			$('#locations_${id}').append(tooltips_add);
		}
	}
	//历史轨迹查询
	function getHistoryRoute(personId){//搜索框中会加一个personId
	   	Ext.Ajax.request({
       		url : 'location/history-route.action',
//       		params: {personId:personId},
       		params: searchRoutePanel.getForm().getValues(),
            success : function(xhr) {
            	var responseText = Ext.decode(xhr.responseText);
            	var dataList = responseText.list;
            	var personId = searchRoutePanel.getForm().getValues().personId;
//            	console.debug('dataList.length='+dataList.length);
            	if(dataList.length > 0){
            		$('#'+personId).animate(
            			{top:dataList[0].locationY,left:dataList[0].locationX},
            			{duration: 0,
							easing:'linear',
					    	complete:function(animation){
					    		if(dataList.length > 1){
				            		drowToNextPoint(personId,dataList,1);
				            	}
					    	}
			  			});
            	}
           	},
		    failure: function(xhr){
		    }
        })
	};
	function drowToNextPoint(personId,points,index){
		if(index>=points.length){
			return;
		}
//		console.debug('index='+index);
		$('#'+personId).animate(
			{top:points[index].locationY,left:points[index].locationX},
			{duration: walkPointToPointTimes,
				easing:'linear',
		    	start:function(animation){
//	      			console.debug('drow route');
	      			var left = parseFloat($(this).position().left);
					var top = parseFloat($(this).position().top);
	      			var currentPointDistance = Math.sqrt(Math.pow((left-points[index].locationX),2)+Math.pow((top-points[index].locationY),2));
					var drowRoutePointTimes = walkPointToPointTimes*maxRoutePointDistance/currentPointDistance;
					drowLineTask.delay(0,drowLine,this,[$(this),points[index],drowRoutePointTimes]);
		    	},
		    	complete:function(animation){
//	      			console.debug('drow next point');
		    		if(map_type == 'historyRoute'){
	      				drowToNextPoint(personId,points,++index);
		    		}
		    	}
  		});
	}

	//加载信号收集站点,成功后加载人员信息
	function loadInformations(){
		Ext.Msg.wait('正在加载设备信息....', '提示');
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
				loadPersons();
			},
			failure:function(form,action){
				Ext.Msg.hide(); 
				Ext.Msg.alert("温馨提醒","获取设备信息失败，稍后请重试！");
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
            	//生成列表中的人员信息列表
            	for(var i=0;i<dataList.length;i++){
            		$('#persons_information_div_${id}').append(
//            				'<div style="padding-top:10px;height:50px;">' +
            					'<div id="person_info_'+dataList[i].id+'" style="height:50px;">' +
	            					'<img src="'+dataList[i].photo+'" class="person-list-image"/>' +
	            					'<div class="person-list-info">' +
	            						''+dataList[i].KEY_VALUE +
			            				'<br>'+dataList[i].name +
			            				'<br>'+dataList[i].phone +
	            					'</div>' +
//	            					'<img src="hr/img/icons/history_route.png" class="person-list-route person-list-history-route"/>' +
	            					'<img src="hr/img/icons/current_route.png" class="person-list-route person-list-current-route"/>' +
	            				'</div>' +	
//            				'</div>' +
            				'<hr>');
            		$('#person_info_'+dataList[i].id).find('.person-list-history-route').on('click',function(){
            			showSearchHistoryRoutePanel();
					});
					$('#person_info_'+dataList[i].id).find('.person-list-current-route').on('click',function(){
						currentRoutePersonId = $(this).parent().attr('id').substr(12);
						$('#locations_${id}').find('.route').remove();
//						mapObject.MoveMap(0, 0);
					});
            		$('#person_info_'+dataList[i].id).find('.person-list-route').hide();
            		
					$('#person_info_'+dataList[i].id).on('mouseover',function(){
	        			$(this).css("background","#BCE2F4"); 
	        			$(this).find('.person-list-route').show();
					});
            		$('#person_info_'+dataList[i].id).on('mouseout',function(){
	        			$(this).css("background","#DFEEF6"); 
	        			$(this).find('.person-list-route').hide();
					});
					$('#person_info_'+dataList[i].id).on('click',function(){
						var thisPersonId = $(this).attr('id').substr(12);
	        			movePositionToMapCenter($('#'+thisPersonId).position().left,$('#'+thisPersonId).position().top);
					});
            	}
            	
            	
            	//生成地图上的人员定位点
            	var personLocationHtml = '';
            	for(var i=0;i<dataList.length;i++){
            		personLocationHtml=personLocationHtml+'<div id="'+dataList[i].id+'" class="ip_tooltip ip_img32" style="top: -10px; left: -10px;" ' +
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
				$('#locations_${id}').html(personLocationHtml);
				$("#map_${id}").iPicture({id:"locations_${id}"});
				positionTask.delay(0);//成功后加载人员实时位置信息
           	},
		    failure: function(xhr){
		    }
        })
	}
	function cancelLocationPersons(){
		$('#locations_${id}').children().animate({top:'-10px',left:'-10px'},0); 
	}
	function changeFunction(current_map_type){
		map_type = current_map_type;//功能切换
//		$('#locations_${id}').html('');//删除位置信息
//		cancelLocationPersons();
		$('#locations_${id}').find('.route').remove();//删除轨迹点
		try{
			positionTask.cancel();
		}catch(e){
		}
		try{
			drowLineTask.cancel();
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
		        //给地图添加拖动效果
        		mapObject = new SpryMap({id : "map_${id}",
                         height: parseFloat('${mapHeightPixel}'),
                         width: parseFloat('${mapWidthPixel}'),
                         startX: 0,
                         startY: 0,
                         cssClass: ""});
                changMapSize();
          	},
          	"bodyresize":function(){
          		changMapSize();
          		refreshMapPosition();
			}
        }
    });
    function changMapSize(){//当窗口改变时，更改地图的显示尺寸，保证图片可以拖动显示全部
    	var divHeight = $('#map_${id}').parent().parent().height();
    	var divWidth = $('#map_${id}').parent().parent().width();
    	$('#map_${id}').parent().height(divHeight);
    	$('#map_${id}').parent().width(divWidth);
    }
    //解决当地图显示区域为右下角时，再拉大浏览器窗口时会出现地图无法全部填充centerPanel的情况
    //要考虑的问题：1.地图可填充满centerPanel,但未填充满；2.地图已近展示到最大了还是无法展示全部的地图
    function refreshMapPosition(){
    	//可视窗口的大小
    	var mapHeight = parseFloat($('#map_${id}').parent().height());
    	var mapWidth = parseFloat($('#map_${id}').parent().width());
    	//地图相对于centerPanel的偏移量
    	var position = $('#map_${id}').position(); 
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
    	$('#map_${id}').animate({top:positionTop+'px',left:positionLeft+'px'},0); 
    }
    
    function movePositionToMapCenter(targetX,targetY){
    	//可视窗口的大小
    	var mapHeight = parseFloat($('#map_${id}').parent().height());
    	var mapWidth = parseFloat($('#map_${id}').parent().width());
    	//地图相对于centerPanel的偏移量
    	var position = $('#map_${id}').position(); 
    	var positionLeft = parseFloat(position.left);//为负值
    	var positionTop = parseFloat(position.top);//为负值
    	
    	var centerX = Math.abs(positionLeft)+mapWidth/2;
    	var centerY = Math.abs(positionTop)+mapHeight/2;
    	
    	var newLeft = positionLeft + (centerX - targetX);
    	var newTop = positionTop + (centerY - targetY);
    	
    	var maxOffsetLeft = parseFloat(mapObject.width) - mapWidth;
    	var maxOffsetTop = parseFloat(mapObject.height) - mapHeight;
    	if(newLeft > 0){
    		newLeft = 0;
    	}
    	if(Math.abs(newLeft) > maxOffsetLeft){
    		newLeft = 0-maxOffsetLeft;
    	}
    	if(newTop > 0){
    		newTop = 0;
    	}
    	if(Math.abs(newTop) > maxOffsetTop){
    		newTop = 0-maxOffsetTop;
    	}
    	
//    	alert("targetX="+targetX+"---targetY="+targetY+
//    			"\nmapHeight="+mapHeight+"---mapWidth="+mapWidth+
//    			"\npositionLeft="+positionLeft+"---positionTop="+positionTop+
//    			"\ncenterX="+centerX+"---centerY="+centerY+
//    			"\nnewOffsetLeft="+newLeft+"---newOffsetTop="+newTop);
//    	$('#map_${id}').css({"left":newOffsetLeft+"px","top":newOffsetTop+"px"});
//		document.getElementById('map_${id}').style.left = (positionLeft-10)+"px";
//        document.getElementById('map_${id}').style.top = (positionLeft-10)+"px";
        $('#map_${id}').animate({top:newTop+'px',left:newLeft+'px'},300); 
    }
    
    function hideSearchHistoryRoutePanel(){
//    	searchHistoryRoutePanel.collapse(false);
    	$('#search_route_panel_${id}').hide(0);
//    	$('#search_route_panel_${id}').height('0px');
    	searchHistoryRoutePanel.doLayout();
    	leftPanel.doLayout();
    }
    function showSearchHistoryRoutePanel(){
//    	searchHistoryRoutePanel.expand(false);
    	$('#search_route_panel_${id}').show();
    	$('#search_route_panel_${id}').height('100px');
    	searchHistoryRoutePanel.doLayout();
    	leftPanel.doLayout();
    	$('#search_route_panel_${id}').find('form').height('42px')
    }
    
    var searchHistoryRoutePanel = new Ext.FormPanel({
    	id:'search_route_panel_${id}',
    	header : false,
//    	title : '历史轨迹搜索',
    	region : 'north',
      	labelWidth: 65, 
//        frame:false,
        height:100,
      	bodyStyle:'padding:10px 10px',
      	defaults: {
      		anchor: '100%'
      	},
      	border:false,
      	defaultType: 'textfield',
        buttonAlign :'center',
//        layout:'fit',//使用后时间文本框的fieldLabel不会显示
//        collapsible:true,
        items: [{
        	xtype:'hidden',
        	id:'history_route_${id}',
        	name:'personId'
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
         		hideSearchHistoryRoutePanel();
         		getHistoryRoute();
		    }
        },{
         	text:'重置',
         	handler: function () {
		      	searchHistoryRoutePanel.getForm().reset();
		    }
        }],
        listeners:{
        	"afterrender":function(panel){
        		hideSearchHistoryRoutePanel();
          	}
		}
    });
    var personsInformationPanel = new Ext.Panel({
    	header : false,
//		title : '人员信息',
    	id:"persons_information_panel_${id}",
    	region : 'center',
//    	bodyStyle:'padding:10px 10px',
    	border:false,
		autoScroll : true,
		layout:'fit',
		html : '<div id="persons_information_div_${id}"></div>',
		listeners:{
        	"afterrender":function(panel){
        		 loadInformations();
			}
		}
    });
    
    var leftPanel = new Ext.Panel({
		title : '人员列表',
		id:"west_panel_${id}",
		region : 'west',
		width: 250,
		collapsible:true,
        collapseMode:'mini',
		layout : 'border',
		items :[searchHistoryRoutePanel,personsInformationPanel],
		listeners:{
            "afterrender":function(panel){
//        		searchHistoryRoutePanel.doLayout();
//        		personsInformationPanel.doLayout();
//        		leftPanel.doLayout();
			}
		}
    });
    
    var mainPanel = new Ext.Panel({
		autoWidth:true,
    	header : false,
		title : '首页',
		border : false,
		layout : 'border',
		items : [leftPanel,centerPanel],
		listeners: {  
             
        }  
	});
	return mainPanel;
})();