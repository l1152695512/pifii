<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var refreshPersonTime = 10000;
	var map;
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
//        this.baseParams.nodeId = node.id;
        this.baseParams.type = node.attributes.type;
    });
    treeLoader.on("load", function(loader, node) {
        var nodes = node.childNodes;
		for(var i=0;i<nodes.length;i++){
			nodes[i].expand();
		}
    });
//  var positionTask = new Ext.util.DelayedTask(function(){//刷新楼层中的人
//    	updatePerson(rootNode);
//    	positionTask.delay(refreshPersonTime);
//	});
//	function updatePerson(node){
//		//console.debug(node);
//		if(node.attributes.type == "3"){
//			if(node.attributes.refresh == "1"){
//				//console.debug(node);
//				var isExpanded = node.isExpanded();
//				treeLoader.load(node,function(loadNode){
//					if(isExpanded){
//						loadNode.expand();
//					}
//				});
//			}
//		}else{
//			if(node.loaded){
//				for(var i=0;i<node.childNodes.length;i++){
//				updatePerson(node.childNodes[i]);
//			}
//			}
//		}
//	}
 	var leftPanel = new Ext.tree.TreePanel({
 		region : 'west',
    	title : '小区定位',
	    autoScroll : true,
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
      			if(node.attributes.type == "3"){
      				var lng = node.attributes.longitude;
      				var lat = node.attributes.latitude;
      				var id = node.attributes.id;
      				var text = node.attributes.text;
      				var previewImage = node.attributes.previewImage;
      				var areaType = node.attributes.area_type;
      				addMarker(id,lng,lat,text,previewImage,areaType);
      			}else if(node.attributes.type == "4"){
      				movePersonToCenter(node.id);
      			}else{
      				//map.clearOverlays();//清除地图上的标记点
      			}
      		 },
      		 'checkchange':function(thisNode,isChecked){
      		 	var nodeText = $(thisNode.text).html();
				if(!isChecked && thisNode.attributes.type == "4" && undefined != nodeText){//当取消选中时，去掉人员的红色样式
					thisNode.setText(nodeText.substring(0,nodeText.indexOf("(")));
				}
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
				if(personsLocation[i].upload_date != ''){
					var marker = new BMap.Marker(
						new BMap.Point(Number(personsLocation[i].location_x),Number(personsLocation[i].location_y)),
						{icon:new BMap.Icon("hr/img/person_photo.png", new BMap.Size(16,16))}
					);
					var label = new BMap.Label(personsLocation[i].name+" - "+personsLocation[i].upload_date,{offset:new BMap.Size(16,0)});
					marker.setLabel(label);
	    			map.addOverlay(marker);
					//百度地图的zoom值为3-18
					map.centerAndZoom(new BMap.Point(personsLocation[i].location_x, personsLocation[i].location_y), map.getZoom());//
				}
				return;
			}
		}
	}
	var refresh_location= new Ext.Action({
        text: '刷新人员位置',
        tooltip:'自动刷新',
        iconCls :'x-tbar-loading',
        handler: function(){
        	changeFunction();
			refresh_location_task.delay(0);
        }
    });
    function changeFunction(){
		auto_refersh.setValue(false);//取消自动刷新
		refresh_location_task.cancel();//停止刷新位置信息
		map.clearOverlays();//清除地图上的标记点
	}
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
	var refreshTime = 10000;
	var personsLocation = [];//存储请求的人员位置信息，用于点击tree中的人员时，将该人员的位置移动到屏幕中间
	var refresh_location_task = new Ext.util.DelayedTask(function(){
		var phones = [];
		Ext.each(leftPanel.getChecked(), function(node) {
			if(node.attributes.type == '4'){//是人员
				phones.push(node.id);
			}
		});
	   	Ext.Ajax.request({
       		url : 'map/gps-data.action',
			params : {phones:phones},
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
    		if(personsLocation[i].upload_date != ''){
    			leftPanel.getNodeById(personsLocation[i].phone).setText(personsLocation[i].name);
//    			if(personsLocation[i].is_gps == "1"){
//    				var gpsPoint = new BMap.Point(Number(personsLocation[i].location_x),Number(personsLocation[i].location_y));
//	    			gpsPoint.labelText = personsLocation[i].name+" - "+personsLocation[i].upload_date;
//					BMap.Convertor.translate(gpsPoint,0,translateCallback);//真实经纬度转成百度坐标
//    			}else{
	    			var marker = new BMap.Marker(
						new BMap.Point(Number(personsLocation[i].location_x),Number(personsLocation[i].location_y)),
						{icon:new BMap.Icon("hr/img/person_photo.png", new BMap.Size(16,16))}
					);
					var label = new BMap.Label(personsLocation[i].name+" - "+personsLocation[i].upload_date,{offset:new BMap.Size(16,0)});
					marker.setLabel(label);
	    			map.addOverlay(marker);
					map.setCenter(new BMap.Point(personsLocation[i].location_x, personsLocation[i].location_y));
//    			}
    		}else{
    			leftPanel.getNodeById(personsLocation[i].phone).setText("<span style='color:red'>"+personsLocation[i].name+"(无位置信息)</span>");
    		}
		}
	}
//	translateCallback = function (point){
//		console.debug(point);
//		console.debug(aa);
//	    var marker = new BMap.Marker(point,{icon:new BMap.Icon("hr/img/person_photo.png", new BMap.Size(16,16))});
//	    map.addOverlay(marker);
//	    var label = new BMap.Label("我是百度标注哦",{offset:new BMap.Size(16,0)});
//	    marker.setLabel(label); //添加百度label
//	    map.setCenter(point);
//	    //alert(point.lng + "," + point.lat);
//	}
//	var map = null;
    var mapPanel = new Ext.Panel({
    	header : false,
     	region:'center',
        layout:'fit',
	    html: '<div id="community_map" style="width:100%;height:100%">',
	    tbar: [auto_refersh,refresh_location],
	    listeners : {
			'afterrender':function(){
				map = new BMap.Map('community_map');
	    		map.centerAndZoom(new BMap.Point(113.368159, 23.129269), 11);
				map.addControl(new BMap.NavigationControl());  //添加默认缩放平移控件
				map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
				map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
//				map.disableAutoResize();
				map.enableAutoResize();
				map.setMinZoom(5);
				new Ext.util.DelayedTask(function(){
					map.zoomIn();
				}).delay(500);
//				positionTask.delay(0);
			}
		}
    });
	function addMarker(id,lng,lat,text,previewImage,areaType){
		var point = new BMap.Point(lng, lat);
		var marker = new BMap.Marker(
			point,
			{icon:new BMap.Icon("hr/img/net_16.png", new BMap.Size(16,16))}
		);
		var opts = {
		  width : 300,     // 信息窗口宽度
		  height: 200,     // 信息窗口高度
		  title : text , // 信息窗口标题
		  enableMessage:false//设置允许信息窗发送短息
		}
		var infoWindow = new BMap.InfoWindow('<img onclick="window.enterCommunity(\''+id+'\',\''+text+'\',\''+areaType+'\')" src="'+previewImage+'" style="cursor:pointer;position:absolute;width:97%;height:90%;"><br><br><br>没有添加预览图片</img>', opts);  // 创建信息窗口对象
		map.openInfoWindow(infoWindow,point); //开启信息窗口
	
		var label = new BMap.Label(text,{offset:new BMap.Size(16,0)});
		marker.setLabel(label);
		map.addOverlay(marker);
		marker.addEventListener("click", function(){
			this.openInfoWindow(infoWindow);
			});
	}
	window.enterCommunity = function(communityId,text,areaType){
	  	Ext.Msg.wait('获取地图信息....', '提示');
	  	var url = 'mjgl/work-view.action';
	  	if(areaType == '2'){
	  		url = 'mapfloor/index.action';
	  	}
		Ext.Ajax.request({
			url:url,
			params:{id:communityId,title:text},
			success:function(xhr){
				Ext.Msg.hide();
				var map_info_win = eval(xhr.responseText);
				map_info_win.show();
			},
			failure:function(xhr){
				Ext.Msg.hide(); 
				Ext.Msg.alert('提示','获取信息失败，稍后请重试！');
			}
		});
	}
  
    var mainPanel = new Ext.Panel({
       	layout:'border',
    	items:[leftPanel,mapPanel]
    });
    
    return mainPanel;
})();