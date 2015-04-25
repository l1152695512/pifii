<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var changePreviewImage = new Ext.FormPanel({
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
	        	name:'id',
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
    var previewWin = new Ext.Window({
//		title: '预览',
        width: 300,
        height:100, 
        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        resizable:false,
        items: [changePreviewImage],
        buttons: [{
            text: '提交',
            handler: function(){
                if(changePreviewImage.getForm().isValid()){
                     changePreviewImage.getForm().submit({
                         url: 'map/area/save-preview-image.action',
                         waitMsg: '正在保存数据...',
                         success: function(xhr, response){
                         	$('#preview-image').find('img').attr('src',response.result.msg);
                            previewWin.hide();
                         },
	            		 failure: function(xhr, response){
						     Ext.Msg.alert('提示',response.result.msg);
						 }
                     });
                }
            }
        },{
            text: '取消',
            handler: function(){
                previewWin.hide();
            }
        }]
	});
//	var imagebox = new Ext.BoxComponent({
//		
//		autoEl: {
//			//margin-bottom:10px
//			style: 'width:250px;height:150px;border:0px solid #ccc; text-align:left;padding-bottom:10px;padding-top:10px;cursor: pointer;',
//			tag: 'div',
//			id: 'map-show',
//			html: '<img src="${map}" style="position: absolute;width:250px;height:150px;" title="点击更换地图"><br><br><br>点击添加地图</img>'
//		},
//		listeners:{
//			"afterrender":function(){
//				$('#regionId').val('${pid}');
//				$('#regionName').val('${pname}');
//				
//				$('#map-show').on('click',function(){
//					previewWin.show();
//				});
//          	}
//		}
//	});
	
	var imagebox = new Ext.BoxComponent({
	    xtype: 'box', 
	    width: 500, 
	    height: 300, 
	    style:'',
	    autoEl: {
	    	style: 'width:470px;height:250px;border:0px solid #ccc; text-align:left;padding-bottom:10px;padding-top:10px;cursor: pointer;',
			tag: 'div',
			id: 'preview-image',
			html: '<img src="${path}" style="position: absolute;width:470px;height:250px;" title="双击更新预览图"></img>'
	    	
//	        tag: 'img', 
//	        id: 'preview-image',
//	        src:'${path}'    //指定url路径
	    },
	    listeners:{
			'afterrender' : function(){
				$('#preview-image').dblclick(function(){
					previewWin.show();
				});
			}
		}
	});
   var previewMapWin = new Ext.Window({
		title: '预览图【双击地图区域可添加/更改预览图】',
        width: 500,
        height:300, 
        border:false,
        modal :true,
        layout: 'fit',
        plain:true,
	    constrain: true,
        closable: true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        resizable:false,
        items: [imagebox]
	});
	return previewMapWin;
})();