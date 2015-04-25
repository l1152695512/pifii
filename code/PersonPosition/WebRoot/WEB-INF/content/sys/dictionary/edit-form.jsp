<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var saveFormUrl = 'sys/dictionary/save-form.action';
    var simple = new Ext.FormPanel({
        labelWidth: 70, 
        url:saveFormUrl,
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        width: 350,
        defaults: {width: 250},
        defaultType: 'textfield',
        items: [{
                name: 'dicId',
                hidden: true,
                hideLabel:true,
                allowBlank:false
            },{
                fieldLabel: '数据名称',
                name: 'keyName',
                allowBlank:false,
                emptyText:'请输入数据名称'
            },{
                fieldLabel: '类型名称',
                name: 'typeName',
                allowBlank:false,
                emptyText:'请输入类型名称'
            },{
                fieldLabel: '数&nbsp;&nbsp;据&nbsp;值',
                name: 'keyValue'
            }, {
                fieldLabel: '备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注',
                name: 'remark',
                xtype: 'textarea',
                height:100,
	            maxLength:60
            }]
    });
    simple.on("render",function(cmp){
    	var config = {
    		url:'sys/dictionary/edit-form-data.action', 
    		params:{dicId:'${editId}'}
    		};
    	Ext.apply(config,commonLoadFormConfig);
    	cmp.getForm().load(config);
    });
    return simple;
})();