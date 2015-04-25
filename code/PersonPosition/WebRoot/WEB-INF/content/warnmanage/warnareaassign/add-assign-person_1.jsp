<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var person_storeFields = ['id','name','photo','age','phone','description'];
	var unassigned_person_store = new Ext.data.JsonStore({
        url: 'warnmanage/warnareaassign/unassigned-person.action',
        root: 'list',
        autoLoad:true,
        totalProperty: 'totalRecord',
        idProperty: 'id',
        remoteSort: true,
        fields: person_storeFields
    });
	unassigned_person_store.on('beforeload',function(store){ 
       store.baseParams = {areaId:'${areaId}'}; 
    });
    unassigned_person_store.setDefaultSort('name', 'desc');
    var sm = new Ext.grid.CheckboxSelectionModel({
		checkOnly:true
	});
	var person_gridColumns = [sm,
    	{
//            header: "头像",
//            dataIndex: 'photo',
//            align: 'center',
//            width: 30,
//            renderer: function(value,metadata,record,rowIndex){
//    			return '<img src="'+value+'" style="width:60;height:80;"/>';  
//    		}
//        },{
            header: "姓名",
            dataIndex: 'name',
            align: 'center',
            sortable:true,
            width: 90 
        },{
            header: "年龄",
            dataIndex: 'age',
            sortable:true,
            align: 'center',
            width: 50
        },{
            header: "电话",
            dataIndex: 'phone',
            align: 'center',
            width: 70
        },{
            header: "描述",
            dataIndex: 'description',
            align: 'center',
            width: 70,
            renderer: function(value,metadata,record,rowIndex){
    			return '<div ext:qtitle="" ext:qtip="' + value + '">'+ value.replace(new RegExp('<br>', 'g'), '\n') +'</div>';  
    		}
        }];
	var personListGrid = new Ext.grid.GridPanel({
        store: unassigned_person_store,
        height:300,
        region : 'center',
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        columns:person_gridColumns,
        viewConfig: {
            forceFit:true
        },
        sm: sm,
        listeners:{  
	        rowdblclick : function(grid,row){  
				var row_data = grid.getStore().getAt(row);
				var person_id = row_data.id;
				showFormPanel(person_id);
			}
        }
    });
	
	var saveForm=new Ext.FormPanel({
		url: 'warnmanage/warnareaassign/save-assign-person.action',
//		height:'20px',
		frame:true,
		style : 'margin:5px 0px 5px 0px;',
	    defaultType: 'textfield',
	    labelAlign: 'center',
//		items : [{
//			xtype:'hidden',
//            name:'addPersons',
//        	hidden: true,
//        	hideLabel:true
//	    }],
	    buttonAlign:'center',
	    buttons: [{
            text: '保存',
            handler:function(){
            	var selected = personListGrid.getSelectionModel().getSelections();
				var params = [];
				Ext.each(selected, function(item) {
					params.push(item.id);
				});
				if(params.length == 0){
			   		Ext.Msg.alert("提示","请选择要添加的人员！");
			   		return;
			    }
			    saveForm.getForm().submit({
					waitMsg: '正在添加人员...',
					params: {areaId:'${areaId}',personIds:params},
	        		success:function(form,action){
						if(action.result.success){
							personListWin.close();
							assigned_person_store.reload();
						}else{
							Ext.Msg.alert("温馨提醒","保存失败，稍后请重试！");
						}
	        		},
	        		failure: function(form,action){
	        			Ext.Msg.alert("温馨提醒","保存失败，稍后请重试！");
					}
	        	});
            }
        },{
            text: '取消',
            handler:function(){
            	personListWin.close();
            }
        }]
	});
	
    var personListPanel = new Ext.Panel({
	    frame:true,
//	    width:420,
// 		height:400,
 		header : false,
//		border : false,
		layout : 'form',
	    items : [personListGrid,saveForm]
    });
    var personListWin = new Ext.Window({
		title: '路线点信息',
        width: 400,
        height:405, 
        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        resizable:false,
        items: [personListPanel]
	});
    return personListWin;
})();



