<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var imagebox = new Ext.BoxComponent({
		autoEl: {
			//margin-bottom:10px
			style: 'width:200px;height:150px;margin:0px auto;border:1px solid #ccc; text-align:center;padding-top:5px;',
			tag: 'div',
			id: 'imageshow',
			html: '<img src="${path}" style="width:330px;height:145px;"/>'
		}
	});
	 var editForm = new Ext.FormPanel({
	 	header : false,
        fileUpload: true,              //标志此表单数据中包含文件数据
        autoHeight: true,
        border:false,
//        bodyStyle: 'padding: 10px 10px 0 10px;',
        labelWidth: 65,
        buttonAlign:'center',
        defaults: {
            anchor: '90%',
            allowBlank: false
        },
        items: [{
        	xtype:'hidden',
        	id:'id',
        	name:'id',
        	value:'${id}'
         },{
            xtype: 'textfield',
            fieldLabel: '文件名称',
            name:'name',
            value:'${name}'
         },{
            xtype: 'displayfield',
            fieldLabel: '上传时间',
            value:'${uploadDate}'
         },{
		    xtype: 'radiogroup',
		    name:'isUse',
		    fieldLabel: "使用",
		    items : [{
		    	boxLabel: '是',
		    	name: 'use',
		    	inputValue:'1',  //映射的值
		    	checked : '${isUsed}'=='1'
		    },{
		    	boxLabel: '否',
		    	name: 'use',
		    	inputValue:'0',
		    	checked : '${isUsed}'=='0'
		    }]
		},imagebox],
        buttons: [{
            text: '保存',
            handler: function(){
                if(editForm.getForm().isValid()){
                     editForm.getForm().submit({
                         url: 'mapseeting/mapmanage/save-modify-map.action',
                         waitMsg: '正在上传...',
                         success: function(editForm, o){
                            mapInfoWinEdit.close();
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
            handler:function(){
            	mapInfoWinEdit.close();
            }
        }]
    });
    return editForm;
})();