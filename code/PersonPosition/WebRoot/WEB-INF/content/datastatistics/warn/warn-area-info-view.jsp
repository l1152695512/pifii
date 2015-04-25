<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var warn_area_info_storeFields = ['start_time','end_time'];
	var warn_area_info_store = new Ext.data.JsonStore({
        root: 'list',
        autoLoad:{params:{personId:'${personId}',phone:'${phone}',areaId:'${areaId}',areaTimeId:'${areaTimeId}',date:'${date}',areaType:'${areaType}'}},
        totalProperty: 'totalRecord',
        idProperty: 'id',
        remoteSort: false,
        fields: warn_area_info_storeFields,
        url: 'datastatistics/warn/warn-area-info-data.action'
    });
    warn_area_info_store.setDefaultSort('start_time', 'asc');
	var warn_area_info_gridColumns = [
    	{
            header: "开始时间",
            dataIndex: 'start_time',
            sortable:true,
            align: 'center',
            width: 70
        },{
            header: "结束时间",
            dataIndex: 'end_time',
            sortable:true,
            align: 'center',
            width: 70
        },{
            header: "类型",
            dataIndex: 'type',
            align: 'center',
            sortable:false,
            width: 90,
            renderer: function(value,metadata,record,rowIndex){
            	if("9"=='${areaType}'){
            		return '<span style="color:red">进入了非法区域</span>';
            	}else{
            		return '<span style="color:red">离开了指定区域</span>';
            	}
    		}
//        },{
//            header: "姓名",
//            dataIndex: 'person_name',
//            sortable:true,
//            align: 'center',
//            width: 50,
//            renderer: function(value,metadata,record,rowIndex){
//            	return '${personName}';
//    		}
//        },{
//            header: "小区名称",
//            dataIndex: 'community_name',
//            sortable:true,
//            align: 'center',
//            width: 70,
//            renderer: function(value,metadata,record,rowIndex){
//            	return '${communityName}';
//    		}
//        },{
//            header: "区域名称",
//            dataIndex: 'warn_area_name',
//            align: 'center',
//            sortable:true,
//            width: 90,
//            renderer: function(value,metadata,record,rowIndex){
//            	return '${areaName}';
//    		}
        }];
    var warn_area_info_grid = new Ext.grid.GridPanel({
        store: warn_area_info_store,
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        columns:warn_area_info_gridColumns,
        viewConfig: {
            forceFit:true
        }
//        ,bbar: new Ext.PagingToolbar({
//            store: warn_area_info_store,
//            displayInfo: true,
//            displayMsg: '第{0} - {1}行,共 {2}行',
//            emptyMsg: "空"
//        })
    });
    
	var warn_area_info_win = new Ext.Window({
		title: '${date}&nbsp;&nbsp;>&nbsp;&nbsp;${communityName}&nbsp;&nbsp;>&nbsp;&nbsp;${personName}&nbsp;&nbsp;>&nbsp;&nbsp;${areaName}',
		layout : 'fit',
		width : 500,
		height : 300,
        border:false,
        modal :true,
//        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        resizable:false,
        items: [warn_area_info_grid]
	});
    return warn_area_info_win;
})();
