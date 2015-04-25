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
		dataUrl : 'patrolmanage/routeplan/get-tree-nodes.action'
	});
	treeLoader.on("beforeload", function(loader, node) {
        this.baseParams.nodeId = node.id;
    });
    treeLoader.on("load", function(loader, node) {
		var nodes = node.childNodes;
		for(var i=0;i<nodes.length;i++){
//			nodes[i].addListener('mouseover',showNodeFunctionTips()); 
//			nodes[i].
		}
    });
//    function showNodeFunctionTips(){
//    }
    Ext.tree.TreeNodeUI.prototype.onOver=function(e){ 
//        this.addClass('x-tree-node-over'); 
//        this.fireEvent("mouseover", this.node, e); 
	}; 
	Ext.tree.TreeNodeUI.prototype.onOut=function(e){ 
	};
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
				node.expand(false,true,function(){//数据加载完成后执行选中，
					if(lastChildSelect){
						//选中新添加的路线
            			var addNode = node.lastChild;
            			addNode.select();
            			changeRoute(addNode);
					}else{
						tree.getNodeById(selectNodeId).select();
					}
				});
			});
		}
	}
	var newRouteAction =  new Ext.Action({
		text : '增加',
		iconCls : 'icon-add',
		handler : function() {
			var selectNode = tree.getSelectionModel().getSelectedNode();
			if(selectNode && selectNode.id != rootNodeId && !selectNode.isLeaf()){
				Ext.Msg.wait('添加路线中....', '提示');
				Ext.Ajax.request({
					url:'patrolmanage/routeplan/add-or-modify-route.action',
					params:{communityId:selectNode.id},
					success:function(xhr){
						Ext.Msg.hide(); 
						var routeWin = eval(xhr.responseText);
						routeWin.show();
					},
					failure:function(xhr){
						Ext.Msg.hide(); 
						Ext.Msg.alert('提示','暂时不能添加路线！');
					}
				});
			}else{
				Ext.Msg.alert("提示","请选择小区！");
			}
		}
	});
	var deleteRouteAction = new Ext.Action({
		text:'删除',
		iconCls:'icon-delete',
		handler:function(){
			var selectNode = tree.getSelectionModel().getSelectedNode();
			if(selectNode && selectNode.id != rootNodeId && selectNode.isLeaf()){
				Ext.Msg.confirm('确认', '确定要删除该路线？',
					function(btn, text) {
						if (btn == 'yes') {
							Ext.Msg.wait('正在删除数据....', '提示');
							Ext.Ajax.request({
					       		url:'patrolmanage/routeplan/delete-route.action',
					       		params: {routeId:selectNode.id},
					            success : function(xhr) {
					            	Ext.Msg.hide(); 
					            	var obj = Ext.decode(xhr.responseText);
									if(obj.success){
										var parentNode = tree.getNodeById(selectNode.id).parentNode;
						            	parentNode.select();
						            	reloadTreeNode(false);
						            	changeRoute(parentNode);
									}else{
										Ext.Msg.alert("提示","删除失败，稍后请重试！");
									}
					           	},
							    failure: function(xhr){
							    	Ext.Msg.hide(); 
							    	Ext.Msg.alert("提示","删除失败，稍后请重试！");
							    }
					        })
						}
				});
			}else{
				Ext.Msg.alert("提示","请选择具体的路线！");
			}
		}
	});
	var modifyRouteAction = new Ext.Action({
		text:'修改',
		iconCls:'icon-edit',
		handler:function(){
			var selectNode = tree.getSelectionModel().getSelectedNode();
			if(selectNode && selectNode.id != rootNodeId && selectNode.isLeaf()){
				var text = selectNode.text;
				Ext.Msg.wait('获取路线信息....', '提示');
				Ext.Ajax.request({
					url:'patrolmanage/routeplan/add-or-modify-route.action',
					params:{id:selectNode.id},
					success:function(xhr){
						Ext.Msg.hide(); 
						var routeWin = eval(xhr.responseText);
						routeWin.show();
					},
					failure:function(xhr){
						Ext.Msg.hide(); 
						Ext.Msg.alert('提示','暂时不能修改路线！');
					}
				});
			}else{
				Ext.Msg.alert("提示","请选择具体的路线！");
			}
		}
	});
    var tree = new Ext.tree.TreePanel({
//    	id:"route_plan_tree_panel",
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
		tbar:[newRouteAction,'-',deleteRouteAction,'-',modifyRouteAction],
 	    listeners:{    
      		 'click':function(node, event) {
//  		 		try{
//  		 			routePlanTask.cancel();
//  		 		}catch(e){
//  		 		}
      			changeRoute(node);
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
	
	function changeRoute(selectNode){
		var currentCommunityId = $('#route_plan_map').data("communityId");
		$('#route_plan_route_line').children().stop();
		if(selectNode && selectNode.id != rootNodeId){
			if(!selectNode.isLeaf()){
				$('#route_plan_point').html('');
  		 		$('#route_plan_route_line').html('');
				if(currentCommunityId != selectNode.id){
					changeCommunity(selectNode.id);
				}
			}else{//路线节点
				var parentNodeCommunityId = selectNode.parentNode.id;
				if(currentCommunityId != parentNodeCommunityId){
					changeCommunity(parentNodeCommunityId);
				}
				//加载路线点
				loadRoutePoints(selectNode.id);
			}
		}
	}
		
	function changeCommunity(nodeId){
		var communityNode = tree.getNodeById(nodeId);
		var map = communityNode.attributes.map;
		$("#route_plan_map").attr("src", map);
		$("#route_plan_map").data("communityId", nodeId);
		mapObject.width = communityNode.attributes.mapWidthPixel;
      	mapObject.height = communityNode.attributes.mapHeightPixel;
		loadDevices();
	}
	
	function loadDevices(){
		Ext.Msg.wait('正在加载设备点....', '提示');
		Ext.Ajax.request({
			url : 'devicemanage/get-devices.action',
			params : {communityId : $('#route_plan_map').data("communityId")},
			success : function(form,action){
				Ext.Msg.hide(); 
				$('#route_plan_device_point').html('');
				var responseText = Ext.decode(form.responseText);
            	var dataList = responseText.list;
            	var deviceHtml = '';
            	for(var i=0;i<dataList.length;i++){
            		deviceHtml=deviceHtml+getDeviceHtml(dataList[i]);;
				}
				$('#route_plan_device_point').html(deviceHtml);
				$("#iPicture_route_plan").iPicture({id:"route_plan_device_point"});
				//去掉点击遮盖事件
				$('#route_plan_device_point').find('.ip_tooltip .button').css("pointer-events","none");
			},
			failure:function(form,action){
				Ext.Msg.hide(); 
				Ext.Msg.alert("提示","获取站点信息失败，稍后请重试！");
			}
		});
	}
	
	function loadRoutePoints(routeId){
		Ext.Msg.wait('正在加载路线点....', '提示');
		Ext.Ajax.request({
			url:'patrolmanage/routeplan/get-route-points.action',
			params : {routeId : routeId},
			success : function(form,action){
				Ext.Msg.hide(); 
				$('#route_plan_point').html('');
  		 		$('#route_plan_route_line').html('');
				var responseText = Ext.decode(form.responseText);
            	var dataList = responseText.list;
            	start_drow(dataList,0);
//            	if(dataList.length > 0){
//            		insertRoutePlanPoint(dataList[0]);
////	            	$('#route_plan_point').append(getRoutePointHtml(dataList[0]));
////	            	$("#iPicture_route_plan").iPictureInsert($('#route_plan_point').find('.ip_tooltip:last'));
//            	}
//            	drowRoutePlan(dataList,0);
			},
			failure:function(form,action){
				Ext.Msg.hide(); 
				Ext.Msg.alert("提示","获取站点信息失败，稍后请重试！");
			}
		});
	}
	var drow_route_time = 300;
	function start_drow(data_list,index){//用于保证在规定时间内匀速画完线
		var all_route_length = 0;
		for(var i=0;i<data_list.length;i++){
    		if(i < data_list.length-1){
    			var line_length = Math.sqrt(Math.pow((Number(data_list[i].location_x)-Number(data_list[i+1].location_x)),2)+
					Math.pow((Number(data_list[i].location_y)-Number(data_list[i+1].location_y)),2));
    			all_route_length = all_route_length + line_length;
    		}
    	}
    	drow_route(data_list,index,all_route_length);
	}
	function drow_route(data_list,index,all_route_length){
		if(index < data_list.length){
			var routePointHtml = '<div data-point-id="'+data_list[index].id+'" class="ip_tooltip ip_img32" style="top: '+data_list[index].location_y+'px; left: '+ data_list[index].location_x+'px;"><div class="button route-plan-point"></div></div>';
			$('#route_plan_point').append(routePointHtml);
		}
		if(index < data_list.length-1){
			drow_line(data_list,index,all_route_length);
		}else{
			add_point_click_listener();
		}
	}
	
	function drow_line(data_list,index,all_route_length){//动画划线
		var start_point = data_list[index];
		var end_point = data_list[index+1];
		var lineWidth = Math.sqrt(Math.pow((Number(start_point.location_x)-Number(end_point.location_x)),2)+
					Math.pow((Number(start_point.location_y)-Number(end_point.location_y)),2));
		var angle = ((Number(start_point.location_x) > Number(end_point.location_x) == Number(start_point.location_y) > Number(end_point.location_y))?"":"-")
					+ (Math.asin(Math.abs(Number(end_point.location_y) - Number(start_point.location_y))/lineWidth)*360/2/Math.PI);
		$('#route_plan_route_line').append('<div class="route_div" style="top: '+start_point.location_y+'px; left: '+start_point.location_x+'px;width: 0px;"></div>');
		var lineObj = $('#route_plan_route_line').find('.route_div:last');
		lineObj.css("transform", "rotate("+angle+"deg)");
		var animate_time = drow_route_time*lineWidth/all_route_length;
		lineObj.animate(
			{width:lineWidth},
			{duration: animate_time,
				easing:'linear',
				step:function(now_width,tween){
					//now在这里会是当前对象的当前width
					var top = Number(start_point.location_y) + now_width*(Number(end_point.location_y) - Number(start_point.location_y))/lineWidth/2;
					var left = Number(start_point.location_x) + now_width*(Number(end_point.location_x) - Number(start_point.location_x))/lineWidth/2 - now_width/2+2;//2为线的宽度的一半
					lineObj.css({top:top+"px",left:left+"px"});
				},
				progress:function(animation,progress,remainingMs){
					//remainingMs剩余的毫秒数，可根据这个去控制刷新line位置的频率
					
				},
				complete:function(){
					drow_route(data_list,++index,all_route_length);
				}
			}
		);
	}
	
	var isdb;
	function add_point_click_listener(){
		var routePlanPointButton = $('#route_plan_point').find('.ip_tooltip .button');
		routePlanPointButton.on('dblclick',function(){
			isdb=true;
			removeRoutePlanPoint($(this).parent().data('pointId'));
		});
//		routePlanPointButton.on('click',function(){
//			isdb=false;
//		    window.setTimeout(execute, 500);
//		    var id = $(this).parent().data('pointId');
//		    function execute(){
//		        if(isdb!=false) return;
//		        showRoutePointForm(id,"","");
//		    }
//		});
	}
	
	function removeRoutePlanPoint(pointId){
		Ext.Msg.confirm('删除检查点', '确定要删除该检查点？',function(btn, text){
			if (btn == 'yes') {
				Ext.Msg.wait('删除数据中....', '提示');
				Ext.Ajax.request({
					url:'patrolmanage/routeplan/delete-route-point.action',
					params : {pointId : pointId},
					success : function(form,action){
						Ext.Msg.hide();
						var obj = Ext.decode(form.responseText);
						if(obj.success){
							loadRoutePoints(tree.getSelectionModel().getSelectedNode().id);
						}else{
							Ext.Msg.alert("提示","删除失败，稍后请重试！");
						}
 					},
 					failure:function(form,action){
 						Ext.Msg.hide(); 
 						Ext.Msg.alert("提示","删除失败，稍后请重试！");
 					}
				});
			}
		});
	}
	
	Ext.addRoutePlanPoint = function(event){
//		Ext.Msg.confirm('添加检查点', '确定要在此处添加检查点？',function(btn, text){
//			if (btn == 'yes') {		
				var selectNode = tree.getSelectionModel().getSelectedNode();
				if(selectNode && selectNode.isLeaf()){
					var locationX = event.layerX;
					var locationY = event.layerY;
					Ext.MessageBox.prompt('路线点添加','请输入该路线点的名称',function(btn,text){
						if("ok" == btn){
							Ext.Msg.wait('添加数据中....', '提示');
							Ext.Ajax.request({
								async : false,
								url:'patrolmanage/routeplan/save-route-point.action',
								params : {routeId:tree.getSelectionModel().getSelectedNode().id,locationX:locationX,locationY:locationY,name:text},
								success : function(xhr){
									Ext.Msg.hide();
									var responseText = Ext.decode(xhr.responseText);
									var lastPoint = $('#route_plan_point').children().last();
									var points = [];
									var startPoint = {'id': lastPoint.data("pointId"),'location_x': parseFloat(lastPoint.css('left')),'location_y':parseFloat(lastPoint.css('top'))};
									var endPoint = {'id': responseText.msg,'location_x': locationX,'location_y':locationY};
									points.push(startPoint);
									points.push(endPoint);
									var line_length = Math.sqrt(Math.pow((Number(startPoint.location_x)-Number(endPoint.location_x)),2)+
											Math.pow((Number(startPoint.location_y)-Number(endPoint.location_y)),2));
									drow_line(points,0,line_length);
								},
								failure:function(xhr){
									Ext.Msg.hide(); 
									Ext.Msg.alert("提示","数据添加失败,稍后请重试！");
								}
							});
						}
					});
				}else{
					Ext.Msg.alert("提示","请选择或者创建线路，然后添加路线点！");
				}
//			}
//		});
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
//    	id:'map_panel',
		header : false,
		region:'center',
		layout : 'fit',
		border : false,
//		autoHeight:true,
//		autoWidth:true,
//		autoScroll : true,
        html:'<div id="iPicture_route_plan" data-interaction="hover">' +
        		'<div class="ip_slide">' +
        			'<img id="route_plan_map" class="ip_tooltipImg" data-community-id="" onDblClick="Ext.addRoutePlanPoint(event);" src=""/>' +
        			'<div id="route_plan_device_point" class="devices">' +
	        		'</div>' +
	        		'<div id="route_plan_route_line" class="locations">' +
	        		'</div>' +
        			'<div id="route_plan_point" class="points">' +
	        		'</div>' +
        		'</div>' +
        	'</div>',
        listeners:{
        	"afterrender":function(panel){
		        //加载信号收集站点
        		mapObject = new SpryMap({id : "iPicture_route_plan",
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
    	var divHeight = $('#iPicture_route_plan').parent().parent().height();
    	var divWidth = $('#iPicture_route_plan').parent().parent().width();
    	$('#iPicture_route_plan').parent().height(divHeight);
    	$('#iPicture_route_plan').parent().width(divWidth);
    }
    //解决当地图显示区域为右下角时，再拉大浏览器窗口时会出现地图无法全部填充centerPanel的情况
    //要考虑的问题：1.地图可填充满centerPanel,但未填充满；2.地图已近展示到最大了还是无法展示全部的地图
    function refreshMapPosition(){
    	//可视窗口的大小
    	var mapHeight = parseFloat($('#iPicture_route_plan').parent().height());
    	var mapWidth = parseFloat($('#iPicture_route_plan').parent().width());
    	//地图相对于centerPanel的偏移量
    	var position = $('#iPicture_route_plan').position(); 
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
    	$('#iPicture_route_plan').animate({top:positionTop+'px',left:positionLeft+'px'},0); 
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