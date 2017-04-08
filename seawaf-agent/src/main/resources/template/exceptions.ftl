<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="refresh" content="5">
<title>SEAWAF-EXCEPTIONS</title>
<#include "style.ftl"/>
</head>
<body>
<div>
<h1 class="logo"><span><a href="${path}/seawaf?a=h">返回</a></span>SEAWAF</h1>
</div>
<div style="margin-top:80px">
	<div style="width: 960px;margin:0px auto">
		<p><input type="text" name="q"><a class="btn" href="#">查询</a></p>
		<table class="grid">
			<tr>
				<th class="seq">#</th>
				<th>ID</th>
				<th>发生时间</th>
				<th>请求地址</th>
				<th>操作用户</th>
				<th>用户IP</th>
			</tr>
			<#list exceptions as e>
			<tr>
				<td class="seq">${e_index+1}</td>
				<td><a href="${path}/seawaf?a=ed&q=${e.id}">${e.id}</a></td>
				<td>${e.created?string("yyyy-MM-dd HH:mm:ss")}</td>
				<td>${e.url}</td>
				<td>${e.userName!''}</td>
				<td>${e.ip}</td>
			</tr>
			</#list>
		</table>
	</div>
</div>
<div>
</div>
</body>
</html>