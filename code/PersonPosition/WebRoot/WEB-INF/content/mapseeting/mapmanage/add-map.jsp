<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	 var addForm = new Ext.FormPanel({
//	 	layout : "form",
	 	header : false,
        fileUpload: true,              //标志此表单数据中包含文件数据
//        enctype: 'multipart/form-data',
        autoHeight: true,
        border:false,
//        bodyStyle: 'padding: 10px 10px 0 10px;',
        labelWidth: 65,
        buttonAlign:'center',
        defaults: {
            anchor: '90%',
            allowBlank: false
//            msgTarget: 'side'
        },
        items: [{
            xtype: 'textfield',
            fieldLabel: '文件名称',
            name:'name'
        },{
		    xtype: 'radiogroup',
		    name:'isUse',
		    fieldLabel: "使用",
		    items : [{
		    	boxLabel: '是',
		    	name: 'use',
		    	inputValue:'1',  //映射的值
		    	checked : true
		    },{
		    	boxLabel: '否',
		    	name: 'use',
		    	inputValue:'0'
		    }]
		},{
            xtype: 'fileuploadfield',     //表单域类型
            emptyText: '请您选择文件',
            fieldLabel: '文件路径',
            name: 'upload',
            id: 'upload',
            buttonCfg: {
                text: '',
                iconCls: 'upload-icon'     //按钮图标
            }
        }],
        buttons: [{
            text: '保存',
            handler: function(){
                if(addForm.getForm().isValid()){
                     addForm.getForm().submit({
                         url: 'mapseeting/mapmanage/save-add-map.action',
                         waitMsg: '正在上传...',
                         success: function(addForm, o){
                            mapInfoWinAdd.close();
							map_store.reload();
                         },
	            		 failure: function(form,action){
						     Ext.Msg.alert('提示','操作失败！');
						 }
                     });
                }
            }
        },{
            text: '取消',
            handler: function(){
                mapInfoWinAdd.close();
            }
        }]
    });
    return addForm;
})();