<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
    var simple = new Ext.FormPanel({
        labelWidth: 45, 
        url:'sys/action/item/save-form.action',
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        defaults: {width: 260},
        defaultType: 'textfield',
        items: [{
                fieldLabel: '名称',
                name: 'name',
                maxLength:200
              //  minLength:4
              //  allowBlank:false
            }, 
             {
                name: 'said',
                hidden: true,
                value:'${said}',
                hideLabel:true,
                allowBlank:false
            },
            {
            	fieldLabel: '路径',
                name: 'path',
	            maxLength:300,
               // minLength:4,
                allowBlank:false,
	            flex: 1  // Take up all *remaining* vertical space
            }]
    });
    return simple;
})();