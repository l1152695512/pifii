<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pline = null;
	var drowLineTime = 200;
	var drowLineTask = new Ext.util.DelayedTask();
	var line_brush = new MBrush();  
    line_brush.arrow = true;  
	function drowLine(locations,index){
		if(index < locations.length){
			var addPoint = new MPoint(locations[index].location_x,locations[index].location_y);
			pline.appendPoint(addPoint,false);
            window.maplet.setCenter(addPoint);
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
           	blankText: '视频结束时间不能为空'
       	},{
	   		xtype:'datetimefield',
       		fieldLabel: '结束时间',
			format:'Y-m-d H:i:s',
       		editable:false,
       		name:'endTime',
       		allowBlank : false,
           	blankText: '视频结束时间不能为空'
       	}],
	    buttonAlign:'center',
	    buttons: [{
	        text: '查询',
	        handler:function(){
	        	var startTime = dateRangePanel.getForm().getValues().startTime;
	        	var endTime = dateRangePanel.getForm().getValues().endTime;
	        	
	        	if(startTime > endTime){
            		Ext.Msg.alert("提示","开始时间不能大于结束时间！");
            		return;
            	}
				Ext.Ajax.request({
		       		url : 'mapposition/person-history-location.action',
					params : {id:'${id}',startTime:startTime,endTime:endTime},
		            success : function(xhr) {
		            	dateRangeWin.close();
		        		var responseText = Ext.decode(xhr.responseText);
		            	locations = responseText.list;
		            	if(locations.length > 1){
		            		var endPoint = new MPoint(locations[1].location_x,locations[1].location_y);
		            		pline = new MPolyline([new MPoint(locations[0].location_x,locations[0].location_y),endPoint],line_brush);  
                   			window.maplet.addOverlay(pline);  
                   			window.maplet.setCenter(endPoint);
		            		drowLineTask.delay(drowLineTime,drowLine,this,[locations,2]);
		            	}
		           	},
				    failure: function(xhr){
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



