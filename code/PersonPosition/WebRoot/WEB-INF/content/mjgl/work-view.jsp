<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


//问题：当前tab关闭时结束所有的task;
(function(){
//	var maxSecondForHiddenPerson = 120;//多久没有检测到人员位置点时隐藏地图上的人员
	
	var personDefaultLocationX = -50;//第一次加载位置之前人员的默认位置
	var personDefaultLocationY = -50;//第一次加载位置之前人员的默认位置
	
	var previousLoadDate = '';//每次获取位置数据时，不在获取以前加载过的位置
	var currentRoutePersonId = '';
	var max_line_num = 30;//用于标识画历史轨迹线的最大条数，超过这个条数会将轨迹线删除
	var mapObject;
//	var needDrawLine = false;//标识是实时定位还是实时轨迹
//	var isFirstPoint = true;//标识画实时轨迹时第一个点不用画线
	
	var refreshTime = 3000;//实时定位和实时轨迹的数据刷新间隔（毫秒），最好小于等于后台数据插入的间隔，以保证每一个轨迹点都能在地图上呈现
	var walkPointToPointTimes = 1000;//从上一个轨迹移动到新轨迹使用的时间，时间越大移动的越慢，理论上如果该值等于refreshTime的话，人的移动会比较有连贯性。
//	var maxRoutePointDistance = 0.2;//决定画路线时点的密度，值越大，密度越小
	//实时定位查询或实时轨迹查询
	var positionTask = new Ext.util.DelayedTask(function(){
//		alert($('#${id}').html());
		if($('#${id}').html() != undefined){//如果定位窗口没有关闭
		   	Ext.Ajax.request({
	       		url : 'location/person-location.action',
	       		params : {communityId : '${id}',previousLoadDate:previousLoadDate},//,maxSecondForHiddenPerson:maxSecondForHiddenPerson
	            success : function(xhr) { 
	        		var responseText = Ext.decode(xhr.responseText);
	        		previousLoadDate = responseText.nextLoadDate;
	            	var dataList = responseText.list;
	            	var dataHtml = '';
	            	$("#locations_${id}").children("div.ip_tooltip").hide();
	            	for(var i=0;i<dataList.length;i++){
	            		$('#'+dataList[i].id).show();
	            		if(dataList[i].is_new == "0"){//该位置点为上一次请求的点
	            			continue;
	            		}
	            		var warnEvents = Ext.decode(dataList[i].wareEvents);
	            		if(warnEvents.length > 0){
	            			change_person_style(dataList[i].id,'roundBgR');
	            			for(var j=0;j<warnEvents.length;j++){
	            				addWarnEvent(dataList[i].id,warnEvents[j].eventId,dataList[i].date,warnEvents[j].eventType,warnEvents[j].eventName);
	            			}
	            		}
//	            		if("" != dataList[i].routeWarn || areaWarnEvent.length > 0){
//	            			change_person_style(dataList[i].id,'roundBgR');
//	            			if("" != dataList[i].routeWarn){
//	            				var routeWarnType = dataList[i].routeWarn.substr(0,1);
//	            				addWarnEvent(dataList[i].id,dataList[i].routeWarn.substr(2),dataList[i].date,routeWarnType)
//	            			}
//	            			if(areaWarnEvent.length > 0){
//	            				var areaWarnType = dataList[i].areaWarn.substr(0,1);
//	            				addWarnEvent(dataList[i].id,dataList[i].areaWarn.substr(2),dataList[i].date,areaWarnType)
//	            			}
////	            		}else{
////	            			$('#'+dataList[i].id).find('[class ^= roundBgR]').remove();
//	            		}
//	            		addWarnEvent(dataList[i].id,'eventId','2014-01-03 15:30:00','1');
//	            		$('#'+dataList[i].id).animate(
//	            			{top:dataList[i].locationY,left:dataList[i].locationX},
//	            			{duration: walkPointToPointTimes,
//	            				easing:'linear',
//						    	start:function(animation){
//						    		var left = parseFloat($(this).position().left);
//									var top = parseFloat($(this).position().top);
//						      		if(dataList[i].id==currentRoutePersonId){
//						      			var start_point = {'locationX':left,'locationY':top};
//										var end_point = {'locationX':dataList[i].locationX,'locationY':dataList[i].locationY};
//										if(left != dataList[i].locationX || top!=dataList[i].locationY){
//						      				drow_line(start_point,end_point);
//										}
//	            					}
//						    	}
//					  		});
					  	drow_route(dataList[i].id,dataList[i].adjacentPoints,0);
					}
					if(refreshTime>=0){
						positionTask.delay(refreshTime);
					}
	           	},
			    failure: function(xhr){
			    	if(refreshTime>=0){
						positionTask.delay(refreshTime);
					}
			    }
	        })
		}
	});
	function drow_route(person_id,data_list,index){
		if($('#${id}').html() != undefined && index<data_list.length){//如果定位窗口没有关闭
			$('#'+person_id).animate(
    			{top:data_list[index].locationY,left:data_list[index].locationX},
    			{duration: walkPointToPointTimes,
    				easing:'linear',
			    	start:function(animation){
			    		var left = parseFloat($(this).position().left);
						var top = parseFloat($(this).position().top);
			      		if(person_id==currentRoutePersonId && //如果当前人员为需要画历史轨迹的人员
			      				left!=personDefaultLocationX && top!=personDefaultLocationY){//如果起始点不为默认点
			      			var start_point = {'locationX':left,'locationY':top};
							var end_point = {'locationX':data_list[index].locationX,'locationY':data_list[index].locationY};
							if(left != data_list[index].locationX || top!=data_list[index].locationY){
			      				drow_line(start_point,end_point);
							}
    					}
			    	},
			    	complete:function(){
						drow_route(person_id,data_list,++index);
					}
		  		});
		}
	}
	//画线的方法
	function drow_line(start_point,end_point){//动画划线
		if($('#${id}').html() != undefined){//如果定位窗口没有关闭
			if($('#locations_${id}').find('.route_div').length > max_line_num){
				$('#locations_${id}').find('.route_div').first().remove();//删除轨迹线
			}
			var lineWidth = Math.sqrt(Math.pow((Number(start_point.locationX)-Number(end_point.locationX)),2)+
						Math.pow((Number(start_point.locationY)-Number(end_point.locationY)),2));
			var angle = ((Number(start_point.locationX) > Number(end_point.locationX) == Number(start_point.locationY) > Number(end_point.locationY))?"":"-")
						+ (Math.asin(Math.abs(Number(end_point.locationY) - Number(start_point.locationY))/lineWidth)*360/2/Math.PI);
			$('#locations_${id}').append('<div class="route_div" style="top: '+start_point.locationY+'px; left: '+start_point.locationX+'px;width: 0px;"></div>');
			var lineObj = $('#locations_${id}').find('.route_div:last');
			lineObj.css("transform", "rotate("+angle+"deg)");
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
	}
	
	function addWarnEvent(personId,eventId,eventTime,eventType,eventName){
		if($("#"+personId).find('table').find('tr').length > 5){
			$("#"+personId).find('table').find('tr:last').remove();
		}
		var eventDescription = "";
		if("1" == eventType){
			eventDescription = "离开了指定区域";
		}else if("2" == eventType){
			eventDescription = "进入了非法区域";
		}else if("3" == eventType){
			eventDescription = "离开了指定路线";
		}
		eventDescription = eventDescription + "\""+eventName+"\"";
		$("#"+personId).find('table').prepend('<tr class = "'+eventId+'"><td style="color:red;width:90%">'+eventTime+eventDescription+'&nbsp;&nbsp;&nbsp;&nbsp;</td><td style="color:red;width:10%"><a data-id="'+eventId+'" data-description="'+eventDescription+'" data-person-id="'+personId+'" href="#">处理</a></td></tr>');
		$("#"+personId).find('table').find('a:first').on('click',function(){
			var id = $(this).data('id');
			var eventTypeDescription = $(this).data('description');
			var thisPersonId = $(this).data('personId');
			Ext.Ajax.request({
				url:'location/warn-event-show.action',
				params: {eventId:id,eventType:eventTypeDescription,personId:thisPersonId},
				success:function(xhr){
					var dealWin = eval(xhr.responseText);
					dealWin.show();
				},
				failure:function(xhr){
					Ext.Msg.alert('提示','暂时不能处理，稍后请重试！');
				}
			});
		});
		$("#"+personId).find('span').css('display','none');
		$("#"+personId).find('table').css('display','block');
		showWarnEventTip(personId);
	}
	function removeWarnEvent(personId,eventId){
		$("#"+personId).find('table').find('.'+eventId).remove();
		if($("#"+personId).find('table').find('tbody').children().length == 0){
			end_warn_sound();
			hideWarnEventTip(personId);
			$("#"+personId).find('span').css('display','block');
			$("#"+personId).find('table').css('display','none');
			$('#'+personId).find('[class ^= roundBgR]').remove();
		}
	}
	function showWarnEventTip(personId){
		$("#"+personId).css('z-index','9999');
    	$("#"+personId).addClass('show');
    	$("#"+personId).find(".xs").css('display','block');
    	$("#"+personId).find(".ip_descr").addClass('ltr-slide');
	}
	function hideWarnEventTip(personId){
    	$("#"+personId).css('z-index','1');
    	$("#"+personId).removeClass('show');
    	$("#"+personId).find(".xs").css('display','none');
    	$("#"+personId).find(".ip_descr").removeClass('ltr-slide');
	}
	function start_warn_sound(){
		$("#warn_sound_${id}").jPlayer("play");
	}
	function end_warn_sound(){
		$("#warn_sound_${id}").jPlayer("stop");
	}
	//加载信号收集站点
	function loadInformations(){
		Ext.Msg.wait('正在加载设备信息....', '提示');
		Ext.Ajax.request({
			url : 'devicemanage/get-devices.action',
			params : {communityId : '${id}'},
			success : function(form,action){
				Ext.Msg.hide(); 
				var responseText = Ext.decode(form.responseText);
            	var dataList = responseText.list;
            	var deviceHtml = '';
            	for(var i=0;i<dataList.length;i++){
            		deviceHtml=deviceHtml+'<div id="'+dataList[i].id+'" class="ip_tooltip ip_img32" ' +
            				'style="top: '+dataList[i].locationY+'px; left: '+ dataList[i].locationX+
            				'px;" data-tooltipbg="bgwhite" data-round="roundBgW" data-button="device" data-animationtype="ltr-slide">'+
            				'<p>设备信息<hr>' +
            				'名称：'+dataList[i].name +
            				'<br>添加时间：'+dataList[i].addDate +
            				'<br>描述：'+dataList[i].description +
            				'</p></div>';
				}
				$('#devices_${id}').html(deviceHtml);
				$("#map_${id}").iPicture({id:"devices_${id}"});
			},
			failure:function(form,action){
				Ext.Msg.hide(); 
				Ext.Msg.alert("提示","获取设备信息失败，稍后请重试！");
			}
		});
	}
	//加载人员信息
	var phoneNum=0;
	function loadPersons(){
		Ext.Msg.wait('获取人员数据....', '提示');
	   	Ext.Ajax.request({
       		url : 'location/get-persons.action',
       		params : {communityId : '${id}'},
            success : function(xhr) {
            	Ext.Msg.hide();
        		var responseText = Ext.decode(xhr.responseText);
            	var dataList = responseText.list;
            	
            	//生成列表中的人员信息列表
            	var person_type_str = "";
            	for(var i=0;i<dataList.length;i++){
            		var person_list_html = "";
            		var person_type_list_html = "";	
//            		$('#persons_information_div_${id}').append(
////            				'<div style="padding-top:10px;height:50px;">' +
//            					'<div id="person_info_'+dataList[i].id+'" style="height:50px;">' +
//	            					'<img src="'+dataList[i].photo+'" class="person-list-image"/>' +
//	            					'<div class="person-list-info">' +
//	            						''+dataList[i].KEY_VALUE +
//			            				'<br>'+dataList[i].name +
//			            				'<br>'+dataList[i].phone +
//	            					'</div>' +
//	            					'<img src="hr/img/icons/current_route.png" class="person-list-route"/>' +
//	            				'</div>' +	
////            				'</div>' +
//            				'<hr>');
		
           			//人员列表
					person_list_html = person_list_html + 
								'<div data-person-id="'+dataList[i].id+'" data-person-phone="'+dataList[i].phone+'" style="height:50px;border-bottom: 1px solid #9A9A9A;" class="person_detail">' +
	            					'<img src="'+dataList[i].photo+'" class="person-list-image"/>' +
	            					'<span class="person-list-info">' +
	            						//''+dataList[i].KEY_VALUE +
			            				'<br>'+dataList[i].name +
			            				'<br>'+dataList[i].phone +
	            					'</span>' +
	            					'<div>' +
		            					'<img src="hr/img/icons/current_route.png" class="person-list-route show_history_route" title="显示轨迹"/>' +
		            					'<img src="hr/img/icons/message.png" class="person-list-route send_message" title="发送短信"/>' +
	            					'</div>' +
	            				'</div>';
					
           
	            	// 人员类型列表
	            	if(person_type_str.indexOf(dataList[i].dictionaryId)<0){
           				person_type_list_html = person_type_list_html + 
		           				'<div id="person_type_'+dataList[i].dictionaryId +'">'+
		           					'<div class="person_type" style="height:30px;padding-top:5px;font-size:14px;font-weight:bold;background:#CCE0E7;border-bottom: 1px solid #9A9A9A;" >' + 
			           					''+dataList[i].KEY_VALUE +
			           					//'<div style="height:30px;padding-top:5px;font-size:14px;font-weight:bold">'+
			           					'<input type="button" value="组内群发短信" style="float:right">'+
			           					//'</div>'+
		           					'</div>' +
		           				'</div>' ;
           				$('#persons_information_div_${id}').append(person_type_list_html);
           				person_type_str = person_type_str + dataList[i].dictionaryId;
           			}
            		$('#person_type_'+dataList[i].dictionaryId).append(person_list_html);
            	}
//            	$('#persons_information_div_${id}').children().css({"border-bottom":"2px solid #9A9A9A"});
//            	$('#persons_information_div_${id}').find('.person_detail').hide();//折叠所有类型的人员
//            	$('#persons_information_div_${id}').children().eq(0).find('.person_detail').show();
            	//添加人员类型点击折叠展开的效果
            	$('#persons_information_div_${id}').find('.person_type').on('click',function(){
            		$(this).parent().find('.person_detail').toggle();
            	}); 
            	//添加人员类型列表头的按钮的点击事件
	        	$('#persons_information_div_${id}').children("div").children(0).find("input[type=button]").click(function(event){
//        			if($(this).parent().parent().find('.person_detail').hasClass("person-list-getlon")){
//        				$(this).parent().parent().find('.person_detail').removeClass("person-list-getlon");
//        			}else{
//        				$(this).parent().parent().find('.person_detail').addClass("person-list-getlon");
//        			}
	        		var phones = [];
					$(this).parent().parent().find('.person_detail').each(function(index){
						phones.push($(this).data('personPhone'));
					});
					sendMessage(phones);
					event.stopPropagation();
				});

	           	/*if($('#persons_information_div_${id}').find('.person_detail').hasClass("person-list-getlon")){
						$('#persons_information_div_${id}').find('.person_detail').removeClass("person-list-getlon");
					}else{
						$('#persons_information_div_${id}').find('.person_detail').addClass("person-list-getlon");
					}*/
			  	//$('#persons_information_div_${id}').find('.person_detail').addClass("person-list-getlon");
				//});
          		//历史轨迹按钮的点击事件
            	$('#persons_information_div_${id}').find('.show_history_route').click(function(event){
            		$('#persons_information_div_${id}').find('.show_history_route').hide();
            		var thisPersonId = $(this).parent().parent().data('personId');
            		if(thisPersonId != currentRoutePersonId){
            			$(this).show();
            			currentRoutePersonId = $(this).parent().parent().data('personId');
            		}else{
            			currentRoutePersonId = "";
            		}
					change_person_route();
					event.stopPropagation();
				});
        		$('#persons_information_div_${id}').find('.show_history_route').hide();
        		
        		$('#persons_information_div_${id}').find('.send_message').click(function(event){
            		var phones = [];
            		phones.push($(this).parent().parent().data('personPhone'));
            		sendMessage(phones);
            		event.stopPropagation();
				});
        		$('#persons_information_div_${id}').find('.send_message').hide();
				$('#persons_information_div_${id}').find('.person_detail').on('mouseover',function(){
					$(this).addClass("person-list-mouseover");
        			$(this).find('.show_history_route').show();
        			$(this).find('.send_message').show();
				});
        		$('#persons_information_div_${id}').find('.person_detail').on('mouseout',function(){
        			$(this).removeClass("person-list-mouseover");
        			$(this).find('.send_message').hide();
        			if(currentRoutePersonId != $(this).data('personId')){
        				$(this).find('.show_history_route').hide();
        			}
				});
				$('#persons_information_div_${id}').find('.person_detail').on('click',function(){
					var thisPersonId = $(this).data('personId');
					if($('#'+thisPersonId).is(":visible")){
						if($('#'+thisPersonId).find('[class ^= roundBgY]').length > 0){
							$('#'+thisPersonId).find('[class ^= roundBgY]').remove();
						}else{
							$('#locations_${id}').find('[class ^= roundBgY]').remove();//删除地图上标记为实时轨迹的人员样式
							change_person_style(thisPersonId,'roundBgY');
						}
	//					$(this).parent().parent().find('.person-list-click').removeClass("person-list-click");//删除列表中点击后的人员样式
	//					if(!$(this).hasClass("person-list-click")){
	//						change_person_style(thisPersonId,'roundBgY');
	//					}
						$('#'+thisPersonId).mouseover();
		        		movePositionToMapCenter($('#'+thisPersonId).position().left,$('#'+thisPersonId).position().top);
	//					if($(this).hasClass("person-list-getlon")){
	//						$(this).removeClass("person-list-getlon");
	//					}else{
	//						$(this).addClass("person-list-getlon");
	//					}
					}else{
						Ext.Msg.alert("提示","该人员不在精定位的区域中，可切换到gps定位查看该人员位置。");
					}
				});
				
            	//生成地图上的人员定位点
            	var personLocationHtml = '';
            	for(var i=0;i<dataList.length;i++){
            		var personTypeImg = '';
            		if(dataList[i].dictionaryId==1){//维修人员
            			personTypeImg = 'person1';
            		}else if(dataList[i].dictionaryId==2){//安保人员
            			personTypeImg = 'person2';
            		}else if(dataList[i].dictionaryId==3){//物业人员
            			personTypeImg = 'person3';
            		}else if(dataList[i].dictionaryId==4){//清洁人员
            			personTypeImg = 'person4';
            		}else{//清洁人员
            			personTypeImg = 'person';
            		}
            		personLocationHtml=personLocationHtml+'<div id="'+dataList[i].id+'" class="ip_tooltip ip_img32" style="top: '+personDefaultLocationY+'px; left: '+personDefaultLocationX+'px;" ' +
            				'data-tooltipbg="bgwhite" data-round="" data-button="'+personTypeImg+'" data-animationtype="ltr-slide" ' +
            				'data-text="'+dataList[i].name+'" data-dictionary-id="'+dataList[i].dictionaryId+'">'+
            				'<p>' +
//            					'<b>人员信息</b><hr>' +
//	            				'<img src="'+dataList[i].photo+'" class="person-image" style="width:60px;height:70px;"/>' +
//	            				'类型：'+dataList[i].KEY_VALUE+
//	            				'<br>姓名：'+dataList[i].name+
//	            				'<br>年龄：'+dataList[i].age+
//	            				'<br>电话：'+dataList[i].phone+
//	            				'<br>其他：'+dataList[i].description.replace(new RegExp('<br>', 'g'), '\n')+
            					'<b>警告处理事件</b><hr>' +
            					'<span style="display:block;">无警告事件</span>' +
            					'<table style="display:none;font-size:12px;"></table>' +
            				'</p>' +
            			'</div>';
				}
				$('#locations_${id}').html(personLocationHtml);
				$("#map_${id}").iPicture({id:"locations_${id}"});
//				$('#locations_${id}').find('.person').on('dblclick',function(){
//					var personType = $(this).parent().data('dictionaryId');
////					if("2"==personType){//安保人员
//						Ext.Msg.wait('调度信息获取....', '提示');
//						Ext.Ajax.request({
//							url:'location/add-dispatch-task.action',
//							params:{personId:$(this).parent().attr('id'),mapPath:'${map}'},
//							success:function(xhr){
//								Ext.Msg.hide(); 
//								var dispatchWin = eval(xhr.responseText);
//								dispatchWin.show();
//							},
//							failure:function(xhr){
//								Ext.Msg.hide(); 
//								Ext.Msg.alert('提示','暂时无法连接服务器，稍后请重试！');
//							}
//						});
////					}
//				});
				$('#locations_${id}').children().mouseover(function(){
					$(this).parent().children().css({"z-index":"auto"});
					$(this).css({"z-index":"10"});//更改z-index,将当前人员显示在最上
				});
				positionTask.delay(0);//成功后加载人员实时位置信息
           	},
		    failure: function(xhr){
		    	Ext.Msg.hide();
		    	Ext.Msg.alert("提示","获取失败，稍后请重试！");
		    }
        })
	}
	
	var centerPanel=new Ext.Panel({
		header : false,
		region:'center',
		layout : 'fit',
		border : false,
//		autoHeight:true,
//		autoWidth:true,
//		autoScroll : true,
        html:'<div id="map_${id}" data-interaction="click">' +
        		'<div class="ip_slide">' +
        			'<img id="${id}" class="ip_tooltipImg"  src="${map}"/>' +
        			'<div id="devices_${id}" class="devices">' +
	        		'</div>' +
        			'<div id="locations_${id}" class="locations">' +
	        		'</div>' +
        		'</div>' +
        	'</div>' +
        	'<div id="warn_sound_${id}">' +
        	'</div>',
        listeners:{
        	"afterrender":function(panel){
		        //给地图添加拖动效果
        		mapObject = new SpryMap({id : "map_${id}",
                         height: parseFloat('${mapHeightPixel}'),
                         width: parseFloat('${mapWidthPixel}'),
                         startX: 0,
                         startY: 0,
                         cssClass: ""});
                changMapSize();
                $("#warn_sound_${id}").jPlayer({
					swfPath: "/script/jquery/plugin",
					supplied: "mp3, oga",
					ready: function () {
						$(this).jPlayer("setMedia", {
							mp3: "sound/system.mp3",
							oga: "sound/system.ogg"
						});
					},
					ended: function (event) {
						$(this).jPlayer("play");
					}
				});
          	},
          	"bodyresize":function(){
          		changMapSize();
          		refreshMapPosition();
			}
        }
    });
    function changMapSize(){//当窗口改变时，更改地图的显示尺寸，保证图片可以拖动显示全部
    	var divHeight = $('#map_${id}').parent().parent().height();
    	var divWidth = $('#map_${id}').parent().parent().width();
    	$('#map_${id}').parent().height(divHeight);
    	$('#map_${id}').parent().width(divWidth);
    }
    //解决当地图显示区域为右下角时，再拉大浏览器窗口时会出现地图无法全部填充centerPanel的情况
    //要考虑的问题：1.地图可填充满centerPanel,但未填充满；2.地图已近展示到最大了还是无法展示全部的地图
    function refreshMapPosition(){
    	//可视窗口的大小
    	var mapHeight = parseFloat($('#map_${id}').parent().height());
    	var mapWidth = parseFloat($('#map_${id}').parent().width());
    	//地图相对于centerPanel的偏移量
    	var position = $('#map_${id}').position(); 
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
    	$('#map_${id}').animate({top:positionTop+'px',left:positionLeft+'px'},0); 
    }
    
    function movePositionToMapCenter(targetX,targetY){
    	//可视窗口的大小
    	var mapHeight = parseFloat($('#map_${id}').parent().height());
    	var mapWidth = parseFloat($('#map_${id}').parent().width());
    	var newTop = mapHeight/2 - targetY;
    	if(newTop > 0){//上边界
    		newTop = 0;
    	}
    	if(newTop < mapHeight - mapObject.height){//下边界
    		newTop = mapHeight - mapObject.height;
    	}
    	var newLeft = mapWidth/2 - targetX;
    	if(newLeft > 0){//左边界
    		newLeft = 0;
    	}
    	if(newLeft < mapWidth - mapObject.width){//右边界
    		newLeft = mapWidth - mapObject.width;
    	}
    	$('#map_${id}').animate({top:newTop+'px',left:newLeft+'px'},300); 
    	//地图相对于centerPanel的偏移量
//    	var position = $('#map_${id}').position(); 
//    	var positionLeft = parseFloat(position.left);//为负值
//    	var positionTop = parseFloat(position.top);//为负值
//    	
//    	var centerX = Math.abs(positionLeft)+mapWidth/2;
//    	var centerY = Math.abs(positionTop)+mapHeight/2;
//    	
//    	var newLeft = positionLeft + (centerX - targetX);
//    	var newTop = positionTop + (centerY - targetY);
//    	
//    	var maxOffsetLeft = parseFloat(mapObject.width) - mapWidth;
//    	var maxOffsetTop = parseFloat(mapObject.height) - mapHeight;
//    	if(Math.abs(newLeft) > maxOffsetLeft){
//    		newLeft = 0-maxOffsetLeft;
//    	}
//    	if(newLeft > 0){
//    		newLeft = 0;
//    	}
//    	if(Math.abs(newTop) > maxOffsetTop){
//    		newTop = 0-maxOffsetTop;
//    	}
//    	if(newTop > 0){
//    		newTop = 0;
//    	}
//        $('#map_${id}').animate({top:newTop+'px',left:newLeft+'px'},300); 
    }
    var imgload_info;
	//function showFormPanel(){
    	//Ext.Ajax.request({
			//url : 'mjgl/webinfoupload.action',
			//success:function(xhr){
				//var newwind = eval(xhr.responseText);
				//newwind.show();
			//}
		//}) 
	//}
    var clear_route = new Ext.Action({
		text : '清除路线',
		iconCls : 'icon-clear-route',
		handler : function() {
			change_person_route();
		}
	});
	function change_person_route(){
//		try{
//			drowLineTask.cancel();
//		}catch(e){
//		}
		$('#locations_${id}').find('.route_div').remove();//删除轨迹线
//		$('#'+currentRoutePersonId).find('[class ^= roundBgY]').remove();//删除标记为实时轨迹的人员样式
//		if(new_person != ''){
//			change_person_style(new_person,'roundBgY');
//		}
	}
	//var selModel = grid.getSelectionModel();
//if(selModel.hasSelection()){   
  //var sendUser = selModel.getSelected().get("sendUser ");
  //var sendTime = selModel.getSelected().get("sendTime ");
//}else{
  //Ext.Msg.alert("提示","请选择一条记录");
//}
	var send_info = new Ext.Action({
		text : '所有人员群发信息',
		iconCls : 'icon-send-info',
		handler : function() {
			var phones = [];
			$('#persons_information_div_${id}').find('.person_detail').each(function(index){
				phones.push($(this).data('personPhone'));
			});
			sendMessage(phones);
			/*var selected = person_grid.getSelectionModel().getSelections();
			var params = [];
			Ext.each(selected, function(item) {
				params.push(item.phone);
			});  
			if(params.length == 0){
		   		Ext.Msg.alert("提示","没有选择收件人！");
		   		return;
			}
			Ext.Msg.confirm('确认', '确定要给选中的人员发送信息？',
				function(btn, text) {
					if (btn == 'yes') {
						Ext.Msg.wait('正在发送短信....', '提示');
						Ext.Ajax.request({
				       		url : 'Test.action',
				       		params: {phone:phoneNum},
				            success : function(xhr) {
				            	Ext.Msg.hide(); 
				            	loadPersons();
				           	},
						    failure: function(xhr){
						    	Ext.Msg.hide(); 
						    	Ext.Msg.alert("温馨提醒","发送失败，稍后请重试！");
						    }
				        })
					}
			});*/
        }
    });
    
    function sendMessage(phones){
		Ext.MessageBox.show({  
            title:'短信内容',  
            msg:'输入信息内容:',  
            width:300,  
            buttons:Ext.MessageBox.OKCANCEL,  
            multiline:true,  
            fn:function(btn,text){  
            	if(btn=="cancel"){
            		Ext.MessageBox.hide();
            	}else{
                	Ext.Msg.alert("提示","发送成功！"); 
            	}
            }  
        });  
    }
	
	function change_person_style(personId,styleClass){
		if(personId != '' && $('#'+personId).find('[class ^= roundBgR]').length == 0){//如果该人员的样式不是求救样式，则更改样式
			$('#'+personId).find('[class ^= roundBg]').remove();
			if(styleClass != ''){
				$('#'+personId).prepend('<div class="'+styleClass+'"></div><div class="'+styleClass+'In"></div><div class="'+styleClass+'Inner"></div>');
			}
		}
		start_warn_sound();
	}
    var personsInformationPanel = new Ext.Panel({
//    	header : false,
		title : '人员列表',
    	id:"persons_information_panel_${id}",
    	region : 'west',
    	width: 250,
//    	bodyStyle:'padding:10px 10px',
    	collapsible:true,
    	collapseMode:'mini',
//    	border:false,
		autoScroll : true,
		layout:'fit',
		tbar: [clear_route,send_info],
		html : '<div id="persons_information_div_${id}"></div>',
		listeners:{
        	"afterrender":function(panel){
//        		 loadInformations();
        		 loadPersons();
			}
		}
    });
    
//    var mainPanel = new Ext.Panel({
//		autoWidth:true,
//    	header : false,
//		title : '首页',
//		border : false,
//		layout : 'border',
//		items : [personsInformationPanel,centerPanel],
//		listeners: {  
//        }  
//	});
	 var map_info_win = new Ext.Window({
		title: '${title}',
        width: 900,
        height: 600, 
        border: false,
        modal : true,
        maximizable :true,
        layout : 'border',
        plain: true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        resizable:true,
        items : [personsInformationPanel,centerPanel],
        listeners: {
        	"close":function(){
        		try{
        			positionTask.cancel();
        		}catch(e){
        		}
        		try{
        			end_warn_sound();
        		}catch(e){
        		}
        	}
        }
	});
	return map_info_win;
})();