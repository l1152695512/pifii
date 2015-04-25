<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var mapPanel=new Ext.Panel({
		header : false,
		layout : 'fit',
		border : false,
        html:'<div title="双击更换地图">' +
        		'<img id="view_comunity_img" style="cursor: pointer;" src="${communityMapPath}">' +
        	'</div>',
        listeners:{
        	"afterrender":function(panel){
		        //加载信号收集站点
        		var mapObject = new SpryMap({
					id : "view_comunity_img",
	                height: 455,
	                width: 672,
	                startX: 0,
	                startY: 0,
	                cssClass: ""
                });
                $("#view_comunity_img").parent().dblclick(function() {
//			    	changeMapForm.getForm().reset();
//			    	changeMapForm.getForm().setValues({communityId:'${id}'});
			    	mapWin.show();
				});
				if('${communityMapPath}' == ''){
					$("#view_comunity_img").parent().append("<div style='cursor: pointer;line-height: 455px;text-align: center;font-size: 20px;width: 100%;'>双击添加地图</div>");
				}
          	},
          	"bodyresize":function(){
			}
        }
    });
//验证比例格式
   /* Ext.apply(Ext.form.VTypes,{
    proportion:function(val,field){
       var reg=/^[:x22]+ $/; 
        if(!reg.test(val))    
        {    
            return false;    
        }    
        return true;    
  }
  	//proportionText:'请正确的比例格式'
});*/
    
    var changeMapForm = new Ext.FormPanel({
	 	header : false,
        fileUpload: true,              //标志此表单数据中包含文件数据
        autoHeight: true,
        border:false,
//        bodyStyle: 'padding: 10px 10px 0 10px;',
        labelWidth: 65,
        buttonAlign:'center',
        defaults: {
            anchor: '90%',
            allowBlank: false
        },
        items: [{
        	xtype:'hidden',
        	name:'communityId',
        	value:'${id}'
        },{
            xtype: 'fileuploadfield',     //表单域类型
            emptyText: '请您选择文件',
            fieldLabel: '文件路径',
            name: 'upload',
            buttonCfg: {
                text: '',
                iconCls: 'upload-icon'     //按钮图标
            }
    	}]
    });
    var mapWin = new Ext.Window({
//		title: '地图',
        width: 300,
        height:120, 
        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        resizable:false,
        items: [changeMapForm],
        buttons: [{
            text: '提交',
            handler: function(){
                if(changeMapForm.getForm().isValid()){
                     changeMapForm.getForm().submit({
                         url: 'map/area/save-map.action',
                         waitMsg: '正在保存数据...',
                         success: function(xhr, response){
                         	$('#view_comunity_img').attr('src',response.result.msg);                         	
//                         	$('#map').val(response.result.msg);
//                         	changeMapForm.getForm().setValues({mapImage:response.result.msg});
//				                         	console.debug(changeMapForm.getForm());
//				                         	$('#map-image').val(response.result.msg);
                            mapWin.hide();
                         },
	            		 failure: function(form,action){
						     Ext.Msg.alert('提示','操作失败！');
						 }
                     });
                }
            }
        },{
            text: '取消',
            handler: function(){
                mapWin.hide();
            }
        }]
	});
//	var myImage = new Ext.BoxComponent({
//	    xtype: 'box', 
//	    width: 1000, 
//	    height: 500, 
//	    style:'',
//	    autoEl: {
//	        tag: 'img',    
//	        src:'${path}'    //指定url路径
//	    }
//	});
	var viewMapWin = new Ext.Window({
		title: '地图【双击地图区域可添加/更改地图】',
        width: 700,
        height:500, 
//      border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        resizable:false,
        items: [mapPanel]
	});
	return viewMapWin;
})();