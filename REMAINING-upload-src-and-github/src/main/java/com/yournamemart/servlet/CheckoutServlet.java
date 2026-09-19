package com.yournamemart.servlet;

import com.yournamemart.dao.CartDAO;
import com.yournamemart.dao.OrderDAO;
import com.yournamemart.model.CartItem;
import com.yournamemart.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {

    private final CartDAO cartDAO = new CartDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        List<CartItem> items = cartDAO.findByUser(user.getId());
        if (items.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }

        BigDecimal total = cartDAO.getCartTotal(user.getId());
        req.setAttribute("cartItems", items);
        req.setAttribute("cartTotal", total);
        req.getRequestDispatcher("/pages/checkout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Mock payment confirmation
        String confirm = req.getParameter("confirmPayment");
        if (!"yes".equals(confirm)) {
            req.setAttribute("error", "Please confirm the mock payment to place the order.");
            doGet(req, resp);
            return;
        }

        long orderId = orderDAO.placeOrder(user.getId());
        if (orderId > 0) {
            resp.sendRedirect(req.getContextPath() + "/orders?success=" + orderId);
        } else {
            req.setAttribute("error", "Could not place order. Check stock availability and try again.");
            doGet(req, resp);
        }
    }
}
