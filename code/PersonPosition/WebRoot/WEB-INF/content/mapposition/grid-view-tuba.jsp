<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var rootId = '402881fe3127d5510131283920d30001';
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
					window.maplet.centerAndZoom(new MPoint(personsLocation[i].location_x, personsLocation[i].location_y),12);
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
//      removeAllMarker();
    	window.maplet.clearOverlays(true);//清除地图上的标记点
    	var marker = new MMarker(
		    new MPoint(113.34837276504,23.147850685927),
		    new MIcon("hr/img/person_photo.png", 16, 16),
		    new MInfoWindow("test", "定位时间：\n手机号码："),  
		    new MLabel("test")
		);  
		window.maplet.addOverlay(marker);
		window.maplet.setCenter(new MPoint(113.34837276504,23.147850685927));
    	
//    	for(var i=0;i<personsLocation.length;i++){
//    		if(personsLocation[i].location_x != ''){
//    			leftPanel.getNodeById(personsLocation[i].phone).setText(personsLocation[i].name);
//    			var marker = new MMarker(
//				    new MPoint(Number(personsLocation[i].location_x),Number(personsLocation[i].location_y)),
//				    new MIcon("hr/img/person_photo.png", 16, 16),
//				    new MInfoWindow(personsLocation[i].name, "定位时间："+personsLocation[i].upload_date+"\n手机号码："+personsLocation[i].phone),  
//				    new MLabel(personsLocation[i].name)
//				);  
//				window.maplet.addOverlay(marker);
//				window.maplet.setCenter(new MPoint(personsLocation[i].location_x, personsLocation[i].location_y));
////				allMarkers.push(marker);
//    		}else{
//    			var title = "";
//    			if("0" == personsLocation[i].info){
//    				title = "暂时无法获取位置信息";
//    			}
//    			leftPanel.getNodeById(personsLocation[i].phone).setText("<span style='color:red' title='"+title+"'>"+personsLocation[i].name+"</span>");
//    		}
//		}
	}
	function changeFunction(){
		auto_refersh.setValue(false);//取消自动刷新
		refresh_location_task.cancel();//停止刷新位置信息
//		removeAllMarker();
		window.maplet.clearOverlays(true);//清除地图上的标记点
//		try{
//    		window.maplet.removeOverlay(pline);//清除地图上搜索的历史轨迹
//    	}catch(e){
//    	}
	}
//	function removeAllMarker(){
//		for(var i=0;i<allMarkers.length;i++){
//			window.maplet.removeOverlay(allMarkers[i]);
//		}
//	}
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
						dateRangeWin.show();
		           	},
				    failure: function(xhr){
				    	Ext.Msg.hide(); 
				    }
		        })
        	}
        }
    });
    var firstClickFindInArea = true;
    var area_person_find = new Ext.Action({
        text: '区域查询',
        iconCls :'icon-find',
        handler: function(){
        	window.maplet.clearOverlays(true);//清除地图上的标记点
        	refresh_location.setDisabled(true);
        	history_route.setDisabled(true);
        	area_person_find.setDisabled(true);
        	if(firstClickFindInArea){
        		window.maplet.setMode('lookup',findPersonInArea);
        		firstClickFindInArea = false;
        	}else{
        		window.maplet.setMode('lookup');
        	}
        }
    });
    function findPersonInArea(dataObj){
		var min = dataObj.min;
		var max = dataObj.max;
		Ext.Ajax.request({
       		url : 'mapposition/person-location-in-area.action',
			params : {minLocationX:min.lon,maxLocationX:max.lon,minLocationY:min.lat,maxLocationY:max.lat},
            success : function(xhr) {
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
	    html: '<div id="mapbar" style="width:100%;height:100%">',
	    listeners : {
	    	'afterrender':function(){
	    		window.maplet = new Maplet("mapbar");  
		        window.maplet.centerAndZoom(new MPoint(113.368159, 23.129269), 9);  
		        window.maplet.addControl(new MStandardControl()); 
		        
		    	window.maplet.resize(mapPanel.getWidth(), mapPanel.getHeight()-30);
	    	},
	    	'bodyresize':function(){
				window.maplet.resize(mapPanel.getWidth(), mapPanel.getHeight()-30);
	    	}
	    }
    });
    
    var mainPanel = new Ext.Panel({
       	layout:'border',
    	items:[leftPanel,mapPanel]
    });
    
    return mainPanel;
})();