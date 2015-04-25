	Ext.onReady(function(){
		//初始化全局 QuickTips 实例,为所有元素提供有吸引力和可定制的工具提示
		Ext.QuickTips.init();
		//表单元素的基类，提供事件操作、尺寸调整、值操作与其它功能
    		Ext.form.Field.prototype.msgTarget = 'side';  		
    		//一种基础性的tab容器	
	    var tabs = new Ext.TabPanel({
	        id:'tabs',
	        resizeTabs:true, // turn on tab resizing
	        region:'center',
	        minTabWidth: 135,
	        tabWidth:145,
	        enableTabScroll:true,
	        layoutOnTabChange:true,
	        margins: '2 0',
	        defaults: {autoScroll:true},
	        plugins: new Ext.ux.TabCloseMenu({
				closeTabText: '关闭标签',
				closeOtherTabsText: '关闭其他',
				closeAllTabsText: '关闭所有'
	        })
	    });
		    //生成主菜单
		    var menu = menuCreate();   
		    var tb=createToolbar();
		    //一个表示程序可视区域的特殊容器（浏览器可视区域）。
		    new Ext.Viewport({
				layout: 'border',
				title: 'Main',
				items: [
				//此部分到时可能用来放
				 {
					xtype: 'box',
					region: 'north',
					applyTo: 'header',
					height: 60
				},{
					xtype:'panel',
					layout:'border',
					region:'center',
					items:[
					/*
					
						{
					     xtype:'toolbar',
						region:'north',
						height:29,
						id:'title-bar',
						defaults:{
							enableToggle:true,
							xtype:'button',
							style:{
								margin:'3 1 3 1'
							}
						}
						},*/
						tb,
						//menu,
						tabs]
				}],
		        renderTo: Ext.getBody()
		    });
		    //Ext.getCmp("menus").get(0).collapse();//是否开始的时候合起菜单？
		    var loginForm = new Ext.FormPanel({
					        labelWidth: 75, 
					        frame:true,
					        title: '重登录',
					        bodyStyle:'padding:5px 5px 0',
					        width: 350,
					        defaults: {width: 150},
					        defaultType: 'textfield',
							
					        items: [{
					                fieldLabel: '用户名',
					                name: 'j_username',
					                allowBlank:false
					            },{
					                fieldLabel: '密码',
					                name: 'j_password',
					                allowBlank:false,
					                inputType: 'password'
					            }]
	                });
		    
		    //
			var loginWin = new Ext.Window({
	                layout:'fit',
	                width:300,
	                height:200,
	                modal:true,
	                closable:false,
	                id:'login-window',
	                items: [loginForm],
	                buttons: [{
	                    text:'登录',
	                    handler:function(){
	                    	Ext.Ajax.request({
	                    		url : 'j_spring_security_check',
	                    		params:{
	                    				j_username:loginForm.getForm().findField("j_username").getValue(),
	                    				j_password:loginForm.getForm().findField("j_password").getValue()
	                    		},
	                    		success:function(xhr){
	                    			loginForm.getForm().reset();
	                    			loginWin.hide();
	                    		}
	                    	});
	                    }
	                }]
	            });
		    deskTop(tabs);
		
	});
	
    var currentToggleMenu = null; 
    //start
    function createToolbar(){
    	 var tb = new Ext.Toolbar({
					     xtype:'toolbar',
						region:'north',
						height:29,
						id:'title-bar',
						defaults:{
							enableToggle:true,
							xtype:'button',
							style:{
								margin:'3 1 3 1'
							}
						}
		});
        
	tb.on("render",function(){
		Ext.Ajax.request({
			url:'sys/menus/menu-data.action',
			success:function(xhr){
			var menus = Ext.decode(xhr.responseText);
			Ext.each(menus,function(item,index){
					tb.add('-');
					var sp=new Ext.Toolbar.SplitButton({
						text:item.text,
						iconCls: item.iconCls,
						toggleHandler:function(button,press){
							if(currentToggleMenu == this){
								this.toggle(true,true);
								return;
							}
						     if(currentToggleMenu)currentToggleMenu.toggle(false,true);
						     if(press){
								currentToggleMenu = this;
						     }
						},
					 menu : {items: []}
					});
					Ext.each(item.children,function(item,index){
						var t=new Ext.menu.Item({
							    text:item.text,
							    action:item.action,
							    leaf:true,
							    handler: onItemClick,
							    iconCls: item.iconCls
						 });
						 //是子菜单
						if(item.leaf){
						    t.id=item.id;
						    sp.menu.addItem(t);
						}
						else{
							//二级菜单
							t.leaf=false;
							t.menu=new Ext.menu.Menu({
							    text:item.text
							    ,iconCls: item.iconCls
							    }
							  );
							 Ext.each(item.children,function(item,index){
								  var tt=new Ext.menu.Item({
									    text:item.text,
									    id:item.id,
									    leaf:true,
									    action:item.action,
									    handler: onItemClick,
									    iconCls: item.iconCls
								   });
								  t.menu.addItem(tt);
								  
						      });	
						       sp.menu.addItem(t);
						}
					});					
					tb.add(sp);
					
				});
				tb.add('-');
				tb.doLayout();

			}
		});
	});				
    	 return tb;
    } 
    //保留
    function createToolbar2(){
    	 var tb = new Ext.Toolbar({
					     xtype:'toolbar',
						region:'north',
						height:29,
						id:'title-bar',
						defaults:{
							enableToggle:true,
							xtype:'button',
							style:{
								margin:'3 1 3 1'
							}
						}
		});

        
	tb.on("render",function(){
		Ext.Ajax.request({
			url:'sys/menus/menu-data.action',
			success:function(xhr){
			var menus = Ext.decode(xhr.responseText);
			var tree;
			Ext.each(menus,function(item,index){
				    tree= new Ext.tree.AsyncTreeNode({
										expanded : true,
										children : item.children
						});
					tb.add('-');
					tb.add({
						text:item.text,
						iconCls: item.iconCls,
						toggleHandler:function(button,press){
							if(currentToggleMenu == this){
								this.toggle(true,true);
								return;
							}
						     if(currentToggleMenu)currentToggleMenu.toggle(false,true);
						     if(press){
								currentToggleMenu = this;
						     }
						},
					 menu : {
	                       items: [
	                       new Ext.tree.TreePanel({
							id : item.id,
							split : true,
							lines:true,
							expanded:true,
							rootVisible:true,
							autoHeight:true,
							autoScroll : true,
							animate:true,
							frame : true,
							rootVisible : false,
							lines : false,
							singleExpand : false,
							useArrows : true,
							listeners:{
								click:menuSelFunction
							},
							
						    root :tree
	
						})
						]

					   }
					});
					tree.expandChildNodes(true);
				});
				tb.add('-');
				tb.doLayout();

			}
		});
		
	});					
    	 return tb;
    }

    function onItemClick(item){
        	var layoutAttr =  {
			layout : "fit"
		};
		
       if (item.leaf) { 
        		if(item.action==''||item.action=="undefined"||item.action==undefined){
        			Ext.Msg.alert("提示","你未设置action路径");
        		    return;
        		}	
			addLazyLoadWorkSpaceTab(item.id+"Tab",item.text,item.action+"?search=other",layoutAttr,null,item.iconCls);
		}
	
    }    
    	var menuSelFunction = function(n) {
		var sn = this.selModel.selNode || {}; // selNode is null on initial selection
		var layoutAttr = n.attributes.layoutAttr || {
			layout : "fit"
		};//自定义布局，默认布局是fit，自定义布局来自属性菜单定义
		var action = n.attributes.action || n.id+".action";	
		if (n.leaf) { // ignore clicks on folders and currently selected node 
			var tabId = n.id + "Tab";
		
			var tabTitle = n.text;
			addLazyLoadWorkSpaceTab(tabId,tabTitle,action,layoutAttr,null,n.attributes.iconCls);
		}
	}
    
    
    
    
    
    
    
    