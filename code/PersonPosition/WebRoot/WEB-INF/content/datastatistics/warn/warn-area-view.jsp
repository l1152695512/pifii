<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
//	var pageLimit = 15;
	var warn_area_storeFields = ['person_name','community_name','area_name','start_time','end_time','area_type','date','position_date','is_legal','person_id','phone','area_id','area_time_id'];
	var warn_area_store = new Ext.data.JsonStore({
        root: 'list',
//        autoLoad:{params:{start:0, limit:pageLimit}},
        autoLoad:false,
        autoLoad:true,
        totalProperty: 'totalRecord',
        idProperty: 'id',
        remoteSort: false,
        fields: warn_area_storeFields,
        url: 'datastatistics/warn/warn-area-data.action'
    });
    warn_area_store.setDefaultSort('date', 'desc');
	warn_area_store.on('beforeload',function(store){ 
		var searchValues = leftPanel.getForm().getValues();
       	store.baseParams = searchValues; 
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
				if("1"==record.data.is_legal){
					if(record.data.position_date.length > 0){
						Ext.Msg.alert('提示','没有违规记录！');
					}else{
						Ext.Msg.alert('提示','无历史位置记录！');
					}
				}else{
					var illegal_type = "";
					if("9"==record.data.area_type){
						illegal_type = "1";
					}else{
						illegal_type = "-1";
					}
					Ext.Msg.wait('加载数据中....', '提示');
					Ext.Ajax.request({
						url:'datastatistics/warn/warn-area-info-view.action',
						params:{personName:record.data.person_name,areaName:record.data.area_name,communityName:record.data.community_name,
								date:record.data.date,personId:record.data.person_id,areaId:record.data.area_id,
								areaTimeId:record.data.area_time_id,areaType:record.data.area_type,phone:record.data.phone},
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
			}
		}]
	});
	var warn_area_gridColumns = [
    	new Ext.grid.RowNumberer({
			header : '序号',
			align : 'center',
			width : 40,
			renderer : function(value, metadata, record, rowIndex) {
				return 1 + rowIndex;
			}
		}),{
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
            header: "区域名称",
            dataIndex: 'area_name',
            align: 'center',
            sortable:true,
            width: 90 
        },{
            header: "限制",
            dataIndex: 'area_type',
            align: 'center',
            sortable:true,
            width: 90,
            renderer: function(value,metadata,record,rowIndex){
            	if(value == '9'){
	    			return '区域外';  
            	}else if(value == '10'){
            		return '区域内';
            	}
    		} 
        },{
            header: "开始时间",
            dataIndex: 'start_time',
            align: 'center',
            sortable:true,
            width: 90 
        },{
            header: "结束时间",
            dataIndex: 'end_time',
            align: 'center',
            sortable:true,
            width: 90 
        },{
            header: "结果",
            dataIndex: 'is_legal',
            sortable:true,
            align: 'center',
            width: 70,
            renderer: function(value,metadata,record,rowIndex){
            	if(value == '0'){
	    			return '<span style="color:red">越界</span>';  
            	}else if(value == '1'){
            		if(record.data.position_date.length > 0){
            			return "正常";
            		}else{
            			return '<span style="color:red">无历史位置记录</span>';
            		}
            	}
    		}
        },rowAction];
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
    		var paramsEncode = Ext.urlEncode(params);
    		console.debug(paramsEncode);
    		var url = "datastatistics/warn/export-excel.action?" + paramsEncode;
    		var xls = window.open(url); 
		 	xls.focus();
    	}
    }));
	var warn_area_grid = new Ext.grid.GridPanel({
        store: warn_area_store,
        region : 'center',
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        columns:warn_area_gridColumns,
        viewConfig: {
            forceFit:true
        },
        plugins:[rowAction],
        tbar : [exportExcel]
//        ,
//        bbar: new Ext.PagingToolbar({
////            pageSize: pageLimit,//pageSize 表示每页有几条记录,
//            store: warn_area_store,
//            displayInfo: true,
//            displayMsg: '第{0} - {1}行,共 {2}行',
//            emptyMsg: "空"
//        })
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
			warn_area_combo_store.load({params : {
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
				warn_area_show.clearValue();
				warn_area_combo_store.reload({params : {
					communityId : combo.value
				}});
			}
		}
	});
	var warn_area_combo_store = new Ext.data.JsonStore({ 
		url:'datastatistics/warn/combo-warn-area.action',
		fields : ['id','name'], 
		root : "values"
	});
	warn_area_combo_store.addListener("load", function(){ 
		warn_area_combo_store.insert(0, new Ext.data.Record({ 
			'id' : "",
			'name' : '全部'
		})); 
		warn_area_show.setValue(this.getAt(0).get('id'));
	}); 
	warn_area_combo_store.load();
	var warn_area_show = new Ext.form.ComboBox( { 
		fieldLabel : '告警区域', 
		mode : 'local', 
		triggerAction : 'all', 
//		selectOnFocus : true, 
//		forceSelection : true, 
		editable : false, 
		valueField : 'id', 
		displayField : 'name', 
		hiddenName:'warnAreaId',
		name : 'warnAreaId',
		store : warn_area_combo_store 
	});
	var is_illegal_show=new Ext.form.ComboBox ({
		name: 'isLegal',
		fieldLabel : '结&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;果', 
		hiddenName : 'isLegal',
		autoSelect:true,
		editable:false,
		width:100,
		mode : 'local',
		triggerAction : 'all',
		store : [['','全部'],
				['1','正常'],
				['0','越界']
			]
    });
    is_illegal_show.setValue("");
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
        },community_show,warn_area_show],
        buttons:[{
         	text:'查询',
         	handler: function () {
			 	warn_area_store.reload();
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
		title : '警告区域数据统计',
		border : false,
		layout : 'border',
		items : [leftPanel,warn_area_grid],
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