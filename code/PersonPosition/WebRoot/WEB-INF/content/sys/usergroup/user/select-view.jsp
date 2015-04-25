<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 20;
	var gridDataUrl = "sys/usergroup/user/select-data.action";
	var rowId = 'userId';
	var storeFields = [
            'accountName','userName',
            {name: 'createTime', type: 'date', dateFormat: 'Y-m-d'}
        ];       
    var tabTitle = "选择要添加的用户";
    var sm = new Ext.grid.CheckboxSelectionModel({checkOnly:true});//复选框
    var gridColumns = [sm,
    	new Ext.grid.RowNumberer({
    		header:'序号',
    		align:'center',
    		width: 40,
    		renderer: function(value,metadata,record,rowIndex){
    			return store.lastOptions.params.start + 1 + rowIndex;
    		}
    	}),
    	{
            header: "帐号",
            dataIndex: 'accountName',
            align: 'center',
            width: 120,
            sortable: true
        },
        {
            header: "用户名",
            dataIndex: 'userName',
            align: 'center',
            width: 120,
            sortable: true
        }];
   
    var delAction =  new Ext.Action({
        text: '添加',
        iconCls :'icon-add',
        handler: function(){
            var selected = grid.getSelectionModel().getSelections();
            var params = []
            Ext.each(selected,function(item){
            	params.push(item.id);
            });
            var reloadFn = function(xhr){
            	try{
            		var result = Ext.decode(xhr.responseText);
            		if(result.msg)Ext.Msg.alert("警告",result.msg);
            	}catch(e){
            		alert(e.description);
            	}
			    store.load(store.lastOptions);        
			   };
			if(params.length == 0){
		   		Ext.Msg.alert("提示","请选择要添加的数据！");
		   		return;
		    }else{
	           Ext.Ajax.request({
				   url: 'sys/usergroup/user/select-save.action',
				   success: reloadFn,
				   failure: reloadFn,
				   params: { ids: params,sugid:'${sugid}'}
				});
			}
        }
    });
    // 数据存储
    var store = new Ext.data.JsonStore({
        root: 'list',
        autoLoad:{params:{start:0, limit:pageLimit}},
        totalProperty: 'totalRecord',
        idProperty: rowId,
        remoteSort: true,
        fields: storeFields,
        url: gridDataUrl
    });
     store.on('beforeload',function(store){ 
        store.baseParams = {sugid:'${sugid}'}; }
    );
    var  t1=new Ext.form.Label({
		html:'帐号：'
	});
	var t2=new Ext.form.TextField({				
						xtype : 'textfield',
						name : 'accountName',	
						width:80				
	});
	  var  t3=new Ext.form.Label({
		html:'姓名：'
	});
	var t4=new Ext.form.TextField({				
						xtype : 'textfield',
						name : 'userName',	
						width:80				
	});

	var q = new Ext.Action({
		text : '查询',
		iconCls : 'icon-find',
		handler : function() {
			 var accountName=t2.getValue();
			 store.setBaseParam("accountName",accountName);
			  var userName=t4.getValue();
			 store.setBaseParam("userName",userName);
			 store.reload();
		}
	});
	var r = new Ext.Action({
		text : '清空',
		iconCls : 'icon-redo',
		handler : function() {
			 t2.setValue(""); 
			 t4.setValue(""); 
		}
	});
    store.setDefaultSort('userId', 'desc');
    var grid = new Ext.grid.GridPanel({
        title:tabTitle,
        store: store,
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        columns:gridColumns,
        viewConfig: {
            forceFit:true
        },
		//tbar: [delAction],
		tbar: [delAction,'-',t1,t2,'&nbsp;&nbsp;',t3,t4,'&nbsp;&nbsp;',q,'&nbsp;&nbsp;',r],
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,//pageSize 表示每页有几条记录,
            store: store,
            displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
        }),
        sm: sm
    });
    return grid;
})();