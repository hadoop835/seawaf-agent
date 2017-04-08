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
		<h2 style="text-align: center;">${e.id!''}</h2>
		<table class="grid">
			<tr>
				<td class="label">应用ID</td>
				<td class="content">${app.id}</td>
				<td class="label">应用名称</td>
				<td class="content">${app.name}</td>
			</tr>
			<tr>
				<td class="label">服务IP</td>
				<td class="content">${app.ip}</td>
				<td class="label">服务端口</td>
				<td class="content">${app.port}</td>
			</tr>
			<tr>
				<td class="label">异常编号</td>
				<td class="content">${e.id!''}</td>
				<td class="label">发生时间</td>
				<td class="content">${e.created?string("yyyy-MM-dd HH:mm:ss")}</td>
			</tr>
			<tr>
				<td class="label">账号名称</td>
				<td class="content">${e.userName!''}</td>
				<td class="label">用户IP</td>
				<td class="content">${e.ip!''}</td>
			</tr>
			<tr>
				<td class="label">请求URL</td>
				<td class="content" colspan="3">${e.url!''}</td>
			</tr>
			<tr>
				<td class="label">异常消息</td>
				<td class="content" colspan="3"><xmp>${e.message!''}</xmp></td>
			</tr>
			<tr>
				<td class="label">堆栈信息</td>
				<td class="content" colspan="3">
					<xmp>
						${e.stacktrace!''}
					</xmp>
				</td>
			</tr>			
		</table>
	</div>
</div>
<div>
</div>
</body>
</html>