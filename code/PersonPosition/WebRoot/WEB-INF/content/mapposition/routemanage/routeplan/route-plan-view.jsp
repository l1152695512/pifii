<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var map;
	var drawingManager;
	var pline;//地图上的路线对象
	
	var isEdit = false;//判断路线是修改还是添加路线点
	var hasModified = false;//标识是否修改过路线点
	var rootNodeId = '0';
	var rootNode = new Ext.tree.AsyncTreeNode({
		id : rootNodeId,
		text : '所有路线',
		leaf : false,
		draggable : false,
		expanded : true
	});
	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl : 'mapposition/routemanage/routeplan/get-routes.action'
	});
    treeLoader.on("load", function(loader, node) {
    });
	function reloadTreeNode(lastChildSelect){
		var selectNode = tree.getSelectionModel().getSelectedNode() ||　rootNode;
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
			if(modify_route.isHidden()){//在修改路线点
  		 		Ext.Msg.alert("温馨提醒","请先完成路线点的修改！");
  		 		return;
  		 	}
			Ext.Msg.wait('添加路线中....', '提示');
			Ext.Ajax.request({
				url:'mapposition/routemanage/routeplan/add-or-modify-route.action',
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
		}
	});
	var deleteRouteAction = new Ext.Action({
		text:'删除',
		iconCls:'icon-delete',
		handler:function(){
			if(modify_route.isHidden()){//在修改路线点
  		 		Ext.Msg.alert("温馨提醒","请先完成路线点的修改！");
  		 		return;
  		 	}
			var selectNode = tree.getSelectionModel().getSelectedNode();
			if(selectNode && selectNode.isLeaf()){
				Ext.Msg.confirm('确认', '确定要删除该路线？',
					function(btn, text) {
						if (btn == 'yes') {
							Ext.Msg.wait('正在删除数据....', '提示');
							Ext.Ajax.request({
					       		url:'mapposition/routemanage/routeplan/delete-route.action',
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
				Ext.Msg.alert("提示","请先选择路线！");
			}
		}
	});
	var modifyRouteAction = new Ext.Action({
		text:'修改',
		iconCls:'icon-edit',
		handler:function(){
			if(modify_route.isHidden()){//在修改路线点
  		 		Ext.Msg.alert("温馨提醒","请先完成路线点的修改！");
  		 		return;
  		 	}
			var selectNode = tree.getSelectionModel().getSelectedNode();
			if(selectNode && selectNode.isLeaf()){
				var text = selectNode.text;
				Ext.Msg.wait('获取路线信息....', '提示');
				Ext.Ajax.request({
					url:'mapposition/routemanage/routeplan/add-or-modify-route.action',
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
				Ext.Msg.alert("提示","请先选择路线！");
			}
		}
	});
    var tree = new Ext.tree.TreePanel({
		title : '路线信息',
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
      			changeRoute(node);
      		 },
      		 'beforeclick':function(node, event) {
      		 	var previousSelectNode = tree.getSelectionModel().getSelectedNode();
      		 	if(previousSelectNode){
      		 		if(previousSelectNode.id == node.id || node.id == rootNodeId){
      		 			return false;
      		 		}
      		 	}
      		 	if(modify_route.isHidden()){//在修改路线点
      		 		Ext.Msg.alert("温馨提醒","请先完成路线点的修改！");
      		 		return false;
      		 	}
      		 }
      	} 
	});
	
	function changeRoute(selectNode){
		map.clearOverlays();//清除地图上的标记点
		modify_route.show();
		save_route.hide();
		cancel.hide();
		repaint.hide();
		try{
			pline.disableEditing();
		}catch(e){
		}
		hasModified = false;
		newRouteAction.setDisabled(false);
    	deleteRouteAction.setDisabled(false);
    	modifyRouteAction.setDisabled(false);
		if(selectNode.isLeaf()){
			Ext.Msg.wait('正在加载路线点....', '提示');
			Ext.Ajax.request({
				url:'mapposition/routemanage/routeplan/get-route-points.action',
				params : {routeId : selectNode.id},
				success : function(form,action){
					Ext.Msg.hide(); 
					var responseText = Ext.decode(form.responseText);
					var dataList = responseText.list;
					if(dataList.length > 0){
						isEdit = true;
						var points = [];
						for(var i=0;i<dataList.length;i++){
							points.push(new BMap.Point(dataList[i].location_x,dataList[i].location_y));
						}
						pline = new BMap.Polyline(points,{strokeColor:'blue',strokeWeight: 3,strokeOpacity: 0.8});  
			            map.addOverlay(pline);
			            map.centerAndZoom(points[0], 16);
					}else{
						isEdit = false;
					}
				},
				failure:function(form,action){
					Ext.Msg.hide(); 
					Ext.Msg.alert("温馨提醒","获取路线信息失败，稍后请重试！");
				}
			});
		}
	}
	var modify_route= new Ext.Action({
        text: '修改',
        iconCls :'icon-edit',
        handler: function(){
        	var selectNode = tree.getSelectionModel().getSelectedNode();
        	if(selectNode && selectNode.isLeaf()){
        		modify_route.hide();
	        	save_route.show();
	        	cancel.show();
	        	repaint.show();
	        	newRouteAction.setDisabled(true);
	        	deleteRouteAction.setDisabled(true);
	        	modifyRouteAction.setDisabled(true);
	        	if(isEdit){
	        		pline.enableEditing();
	        		pline.addEventListener('lineupdate', function(type, target){
	        			hasModified = true;
	        		});
	        	}else{
	        		drowRoute();
	        	}
        	}else{
        		Ext.Msg.alert("温馨提醒","请先选择路线！");
        	}
        }
    });
    var save_route= new Ext.Action({
        text: '保存',
        iconCls :'icon-save',
        hidden : true,
        handler: function(){
        	if(!isEdit && !hasModified){
        		Ext.Msg.alert("温馨提醒","请先双击地图结束路线！");
        		return;
        	}
        	var points = pline.getPath();
        	if(points.length < 2){
        		Ext.Msg.alert("温馨提醒","路线点的个数必须大于2，请添加路线点！");
        		return;
        	}
        	var pointsArray = [];
        	for(var i=0;i<points.length;i++){
        		pointsArray.push(points[i].lng+","+points[i].lat);
    		}
    		var selectNode = tree.getSelectionModel().getSelectedNode();
    		Ext.Msg.wait('保存数据中....', '提示');
    		Ext.Ajax.request({
				url:'mapposition/routemanage/routeplan/save-route-points.action',
				params : {routeId : selectNode.id,points : pointsArray},
				success : function(form,action){
					Ext.Msg.hide(); 
					var responseText = Ext.decode(form.responseText);
					if(responseText.success){
						changeRoute(selectNode);
					}else{
						Ext.Msg.alert("温馨提醒","保存线路点失败，稍后请重试！");
					}
				},
				failure:function(form,action){
					Ext.Msg.hide(); 
					Ext.Msg.alert("温馨提醒","保存线路点失败，稍后请重试！");
				}
			});
        }
    });
    var cancel= new Ext.Action({
        text: '取消',
        iconCls :'icon-cancel',
        hidden : true,
        handler: function(){
        	if(!isEdit && !hasModified){
        		Ext.Msg.alert("温馨提醒","请先双击地图结束路线！");
        		return;
        	}
        	var selectNode = tree.getSelectionModel().getSelectedNode();
        	if(hasModified){
	        	Ext.Msg.confirm('确认', '确定要丢弃修改的路线点？',
					function(btn, text) {
						if (btn == 'yes') {
							changeRoute(selectNode);
						}
				});
	    	}else{
	    		changeRoute(selectNode);
	    	}
        }
    });
    
    var repaint= new Ext.Action({
        text: '重画路线',
        iconCls :'icon-clear-route',
        hidden : true,
        handler: function(){
        	drowRoute();
        }
    });
    function drowRoute(){
    	map.clearOverlays();
    	save_route.setDisabled(true);
    	cancel.setDisabled(true);
    	repaint.setDisabled(true);
    	drawingManager.setDrawingMode(BMAP_DRAWING_POLYLINE);
        drawingManager.open();
    }
	var mapPanel = new Ext.Panel({
    	header : false,
     	region:'center',
        layout:'fit',
        tbar: [modify_route,save_route,'-',cancel,'-',repaint],
	    html: '<div id="route_plan" style="width:100%;height:100%">',
	    listeners : {
	    	'afterrender':function(){
		    	map = new BMap.Map('route_plan');
	    		map.centerAndZoom(new BMap.Point(113.368159, 23.129269), 13);
				map.addControl(new BMap.NavigationControl());  //添加默认缩放平移控件
				map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
				map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
				map.enableAutoResize();
				map.setMinZoom(5);
				
				drawingManager = new BMapLib.DrawingManager(map, {
			        isOpen: false, //是否开启绘制模式
			        enableDrawingTool: false, //是否显示工具栏
			        polylineOptions: {
				        strokeColor:"blue",    //边线颜色。
				        fillColor:"",      //填充颜色。当参数为空时，圆形将没有填充效果。
				        strokeWeight: 3,       //边线的宽度，以像素为单位。
				        strokeOpacity: 0.8,	   //边线透明度，取值范围0 - 1。
				        fillOpacity: 0.6,      //填充的透明度，取值范围0 - 1。
				        strokeStyle: 'solid' //边线的样式，solid或dashed。
				    } //矩形的样式
			    });
			    //添加鼠标绘制工具监听事件，用于获取绘制结果
			    drawingManager.addEventListener('overlaycomplete', function(e){
			    	drawingManager.close();
			    	save_route.setDisabled(false);
			    	cancel.setDisabled(false);
			    	repaint.setDisabled(false);
		        	hasModified = true;
		        	pline = e.overlay;
		    		pline.enableEditing();
			    });
	    	}
	    }
    });
	var mainPanel = new Ext.Panel({
    	header : false,
		title : '路线规划',
		border : false,
		layout : 'border',
		items : [tree,mapPanel]
	});
    return mainPanel;
})();