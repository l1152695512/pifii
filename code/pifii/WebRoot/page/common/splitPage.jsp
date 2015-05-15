<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript">
	$(document).ready(function() {
		var totalRow = ${splitPage.page.totalRow};			//总行数
		var pageSize = ${splitPage.page.pageSize};			//每页显示多少航 
		var pageNumber = ${splitPage.page.pageNumber};		//当前第几页 
		var totalPages = ${splitPage.page.totalPage};		//总页数 
		var isSelectPage = true;							// 是否显示第几页
		var isSelectSize = true;							// 是否显示每页显示多少条
		var orderColunm = "${splitPage.orderColunm}"; 		//表格排序的列
		var orderMode = "${splitPage.orderMode}";			//排序的方式
		// 获取分页HTML信息
		var splitStr = splitPageOut(totalRow, pageSize, pageNumber, totalPages, isSelectPage, isSelectSize, orderColunm, orderMode);
		// 显示分页信息
		$("#splitPageDiv").html(splitStr);
	});
</script>