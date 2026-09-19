package com.yournamemart.servlet;

import com.yournamemart.dao.ProductDAO;
import com.yournamemart.dao.ReviewDAO;
import com.yournamemart.model.Product;
import com.yournamemart.model.Review;
import com.yournamemart.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {"/products", "/product"})
public class ProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        if ("/product".equals(path)) {
            String idStr = req.getParameter("id");
            if (idStr == null || idStr.isBlank()) {
                resp.sendRedirect(req.getContextPath() + "/products");
                return;
            }
            try {
                long id = Long.parseLong(idStr);
                Optional<Product> opt = productDAO.findById(id);
                if (opt.isEmpty()) {
                    resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found");
                    return;
                }
                Product product = opt.get();
                product.setAvgRating(reviewDAO.getAverageRating(id));
                product.setReviewCount(reviewDAO.getReviewCount(id));

                List<Review> reviews = reviewDAO.findByProduct(id);
                req.setAttribute("product", product);
                req.setAttribute("reviews", reviews);

                User user = (User) req.getSession().getAttribute("user");
                if (user != null) {
                    reviewDAO.findByUserAndProduct(user.getId(), id)
                            .ifPresent(r -> req.setAttribute("myReview", r));
                }

                if ("1".equals(req.getParameter("reviewed"))) {
                    req.setAttribute("successMessage", "Your review has been saved.");
                }

                req.getRequestDispatcher("/pages/product-detail.jsp").forward(req, resp);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
            return;
        }

        // /products list
        String keyword = req.getParameter("q");
        String category = req.getParameter("category");

        List<Product> products;
        if ((keyword != null && !keyword.isBlank()) || (category != null && !category.isBlank())) {
            products = productDAO.search(keyword, category);
        } else {
            products = productDAO.findAllActive();
        }

        // Attach ratings for list display
        for (Product p : products) {
            p.setAvgRating(reviewDAO.getAverageRating(p.getId()));
            p.setReviewCount(reviewDAO.getReviewCount(p.getId()));
        }

        req.setAttribute("products", products);
        req.setAttribute("keyword", keyword != null ? keyword : "");
        req.setAttribute("category", category != null ? category : "");
        req.getRequestDispatcher("/pages/products.jsp").forward(req, resp);
    }
}
