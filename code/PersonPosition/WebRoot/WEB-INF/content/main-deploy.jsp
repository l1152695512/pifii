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
<!--	 <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/ext-all.css"/>" />-->
<!--	 <link rel="stylesheet" type="text/css" href="<c:url value="/resources/css/xtheme-gray.css"/>" />-->
	 <link rel="stylesheet" type="text/css" href="<c:url value="/resources/themes/${theme}/resources/css/ext-all.css"/>" />
	 <link rel="stylesheet" type="text/css" href="<c:url value="/css/main.css"/>" />
	 <link rel="stylesheet" type="text/css" href="<c:url value="/script/ext/plugin/Ext.ux.grid.RowActions.css"/>" />
	 <link rel="stylesheet" href="<c:url value='/hr/css/icons.css'/>" type="text/css"></link>



<!--	 <link rel="stylesheet" href="<c:url value='/hr/css/xtheme-purple.css'/>" type="text/css"></link>-->
	 <script type="text/javascript" src="<c:url value="/adapter/ext/ext-base.js"/>">
	</script>
	<script type="text/javascript" src="<c:url value="/script/ext/ext-all.js"/>">
	</script>
	<script type="text/javascript" src="<c:url value="/script/ext/ux-all.js"/>">
	</script>
	<script type="text/javascript" src="<c:url value="/script/ext/plugin/Ext.ux.grid.RowActions.js"/>">
	</script>
	<script type="text/javascript" src="<c:url value="/ext/bug-fix.js"/>">
	</script>
	<script type="text/javascript" src="<c:url value="/resources/locale/ext-lang-zh_CN.js"/>">
	</script>


	<script type="text/javascript" src="<c:url value="/ext/init-config.js"/>">	</script>
	<script type="text/javascript" src="<c:url value="/hr/script/menu.js"/>"></script>

	<script type="text/javascript" src="<c:url value="/ext/main2.js"/>">
	</script>

	<script type="text/javascript" src="<c:url value="/hr/script/desktop-config2.js"/>"></script>
	<script type="text/javascript">
		var debugMode = false;
		Ext.BLANK_IMAGE_URL = Ext.isIE6 || Ext.isIE7 || Ext.isAir ? '<c:url value="/resources/images/default/s.gif"/>' : 'data:image/gif;base64,R0lGODlhAQABAID/AMDAwAAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==';
	</script>
  </head>

  <body>
  	<div id="header" >
  		<div class="main-title">
  		<img align="middle" src="<c:url value="/hr/img/hr_24x24.png"/>" />
  		人力资源系统

  		&nbsp;
  		&nbsp;
  		&nbsp;
  		主题选择：
  		<select id="theme" onchange="location.search='theme='+this.value">
  			<c:forEach var="item" items="${themes}">
  				<c:choose>
  					<c:when test="${item == theme}">
  						<option selected="selected" value="${item}">${item}</option>
  					</c:when>
  					<c:otherwise>
  						<option value="${item}">${item}</option>
  					</c:otherwise>
  				</c:choose>
  			</c:forEach>
  		</select>
  		&nbsp;
  		&nbsp;
  		&nbsp;
  		<a href="<c:url value="/j_spring_security_logout"/>" style="color:white">注销</a>
  		</div>

  	</div>
  </body>
</html>
