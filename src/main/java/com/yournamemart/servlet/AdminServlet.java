package com.yournamemart.servlet;

import com.yournamemart.dao.OrderDAO;
import com.yournamemart.dao.ProductDAO;
import com.yournamemart.dao.UserDAO;
import com.yournamemart.model.Order;
import com.yournamemart.model.Product;
import com.yournamemart.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.yournamemart.util.DBUtil;

@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();
    private final ProductDAO productDAO = new ProductDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !user.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin access required");
            return;
        }

        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            // Dashboard overview
            List<User> users = userDAO.findAll();
            List<Product> products = productDAO.findAllActive();
            List<Order> orders = orderDAO.findAll();
            req.setAttribute("users", users);
            req.setAttribute("products", products);
            req.setAttribute("orders", orders);
            req.setAttribute("userCount", users.size());
            req.setAttribute("productCount", products.size());
            req.setAttribute("orderCount", orders.size());
            req.getRequestDispatcher("/pages/admin/dashboard.jsp").forward(req, resp);
            return;
        }

        if (path.equals("/users")) {
            req.setAttribute("users", userDAO.findAll());
            req.getRequestDispatcher("/pages/admin/users.jsp").forward(req, resp);
            return;
        }

        if (path.equals("/products")) {
            req.setAttribute("products", productDAO.findAllActive());
            req.getRequestDispatcher("/pages/admin/products.jsp").forward(req, resp);
            return;
        }

        if (path.equals("/orders")) {
            req.setAttribute("orders", orderDAO.findAll());
            req.getRequestDispatcher("/pages/admin/orders.jsp").forward(req, resp);
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null || !user.isAdmin()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = req.getParameter("action");
        if ("deactivateProduct".equals(action)) {
            try {
                long productId = Long.parseLong(req.getParameter("productId"));
                softDeleteProductAsAdmin(productId);
            } catch (NumberFormatException ignored) {}
            resp.sendRedirect(req.getContextPath() + "/admin/products");
            return;
        }

        if ("toggleUser".equals(action)) {
            try {
                long userId = Long.parseLong(req.getParameter("userId"));
                boolean active = "true".equals(req.getParameter("active"));
                setUserActive(userId, active);
            } catch (NumberFormatException ignored) {}
            resp.sendRedirect(req.getContextPath() + "/admin/users");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/admin");
    }

    private void softDeleteProductAsAdmin(long productId) {
        String sql = "UPDATE products SET active = FALSE, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setUserActive(long userId, boolean active) {
        // Never deactivate the main admin (id=1)
        if (userId == 1) return;
        String sql = "UPDATE users SET active = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, active);
            ps.setLong(2, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
