<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
    var simple = new Ext.FormPanel({
        labelWidth: 75, 
        url:'setting/usergroup/user/save-form.action',
        frame:true,
        //title: '添加新用户',
        bodyStyle:'padding:5px 5px 0',
        width: 350,
        defaults: {width: 230},
        defaultType: 'textfield',

        items: [{
                name: 'sugid',
                value:'${sugid}',
                hidden: true,
                xtype:'hidden',
                hideLabel:true,
                allowBlank:false
            },
            {
                fieldLabel: '账户名',
                name: 'accountName',
                maxLength:20,
                minLength:4,
                allowBlank:false
            },{
                fieldLabel: '密码',
                name: 'password',
                id: 'password',
                maxLength:20,
                minLength:4,
                allowBlank:false,
                inputType: 'password'
            },{
                fieldLabel: '确认密码',
                id: 'pwd-cfm',
                inputType: 'password',
                maxLength:20,
                minLength:4,
                allowBlank:false,
                submitValue: false,//确认密码不用提交
                vtype: 'password',
        		initialPassField: 'password' // id of the initial password field
            }, {
                fieldLabel: '姓名',
                name: 'userName',
                maxLength:20
            }, {
                fieldLabel: '电话',
                name: 'telNum',
                maxLength:20
            }, {
                fieldLabel: '手机',
                name: 'cellPhone',
                maxLength:20
            }, {
                fieldLabel: 'Email',
                name: 'email',
                vtype:'email',
                //allowBlank:false,
                maxLength:50
            }, {
            	fieldLabel: '描述',
                name: 'description',
	            xtype: 'textarea',
	            maxLength:60,
	            flex: 1  // Take up all *remaining* vertical space
            }]

       
    });
    return simple;
})();