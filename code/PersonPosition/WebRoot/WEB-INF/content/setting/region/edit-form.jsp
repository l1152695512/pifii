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
                disabled:true,
                allowBlank:false
            },
            {
			  name: 'rid',
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
    
        simple.on("render",function(cmp){
    	var config = {
    		url:'setting/region/edit-form-data.action', 
    		params:{rid:'${editId}'}
    		};
    	Ext.apply(config,commonLoadFormConfig);
         var task = new Ext.util.DelayedTask(function(){
		   cmp.getForm().load(config);
		});
		task.delay(25);
    });
	
    return simple;
})();