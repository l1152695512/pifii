<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
	<head>

		<title>文件上传</title>

	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/ext-all.css"/>" />
	 <link rel="stylesheet" type="text/css" href="<c:url value="/css/main.css"/>" />
	 <link rel="stylesheet" type="text/css" href="<c:url value="/css/plugin/fileuploadfield.css"/>" />
	 <link rel="stylesheet" href="<c:url value='/hr/css/icons.css'/>" type="text/css"></link>
	 <script type="text/javascript" src="<c:url value="/adapter/ext/ext-base-debug.js"/>">
	</script>
	<script type="text/javascript" src="<c:url value="/script/ext/ext-all-debug.js"/>">
	</script>
	<script type="text/javascript" src="<c:url value="/script/ext/ux-all-debug.js"/>">
	</script>
	<script type="text/javascript" src="<c:url value="/ext/bug-fix.js"/>">
	</script>
	<script type="text/javascript" src="<c:url value="/resources/locale/ext-lang-zh_CN.js"/>">
	</script>
	<style type="text/css">
		.x-form-element{
			background-color: #dfe8f6;
		}
		.x-form-item{
			background-color: #dfe8f6;
		}
		.x-form {
			background-color: #dfe8f6;
		}
	</style>
	</head>
	<script type="text/javascript">
		Ext.BLANK_IMAGE_URL = Ext.isIE6 || Ext.isIE7 || Ext.isAir ? '<c:url value="/resources/images/default/s.gif"/>' : 'data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==';
		Ext.onReady(function(){
			Ext.QuickTips.init();
			var simple = new Ext.FormPanel({
		        frame:false,
		        url:'file-upload.action',
		        fileUpload:true,
		        //labelWidth:25,
		        //padding:2,
		        style:{backgroundColor:'#dfe8f6'},
		        border:false,
		        renderTo:Ext.getBody(),
		        items: [{
		            xtype: 'fileuploadfield',
		            hideLabel:true,
		            name: 'upload',
		            buttonText: '选择文件',
		            buttonOnly:true,
		            listeners: {
			            'fileselected': function(fb, v){
			         
			                simple.getForm().submit({
			                	success:function(form,action){
			                		if('${inputId}'!='' ){
			                			window.parent.Ext.getCmp('${inputId}_path').setValue(action.result.saveName);
			                			window.parent.Ext.getCmp('${inputId}_name').setValue(action.result.uploadFileName);
			                			window.parent.Ext.getCmp('${inputId}_display').setValue(action.result.uploadFileName);
			                		}
			                	
			                	},
			                	failure:function(form,action){
			                		//alert("failure:"+action.result.msg);
			                		window.parent.Ext.Msg.alert("上传失败",action.result?action.result.msg:"");
			                	}
			                });
			            }
			        }
		        }]
		    });
		    
		});
	</script>
	<body style="overflow: hidden;background-color: #dfe8f6;">	
			
	</body>
</html>
