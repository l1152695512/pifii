<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){

	var pageLimit = 20;
	var gridDataUrl = "setting/usergroup/grid-data.action";
	var newFormUrl = "setting/usergroup/new-form.action";
	var rowId = 'sugid';
	var storeFields = [
            'name','remards','companyName',
            {name: 'createTime', type: 'date', dateFormat: 'Y-m-d'}
        ];   	
	    var rowaction=	new Ext.ux.grid.RowActions({
			 header:'操作',
			 width:100,
			 align:'center',
			 autoWidth:false,
			 keepSelection:true,
			 actions:[{
				iconCls:'icon-checkrvinfo',
				tooltip:'管理该用户组用户',
				text:'用户设置',
				callback:function(grid, record, action, row, col){
					var reloadFn = function(){
					    store.load(store.lastOptions);
					   };
           			Ext.Ajax.request({
		    			url:'setting/usergroup/user/grid-view.action',
		    			params: {sugid:record.id,name:record.data.name},
		    			success:function(xhr){
		    				var win2 = eval(xhr.responseText);
		    				win2.show();
		    			}
		    		});
				}
			},{
				iconCls:'icon-perorasse',
				tooltip:'管理该用户组菜单项',
				text:'菜单管理',
				callback:function(grid, record, action, row, col){
					var reloadFn = function(){
						alert("test");
					    store.load(store.lastOptions);
					   };
           			Ext.Ajax.request({
		    			url:'setting/usergroup/menus/grid-view.action',
		    			params: {sugid:record.id,name:record.data.name},
		    			success:function(xhr){
		    				var win= eval(xhr.responseText);
		    				win.show();
		    			}
		    		});
				}
			}]
			
		});
    var tabTitle = "用户组管理";
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
            header: "用户组名称",
            dataIndex: 'name',
            align: 'center',
            width: 120,
            sortable: true
        },  
        {
            header: "所属分公司或二级单位",
            dataIndex: 'companyName',
            align: 'center',
            width: 120,
            sortable: true
        },
        {
            header: "备注",
            dataIndex: 'remards', 
            align: 'center',
            width: 150,
            sortable: true
        },{
            header: "创建时间",
            dataIndex: 'createTime',
            align: 'center',
            width: 70,
            renderer:renderDateISOShort,
            sortable: true
        },
        rowaction];
    //添加按钮
    var action = new Ext.Action(Ext.apply({
    	text: '添加',
    	iconCls :'icon-add',
    	handler: function(){
    		Ext.Ajax.request({
    			url:newFormUrl,
    			success:function(xhr){
	    			if(xhr.responseText=="<script>alert('你没有此权限,请与管理员联系!');</script>"){
						Ext.Msg.alert("警告","你没有此权限,请与管理员联系!");
			 	 		return ;
			 	 	}	
    				var newForm = eval(xhr.responseText);
    				if(newForm==undefined||newForm=="undefined")return ;//没有权限	
    				var newWin = new Ext.Window({
    					title: '添加新用户组',
				        width: 400,
				        height:300,
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
            	try{
            		var result = Ext.decode(xhr.responseText);
            		if(result.msg)Ext.Msg.alert("警告",result.msg);
            	}catch(e){
            		alert(e.description);
            	}
			    store.load(store.lastOptions);
			   };
			if(params.length == 0){
		   		Ext.Msg.alert("提示","请选择要删除的数据！");
		   		return;
		    }else{
	           Ext.Ajax.request({
				   url: 'setting/usergroup/delete-data.action',
				   success: reloadFn,
				   failure: reloadFn,
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
			   if(columnIndex != 0&&columnIndex<5){
			    	var rec = grid.store.getAt(rowIndex);
	       			Ext.Ajax.request({
			    		url:"setting/usergroup/edit-form.action",
			    		params: { editId :rec.id },
			    		success:function(xhr){
			    			var editForm = eval(xhr.responseText);
			    			if(editForm==undefined||editForm=="undefined")return ;//没有权限	
			    			var editWin = new Ext.Window({
			    				title: "用户组"+rec.get('name')+"修改",
						        width: 400,
				                height:300,        
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
		html:'用户组名称：'
	});
	var t2=new Ext.form.TextField({				
						xtype : 'textfield',
						name : 'name',	
						width:100				
	});

	var q = new Ext.Action({
		text : '查询',
		iconCls : 'icon-find',
		handler : function() {
			 var name=t2.getValue();
			 store.setBaseParam("name",name);
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
    
    store.setDefaultSort('sugid', 'desc');
	
    var grid = new Ext.grid.GridPanel({
        title:tabTitle,
        store: store,
        //id:'userListGrid',
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        // grid columns
        columns:gridColumns,
        // customize view config
        viewConfig: {
            forceFit:true
        },
        plugins:[rowaction],
		tbar: [action,'-',delAction,'-',t1,t2,'&nbsp;&nbsp;',q,'&nbsp;&nbsp;',r],
        // paging bar on the bottom
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