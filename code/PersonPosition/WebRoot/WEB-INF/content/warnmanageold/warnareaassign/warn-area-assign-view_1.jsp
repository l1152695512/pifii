<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var rootNodeId = '0';
	var currentNodeId = rootNodeId;
//	var isLeaf = false;
	
	var rootNode = new Ext.tree.AsyncTreeNode({
		id : rootNodeId,
		text : '所有小区',
		draggable : false,
		expanded : true
	});
	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl : 'warnmanage/warnarea/get-tree-nodes.action'
	});
	treeLoader.on("beforeload", function(loader, node) {
        this.baseParams.node = node.id;
    });
    var tree = new Ext.tree.TreePanel({
		title : '小区区域浏览',
	    region : 'west',
		useArrows : true,
		autoScroll : true,
		enableDD : false,
		animate : true,
		width : 170,
		collapsible : true,
		rootVisible : true, 
		containerScroll : true,
		loader : treeLoader,
		root : rootNode,
 	    listeners:{    
      		 'click':function(node, event) {
      			currentNodeId = node.id;
      			assigned_person_store.reload();
      		 },
      		 'beforeclick':function(node, event) {
      		 	if(!node.isLeaf()){
  		 			return false;
  		 		}
      		 }
      	} 
	});
//	var pageLimit = 15;
	var person_storeFields = ['id','person_id','name','age','phone','description','photo'];
	var assigned_person_store = new Ext.data.JsonStore({
        url: 'warnmanage/warnareaassign/assigned-person.action',
        root: 'list',
//        autoLoad:true,
        totalProperty: 'totalRecord',
        idProperty: 'id',
        remoteSort: true,
        fields: person_storeFields
    });
	assigned_person_store.on('beforeload',function(store){ 
       store.baseParams = {nodeId:currentNodeId}; 
    });
    assigned_person_store.setDefaultSort('name', 'desc');
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
	var addPerson= new Ext.Action({
        text: '添加',
        iconCls :'icon-add',
        handler: function(){
        	if(currentNodeId == rootNodeId){
        		Ext.Msg.alert("提示","请先点击左边的具体区域，再进行添加！");
        		return;
        	}
        	Ext.Msg.wait('获取人员列表....', '提示');
			Ext.Ajax.request({
				async : false,
				url:'warnmanage/warnareaassign/add-assign-person.action',
				params : {areaId:currentNodeId},
				success : function(xhr){
					Ext.Msg.hide();
					var personListInfoWin = eval(xhr.responseText);
					personListInfoWin.show();
				},
				failure:function(xhr){
					Ext.Msg.hide(); 
					Ext.Msg.alert("温馨提醒","数据查询失败,稍后请重试！");
				}
			});
        }
    });
	var deletePerson= new Ext.Action({
        text: '删除',
        iconCls :'icon-delete',
        handler: function(){
        	var selected = person_assign_grid.getSelectionModel().getSelections();
			var params = [];
			Ext.each(selected, function(item) {
				params.push(item.id);
			});
			if(params.length == 0){
		   		Ext.Msg.alert("提示","请选择要删除的人员！");
		   		return;
		    }
		    Ext.Msg.confirm('确认', '确定要删除选中的人员？',
				function(btn, text) {
					if (btn == 'yes') {
						Ext.Msg.wait('正在删除....', '提示');
						Ext.Ajax.request({
				       		url:'warnmanage/warnareaassign/delete-assign-person.action',
				       		params: {ids:params},
				            success : function(xhr) {
				            	Ext.Msg.hide(); 
				            	assigned_person_store.reload();
				           	},
						    failure: function(xhr){
						    	Ext.Msg.hide(); 
						    	Ext.Msg.alert("温馨提醒","删除失败，稍后请重试！");
						    }
				        })
					}
			});
		}
    });
    function showFormPanel(id){
    	Ext.Msg.wait('正在获取数据....', '提示');
    	Ext.Ajax.request({
			async : false,
			url : 'personmanage/add-or-modify-person.action',
			params : {id:id,isRead:"yes"},
			success : function(xhr){
				Ext.Msg.hide(); 
				var personInfoWin = eval(xhr.responseText);
				personInfoWin.show();
			},
			failure:function(xhr){
				Ext.Msg.hide(); 
				Ext.Msg.alert("温馨提醒","数据查询失败,稍后请重试！");
			}
		});
    }
	var person_assign_grid = new Ext.grid.GridPanel({
        store: assigned_person_store,
        region : 'center',
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        border : false,
        columns:person_gridColumns,
        viewConfig: {
            forceFit:true
        },
        sm: sm,
        tbar: [addPerson,'-',deletePerson],
        listeners:{  
			rowdblclick : function(grid,row){  
				var row_data = grid.getStore().getAt(row);
				var person_id = row_data.data.person_id;
				showFormPanel(person_id);
			}
		} 
//        ,
//        bbar: new Ext.PagingToolbar({
////            pageSize: pageLimit,//pageSize 表示每页有几条记录,
//            store: assigned_person_store,
//            displayInfo: true
////            displayMsg: '第{0} - {1}行,共 {2}行',
////            emptyMsg: "空"
//        })
    });
	var mainPanel = new Ext.Panel({
    	header : false,
		title : '人员警告区域分配',
		border : false,
		layout : 'border',
		items : [tree,person_assign_grid]
	});
    return mainPanel;
})();