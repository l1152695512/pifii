<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var route_point_store = new Ext.data.JsonStore({ 
		url:'patrolmanage/routeplan/get-route-points-list.action',
		fields : ['id','name'], 
		root : "values", 
		baseParams : { 
			id:'${id}',
			routeId:'${routeId}'
		} 
	}); 
	route_point_store.addListener("load", function(){ 
		route_point_store.insert(0, new Ext.data.Record({ 
			'id' : "",
			'name' : "-"
		}));
		previous_route_point_show.setValue('${previousPoint}');
		next_route_point_show.setValue('${nextPoint}');
	}); 
	route_point_store.load();
	var previous_route_point_show = new Ext.form.ComboBox( { 
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
		store : route_point_store 
	});
	var next_route_point_show = new Ext.form.ComboBox( { 
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
		store : route_point_store 
	});
	var routePlanPointStartTime = new Ext.form.TimeField({ 
     	fieldLabel:'有效开始时间',
     	name:'effectiveStartTime',
        value:'${effectiveStartTime}',
        allowBlank : false,
       	blankText: '有效开始时间不能为空',
//            width:150,  
//        maxValue:'',//最大时间  
//        maxText:'所选时间小于{0}',  
//            minValue:'10:00',                      //最小时间  
//            minText:'所选时间大于{0}',  
//            maxHeight:70,  
        increment:30,                          //时间间隔  
        format:'H:i:s',                    //时间格式  
        invalidText:'时间格式无效'
    });
    var routePlanPointEndTime = new Ext.form.TimeField({ 
     	fieldLabel:'有效结束时间',
     	name:'effectiveEndTime',
        value:'${effectiveEndTime}',
        allowBlank : false,
       	blankText: '有效结束时间不能为空',
//       	minValue:'',                      //最小时间  
//        minText:'所选时间大于{0}',
       	increment:30,                          //时间间隔  
        format:'H:i:s',                    //时间格式  
        invalidText:'时间格式无效'
//        ,
//        listeners:{
//        	"change":function(panel,newValue,oldValue){
//		        alert("end");
//          	}
//        }
    })
	var routePointInfoPanel = new Ext.FormPanel({
		url:'patrolmanage/routeplan/save-route-point.action',
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
        	name:'routeId',
        	value:'${routeId}'
         },{
        	fieldLabel: '名称',
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
	     },routePlanPointStartTime,
	     routePlanPointEndTime,
         previous_route_point_show,
         next_route_point_show,
//         {
//        	fieldLabel: '前一个点',
//            name:'previousPoint',
//            value:'${previousPoint}'
//	     },{
//        	fieldLabel: '后一个点',
//            name:'nextPoint',
//            value:'${nextPoint}'
//	     },
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
////					routePointInfoPanel.getForm().reset();
////					console.debug($('#locationX'));
////					Ext.util.Format.date(new Date(),'Y-m-d')
//				}
          	}
		},
	    buttons: [{
            text: '保存',
            handler:function(){
            	if(routePlanPointStartTime.validate() && routePlanPointEndTime.validate()){
            		if(!routePointInfoPanel.getForm().isValid()){
            			Ext.Msg.alert("提示","有些字段没有按要求填写，请重新填写！");
            			return;
            		}
            		if(routePlanPointStartTime.getValue() >= routePlanPointEndTime.getValue()){
            			Ext.Msg.alert("提示","有效开始时间必须小于有效结束时间！");
            			return;
            		}
            	}
            	var fieldValues = routePointInfoPanel.getForm().getValues();
        		Ext.Msg.wait('正在检查数据....', '提示');
				Ext.Ajax.request({
					url:'patrolmanage/routeplan/check-previous-and-next-point.action',
					params : {id:fieldValues.id,routeId:fieldValues.routeId,previousPoint:fieldValues.previousPoint,nextPoint:fieldValues.nextPoint},
					success : function(form,action){
						Ext.Msg.hide(); 
						var obj = Ext.decode(form.responseText);
						if(obj.success){
							routePointInfoPanel.getForm().submit({
								waitMsg: '正在保存数据...',
			            		success:function(form,action){
			            			routePointWin.close();
			            			loadRoutePoints('${routeId}');
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
            	routePointWin.close();
            }
        }]
	});
	var routePointWin = new Ext.Window({
		title: '路线点信息',
        width: 350,
        height:300, 
//        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        resizable:false,
        items: [routePointInfoPanel]
	});
    return routePointWin;
})();



