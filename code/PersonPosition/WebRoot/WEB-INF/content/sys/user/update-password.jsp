<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var saveFormUrl = 'sys/user/update-password.action';    
      var action =  new Ext.Action({
      	text: '保存',
    	iconCls :'icon-save',
        handler: function(){
            var good=simple.getForm().isValid() ;
    	    var password=simple.getForm().findField("userpassword${uniqueId}").getValue();
    	    if(password==''||good==false)return;

         
        var successMesage = function(xhr){
			    Ext.Msg.alert("提示","成功修改密码!");
			   };
			  var errorMesage = function(xhr){
	             Ext.Msg.alert("提示","对不起,修改密码出错!");
			   };
			   
			     Ext.Ajax.request({
				   url: 'sys/user/update-password.action',
				   success: successMesage,
				   failure: errorMesage,
				   params: { password: password }
				});
		
        }
    });
    
    
    
    
    var simple = new Ext.FormPanel({
        labelWidth: 75, 
        url:saveFormUrl,
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        height:380,
        width: 350,
        defaults: {width: 230},
        defaultType: 'textfield',
        items: [{
                fieldLabel: '密码',
                name: 'password',
                maxLength:20,
                id: 'userpassword${uniqueId}',//ID是全局唯一的，因为本系统可以打开很多个窗口，所以id必须跟上记录的ID，以便是全局唯一
                inputType: 'password',
                validator: function(value){
                	if(value != "" && value.length < 4){
                		return "密码至少4位";
                	}
                	return true;
                }
            },{
                fieldLabel: '确认密码',
                inputType: 'password',
                maxLength:20,
                submitValue: false,//确认密码不用提交
                vtype: 'password',
        		initialPassField: 'userpassword${uniqueId}' // id of the initial password field
            }],
            tbar: [action]
    });
    return simple;
})();