package com.yournamemart.servlet;

import com.yournamemart.dao.CartDAO;
import com.yournamemart.model.CartItem;
import com.yournamemart.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private final CartDAO cartDAO = new CartDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<CartItem> items = cartDAO.findByUser(user.getId());
        BigDecimal total = cartDAO.getCartTotal(user.getId());

        req.setAttribute("cartItems", items);
        req.setAttribute("cartTotal", total);
        req.getRequestDispatcher("/pages/cart.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) action = "view";

        switch (action) {
            case "add" -> {
                try {
                    long productId = Long.parseLong(req.getParameter("productId"));
                    int qty = 1;
                    String qtyStr = req.getParameter("quantity");
                    if (qtyStr != null && !qtyStr.isBlank()) {
                        qty = Integer.parseInt(qtyStr);
                        if (qty < 1) qty = 1;
                    }
                    cartDAO.addOrUpdate(user.getId(), productId, qty);
                } catch (NumberFormatException ignored) {}
                resp.sendRedirect(req.getContextPath() + "/cart");
            }
            case "update" -> {
                try {
                    long productId = Long.parseLong(req.getParameter("productId"));
                    int qty = Integer.parseInt(req.getParameter("quantity"));
                    cartDAO.updateQuantity(user.getId(), productId, qty);
                } catch (NumberFormatException ignored) {}
                resp.sendRedirect(req.getContextPath() + "/cart");
            }
            case "remove" -> {
                try {
                    long productId = Long.parseLong(req.getParameter("productId"));
                    cartDAO.remove(user.getId(), productId);
                } catch (NumberFormatException ignored) {}
                resp.sendRedirect(req.getContextPath() + "/cart");
            }
            default -> resp.sendRedirect(req.getContextPath() + "/cart");
        }
    }
}
