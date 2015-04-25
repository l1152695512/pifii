<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 15;
	var device_storeFields = ['id','communityName','name','age','phone','type','description'];
	var person_store = new Ext.data.JsonStore({
        root: 'list',
        autoLoad:{params:{start:0, limit:pageLimit}},
        totalProperty: 'totalRecord',
        idProperty: 'id',
        remoteSort: true,
        fields: device_storeFields,
        url: 'mapseeting/stationmanage/stations-data.action'
    });
	person_store.on('beforeload',function(store){ 
       store.baseParams = leftPanel.getForm().getValues(); 
    });
    person_store.setDefaultSort('name', 'desc');
    var sm = new Ext.grid.CheckboxSelectionModel({
		checkOnly:true
	});
	var rowAction=	new Ext.ux.grid.RowActions({
		 header:'操作',
		 width:100,
		 align: 'center',
		 autoWidth:false,
		 keepSelection:true,
		 actions:[{
			iconCls:'my-icon-search',
			tooltip:'查询历史轨迹',
			text: '查询历史轨迹',
			callback:function(grid, record, action, row, col){
				addLazyLoadWorkSpaceTab(record.data.id,'历史轨迹查询','historyroute/grid-view.action?personId='+record.data.id,{layout:'anchor'},null,null,null);
			}
		},[]]
	});
	var device_gridColumns = [sm,
    	{
            header: "小区",
            dataIndex: 'communityName',
            sortable:true,
            align: 'center',
            width: 50
        },{
            header: "类型",
            dataIndex: 'type',
            align: 'center',
            width: 70
        },{
            header: "姓名",
            dataIndex: 'name',
            align: 'center',
            sortable:true,
            width: 90 
        },{
            header: "年龄",
            dataIndex: 'age',
            sortable:true,
            align: 'center',
            width: 50
        },{
            header: "电话",
            dataIndex: 'phone',
            align: 'center',
            width: 70
        },{
            header: "描述",
            dataIndex: 'description',
            align: 'center',
            width: 70
        },rowAction];
	var addDevice= new Ext.Action({
        text: '添加',
        iconCls :'icon-add',
        handler: function(){
        	showFormPanel('');
        }
    });
    var photoWin;
    function showFormPanel(id){
    	Ext.Msg.wait('正在获取数据....', '提示');
    	Ext.Ajax.request({
			async : false,
			url : 'mapseeting/stationmanage/add-or-modify-station.action',
			params : {id:id},
			success : function(xhr){
				Ext.Msg.hide(); 
				var personInfoForm = eval(xhr.responseText);
				var personInfoWin = new Ext.Window({
					title: '人员信息',
			        width: 330,
			        height:340, 
			        border:false,
			        modal :true,
			        layout: 'fit',
			        plain:true,
				    constrain: true,
			        closable: true,
			        bodyStyle:'padding:5px;',
			        buttonAlign:'center',
			        resizable:false,
			        items: [personInfoForm],
			        listeners:{
						"close":function(panel){
							try{
								photoWin.destroy();
							}catch(e){
							}
			          	}
					}
				});
				personInfoWin.show();
			},
			failure:function(xhr){
				Ext.Msg.hide(); 
				Ext.Msg.alert("温馨提醒","数据查询失败,稍后请重试！");
			}
		});
    }
    
    var deleteDevice= new Ext.Action({
        text: '删除',
        iconCls :'icon-delete',
        handler: function(){
        	var selected = device_grid.getSelectionModel().getSelections();
			var params = [];
			Ext.each(selected, function(item) {
				params.push(item.id);
			});
			if(params.length == 0){
		   		Ext.Msg.alert("提示","没有选择数据！");
		   		return;
		    }
			Ext.Msg.confirm('确认', '确定要删除选中的人员信息？',
				function(btn, text) {
					if (btn == 'yes') {
						Ext.Msg.wait('正在删除数据....', '提示');
						Ext.Ajax.request({
				       		url : 'mapseeting/stationmanage/delete-stations.action',
				       		params: {ids:params},
				            success : function(xhr) {
				            	Ext.Msg.hide(); 
				            	person_store.reload();
				           	},
						    failure: function(xhr){
						    	Ext.Msg.hide(); 
						    	Ext.Msg.alert("温馨提醒","删除失败，稍后请重试！");
						    }
				        })
					}
			});
        }
    });
    
	var device_grid = new Ext.grid.GridPanel({
        store: person_store,
        region : 'center',
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        columns:device_gridColumns,
        viewConfig: {
            forceFit:true
        },
        sm: sm,
        plugins:[rowAction],
        tbar: [addDevice,'-',deleteDevice],
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,//pageSize 表示每页有几条记录,
            store: person_store,
            displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
        }),
        listeners:{  
			rowdblclick : function(grid,row){  
				var id = grid.getStore().getAt(row).get('id');
				showFormPanel(id);
			}
		} 
    });
    var comunity_store = new Ext.data.JsonStore({ 
		url : "mapseeting/stationmanage/communitys-data.action", 
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
			community.setValue(this.getAt(0).get('id'));
		}
	}); 
	comunity_store.load();
	var community = new Ext.form.ComboBox( { 
		fieldLabel : '小区', 
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
            fieldLabel: '名称',
            name:'name',
            maxLength:15
        },community],
        buttons:[{
         	text:'查询',
         	handler: function () {
			 	person_store.reload();
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
		title : '人员管理',
		border : false,
		layout : 'border',
		items : [leftPanel,device_grid],
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