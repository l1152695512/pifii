<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	
	var rootId = '0';
	var rootNode = new Ext.tree.AsyncTreeNode({
		text : '菜单',
		id : rootId,
		expanded : true
	});
	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl : 'sys/menus/tree-data.action'
	});
	
	treeLoader.on("beforeload", function(loader, node) {
        this.baseParams.nodeId = node.id;
    });
    
	function reloadTreeNode(isNew,theForm){
		var sn = tree.getSelectionModel().getSelectedNode();
		if(sn){
			var task = new Ext.util.DelayedTask(function(){
				treeLoader.load(isNew?sn:sn.parentNode,function(node){
					node.expand();
				});
			});
			task.delay(200);
		}
		theForm.reset();
	}
	
	var formConfig={
        labelWidth: 100, 
        url:'sys/menus/save-form.action',
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        defaults: {width: 300},
        defaultType: 'textfield',
		tbar:[new Ext.Action({
			text : '保存',
			iconCls : 'icon-save',
			handler : function() {
				var config = {success:function(form,action){
					reloadTreeNode(form.findField('isNew').getValue() == "true",form);
					if(win&&win.isVisible())win.hide();
				}};
				Ext.applyIf(config,commonSubmitConfig)
				this.findParentByType ('form').getForm().submit(config);
				
			}
		})],
        items: [{
                name: 'isNew',
                xtype:'hidden',
                submitValue:false
            },{
                name: 'systemMenuId',
                xtype:'hidden'
            },{
                name: 'parentId',
                xtype:'hidden'
            },{
                fieldLabel: '菜单名',
                name: 'menuText',
                maxLength:20,
                minLength:3,
                allowBlank:false
            },/*{
                fieldLabel: 'Ext Id',
                name: 'extId',
                maxLength:64
            },*/{
                fieldLabel: '图标class',
                name: 'iconClass',
                maxLength:32
            },{
                fieldLabel: '链接action',
                name: 'actionPath',
                maxLength:128
            },{
               fieldLabel: '展开',
                xtype: 'radiogroup',
               	name: 'expanded',
                defauls:{
                	qtip: '初始加载的时候是否展开，只针对有子菜单的菜单'
                },
                items: [{
                    inputValue: 'true',
                    boxLabel: '是',
                    name: 'expanded',
                    checked:true
                },{
                    inputValue: 'false',
                    boxLabel: '否',
                    name: 'expanded'
                }]
            },{
               fieldLabel: '叶节点',
                xtype: 'radiogroup',
                name: 'leaf',
                items: [{
                    inputValue: 'true',
                    boxLabel: '是',
                    name: 'leaf',
                    checked:true
                },{
                    inputValue: 'false',
                    boxLabel: '否',
                    name: 'leaf'
                }]
            },{
               fieldLabel: '屏蔽',
                xtype: 'radiogroup',
                name: 'enabled',
                items: [{
                    inputValue: 'true',
                    boxLabel: '是',
                    name: 'enabled',
                    checked:true
                },{
                    inputValue: 'false',
                    boxLabel: '否',
                    name: 'enabled'
                }]
            },{
               fieldLabel: '禁用',
                xtype: 'radiogroup',
                name: 'isGrey',
                items: [{
                    inputValue: 'true',
                    boxLabel: '是',
                    name: 'isGrey',
                    checked:true
                },{
                    inputValue: 'false',
                    boxLabel: '否',
                    name: 'isGrey'
                }]
            },{
               fieldLabel: '快捷菜单',
                xtype: 'radiogroup',
                name: 'shortcuts',
                items: [{
                    inputValue: 'true',
                    boxLabel: '是',
                    name: 'shortcuts',
                    checked:false
                },{
                    inputValue: 'false',
                    boxLabel: '否',
                    name: 'shortcuts'
                }]
            }/*,{
            	fieldLabel: 'layoutAttr',
                name: 'layoutAttr',
                qtip: '生成的右边工作区tab自定义 layout属性，默认的layout是fit,例如{layout : \'vbox\',layoutConfig : {align : \'stretch\'}}',
	            xtype: 'textarea',
	            maxLength:60,
	            flex: 1
            }*/]
    };
    
    var win = null;
	var newMenuAction =  new Ext.Action({
		text : '增加',
		iconCls : 'icon-add',
		handler : function() {
			var fp
			
			if(!win){
				fp=new Ext.form.FormPanel(formConfig);
				win = new Ext.Window({
					title:'新增菜单',
					height:360,
					width:500,
					constrain:true,
					layout:'fit',
					modal : true,
					closeAction: 'hide',
					items:[
						fp
					]
				});
			}else{
				fp=win.get(0);
			}
			if(fp){
				var sn = tree.getSelectionModel().getSelectedNode();
				if(sn)fp.getForm().findField('parentId').setValue(sn.id);
				fp.getForm().findField('isNew').setValue("true");
			}
			win.show(this.el);
		}
	});
	
	var delAction = new Ext.Action({
		text:'删除',
		iconCls:'icon-delete',
		handler:function(){
			var sn = tree.getSelectionModel().getSelectedNode();
			if(sn){
				Ext.Ajax.request({
					url:'sys/menus/delete-data.action',
					params:{id:sn.id},
					success:function(xhr){
						reloadTreeNode(false,form.getForm());
					},
					failure:function(xhr){
						try{
							var json = Ext.decoder(xhr.responseText);
							if(json.msg)Ext.Msg.alert('失败',json.msg);
						}catch(e){
						}
					}
				});
			}
		}
	});
	
	var tree = new Ext.tree.TreePanel({
		region : 'west',
		useArrows : true,
		autoScroll : true,
		enableDD : true,
		animate : true,
		width : 250,
		containerScroll : true,
		loader : treeLoader,
		root : rootNode,
		tbar:[newMenuAction,'-',delAction],
		tools:[{
				id:'plus',
				qtip:'全部展开',
				handler:function(ev, el, p){
					p.expandAll();
				}
			  },{
				id:'minus',
				qtip:'全部折叠',
				handler:function(ev, el, p){
					p.collapseAll();
				}
			  }]
	});
	var regionCfg = {region:'center',title:'菜单项',hidden:true}
	Ext.apply(regionCfg,formConfig)
	var form = new Ext.FormPanel(regionCfg);
	tree.on('beforenodedrop',function(de){
		Ext.Ajax.request({
			url:'sys/menus/tree-dnd.action',
			params:{dragId:de.dropNode.id,dropId:de.target.id,point:de.point},
			success:function(xhr){
				var n = null;
				if(de.point == 'append'){
					n=de.target;
				}else{
					n=de.target.parentNode
				}
				var task = new Ext.util.DelayedTask(function(){
					treeLoader.load(n,function(node){
						node.expand();
					});
				});
				task.delay(200);
				
			}
		});
		//alert("dragId:"+de.dropNode.id +" dropId:"+de.target.id+" point:"+de.point);
	});
	tree.on("click",function(node){
		if(node.id == '0'){
			delAction.disable();
			form.hide();
		}else{
			delAction.enable();
			form.show();
			form.doLayout();
			var config = {
	    		url:'sys/menus/edit-form-data.action', 
	    		params:{beanId:node.id}
	    		};
	    	Ext.apply(config,commonLoadFormConfig);
	    	form.getForm().load(config);
		}
	});
	
	rootNode.on("append",function(){
		rootNode.fireEvent('click',rootNode);
	});
	
	var panel = new Ext.Panel({
		layout:'border',
		items:[tree,form]
	});
	
	return panel;
})();