<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var eventPanel = new Ext.FormPanel({
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
        	value:'${eventId}'
        },{
        	xtype:'displayfield',
        	fieldLabel: '类型',
            name:'eventType',
            value:'${eventType}'
	    },{
	    	xtype: 'radiogroup',
//		    name:'isUsed',
		    fieldLabel: "解决",
		    items : [{
		    	boxLabel: '是',
		    	name: 'isDeal',
		    	inputValue:'1',  //映射的值
		    	checked : true
		    },{
		    	boxLabel: '否',
		    	name: 'isDeal',
		    	inputValue:'0'
		    }]
		},{
        	xtype:'textarea',
        	fieldLabel: '描述',
        	width:'95%',
            name:'description'
		}]
	});
	var eventWin = new Ext.Window({
		title: '处理警告事件',
        width: 300,
        height:230, 
//        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        resizable:false,
        items: [eventPanel],
	    buttons: [{
            text: '稍后处理',
            handler:function(){
            	eventWin.close();
            }
        },{
            text: '处理',
            handler:function(){
				eventPanel.getForm().submit({
                    waitMsg: '正在提交处理信息...',
                    url:'location/deal-warn-event.action',
            		success:function(form,action){
						if(action.result.success){
							eventWin.close();
							removeWarnEvent('${personId}','${eventId}');
						}else{
							Ext.Msg.alert("温馨提醒","提交数据失败，稍后请重试！");
						}
            		},
            		failure: function(form,action){
            			Ext.Msg.hide();
					    Ext.Msg.alert("温馨提醒","提交数据失败，稍后请重试！");
					}
            	});
            }
        }]
	});
    return eventWin;
})();



