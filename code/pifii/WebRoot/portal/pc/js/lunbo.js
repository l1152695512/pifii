			$(document).ready(
				function(){
					var jiangeshijian = 2000; //  ← 请设置这个值，表示自动轮播的间隔时间

					//信号量，指示当前图片编号
					var nowshowpic = 0; 


					//得到所有图片的数量
					var imgamount = $("#tupian ul li").length;
					//控制小圆点，和图片数量一样多
					for(var i = 1 ; i <= imgamount ; i++){
						$("#xiaoyuandian ul").append("<li>" + i + "</li>");
					}
					//让第一个小圆点变蓝
					shezhixiaoyuandian(0);

					//每2秒种，模拟点击一次右按钮
					var mytimer = null;
					zidong();
					function zidong(){
						window.clearInterval(mytimer);
						mytimer = window.setInterval(
							function(){
								$("#anniu .you").trigger("click");
							}
						,jiangeshijian);
					}

					//鼠标进入，图片停止轮播
					$("#lunbo,#anniu").mouseenter(
						function(){
							window.clearInterval(mytimer);
						}
					);

					//鼠标离开，图片继续轮播
					$("#lunbo,#anniu").mouseleave(
						function(){
							zidong();
						}
					);

					//给右边按钮添加监听
					$("#anniu .you").click(
						function(){
							if(!$("#tupian ul").is(":animated")){
								if(nowshowpic < imgamount - 1){
									nowshowpic ++
								}else{
									nowshowpic = 0;
								}
								huantu(nowshowpic);
								shezhixiaoyuandian(nowshowpic);
							}
						}
					);

					//给右边按钮添加监听
					$("#anniu .zuo").click(
						function(){
							if(!$("#tupian ul").is(":animated")){
								if(nowshowpic > 0){
									nowshowpic --
								}else{
									nowshowpic = imgamount - 1;
								}
								console.log("主人，信号量已经改变哦~" + nowshowpic);
								huantu(nowshowpic);
								shezhixiaoyuandian(nowshowpic);
							}
						}
					);

					//给所有小圆点添加监听
					$("#xiaoyuandian ul li").click(
						function(){
							nowshowpic = $(this).index();
							huantu(nowshowpic);
							shezhixiaoyuandian(nowshowpic);
						}
					);

					//让第num个li有cur，其余没cur
					function shezhixiaoyuandian(num){
						$("#xiaoyuandian ul li").eq(num).addClass("cur").siblings().removeClass("cur");
					}

					//核心函数，跑火车的执行者。
					function huantu(num){
						$("#tupian ul").animate(
							{
								"left": -1 * $("#tupian ul li img").width() * num
							}
							,1000
						);
					}
				}
			);
			
