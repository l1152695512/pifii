<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var map;
    var mapPanel = new Ext.Panel({
    	header : false,
     	region:'center',
        layout:'fit',
	    html: '<div id="add_community_map" title="点击地图选择小区位置" style="width:100%;height:100%">',
	    listeners : {
			'afterrender':function(){
				map = new BMap.Map('add_community_map');
	    		map.centerAndZoom(new BMap.Point(113.368159, 23.129269), 12);
				map.addControl(new BMap.NavigationControl());  //添加默认缩放平移控件
				map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
				map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
//				map.disableAutoResize();
				map.enableAutoResize();
				map.setMinZoom(5);
				new Ext.util.DelayedTask(function(){
					map.zoomIn();
				}).delay(500);
				var gc = new BMap.Geocoder();
				map.addEventListener("click", function(e){
					Ext.Msg.wait('正在获取数据....', '提示');
					gc.getLocation(e.point, function(rs){
						var addComp = rs.addressComponents;
//						alert(addComp.province+"\n"+addComp.city+"\n"+addComp.district+"\n"+addComp.street+"\n"+e.point.lng+"\n"+e.point.lat);
						showFormPanel(addComp,e.point);
					});  
				});
			}
		}
    });
    
    function showFormPanel(addComp,point){
    	Ext.Ajax.request({
			async : false,
			url : 'map/area/add-or-modify-map.action',
			params : {provinceName:addComp.province,cityName:addComp.city,districtName:addComp.district,locationX:point.lng,locationY:point.lat},
			success : function(xhr){
				Ext.Msg.hide(); 
				var editWin = eval(xhr.responseText);
				editWin.show();
			},
			failure:function(xhr){
				Ext.Msg.hide(); 
				Ext.Msg.alert("提示","数据查询失败,稍后请重试！");
			}
		});
    }
    
    var addCommunityWin = new Ext.Window({
		title: '添加小区',
        border:false,
        modal :true,
//        resizable:false,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
//        autoHeight: true,
        width:900,
        height:600,
//        autoWidth: true,
        items: [mapPanel],
	    listeners:{
            "afterrender":function(panel){
            	
          	}
		}
	});
    return addCommunityWin;
})();