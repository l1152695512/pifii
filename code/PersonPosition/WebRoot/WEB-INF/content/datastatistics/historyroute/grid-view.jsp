<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

(function(){
	var mapObject;
//	var animateFinish = false;
	var walkPointToPointTimes = 1000;//从上一个轨迹移动到新轨迹使用的时间，时间越大移动的越慢，理论上如果该值等于refreshTime的话，人的移动会比较有连贯性。
//	var maxRoutePointDistance = 1.0;//决定画路线时点的密度，值越大，密度越小
	//历史轨迹查询
	function getHistoryRoute(){
		Ext.Msg.wait('正在加载路线数据....', '提示');
	   	Ext.Ajax.request({
       		url : 'datastatistics/historyroute/get-history-route.action',
       		params: leftPanel.getForm().getValues(),
            success : function(xhr) {
            	Ext.Msg.hide(); 
            	var responseText = Ext.decode(xhr.responseText);
            	var dataList = responseText.list;
            	if(dataList.length > 2){
            		drow_route(dataList,0);
            	}else{
            		Ext.Msg.alert("提示","无历史轨迹！");
            	}
           	},
		    failure: function(xhr){
		    	Ext.Msg.hide(); 
		    	Ext.Msg.alert("提示","获取数据信息失败，稍后请重试！");
		    }
        })
	};
//	var per_distance_time = 10;
	var maxLineNum = 50;
	function drow_route(data_list,index){
		if($('#history_route_img').html() != undefined){//如果该窗口没有关闭
			movePositionToMapCenter(data_list[index].locationX,data_list[index].locationY);
			if(data_list[index].date != undefined){ 
				$('#route_time_div').append('<div style="padding:2px 0px 2px 10px;cursor: pointer;border-bottom: 1px solid rgb(167, 167, 170);" data-location-x="'+data_list[index].locationX+
							'" data-location-y="'+data_list[index].locationY+'"><img style="margin-right: 5px;" src="hr/img/icons/clock.ico"></img>'+data_list[index].date +'</div>');
			}
//			var routePointHtml = '<div class="ip_tooltip ip_img32" ' + 'style="top: '+data_list[index].locationY+
//					'px; left: '+ data_list[index].locationX+'px;" data-button="route-plan-point"></div>';//线之间的点
//			$('#history_route_locations').append(routePointHtml);
//			var currentPoint = $('#history_route_locations').find('.ip_tooltip:last');
//			$("#history_route_map").iPictureInsert(currentPoint);
			
			if($('#route_time_div').children().length > maxLineNum){
				Ext.Msg.confirm('确认', '轨迹数据过多，点击“是”清除当前轨迹继续后面的轨迹显示，点击“否”终止后面的轨迹显示。',function(btn, text){
					if (btn == 'yes') {
						$('#route_time_div').html('');
						$('#history_route_locations').html('');
						if(index < data_list.length-1){
							drow_line(data_list,index);
						}else{
							add_times_listener();
						}
					}else{
						add_times_listener();
					}
				});
//				$('#route_time_div').html('');
//				$('#history_route_locations').html('');
			}else{
				if(index < data_list.length-1){
					drow_line(data_list,index);
				}else{
					add_times_listener();
				}
			}
		}
	}
	function drow_line(data_list,index){//动画划线
		var start_point = data_list[index];
		var end_point = data_list[index+1];
		var lineWidth = Math.sqrt(Math.pow((Number(start_point.locationX)-Number(end_point.locationX)),2)+
					Math.pow((Number(start_point.locationY)-Number(end_point.locationY)),2));
		var angle = ((Number(start_point.locationX) > Number(end_point.locationX) == Number(start_point.locationY) > Number(end_point.locationY))?"":"-")
					+ (Math.asin(Math.abs(Number(end_point.locationY) - Number(start_point.locationY))/lineWidth)*360/2/Math.PI);
		$('#history_route_locations').append('<div class="route_div" style="top: '+start_point.locationY+'px; left: '+start_point.locationX+'px;width: 0px;"></div>');
		var lineObj = $('#history_route_locations').find('.route_div:last');
		lineObj.css("transform", "rotate("+angle+"deg)");
//		var animate_time = per_distance_time*lineWidth;
		$('#history_route').css({top:start_point.locationY+"px",left:start_point.locationX+"px"});
		$('#history_route').animate(
			{top:end_point.locationY,left:end_point.locationX},
			{duration: walkPointToPointTimes,
				easing:'linear',
				complete:function(){
					drow_route(data_list,++index);
				}
  		});
		lineObj.animate(
			{width:lineWidth},
			{duration: walkPointToPointTimes,
				easing:'linear',
				step:function(now_width,tween){
					//now在这里会是当前对象的当前width
					var top = Number(start_point.locationY) + now_width*(Number(end_point.locationY) - Number(start_point.locationY))/lineWidth/2;
					var left = Number(start_point.locationX) + now_width*(Number(end_point.locationX) - Number(start_point.locationX))/lineWidth/2 - now_width/2+2;//2为线的宽度的一半
					lineObj.css({top:top+"px",left:left+"px"});
				},
				progress:function(animation,progress,remainingMs){
					//remainingMs剩余的毫秒数，可根据这个去控制刷新line位置的频率
					
				}
			}
		);
	}
	function add_times_listener(routePoint){
		var times = $('#route_time_div').children();
		times.on('click',function(){
			$('#route_time_div').children().removeClass("history-route-point-click"); 
			$(this).addClass("history-route-point-click"); 
			$('#history_route').animate({top:$(this).data("locationY")+'px',left:$(this).data("locationX")+'px'}); 
			movePositionToMapCenter($(this).data("locationX"),$(this).data("locationY"));
		});
		times.on('mouseover',function(){
			$(this).addClass("history-route-point-mouseover"); 
		});
		times.on('mouseout',function(){
			$(this).removeClass("history-route-point-mouseover"); 
		});
	}
	function movePositionToMapCenter(targetX,targetY){
    	//可视窗口的大小
    	var mapHeight = parseFloat($('#history_route_map').parent().height());
    	var mapWidth = parseFloat($('#history_route_map').parent().width());
    	
    	var newTop = mapHeight/2 - targetY;
    	if(newTop > 0){//上边界
    		newTop = 0;
    	}
    	if(newTop < mapHeight - mapObject.height){//下边界
    		newTop = mapHeight - mapObject.height;
    	}
    	if(newTop > 0){//上边界
    		newTop = 0;
    	}
    	
    	var newLeft = mapWidth/2 - targetX;
    	if(newLeft > 0){//左边界
    		newLeft = 0;
    	}
    	if(newLeft < mapWidth - mapObject.width){//右边界
    		newLeft = mapWidth - mapObject.width;
    	}
    	if(newLeft > 0){//左边界
    		newLeft = 0;
    	}
    	
    	$('#history_route_map').animate({top:newTop+'px',left:newLeft+'px'},300); 
    	
    	//地图相对于centerPanel的偏移量
//    	var position = $('#history_route_map').position(); 
//    	try{
//	    	var positionLeft = parseFloat(position.left);//为负值
//	    	var positionTop = parseFloat(position.top);//为负值
//	    	console.debug("positionLeft="+positionLeft+",positionTop="+positionTop);
//	    	var centerX = Math.abs(positionLeft)+mapWidth/2;
//	    	var centerY = Math.abs(positionTop)+mapHeight/2;
//	    	console.debug("centerX="+centerX+",centerY="+centerY);
//	    	var newLeft = positionLeft + (centerX - targetX);
//	    	var newTop = positionTop + (centerY - targetY);
//	    	console.debug("newLeft="+newLeft+",newTop="+newTop);
//	    	var maxOffsetLeft = parseFloat(mapObject.width) - mapWidth;
//	    	var maxOffsetTop = parseFloat(mapObject.height) - mapHeight;
//	    	console.debug("maxOffsetLeft="+maxOffsetLeft+",maxOffsetTop="+maxOffsetTop);
//	    	if(Math.abs(newLeft) > maxOffsetLeft){
//	    		newLeft = 0-maxOffsetLeft;
//	    	}
//	    	if(newLeft > 0){
//	    		newLeft = 0;
//	    	}
//	    	if(Math.abs(newTop) > maxOffsetTop){
//	    		newTop = 0-maxOffsetTop;
//	    	}
//	    	if(newTop > 0){
//	    		newTop = 0;
//	    	}
//	    	console.debug("targetX="+targetX+",targetY="+targetY+"--------newLeft="+newLeft+",newTop="+newTop);
//	        $('#history_route_map').animate({top:newTop+'px',left:newLeft+'px'},300); 
//    	}catch(e){
//    	}
    }
	//加载信号收集站点,成功后加载人员信息
	function loadInformations(){
		Ext.Msg.wait('正在加载设备信息....', '提示');
		Ext.Ajax.request({
			url : 'devicemanage/get-devices.action',
			params : {communityId : '${communityId}'},
			success : function(form,action){
				Ext.Msg.hide(); 
				var responseText = Ext.decode(form.responseText);
            	var dataList = responseText.list;
            	var deviceHtml = '';
            	for(var i=0;i<dataList.length;i++){
            		deviceHtml=deviceHtml+'<div class="ip_tooltip ip_img32" ' +
            				'style="top: '+dataList[i].locationY+'px; left: '+ dataList[i].locationX+
            				'px;" data-tooltipbg="bgwhite" data-round="roundBgW" data-button="device" data-animationtype="ltr-slide">'+
            				'<p>设备信息<hr>' +
            				'名称：'+dataList[i].name +
            				'<br>添加时间：'+dataList[i].addDate +
            				'<br>描述：'+dataList[i].description +
            				'</p></div>';
				}
				$('#history_route_devices').html(deviceHtml);
				$("#history_route_map").iPicture({id:"history_route_devices"});
			},
			failure:function(form,action){
				Ext.Msg.hide(); 
				Ext.Msg.alert("温馨提醒","获取设备信息失败，稍后请重试！");
			}
		});
	}
	
	function showSearchPerson(){
		var personLocationHtml = '<div id="history_route" class="ip_tooltip ip_img32" style="top: -100px; left: -100px;"><div class="button person"></div></div>';
		$('#history_route_person').html(personLocationHtml);
		
//    	var personLocationHtml = '<div id="history_route" class="ip_tooltip ip_img32" style="top: -100px; left: -100px;" ' +
//				'data-tooltipbg="bgwhite" data-round="" data-button="person" data-animationtype="ltr-slide"></div>';
//		$('#history_route_person').html(personLocationHtml);
//		$("#history_route_map").iPicture({id:"history_route_person"});
	}
	//导出报表
    var exportExcel = new Ext.Action(Ext.apply({
    	text: '导出实时报表数据',
    	iconCls :'export-excel',
    	handler: function(){
    		var params = leftPanel.getForm().getValues();
    		if(params.communityId != ''){
    			params.communityName = comunity_store.getById(params.communityId).get("name");
    		}
    		if(params.personId != ''){
    			params.personName = person_store.getById(params.personId).get("name");
    		}
    		var paramsEncode = Ext.urlEncode(params);
    		var url = "datastatistics/historyroute/export-excel.action?" + paramsEncode;
    		var xls = window.open(url); 
		 	xls.focus();
    	}
    }));
	var mapPanel=new Ext.Panel({
		header : false,
		region:'center',
		layout : 'fit',
		border : true,
		tbar : [exportExcel],
		//bbar : ['-'],
		//tbar : ['-'],
        html:'<div id="history_route_map" data-interaction="hover">' +
        		'<div class="ip_slide">' +//下面的元素注意放的位置，防止不合理的遮挡，下面的元素会遮挡上面的元素
        			'<img id="history_route_img" class="ip_tooltipImg" src=""/>' +
        			'<div id="history_route_devices" class="devices">' +
	        		'</div>' +
//	        		'<div id="history_route_click" class="ip_tooltip ip_img32">' +
//	        			'<div class="roundBgY"></div>' +
//	        			'<div class="roundBgYIn"></div>' +
//	        			'<div class="roundBgYInner"></div>' +
//	        		'</div>' +
        			'<div id="history_route_locations" class="locations">' +
	        		'</div>' +
	        		'<div id="history_route_person" class="locations">' +
	        		'</div>' +
        		'</div>' +
        	'</div>',
        listeners:{
        	"afterrender":function(panel){
//        		$('#history_route_click').hide();
		        //给地图添加拖动效果
        		mapObject = new SpryMap({id : "history_route_map",
                         height: 300,
                         width: 300,
                         startX: 0,
                         startY: 0,
                         cssClass: ""});
                changMapSize();
//                loadInformations();
                showSearchPerson();
                getMaps();
          	},
          	"bodyresize":function(){
          		changMapSize();
          		refreshMapPosition();
			}
        }
    });
    function changMapSize(){//当窗口改变时，更改地图的显示尺寸，保证图片可以拖动显示全部
    	var divHeight = $('#history_route_map').parent().parent().height();
    	var divWidth = $('#history_route_map').parent().parent().width();
    	$('#history_route_map').parent().height(divHeight);
    	$('#history_route_map').parent().width(divWidth);
    }
    //解决当地图显示区域为右下角时，再拉大浏览器窗口时会出现地图无法全部填充centerPanel的情况
    //要考虑的问题：1.地图可填充满centerPanel,但未填充满；2.地图已近展示到最大了还是无法展示全部的地图
    function refreshMapPosition(){
    	//可视窗口的大小
    	var mapHeight = parseFloat($('#history_route_map').parent().height());
    	var mapWidth = parseFloat($('#history_route_map').parent().width());
    	//地图相对于centerPanel的偏移量
    	var position = $('#history_route_map').position(); 
    	try{
	    	var positionLeft = parseFloat(position.left);//为负值
	    	var positionTop = parseFloat(position.top);//为负值
	    	if(Math.abs(positionLeft)+mapWidth > mapObject.width){
	    		positionLeft = mapWidth-mapObject.width;
	    		if(positionLeft > 0){//解决问题2
	    			positionLeft = 0;
	    		}
	    	}
	    	if(Math.abs(positionTop)+mapHeight > mapObject.height){
	    		positionTop = mapHeight-mapObject.height;
	    		if(positionTop > 0){//解决问题2
	    			positionTop = 0;
	    		}
	    	}
	    	$('#history_route_map').animate({top:positionTop+'px',left:positionLeft+'px'},0); 
    	}catch(e){
    	}
    }
    var maps = [];
    function getMaps(){
	   	Ext.Ajax.request({
       		url : 'datastatistics/historyroute/get-maps.action',
            success : function(xhr) {
            	var responseText = Ext.decode(xhr.responseText);
            	maps = responseText.list;
           	}
        })
    }
    function changeMap(communityId){
    	for(var i = 0; i < maps.length; i++) {
			if (maps[i].id == communityId) {
				var oldMapSrc = $("#history_route_img").attr("src");
				if(oldMapSrc != maps[i].map){
					$("#history_route_img").attr("src", maps[i].map);
					mapObject.width = maps[i].mapWidthPixel;
      				mapObject.height = maps[i].mapHeightPixel;
				}
				return;
			}
		}
    }
    var comunity_store = new Ext.data.JsonStore({ 
		url : "map/area/communitys-data.action", 
		fields : ['id','name'], 
		root : "values", 
		baseParams : { 
		} 
	}); 
	comunity_store.addListener("load", function(){ 
		/*comunity_store.insert(0, new Ext.data.Record({ 
			'id' : "",
			'name' : '全部'
		})); */
		if(this.totalLength>0){
			var showValue = this.getAt(0).get('id');
			community_show.setValue(showValue);
			person_store.load({params : {
				communityId : showValue
			}});
		}
	}); 
	comunity_store.load();
	var community_show = new Ext.form.ComboBox( { 
		fieldLabel : '小&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;区', 
//		anchor : anchor_w, 
		mode : 'local', 
		triggerAction : 'all', 
		//allowBlank : false,
		selectOnFocus : true, 
		forceSelection : true, 
		editable : false, 
		valueField : 'id', 
		displayField : 'name', 
		hiddenName:'communityId',
		name : 'communityId',
		store : comunity_store,
		listeners : {
			'select' : function(combo,record,index ){
				person_show.clearValue();
				person_store.reload({params : {
					communityId : combo.value
				}});
			}
		}
	});
	var person_store = new Ext.data.JsonStore({ 
		url:'datastatistics/historyroute/combo-person.action',
		fields : ['id','name'], 
		root : "values"
	});
	person_store.addListener("load", function(){ 
//		person_store.insert(0, new Ext.data.Record({ 
//			'id' : "",
//			'name' : '全部'
//		})); 
		person_show.setValue(this.getAt(0).get('id'));
		}); 
	person_store.load();
	var person_show = new Ext.form.ComboBox({ 
		fieldLabel : '人&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;员', 
		mode : 'local', 
		triggerAction : 'all', 
		//allowBlank : false,
		//selectOnFocus : true, 
		//forceSelection : true, 
		editable : false, 
		valueField : 'id', 
		displayField : 'name', 
		hiddenName:'personId',
		queryMode: 'local',
		name : 'personId',
		store : person_store
		});
		/*var person_show = new Ext.define('Ext.ux.MultiComboBox', {  
    	extend: 'Ext.form.ComboBox',  
    	alias: 'widget.multicombobox',  
    	xtype: 'multicombobox',
    	fieldLabel : '人&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;员', 
		mode : 'local', 
		triggerAction : 'all', 
		editable : false, 
		valueField : 'id', 
		displayField : 'name', 
		hiddenName:'personId',
		queryMode: 'local',
		name : 'personId',
		store : person_store,
    	initComponent: function(){  
        	this.multiSelect = true;  
        	this.listConfig = {  
              	itemTpl : Ext.create('Ext.XTemplate',  
                    	'<input type=checkbox>{values}'),  
              	onItemSelect: function(record) {      
                  	var node = this.getNode(record);  
                  	if (node) {  
                     	Ext.fly(node).addCls(this.selectedItemCls);  
                       
                     	var checkboxs = node.getElementsByTagName("input");  
                     	if(checkboxs!=null)  
                     	{  
                         	var checkbox = checkboxs[0];  
                         	checkbox.checked = true;  
                     	}  
                  	}  
              	},  
              	listeners:{  
                  	itemclick:function(view, record, item, index, e, eOpts ){  
                      	var isSelected = view.isSelected(item);  
                      	var checkboxs = item.getElementsByTagName("input");  
                      	if(checkboxs!=null)  
                      	{  
                          	var checkbox = checkboxs[0];  
                          	if(!isSelected)  
                          	{  
                              	checkbox.checked = true;  
                          	}else{  
                              	checkbox.checked = false;  
                          	}  
                      	}  
                  	}  
              	}         
        	}         
        	this.callParent();  
    	}  
	});  */
	
	var startDateTime = new Ext.ux.form.DateTimeField({
		fieldLabel : '开始时间',
		name : 'startDateTime',
		format : 'Y-m-d H:i:s', 
		editable:false,
		border:false,
   		allowBlank : false,
   		value:Ext.Date.add(new Date(), Ext.Date.DAY, -1),
		width : 150
	});
	var endDateTime = new Ext.ux.form.DateTimeField({
		fieldLabel : '结束时间',
		name : 'endDateTime',
		format : 'Y-m-d H:i:s',
		editable:false,
		border:false,
   		allowBlank : false,
        value:new Date(),
		width : 150
	});
    var leftPanel = new Ext.FormPanel({
		title : '搜索',
		region : 'west',
       	margins:'3',
       	labelWidth: 65, 
	    displayfieldWidth: 25, 
//        frame:true,
        bodyStyle:'padding:5px 5px 10px',
        width: 250,
        defaults: {anchor: '95%'},
        defaultType: 'textfield',
        buttonAlign :'center',
        autoHeight:true,
      	border:false,
//        split:true,
        collapsible:true,
        collapseMode:'mini',
        items: [startDateTime,endDateTime,community_show,person_show],
        buttons:[{
         	text:'查询',
         	handler: function () {
				var start = startDateTime.value;
				var end = endDateTime.value;
				if(start == undefined || end == undefined){
					Ext.Msg.alert("温馨提醒",'开始时间和结束时间必须填写！');
					return;
				}
				if(start > end){
					Ext.Msg.alert("温馨提醒",'开始时间不能大于结束时间！');
					return;
				}
				if(undefined ==person_show.value ){
					Ext.Msg.alert("温馨提醒",'请选择人员！');
					return;
				}
				if(leftPanel.getForm().isValid()){
					$('#history_route').stop();
					$('#route_time_div').html("");
					$('#history_route_locations').html("");
					routeTimePanel.expand();
					changeMap(leftPanel.getForm().getValues().communityId);
					getHistoryRoute();
				}else{
					 Ext.Msg.alert("提示","查询数据填写有误！");
				}
			}
        },{
         	text:'重置',
         	handler: function () {
		      	leftPanel.getForm().reset();
		      	person_show.clearValue();
		    }
        }]  
    });
    var routeTimePanel = new Ext.Panel({
//    	header : false,
		title : '位置详细信息',
//    	id:"persons_information_panel_${id}",
    	region : 'east',
    	width: 170,
//    	bodyStyle:'padding:10px 10px',
    	collapsed : true,
    	collapsible:true,
    	collapseMode:'mini',
//    	border:false,
		autoScroll : true,
		layout:'fit',
//		tbar: [clear_route],
		html : '<div id="route_time_div"></div>',
		listeners:{
        	"afterrender":function(panel){
//        		 loadInformations();
			}
		}
    });
    var mainPanel = new Ext.Panel({
		autoWidth:true,
    	header : false,
		title : '历史轨迹查询',
		border : false,
		layout : 'border',
		items : [leftPanel,mapPanel,routeTimePanel],
		listeners: {  
             
        }  
	});
	return mainPanel;
})();