<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
	<head>
	    <title>${audio.name}--本地电台</title>
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	    <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
	   	<meta name="MobileOptimized" content="320"/>
	   	<script src="${pageContext.request.contextPath}/portal/commonjs/jquery-1.8.3.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/commonjs/jquery.cookie.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/portal/commonjs/commons.js" type="text/javascript"></script>
	    <link rel="stylesheet" href="${pageContext.request.contextPath}/portal/commoncss/jplayer/jplayer.blue.monday.css" type="text/css" media="all" />
	    <script src="${pageContext.request.contextPath}/portal/commonjs/jquery.jplayer.min.js" type="text/javascript"></script>
		
		<script type="text/javascript">
			$(document).ready(function(){
				var name=getQueryStringByName('name');
				var playpath="m/"+name;
			    $("#jquery_jplayer_1").jPlayer({
			        ready: function () {
			            $(this).jPlayer("setMedia", {
			                mp3: "${audio.link}",
			            }).jPlayer("play"); // auto play
			        },
			        ended: function (event) {
			            $(this).jPlayer("play");
			        },
			        swfPath: "swf",
			        supplied: "mp3"
			    })
			    .bind($.jPlayer.event.play, function() { // pause other instances of player when current one play
			            $(this).jPlayer("pauseOthers");
			    });
			});
	   	</script>
		<style type="text/css">
			body{background:#eee;font-family:Verdana, Helvetica, Arial, sans-serif;margin:0;padding:0}
			.example{background:#FFF;width:1000px;height:500px;font-size:80%;border:1px #000 solid;margin:0.5em 10% 0.5em;padding:1em 2em 2em;-moz-border-radius:3px;-webkit-border-radius:3px}
			.example .players{float:left;margin:10px}
			
			.cl {clear: both;}
			.example .players {margin-bottom: 0 !important;}
			.pic {width: 98%;height: 300px;margin-top: 6%;}
			.example {margin: 0 !important;padding: 0 !important;border: 0 !important;width: 100%;}
			#jp_interface_1 {width: 98% !important;}
			div.jp-playlist {width: 98%;}
			div.jp-audio {width: 98% !important;}
			.players {width: 98% !important;}
			div.jp-audio div.jp-type-single a.jp-play, div.jp-audio div.jp-type-single a.jp-pause{left: 6px !important;}
			div.jp-audio div.jp-type-single a.jp-stop {left: 53px !important;}
			div.jp-audio div.jp-type-single div.jp-progress {left: 97px !important;width: 80px !important;}
			div.jp-audio div.jp-type-single a.jp-mute, div.jp-audio div.jp-type-single a.jp-unmute{left: 190px !important;}
			div.jp-audio div.jp-type-single div.jp-volume-bar {left: 217px !important;}
			.banquan {width: 100%;height: 20px;background: #ccc;text-align: center;font-size: 12px;color: #666;margin-top: 15%;}
			.banquan p {height: 19px;padding-top: 1px;margin-bottom: 0;}
			.banquan a:link {text-decoration: none;color: #666;}
			.banquan a:hover, .banquan a:visited {text-decoration: underline;}
			.liebiao_1 div.no {margin-right: 0;}
			.pic img {width: 100%;}
		</style>
	</head>
	<body>
		<div class="example">
			<div align="center"></div>
			<div class="players">
				<div align="center"></div>
				<h1 align="center"><font face="微软雅黑" color="#8000ff">PiFii智能盒子本地电台</font></h1>
	            <div class="jp-audio">
	                <div class="jp-type-single">
	                    <div id="jquery_jplayer_1" class="jp-jplayer"></div>
	                    <div id="jp_interface_1" class="jp-interface">
	                        <ul class="jp-controls">
	                            <li><a href="#" class="jp-play" tabindex="1">play</a></li>
	                            <li><a href="#" class="jp-pause" tabindex="1">pause</a></li>
	                            <li><a href="#" class="jp-stop" tabindex="1">stop</a></li>
	                            <li><a href="#" class="jp-mute" tabindex="1">mute</a></li>
	                            <li><a href="#" class="jp-unmute" tabindex="1">unmute</a></li>
	                        </ul>
	                        <div class="jp-progress">
	                            <div class="jp-seek-bar">
	                                <div class="jp-play-bar"></div>
	                            </div>
	                        </div>
	                        <div class="jp-volume-bar">
	                            <div class="jp-volume-bar-value"></div>
	                        </div>
	                        <div class="jp-current-time"></div>
	                        <div class="jp-duration"></div>
	                    </div>
	                    <div id="jp_playlist_1" class="jp-playlist">
	                        <ul>
	                            <li>精彩电台，尽在这里...</li>
	                        </ul>
	                    </div>
	                    <div class="pic"><img src="img/music.jpg"/></div>
	                </div>
	            </div>
	        </div>
	    </div>
		<div class="cl"></div>
		<div class="banquan">
		     <p>Copyright&nbsp;@派路由&nbsp;<a href="http://www.pifii.com">www.pifii.com</a></p>
		</div>
	</body>
</html>