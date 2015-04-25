<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
(function(){
	var depTreeLoader = new Ext.tree.TreeLoader({
		dataUrl:'organizations/dept/tree-data.action'
	});
	depTreeLoader.on('load',function(loader,node,response){
		var checked = node.getUI().isChecked();
		node.eachChild(function(current){
   			current.getUI().toggleCheck(checked);
   		});
	});
	depTreeLoader.on("beforeload", function(loader, node) {
        this.baseParams.depid = node.id;
    });
	var depTree = new Ext.tree.TreePanel({
        height: 230,
        width:"85%",
        useArrows:true,
        autoScroll:true,
        animate:true,
        frame:true,
        containerScroll: true,
        rootVisible: true,
        root: {
            text:'公司',
            checked:false,
            expanded:true,
            nodeType:'async',
            id:-1
        },
        loader:depTreeLoader,
        listeners: {
            'checkchange': function(node, checked){
            	if(node.hasChildNodes()){
            		if(checked)node.expand();
            		node.eachChild(function(current){
            			current.getUI().toggleCheck(checked);
            		});
            	}
            	var root = userTree.getRootNode();
                if(checked){
                	if(node.isLeaf()){
			           	var newNode = new Ext.tree.TreeNode({text:node.text,leaf:true,id:node.id});
			           	if(!root.findChild('id',node.id,false)){
			           		root.appendChild(newNode);
			           	}
		           }
                }else{
                	if(node.isLeaf()){
			           	var theNode = root.findChild('id',node.id,false);
			           	if(theNode){
			           		root.removeChild(theNode,true);
			           	}
		           }
                }
	           
            }
        }
	});
	var userTree = new Ext.tree.TreePanel({
        height: 230,
        width:"85%",
        ref:'userTree',
        useArrows:true,
        autoScroll:true,
        rootVisible: false,
        animate:true,
        frame: true,
        root: new Ext.tree.TreeNode({
        })
	});
	var userSelector = new Ext.Panel({
	    	layout:'column',
	    	columnWidth:.5,
	    	items:[{
                columnWidth:.5,
                border:false,
                items: depTree
            },{
                columnWidth:.5,
                ref:'treePanel',
                border:false,
                items: userTree
            }
	    	]
	    });
	userSelector.getSelectedUser = function(){
		var ids = [];
		Ext.each(this.treePanel.userTree.getRootNode().childNodes,function(){
			ids.push(this.id);
		});
		return ids;
	}
	return userSelector;
})();
