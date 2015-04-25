<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
    var simple = new Ext.FormPanel({
		displayfieldWidth: 75, 
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        defaultType: 'displayfield',   
        items : [{	
			xtype : 'fieldset',
			collapsible : true,
			title:'基础信息',
			items:[{
			    layout:'column',
                layoutConfig: {columns:2},
                defaults: {frame:false,  bodyStyle:'padding:0px'},
                items:[{
					layout : 'form',
					displayfieldWidth : 80,
					border : false,
					columnWidth:0.33,
					items : [{
		               	fieldLabel: '编码',name: 'code',anchor : '90%',xtype:'displayfield'			           
		            },{
		               	fieldLabel: '简称',xtype : 'displayfield',anchor : '90%',name: 'shortName'         
					},{
		               	fieldLabel: '全称',anchor : '90%',xtype : 'displayfield',name: 'fullName'         
					},{
		               	fieldLabel: '所在城市',	anchor : '90%',xtype : 'displayfield',name: 'cityName'         
					},{
		               	fieldLabel: '所属区域',anchor : '90%',	xtype : 'displayfield',name: 'ridName'         
					},{
		               	fieldLabel: '建筑结构',anchor : '90%',xtype : 'displayfield',	name: 'bgc'         
					},{
		               	fieldLabel: '业务状态',anchor : '90%',xtype : 'displayfield',name: 'businessState'         
					},{
		               	fieldLabel: '完工日期',anchor : '90%',xtype : 'displayfield',name: 'completionDate'         
					},{
		               	fieldLabel: '变压器容量',anchor : '90%',xtype : 'displayfield',name: 'transCapacity'         
					},{
		               	fieldLabel: '发电机容量',anchor : '90%',xtype : 'displayfield',name: 'generCapacity'         
					}]	
				},{
					layout : 'form',
					displayfieldWidth : 80,
					border : false,
					columnWidth:0.32,
					items : [{
		               	fieldLabel: '基站峰值负载',anchor : '90%',	xtype : 'displayfield',name: 'exisRoomLoad'         
					},{
		               	fieldLabel: '产权归属',anchor : '90%',	xtype : 'displayfield',name: 'proRight'         
					},{
		               	fieldLabel: '经度',anchor : '90%',	xtype : 'displayfield',name: 'XCoordinate'         
					},{
		               fieldLabel: '纬度', anchor : '90%',xtype : 'displayfield',name: 'YCoordinate'         
					},{
		               fieldLabel: '总建筑面积', name: 'totalArea', anchor : '90%',xtype : 'displayfield'   
					},{
		               fieldLabel: '可用面积',anchor : '90%', xtype : 'displayfield',name: 'availableArea'         
					},{
		               fieldLabel: '已用面积',name: 'usedArea', anchor : '90%', xtype : 'displayfield'     
					},{
		               fieldLabel: '剩余面积',anchor : '90%',xtype : 'displayfield', name: 'remainArea'         
					},{
		               fieldLabel: '其它面积',name: 'otherArea',anchor : '90%', xtype : 'displayfield'     
					},{
					  fieldLabel: '可供使用的容量',anchor : '90%',xtype : 'displayfield',name: 'availCapacity'     
					}]					
				},{
					layout : 'form',
					displayfieldWidth : 60,
					border : false,
					columnWidth:0.33,
					items : [{
		               fieldLabel: '可否扩容', anchor : '95%',xtype : 'displayfield',name: 'couldExpansion'         
					},{
		               fieldLabel: '规划建议', anchor : '95%',height:50,xtype : 'displayfield',name: 'plaAdvice'         
					},{
		               fieldLabel: '地址', anchor : '95%',height:50,xtype : 'displayfield',name: 'address'         
					},{
		               fieldLabel: '备注',anchor : '95%', height:50,xtype : 'displayfield',name: 'remarks'         
					},{
		               fieldLabel: '空调', anchor : '95%', height:60,xtype : 'displayfield',name: 'airCondition'         
					}]					
				}]
			}]   
		}]
    });  
    
    simple.on("render",function(cmp){
    	var config = {
    		url:'input/building/edit-form-data.action',    
    		params:{bid:'${editId}'}
    	};
    	Ext.apply(config,commonLoadFormConfig);
    	 	var task = new Ext.util.DelayedTask(function(){
		   	cmp.getForm().load(config);
		});
		task.delay(500);
    });  
    
    return simple;
    
})();



