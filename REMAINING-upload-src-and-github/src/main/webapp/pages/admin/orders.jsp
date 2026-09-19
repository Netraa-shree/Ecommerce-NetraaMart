<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Orders – Admin – NetraaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .admin-nav { display: flex; gap: 0.75rem; flex-wrap: wrap; margin-bottom: 1.5rem; }
        .admin-nav a {
            padding: 0.5rem 1rem; border-radius: var(--radius);
            border: 1px solid var(--gray-200); font-weight: 500; color: var(--gray-600);
        }
        .admin-nav a:hover, .admin-nav a.active {
            background: var(--primary); color: white; border-color: var(--primary); text-decoration: none;
        }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 0.7rem; text-align: left; border-bottom: 1px solid var(--gray-200); }
        th { background: var(--gray-50); font-size: 0.875rem; }
        .badge {
            display: inline-block; padding: 0.15rem 0.5rem; border-radius: 999px;
            font-size: 0.75rem; font-weight: 600; background: #dbeafe; color: #1e40af;
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
    <h1 style="font-size:1.5rem; margin-bottom:1rem;">All Orders</h1>
    <div class="admin-nav">
        <a href="${pageContext.request.contextPath}/admin">Overview</a>
        <a href="${pageContext.request.contextPath}/admin/users">Users</a>
        <a href="${pageContext.request.contextPath}/admin/products">Products</a>
        <a href="${pageContext.request.contextPath}/admin/orders" class="active">Orders</a>
    </div>

    <div class="card">
        <c:choose>
            <c:when test="${empty orders}">
                <p style="color:var(--gray-600); padding:1rem;">No orders yet.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>Order #</th>
                            <th>Buyer</th>
                            <th>Total</th>
                            <th>Status</th>
                            <th>Payment Ref</th>
                            <th>Date</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="o" items="${orders}">
                            <tr>
                                <td><strong>#${o.id}</strong></td>
                                <td>${o.buyerName}</td>
                                <td>₹<fmt:formatNumber value="${o.totalAmount}" minFractionDigits="2"/></td>
                                <td><span class="badge">${o.status}</span></td>
                                <td style="font-size:0.85rem;">${o.paymentRef}</td>
                                <td><fmt:formatDate value="${o.createdAt}" pattern="dd MMM yyyy HH:mm"/></td>
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
