<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var warn_area_type_store = new Ext.data.JsonStore({ 
		url : "personmanage/types-data.action", 
		fields : ['id','name'], 
		root : "values", 
		baseParams : { 
			type:'WARN_AREA_TYPE'
		} 
	}); 
	warn_area_type_store.addListener("load", function(){ 
		if('${type}' != ''){
			warn_area_type_show.setValue('${type}');
		}else if(this.totalLength>0){
			warn_area_type_show.setValue(this.getAt(0).get('id'));
		}
	}); 
	warn_area_type_store.load();
	var warn_area_type_show = new Ext.form.ComboBox( { 
		fieldLabel : '类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型', 
		mode : 'local', 
		triggerAction : 'all', 
		selectOnFocus : true, 
		forceSelection : true, 
		editable : false, 
		valueField : 'id', 
		displayField : 'name', 
		hiddenName:'type',
		name : 'type',
		store : warn_area_type_store,
		listeners:{
		}
	});
	var warn_area_start_time = new Ext.form.TimeField({ 
     	fieldLabel:'有效开始时间',
     	name:'effectiveStartTime',
        value:'${effectiveStartTime}',
        allowBlank : false,
       	blankText: '有效开始时间不能为空',
//        maxValue:'',//最大时间  
//        maxText:'所选时间小于{0}',  
//            minValue:'10:00',                      //最小时间  
//            minText:'所选时间大于{0}',  
        increment:30,                          //时间间隔  
        format:'H:i:s',                    //时间格式  
        invalidText:'时间格式无效'
    });
    var warn_area_end_time = new Ext.form.TimeField({ 
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
	var warn_area_panel = new Ext.FormPanel({
		labelWidth: 50, 
      	defaults: {
      		anchor: '90%'
      	},
        frame:true,
        defaultType: 'textfield',
		border:false,
        items: [{
        	xtype:'hidden',
        	name:'communityId',
        	value:'${communityId}'
        },{
        	xtype:'hidden',
        	name:'id',
        	value:'${id}'
        },{
        	fieldLabel: '名&nbsp;&nbsp;&nbsp;&nbsp;称',
            name:'name',
            value:'${name}',
	    	allowBlank : false,
           	blankText: '名称不能为空',
           	maxLength:15
//	    },warn_area_type_show
//	    ,warn_area_start_time
//	    ,warn_area_end_time
//	    ,{
//	    	xtype: 'radiogroup',
//		    fieldLabel: "使&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;用",
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
		}]
//	    ,
//	    listeners:{
//			"afterrender":function(){
//				warn_area_panel.getForm().reset();
//          	}
//		}
	});
	var warn_area_win = new Ext.Window({
		title: '路线信息',
        width: 250,
        height:120, 
//        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        resizable:false,
        items: [warn_area_panel],
	    buttons: [{
            text: '保存',
            handler:function(){
				warn_area_panel.getForm().submit({
                    waitMsg: '正在保存数据...',
                    url:'warnmanage/warnarea/save-warn-area.action',
            		success:function(form,action){
						if(action.result.success){
							warn_area_win.close();
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
            	warn_area_win.close();
            }
        }]
	});
    return warn_area_win;
})();



