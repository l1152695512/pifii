<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<title>热门影视</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0,user-scalable=no"/>
		<meta name="MobileOptimized" content="320"/>
		<script src="${pageContext.request.contextPath}/portal/commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/commonjs/jquery.cookie.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/commonjs/TouchSlide.1.1.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/commonjs/commons.js" type="text/javascript"></script>
		<style type="text/css">
			/* css 重置 */
			body, p, input, h1, h2, h3, h4, h5, h6, ul, li, dl, dt, dd, form { margin: 0; padding: 0; list-style: none; vertical-align: middle; font-weight:normal; }
			img { border:0; margin: 0; padding: 0;   }
			body { color: #000; -webkit-user-select: none; -webkit-text-size-adjust: none; font:normal 16px/200% "微软雅黑", helvetica, arial; text-align:left;   }
			header, section, footer { display: block; margin: 0; padding: 0 }
			a{text-decoration:none;color:#000;}
			body{ background:#f4f4f4;  }
			.fl{float:left}
			.fr{float:right;}
			.cl{clear:both;}
			h3{font-size:16px;color:#252525;height:20px;line-height: 20px;text-indent: 2px;}
			h4{float:left;width: 40px;padding-top:12px;height: 28px;line-height: 20px;font-size: 16px;}
			/* 头部 */
			header{  background:#F5AB38; padding-bottom:10px; border-bottom:3px solid #CF7F21;  }
			header .logo{ padding:10px 0 5px 10px;  color:#fff;  font-style:italic; font-weight:bold; text-shadow:1px 1px 1px #965e18; font-size:12px; }
			header .logo a{ font-size:30px; line-height:30px; color:#fff;  }
			/* 内容 */
			#content{background:#fff;}
			.path .back{width: 80px;height: 50px;line-height: 50px;background: #77B5E5;float: left;color: white;text-align: center;border-radius: 3px;cursor: pointer;}
			.path .name{text-align: center;height: 50px;font-size:18px;line-height: 50px;}
			.line-jianbian{background: url(${pageContext.request.contextPath}/portal/commonimg/line-jianbian.png);width: 100%;height:7px;}
			.video video{width: 100%;}
			.juji{width: 92%;height:40px;background:;margin: 0 auto;padding:0 1%;}
			.xiazai{width: 32px;height:100%;background: url(${pageContext.request.contextPath}/portal/mb/video/img/xiazai.png) no-repeat;font-size: 10px;line-height: 58px;}
			/*.tro{width: 96%;margin:0 auto;margin-top:10px;padding:0 1%;height: 100px;}
			.tro li{width:54px;margin-right: 2%;height:54px;float:left;line-height: 54px;font-size: 14px;text-align: center;margin-bottom:2%;border:1px solid #C6C6C6;}
			.tro li:nth-child(5n){margin-right:0;}*/
		</style>
	</head>
	<body>
		<div id="content">
			<div class="path">
				<!-- <div class="back">返回</div> -->
				<div class="name">${video.name}</div>
			</div>
			<div class="line-jianbian"></div><!--渐变介绍-->
			<div class="video"><!--vedio 视频播放开始-->
				<video width="320" height="240" controls="controls" autoplay="autoplay">
					  <source id="videoSource" src="${video.link}" type="video/mp4">
						<span style="color:red;">你的浏览器不支持播放视频，请升级浏览器</span>
				</video>
			</div><!--视频播放结束-->
		</div>
		<script>
			$('video').media({
				autoplay: true,
				alwaysShowControls : false,
				videoVolume : 'horizontal',
				features : [ 'playpause', 'progress', 'volume', 'fullscreen' ]
			});
		</script>
	</body>
</html>
