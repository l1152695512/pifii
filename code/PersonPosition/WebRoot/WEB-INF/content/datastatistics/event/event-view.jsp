<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 20;
	var event_storeFields = ['community_name','person_name','user_name','key_value','event_time','is_deal','description'];
	var event_store = new Ext.data.JsonStore({
        root: 'list',
        autoLoad:{params:{start:0, limit:pageLimit}},
        totalProperty: 'totalRecord',
        idProperty: 'id',
        remoteSort: true,
        fields: event_storeFields,
        url: 'datastatistics/event/event-data.action'
    });
	event_store.on('beforeload',function(store){ 
       store.baseParams = leftPanel.getForm().getValues(); 
    });
    event_store.setDefaultSort('event_time', 'desc');
	var event_gridColumns = [
		new Ext.grid.RowNumberer({
			header : '序号',
			align : 'center',
			width : 40,
			renderer : function(value, metadata, record, rowIndex) {
				return event_store.lastOptions.params.start + 1 + rowIndex;
			}
		}),{
            header: "位置",
            dataIndex: 'community_name',
            sortable:false,
            align: 'center',
            width: 50
        },{
            header: "事件发起人",
            dataIndex: 'person_name',
            sortable:true,
            align: 'center',
            width: 50
        },{
            header: "处理人",
            dataIndex: 'user_name',
            sortable:true,
            align: 'center',
            width: 70
        },{
            header: "事件类型",
            dataIndex: 'key_value',
            align: 'center',
            sortable:true,
            width: 90 
        },{
            header: "处理结果",
            dataIndex: 'is_deal',
            align: 'center',
            width: 70,
            renderer: function(value,metadata,record,rowIndex){
            	if('' == value){
            		return '未处理';
            	}else if('1' == value){
            		return '已解决';
            	}else{
            		return '未解决';
            	}
    		}
        },{
            header: "事件描述",
            dataIndex: 'description',
            sortable:true,
            align: 'center',
            width: 70
        },{
            header: "发生时间",
            dataIndex: 'event_time',
            sortable:true,
            align: 'center',
            width: 70
        }];
    //导出报表
    var exportExcel = new Ext.Action(Ext.apply({
    	text: '导出详细报表数据',
    	iconCls :'export-excel',
    	handler: function(){
    		var params = leftPanel.getForm().getValues();
    		if(params.communityId != ''){
    			params.communityName = comunity_store.getById(params.communityId).get("name");
    		}
    		if(params.warnAreaId != ''){
    			params.areaName = warn_area_combo_store.getById(params.warnAreaId).get("name");
    		}
    		console.debug(params);
    		var paramsEncode = Ext.urlEncode(params);
    		console.debug(paramsEncode);
    		var url = "datastatistics/warn/export-excel.action?" + paramsEncode;
    		var xls = window.open(url); 
		 	xls.focus();
    	}
    }));
    //导出报表
    var exportExcel = new Ext.Action(Ext.apply({
    	text: '导出报表',
    	iconCls :'export-excel',
    	handler: function(){
    		var params = leftPanel.getForm().getValues();
    		if(params.eventType != ''){
    			params.eventTypeName = event_type_store.getById(params.eventType).get("name");
    		}
    		if(params.eventResult != ''){
    			params.eventResultName = event_result_show.lastSelectionText;
    		}
    		var paramsEncode = Ext.urlEncode(params);
    		console.debug(paramsEncode);
    		var url = "datastatistics/event/export-excel.action?" + paramsEncode;
    		var xls = window.open(url); 
		 	xls.focus();
    	}
    }));
	var event_grid = new Ext.grid.GridPanel({
        store: event_store,
        region : 'center',
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        columns:event_gridColumns,
        viewConfig: {
            forceFit:true
        },
        tbar : [exportExcel],
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,//pageSize 表示每页有几条记录,
            store: event_store,
            displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
        })
    });
    
    var event_type_store = new Ext.data.JsonStore({ 
		url : "personmanage/types-data.action", 
		fields : ['id','name'], 
		root : "values", 
		baseParams : { 
			type:'WARN_EVENT_TYPE'
		} 
	}); 
	event_type_store.addListener("load", function(){ 
		event_type_store.insert(0, new Ext.data.Record({ 
			'id' : "",
			'name' : '全部'
		})); 
		if(this.totalLength>0){
			event_type_show.setValue(this.getAt(0).get('id'));
		}
	}); 
	event_type_store.load();
    var event_type_show = new Ext.form.ComboBox( { 
		fieldLabel : '类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型', 
		mode : 'local', 
		triggerAction : 'all', 
		selectOnFocus : true, 
		forceSelection : true, 
		editable : false, 
		valueField : 'id', 
		displayField : 'name', 
		hiddenName:'eventType',
		name : 'eventType',
		store : event_type_store
	});
	var event_result_show=new Ext.form.ComboBox ({
		name: 'eventResult',
		fieldLabel : '处&nbsp;理&nbsp;结&nbsp;果', 
		hiddenName : 'eventResult',
		autoSelect:true,
		editable:false,
		width:100,
		mode : 'local',
		triggerAction : 'all',
		store : [['','全部'],
				['2','已解决'],
				['1','未解决'],
				['0','未处理']
			]
    });
    event_result_show.setValue("");
	var leftPanel = new Ext.FormPanel({
		title : '搜索',
		region : 'west',
       	margins:'3',
       	labelWidth: 70, 
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
        items: [event_type_show,{
            fieldLabel: '事件发起人',
            name:'personName',
            maxLength:15
        },{
            xtype : 'datetimefield',
//            id:'start',
            name : 'startDate',
            format : 'Y-m-d H:i:s', 
            fieldLabel : '开&nbsp;始&nbsp;时&nbsp;间',
            border:false,
       		editable:false,
       		allowBlank : false,
            value:Ext.Date.add(new Date(), Ext.Date.DAY, -5)
        },{
            xtype : 'datetimefield',
//            id:'end',
            name : 'endDate',
            format : 'Y-m-d H:i:s', 
            fieldLabel : '结&nbsp;束&nbsp;时&nbsp;间',
            border:false,
       		editable:false,
       		allowBlank : false,
            value:new Date()
        },event_result_show],
        buttons:[{
         	text:'查询',
         	handler: function () {
			 	event_store.reload();
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
		title : '事件处理查询',
		border : false,
		layout : 'border',
		items : [leftPanel,event_grid],
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