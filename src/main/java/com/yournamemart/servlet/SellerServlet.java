package com.yournamemart.servlet;

import com.yournamemart.dao.ProductDAO;
import com.yournamemart.model.Product;
import com.yournamemart.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@WebServlet("/seller/*")
public class SellerServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null || (!user.isSeller() && !user.isAdmin())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            // Dashboard – list seller's products
            List<Product> products = productDAO.findBySeller(user.getId());
            req.setAttribute("products", products);
            req.getRequestDispatcher("/pages/seller/dashboard.jsp").forward(req, resp);
            return;
        }

        if (path.equals("/add")) {
            req.getRequestDispatcher("/pages/seller/product-form.jsp").forward(req, resp);
            return;
        }

        if (path.startsWith("/edit/")) {
            try {
                long id = Long.parseLong(path.substring(6));
                Optional<Product> opt = productDAO.findById(id);
                if (opt.isEmpty() || opt.get().getSellerId() != user.getId()) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                req.setAttribute("product", opt.get());
                req.getRequestDispatcher("/pages/seller/product-form.jsp").forward(req, resp);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null || (!user.isSeller() && !user.isAdmin())) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        String action = req.getParameter("action");
        if (action == null) action = "save";

        switch (action) {
            case "save" -> handleSave(req, resp, user);
            case "delete" -> handleDelete(req, resp, user);
            default -> resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void handleSave(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {

        String idStr = req.getParameter("id");
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String priceStr = req.getParameter("price");
        String stockStr = req.getParameter("stockQty");
        String category = req.getParameter("category");
        String imageUrl = req.getParameter("imageUrl");

        // Validation
        if (name == null || name.isBlank() || priceStr == null || stockStr == null) {
            req.setAttribute("error", "Name, price and stock are required.");
            req.getRequestDispatcher("/pages/seller/product-form.jsp").forward(req, resp);
            return;
        }

        BigDecimal price;
        int stock;
        try {
            price = new BigDecimal(priceStr);
            stock = Integer.parseInt(stockStr);
            if (price.compareTo(BigDecimal.ZERO) < 0 || stock < 0) {
                throw new NumberFormatException("negative");
            }
        } catch (NumberFormatException e) {
            req.setAttribute("error", "Invalid price or stock quantity.");
            req.getRequestDispatcher("/pages/seller/product-form.jsp").forward(req, resp);
            return;
        }

        Product p = new Product();
        p.setSellerId(user.getId());
        p.setName(name.trim());
        p.setDescription(description);
        p.setPrice(price);
        p.setStockQty(stock);
        p.setCategory(category);
        p.setImageUrl(imageUrl);

        boolean success;
        if (idStr != null && !idStr.isBlank()) {
            // Update
            p.setId(Long.parseLong(idStr));
            success = productDAO.update(p);
        } else {
            // Create
            long newId = productDAO.create(p);
            success = newId > 0;
        }

        if (success) {
            resp.sendRedirect(req.getContextPath() + "/seller");
        } else {
            req.setAttribute("error", "Failed to save product. Please try again.");
            req.setAttribute("product", p);
            req.getRequestDispatcher("/pages/seller/product-form.jsp").forward(req, resp);
        }
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        String idStr = req.getParameter("id");
        if (idStr != null) {
            try {
                long id = Long.parseLong(idStr);
                productDAO.softDelete(id, user.getId());
            } catch (NumberFormatException ignored) {}
        }
        resp.sendRedirect(req.getContextPath() + "/seller");
    }
}
