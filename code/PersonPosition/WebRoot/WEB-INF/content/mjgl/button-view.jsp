<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){

	var button = Ext.extend(Ext.Button, {
	    //text  : '测试',
	    tooltip : '测试',
	    url : '',
	    tabId : '',
	    width : 35,
	    height : 35,
	    iconCls : '',
	    handler : function() {   
        	addLazyLoadWorkSpaceTab(this.tabId,this.tooltip,this.url,{layout:'anchor'},null,null,null); 
        }, 
	    initComponent : function() {
	        button.superclass.initComponent.apply(this,arguments);
	    }
	});
	
	var getInstance = function(cfg) {
	    return new button(cfg);
	}

	var data =Ext.decode('${jsonString }');
	
	var simple = new Ext.Panel({
 		autoHeight:true,
 		autoWidth:true,
 		border : false,
 		align : 'center',
 		bodyStyle:'padding:5px;',
        items:[{
            layout : 'column',
            id:'item1',
            border : false,
            items : []
        }]
    })
   
   	for (var index = 0; index < data.length; index++) {
        var param = data[index];
        var value = param.text == null ? '' : param.text.trim();
        try{
            //无效数据或不显示的参数不作处理.
            if (Ext.isEmpty(param) || param.action == '')
                continue;

            var cfg = {
            	tabId : param.id,
                tooltip : value,
                iconCls : param.iconCls,
                url : param.action
            };

            var paramComp = getInstance(cfg);
          
            //add to pane
            Ext.getCmp('item1').add({
	             columnWidth : 0.25, 
	             layout : 'form',
	             bodyStyle:'padding:5px 5px 3px 5px;',
	             align : 'center',
	             border : false,
	             items : [paramComp]
        	});
            Ext.getCmp('item1').doLayout();
            
        }catch(e){
            Ext.Msg.alert('错误!','处理参数'+param.COLUMN_NAME+',发生错误!'+e);
        }
   }

   return simple;
   
})();