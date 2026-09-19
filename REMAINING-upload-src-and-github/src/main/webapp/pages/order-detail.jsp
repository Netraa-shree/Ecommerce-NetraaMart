<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Order #${order.id} – NetraaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .meta-grid {
            display: grid; grid-template-columns: repeat(auto-fit, minmax(160px, 1fr));
            gap: 1rem; margin-bottom: 1.5rem;
        }
        .meta-item label { display: block; font-size: 0.8rem; color: var(--gray-600); }
        .meta-item span { font-weight: 600; }
        .item-table { width: 100%; border-collapse: collapse; }
        .item-table th, .item-table td {
            padding: 0.65rem; text-align: left; border-bottom: 1px solid var(--gray-200);
        }
        .item-table th { background: var(--gray-50); font-size: 0.875rem; }
        .badge {
            display: inline-block; padding: 0.2rem 0.55rem; border-radius: 999px;
            font-size: 0.75rem; font-weight: 600; background: #dbeafe; color: #1e40af;
        }
    </style>
</head>
<body>
<nav class="navbar">
    <a href="${pageContext.request.contextPath}/" class="brand">NetraaMart</a>
    <nav>
        <a href="${pageContext.request.contextPath}/orders">← Orders</a>
        <span style="color:var(--gray-600);">Hi, ${sessionScope.user.fullName}</span>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </nav>
</nav>

<div class="container" style="max-width:720px;">
    <h1 style="font-size:1.5rem; margin-bottom:1.25rem;">Order #${order.id}</h1>

    <div class="card">
        <div class="meta-grid">
            <div class="meta-item">
                <label>Date</label>
                <span><fmt:formatDate value="${order.createdAt}" pattern="dd MMM yyyy HH:mm"/></span>
            </div>
            <div class="meta-item">
                <label>Status</label>
                <span class="badge">${order.status}</span>
            </div>
            <div class="meta-item">
                <label>Payment Ref</label>
                <span>${order.paymentRef}</span>
            </div>
            <div class="meta-item">
                <label>Buyer</label>
                <span>${order.buyerName}</span>
            </div>
            <div class="meta-item">
                <label>Total</label>
                <span>₹<fmt:formatNumber value="${order.totalAmount}" minFractionDigits="2"/></span>
            </div>
        </div>

        <h2 style="font-size:1.05rem; margin-bottom:0.75rem;">Items</h2>
        <table class="item-table">
            <thead>
                <tr>
                    <th>Product</th>
                    <th>Qty</th>
                    <th>Unit Price</th>
                    <th>Subtotal</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${order.items}">
                    <tr>
                        <td>${item.productName}</td>
                        <td>${item.quantity}</td>
                        <td>₹<fmt:formatNumber value="${item.unitPrice}" minFractionDigits="2"/></td>
                        <td>₹<fmt:formatNumber value="${item.lineTotal}" minFractionDigits="2"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<footer class="footer">NetraaMart &copy; 2026</footer>
</body>
</html>
