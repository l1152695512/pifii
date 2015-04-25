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
	var timePanel = new Ext.FormPanel({
		labelWidth: 65, 
      	defaults: {
      		anchor: '90%'
      	},
        frame:true,
        defaultType: 'textfield',
		border:false,
        items: [{
        	xtype:'hidden',
        	name:'routeId',
        	value:'${routeId}'
        },{
        	xtype:'hidden',
        	name:'id',
        	value:'${id}'
        },{
	    	xtype: 'radiogroup',
		    fieldLabel: "使&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;用",
		    items : [{
		    	boxLabel: '是',
		    	name: 'isUsed',
		    	inputValue:'1',  //映射的值
		    	checked : true
		    },{
		    	boxLabel: '否',
		    	name: 'isUsed',
		    	inputValue:'0',
		    	checked : '${isUsed}'=='0'
		    }]
		},{
        	fieldLabel: '名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称',
            name:'name',
            value:'${name}',
           	maxLength:30
	    },startTime,endTime]
	});
	var timeWin = new Ext.Window({
		title: '路线信息',
        width: 300,
        height:200, 
//        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        resizable:false,
        items: [timePanel],
	    buttons: [{
            text: '保存',
            handler:function(){
            	if(startTime.validate() && endTime.validate()){
            		if(startTime.getValue() >= endTime.getValue()){
            			Ext.Msg.alert("提示","开始时间必须小于结束时间！");
            			return;
            		}
            	}
				timePanel.getForm().submit({
                    waitMsg: '正在保存数据...',
                    url:'patrolmanage/routeassign/save-route-time.action',
            		success:function(form,action){
						if(action.result.success){
							timeWin.close();
							route_time_store.reload();
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
            	timeWin.close();
            }
        }]
	});
    return timeWin;
})();



