<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 15;
	var task_storeFields = ['type','name','help_name','description','task_date'];
	var task_store = new Ext.data.JsonStore({
        root: 'list',
        autoLoad:{params:{start:0, limit:pageLimit}},
        totalProperty: 'totalRecord',
        idProperty: 'id',
        remoteSort: true,
        fields: task_storeFields,
        url: 'datastatistics/dispatch/task-data.action'
    });
	task_store.on('beforeload',function(store){ 
       store.baseParams = leftPanel.getForm().getValues(); 
    });
    task_store.setDefaultSort('task_date', 'desc');
	var task_gridColumns = [
    	{
            header: "调度类型",
            dataIndex: 'type',
            sortable:true,
            align: 'center',
            width: 50
        },{
            header: "调度人员",
            dataIndex: 'name',
            sortable:true,
            align: 'center',
            width: 70
        },{
            header: "救助人员",
            dataIndex: 'help_name',
            align: 'center',
            sortable:true,
            width: 90 
        },{
            header: "调度时间",
            dataIndex: 'task_date',
            sortable:true,
            align: 'center',
            width: 70
        },{
            header: "描述",
            dataIndex: 'description',
            align: 'center',
            width: 70,
            renderer: function(value,metadata,record,rowIndex){
    			return '<div ext:qtitle="" ext:qtip="' + value + '">'+ value.replace(new RegExp('<br>', 'g'), '\n') +'</div>';  
    		}
        }];
    
    
	var task_grid = new Ext.grid.GridPanel({
        store: task_store,
        region : 'center',
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        columns:task_gridColumns,
        viewConfig: {
            forceFit:true
        },
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,//pageSize 表示每页有几条记录,
            store: task_store,
            displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
        })
    });
    
    var dispatch_type_store = new Ext.data.JsonStore({ 
		url : "personmanage/types-data.action", 
		fields : ['id','name'], 
		root : "values", 
		baseParams : { 
			type:'DISPATCH_TYPE'
		} 
	}); 
	dispatch_type_store.addListener("load", function(){ 
		dispatch_type_store.insert(0, new Ext.data.Record({ 
			'id' : "",
			'name' : '全部'
		})); 
		if(this.totalLength>0){
			dispatch_type_show.setValue(this.getAt(0).get('id'));
		}
	}); 
	dispatch_type_store.load();
    var dispatch_type_show = new Ext.form.ComboBox( { 
		fieldLabel : '类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型', 
		mode : 'local', 
		triggerAction : 'all', 
		selectOnFocus : true, 
		forceSelection : true, 
		editable : false, 
		valueField : 'id', 
		displayField : 'name', 
		hiddenName:'type',
		name : 'type',
		store : dispatch_type_store
	});
	
	var leftPanel = new Ext.FormPanel({
		title : '搜索',
		region : 'west',
       	margins:'3',
       	labelWidth: 80, 
	    displayfieldWidth: 25, 
//        frame:true,
        bodyStyle:'padding:5px 5px 10px',
        width: 250,
        defaults: {anchor: '100%'},
        defaultType: 'textfield',
        buttonAlign :'center',
        autoHeight:true,
      	border:false,
//        split:true,
        collapsible:true,
        collapseMode:'mini',
        items: [dispatch_type_show,{
            fieldLabel: '调度人员名称',
            name:'name',
            maxLength:15
        },{
            fieldLabel: '救助人员名称',
            name:'helpPersonName',
            maxLength:15
        },{
            xtype : 'datetimefield',
//            id:'start',
            name : 'startDate',
            format : 'Y-m-d H:i:s', 
            fieldLabel : '调度开始时间',
            border:false,
       		editable:false
        },{
            xtype : 'datetimefield',
//            id:'end',
            name : 'endDate',
            format : 'Y-m-d H:i:s', 
            fieldLabel : '调度结束时间',
            border:false,
       		editable:false
        }],
        buttons:[{
         	text:'查询',
         	handler: function () {
			 	task_store.reload();
		    }
        },{
         	text:'重置',
         	handler: function () {
		      	leftPanel.getForm().reset();
		    }
        }]  
    });
	var mainPanel = new Ext.Panel({
    	header : false,
		title : '调度数据查看',
		border : false,
		layout : 'border',
		items : [leftPanel,task_grid],
		listeners:{
            "bodyresize":function(){
				//leftPanel.setWidth(mainPanel.getWidth()*0.2);
			},
			"beforerender":function(panel){
				//showAlarm(panel);
          	}
		}
	});
    return mainPanel;
})();