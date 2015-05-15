var scrollElementList = [];
var scrollingTextInterval;
var scrollingTextMoveSize = 1;
var stopTimeLength = 1000;
var scrollingTextIntervalTime = 50;
function scrollMe(element,showWidth){
	if(undefined == showWidth){
		showWidth = $(element).parent().width();
	}
	var thisWidth = $(element).width();
	if(thisWidth > showWidth){//显示宽度小于文字宽度时滚动
		$(element).parent().css({"overflow":"hidden","white-space":"nowrap"});
		$(element).css({"position": "relative","left": "0px"});
		$(element).data("stopTime",0);
		scrollElementList.push({element:element,showWidth:showWidth});
		try{
			clearInterval(scrollingTextInterval);
		}catch(e){
		}
		scrollingTextInterval = setInterval('scrollingText()',scrollingTextIntervalTime);
	}
}

function scrollingText(){
	for(var i=0;i<scrollElementList.length;i++){
		var thisElement = $(scrollElementList[i].element);
		var left = (parseFloat(thisElement.css("left"))-scrollingTextMoveSize)%(thisElement.width()+10-scrollElementList[i].showWidth);
		if(left > parseFloat(thisElement.css("left"))){
			var stopTime = parseInt(thisElement.data("stopTime"));
			if(stopTime >= stopTimeLength){
				thisElement.css("left",left);
				thisElement.data("stopTime",0);
			}else{
				thisElement.data("stopTime",stopTime+scrollingTextIntervalTime);
			}
		}else{
			thisElement.css("left",left);
		}
	}
}
function getMarginPaddingBorderWidth(element){
	return parseFloat($(element).css("margin-right"))
				+parseFloat($(element).css("margin-left"))
				+parseFloat($(element).css("padding-right"))
				+parseFloat($(element).css("padding-left"))
				+parseFloat($(element).css("border"));
}
