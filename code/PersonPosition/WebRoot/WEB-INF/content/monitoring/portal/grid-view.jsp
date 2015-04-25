<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 20;
	var gridDataUrl = "sys/usergroup/grid-data.action";
	var rowId = 'sugid';
	var storeFields = [
            'name','remards',
            {name: 'createTime', type: 'date', dateFormat: 'Y-m-d'}
        ];   	
    var rowaction=	new Ext.ux.grid.RowActions({
		 header:'操作',
		 width:150,
		 autoWidth:false,
		 keepSelection:true,
		 actions:[{
			iconCls:'movies2-icon',
			tooltip:'视频直播',
			text:'视频直播',
			callback:function(grid, record, action, row, col){
				if((row?row%2?"奇数":"偶数":"0") == "偶数"){
					alert("时间还未到，请稍后！");
				}else{
					Ext.Ajax.request({
		    			url:'monitoring/video-view.action',
		    			params: {sugid:record.id,name:record.data.name},
		    			success:function(xhr){
		    				var win1 = eval(xhr.responseText);
		    				win1.show();
		    			}
		    		});
		    	}
			}
		},{
			iconCls:'back-icon',
			tooltip:'视频回放',
			text:'视频回放',
			callback:function(grid, record, action, row, col){
				if((row?row%2?"奇数":"偶数":"0") == "偶数"){
					alert("时间还未到，请稍后！");
				}else{
					Ext.Ajax.request({
		    			url:'monitoring/back-view.action',
		    			params: {sugid:record.id,name:record.data.name},
		    			success:function(xhr){
		    				var win2 = eval(xhr.responseText);
		    				win2.show();
		    			}
		    		});
		    	}
			}
		},{
			iconCls:'icon-basic-menus',
			tooltip:'查看明细',
			text:'查看明细',
			callback:function(grid, record, action, row, col){
				Ext.Ajax.request({
	    			url:'monitoring/video-view.action',
	    			params: {sugid:record.id,name:record.data.name},
	    			success:function(xhr){
	    				var win3 = eval(xhr.responseText);
	    				win3.show();
	    			}
	    		});
			}
		}]
		
	});
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
            header: "项目名称",
            dataIndex: 'name',
            align: 'center',
            width: 120,
            renderer: function(value,metadata,record,rowIndex){
            	return "名称"+(rowIndex+1);
    		}
        },{
            header: "开始时间",
            dataIndex: 'createTime',
            align: 'center',
            width: 70,
            renderer:renderDateISOLong,
            sortable: true
        },{
         	header: "结束时间",
            dataIndex: 'createTime',
            align: 'center',
            width: 70,
            renderer:renderDateISOLong,
            sortable: true
        },{
            header: "负责人",
            dataIndex: 'remards', 
            align: 'center',
            renderer: function(value,metadata,record,rowIndex){
    			return "负责人"+(rowIndex+1);
    		}
        },{
            header: "客户名称",
            dataIndex: 'remards',
            align: 'center',
            width: 70,
            renderer: function(value,metadata,record,rowIndex){
    			return "客户名称"+(rowIndex+1);
    		}
        },
        rowaction];


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
    
    store.setDefaultSort('sugid', 'desc');
	
	
    var grid = new Ext.grid.GridPanel({
        store: store,
        region : 'center',
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        columns:gridColumns,
        viewConfig: {
            forceFit:true
        },
        plugins:[rowaction],
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,//pageSize 表示每页有几条记录,
            store: store,
            displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
        })
    });


    var leftPanel = new Ext.FormPanel({
		title : '搜索',
		region : 'west',
       	margins:'3',
       	labelWidth: 65, 
	    displayfieldWidth: 25, 
        url:'sys/usergroup/save-form.action',
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        width: 550,
        defaults: {width: 130},
        defaultType: 'textfield',
        buttonAlign :'right',
        items: [{
                fieldLabel: '项目名称',
                name: 'pname',
                maxLength:200,
                minLength:4
            },{
            	fieldLabel: '开始时间',
                name: 'bname',
                maxLength:200,
                minLength:4
            },{
            	fieldLabel: '结束时间',
                name: 'ename',
                maxLength:200,
                minLength:4
            },{
            	fieldLabel: '客户名称',
                name: 'cname',
                maxLength:200,
                minLength:4
         }],
         buttons:[{
         	text:'搜索'
         }]  
    });
    //############################################主panel##################################################
    var mainPanel = new Ext.Panel({
    	header : false,
		title : '招标门户',
		border : false,
		layout : 'border',
		items : [leftPanel,grid],
		listeners:{
            "bodyresize":function(){
				leftPanel.setWidth(mainPanel.getWidth()*0.2);
			},
			"beforerender":function(panel){
				//showAlarm(panel);
          	}
		}
	});
	
    return mainPanel;
})();