<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var saveFormUrl = 'setting/company/save-form.action';
    var simple = new Ext.FormPanel({
        labelWidth: 75, 
        url:saveFormUrl,
        frame:true,
        bodyStyle:'padding:5px 5px 0',

        defaults: {width: 200},
        defaultType: 'textfield',
        items: [        
           {
			  name: 'cid',
			  xtype:'hidden'
			 },	 
        	{
                fieldLabel: '名称',
                name: 'cname',
                maxLength:20,
                minLength:4,
                allowBlank:false
            },		         
           	new Ext.form.ComboBox({
							fieldLabel : '所属域市',
							emptyText : '...所属域市...',	
							name : 'rid',
							hiddenName : 'rid',
							//anchor : '95%',
							autoSelect : true,
							editable : false,
							typeAhead : true,
							mode : 'local',
							displayField : 'name',
							valueField : 'rid',
							allowBlank : false,
							triggerAction : 'all',
							store : new Ext.data.JsonStore({
								autoLoad : true,
								fields : [ {
									name : 'rid'
								},
								 {
									name : 'name'
								}],
							    url : 'commons/combo-region.action?onlyCity=true',
								root : 'rows'
							})
                    }),
       	             {
						fieldLabel : '类型',
						allowBlank : false,
						xtype : 'compositefield',
						items : [{
							xtype : 'radio',
							inputValue : 1,
							boxLabel : '分公司',
							width : 60,
							name : 'category'
						}, {
							xtype : 'radio',
							inputValue : 2,
							boxLabel : '二级单位',
							name : 'category'
						}]
				   }
            ]
    });
    simple.on("render",function(cmp){
    	var config = {
    		url:'setting/company/edit-form-data.action', 
    		params:{cid:'${editId}'}
    		};
    	Ext.apply(config,commonLoadFormConfig);
         var task = new Ext.util.DelayedTask(function(){
		   cmp.getForm().load(config);
		});
		task.delay(25);
    });
    
   
    return simple;
})();