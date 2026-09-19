<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Browse Products – NetraaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .search-bar {
            display: flex; gap: 0.75rem; flex-wrap: wrap; margin-bottom: 1.5rem;
        }
        .search-bar input, .search-bar select {
            padding: 0.6rem 0.75rem; border: 1px solid var(--gray-200);
            border-radius: var(--radius); font-size: 1rem;
        }
        .search-bar input { flex: 1; min-width: 180px; }
        .product-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
            gap: 1.25rem;
        }
        .product-card {
            background: white; border: 1px solid var(--gray-200);
            border-radius: var(--radius); overflow: hidden;
            transition: box-shadow 0.15s;
        }
        .product-card:hover { box-shadow: 0 4px 12px rgb(0 0 0 / 0.08); }
        .product-card .img-placeholder {
            height: 140px; background: var(--gray-100);
            display: flex; align-items: center; justify-content: center;
            color: var(--gray-600); font-size: 0.9rem;
        }
        .product-card .img-placeholder img {
            width: 100%; height: 100%; object-fit: cover;
        }
        .product-card .body { padding: 1rem; }
        .product-card .name {
            font-weight: 600; margin-bottom: 0.25rem;
            display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical;
            overflow: hidden;
        }
        .product-card .price { font-size: 1.15rem; font-weight: 700; color: var(--primary); }
        .product-card .meta { font-size: 0.8rem; color: var(--gray-600); margin-top: 0.35rem; }
        .empty { text-align: center; padding: 3rem; color: var(--gray-600); }
    </style>
</head>
<body>
<nav class="navbar">
    <a href="${pageContext.request.contextPath}/" class="brand">NetraaMart</a>
    <nav>
        <a href="${pageContext.request.contextPath}/products">Browse</a>
        <c:if test="${not empty sessionScope.user}">
            <c:if test="${sessionScope.user.seller || sessionScope.user.admin}">
                <a href="${pageContext.request.contextPath}/seller">Seller Dashboard</a>
            </c:if>
            <a href="${pageContext.request.contextPath}/cart">Cart</a>
            <span style="color:var(--gray-600);">Hi, ${sessionScope.user.fullName}</span>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </c:if>
        <c:if test="${empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/login">Login</a>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-primary" style="padding:0.4rem 0.9rem;">Register</a>
        </c:if>
    </nav>
</nav>

<div class="container">
    <h1 style="font-size:1.5rem; margin-bottom:1rem;">Browse Products</h1>

    <form method="get" action="${pageContext.request.contextPath}/products" class="search-bar">
        <input type="text" name="q" value="${keyword}" placeholder="Search by name or description...">
        <input type="text" name="category" value="${category}" placeholder="Category (e.g. Electronics)" style="max-width:180px;">
        <button type="submit" class="btn btn-primary">Search</button>
        <c:if test="${not empty keyword or not empty category}">
            <a href="${pageContext.request.contextPath}/products" class="btn btn-outline">Clear</a>
        </c:if>
    </form>

    <c:choose>
        <c:when test="${empty products}">
            <div class="empty">
                <p style="font-size:1.1rem;">No products found</p>
                <p>Try a different search or check back later.</p>
            </div>
        </c:when>
        <c:otherwise>
            <p style="color:var(--gray-600); margin-bottom:1rem; font-size:0.9rem;">
                ${products.size()} product${products.size() == 1 ? '' : 's'} found
            </p>
            <div class="product-grid">
                <c:forEach var="p" items="${products}">
                    <a href="${pageContext.request.contextPath}/product?id=${p.id}" class="product-card" style="text-decoration:none; color:inherit;">
                        <div class="img-placeholder">
                            <c:choose>
                                <c:when test="${not empty p.imageUrl}">
                                    <img src="${p.imageUrl}" alt="${p.name}" onerror="this.parentElement.textContent='No image'">
                                </c:when>
                                <c:otherwise>No image</c:otherwise>
                            </c:choose>
                        </div>
                        <div class="body">
                            <div class="name">${p.name}</div>
                            <div class="price">₹<fmt:formatNumber value="${p.price}" minFractionDigits="2"/></div>
                            <div class="meta">
                                ${empty p.category ? 'Uncategorized' : p.category}
                                · by ${p.sellerName}
                                <c:if test="${p.stockQty == 0}"> · <span style="color:var(--danger);">Out of stock</span></c:if>
                            </div>
                        </div>
                    </a>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<footer class="footer">NetraaMart &copy; 2026</footer>
</body>
</html>
