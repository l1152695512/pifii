<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<style type="text/css">
	#orderingAdd .content_list{padding:0;margin-bottom:10px;}
	#orderingAdd .content_list span{float: left;width:120px;height:30px;line-height:30px;color:gray;font-size:15px;font-weight:bold;text-align:right;display:inline-block;}
	#orderingAdd .content_list input{ border:1px solid #b4c0ce; color:#666;margin-left:20px; width:300px; height:30px; line-height:30px;border-radius:3px;}
	#orderingAdd .content_list select{outline:none;width:304px;color: rgb(105, 100, 100);height:30px;margin-left: 20px;line-height:30px;border: 1px solid #B4C0CE;font-weight:bold;border-radius:4px;}
	#orderingAdd .content_list textarea{ border:1px solid #b4c0ce;color:#666;width:298px;margin-left: 20px; height:60px;border-radius:3px;overflow:hidden;}
/* 	#orderingAdd .content_list img{width:140px;height:80px;overflow:hidden;margin-left: 20px;} */
	#orderingAdd .content_list .tupian{width:140px;height:80px;overflow: hidden;margin-left: 20px;}
 	#orderingAdd .content_list .tupian img{width:100%;height:100%;}
	#orderingAdd .content_list .line_height_30{height:30px;line-height:30px;}
/* 	#orderingAdd .content_list .line_height_60{height:60px;line-height:60px;} */
	#orderingAdd .content_list .line_height_80{height:80px;line-height:80px;}
	
	#orderingAdd .button_upload{overflow: hidden;position:relative;height:40px;line-height:40px;width:157px; text-align:center;color:#FFF; background:#31a3ff;display:inline-block;border-radius:4px; font-weight:bold;top:40px;left:5px;}
	#orderingAdd .button_upload:hover{background:#52B0FC;}
	#orderingAdd .button_upload input{width:3774px;height: 285px;filter: alpha(opacity=0);font-size: 40px;position:absolute;opacity:0;right:0;top:0;cursor:pointer;}
	
	#orderingAdd .content_btn1{margin-top:20px;margin-left:120px;float:left;}
	#orderingAdd .content_btn1 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
	#orderingAdd .content_btn2{float:left;margin-top:20px;margin-left:10px;}
	#orderingAdd .content_btn2 span{display:block;width:120px;height:30px;line-height:30px;border-radius:3px;color:#fff;font-weight:bold;font-size:16px;background:#31a3ff;text-align:center;padding:3px;cursor: pointer;}
</style>

<div id="orderingAdd">
	<form id="ordering_add_form" action="${cxt}/business/app/menu/save"  method="post" enctype="multipart/form-data">
	<div id="tanchuceng_bg"></div>
        <div class="tanchuceng_content" id="content_popup">
        	<div class="close"></div>
        	<div class="content_list">
            	<span class="line_height_30">菜名：</span><input type="text" name="menu.name" value="${menu.name}" placeholder="添加的菜名" />
            </div>
            <div class="content_list">
            	<span class="line_height_80">图片：</span>
            	<div class="tupian fl">
	            	<img src="<c:if test='${empty menu.icon}'>aa.jpg</c:if><c:if test='${not empty menu.icon}'> ${menu.icon} </c:if>" onerror="this.src='images/business/ad-1.jpg'"/>
            	</div>
                <div class="button_upload"><input name="upload" type="file" />本地上传</div>
                <div class="cl"></div>
            </div>
            <div class="content_list">
            	<span class="line_height_30">原价：</span><input type="text" onkeyup="value=value.replace(/[^\d.]/g,'')" name="menu.old_price" value="${menu.old_price}" /><b>元</b>
            </div>
            <div class="content_list">
            	<span class="line_height_30">现价：</span><input type="text" onkeyup="value=value.replace(/[^\d.]/g,'')" name="menu.new_price" value="${menu.new_price}"/><b>元</b>
            </div>
            <div class="content_list">
            	<span class="line_height_30">单位：</span>  
				<select name="menu.unit"> 
					<option value="1" <c:if test="${menu.unit==1}">selected="selected"</c:if>>份</option> 
					<option value="2" <c:if test="${menu.unit==2}">selected="selected"</c:if>>位</option> 
					<option value="3" <c:if test="${menu.unit==3}">selected="selected"</c:if>>个</option> 
				</select>
            </div>
            <div class="content_list">
            	<span class="line_height_30">类型：</span>  
				<select name="menu.theme"> 
					<option value="1" <c:if test="${menu.theme==1}">selected="selected"</c:if>>人气</option> 
					<option value="2" <c:if test="${menu.theme==2}">selected="selected"</c:if>>推荐</option> 
					<option value="3" <c:if test="${menu.theme==3}">selected="selected"</c:if>>新品</option> 
				</select> 
            </div>
            <div class="content_list">
            	<span class="line_height_30">类型：</span>  
				<select id ="select_type"  name="menu.type"> 
				</select> 
<%--                 <a href="#" class="choose fr"><img src="${pageContext.request.contextPath}/images/business/ordering/delete_icon.png"></a> --%>
<%--             	<a href="#" class="choose fr"><img src="${pageContext.request.contextPath}/images/business/ordering/edit_icon.png"></a> --%>
<%--             	<a href="#" class="choose fr"><img src="${pageContext.request.contextPath}/images/business/ordering/add_icon.png"></a> --%>
            </div>
            <div class="content_list">
            	<span class="line_height_30">口味：</span><input type="text"  name="menu.taste" value="${menu.taste}"/>
            </div>
            <div class="content_btn1">
            	 <a href="javascript:void(0)" id="sva_info"  onclick="submitInfo()" ><span>确定</span>
            </div>
            <div class="content_btn2">
            	<a href="javascript:void(0)" onclick="closePop();">
            	<span>取消</span>
                </a>
            </div>
        </div>
        <input id="orderingAdd_id" name="menu.id" type="hidden" value="${menu.id}" />
		<input id="orderingAdd_shopId" name="menu.shopId" type="hidden" value="" />
		<input name="menu.uid" type="hidden" value="${menu.uid}" />
        </form>
</div>
<script type="text/javascript">
	$(function() {
		$.post('${pageContext.request.contextPath}/business/app/menu/getTypeByShopId', null, function(data) {
			data = eval(data);
			for (var i = 0; i < data.length; i++) {
				var selected = "";
				if('${menu.type}' == data[i].id){
					selected = "selected='selected'";
				}
				$("#select_type").append("<option "+selected+" value='"+data[i].id+"'>"+data[i].name+"</option>"); 
			}
		})
		$("#orderingAdd form input[name=upload]").uploadPreview({ Img: $("#orderingAdd form .tupian").children("img")});
	});
	var shopId = getSelectedShopId();
	$("#orderingAdd_shopId").attr("value",shopId);
	function submitInfo(){
		var name = $("#orderingAdd form input[name='menu.name']").attr("value");
		if(name.length==0){
			myAlert("菜名不能为空！");
			return;
		}
		var localImgPath = $("#orderingAdd form input[name=upload]").val();
		if("${menu.id}"=="" && localImgPath==""){
			myAlert("请上传图片！");
			return;
		}
		var oldPrice = $("#orderingAdd form input[name='menu.old_price']").attr("value");
		if(oldPrice.length==0){
			myAlert("原价不能为空！");
			return;
		}else if(parseFloat(oldPrice)<=0){
			myAlert("原价必须大于0！");
			return;
		}
		var newPrice = $("#orderingAdd form input[name='menu.new_price']").attr("value");
		if(newPrice.length==0){
			myAlert("现价不能为空！");
			return;
		}else if(parseFloat(newPrice)<=0){
			myAlert("现价必须大于0！");
			return;
		}
		 $("#ordering_add_form").ajaxSubmit({
		       success: function(resp){
	    			closePop();
	    			getMenuList();
// 		    			$("#ordering_list").show(function() {
// 		    				var shopId = getSelectedShopId();
// 		    				var name = $("#order_name").val();
// 		    				$.ajax({
// 		    					type : "POST",
// 		    					url : '${cxt}/business/app/menu/orderingList?shopId=' + shopId,
// 		    					data : {
// 		    						name : name
// 		    					},
// 		    					success : function(data) {
// 		    						var divshow = $("#ordering_list");
// 		    						divshow.html(data);
// 		    					}
// 		    				});
// 		    			});
		       }
		  	});
	 }
</script>