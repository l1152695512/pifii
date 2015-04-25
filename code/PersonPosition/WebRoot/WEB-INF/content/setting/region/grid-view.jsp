<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>(function() {

	var rootId = 'rootId';
	var pid='rootId';
	var pname='无父区域';

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
		// auto create TreeLoader
		loader : treeLoader,
		root : rootNode
	});
	// 右边Grid
	var pageLimit = 20;
	var gridDataUrl = "setting/region/grid-data.action";
	var newFormUrl = "setting/region/new-form.action";
	var rowId = 'rid';
	var storeFields = [ 'parent', 'name','code'];
	var tabTitle = "区域列表";
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
	}), {
		header : "所属区域",
		dataIndex : 'parent'
	},  {
		header : "区域名称",
		dataIndex : 'name'
	},
	 {
		header : "区域编码",
		dataIndex : 'code'
	}
	];
	// 添加按钮
     var gridAddAction = new Ext.Action(Ext.apply({
    	text: '添加',
    	iconCls :'icon-add',
    	handler: function(){
    		Ext.Ajax.request({
    			url:newFormUrl,
    			params:{pid:pid,pname:pname},
    			success:function(xhr){
    				var newForm = eval(xhr.responseText);
    				var newWin = new Ext.Window({
    					title: '添加新区域',
				        width: 300,
				        height:180,   
				        modal :true,
				        layout: 'fit',
				        plain:true,
				        constrain: true,
				        closable: true,
				        bodyStyle:'padding:5px;',
				        buttonAlign:'center',
				        items: [newForm],       
				        buttons: [{
				            text: '保存',
				            handler:function(){
				            	newForm.getForm().submit({
				            		success:function(form,action){
				            			store.load(store.lastOptions);
				            			var task = new Ext.util.DelayedTask(function(){
										treeLoader.load(rootNode,function(node){
												node.expand();
											});
										});
										task.delay(200);
				            			newWin.close();
				            		},
				            		failure:function(form,action){
				                    	if(action.result){
						            		Ext.Msg.alert("保存失败",action.result.msg);
						            	}
				                    }
				            	});
				            }
				        },{
				            text: '重置',
				            handler:function(){
				            	newForm.getForm().reset();
				            }
				        },{
				            text: '取消',
				            handler:function(){
				            	newWin.close();
				            }
				        }]
    				});
    				newWin.show();
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
				store.load(store.lastOptions);
				var task = new Ext.util.DelayedTask(function(){
						treeLoader.load(rootNode,function(node){
								node.expand();
							});
						});
			  task.delay(200);
			};
			Ext.Ajax.request({
				url : 'setting/region/delete-data.action',
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
			   if(columnIndex != 0&&columnIndex<9){
			    	var rec = grid.store.getAt(rowIndex);
	       			Ext.Ajax.request({
			    	   url:"setting/region/edit-form.action",
			    		params: { editId :rec.id  },
			    		success:function(xhr){
			    			var editForm = eval(xhr.responseText);
			    			var editWin = new Ext.Window({
			    				title: rec.get('name')+"资料修改",
						         width: 300,
				                height:180,    
						        modal :true,
						        layout: 'fit',
						        plain:true,
						        constrain: true,
						        closable: true,
						        bodyStyle:'padding:5px;',
						        buttonAlign:'center',
						        items: [editForm],
						        buttons: [{
						            text: '保存',
						            handler:function(){
						            	editForm.getForm().submit({
						            		success:function(form,action){
						            			store.load(store.lastOptions);
						            			editWin.close();
						            			var task = new Ext.util.DelayedTask(function(){
												treeLoader.load(rootNode,function(node){
														node.expand();
													});
												});
											  task.delay(200);
						            		},
						            		failure:function(form,action){
						                    	if(action.result){
								            		Ext.Msg.alert("保存失败",action.result.msg);
								            	}
						                    }
						            	});
						            }
						        },{
						            text: '取消',
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
		html : ' &nbsp;&nbsp;名称:&nbsp;'

	});
	var t2 = new Ext.form.TextField({
		name : 'name',
		width : 65
	});
  
	var q = new Ext.Action({
		text : '查询',
		iconCls : 'icon-find',
		handler : function() {
			var name = t2.getValue();
			store.setBaseParam("name", name);
		     if(pid!=''&&pid!='rootId'){
    	      store.setBaseParam('pid','${pid}');
    	    }
	 
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
		tbar : [gridAddAction, '-', gridDelAction, '-', gridrefleshAction,'-',t1,t2,'&nbsp;',q,'&nbsp;&nbsp;',r],
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

	var actionCfg = {
		handler : function() {
			var sn = tree.selModel.selNode || {};
			Ext.Ajax.request({
				url : 'setting/region/type-pop-form.action',
				params : this.isEditSupply ? {
					supplyTypeId : (sn.id ? sn.id : '')
				} : {
					parentId : (sn.id ? sn.id : '')
				},
				success : function(xhr) {
					// try{
					var formPanel = eval(xhr.responseText);
					var win = new Ext.Window({
						layout : 'fit',
						width : 500,
						height : 300,
						modal : true,
						constrain:true,
						plain : true,
						title : '供应商类别',
						closable : true,
						items : [formPanel],

						buttons : [{
							text : '保存',
							handler : function() {
								formPanel.getForm().submit({
									success : function(form, action) {
										treeLoader.load(rootNode,
												function(node) {
													node.expand();
												});
										win.close();
									},
									failure : function(form, action) {
										Ext.Msg
												.alert("保存失败",
														action.result.msg);
									}
								});
							}
						}]
					});
					win.show(this.el);
					// }catch(e){
					// alert(e.description);
					// }
				}
			})

		}
	};

	var addAction = new Ext.Action(Ext.apply({
		text : '增加类别',
		isEditSupply : false
	}, actionCfg));
	var editAction = new Ext.Action(Ext.apply({
		text : '修改类别',
		isEditSupply : true,
		disabled : true
	}, actionCfg));
	var delAction = new Ext.Action({
		text : '删除类别',
		handler : function() {
			var sn = tree.selModel.selNode || {};
			if (sn.id && sn.id != rootId) {
				Ext.Msg.confirm('确认', '是否确定删除“' + sn.text + '”及其子分类？',
						function(btn, text) {
							if (btn == 'yes') {
								var reloadFn = function() {
									treeLoader.load(rootNode, function(node) {
										node.expand();
									});
								};
								Ext.Ajax.request({
									url : 'basic/supplytype/delete-data.action',
									success : reloadFn,
									params : {
										ids : sn.id
									}
								});
							}
						});
			}
		}
	});
	tree.on('click', function(node) {
		if (node.id == rootId) {
		     pid=node.id;
		     pname='无父区域';
			//editAction.disable();
		} else {
			//editAction.enable();
			 pid=node.id;
		     pname=node.text;
		}	
		store.setBaseParam("pid", pid);
		store.load({
			params : {
				start : 0,
				limit : pageLimit
			}
		});
	});
	var panel = new Ext.Panel({
		layout : 'border',
		margins : '2 5 5 0',
		border : false,
		items : [tree, grid]
	});

    return panel;
})();