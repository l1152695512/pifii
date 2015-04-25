<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
  var simple = new Ext.FormPanel({
        labelWidth: 50, 
        url:'sys/usergroup/save-form.action',
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        width: 350,
        defaults: {width: 250},
        defaultType: 'textfield',
        items: [{
                fieldLabel: '名称',
                name: 'name',
                maxLength:200,
                minLength:4,
                allowBlank:false
            },
             {
                name: 'sugid',
                hidden: true,
                hideLabel:true,
                allowBlank:false
            },{
            	fieldLabel: '描述',
                name: 'remards',
	            xtype: 'textarea',
	            height:120,
	            maxLength:500,
	            flex: 1  // Take up all *remaining* vertical space
            }]    
    });
    simple.on("render",function(cmp){
    	var config = {
    		url:'sys/usergroup/edit-form-data.action', 
    		params:{sugid:'${editId}'}
    		};
    	Ext.apply(config,commonLoadFormConfig);
    	cmp.getForm().load(config);
    });
    return simple;
})();