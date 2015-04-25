<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
(function(){
	var tabTitle = "预约向导";
	var saveFormUrl = 'meeting/book/save-form.action';	
	var flag='000';
	var flag1='000';
	var flag2='000';
	var ctype='';
	var croom='';
	var cname='';
	var corder='';
	var cstart='';
	var cend='';
	var cpro='';
	var ccom='';
	var cper='';
	var cwper='';
	var cmemo='';
	
	var room = new Ext.data.SimpleStore({
		fields: ['id', 'name'],
		data : [['1','1号会议室'],['2','2号会议室'],['3','3号会议室'],['4','4号会议室'],['5','5号会议室'],['6','6号会议室']],
		autoLoad:true
	});	
	
	var mType = new Ext.data.SimpleStore({
		fields: ['id', 'name'],
		data : [['1','招投标会议'],['2','其他']],
		autoLoad:true
	});
	
	var start = new Ext.data.SimpleStore({
		fields: ['id', 'name'],
		data : [['1','8:30:00'],['2','9:00:00'],['3','9:30:00'],['4','10:00:00'],['5','10:30:00'],['6','11:00:00'],['7','11:30:00'],['8','12:00:00'],['9','12:30:00'],['10','13:00:00'],['11','13:30:00'],['12','14:00:00'],['13','14:30:00'],['14','15:00:00'],['15','15:30:00'],['16','16:00:00'],['17','16:30:00'],['18','17:00:00']],
		autoLoad:true
	});
	
	var end = new Ext.data.SimpleStore({
		fields: ['id', 'name'],
		data : [['1','9:00:00'],['2','9:30:00'],['3','10:00:00'],['4','10:30:00'],['5','11:00:00'],['6','11:30:00'],['7','12:00:00'],['8','12:30:00'],['9','13:00:00'],['10','13:30:00'],['11','14:00:00'],['12','14:30:00'],['13','15:00:00'],['14','15:30:00'],['15','16:00:00'],['16','16:30:00'],['17','17:00:00'],['18','17:30:00']],
		autoLoad:true
	});
	
	var top=new Ext.Panel({
		id:'top',
		height:20,
		title:'第一步', 
		style : 'margin:5px 0px 5px 0px;'
	});
	
	var content=new Ext.FormPanel({
		id:'content',
		height:355,
		frame:true,
		x:0,
		y:40,
		style : 'margin:5px 0px 5px 0px;',
	    defaultType: 'textfield',
	    defaults: {width: 200},
	    labelAlign: 'center',
		items : [{
			xtype: 'combo',
			fieldLabel: '会议类型',
			allowBlank : false,
           	blankText: '请选择会议类型',
			mode: 'local',
			store: mType,
			editable: false, 
			triggerAction : 'all',
			displayField:'name',
			valueField:'name',
			id:'type',
			autoSelect : true,
			emptyText : '请选择'
		},{
			xtype: 'combo',
			fieldLabel: '会议室',
			allowBlank : false,
           	blankText: '请选择会议室',
			mode: 'local',
			editable: false, 
			store: room,
			triggerAction : 'all',
			displayField:'name',
			valueField:'name',
			autoSelect : true,
			emptyText : '请选择',
			id:'room'
		},{
	    	fieldLabel: '会议名称',
           	allowBlank : false,
           	blankText: '请输入会议名称',
           	maxLength:20,
           	id:'mname'
	    },{
	    	fieldLabel: '预约人',
           	allowBlank : false,
           	blankText: '请输入预约人名称',
           	maxLength:20,
           	readOnly:true,
           	id:'order'
	    },{
	   		xtype:'datefield',
       		fieldLabel: '预约日期',
			format:'Y-m-d',
			minValue : new Date(),
			editable:false,
			value:new Date(),
       		id:'book',
       	},{
	    	xtype: 'combo',
			fieldLabel: '开始时间',
			allowBlank : false,
           	blankText: '请选择开始时间',
			mode: 'local',
			editable: false, 
			store: start,
			triggerAction : 'all',
			displayField:'name',
			valueField:'name',
			autoSelect : true,
			emptyText : '请选择',
			id:'start'
        },{
       		xtype: 'combo',
			fieldLabel: '结束时间',
			allowBlank : false,
           	blankText: '请选择结束时间',
			mode: 'local',
			editable: false, 
			store: end,
			triggerAction : 'all',
			displayField:'name',
			valueField:'name',
			autoSelect : true,
			emptyText : '请选择',
			id:'end'
       	}]
	});
	
	var content1=new Ext.FormPanel({
		id:'content1',
		height:355,
		frame:true,
		x:0,
		y:40,
		style : 'margin:5px 0px 5px 0px;',
	    defaultType: 'textfield',
	    defaults: {width: 200},
	    labelAlign: 'center',
		items : [{
	    	fieldLabel: '招标项目',
           	allowBlank : false,
           	blankText: '请输入招标项目',
           	id:'pro'
	    },{
	    	fieldLabel: '招标单位',
           	allowBlank : false,
           	blankText: '请输入招标单位',
           	id:'com'
	    },{
	    	fieldLabel: '参与人',
           	allowBlank : false,
           	blankText: '请输入参与人',
           	id:'per'
	    },{
	    	fieldLabel: '外部人员',
           	allowBlank : false,
           	blankText: '请输入外部人员',
           	id:'wper'
	    },{
	    	fieldLabel: '会议描述',
	    	allowBlank : false,
           	blankText: '请输入会议描述',
           	id:'memo'
	    }]
	});
	
	var content2=new Ext.FormPanel({
		url:saveFormUrl,
		id:'content2',
		height:355,
		frame:true,
		x:0,
		y:40,
		style : 'margin:5px 0px 5px 0px;',
	    defaultType: 'textfield',
	    defaults: {width: 200},
	    labelAlign: 'center',
		items : [{
	    	fieldLabel: '会议类型',
	    	readOnly:true,
           	id:'rrType',
           	name:'M_TYPE'
	    },{
	    	fieldLabel: '会议室',
	    	readOnly:true,
           	id:'rrRoom',
           	name:'M_ROOM'           	
	    },{
	    	fieldLabel: '会议名称',
	    	readOnly:true,
           	id:'rrName',
           	name:'M_NAME'
	    },{
	    	fieldLabel: '预约人',
	    	readOnly:true,
           	id:'rrBook',
           	name:'CHARGE'
	    },{
	    	fieldLabel: '开始时间',
	    	readOnly:true,
           	id:'rrStart',
           	name:'START_TIME'
	    },{
	    	fieldLabel: '结束时间',
	    	readOnly:true,
           	id:'rrEnd',
           	name:'END_TIME'
	    },{
	    	fieldLabel: '招标项目',
	    	readOnly:true,
           	id:'rrPro',
           	name:'PROJECT'
	    },{
	    	fieldLabel: '招标单位',
	    	readOnly:true,
           	id:'rrCom',
           	name:'COMPANY'
	    },{
	    	fieldLabel: '参与人',
	    	readOnly:true,
           	id:'rrPer',
           	name:'JOIN'
	    },{
	    	fieldLabel: '外部人员',
	    	readOnly:true,
           	id:'rrWPer',
           	name:'W_JOIN'
	    },{
	    	fieldLabel: '会议描述',
	    	readOnly:true,
           	id:'rrMemo',
           	name:'MEMO'
	    }]
	});
	
	var content3=new Ext.FormPanel({
		url:saveFormUrl,
		id:'content3',
		height:355,
		frame:true,
		x:0,
		y:40,
		style : 'margin:5px 0px 5px 0px;',
	    defaultType: 'textfield',
	    defaults: {width: 200},
	    labelAlign: 'center',
		items : [{
	    	fieldLabel: '会议类型',
	    	readOnly:true,
           	id:'rType',
           	name:'M_TYPE'
	    },{
	    	fieldLabel: '会议室',
	    	readOnly:true,
           	id:'rRoom',
           	name:'M_ROOM'
	    },{
	    	fieldLabel: '会议名称',
	    	readOnly:true,
           	id:'rName',
           	name:'M_NAME'
	    },{
	    	fieldLabel: '预约人',
	    	readOnly:true,
           	id:'rBook',
           	name:'CHARGE'
	    },{
	    	fieldLabel: '开始时间',
	    	readOnly:true,
           	id:'rStart',
           	name:'START_TIME'
	    },{
	    	fieldLabel: '结束时间',
	    	readOnly:true,
           	id:'rEnd',
           	name:'END_TIME'
	    }]
	});
	
	var bottom3=new Ext.Panel({
		id:'bottom3',
		height:40,
		frame:true,
		x:0,
		y:400,
		style : 'margin:5px 0px 5px 0px;',
		buttonAlign: 'center',
		buttons:[{
			text:'上一步',
			handler:function(){
				Ext.getCmp('top').setTitle('第一步');
				Ext.getCmp('bottom3').hide();
				Ext.getCmp('content3').hide();
				Ext.getCmp('bottom').show();
				Ext.getCmp('content').show();
				Ext.getCmp('left').body.update('<img id="ddd7" src="meeting/asd.png" />');
				Ext.getCmp('right').doLayout();
			}
		},{
			text:'预约',
			handler:function(){
				Ext.getCmp('content3').getForm().submit({
            		success:function(form,action){
            			Ext.Msg.alert('提示','预约完成！');
            			var tt=Ext.util.Format.date(Ext.getCmp('book').getValue(), 'Y-m-d');
						store.setBaseParam('queryInfo',tt);
						store.reload();
            			newWin.close();
            		},
            		failure:function(form,action){
                    	if(action.result){
		            		Ext.Msg.alert('提示','请选择其他时间段进行预约！');
		            	}
                    }
            	});
			}
		},{
			text:'取消',
			handler:function(){
				newWin.close();
			}
		}]
	});
	
	var bottom2=new Ext.Panel({
		id:'bottom2',
		height:40,
		frame:true,
		x:0,
		y:400,
		style : 'margin:5px 0px 5px 0px;',
		buttonAlign: 'center',
		buttons:[{
			text:'上一步',
			handler:function(){
				Ext.getCmp('top').setTitle('第二步');
				Ext.getCmp('bottom2').hide();
				Ext.getCmp('content2').hide();
				Ext.getCmp('bottom1').show();
				Ext.getCmp('content1').show();
				Ext.getCmp('left').body.update('<img id="ddd6" src="meeting/asd1.png" />');
				Ext.getCmp('right').doLayout();
			}
		},{
			text:'预约',
			handler:function(){
				Ext.getCmp('content2').getForm().submit({
            		success:function(form,action){
            			Ext.Msg.alert('提示','预约完成！');
            			var tt=Ext.util.Format.date(Ext.getCmp('book').getValue(), 'Y-m-d');
						store.setBaseParam('queryInfo',tt);
						store.reload();
            			newWin.close();
            		},
            		failure:function(form,action){
                    	if(action.result){
		            		Ext.Msg.alert('提示','请选择其他时间段进行预约！');
		            	}
                    }
            	});
			}
		},{
			text:'取消',
			handler:function(){
				newWin.close();
			}
		}]
	});
	
	var bottom1=new Ext.Panel({
		id:'bottom1',
		height:40,
		frame:true,
		x:0,
		y:400,
		style : 'margin:5px 0px 5px 0px;',
		buttonAlign: 'center',
		buttons:[{
			text:'上一步',
			handler:function(){
				Ext.getCmp('top').setTitle('第一步');
				Ext.getCmp('bottom1').hide();
				Ext.getCmp('content1').hide();
				Ext.getCmp('bottom').show();
				Ext.getCmp('content').show();
				Ext.getCmp('left').body.update('<img id="ddd5" src="meeting/asd.png" />');
				Ext.getCmp('right').doLayout();
			}
		},{
			text:'下一步',
			handler:function(){
				cpro=Ext.getCmp('pro').getValue();
				ccom=Ext.getCmp('com').getValue();
				cper=Ext.getCmp('per').getValue();
				cwper=Ext.getCmp('wper').getValue();
				cmemo=Ext.getCmp('memo').getValue();
				if(cpro=='' || ccom=='' || cper=='' || cwper=='' || cmemo==''){
					Ext.Msg.alert('提示','请先填写完整信息');
				}else{
					Ext.getCmp('top').setTitle('第三步:  预约信息确认');
					Ext.getCmp('bottom1').hide();
					Ext.getCmp('content1').hide();
					if(flag1=='000'){
						Ext.getCmp('right').add(content2);
						Ext.getCmp('right').add(bottom2);
					}else{
						Ext.getCmp('bottom2').show();
						Ext.getCmp('content2').show();
					}
					Ext.getCmp('rrType').setValue(ctype);
					Ext.getCmp('rrRoom').setValue(croom);
					Ext.getCmp('rrName').setValue(cname);
					Ext.getCmp('rrBook').setValue(corder);
					Ext.getCmp('rrStart').setValue(cstart);
					Ext.getCmp('rrEnd').setValue(cend);
					Ext.getCmp('rrPro').setValue(cpro);
					Ext.getCmp('rrCom').setValue(ccom);
					Ext.getCmp('rrPer').setValue(cper);
					Ext.getCmp('rrWPer').setValue(cwper);
					Ext.getCmp('rrMemo').setValue(cmemo);
					Ext.getCmp('left').body.update('<img id="ddd4" src="meeting/asd2.png" />');
					flag1='111';
					Ext.getCmp('right').doLayout();
				}
			}
		},{
			text:'取消',
			handler:function(){
				newWin.close();
			}
		}]
	});
	
	var parseNum=function(str){
		var a=str.split(' ');
		var b=a[0];
		var c=a[1];
		var d=b.split('-');
		var e=c.split(':');
		var result=d[0]+d[1]+d[2]+e[0]+e[1]+e[2];
		return parseInt(result);
	}
	
	var bottom=new Ext.Panel({
		id:'bottom',
		height:40,
		frame:true,
		x:0,
		y:400,
		style : 'margin:5px 0px 5px 0px;',
		buttonAlign: 'center',
		buttons:[{
			text:'下一步',
			handler:function(){
				ctype=Ext.getCmp('type').getValue();
				croom=Ext.getCmp('room').getValue();
				cname=Ext.getCmp('mname').getValue();
				corder=Ext.getCmp('order').getValue();
				var vv=Ext.util.Format.date(Ext.getCmp('book').getValue(), 'Y-m-d');
				var ss=Ext.getCmp('start').getValue();
				var ee=Ext.getCmp('end').getValue();
				cstart=vv+' '+ss;
				cend=vv+' '+ee;
				if(ctype=='' || croom=='' || cname=='' || ss=='' || ee==''){
					Ext.Msg.alert('提示','请先填写完整信息');
				}else{
					var i=ss.split(':');
					var j=i[0]+i[1]+i[2];
					var t=ss;
					if(i[0].length==1){
						t='0'+t;
					}
					var m=ee.split(':');
					var l=m[0]+m[1]+m[2];
					var a=Ext.util.Format.date(new Date(), 'Y-m-d H:i:s');
					var b=vv+' '+t;
					if(parseInt(j)>parseInt(l)){
						Ext.Msg.alert('提示','开始时间不能大于结束时间');
					}else{
						if(parseNum(a)>parseNum(b)){
							Ext.Msg.alert('提示','开始时间不能小于当前时间');
						}else{
							Ext.getCmp('bottom').hide();
							Ext.getCmp('content').hide();
							if(ctype=='其他'){
								Ext.getCmp('top').setTitle('第二步:  预约信息确认');
								if(flag2=='000'){
									Ext.getCmp('right').add(content3);
									Ext.getCmp('right').add(bottom3);
								}else{
									Ext.getCmp('bottom3').show();
									Ext.getCmp('content3').show();
								}
								Ext.getCmp('rType').setValue(ctype);
								Ext.getCmp('rRoom').setValue(croom);
								Ext.getCmp('rName').setValue(cname);
								Ext.getCmp('rBook').setValue(corder);
								Ext.getCmp('rStart').setValue(cstart);
								Ext.getCmp('rEnd').setValue(cend);
								Ext.getCmp('left').body.update('<img id="ddd3" src="meeting/asd2.png" />');
								flag2='111';
							}else{
								Ext.getCmp('top').setTitle('第二步');
								if(flag=='000'){
									Ext.getCmp('right').add(content1);
									Ext.getCmp('right').add(bottom1);
								}else{
									Ext.getCmp('bottom1').show();
									Ext.getCmp('content1').show();
								}
								Ext.getCmp('left').body.update('<img id="ddd2" src="meeting/asd1.png" />');
								flag='111';
							}
						}
						Ext.getCmp('right').doLayout();
					}
				}
			}
		},{
			text:'取消',
			handler:function(){
				newWin.close();
			}
		}]
	});
	
    var right = new Ext.Panel({
    	id:'right',
    	region : 'center',
	    frame:true,
	    width:380,
 		height:500,
 		header : false,
		border : false,
		layout : 'form',
	    items : [top,content,bottom]
    });
    
    content.on("render",function(cmp){
    	var user='${userName}';
    	Ext.getCmp('order').setValue(user);
    });
    
    var left = new Ext.Panel({
    	id:'left',
		region : 'west',
        frame:true,
	    width:300,
 		height:500,
        bodyStyle:'padding:5px 5px 0',
        html:'<img id="ddd1" src="meeting/asd.png" />'
    });
    
	var mainPanel = new Ext.Panel({
		id:'main',
    	header : false,
		border : false,
		layout : 'border',
		items : [left,right]
	});
	
    return mainPanel;
})();



