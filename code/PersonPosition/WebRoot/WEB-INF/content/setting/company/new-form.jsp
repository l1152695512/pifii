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
							checked :true,
							name : 'category'
						}, {
							xtype : 'radio',
							inputValue : 2,
							boxLabel : '二级单位',
							name : 'category'
						}]
				   },new Ext.form.ComboBox({
							fieldLabel : '上级部门',
							emptyText : '...上级部门...',	
							name : 'deptNo',
							hiddenName : 'deptNo',
							autoSelect : true,
							editable : false,
							typeAhead : true,
							mode : 'local',
							displayField : 'name',
							valueField : 'dno',
							//allowBlank : false,
							triggerAction : 'all',
							store : new Ext.data.JsonStore({
								autoLoad : true,
								fields : [ {
									name : 'dno'
								},
								 {
									name : 'name'
								}],
							    	url : 'commons/combo-dept.action',
								root : 'rows'
							})
                    }),
            ]
    });
	
    return simple;
})();