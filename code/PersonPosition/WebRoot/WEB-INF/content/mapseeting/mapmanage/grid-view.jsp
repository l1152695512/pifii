<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 15;
	var map_storeFields = ['id','name','uploadDate','isUsed'];
	var map_store = new Ext.data.JsonStore({
        root: 'list',
        autoLoad:{params:{start:0, limit:pageLimit}},
        totalProperty: 'totalRecord',
        idProperty: 'id',
        remoteSort: true,
        fields: map_storeFields,
        url: 'mapseeting/mapmanage/maps-data.action'
    });
	map_store.on('beforeload',function(store){ 
       store.baseParams = leftPanel.getForm().getValues(); 
    });
    map_store.setDefaultSort('uploadDate', 'desc');
    var sm = new Ext.grid.CheckboxSelectionModel({
		checkOnly:true
	});
	var map_gridColumns = [sm,
    	{
            header: "名称",
            dataIndex: 'name',
            sortable:true,
            align: 'center',
            width: 50
        },{
            header: "上传时间",
            dataIndex: 'uploadDate',
            align: 'center',
            sortable:true,
            width: 90 
        },{
            header: "使用",
            dataIndex: 'isUsed',
            sortable:false,
            align: 'center',
            width: 50,
            renderer: function(value,metadata,record,rowIndex){
    			if('1' == value){
    				return "在使用";
    			}else{
    				return "未使用";
    			}
    		}
        }];
	var addMap= new Ext.Action({
        text: '添加',
        iconCls :'icon-add',
        handler: function(){
        	Ext.Msg.wait('获取数据中....', '提示');
        	Ext.Ajax.request({
				async : false,
				url : 'mapseeting/mapmanage/add-map.action',
				params : {id:id},
				success : function(xhr){
					Ext.Msg.hide();
					var mapInfoFormAdd = eval(xhr.responseText);
					var mapInfoWinAdd = new Ext.Window({
						title: '地图信息',
				        width: 300,
				        height:180, 
				        border:false,
				        modal :true,
				        layout: 'fit',
				        plain:true,
					    constrain: true,
				        closable: true,
				        bodyStyle:'padding:5px;',
				        buttonAlign:'center',
				        resizable:false,
				        items: [mapInfoFormAdd]
					});
					mapInfoWinAdd.show();
				},
				failure:function(xhr){
					Ext.Msg.hide();
					Ext.Msg.alert("提示","数据查询失败,稍后请重试！");
				}
			});
        }
    });
    
    function editMap(id){
    	Ext.Msg.wait('获取数据中....', '提示');
    	Ext.Ajax.request({
			async : false,
			url : 'mapseeting/mapmanage/modify-map.action',
			params : {id:id},
			success : function(xhr){
				Ext.Msg.hide();
				var mapInfoFormEdit = eval(xhr.responseText);
				var mapInfoWinEdit = new Ext.Window({
					title: '地图信息',
			        width: 400,
			        height:330, 
			        border:false,
			        modal :true,
			        layout: 'fit',
			        plain:true,
				    constrain: true,
			        closable: true,
			        bodyStyle:'padding:5px;',
			        buttonAlign:'center',
			        resizable:false,
			        items: [mapInfoFormEdit]
				});
				mapInfoWinEdit.show();
			},
			failure:function(xhr){
				Ext.Msg.hide();
				Ext.Msg.alert("提示","数据获取失败,稍后请重试！");
			}
		});
    }
    
    var deleteMaps= new Ext.Action({
        text: '删除',
        iconCls :'icon-delete',
        handler: function(){
        	var selected = map_grid.getSelectionModel().getSelections();
			var params = [];
			Ext.each(selected, function(item) {
				params.push(item.id);
			});
			if(params.length == 0){
		   		Ext.Msg.alert("提示","没有选择行记录！");
		   		return;
		    }
			Ext.Msg.confirm('确认', '确定要删除选中的地图？',
				function(btn, text) {
					if (btn == 'yes') {
						Ext.Msg.wait('删除数据中....', '提示');
						Ext.Ajax.request({
				       		url : 'mapseeting/mapmanage/delete-maps.action',
				       		params: {ids:params},
				            success : function(xhr) {
				            	Ext.Msg.hide();
				            	map_store.reload();
				           	},
						    failure: function(xhr){
						    	Ext.Msg.hide();
						    	Ext.Msg.alert("提示","删除失败，稍后请重试！");
						    }
				        })
					}
			});
        }
    });
    
	var map_grid = new Ext.grid.GridPanel({
        store: map_store,
        region : 'center',
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        columns:map_gridColumns,
        viewConfig: {
            forceFit:true
        },
        sm: sm,
        tbar: [addMap,'-',deleteMaps],
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,//pageSize 表示每页有几条记录,
            store: map_store,
            displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
        }),
        listeners:{  
			rowdblclick : function(grid,row){  
				var id = grid.getStore().getAt(row).get('id');
				editMap(id);
			}
		} 
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
            fieldLabel: '名称',
            name:'name',
            maxLength:15
        },{
        	xtype : 'datefield',
        	format : 'Y-m-d', 
        	fieldLabel: '上传时间',
            name: 'uploadDate',
            editable:false
        }],
        buttons:[{
         	text:'查询',
         	handler: function () {
			 	map_store.reload();
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
		title : '设备管理',
		border : false,
		layout : 'border',
		items : [leftPanel,map_grid],
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