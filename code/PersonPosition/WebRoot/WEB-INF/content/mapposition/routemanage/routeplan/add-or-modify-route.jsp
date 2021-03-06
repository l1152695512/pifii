<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var routePanel = new Ext.FormPanel({
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
        	fieldLabel: '有效距离',
            name:'effectiveRange',
            value:'${effectiveRange}',
            allowBlank : false,
           	blankText: '有效距离不能为空'
//	    },{
//	    	xtype: 'radiogroup',
////		    name:'isUsed',
//		    fieldLabel: "使&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;用",
//		    items : [{
//		    	boxLabel: '是',
//		    	name: 'isUsed',
//		    	inputValue:'1',  //映射的值
//		    	checked : true
//		    },{
//		    	boxLabel: '否',
//		    	name: 'isUsed',
//		    	inputValue:'0',
//		    	checked : '${isUsed}'=='0'
//		    }]
		},{
	        	xtype:'textarea',
	        	fieldLabel: '描&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述',
	        	width:'95%',
	            name:'description',
	            value:'${description}'.replace(new RegExp('<br>', 'g'), '\n')
		}]
//	    ,
//	    listeners:{
//			"afterrender":function(){
//				routePanel.getForm().reset();
//          	}
//		}
	});
	var routeWin = new Ext.Window({
		title: '路线信息',
        width: 300,
        height:210, 
//        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        resizable:false,
        items: [routePanel],
	    buttons: [{
            text: '保存',
            handler:function(){
				routePanel.getForm().submit({
                    waitMsg: '正在保存数据...',
                    url:'mapposition/routemanage/routeplan/save-route.action',
            		success:function(form,action){
						if(action.result.success){
							routeWin.close();
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
            	routeWin.close();
            }
        }]
	});
    return routeWin;
})();



