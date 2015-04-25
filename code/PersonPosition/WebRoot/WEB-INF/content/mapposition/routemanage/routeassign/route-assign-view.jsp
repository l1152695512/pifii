<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 20;
	var rootNodeId = '0';
	var currentNodeId = rootNodeId;
	var rootNode = new Ext.tree.AsyncTreeNode({
		id : rootNodeId,
		text : '所有路线',
		leaf : false,
		draggable : false,
		expanded : true
	});
	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl : 'mapposition/routemanage/routeplan/get-routes.action'
	});
    treeLoader.on("load", function(loader, node) {
    });
    var tree = new Ext.tree.TreePanel({
		title : '路线信息',
	    region : 'west',
		useArrows : true,
		autoScroll : true,
		enableDD : false,
		animate : true,
		width : 250,
//		collapsible : true,
		rootVisible : true, 
		containerScroll : true,
		loader : treeLoader,
		root : rootNode,
 	    listeners:{    
      		 'click':function(node, event) {
      		 	currentNodeId = node.id;
      		 	personRootNode.getUI().toggleCheck(false);
      		 	personTree.getTopToolbar().hide();
      		 	disablePersonTree(true);
      			route_time_store.reload();
      		 },
      		 'beforeclick':function(node, event) {
      		 	var previousSelectNode = tree.getSelectionModel().getSelectedNode();
      		 	if(previousSelectNode){
      		 		if(previousSelectNode.id == node.id){
      		 			return false;
      		 		}
      		 	}
      		 	if(modifyAction.isHidden()){
      		 		Ext.Msg.alert("温馨提醒","请先保存右边的人员列表！");
      		 		return false;
      		 	}
      		 }
      	} 
	});
	var route_time_storeFields = ['route_id','id','route_name','name','start_time','end_time','is_used'];
	var route_time_store = new Ext.data.JsonStore({
        url: 'mapposition/routemanage/routeassign/route-times.action',
        root: 'list',
        autoLoad:{params:{start:0, limit:pageLimit}},
        totalProperty: 'totalRecord',
        idProperty: 'id',
        remoteSort: true,
        fields: route_time_storeFields
    });
	route_time_store.on('beforeload',function(store){ 
		clickRow = -1;
      	store.baseParams = {routeId:currentNodeId}; 
    });
    route_time_store.setDefaultSort('route_name', 'desc');
    var sm = new Ext.grid.CheckboxSelectionModel({
		checkOnly:false
	});
	var rowAction=	new Ext.ux.grid.RowActions({
		 header:'操作',
		 width:100,
		 align: 'center',
		 autoWidth:false,
		 keepSelection:true,
		 actions:[{
			iconCls:'setting-btn',
			tooltip:'详细设置',
			text: '详细设置',
			callback:function(grid, record, action, row, col){
				Ext.Msg.wait('加载数据中....', '提示');
				Ext.Ajax.request({
					url:'mapposition/routemanage/routeassign/show-route-points.action',
					params:{routeTimeId:record.data.id},
					success:function(xhr){
						Ext.Msg.hide(); 
						var route_win = eval(xhr.responseText);
						route_win.show();
					},
					failure:function(xhr){
						Ext.Msg.hide(); 
						Ext.Msg.alert('提示','加载数据失败，稍后请重试！');
					}
				});
			}
		}]
	});
	var route_time_gridColumns = [sm,
    	new Ext.grid.RowNumberer({
			header : '序号',
			align : 'center',
			width : 40,
			renderer : function(value, metadata, record, rowIndex) {
				return route_time_store.lastOptions.params.start + 1 + rowIndex;
			}
		}),{
            header: "所属路线",
            dataIndex: 'route_name',
            align: 'center',
            sortable:true,
            width: 90 
        },{
            header: "名称",
            dataIndex: 'name',
            align: 'center',
            sortable:false,
            width: 90 
        },{
            header: "开始时间",
            dataIndex: 'start_time',
            sortable:true,
            align: 'center',
            width: 50
        },{
            header: "结束时间",
            dataIndex: 'end_time',
            align: 'center',
            sortable:true,
            width: 70
        },{
            header: "使用",
            dataIndex: 'is_used',
            align: 'center',
            sortable:false,
            width: 90,
            renderer:function(value,metadata,record,rowIndex){
				if(value=='1'){
					return '是';
				}
				return '否';
    		}
        },rowAction];
    var addTime= new Ext.Action({
        text: '添加',
        iconCls :'icon-add',
        handler: function(){
        	if(modifyAction.isHidden()){
				Ext.Msg.alert("温馨提醒","请先保存右边的人员列表！");
		 		return;
			}
        	var selectNode = tree.getSelectionModel().getSelectedNode();
			if(selectNode && selectNode.isLeaf()){
				show_route_time_panel(selectNode.id,'')
			}else{
				Ext.Msg.alert("提示","请先选择路线！");
			}
        }
    });
	var deleteTime= new Ext.Action({
        text: '删除',
        iconCls :'icon-delete',
        handler: function(){
        	if(modifyAction.isHidden()){
				Ext.Msg.alert("温馨提醒","请先保存右边的人员列表！");
		 		return;
			}
        	var selected = route_time_grid.getSelectionModel().getSelections();
			var params = [];
			Ext.each(selected, function(item) {
				params.push(item.id);
			});
			if(params.length == 0){
		   		Ext.Msg.alert("提示","请先选择数据！");
		   		return;
		    }
			Ext.Msg.confirm('确认', '确定要删除选中的巡更时间？',
				function(btn, text) {
					if (btn == 'yes') {
						Ext.Msg.wait('正在删除数据....', '提示');
						Ext.Ajax.request({
				       		url : 'mapposition/routemanage/routeassign/delete-route-time.action',
				       		params: {ids:params},
				            success : function(xhr) {
				            	Ext.Msg.hide(); 
				            	var obj = Ext.decode(xhr.responseText);
								if(obj.success){
									personRootNode.getUI().toggleCheck(false);
      		 						personTree.getTopToolbar().hide();
      		 						disablePersonTree(true);
					      		 	route_time_grid.selModel.deselectRow(clickRow);
					      		 	clickRow = -1;
					      		 	route_time_store.reload();
								}else{
									Ext.Msg.alert("温馨提醒","删除失败，稍后请重试！");
								}
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
    function show_route_time_panel(route_id,id){
    	Ext.Msg.wait('获取数据中....', '提示');
		Ext.Ajax.request({
			url:'mapposition/routemanage/routeassign/add-or-modify-route-time.action',
			params: {routeId:route_id,id:id},
			success:function(xhr){
				Ext.Msg.hide(); 
				var routeTimeWin = eval(xhr.responseText);
				routeTimeWin.show();
			},
			failure:function(xhr){
				Ext.Msg.hide(); 
				Ext.Msg.alert('提示','暂时不能添加巡更时间！');
			}
		});
    }
	var route_time_grid = new Ext.grid.GridPanel({
        store: route_time_store,
        region : 'center',
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        border : false,
        columns:route_time_gridColumns,
        viewConfig: {
            forceFit:true
        },
        sm: sm,
        plugins:[rowAction],
        tbar: [addTime,'-',deleteTime],
        listeners:{  
			'rowdblclick' : function(grid,row){  
				if(modifyAction.isHidden()){
					Ext.Msg.alert("温馨提醒","请先保存右边的人员列表！");
			 		return;
				}
				var row_data = grid.getStore().getAt(row);
				var id = row_data.id;
				show_route_time_panel('',id);
			},
			'rowclick' : function(grid,row){  
				if(modifyAction.isHidden()){
					Ext.Msg.alert("温馨提醒","请先保存右边的人员列表！");
					route_time_grid.selModel.deselectRow(row);
					route_time_grid.selModel.selectRow(clickRow,true);
      		 		return false;
				}
				if(clickRow != row){
					clickRow = row;
					showAssignedPerson();
				}
			}
		},
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,
            store: route_time_store,
            displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
        })
    });
    var clickRow = -1;
    function showAssignedPerson(){
    	personTree.getTopToolbar().show();
    	disablePersonTree(true);
    	modifyAction.show();
    	saveAction.hide();
    	cancelAction.hide();
    	Ext.Msg.wait('获取已分配人员....', '提示');
    	Ext.Ajax.request({
       		url : 'mapposition/routemanage/routeassign/assigned-person.action',
			params : {routeTimeId:route_time_grid.getStore().getAt(clickRow).id},
            success : function(xhr) {
            	Ext.Msg.hide();
            	personRootNode.getUI().toggleCheck(false);//取消所有人员的选中状态
        		var responseText = Ext.decode(xhr.responseText);
            	var dataList = responseText.list;
            	for(var i=0;i<dataList.length;i++){
            		personTree.getNodeById(dataList[i].person_id).getUI().toggleCheck(true);
            	}
           	},
		    failure: function(xhr){
		    	Ext.Msg.hide();
		    	Ext.Msg.alert("提示","获取数据失败，稍后请重试！");
		    }
        });
    }
    
	var personRootNodeId = '0';
	var personRootNode = new Ext.tree.AsyncTreeNode({
		id : personRootNodeId,
		text : '所有人员',
		leaf : false,
		checked : false,
		draggable : false,
		expanded : true
	});
	var personTreeLoader = new Ext.tree.TreeLoader({
		dataUrl : 'mapposition/routemanage/routeassign/get-all-person.action'
	});
    personTreeLoader.on("load", function(loader, node) {
    	disablePersonTree(true);
    });
    var modifyAction = new Ext.Action({
		text:'修改',
		iconCls:'icon-edit',
		handler:function(){
			disablePersonTree(false);
			modifyAction.hide();
        	saveAction.show();
        	cancelAction.show();
		}
	});
    var saveAction = new Ext.Action({
		text:'保存',
		iconCls:'icon-save',
		hidden : true,
		handler:function(){
			var personIds = [];
			Ext.each(personTree.getChecked(), function(node) {
				if(node.isLeaf()){
					personIds.push(node.id);
				}
			});
			Ext.Msg.wait('保存数据中....', '提示');
		   	Ext.Ajax.request({
	       		url : 'mapposition/routemanage/routeassign/save-assign-person.action',
				params : {routeTimeId:route_time_grid.getStore().getAt(clickRow).id,personIds:personIds},
	            success : function(xhr) {
	            	Ext.Msg.hide(); 
	            	var obj = Ext.decode(xhr.responseText);
					if(obj.success){
						showAssignedPerson();
					}else{
						Ext.Msg.alert("温馨提醒","保存失败，稍后请重试！");
					}
				},
			    failure: function(xhr){
			    	Ext.Msg.hide(); 
			    	Ext.Msg.alert("温馨提醒","保存失败，稍后请重试！");
			    }
	        })
		}
	});
	var cancelAction = new Ext.Action({
		text:'取消',
		iconCls:'icon-cancel',
		hidden : true,
		handler:function(){
			Ext.Msg.confirm('确认', '确定要丢弃修改的数据？',
				function(btn, text) {
					if (btn == 'yes') {
						showAssignedPerson();
					}
			});
		}
	});
	function disablePersonTree(disable){
		if(disable){
			personRootNode.disable();
		}else{
			personRootNode.enable();
		}
		personRootNode.eachChild(function(current){
			if(disable){
				current.disable();
			}else{
				current.enable();
			}
		});
	}
    var personTree = new Ext.tree.TreePanel({
		title : '人员列表',
	    region : 'east',
		useArrows : true,
		autoScroll : true,
		enableDD : false,
		animate : true,
		width : 250,
//		collapsible : true,
		rootVisible : true, 
		containerScroll : true,
		loader : personTreeLoader,
		root : personRootNode,
		tbar:[modifyAction,saveAction,'-',cancelAction],
		plugins:[
  	        new Ext.plugin.tree.TreeNodeChecked({
 	        	cascadeCheck: true,
 	          	cascadeParent: false,
 	          	cascadeChild: true,
  	          	linkedCheck: false, 
  	          	asyncCheck: false, 
 	          	displayAllCheckbox: false
	        })
 	    ],
 	    listeners:{    
      		 'afterrender':function(){
      		 	personTree.getTopToolbar().hide();
      		 }
      	} 
	});
	var mainPanel = new Ext.Panel({
    	header : false,
		title : '路线分配',
		border : false,
		layout : 'border',
		items : [tree,route_time_grid,personTree]
	});
    return mainPanel;
})();