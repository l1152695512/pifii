<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var warn_area_point_store = new Ext.data.JsonStore({ 
		url:'warnmanage/warnarea/get-warn-area-points-list.action',
		fields : ['id','name'], 
		root : "values", 
		baseParams : { 
			id:'${id}',
			warnAreaId:'${warnAreaId}'
		} 
	}); 
	warn_area_point_store.addListener("load", function(){ 
		warn_area_point_store.insert(0, new Ext.data.Record({ 
			'id' : "",
			'name' : "-"
		}));
		previous_warn_area_point_show.setValue('${previousPoint}');
		next_warn_area_point_show.setValue('${nextPoint}');
	}); 
	warn_area_point_store.load();
	var previous_warn_area_point_show = new Ext.form.ComboBox( { 
		fieldLabel : '前一个点', 
		mode : 'local', 
		triggerAction : 'all', 
//		selectOnFocus : true, 
//		forceSelection : true, 
		editable : false, 
		valueField : 'id', 
		displayField : 'name', 
		hiddenName:'previousPoint',
		name : 'previousPoint',
		store : warn_area_point_store 
	});
	var next_warn_area_point_show = new Ext.form.ComboBox( { 
		fieldLabel : '后一个点', 
		mode : 'local', 
		triggerAction : 'all', 
//		selectOnFocus : true, 
//		forceSelection : true, 
		editable : false, 
		valueField : 'id', 
		displayField : 'name', 
		hiddenName:'nextPoint',
		name : 'nextPoint',
		store : warn_area_point_store 
	});
	var warn_area_point_info_panel = new Ext.FormPanel({
		url:'warnmanage/warnarea/save-warn-area-point.action',
//		title : '磁盘信息',
		labelWidth: 90, 
      	defaults: {
      		anchor: '90%'
      	},
        frame:true,
        defaultType: 'textfield',
//		border:false,
        items: [{
        	xtype:'hidden',
        	name:'id',
        	value:'${id}'
         },{
        	xtype:'hidden',
        	name:'warnAreaId',
        	value:'${warnAreaId}'
         },{
        	fieldLabel: '名称',
            name:'name',
            value:'${name}',
	    	allowBlank : false,
           	blankText: '名称不能为空',
           	maxLength:15
	     },previous_warn_area_point_show,
         next_warn_area_point_show,
	     {
        	fieldLabel: 'X坐标',
            name:'locationX',
            value:'${locationX}',
            readOnly:true,
	    	allowBlank : false,
           	blankText: 'X坐标不能为空'
	     },{
        	fieldLabel: 'Y坐标',
        	readOnly:true,
            name:'locationY',
            value:'${locationY}',
	    	allowBlank : false,
           	blankText: 'Y坐标不能为空'
	     }],
	    buttonAlign:'center',
	    listeners:{
			"afterrender":function(){
//				console.debug(Ext.getCmp('routePlanPointEndTime'));
//				if('' != '${id}'){
//					Ext.getCmp('routePlanPointStartTime').setValue('${effectiveStartTime}');
//					Ext.getCmp('routePlanPointEndTime').setValue('${effectiveEndTime}');
////					warn_area_point_info_panel.getForm().reset();
////					console.debug($('#locationX'));
////					Ext.util.Format.date(new Date(),'Y-m-d')
//				}
          	}
		},
	    buttons: [{
            text: '保存',
            handler:function(){
            	var fieldValues = warn_area_point_info_panel.getForm().getValues();
        		Ext.Msg.wait('正在检查数据....', '提示');
				Ext.Ajax.request({
					url:'warnmanage/warnarea/check-previous-and-next-point.action',
					params : {id:fieldValues.id,warnAreaId:fieldValues.warnAreaId,previousPoint:fieldValues.previousPoint,nextPoint:fieldValues.nextPoint},
					success : function(form,action){
						Ext.Msg.hide(); 
						var obj = Ext.decode(form.responseText);
						if(obj.success){
							warn_area_point_info_panel.getForm().submit({
								waitMsg: '正在保存数据...',
			            		success:function(form,action){
			            			warn_area_point_win.close();
			            			load_warn_area_points('${warnAreaId}');
			            		},
			            		failure: function(form,action){
			            			Ext.Msg.hide();
			            			Ext.Msg.alert("温馨提醒","保存失败，稍后请重试！");
								}
			            	});
						}else{
							Ext.Msg.alert("温馨提醒",obj.msg);
						}
					},
					failure:function(response,action){
						Ext.Msg.hide(); 
						var obj=Ext.decode(response.responseText);    
						Ext.Msg.alert("温馨提醒",obj.result);
					}
				});
            }
        },{
            text: '取消',
            handler:function(){
            	warn_area_point_win.close();
            }
        }]
	});
	var warn_area_point_win = new Ext.Window({
		title: '路线点信息',
        width: 350,
        height:220, 
//        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        resizable:false,
        items: [warn_area_point_info_panel]
	});
    return warn_area_point_win;
})();



