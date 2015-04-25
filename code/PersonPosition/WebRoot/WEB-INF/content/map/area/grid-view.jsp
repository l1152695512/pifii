<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>(function() {
	//userId='${userId}';
	var isLeaf = false;
	var rootId = 'rootId';
	var pid='rootId';
//	var pname='无父区域';

	var rootNode = new Ext.tree.AsyncTreeNode({
		text : '区域',
		id : rootId,
		expanded : true
	});

	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl : 'setting/region/tree-data.action'
	});
	var tree = new Ext.tree.TreePanel({
	    title : '区域列表',
		region : 'west',
		useArrows : true,
		autoScroll : true,
		enableDD : false,
		animate : true,
		width : 200,
		collapsible : true,
		containerScroll : true,
		rootVisible : false, 
		// auto create TreeLoader
		loader : treeLoader,
		root : rootNode
	});
	tree.on('click', function(node) {
		if(node.leaf){
			isLeaf = true;
		}else{
			isLeaf = false;
		}
		if (node.id == rootId) {
		     pid=node.id;
//		     pname='无父区域';
		} else {
			 pid=node.id;
//		     pname=node.text;
		}	
		store.reload();
	});
	function hideRowAction(iconCls,index){
    	$("#this_grid_panel").find(".x-grid3-row").eq(index).find("."+iconCls).hide();
    }
	var floorClass = 'floor-map';
	var previewClass = 'preview';
	var viewMapClass = 'view-map';
	var rowaction=	new Ext.ux.grid.RowActions({
		 header:'操作',
		 width:180,
		 align: 'center',
		 autoWidth:false,
		 keepSelection:true,
		 actions:[{
			iconCls:viewMapClass,
			tooltip:'编辑地图',
			text: '编辑地图',
			callback:function(grid, record, action, row, col){
				Ext.Msg.wait('获取地图中....', '提示');
				Ext.Ajax.request({
					async : false,
					url : 'map/area/view-map.action',
					params : {id:record.data.id},
					success : function(xhr){
						Ext.Msg.hide();
						var viewMapWin = eval(xhr.responseText);
						viewMapWin.show();
					},
					failure:function(xhr){
						Ext.Msg.hide();
						Ext.Msg.alert("提示","数据获取失败,稍后请重试！");
					}
				});
			}
		},{
			iconCls:previewClass,
			tooltip:'编辑预览图',
			text: '编辑预览图',
			callback:function(grid, record, action, row, col){
				Ext.Msg.wait('获取预览图中....', '提示');
				Ext.Ajax.request({
					async : false,
					url : 'map/area/preview-image.action',
					params : {id:record.data.id},
					success : function(xhr){
						Ext.Msg.hide();
						var previewMapWin = eval(xhr.responseText);
						previewMapWin.show();
					},
					failure:function(xhr){
						Ext.Msg.hide();
						Ext.Msg.alert("提示","数据获取失败,稍后请重试！");
					}
				});
			}
		},{
			iconCls:floorClass,
			tooltip:'添加该楼层平面',
			text: '添加该楼层平面',
			callback:function(grid, record, action, row, col){
				editCommunity({dependentId:record.data.id});
				//Ext.Msg.alert("提示","功能正在开发！");
			}
		}]
	});
	var mapWin;//用于显示上传框
	function editCommunity(params){
    	Ext.Msg.wait('正在获取数据....', '提示');
    	Ext.Ajax.request({
			async : false,
			url : 'map/area/add-or-modify-map.action',
			params : params,
			success : function(xhr){
				Ext.Msg.hide(); 
				var editWin = eval(xhr.responseText);
				editWin.show();
			},
			failure:function(xhr){
				Ext.Msg.hide(); 
				Ext.Msg.alert("提示","数据查询失败,稍后请重试！");
			}
		});
    }
	var pageLimit = 20;
	var gridDataUrl = "map/area/grid-data.action";
	var rowId = 'id';
	var storeFields = [ 'id','districtName','communityName','cityName','provinceName','createDate','area_type','dependent_community'];
	var tabTitle = "小区列表";
	var sm = new Ext.grid.CheckboxSelectionModel({
		checkOnly : true
	});// 复选框
	var gridColumns = [sm,new Ext.grid.RowNumberer({
			header : '序号',
			align : 'center',
			width : 40,
			renderer : function(value, metadata, record, rowIndex) {
				return store.lastOptions.params.start + 1 + rowIndex;
			}
		}),{
		 	align: 'center',
			header : "省",
			width : 80,
			dataIndex : 'provinceName'
		},{
		 	align: 'center',
			header : "市",
			width : 80,
			dataIndex : 'cityName'
		},{
		 	align: 'center',
			header : "区",
			width : 80,
			dataIndex : 'districtName'
		},{
		 	align: 'center',
			header : "小区名称",
			width : 100,
			dataIndex : 'communityName'
		},{
		 	align: 'center',
			header : "类型",
			width : 60,
			dataIndex : 'area_type',
			sortable:false,
            renderer: function(value,metadata,record,rowIndex){
            	if('2' == value){
            		return '楼层';
            	}else{
            		return '平面';
            	}
    		}
		},{
			align: 'center',
			header : "创建日期",
			width : 100,
			dataIndex : 'createDate'
		},rowaction
	];
	// 添加按钮
     var gridAddAction = new Ext.Action(Ext.apply({
    	text: '添加',
    	iconCls :'icon-add',
    	handler: function(){
//    		if(!isLeaf){
//    			Ext.Msg.alert('提示','请先点击左边的区域列表中的区，然后向该区中添加小区！');
//    			return;
//    		}
    		Ext.Msg.wait('正在获取数据....', '提示');
	    	Ext.Ajax.request({
				async : false,
				url : 'map/area/add-community.action',
				success : function(xhr){
					Ext.Msg.hide(); 
					var editWin = eval(xhr.responseText);
					editWin.show();
				},
				failure:function(xhr){
					Ext.Msg.hide(); 
					Ext.Msg.alert("提示","数据查询失败,稍后请重试！");
				}
			});
    	}
    }));
 
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
				Ext.Msg.hide();
				store.load(store.lastOptions);
			};
			if(params.length == 0){
		   		Ext.Msg.alert("提示","没有选择数据！");
		   		return;
		    }
			Ext.Msg.confirm('确认', '确定要删除选中的数据？',
				function(btn, text) {
					if (btn == 'yes') {
						Ext.Msg.wait('删除数据中....', '提示');
						Ext.Ajax.request({
							url : 'map/area/delete-data.action',
							success : reloadFn,
							failure : reloadFn,
							params : {
								ids : params
							}
						});
					}
			});
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
		url : gridDataUrl,
        listeners:{
			"load":function(store,records,options){
				for(var i=0;i<records.length;i++){
					if(records[i].data.area_type != "2"){//如果不是楼层的话就隐藏“添加该楼层平面”
						hideRowAction(floorClass,i);
//					}else{//如果是楼层就隐藏添加地图按钮
//						hideRowAction(viewMapClass,i);
					}
					if(records[i].data.dependent_community != ""){//如果是楼层中具体的楼层平面则隐藏区域预览图的添加
						hideRowAction(previewClass,i);
					}
				}
	      	}
        }

	});
	store.setDefaultSort('createDate', 'desc');
	store.on('beforeload',function(store){
       	store.baseParams.name = t2.getValue();
       	if(pid!=''&&pid!='rootId'){
       		store.baseParams.pid = pid;
       	}
    });
	
	
	var t1 = new Ext.form.Label({
		width : '50',
		html : ' &nbsp;&nbsp;小区名称:&nbsp;'

	});
	var t2 = new Ext.form.TextField({
		name : 'name',
		width : 120
	});
  
	var q = new Ext.Action({
		text : '查询',
		iconCls : 'icon-find',
		handler : function() {
			store.reload();
		}
	});
	
	var r = new Ext.Action({
		text : '清空',
		iconCls : 'icon-redo',
		handler : function() {
			 t2.setValue("");
		}
	});
    
	var grid = new Ext.grid.GridPanel({
		id:'this_grid_panel',
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
		tbar : [gridAddAction, '-', gridDelAction,'-',t1,t2,'&nbsp;',q,'&nbsp;&nbsp;',r],
		bbar : new Ext.PagingToolbar({
			pageSize : pageLimit,// pageSize 表示每页有几条记录,
			store : store,
			displayInfo : true,
			displayMsg : '第{0} - {1}行,共 {2}行',
			emptyMsg : "空"
		}),
		sm : sm,
		plugins:[rowaction],
		listeners:{  
			rowdblclick : function(grid,row){  
				var id = grid.getStore().getAt(row).get('id');
				editCommunity({id:id});
			}
		} 
	});
	
	var panel = new Ext.Panel({
		layout : 'border',
		margins : '2 5 5 0',
		border : false,
		items : [tree, grid]
	});

    return panel;
})();