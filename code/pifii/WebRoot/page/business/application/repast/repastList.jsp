<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="../../../common/inc.jsp"></jsp:include>
<html>
<body style="background-color: transparent">
			<table id="grid-table" style="width: 100%"></table>

			<div id="grid-pager" style="width: 100%"></div>
</body>
<script type="text/javascript">
			var grid_data = [];
			
			jQuery(function($) {
				var grid_selector = "#grid-table";
				var pager_selector = "#grid-pager";
			
				jQuery(grid_selector).jqGrid({
					url:"",
					datatype: "json",
					height:100,
					colNames:[' ', '菜名','图片','原价', '现价', '单位','类型','菜类','口味','更新时间'],
					colModel:[
						{name:'myac',index:'', width:80, fixed:true, sortable:false, resize:false,
							formatter:'actions', 
							formatoptions:{ 
								keys:true,
								delOptions:{recreateForm: true},
								//editformbutton:true, editOptions:{recreateForm: true, beforeShowForm:beforeEditCallback}
							}
						},
						{name:'name',index:'name',width:90},
						{name:'pic',index:'pic', width:150},
						{name:'price',index:'price', width:70},
						{name:'nowPrice',index:'nowPrice', width:70},
						{name:'unit',index:'unit', width:90},
						{name:'type',index:'type', width:90},
						{name:'sort',index:'sort', width:90},
						{name:'taste',index:'taste', width:90},
						{name:'update',index:'update', width:90}
					], 
			
					viewrecords : true,
					rowNum:10,
					rowList:[10,20,30],
					pager : pager_selector,
					altRows: true,
					//toppager: true,
					multiselect: true,
					//multikey: "ctrlKey",
			        multiboxonly: true,
					loadComplete : function() {
						var table = this;
						setTimeout(function(){
							updatePagerIcons(table);
						}, 0);
					},
			
					caption: "菜品列表",
					autowidth: true
			
				});
						
				jQuery(grid_selector).jqGrid('navGrid',pager_selector);
				function updatePagerIcons(table) {
					var replacement = 
					{
						'ui-icon-seek-first' : 'icon-double-angle-left bigger-140',
						'ui-icon-seek-prev' : 'icon-angle-left bigger-140',
						'ui-icon-seek-next' : 'icon-angle-right bigger-140',
						'ui-icon-seek-end' : 'icon-double-angle-right bigger-140'
					};
					$('.ui-pg-table:not(.navtable) > tbody > tr > .ui-pg-button > .ui-icon').each(function(){
						var icon = $(this);
						var $class = $.trim(icon.attr('class').replace('ui-icon', ''));
						
						if($class in replacement) icon.attr('class', 'ui-icon '+replacement[$class]);
					})
				}
			
			});
		</script>
</html>

