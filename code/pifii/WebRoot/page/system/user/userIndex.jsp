<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ include file="../../common/splitPage.jsp" %> 
<form id="splitPage" class="form-horizontal" action="${pageContext.request.contextPath}/system/user" method="POST">
	<div>
		<ul class="breadcrumb">
			<li><a href="#" onclick="ajaxContent('/jf/index/content');">主页</a>
				<span class="divider">/</span></li>
			<li><a href="#">用户管理</a></li>
		</ul>
	</div>

	<div class="row-fluid sortable">
		<div class="box span12">
			<div class="box-header well" data-original-title>
				<h2><i class="icon-edit"></i> 用户查询</h2>
				<div class="box-icon">
				
					<a href="#" class="btn btn-round" title="添加用户" onclick="ajaxContent('/system/user/addUser');"><i class="icon-plus-sign"></i></a>
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-down"></i></a>
					<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
				</div>
			</div>
			<div class="box-content" style="display: none;">
				<fieldset>
				  	<div class="control-group">
						<label class="control-label" for="focusedInput">账号</label>
						<div class="controls">
					  		<input class="input-xlarge focused" type="text" name="_query.userName" value='' maxlength="20" >
						</div>
				  	</div>
				  
				  	<div class="control-group">
						<label class="control-label">邮箱</label>
						<div class="controls">
					  		<input class="input-xlarge" type="text" name="_query.email" value='' maxlength="40" >
						</div>
				  	</div>
				
				  	<div class="form-actions">
						<button type="button" class="btn btn-primary" onclick='ajaxForm("splitPage");'>查询</button>
						<button type="reset" class="btn">清除</button>
				  	</div>
				</fieldset>
			</div>
		</div>
		<!--/span-->
	</div>
	<!--/row-->

	<div class="row-fluid sortable">
		<div class="box span12">

			<div class="box-header well" data-original-title>
				<h2><i class="icon-user"></i> 用户列表</h2>
				<div class="box-icon">
					<a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> 
					<a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
				</div>
			</div>
			<div class="box-content">
				<table
					class="table table-striped table-bordered bootstrap-datatable ">
					<thead>
						<tr>
							<th onclick="orderbyFun('name')">用户名</th>
							<th onclick="orderbyFun('email')">邮箱</th>
							<th onclick="orderbyFun('des')">描述</th>
							<th onclick="orderbyFun('roleName')">所属角色</th>
							<th onclick="orderbyFun('status')">状态</th>
							<th width="280">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="user" items="${splitPage.page.list}" >
							<tr>
							<td>${user.get("name")}</td>
							<td class="center">${user.get("email")}</td>
							<td class="center">${user.get("des")}</td>
							<td class="center">${user.get("roleName")}</td>
							<td class="center">
							${user.get("status")==1?'正常':'冻结'}
							</td>
							<td class="center">
								<a class="btn btn-success" href="#" onclick="ajaxContent();"> <i class="icon-zoom-in icon-white"></i> 查看</a>
								<a class="btn btn-info" href="#" onclick="ajaxContent();"> <i class="icon-edit icon-white"></i> 编辑</a>
								<a class="btn btn-danger" href="#" onclick="ajaxContent('/system/user/delete?id='${user.get(id)});"> <i class="icon-trash icon-white"></i> 删除</a>
							</td>
						</tr>
						</c:forEach>
						
					</tbody>
				</table>
				<div id="splitPageDiv" class="pagination pagination-centered"></div>
			</div>

		</div>
		<!--/span-->
	</div>
	<!--/row-->

</form>