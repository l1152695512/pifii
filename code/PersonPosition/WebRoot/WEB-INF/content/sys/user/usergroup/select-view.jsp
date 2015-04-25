<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 20;
	var gridDataUrl = "sys/user/usergroup/select-data.action";
	var rowId = 'sugid';
	var storeFields = [
            'name',
            {name: 'createTime', type: 'date', dateFormat: 'Y-m-d'}
        ];       
    var tabTitle = "选择要添加的用户组";
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
            header: "用户组名称",
            dataIndex: 'name',
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
				   url: 'sys/user/usergroup/select-save.action',
				   success: reloadFn,
				   failure: reloadFn,
				   params: { ids: params,userId:'${userId}'}
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
        store.baseParams = {userId:'${userId}'}; }
    );
    store.setDefaultSort('sugid', 'desc');
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
		tbar: [delAction],
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