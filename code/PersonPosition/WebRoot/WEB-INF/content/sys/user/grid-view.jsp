<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 20;
	var gridDataUrl = "sys/user/grid-data.action";
	var newFormUrl = "sys/user/new-form.action";
	var rowId = 'userId';
	var storeFields = [
            'accountName','userName','cellPhone','telNum','email','loginCount',
            {name: 'lastLogin', type: 'date', dateFormat: 'Y-m-d H:i:s'},
            {name: 'createTime', type: 'date', dateFormat: 'Y-m-d'},'description'
        ];
       var rowaction=	new Ext.ux.grid.RowActions({
			 header:'操作',
			 width:150,
			 autoWidth:false,
			 keepSelection:true,
			 actions:[{
				iconCls:'icon-save',
				tooltip:'管理该用户所属用户组',
				text: '用户组设置',
				callback:function(grid, record, action, row, col){
					var reloadFn = function(){
					    store.load(store.lastOptions);
					   };
					 var id=record.id;
					
					 var accountName=record.data.accountName;
           			Ext.Ajax.request({
		    		    url:'sys/user/usergroup/grid-view.action',
		    			params: {userId:id,accountName:accountName},
		    			success:function(xhr){
		    				var win1 = eval(xhr.responseText);
		    				if(win1==undefined||win1=="undefined"){
		    				haveNoJurisdictiion();
		    				return ;//没有权限
		    				}
		    				win1.show();
		    			}
		    		});
				}
			}]
			
		});
		
    var tabTitle = "用户管理";
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
            itemId: 'accountName', 
            header: "账号",
            dataIndex: 'accountName',
            align: 'center',
            width: 100,
            sortable: true
        },{
        	itemId: 'userName',
            header: "姓名",
            dataIndex: 'userName', 
            align: 'center',
            width: 70,
            sortable: true
        },{
            header: "登录次数",
            dataIndex: 'loginCount', 
            align: 'center',
            width: 70,
            sortable: true
        },{
            itemId: 'cellPhone',
            header: "手机",
            dataIndex: 'cellPhone',
            align: 'center',
            width: 70,
            sortable: true
        },{
            itemId: 'telNum',
            header: "固话",
            dataIndex: 'telNum',
            align: 'center',
            width: 70,
            sortable: true
        },{
            itemId: 'email',
            header: "电子邮件",
            dataIndex: 'email',
            align: 'center',
            width: 50,
            sortable: true
        },{
            itemId: 'lastLogin',
            header: "最后登录",
            dataIndex: 'lastLogin',
            align: 'center',
            width: 100,
            renderer:renderDateISOLong,
            sortable: true
        },{
            itemId: 'createTime',
            header: "创建时间",
            dataIndex: 'createTime',
            align: 'center',
            width: 100,
            renderer:renderDateISOShort,
            sortable: true
        },rowaction];
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
    					title: '添加新用户',
				        width: 400,
				        height:380,
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
				   url: 'sys/user/delete-data.action',
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
			    		url:"sys/user/edit-form.action",
			    		params: { editId :rec.id  },
			    		success:function(xhr){
			    			var editForm = eval(xhr.responseText);
			    		    if(editForm==undefined||editForm=="undefined")return ;//没有权限	
			    			var editWin = new Ext.Window({
			    				title: "用户"+rec.get('accountName')+"资料修改",
						        width: 400,
				                height:380,        
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
		html:'帐号：'
	});
	var t2=new Ext.form.TextField({				
						xtype : 'textfield',
						name : 'accountName',	
						width:65				
	});
    var  t3=new Ext.form.Label({
		html:'姓名：'
	});
	var t4=new Ext.form.TextField({				
						xtype : 'textfield',
						name : 'userName',	
						width:65		
	});
	var q = new Ext.Action({
		text : '查询',
		iconCls : 'icon-find',
		handler : function() {
			 var accountName=t2.getValue();
			 var userName=t4.getValue();
			 store.setBaseParam("accountName",accountName);
			 store.setBaseParam("userName",userName);
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
    
    store.setDefaultSort('userId', 'desc');
	
    var grid = new Ext.grid.GridPanel({
        //title:tabTitle,
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

		
		tbar: [action,'-',delAction,'&nbsp;&nbsp;&nbsp;&nbsp;',t1,t2,'&nbsp;&nbsp;',t3,t4,'&nbsp;&nbsp;',q,'&nbsp;&nbsp;',r],
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