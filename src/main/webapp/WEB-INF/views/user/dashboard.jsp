<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en"><head><meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Dashboard | RecSys</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css"></head>
<body>
<header class="topbar"><strong>RecSys</strong>
<form method="post" action="${pageContext.request.contextPath}/logout">
<span><c:out value="${sessionScope.user.name}"/> <span class="badge">USER</span></span>
<input type="hidden" name="csrf" value="<c:out value='${csrf}'/>">
<button class="btn btn-ghost" type="submit">Log out</button></form></header>
<div class="container">
<h1>Hello, <c:out value="${sessionScope.user.name}"/></h1>
<p class="muted">Your personalized recommendations will appear here.</p>
<div class="grid"><div class="card"><div class="stat"><c:out value="${prefCount}"/></div><div class="muted">Categories you follow</div></div></div>
</div>
<footer class="footer">&copy; 2024 GUVI Geek Network Pvt. Ltd. All rights reserved. No part of this document may be reproduced or distributed without prior written permission.</footer>
</body></html>
