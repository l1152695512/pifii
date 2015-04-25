<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>(function() {

	var rootId = 'layerId';
	var blid='';

	var rootNode = new Ext.tree.AsyncTreeNode({
		text : '楼层',
		id : rootId,
		expanded : true
	});

	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl : 'input/room/tree-data.action?bid=${bid}'
	});
	var tree = new Ext.tree.TreePanel({
	    title : '楼层列表',
		region : 'west',
		useArrows : true,
		autoScroll : true,
		enableDD : false,
		animate : true,
		width : 200,
		collapsible : true,
		containerScroll : true,
		// auto create TreeLoader
		loader : treeLoader,
		root : rootNode
	});
	// 右边Grid
	var pageLimit = 20;
	var gridDataUrl = "input/room/grid-data.action?bid=${bid}";
	var rowId = 'rid';
	var storeFields = [ 'code', 'rid','shortName', 'lev','ywBuidlayer','proRight', 'roomType'];
			
   
	var tabTitle = "机房列表";
	var sm = new Ext.grid.CheckboxSelectionModel({
		checkOnly : true
	});// 复选框
	var gridColumns = [sm,
		new Ext.grid.RowNumberer({
			header : '序号',
			align : 'center',
			width : 40,
			renderer : function(value, metadata, record, rowIndex) {
				return store.lastOptions.params.start + 1 + rowIndex;
			}
		}), {
			header : "机房编号",
			dataIndex : 'code'
		},  {
			header : "机房简称",
			dataIndex : 'shortName'
		},{
			header : "所属楼层",
			dataIndex : 'ywBuidlayer'
		},{
			header : "机房类型",
			dataIndex : 'roomType',
			sortable : true
		}, {
			header : "机房级别",
			dataIndex : 'lev',
			sortable : true
		}
	];

 
	var gridDelAction = new Ext.Action({
		text : '删除',
		iconCls : 'icon-delete',
		handler : function() {
			var selected = grid.getSelectionModel().getSelections();
			var params = []
			Ext.each(selected, function(item) {
				params.push(item.id);
			});
			var reloadFn = function() {
				store.load(store.lastOptions);
			};
			Ext.Ajax.request({
				url : 'input/room/delete-data.action',
				success : reloadFn,
				failure : reloadFn,
				params : {
					ids : params
				}
			});
		}
	});

	
	var gridrefleshAction = new Ext.Action({
		text : '刷新',
		iconCls : 'icon-redo',
		handler : function() {
			store.reload();
		}
	});
	// 数据存储
	var store = new Ext.data.JsonStore({
		root : 'list',
		autoLoad : {
			params : {
				start : 0,
				limit : pageLimit
			}
		},
		totalProperty : 'totalRecord',
		idProperty : rowId,
		remoteSort : true,
		fields : storeFields,
		url : gridDataUrl

	});
	    //grid事件定义
    var gridListeners = {
    	cellclick:function(grid, rowIndex, columnIndex, e) {
			if(columnIndex != 0){
				var rec = grid.store.getAt(rowIndex);
	       		Ext.Ajax.request({
			    	url:"mjgl/map/room-form.action",
			    	params: { editId :rec.id  },
			    	success:function(xhr){
			    		var editForm = eval(xhr.responseText);
			    		var editWin = new Ext.Window({
			    			title: "机房"+rec.get('shortName'),
						    width: 650,
					        modal :true,
					        layout: 'fit',
					        plain:true,
					        constrain: true,
					        closable: true,
					        bodyStyle:'padding:5px;',
					        buttonAlign:'center',
					        items: [editForm],
						    buttons: [{
						        text: '关闭',
						        handler:function(){
						            editWin.close();
						        }
						    }]
			    		});
			    		editWin.show();
			    	}
			   });
			}
		}
    };
	
	store.setDefaultSort('rid', 'desc');
	var t1 = new Ext.form.Label({
		width : '50',
		html : ' &nbsp;&nbsp;简称:&nbsp;'

	});
	var t2 = new Ext.form.TextField({
		name : 'name',
		width : 65
	});
    var  t3=new Ext.form.Label({
		html:'编码: '   
	});
     var t4 = new Ext.form.TextField({
		name : 'code',
		width : 65
	});   
	var q = new Ext.Action({
		text : '查询',
		iconCls : 'icon-find',
		handler : function() {
			var shortName = t2.getValue();
			var code=t4.getValue();
			store.setBaseParam("shortName", shortName);
			store.setBaseParam("code", code);
		    if(blid!=''&&blid!='layerId'){
    	      	store.setBaseParam('blid','${blid}');
    	    }
	 
			store.reload();
		}
	});
	
	var r = new Ext.Action({
		text : '清空',
		iconCls : 'icon-redo',
		handler : function() {
			 t2.setValue("");
			 t4.setValue("");
		}
	});
	
	
	var grid = new Ext.grid.GridPanel({
		region : 'center',
		title : tabTitle,
		store : store,
		region : 'center',
		trackMouseOver : true,

		stripeRows : true,
		disableSelection : true,
		loadMask : true,
		columns : gridColumns,
		viewConfig : {
			forceFit : true
		},
		tbar : [gridDelAction,'-', gridrefleshAction,'-',t1,t2,'&nbsp;',t3,t4,q,'&nbsp;&nbsp;',r],
		bbar : new Ext.PagingToolbar({
			pageSize : pageLimit,// pageSize 表示每页有几条记录,
			store : store,
			displayInfo : true,
			displayMsg : '第{0} - {1}行,共 {2}行',
			emptyMsg : "空"
		}),

		sm : sm,
		listeners : gridListeners
	});
	// 右边grid结束


	tree.on('click', function(node) {
		blid = node.id;
		store.setBaseParam("blid", node.id);
		store.load({params : {start : 0,limit : pageLimit}});
	});

	var panel = new Ext.Panel({
		layout : 'border',
		margins : '2 5 5 0',
		border : false,
		items : [tree, grid]
	});

    return panel;
})();