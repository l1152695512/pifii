/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.5.24 : Database - cvmsdb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`cvmsdb` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `cvmsdb`;

/*Table structure for table `sys_action_item_tbl` */

DROP TABLE IF EXISTS `sys_action_item_tbl`;

CREATE TABLE `sys_action_item_tbl` (
  `saiid` varchar(40) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `path` varchar(200) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `said` varchar(40) DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `deleted_date` datetime DEFAULT NULL,
  PRIMARY KEY (`saiid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_action_item_tbl` */

insert  into `sys_action_item_tbl`(`saiid`,`create_time`,`path`,`name`,`said`,`deleted`,`deleted_date`) values ('402881524052b321014052d795790004','2013-08-06 16:56:34','/sys/action/grid-view.action','查看视频','402881524052b321014052d674880003',NULL,NULL),('402881524058071c0140581f75db0001','2013-08-07 17:33:11','/setting/usergroup/new-form.action','新增用户组','402881524052b321014052d674880003',NULL,NULL);

/*Table structure for table `sys_action_tbl` */

DROP TABLE IF EXISTS `sys_action_tbl`;

CREATE TABLE `sys_action_tbl` (
  `said` varchar(40) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `remards` varchar(500) DEFAULT NULL,
  `name` varchar(200) DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `deleted_date` datetime DEFAULT NULL,
  PRIMARY KEY (`said`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_action_tbl` */

insert  into `sys_action_tbl`(`said`,`create_time`,`remards`,`name`,`deleted`,`deleted_date`) values ('402881524052b321014052d674880003','2013-08-06 16:55:20','查看视频','查看视频',NULL,NULL),('402881524053102c0140531137230001','2013-08-06 17:59:31','公共权限','公共权限',NULL,NULL);

/*Table structure for table `sys_menu_tbl` */

DROP TABLE IF EXISTS `sys_menu_tbl`;

CREATE TABLE `sys_menu_tbl` (
  `system_menu_id` varchar(40) NOT NULL,
  `parent_menu_id` varchar(40) DEFAULT NULL,
  `menu_text` varchar(32) DEFAULT NULL,
  `icon_class` varchar(32) DEFAULT NULL,
  `action_path` varchar(128) DEFAULT NULL,
  `ext_id` varchar(64) DEFAULT NULL,
  `layout_attr` varchar(256) DEFAULT NULL,
  `leaf` int(11) DEFAULT NULL,
  `expanded` int(11) DEFAULT NULL,
  `index_value` int(11) DEFAULT NULL,
  `enabled` int(11) DEFAULT NULL,
  `isgrey` int(11) DEFAULT NULL,
  `shortcuts` int(11) DEFAULT NULL,
  PRIMARY KEY (`system_menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_menu_tbl` */

insert  into `sys_menu_tbl`(`system_menu_id`,`parent_menu_id`,`menu_text`,`icon_class`,`action_path`,`ext_id`,`layout_attr`,`leaf`,`expanded`,`index_value`,`enabled`,`isgrey`,`shortcuts`) values ('40288152404c125c01404c162aa30002','62','权限管理','icon-chagePwd','sys/action/grid-view.action','','',1,1,16,0,0,1),('402881fe2f2d6777012f2dbc7a4c0011',NULL,'系统设置','icon-monitor','setting/property/grid-view.action','',NULL,0,0,2,0,0,0),('402881fe2f2d6777012f2dbe53040016','62','修改密码','icon-chagePwd','sys/user/update-password.action','',NULL,1,1,1,0,0,0),('402881fe2f9b9a1d012f9ba53ea00004','402881fe2f2d6777012f2dbc7a4c0011','修改密码','icon-chagePwd','sys/user/update-password.action','','100000',1,1,1,0,0,0),('402881fe2f9b9a1d012f9baa3bba000a','402881fe2f2d6777012f2dbc7a4c0011','用户管理','icon-group','','',NULL,0,0,2,0,0,0),('402881fe2f9b9a1d012f9baad48f000b','402881fe2f9b9a1d012f9baa3bba000a','用户组管理','icon-group-manage','setting/usergroup/grid-view.action','',NULL,1,1,4,0,0,0),('402881fe2f9b9a1d012f9bab377f000c','402881fe2f9b9a1d012f9baa3bba000a','分公司/二级单位管理','icon-group-branch','setting/company/grid-view.action','',NULL,1,1,3,0,0,1),('62',NULL,'维护签到','icon-basic','','','sys-manager',0,0,1,0,0,0),('63','62','系统用户管理','icon-basic-user','sys/user/grid-view','','users-view',1,1,5,0,0,0),('73','402881fe2f2d6777012f2dbc7a4c0011','菜单设置','icon-basic-menus','sys/menus/grid-view.action','','menu-setting',1,1,2,0,0,0),('8','402881fe2f2d6777012f2dbc7a4c0011','用户组管理','icon-users-groups','sys/usergroup/grid-view.action','','',1,1,3,0,0,0);

/*Table structure for table `sys_user_tbl` */

DROP TABLE IF EXISTS `sys_user_tbl`;

CREATE TABLE `sys_user_tbl` (
  `user_id` varchar(40) NOT NULL,
  `account_name` varchar(20) DEFAULT NULL,
  `serial_num` varchar(32) DEFAULT NULL,
  `disabled` int(11) DEFAULT '0',
  `password` varchar(128) DEFAULT NULL,
  `pinyin_code` varchar(32) DEFAULT NULL,
  `user_name` varchar(32) DEFAULT NULL,
  `tel_num` varchar(20) DEFAULT NULL,
  `cell_phone` varchar(20) DEFAULT NULL,
  `email` varchar(32) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `last_login` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `user_priority` int(11) DEFAULT NULL,
  `cid` varchar(40) DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `deleted_date` datetime DEFAULT NULL,
  `login_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_user_tbl` */

insert  into `sys_user_tbl`(`user_id`,`account_name`,`serial_num`,`disabled`,`password`,`pinyin_code`,`user_name`,`tel_num`,`cell_phone`,`email`,`description`,`last_login`,`create_time`,`update_time`,`user_priority`,`cid`,`deleted`,`deleted_date`,`login_count`) values ('4028812a3694db33013694e007560073','admin','',0,'ec557bf547043802ef3d625d28f63a92166f03d1','','张三','135','18922177096','','','2013-08-07 17:47:31','2013-08-06 16:28:09','2013-08-06 16:28:09',0,'',0,NULL,23),('402881524057c2f1014057c6cca70002','aaaa',NULL,0,'c7c258883596008d33499095316bf68831cdddb0',NULL,'李','','13888888888','','','2013-08-08 13:20:55','2013-08-07 15:56:21','2013-08-07 15:56:21',NULL,NULL,NULL,NULL,58);

/*Table structure for table `sys_usergroup_action_tbl` */

DROP TABLE IF EXISTS `sys_usergroup_action_tbl`;

CREATE TABLE `sys_usergroup_action_tbl` (
  `sugiad` varchar(40) NOT NULL,
  `sugid` varchar(40) DEFAULT NULL,
  `said` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`sugiad`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_usergroup_action_tbl` */

insert  into `sys_usergroup_action_tbl`(`sugiad`,`sugid`,`said`) values ('402881524052b321014052d929b50005','402881fe2fd89506012fd8a18b970001','402881524052b321014052d674880003');

/*Table structure for table `sys_usergroup_menus_tbl` */

DROP TABLE IF EXISTS `sys_usergroup_menus_tbl`;

CREATE TABLE `sys_usergroup_menus_tbl` (
  `sum_id` varchar(40) NOT NULL,
  `system_menu_id` varchar(40) DEFAULT NULL,
  `sugid` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`sum_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_usergroup_menus_tbl` */

insert  into `sys_usergroup_menus_tbl`(`sum_id`,`system_menu_id`,`sugid`) values ('402881524057c2f1014057c710090004','62','402881524057c2f1014057c563620001'),('402881524057c2f1014057c710090005','40288152404c125c01404c162aa30002','402881524057c2f1014057c563620001'),('402881524057c2f1014057c710090006','402881fe2f2d6777012f2dbe53040016','402881524057c2f1014057c563620001'),('402881524057c2f1014057c710090007','63','402881524057c2f1014057c563620001'),('402881524057c2f1014057c710090008','402881fe2f2d6777012f2dbc7a4c0011','402881524057c2f1014057c563620001'),('402881524057c2f1014057c710090009','402881fe2f9b9a1d012f9ba53ea00004','402881524057c2f1014057c563620001'),('402881524057c2f1014057c71009000a','402881fe2f9b9a1d012f9baa3bba000a','402881524057c2f1014057c563620001'),('402881524057c2f1014057c71009000b','402881fe2f9b9a1d012f9baad48f000b','402881524057c2f1014057c563620001'),('402881524057c2f1014057c71009000c','402881fe2f9b9a1d012f9bab377f000c','402881524057c2f1014057c563620001'),('402881524057c2f1014057c71009000d','73','402881524057c2f1014057c563620001'),('402881524057c2f1014057c71009000e','8','402881524057c2f1014057c563620001');

/*Table structure for table `sys_usergroup_tbl` */

DROP TABLE IF EXISTS `sys_usergroup_tbl`;

CREATE TABLE `sys_usergroup_tbl` (
  `sugid` varchar(40) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `remards` varchar(500) DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `deleted_date` datetime DEFAULT NULL,
  `cid` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`sugid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_usergroup_tbl` */

insert  into `sys_usergroup_tbl`(`sugid`,`create_time`,`name`,`remards`,`deleted`,`deleted_date`,`cid`) values ('402881524057c2f1014057c563620001','2013-08-07 15:54:48','因孚网络','因孚网络',NULL,NULL,'402881e839472485013947257e770001'),('402881fe2fd89506012fd8a18b970001','2011-05-10 14:39:16','管理员用户组','',0,NULL,'402881e839472485013947257e770001');

/*Table structure for table `sys_usergroup_user_tbl` */

DROP TABLE IF EXISTS `sys_usergroup_user_tbl`;

CREATE TABLE `sys_usergroup_user_tbl` (
  `suuid` varchar(40) NOT NULL,
  `user_id` varchar(40) DEFAULT NULL,
  `sugid` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`suuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `sys_usergroup_user_tbl` */

insert  into `sys_usergroup_user_tbl`(`suuid`,`user_id`,`sugid`) values ('4028812a3694db33013694e007610074','4028812a3694db33013694e007560073','402881fe2fd89506012fd8a18b970001'),('402881524057c2f1014057c6ccc60003','402881524057c2f1014057c6cca70002','402881524057c2f1014057c563620001');

/*Table structure for table `yw_company_tbl` */

DROP TABLE IF EXISTS `yw_company_tbl`;

CREATE TABLE `yw_company_tbl` (
  `cid` varchar(40) NOT NULL,
  `cname` varchar(30) DEFAULT NULL,
  `rid` varchar(40) DEFAULT NULL,
  `category` varchar(2) DEFAULT NULL,
  `deleted` int(11) DEFAULT NULL,
  `deleted_date` datetime DEFAULT NULL,
  `fixed_assets` int(11) DEFAULT NULL,
  `dept_no` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`cid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `yw_company_tbl` */

insert  into `yw_company_tbl`(`cid`,`cname`,`rid`,`category`,`deleted`,`deleted_date`,`fixed_assets`,`dept_no`) values ('40288152404c125c01404c14b99f0001','因孚网络','402881fe3127d5510131283920d30001','1',1,'2013-08-06 11:44:34',0,''),('402881e839472485013947257e770001','总经理室','402881fe3127d5510131283920d30001','1',0,NULL,0,'01');

/*Table structure for table `yw_region_tbl` */

DROP TABLE IF EXISTS `yw_region_tbl`;

CREATE TABLE `yw_region_tbl` (
  `RID` varchar(40) NOT NULL,
  `PID` varchar(40) DEFAULT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `DELETED` decimal(1,0) DEFAULT NULL,
  `DELETED_DATE` datetime DEFAULT NULL,
  `CODE` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`RID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `yw_region_tbl` */

insert  into `yw_region_tbl`(`RID`,`PID`,`NAME`,`DELETED`,`DELETED_DATE`,`CODE`) values ('402881fe2f9c5ae6012f9c72a0140005','402881fe3127d5510131283920d30001','广州市',NULL,NULL,'0101'),('402881fe2f9c5ae6012f9c72c3aa0006','402881fe3127d5510131283920d30001','深圳市',NULL,NULL,'755'),('402881fe2f9c5ae6012f9c72e9040007','402881fe3127d5510131283920d30001','佛山市',NULL,NULL,'757'),('402881fe2f9c5ae6012f9c730e4f0008','402881fe3127d5510131283920d30001','东莞市',NULL,NULL,'769'),('402881fe2f9c5ae6012f9c73360b0009','402881fe3127d5510131283920d30001','中山市',NULL,NULL,'760'),('402881fe2f9c5ae6012f9c736151000a','402881fe3127d5510131283920d30001','江门市',NULL,NULL,'750'),('402881fe2f9c5ae6012f9c7383ae000b','402881fe3127d5510131283920d30001','惠州市',NULL,NULL,'752'),('402881fe2f9c5ae6012f9c73aa02000c','402881fe3127d5510131283920d30001','茂名市',NULL,NULL,'668'),('402881fe2f9c5ae6012f9c73d038000d','402881fe3127d5510131283920d30001','珠海市',NULL,NULL,'756'),('402881fe2f9c5ae6012f9c73f033000e','402881fe3127d5510131283920d30001','湛江市',NULL,NULL,'759'),('402881fe2f9c5ae6012f9c7414f1000f','402881fe3127d5510131283920d30001','汕头市',NULL,NULL,'754'),('402881fe2f9c5ae6012f9c7434ce0010','402881fe3127d5510131283920d30001','肇庆市',NULL,NULL,'758'),('402881fe2f9c5ae6012f9c745a670011','402881fe3127d5510131283920d30001','清远市',NULL,NULL,'763'),('402881fe2f9c5ae6012f9c7496370012','402881fe3127d5510131283920d30001','揭阳市',NULL,NULL,'663'),('402881fe2f9c5ae6012f9c74c43c0013','402881fe3127d5510131283920d30001','韶关市',NULL,NULL,'751'),('402881fe2f9c5ae6012f9c74eef60014','402881fe3127d5510131283920d30001','梅州市',NULL,NULL,'753'),('402881fe2f9c5ae6012f9c7511050015','402881fe3127d5510131283920d30001','阳江市',NULL,NULL,'662'),('402881fe2f9c5ae6012f9c7540710016','402881fe3127d5510131283920d30001','潮州市',NULL,NULL,'768'),('402881fe2f9c5ae6012f9c7565010017','402881fe3127d5510131283920d30001','河源市',NULL,NULL,'762'),('402881fe2f9c5ae6012f9c759f990018','402881fe3127d5510131283920d30001','云浮市',NULL,NULL,'766'),('402881fe2f9c5ae6012f9c7605480019','402881fe3127d5510131283920d30001','汕尾市',NULL,NULL,'660'),('402881fe2fc32c95012fc3c64bc00009','402881fe2f9c5ae6012f9c73d038000d','香洲',NULL,NULL,'01'),('402881fe2fc32c95012fc3c6b3b1000a','402881fe2f9c5ae6012f9c73d038000d','金湾',NULL,NULL,'02'),('402881fe2fc32c95012fc3c6fc05000b','402881fe2f9c5ae6012f9c73d038000d','斗门',NULL,NULL,'03'),('402881fe2fc32c95012fc3cd829d000c','402881fe2f9c5ae6012f9c7540710016','潮安县',NULL,NULL,'03'),('402881fe2fc32c95012fc3cde118000d','402881fe2f9c5ae6012f9c7540710016','湘桥区',NULL,NULL,'01'),('402881fe2fc32c95012fc3d018c8000e','402881fe2f9c5ae6012f9c72a0140005','越秀',NULL,NULL,'010101'),('402881fe2fc32c95012fc3d10858000f','402881fe2f9c5ae6012f9c72a0140005','海珠',NULL,NULL,'010102'),('402881fe2fc32c95012fc3d1408f0010','402881fe2f9c5ae6012f9c72a0140005','荔湾',NULL,NULL,'010103'),('402881fe2fc32c95012fc3d184fb0011','402881fe2f9c5ae6012f9c72a0140005','天河',NULL,NULL,'010104'),('402881fe2fc32c95012fc3d1c6970012','402881fe2f9c5ae6012f9c72a0140005','白云',NULL,NULL,'010105'),('402881fe2fc32c95012fc3d1fdb50013','402881fe2f9c5ae6012f9c72a0140005','黄埔',NULL,NULL,'06'),('402881fe2fc32c95012fc3d23d5e0014','402881fe2f9c5ae6012f9c72a0140005','花都',NULL,NULL,'07'),('402881fe2fc32c95012fc3d3bf2b0016','402881fe2f9c5ae6012f9c72a0140005','番禺',NULL,NULL,'09'),('402881fe2fc32c95012fc3d42ce90017','402881fe2f9c5ae6012f9c72a0140005','萝岗',NULL,NULL,'10'),('402881fe2fc32c95012fc3d854440018','402881fe2f9c5ae6012f9c7434ce0010','端州',NULL,NULL,'01'),('402881fe2fc32c95012fc3da3e320019','402881fe2f9c5ae6012f9c7434ce0010','鼎湖',NULL,NULL,'02'),('402881fe2fc32c95012fc3da99ee001a','402881fe2f9c5ae6012f9c7434ce0010','四会',NULL,NULL,'03'),('402881fe2fc32c95012fc3dae271001b','402881fe2f9c5ae6012f9c7434ce0010','高要',NULL,NULL,'04'),('402881fe2fc32c95012fc3db66c4001c','402881fe2f9c5ae6012f9c7434ce0010','怀集',NULL,NULL,'05'),('402881fe2fc32c95012fc3db9cd7001d','402881fe2f9c5ae6012f9c7434ce0010','广宁',NULL,NULL,'06'),('402881fe2fc32c95012fc3dbe22d001e','402881fe2f9c5ae6012f9c7434ce0010','德庆',NULL,NULL,'07'),('402881fe2fc32c95012fc3dc6bef001f','402881fe2f9c5ae6012f9c7434ce0010','封开',NULL,NULL,'08'),('402881fe2fcebe75012fcecc54ba0001','402881fe2f9c5ae6012f9c7540710016','枫溪区',NULL,NULL,'02'),('402881fe2fcebe75012fcecc91080002','402881fe2f9c5ae6012f9c7540710016','饶平县',NULL,NULL,'04'),('402881fe2fcebe75012fced8829600bb','402881fe2f9c5ae6012f9c73360b0009','东区',NULL,NULL,'01'),('402881fe2fcebe75012fced8b96500bc','402881fe2f9c5ae6012f9c73360b0009','西区',NULL,NULL,'02'),('402881fe2fcebe75012fced8f4f700bd','402881fe2f9c5ae6012f9c73360b0009','南区',NULL,NULL,'03'),('402881fe2fcebe75012fced9222200be','402881fe2f9c5ae6012f9c73360b0009','北区',NULL,NULL,'04'),('402881fe2fcebe75012fcee5b74300c8','402881fe2f9c5ae6012f9c730e4f0008','莞城',NULL,NULL,'02'),('402881fe2fcebe75012fcee62d6d00c9','402881fe2f9c5ae6012f9c730e4f0008','万江',NULL,NULL,'03'),('402881fe2fcebe75012fcee68bf800ca','402881fe2f9c5ae6012f9c730e4f0008','东城',NULL,NULL,'04'),('402881fe2fcebe75012fcee6f40900cb','402881fe2f9c5ae6012f9c730e4f0008','南城 ',NULL,NULL,'05'),('402881fe2fcebe75012fcee7680100cc','402881fe2f9c5ae6012f9c730e4f0008','中堂',NULL,NULL,'06'),('402881fe2fcebe75012fcee7a1fc00cd','402881fe2f9c5ae6012f9c730e4f0008','望牛墩',NULL,NULL,'07'),('402881fe2fcebe75012fcee7e31c00ce','402881fe2f9c5ae6012f9c730e4f0008','麻涌',NULL,NULL,'08'),('402881fe2fcebe75012fcee824b900cf','402881fe2f9c5ae6012f9c730e4f0008','石碣',NULL,NULL,'09'),('402881fe2fcebe75012fcee8edf500d0','402881fe2f9c5ae6012f9c730e4f0008','高埗',NULL,NULL,'10'),('402881fe2fcebe75012fcee942bc00d1','402881fe2f9c5ae6012f9c730e4f0008','道窖',NULL,NULL,'11'),('402881fe2fcebe75012fcee97dc100d2','402881fe2f9c5ae6012f9c730e4f0008','沙田',NULL,NULL,'12'),('402881fe2fcebe75012fcee9b91500d3','402881fe2f9c5ae6012f9c730e4f0008','厚街',NULL,NULL,'13'),('402881fe2fcebe75012fcee9faf000d4','402881fe2f9c5ae6012f9c730e4f0008','长安',NULL,NULL,'14'),('402881fe2fcebe75012fceea5d8200d5','402881fe2f9c5ae6012f9c730e4f0008','洪梅',NULL,NULL,'15'),('402881fe2fcebe75012fceea9df600d6','402881fe2f9c5ae6012f9c730e4f0008','寮步',NULL,NULL,'16'),('402881fe2fcebe75012fceeae2ef00d7','402881fe2f9c5ae6012f9c730e4f0008','大朗',NULL,NULL,'17'),('402881fe2fcebe75012fceeb213000d8','402881fe2f9c5ae6012f9c730e4f0008','大岭山',NULL,NULL,'18'),('402881fe2fcebe75012fcf0da68800d9','402881fe2f9c5ae6012f9c730e4f0008','石龙',NULL,NULL,'20'),('402881fe2fcebe75012fcf0ddff700da','402881fe2f9c5ae6012f9c730e4f0008','石排',NULL,NULL,'21'),('402881fe2fcebe75012fcf0e154f00db','402881fe2f9c5ae6012f9c730e4f0008','茶山',NULL,NULL,'22'),('402881fe2fcebe75012fcf0e546b00dc','402881fe2f9c5ae6012f9c730e4f0008','企石',NULL,NULL,'23'),('402881fe2fcebe75012fcf0eab8400dd','402881fe2f9c5ae6012f9c730e4f0008','横沥',NULL,NULL,'24'),('402881fe2fcebe75012fcf0edc0a00de','402881fe2f9c5ae6012f9c730e4f0008','桥头',NULL,NULL,'26'),('402881fe2fcebe75012fcf0f9fb800df','402881fe2f9c5ae6012f9c730e4f0008','樟木头',NULL,NULL,'27'),('402881fe2fcebe75012fcf1164e700e0','402881fe2f9c5ae6012f9c730e4f0008','谢岗',NULL,NULL,'29'),('402881fe2fcebe75012fcf11b47e00e1','402881fe2f9c5ae6012f9c730e4f0008','东坑',NULL,NULL,'31'),('402881fe2fcebe75012fcf11e31000e2','402881fe2f9c5ae6012f9c730e4f0008','常平',NULL,NULL,'32'),('402881fe2fcebe75012fcf120a1100e3','402881fe2f9c5ae6012f9c730e4f0008','黄江',NULL,NULL,'36'),('402881fe2fcebe75012fcf122c0000e4','402881fe2f9c5ae6012f9c730e4f0008','清溪',NULL,NULL,'33'),('402881fe2fcebe75012fcf1253cc00e5','402881fe2f9c5ae6012f9c730e4f0008','塘厦',NULL,NULL,'34'),('402881fe2fcebe75012fcf127c2400e6','402881fe2f9c5ae6012f9c730e4f0008','凤岗',NULL,NULL,'35'),('402881fe2fcebe75012fcf12a49b00e7','402881fe2f9c5ae6012f9c730e4f0008','虎门',NULL,NULL,'37'),('402881fe2fcebe75012fcf226830014e','402881fe2f9c5ae6012f9c72e9040007','南海',NULL,NULL,'01'),('402881fe2fcebe75012fcf25ff90014f','402881fe2f9c5ae6012f9c72e9040007','禅城',NULL,NULL,'02'),('402881fe2fcebe75012fcf26cb9a0150','402881fe2f9c5ae6012f9c72e9040007','顺德',NULL,NULL,'03'),('402881fe2fcebe75012fcf27235f0151','402881fe2f9c5ae6012f9c72e9040007','三水',NULL,NULL,'04'),('402881fe2fcebe75012fcf275f3f0152','402881fe2f9c5ae6012f9c72e9040007','高明',NULL,NULL,'05'),('402881fe2fcebe75012fcf301f4c01c0','402881fe2f9c5ae6012f9c72a0140005','增城',NULL,NULL,'11'),('402881fe2fcebe75012fcf3422270217','402881fe2f9c5ae6012f9c72a0140005','从化',NULL,NULL,'12'),('402881fe2fcebe75012fcf3568820264','402881fe2f9c5ae6012f9c72a0140005','南沙',NULL,NULL,'13'),('402881fe2fcebe75012fcf3a81710337','402881fe2f9c5ae6012f9c7565010017','连平县',NULL,NULL,'01'),('402881fe2fcebe75012fcf3ab9880338','402881fe2f9c5ae6012f9c7565010017','龙川县',NULL,NULL,'02'),('402881fe2fcebe75012fcf3af4100339','402881fe2f9c5ae6012f9c7565010017','紫金县',NULL,NULL,'03'),('402881fe2fcebe75012fcf3b2a05033a','402881fe2f9c5ae6012f9c7565010017','东源县',NULL,NULL,'04'),('402881fe2fcebe75012fcf3d66870342','402881fe2f9c5ae6012f9c7565010017','和平县',NULL,NULL,'05'),('402881fe2fcf429c012fcf4853380035','402881fe2f9c5ae6012f9c7383ae000b','惠城区',NULL,NULL,'01'),('402881fe2fcf429c012fcf488b110036','402881fe2f9c5ae6012f9c7383ae000b','大亚湾区',NULL,NULL,'02'),('402881fe2fcf429c012fcf48bf7f0037','402881fe2f9c5ae6012f9c7383ae000b','博罗县',NULL,NULL,'03'),('402881fe2fcf429c012fcf49014b0038','402881fe2f9c5ae6012f9c7383ae000b','惠东县',NULL,NULL,'04'),('402881fe2fcf429c012fcf4939df0039','402881fe2f9c5ae6012f9c7383ae000b','龙门县',NULL,NULL,'05'),('402881fe2fcf429c012fcf4b6e730042','402881fe2f9c5ae6012f9c7383ae000b','惠阳区',NULL,NULL,'06'),('402881fe2fcf429c012fcf5b217e00f2','402881fe2f9c5ae6012f9c736151000a','新会',NULL,NULL,'01'),('402881fe2fcf429c012fcf5b526100f3','402881fe2f9c5ae6012f9c736151000a','台山',NULL,NULL,'02'),('402881fe2fcf429c012fcf5b864200f4','402881fe2f9c5ae6012f9c736151000a','开平',NULL,NULL,'03'),('402881fe2fcf429c012fcf5bbce300f5','402881fe2f9c5ae6012f9c736151000a','鹤山',NULL,NULL,'04'),('402881fe2fcf429c012fcf5bed2a00f6','402881fe2f9c5ae6012f9c736151000a','恩平',NULL,NULL,'05'),('402881fe2fcf429c012fcf630fc40151','402881fe2f9c5ae6012f9c7496370012','普宁',NULL,NULL,'01'),('402881fe2fcf429c012fcf63c0470153','402881fe2f9c5ae6012f9c7496370012','揭东',NULL,NULL,'03'),('402881fe2fcf429c012fcf63ef560154','402881fe2f9c5ae6012f9c7496370012','揭西',NULL,NULL,'04'),('402881fe2fcf429c012fcf6431220155','402881fe2f9c5ae6012f9c7496370012','惠来',NULL,NULL,'05'),('402881fe2fcf429c012fcf6b447901b2','402881fe2f9c5ae6012f9c73aa02000c','茂南',NULL,NULL,'01'),('402881fe2fcf429c012fcf6b80d601b3','402881fe2f9c5ae6012f9c73aa02000c','电白',NULL,NULL,'02'),('402881fe2fcf429c012fcf6bb2e201b4','402881fe2f9c5ae6012f9c73aa02000c','高州',NULL,NULL,'03'),('402881fe2fcf429c012fcf6be4ff01b5','402881fe2f9c5ae6012f9c73aa02000c','化州',NULL,NULL,'04'),('402881fe2fcf429c012fcf6c19ca01b6','402881fe2f9c5ae6012f9c73aa02000c','信宜',NULL,NULL,'05'),('402881fe2fcf429c012fcf6e6f3401f6','402881fe2f9c5ae6012f9c74eef60014','梅江区',NULL,NULL,'01'),('402881fe2fcf429c012fcf6ea2c701f7','402881fe2f9c5ae6012f9c74eef60014','梅县',NULL,NULL,'02'),('402881fe2fcf429c012fcf6f064401f9','402881fe2f9c5ae6012f9c74eef60014','兴宁市',NULL,NULL,'04'),('402881fe2fcf429c012fcf6f8b3301fb','402881fe2f9c5ae6012f9c74eef60014','五华县',NULL,NULL,'06'),('402881fe2fcf429c012fcf6fc46301fc','402881fe2f9c5ae6012f9c74eef60014','大埔县',NULL,NULL,'07'),('402881fe2fcf429c012fcf6ffce801fd','402881fe2f9c5ae6012f9c74eef60014','丰顺县',NULL,NULL,'08'),('402881fe2fcf429c012fcf7037bf01fe','402881fe2f9c5ae6012f9c74eef60014','蕉岭县',NULL,NULL,'09'),('402881fe2fcf429c012fcf706f8801ff','402881fe2f9c5ae6012f9c74eef60014','平远县',NULL,NULL,'10'),('402881fe2fcf429c012fcfa011ee0234','402881fe2f9c5ae6012f9c745a670011','佛冈',NULL,NULL,'01'),('402881fe2fcf429c012fcfa042f00235','402881fe2f9c5ae6012f9c745a670011','连南',NULL,NULL,'02'),('402881fe2fcf429c012fcfa079fe0236','402881fe2f9c5ae6012f9c745a670011','连山',NULL,NULL,'03'),('402881fe2fcf429c012fcfa0ae4d0237','402881fe2f9c5ae6012f9c745a670011','连州',NULL,NULL,'04'),('402881fe2fcf429c012fcfa0e54b0238','402881fe2f9c5ae6012f9c745a670011','清新',NULL,NULL,'05'),('402881fe2fcf429c012fcfa11ac20239','402881fe2f9c5ae6012f9c745a670011','阳山',NULL,NULL,'06'),('402881fe2fcf429c012fcfa1550c023a','402881fe2f9c5ae6012f9c745a670011','英德',NULL,NULL,'07'),('402881fe2fcf429c012fcfa918a8027f','402881fe2f9c5ae6012f9c7414f1000f','澄海区',NULL,NULL,'01'),('402881fe2fcf429c012fcfa94b420280','402881fe2f9c5ae6012f9c7414f1000f','潮阳区',NULL,NULL,'02'),('402881fe2fcf429c012fcfa97be70281','402881fe2f9c5ae6012f9c7414f1000f','潮南区',NULL,NULL,'03'),('402881fe2fcf429c012fcfa9af890282','402881fe2f9c5ae6012f9c7414f1000f','南澳县',NULL,NULL,'04'),('402881fe2fcf429c012fcface66a02db','402881fe2f9c5ae6012f9c7605480019','海丰县',NULL,NULL,'01'),('402881fe2fcf429c012fcfad1efe02dc','402881fe2f9c5ae6012f9c7605480019','陆丰市',NULL,NULL,'02'),('402881fe2fcf429c012fcfad4b3e02dd','402881fe2f9c5ae6012f9c7605480019','陆河县',NULL,NULL,'03'),('402881fe2fcf429c012fcfaf11390308','402881fe2f9c5ae6012f9c74c43c0013','曲江区',NULL,NULL,'01'),('402881fe2fcf429c012fcfaf44fb0309','402881fe2f9c5ae6012f9c74c43c0013','始兴县',NULL,NULL,'02'),('402881fe2fcf429c012fcfaf79e6030a','402881fe2f9c5ae6012f9c74c43c0013','翁源',NULL,NULL,'03'),('402881fe2fcf429c012fcfafa664030b','402881fe2f9c5ae6012f9c74c43c0013','仁化县',NULL,NULL,'04'),('402881fe2fcf429c012fcfafd63e030c','402881fe2f9c5ae6012f9c74c43c0013','新丰',NULL,NULL,'05'),('402881fe2fcf429c012fcfb004f0030d','402881fe2f9c5ae6012f9c74c43c0013','乐昌市',NULL,NULL,'06'),('402881fe2fcf429c012fcfb036cd030e','402881fe2f9c5ae6012f9c74c43c0013','乳源县',NULL,NULL,'07'),('402881fe2fcf429c012fcfb06c83030f','402881fe2f9c5ae6012f9c74c43c0013','南雄市',NULL,NULL,'08'),('402881fe2fcf429c012fcfb4a42a032a','402881fe2f9c5ae6012f9c72c3aa0006','南山区',NULL,NULL,'01'),('402881fe2fcf429c012fcfb4eaf7032b','402881fe2f9c5ae6012f9c72c3aa0006','宝安区',NULL,NULL,'02'),('402881fe2fcf429c012fcfb52d11032c','402881fe2f9c5ae6012f9c72c3aa0006','福田区',NULL,NULL,'03'),('402881fe2fcf429c012fcfb55d48032d','402881fe2f9c5ae6012f9c72c3aa0006','光明区',NULL,NULL,'04'),('402881fe2fcf429c012fcfb58f16032e','402881fe2f9c5ae6012f9c72c3aa0006','龙岗区',NULL,NULL,'05'),('402881fe2fcf429c012fcfb630580330','402881fe2f9c5ae6012f9c72c3aa0006','罗湖区',NULL,NULL,'07'),('402881fe2fcf429c012fcfb66e3b0331','402881fe2f9c5ae6012f9c72c3aa0006','盐田区',NULL,NULL,'08'),('402881fe2fcf429c012fcfb927510378','402881fe2f9c5ae6012f9c7511050015','江城',NULL,NULL,'01'),('402881fe2fcf429c012fcfb959da0379','402881fe2f9c5ae6012f9c7511050015','阳春',NULL,NULL,'02'),('402881fe2fcf429c012fcfb98b0c037a','402881fe2f9c5ae6012f9c7511050015','阳东',NULL,NULL,'03'),('402881fe2fcf429c012fcfb9bb34037b','402881fe2f9c5ae6012f9c7511050015','阳西',NULL,NULL,'04'),('402881fe2fcf429c012fcfbe076c0398','402881fe2f9c5ae6012f9c759f990018','云城区',NULL,NULL,'01'),('402881fe2fcf429c012fcfbe4cd20399','402881fe2f9c5ae6012f9c759f990018','罗定区',NULL,NULL,'02'),('402881fe2fcf429c012fcfbe80e2039a','402881fe2f9c5ae6012f9c759f990018','新兴区',NULL,NULL,'03'),('402881fe2fcf429c012fcfbeb38b039b','402881fe2f9c5ae6012f9c759f990018','郁南区',NULL,NULL,'04'),('402881fe2fcf429c012fcfbee568039c','402881fe2f9c5ae6012f9c759f990018','云安区',NULL,NULL,'06'),('402881fe2fcf429c012fcfc1142003be','402881fe2f9c5ae6012f9c73f033000e','霞山区',NULL,NULL,'01'),('402881fe2fcf429c012fcfc1417903bf','402881fe2f9c5ae6012f9c73f033000e','开发区',NULL,NULL,'02'),('402881fe2fcf429c012fcfc16df803c0','402881fe2f9c5ae6012f9c73f033000e','赤坎区',NULL,NULL,'03'),('402881fe2fcf429c012fcfc1a45903c1','402881fe2f9c5ae6012f9c73f033000e','廉江区',NULL,NULL,'04'),('402881fe2fcf429c012fcfc1dcaf03c2','402881fe2f9c5ae6012f9c73f033000e','雷州区',NULL,NULL,'05'),('402881fe2fcf429c012fcfc20b1203c3','402881fe2f9c5ae6012f9c73f033000e','遂溪区',NULL,NULL,'06'),('402881fe2fcf429c012fcfc23d6d03c4','402881fe2f9c5ae6012f9c73f033000e','徐闻区',NULL,NULL,'07'),('402881fe2fcf429c012fcfc26be003c5','402881fe2f9c5ae6012f9c73f033000e','吴川区',NULL,NULL,'08'),('402881fe3127872a013127ac00870001','402881fe2f9c5ae6012f9c72a0140005','黄萝',NULL,NULL,'14'),('402881fe3127d5510131283920d30001',NULL,'广东省',NULL,NULL,'gds'),('ff808081312df54101312e0da24e0025','402881fe2f9c5ae6012f9c736151000a','城区',NULL,NULL,'06'),('ff808081312df54101312e0e23f10026','402881fe2f9c5ae6012f9c7540710016','城区',NULL,NULL,'05'),('ff808081312df54101312e0e5bda0027','402881fe2f9c5ae6012f9c7496370012','城区',NULL,NULL,'06'),('ff808081312df54101312e0eaa0a0028','402881fe2f9c5ae6012f9c7605480019','城区',NULL,NULL,'04'),('ff808081312df54101312e0ef2bb0029','402881fe2f9c5ae6012f9c759f990018','城区',NULL,NULL,'07'),('ff808081312df54101312e0f2eba002a','402881fe2f9c5ae6012f9c7565010017','城区',NULL,NULL,'06'),('ff808081312df54101312e0f7410002b','402881fe2f9c5ae6012f9c7511050015','城区',NULL,NULL,'05'),('ff808081312df54101312e0fa5bf002c','402881fe2f9c5ae6012f9c74eef60014','城区',NULL,NULL,'11'),('ff808081312df54101312e0fdff9002d','402881fe2f9c5ae6012f9c74c43c0013','城区',NULL,NULL,'09'),('ff808081312df54101312e102a51002e','402881fe2f9c5ae6012f9c745a670011','城区',NULL,NULL,'08'),('ff808081312df54101312e106a28002f','402881fe2f9c5ae6012f9c7434ce0010','城区',NULL,NULL,'09'),('ff808081312df54101312e109f320030','402881fe2f9c5ae6012f9c7414f1000f','城区',NULL,NULL,'05'),('ff808081312df54101312e10de8d0031','402881fe2f9c5ae6012f9c73f033000e','城区',NULL,NULL,'09'),('ff808081312df54101312e11468e0032','402881fe2f9c5ae6012f9c73d038000d','城区',NULL,NULL,'04'),('ff808081312df54101312e1177df0033','402881fe2f9c5ae6012f9c73aa02000c','城区',NULL,NULL,'06'),('ff808081312df54101312e17a5c8003d','402881fe2f9c5ae6012f9c73360b0009','城区',NULL,NULL,'05'),('ff808081312df54101312e22eaea003e','402881fe2f9c5ae6012f9c73aa02000c','茂港',NULL,NULL,'07'),('ff808081312df54101312e23c627003f','402881fe2f9c5ae6012f9c73aa02000c','效区',NULL,NULL,'08'),('ff808081312df54101312e2aa2fd0040','402881fe2f9c5ae6012f9c72c3aa0006','城区',NULL,NULL,'09'),('ff808081312df54101312e2d8e6c0041','402881fe2f9c5ae6012f9c72e9040007','城区',NULL,NULL,'06');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
