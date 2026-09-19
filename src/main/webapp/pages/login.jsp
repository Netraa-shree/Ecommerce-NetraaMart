<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login – NetraaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar">
    <a href="${pageContext.request.contextPath}/" class="brand">NetraaMart</a>
    <nav>
        <a href="${pageContext.request.contextPath}/register">Register</a>
    </nav>
</nav>

<div class="auth-wrapper">
    <div class="card auth-card">
        <h1>Welcome back</h1>
        <p class="subtitle">Sign in to your account</p>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/login">
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required
                       placeholder="you@example.com" value="${param.email}">
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required
                       placeholder="••••••••">
            </div>
            <button type="submit" class="btn btn-primary btn-block">Sign in</button>
        </form>

        <p style="margin-top:1.25rem; text-align:center; font-size:0.9rem;">
            Don’t have an account?
            <a href="${pageContext.request.contextPath}/register">Create one</a>
        </p>
    </div>
</div>
</body>
</html>
