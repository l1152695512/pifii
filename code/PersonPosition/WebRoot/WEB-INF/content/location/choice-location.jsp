<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var map_width = 778;
	var map_height = 460;
	var choice_location_panel=new Ext.Panel({
		header : false,
		region:'center',
		//layout : 'fit',
		border : false,
		width : map_width,
		height : map_height,
        html:'<div id="choice_location_map" data-interaction="hover">' +
        		'<div class="ip_slide">' +
//		        	'<div id="choice_location_div">' +
		    			'<img onDblClick="Ext.choice_location(event);" src="${mapPath}"/>' +
//		        	'</div>'+
		        	'<div id="choice_location_persons" class="locations">' +
	        		'</div>' +
		        '</div>' +
        	'</div>',
        listeners:{
        	"afterrender":function(panel){
		        //加载信号收集站点
        		mapObject = new SpryMap({
        			 id : "choice_location_map",
                     height: map_height,
                     width: map_width,
                     startX: 0,
                     startY: 0,
                     cssClass: ""
                });
                if('${type}' == "5"){
                	loadPersons();
                }
          	}
        }
    });
    //加载人员信息
	function loadPersons(){
		Ext.Msg.wait('加载人员信息....', '提示');
	   	Ext.Ajax.request({
       		url : 'location/get-for-help-person.action',
       		params : {communityId : '${communityId}'},
            success : function(xhr) {
            	Ext.Msg.hide();
        		var responseText = Ext.decode(xhr.responseText);
            	var dataList = responseText.list;
            	//生成地图上的人员定位点
            	var personLocationHtml = '';
            	for(var i=0;i<dataList.length;i++){
            		personLocationHtml=personLocationHtml+'<div data-person-id="'+dataList[i].id+'" class="ip_tooltip ip_img32" style="top: '+dataList[i].locationY+'px; left: '+dataList[i].locationX+'px;" ' +
            				'data-tooltipbg="bgwhite" data-round="roundBgR" data-button="person" data-animationtype="ltr-slide" data-dictionary-id="'+dataList[i].dictionaryId+'">'+
            				'<p>人员信息('+dataList[i].date+')<hr>' +
	            				'<img src="'+dataList[i].photo+'" class="person-image" style="width:60px;height:70px;"/>' +
	            				'类型：'+dataList[i].KEY_VALUE+
	            				'<br>姓名：'+dataList[i].name+
	            				'<br>年龄：'+dataList[i].age+
	            				'<br>电话：'+dataList[i].phone+
	            				'<br>其他：'+dataList[i].description.replace(new RegExp('<br>', 'g'), '\n')+
            				'</p></div>';
				}
				$('#choice_location_persons').html(personLocationHtml);
				$("#choice_location_map").iPicture({id:"choice_location_persons"});
				$('#choice_location_persons').find('.person').on('dblclick',function(){
					var person_div = $(this).parent();
					Ext.Msg.confirm('确认', '确定将该人员作为救助对象？',function(btn, text){
						if (btn == 'yes') {
							dispathTaskPanel.getForm().setValues({helpPersonId:person_div.data("personId")});
							location_field.setValue(parseFloat(person_div.css("left"))+","+parseFloat(person_div.css("top")));
							choice_location_win.close();
						}
					});
				});
           	},
		    failure: function(xhr){
		    	Ext.Msg.hide();
		    	Ext.Msg.alert("提示","加载失败，稍后请重试！");
		    }
        })
	}
    Ext.choice_location = function(event){
		var locationX = event.layerX;
		var locationY = event.layerY;
		Ext.Msg.confirm('确认', '确定选择该位置？',function(btn, text){
			if (btn == 'yes') {
				dispathTaskPanel.getForm().setValues({helpPersonId:''});
				location_field.setValue(locationX+","+locationY);
				choice_location_win.close();
			}
		});
	}
	var choice_location_win = new Ext.Window({
		title: '选择目标位置',
		layout : 'fit',
        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        resizable:false,
        items: [choice_location_panel]
	});
    return choice_location_win;
})();
