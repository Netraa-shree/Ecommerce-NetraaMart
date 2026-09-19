<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cart – NetraaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .cart-table { width: 100%; border-collapse: collapse; }
        .cart-table th, .cart-table td {
            padding: 0.85rem; text-align: left; border-bottom: 1px solid var(--gray-200);
        }
        .cart-table th { background: var(--gray-50); font-size: 0.875rem; }
        .qty-input { width: 70px; padding: 0.35rem; text-align: center; }
        .summary {
            margin-top: 1.5rem; padding: 1.25rem;
            background: var(--gray-50); border-radius: var(--radius);
            display: flex; justify-content: space-between; align-items: center; flex-wrap: wrap; gap: 1rem;
        }
        .total { font-size: 1.35rem; font-weight: 700; }
        .empty { text-align: center; padding: 3rem; color: var(--gray-600); }
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

<div class="container">
    <h1 style="font-size:1.5rem; margin-bottom:1.25rem;">Your Cart</h1>

    <div class="card">
        <c:choose>
            <c:when test="${empty cartItems}">
                <div class="empty">
                    <p style="font-size:1.1rem; margin-bottom:0.5rem;">Your cart is empty</p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-primary" style="margin-top:1rem;">Browse Products</a>
                </div>
            </c:when>
            <c:otherwise>
                <table class="cart-table">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Price</th>
                            <th>Quantity</th>
                            <th>Subtotal</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="item" items="${cartItems}">
                            <tr>
                                <td>
                                    <strong>${item.productName}</strong>
                                    <c:if test="${item.quantity > item.stockQty}">
                                        <br><small style="color:var(--danger);">Only ${item.stockQty} available</small>
                                    </c:if>
                                </td>
                                <td>₹<fmt:formatNumber value="${item.unitPrice}" minFractionDigits="2"/></td>
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/cart" style="display:flex; gap:0.35rem; align-items:center;">
                                        <input type="hidden" name="action" value="update">
                                        <input type="hidden" name="productId" value="${item.productId}">
                                        <input type="number" name="quantity" value="${item.quantity}"
                                               min="1" max="${item.stockQty}" class="qty-input">
                                        <button type="submit" class="btn btn-outline" style="padding:0.3rem 0.6rem; font-size:0.8rem;">Update</button>
                                    </form>
                                </td>
                                <td>₹<fmt:formatNumber value="${item.lineTotal}" minFractionDigits="2"/></td>
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/cart">
                                        <input type="hidden" name="action" value="remove">
                                        <input type="hidden" name="productId" value="${item.productId}">
                                        <button type="submit" class="btn btn-danger" style="padding:0.3rem 0.6rem; font-size:0.8rem;">Remove</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <div class="summary">
                    <div class="total">
                        Total: ₹<fmt:formatNumber value="${cartTotal}" minFractionDigits="2"/>
                    </div>
                    <div style="display:flex; gap:0.75rem;">
                        <a href="${pageContext.request.contextPath}/products" class="btn btn-outline">Continue Shopping</a>
                        <a href="${pageContext.request.contextPath}/checkout" class="btn btn-primary">Proceed to Checkout</a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<footer class="footer">NetraaMart &copy; 2026</footer>
</body>
</html>
