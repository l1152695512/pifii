var myTabs;
function getTabs(){
	if(myTabs==null){
	    myTabs = new Ext.TabPanel({
		    renderTo: Ext.getBody(),
		    activeTab: 0,
		    border:false,
		    plugins: new Ext.ux.TabCloseMenu(),
		    enableTabScroll : true,
		    items: []
		})
	}
	return myTabs;
};


Ext.onReady(function(){
		//初始化全局 QuickTips 实例,为所有元素提供有吸引力和可定制的工具提示
		Ext.QuickTips.init();
		//表单元素的基类，提供事件操作、尺寸调整、值操作与其它功能
    	Ext.form.Field.prototype.msgTarget = 'side';  	
    	var tabPane=getTabs();
	   var tabs= new Ext.Panel({
           id:'tabs',
           layout:'fit',
           region:'center',
           collapsible:false,
           style:{
			margin:'0 0 0 0'
		  },
           items: [tabPane]
	   });
		   
		    //生成主菜单
		    var menu = menuCreate();   
		    var tb=createToolbar();
		    //一个表示程序可视区域的特殊容器（浏览器可视区域）。
		     new Ext.Viewport({
				layout: 'border',
				//title: 'Main',
				buttonAlign:'right',
				items: [
				 {
				 	id:'north-region',
					region: 'north',
					autoHeight:true,
					frame:false,
					cls:'icon-system-banner',
//					collapsible:true,
//					header : false,
					bodyStyle : 'margin:0px 0px 0px 0px;padding:0px 0px 0px 0px',
					items:[{
					        layout:'column',
					        height:120,
					        items:
					          [{
					             columnWidth:.80,
					             layout:'form',
					             border:false,
					             style:'padding:0px 0px 0px 0px',
					             labelWidth:30,
					             items:[{
					                   xtype:'label',
					                   id:'hiddenId',
					                   text:'.',   
					                   width:80
					                 }]
					          },{
					             columnWidth:.20,
					             layout:'form',
					             border:false,
					             style:'padding:0px 0px 0px 0px;float:right;font-weight:bold;color:white;',
					              labelWidth:80,
					             items:[{
					                    fieldLabel: "您好",  
					                    height:25,
                                        style:'float:left;font-size:12px;text-align:left;weight:12px;color:white;padding-top:3px;font-weight:bold;',
                                        xtype:'label',
                                        text:Ext.get('name').dom.value,
					                     width:115
					                   },
					                   {
					                     fieldLabel : "当前系统时间",  
					                     height:35,
                                                 id : "time",
                                               style:'float:left;font-size:12px;text-align:left;weight:12px;color:white;padding-top:3px;font-weight:bold;',
                                               xtype:'label',
                                                text:''
					                   
					                   },{
					                   	layout:'column',
					                   	border:false,
					                   	items:[{
					                   		columnWidth:1,
					                   		
					                   		layout:'form',
					                   		border:false,
					                   		items:[new Ext.ux.ImageButton({
					                   			imgPath:'./hr/img/icons/btn_hd_help.png',
					                   			imgHeight:32,
					                   			imgWidth:32,
					                   			title:'帮助',
					                   			style:'float:right;margin-right:5px;',
					                   			handler:function(btn){
					                   				window.location.href='main.action';
					                   					window.location.target='_top';
					                   			}
					                   		}),new Ext.ux.ImageButton({
					                   			imgPath:'./hr/img/icons/btn_hd_exit.png',
					                   			imgWidth:32,
					                   			imgHeight:32,
					                   			title:'注销',
					                   			style:'float:right;margin-right:3px;',
					                   			handler:function(){
					                   				Ext.Msg.confirm("温馨提醒","确定注销？",function(btn){
					                   					if(btn=="yes")
					                   					{
					                   						window.location.href='PersonPosition/j_spring_security_logout';
                                         					window.location.target='_top';
					                   					}
					                   				})
					                   			}
					                   		}),new Ext.ux.ImageButton({
					                   			imgPath:'./hr/img/icons/btn_hd_password.png',
					                   			imgWidth:32,
					                   			imgHeight:32,
					                   			style:'float:right;margin-right:3px;',
					                   			title:'修改密码',
					                   			handler:function(){
					                   				var item = {id:'100000',action:'sys/user/update-password.action',leaf:true,text:'密码修改'};
	                                        		onItemClick(item);
					                   			}
					                   		})]
					                   	}]
					                   }
					                   ]  
					              
					           }]
					
					      }]
				},{
					xtype:'panel',
					layout:'border',
					region:'center',
					items:[tb,tabs]
				}]
		    });
		    	Ext.TaskMgr.start({  
                        run : function() { 
                             
                            Ext.getCmp("time").setText(new Date()  
                                    .format('Y-m-d G:i:s'));  
                        },  
                        interval : 1000  
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
	                    			window.location.href='main.action';
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
    		//cls:'bak-color',
    		xtype:'toolbar',
			region:'north',
			height:29,
			id:'title-bar',
			defaults:{
			enableToggle:false,
				xtype:'button',
				style:{
					margin:'3 1 3 1'
				}
			}
		});
        
	tb.on("render",function(){
		Ext.Ajax.request({
		url : 'sys/menus/menu-data.action',
		success : function(xhr) {
			var menus = Ext.decode(xhr.responseText);

			Ext.each(menus, function(item, index) {
				tb.add('-');
				var sp = new Ext.Toolbar.Button({
					text : item.text,
					disabled : item.disabled,
					iconCls : item.iconCls,
					toggleHandler : function(button, press) {
						if (currentToggleMenu == this) {
							this.toggle(true, true);
							return;
						}
						if (currentToggleMenu)
							currentToggleMenu.toggle(false, true);
						if (press) {
							currentToggleMenu = this;
						}
					},
					menu : {
						items : []
					}
				});
				Ext.each(item.children, function(item, index) {
				
					var t = new Ext.menu.Item({
								text : item.text,
								action : item.action,
								leaf:true,
								handler : onItemClick,
								iconCls : item.iconCls,
								disabled : item.disabled
							});
					// 是子菜单
					if (item.leaf) {
						t.id = item.id;
						sp.menu.addItem(t);
					} else {
						// 二级菜单
						//t.leaf = false;
						t.menu = new Ext.menu.Menu({
									text : item.text,
									iconCls : item.iconCls
								});
						Ext.each(item.children, function(item, index) {
									var tt = new Ext.menu.Item({
												text : item.text,
												id : item.id,
												leaf:true,
												action : item.action,
												handler : onItemClick,
												iconCls : item.iconCls,
												disabled : item.disabled
											});
											if(item.leaf){
												t.menu.addItem(tt);
											}else{
												tt.menu = new Ext.menu.Menu({
													text:item.text,
													iconCls:item.icolCls
												});
												
												Ext.each(item.children, function(item,
													index) {
												var s = new Ext.menu.Item({
															text : item.text,
															id : item.id,
															action : item.action,
															leaf:true,
															iconCls : item.iconCls,
															disabled : item.disabled,
															handler : onItemClick
														});
												tt.menu.add(s);
											});

												t.menu.addItem(tt);

											}
											

									
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
        			Ext.Msg.alert("提示","请选择具体子菜单！");
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
    
    
    
    
    
    
    
    