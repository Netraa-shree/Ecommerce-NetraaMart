<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${listTitle} – NetraaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .order-table { width: 100%; border-collapse: collapse; }
        .order-table th, .order-table td {
            padding: 0.75rem; text-align: left; border-bottom: 1px solid var(--gray-200);
        }
        .order-table th { background: var(--gray-50); font-size: 0.875rem; }
        .badge {
            display: inline-block; padding: 0.2rem 0.55rem; border-radius: 999px;
            font-size: 0.75rem; font-weight: 600; background: #dbeafe; color: #1e40af;
        }
        .empty { text-align: center; padding: 3rem; color: var(--gray-600); }
        .tabs { display: flex; gap: 0.5rem; margin-bottom: 1.25rem; }
        .tabs a {
            padding: 0.45rem 1rem; border-radius: var(--radius);
            border: 1px solid var(--gray-200); color: var(--gray-600); font-weight: 500;
        }
        .tabs a.active { background: var(--primary); color: white; border-color: var(--primary); }
    </style>
</head>
<body>
<nav class="navbar">
    <a href="${pageContext.request.contextPath}/" class="brand">NetraaMart</a>
    <nav>
        <a href="${pageContext.request.contextPath}/products">Browse</a>
        <a href="${pageContext.request.contextPath}/cart">Cart</a>
        <a href="${pageContext.request.contextPath}/orders">Orders</a>
        <c:if test="${sessionScope.user.seller || sessionScope.user.admin}">
            <a href="${pageContext.request.contextPath}/seller">Seller</a>
        </c:if>
        <span style="color:var(--gray-600);">Hi, ${sessionScope.user.fullName}</span>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </nav>
</nav>

<div class="container">
    <h1 style="font-size:1.5rem; margin-bottom:0.75rem;">${listTitle}</h1>

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">${successMessage}</div>
    </c:if>

    <c:if test="${sessionScope.user.seller || sessionScope.user.admin}">
        <div class="tabs">
            <a href="${pageContext.request.contextPath}/orders"
               class="${empty param.type ? 'active' : ''}">My Orders</a>
            <a href="${pageContext.request.contextPath}/orders?type=incoming"
               class="${param.type == 'incoming' ? 'active' : ''}">Incoming Orders</a>
        </div>
    </c:if>

    <div class="card">
        <c:choose>
            <c:when test="${empty orders}">
                <div class="empty">
                    <p style="font-size:1.1rem;">No orders yet</p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary" style="margin-top:1rem;">Browse Products</a>
                </div>
            </c:when>
            <c:otherwise>
                <table class="order-table">
                    <thead>
                        <tr>
                            <th>Order #</th>
                            <th>Date</th>
                            <th>Total</th>
                            <th>Status</th>
                            <th>Payment Ref</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="o" items="${orders}">
                            <tr>
                                <td><strong>#${o.id}</strong></td>
                                <td><fmt:formatDate value="${o.createdAt}" pattern="dd MMM yyyy HH:mm"/></td>
                                <td>₹<fmt:formatNumber value="${o.totalAmount}" minFractionDigits="2"/></td>
                                <td><span class="badge">${o.status}</span></td>
                                <td style="font-size:0.85rem; color:var(--gray-600);">${o.paymentRef}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/orders?id=${o.id}" class="btn btn-outline" style="padding:0.3rem 0.65rem; font-size:0.85rem;">View</a>
                                </td>
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
