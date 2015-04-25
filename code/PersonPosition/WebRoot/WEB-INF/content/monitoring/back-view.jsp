<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

(function(){
	var gridDataUrl = "sys/usergroup/grid-data.action";
	var rowId = 'sugid';
	var storeFields = ['name','remards'];   
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
    
    store.setDefaultSort('sugid', 'desc');
    
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
            header: "视频名称",
            dataIndex: 'name',
            align: 'center',
            width: 120,
            renderer: function(value,metadata,record,rowIndex){
            	return "xxxx项目"+(rowIndex+1);
    		}
    }];
    //grid事件定义
    var gridListeners = {
       	celldblclick:function(grid, rowIndex, columnIndex, e) {
			if(columnIndex == 1){
				plvPlayer((rowIndex+1)+'.flv');
			}
		}
    };
    var grid = new Ext.grid.GridPanel({
        store: store,
        region : 'center',
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        height:350,
        columns:gridColumns,
        viewConfig: {
            forceFit:true
        },
        listeners:gridListeners
    });
   
	var infoPanel = new Ext.FormPanel({
        region:'west',
        width: 280,
	    displayfieldWidth: 75, 
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        defaultType: 'displayfield',   
        items : [{	
			xtype : 'fieldset',
			collapsible : true,
			title:'信息栏',
			items : [{
		               	fieldLabel: '项目名称',name: 'code',anchor : '90%',xtype:'displayfield'			           
		            },{
		                fieldLabel: '开始时间',anchor : '90%',	xtype : 'displayfield',name: 'ridName'         
					},{
		               	fieldLabel: '结束时间',anchor : '90%',xtype : 'displayfield',	name: 'bgc'     
					},{
		               	fieldLabel: '负责人',anchor : '90%',xtype : 'displayfield',name: 'fullName'         
					},{
		               	fieldLabel: '客户名称',anchor : '90%',xtype : 'displayfield',name: 'businessState'         
					}
			]	 
		},{	
			xtype : 'fieldset',
			collapsible : true,
			title:'播放列表',
			layout:'fit',
			items:grid 
		}]
    });  
    
    infoPanel.on("render",function(cmp){
    	var config = {
    		url:'input/building/edit-form-data.action',    
    		params:{bid:'402881fe2fcebe75012fcf20b1b6013e'}
    	};
    	Ext.apply(config,commonLoadFormConfig);
    	 	var task = new Ext.util.DelayedTask(function(){
		   	cmp.getForm().load(config);
		});
		task.delay(500);
    });  
    
    
    var viewPanel = new Ext.Panel({
		region:'center',
        layout:'fit',
		html:'<div id="vidoe" style="width:650px;height:600px"></div>'
	});
	
	viewPanel.on('afterrender',function(){
		plvPlayer('1.flv');
    },viewPanel,{delay:500});
    
    function plvPlayer(flvFile){
    	var s5 = new SWFObject("FlvPlayer201002.swf","playlist","650","600","7");
		s5.addParam("allowfullscreen","true");
		s5.addVariable("autostart","false");
		s5.addVariable("file",flvFile);
		s5.addVariable("width","650");
		s5.addVariable("height","600");
		s5.write("vidoe");
    }
    

    var win = new Ext.Window({
        title: '视频回放',
        iconCls:'back-icon',
        width: 950,
        height: 635,
        modal :true,
        layout:'border',
        plain:true,
        constrain: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        items: [viewPanel,infoPanel]
    });
    return win;
})();