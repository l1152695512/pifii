<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var isEdit = false;//判断路线是修改还是添加路线点
	var hasModified = false;//标识是否修改过路线点
	var line_brush = new MBrush();//画笔对象
	var pline = {brush: line_brush,points: [],zoom:0};//地图上的路线对象
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
//      			mapPanel.getTopToolbar().show();
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
//      		 	if(hasModified){
//      		 		Ext.Msg.confirm('确认', '确定要丢弃修改的路线点？',
//						function(btn, text) {
//							if (btn == 'yes') {
//								node.select();
//								changeRoute(node);
//							}
//					});
//					return false;
//      		 	}
      		 }
      	} 
	});
	
	function changeRoute(selectNode){
		window.maplet.clearOverlays(true);
		modify_route.show();
		save_route.hide();
		cancel.hide();
		repaint.hide();
		try{
			pline.setEditable(false); 
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
							points.push(new MPoint(dataList[i].location_x,dataList[i].location_y));
						}
						pline = new MPolyline(points,line_brush);  
			            window.maplet.addOverlay(pline);
//							window.maplet.setMode('drawline',function(dataObj){
//				        		var points = dataObj.points;
//				        		for(var i=0;i<points.length;i++){
//				        			console.debug(points[i].getPid());
//				        		}
//				        		var line_brush = dataObj.brush;
//				        		
//				        		var pline = new MPolyline(points,line_brush);  
//				                window.maplet.addOverlay(pline); 
//				                pline.setEditable(true);
//				        	});
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
	        		pline.setEditable(true);
	        		MEvent.addListener(window.maplet, "edit", function(overlay){
	        			hasModified = true;
			        });
	        	}else{
	        		drowRoute();
//	        		window.maplet.setMode('drawline',function(dataObj){
//		                hasModified = true;
//		        		var points = dataObj.points;
//		        		points.pop();
//		        		pline = new MPolyline(points,line_brush);  
//		                window.maplet.addOverlay(pline); 
//		                pline.setEditable(true);
//		        	});
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
        	var points = pline.pts;
        	if(points.length < 2){
        		Ext.Msg.alert("温馨提醒","路线点的个数必须大于2，请添加路线点！");
        		return;
        	}
        	var pointsArray = [];
        	for(var i=0;i<points.length;i++){
        		pointsArray.push(points[i].getPid());
    		}
    		var selectNode = tree.getSelectionModel().getSelectedNode();
    		Ext.Ajax.request({
				url:'mapposition/routemanage/routeplan/save-route-points.action',
				params : {routeId : selectNode.id,points : pointsArray},
				success : function(form,action){
					Ext.Msg.hide(); 
					var responseText = Ext.decode(form.responseText);
					if(responseText.success){
						changeRoute(selectNode);
        				Ext.Msg.alert("温馨提醒","路线已保存。");
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
    var firstClickDrowRoute = true;
    function drowRoute(){
//    	try{
//    		pline.setEditable(false);
//    	}catch(e){
//    	}
    	window.maplet.clearOverlays(true);
    	save_route.setDisabled(true);
    	cancel.setDisabled(true);
    	repaint.setDisabled(true);
    	if(firstClickDrowRoute){
    		window.maplet.setMode('drawline',function(dataObj){
	    		save_route.setDisabled(false);
		    	cancel.setDisabled(false);
		    	repaint.setDisabled(false);
	        	hasModified = true;
	    		var points = dataObj.points;
	//    		alert(points.length);
	//    		points.pop();
	    		pline = new MPolyline(points,line_brush);  
	            window.maplet.addOverlay(pline); 
	            pline.setEditable(true);
	    	});
	    	firstClickDrowRoute = true;
    	}else{
    		window.maplet.setMode('drawline');
    	}
    }
	var mapPanel = new Ext.Panel({
    	header : false,
     	region:'center',
        layout:'fit',
        tbar: [modify_route,save_route,'-',cancel,'-',repaint],
	    html: '<div id="mapbar" style="width:100%;height:100%">',
	    listeners : {
	    	'afterrender':function(){
	    		window.maplet = new Maplet("mapbar");  
		        window.maplet.centerAndZoom(new MPoint(113.368159, 23.129269), 12);  
		        window.maplet.addControl(new MStandardControl()); 
		    	window.maplet.resize(mapPanel.getWidth(), mapPanel.getHeight());
		    	
//		    	mapPanel.getTopToolbar().hide();
	    	},
	    	'bodyresize':function(){
				window.maplet.resize(mapPanel.getWidth(), mapPanel.getHeight()-30);
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