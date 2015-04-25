<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var description = '${description}';
	var comunity_show_store = new Ext.data.JsonStore({ 
		url : "mapseeting/stationmanage/communitys-data.action", 
		fields : ['id','name'], 
		root : "values", 
		baseParams : { 
		} 
	}); 
	comunity_show_store.addListener("load", function(){ 
		if('${communityId}' != ''){
			community_show.setValue('${communityId}');
			community_show.disable(true);
		}else if(this.totalLength>0){
			community_show.setValue(this.getAt(0).get('id'));
		}
	}); 
	comunity_show_store.load();
	var community_show = new Ext.form.ComboBox( { 
		fieldLabel : '小&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;区', 
		mode : 'local', 
		triggerAction : 'all', 
		selectOnFocus : true, 
		forceSelection : true, 
		editable : false, 
		valueField : 'id', 
		displayField : 'name', 
		hiddenName:'communityId',
		name : 'communityId',
		store : comunity_show_store 
	});
	var type_show_store = new Ext.data.JsonStore({ 
		url : "mapseeting/stationmanage/types-data.action", 
		fields : ['id','name'], 
		root : "values", 
		baseParams : { 
			type:'PERSON_TYPE'
		} 
	}); 
	type_show_store.addListener("load", function(){ 
		if('${dictionaryId}' != ''){
			type_show.setValue('${dictionaryId}');
		}else if(this.totalLength>0){
			type_show.setValue(this.getAt(0).get('id'));
		}
	}); 
	type_show_store.load();
	var type_show = new Ext.form.ComboBox( { 
		fieldLabel : '类&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;型', 
		mode : 'local', 
		triggerAction : 'all', 
		selectOnFocus : true, 
		forceSelection : true, 
		editable : false, 
		valueField : 'id', 
		displayField : 'name', 
		hiddenName:'dictionaryId',
		name : 'dictionaryId',
		store : type_show_store 
	});
	
	var changePhotoForm = new Ext.FormPanel({
	 	header : false,
        fileUpload: true,              //标志此表单数据中包含文件数据
        autoHeight: true,
        border:false,
//        bodyStyle: 'padding: 10px 10px 0 10px;',
        labelWidth: 65,
        buttonAlign:'center',
        defaults: {
            anchor: '90%',
            allowBlank: false
        },
        items: [{
	        	xtype:'hidden',
	        	name:'photoImage'
	        },{
            xtype: 'fileuploadfield',     //表单域类型
            emptyText: '请您选择文件',
            fieldLabel: '文件路径',
            name: 'upload',
            buttonCfg: {
                text: '',
                iconCls: 'upload-icon'     //按钮图标
            }
        }]
    });
	photoWin = new Ext.Window({
		title: '头像',
        width: 300,
        height:100, 
        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        resizable:false,
        items: [changePhotoForm],
        buttons: [{
            text: '提交',
            handler: function(){
                if(changePhotoForm.getForm().isValid()){
                     changePhotoForm.getForm().submit({
                         url: 'mapseeting/stationmanage/save-person-photo.action',
                         waitMsg: '正在保存数据...',
                         success: function(xhr, response){
                         	$('#photo-show').find('img').attr('src',response.result.msg);
                         	$('#photo').val(response.result.msg);
                         	changePhotoForm.getForm().setValues({mapImage:response.result.msg});
                            photoWin.hide();
                         },
	            		 failure: function(form,action){
						     Ext.Msg.alert('提示','操作失败！');
						 }
                     });
                }
            }
        },{
            text: '取消',
            handler: function(){
                photoWin.hide();
            }
        }]
	});
	var imagebox = new Ext.BoxComponent({
		autoEl: {
			//margin-bottom:10px
			style: 'width:80px;height:100px;border:0px solid #ccc; text-align:left;padding-bottom:10px;padding-right:10px;cursor: pointer;',
			tag: 'div',
			id: 'photo-show',
			html: '<img src="${photo}" style="position: absolute;width:80px;height:100px;" title="点击更换头像"><br><br><br>点击添加头像</img>'
		},
		listeners:{
			"afterrender":function(){
				$('#photo-show').on('click',function(){
					photoWin.show();
				});
          	}
		}
	});
	var personPanel = new Ext.FormPanel({
		url:'mapseeting/stationmanage/save-station.action',
//		title : '',
//		labelWidth: 70, 
      	defaults: {
      		anchor: '90%'
      	},
        frame:true,
        defaultType: 'textfield',
		border:false,
        items: [{
        	xtype : 'fieldset',
        	anchor: '100%',
        	labelWidth : 80,
        	items:[{
			    layout:'column',
	            layoutConfig: {columns:2},
	            items:[imagebox,{
					layout : 'form',
					labelWidth : 40,
					border : false,
					columnWidth:1.0,
					defaultType: 'textfield',
					bodyStyle:'padding-top:15px',
//					defaults: {bodyStyle:'padding-top:20px'},
					items : [{
			        	fieldLabel: '姓&nbsp;&nbsp;&nbsp;名',
			            name:'name',
			            value:'${name}',
				    	allowBlank : false,
			           	blankText: '姓名不能为空',
			           	maxLength:15
				    },{
			        	fieldLabel: '年&nbsp;&nbsp;&nbsp;龄',
			            name:'age',
			            value:'${age}',
				    	allowBlank : false,
			           	blankText: '年龄不能为空'
				    },{
			        	fieldLabel: '电&nbsp;&nbsp;&nbsp;话',
			            name:'phone',
			            value:'${phone}'
				    }]		   
				}]  
			},{
	        	xtype:'hidden',
	        	id:'id',
	        	name:'id',
	        	value:'${id}'
	        },{
	        	xtype:'hidden',
	        	id:'photo',
	        	name:'photo',
	        	value:'${photo}'
	        },community_show,type_show,{
	        	xtype:'textarea',
	        	fieldLabel: '描&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;述',
	        	width:'95%',
	            name:'description',
	            value:description.replace(new RegExp('<br>', 'g'), '\n')
		    }] 
		}],
	    buttonAlign:'center',
	    listeners:{
			"afterrender":function(){
				if('' == '${id}'){
					personPanel.getForm().reset();
				}
          	}
		},
	    buttons: [{
            text: '保存',
            handler:function(){
				personPanel.getForm().submit({
					waitMsg: '正在保存数据...',
            		success:function(form,action){
            			personInfoWin.close();
            			person_store.reload();
            		},
            		failure: function(form,action){
					    Ext.Msg.alert('提示','操作失败！');
					}
            	});
            }
        },{
            text: '取消',
            handler:function(){
            	personInfoWin.close();
            }
        }]
	});
    return personPanel;
})();



