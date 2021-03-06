Date.patterns = {
    ISO8601Long:"Y-m-d H:i:s",
    ISO8601Short:"Y-m-d",
    ShortDate: "n/j/Y",
    LongDate: "l, F d, Y",
    FullDateTime: "l, F d, Y g:i:s A",
    MonthDay: "F d",
    ShortTime: "g:i A",
    LongTime: "g:i:s A",
    H24ShortTime: "H:i",
    H24LongTime: "H:i:s",
    SortableDateTime: "Y-m-d\\TH:i:s",
    UniversalSortableDateTime: "Y-m-d H:i:sO",
    YearMonth: "F, Y",
    WeekDay: "l"
}; 

Ext.state.Manager.setProvider(new Ext.state.CookieProvider({
    expires: new Date(new Date().getTime()+(1000*60*60*24*7)) //7 days from now
}));

Ext.util.Format.comboRenderer = function(combo){
    return function(value){
        var record = combo.findRecord(combo.valueField, value);
        return record ? record.get(combo.displayField) : combo.valueNotFoundText;
    }
};

//Ext.Ajax.on('beforerequest', this.showSpinner, this);

//判断是否登录
Ext.Ajax.on('requestcomplete', function(conn,xhr,options){
try{
if(xhr.getResponseHeader("ext-login") == "true"){
       	var task = new Ext.util.DelayedTask(function(){
		  showLoginWin();
		});
		task.delay(250);
	}

}catch(e){

    if(xhr.getResponseHeader =='undefined'){
       alert(xhr);
    }


}finally{


}
 
});
//Ext.Ajax.on('requestexception', this.hideSpinner, this);
//var arr = ['a','b',false];
//alert(Ext.flatten(arr));
//alert(arr);
function renderDateISOLong(value){
	return value&&Ext.isDate(value)?value.format(Date.patterns.ISO8601Long):"";
}

function renderDateISOShort(value){
	return value&&Ext.isDate(value)?value.format(Date.patterns.ISO8601Short):"";
}

function renderShortTime(value){
	return value&&Ext.isDate(value)?value.format(Date.patterns.ShortTime):"";
}
function renderLongTime(value){
	return value&&Ext.isDate(value)?value.format(Date.patterns.LongTime):"";
}
function renderH24LongTime(value){
	return value&&Ext.isDate(value)?value.format(Date.patterns.H24LongTime):"";
}
function renderWeekDay(value){
	return value&&Ext.isDate(value)?value.format(Date.patterns.WeekDay):"";
}



//form validation
Ext.apply(Ext.form.VTypes, {
    daterange : function(val, field) {
        var date = field.parseDate(val);

        if(!date){
            return false;
        }
        if (field.startDateField && (!this.dateRangeMax || (date.getTime() != this.dateRangeMax.getTime()))) {
            var start = Ext.getCmp(field.startDateField);
            start.setMaxValue(date);
            start.validate();
            this.dateRangeMax = date;
        }
        else if (field.endDateField && (!this.dateRangeMin || (date.getTime() != this.dateRangeMin.getTime()))) {
            var end = Ext.getCmp(field.endDateField);
            end.setMinValue(date);
            end.validate();
            this.dateRangeMin = date;
        }
        /*
         * Always return true since we're only using this vtype to set the
         * min/max allowed values (these are tested for after the vtype test)
         */
        return true;
    },

    password : function(val, field) {
        if (field.initialPassField) {
            var pwd = Ext.getCmp(field.initialPassField);
            return (val == pwd.getValue());
        }
        return true;
    },

    passwordText : '密码不匹配',

    email : function(v){
    	var email = /^(\w+)([\-+.][\w]+)*@(\w[\-\w]*\.){1,5}([A-Za-z]){2,6}$/;
    	return email.test(v) || v =="";
    }
});


var commonSubmitConfig = {
    clientValidation: true,
    success: function(form, action) {
    	if(action.result){
	       var lastAction = function(){
				var closeId = action.result.closeId;
				var reloadId = action.result.reloadId;
				if(closeId){
					var tabs = Ext.getCmp("tabs");
					var tab = tabs.getItem(closeId);
					if(tab){
						tabs.remove(tab);
					}
				}
				if(reloadId){
					var reloadItem = Ext.getCmp(reloadId);
					if(reloadItem){
						var store = reloadItem.store;
						if(store)store.load(store.lastOptions);
					}
				}
	       }
	       if(action.result.msg){
	       	Ext.Msg.alert('成功', action.result.msg, lastAction);
	       }else{
	       	lastAction();
	       }
    	}
    },
    failure: function(form, action) {
        switch (action.failureType) {
            case Ext.form.Action.CLIENT_INVALID:
                //Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
                break;
            case Ext.form.Action.CONNECT_FAILURE:
                Ext.Msg.alert('Failure', '网络连接异常');
                break;
            case Ext.form.Action.SERVER_INVALID:
               Ext.Msg.alert('Failure', action.result.msg);
       }
    }
};

var commonLoadFormConfig = {
	waitMsg:"加载....",
    failure: function(form, action) {
        switch (action.failureType) {
            case Ext.form.Action.CLIENT_INVALID:
                //Ext.Msg.alert('Failure', 'Form fields may not be submitted with invalid values');
                break;
            case Ext.form.Action.CONNECT_FAILURE:
                Ext.Msg.alert('Failure', '网络连接异常');
                break;
            case Ext.form.Action.SERVER_INVALID:
                Ext.Msg.alert('Failure', action.result.errorMessage);
                break;
            case Ext.form.Action.LOAD_FAILURE:
            	Ext.Msg.alert('Failure', action.result.msg);
       }
       //var tabs = Ext.getCmp("tabs");

       //var panel = new Ext.Panel({
       //	    html:action.result
       //})
    }
};
var commonTask = null;
var stopTask = function(){
	if(commonTask){
		Ext.TaskMgr.stop(commonTask);
	}
}
//添加的tab的方法

function addLazyLoadWorkSpaceTab(tabId,tabTitle,loadUrl,layoutConfig,params,icon,closable){
	var tabs = Ext.getCmp("tabs");
	var tab = tabs.getItem(tabId);
	if(closable=='undefined'||closable==undefined)closable=true;//没有关闭来自工作台
	if (tab) {//如果已经添加，仅仅激活它
		tabs.activate(tab);
		return tab;
	} else {		
		var config = {
			itemId : tabId,
			title : tabTitle,
			url:loadUrl,
			closable : closable
		};
		if(icon)Ext.apply(config, {iconCls:icon});
		if(layoutConfig)Ext.apply(config, layoutConfig);

		var panel = new Ext.Panel(config);
		
//            Ext.Ajax.request({
//     		url : loadUrl,
//     		success:function(xhr){
     		  try{
     		  	// var newComponent = eval(xhr.responseText);
     		  	 panel.on("added", function(self, ct) {
					//开始加载tab里面的内容。
					var id = this.itemId;
					var thisPanel = this;
					var ajaxConfig = {
								url : loadUrl,
								success : function(xhr) {
									//try{
										var newComponent = eval(xhr.responseText);
										thisPanel.add(newComponent);
										//视情况 启用 LoadMask
										//thisPanel.loadMask.hide();
										//thisPanel.loadMask = null;
										ct.activate(thisPanel);
										thisPanel.doLayout();
										newComponent.show();
										thisPanel.addListener("activate", function(){
											var temp = this.reloadOnActivate;
						
											if(temp){
												while(temp.reloadOnActivate){
													temp = temp.reloadOnActivate;
												}
												if(temp.store){
													//temp.store.load(temp.store.lastOptions);
													if(commonTask){
														Ext.TaskMgr.stop(commonTask);
													}
													commonTask = {
														run:function(){
															this.load(this.lastOptions);
														},
														scope:temp.store,
														interval:60000
													}
													Ext.TaskMgr.start(commonTask);
												}
											}
										});
										thisPanel.addListener("deactivate", stopTask);
										thisPanel.addListener("destroy", stopTask);
				
									//}catch(e){
				//								var theTabs = Ext.getCmp("tabs");
				//								var theTab = tabs.getItem(tabId);
				//								theTabs.remove(theTab);
				//								if(debugMode)throw e;
									//}
								},
								failure : function() {
									Ext.Msg.alert("失败", "网络通信故障,加载失败");
								}
							};
					if(params) {
						Ext.apply(ajaxConfig, params);
					}
					//alert(ajaxConfig.params);
					Ext.Ajax.request(ajaxConfig);
				});
				
				var newTab = tabs.add(panel);
				newTab.show();
				return newTab;
     		  	
     		  }
     		  catch(e){
     		  	alert("e===="+e);
     		  //showLoginWin();//出现异常之一是用户session失效
     		  }
     		 
//     		  
//     		},
//     		failure:function(){
//     		}
//     	});
		

 
		//视情况 启用 LoadMask
		//panel.loadMask = new Ext.LoadMask(panel.getEl(), {removeMask:true,msg:"请稍等..."});
		//panel.loadMask.show();
	}
}

/*获取加载URL 路径并解析出参数*/

function sprlitObjVar(params,object){

   var args = new Object();
	var query = object.substring(1); // Get query string
	var pairs = query.split("?"); // Break at ampersand
	for(var i=0;i<pairs.length;i++){
	        var pos = pairs[i].indexOf('='); // Look for "name=value"
	       if (pos == -1)
			continue; // If not found, skip
		var argname = pairs[i].substring(0, pos); // Extract the name
		var value = pairs[i].substring(pos + 1); 
		value = decodeURIComponent(value);
		 args[argname]=value;
	}
	if ((params || "") == "")
		return args; // Return the object,you can use it by this method:
	// "getCurArgs()["xx"]" or "getCurArgs().xx"
	else
		return args[params];
}

//没有权限提示 Have no jurisdiction 
function haveNoJurisdictiion(msg){
  if(msg==''||msg==undefined||msg=="undefined") msg="你没有相应的权限,如若需要请与管理员联系!";
  Ext.Msg.alert("提示",msg);
}

function showLoginWin(){
    Ext.getCmp('login-window').show(Ext.getBody());
}
