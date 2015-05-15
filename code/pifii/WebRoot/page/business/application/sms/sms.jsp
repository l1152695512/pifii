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
			//分页基本变量，jqGrid会将这些字段作为传入后台的参数名
			var grid_selector = "#grid-table";
			var pager_selector = "#grid-pager";
			var pageNum = "pageNum";
			var pageSize = "pageSize";
			var sortColumn = "sortColumn";
			var sortType = "sortType";
			$(function(){
				var $table = $("#grid-table");
				$table.jqGrid({
					url: '${pageContext.request.contextPath}/business/app/sms/getListByShopId',
					mtype: 'POST',
					postData:{shopId:1},
					datatype : "json",
					height:320,
					rownumbers: true,
					colNames:['短信主题','目标客户', '发送数量', '发送内容','发送时间','创建时间','发送状态'],
					colModel:[
						{name:'title',index:'title', width:70},
						{name:'send_type',index:'send_type', width:70,formatter:function(cellvalue, options, rowObject){
							if(cellvalue=="0"){
								return "最近访问";
							} else{
								return "随机抽取";
							} 
						}},
						{name:'content',index:'content', width:70},
						{name:'send_num',index:'send_num', width:90},
						{name:'send_date',index:'send_date', width:70},
						{name:'create_date',index:'create_date', width:90},
						{name:'status',index:'status', width:70,formatter:function(cellvalue, options, rowObject){
							if(cellvalue==1){
								return "已发送";
							} else{
								return "未发送";
							} 
						}}
					], 
					rowNum : 10,
					rowList : [10, 20, 30 ],
					pager : '#grid-pager',
					altRows: true,
					multiselect: true,
			        multiboxonly: true,
			        viewrecords : true,
					jsonReader : { 
						root: "list", 
						page: "pageNumber", 
						total: "totalPage", 
						records: "totalRow", 
						repeatitems: false
					},
					prmNames: {page : pageNum, rows:pageSize},
					loadComplete : function() {
						var table = this;
						setTimeout(function(){
							updatePagerIcons(table);
							enableTooltips(table);
						}, 0);
					},
					caption: "短信列表",
					autowidth: true
				});

				jQuery(grid_selector).jqGrid('navGrid',pager_selector,{ 	
					edit:false,add:false,del:false,search:false,refresh:false,view:false
				})
					
				jQuery(grid_selector).navButtonAdd(pager_selector, {   
					caption:"",   
	                buttonicon:"ui-icon-trash",   
	                onClickButton: deleteRow,  
	                position: "last",
	                title:"",   
	                cursor: "pointer"  
		        });  
					
				function deleteRow() { 
					alert(5);
				}
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
			
				function enableTooltips(table) {
					$('.navtable .ui-pg-button').tooltip({container:'body'});
					$(table).find('.ui-pg-div').tooltip({container:'body'});
				}
			});
			
		</script>
</html>

