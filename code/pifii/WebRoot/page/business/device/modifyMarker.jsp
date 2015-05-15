<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>

<style type="text/css">
 	#router_marker_modify .row{padding:0;margin-bottom:10px;}
 	#router_marker_modify .row>span{float: left;line-height: 35px;width:50px;height:30px;color:gray;font-size:15px;font-weight:bold;text-align:right;display:inline-block;}
 	#router_marker_modify .row input{ border:1px solid #b4c0ce; color:#666;margin-left:20px; height:30px;border-radius:3px;width: 200px;}
/* 	#router_marker_modify .control-label{ */
/* 		float: left; */
/* 		line-height: 26px; */
/* 		width: 50px; */
/* 		text-align: center; */
/* 	} */
</style>
<div id="router_marker_modify">
	<form action="business/device/saveMarker" method="POST">
		<input type="hidden" name="sn" value="${sn}">
		<div class="row">
<!-- 			<label>名称</label> -->
			<span>昵称：</span>
			<div>
				<input type="text" name="marker" value="${marker}">
			</div>
		</div>
	</form>
</div>
<script type="text/javascript">
	function saveRouterMarker(){
		$("#router_marker_modify form").ajaxSubmit({
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

