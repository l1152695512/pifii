<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<style type="text/css">
 	#router_name_modify .row{padding:0;margin-bottom:10px;}
 	#router_name_modify .row>span{float: left;line-height: 45px;width:50px;height:30px;color:gray;font-size:15px;font-weight:bold;text-align:right;display:inline-block;}
 	#router_name_modify .row input{text-align: center;margin-bottom: 15px;color:#666;border: 0;border-bottom: 1px solid #b4c0ce;height:30px;width: 150px;outline:none;}
/* 	#router_name_modify .control-label{ */
/* 		float: left; */
/* 		line-height: 26px; */
/* 		width: 50px; */
/* 		text-align: center; */
/* 	} */
</style>
<div id="router_name_modify">
	<form action="business/device/saveName" method="POST">
		<input type="hidden" name="sn" value="${sn}">
		<div class="row">
<!-- 			<label>名称</label> -->
			<span>名称：</span>
			<div>
				<span style="margin-left: 21px;">${name_prefix}</span>
				<input type="text" name="name" value="${name}">
				<span>${name_suffix}</span>
			</div>
		</div>
	</form>
</div>
<script type="text/javascript">
	function saveRouterName(){
		$("#router_name_modify form").ajaxSubmit({
			success : function(resp) {
				if(resp.msg){
		    		myAlert(resp.msg);
		    	}else{
		    		closePop();
					refreshStatus(true);
		    	}
			}
		});
	}
</script>

