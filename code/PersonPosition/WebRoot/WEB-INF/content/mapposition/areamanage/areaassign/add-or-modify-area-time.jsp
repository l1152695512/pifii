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
		if('${areaType}' != ''){
			warn_area_type_show.setValue('${areaType}');
		}else if(this.totalLength>0){
			warn_area_type_show.setValue(this.getAt(0).get('id'));
		}
	}); 
	warn_area_type_store.load();
	var warn_area_type_show = new Ext.form.ComboBox( { 
		fieldLabel : '类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型', 
		mode : 'local', 
		triggerAction : 'all', 
		selectOnFocus : true, 
		forceSelection : true, 
		editable : false, 
		valueField : 'id', 
		displayField : 'name', 
		hiddenName:'areaType',
		name : 'areaType',
		store : warn_area_type_store,
		listeners:{
		}
	});
	var startTime = new Ext.form.TimeField({ 
     	fieldLabel:'开始时间',
     	name:'startTime',
        value:'${startTime}',
        allowBlank : false,
       	blankText: '开始时间不能为空',
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
    var endTime = new Ext.form.TimeField({ 
     	fieldLabel:'结束时间',
     	name:'endTime',
        value:'${endTime}',
        allowBlank : false,
       	blankText: '结束时间不能为空',
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
	var areaTimePanel = new Ext.FormPanel({
		labelWidth: 65, 
      	defaults: {
      		anchor: '90%'
      	},
        frame:true,
        defaultType: 'textfield',
		border:false,
        items: [{
        	xtype:'hidden',
        	name:'areaId',
        	value:'${areaId}'
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
		},warn_area_type_show,startTime,endTime,{
        	fieldLabel: '描&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述',
            name:'name',
            value:'${name}',
           	maxLength:15
	    }]
//	    ,
//	    listeners:{
//			"afterrender":function(){
//				routePanel.getForm().reset();
//          	}
//		}
	});
	var areaTimeWin = new Ext.Window({
		title: '警告区域信息',
        width: 300,
        height:220, 
//        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        resizable:false,
        items: [areaTimePanel],
	    buttons: [{
            text: '保存',
            handler:function(){
            	if(startTime.validate() && endTime.validate()){
            		if(startTime.getValue() >= endTime.getValue()){
            			Ext.Msg.alert("提示","开始时间必须小于结束时间！");
            			return;
            		}
            	}
				areaTimePanel.getForm().submit({
                    waitMsg: '正在保存数据...',
                    url:'mapposition/areamanage/areaassign/save-area-time.action',
            		success:function(form,action){
						if(action.result.success){
							areaTimeWin.close();
							area_time_store.reload();
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
            	areaTimeWin.close();
            }
        }]
	});
    return areaTimeWin;
})();



