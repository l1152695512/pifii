<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 15;
	var rootNodeId = '0';
	var currentCommunityId;
	var rootNode = new Ext.tree.AsyncTreeNode({
		id : rootNodeId,
		text : '所有区域',
		leaf : false,
		draggable : false,
		expanded : true
	});
	var currentSelectNode = rootNode;
	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl : 'warnmanage/warnarea/get-tree-nodes.action'
	});
//	treeLoader.on("beforeload", function(loader, node) {
//        this.baseParams.nodeId = node.id;
//    });
    treeLoader.on("load", function(loader, node) {
    });
    var tree = new Ext.tree.TreePanel({
		title : '区域信息',
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
      		 	currentSelectNode = node;
      		 	var communityId;
      		 	if(node.isLeaf()){
      		 		communityId = node.parentNode.id;
      		 	}else{
      		 		communityId = node.id;
      		 	}
      		 	if(communityId != currentCommunityId){
      		 		personRootNode.removeAll(true);
      		 	}
      		 	personRootNode.getUI().toggleCheck(false);
      		 	personTree.getTopToolbar().hide();
      		 	disablePersonTree(true);
      			area_time_store.reload();
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
	var area_time_storeFields = ['id','community_id','community_name','area_name','name','area_type','start_time','end_time','is_used'];
	var area_time_store = new Ext.data.JsonStore({
        url: 'warnmanage/warnareaassign/area-times.action',
        root: 'list',
        autoLoad:{params:{start:0, limit:pageLimit}},
        totalProperty: 'totalRecord',
        idProperty: 'id',
        remoteSort: true,
        fields: area_time_storeFields
    });
	area_time_store.on('beforeload',function(store){ 
		clickRow = -1;
		var isArea;
		if(currentSelectNode.isLeaf()){
			isArea="1";
		}else{
			isArea="0";
		}
        store.baseParams = {id:currentSelectNode.id,isArea:isArea}; 
    });
    area_time_store.setDefaultSort('community_name', 'desc');
    var sm = new Ext.grid.CheckboxSelectionModel({
		checkOnly:false
	});
	var area_time_gridColumns = [sm,
    	{
            header: "所属小区",
            dataIndex: 'community_name',
            align: 'center',
            sortable:true,
            width: 90 
        },{
            header: "所属区域",
            dataIndex: 'area_name',
            align: 'center',
            sortable:true,
            width: 90 
        },{
            header: "类型",
            dataIndex: 'area_type',
            align: 'center',
            sortable:true,
            width: 70,
            renderer:function(value,metadata,record,rowIndex){
				if(value=='9'){
					return '禁止进入';
				}
				return '禁止离开';
    		}
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
        },{
            header: "描述",
            dataIndex: 'name',
            align: 'center',
            sortable:false,
            width: 90 
        }];
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
				show_area_time_panel(selectNode.id,'')
			}else{
				Ext.Msg.alert("提示","请先选择区域！");
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
        	var selected = area_time_grid.getSelectionModel().getSelections();
			var params = [];
			Ext.each(selected, function(item) {
				params.push(item.id);
			});
			if(params.length == 0){
		   		Ext.Msg.alert("提示","请先选择数据！");
		   		return;
		    }
			Ext.Msg.confirm('确认', '确定要删除选中的区域警告时间？',
				function(btn, text) {
					if (btn == 'yes') {
						Ext.Msg.wait('正在删除数据....', '提示');
						Ext.Ajax.request({
				       		url : 'warnmanage/warnareaassign/delete-area-time.action',
				       		params: {ids:params},
				            success : function(xhr) {
				            	Ext.Msg.hide(); 
				            	var obj = Ext.decode(xhr.responseText);
								if(obj.success){
									personRootNode.getUI().toggleCheck(false);
					      		 	personTree.getTopToolbar().hide();
					      		 	disablePersonTree(true);
					      		 	area_time_grid.selModel.deselectRow(clickRow);
					      		 	clickRow = -1;
									area_time_store.reload();
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
    function show_area_time_panel(area_id,id){
    	Ext.Msg.wait('获取数据中....', '提示');
		Ext.Ajax.request({
			url:'warnmanage/warnareaassign/add-or-modify-area-time.action',
			params: {areaId:area_id,id:id},
			success:function(xhr){
				Ext.Msg.hide(); 
				var areaTimeWin = eval(xhr.responseText);
				areaTimeWin.show();
			},
			failure:function(xhr){
				Ext.Msg.hide(); 
				Ext.Msg.alert('提示','暂时不能添加区域警告时间！');
			}
		});
    }
	var area_time_grid = new Ext.grid.GridPanel({
        store: area_time_store,
        region : 'center',
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        border : false,
        columns:area_time_gridColumns,
        viewConfig: {
            forceFit:true
        },
        sm: sm,
        tbar: [addTime,'-',deleteTime],
        listeners:{  
        	'celldblclick':function(grid, rowIndex, columnIndex, e){	
        		if(modifyAction.isHidden()){
					Ext.Msg.alert("温馨提醒","请先保存右边的人员列表！");
			 		return;
				}
				if(columnIndex!=0){
					var row_data = grid.getStore().getAt(rowIndex);
					var id = row_data.id;
					show_area_time_panel('',id);
				}
			},
			'cellclick' : function(grid, rowIndex, columnIndex, e){  
				if(modifyAction.isHidden()){
					Ext.Msg.alert("温馨提醒","请先保存右边的人员列表！");
					area_time_grid.selModel.deselectRow(rowIndex);
					area_time_grid.selModel.selectRow(clickRow,true);
      		 		return false;
				}
				if(columnIndex!=0 && clickRow != rowIndex){
					clickRow = rowIndex;
					loadPersonAndShowAssign();
				}
			}
		},
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,
            store: area_time_store,
            displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
        })
    });
    function loadPersonAndShowAssign(){
    	if(currentCommunityId != area_time_grid.getStore().getAt(clickRow).data.community_id || !personRootNode.hasChildNodes()){
    		personTreeLoader.load(personRootNode,function(node){
				showAssignedPerson();
			});
    	}else{
    		showAssignedPerson();
    	}
    }
    var clickRow = -1;//仅用在点击了修改人员分配按钮后的方法里
    function showAssignedPerson(){
    	personTree.getTopToolbar().show();
    	disablePersonTree(true);
    	modifyAction.show();
    	saveAction.hide();
    	cancelAction.hide();
    	Ext.Ajax.request({
       		url : 'warnmanage/warnareaassign/assigned-person.action',
			params : {areaTimeId:area_time_grid.getStore().getAt(clickRow).id},
            success : function(xhr) {
            	personRootNode.getUI().toggleCheck(false);//取消所有人员的选中状态
        		var responseText = Ext.decode(xhr.responseText);
            	var dataList = responseText.list;
            	for(var i=0;i<dataList.length;i++){
            		personTree.getNodeById(dataList[i].person_id).getUI().toggleCheck(true);
            	}
           	},
		    failure: function(xhr){
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
		dataUrl : 'warnmanage/warnareaassign/get-community-person.action'
	});
    personTreeLoader.on("load", function(loader, node) {
    	disablePersonTree(true);
    	personRootNode.expand();
    });
    personTreeLoader.on("beforeload", function(loader, node) {
    	try{
    		currentCommunityId = area_time_grid.getStore().getAt(clickRow).data.community_id;
    	}catch(e){
    		currentCommunityId = rootNodeId;
    	}
        this.baseParams.communityId = currentCommunityId;
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
	       		url : 'warnmanage/warnareaassign/save-assign-person.action',
				params : {areaTimeId:area_time_grid.getStore().getAt(clickRow).id,personIds:personIds},
	            success : function(xhr) {
	            	Ext.Msg.hide(); 
	            	var obj = Ext.decode(xhr.responseText);
					if(obj.success){
						loadPersonAndShowAssign();
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
						loadPersonAndShowAssign();
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
		title : '区域分配',
		border : false,
		layout : 'border',
		items : [tree,area_time_grid,personTree]
	});
    return mainPanel;
})();