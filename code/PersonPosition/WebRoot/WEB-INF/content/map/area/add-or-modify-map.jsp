<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var district_store = new Ext.data.JsonStore({ 
		url : "map/area/combo-district-data.action", 
		fields : ['id','name'], 
		root : "values", 
		baseParams : {
			provinceName:'${provinceName}',
			cityName:'${cityName}'
		} 
	}); 
	district_store.addListener("load", function(){
		if('${districtId}' != ''){
			district_show.setValue('${districtId}');
		}else if(this.totalLength>0){
			var defaultValue = this.getAt(0).get('id');
			district_show.setValue(defaultValue);
			this.each(function(item){
				if(item.data.name == '${districtName}'){
					district_show.setValue(item.data.id);
				}
			});
		}
	}); 
	district_store.load();
	var district_show = new Ext.form.ComboBox({
		fieldLabel : '　　　区', 
		mode : 'local', 
		triggerAction : 'all', 
		selectOnFocus : true, 
		forceSelection : true, 
		editable : false, 
		valueField : 'id', 
		displayField : 'name', 
		hiddenName:'districtId',
		name : 'districtId',
		store : district_store
	});
	
	var user_store = new Ext.data.JsonStore({ 
		url : "sys/user/combo-user.action", 
		fields : ['id','name'], 
		root : "values"
	}); 
	user_store.addListener("load", function(){
		if('${userId}' != ''){
			user_show.setValue('${userId}');
		}else if(this.totalLength>0){
			var defaultValue = this.getAt(0).get('id');
			user_show.setValue(defaultValue);
		}
	}); 
	user_store.load();
	var user_show = new Ext.form.ComboBox({
		fieldLabel : '用　　户',
		mode : 'local', 
		triggerAction : 'all', 
		selectOnFocus : true, 
		forceSelection : true, 
		editable : false, 
		valueField : 'id', 
		displayField : 'name', 
		hiddenName:'userId',
		name : 'userId',
		store : user_store
	});
	
    var communityPanel = new Ext.FormPanel({
        labelWidth: 65, 
        url:'map/area/save-map-info.action',
        fileUpload: true, 
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        defaults: {width: 160},
        defaultType: 'textfield',
        items: [{
	        	xtype:'hidden',
	        	name:'id',
	        	value:'${id}'
        },{
            fieldLabel: '　　　省',
            name: 'provinceName',
            value:'${provinceName}',
            disabled:true
        },{
            fieldLabel: '　　　市',
            name: 'cityName',
            value:'${cityName}',
            disabled:true
        },district_show,user_show,{
            fieldLabel: '小区名称',
            name: 'name',
            allowBlank:false,
            value:'${name}'
        },{
        	id:'area_type_id',
	    	xtype: 'radiogroup',
		    fieldLabel: "类　　型",
		    items : [{
		    	boxLabel: '平面',
		    	name: 'areaType',
		    	inputValue:'1',  //映射的值
		    	checked : true
		    },{
		    	boxLabel: '楼层',
		    	name: 'areaType',
		    	inputValue:'2',
		    	checked : '${areaType}'=='2'
		    }]
		},{
        	xtype:'hidden',
            fieldLabel: '依赖区域',
            name: 'dependentId',
//            allowBlank:false,
            value:'${dependentId}'
        },{
        	xtype:'hidden',
            fieldLabel: '依赖区域名称',
            name: 'dependentName',
//            allowBlank:false,
            value:'${dependentName}'
        },{
        	xtype:'hidden',
            fieldLabel: '经　　度',
            name: 'locationX',
//            allowBlank:false,
            value:'${locationX}'
        },{
        	xtype:'hidden',
            fieldLabel: '纬　　度',
            name: 'locationY',
//            allowBlank:false,
            value:'${locationY}'
        },{
        	xtype:'hidden',
            fieldLabel: '用户ID',
            name: 'userIdField',
//            allowBlank:false,
            value:'${userId}'
        }]
    });
    var editWin = new Ext.Window({
		title: '小区信息',
	    width: 300,
	    height:250,
	    border:false,
	    modal :true,
	    layout: 'fit',
	    plain:true,
	    constrain: true,
	    closable: true,
	    bodyStyle:'padding:5px;',
	    buttonAlign:'center',
		resizable:false,
	    items: [communityPanel],
	    buttons: [{
	        text: '保存',
	        handler:function(){
	        	 if(communityPanel.getForm().isValid()){
	                 communityPanel.getForm().submit({
	                     waitMsg: '正在保存数据...',
	                     success: function(communityPanel, o){
	                        editWin.close();
	                        if(typeof(addCommunityWin) != "undefined") {
	                        	addCommunityWin.close();
	                        }
							store.load(store.lastOptions);
	                     },
	            		 failure: function(form,action){
						     Ext.Msg.alert('提示','操作失败！');
						 }
	                 });
	        	}
	        }
	    },{
	        text: '取消',
	        handler:function(){
	        	editWin.close();
	        }
	    }],
        listeners:{
			"afterrender":function(){
				if('' != '${id}'){
					district_show.disable();
					user_show.disable();
					$("#area_type_id").parent().parent().hide();
				}
				if('' != '${dependentId}'){
					$("#area_type_id").parent().parent().hide();
					user_show.disable();
				}
          	}
		}
	});
    return editWin;
})();