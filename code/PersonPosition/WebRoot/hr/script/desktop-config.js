

function getCurArgs(argName) {
	var args = new Object();
	var query = location.search.substring(1); // Get query string
	var pairs = query.split("&"); // Break at ampersand
	for (var i = 0; i < pairs.length; i++) {
		var pos = pairs[i].indexOf('='); // Look for "name=value"
		if (pos == -1)
			continue; // If not found, skip
		var argname = pairs[i].substring(0, pos); // Extract the name
		var value = pairs[i].substring(pos + 1); // Extract the value
		value = decodeURIComponent(value); // Decode it, if needed
		args[argname] = value; // Store as a property
	}
	if ((argName || "") == "")
		return args; // Return the object,you can use it by this method:
	// "getCurArgs()["xx"]" or "getCurArgs().xx"
	else
		return args[argName]; // return the element,you can use it by this
	// method: getCurArgs("xx")
}
function openLoadTabPanel(ids){
	var layoutAttr =  {
						layout : "fit"
				  };
				  this.addLazyLoadWorkSpaceTab("searChTabs", "机楼信息查询", "input/building/grid-view.action?bid="+ids, layoutAttr,null, null);
}
function openMsg(){
   //Ext.Msg.alert("提示", "由于这是您第一次查看地图，请先初始化数据");
}

function deskTop(tabs) {
	   
   var layoutAttr = {layout : "border"};
	addLazyLoadWorkSpaceTab("work-desk", "首页", "map/grid-view.action", layoutAttr,null, null);		
	//addLazyLoadWorkSpaceTab("work-desk", "工作台", "jngh/contract/contractinf/grid-view.action", layoutAttr,null, null);		
}

var currentToggleMenu = null;
  function openPortal(params){
		     window.parent.window.location.href="../main.action?bid="+params+"&search=map"; 
 }
function menuCreate() {
	var menuPanel = new Ext.Panel({
				title : '菜单',
				id : 'menus',
				iconCls : 'main-menu-icon',
				region : 'west',
				width : 200,
				margins : '2 4',
				cmargins : '2 4',
				layout : 'card',
				collapsible : true,
				layoutConfig : {
					//layout-specific configs go here
					animate : true
					//activeOnTop: true
				}
		});
			
	var bid = getCurArgs("bid");//获取参数值
	var search=getCurArgs("search");
	
	var menuSelFunction = function(n) {
			var sn = this.selModel.selNode || {}; // selNode is null on
													// initial
			// selection
			var layoutAttr = n.attributes.layoutAttr || {
				layout : "fit"
			};// 自定义布局，默认布局是fit，自定义布局来自属性菜单定义
			var action = n.attributes.action || n.id + ".action";
			if (n.leaf) { // ignore clicks on folders and currently selected
							// node
				var tabId = n.id + "Tab";
				var tabTitle = n.text;
				
				addLazyLoadWorkSpaceTab(tabId, tabTitle, action, layoutAttr,
						null, n.attributes.iconCls);
			}

	  }
	
	/*
	if (typeof bid =="undefined") {
		var menuSelFunction = function(n) {
			var sn = this.selModel.selNode || {}; // selNode is null on
													// initial
			// selection
			var layoutAttr = n.attributes.layoutAttr || {
				layout : "fit"
			};// 自定义布局，默认布局是fit，自定义布局来自属性菜单定义
			var action = n.attributes.action || n.id + ".action";
			if (n.leaf) { // ignore clicks on folders and currently selected
							// node
				var tabId = n.id + "Tab";
				var tabTitle = n.text;
				
				addLazyLoadWorkSpaceTab(tabId, tabTitle, action, layoutAttr,
						null, n.attributes.iconCls);
			}

		}
	} else if(bid!="undefined"||search=="map") {
		addLazyLoadWorkSpaceTab("searChTabs", "机楼信息管理", "input/building/grid-view.action?search=map", "fit",
						null, bid);
	} */
	/*
	if(bid!="undefined"||search=="map") {
		addLazyLoadWorkSpaceTab("searChTabs", "机楼信息管理", "input/building/grid-view.action?search=map", "fit",
						null, bid);
	}*/
	
	

	menuPanel.on("render", function() {
		var loadMask = new Ext.LoadMask(this.body, {
					removeMask : true,
					msg : "请稍等..."
				});
		loadMask.show();
		Ext.Ajax.request({
			url : 'sys/menus/menu-data.action',
			success : function(xhr) {
				var menus = Ext.decode(xhr.responseText);
				var titleBar = Ext.getCmp('title-bar');
				Ext.each(menus, function(item, index) {
				       

							titleBar.add('-');
							titleBar.add({
										text : item.text,
										iconCls : item.iconCls,
										toggleHandler : function(button, press) {
											// if(!currentToggleMenu)return;
											if (currentToggleMenu == this) {
												this.toggle(true, true);
												return;
											}
											if (currentToggleMenu)
												currentToggleMenu.toggle(false,
														true);
											if (press) {
												var card = menuPanel.get(index);
												menuPanel.layout
														.setActiveItem(index);
												card.el.hide().slideIn('l', {
															stopFx : true,
															duration : .2
														});
												currentToggleMenu = this;
											}
										}
									});
							menuPanel.add(new Ext.tree.TreePanel({
										id : item.id,
										split : true,
										minSize : 100,
										autoScroll : true,

										// tree-specific configs:
										rootVisible : false,
										lines : false,
										singleExpand : false,
										useArrows : true,
										listeners : {
											click : menuSelFunction
										},
										root : new Ext.tree.AsyncTreeNode({
													expanded : true,
													children : item.children
												})

									}));
						});
						
				titleBar.add('-');
				titleBar.doLayout();
				titleBar.get(1).toggle(true);
				menuPanel.doLayout();
				menuPanel.layout.setActiveItem(0);
				loadMask.hide();
			}
		});
	});

	return menuPanel;
}