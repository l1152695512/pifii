##部署方式

1、还原数据库文件；在app.properties中修改数据库配置

2、默认账号/密码：liting/123456


使用技术说明：
jfinal shiro quartz druid 
数据库：mysql





##quartz表达式：
http://my.oschina.net/b1412/blog/68082

设置表达式：
需要在web.xml中的主配置文件中加载的：configPlugin(Plagin me){
me.add(new QuartaPlagin());
}
进行配置


## druid 

DruidU工具类 
增删改查使用apacheutil





