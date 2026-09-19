<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Register – NetraaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar">
    <a href="${pageContext.request.contextPath}/" class="brand">NetraaMart</a>
    <nav>
        <a href="${pageContext.request.contextPath}/login">Login</a>
    </nav>
</nav>

<div class="auth-wrapper">
    <div class="card auth-card">
        <h1>Create account</h1>
        <p class="subtitle">Join as a Buyer or Seller</p>

        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/register">
            <div class="form-group">
                <label for="fullName">Full Name</label>
                <input type="text" id="fullName" name="fullName" required
                       placeholder="John Doe" value="${param.fullName}">
            </div>
            <div class="form-group">
                <label for="email">Email</label>
                <input type="email" id="email" name="email" required
                       placeholder="you@example.com" value="${param.email}">
            </div>
            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" required
                       minlength="6" placeholder="At least 6 characters">
            </div>
            <div class="form-group">
                <label for="role">I want to</label>
                <select id="role" name="role" required>
                    <option value="BUYER" ${param.role == 'BUYER' ? 'selected' : ''}>Buy products</option>
                    <option value="SELLER" ${param.role == 'SELLER' ? 'selected' : ''}>Sell products</option>
                </select>
            </div>
            <button type="submit" class="btn btn-primary btn-block">Create account</button>
        </form>

        <p style="margin-top:1.25rem; text-align:center; font-size:0.9rem;">
            Already have an account?
            <a href="${pageContext.request.contextPath}/login">Sign in</a>
        </p>
    </div>
</div>
</body>
</html>
