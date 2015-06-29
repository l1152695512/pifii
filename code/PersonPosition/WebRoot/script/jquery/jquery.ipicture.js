/*
 * iPicture² 1.0.0
 * 
 * Author: Sara D'Alia
 * 
 */
(function($){
 
    $.fn.extend({
         
        //pass the options variable to the function
    	iPicture: function(options) {

            var defaults = {
        		animationBg: "bgblack",
        		animationType:"ltr-slide",
        		button: "moreblack"
            }
                 
            var options =  $.extend(defaults, options);
 
            return this.each(function() {
                var o = options;
                var self = this;
            	//each picture-----.ip_slide 
        		$('#'+o.id).each(function( index, value ) {
        			$(this).find('.ip_tooltip').each(function( index, value ){
        				htmlContent=$(value).html();
        				button = $(value).data('button');
        				round = $(value).data('round');
        				tooltipBg = $(value).data('tooltipbg');
        				var text = $(value).data('text');
        				$(value).html('');
        				if(round!=undefined && round!=""){
        					$('<div class="'+round+'"></div><div class="'+round+'In"></div><div class="'+round+'Inner"></div>')
        						.appendTo(value);
        				}
        				if(text!=undefined && text!=""){
        					var textHtml = $('<div>'+text+'</div>').appendTo(value);
        					textHtml.css({"position":"relative","background":"white","text-align":"center",
        						"top":"16px","border":"1px solid","padding-left":"5px","padding-right":"5px"});
        					var left = 0-textHtml.actual('width')/2;//防止元素隐藏无法获取元素宽度
        					textHtml.css({"left":left});
        				}
        				$('<div class="button '+button+'"></div>').appendTo(value);
        				descrContainer = $('<div class="descrContainer"><div class="ip_descr '+tooltipBg+'"><div class="xs">'+htmlContent+'</div></div></div>')
        					.appendTo(value);
        			});
        		});
        		var interaction=$(self).data('interaction');
        		//each picture
        		$('#'+o.id).each(function( index, value ) {
        			$(this).find('.ip_tooltip').each(function( index, value ){
        				var type=$(value).data('animationtype');
        				switch( type ) {
        				case "ttb-slide":
        					insertDescr($(value),'ttb-before','pass-ttb');
        					//Animation function left to right sliding
        					if(interaction=="click") clickHandler($(value), 'ttb-slide');
        						else mouseOverHandler($(value), 'ttb-slide');
        					break;
        				case "btt-slide":
        					insertDescr($(value),'btt-before','pass-btt');
        					//Animation function left to right sliding
        					if(interaction=="click") clickHandler($(value), 'btt-slide');
        						else mouseOverHandler($(value), 'btt-slide');
        					break;
        				case "rtl-slide":
        					insertDescr($(value),'rtl-before','pass-rtl');
        					//Animation function left to right sliding
        					if(interaction=="click") clickHandler($(value), 'rtl-slide'); 
        						else mouseOverHandler($(value), 'rtl-slide');
        					break;
        				default:
        					insertDescr($(value), 'ltr-before','pass-ltr');
        					//Animation function left to right sliding
        					if(interaction=="click") clickHandler($(value), 'ltr-slide');
        						else mouseOverHandler($(value), 'ltr-slide');
        					break;
        				};
        			});
        		});
            });
            function insertDescr(selector, descrClass, divClass){
            	var descr = selector.find(".ip_descr").addClass(descrClass);
				$('<div class="'+divClass+'"></div>').insertBefore(descr);
            };
            function clickHandler(selector, animationType){
            	var clickToggle=true;
				selector.find(".button").on('click',function(){
					if(clickToggle){
						showTooltip(selector, animationType);
						clickToggle=false;
					} else {
						hideTooltip(selector, animationType);
						clickToggle=true;
					}
				});
            };
            function mouseOverHandler(selector, animationType){
            	selector.find(".button").on('mouseover',function(eventObject){
					showTooltip(selector, animationType);
				});
            	selector.find(".button").on('mouseout', function (eventObject) {
					hideTooltip(selector, animationType);
				});
			};
            function showTooltip(selector, animationType){
            	selector.css('z-index','9999');
            	selector.addClass('show');
            	selector.find(".xs").css('display','block');
            	selector.find(".ip_descr").addClass(animationType);
            };
            function hideTooltip(selector, animationType){
            	selector.css('z-index','1');
            	selector.removeClass('show');
            	selector.find(".xs").css('display','none');
            	selector.find(".ip_descr").removeClass(animationType);
            };
        },
		iPictureInsert: function(value) {
			htmlContent=$(value).html();
			button = $(value).data('button');
			round = $(value).data('round');
			tooltipBg = $(value).data('tooltipbg');
			var text = $(value).data('text');
			$(value).html('');
			if(round!=undefined && round!=""){
				$('<div class="'+round+'"></div><div class="'+round+'In"></div><div class="'+round+'Inner"></div>').appendTo(value);
			}
			if(text!=undefined && text!=""){
				var textHtml = $('<div>'+text+'</div>').appendTo(value);
				textHtml.css({"position":"relative","background":"white","text-align":"center",
					"top":"16px","border":"1px solid","padding-left":"5px","padding-right":"5px"});
				var left = 0-textHtml.actual('width')/2;//防止元素隐藏无法获取元素宽度
				textHtml.css({"left":left});
			}
			if(tooltipBg!=undefined && tooltipBg!=""){
				$('<div class="button '+button+'"></div>').appendTo(value);
			}else{
				$('<div class="route '+button+'"></div>').appendTo(value);
			}
			if(tooltipBg!=undefined && tooltipBg!=""){
				descrContainer = $('<div class="descrContainer"><div class="ip_descr '+tooltipBg+'"><div class="xs">'+htmlContent+'</div></div></div>').appendTo(value);
			}else{
				return;
			}
			var interaction=$(this).data('interaction');
			var type=$(value).data('animationtype');
			switch( type ) {
			case "ttb-slide":
				insertDescr($(value),'ttb-before','pass-ttb');
				//Animation function left to right sliding
				if(interaction=="click") clickHandler($(value), 'ttb-slide');
					else mouseOverHandler($(value), 'ttb-slide');
				break;
			case "btt-slide":
				insertDescr($(value),'btt-before','pass-btt');
				//Animation function left to right sliding
				if(interaction=="click") clickHandler($(value), 'btt-slide');
					else mouseOverHandler($(value), 'btt-slide');
				break;
			case "rtl-slide":
				insertDescr($(value),'rtl-before','pass-rtl');
				//Animation function left to right sliding
				if(interaction=="click") clickHandler($(value), 'rtl-slide'); 
					else mouseOverHandler($(value), 'rtl-slide');
				break;
			default:
				insertDescr($(value), 'ltr-before','pass-ltr');
				//Animation function left to right sliding
				if(interaction=="click") clickHandler($(value), 'ltr-slide');
					else mouseOverHandler($(value), 'ltr-slide');
				break;
			};
			
            function insertDescr(selector, descrClass, divClass){
            	var descr = selector.find(".ip_descr").addClass(descrClass);
				$('<div class="'+divClass+'"></div>').insertBefore(descr);
            };
            function clickHandler(selector, animationType){
            	var clickToggle=true;
				selector.find(".button").on('click',function(){
					if(clickToggle){
						showTooltip(selector, animationType);
						clickToggle=false;
					} else {
						hideTooltip(selector, animationType);
						clickToggle=true;
					}
				});
            };
            function mouseOverHandler(selector, animationType){
            	selector.find(".button").on('mouseover',function(eventObject){
					showTooltip(selector, animationType);
				});
            	selector.find(".button").on('mouseout', function (eventObject) {
					hideTooltip(selector, animationType);
				});
			};
            function showTooltip(selector, animationType){
            	selector.css('z-index','9999');
            	selector.addClass('show');
            	selector.find(".xs").css('display','block');
            	selector.find(".ip_descr").addClass(animationType);
            };
            function hideTooltip(selector, animationType){
            	selector.css('z-index','1');
            	selector.removeClass('show');
            	selector.find(".xs").css('display','none');
            	selector.find(".ip_descr").removeClass(animationType);
            };
        }
    });
     
})(jQuery);