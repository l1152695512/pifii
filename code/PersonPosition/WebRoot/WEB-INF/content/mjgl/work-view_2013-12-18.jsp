<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


//问题：当前tab关闭时结束所有的task;
(function(){
	var currentRoutePersonId = '';
	
	var mapObject;
//	var needDrawLine = false;//标识是实时定位还是实时轨迹
//	var isFirstPoint = true;//标识画实时轨迹时第一个点不用画线
	
	var refreshTime = -3000;//实时定位和实时轨迹的数据刷新间隔（毫秒），最好小于等于后台数据插入的间隔，以保证每一个轨迹点都能在地图上呈现
	var walkPointToPointTimes = 1000;//从上一个轨迹移动到新轨迹使用的时间，时间越大移动的越慢，理论上如果该值等于refreshTime的话，人的移动会比较有连贯性。
	var maxRoutePointDistance = 0.2;//决定画路线时点的密度，值越大，密度越小
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
	            		if("1" == dataList[i].forHelp){
	            			change_person_style(dataList[i].id,'roundBgR');
	            		}else{
	            			$('#'+dataList[i].id).find('[class ^= roundBgR]').remove();
	            		}
	            		$('#'+dataList[i].id).animate(
	            			{top:dataList[i].locationY,left:dataList[i].locationX},
	            			{duration: walkPointToPointTimes,
	            				easing:'linear',
						    	start:function(animation){
						    		var left = parseFloat($(this).position().left);
									var top = parseFloat($(this).position().top);
						      		if(dataList[i].id==currentRoutePersonId){
//						      			var currentPointDistance = Math.sqrt(Math.pow((left-dataList[i].locationX),2)+Math.pow((top-dataList[i].locationY),2));
//										var drowRoutePointTimes = walkPointToPointTimes*maxRoutePointDistance/currentPointDistance;
//										drowLineTask.delay(0,drowLine,this,[$(this),dataList[i], drowRoutePointTimes]);
						      			var point1 = {'locationX':left,'locationY':top};
										var point2 = {'locationX':dataList[i].locationX,'locationY':dataList[i].locationY};
										var points = new Array();
										points[0] = point1;
										points[1] = point2;
						      			drowLine(points,0);
	            					}
						    	}
					  		});
					}
					if(refreshTime>=0){
						positionTask.delay(refreshTime);
					}
	           	},
			    failure: function(xhr){
			    	if(refreshTime>=0){
						positionTask.delay(refreshTime);
					}
			    }
	        })
		}
	});
//	var drowLineTask = new Ext.util.DelayedTask();
//	function drowLine(personLocation,endPoint,drowRoutePointTimes){
//		if($('#${id}').html() != undefined){//如果定位窗口没有关闭
//			var left = parseFloat(personLocation.position().left);
//			var top = parseFloat(personLocation.position().top);
////			console.debug("left="+left+"----top="+top);
//			if(left!=endPoint.locationX || top!=endPoint.locationY){
//				var tooltips_add = '<div class="route" style="top: ' + top + 'px; left: ' + left +
//					'px;"></div>';//<div class="route person-location-route"></div>
//				$('#locations_${id}').append(tooltips_add);
//				drowLineTask.delay(drowRoutePointTimes,drowLine,this,[personLocation,endPoint,drowRoutePointTimes]);
//			}
//		}
//	}
	//画线的方法
	var drowTask_location = new Ext.util.DelayedTask();
	var refreshRouteTime = 50;
	function drowLine(pointsArray,startIndex){
		if($('#${id}').html() != undefined){//如果定位窗口没有关闭
			if(pointsArray.length > startIndex+1){//如果还有下一个点
				var startPoint = pointsArray[startIndex];
				var endPoint = pointsArray[startIndex+1];
				if(startPoint.locationX == endPoint.locationX && startPoint.locationY == endPoint.locationY){
					drowLine(pointsArray,++startIndex);
				}else{
					$('#locations_${id}').append('<div class="route_div" style="top: '+startPoint.locationY+'px; left: '+startPoint.locationX+'px;width: 0px;"></div>');
					var lineObj = $('#locations_${id}').find('.route_div:last');
					var lineWidth = Math.sqrt(Math.pow((Number(startPoint.locationX)-Number(endPoint.locationX)),2)+
												Math.pow((Number(startPoint.locationY)-Number(endPoint.locationY)),2));
					var angle = ((Number(startPoint.locationX) > Number(endPoint.locationX) == Number(startPoint.locationY) > Number(endPoint.locationY))?"":"-")
								+ (Math.asin(Math.abs(Number(endPoint.locationY) - Number(startPoint.locationY))/lineWidth)*360/2/Math.PI);
					lineObj.css("transform", "rotate("+angle+"deg)");
					lineObj.animate(
						{width:lineWidth+"px"},
						{duration: 1000,
							easing:'linear',
							start:function(animation){
								drowTask_location.delay(0,drow,this,[$(this),startPoint,endPoint,lineWidth]);
							},
							complete:function(){
								drowLine(pointsArray,++startIndex);
							}
					});
				}
			}
		}
	}
	function drow(lineObj,startPoint,endPoint,lineWidth){
		if(lineObj && lineObj.width()<lineWidth && lineObj.is(":animated")){
			var top = startPoint.locationY + 1/2*lineObj.width()*((endPoint.locationY-startPoint.locationY)/lineWidth);
			var left = startPoint.locationX - 1/2*lineObj.width()*(1-(endPoint.locationX-startPoint.locationX)/lineWidth);
			lineObj.css({top:top+"px",left:left+"px"});
			drowTask_location.delay(refreshRouteTime,drow,this,[lineObj,startPoint,endPoint,lineWidth]);
		}
	}
	
	//加载信号收集站点
	function loadInformations(){
		Ext.Msg.wait('正在加载设备信息....', '提示');
		Ext.Ajax.request({
			url : 'devicemanage/get-devices.action',
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
				Ext.Msg.alert("温馨提醒","获取设备信息失败，稍后请重试！");
			}
		});
	}
	//加载人员信息
	function loadPersons(){
	   	Ext.Ajax.request({
       		url : 'location/get-persons.action',
       		params : {communityId : '${id}'},
            success : function(xhr) {
        		var responseText = Ext.decode(xhr.responseText);
            	var dataList = responseText.list;
            	//生成列表中的人员信息列表
            	var person_list_html = "";
            	for(var i=0;i<dataList.length;i++){
//            		$('#persons_information_div_${id}').append(
////            				'<div style="padding-top:10px;height:50px;">' +
//            					'<div id="person_info_'+dataList[i].id+'" style="height:50px;">' +
//	            					'<img src="'+dataList[i].photo+'" class="person-list-image"/>' +
//	            					'<div class="person-list-info">' +
//	            						''+dataList[i].KEY_VALUE +
//			            				'<br>'+dataList[i].name +
//			            				'<br>'+dataList[i].phone +
//	            					'</div>' +
//	            					'<img src="hr/img/icons/current_route.png" class="person-list-route"/>' +
//	            				'</div>' +	
////            				'</div>' +
//            				'<hr>');
            		
					person_list_html = person_list_html + 
								'<div data-person-id="'+dataList[i].id+'" style="height:50px;">' +
	            					'<img src="'+dataList[i].photo+'" class="person-list-image"/>' +
	            					'<div class="person-list-info">' +
	            						''+dataList[i].KEY_VALUE +
			            				'<br>'+dataList[i].name +
			            				'<br>'+dataList[i].phone +
	            					'</div>' +
	            					'<img src="hr/img/icons/current_route.png" class="person-list-route"/>' +
	            				'</div><hr>';
            	}
            	$('#persons_information_div_${id}').append(person_list_html);
            	$('#persons_information_div_${id}').find('.person-list-route').on('click',function(){
            		$('#persons_information_div_${id}').find('.person-list-route').hide();
            		$(this).show();
            		currentRoutePersonId = $(this).parent().data('personId');
					change_person_route();
//						currentRoutePersonId = $(this).parent().attr('id').substr(12);
//						$('#locations_${id}').find('.route').remove();
//						$('#'+currentRoutePersonId).prepend('<div class="roundBgY"></div><div class="roundBgYIn"></div><div class="roundBgYInner"></div>');
				});
        		$('#persons_information_div_${id}').find('.person-list-route').hide();
        		
				$('#persons_information_div_${id}').children().on('mouseover',function(){
					$(this).addClass("person-list-mouseover");
        			$(this).find('.person-list-route').show();
				});
        		$('#persons_information_div_${id}').children().on('mouseout',function(){
        			$(this).removeClass("person-list-mouseover");
        			if(currentRoutePersonId != $(this).data('personId')){
        				$(this).find('.person-list-route').hide();
        			}
				});
				$('#persons_information_div_${id}').children().on('click',function(){
					var thisPersonId = $(this).data('personId');
					$('#locations_${id}').find('[class ^= roundBgY]').remove();//删除地图上标记为实时轨迹的人员样式
					$(this).parent().find('.person-list-click').removeClass("person-list-click");//删除列表中点击后的人员样式
					$(this).addClass("person-list-click");
					change_person_style(thisPersonId,'roundBgY');
        			movePositionToMapCenter($('#'+thisPersonId).position().left,$('#'+thisPersonId).position().top);
				});
            	
            	
            	//生成地图上的人员定位点
            	var personLocationHtml = '';
            	for(var i=0;i<dataList.length;i++){
            		personLocationHtml=personLocationHtml+'<div id="'+dataList[i].id+'" class="ip_tooltip ip_img32" style="top: -10px; left: -10px;" ' +
            				'data-tooltipbg="bgwhite" data-round="" data-button="person" data-animationtype="ltr-slide" data-dictionary-id="'+dataList[i].dictionaryId+'">'+
            				'<p>人员信息<hr>' +
	            				'<img src="'+dataList[i].photo+'" class="person-image" style="width:60px;height:70px;"/>' +
	            				'类型：'+dataList[i].KEY_VALUE+
	            				'<br>姓名：'+dataList[i].name+
	            				'<br>年龄：'+dataList[i].age+
	            				'<br>电话：'+dataList[i].phone+
	            				'<br>其他：'+dataList[i].description.replace(new RegExp('<br>', 'g'), '\n')+
            				'</p></div>';
				}
				$('#locations_${id}').html(personLocationHtml);
				$("#map_${id}").iPicture({id:"locations_${id}"});
				$('#locations_${id}').find('.person').on('click',function(){
					var personType = $(this).parent().data('dictionaryId');
//					if("2"==personType){//安保人员
						Ext.Msg.wait('调度信息获取....', '提示');
						Ext.Ajax.request({
							url:'location/add-dispatch-task.action',
							params:{personId:$(this).parent().attr('id'),mapPath:'${map}'},
							success:function(xhr){
								Ext.Msg.hide(); 
								var dispatchWin = eval(xhr.responseText);
								dispatchWin.show();
							},
							failure:function(xhr){
								Ext.Msg.hide(); 
								Ext.Msg.alert('提示','暂时无法连接服务器，稍后请重试！');
							}
						});
//					}
				});
				positionTask.delay(0);//成功后加载人员实时位置信息
           	},
		    failure: function(xhr){
		    }
        })
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
        $('#map_${id}').animate({top:newTop+'px',left:newLeft+'px'},300); 
    }
    var clear_route = new Ext.Action({
		text : '清除路线',
		iconCls : 'icon-clear-route',
		handler : function() {
			change_person_route();
		}
	});
	function change_person_route(){
		try{
			drowLineTask.cancel();
		}catch(e){
		}
		$('#locations_${id}').find('.route').remove();//删除轨迹点
//		$('#'+currentRoutePersonId).find('[class ^= roundBgY]').remove();//删除标记为实时轨迹的人员样式
//		if(new_person != ''){
//			change_person_style(new_person,'roundBgY');
//		}
	}
	function change_person_style(personId,styleClass){
		if(personId != '' && $('#'+personId).find('[class ^= roundBgR]').length == 0){//如果该人员的样式不是求救样式，则更改样式
			$('#'+personId).find('[class ^= roundBg]').remove();
			if(styleClass != ''){
				$('#'+personId).prepend('<div class="'+styleClass+'"></div><div class="'+styleClass+'In"></div><div class="'+styleClass+'Inner"></div>');
			}
		}
	}
    var personsInformationPanel = new Ext.Panel({
//    	header : false,
		title : '人员列表',
    	id:"persons_information_panel_${id}",
    	region : 'west',
    	width: 250,
//    	bodyStyle:'padding:10px 10px',
    	collapsible:true,
    	collapseMode:'mini',
//    	border:false,
		autoScroll : true,
		layout:'fit',
		tbar: [clear_route],
		html : '<div id="persons_information_div_${id}"></div>',
		listeners:{
        	"afterrender":function(panel){
//        		 loadInformations();
        		 loadPersons();
			}
		}
    });
    
    var mainPanel = new Ext.Panel({
		autoWidth:true,
    	header : false,
		title : '首页',
		border : false,
		layout : 'border',
		items : [personsInformationPanel,centerPanel],
		listeners: {  
             
        }  
	});
	return mainPanel;
})();