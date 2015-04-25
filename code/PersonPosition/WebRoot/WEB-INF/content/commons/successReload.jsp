<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
{
	success:true,
	"msg":"${msg}",
	"closeId":"${closeId}",//自动关闭tab,
	"reloadId":"${reloadId}",//自动刷新视图
	"reForwarUrl":"${reForwarUrl}"//自动跳转url
}