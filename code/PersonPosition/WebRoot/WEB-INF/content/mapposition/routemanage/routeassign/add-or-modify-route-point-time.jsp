<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var startTime = new Ext.form.TimeField({ 
     	fieldLabel:'开始时间',
     	name:'startTime',
        value:'${startTime}',
        allowBlank : false,
       	blankText: '开始时间不能为空',
        increment:30,                          //时间间隔  
        format:'H:i:s',                    //时间格式  
        invalidText:'时间格式无效'
    });
    var endTime = new Ext.form.TimeField({ 
     	fieldLabel:'结束时间',
     	name:'endTime',
        value:'${endTime}',
        allowBlank : false,
       	blankText: '结束时间不能为空',
       	increment:30,                          //时间间隔  
        format:'H:i:s',                    //时间格式  
        invalidText:'时间格式无效'
    })
	var routePointTimePanel = new Ext.FormPanel({
		labelWidth: 65, 
      	defaults: {
      		anchor: '90%'
      	},
        frame:true,
        defaultType: 'textfield',
		border:false,
        items: [{
        	xtype:'hidden',
        	name:'id',
        	value:'${id}'
        },{
        	xtype:'hidden',
        	name:'routePointId',
        	value:'${routePointId}'
        },{
        	xtype:'hidden',
        	name:'routeTimeId',
        	value:'${routeTimeId}'
        },startTime,endTime]
	});
	var routePointTimeWin = new Ext.Window({
		title: '路线点时间信息',
        width: 300,
        height:150, 
//        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        resizable:false,
        items: [routePointTimePanel],
	    buttons: [{
            text: '保存',
            handler:function(){
            	if(startTime.validate() && endTime.validate()){
            		if(startTime.getValue() >= endTime.getValue()){
            			Ext.Msg.alert("提示","开始时间必须小于结束时间！");
            			return;
            		}
            	}
				routePointTimePanel.getForm().submit({
                    waitMsg: '正在保存数据...',
                    url:'mapposition/routemanage/routeassign/save-route-point-time.action',
            		success:function(form,action){
						if(action.result.success){
							routePointTimeWin.close();
							getRoutePoints(false);
						}else{
							Ext.Msg.alert("提示","保存数据失败，稍后请重试！");
						}
            		},
            		failure: function(form,action){
            			Ext.Msg.hide();
					    Ext.Msg.alert("提示","保存数据失败，稍后请重试！");
					}
            	});
            }
        },{
            text: '取消',
            handler:function(){
            	routePointTimeWin.close();
            }
        }]
	});
    return routePointTimeWin;
})();



