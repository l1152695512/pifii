<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div>
	<ul class="breadcrumb">
		<li><a href="#" onclick="ajaxContent('/jf/index/content');">主页</a></li>
	</ul>
</div>

<div class="sortable row-fluid">
	<a data-rel="tooltip" class="well span3 top-block" href="#"> <span class="icon32 icon-red icon-user"></span>
		<div onclick="ajaxContent('/jf/user/toUrl?toUrl=/view/operatorMessage.html');">会员总数</div>
		<div>507名</div> <span class="notification">6名</span>
	</a> 
	<a data-rel="tooltip" class="well span3 top-block" href="#"> <span class="icon32 icon-color icon-star-on"></span>
		<div>活跃会员</div>
		<div>228名</div> <span class="notification green">4名</span>
	</a> 
	<a data-rel="tooltip" class="well span3 top-block" href="#"> <span class="icon32 icon-color icon-cart"></span>
		<div>销售额</div>
		<div>13320元</div> <span class="notification yellow">34元</span>
	</a> 
	<a data-rel="tooltip" class="well span3 top-block" href="#"> <span class="icon32 icon-color icon-envelope-closed"></span>
		<div>消息</div>
		<div>25未读消息</div> <span class="notification red">12条</span>
	</a>
</div>

<div class="row-fluid">
	<div class="box span12">
		<div class="box-header well">
			<h2>
				<i class="icon-info-sign"></i> 系统负载
			</h2>
			<div class="box-icon">
				 <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a> <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
			</div>
		</div>
		<div class="box-content">
			<div id="content_pv" style="height:200px;border:1px solid #ccc;padding:10px;"></div> <br/>
			
			<div id="content_cpu" style="height:200px;border:1px solid #ccc;padding:10px;"></div> <br/>
			
			<div id="content_phymemory" style="height:200px;border:1px solid #ccc;padding:10px;"></div> <br/>
			
			<div id="content_jvmmemory" style="height:200px;border:1px solid #ccc;padding:10px;"></div>
			
		</div>
	</div>
</div>

