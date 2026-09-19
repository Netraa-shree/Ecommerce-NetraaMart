<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Checkout – NetraaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .order-summary { margin-bottom: 1.5rem; }
        .order-summary table { width: 100%; border-collapse: collapse; }
        .order-summary th, .order-summary td {
            padding: 0.6rem; text-align: left; border-bottom: 1px solid var(--gray-200);
        }
        .mock-box {
            background: #eff6ff; border: 1px solid #bfdbfe;
            border-radius: var(--radius); padding: 1.25rem; margin: 1.5rem 0;
        }
        .total-row { font-size: 1.25rem; font-weight: 700; margin-top: 1rem; }
    </style>
</head>
<body>
<nav class="navbar">
    <a href="${pageContext.request.contextPath}/" class="brand">NetraaMart</a>
    <nav>
        <a href="${pageContext.request.contextPath}/products">Browse</a>
        <a href="${pageContext.request.contextPath}/cart">Cart</a>
        <span style="color:var(--gray-600);">Hi, ${sessionScope.user.fullName}</span>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </nav>
</nav>

<div class="container" style="max-width:640px;">
    <h1 style="font-size:1.5rem; margin-bottom:0.5rem;">Checkout</h1>
    <p style="color:var(--gray-600); margin-bottom:1.5rem;">Review your order and confirm mock payment</p>

    <c:if test="${not empty error}">
        <div class="alert alert-error">${error}</div>
    </c:if>

    <div class="card order-summary">
        <h2 style="font-size:1.1rem; margin-bottom:1rem;">Order Summary</h2>
        <table>
            <thead>
                <tr>
                    <th>Product</th>
                    <th>Qty</th>
                    <th>Price</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="item" items="${cartItems}">
                    <tr>
                        <td>${item.productName}</td>
                        <td>${item.quantity}</td>
                        <td>₹<fmt:formatNumber value="${item.lineTotal}" minFractionDigits="2"/></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div class="total-row">
            Total: ₹<fmt:formatNumber value="${cartTotal}" minFractionDigits="2"/>
        </div>
    </div>

    <div class="mock-box">
        <h3 style="font-size:1rem; margin-bottom:0.5rem;">Mock Payment</h3>
        <p style="font-size:0.9rem; color:var(--gray-600); margin-bottom:1rem;">
            This is a simulated payment step for the project demo.
            No real money is charged. Click the button below to confirm.
        </p>
        <form method="post" action="${pageContext.request.contextPath}/checkout">
            <input type="hidden" name="confirmPayment" value="yes">
            <button type="submit" class="btn btn-primary btn-block">
                Confirm Mock Payment &amp; Place Order
            </button>
        </form>
    </div>

    <a href="${pageContext.request.contextPath}/cart" class="btn btn-outline">← Back to Cart</a>
</div>

<footer class="footer">NetraaMart &copy; 2026</footer>
</body>
</html>
