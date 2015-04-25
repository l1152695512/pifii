<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 20;
	var gridDataUrl = "sys/usergroup/menus/grid-data.action";
	var newFormUrl = "sys/usergroup/menus/select-view.action";
	var rowId = 'sumId';
	var storeFields = [
            'parentMenuText','menuText'
        ];
                
    var tabTitle = '${name}'+"->菜单列表";
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
            header: "菜单名称",
            dataIndex: 'menuText',
            align: 'center',
            width: 120,
            sortable: true
        },
    	{
            header: "上级菜单",
            dataIndex: 'parentMenuText',
            align: 'center',
            width: 120,
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
    			params: { sugid:'${sugid}'},
    			success:function(xhr){
    				var newForm = eval(xhr.responseText);
    				if(newForm==undefined||newForm=="undefined")return ;//没有权限	
    				var newWin = new Ext.Window({
				        width: 600,
                        height: 500,
				        modal :true,
				        layout: 'fit',
				        plain:true,
				        constrain: true,
				        closable: false,
				        bodyStyle:'padding:5px;',
				        buttonAlign:'center',
				        items: [newForm],       
				        buttons: [{
				            text: '关闭',
				            handler:function(){
				            	newWin.close();
				                store.load(store.lastOptions);			            
				            }
				        }]
    				});
    				
    				newWin.show();
    				
    			}
    		});
    	}
    }));
    var delAction =  new Ext.Action({
        text: '移去',
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
		   		Ext.Msg.alert("提示","请选择要移去的用户！");
		   		return;
		    }else{
	           Ext.Ajax.request({
				   url: 'sys/usergroup/menus/delete-data.action',
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
     store.on('beforeload',function(store){ 
        store.baseParams = {sugid:'${sugid}'}; }
    );
    
    //grid事件定义
    var gridListeners = {
        	cellclick:function(grid, rowIndex, columnIndex, e) {
        	  //永远不执行
			   if(columnIndex < 0){
			    	var rec = grid.store.getAt(rowIndex);
	       			Ext.Ajax.request({
			    		url:"sys/usergroup/menus/edit-form.action",
			    		params: { editId :rec.id },
			    		success:function(xhr){
			    			var editForm = eval(xhr.responseText);
			    			if(editForm==undefined||editForm=="undefined")return ;//没有权限	
			    			var editWin = new Ext.Window({
			    				title: "权限Action"+rec.get('name')+"修改",
						  		width: 380,
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
						name : 'name',	
						width:80				
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
        
    store.setDefaultSort('sumId', 'desc');
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
		//tbar: [action,'-',delAction],
		tbar: [action,'-',delAction,'-',t1,t2,'&nbsp;&nbsp;',q,'&nbsp;&nbsp;',r],
        // paging bar on the bottom
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,//pageSize 表示每页有几条记录,
            sugid:'${sugid}',
            store: store,
            displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
        }),
       
        sm: sm,
        listeners:gridListeners
    });
    var win = new Ext.Window({
        title: '',
        width: 600,
        height: 500,
        modal :true,
        layout: 'fit',
        plain:true,
        constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        items: grid
    });
    return win;
})();