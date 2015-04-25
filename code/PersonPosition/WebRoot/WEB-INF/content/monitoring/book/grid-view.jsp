<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 20;
	var gridDataUrl = "sys/usergroup/grid-data.action";
	var rowId = 'sugid';
	var storeFields = [
            'name','remards',
            {name: 'createTime', type: 'date', dateFormat: 'Y-m-d'}
        ];   	
    var rowaction=	new Ext.ux.grid.RowActions({
		 header:'操作',
		 width:150,
		 autoWidth:false,
		 keepSelection:true,
		 actions:[{
			iconCls:'movies1-icon',
			tooltip:'会议监控',
			text: '会议监控',
			callback:function(grid, record, action, row, col){
				if((row?row%2?"奇数":"偶数":"0") == "偶数"){
					alert("时间还未到，请稍后！");
				}else{
	          		Ext.Ajax.request({
		    		   	url:'monitoring/monitor-view.action',
		    			params: {sugid:record.id,name:record.data.name},
		    			success:function(xhr){
		    				var win1 = eval(xhr.responseText);
		    				win1.show();
		    			}
		    		});
		    	}
			}
		},{
			iconCls:'movies2-icon',
			tooltip:'视频直播',
			text:'视频直播',
			callback:function(grid, record, action, row, col){
				if((row?row%2?"奇数":"偶数":"0") == "偶数"){
					alert("时间还未到，请稍后！");
				}else{
					Ext.Ajax.request({
		    			url:'monitoring/video-view.action',
		    			params: {sugid:record.id,name:record.data.name},
		    			success:function(xhr){
		    				var win2 = eval(xhr.responseText);
		    				win2.show();
		    			}
		    		});
		    	}
			}
		},{
			iconCls:'back-icon',
			tooltip:'视频回放',
			text:'视频回放',
			callback:function(grid, record, action, row, col){
				if((row?row%2?"奇数":"偶数":"0") == "偶数"){
					alert("时间还未到，请稍后！");
				}else{
					Ext.Ajax.request({
		    			url:'monitoring/video-view.action',
		    			params: {sugid:record.id,name:record.data.name},
		    			success:function(xhr){
		    				var win2 = eval(xhr.responseText);
		    				win2.show();
		    			}
		    		});
		    	}
			}
		}]
		
	});
    var gridColumns = [
    	new Ext.grid.RowNumberer({
    		header:'序号',
    		align:'center',
    		width: 40,
    		renderer: function(value,metadata,record,rowIndex){
    			return store.lastOptions.params.start + 1 + rowIndex;
    		}
    	}),
    	{
            header: "会议室编号",
            dataIndex: 'name',
            align: 'center',
            width: 120,
            renderer: function(value,metadata,record,rowIndex){
            	return "第"+(rowIndex+1)+"会议室";
    		}
        },{
            header: "会议名称",
            dataIndex: 'name',
            align: 'center',
            width: 120,
            renderer: function(value,metadata,record,rowIndex){
            	return "会议名称"+(rowIndex+1);
    		}
        },{
            header: "状态",
            dataIndex: 'remards', 
            align: 'center',
            renderer: function(value,metadata,record,rowIndex){
            	if((rowIndex?rowIndex%2?"奇数":"偶数":"0") == "偶数"){
            		return "未开始";
            	}else{
    				return "进行中";
    			}
    		}
        },{
            header: "负责人",
            dataIndex: 'remards', 
            align: 'center',
            renderer: function(value,metadata,record,rowIndex){
    			return "负责人"+(rowIndex+1);
    		}
        },{
            header: "开始时间",
            dataIndex: 'createTime',
            align: 'center',
            width: 70,
            renderer:renderDateISOLong,
            sortable: true
        },{
            header: "结束时间",
            dataIndex: 'createTime',
            align: 'center',
            width: 70,
            renderer:renderDateISOLong,
            sortable: true
        },
        rowaction];


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
			    		url:"sys/usergroup/edit-form.action",
			    		params: { editId :rec.id },
			    		success:function(xhr){
			    			var editForm = eval(xhr.responseText);
			    			if(editForm==undefined||editForm=="undefined")return ;//没有权限	
			    			var editWin = new Ext.Window({
			    				title: "用户组"+rec.get('name')+"修改",
						        width: 400,
				                height:280,        
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
		html:'会议名称：'
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
        plugins:[rowaction],
		tbar: [t1,t2,'&nbsp;&nbsp;',q,'&nbsp;&nbsp;',r],
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,//pageSize 表示每页有几条记录,
            store: store,
            displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
        }),
        listeners:gridListeners
    });

    return grid;
})();