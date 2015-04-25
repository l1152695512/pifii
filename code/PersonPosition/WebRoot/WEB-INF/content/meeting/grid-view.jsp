<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var tabTitle = "会议预约";
	var gridDataUrl= 'meeting/grid-data.action';
	
	var storeFields=[
		'DATE','ONE','TWO','THREE','FOUR','FIVE','SIX','SEVEN','EIGHT'
	];
	
	var setStyle=function(v,cellmeta){
		if(v=='空闲'){
			return '<font color=#cffbd4>'+v+'</font>';
		}else{
			return '<font color=red>'+v+'</font>';
		}
	}
	
	var gridColumns=[{
			header:'时间段',
			dataIndex:'DATE',
			align:'center',
			sortable:true
		},{
			header:'1号会议室',
			dataIndex:'ONE',
			align:'center',
			sortable:true,
			renderer:setStyle
		},{
			header:'2号会议室',
			dataIndex:'TWO',
			align:'center',
			sortable:true,
			renderer:setStyle
		},{
			header:'3号会议室',
			dataIndex:'THREE',
			align:'center',
			sortable:true,
			renderer:setStyle
		},{
			header:'4号会议室',
			dataIndex:'FOUR',
			align:'center',
			sortable:true,
			renderer:setStyle
		},{
			header:'5号会议室',
			dataIndex:'FIVE',
			align:'center',
			sortable:true,
			renderer:setStyle
		},{
			header:'6号会议室',
			dataIndex:'SIX',
			align:'center',
			sortable:true,
			renderer:setStyle
		}];
	
	var store=new Ext.data.JsonStore({
		url:gridDataUrl,
		fields:storeFields,
		root:'list',
		idProperty: 'DATE',
		totalProperty:'totalRecord',
		autoLoad:true,
		remoteSort: true
	});	
	
	var queLabel=new Ext.form.Label({
		text:'  预约日期：'
	});
	
	var queInfo=new Ext.form.DateField({
		name : 'date',	
		width:90,
		format:'Y-m-d',
		editable:false,
		value:new Date()
	});
	
	var queAction=new Ext.Action({
		text : '查询',
		iconCls : 'icon-query',
		handler : function() {
			var info=queInfo.getValue();
			store.setBaseParam('queryInfo',info);
			store.reload();
		}
	});
	
	var gridListeners={
		cellclick:function(grid, rowIndex, columnIndex, e){	
			if(columnIndex!=0 && columnIndex<=6){
				var rec=grid.store.getAt(rowIndex);
				var va='';
				if(columnIndex==1){
					va='ONE';
				}else if(columnIndex==2){
					va='TWO';
				}else if(columnIndex==3){
					va='THREE';
				}else if(columnIndex==4){
					va='FOUR';
				}else if(columnIndex==5){
					va='FIVE';
				}else if(columnIndex==6){
					va='SIX';
				}
				var temp=rec.get(va);
				if(temp=='空闲'){
					Ext.Msg.alert('提示',columnIndex+'号会议室，在时间段['+rec.id+']，处于空闲状态');
				}
				else{
					Ext.Ajax.request({
						url:'meeting/detail-view.action',
						params:{userName:temp,
							date1:queInfo.getValue(),
							date2:rec.id
						},
						success:function(xhr){
							var newForm=eval(xhr.responseText);
							if(newForm==undefined||newForm=='undefined')return;
							var newWin=new Ext.Window({
								title:'预约详情',
								width: 1000,
						        height:100,
						        modal :true,
						        layout: 'fit',
						        plain:true,
						        constrain: true,
						        closable: true,
						        bodyStyle:'padding:5px;',
						        buttonAlign:'center',
						        items: [newForm]
		    				});
		    				newWin.show();
						}					
					});
				}
			}
		} 
	};
	
	store.setDefaultSort('DATE', 'desc');
	
	var grid=new Ext.grid.GridPanel({
		columns:gridColumns,
		store:store,
		tbar:[queLabel,queInfo,'&nbsp;&nbsp;',queAction],
		stripeRows:true,
		disableSelection: true,
		loadMask: true,
		columnLines:true,
		listeners:gridListeners,
		trackMouseOver:true,
		viewConfig:{
			forceFit:true
		},
		ref:'reloadOnActivate'
	});
	
	grid.getStore().on('load',function(s,records){
        var girdcount=0;
        s.each(function(r){
            if(r.get('ONE')!='空闲'){
                grid.getView().getCell(girdcount,1).style.backgroundColor='#eccc6a';
            }else{
            	grid.getView().getCell(girdcount,1).style.backgroundColor='#cffbd4';
            }
            if(r.get('TWO')!='空闲'){
                grid.getView().getCell(girdcount,2).style.backgroundColor='#eccc6a';
            }else{
            	grid.getView().getCell(girdcount,2).style.backgroundColor='#cffbd4';
            }
			if(r.get('THREE')!='空闲'){
                grid.getView().getCell(girdcount,3).style.backgroundColor='#eccc6a';
            }else{
            	grid.getView().getCell(girdcount,3).style.backgroundColor='#cffbd4';
            }
            if(r.get('FOUR')!='空闲'){
                grid.getView().getCell(girdcount,4).style.backgroundColor='#eccc6a';
            }else{
            	grid.getView().getCell(girdcount,4).style.backgroundColor='#cffbd4';
            }
            if(r.get('FIVE')!='空闲'){
                grid.getView().getCell(girdcount,5).style.backgroundColor='#eccc6a';
            }else{
            	grid.getView().getCell(girdcount,5).style.backgroundColor='#cffbd4';
            }
            if(r.get('SIX')!='空闲'){
                grid.getView().getCell(girdcount,6).style.backgroundColor='#eccc6a';
            }else{
            	grid.getView().getCell(girdcount,6).style.backgroundColor='#cffbd4';
            }
            girdcount=girdcount+1;
        });
    });
	
	return grid;
})();