<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var pageLimit = 20;
	var rowId = 'bid';
	var gridDataUrl = "mjgl/map/grid-data.action";
	var maplet = null;
	var user = new Ext.form.Label({
		html:'内容：'
	});
	var uText=new Ext.form.TextField({
		xtype : 'textfield',
		name : 'key',	
		width:100				
	});
	var query = new Ext.Action({
		text : '查询',
		iconCls : 'icon-find',
		handler : function() {
			if(uText.getValue() == ''){
				Ext.Msg.alert("提示","请输入查询内容！");
				uText.focus();
 	        	return;
			}
			var key = "'"+uText.getValue()+"'";
			Ext.TaskMgr.stopAll();
			loadStore(key,'0');
		}
	});
	var rootId = '0';
	var rootNode = new Ext.tree.AsyncTreeNode({
		text : '所有基站/机楼',
		id : rootId,
		expanded : true
	});
	var treeLoader = new Ext.tree.TreeLoader({
		dataUrl: 'mjgl/map/labs-tree-data.action'
	});
	treeLoader.on("beforeload", function(loader, node) {
        this.baseParams.nodeId = node.id;
    });
 	
	

    var userTree =  new Ext.tree.TreePanel({
     	region:'west',
     	//header : false,
     	title:'定位查询',
    	collapsible: true,
	    autoScroll : true,
	    width:230,
      	animate : true,
       	containerScroll : true,
       	lines:true,
       	rootVisible : true,
	    autoScroll : true,
	    root: rootNode,
	    loader : treeLoader,
  	    plugins:[
  	        new Ext.plugin.tree.TreeNodeChecked({
 	        	cascadeCheck: true,
 	          	cascadeParent: false,
 	          	cascadeChild: true,
  	          	linkedCheck: false, 
  	          	asyncCheck: false, 
 	          	displayAllCheckbox: false
	        })
 	    ],
 	     tbar:[' ',   
             new Ext.form.TextField({   
             	width:150,   
                emptyText:'快速检索',   
                enableKeyEvents: true,   
               	listeners:{   
                    keyup:function(node, event) {   
                        findByKeyWordPath(node, event); 
                    },   
                    scope: this  
                }   
             })   
        ],  
 	    listeners:{
			"click":function(node){
				if(node.attributes.type != undefined){
					window.maplet.clearOverlays(true);
					window.maplet.setMode("pan");
					if(node.attributes.type == '0'){
						store.setBaseParam("code",node.id);
						store.setBaseParam("type",node.attributes.type);
						store.reload();
					}else{
						var bid = node.id.substring(5, node.length);
						store.setBaseParam("code",bid);
						store.setBaseParam("type",node.attributes.type);
						store.reload();
					}
				}
			},
			"collapse":function(){
			},
			"bodyresize":function(){
				this.setWidth(mainPanel.getWidth()*0.2);
			}
			
		}
	});
   

    var tcenter=new Ext.form.Label({
		html:'地图中心：'
	});
	var tadrr=new Ext.form.Label({
		html:'广州省>广州市>天河区>天河北路>光大银行大厦'
	});
    var mapPanel = new Ext.Panel({
    	header : false,
     	region:'center',
        layout:'fit',
	    html: '<div id="dzmap" style="width:500px;height:300px">'
	    //tbar:['&nbsp;&nbsp;',tcenter,tadrr]
    });
    
       
    
	var storeFields = [
            'name','remards','type',
            {name: 'completionDate', type: 'date', dateFormat: 'Y-m-d'},'cityId','cityName',
            'bid','ywRegion','code','fullName','shortName','proRight','layers','totalArea','availableArea','usedArea','remainArea','totalUtilization','officeArea','businessArea','otherArea','transCapacity','generCapacity','exisRoomLoad','availCapacity','couldExpansion',' plaAdvice','bgc','categoryName','businessState','XCoordinate','YCoordinate','address','airCondition'
    ];  
    
    var store = new Ext.data.JsonStore({
        root: 'list',
        autoLoad: {params:{start:0, limit:pageLimit}},
        totalProperty: 'totalRecord',
        idProperty: rowId,
        remoteSort: true,
        fields: storeFields,
        url: gridDataUrl,
        listeners:{
			"load":function(t,r,o){
				if(r != ''){
					window.maplet.clearOverlays(true);
				 	addMarker(t,r);
	 				window.maplet.setAutoZoom();
 				}
			}
		}
    }); 	
    
    var gridColumns = [
    	new Ext.grid.RowNumberer({
    		header:'序号',
    		align:'center',
    		width: 40,
    		renderer: function(value,metadata,record,rowIndex){
    			return store.lastOptions.params.start + 1 + rowIndex;
    		}
    	}),
    	{
            header: "编码",
            dataIndex: 'code',
            align: 'center',
            sortable: true
        },{
            header: "简称",
            dataIndex: 'shortName', 
            align: 'center',
            sortable: true
        },
        {
            header: "所在城市",
            dataIndex: 'cityName', 
            align: 'center',
            sortable: true
        },{
            header: "所在区域",
            dataIndex: 'ywRegion', 
            align: 'center',
            sortable: true
        },{
            header: "产权归属",
            dataIndex: 'proRight', 
            align: 'center',
            sortable: true
        },{
            header: "类型",
            dataIndex: 'type', 
            align: 'center',
            sortable: true,
            renderer: function(value,metadata,record,rowIndex){
    			if(value == '1'){
    				return '机楼';
    			}else{
    				return '基站';
    			}
    		}
        }];
        
        
    store.setDefaultSort('bid', 'desc');
    
    //grid事件定义
    var gridListeners = {
       	cellclick:function(grid, rowIndex, columnIndex, e) {
		   if(columnIndex != 0 && columnIndex<7){
		    	var rec = grid.store.getAt(rowIndex);
       			window.maplet.clearOverlays(true);
       			window.maplet.setMode("pan");
				var marker = 'm'+rec.data.bid;
				var icon = "img/map/home.png";
				if(rec.data.type == '2'){
				    icon = "img/map/wz.png";
				}
				var options = {
					xoffset: 30,
					yoffset: 0,
					opacity: 100,
					enableStyle: false,
					visible: true
				};
		     	marker = new MMarker(   
			    	new MPoint(rec.data.XCoordinate,rec.data.YCoordinate),   
			    	new MIcon(icon, 32, 32),   
			    	null,
			    	new MLabel("<h2 style='color:red'>"+rec.data.shortName+"</h2>",options)   
				);
				MEvent.addListener(marker, "click", function() {   
	                 openInfo(rec);
	        	})  
				window.maplet.addOverlay(marker);
				window.maplet.centerAndZoom(new MPoint(rec.data.XCoordinate,rec.data.YCoordinate),14);
	 			window.maplet.setAutoZoom();
			}
		}
    };
    
    var gridPanel = new Ext.grid.GridPanel({
        header : false,
        region:'south',
        stripeRows: true,
        height:150,
        store: store,
        ref:'reloadOnActivate',//自定义属性，作用是当被激活时自动从新加载数据
        trackMouseOver:true,
        stripeRows: true,
        disableSelection:true,
        loadMask: true,
        columns:gridColumns,
        viewConfig: {
            forceFit:true
        },
        bbar: new Ext.PagingToolbar({
            pageSize: pageLimit,//pageSize 表示每页有几条记录,
            store: store,
            displayInfo: true,
            displayMsg: '第{0} - {1}行,共 {2}行',
            emptyMsg: "空"
        }),
        listeners:gridListeners
    });
    
    
   //*********************************************
    
    //mapPanel渲染之后实例化mapbar
    mapPanel.on('render',function(){
	    window.maplet = new Maplet("dzmap");
		window.maplet.centerAndZoom(new MPoint(113.25191,23.1212), 8);
		window.maplet.addControl(new MStandardControl());
		window.maplet.resize(mapPanel.getWidth(), mapPanel.getHeight()-30);
		mapPanel.on('bodyresize',function(){
			window.maplet.resize(mapPanel.getWidth(), mapPanel.getHeight()-30);
    	});
    	window.maplet.setIwStdSize(400,200);   
    },mapPanel,{delay:500});
    
    
    //*********************************************
	
	//加标注
	function addMarker(t,r){
		var options = {
			xoffset: 30,
			yoffset: 0,
			opacity: 100,
			enableStyle: false,
			visible: true
		};
		
		t.each(function(r){
			var marker = 'm'+r.data.bid;
			var icon = "img/map/home.png";
			if(r.data.type == '2'){
			    icon = "img/map/wz.png";
			}
	     	marker = new MMarker(   
		    	new MPoint(r.data.XCoordinate,r.data.YCoordinate),   
		    	new MIcon(icon, 32, 32),   
		    	null,
		    	new MLabel("<h2 style='color:red'>"+r.data.shortName+"</h2>",options)   
			);
			MEvent.addListener(marker, "click", function() {   
                 openInfo(r);
        	})  
			window.maplet.addOverlay(marker);
		}); 
	}
	
	function openInfo(record){
		var station = '';
		  				
		Ext.Ajax.request({
  			url : "mjgl/map/station-view.action",
  			params: {bid:record.id,name:record.data.shortName},
  			success:function(xhr){
  				station = eval(xhr.responseText);
  			}
 		});	
		
		
		Ext.Ajax.request({
   			url : "mjgl/map/floor-view.action",
   			params: {editId:record.id},
   			success:function(xhr){
   				var newForm = eval(xhr.responseText);
   				if(newForm==undefined||newForm=="undefined")return ;//没有权限	
   				
  				var tabs = new Ext.TabPanel({
			        activeTab: 0,
			        deferredRender: false,
			        autoTabs: true,
			        resizeTabs: true, 
			        items: [{
			            title: '机楼信息',
			            items: [newForm]
			        },{
			            title: '机房信息',
			            items: [station]
			        }]
			    });
   				
   				var newWin = new Ext.Window({
   					title: record.data.shortName,
   					width: 800,
   					height:500,
			        modal :true,
			        layout: 'fit',
			        plain:true,
			        constrain: true,
			        closable: true,
			        bodyStyle:'padding:5px;',
			        buttonAlign:'center',
			        items: [tabs],       
			        buttons: [{
			            text: '关闭',
			            width: 45,
			            handler:function(){
			            	newWin.close();
			            }
			        }]
   				});
   				newWin.show();
   			}
   		});
	}
	
	
	
	
	
	
	
	
	
	
	var timeOutId = null; 
	
	/** 
	 * 通过关键字查找树的节点，并定位，这儿有2种实现方式， 1.通过treeFilter，当数据量小的时候是可行的 2.通过返回tree路径，对应数据量大的时候 
	 * 支持模糊查询 
	 *  
	 * @param {} 
	 *            node 
	 * @param {} 
	 *            event 
	 * @author centre 
	 */  
	var findByKeyWordPath = function(node, event) {  
	    // 清除timeOutId   
	    clearTimeout(timeOutId);  
	    timeOutId = setTimeout(function() {  
	        // 获取输入框的值。   
	        var text = node.getValue().trim();  
	        // 采用ajax获得需要展开的路径   
	        if (text != "") {  
	         	Ext.Ajax.request({  
                   url : 'mjgl/map/filter-tree-data.action',  
                   success : function(response, opts) {  
                       var obj = Ext.decode(response.responseText); 
                       var length = obj.length;  
                       userTree.root.reload();  
                       for (var i = 0; i < length; i++) {  
                           var path = obj[i].path;  
                           userTree.expandPath(path,'id',onExpandPathComplete);  
                      }  
                   },  
                   failure : function(response, opts) {  
                       Ext.Msg.alert("错误提示", "请求失败,请与开发人员联系。")  
                               .setIcon(Ext.MessageBox.ERROR);  
                   },  
                   params : {  
                       keyWord : text  
                   }  
               });  

	              
	        } else { 
	        
	        }  
	     }, 500);  
	}  
	  
	function onExpandPathComplete(bSuccess, oLastNode) {  
	    if (!bSuccess)  
	        return;  
	    oLastNode.ensureVisible();  
	    oLastNode.select(); 
	   // oLastNode.fireEvent('click', oLastNode);  
	    
	} 
	
	
	
	var treeFilter = new Ext.tree.TreeFilter(userTree, {  
        clearBlank : true,  
        autoClear : true  
    });  
	// 保存上次隐藏的空节点   
	var hiddenPkgs = []; 
	
	var judge =function(n,re){    
	    var str=false;  
	    n.cascade(function(n1){  
	     if(n1.isLeaf())  
	     {  
	      if(re.test(n1.text)){str=true;return;}  
	     }  
	    else{  
	          
	       if(re.test(n1.text)){str=true;return;}  
	     }  
	    });  
       
     	return str;  
    };  
	
	var timeOutId2 = null;
	var findByKeyWordFiler = function(node, event) {  
	    // 清除timeOutId2   
	  	clearTimeout(timeOutId2);  
   	 	// 为了避免重复的访问后台，给服务器造成的压力，采用timeOutId进行控制，如果采用treeFilter也可以造成重复的keyup   
    	timeOutId2 = setTimeout(function() {  
             // 获取输入框的值。   
             var text = node.getValue();  
             // 根据输入制作一个正则表达式，'i'代表不区分大小写   
             var re = new RegExp(Ext.escapeRe(text), 'i');  
             // 先要显示上次隐藏掉的节点   
             Ext.each(hiddenPkgs, function(n) {  
                n.ui.show();  
             });  
             hiddenPkgs = [];  
             if (text != "") {  
                   
                 treeFilter.filterBy(function(n) {  
                             // 只过滤叶子节点，这样省去枝干被过滤的时候，底下的叶子都无法显示   
                             return !n.isLeaf() || re.test(n.text);  
                        });  
                 // 如果这个节点不是叶子，而且下面没有子节点，就应该隐藏掉   
                 userTree.root.cascade(function(n) {         
                         if(n.id!='0'){  
                            if(!n.isLeaf() &&judge(n,re)==false&& !re.test(n.text)){  
                             hiddenPkgs.push(n);  
                             n.ui.hide();  
                             }   
                          }  
                       });    
                   
             } else {  
                 treeFilter.clear();  
                return;  
             }  
  
        }, 600);  
	}  
	 

	
	
	
	//*********************************************
    
    //右边视图
    var centerPanel = new Ext.Panel({
        header : false,
        border : false,
        region:'center',
        layout:'border',
	   	items:[mapPanel,gridPanel]
    });
    
    //主panel
    var mainPanel = new Ext.Panel({
        layout:'border',
	    items:[userTree,centerPanel]
    });
    
   return mainPanel;
})();