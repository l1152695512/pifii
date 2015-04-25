<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var map;
	var mapPanel = new Ext.Panel({
    	header : false,
     	region:'center',
        layout:'fit',
	    html: '<div id="route_points" style="width:100%;height:100%">',
	    listeners : {
	    	'afterrender':function(){
//	    		window.maplet = new Maplet("route_points");  
//		        window.maplet.centerAndZoom(new MPoint(113.368159, 23.129269), 12);  
//		        window.maplet.addControl(new MStandardControl()); 
//		    	window.maplet.resize(mapPanel.getWidth(), mapPanel.getHeight());
		    	
		    	map = new BMap.Map('route_points');
	    		map.centerAndZoom(new BMap.Point(113.368159, 23.129269), 13);
				map.addControl(new BMap.NavigationControl());  //添加默认缩放平移控件
				map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
				map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
				map.enableAutoResize();
				map.setMinZoom(5);
		    	
		    	getRoutePoints(true);
	    	}
	    }
    });
    function getRoutePoints(moveCenter){
    	Ext.Msg.wait('正在加载路线点....', '提示');
		Ext.Ajax.request({
			url:'mapposition/routemanage/routeassign/get-route-points.action',
			params : {routeTimeId : '${routeTimeId}'},
			success : function(form,action){
				Ext.Msg.hide(); 
				map.clearOverlays();
				var responseText = Ext.decode(form.responseText);
				var dataList = responseText.list;
				var points = [];
				for(var i=0;i<dataList.length;i++){
					var point = new BMap.Point(dataList[i].location_x,dataList[i].location_y)
					points.push(point);
					var marker = new BMap.Marker(
						point,
						{icon:new BMap.Icon("hr/img/icons/marker.png", new BMap.Size(32,32),{anchor:new BMap.Size(16,25)})}
					);
					
					marker.setLabel(new BMap.Label(dataList[i].start_time+'-'+dataList[i].end_time,{offset:new BMap.Size(20,5)}));
					map.addOverlay(marker);
					marker.id = dataList[i].route_point_time_id;
					marker.routePointId = dataList[i].id;
					marker.startTime = dataList[i].start_time;
					marker.endTime = dataList[i].end_time;
					marker.addEventListener('click',function(type, target){
						console.debug(this.id+"----------"+this.routePointId+"----------"+this.startTime+"------"+this.endTime);
						Ext.Msg.wait('获取数据中', '提示');
						Ext.Ajax.request({
							url:'mapposition/routemanage/routeassign/add-or-modify-route-point-time.action',
							params:{id:this.id,routePointId:this.routePointId,routeTimeId:'${routeTimeId}',startTime:this.startTime,endTime:this.endTime},
							success:function(xhr){
								Ext.Msg.hide(); 
								var point_edit_win = eval(xhr.responseText);
								point_edit_win.show();
							},
							failure:function(xhr){
								Ext.Msg.hide(); 
								Ext.Msg.alert('提示','获取数据失败，稍后请重试！');
							}
						});
					});
				}
				var pline = new BMap.Polyline(points,{strokeColor:'blue',strokeWeight: 3,strokeOpacity: 0.8});  
	            map.addOverlay(pline);
	            if(moveCenter){
	           		map.centerAndZoom(points[0],16);
	            }
			},
			failure:function(form,action){
				Ext.Msg.hide(); 
				Ext.Msg.alert("温馨提醒","获取路线信息失败，稍后请重试！");
			}
		});
    }
    var routeWin = new Ext.Window({
		title: '路线信息',
        width: 700,
        height:400, 
//        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        resizable:false,
        items: [mapPanel]
	});
    return routeWin;
})();



