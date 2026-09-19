<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Users – Admin – NetraaMart</title>
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
        .badge { display: inline-block; padding: 0.15rem 0.5rem; border-radius: 999px; font-size: 0.75rem; font-weight: 600; }
        .badge-admin { background: #fce7f3; color: #9d174d; }
        .badge-seller { background: #dbeafe; color: #1e40af; }
        .badge-buyer { background: #dcfce7; color: #166534; }
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
    <h1 style="font-size:1.5rem; margin-bottom:1rem;">Users</h1>
    <div class="admin-nav">
        <a href="${pageContext.request.contextPath}/admin">Overview</a>
        <a href="${pageContext.request.contextPath}/admin/users" class="active">Users</a>
        <a href="${pageContext.request.contextPath}/admin/products">Products</a>
        <a href="${pageContext.request.contextPath}/admin/orders">Orders</a>
    </div>

    <div class="card">
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Joined</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="u" items="${users}">
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.fullName}</td>
                        <td>${u.email}</td>
                        <td>
                            <c:choose>
                                <c:when test="${u.role == 'ADMIN'}"><span class="badge badge-admin">ADMIN</span></c:when>
                                <c:when test="${u.role == 'SELLER'}"><span class="badge badge-seller">SELLER</span></c:when>
                                <c:otherwise><span class="badge badge-buyer">BUYER</span></c:otherwise>
                            </c:choose>
                        </td>
                        <td><fmt:formatDate value="${u.createdAt}" pattern="dd MMM yyyy"/></td>
                        <td>${u.active ? 'Active' : 'Inactive'}</td>
                        <td>
                            <c:if test="${u.id != 1}">
                                <form method="post" action="${pageContext.request.contextPath}/admin" style="display:inline;">
                                    <input type="hidden" name="action" value="toggleUser">
                                    <input type="hidden" name="userId" value="${u.id}">
                                    <input type="hidden" name="active" value="${!u.active}">
                                    <button type="submit" class="btn ${u.active ? 'btn-danger' : 'btn-primary'}"
                                            style="padding:0.25rem 0.6rem; font-size:0.8rem;">
                                        ${u.active ? 'Deactivate' : 'Activate'}
                                    </button>
                                </form>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<footer class="footer">NetraaMart &copy; 2026</footer>
</body>
</html>
