<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
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
		    	
		    	getRoutePoints();
	    	},
	    	'bodyresize':function(){
				window.maplet.resize(mapPanel.getWidth(), mapPanel.getHeight());
	    	}
	    }
    });
    function getRoutePoints(){
    	Ext.Msg.wait('正在加载路线点....', '提示');
		Ext.Ajax.request({
			url:'mapposition/routemanage/routeassign/get-route-points.action',
			params : {routeTimeId : '${routeTimeId}'},
			success : function(form,action){
				Ext.Msg.hide(); 
				window.maplet.clearOverlays(true);
				var responseText = Ext.decode(form.responseText);
				var dataList = responseText.list;
				var points = [];
				for(var i=0;i<dataList.length;i++){
					var point = new MPoint(dataList[i].location_x,dataList[i].location_y);
					points.push(point);
					var marker = new MMarker(  
					    point,
					    new MIcon("hr/img/icons/marker.png",32,32,16,25)
//					    new MInfoWindow('',''),
//					    new MLabel(dataList[i].start_time+'-'+dataList[i].end_time)
					);  
					marker.setLabel(new MLabel(dataList[i].start_time+'-'+dataList[i].end_time));
					window.maplet.addOverlay(marker);
					marker.id = dataList[i].route_point_time_id;
					marker.routePointId = dataList[i].id;
					marker.startTime = dataList[i].start_time;
					marker.endTime = dataList[i].end_time;
					console.debug("add marker");
					MEvent.addListener(marker, "click", function(marker) {
						console.debug(marker.id+"-------"+marker.routePointId);
						Ext.Msg.wait('获取数据中', '提示');
						Ext.Ajax.request({
							url:'mapposition/routemanage/routeassign/add-or-modify-route-point-time.action',
							params:{id:marker.id,routePointId:marker.routePointId,routeTimeId:'${routeTimeId}',startTime:marker.startTime,endTime:marker.endTime},
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
				pline = new MPolyline(points,new MBrush());  
	            window.maplet.addOverlay(pline);
	            window.maplet.setCenter(points[points.length-1]);
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



