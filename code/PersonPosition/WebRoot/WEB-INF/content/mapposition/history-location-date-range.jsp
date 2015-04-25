<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pline = null;
	var drowLineTime = 200;
	var drowLineTask = new Ext.util.DelayedTask();
	function drowLine(locations,index){
		if(index < locations.length){
			var addPoint = new BMap.Point(locations[index].location_x,locations[index].location_y);
			pline.setPositionAt(index, addPoint);
            map.setCenter(addPoint);
			drowLineTask.delay(drowLineTime,drowLine,this,[locations,++index]);
		}
	}
	var dateRangePanel = new Ext.FormPanel({
//		url:'personmanage/save-person.action',
//		title : '',
		labelWidth: 70, 
      	defaults: {
      		anchor: '90%'
      	},
        frame:true,
        defaultType: 'textfield',
		border:false,
        items: [{
	   		xtype:'datetimefield',
       		fieldLabel: '开始时间',
			format:'Y-m-d H:i:s',
       		editable:false,
       		name:'startTime',
       		allowBlank : false,
           	blankText: '时间不能为空'
       	},{
	   		xtype:'datetimefield',
       		fieldLabel: '结束时间',
			format:'Y-m-d H:i:s',
       		editable:false,
       		name:'endTime',
       		allowBlank : false,
           	blankText: '时间不能为空'
       	}],
	    buttonAlign:'center',
	    buttons: [{
	        text: '查询',
	        handler:function(){
	        	var startTime = dateRangePanel.getForm().getValues().startTime;
	        	var endTime = dateRangePanel.getForm().getValues().endTime;
	        	if(startTime == '' || endTime == ''){
	        		Ext.Msg.alert("提示","时间不能为空！");
            		return;
	        	}
	        	if(startTime > endTime){
            		Ext.Msg.alert("提示","开始时间不能大于结束时间！");
            		return;
            	}
            	Ext.Msg.wait('正在查询数据....', '提示');
				Ext.Ajax.request({
		       		url : 'mapposition/person-history-location.action',
					params : {id:'${id}',startTime:startTime,endTime:endTime},
		            success : function(xhr) {
		            	Ext.Msg.hide();
		            	dateRangeWin.close();
		        		var responseText = Ext.decode(xhr.responseText);
		            	locations = responseText.list;
		            	if(locations.length <= 0){
		            		Ext.Msg.alert("提示","无历史位置记录！");
		            		return;
		            	}
		            	
		            	  var points = [];
		            	for(var i=0;i<locations.length;i++){
		            		points.push(new BMap.Point(locations[i].location_x,locations[i].location_y));
		            	}
		            	pline = new BMap.Polyline(points); 
		            //	map.addOverlay(pline);  
                   		map.setCenter(new BMap.Point(locations[locations.length-1].location_x,locations[locations.length-1].location_y));
                   		
                   		
          	window.run = function (){
	  var j=locations.length;
		var myIcon = new BMap.Icon("./hr/img/Mario.png", new BMap.Size(32, 70), {    //小车图片
		//offset: new BMap.Size(0, -5),    //相当于CSS精灵
		imageOffset: new BMap.Size(0, 0)    //图片的偏移量。为了是图片底部中心对准坐标点。
	  });
	var myP1=new BMap.Point(locations[0].location_x,locations[0].location_y);
	var myP2=new BMap.Point(locations[j-1].location_x,locations[j-1].location_y);
	var driving = new BMap.DrivingRoute(map, {renderOptions:{map: map, autoViewport: false}});    //驾车实例
		driving.search(myP1, myP2);
		driving.setSearchCompleteCallback(function(){
			var points = driving.getResults().getPlan(0).getRoute(0).getPath();    //通过驾车实例，获得一系列点的数组
			var paths = points.length;    //获得有几个点
			var carMk = new BMap.Marker(points[0],{icon:myIcon});
			map.addOverlay(carMk);
			map.addOverlay(new BMap.Polyline(points));
			i=0;
			function resetMkPoint(i){
				carMk.setPosition(points[i]);
				if(i < paths){
					setTimeout(function(){
						i++;
						resetMkPoint(i);
					},100);
				}
			}
			setTimeout(function(){
				resetMkPoint(5);
			},100)

		});
	}
	setTimeout(function(){
		run();
	},1500);
	
		           	},
				    failure: function(xhr){
				    	Ext.Msg.hide();
				    	Ext.Msg.alert("提示","获取数据失败，稍后请重试！");
				    }
		        });
	        }
	    },{
	        text: '取消',
	        handler:function(){
	        	dateRangeWin.close();
	        }
	    }]
	});
	var dateRangeWin = new Ext.Window({
		title: '历史轨迹查询',
        width: 300,
        height:140, 
        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        resizable:false,
        items: [dateRangePanel]
	});
    return dateRangeWin;
})();



