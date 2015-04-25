<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <script type="text/javascript" src="ext/flexpaper_flash.js"></script>
    <script type="text/javascript" src="adapter/ext/ext-base.js"></script>
	<script type="text/javascript" src="script/ext/ext-all.js"></script>
	<link rel="stylesheet" type="text/css" href="resources/css/ext-all.css"/>
  </head>
  
  <body>
    <div style="position:absolute;left:10px;top:10px;">
	        
	        <script type="text/javascript"> 
	        
	        	Ext.onReady(function(){
	        		var p = new Ext.Panel({
	        			border:false,
	        			id:'viewerPlaceHolder',
	        			width:686,
	        			height:480
	        		});
	        		
	        		var win = new Ext.Window({
	        			id:'mywin',
	        			title:'test',
	        			modal:true,
	        			constrain:true,
	        			width:700,
	        			height:500,
	        			items:[p]
	        		});
	        		
	        		win.show();
	  
	        	
	        		var fp = new FlexPaperViewer(
			'swf/FlexPaperViewer',
			p.body.dom.id, {
				config : {
					SwfFile : escape('swf/admin.swf'),
					Scale : 0.6,
					ZoomTransition : 'easeOut',
					ZoomTime : 0.5,
					ZoomInterval : 0.2,
					FitPageOnLoad : true,
					FitWidthOnLoad : false,
					FullScreenAsMaxWindow : false,
					ProgressiveLoading : false,
					MinZoomSize : 0.2,
					MaxZoomSize : 5,
					SearchMatchAll : false,
					InitViewMode : 'Portrait',
					PrintPaperAsBitmap : false,

					ViewModeToolsVisible : true,
					ZoomToolsVisible : true,
					NavToolsVisible : true,
					CursorToolsVisible : true,
					SearchToolsVisible : true,

					localeChain : 'en_US'
				}
			});
	        	
	        	
	        	})
	        
	        
				
	        </script>
        </div>
        
        
        <!-- <div id='test' style="width:700px;height:500px;border:1px;">
			<object name="_310289979" width="100%" height="100%" id="_310289979" classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" )>
	  			<param name="movie" value="swf/FlexPaperViewer.swf?0.6806788439188907"/>
	  			<param name="allowfullscreen" value="true"/>
	  			<param name="allowscriptaccess" value="always"/>
	  			<param name="quality" value="high"/>
	  			<param name="cachebusting" value="true"/>
	  			<param name="flashvars" value="SwfFile=swf/admin.swf&Scale=0.6&ZoomTransition=easeOut&ZoomTime=0.5&ZoomInterval=0.2&FitPageOnLoad=true&MinZoomSize=0.2&MaxZoomSize=5&InitViewMode=Portrait&ViewModeToolsVisible=true&ZoomToolsVisible=true&NavToolsVisible=true&CursorToolsVisible=true&SearchToolsVisible=true&localeChain=en_US"/>
  			</object>
		</div> -->
  </body>
</html>
