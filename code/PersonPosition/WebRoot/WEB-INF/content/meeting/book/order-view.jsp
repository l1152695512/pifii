<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var tabTitle = "我的预约";
	var pageLimit=20;
	var gridDataUrl= 'meeting/book/order-data.action';
	var storeFields=[
		'M_ID','M_TYPE','M_NAME','M_ROOM','ORDER_TIME','START_TIME','END_TIME','PROJECT','COMPANY','JOIN','W_JOIN','MEMO'
	];
	
	var selModel= new Ext.grid.CheckboxSelectionModel({
		checkOnly:true
	});
	
	var gridColumns=[
		selModel,
		new Ext.grid.RowNumberer({
			header:'序号',
			align:'center',
			width:40,
			renderer:function(value,metadata,record,rowIndex){
    			return store.lastOptions.params.start + 1 + rowIndex;
    		}
		}),
		{
			header:'会议类型',
			dataIndex:'M_TYPE',
			align:'center',
			sortable:true	
		},{
			header:'会议名称',
			dataIndex:'M_NAME',
			align:'center',
			sortable:true
		},{
			header:'会议室',
			dataIndex:'M_ROOM',
			align:'center',
			sortable:true
		},{
			header:'预约时间',
			dataIndex:'ORDER_TIME',
			align:'center',
			sortable:true,
			width:160
		},{
			header:'开始时间',
			dataIndex:'START_TIME',
			align:'center',
			sortable:true,
			width:160
		},{
			header:'结束时间',
			dataIndex:'END_TIME',
			align:'center',
			sortable:true,
			width:160
		},{
			header:'招标项目',
			dataIndex:'PROJECT',
			align:'center',
			sortable:true
		},{
			header:'招标单位',
			dataIndex:'COMPANY',
			align:'center',
			sortable:true
		},{
			header:'参与人',
			dataIndex:'JOIN',
			align:'center',
			sortable:true
		},{
			header:'外部人员',
			dataIndex:'W_JOIN',
			align:'center',
			sortable:true
		},{
			header:'会议描述',
			dataIndex:'MEMO',
			align:'center',
			sortable:true
		}];
	
	var store=new Ext.data.JsonStore({
		url:gridDataUrl,
		fields:storeFields,
		root:'list',
		idProperty: 'M_ID',
		totalProperty:'totalRecord',
		autoLoad:{
			params:{
				start:0,
				limit:pageLimit,
				userName:'${userName}'
			}
		},
		remoteSort: true
	});
	
	var parseNum=function(str){
		var a=str.split(' ');
		var b=a[0];
		var c=a[1];
		var d=b.split('-');
		var e=c.split(':');
		var result=d[0]+d[1]+d[2]+e[0]+e[1]+e[2];
		return parseInt(result);
	}
	
	var delAction=new Ext.Action({
		text:'撤销',
		iconCls:'icon-undo',
		handler:function(){
			var selected=grid.getSelectionModel().getSelections();
			var params=[];
			var dates=[];
			var flag='000';
			Ext.each(selected,function(item){
				params.push(item.id);
				var a=parseNum(item.get('START_TIME'));
				var c=Ext.util.Format.date(new Date(), 'Y-m-d H:i:s');
				var b=parseNum(c);
				if(b>a){
					flag='111';
				}				
			});
			if(params.length==0){
				Ext.Msg.alert('提示','请选择要撤销的预约！');
			}else{
				if(flag=='111'){
					Ext.Msg.alert('提示','请选择还未执行的预约进行撤销！');
				}else{
					Ext.MessageBox.confirm('提示', '是否撤销该条预约？', function(btn) {  
		                if (btn == 'yes') {  
		                	Ext.Ajax.request({
								url:'meeting/book/delete-data.action',
								params: { ids: params},
								success: function(form,action){
									store.load(store.lastOptions);
								},
							    failure: function(form,action){
							    	Ext.Msg.alert('提示','撤销失败！');
							    }
							});	                 
		                }  
		            }); 
				}
			}
		}
	});			
	
	var queLabel=new Ext.form.Label({
		text:'会议名称：'
	});
	
	var queInfo=new Ext.form.TextField({
		name : 'name',	
		width:100
	});
	
	var queAction=new Ext.Action({
		text : '查询',
		iconCls : 'icon-find',
		handler : function() {
			var name=queInfo.getValue();
			store.setBaseParam('queryInfo',name);
			store.reload();
		}
	});
	
	var cleAction=new Ext.Action({
		text:'清空',
		iconCls:'icon-redo',
		handler:function(){
			queInfo.setValue('');
		}
	});
	
	store.setDefaultSort('M_ID', 'desc');
	
	var gridListeners={
		cellclick:function(grid, rowIndex, columnIndex, e){
			if(columnIndex!=0 && columnIndex<=12){
				var rec=grid.store.getAt(rowIndex);
				var a=parseNum(rec.get('START_TIME'));
				var c=Ext.util.Format.date(new Date(), 'Y-m-d H:i:s');
				var b=parseNum(c);
				if(b>a){
					Ext.Msg.alert('提示','只能修改还未执行的预约的起始时间！');
				}else{
					Ext.Ajax.request({
						url:'meeting/book/edit-form.action',
						params:{editId:rec.id,
							mName:rec.get('M_NAME'),
							start:rec.get('START_TIME'),
							end:rec.get('END_TIME')
						},
						success:function(xhr){
							var editForm=eval(xhr.responseText);
							if(editForm==undefined||editForm=='undefined')return;
							var editWin=new Ext.Window({
								title:'修改预约起始时间',
								width:380,
								modal :true,
						        layout: 'fit',
						        plain:true,
						        constrain: true,
						        closable: true,
						        bodyStyle:'padding:5px;',
						        buttonAlign:'center',
						        items: [editForm]
			    			});
			    			editWin.show();
						}
					});
				}
			}
		} 
	};
	
	var grid=new Ext.grid.GridPanel({
		selModel:selModel,
		columns:gridColumns,
		store:store,
		tbar:[queLabel,queInfo,'&nbsp;&nbsp;',queAction,'-',cleAction,'-',delAction],
		bbar:new Ext.PagingToolbar({
			pageSize:pageLimit,
			store:store,
			displayInfo:true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: '空'
		}),
		listeners:gridListeners,
		stripeRows:true,
		disableSelection: true,
		loadMask: true,
		trackMouseOver:true,
		viewConfig:{
			forceFit:true
		},
		ref:'reloadOnActivate'
	});
	
    return grid;
})();



