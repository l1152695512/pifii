<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 25;
	var photo_storeFields = ['communityName','id','name','phone','type'];
	var photo_store = new Ext.data.JsonStore({
        root: 'list',
        autoLoad:{params:{start:0, limit:pageLimit}},
        totalProperty: 'totalRecord',
        idProperty: 'id',
        remoteSort: true,
        fields: photo_storeFields,
        url: 'mjgl/getphoto-data.action'
    });
	photo_store.on('beforeload',function(store){ 
       store.baseParams = leftPanel.getForm().getValues(); 
    });
   photo_store.setDefaultSort('communityName', 'desc');
    
     var rowaction=	new Ext.ux.grid.RowActions({
			 header:'操作',
			 width:90,
			 autoWidth:false,
			 keepSelection:true,
			 align: 'center',
			 actions:[{
				iconCls:'icon-photo-show',
				text: '拍照',
				callback:function(grid, record, action, row, col){
					Ext.Ajax.request({
		    		   url:'mjgl/photo-show.action',
		    			params: {id:record.id},
		    			success:function(xhr){
		    				var win1 = eval(xhr.responseText);
		    				win1.show();
		    			}
		    		});
				}
		},{
				iconCls:'icon-video-show',
				text:'录像',
				callback:function(grid, record, action, row, col){
					Ext.Ajax.request({
						url : 'mjgl/video-show.action',
						params: {id:record.id},
		    			success:function(xhr){
		    				var win1 = eval(xhr.responseText);
		    				win1.show();
		    			}
		    		});
				}
			}]
			
		});

	var photo_gridColumns = [new Ext.grid.RowNumberer({
			header : '序号',
			align : 'center',
			width : 40,
			renderer : function(value, metadata, record, rowIndex) {
				return photo_store.lastOptions.params.start + 1 + rowIndex;
			}
		}),{
            header: "小区",
            dataIndex: 'communityName',
            align: 'center',
            sortable:true,
            width: 70
        },{
            header: "人员",
            dataIndex: 'name',
            align: 'center',
            sortable:true,
            width: 70
        },{
            header: "电话号码",
            dataIndex: 'phone',
            align: 'center',
            sortable:true,
            width: 90 
        },{
            header: "所属群组",
            dataIndex: 'type',
            sortable:true,
            align: 'center',
            width: 70
        },
        rowaction];
	 
	var photo_grid = new Ext.grid.GridPanel({
        store: photo_store,
        region : 'center',
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        columns:photo_gridColumns,
        viewConfig: {
            forceFit:true
        },
        plugins:[rowaction],
        
//        plugins:[rowAction],
       // tbar: [addPhoto,'-',deletePhoto],
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,//pageSize 表示每页有几条记录,
            store: photo_store,
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
      var type_store = new Ext.data.JsonStore({ 
		url : "mjgl/type-view.action", 
		fields : ['DIC_ID','KEY_VALUE'], 
		root : "values", 
		baseParams : { 
		} 
	}); 
	type_store.addListener("load", function(){ 
		type_store.insert(0, new Ext.data.Record({ 
			'DIC_ID' : "",
			'KEY_VALUE' : '全部'
		})); 
		if(this.totalLength>0){
			type.setValue(this.getAt(0).get('DIC_ID'));
		}
	}); 
	type_store.load();
	var type = new Ext.form.ComboBox( { 
		fieldLabel : '群&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;组', 
//		anchor : anchor_w, 
		mode : 'local', 
		triggerAction : 'all', 
		selectOnFocus : true, 
		forceSelection : true, 
		editable : false, 
		valueField : 'DIC_ID', 
		displayField : 'KEY_VALUE', 
		hiddenName:'id',
		name : 'id',
		store : type_store 
	});
	var comunity_store = new Ext.data.JsonStore({ 
		url : "personmanage/communitys-data.action", 
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
		fieldLabel : '小&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;区', 
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
            fieldLabel: '姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名',
            name:'name',
            maxLength:15
        },type,community],
        buttons:[{
         	text:'查询',
         	handler: function () {
			 	photo_store.reload();
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
		title : '照片管理',
		border : false,
		layout : 'border',
		items : [leftPanel,photo_grid],
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
