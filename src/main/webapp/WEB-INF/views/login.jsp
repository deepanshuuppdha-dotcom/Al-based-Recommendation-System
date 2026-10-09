<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en"><head><meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Sign in | RecSys</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css"></head>
<body><div class="auth">
<section class="auth-side"><h1>RecSys</h1>
<p class="muted">Personalized product and content recommendations, based on what you like and how you browse.</p></section>
<main class="auth-main"><div class="card">
<h2>Sign in</h2>
<c:if test="${param.registered == '1'}"><div class="alert alert-ok" role="status">Account created. Please sign in.</div></c:if>
<c:if test="${not empty error}"><div class="alert alert-error" role="alert"><c:out value="${error}"/></div></c:if>
<form method="post" action="${pageContext.request.contextPath}/login">
<input type="hidden" name="csrf" value="<c:out value='${csrf}'/>">
<label for="email">Email</label>
<input id="email" name="email" type="email" required value="<c:out value='${email}'/>">
<label for="password">Password</label>
<input id="password" name="password" type="password" required>
<button class="btn btn-block" type="submit">Sign in</button>
</form>
<p class="muted">New here? <a href="${pageContext.request.contextPath}/register">Create an account</a></p>
</div></main></div></body></html>
