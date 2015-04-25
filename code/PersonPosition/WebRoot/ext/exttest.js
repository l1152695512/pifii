
(function(){
	var pageLimit = 20;
	var gridDataUrl = "user/grid-data.action";
	var newFormUrl = "user/new-form.action";
	var rowId = 'userId';
	var storeFields = [
            'accountName','userName','cellPhone','telNum','email',
            {name: 'lastLogin', type: 'date', dateFormat: 'Y-m-d H:i:s'},
            {name: 'createTime', type: 'date', dateFormat: 'Y-m-d H:i:s'},'description'
        ];
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
            width: 100,
            sortable: true
        },{
        	itemId: 'userName',
            header: "姓名",
            dataIndex: 'userName', 
            width: 70,
            sortable: true
        },{
            itemId: 'cellPhone',
            header: "手机",
            dataIndex: 'cellPhone',
            width: 70,
            sortable: true
        },{
            itemId: 'telNum',
            header: "固话",
            dataIndex: 'telNum',
            width: 70,
            sortable: true
        },{
            itemId: 'email',
            header: "电子邮件",
            dataIndex: 'email',
            width: 50,
            sortable: true
        },{
            itemId: 'lastLogin',
            header: "最后登录",
            dataIndex: 'lastLogin',
            width: 100,
            renderer:renderDateISOLong,
            sortable: true
        },{
            itemId: 'createTime',
            header: "创建时间",
            dataIndex: 'createTime',
            width: 100,
            renderer:renderDateISOLong,
            sortable: true
        }];
    //添加按钮
    var action = new Ext.Action(Ext.apply({
    	text: '添加',
    	iconCls :'icon-reserv',
    	handler: function(){
    		Ext.Ajax.request({
    			url:newFormUrl,
    			success:function(xhr){
    				var newForm = eval(xhr.responseText);
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
            	try{
            		var result = Ext.decode(xhr.responseText);
            		if(result.msg)Ext.Msg.alert("警告",result.msg);
            	}catch(e){
            		alert(e.description);
            	}
			    store.load(store.lastOptions);
			   };
           Ext.Ajax.request({
			   url: 'user/delete-data.action',
			   success: reloadFn,
			   failure: reloadFn,
			   params: { ids: params }
			});
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
			   if(columnIndex != 0){
			    	var rec = grid.store.getAt(rowIndex);
	       			Ext.Ajax.request({
			    		url:"user/edit-form.action",
			    		params: { editId :rec.id  },
			    		success:function(xhr){
			    			var editForm = eval(xhr.responseText);
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
		width :'50',
		html:' &nbsp;帐号:&nbsp; '
	});
	var t2=new Ext.form.TextField({				
						xtype : 'textfield',
						name : 'accountName',	
						width:65				
	});
    var  t3=new Ext.form.Label({
		width :'50',
		html:' &nbsp;&nbsp;姓名:&nbsp;'
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
    
    store.setDefaultSort('createTime', 'desc');
	
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

		
		tbar: [action,'-',delAction,'-',t1,t2,t3,t4,q,r],
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