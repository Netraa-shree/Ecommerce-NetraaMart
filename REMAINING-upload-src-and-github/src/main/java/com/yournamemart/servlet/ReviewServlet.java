package com.yournamemart.servlet;

import com.yournamemart.dao.ReviewDAO;
import com.yournamemart.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/review")
public class ReviewServlet extends HttpServlet {

    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String productIdStr = req.getParameter("productId");
        String ratingStr = req.getParameter("rating");
        String comment = req.getParameter("comment");

        if (productIdStr == null || ratingStr == null) {
            resp.sendRedirect(req.getContextPath() + "/products");
            return;
        }

        try {
            long productId = Long.parseLong(productIdStr);
            int rating = Integer.parseInt(ratingStr);
            if (rating < 1 || rating > 5) {
                resp.sendRedirect(req.getContextPath() + "/product?id=" + productId + "&error=rating");
                return;
            }
            reviewDAO.addOrUpdate(productId, user.getId(), rating, comment);
            resp.sendRedirect(req.getContextPath() + "/product?id=" + productId + "&reviewed=1");
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/products");
        }
    }
}
