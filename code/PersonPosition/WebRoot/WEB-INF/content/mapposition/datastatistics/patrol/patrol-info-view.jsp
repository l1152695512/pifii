<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var mapPanel = new Ext.Panel({
    	header : false,
     	region:'center',
        layout:'fit',
	    html: '<div id="patrol-info" style="width:100%;height:100%">',
	    listeners : {
	    	'afterrender':function(){
	    		map = new BMap.Map('patrol-info');
	    		map.centerAndZoom(new BMap.Point(113.368159, 23.129269), 13);
				map.addControl(new BMap.NavigationControl());  //添加默认缩放平移控件
				map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
				map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
				map.enableAutoResize();
				map.setMinZoom(5);
		    	
		    	getRoutePoints();
	    	}
	    }
    });
    function getRoutePoints(){
    	Ext.Msg.wait('正在加载路线点....', '提示');
		Ext.Ajax.request({
			url:'mapposition/datastatistics/patrol/patrol-info-data.action',
			params : {phone:'${phone}',routeTimeId : '${routeTimeId}',date:'${date}'},
			success : function(form,action){
				Ext.Msg.hide(); 
				map.clearOverlays();
				var responseText = Ext.decode(form.responseText);
				var dataList = responseText.list;
				var points = [];
				for(var i=0;i<dataList.length;i++){
					var point = new BMap.Point(dataList[i].location_x,dataList[i].location_y)
					points.push(point);
					var icon = dataList[i].is_done == 1?'hr/img/icons/done_marker.png':'hr/img/icons/marker.png';
					var color = dataList[i].is_done == 1?'blue':'red';
					var isDone = dataList[i].is_done == 1?'完成':'未完成';
					var marker = new BMap.Marker(
						point,
						{icon:new BMap.Icon(icon, new BMap.Size(32,32),{anchor:new BMap.Size(16,25)})}
					);
					marker.setLabel(new BMap.Label('<span style="color:'+color+'" title="'+isDone+'">'+dataList[i].start_time+'-'+dataList[i].end_time+'</span>',{offset:new BMap.Size(25,0)}));
					map.addOverlay(marker);
					
//					
//					var marker = new MMarker(  
//					    point,
//					    new MIcon(icon,32,32,16,25)
//					);  
//					marker.setLabel(new MLabel('<span style="color:'+color+'" title="'+isDone+'">'+dataList[i].start_time+'-'+dataList[i].end_time+'</span>'));
//					window.maplet.addOverlay(marker);
				}
				var pline = new BMap.Polyline(points,{strokeColor:'blue',strokeWeight: 3,strokeOpacity: 0.8});  
				map.addOverlay(pline);
				map.centerAndZoom(points[0], 16);
				
//				var pline = new MPolyline(points,new MBrush());  
//	            window.maplet.addOverlay(pline);
//	            window.maplet.setCenter(points[points.length-1]);
			},
			failure:function(form,action){
				Ext.Msg.hide(); 
				Ext.Msg.alert("温馨提醒","获取路线信息失败，稍后请重试！");
			}
		});
    }
    var routeWin = new Ext.Window({
		title: '${date}&nbsp;&nbsp;>&nbsp;&nbsp;${personName}&nbsp;&nbsp;>&nbsp;&nbsp;${routeName}',
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



