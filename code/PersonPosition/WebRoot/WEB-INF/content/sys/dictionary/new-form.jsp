<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
    var simple = new Ext.FormPanel({
        labelWidth: 70, 
        url:'sys/dictionary/save-form.action',
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        width: 350,
        defaults: {width: 250},
        defaultType: 'textfield',
        items: [{
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
    return simple;
})();



