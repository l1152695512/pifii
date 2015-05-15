<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style type="text/css">
/* 	#micro_shop_type_icons{width: 670px;} */
	#micro_shop_type_icons ul{height: 450px;overflow-y: scroll;}
	#micro_shop_type_icons li{float: left;margin: 15px;cursor: pointer;border-radius: 10px;}
	#micro_shop_type_icons li:hover{background:#52B0FC;color: white;}
	#micro_shop_type_icons li img{width:110px;height:100px;}
	#micro_shop_type_icons li span{text-align: center;display: inherit;}
</style>
<div id="micro_shop_type_icons">
	<ul>
	<c:forEach items="${icons}" var="row" varStatus="status">
		<li><img src="${row.icon}" ><span>${row.name}</span></li>
	</c:forEach>
		<div class="cl"></div>
	</ul>
</div>
<script type="text/javascript">
	$(function() {
		$("#micro_shop_type_icons li").click(function(){
			$("#microShopTypeInfo .tupian img").attr("src",$(this).find("img").attr("src"));
			$("#microShopTypeInfo input[name='microShopType.icon']").val($(this).find("img").attr("src"));
			closePop();
		});
	});
</script>