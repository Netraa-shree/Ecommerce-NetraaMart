package com.yournamemart.servlet;

import com.yournamemart.dao.OrderDAO;
import com.yournamemart.model.Order;
import com.yournamemart.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/orders")
public class OrderServlet extends HttpServlet {

    private final OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String successId = req.getParameter("success");
        if (successId != null) {
            req.setAttribute("successMessage", "Order #" + successId + " placed successfully!");
        }

        String viewId = req.getParameter("id");
        if (viewId != null && !viewId.isBlank()) {
            try {
                long id = Long.parseLong(viewId);
                Optional<Order> opt = orderDAO.findById(id);
                if (opt.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                Order order = opt.get();
                // Buyer can see own orders; Admin can see all
                if (!user.isAdmin() && order.getBuyerId() != user.getId()) {
                    // Seller might see if they have items in it - simplify: only buyer/admin for detail
                    if (!user.isSeller()) {
                        resp.sendError(HttpServletResponse.SC_FORBIDDEN);
                        return;
                    }
                }
                req.setAttribute("order", order);
                req.getRequestDispatcher("/pages/order-detail.jsp").forward(req, resp);
                return;
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
        }

        // List view
        List<Order> orders;
        if (user.isSeller() && "incoming".equals(req.getParameter("type"))) {
            orders = orderDAO.findIncomingForSeller(user.getId());
            req.setAttribute("listTitle", "Incoming Orders");
        } else {
            orders = orderDAO.findByBuyer(user.getId());
            req.setAttribute("listTitle", "My Orders");
        }

        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/pages/orders.jsp").forward(req, resp);
    }
}
