package com.yournamemart.servlet;

import com.yournamemart.dao.UserDAO;
import com.yournamemart.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.getRequestDispatcher("/pages/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String fullName = req.getParameter("fullName");
        String role = req.getParameter("role");

        // Basic validation
        if (email == null || password == null || fullName == null || role == null
                || email.isBlank() || password.isBlank() || fullName.isBlank()) {
            req.setAttribute("error", "All fields are required.");
            req.getRequestDispatcher("/pages/register.jsp").forward(req, resp);
            return;
        }

        email = email.trim().toLowerCase();
        fullName = fullName.trim();

        if (!"BUYER".equals(role) && !"SELLER".equals(role)) {
            req.setAttribute("error", "Invalid role selected.");
            req.getRequestDispatcher("/pages/register.jsp").forward(req, resp);
            return;
        }

        if (password.length() < 6) {
            req.setAttribute("error", "Password must be at least 6 characters.");
            req.getRequestDispatcher("/pages/register.jsp").forward(req, resp);
            return;
        }

        if (userDAO.emailExists(email)) {
            req.setAttribute("error", "Email is already registered.");
            req.getRequestDispatcher("/pages/register.jsp").forward(req, resp);
            return;
        }

        User user = new User(email, null, fullName, role);
        long id = userDAO.register(user, password);

        if (id > 0) {
            // Auto-login after registration
            user.setId(id);
            HttpSession session = req.getSession(true);
            session.setAttribute("user", user);
            session.setMaxInactiveInterval(30 * 60);

            String redirect = "SELLER".equals(role) ? "/seller" : "/";
            resp.sendRedirect(req.getContextPath() + redirect);
        } else {
            req.setAttribute("error", "Registration failed. Please try again.");
            req.getRequestDispatcher("/pages/register.jsp").forward(req, resp);
        }
    }
}
