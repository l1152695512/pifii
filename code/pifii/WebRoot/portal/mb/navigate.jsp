<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
        <meta name="apple-mobile-web-app-capable" content="yes" />
        <meta name="apple-mobile-web-app-status-bar-style" content="black" />
        <meta name="format-detection" content="telephone=no" />
        <title>mo派生活</title>     
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/portal/mb/index2/css/common.css" />
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/portal/mb/index2/css/cer.css" />
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/portal/mb/index2/css/cer_inner.css" />
        <script src="${pageContext.request.contextPath}/portal/mb/index2/js/TouchSlide.1.1.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/portal/mb/index2/js/jquery-1.8.3.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/portal/mb/index2/js/baidu.search.js" type="text/javascript"></script>
    </head>
    <body>
<!--     	<header> -->
<!--         	<h3 class="pic_logo"></h3> -->
<!--         </header> -->
        <div id="slideBox" class="slideBox">
            <div class="bd">
                <ul>
                <c:forEach items="${banner_advs}" var="row">
					<li>
                        <a class="pic" href="${row.link}"><img src="${row.image}" onerror="this.src='${pageContext.request.contextPath}/portal/mb/index1/img/ad-1.jpg'"/></a>
                    </li>
				</c:forEach>
<!--                         <li> -->
<!--                             <a class="pic" href="http://www.yixun.com"><img src="index2/images/banner_01.jpg"  style="height:10rem;min-height:8rem;"/></a> -->
<!--                         </li> -->
<!--                         <li> -->
<!--                             <a class="pic" href="http://www.yixun.com"><img src="index2/images/banner_02.jpg"  style="height:10rem;min-height:8rem;"/></a> -->
<!--                         </li> -->
<!--                         <li> -->
<!--                             <a class="pic" href="http://www.yixun.com"><img src="index2/images/banner_03.jpg"  style="height:10rem;min-height:8rem;"/></a> -->
<!--                         </li> -->
                </ul>
            </div>
            <div class="hd">
                <ul></ul>
            </div>
            <script type="text/javascript">
	            var bannerImgWidth = $(document.body).width();
	        	var bannerImgHeight = 150/290*bannerImgWidth;
	        	$("#slideBox .bd img").css({"width":bannerImgWidth,"height":bannerImgHeight});
                try{
                    TouchSlide({ 
                        slideCell:"#slideBox",
                        titCell:".hd ul", //开启自动分页 autoPage:true ，此时设置 titCell 为导航元素包裹层
                        mainCell:".bd ul", 
                        effect:"leftLoop", 
                        autoPage:true,//自动分页
                        autoPlay:true //自动播放
                    });
                }catch(e){
                }
            </script>
        </div>
        <div class="m_search kline">
            <form id="form" action="http://www.baidu.com/s" target="_self" method="get" accept-charset="utf-8">
                <div class="search">
                	<input type="text" placeholder="请输入关键字" name="wd" id="searchText" autocomplete="off">
                	<input type="hidden" name="tn" value="47096130_pg">
					<input type="hidden" name="ch" value="1">
                    <div id="auto">
                    </div>
                </div>
                <button type="submit" id="btnSearch">百度一下</button>
            </form>
        </div>
        <div class="web_portals">
            <ul>
                <li><a href="http://www.7k7k.com"><img src="${pageContext.request.contextPath}/portal/mb/index2/images/7k.png" ><p>游戏</p></a></li>
                <li><a href="http://www.youku.com"><img src="${pageContext.request.contextPath}/portal/mb/index2/images/youku.png" ><p>优酷</p></a></li>
                <li><a href="http://www.baidu.com"><img src="${pageContext.request.contextPath}/portal/mb/index2/images/baidu.png" ><p>百度</p></a></li>
                <li><a href="http://www.qq.com"><img src="${pageContext.request.contextPath}/portal/mb/index2/images/tengxun.png" ><p>腾讯</p></a></li>
                <li><a href="http://www.163.com"><img src="${pageContext.request.contextPath}/portal/mb/index2/images/wangyi.png" ><p>网易</p></a></li>
                <li><a href="http://www.sina.com.cn"><img src="${pageContext.request.contextPath}/portal/mb/index2/images/xinlang.png" ><p>新浪</p></a></li>
                <li><a href="http://www.sohu.com"><img src="${pageContext.request.contextPath}/portal/mb/index2/images/souhu.png" ><p>搜狐</p></a></li>
                <li><a href="http://www.ifeng.com"><img src="${pageContext.request.contextPath}/portal/mb/index2/images/fenghuang.png" ><p>凤凰</p></a></li>
                <li><a href="http://www.taobao.com"><img src="${pageContext.request.contextPath}/portal/mb/index2/images/taobao.png" ><p>淘宝</p></a></li>
                <li><a href="http://www.meituan.com"><img src="${pageContext.request.contextPath}/portal/mb/index2/images/meituan.png" ><p>美团</p></a></li>
                <li><a href="http://www.dianping.com"><img src="${pageContext.request.contextPath}/portal/mb/index2/images/dianping.png" ><p>点评</p></a></li>
                <li><a href="http://www.58.com"><img src="${pageContext.request.contextPath}/portal/mb/index2/images/58.png" ><p>同城</p></a></li>   
                <li><a href="http://www.jd.com"><img src="${pageContext.request.contextPath}/portal/mb/index2/images/jingdong.png" ><p>京东</p></a></li>
                <li><a href="http://www.tmall.com"><img src="${pageContext.request.contextPath}/portal/mb/index2/images/tianmao.png" ><p>天猫</p></a></li>
                <li><a href="http://xuan.3g.cn"><img src="${pageContext.request.contextPath}/portal/mb/index2/images/3g.png" ><p>3G</p></a></li>
                <li><a href="http://hao.360.cn" target="_self"><img src="${pageContext.request.contextPath}/portal/mb/index2/images/gengduo.png" ><p>更多</p></a></li> 
            </ul>
        	<div class="cl"></div>
        </div>
        <div class="m_title">
            <!-- 头条 -->
            <div class="m_siteclass  kline m_toutiao">
            	<a href="http://news.163.com" target="_self">
                    <div class="title">
                        <h2>头条&ensp;新闻&ensp;<!-- 广州 --></h2>
                        <div class="addinfo">
                            <span class="wyicon"></span>
                        </div>
                    </div>
                    <div class="path_ajax"></div>
                </a>
            </div>
            <!-- 游戏 -->
            <div class="m_siteclass  kline m_youxi">
            	<a href="http://www.91.com" target="_self">
                    <div class="title">
                        <h2>游戏&ensp;娱乐圈</h2>
                        <div class="addinfo">
                            <span>Android 游戏</span>
                        </div>
                    </div>
                    <div class="path_ajax"></div>
                </a>
            </div>
            <!-- 团购 -->
            <div class="m_siteclass  kline m_tuangou">
            	<a href="http://t.yhd.com" target="_self">
                    <div class="title">
                        <h2>团购&ensp;购物&ensp;彩票</h2>
                        <div class="addinfo">
                            <span>热门团购</span>
                        </div>
                    </div>
                    <div class="path_ajax"></div>
                </a>
            </div>
            <!-- 小说 -->
            <div class="m_siteclass  kline m_xiaoshuo">
            	<a href="http://www.qidian.com" target="_self">
                    <div class="title">
                        <h2>小说&ensp;视频&ensp;漫画</h2>
                        <div class="addinfo">
                            <span>莽荒记</span>
                        </div>
                    </div>
                    <div class="path_ajax"></div>
                </a>
            </div>
            <!-- 生活 -->
            <div class="m_siteclass  kline m_shenghuo">
            	<a href="http://www.hao123.com" target="_self">
                    <div class="title">
                        <h2>生活助手</h2>
                        <div class="addinfo">
                            <span>智能公交</span>
                        </div>
                    </div>
                    <div class="path_ajax"></div>
                </a>
            </div>
            <div class="m_copyright">
                <p>CopyRight &copy; MO派生活</p>
            </div>
        </div>
    </body>
</html>
