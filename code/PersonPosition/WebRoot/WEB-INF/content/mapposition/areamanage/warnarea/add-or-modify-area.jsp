<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	
	var areaPanel = new Ext.FormPanel({
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
        	fieldLabel: '名&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;称',
            name:'name',
            value:'${name}',
	    	allowBlank : false,
           	blankText: '名称不能为空',
           	maxLength:15
	    },{
        	xtype:'textarea',
        	fieldLabel: '描&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述',
        	width:'95%',
            name:'description',
            value:'${description}'.replace(new RegExp('<br>', 'g'), '\n')
		}]
	});
	var areaWin = new Ext.Window({
		title: '区域信息',
        width: 300,
        height:180, 
//        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        resizable:false,
        items: [areaPanel],
	    buttons: [{
            text: '保存',
            handler:function(){
				areaPanel.getForm().submit({
                    waitMsg: '正在保存数据...',
                    url:'mapposition/areamanage/warnarea/save-area.action',
            		success:function(form,action){
						if(action.result.success){
							areaWin.close();
							if("" != '${id}'){
								reloadTreeNode(false);
							}else{
								reloadTreeNode(true);
							}
						}else{
							Ext.Msg.alert("温馨提醒","保存数据失败，稍后请重试！");
						}
            		},
            		failure: function(form,action){
            			Ext.Msg.hide();
					    Ext.Msg.alert("温馨提醒","保存数据失败，稍后请重试！");
					}
            	});
            }
        },{
            text: '取消',
            handler:function(){
            	areaWin.close();
            }
        }]
	});
    return areaWin;
})();



