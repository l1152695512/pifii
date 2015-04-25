<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var saveFormUrl = 'setting/usergroup/user/save-form.action';
    var simple = new Ext.FormPanel({
        labelWidth: 75, 
        url:saveFormUrl,
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        height:380,
       // width: 500,
       // defaults: {width: 350},
        width: 350,
        defaults: {width: 230},
        defaultType: 'textfield',
        items: [{
                name: 'userId',
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
                maxLength:20,
                id: 'userpassword${editId}',//ID是全局唯一的，因为本系统可以打开很多个窗口，所以id必须跟上记录的ID，以便是全局唯一
                inputType: 'password',
                validator: function(value){
                	if(value != "" && value.length < 4){
                		return "密码至少4位";
                	}
                	return true;
                }
            },{
                fieldLabel: '确认密码',
                id: 'userpwd_cfm${editId}',
                inputType: 'password',
                maxLength:20,
                submitValue: false,//确认密码不用提交
                vtype: 'password',
        		initialPassField: 'userpassword${editId}' // id of the initial password field
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
    simple.on("render",function(cmp){
    	var config = {
    		url:'setting/usergroup/user/edit-form-data.action', 
    		params:{userId:'${editId}'}
    		};
    	Ext.apply(config,commonLoadFormConfig);
    	cmp.getForm().load(config);
    });
    


    return simple;
})();