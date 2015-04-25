<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 20;
	var gridDataUrl = "setting/company/grid-data.action";
	var newFormUrl = "setting/company/new-form.action";
	var rowId = 'cid';
	var storeFields = [
            'cname','ywRegion','typeName','cid'
        ];
        
    var tabTitle = "分公司/二级单位管理";
    var sm = new Ext.grid.CheckboxSelectionModel({checkOnly:true});//复选框
    var gridColumns = [sm,
    	new Ext.grid.RowNumberer({
    		header:'序号',
    		align:'center',
    		width: 40,
    		renderer: function(value,metadata,record,rowIndex){
    			return store.lastOptions.params.start + 1 + rowIndex;
    		}
    	}),
    	{
            header: "名称",
            dataIndex: 'cname',
            align: 'center',
            sortable: true
        },{
            header: "所在地区",
            dataIndex: 'ywRegion', 
            align: 'center',
            sortable: true
        },{
            header: "类型",
            dataIndex: 'typeName',
            align: 'center',
            sortable: true
        }
        ];
    //添加按钮
    var action = new Ext.Action(Ext.apply({
    	text: '添加',
    	iconCls :'icon-add',
    	handler: function(){
    		Ext.Ajax.request({
    			url:newFormUrl,
    			success:function(xhr){
    				var newForm = eval(xhr.responseText);
    				if(newForm==undefined||newForm=="undefined")return ;//没有权限	
    				var newWin = new Ext.Window({
    					title: '添加新分公司/二级单位',
				        width: 350,
				        height:250,  
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
    var delAction =  new Ext.Action({
        text: '删除',
        iconCls :'icon-delete',
        handler: function(){
            var selected = grid.getSelectionModel().getSelections();
            var params = []
            Ext.each(selected,function(item){
            	params.push(item.id);
            });
            var reloadFn = function(xhr){
			    store.load(store.lastOptions);
			   };
			  var reloadFn2 = function(xhr){
	
			    store.load(store.lastOptions);
			   };
			if(params.length == 0){
		   		Ext.Msg.alert("提示","请选择要删除的数据！");
		   		return;
		    }else{
	           Ext.Ajax.request({
				   url: 'setting/company/delete-data.action',
				   success: reloadFn,
				   failure: reloadFn2,
				   params: { ids: params }
				});
			}
        }
    });
    // 数据存储
    var store = new Ext.data.JsonStore({
        root: 'list',
        autoLoad:{params:{start:0, limit:pageLimit}},
        totalProperty: 'totalRecord',
        idProperty: rowId,
        remoteSort: true,
        fields: storeFields,
        url: gridDataUrl
       
    });
    //grid事件定义
    var gridListeners = {
        	cellclick:function(grid, rowIndex, columnIndex, e) {
			   if(columnIndex != 0&&columnIndex<9){
			    	var rec = grid.store.getAt(rowIndex);
	       			Ext.Ajax.request({
			    		url:"setting/company/edit-form.action",
			    		params: { editId :rec.id  },
			    		success:function(xhr){
			    			var editForm = eval(xhr.responseText);
			    		    if(editForm==undefined||editForm=="undefined")return ;//没有权限	
			    			var editWin = new Ext.Window({
			    				title: rec.get('cname')+"资料修改",
						        width: 350,
				                height:250,        
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
    var  t1=new Ext.form.Label({
		html:'名称：'
	});
	var t2=new Ext.form.TextField({				
						xtype : 'textfield',
						name : 'cname',	
						width:65				
	});
	
	var q = new Ext.Action({
		text : '查询',
		iconCls : 'icon-find',
		handler : function() {
			 var cname=t2.getValue();
			 store.setBaseParam("cname",cname);
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
    store.setDefaultSort('cid', 'desc');
    var grid = new Ext.grid.GridPanel({
        title:tabTitle,
        store: store,
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        columns:gridColumns,
        viewConfig: {
            forceFit:true
        },		
		tbar: [action,'-',delAction,'&nbsp;&nbsp;&nbsp;&nbsp;',t1,t2,q,'&nbsp;&nbsp;',r],
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,//pageSize 表示每页有几条记录,
            store: store,
            displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
        }),  
        sm: sm,
        listeners:gridListeners
    });

    return grid;
})();