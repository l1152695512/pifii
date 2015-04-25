<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var map_width = 778;
	var map_height = 460;
	var route_info_panel=new Ext.Panel({
		header : false,
		region:'center',
		//layout : 'fit',
		border : false,
		width : map_width,
		height : map_height,
        html:'<div id="iPicture_fine_show_route_points" data-interaction="click">' +
        		'<div class="ip_slide">' +
	    			'<img src="${mapPath}"/>' +
		        	'<div id="fine_show_route_points_lines" class="locations"></div>' +
        			'<div id="fine_show_route_points_points" class="points"></div>' +
		        '</div>' +
        	'</div>',
        listeners:{
        	"afterrender":function(panel){
		        //加载信号收集站点
        		mapObject = new SpryMap({
        			 id : "iPicture_fine_show_route_points",
                     height: map_height,
                     width: map_width,
                     startX: 0,
                     startY: 0,
                     cssClass: ""
                });
            	getRoutePoints();
          	}
        }
    });
	function getRoutePoints(){
		Ext.Msg.wait('正在加载路线点....', '提示');
	   	Ext.Ajax.request({
       		url : 'patrolmanage/routeassign/get-route-points.action',
       		params : {routeTimeId : '${routeTimeId}'},
            success : function(xhr) {
            	Ext.Msg.hide(); 
				$('#fine_show_route_points_lines').html('');
				var responseText = Ext.decode(xhr.responseText);
            	var dataList = responseText.list;
            	start_drow(dataList,0);
           	},
		    failure: function(xhr){
		    }
        })
	}
	var drow_route_time = 20;//画完所有路线用的时间
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
		insertRoutePoint(data_list[index]);
		if(index < data_list.length-1){
			drow_line(data_list,index,all_route_length);
		}
	}
	function insertRoutePoint(point_obj){
		var timePeriod = '&nbsp&nbsp&nbsp?&nbsp&nbsp&nbsp-&nbsp&nbsp&nbsp?&nbsp&nbsp&nbsp';
		if(point_obj.route_point_time_id != ''){
			timePeriod = point_obj.start_time+'-'+point_obj.end_time;
		}
		var routePointHtml = '<div style="top: '+point_obj.location_y+'px; left: '+ point_obj.location_x+'px;" class="ip_tooltip ip_img32 show" ' +
								'data-point-id="'+point_obj.id+'" data-id="'+point_obj.route_point_time_id+'" ' +
										'data-start-time="'+point_obj.start_time+'" data-end-time="'+point_obj.end_time+'">' +
								'<div class="button route-plan-point">' +
									'<div title="点击修改时间" style="position: absolute;font-size: 120%;background: rgba(255,255,255,0.8);border-radius: 5px;cursor: pointer;height: 20px;width: 120px;top:-5px;left:10px;">' +
										timePeriod+
									'</div>' +
								'</div>' +
							'</div>';
		$('#fine_show_route_points_points').append(routePointHtml);
		var currentPoint = $('#fine_show_route_points_points').find('.ip_tooltip:last');
		currentPoint.on('click',function(){
		    var pointId = $(this).data('pointId');
		    var id = $(this).data('id');
		    var startTime = $(this).data('startTime');
		    var endTime = $(this).data('endTime');
		    Ext.Msg.wait('获取数据中', '提示');
			Ext.Ajax.request({
				url:'patrolmanage/routeassign/add-or-modify-route-point-time.action',
				params:{id:id,routePointId:pointId,routeTimeId:'${routeTimeId}',startTime:startTime,endTime:endTime},
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
	function drow_line(data_list,index,all_route_length){//动画划线
		var start_point = data_list[index];
		var end_point = data_list[index+1];
		var lineWidth = Math.sqrt(Math.pow((Number(start_point.location_x)-Number(end_point.location_x)),2)+
					Math.pow((Number(start_point.location_y)-Number(end_point.location_y)),2));
		var angle = ((Number(start_point.location_x) > Number(end_point.location_x) == Number(start_point.location_y) > Number(end_point.location_y))?"":"-")
					+ (Math.asin(Math.abs(Number(end_point.location_y) - Number(start_point.location_y))/lineWidth)*360/2/Math.PI);
		$('#fine_show_route_points_lines').append('<div class="route_div" style="top: '+start_point.location_y+'px; left: '+start_point.location_x+'px;width: 0px;"></div>');
		var lineObj = $('#fine_show_route_points_lines').find('.route_div:last');
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
	
	var route_info_win = new Ext.Window({
		title: '路线点信息',
		layout : 'fit',
        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        resizable:false,
        items: [route_info_panel]
	});
    return route_info_win;
})();



