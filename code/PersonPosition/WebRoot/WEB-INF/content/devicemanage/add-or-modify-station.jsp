<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var devicePanel = new Ext.FormPanel({
		url:'devicemanage/save-device.action',
//		title : '磁盘信息',
		labelWidth: 70, 
      	defaults: {
      		anchor: '90%'
      	},
//        frame:true,
        defaultType: 'textfield',
		border:false,
        items: [{
        	xtype:'hidden',
        	name:'id',
        	value:'${id}'
         },{
        	xtype:'hidden',
        	name:'communityId',
        	value:'${communityId}'
         },{
        	fieldLabel: '设备ID',
        	name:'cardMark',
        	allowBlank : false,
        	blankText: '设备ID不能为空',
        	value:'${cardMark}',
        	disabled:'${cardMark}'!=''
         },{
        	fieldLabel: '名称',
            name:'name',
            value:'${name}',
	    	allowBlank : false,
           	blankText: '名称不能为空',
           	maxLength:15
	     },{
        	fieldLabel: 'X坐标',
            value:'${locationX}',
	    	allowBlank : false,
           	blankText: '坐标不能为空',
        	disabled:true
	     },{
        	xtype:'hidden',
        	name:'locationX',
        	value:'${locationX}'
         },{
        	fieldLabel: 'Y坐标',
            value:'${locationY}',
	    	allowBlank : false,
           	blankText: '坐标不能为空',
        	disabled:true
	     },{
        	xtype:'hidden',
        	name:'locationY',
        	value:'${locationY}'
         },{
        	fieldLabel: '描述',
            name:'description',
            value:'${description}'
//	     },{
//            xtype : 'datetimefield',
//            id : 'addDate',
//            name : 'addDate',
//            format : 'Y-m-d H:i:s', 
//            fieldLabel : '添加时间',
//            border:false,
//       		editable:false,
//       		readOnly:true,
//       		allowBlank : false,
//            value:'${addDate}'
        }],
	    buttonAlign:'center',
	    listeners:{
			"afterrender":function(){
				if('' == '${id}'){
//					Ext.getCmp('addDate').setValue(new Date());
//					devicePanel.getForm().reset();
//					console.debug($('#locationX'));
//					Ext.util.Format.date(new Date(),'Y-m-d')
				}else{
//					Ext.getCmp('location_x').setReadOnly(true);
//					Ext.getCmp('location_y').setReadOnly(true);
//					Ext.getCmp('card_mark').setReadOnly(true);
				}
          	}
		},
	    buttons: [{
            text: '保存',
            handler:function(){
            	Ext.Msg.wait('保存数据中....', '提示');
				devicePanel.getForm().submit({
            		success:function(form,action){
            			Ext.Msg.hide();
            			deviceInfoWin.close();
            			loadDevices();
            		},
            		failure: function(form,action){
            			Ext.Msg.hide();
            			if(action.result.msg == 'cardMarkError'){
            				Ext.Msg.alert('提示','该设备ID已存在，请填写其他设备ID！');
            			}else{
						    Ext.Msg.alert('提示','操作失败！');
            			}
					}
            	});
            }
        },{
            text: '取消',
            handler:function(){
            	deviceInfoWin.close();
            }
        }]
	});
    return devicePanel;
})();



