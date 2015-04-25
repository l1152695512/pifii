<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var dispatch_type_store = new Ext.data.JsonStore({ 
		url : "personmanage/types-data.action", 
		fields : ['id','name'], 
		root : "values", 
		baseParams : { 
			type:'DISPATCH_TYPE'
		} 
	}); 
	dispatch_type_store.addListener("load", function(){ 
		if(this.totalLength>0){
			dispatch_type_show.setValue(this.getAt(0).get('id'));
		}
	}); 
	dispatch_type_store.load();
	var dispatch_type_show = new Ext.form.ComboBox( { 
		fieldLabel : '类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型', 
		mode : 'local', 
		triggerAction : 'all', 
		selectOnFocus : true, 
		forceSelection : true, 
		editable : false, 
		valueField : 'id', 
		displayField : 'name', 
		hiddenName:'type',
		name : 'type',
		store : dispatch_type_store,
		listeners:{
			"select":function(combo,record,index){
				randerLocation();
          	}
		}
	});
	var location_field = new Ext.form.TextField({
		fieldLabel: '',
    	width:'59%',
    	name:'location',
    	readOnly : true
	});
	var dispathTaskPanel = new Ext.FormPanel({
		labelWidth: 60, 
        frame:true,
        //defaultType: 'textfield',
		border:false,
        items: [{
        	xtype:'hidden',
        	name:'personId',
        	value:'${personId}'
        },{
        	xtype:'displayfield',
        	fieldLabel: '人&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;员',
            value:'${name}'
	    },
	    dispatch_type_show,{
        	xtype:'hidden',
        	name:'helpPersonId'
        },
	    {
//        	xtype: 'fieldcontainer',
            fieldLabel: '目标位置',
//            combineErrors: true,
            id:'dispatch_task_location_${personId}',
            msgTarget : 'side',
            layout: 'hbox',
            border:false,
            items: [location_field,{
                xtype : 'button',
                text : '选择位置',
                listeners : {
                	'click' : function(){
                		var type = dispatch_type_show.getValue();
                		Ext.Msg.wait('加载地图中....', '提示');
						Ext.Ajax.request({
							url:'location/choice-location.action',
							params:{mapPath:'${mapPath}',type:type,personId:'${personId}'},
							success:function(xhr){
								Ext.Msg.hide(); 
								var choice_location_win = eval(xhr.responseText);
								choice_location_win.show();
							},
							failure:function(xhr){
								Ext.Msg.hide(); 
								Ext.Msg.alert('提示','加载地图失败，稍后请重试！');
							}
						});
                	}
                }
            }]
        },{
        	xtype:'textarea',
        	fieldLabel: '任务描述',
        	width:'85%',
            name:'description',
            allowBlank : false,
            blankText: '任务描述不能为空'
		}]
	    ,
	    listeners:{
			"afterrender":function(){
				//randerLocation();
//				dispathTaskPanel.getForm().reset();
          	}
		}
	});
	function randerLocation(){
		var type = dispatch_type_show.getValue();
		if(type == '8'){//如果类型为发送信息，则隐藏位置框
			dispathTaskPanel.getForm().setValues({location:''});
			$("#dispatch_task_location_${personId}").parent().parent().hide();
		}else{
			$("#dispatch_task_location_${personId}").parent().parent().show();
		}
		if(type != '5'){//如果类型不为响应求助，则清空求助人员id
			dispathTaskPanel.getForm().setValues({helpPersonId:''});
		}
	}
	var dispathTaskWin = new Ext.Window({
		title: '调度任务',
        width: 300,
        height:230, 
        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        resizable:false,
        items: [dispathTaskPanel],
	    buttons: [{
            text: '保存',
            handler:function(){
            	if(dispatch_type_show.getValue() != "8" && location_field.getValue() == ""){
            		Ext.Msg.alert("温馨提醒","请选择目标位置！");
            		return;
            	}
				dispathTaskPanel.getForm().submit({
                    waitMsg: '正在保存数据...',
                    url:'location/save-dispatch-task.action',
            		success:function(form,action){
						if(action.result.success){
							dispathTaskWin.close();
							Ext.Msg.alert("温馨提醒","调度任务已发出！");
						}else{
							Ext.Msg.alert("温馨提醒","调度任务失败，稍后请重试！");
						}
            		},
            		failure: function(form,action){
            			Ext.Msg.hide();
					    Ext.Msg.alert("温馨提醒","调度任务失败，稍后请重试！");
					}
            	});
            }
        },{
            text: '取消',
            handler:function(){
            	dispathTaskWin.close();
            }
        }]
	});
    return dispathTaskWin;
})();



