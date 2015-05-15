$(document).ready(function(){
    $(".down_list").click(function(){
		$(this).toggleClass('dir');
		$(".ar_2 li:gt(3)").toggle(200);
	});
});