<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en"><head><meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Register | RecSys</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/app.css"></head>
<body><div class="auth">
<section class="auth-side"><h1>RecSys</h1>
<p class="muted">Create an account and tell us what you like. Your recommendations improve as you use the app.</p></section>
<main class="auth-main"><div class="card">
<h2>Create account</h2>
<c:if test="${not empty error}"><div class="alert alert-error" role="alert"><c:out value="${error}"/></div></c:if>
<form method="post" action="${pageContext.request.contextPath}/register">
<input type="hidden" name="csrf" value="<c:out value='${csrf}'/>">
<label for="name">Name</label>
<input id="name" name="name" required minlength="2" value="<c:out value='${name}'/>">
<label for="email">Email</label>
<input id="email" name="email" type="email" required value="<c:out value='${email}'/>">
<label for="password">Password</label>
<input id="password" name="password" type="password" required minlength="8">
<p class="muted">At least 8 characters, with a letter and a digit.</p>
<button class="btn btn-block" type="submit">Register</button>
</form>
<p class="muted">Already registered? <a href="${pageContext.request.contextPath}/login">Sign in</a></p>
</div></main></div></body></html>
