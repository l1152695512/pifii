<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>机楼/机房规划管理系统</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	 <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/ext-all.css"/>" />
	 <link rel="stylesheet" href="<c:url value='/hr/css/icons.css'/>" type="text/css"></link>
	<script type="text/javascript" src="<c:url value="/adapter/ext/ext-base.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/ext/ext-all.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/ext/ux-all.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/locale/ext-lang-zh_CN.js"/>"></script>
	
	<script type="text/javascript">
		function closeFirstWin(){
		   window.open("","_self");
		   top.opener=null;
		   top.close();
		}
	
		Ext.BLANK_IMAGE_URL = Ext.isIE6 || Ext.isIE7 || Ext.isAir ? '<c:url value="/resources/images/default/s.gif"/>' : 'data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==';
		Ext.onReady(function(){
			//初始化全局 QuickTips 实例,为所有元素提供有吸引力和可定制的工具提示
			Ext.QuickTips.init();

			//表单元素的基类，提供事件操作、尺寸调整、值操作与其它功能
    		Ext.form.Field.prototype.msgTarget = 'side';
    		
    		 var win = new Ext.Window({
	            width:200,
	            height:50,
	            modal:true,
	            constrain:true,
	            border:false,
	            plain:true,
	            closable:false,
	           
	            layout: 'fit',
	
	            items: [new Ext.Button({
	                text: '<span style="font-size:12pt;font-weight:bold">进入机楼/机房规划管理系统</span>',
	                scale: 'large',
	                iconCls:'icon-login-hr',
	                handler:function(){
	                	window.open('login.action','_blank','height=768, width=1024, top=0, left=0, toolbar=no, menubar=no, resizable=no,location=no, status=no , scrollbars=yes');
	                	closeFirstWin();
	                }
	            })]
	        });
    		
    		win.show(Ext.getBody());
    	});
	</script>
  </head>
  
  <body>
  	
  </body>
</html>
