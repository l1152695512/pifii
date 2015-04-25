<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var saveFormUrl = 'meeting/book/save-edit.action';
	
	var start = new Ext.data.SimpleStore({
		fields: ['id', 'name'],
		data : [['1','8:30:00'],['2','9:00:00'],['3','9:30:00'],['4','10:00:00'],['5','10:30:00'],['6','11:00:00'],['7','11:30:00'],['8','12:00:00'],['9','12:30:00'],['10','13:00:00'],['11','13:30:00'],['12','14:00:00'],['13','14:30:00'],['14','15:00:00'],['15','15:30:00'],['16','16:00:00'],['17','16:30:00'],['18','17:00:00']],
		autoLoad:true
	});
	
	var end = new Ext.data.SimpleStore({
		fields: ['id', 'name'],
		data : [['1','9:00:00'],['2','9:30:00'],['3','10:00:00'],['4','10:30:00'],['5','11:00:00'],['6','11:30:00'],['7','12:00:00'],['8','12:30:00'],['9','13:00:00'],['10','13:30:00'],['11','14:00:00'],['12','14:30:00'],['13','15:00:00'],['14','15:30:00'],['15','16:00:00'],['16','16:30:00'],['17','17:00:00'],['18','17:30:00']],
		autoLoad:true
	});
	
	var simple = new Ext.FormPanel({
	    url:saveFormUrl,
	    frame:true,
	    autoHeight:true,
 		autoWidth:true,
	    bodyStyle:'padding:5px 5px 0',
	    defaultType: 'textfield',
	    defaults: {width: 200},
	    labelAlign: 'center',
	    items : [{
	    	xtype:'hidden',
            name: 'M_ID',
        	hidden: true,
        	hideLabel:true,
        	value:'${editId}'
	    },{
	    	fieldLabel: '会议名称',
	    	readOnly:true,
	    	value:'${mName}',
           	id:'mname'
	    },{
	   		xtype:'datefield',
       		fieldLabel: '预约日期',
			format:'Y-m-d',
			minValue : new Date(),
			value:Ext.util.Format.date('${bDate}', 'Y-m-d'),
       		id:'book',
       		editable:false,
       		name:'M_DATE'
       	},{
	    	xtype: 'combo',
			fieldLabel: '开始时间',
			allowBlank : false,
           	blankText: '请选择开始时间',
			mode: 'local',
			editable: false, 
			store: start,
			triggerAction : 'all',
			displayField:'name',
			valueField:'name',
			autoSelect : true,
			emptyText : '请选择',
			value:'${start}',
			id:'start',
			name:'START'
        },{
       		xtype: 'combo',
			fieldLabel: '结束时间',
			allowBlank : false,
           	blankText: '请选择结束时间',
			mode: 'local',
			editable: false, 
			store: end,
			triggerAction : 'all',
			displayField:'name',
			valueField:'name',
			autoSelect : true,
			emptyText : '请选择',
			value:'${end}',
			id:'end',
			name:'END'
       	}],
       	buttonAlign: 'center',
       	buttons: [{
            text: '保存',
            handler:function(){
            	var vv=Ext.util.Format.date(Ext.getCmp('book').getValue(), 'Y-m-d');
				var ss=Ext.getCmp('start').getValue();
				var ee=Ext.getCmp('end').getValue();
				var i=ss.split(':');
				var j=i[0]+i[1]+i[2];
				var t=ss;
				if(i[0].length==1){
					t='0'+t;
				}
				var m=ee.split(':');
				var l=m[0]+m[1]+m[2];
				var a=Ext.util.Format.date(new Date(), 'Y-m-d H:i:s');
				var b=vv+' '+t;
				if(parseInt(j)>parseInt(l)){
					Ext.Msg.alert('提示','开始时间不能大于结束时间');
				}else{
					if(parseNum(a)>parseNum(b)){
						Ext.Msg.alert('提示','开始时间不能小于当前时间');
					}else{
						editForm.getForm().submit({
		            		success:function(form,action){
		            			store.load(store.lastOptions);
		            			editWin.close();
		            		},
		            		failure: function(form,action){
							    Ext.Msg.alert('提示','修改失败！');
							}
		            	});
					}
				}
            }
        },{
            text: '取消',
            handler:function(){
            	editWin.close();
            }
        }]
    });    
    
    return simple;
})();



