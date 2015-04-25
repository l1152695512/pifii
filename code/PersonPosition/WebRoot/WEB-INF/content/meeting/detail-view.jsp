<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var tabTitle = "预约详情";
	var gridDataUrl= 'meeting/detail-data.action';
	var storeFields=[
		'M_ID','CHARGE','M_TYPE','M_NAME','M_ROOM','ORDER_TIME','START_TIME','END_TIME','PROJECT','COMPANY','JOIN','W_JOIN','MEMO'
	];
	
	var gridColumns=[
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
			header:'负责人',
			dataIndex:'CHARGE',
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
			width:165
		},{
			header:'开始时间',
			dataIndex:'START_TIME',
			align:'center',
			sortable:true,
			width:165
		},{
			header:'结束时间',
			dataIndex:'END_TIME',
			align:'center',
			sortable:true,
			width:165
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
				userName:'${userName}',
				date1:'${date1}',
				date2:'${date2}'
			}
		},
		remoteSort: true
	});
	
	store.setDefaultSort('M_ID', 'desc');
	
	var grid=new Ext.grid.GridPanel({
		columns:gridColumns,
		store:store,
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



