function myAlert(contents,afterCloseCallback,buttonText){
	if(buttonText == undefined || '' == buttonText){
		buttonText = '关闭';
	}
	var buttons = [{
		text:buttonText,
		classe:'btn primary'
	}];
	$.fn.SimpleModal({
		title: '提示',
        overlayClick:  true,
        closeButton: true,
        buttons: buttons,
        keyEsc: true,
        width: 400,
        contents: contents,
        afterClose:afterCloseCallback
    }).showModal();
}

function myAlertClosePop(contents){
	myAlert(contents,function(){
    	closePop();
    });
}

function myConfirm(contents,callback){
	$.fn.SimpleModal({
        title: '确认',
        width: 400,
        keyEsc:true,
        buttons: [{
	    		text:'确定',
	    		classe:'btn primary btn-margin',
	    		clickEvent:function() {
	            	var optionsIndex = this._popOptionsIndex();
		        	if(optionsIndex != -1){
		        		callback.apply(this);
	                    this.hideModal();
		        	}
	            }
	    	},{
	    		text:'取消',
	    		classe:'btn secondary'
	    	}],
        contents: contents
    }).showModal();
}

function closePop(){
	var index = $.fn._maxZIndexOptionIndex();
	if(index != -1){
		var popId = window.popsOption[index].id;
		$("#simple-modal-"+popId).hideModal();
	}
	
//	if(window.popsOption != undefined && window.popsOption.length > 0){
//		var maxZIndex = -1;
//		var index = -1;
//		for(var i=0;i<window.popsOption.length;i++){
//			var thisOptions = window.popsOption[i];
//			if(thisOptions.zindex > maxZIndex){
//				maxZIndex = thisOptions.zindex;
//				index = i;
//			}
//		}
//		if(index != -1){
//			var popId = window.popsOption[index].id;
//			$("#simple-modal-"+popId).hideModal();
//		}
//	}
}

//function popPage(title,url,data,callback){
//	var thisPage = $.fn.SimpleModal({
//		model: 'modal-ajax',
//		title: title,
//		param: {
//			url: url,
//			data: data,
//			onRequestComplete: callback
//		}
// 	});
//	return thisPage;
//}
