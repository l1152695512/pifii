<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var mapObject;
	var rootNodeId = '0';
	
	var rootNode = new Ext.tree.AsyncTreeNode({
		id : rootNodeId,
		text : '所有小区',
		draggable : false,
		expanded : true
	});
	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl : 'warnmanage/warnarea/get-tree-nodes.action'
	});
	treeLoader.on("beforeload", function(loader, node) {
        this.baseParams.node = node.id;
    });
    treeLoader.on("load", function(loader, node) {
    });
	function reloadTreeNode(lastChildSelect){
		var selectNode = tree.getSelectionModel().getSelectedNode();
		if(selectNode){
			var selectNodeId = selectNode.id;
			var refreshNode;
			if(selectNode.isLeaf()){
				refreshNode = selectNode.parentNode;
			}else{
				refreshNode = selectNode;
			}
			treeLoader.load(refreshNode,function(node){
				node.expand(false,true,function(){
					if(lastChildSelect){
						//选中新添加叶子节点
	        			var addNode = node.lastChild;
	        			addNode.select();
	        			change_warn_area(addNode);
					}else{
						tree.getNodeById(selectNodeId).select();
					}
				});
			});
		}
	}
	var new_warn_area_action =  new Ext.Action({
		text : '增加',
		iconCls : 'icon-add',
		handler : function() {
			var selectNode = tree.getSelectionModel().getSelectedNode();
			if(selectNode && selectNode.id != rootNodeId && !selectNode.isLeaf()){
				Ext.Msg.wait('添加警告区域中....', '提示');
				Ext.Ajax.request({
					url:'warnmanage/warnarea/add-or-modify-warn-area.action',
					params:{communityId:selectNode.id},
					success:function(xhr){
						Ext.Msg.hide(); 
						var warn_area_win = eval(xhr.responseText);
						warn_area_win.show();
					},
					failure:function(xhr){
						Ext.Msg.hide(); 
						Ext.Msg.alert('提示','暂时不能添加警告区域！');
					}
				});
			}else{
				Ext.Msg.alert("提示","请选择小区！");
			}
		}
	});
	var delete_warn_area_action = new Ext.Action({
		text:'删除',
		iconCls:'icon-delete',
		handler:function(){
			var selectNode = tree.getSelectionModel().getSelectedNode();
			if(selectNode && selectNode.id != rootNodeId && selectNode.isLeaf()){
				Ext.Msg.confirm('确认', '确定要删除该警告区域？',
					function(btn, text) {
						if (btn == 'yes') {
							Ext.Msg.wait('正在删除....', '提示');
							Ext.Ajax.request({
					       		url:'warnmanage/warnarea/delete-warn-area.action',
					       		params: {warnAreaId:selectNode.id},
					            success : function(xhr) {
					            	Ext.Msg.hide(); 
					            	var obj = Ext.decode(xhr.responseText);
									if(obj.success){
										var parentNode = tree.getNodeById(selectNode.id).parentNode;
						            	parentNode.select();
						            	reloadTreeNode(false);
						            	change_warn_area(parentNode);
									}else{
										Ext.Msg.alert("温馨提醒","删除失败，稍后请重试！");
									}
					           	},
							    failure: function(xhr){
							    	Ext.Msg.hide(); 
							    	Ext.Msg.alert("温馨提醒","删除失败，稍后请重试！");
							    }
					        })
						}
				});
			}else{
				Ext.Msg.alert("提示","请选择具体的警告区域！");
			}
		}
	});
	var modify_warn_area_action = new Ext.Action({
		text:'修改',
		iconCls:'icon-edit',
		handler:function(){
			var selectNode = tree.getSelectionModel().getSelectedNode();
			if(selectNode && selectNode.id != rootNodeId && selectNode.isLeaf()){
				var text = selectNode.text;
				Ext.Msg.wait('获取警告区域信息....', '提示');
				Ext.Ajax.request({
					url:'warnmanage/warnarea/add-or-modify-warn-area.action',
					params:{id:selectNode.id},
					success:function(xhr){
						Ext.Msg.hide(); 
						var warn_area_win = eval(xhr.responseText);
						warn_area_win.show();
					},
					failure:function(xhr){
						Ext.Msg.hide(); 
						Ext.Msg.alert('提示','暂时不能修改警告区域！');
					}
				});
			}else{
				Ext.Msg.alert("提示","请选择具体的警告区域！");
			}
		}
	});
    var tree = new Ext.tree.TreePanel({
		title : '小区浏览',
	    region : 'west',
		useArrows : true,
		autoScroll : true,
		enableDD : false,
		animate : true,
		width : 200,
		collapsible : true,
		rootVisible : true, 
		containerScroll : true,
		loader : treeLoader,
		root : rootNode,
		tbar:[new_warn_area_action,'-',delete_warn_area_action,'-',modify_warn_area_action],
 	    listeners:{    
      		 'click':function(node, event) {
//  		 		try{
//  		 			warn_area_task.cancel();
//  		 		}catch(e){
//  		 		}
      			change_warn_area(node);
      		 },
      		 'beforeclick':function(node, event) {
      		 	var previousSelectNode = tree.getSelectionModel().getSelectedNode();
      		 	if(previousSelectNode){
      		 		if(previousSelectNode.id == node.id || node.id == rootNodeId){
      		 			return false;
      		 		}
      		 	}
      		 }
      	} 
	});
	
	function change_warn_area(selectNode){
		var currentCommunityId = $('#warn_area_map').data("communityId");
		if(selectNode && selectNode.id != rootNodeId){
			if(!selectNode.isLeaf()){
				$('#warn_area_point').html('');
  		 		$('#warn_area_line').html('');
				if(currentCommunityId != selectNode.id){
					changeCommunity(selectNode.id);
				}
			}else{//路线节点
				var parentNodeCommunityId = selectNode.parentNode.id;
				if(currentCommunityId != parentNodeCommunityId){
					changeCommunity(parentNodeCommunityId);
				}
				//加载路线点
				load_warn_area_points(selectNode.id);
			}
		}
	}
		
	function changeCommunity(nodeId){
		var communityNode = tree.getNodeById(nodeId);
		var map = communityNode.attributes.map;
		$("#warn_area_map").attr("src", map);
		$("#warn_area_map").data("communityId", nodeId);
		mapObject.width = communityNode.attributes.mapWidthPixel;
      	mapObject.height = communityNode.attributes.mapHeightPixel;
		loadDevices();
	}
	
	function loadDevices(){
		Ext.Msg.wait('正在加载设备点....', '提示');
		Ext.Ajax.request({
			url : 'devicemanage/get-devices.action',
			params : {communityId : $('#warn_area_map').data("communityId")},
			success : function(form,action){
				Ext.Msg.hide(); 
				$('#warn_area_device_point').html('');
				var responseText = Ext.decode(form.responseText);
            	var dataList = responseText.list;
            	var deviceHtml = '';
            	for(var i=0;i<dataList.length;i++){
            		deviceHtml=deviceHtml+getDeviceHtml(dataList[i]);;
				}
				$('#warn_area_device_point').html(deviceHtml);
				$("#iPicture_warn_area").iPicture({id:"warn_area_device_point"});
				//去掉点击遮盖事件
				$('#warn_area_device_point').find('.ip_tooltip .button').css("pointer-events","none");
			},
			failure:function(form,action){
				Ext.Msg.hide(); 
				Ext.Msg.alert("温馨提醒","获取站点信息失败，稍后请重试！");
			}
		});
	}
	
	function load_warn_area_points(warn_area_id){
		Ext.Msg.wait('正在加载告警区域....', '提示');
		Ext.Ajax.request({
			url:'warnmanage/warnarea/get-warn-area-points.action',
			params : {warnAreaId : warn_area_id},
			success : function(form,action){
				Ext.Msg.hide(); 
				$('#warn_area_point').html('');
  		 		$('#warn_area_line').html('');
				var responseText = Ext.decode(form.responseText);
            	var dataList = responseText.list;
            	for(var i=0;i<dataList.length;i++){
            		insert_warn_area_point(dataList[i]);
            		drow_line(dataList[i],dataList[(i+1)%dataList.length]);
            	}
            	add_point_click_listener();
            	
            	
//            	if(dataList.length > 0){
//            		insert_warn_area_point(dataList[0]);
//            	}
//            	drow_warn_area_line(dataList,0);
			},
			failure:function(form,action){
				Ext.Msg.hide(); 
				Ext.Msg.alert("温馨提醒","获取数据失败，稍后请重试！");
			}
		});
	}
	
	function drow_line(start_point,end_point){
		var lineWidth = Math.sqrt(Math.pow((Number(start_point.location_x)-Number(end_point.location_x)),2)+
					Math.pow((Number(start_point.location_y)-Number(end_point.location_y)),2));
		var angle = ((Number(start_point.location_x) > Number(end_point.location_x) == Number(start_point.location_y) > Number(end_point.location_y))?"":"-")
					+ (Math.asin(Math.abs(Number(end_point.location_y) - Number(start_point.location_y))/lineWidth)*360/2/Math.PI);
		var line_center_top = (Number(start_point.location_y) + Number(end_point.location_y))/2;
		var line_center_left = (Number(start_point.location_x) + Number(end_point.location_x) - lineWidth)/2+2;//2为线的宽度的一半
		$('#warn_area_line').append('<div class="route_div" style="top: '+line_center_top+'px; left: '+line_center_left+'px;width: '+lineWidth+'px;"></div>');
		var lineObj = $('#warn_area_line').find('.route_div:last');
		lineObj.css("transform", "rotate("+angle+"deg)");
	}
	
//	var locationRadius = 2;
//	var warn_area_task;
//	function drow_warn_area_line(locations,index){
//		if(index+1<locations.length){
//			var startPoint = locations[index];
//			var endPoint = locations[index+1];
//			var times = 1;
//			var lengthXY = Math.sqrt(Math.pow(startPoint.location_x - endPoint.location_x,2)+Math.pow(startPoint.location_y - endPoint.location_y,2))
//			warn_area_task = new Ext.util.DelayedTask(function(){
//				var currentLengthXY = times*locationRadius;
//				var positionX = Number(startPoint.location_x)+currentLengthXY*(endPoint.location_x - startPoint.location_x)/lengthXY;
//				var positionY = Number(startPoint.location_y)+currentLengthXY*(endPoint.location_y - startPoint.location_y)/lengthXY;
//				var currentXY = Math.sqrt(Math.pow(positionX,2)+Math.pow(positionY,2));
//				if(currentLengthXY >= lengthXY){
//					insert_warn_area_point(locations[index+1]);
////					$('#warn_area_point').append(getRoutePointHtml(locations[index+1]));
////					$("#iPicture_warn_area").iPictureInsert($('#warn_area_point').find('.ip_tooltip:last'));
//				}else{
//					var tooltips_add = '<div class="route" style="top: ' + positionY + 'px; left: ' + positionX +'px;"></div>';
//					$('#warn_area_line').append(tooltips_add);
//				}
//				times = times+1;
//				if(currentLengthXY < lengthXY){
//					warn_area_task.delay(0);
//				}else{
//					drow_warn_area_line(locations,index+1);
//				}
//			});
//			warn_area_task.delay(0);
//		}else{
//			add_point_click_listener();
//		}
//	}
	var isdb;
	function add_point_click_listener(){
		var warn_area_points = $('#warn_area_point').find('.ip_tooltip .button');
		warn_area_points.on('dblclick',function(){
			isdb=true;
			remove_warn_area_point($(this).parent().data('pointId'));
		});
		warn_area_points.on('click',function(){
			isdb=false;
		    window.setTimeout(execute, 500);
		    var id = $(this).parent().data('pointId');
		    function execute(){
		        if(isdb!=false) return;
		        show_warn_area_point_form(id,"","");
		    }
		});
		//第一个点双击后可选择删除或结束画区域
//		$(warn_area_points[0]).on('dblclick',function(){
//			isdb=true;
//			remove_or_finish_drow_area($(this).parent().data('pointId'));
//		});
	}
	
	function remove_or_finish_drow_area(point_id){
		Ext.MessageBox.show({
			title: '区域点操作',
			msg:'请选择具体的操作',
			modal: true,
			buttons:{yes:'区域封闭', no:'删除',cancel:Ext.MessageBox.CANCEL},
			icon:Ext.Msg.QUESTION,
			fn : function(btn, text){
				if(btn == 'yes') {
					var area_point = $('#warn_area_point').find('.ip_tooltip .button');
					var start_point = $(area_point[0]).parent();
					var end_point = $(area_point[area_point.length-1]).parent();
					var start_point_obj = {'location_x': parseFloat(start_point.css("left")),'location_y': parseFloat(start_point.css("top"))};
					var end_point_obj = {'location_x': parseFloat(end_point.css("left")),'location_y': parseFloat(end_point.css("top"))};
					drow_line(start_point_obj,end_point_obj);
				}else if(btn == 'no'){
					remove_warn_area_point(point_id);
				}
			}
        }) 
	}
	
	function insert_warn_area_point(point_obj){
		var warn_area_point_html = '<div data-point-id="'+point_obj.id+'" class="ip_tooltip ip_img32" ' +
			'style="top: '+point_obj.location_y+'px; left: '+ point_obj.location_x+
			'px;" data-tooltipbg="bgwhite" data-round="" data-button="route-plan-point" data-animationtype="ltr-slide">'+
			'<p>路线点信息<hr>' +
			'名称：'+point_obj.name +
			'<br>添加时间：'+point_obj.add_date +
			'</p></div>';
		$('#warn_area_point').append(warn_area_point_html);
		var currentPoint = $('#warn_area_point').find('.ip_tooltip:last');
		$("#iPicture_warn_area").iPictureInsert(currentPoint);
		//插入范围效果
//		var range = Number(point_obj.effective_range);
//		currentPoint.prepend('<div class="route-plan-point-range" style="width:'+range+'px;height:'+range+'px;top:-'+range*0.46+'px;left:-'+range*0.46+'px"></div>');
	}
	
	function remove_warn_area_point(pointId){
		Ext.Msg.confirm('确认', '确定要删除该点？',function(btn, text){
			if (btn == 'yes') {
				delete_warn_area_point(pointId);
			}
		});
	}
	
	function delete_warn_area_point(pointId){
		Ext.Msg.wait('删除数据中....', '提示');
		Ext.Ajax.request({
			url:'warnmanage/warnarea/delete-warn-area-point.action',
			params : {id : pointId},
			success : function(form,action){
				Ext.Msg.hide();
				var obj = Ext.decode(form.responseText);
				if(obj.success){
					load_warn_area_points(tree.getSelectionModel().getSelectedNode().id);
				}else{
					Ext.Msg.alert("温馨提醒","删除失败，稍后请重试！");
				}
			},
			failure:function(form,action){
				Ext.Msg.hide(); 
				Ext.Msg.alert("温馨提醒","删除失败，稍后请重试！");
			}
		});
	}
	
	Ext.add_warn_area_point = function(event){
		var selectNode = tree.getSelectionModel().getSelectedNode();
		if(selectNode && selectNode.isLeaf()){
			var locationX = event.layerX;
			var locationY = event.layerY;
			//测试点在警告区域的位置（内，外，线上）
//			Ext.Msg.wait('加载数据中....', '提示');
//			Ext.Ajax.request({
//				async : false,
//				url:'datastatistics/warn/check-point.action',
//				params : {warnAreaId:tree.getSelectionModel().getSelectedNode().id,locationX:locationX,locationY:locationY},
//				success : function(xhr){
//					Ext.Msg.hide();
//					console.debug(xhr);
//					var responseText = Ext.decode(xhr.responseText)
//					Ext.Msg.alert("温馨提醒",responseText.msg);
//				},
//				failure:function(xhr){
//					Ext.Msg.hide(); 
//					Ext.Msg.alert("温馨提醒","数据查询失败,稍后请重试！");
//				}
//			});
			
//			Ext.Msg.confirm('确认', '确定要在此处添加检查点？',function(btn, text){
//				if (btn == 'yes') {		
					show_warn_area_point_form("",locationX,locationY);
//				}
//			});
		}
	}
	function show_warn_area_point_form(id,locationX,locationY){
		Ext.Msg.wait('加载数据中....', '提示');
		Ext.Ajax.request({
			async : false,
			url:'warnmanage/warnarea/add-or-modify-warn-area-point.action',
			params : {id:id,warnAreaId:tree.getSelectionModel().getSelectedNode().id,locationX:locationX,locationY:locationY},
			success : function(xhr){
				Ext.Msg.hide();
				var warn_area_point_info_win = eval(xhr.responseText);
				warn_area_point_info_win.show();
			},
			failure:function(xhr){
				Ext.Msg.hide(); 
				Ext.Msg.alert("温馨提醒","数据查询失败,稍后请重试！");
			}
		});
	}
	
	function getDeviceHtml(device_obj){
		return '<div deviceId="'+device_obj.id+'" class="ip_tooltip ip_img32" ' +
			'style="top: '+device_obj.locationY+'px; left: '+ device_obj.locationX+
			'px;" data-tooltipbg="bgwhite" data-round="roundBgW" data-button="device" data-animationtype="ltr-slide">'+
			'<p>设备信息<hr>' +
			'名称：'+device_obj.name +
			'<br>添加时间：'+device_obj.addDate +
			'<br>描述：'+device_obj.description +
			'</p></div>';
	}
    var centerPanel=new Ext.Panel({
		header : false,
		region:'center',
		layout : 'fit',
		border : false,
//		autoHeight:true,
//		autoWidth:true,
//		autoScroll : true,
        html:'<div id="iPicture_warn_area" data-interaction="hover">' +
        		'<div class="ip_slide">' +
        			'<img id="warn_area_map" class="ip_tooltipImg" data-community-id="" onDblClick="Ext.add_warn_area_point(event);" src=""/>' +
        			'<div id="warn_area_device_point" class="devices">' +
	        		'</div>' +
	        		'<div id="warn_area_line" class="locations">' +
	        		'</div>' +
        			'<div id="warn_area_point" class="points">' +
	        		'</div>' +
        		'</div>' +
        	'</div>',
        listeners:{
        	"afterrender":function(panel){
		        //加载信号收集站点
        		mapObject = new SpryMap({id : "iPicture_warn_area",
                         height: 300,
                         width: 300,
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
    function changMapSize(){
    	var divHeight = $('#iPicture_warn_area').parent().parent().height();
    	var divWidth = $('#iPicture_warn_area').parent().parent().width();
    	$('#iPicture_warn_area').parent().height(divHeight);
    	$('#iPicture_warn_area').parent().width(divWidth);
    }
    //解决当地图显示区域为右下角时，再拉大浏览器窗口时会出现地图无法全部填充centerPanel的情况
    //要考虑的问题：1.地图可填充满centerPanel,但未填充满；2.地图已近展示到最大了还是无法展示全部的地图
    function refreshMapPosition(){
    	//可视窗口的大小
    	var mapHeight = parseFloat($('#iPicture_warn_area').parent().height());
    	var mapWidth = parseFloat($('#iPicture_warn_area').parent().width());
    	//地图相对于centerPanel的偏移量
    	var position = $('#iPicture_warn_area').position(); 
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
    	$('#iPicture_warn_area').animate({top:positionTop+'px',left:positionLeft+'px'},0); 
    }
	var mainPanel = new Ext.Panel({
    	header : false,
		title : '路线规划',
		border : false,
		layout : 'border',
		items : [tree,centerPanel]
	});
    return mainPanel;
})();