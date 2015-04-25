<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 15;
	var patrol_storeFields = ['person_name','community_name','route_name','date','position_date','is_done','person_id','route_id','map','start_time','end_time'];
	var patrol_store = new Ext.data.JsonStore({
        root: 'list',
        autoLoad:{params:{start:0, limit:pageLimit}},
        totalProperty: 'totalRecord',
        idProperty: 'id',
        remoteSort: true,
        fields: patrol_storeFields,
        url: 'datastatistics/patrol/patrol-data.action'
    });
    patrol_store.setDefaultSort('date', 'desc');
	patrol_store.on('beforeload',function(store){ 
       store.baseParams = leftPanel.getForm().getValues(); 
    });
    var rowAction=	new Ext.ux.grid.RowActions({
		 header:'操作',
		 width:100,
		 align: 'center',
		 autoWidth:false,
		 keepSelection:true,
		 actions:[{
			iconCls:'my-icon-search',
			tooltip:'查询详情',
			text: '查询详情',
			callback:function(grid, record, action, row, col){
				Ext.Msg.wait('加载巡更详情中....', '提示');
				Ext.Ajax.request({
					url:'datastatistics/patrol/patrol-info-view.action',
					params:{personName:record.data.person_name,routeName:record.data.route_name,
								date:record.data.date,personId:record.data.person_id,routeId:record.data.route_id,mapPath:record.data.map},
					success:function(xhr){
						Ext.Msg.hide(); 
						var patrol_info_win = eval(xhr.responseText);
						patrol_info_win.show();
					},
					failure:function(xhr){
						Ext.Msg.hide(); 
						Ext.Msg.alert('提示','加载数据失败，稍后请重试！');
					}
				});
			}
		}]
	});
	var patrol_gridColumns = [
    	{
            header: "时间",
            dataIndex: 'date',
            sortable:true,
            align: 'center',
            width: 70
        },{
            header: "姓名",
            dataIndex: 'person_name',
            sortable:true,
            align: 'center',
            width: 50
        },{
            header: "小区名称",
            dataIndex: 'community_name',
            sortable:true,
            align: 'center',
            width: 70
        },{
            header: "路线名称",
            dataIndex: 'route_name',
            align: 'center',
            sortable:true,
            width: 90 
        },{
            header: "开始时间",
            dataIndex: 'start_time',
            align: 'center',
            sortable:false,
            width: 90 
        },{
            header: "结束时间",
            dataIndex: 'end_time',
            align: 'center',
            sortable:false,
            width: 90 
        },{
            header: "结果",
            dataIndex: 'is_done',
            sortable:true,
            align: 'center',
            width: 70,
            renderer: function(value,metadata,record,rowIndex){
            	if(value == '1'){
            		return '完成';
            	}else{
            		if(record.data.position_date.length > 0){
            			return '<span style="color:red">未完成</span>'; 
            		}else{
            			return '<span style="color:red">无历史位置记录</span>'; 
            		}
            	}
    		}
        },rowAction];
    
	var patrol_grid = new Ext.grid.GridPanel({
        store: patrol_store,
        region : 'center',
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        columns:patrol_gridColumns,
        viewConfig: {
            forceFit:true
        },
        plugins:[rowAction],
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,//pageSize 表示每页有几条记录,
            store: patrol_store,
            displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
        })
    });
    
    var comunity_store = new Ext.data.JsonStore({ 
		url : "map/area/communitys-data.action", 
		fields : ['id','name'], 
		root : "values", 
		baseParams : { 
		} 
	}); 
	comunity_store.addListener("load", function(){ 
		comunity_store.insert(0, new Ext.data.Record({ 
			'id' : "",
			'name' : '全部'
		})); 
		if(this.totalLength>0){
			var defaultValue = this.getAt(0).get('id');
			community_show.setValue(defaultValue);
			route_store.load({params : {
				communityId : defaultValue
			}});
		}
	}); 
	comunity_store.load();
	var community_show = new Ext.form.ComboBox( { 
		fieldLabel : '小&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;区', 
//		anchor : anchor_w, 
		mode : 'local', 
		triggerAction : 'all', 
		selectOnFocus : true, 
		forceSelection : true, 
		editable : false, 
		valueField : 'id', 
		displayField : 'name', 
		hiddenName:'communityId',
		name : 'communityId',
		store : comunity_store,
		listeners : {
			'select' : function(combo,record,index ){
				route_show.clearValue();
				route_store.reload({params : {
					communityId : combo.value
				}});
			}
		}
	});
	var route_store = new Ext.data.JsonStore({ 
		url:'datastatistics/patrol/get-routes.action',
		fields : ['id','name'], 
		root : "values"
	});
	route_store.addListener("load", function(){ 
		route_store.insert(0, new Ext.data.Record({ 
			'id' : "",
			'name' : '全部'
		})); 
		route_show.setValue(this.getAt(0).get('id'));
	}); 
	route_store.load();
	var route_show = new Ext.form.ComboBox( { 
		fieldLabel : '路&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;线', 
		mode : 'local', 
		triggerAction : 'all', 
//		selectOnFocus : true, 
//		forceSelection : true, 
		editable : false, 
		valueField : 'id', 
		displayField : 'name', 
		hiddenName:'routeId',
		name : 'routeId',
		store : route_store 
	});
	var is_done_show=new Ext.form.ComboBox ({
		name: 'isDone',
		fieldLabel : '结&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;果', 
		hiddenName : 'isDone',
		autoSelect:true,
		editable:false,
		width:100,
		mode : 'local',
		triggerAction : 'all',
		store : [['','全部'],
				['1','完成'],
				['0','未完成']
			]
    });
    is_done_show.setValue("");
	var leftPanel = new Ext.FormPanel({
		title : '搜索',
		region : 'west',
       	margins:'3',
       	labelWidth: 60, 
	    displayfieldWidth: 25, 
//        frame:true,
        bodyStyle:'padding:5px 5px 10px',
        width: 240,
        defaults: {anchor: '100%'},
        defaultType: 'textfield',
        buttonAlign :'center',
        autoHeight:true,
      	border:false,
//        split:true,
        collapsible:true,
        collapseMode:'mini',
        items: [{
            xtype : 'datefield',
            name : 'startDate',
            format : 'Y-m-d', 
            fieldLabel : '开始时间',
            border:false,
       		editable:false,
       		allowBlank : false,
       		value:Ext.Date.add(new Date(), Ext.Date.DAY, -3),
       		maxValue : new Date()
        },{
            xtype : 'datefield',
            name : 'endDate',
            format : 'Y-m-d', 
            fieldLabel : '结束时间',
            border:false,
       		editable:false,
       		allowBlank : false,
            value:new Date(),
            maxValue : new Date()
        },{
            fieldLabel: '姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名',
            name:'name',
            maxLength:15
        },community_show,route_show,is_done_show],
        buttons:[{
         	text:'查询',
         	handler: function () {
			 	patrol_store.reload();
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
		title : '巡更数据查看',
		border : false,
		layout : 'border',
		items : [leftPanel,patrol_grid],
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