<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 20;
	var event_storeFields = ['person_name','event_type','name','time_name','add_date'];
	var event_store = new Ext.data.JsonStore({
        root: 'list',
        autoLoad:{params:{start:0, limit:pageLimit}},
        totalProperty: 'totalRecord',
        idProperty: 'id',
        remoteSort: true,
        fields: event_storeFields,
        url: 'mapposition/datastatistics/event/event-data.action'
    });
	event_store.on('beforeload',function(store){ 
       store.baseParams = leftPanel.getForm().getValues(); 
    });
    event_store.setDefaultSort('add_date', 'desc');
	var event_gridColumns = [
    	new Ext.grid.RowNumberer({
			header : '序号',
			align : 'center',
			width : 40,
			renderer : function(value, metadata, record, rowIndex) {
				return event_store.lastOptions.params.start + 1 + rowIndex;
			}
		}),{
            header: "人员",
            dataIndex: 'person_name',
            sortable:true,
            align: 'center',
            width: 50
        },{
            header: "路线/区域名称",
            dataIndex: 'name',
            sortable:true,
            align: 'center',
            width: 70
        },{
            header: "时间段名称",
            dataIndex: 'time_name',
            sortable:false,
            align: 'center',
            width: 70
        },{
            header: "事件类型",
            dataIndex: 'event_type',
            align: 'center',
            width: 70,
            renderer: function(value,metadata,record,rowIndex){
            	if('1' == value){
            		return '巡更提醒';
            	}else if('2' == value){
            		return '路线违规';
            	}else if('3' == value){
            		return '进入非法区域';
            	}else if('4' == value){
            		return '离开指定区域';
            	}
    		}
        },{
            header: "时间",
            dataIndex: 'add_date',
            sortable:true,
            align: 'center',
            width: 70
        }];
    
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
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,//pageSize 表示每页有几条记录,
            store: event_store,
            displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
        })
    });
    
    
    
	var leftPanel = new Ext.FormPanel({
		title : '搜索',
		region : 'west',
       	margins:'3',
       	labelWidth: 60, 
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
        items: [{
	    	xtype: 'radiogroup',
		    fieldLabel: "类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型",
		    items : [{
		    	boxLabel: '路线',
		    	name: 'type',
		    	inputValue:'1',
		    	checked : true
		    },{
		    	boxLabel: '区域',
		    	name: 'type',
		    	inputValue:'2'
		    }],
		    listeners : {
		    	"change":function(thisRadio,checked){
		    		event_store.reload();
				}
		    }
		},{
            fieldLabel: '人员名称',
            name:'personName',
            maxLength:15
        },{
            xtype : 'datetimefield',
            name : 'startDate',
            format : 'Y-m-d H:i:s', 
            fieldLabel : '开始时间',
            border:false,
       		editable:false
        },{
            xtype : 'datetimefield',
            name : 'endDate',
            format : 'Y-m-d H:i:s', 
            fieldLabel : '结束时间',
            border:false,
       		editable:false
        }],
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