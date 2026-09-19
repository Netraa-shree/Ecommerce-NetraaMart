<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${empty product ? 'Add' : 'Edit'} Product – NetraaMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar">
    <a href="${pageContext.request.contextPath}/" class="brand">NetraaMart</a>
    <nav>
        <a href="${pageContext.request.contextPath}/seller">← Back to Dashboard</a>
        <span style="color:var(--gray-600);">Hi, ${sessionScope.user.fullName}</span>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </nav>
</nav>

<div class="container" style="max-width:640px;">
    <h1 style="font-size:1.5rem; margin-bottom:0.5rem;">
        ${empty product ? 'Add New Product' : 'Edit Product'}
    </h1>
    <p style="color:var(--gray-600); margin-bottom:1.5rem;">
        Fill in the details below. Price and stock are required.
    </p>

    <div class="card">
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>

        <form method="post" action="${pageContext.request.contextPath}/seller">
            <input type="hidden" name="action" value="save">
            <c:if test="${not empty product}">
                <input type="hidden" name="id" value="${product.id}">
            </c:if>

            <div class="form-group">
                <label for="name">Product Name *</label>
                <input type="text" id="name" name="name" required maxlength="200"
                       value="${product.name}" placeholder="e.g. Wireless Headphones">
            </div>

            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description" rows="4"
                          placeholder="Brief description of the product">${product.description}</textarea>
            </div>

            <div style="display:grid; grid-template-columns:1fr 1fr; gap:1rem;">
                <div class="form-group">
                    <label for="price">Price (₹) *</label>
                    <input type="number" id="price" name="price" required min="0" step="0.01"
                           value="${product.price}" placeholder="0.00">
                </div>
                <div class="form-group">
                    <label for="stockQty">Stock Quantity *</label>
                    <input type="number" id="stockQty" name="stockQty" required min="0"
                           value="${product.stockQty}" placeholder="0">
                </div>
            </div>

            <div class="form-group">
                <label for="category">Category</label>
                <input type="text" id="category" name="category" maxlength="100"
                       value="${product.category}" placeholder="e.g. Electronics, Clothing, Books">
            </div>

            <div class="form-group">
                <label for="imageUrl">Image URL (optional)</label>
                <input type="url" id="imageUrl" name="imageUrl" maxlength="500"
                       value="${product.imageUrl}" placeholder="https://example.com/image.jpg">
            </div>

            <div style="display:flex; gap:0.75rem; margin-top:1.5rem;">
                <button type="submit" class="btn btn-primary">
                    ${empty product ? 'Create Product' : 'Save Changes'}
                </button>
                <a href="${pageContext.request.contextPath}/seller" class="btn btn-outline">Cancel</a>
            </div>
        </form>
    </div>
</div>

<footer class="footer">NetraaMart &copy; 2026</footer>
</body>
</html>
