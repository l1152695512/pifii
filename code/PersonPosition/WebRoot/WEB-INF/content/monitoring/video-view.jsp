<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

(function(){
	//全局变量定义
	var m_iNowChanNo = -1;                           //当前通道号
	var m_iLoginUserId = -1;                         //注册设备用户ID
	var m_iChannelNum = -1;							 //模拟通道总数
	var m_bDVRControl = null;						 //OCX控件对象
	var m_iProtocolType = 0;                         //协议类型，0 – TCP， 1 - UDP
	var m_iStreamType = 0;                           //码流类型，0 表示主码流， 1 表示子码流
	var m_iPlay = 0;                                 //当前是否正在预览

	var viewPanel = new Ext.Panel({
		region:'center',
        layout:'fit',
		html:' <div style="width:400px;height:450px;margin-bottom:5px;"> '+
		 '<div style="float:left;display:; width:1px; height:1px;" id="NetPlayOCX1">'+
         '<object classid="CLSID:CAFCF48D-8E34-4490-8154-026191D73924" codebase="NetVideoActiveX23.cab#version=2,3,13,1" standby="Waiting..." id="HIKOBJECT1" width="100%" height="100%" name="HIKOBJECT1" ></object>'+
       '</div>'+
       '<div style="float:left;display:; width:1px; height:1px;" id="NetPlayOCX2">'+
         '<object classid="CLSID:CAFCF48D-8E34-4490-8154-026191D73924" standby="Waiting..." id="HIKOBJECT2" width="100%" height="100%" name="HIKOBJECT2" ></object>'+
       '</div>'+
       '<div style="float:left;display:; width:1px; height:1px;" id="NetPlayOCX3">'+
         '<object classid="CLSID:CAFCF48D-8E34-4490-8154-026191D73924" standby="Waiting..." id="HIKOBJECT3" width="100%" height="100%" name="HIKOBJECT3" ></object>'+
       '</div>'+
       '<div style="float:left;display:; width:1px; height:1px;" id="NetPlayOCX4">'+
         '<object classid="CLSID:CAFCF48D-8E34-4490-8154-026191D73924" standby="Waiting..." id="HIKOBJECT4" width="100%" height="100%" name="HIKOBJECT4" ></object>'+
       '</div>'+
     '</div>'	
	});
	
	viewPanel.on('beforerender',function(){
	    var myDate = new Date();
		var iYear = myDate.getFullYear();        
		if(iYear < 1971 || iYear > 2037){
			alert("为了正常使用本软件，请将系统日期年限设置在1971-2037范围内！");
		}
		if(document.getElementById("HIKOBJECT1").object == null){
			alert("请先下载控件并注册！");
			m_bDVRControl = null;
		}else{
			m_bDVRControl = document.getElementById("HIKOBJECT1");
			ChangeStatus(1);
			ArrangeWindow(1);
			ButtonPress('LoginDev');
			ButtonPress('getDevChan');
			ButtonPress('Preview:start');
		}
    },viewPanel,{delay:500});
    
    function ChangeStatus(iWindowNum){  
		m_bDVRControl = document.getElementById("HIKOBJECT" + iWindowNum);
		for(var i = 1; i <= 4; i ++){
			if(i == iWindowNum){
				document.getElementById("NetPlayOCX" + i).style.border = "1px solid #00F";
			}else{
				document.getElementById("NetPlayOCX" + i).style.border = "1px solid #EBEBEB";	
			}
		}
	}

	function ArrangeWindow(x){
		var iMaxWidth = 650;
		var iMaxHeight = 600;
		for(var i = 1; i <= 4; i ++){
			if(i <= x){
				document.getElementById("NetPlayOCX" + i).style.display = "";
			}else{
				document.getElementById("NetPlayOCX" + i).style.display = "none";	
			}
		}
		var d = Math.sqrt(x);
		var iWidth = iMaxWidth/d;
		var iHight = iMaxHeight/d;
		for(var j = 1; j <= x; j ++){
			document.getElementById("NetPlayOCX" + j).style.width = iWidth;
			document.getElementById("NetPlayOCX" + j).style.height = iHight;
		}
	}

	function ButtonPress(sKey){
		try{
			switch (sKey){
				case "LoginDev":{
					var szDevIp = "192.168.1.119"; 
					var szDevPort = 8000; 
					var szDevUser = 'admin'; 
					var szDevPwd = '12345'; 
					m_iLoginUserId = m_bDVRControl.Login(szDevIp,szDevPort,szDevUser,szDevPwd);
					if(m_iLoginUserId == -1){
						alert("注册失败！");
					}else{
						for(var i = 2; i <= 4; i ++){
							document.getElementById("HIKOBJECT" + i).SetUserID(m_iLoginUserId);
						}
					}
					break;
				}
				case "LogoutDev":{
					if(m_bDVRControl.Logout()){
						alert("注销成功！");
					}else{
						alert("注销失败！");
					}
					break;
				}
				case "getDevChan":{
					szServerInfo = m_bDVRControl.GetServerInfo();
					var xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
					xmlDoc.async="false"
					xmlDoc.loadXML(szServerInfo)
					m_iChannelNum = parseInt(xmlDoc.documentElement.childNodes[0].childNodes[0].nodeValue);
					if(m_iChannelNum < 1)
					{
						alert("获取通道失败！");
					}
					break;
				}
				case "Preview:start":{
					m_iNowChanNo = 0;
					if(m_iNowChanNo > -1){
						if(m_iPlay == 1){
							m_bDVRControl.StopRealPlay();
						}
						var bRet = m_bDVRControl.StartRealPlay(m_iNowChanNo,m_iProtocolType,m_iStreamType);
						if(bRet){
							m_iPlay = 1;
						}
					}
					break;
				}
				case "Preview:stop":{
					if(m_bDVRControl.StopRealPlay()){
						alert("停止预览成功！");
						m_iPlay = 0;
					}else{
						alert("停止预览失败！");
					}
					break;
				}
				default:{
					break;
				}
			}
		}catch(err){
			alert(err);
		}
	}
	
	var button = Ext.extend(Ext.Button, {
	    text  : '测试',
	    tooltip : '测试',
	    width : 35,
	    height : 35,
	    iconCls : '',
	    handler : function() { 
	      
        }, 
	    initComponent : function() {
	        button.superclass.initComponent.apply(this,arguments);
	    }
	});
	
	var infoPanel = new Ext.FormPanel({
        region:'east',
        width: 280,
	    displayfieldWidth: 75, 
        frame:true,
        bodyStyle:'padding:5px 5px 0',
        defaultType: 'displayfield',   
        items : [{	
			xtype : 'fieldset',
			collapsible : true,
			height:440,
			title:'信息栏',
			items : [{
		               	fieldLabel: '会议名称',name: 'code',anchor : '90%',xtype:'displayfield'			           
		            },{
		               	fieldLabel: '招标标号',xtype : 'displayfield',anchor : '90%',name: 'shortName'         
					},{
		               	fieldLabel: '负责人',anchor : '90%',xtype : 'displayfield',name: 'fullName'         
					},{
		               	fieldLabel: '联系电话',	anchor : '90%',xtype : 'displayfield',name: 'cityName'         
					},{
		               	fieldLabel: '开始时间',anchor : '90%',	xtype : 'displayfield',name: 'ridName'         
					},{
		               	fieldLabel: '结束时间',anchor : '90%',xtype : 'displayfield',	name: 'bgc'         
					},{
		               	fieldLabel: '客户名称',anchor : '90%',xtype : 'displayfield',name: 'businessState'         
					},{
		               	fieldLabel: '甲方代表',anchor : '90%',xtype : 'displayfield',name: 'completionDate'         
					},{
		               	fieldLabel: '备注',anchor : '90%',xtype : 'displayfield',name: 'transCapacity'         
					}
			]	 
		},{	
			xtype : 'fieldset',
			collapsible : true,
			title:'控制栏',
			items : [{
	            layout : 'column',
	            border : false,
	            items : [{
		             columnWidth : 1, 
		             layout : 'fit',
		             bodyStyle:'padding:5px 5px 3px 5px;',
		             align : 'center',
		             border : false,
		             items : [{
		             	xtype:'button',
		             	text  : '关闭',
					    tooltip : '关闭',
					    iconCls:'movies1-icon',
					    iconAlign: 'top',
					    width : 75,
					    height : 65,
					    handler : function() { 
					    	if(window.confirm( '是否确定直播结束? ')){
					    	   	m_bDVRControl.Logout();
								win.close();
					    	}
					    }
		             }]
	             }]
	        }]	 
		}]
    });  
    
    infoPanel.on("render",function(cmp){
    	var config = {
    		url:'input/building/edit-form-data.action',    
    		params:{bid:'402881fe2fcebe75012fcf20b1b6013e'}
    	};
    	Ext.apply(config,commonLoadFormConfig);
    	 	var task = new Ext.util.DelayedTask(function(){
		   	cmp.getForm().load(config);
		});
		task.delay(500);
    });  

    var win = new Ext.Window({
        title: '视频直播',
        iconCls:'movies2-icon',
        width: 950,
        height: 635,
        modal :true,
        layout:'border',
        plain:true,
        constrain: true,
        closable: false,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        items: [viewPanel,infoPanel]
    });
    return win;
})();