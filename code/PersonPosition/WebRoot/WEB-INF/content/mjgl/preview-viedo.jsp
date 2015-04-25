<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
function loadVideos(){
	   	Ext.Ajax.request({
       		url : 'mjgl/videos-data.action',
       		params : {id : '${id}'},
            success : function(xhr) {
        		var responseText = Ext.decode(xhr.responseText);       		
        		var currentDate = "";
            	var dataList = responseText.list;
            	
            	for(var i=0;i<dataList.length;i++){
            		if(dataList[i].videoTime != currentDate){
						
					currentDate = dataList[i].videoTime;
					$("#viedo_div_outer").append(
						'<div>' +
							'<div class="picTitle">'+dataList[i].videoTime+'</div>' +
						
							'<div class="pics"></div>' +
						'</div>');
				}
				$("#viedo_div_outer").children().last().find(".pics").append(
							'<div class="pic">' +
						 		'<img src="'+dataList[i].videoContent+'"/>' +
						 		'<div>'+dataList[i].vidTime+'</div>' +
							'</div>');
			}
			$("#viedo_div_outer").find(".pics").append('<div style="clear:both;"></div>');
			
            }
            	
            	 	});
            	 	
           	$("#viedo_div_outer").css({"font-size":"20px"});
			$("#viedo_div_outer").children().css({"margin-bottom":"20px"});
			$("#viedo_div_outer").find(".picTitle").css({"width":"100%","height":"30px","line-height":"30px","background":"#6ce2f4","margin-bottom":"15px","font-size":"23px","font-weight":"bold"});
			$("#viedo_div_outer").find(".pic").css({"float":"left","margin-left":"20px","margin-bottom":"10px"});
			$("#viedo_div_outer").find(".pic").children("div").css({"text-align":"center","background":"#6ce2f4"});
			
			}
	  
	var shot_panel = new Ext.Panel({
		header : false,
		border : false,
		modal : true,
		region : 'center',
		height : 300,
		frame : true,
		autoScroll:true,
		width : "600px",	
		html : '<div id="viedo_div_outer">' +
					/*'<div>' +
						'<div class="picTitle">2014-05-03</div>' +
						'<div class="pics">'+
							'<div class="pic">' +
						 		'<img src="photos/p3.jpg" class="photo-list-image"/>' +
						 		'<div>15:11:14</div>' +
							'</div>' +
							'<div style="clear:both;"></div>'+
						'</div>' +
					'</div>' +
					'<div>' +
						'<div class="picTitle">2014-05-03</div>' +
						'<div class="pics">'+
							'<div class="pic">' +
						 		'<img src="photos/p3.jpg"/>' +
						 		'<div>15:11:14</div>' +
							'</div>'+
							'<div style="clear:both;"></div>'+
						'</div>' +
					'</div>' +*/
				'</div>',
				

		listeners:{"afterrender":function(panel){ 
					loadVideos();
			} 
		}
			 
	});
	 
	var top_panel = new Ext.Panel({
				title : '',
				region : 'north',
				layout : 'table',
				width : 800,
				height : 50,
				frame : true,
				items : [{
					columnWidth : .25,
					defaults : {
						width : 80
					},
					//align : 'west',
					layout : 'form',
					border : false,
					items : [{
								xtype : 'datefield',
								// text:'从',
								name : 'startDate',
								format : 'Y-m-d',
								width : 180,
								fieldLabel :'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;从',
								// columnWidth:0.4,
								// border:false,
								// editable:false,
								// allowBlank : false,
								value : Ext.Date.add(new Date(), Ext.Date.DAY,-3),
								maxValue : new Date()
							}]
				}, {
					columnWidth : .25,
					defaults : {
						width : 80
					},
					//align : 'north',
					layout : 'form',
					border : false,

					items : [{
								xtype : 'datefield',
								name : 'endDate',
								format : 'Y-m-d',
								width : 180,
								// text:'到',
								fieldLabel : '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;到',
								// columnWidth:0.1,
								// border:false,
								// editable:false,
								// allowBlank : false,
								value : new Date(),
								maxValue : new Date()
							}]
				}, {
					layout : 'form',
					width : 100,
					border : false,
						defaults : {
						width : 80
					},
					items : [{
								xtype : "button",
								text : "查询",
								handler : function() {
								loadVideos().reload();
								}
							}]
				}]
			});

	   var previewMapWin = new Ext.Window({
		title: '视频显示',
        width: 800,
        height:600, 
        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        resizable:false,
        items:[top_panel,loadVideos]
	});
    return previewMapWin;
})();

