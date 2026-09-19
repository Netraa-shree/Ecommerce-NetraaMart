<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Seller Dashboard – NetraaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .product-table { width: 100%; border-collapse: collapse; margin-top: 1rem; }
        .product-table th, .product-table td {
            padding: 0.75rem; text-align: left; border-bottom: 1px solid var(--gray-200);
        }
        .product-table th { background: var(--gray-50); font-weight: 600; font-size: 0.875rem; }
        .badge {
            display: inline-block; padding: 0.2rem 0.5rem; border-radius: 999px;
            font-size: 0.75rem; font-weight: 600;
        }
        .badge-ok { background: #dcfce7; color: #166534; }
        .badge-low { background: #fef3c7; color: #92400e; }
        .actions { display: flex; gap: 0.5rem; }
        .actions form { display: inline; }
        .empty-state { text-align: center; padding: 3rem 1rem; color: var(--gray-600); }
    </style>
</head>
<body>
<nav class="navbar">
    <a href="${pageContext.request.contextPath}/" class="brand">NetraaMart</a>
    <nav>
        <a href="${pageContext.request.contextPath}/products">Browse</a>
        <a href="${pageContext.request.contextPath}/seller">Seller Dashboard</a>
        <a href="${pageContext.request.contextPath}/cart">Cart</a>
        <span style="color:var(--gray-600);">Hi, ${sessionScope.user.fullName}</span>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </nav>
</nav>

<div class="container">
    <div style="display:flex; justify-content:space-between; align-items:center; margin-bottom:1.5rem;">
        <div>
            <h1 style="font-size:1.5rem;">Seller Dashboard</h1>
            <p style="color:var(--gray-600); font-size:0.95rem;">Manage your product listings</p>
        </div>
        <a href="${pageContext.request.contextPath}/seller/add" class="btn btn-primary">+ Add Product</a>
    </div>

    <div class="card">
        <c:choose>
            <c:when test="${empty products}">
                <div class="empty-state">
                    <p style="font-size:1.1rem; margin-bottom:0.5rem;">No products yet</p>
                    <p>Start by adding your first product listing.</p>
                    <a href="${pageContext.request.contextPath}/seller/add" class="btn btn-primary" style="margin-top:1rem;">Add Product</a>
                </div>
            </c:when>
            <c:otherwise>
                <table class="product-table">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Category</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${products}">
                            <tr>
                                <td>
                                    <strong>${p.name}</strong>
                                    <c:if test="${not empty p.description}">
                                        <br><small style="color:var(--gray-600);">${p.description.length() > 60 ? p.description.substring(0,60).concat('...') : p.description}</small>
                                    </c:if>
                                </td>
                                <td>${empty p.category ? '—' : p.category}</td>
                                <td>₹<fmt:formatNumber value="${p.price}" minFractionDigits="2"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${p.stockQty == 0}">
                                            <span class="badge badge-low">Out of stock</span>
                                        </c:when>
                                        <c:when test="${p.stockQty < 5}">
                                            <span class="badge badge-low">${p.stockQty} left</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-ok">${p.stockQty}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td class="actions">
                                    <a href="${pageContext.request.contextPath}/seller/edit/${p.id}" class="btn btn-outline" style="padding:0.35rem 0.7rem; font-size:0.85rem;">Edit</a>
                                    <form method="post" action="${pageContext.request.contextPath}/seller" onsubmit="return confirm('Delete this product?');">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${p.id}">
                                        <button type="submit" class="btn btn-danger" style="padding:0.35rem 0.7rem; font-size:0.85rem;">Delete</button>
                                    </form>
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
