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
				<th>URL地址</th>
				<th>频次</th>
			</tr>
			<#list results as u>
			<tr>
				<td class="seq">${u_index+1}</td>
				<td>${(u?split('|'))[0]}</td>
				<td>${(u?split('|'))[1]}</td>
				<td>${(u?split('|'))[2]}</td>
			</tr>
			</#list>
		</table>
	</div>
</div>
<div>
</div>
</body>
</html>