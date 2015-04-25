<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var saveFormUrl = 'setting/region/save-form.action';
    var simple = new Ext.FormPanel({
        labelWidth: 45, 
        url:saveFormUrl,
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        defaults: {width: 160},
        defaultType: 'textfield',
        items: [
          	{
                fieldLabel: '父区域',
                name: 'pname',
                value:'${pname}',
                disabled:true,
                allowBlank:false
            },
            {
			  name: 'pid',
			  value:'${pid}',
			  xtype:'hidden'
			 },	
        	  {
                fieldLabel: '名称',
                name: 'name',
                allowBlank:false
            },
            {
                fieldLabel: '编码',
                name: 'code',
                allowBlank:false
            }		          
    
            ]
    });
	
    return simple;
})();