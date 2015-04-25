<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var map_width = 778;
	var map_height = 460;
	var patrol_info_panel=new Ext.Panel({
		header : false,
		region:'center',
		//layout : 'fit',
		border : false,
		width : map_width,
		height : map_height,
        html:'<div id="patrol_info_map" data-interaction="hover">' +
        		'<div class="ip_slide">' +
	    			'<img src="${mapPath}"/>' +
		        	'<div id="patrol_info_route_line" class="locations"></div>' +
        			'<div id="patrol_info_point" class="points"></div>' +
		        '</div>' +
        	'</div>',
        listeners:{
        	"afterrender":function(panel){
		        //加载信号收集站点
        		mapObject = new SpryMap({
        			 id : "patrol_info_map",
                     height: map_height,
                     width: map_width,
                     startX: 0,
                     startY: 0,
                     cssClass: ""
                });
            	load_route();
          	}
        }
    });
	function load_route(){
		Ext.Msg.wait('正在加载路线点....', '提示');
	   	Ext.Ajax.request({
       		url : 'datastatistics/patrol/patrol-info-data.action',
       		params : {personId : '${personId}',phone : '${phone}',routeTimeId : '${routeTimeId}',date : '${date}'},
            success : function(xhr) {
            	Ext.Msg.hide(); 
				$('#patrol_info_route_line').html('');
				var responseText = Ext.decode(xhr.responseText);
            	var dataList = responseText.list;
            	start_drow(dataList,0);
           	},
		    failure: function(xhr){
		    }
        })
	}
	var drow_route_time = 100;//画完所有路线用的时间
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
		if(index < data_list.length){
			insertRoutePlanPoint(data_list[index]);
		}
		if(index < data_list.length-1){
			drow_line(data_list,index,all_route_length);
		}
	}
	function insertRoutePlanPoint(point_obj){
		var background = "";
		var result = "";
		if(point_obj.is_done == "1"){//该检查点完成
			background = "rgba(0,255,0, 0.7)";
			result = "<span style='white-space:nowrap;word-break:break-all'>完成</span>";
		}else{
			background = "rgba(255,0,0, 0.7)";
			result = "<span style='white-space:nowrap;word-break:break-all'>未完成</span>";
		}
		var routePointHtml = '<div style="top: '+point_obj.location_y+'px; left: '+ point_obj.location_x+'px;" class="ip_tooltip ip_img32 show">' +
								'<div class="button route-plan-point">' +
									'<div style="position: absolute;font-size: 120%;background: '+background+';border-radius: 5px;height: 20px;top:-5px;left:10px;">' +
										result+
									'</div>' +
								'</div>' +
							'</div>';
		$('#patrol_info_point').append(routePointHtml);
	}
	function drow_line(data_list,index,all_route_length){//动画划线
		var start_point = data_list[index];
		var end_point = data_list[index+1];
		var lineWidth = Math.sqrt(Math.pow((Number(start_point.location_x)-Number(end_point.location_x)),2)+
					Math.pow((Number(start_point.location_y)-Number(end_point.location_y)),2));
		var angle = ((Number(start_point.location_x) > Number(end_point.location_x) == Number(start_point.location_y) > Number(end_point.location_y))?"":"-")
					+ (Math.asin(Math.abs(Number(end_point.location_y) - Number(start_point.location_y))/lineWidth)*360/2/Math.PI);
		$('#patrol_info_route_line').append('<div class="route_div" style="top: '+start_point.location_y+'px; left: '+start_point.location_x+'px;width: 0px;"></div>');
		var lineObj = $('#patrol_info_route_line').find('.route_div:last');
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
	
	var patrol_info_win = new Ext.Window({
		title: '${date}&nbsp;&nbsp;>&nbsp;&nbsp;${communityName}&nbsp;&nbsp;>&nbsp;&nbsp;${personName}&nbsp;&nbsp;>&nbsp;&nbsp;${routeName}',
		layout : 'fit',
        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        resizable:false,
        items: [patrol_info_panel]
	});
    return patrol_info_win;
})();
