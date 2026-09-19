<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Admin Dashboard – NetraaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .stats {
            display: grid; grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
            gap: 1rem; margin-bottom: 2rem;
        }
        .stat-card {
            background: white; border: 1px solid var(--gray-200); border-radius: var(--radius);
            padding: 1.25rem; text-align: center;
        }
        .stat-card .num { font-size: 2rem; font-weight: 700; color: var(--primary); }
        .stat-card .label { color: var(--gray-600); font-size: 0.9rem; }
        .admin-nav { display: flex; gap: 0.75rem; flex-wrap: wrap; margin-bottom: 1.5rem; }
        .admin-nav a {
            padding: 0.5rem 1rem; border-radius: var(--radius);
            border: 1px solid var(--gray-200); font-weight: 500; color: var(--gray-600);
        }
        .admin-nav a:hover, .admin-nav a.active {
            background: var(--primary); color: white; border-color: var(--primary); text-decoration: none;
        }
    </style>
</head>
<body>
<nav class="navbar">
    <a href="${pageContext.request.contextPath}/" class="brand">NetraaMart</a>
    <nav>
        <a href="${pageContext.request.contextPath}/admin">Admin</a>
        <span style="color:var(--gray-600);">Hi, ${sessionScope.user.fullName}</span>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </nav>
</nav>

<div class="container">
    <h1 style="font-size:1.5rem; margin-bottom:0.5rem;">Admin Dashboard</h1>
    <p style="color:var(--gray-600); margin-bottom:1.5rem;">Platform overview and moderation</p>

    <div class="admin-nav">
        <a href="${pageContext.request.contextPath}/admin" class="active">Overview</a>
        <a href="${pageContext.request.contextPath}/admin/users">Users</a>
        <a href="${pageContext.request.contextPath}/admin/products">Products</a>
        <a href="${pageContext.request.contextPath}/admin/orders">Orders</a>
    </div>

    <div class="stats">
        <div class="stat-card">
            <div class="num">${userCount}</div>
            <div class="label">Users</div>
        </div>
        <div class="stat-card">
            <div class="num">${productCount}</div>
            <div class="label">Active Products</div>
        </div>
        <div class="stat-card">
            <div class="num">${orderCount}</div>
            <div class="label">Orders</div>
        </div>
    </div>

    <div class="card">
        <h2 style="font-size:1.1rem; margin-bottom:1rem;">Recent Orders</h2>
        <c:choose>
            <c:when test="${empty orders}">
                <p style="color:var(--gray-600);">No orders yet.</p>
            </c:when>
            <c:otherwise>
                <table style="width:100%; border-collapse:collapse;">
                    <thead>
                        <tr style="background:var(--gray-50);">
                            <th style="padding:0.6rem; text-align:left;">#</th>
                            <th style="padding:0.6rem; text-align:left;">Buyer</th>
                            <th style="padding:0.6rem; text-align:left;">Total</th>
                            <th style="padding:0.6rem; text-align:left;">Status</th>
                            <th style="padding:0.6rem; text-align:left;">Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="o" items="${orders}" end="9">
                            <tr style="border-bottom:1px solid var(--gray-200);">
                                <td style="padding:0.6rem;">${o.id}</td>
                                <td style="padding:0.6rem;">${o.buyerName}</td>
                                <td style="padding:0.6rem;">₹<fmt:formatNumber value="${o.totalAmount}" minFractionDigits="2"/></td>
                                <td style="padding:0.6rem;">${o.status}</td>
                                <td style="padding:0.6rem;"><fmt:formatDate value="${o.createdAt}" pattern="dd MMM yyyy"/></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<footer class="footer">NetraaMart &copy; 2026</footer>
</body>
</html>
