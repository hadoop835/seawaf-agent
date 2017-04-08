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
				<th>创建时间</th>
				<th>绑定用户</th>
				<th>频次</th>
			</tr>
			<#list sessions as s>
			<tr>
				<td class="seq">${s_index+1}</td>
				<td>${s.session.id}</td>
				<td>${s.created?string("yyyy-MM-dd HH:mm:ss")}</td>
				<td>${s.userId!''} ${s.userName!''}</td>
				<td>${s.count}</td>
			</tr>
			</#list>
		</table>
	</div>
</div>
<div>
</div>
</body>
</html>