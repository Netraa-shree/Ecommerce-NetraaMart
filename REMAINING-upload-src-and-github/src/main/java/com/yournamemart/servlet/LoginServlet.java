package com.yournamemart.servlet;

import com.yournamemart.dao.UserDAO;
import com.yournamemart.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // If already logged in, go home
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.getRequestDispatcher("/pages/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (email == null || password == null || email.isBlank() || password.isBlank()) {
            req.setAttribute("error", "Email and password are required.");
            req.getRequestDispatcher("/pages/login.jsp").forward(req, resp);
            return;
        }

        Optional<User> opt = userDAO.findByEmail(email.trim().toLowerCase());
        if (opt.isEmpty() || !userDAO.verifyPassword(password, opt.get().getPasswordHash())) {
            req.setAttribute("error", "Invalid email or password.");
            req.getRequestDispatcher("/pages/login.jsp").forward(req, resp);
            return;
        }

        User user = opt.get();
        // Prevent session fixation
        HttpSession oldSession = req.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }
        HttpSession session = req.getSession(true);
        session.setAttribute("user", user);
        session.setMaxInactiveInterval(30 * 60); // 30 minutes

        // Redirect based on role
        String redirect = switch (user.getRole()) {
            case "ADMIN" -> "/admin";
            case "SELLER" -> "/seller";
            default -> "/";
        };
        resp.sendRedirect(req.getContextPath() + redirect);
    }
}
