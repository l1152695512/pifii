<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var isEdit = false;//判断区域是修改还是添加路线点
	var hasModified = false;//标识是否修改过区域
	var line_brush = new MBrush();//画笔对象
	line_brush.fill = true;
	var pline = {brush: line_brush,points: [],zoom:0};//地图上的路线对象
	var rootNodeId = '0';
	var rootNode = new Ext.tree.AsyncTreeNode({
		id : rootNodeId,
		text : '所有区域',
		leaf : false,
		draggable : false,
		expanded : true
	});
	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl : 'mapposition/areamanage/warnarea/get-areas.action'
	});
    treeLoader.on("load", function(loader, node) {
    	
    });
	function reloadTreeNode(lastChildSelect){
		var selectNode = tree.getSelectionModel().getSelectedNode() || rootNode;
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
						//选中新添加的区域
            			var addNode = node.lastChild;
            			addNode.select();
            			changeArea(addNode);
					}else{
						tree.getNodeById(selectNodeId).select();
					}
				});
			});
		}
	}
	var newAreaAction =  new Ext.Action({
		text : '增加',
		iconCls : 'icon-add',
		handler : function() {
			if(modify_area.isHidden()){//在修改区域点
  		 		Ext.Msg.alert("温馨提醒","请先完成区域的修改！");
  		 		return;
  		 	}
			Ext.Msg.wait('添加区域中....', '提示');
			Ext.Ajax.request({
				url:'mapposition/areamanage/warnarea/add-or-modify-area.action',
				success:function(xhr){
					Ext.Msg.hide(); 
					var areaWin = eval(xhr.responseText);
					areaWin.show();
				},
				failure:function(xhr){
					Ext.Msg.hide(); 
					Ext.Msg.alert('提示','暂时不能添加区域！');
				}
			});
		}
	});
	var deleteAreaAction = new Ext.Action({
		text:'删除',
		iconCls:'icon-delete',
		handler:function(){
			if(modify_area.isHidden()){//在修改路线点
  		 		Ext.Msg.alert("温馨提醒","请先完成区域的修改！");
  		 		return;
  		 	}
			var selectNode = tree.getSelectionModel().getSelectedNode();
			if(selectNode && selectNode.isLeaf()){
				Ext.Msg.confirm('确认', '确定要删除该区域？',
					function(btn, text) {
						if (btn == 'yes') {
							Ext.Msg.wait('正在删除数据....', '提示');
							Ext.Ajax.request({
					       		url:'mapposition/areamanage/warnarea/delete-area.action',
					       		params: {areaId:selectNode.id},
					            success : function(xhr) {
					            	Ext.Msg.hide(); 
					            	var obj = Ext.decode(xhr.responseText);
									if(obj.success){
										var parentNode = tree.getNodeById(selectNode.id).parentNode;
						            	parentNode.select();
						            	reloadTreeNode(false);
						            	changeArea(parentNode);
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
				Ext.Msg.alert("提示","请先选择区域！");
			}
		}
	});
	var modifyAreaAction = new Ext.Action({
		text:'修改',
		iconCls:'icon-edit',
		handler:function(){
			if(modify_area.isHidden()){//在修改路线点
  		 		Ext.Msg.alert("温馨提醒","请先完成区域的修改！");
  		 		return;
  		 	}
			var selectNode = tree.getSelectionModel().getSelectedNode();
			if(selectNode && selectNode.isLeaf()){
				var text = selectNode.text;
				Ext.Msg.wait('获取区域信息....', '提示');
				Ext.Ajax.request({
					url:'mapposition/areamanage/warnarea/add-or-modify-area.action',
					params:{id:selectNode.id},
					success:function(xhr){
						Ext.Msg.hide(); 
						var areaWin = eval(xhr.responseText);
						areaWin.show();
					},
					failure:function(xhr){
						Ext.Msg.hide(); 
						Ext.Msg.alert('提示','暂时不能修改区域！');
					}
				});
			}else{
				Ext.Msg.alert("提示","请先选择区域！");
			}
		}
	});
    var tree = new Ext.tree.TreePanel({
		title : '区域信息',
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
		tbar:[newAreaAction,'-',deleteAreaAction,'-',modifyAreaAction],
 	    listeners:{    
      		 'click':function(node, event) {
//      			mapPanel.getTopToolbar().show();
      			changeArea(node);
      		 },
      		 'beforeclick':function(node, event) {
      		 	var previousSelectNode = tree.getSelectionModel().getSelectedNode();
      		 	if(previousSelectNode){
      		 		if(previousSelectNode.id == node.id || node.id == rootNodeId){
      		 			return false;
      		 		}
      		 	}
      		 	if(modify_area.isHidden()){//在修改区域
      		 		Ext.Msg.alert("温馨提醒","请先完成区域的修改！");
      		 		return false;
      		 	}
//      		 	if(hasModified){
//      		 		Ext.Msg.confirm('确认', '确定要丢弃修改的区域？',
//						function(btn, text) {
//							if (btn == 'yes') {
//								node.select();
//								changeArea(node);
//							}
//					});
//					return false;
//      		 	}
      		 }
      	} 
	});
	
	function changeArea(selectNode){
		window.maplet.clearOverlays(true);
		modify_area.show();
		save_area.hide();
		cancel.hide();
		repaint.hide();
		try{
			pline.setEditable(false); 
		}catch(e){
		}
		hasModified = false;
		newAreaAction.setDisabled(false);
    	deleteAreaAction.setDisabled(false);
    	modifyAreaAction.setDisabled(false);
		if(selectNode.isLeaf()){
			Ext.Msg.wait('正在加载区域....', '提示');
			Ext.Ajax.request({
				url:'mapposition/areamanage/warnarea/get-area-points.action',
				params : {areaId : selectNode.id},
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
					}else{
						isEdit = false;
					}
				},
				failure:function(form,action){
					Ext.Msg.hide(); 
					Ext.Msg.alert("温馨提醒","获取区域信息失败，稍后请重试！");
				}
			});
		}
	}
	var modify_area= new Ext.Action({
        text: '修改',
        iconCls :'icon-edit',
        handler: function(){
        	var selectNode = tree.getSelectionModel().getSelectedNode();
        	if(selectNode && selectNode.isLeaf()){
        		modify_area.hide();
	        	save_area.show();
	        	cancel.show();
	        	repaint.show();
	        	newAreaAction.setDisabled(true);
	        	deleteAreaAction.setDisabled(true);
	        	modifyAreaAction.setDisabled(true);
	        	if(isEdit){
	        		pline.setEditable(true);
	        		MEvent.addListener(window.maplet, "edit", function(overlay){
	        			hasModified = true;
			        });
	        	}else{
	        		drowArea();
	        	}
        	}else{
        		Ext.Msg.alert("温馨提醒","请先选择区域！");
        	}
        }
    });
    var save_area= new Ext.Action({
        text: '保存',
        iconCls :'icon-save',
        hidden : true,
        handler: function(){
        	if(!isEdit && !hasModified){
        		Ext.Msg.alert("温馨提醒","请先双击地图结束区域修改！");
        		return;
        	}
        	var points = pline.pts;
        	if(points.length < 3){
        		Ext.Msg.alert("温馨提醒","区域点的个数必须大于3，请重新添加区域！");
        		return;
        	}
        	var pointsArray = [];
        	for(var i=0;i<points.length;i++){
        		pointsArray.push(points[i].getPid());
    		}
    		var selectNode = tree.getSelectionModel().getSelectedNode();
    		Ext.Ajax.request({
				url:'mapposition/areamanage/warnarea/save-area-points.action',
				params : {areaId : selectNode.id,points : pointsArray},
				success : function(form,action){
					Ext.Msg.hide(); 
					var responseText = Ext.decode(form.responseText);
					if(responseText.success){
						changeArea(selectNode);
        				Ext.Msg.alert("温馨提醒","区域已保存。");
					}else{
						Ext.Msg.alert("温馨提醒","保存区域失败，稍后请重试！");
					}
				},
				failure:function(form,action){
					Ext.Msg.hide(); 
					Ext.Msg.alert("温馨提醒","保存区域失败，稍后请重试！");
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
        		Ext.Msg.alert("温馨提醒","请先双击地图结束区域修改！");
        		return;
        	}
        	var selectNode = tree.getSelectionModel().getSelectedNode();
        	if(hasModified){
	        	Ext.Msg.confirm('确认', '确定要丢弃修改的区域？',
					function(btn, text) {
						if (btn == 'yes') {
							changeArea(selectNode);
						}
				});
	    	}else{
	    		changeArea(selectNode);
	    	}
        }
    });
    
    var repaint= new Ext.Action({
        text: '重画区域',
        iconCls :'icon-clear-route',
        hidden : true,
        handler: function(){
        	drowArea();
        }
    });
    var firstClickDrowArea = true;
    function drowArea(){
//    	try{
//    		pline.setEditable(false);
//    	}catch(e){
//    	}
    	window.maplet.clearOverlays(true);
    	save_area.setDisabled(true);
    	cancel.setDisabled(true);
    	repaint.setDisabled(true);
    	if(firstClickDrowArea){//第二次以后的点击，不能再次写回调函数，否则会点击几次，回调函数就在该次点击执行几遍
    		window.maplet.setMode('drawarea',function(dataObj){
	    		save_area.setDisabled(false);
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
    		firstClickDrowArea = false;
    	}else{
    		window.maplet.setMode('drawarea');
    	}
    }
	var mapPanel = new Ext.Panel({
    	header : false,
     	region:'center',
        layout:'fit',
        tbar: [modify_area,save_area,'-',cancel,'-',repaint],
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
		title : '警告区域管理',
		border : false,
		layout : 'border',
		items : [tree,mapPanel]
	});
    return mainPanel;
})();