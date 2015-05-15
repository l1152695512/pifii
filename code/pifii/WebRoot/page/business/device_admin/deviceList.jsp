<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<div class="online">
	<div class="deviceInfoHd">
		<h4 class="fl">在网</h4>
		<span>编辑</span><span>重启</span><span>升级</span>
	</div>
	<div class="deviceInfoBd">
		<ul class="ti">
			<li>设备编号</li>
			<li>鉴权状态</li>
			<li>在网人数</li>
			<li>软件版本</li>
			<li>入网日期</li>
		</ul>
	</div>
	<div class="deviceInfoBd">
		<c:choose>
			<c:when test="${empty onDataList}">
				<ul class="ti">
					<li>没有符合查询的记录！</li>
				</ul>
			</c:when>
			<c:otherwise>
				<c:forEach items="${onDataList}" var="row" varStatus="stat">
					<ul class="ti">
						<li><input type="radio" name="outline" checked />&nbsp;${row.name}</li>
						<li>直通</li>
						<li><span>30</span>人</li>
						<li>v20140601</li>
						<li>2014-8-08-05</li>
					</ul>
				</c:forEach>
			</c:otherwise>
		</c:choose>
		<div class="cl"></div>
	</div>
</div>

<div class="outline">
	<div class="deviceInfoHd">
		<h4 class="fl">脱网</h4>
		<span>编辑</span><span>重启</span><span>升级</span>
	</div>
	<div class="deviceInfoBd">
		<ul class="ti">
			<li>设备编号</li>
			<li>鉴权状态</li>
			<li>软件版本</li>
			<li>入网日期</li>
		</ul>
	</div>
	<div class="deviceInfoFt">
		<ul class="fl">
			<li><input type="radio" name="outline" checked />&nbsp;CMCC-free_PiFii111</li>
			<li><input type="radio" name="outline" />&nbsp;CMCC-free_PiFii111</li>
			<li><input type="radio" name="outline" />&nbsp;CMCC-free_PiFii111</li>
		</ul>
		<ul class="fl">
			<li>直通</li>
			<li>禁通</li>
			<li>鉴权</li>
		</ul>
		<ul class="fl">
			<li>v20140601</li>
			<li>v20140601</li>
			<li>v20140601</li>
		</ul>
		<ul class="fl">
			<li>2014-8-08-05</li>
			<li>2014-8-08-05</li>
			<li>2014-8-08-05</li>
		</ul>
		<div class="cl"></div>
	</div>
</div>
