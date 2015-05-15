var shopPageInfo;
function getSelectedShopId(){
	var shopId = $("#mainPageHeader .selectname .selected_shop p").data("shopId");
	if(shopId != undefined && shopId != ''){
		return shopId;
	}else{
		return "";
	}
}

function getShopPageInfo(){
	return shopPageInfo;
}

function setShopPageInfoStep(step){
	if(step > shopPageInfo.step){
		shopPageInfo.step = step;
	}
}

function getPageId(){
	if(shopPageInfo != ""){
		return getShopPageInfo().id;
	}else{
		return "";
	}
}
