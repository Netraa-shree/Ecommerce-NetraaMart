package com.yournamemart.filter;

import com.yournamemart.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Set;

/**
 * Protects routes that require login.
 * Public: /, /login, /register, /products, /product, /css, /js, /api/health
 */
@WebFilter("/*")
public class AuthFilter implements Filter {

    private static final Set<String> PUBLIC_PREFIXES = Set.of(
            "/login", "/register", "/css/", "/js/", "/images/",
            "/api/health", "/favicon"
    );

    private static final Set<String> PUBLIC_EXACT = Set.of(
            "/", "/index.jsp", "/products", "/product"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Allow public paths
        if (isPublic(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            // Not logged in → redirect to login
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Role-based checks for specific areas
        if (path.startsWith("/seller") && !user.isSeller() && !user.isAdmin()) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Seller access required");
            return;
        }
        if (path.startsWith("/admin") && !user.isAdmin()) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin access required");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isPublic(String path) {
        if (PUBLIC_EXACT.contains(path)) return true;
        for (String prefix : PUBLIC_PREFIXES) {
            if (path.startsWith(prefix)) return true;
        }
        // Allow product details: /product?id=...
        if (path.startsWith("/product")) return true;
        return false;
    }
}
