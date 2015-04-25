<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

(function(){
	var positionTask = new Ext.util.DelayedTask(function(){
	   	Ext.Ajax.request({
       		url : 'realtimelocation/person-location.action',
            success : function(xhr) {
            	var responseText = Ext.decode(xhr.responseText);
            	var dataList = responseText.list;
            	var dataHtml = '';
            	for(var i=0;i<dataList.length;i++){
            		var dataRound = 'roundBgW';
//            		if(dataList[i].locationY>250 || dataList[i].locationX>250){
//            			dataRound = '';
//            		}else if(dataList[i].locationY>150 || dataList[i].locationX>150){
//            			dataRound = 'roundBgB';
//            		}
            		dataHtml=dataHtml+'<div class="ip_tooltip ip_img32" style="top: '+dataList[i].locationY+'px; left: '+ dataList[i].locationX+
            				'px;" data-tooltipbg="bgwhite" data-round="'+dataRound+'" data-button="person-location" data-animationtype="ltr-slide">'+
            				'<p>姓名：'+dataList[i].name+
            				'<br>年龄：'+dataList[i].age+
            				'<br>电话：'+dataList[i].phone+
            				'<br>时间：'+dataList[i].date+
            				'</p></div>';
				}
            	document.getElementById("locations1").innerHTML=dataHtml;
				jQuery( "#iPicture_location1" ).iPicture();
                positionTask.delay(10000);
           	},
		    failure: function(xhr){
		    	positionTask.delay(10000);
		    }
        })
	});
	var centerPanel=new Ext.Panel({
		header : false,
		region:'center',
		layout : 'fit',
		border : false,
        html:'<div id="iPicture_location1" data-interaction="hover">' +
        		'<div class="ip_slide">' +
        			'<img class="ip_tooltipImg" src="images/testPosition.png">' +
        			'<div id="locations1">' +
	        		'</div>' +
        		'</div>' +
        	'</div>',
        listeners:{
        	"afterrender":function(panel){
        		positionTask.delay(0);
          	}
        }
    });
    return centerPanel;
})();