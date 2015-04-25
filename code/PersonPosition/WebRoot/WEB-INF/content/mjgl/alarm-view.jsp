<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){

	var pageLimit = 10;
	var gridDataUrl = "alarm/grid-data.action";
	var rowId = 'alarmId';
	var storeFields = ['alarmId','shortName','alarmType','createTime'];   	
    var tabTitle = "告警管理";
    
    var gridColumns = [
    	new Ext.grid.RowNumberer({
    		header:'序号',
    		align:'center',
    		width: 40,
    		renderer: function(value,metadata,record,rowIndex){
    			return store.lastOptions.params.start + 1 + rowIndex;
    		}
    	}),
    	{
            header: "告警基站/机楼",
            dataIndex: 'shortName',
            align: 'center',
            sortable: true
        },{
            header: "告警类型",
            dataIndex: 'alarmType', 
            align: 'center',
            sortable: true,
            renderer:function(val){
				if(val =='1' ){
					return '火警';
				}
				else{
					return '水警';
				}
			}
        },
        {
            header: "告警开始时间",
            dataIndex: 'createTime', 
            align: 'center',
            sortable: true
        }];
    
    var store = new Ext.data.JsonStore({
       root: 'list',
       autoLoad:{params:{start:0, limit:pageLimit}},
       totalProperty: 'totalRecord',
       idProperty: rowId,
       remoteSort: true,
       fields: storeFields,
       url: gridDataUrl
    });
           
    store.setDefaultSort('createTime', 'desc');
    
    var grid = new Ext.grid.GridPanel({
        store: store,
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        autoHeight:true,
 		autoWidth:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        columns:gridColumns,
        viewConfig: {
            forceFit:true
        },
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,//pageSize 表示每页有几条记录,
            store: store,
            displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
        })
    });

    return grid;
})();