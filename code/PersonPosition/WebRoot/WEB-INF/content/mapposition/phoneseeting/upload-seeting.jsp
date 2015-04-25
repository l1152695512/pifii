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
        format:'H:i',                    //时间格式  
        invalidText:'时间格式无效'
    });
    var endTime = new Ext.form.TimeField({ 
     	fieldLabel:'结束时间',
     	name:'endTime',
        value:'${endTime}',
        allowBlank : false,
       	blankText: '结束时间不能为空',
       	increment:30,                          //时间间隔  
        format:'H:i',                    //时间格式  
        invalidText:'时间格式无效'
    });
    var saveAction= new Ext.Action({
        text: '添加',
        iconCls :'icon-save',
        handler: function(){
        	if(startTime.validate() && endTime.validate()){
        		if(startTime.getValue() >= endTime.getValue()){
        			Ext.Msg.alert("提示","开始时间必须小于结束时间！");
        			return;
        		}
        	}
			uploadSeetingPanel.getForm().submit({
                waitMsg: '正在保存数据...',
                url:'mapposition/phoneseeting/save-upload-seeting.action',
        		success:function(form,action){
					if(action.result.success){
						addLazyLoadWorkSpaceTab('upload_seeting','定位上传设置','mapposition/phoneseeting/upload-seeting.action',{layout:'anchor'},null,null,null); 
//						routePointTimeWin.close();
//						getRoutePoints();
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
    });
    var cancelAction= new Ext.Action({
        text: '取消',
        iconCls :'icon-cancel',
        handler: function(){
        	addLazyLoadWorkSpaceTab('upload_seeting','定位上传设置','mapposition/phoneseeting/upload-seeting.action',{layout:'anchor'},null,null,null); 
        }
    });
    
	var uploadSeetingPanel = new Ext.FormPanel({
//		region : 'center',
		labelWidth: 65, 
		width:200,
      	defaults: {
//      		anchor: '200'
      	},
//        frame:true,
        autoHeight:true,
        bodyStyle:'padding:20px 10px 0px;',
//        defaultType: 'textfield',
		border:false,
		tbar: [saveAction,'-',cancelAction],
        items: [
        {
        	xtype:'hidden',
        	name:'id',
        	value:'${id}'
        },{
//        	xtype: 'fieldcontainer',
            combineErrors: true,
            msgTarget : 'side',
            layout: 'hbox',
            fieldLabel: '定位周期',
            combineErrors: false,
            border:false,
            width:160,
            defaults: {
                hideLabel: true
            },
            items: [
               {
                   name : 'period',
                   value:'${period}',
                   xtype: 'numberfield',
//                   width: '84%',
                   allowBlank: false
               },{
                   xtype: 'displayfield',
                   value: '（秒）'
               }]
	    },{
	    	name : 'sensibility',
	    	fieldLabel: '灵&nbsp;敏&nbsp;&nbsp;度',
           	value:'${sensibility}',
          	xtype: 'numberfield',
          	width: 165,
           	allowBlank: false
	    },startTime,endTime,{
	    	xtype: 'radiogroup',
		    fieldLabel: "开机启动",
		    width:200,
		    items : [{
		    	boxLabel: '是',
		    	name: 'startWidthSystem',
		    	inputValue:'1',  //映射的值
		    	checked : true
		    },{
		    	boxLabel: '否',
		    	name: 'startWidthSystem',
		    	inputValue:'0',
		    	checked : '${startWidthSystem}'=='0'
		    }]
		},{
	    	xtype: 'radiogroup',
		    fieldLabel: "基站定位",
		    width:200,
		    items : [{
		    	boxLabel: '是',
		    	name: 'isBasestation',
		    	inputValue:'1',  //映射的值
		    	checked : true
		    },{
		    	boxLabel: '否',
		    	name: 'isBasestation',
		    	inputValue:'0',
		    	checked : '${isBasestation}'=='0'
		    }]
		},{
	    	xtype: 'radiogroup',
		    fieldLabel: "只&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;读",
		    width:200,
		    items : [{
		    	boxLabel: '是',
		    	name: 'isReadOnly',
		    	inputValue:'1',  //映射的值
		    	checked : true
		    },{
		    	boxLabel: '否',
		    	name: 'isReadOnly',
		    	inputValue:'0',
		    	checked : '${isReadOnly}'=='0'
		    }]
		},{
           xtype: 'displayfield',
           fieldLabel: '<span style="color:red;">提&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;示</span>',
           value: '<span style="color:red;">该设置仅当用户手机重启后才生效</span>'
        }]
	});
    return uploadSeetingPanel;
})();



