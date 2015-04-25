<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function() {
	function loadPhotos(){
		var params = leftPanel.getForm().getValues();
		params.id = '${id}';
	   	Ext.Ajax.request({
       		url : 'mjgl/photo-data.action',
       		params : params,
            success : function(xhr) {
        		var responseText = Ext.decode(xhr.responseText);       		
        		var currentDate = "";
            	var dataList = responseText.list;
            	if(dataList.length == 0){
            		$("#photo_div_outer").html("<h1>无数据！</h1>");
            	}else{
            		$("#photo_div_outer").html("");
	            	for(var i=0;i<dataList.length;i++){
	            		if(dataList[i].photoDate != currentDate){
							currentDate = dataList[i].photoDate;
							$("#photo_div_outer").append(
								'<div>' +
									'<div class="picTitle">'+currentDate+'</div>' +
									'<div class="pics"></div>' +
								'</div>');
						}
						$("#photo_div_outer").children().last().find(".pics").append(
								'<div class="pic">' +
							 		'<img src="'+dataList[i].smallImageSrc+'"/>' +
							 		'<div>'+dataList[i].photoTime+'</div>' +
								'</div>');
					}
					$("#photo_div_outer").find(".pics").append('<div style="clear:both;"></div>');
					
			       	$("#photo_div_outer").css({"font-size":"20px"});
					$("#photo_div_outer").children().css({"margin-bottom":"20px"});
					$("#photo_div_outer").find(".picTitle").css({"width":"100%","height":"30px","line-height":"30px","background":"#98D2E8","margin-bottom":"15px","font-size":"23px","font-weight":"bold"});
					$("#photo_div_outer").find(".pic").css({"float":"left","margin-left":"20px","margin-bottom":"10px"});
					$("#photo_div_outer").find(".pic").find("img").css({"width":"200px"});
					$("#photo_div_outer").find(".pic").children("div").css({"text-align":"center","background":"#B2CFDA"});
            	}
			}
		});
	}
	var shot_panel = new Ext.Panel({
		header : false,
//		border : false,
		modal : true,
		region : 'center',
		height : 600,
		//frame : true,
		width : 1150,	
		html : '<div>' +
					'<div id="photo_div_outer">' +
						/*'<div>' +
							'<div class="picTitle">2014-05-03</div>' +
							'<div class="pics">'+
								'<div class="pic">' +
							 		'<img src="photos/p3.jpg" class="photo-list-image"/>' +
							 		'<div>15:11:14</div>' +
								'</div>' +
								'<div style="clear:both;"></div>'+
							'</div>' +
						'</div>' +*/
					'</div>' +
				'</div>',
				

		listeners:{"afterrender":function(panel){
			var height = $("#photo_div_outer").parent().parent().css("height");
			console.debug(height);
			$("#photo_div_outer").parent().css({"height":height,"overflow":"auto"});
			loadPhotos();
			} 
		}
			 
	});
	
	var leftPanel = new Ext.FormPanel({
		title : '搜索',
		region : 'west',
       	margins:'3',
       	labelWidth: 60, 
	    displayfieldWidth: 25, 
//        frame:true,
        bodyStyle:'padding:5px 5px 10px',
        width: 240,
        defaults: {anchor: '100%'},
        defaultType: 'textfield',
        buttonAlign :'center',
        autoHeight:true,
      	border:false,
//        split:true,
        collapsible:true,
        collapseMode:'mini',
        items: [{
            xtype : 'datefield',
            name : 'startDate',
            format : 'Y-m-d', 
            fieldLabel : '开始时间',
            border:false,
       		editable:false,
       		allowBlank : false,
       		value:Ext.Date.add(new Date(), Ext.Date.DAY, -3),
       		maxValue : new Date()
        },{
            xtype : 'datefield',
            name : 'endDate',
            format : 'Y-m-d', 
            fieldLabel : '结束时间',
            border:false,
       		editable:false,
       		allowBlank : false,
            value:new Date(),
            maxValue : Ext.Date.add(new Date(), Ext.Date.DAY, 1)
        }],//,is_done_show
        buttons:[{
         	text:'查询',
         	handler: function () {
			 	loadPhotos();
		    }
        },{
         	text:'重置',
         	handler: function () {
		      	leftPanel.getForm().reset();
		    }
        }]  
    });
	var shot_win = new Ext.Window({
		title : '照片查看',
		autoWidth:true,
		height : 635,
		modal : true,
		layout : 'border',
//		border:false,
        constrain: true,
        closable: true,
        autoScroll : true,
        resizable:false,
		items : [leftPanel,shot_panel]
	});
	return shot_win;
})();
