<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Products – Admin – NetraaMart</title>
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
    <h1 style="font-size:1.5rem; margin-bottom:1rem;">Moderate Products</h1>
    <div class="admin-nav">
        <a href="${pageContext.request.contextPath}/admin">Overview</a>
        <a href="${pageContext.request.contextPath}/admin/users">Users</a>
        <a href="${pageContext.request.contextPath}/admin/products" class="active">Products</a>
        <a href="${pageContext.request.contextPath}/admin/orders">Orders</a>
    </div>

    <div class="card">
        <c:choose>
            <c:when test="${empty products}">
                <p style="color:var(--gray-600); padding:1rem;">No active products.</p>
            </c:when>
            <c:otherwise>
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Seller</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Category</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${products}">
                            <tr>
                                <td>${p.id}</td>
                                <td>${p.name}</td>
                                <td>${p.sellerName}</td>
                                <td>₹<fmt:formatNumber value="${p.price}" minFractionDigits="2"/></td>
                                <td>${p.stockQty}</td>
                                <td>${empty p.category ? '—' : p.category}</td>
                                <td>
                                    <form method="post" action="${pageContext.request.contextPath}/admin"
                                          onsubmit="return confirm('Deactivate this product?');">
                                        <input type="hidden" name="action" value="deactivateProduct">
                                        <input type="hidden" name="productId" value="${p.id}">
                                        <button type="submit" class="btn btn-danger" style="padding:0.25rem 0.6rem; font-size:0.8rem;">
                                            Deactivate
                                        </button>
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
