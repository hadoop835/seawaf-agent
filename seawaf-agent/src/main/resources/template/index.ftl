<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="refresh" content="5">
<title>SEAWAF</title>
<#include "style.ftl"/>
</head>
<body>
<div>
<h1 class="logo">SEAWAF</h1>
</div>
<div style="margin-top:80px">
	<div style="width: 900px;margin:200px auto">
		<table class="dashboard">
			<tr>
				<td>${sessionsLabel!''}</td>
				<td>${onlineUsersLabel!''}</td>
				<td>${exceptionsLabel!''}</td>
				<td>${ipDeniedLabel!''}</td>
				<td>${ipsLabel!''}</td>
				<td>${urlsLabel!''}</td>
			</tr>
			<tr class="number">
				<td><a href="${path}/seawaf?a=s">${sessions!'0'}</a></td>
				<td><a href="${path}/seawaf?a=u">${onlineUsers!'0'}</a></td>
				<td><a href="${path}/seawaf?a=e">${exceptions!'0'}</a></td>
				<td><a href="${path}/seawaf?a=d">${ipDenied!'0'}</a></td>
				<td><a href="${path}/seawaf?a=i">${ips!'0'}</a></td>
				<td><a href="${path}/seawaf?a=l">${urls!'0'}</a></td>
			</tr>
		</table>
	</div>
</div>
<div>
</div>
</body>
</html>