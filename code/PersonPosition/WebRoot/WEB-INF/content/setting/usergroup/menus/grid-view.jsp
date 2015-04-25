<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>(function() {

	var sugid='${sugid}';
	var rootNode = new Ext.tree.AsyncTreeNode({
		text : '菜单',
		expanded : true,
		checked :false,
		listeners: {
            'checkchange': function(node, checked){ 
                  node.eachChild(function(current){
   			     current.getUI().toggleCheck(checked);
   		      });
            }
        }
	});
	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl : 'setting/usergroup/menus/tree-data.action'
	});	
	treeLoader.on("beforeload", function(loader) {
        this.baseParams.sugid = sugid;
    });

    var addAction =  new Ext.Action({
        text: '保存',
        iconCls :'icon-save',
        handler: function(){
		var systemMenuIds = []
		Ext.each(tree.getChecked(), function(item) {
			systemMenuIds.push(item.id);
		});
            var reloadFn = function(xhr){
			     var task = new Ext.util.DelayedTask(function(){
					treeLoader.load(rootNode,function(node){
					node.expand();
					});
				});
				task.delay(200);
		   };
			if(systemMenuIds.length == 0){
		   		Ext.Msg.alert("提示","菜单不能为空！");
		   		return;  
		    }else{
	           Ext.Ajax.request({
				   url: 'setting/usergroup/menus/save-form.action',
				   success: reloadFn,
				   failure: reloadFn,
				   params: { systemMenuIds: systemMenuIds,sugid:sugid }
				});
			}
        }
    });
	var tree = new Ext.tree.TreePanel({
		region : 'center',
		useArrows : true,
		autoScroll : true,
		enableDD : false,
		animate : true,
		width : 200,
		height:150,
		checked:true,
		containerScroll : true,
		loader : treeLoader,
		root : rootNode,
		listeners: {
            'checkchange': function(node, checked){     
               if(!node.isLeaf()){
                  node.eachChild(function(current){
   			     current.getUI().toggleCheck(checked);
   		      });
               }                
            }
        },
        tbar: [addAction]
	}); 
     var win = new Ext.Window({
        width: 480,
        height: 550,
        modal :true,
        layout: 'fit',
        plain:true,
        constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        items: tree
    });
    return win;
})();