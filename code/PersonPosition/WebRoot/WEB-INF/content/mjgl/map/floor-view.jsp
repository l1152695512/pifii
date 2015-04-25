<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
    var simple = new Ext.FormPanel({
		displayfieldWidth: 75, 
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        autoWidth : true,
        autoHeight : true,
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
		              	fieldLabel : '产权归属',name: 'proRight',anchor : '90%',	xtype:'displayfield'		
		            },{
		              	fieldLabel : '机楼主类型',name: 'cid1Name',anchor : '90%',xtype:'displayfield'	
		            },{
		              	fieldLabel : '机楼次类型',name: 'cid2Name',anchor : '90%',	xtype:'displayfield'		
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
					}]	
				},{
					layout : 'form',
					displayfieldWidth : 80,
					border : false,
					columnWidth:0.32,
					items : [{
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
		               fieldLabel: '办公面积', name: 'officeArea', anchor : '90%', xtype : 'displayfield'     
					},{
		               fieldLabel: '营业厅面积',name: 'businessArea', anchor : '90%',xtype : 'displayfield'     
					},{
		               fieldLabel: '其它面积',name: 'otherArea',anchor : '90%', xtype : 'displayfield'     
					},{
					  fieldLabel: '开始楼层',anchor : '90%',xtype : 'displayfield',name: 'startLayers'     
					},{
		               fieldLabel: '结束层数',name: 'endLayers',anchor : '90%', xtype : 'displayfield'     
					}]					
				},{
					layout : 'form',
					displayfieldWidth : 80,
					border : false,
					columnWidth:0.33,
					items : [{
		               fieldLabel: '规划建议', anchor : '90%', height:93,border : false,xtype : 'displayfield',name: 'plaAdvice'         
					},{
		               fieldLabel: '地址', anchor : '90%',height:93,border : false,xtype : 'displayfield',name: 'address'         
					} ,{
		               fieldLabel: '备注',anchor : '90%', height:93,	border : false,xtype : 'displayfield',name: 'remarks'         
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
    	cmp.getForm().load(config);
    });  
    
    return simple;
    
})();



