<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
      var simple = new Ext.FormPanel({
        labelWidth: 75, 
        frame:true,
        autoWidth : true,
        autoHeight : true,
        bodyStyle:'padding:5px 5px 0',
        defaultType: 'displayfield',   
        items : [
		{	
			xtype : 'fieldset',
			collapsible : true,
			title:'基础信息',
			items:[
			{
			    layout:'column',
                layoutConfig: {columns:2},
                defaults: {frame:false,  bodyStyle:'padding:0px'}
                ,items:[                        
                    {
							layout : 'form',
							labelWidth : 80,
							border : false,
							columnWidth:0.5,
							items : [
							     {
					               fieldLabel: '编码',
					               name: 'code',
					               anchor : '90%',
					               xtype:'displayfield'
					           },      
					           {
					               fieldLabel: '简称',
					               xtype : 'displayfield',
					               anchor : '90%',
					               name: 'shortName'         
					           },{
					               fieldLabel: '全称',
					               anchor : '90%',
					               xtype : 'displayfield',
					               name: 'fullName'         
					           },{
					               fieldLabel: '已用机架',
					               name: 'usedFrame',
					               anchor : '90%',
					               xtype : 'displayfield'					       
					           },{
					               fieldLabel: '未用机架',
					               name: 'unusedFrame',
					               anchor : '90%',
					               xtype : 'displayfield'					       
					           },{
					               fieldLabel: '可装机架',
					               name: 'canInstallFrame',
					               anchor : '90%',
					               xtype : 'displayfield'					       
					           },{
					               fieldLabel: '已用座席',
					               name: 'usedSat',
					               anchor : '90%',
					               xtype : 'displayfield'					       
					           },{
					               fieldLabel: '未用座席',
					               name: 'unusedSat',
					               anchor : '90%',
					               xtype : 'displayfield'					       
					           },{
					               fieldLabel: '可装座席',
					               name: 'canInstallSta',
					               anchor : '90%',
					               xtype : 'displayfield'					       
					           },
					           {
					               fieldLabel: '外围墙面积',
					               name: 'enclosureArea',
					               anchor : '90%',
					               xtype : 'displayfield'					       
					           },{
					               fieldLabel: '经度',
					               name: 'XCoordinate',
					               anchor : '90%',
					               xtype : 'displayfield'
					           },{
					               fieldLabel: '纬度',
					               name: 'YCoordinate',
					               anchor : '90%',
					               xtype : 'displayfield'
					           },{
					               fieldLabel: '空调',
					               anchor : '90%',  
					               xtype : 'displayfield',
					               height:85,
					               name: 'airCondition'         
					           }
							]	
						}
						,
						{
							layout : 'form',
							labelWidth : 80,
							border : false,
							columnWidth:0.5,
							items : [{
							 		fieldLabel: '机房类型',
					               name: 'roomType',
					               anchor : '90%',
					               xtype : 'displayfield'	
							},{
							 		fieldLabel: '机房级别',
					               name: 'roomLevel',
					               anchor : '90%',
					               xtype : 'displayfield'	
							},	 
					            
					           		{
					               fieldLabel: '总建筑面积',
					               name: 'totalArea',
					               anchor : '90%',		
					               xtype : 'displayfield'   
					           },{
					               fieldLabel: '可用面积',
					               anchor : '90%',	
					               xtype : 'displayfield',
					               name: 'availableArea'         
					           },
					           {
					               fieldLabel: '已用面积',
					               name: 'usedArea',
					               anchor : '90%',
					               xtype : 'displayfield'     
					           },{
					               fieldLabel: '剩余面积',
					               anchor : '90%',	
					               xtype : 'displayfield',
					               name: 'remainArea'         
					           },  {
					               fieldLabel: '机房峰值负载',
					               name: 'load',
					               anchor : '90%',
					               xtype : 'displayfield'					       
					           },{
					               fieldLabel: '走线架高度',
					               name: 'wwHight',
					               anchor : '90%',
					               xtype : 'displayfield'					       
					           },
					           {
					               fieldLabel: '机房净高',
					               name: 'roomHeadroom',
					               anchor : '90%',
					               xtype : 'displayfield'					       
					           },{
					            	fieldLabel: '围护结构材料',
					               name: 'thermalMaterials',
					               anchor : '90%',
					               xtype : 'displayfield'	
					           },{
					            	fieldLabel: '机房照明设备',
					               name: 'lighting',
					               anchor : '90%',
					               xtype : 'displayfield'	
					           },
					            {
					            	fieldLabel: '地址',
					               anchor : '90%',
					               xtype : 'displayfield',
					               name: 'address' 
					            },
					           
					             {
					               fieldLabel: '备注',
					               anchor : '90%',
					               height:85,
					               xtype : 'displayfield',
					               name: 'remarks'         
					           }

						  ]					
						}
		
					]
			   } 	   
			]   
		}


		]  
    });    
    
    simple.on("render",function(cmp){
    	var config = {
    		url:'input/room/edit-form-data.action', 
    		params:{rid:'${editId}'}
    	};
    	Ext.apply(config,commonLoadFormConfig);
      	Ext.apply(config,commonLoadFormConfig);
        cmp.getForm().load(config);
    });
    
    return simple;
})();



