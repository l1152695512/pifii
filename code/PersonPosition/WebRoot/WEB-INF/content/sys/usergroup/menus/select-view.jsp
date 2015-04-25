<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 20;
	var gridDataUrl = "sys/usergroup/menus/select-data.action";
	var rowId = 'systemMenuId';
	var storeFields = [
             'parentMenuText','menuText','systemMenuId'
        ];       
    var tabTitle = "选择要添加的菜单";
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
            header: "菜单名称",
            dataIndex: 'menuText',
            align: 'center',
            width: 120,
            sortable: true
        },
    	{
            header: "上级菜单",
            dataIndex: 'parentMenuText',
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
				   url: 'sys/usergroup/menus/select-save.action',
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
		html:'菜单名称：'
	});
	var t2=new Ext.form.TextField({				
						xtype : 'textfield',
						name : 'name',	
						width:80				
	});
	var q = new Ext.Action({
		text : '查询',
		iconCls : 'icon-find',
		handler : function() {
			 var name=t2.getValue();
			 store.setBaseParam("name",name);
			 store.reload();
		}
	});
	var r = new Ext.Action({
		text : '清空',
		iconCls : 'icon-redo',
		handler : function() {
			 t2.setValue(""); 
		}
	});
    store.setDefaultSort('systemMenuId', 'desc');
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
		tbar: [delAction,'-',t1,t2,'&nbsp;&nbsp;',q,'&nbsp;&nbsp;',r],
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,//pageSize 表示每页有几条记录,
            sugid:'${sugid}',
            store: store,
            displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
        }),
        sm: sm
    });
    return grid;
})();