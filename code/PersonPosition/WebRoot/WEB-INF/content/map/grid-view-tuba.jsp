<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){

	var rootId = '402881fe3127d5510131283920d30001';
	var rootNode = new Ext.tree.AsyncTreeNode({
		text : '广东省小区',
		id : rootId,
		icon:'hr/img/e_16.png',
		expanded : true
	});
	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl: 'map/tree-data.action'
	});
	treeLoader.on("beforeload", function(loader, node) {
        this.baseParams.nodeId = node.id;
        this.baseParams.type = node.attributes.type;
    });
 	var leftPanel = new Ext.tree.TreePanel({
 		region : 'west',
    	title : '小区定位',
	    autoScroll : true,
      	animate : true,
      	width: 200,
       	containerScroll : true,
       	lines:true,
       	rootVisible : true,
	    autoScroll : true,
	    collapsible:true,
        collapseMode:'mini',
        //split:true,
	    root: rootNode,
	    loader : treeLoader,
  	    plugins:[
  	        new Ext.plugin.tree.TreeNodeChecked({
 	        	cascadeCheck: true,
 	          	cascadeParent: false,
 	          	cascadeChild: true,
  	          	linkedCheck: false, 
  	          	asyncCheck: false, 
 	          	displayAllCheckbox: false
	        })
 	    ],
 	    listeners:{    
      		'click':function(node, event) {
      			if(node.isLeaf()){
      				var lng = node.attributes.longitude;
      				var lat = node.attributes.latitude;
      				var id = node.attributes.id;
      				var text = node.attributes.text;
      				var previewImage = node.attributes.previewImage;
      				addMarker(id,lng,lat,text,previewImage);
      			}else{
      				window.maplet.clearOverlays(true);
//      				window.maplet.reset();
      			}
      		 }
      	} 
	});
	
//	var map = null;
    var mapPanel = new Ext.Panel({
    	header : false,
     	region:'center',
        layout:'fit',
	    html: '<div id="mapbar" style="width:100%;height:100%">',
	    listeners : {
			'afterrender':function(){
				window.maplet = new Maplet("mapbar");  
		        window.maplet.centerAndZoom(new MPoint(113.368159, 23.129269), 12);  
		        window.maplet.addControl(new MStandardControl()); 
		        
		    	window.maplet.resize(mapPanel.getWidth(), mapPanel.getHeight());
			},
			'bodyresize':function(){
				window.maplet.resize(mapPanel.getWidth(), mapPanel.getHeight());
			}
		}
    });
    
//    mapPanel.on('afterrender',function(){
//    	window.map = new BMap.Map('allmap');
//		window.map.centerAndZoom(new BMap.Point(113.368159, 23.129269), 13);
//		window.map.addControl(new BMap.NavigationControl());  //添加默认缩放平移控件
//		window.map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
//		window.map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
//		window.map.disableAutoResize();
//		window.map.enableAutoResize();
//		window.map.setMinZoom(5);
//    },mapPanel,{delay:500});
    
	//#################################################################
	
	function addMarker(id,lng,lat,text,previewImage){
		window.maplet.setCenter(new MPoint(lng, lat));
		var marker = new MMarker(  
		    new MPoint(lng, lat),  
		    new MIcon("hr/img/net_16.png", 16, 16),
		    new MInfoWindow(text,'<img src="'+previewImage+'" style="position:absolute;width:95%;height:95%;;"><br><br><br>没有添加预览图片</img>'),
		    new MLabel(text)
		);  
//		window.maplet.setIwStdSize(300,200);  
//        window.maplet.setIwZoomInSize(300,200);  
		window.maplet.addOverlay(marker);  
		marker.openInfoWindow();  
		
		
		
//		var marker = 'marker'+id;
//		var pt = new BMap.Point(lng, lat);
//		marker = new BMap.Marker(pt);  // 创建标注
//		var label = new BMap.Label(text,{offset:new BMap.Size(20,-10)});
//		marker.setLabel(label);
//		
//		window.map.addOverlay(marker);
//		window.map.setCenter(pt);
//		window.map.setZoom(15);
		//创建信息窗口
		MEvent.addListener(marker, "click", function(marker) {
//			if("御江南" == text){
				Ext.Msg.wait('获取地图信息....', '提示');
				Ext.Ajax.request({
					url:'mjgl/work-view.action',
					params:{id:id},
					success:function(xhr){
						Ext.Msg.hide(); 
						var map_info = eval(xhr.responseText);
						var map_info_win = new Ext.Window({
							title: text,
					        width: 1200,
					        height:500, 
					        border:false,
					        modal :true,
					        layout: 'fit',
					        plain:true,
						    constrain: true,
					        closable: true,
					        bodyStyle:'padding:5px;',
					        resizable:true,
					        items: [map_info]
						});
						map_info_win.show();
					},
					failure:function(xhr){
						Ext.Msg.hide(); 
						Ext.Msg.alert('提示','获取信息失败，稍后请重试！');
					}
				});
//			}else{
//				addLazyLoadWorkSpaceTab(id,text,'mjgl/work-view.action?id='+id,{layout:'anchor'},null,null,null); 
//			}
		});
	}
	
	//################################################################
    
    var mainPanel = new Ext.Panel({
       	layout:'border',
    	items:[leftPanel,mapPanel]
    });
    
    
    return mainPanel;
})();