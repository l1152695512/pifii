<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var userId='${userId}';
	var mapObject;
	var rootNode = new Ext.tree.AsyncTreeNode({
		id : '0',
		text : '所有小区',
		draggable : false,
		expanded : true
	});
	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl : 'devicemanage/get-all-community.action'
	});
//	treeLoader.on("beforeload", function(loader, node) {
//        this.baseParams.nodeId = node.id;
//        this.baseParams.type = 1;
//    });
    var tree = new Ext.tree.TreePanel({
		title : '小区浏览',
	    region : 'west',
	    layout : 'fit',
	    frame:true,
//	    border:true,
//	    autoHeight:true,
		useArrows : true,
		autoScroll : true,
		enableDD : false,
		animate : true,
		width : 200,
		split:true,
		collapsible : true,
		collapseMode:'mini',
		containerScroll : true,
		rootVisible : true, 
		loader : treeLoader,
		root : rootNode,
 	    listeners:{    
      		'click':function(node, event) {
      			if(node.id != '0'){
      				changeCommunity(node.id,node.attributes.map);
      				mapObject.width = node.attributes.mapWidthPixel;
      				mapObject.height = node.attributes.mapHeightPixel;
      			}
      		 }
      	} 
	});
	function changeCommunity(communityId,map){
		$("#devices_manage_map").attr("src", map);
		$("#devices_manage_map").attr("communityId", communityId);
		loadDevices();
		
//		var point1 = {'locationX':62,'locationY':92};
//		var point2 = {'locationX':162,'locationY':192};
//		var point3 = {'locationX':232,'locationY':122};
//		var point4 = {'locationX':272,'locationY':222};
//		var points = new Array();
//		points[0] = point1;
//		points[1] = point2;
//		points[2] = point3;
//		points[3] = point4;
//		drowLine(points,0);
		
	}
	
	//画线的方法
	var drowTask = new Ext.util.DelayedTask();
	var refreshTime = 50;
	function drowLine(pointsArray,startIndex){
		if(pointsArray.length > startIndex+1){//如果还有下一个点
			console.debug("startIndex="+startIndex);
			var startPoint = pointsArray[startIndex];
			var endPoint = pointsArray[startIndex+1];
			$('#devices_manage_locations_${id}').append('<div class="route_div" style="top: '+startPoint.locationY+'px; left: '+startPoint.locationX+'px;width: 0px;"></div>');
			var lineObj = $('#devices_manage_locations_${id}').find('.route_div:last');
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
						drowTask.delay(0,drow,this,[$(this),startPoint,endPoint,lineWidth]);
					},
					complete:function(){
						drowLine(pointsArray,++startIndex);
					}
			});
		}
	}
	function drow(lineObj,startPoint,endPoint,lineWidth){
		if(lineObj && lineObj.width()<lineWidth && lineObj.is(":animated")){
			var top = startPoint.locationY + 1/2*lineObj.width()*((endPoint.locationY-startPoint.locationY)/lineWidth);
			var left = startPoint.locationX - 1/2*lineObj.width()*(1-(endPoint.locationX-startPoint.locationX)/lineWidth);
			lineObj.css({top:top+"px",left:left+"px"});
			drowTask.delay(refreshTime,drow,this,[lineObj,startPoint,endPoint,lineWidth]);
		}
	}
	
	var isdb;
	function loadDevices(){
		Ext.Msg.wait('正在加载地图信息....', '提示');
		Ext.Ajax.request({
			url : 'devicemanage/get-devices.action',
			params : {communityId : $("#devices_manage_map").attr("communityId")},
			success : function(form,action){
				Ext.Msg.hide(); 
				var responseText = Ext.decode(form.responseText);
            	var dataList = responseText.list;
            	var deviceHtml = '';
            	for(var i=0;i<dataList.length;i++){
            		deviceHtml=deviceHtml+getDeviceHtml(dataList[i]);;
				}
				$('#devices_manage_point').html(deviceHtml);
				$("#iPicture_device_manage").iPicture({id:"devices_manage_point"});
				//onDblClick=\'Ext.removeDevice(event,\"'+dataList[i].id+'\");\' 
				var deviceImageButton = $('#devices_manage_point').find('.ip_tooltip .button');
				deviceImageButton.on('dblclick',function(){
					isdb=true;
					if(userId=="4028812a3694db33013694e007560073"){
					removeDevice($(this).parent().attr('deviceId'));
					}
				});
				deviceImageButton.on('click',function(){
					isdb=false;
				    window.setTimeout(execute, 500);
				    var id = $(this).parent().attr('deviceId');
				    function execute(){
				        if(isdb!=false) return;
				        showDeviceForm(id,"","");
				    }
				});
			},
			failure:function(form,action){
				Ext.Msg.hide(); 
				Ext.Msg.alert("温馨提醒","获取站点信息失败，稍后请重试！");
			}
		});
	}
	
	function removeDevice(deviceId){
		Ext.Msg.confirm('确认', '确定要删除此信号收集器？',function(btn, text){
			if (btn == 'yes') {
				Ext.Msg.wait('删除数据中....', '提示');
				Ext.Ajax.request({
					url : 'devicemanage/delete-device.action',
					params : {deviceId : deviceId},
					success : function(form,action){
						Ext.Msg.hide();
						loadDevices();
 					},
 					failure:function(form,action){
 						Ext.Msg.hide(); 
 						Ext.Msg.alert("温馨提醒","删除失败，稍后请重试！");
 					}
				});
			}
		});
	}
	

	Ext.modifyDevice = function(deviceId){
		showDeviceForm(deviceId,"","");
	}
	
	if(userId=="4028812a3694db33013694e007560073"){
		Ext.addDevicePoint = function(event){
			var locationX = event.layerX;
			var locationY = event.layerY;
			Ext.Msg.confirm('确认', '确定要在此处添加信号收集器？',function(btn, text){
				if (btn == 'yes') {		
					showDeviceForm("",locationX,locationY);
					}
				});
			}
    	}
	function showDeviceForm(id,locationX,locationY){
		Ext.Msg.wait('加载数据中....', '提示');
		Ext.Ajax.request({
			async : false,
			url : 'devicemanage/add-or-modify-station.action',
			params : {id:id,communityId:$("#devices_manage_map").attr("communityId"),locationX:locationX,locationY:locationY},
			success : function(xhr){
				Ext.Msg.hide();
				var deviceInfoForm = eval(xhr.responseText);
				var deviceInfoWin = new Ext.Window({
					title: '设备信息',
			        width: 300,
			        height:220, 
			        border:false,
			        modal :true,
			        layout: 'fit',
			        plain:true,
				    constrain: true,
			        closable: true,
			        bodyStyle:'padding:5px;',
			        buttonAlign:'center',
			        resizable:false,
			        items: [deviceInfoForm]
				});
				deviceInfoWin.show();
			},
			failure:function(xhr){
				Ext.Msg.hide(); 
				Ext.Msg.alert("温馨提醒","数据查询失败,稍后请重试！");
			}
		});
	}
	
//	function addDeviceToMap(device_obj){
//		var station_add = getDeviceHtml(device_obj);
//		$('#devices_manage_point').append(station_add);
//		$("#iPicture_device_manage").iPictureInsert($('#devices_manage_point').find('.ip_tooltip:last'));
//		//onDblClick=\'Ext.removeDevice(event,\"'+device_obj.id+'\");\' 
//		$('#devices_manage_point').find('.ip_tooltip:last .button').on('dblclick',function(event){
//			removeDevice(event,$(this).parent().attr('id'));
//		});
//	}
//	function updateDeviceToMap(device_obj){
//		$('#devices_manage_'+device_obj.id).find('p').html('设备信息<hr>名称：'+device_obj.name +'<br>添加时间：'+device_obj.addDate + '<br>描述：'+device_obj.description);
//	}
	function getDeviceHtml(device_obj){
		return '<div deviceId="'+device_obj.id+'" class="ip_tooltip ip_img32" ' +
		'style="top: '+device_obj.locationY+'px; left: '+ device_obj.locationX+
		'px;" data-tooltipbg="bgwhite" data-round="roundBgW" data-button="device" data-animationtype="ltr-slide">'+
		'<p>设备信息<hr>' +
		'名称：'+device_obj.name + "（" + device_obj.card_mark + "）" +
		'<br>添加时间：'+device_obj.addDate +
		'<br>描述：'+device_obj.description +
		'</p></div>'
	}
	
    var centerPanel=new Ext.Panel({
		id:'map_panel',
		header : false,
		region:'center',
		layout : 'fit',
		border : false,
//		autoHeight:true,
//		autoWidth:true,
//		autoScroll : true,
        html:'<div id="iPicture_device_manage" data-interaction="hover">' +
        		'<div class="ip_slide">' + 
        			'<img id="devices_manage_map" class="ip_tooltipImg" communityId="" onDblClick="Ext.addDevicePoint(event);" src=""/>' +
        			'<div id="devices_manage_point"  class="devices"></div>' +
        			'<div id="devices_manage_locations_${id}" class="locations"></div>' +
        		'</div>' +
        	'</div>',
        listeners:{
        	"afterrender":function(panel){
		        //加载信号收集站点
        		mapObject = new SpryMap({id : "iPicture_device_manage",
                         height: 300,
                         width: 300,
                         startX: 0,
                         startY: 0,
                         cssClass: ""});
                changMapSize();
//		        getAllDevices();
          	},
          	"bodyresize":function(){
          		changMapSize();
          		refreshMapPosition();
			}
        }
    });
    function changMapSize(){
    	var divHeight = $('#iPicture_device_manage').parent().parent().height();
    	var divWidth = $('#iPicture_device_manage').parent().parent().width();
    	$('#iPicture_device_manage').parent().height(divHeight);
    	$('#iPicture_device_manage').parent().width(divWidth);
    }
    //解决当地图显示区域为右下角时，再拉大浏览器窗口时会出现地图无法全部填充centerPanel的情况
    //要考虑的问题：1.地图可填充满centerPanel,但未填充满；2.地图已近展示到最大了还是无法展示全部的地图
    function refreshMapPosition(){
    	//可视窗口的大小
    	var mapHeight = parseFloat($('#iPicture_device_manage').parent().height());
    	var mapWidth = parseFloat($('#iPicture_device_manage').parent().width());
    	//地图相对于centerPanel的偏移量
    	var position = $('#iPicture_device_manage').position(); 
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
    	$('#iPicture_device_manage').animate({top:positionTop+'px',left:positionLeft+'px'},0); 
    }
	var mainPanel = new Ext.Panel({
    	header : false,
		title : '设备管理',
		border : false,
		layout : 'border',
		items : [tree,centerPanel]
	});
    return mainPanel;
})();