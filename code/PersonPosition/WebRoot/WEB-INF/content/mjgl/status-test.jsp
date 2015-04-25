<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 25;
	var status_storeFields = ['id','phone','cardID','comname','uploadtime','name'];
	var status_store = new Ext.data.JsonStore({
        root: 'list',
        autoLoad:{params:{start:0, limit:pageLimit}},
        totalProperty: 'totalRecord',
        idProperty: 'id',
        remoteSort: true,
        fields: status_storeFields,
        url: 'mjgl/status-data.action'
    });
	status_store.on('beforeload',function(store){ 
       store.baseParams = leftPanel.getForm().getValues(); 
    });
   status_store.setDefaultSort('id', 'desc');
   
	var status_gridColumns = [new Ext.grid.RowNumberer({
			header : '序号',
			align : 'center',
			width : 40,
			renderer : function(value, metadata, record, rowIndex) {
				return status_store.lastOptions.params.start + 1 + rowIndex;
			}
		}),{
            header: "卡编号",
            dataIndex: 'cardID',
            align: 'center',
            sortable:true,
            width: 80
        },{
            header: "手机号码",
            dataIndex: 'phone',
            align: 'center',
            sortable:true,
            width: 70
        },{
            header: "小区名称",
            dataIndex: 'comname',
            align: 'center',
            sortable:true,
            width: 70
        },{
            header: "RFID卡名称",
            dataIndex: 'name',
            align: 'center',
            sortable:true,
            width: 50
        },{
            header: "上传时间差",
            dataIndex: 'uploadtime',
            sortable:true,
            align: 'center',
            width: 80
        }];
	  //导出报表
    var exportExcel = new Ext.Action(Ext.apply({
    	text: '导出数据列表',
    	iconCls :'export-excel',
    	handler: function(){
    		var params = leftPanel.getForm().getValues();
    		if(params.communityId != ''){
    			params.communityName = comunity_store.getById(params.communityId).get("name");
    		}
    		console.debug(params);
    		var paramsEncode = Ext.urlEncode(params);
    		console.debug(paramsEncode);
    		var url = "mjgl/export-excel.action?" + paramsEncode;
    		var xls = window.open(url); 
		 	xls.focus();
    	}
    }));
	var status_grid = new Ext.grid.GridPanel({
     	store: status_store,
        region : 'center',
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        columns:status_gridColumns,
        viewConfig: {
            forceFit:true
        },
        tbar : [exportExcel],
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,//pageSize 表示每页有几条记录,
            store: status_store,
            displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
        })
        /*listeners:{  
			rowdblclick : function(grid,row){  
				var id = grid.getStore().getAt(row).get('id');
				showFormPanel(id);
			}
		} */
    });
    
     var comunity_store = new Ext.data.JsonStore({ 
		url : "mjgl/communitys-data.action", 
		fields : ['id','commname'], 
		root : "values", 
		baseParams : { 
		} 
	}); 
	comunity_store.addListener("load", function(){ 
		comunity_store.insert(0, new Ext.data.Record({ 
			'id' : "",
			'commname' : '全部'
		})); 
		if(this.totalLength>0){
			community.setValue(this.getAt(0).get('id'));
		}
	}); 
	comunity_store.load();
	var community = new Ext.form.ComboBox( { 
		fieldLabel : '&nbsp;&nbsp;&nbsp;小&nbsp;&nbsp;&nbsp;&nbsp;区', 
//		anchor : anchor_w, 
		mode : 'local', 
		triggerAction : 'all', 
		selectOnFocus : true, 
		forceSelection : true, 
		editable : false, 
		valueField : 'id', 
		displayField : 'commname', 
		hiddenName:'communityId',
		name : 'communityId',
		store : comunity_store 
	});
    var leftPanel = new Ext.FormPanel({
		title : '搜索',
		region : 'west',
       	margins:'3',
       	labelWidth: 65, 
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
            fieldLabel: '&nbsp;&nbsp;卡&nbsp;编&nbsp;号',
            name:'cardId',
            maxLength:15
        },{
            fieldLabel: '&nbsp;&nbsp;卡&nbsp;名&nbsp;称',
            name:'name',
            maxLength:15
        },community],
        buttons:[{
         	text:'查询',
         	handler: function () {
			 	status_store.reload();
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
		title : '状态检测',
		border : false,
		layout : 'border',
		items : [leftPanel,status_grid],
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