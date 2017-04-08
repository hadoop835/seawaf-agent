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
				<th>IP地址</th>
				<th>封禁时间</th>
				<th>封禁时长</th>
				<th>原因</th>
				<th>操作</th>
			</tr>
			<#list ip99 as ip>
			<tr>
				<td class="seq">${ip_index+1}</td>
				<td>${(ip?split('|'))[0]}</td>
				<td>${(ip?split('|'))[1]}</td>
				<td>永久</td>
				<td>${(ip?split('|'))[3]}</td>
				<td>解除</td>
			</tr>
			</#list>
			<#list ip30 as ip>
			<tr>
				<td class="seq">${ip_index+1}</td>
				<td>${(ip?split('|'))[0]}</td>
				<td>${(ip?split('|'))[1]}</td>
				<td>30min</td>
				<td>${(ip?split('|'))[3]}</td>
				<td>解除</td>
			</tr>
			</#list>
			<#list ip10 as ip>
			<tr>
				<td class="seq">${ip_index+1}</td>
				<td>${(ip?split('|'))[0]}</td>
				<td>${(ip?split('|'))[1]}</td>
				<td>10min</td>
				<td>${(ip?split('|'))[3]}</td>
				<td>解除</td>
			</tr>
			</#list>
			<#list ip5 as ip>
			<tr>
				<td class="seq">${ip_index+1}</td>
				<td>${(ip?split('|'))[0]}</td>
				<td>${(ip?split('|'))[1]}</td>
				<td>5min</td>
				<td>${(ip?split('|'))[3]}</td>
				<td>解除</td>
			</tr>
			</#list>
		</table>
	</div>
</div>
<div>
</div>
</body>
</html>