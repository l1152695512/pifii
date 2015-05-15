<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- <div id="auth_type_setting"> -->
<!-- 	<div class="data_list"> -->
<!-- 		<div class="ft"> -->
<!-- 			<ul> -->
<!-- 				<li style="width: 20px;"><input type="checkbox" name="select_all_adv"/></li> -->
<!-- 				<li style="width: 200px;">名称</li> -->
<!-- 			</ul> -->
<!-- 			<div class="cl"></div> -->
<!-- 			<div class="data" id="flow_pack_show_list_data"> -->
<%-- 				<c:if test="${fn:length(advs) == 0}"> --%>
<!-- 					<div><font color="red">没有记录！</font></div> -->
<%-- 				</c:if> --%>
<%-- 				<c:if test="${fn:length(advs) > 0}"> --%>
<%-- 					<c:forEach var="adv" items="${advs}"> --%>
<!-- 					<ul> -->
<!-- 						<li style="width: " class="radio_cls"><input type="radio" name="check_row_data" value="" /></li> -->
<!-- 						<li style="width: "><span title=""></span></li> -->
<!-- 						<li style="width: 0px;float: none;"></li> -->
<!-- 					</ul> -->
					
<!-- 					<tr> -->
<%-- 						<td><input type="checkbox" <c:if test="${adv.checked=='1'}">checked</c:if> name="selected_adv" value="${adv.id}"/></td> --%>
<%-- 						<td>${adv.template_name}</td> --%>
<%-- 						<td>${adv.adv_name}</td> --%>
<%-- 						<td>${adv.des}</td> --%>
<!-- 					</tr> -->
<%-- 					</c:forEach> --%>
<%-- 				</c:if> --%>
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 		<div class="holder"></div> -->
<!-- 	</div> -->
<!-- </div> -->

<style type="text/css">
	#auth_type_setting{margin: 0 20px;}
	#auth_type_setting .holder{text-align: center;}
	#auth_type_setting .table_content{border: 1px solid #DEDEDE;border-radius: 3px;-webkit-border-radius: 3px;-moz-border-radius: 3px;padding: 10px 20px 0 20px;box-shadow: 0 0 10px rgba(189, 189, 189, 0.4);-webkit-box-shadow: 0 0 10px rgba(189, 189, 189, 0.4);-moz-box-shadow: 0 0 10px rgba(189, 189, 189, 0.4);}
	#auth_type_setting .table_content table{border-spacing: 0;border-collapse: separate;border: 1px solid #dddddd;-webkit-border-radius: 4px;-moz-border-radius: 4px;border-radius: 4px;}
	#auth_type_setting .table_content table .firstRowLeft{-webkit-border-top-left-radius: 4px;border-top-left-radius: 4px;-moz-border-radius-topleft: 4px;}
	#auth_type_setting .table_content table .firstRowRight{-webkit-border-top-right-radius: 4px;border-top-right-radius: 4px;-moz-border-radius-topright: 4px;}
	#auth_type_setting .table_content table th,#auth_type_setting .table_content table td{border-left: 1px solid #dddddd;padding: 8px;line-height: 18px;}
	#auth_type_setting .table_content table th.first,#auth_type_setting .table_content table td.first{border-left: 0;}
	#auth_type_setting .table_content table td{border-top: 1px solid #dddddd;}
	#auth_type_setting .table_content table tbody tr:hover{background:#f5f5f5;}
	
	#auth_type_setting .action span{display: block;width: 120px;height: 30px;line-height: 30px;border-radius: 3px;color: #fff;font-weight: bold;font-size: 16px;background: #31a3ff;text-align: center;padding: 3px;cursor: pointer;}
	#auth_type_setting .action span:hover{background: #54B4EB;}
	#auth_type_setting .action{margin-top: 20px;text-align: center;}
	#auth_type_setting .action div{display: inline-block;}
	#auth_type_setting .action div.cancle{margin-left: 15px;}
</style>

<div id="auth_type_setting">
	<div class="table_content">
		<table>
			<thead>
				<tr>
					<th width="25px" class="first"><input type="checkbox" name="select_all_rows"/></th>
					<th width="200px">名称</th>
				</tr>
			</thead>
			<tbody id="auth_type_data_list">
			<c:if test="${fn:length(authTypes) == 0}">
				<tr><td colspan="2" class="first"><font color="red">暂无可配置的上网方式！</font></td></tr>
			</c:if>
			<c:if test="${fn:length(authTypes) > 0}">
				<c:forEach var="row" items="${authTypes}">
				<tr>
					<td class="first"><input type="checkbox" <c:if test="${row.checked=='1'}">checked</c:if> name="selected_row" value="${row.id}"/></td>
					<td>${row.name}</td>
				</tr>
				</c:forEach>
			</c:if>
			</tbody>
		</table>
		<div class="holder"></div>
	</div>
	<div class="action">
		<div>
			<a href="javascript:void(0);" onclick="submitAuthSetting();"><span>确定</span></a>
		</div>
		<div class="cancle">
			<a href="javascript:void(0);" onclick="closePop();"><span>取消</span></a>
		</div>
	</div>
</div>
<script type="text/javascript">
	$("#auth_type_setting .holder").jPages({
		containerID:"auth_type_data_list",
		first:'首页',
		last:'尾页',
		previous:'上页',
		next :'下页',
		perPage: 5
	});
	//这里需要禁止点击事件冒泡，如果不禁止会出现循环操作，因为每行有一个click事件，会选中该行的多选框
	$("#auth_type_setting tbody tr input").click(function(event){
		event.stopPropagation();
	});
	$("#auth_type_setting tbody tr").click(function(){
		var thisCheckBox = $(this).find("input");
		if(thisCheckBox.is(':checked')){
			thisCheckBox.removeAttr("checked");
		}else{
			thisCheckBox.attr("checked",true);
		}
	});
	$("#auth_type_setting input[name='select_all_rows']").click(function(){
		if($(this).is(':checked')){
			$("#auth_type_setting input[name='selected_row']").attr("checked",true);
		}else{
			$("#auth_type_setting input[name='selected_row']").removeAttr("checked");
		}
		$(this).blur();//兼容处理
		$(this).focus();//兼容处理
	});
	function submitAuthSetting(){
		var selectedAuthTypes = [];
		$("#auth_type_setting input[name='selected_row']:checked").each(function(){
			selectedAuthTypes.push($(this).val());
		});
		var data = $.param({shopId:getSelectedShopId(),selectedAuthTypes:selectedAuthTypes},true);
		$.ajax({
			type : 'POST',
			url :"business/app/auth/save",
			data:data,
			success : function(data) {
				closePop();
			}
		});
	}
</script>