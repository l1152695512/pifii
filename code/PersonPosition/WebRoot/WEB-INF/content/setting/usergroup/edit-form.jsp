<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
  var simple = new Ext.FormPanel({
        labelWidth: 50, 
        url:'setting/usergroup/save-form.action',
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
            },
      	   new Ext.form.ComboBox({
				fieldLabel : '分公司',
				emptyText : '...分公司或二级单位...',	
				name : 'cid',
				hiddenName : 'cid',
				//anchor : '90%',
				autoSelect : true,
				editable : false,
				typeAhead : true,
				mode : 'local',
				displayField : 'name',
				valueField : 'cid',
				triggerAction : 'all',
				store : new Ext.data.JsonStore({
					autoLoad : true,
					fields : [ {
						name : 'cid'
					},
					 {
						name : 'name'
					}],
				    url : 'commons/combo-company.action',
					root : 'rows'
				})
               }),
            
            {
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
    		url:'setting/usergroup/edit-form-data.action', 
    		params:{sugid:'${editId}'}
    		};
   	 Ext.apply(config,commonLoadFormConfig);
        var task = new Ext.util.DelayedTask(function(){
	   cmp.getForm().load(config);
	});
	task.delay(500);
    });
    return simple;
})();