<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>中国电信翼调度人员精定位平台</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/themes/${theme}/resources/css/ext-all.css"/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/main.css"/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/hr/css/map.css"/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/chooser.css"/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/plugin/examples.css"/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/css/plugin/fileuploadfield.css"/>" />
 	<!-- <link rel="stylesheet" type="text/css" href="<c:url value="/script/ext/plugin/HtmlEditor/css/chooser.css"/>" /> -->
 	<link rel="stylesheet" type="text/css" href="<c:url value="/ext4/css/example.css"/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/fusionChartsJs/prettify.css"/>" />
	<link rel="stylesheet" type="text/css" href="<c:url value="/script/ext/plugin/css/Portal.css"/>" />		
	<link rel="stylesheet" type="text/css" href="<c:url value="/script/ext/plugin/Ext.ux.grid.RowActions.css"/>" />
	<link rel="stylesheet" href="<c:url value='/hr/css/icons.css'/>" type="text/css"></link>
	<link rel="stylesheet" type="text/css" href="<c:url value="/hr/css/iconCombox.css"/>" />
	<link rel="stylesheet" href="<c:url value="/script/ext/plugin/HtmlEditor/css/htmleditorplugins.css"/>" type="text/css"></link>
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/plugin/iPicture.css"/>"/>
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/plugin/tango/skin.css"/>"/>
	<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/css/plugin/jquery.classyslider.css"/>"/>
	
	
	<script type="text/javascript" src="<c:url value="/fusionChartsJs/FusionCharts.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/fusionChartsJs/FusionChartsExportComponent.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/fusionChartsJs/prettify.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/adapter/ext/ext-base.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/ext/ext-all.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/ext/ux-all.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/script/ext/plugin/HtmlEditor/js/Ext.ux.form.HtmlEditor.Plugins.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/script/ext/plugin/HtmlEditor/js/Ext.ux.form.HtmlEditor.Image.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/ext/plugin/HtmlEditor/js/Ext.ux.form.HtmlEditor.Table.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/ext/plugin/HtmlEditor/js/Ext.ux.form.HtmlEditor.HR.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/ext/plugin/htmlparser.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/ext4/Date.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/script/ext/plugin/CheckColumn.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/script/ext/Portal.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/ext/PortalColumn.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/ext/Portlet.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/ext/messageWindow.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/ext/plugin/Ext.ux.grid.RowActions.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/ext/GMapPanel.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/script/ext/examples.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/ext/bug-fix.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/resources/locale/ext-lang-zh_CN.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/ext/init-config.js"/>">	</script>
	<script type="text/javascript" src="<c:url value="/ext/main.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/ext/TreeNodeChecked.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/ext/datetimefield.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/ext/flexpaper_flash.js"/>"></script>
	<!-- <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script> -->
	<script type="text/javascript" src="<c:url value="/ext/ImageButton.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/ext/chartsExtWindow.js"/>"></script>
    <script type="text/javascript" src="<c:url value="/ext/infobubble.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/hr/script/desktop-config.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/ext/RowExpander.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/ext/plugin/TipsWindow.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/jquery/jquery-1.8.3.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/jquery/jquery.actual.min.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/jquery/jquery.ipicture.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/jquery/jquery.jcarousel.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/jquery/spryMap-2.js"/>"></script>
	<script type="text/javascript" src="<c:url value="/script/jquery/jquery.classyslider.js"/>"></script>
	
	<!-- 百度地图 -->
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=XpGabbd4W3nxxzOi4WCu03yt"></script>
	<!--加载鼠标绘制工具-->
	<script type="text/javascript" src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
	<link rel="stylesheet" href="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
	<!--加载检索信息窗口-->
	<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.js"></script>
	<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.css" />
	<!--gps坐标转百度坐标-->
	<script type="text/javascript" src="http://developer.baidu.com/map/jsdemo/demo/convertor.js"></script>
	
	<script type="text/javascript" src="http://union.mapbar.com/apis/maps/free?f=mapi&v=31.3&k=aCW9cItqL7sqT7AxaB0zdHZoZSWmbBsuT7JhMHTsMeD6ZIl9NzFsZHT=@JBL979@Iu7lJJZWWq0IDu9xZMzMxq7I9AhH7LAAA6hqzZHZZLTbZZauxlDz7C7DD9ZCFGT="></script>
	<script type="text/javascript" src="<c:url value="/script/jquery/jquery.jplayer.min.js"/>"></script>
	<script type="text/javascript">
		var debugMode = true;//生产环境下去掉。t
		Ext.BLANK_IMAGE_URL = Ext.isIE6 || Ext.isIE7 || Ext.isAir ? '<c:url value="/resources/images/default/s.gif"/>' : 'data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==';
	</script>

	<style type="css/text">

	</style>	
  </head>
  <body>
  	<object id="max-screen" classid="clsid:ADB880A6-D8FF-11CF-9377-00AA003B7A11">
  		<param name="Command" value="Maximize">
  	</object> 
  	<input type="hidden" name="userName" id="name" value="${userName}" />
	<input type="hidden" name="accountName" id="accountName" value="${accountName}" />
	<input type="hidden" name="ccflowip" id="ccflowip" value="${ccflowip}" />
  	<div class="top_body" id="header">

  	</div>
  </body>
</html>
