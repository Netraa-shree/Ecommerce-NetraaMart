<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${product.name} – NetraaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .detail-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 2rem; }
        @media (max-width: 700px) { .detail-grid { grid-template-columns: 1fr; } }
        .detail-img {
            background: var(--gray-100); border-radius: var(--radius);
            height: 320px; display: flex; align-items: center; justify-content: center; overflow: hidden;
        }
        .detail-img img { max-width: 100%; max-height: 100%; object-fit: contain; }
        .price-lg { font-size: 1.75rem; font-weight: 700; color: var(--primary); margin: 0.75rem 0; }
        .stock-ok { color: var(--success); font-weight: 600; }
        .stock-out { color: var(--danger); font-weight: 600; }
        .stars { color: #f59e0b; letter-spacing: 1px; }
        .review-card {
            border: 1px solid var(--gray-200); border-radius: var(--radius);
            padding: 1rem; margin-bottom: 0.75rem;
        }
        .review-card .who { font-weight: 600; }
        .review-card .when { font-size: 0.8rem; color: var(--gray-600); }
    </style>
</head>
<body>
<nav class="navbar">
    <a href="${pageContext.request.contextPath}/" class="brand">NetraaMart</a>
    <nav>
        <a href="${pageContext.request.contextPath}/products">Browse</a>
        <c:if test="${not empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/cart">Cart</a>
            <span style="color:var(--gray-600);">Hi, ${sessionScope.user.fullName}</span>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </c:if>
        <c:if test="${empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/login">Login</a>
        </c:if>
    </nav>
</nav>

<div class="container">
    <p style="margin-bottom:1rem;">
        <a href="${pageContext.request.contextPath}/products">← Back to products</a>
    </p>

    <c:if test="${not empty successMessage}">
        <div class="alert alert-success">${successMessage}</div>
    </c:if>

    <div class="card" style="margin-bottom:1.5rem;">
        <div class="detail-grid">
            <div class="detail-img">
                <c:choose>
                    <c:when test="${not empty product.imageUrl}">
                        <img src="${product.imageUrl}" alt="${product.name}"
                             onerror="this.parentElement.innerHTML='No image available'">
                    </c:when>
                    <c:otherwise>No image available</c:otherwise>
                </c:choose>
            </div>
            <div>
                <h1 style="font-size:1.5rem;">${product.name}</h1>
                <p style="color:var(--gray-600); margin-top:0.25rem;">
                    Sold by <strong>${product.sellerName}</strong>
                    <c:if test="${not empty product.category}"> · ${product.category}</c:if>
                </p>

                <c:if test="${product.reviewCount > 0}">
                    <p class="stars" style="margin-top:0.5rem;">
                        <c:forEach begin="1" end="5" var="s">
                            <c:choose>
                                <c:when test="${s <= product.avgRating}">★</c:when>
                                <c:otherwise>☆</c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <span style="color:var(--gray-600); font-size:0.9rem;">
                            <fmt:formatNumber value="${product.avgRating}" maxFractionDigits="1"/>
                            (${product.reviewCount} review${product.reviewCount == 1 ? '' : 's'})
                        </span>
                    </p>
                </c:if>

                <div class="price-lg">₹<fmt:formatNumber value="${product.price}" minFractionDigits="2"/></div>

                <c:choose>
                    <c:when test="${product.stockQty > 0}">
                        <p class="stock-ok">${product.stockQty} in stock</p>
                    </c:when>
                    <c:otherwise>
                        <p class="stock-out">Out of stock</p>
                    </c:otherwise>
                </c:choose>

                <c:if test="${not empty product.description}">
                    <div style="margin:1.25rem 0; line-height:1.6;">${product.description}</div>
                </c:if>

                <c:choose>
                    <c:when test="${empty sessionScope.user}">
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary">Login to add to cart</a>
                    </c:when>
                    <c:when test="${product.stockQty <= 0}">
                        <button class="btn btn-outline" disabled>Out of stock</button>
                    </c:when>
                    <c:otherwise>
                        <form method="post" action="${pageContext.request.contextPath}/cart" style="display:flex; gap:0.75rem; align-items:center;">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productId" value="${product.id}">
                            <label>
                                Qty
                                <input type="number" name="quantity" value="1" min="1" max="${product.stockQty}"
                                       style="width:70px; margin-left:0.35rem; padding:0.4rem;">
                            </label>
                            <button type="submit" class="btn btn-primary">Add to Cart</button>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <div class="card">
        <h2 style="font-size:1.15rem; margin-bottom:1rem;">Reviews</h2>

        <c:if test="${not empty sessionScope.user}">
            <form method="post" action="${pageContext.request.contextPath}/review" style="margin-bottom:1.5rem; padding:1rem; background:var(--gray-50); border-radius:var(--radius);">
                <input type="hidden" name="productId" value="${product.id}">
                <p style="font-weight:600; margin-bottom:0.5rem;">
                    ${empty myReview ? 'Write a review' : 'Update your review'}
                </p>
                <div class="form-group">
                    <label>Rating</label>
                    <select name="rating" required style="max-width:120px;">
                        <option value="5" ${myReview.rating == 5 ? 'selected' : ''}>5 ★</option>
                        <option value="4" ${myReview.rating == 4 ? 'selected' : ''}>4 ★</option>
                        <option value="3" ${myReview.rating == 3 ? 'selected' : ''}>3 ★</option>
                        <option value="2" ${myReview.rating == 2 ? 'selected' : ''}>2 ★</option>
                        <option value="1" ${myReview.rating == 1 ? 'selected' : ''}>1 ★</option>
                    </select>
                </div>
                <div class="form-group">
                    <label>Comment (optional)</label>
                    <textarea name="comment" rows="3" placeholder="Share your experience...">${myReview.comment}</textarea>
                </div>
                <button type="submit" class="btn btn-primary">Submit Review</button>
            </form>
        </c:if>
        <c:if test="${empty sessionScope.user}">
            <p style="margin-bottom:1rem; color:var(--gray-600);">
                <a href="${pageContext.request.contextPath}/login">Login</a> to leave a review.
            </p>
        </c:if>

        <c:choose>
            <c:when test="${empty reviews}">
                <p style="color:var(--gray-600);">No reviews yet. Be the first!</p>
            </c:when>
            <c:otherwise>
                <c:forEach var="r" items="${reviews}">
                    <div class="review-card">
                        <div class="who">${r.userName}
                            <span class="stars">
                                <c:forEach begin="1" end="5" var="s">
                                    ${s <= r.rating ? '★' : '☆'}
                                </c:forEach>
                            </span>
                        </div>
                        <div class="when"><fmt:formatDate value="${r.createdAt}" pattern="dd MMM yyyy"/></div>
                        <c:if test="${not empty r.comment}">
                            <p style="margin-top:0.5rem;">${r.comment}</p>
                        </c:if>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<footer class="footer">NetraaMart &copy; 2026</footer>
</body>
</html>
