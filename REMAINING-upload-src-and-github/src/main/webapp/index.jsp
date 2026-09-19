<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>NetraaMart – Marketplace</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar">
    <a href="${pageContext.request.contextPath}/" class="brand">NetraaMart</a>
    <nav>
        <a href="${pageContext.request.contextPath}/products">Browse</a>
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <c:if test="${sessionScope.user.seller || sessionScope.user.admin}">
                    <a href="${pageContext.request.contextPath}/seller">Seller Dashboard</a>
                </c:if>
                <c:if test="${sessionScope.user.admin}">
                    <a href="${pageContext.request.contextPath}/admin">Admin</a>
                </c:if>
                <a href="${pageContext.request.contextPath}/cart">Cart</a>
                <span style="color:var(--gray-600);">Hi, ${sessionScope.user.fullName}</span>
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login">Login</a>
                <a href="${pageContext.request.contextPath}/register" class="btn btn-primary" style="padding:0.4rem 0.9rem;">Register</a>
            </c:otherwise>
        </c:choose>
    </nav>
</nav>

<div class="container">
    <div style="text-align:center; padding: 3rem 1rem;">
        <h1 style="font-size:2.25rem; margin-bottom:0.75rem;">Welcome to NetraaMart</h1>
        <p style="color:var(--gray-600); font-size:1.1rem; max-width:560px; margin:0 auto 2rem;">
            A simple multi-role marketplace. Buy products, sell your own listings, or manage the platform as Admin.
        </p>
        <div style="display:flex; gap:1rem; justify-content:center; flex-wrap:wrap;">
            <a href="${pageContext.request.contextPath}/products" class="btn btn-primary">Browse Products</a>
            <c:if test="${empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/register" class="btn btn-outline">Become a Seller</a>
            </c:if>
        </div>
    </div>

    <div class="card" style="margin-top:2rem;">
        <h2 style="margin-bottom:1rem;">Full Build Status (F1–F8)</h2>
        <ul style="list-style:none; color:var(--gray-600); line-height:1.8;">
            <li>✓ F1 – Authentication (Register / Login, seeded Admin)</li>
            <li>✓ F2 – Seller product listings (create / edit / delete)</li>
            <li>✓ F3 – Buyer browse, search & product detail</li>
            <li>✓ F4 – Shopping cart with running total</li>
            <li>✓ F5 – Checkout with mock payment</li>
            <li>✓ F6 – Order history (buyer) & incoming orders (seller)</li>
            <li>✓ F7 – Admin panel (users, products, orders)</li>
            <li>✓ F8 – Product reviews & star ratings</li>
        </ul>
        <p style="margin-top:1rem; font-size:0.9rem;">
            Health check: <a href="${pageContext.request.contextPath}/api/v1/health" target="_blank">/api/v1/health</a>
            · Demo seller: seller@netraamart.com
        </p>
    </div>
</div>

<footer class="footer">
    NetraaMart &copy; 2026 · Built for Full Build + Deploy checkpoint
</footer>
</body>
</html>
