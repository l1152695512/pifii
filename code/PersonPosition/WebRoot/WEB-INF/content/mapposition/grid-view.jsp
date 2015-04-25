<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var rootId = '402881fe3127d5510131283920d30001';
	var map;
	var rootNode = new Ext.tree.AsyncTreeNode({
		id : rootId,
		icon:'hr/img/e_16.png',
		checked : false,
		text : '广东省小区',
		expanded : true
	});
	
	var treeLoader = new Ext.tree.TreeLoader({
//		preloadChildren:true,
		dataUrl: 'mapposition/tree-data.action'
	});
	treeLoader.on("beforeload", function(loader, node) {
        this.baseParams.type = node.attributes.type;
        this.baseParams.checked = node.attributes.checked?"1":"0";
    });
    treeLoader.on("load", function(loader, node) {
		var nodes = node.childNodes;
		for(var i=0;i<nodes.length;i++){
			if(nodes[i].attributes.type != '4'){
				nodes[i].expand();
			}
		}
    });
 	var leftPanel = new Ext.tree.TreePanel({
 		region : 'west',
    	title : '小区定位',
    	useArrows: true,
        frame: true,
      	animate : true,
      	width: 250,
       	containerScroll : true,
       	lines:true,
       	rootVisible : true,
	    autoScroll : true,
	    collapsible:true,
        collapseMode:'mini',
        //split:true,
	    root: rootNode,
	    loader : treeLoader,
//  	    plugins:[
//  	        new Ext.plugin.tree.TreeNodeChecked({
// 	        	cascadeCheck: true,
// 	          	cascadeParent: false,
// 	          	cascadeChild: true,
//  	          	linkedCheck: false, 
//  	          	asyncCheck: false, 
// 	          	displayAllCheckbox: false
//	        })
// 	    ],
 	    listeners:{    
 	    	'click':function(node, event) {
      			if(node.isLeaf()){
      				movePersonToCenter(node.id);
      			}
      		 },
      		 'checkchange':function(thisNode,isChecked){
      		 	leftPanel.suspendEvents(false);//暂停所有事件的触发，这里用来避免在选中或取消一个多选框后循环事件触发，导致死循环，false代表不记录暂停的事件
      		 	updateParentChecked(thisNode,isChecked);
      		 	
      		 	var nodeText = $(thisNode.text).html();
				if(!isChecked && thisNode.isLeaf() && undefined != nodeText){//当取消选中时，去掉人员的红色样式
					thisNode.setText(nodeText);
				}
      		 	updateChildrenChecked(thisNode,isChecked);
      		 	leftPanel.resumeEvents();//恢复事件
      		 	if(auto_refersh.getValue()){
      		 		refresh_location_task.cancel();
					refresh_location_task.delay(0);
      		 	}
      		 }
      	} 
	});
	function movePersonToCenter(id){
		for(var i = 0; i < personsLocation.length; i++) {
			if (personsLocation[i].phone == id) {
				if('' != personsLocation[i].location_x && '' != personsLocation[i].location_y){
					//百度地图的zoom值为3-18
					map.centerAndZoom(new BMap.Point(personsLocation[i].location_x, personsLocation[i].location_y), 18);
				}
				return;
			}
		}
	}
	function updateChildrenChecked(node,checked){
		node.eachChild(function(current){
			current.getUI().toggleCheck(checked);
			var nodeText = $(current.text).html();
			if(!checked && current.isLeaf() && undefined != nodeText){//当取消选中时，去掉人员的红色样式
				current.setText(nodeText);
			}
			if(current.hasChildNodes()){
				updateChildrenChecked(current,checked);
			}
		});
	}
	function updateParentChecked(node,checked){
		var parentNode = node.parentNode;
		if(parentNode != null){
			if(checked){
				var check = true;
				parentNode.eachChild(function(current){
					if(!current.attributes.checked){
						check = false;
						return;
					}
				});
				parentNode.getUI().toggleCheck(check);
			}else{
				parentNode.getUI().toggleCheck(false);
			}
			updateParentChecked(parentNode,checked);
		}
	}

    var refresh_location= new Ext.Action({
        text: '刷新',
        tooltip:'自动刷新',
        iconCls :'x-tbar-loading',
        handler: function(){
        	changeFunction();
			refresh_location_task.delay(0);
        }
    });
    var auto_refersh=new Ext.form.Checkbox({  
//		boxLabel:'自动刷新',
		listeners:{
			'check':function(){
				if(this.checked){
					refresh_location.disable();
					refresh_location_task.cancel();
					refresh_location_task.delay(0);
				}else{
					refresh_location.setDisabled(false);
					refresh_location_task.cancel();
				}
			}  
		}  
	});
//	var pline = null;//历史轨迹划线，清除轨迹时要用
	var refreshTime = 10000;
//	var allMarkers = [];//存储地图上所有的标记
	var personsLocation = [];//存储请求的人员位置信息，用于点击tree中的人员时，将该人员的位置移动到屏幕中间
	var refresh_location_task = new Ext.util.DelayedTask(function(){
		var personIds = [];
		Ext.each(leftPanel.getChecked(), function(node) {
			if(node.attributes.type == '4'){//是人员
				personIds.push(node.id);
			}
		});
	   	Ext.Ajax.request({
       		url : 'mapposition/person-location.action',
			params : {personIds:personIds},
            success : function(xhr) {
            	var responseText = Ext.decode(xhr.responseText);
            	personsLocation = responseText.list;
            	addPersonLocation();
				if(refreshTime>=0 && auto_refersh.getValue()){
					refresh_location_task.delay(refreshTime);
				}
           	},
		    failure: function(xhr){
		    	if(refreshTime>=0 && auto_refersh.getValue()){
					refresh_location_task.delay(refreshTime);
				}
		    }
        })
	});
	function addPersonLocation(){
		map.clearOverlays();//清除地图上的标记点
    	for(var i=0;i<personsLocation.length;i++){
    		if(personsLocation[i].location_x != ''){
    			leftPanel.getNodeById(personsLocation[i].phone).setText(personsLocation[i].name);
    			var marker = new BMap.Marker(
					new BMap.Point(Number(personsLocation[i].location_x),Number(personsLocation[i].location_y)),
					{icon:new BMap.Icon("hr/img/person_photo.png", new BMap.Size(16,16))}
				);
				var label = new BMap.Label(personsLocation[i].name,{offset:new BMap.Size(16,0)});
				marker.setLabel(label);
    			map.addOverlay(marker);
				map.setCenter(new BMap.Point(personsLocation[i].location_x, personsLocation[i].location_y));
//    			var gpsPoint = new BMap.Point(Number(personsLocation[i].location_x),Number(personsLocation[i].location_y));
//    			BMap.Convertor.translate(gpsPoint,0,function(point){
//    				var marker = new BMap.Marker(
//						point,
//						{icon:new BMap.Icon("hr/img/person_photo.png", new BMap.Size(16,16))}
//					);
//					var label = new BMap.Label('',{offset:new BMap.Size(16,0)});
//					marker.setLabel(label);
//	    			map.addOverlay(marker);
//					map.setCenter(point);
//    			}); 
    		}else{
    			var title = "";
    			if("0" == personsLocation[i].info){
    				title = "暂时无法获取位置信息";
    			}
    			leftPanel.getNodeById(personsLocation[i].phone).setText("<span style='color:red' title='"+title+"'>"+personsLocation[i].name+"</span>");
    		}
		}
	}
	function changeFunction(){
		auto_refersh.setValue(false);//取消自动刷新
		refresh_location_task.cancel();//停止刷新位置信息
		map.clearOverlays();//清除地图上的标记点
	}
	var history_route = new Ext.Action({
        text: '历史轨迹',
        iconCls :'icon-history-route',
        handler: function(){
        	var checkedNodes = leftPanel.getChecked();
        	if(checkedNodes.length > 1){
        		Ext.Msg.alert("提示","同时只能查询一个人的历史轨迹，请重新选择左边的人员！");
        	}else if(checkedNodes.length == 0){
        		Ext.Msg.alert("提示","请选择一个左边的人员进行查询！");
        	}else{
        		changeFunction();
        		Ext.Msg.wait('正在获取数据....', '提示');
        		Ext.Ajax.request({
		       		url : 'mapposition/history-location-date-range.action',
					params : {id:checkedNodes[0].id},
		            success : function(xhr) {
		            	Ext.Msg.hide(); 
						var dateRangeWin = eval(xhr.responseText);
						console.log(xhr.responseText);
						console.log(dateRangeWin.initialConfig);
						dateRangeWin.show();
						Ext.Ajax.request({
		       		url : 'mapposition/person-history-location.action',
					params : {id:'${id}',startTime:startTime,endTime:endTime},
		            success : function(xhrs) {
		        		var responseText = Ext.decode(xhrs.responseText);
		            	locations = responseText.list;
		            if(dataList.length > 2){
            			drow_route(dataList,0);
            		}else{
            		Ext.Msg.alert("提示","无历史轨迹！");
            			}
		           	},
				    failure: function(xhr){
				    	Ext.Msg.hide();
				    	Ext.Msg.alert("提示","获取数据失败，稍后请重试！");
				    }
		        });
		           	},
				    failure: function(xhr){
				    	Ext.Msg.hide(); 
				    }
		        })
        	}
        }
    });
    function drow_route(data_list,index){
		if($('#history_route_img').html() != undefined){//如果该窗口没有关闭
			movePositionToMapCenter(data_list[index].locationX,data_list[index].locationY);
			if(data_list[index].date != undefined){ 
				$('#route_time_div').append('<div style="padding:2px 0px 2px 10px;cursor: pointer;border-bottom: 1px solid rgb(167, 167, 170);" data-location-x="'+data_list[index].locationX+
							'" data-location-y="'+data_list[index].locationY+'"><img style="margin-right: 5px;" src="hr/img/icons/clock.ico"></img>'+data_list[index].date +'</div>');
			}
//			var routePointHtml = '<div class="ip_tooltip ip_img32" ' + 'style="top: '+data_list[index].locationY+
//					'px; left: '+ data_list[index].locationX+'px;" data-button="route-plan-point"></div>';//线之间的点
//			$('#history_route_locations').append(routePointHtml);
//			var currentPoint = $('#history_route_locations').find('.ip_tooltip:last');
//			$("#history_route_map").iPictureInsert(currentPoint);
			
			if($('#route_time_div').children().length > maxLineNum){
				Ext.Msg.confirm('确认', '轨迹数据过多，点击“是”清除当前轨迹继续后面的轨迹显示，点击“否”终止后面的轨迹显示。',function(btn, text){
					if (btn == 'yes') {
						$('#route_time_div').html('');
						$('#history_route_locations').html('');
						if(index < data_list.length-1){
							drow_line(data_list,index);
						}else{
							add_times_listener();
						}
					}else{
						add_times_listener();
					}
				});
//				$('#route_time_div').html('');
//				$('#history_route_locations').html('');
			}else{
				if(index < data_list.length-1){
					drow_line(data_list,index);
				}else{
					add_times_listener();
				}
			}
		}
	}
    //实例化鼠标绘制工具
    var drawingManager;
    var area_person_find = new Ext.Action({
        text: '区域人员查询',
        iconCls :'icon-find',
        handler: function(){
//        	window.map.clearOverlays();//清除地图上的标记点
        	refresh_location.setDisabled(true);
        	history_route.setDisabled(true);
        	area_person_find.setDisabled(true);
        	drawingManager.setDrawingMode(BMAP_DRAWING_RECTANGLE);
        	drawingManager.open();
        }
    });
    function findPersonInArea(minLocationX,maxLocationX,minLocationY,maxLocationY){
    	Ext.Msg.wait('获取数据中....', '提示');
		Ext.Ajax.request({
       		url : 'mapposition/person-location-in-area.action',
			params : {minLocationX:minLocationX,maxLocationX:maxLocationX,minLocationY:minLocationY,maxLocationY:maxLocationY},
            success : function(xhr) {
            	Ext.Msg.hide();
            	var responseText = Ext.decode(xhr.responseText);
            	personsLocation = responseText.list;
            	updateChildrenChecked(rootNode,false);
            	if(personsLocation.length == 0){
            		Ext.Msg.alert('提示','该区域内没有人员！');
            	}
            	for(var i=0;i<personsLocation.length;i++){
            		var personNode = leftPanel.getNodeById(personsLocation[i].phone);
            		personNode.getUI().toggleCheck(true);
            		expend_node(personNode);
            	}
            	addPersonLocation();
            	refresh_location.setDisabled(false);
	        	history_route.setDisabled(false);
	        	area_person_find.setDisabled(false);
           	},
		    failure: function(xhr){
		    	Ext.Msg.hide();
		    	refresh_location.setDisabled(false);
	        	history_route.setDisabled(false);
	        	area_person_find.setDisabled(false);
		    }
        })
	}
    function expend_node(node){
    	var parentNode = node.parentNode;
    	if(null != parentNode){
    		parentNode.expand();
    		expend_node(parentNode);
    	}
    }
    var mapPanel = new Ext.Panel({
    	header : false,
     	region:'center',
        layout:'fit',
        tbar: [auto_refersh,refresh_location,'-',history_route,'-',area_person_find],
	    html: '<div id="map" style="width:100%;height:100%">',
	    listeners : {
	    	'afterrender':function(){
	    		map = new BMap.Map('map');
	    		map.centerAndZoom(new BMap.Point(113.368159, 23.129269), 13);
				map.addControl(new BMap.NavigationControl());  //添加默认缩放平移控件
				map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
				map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
//				window.map.disableAutoResize();
				map.enableAutoResize();
				map.setMinZoom(5);
	    		
	    		drawingManager = new BMapLib.DrawingManager(map, {
			        isOpen: false, //是否开启绘制模式
			        enableDrawingTool: false, //是否显示工具栏
			        rectangleOptions: {
				        strokeColor:"red",    //边线颜色。
				        fillColor:"",      //填充颜色。当参数为空时，圆形将没有填充效果。
				        strokeWeight: 3,       //边线的宽度，以像素为单位。
				        strokeOpacity: 0.8,	   //边线透明度，取值范围0 - 1。
				        fillOpacity: 0.6,      //填充的透明度，取值范围0 - 1。
				        strokeStyle: 'solid' //边线的样式，solid或dashed。
				    } //矩形的样式
			    });
			    //添加鼠标绘制工具监听事件，用于获取绘制结果
			    drawingManager.addEventListener('overlaycomplete', function(e){
			    	map.clearOverlays();//清除地图上的标记点
			    	drawingManager.close();
			    	var points = e.overlay.getPath();
			    	var minLocationX = points[0].lng;
			    	var maxLocationX = points[0].lng;
			    	var minLocationY = points[0].lat;
			    	var maxLocationY = points[0].lat;
			    	for(var i=1;i<points.length;i++){
			    		if(points[i].lng < minLocationX){
			    			minLocationX = points[i].lng;
			    		}
			    		if(points[i].lng > maxLocationX){
			    			maxLocationX = points[i].lng;
			    		}
			    		if(points[i].lat < minLocationY){
			    			minLocationY = points[i].lat;
			    		}
			    		if(points[i].lat > maxLocationY){
			    			maxLocationY = points[i].lat;
			    		}
			    	}
			    	findPersonInArea(minLocationX,maxLocationX,minLocationY,maxLocationY);
			    });
	    	}
	    }
    });
    
        var routeTimePanel = new Ext.Panel({
//    	header : false,
		title : '位置详细信息',
//    	id:"persons_information_panel_${id}",
    	region : 'east',
    	width: 170,
//    	bodyStyle:'padding:10px 10px',
    	collapsed : true,
    	collapsible:true,
    	collapseMode:'mini',
//    	border:false,
		autoScroll : true,
		layout:'fit',
//		tbar: [clear_route],
		html : '<div id="route_time_div"></div>',
		listeners:{
        	"afterrender":function(panel){
//        		 loadInformations();
			}
		}
    });
    
    var mainPanel = new Ext.Panel({
       	layout:'border',
    	items:[leftPanel,mapPanel,routeTimePanel]
    });
    
    return mainPanel;
})();