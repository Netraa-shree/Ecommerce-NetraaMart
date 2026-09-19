package com.yournamemart.servlet;

import com.yournamemart.chat.ChatProvider;
import com.yournamemart.chat.MockChatProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/chat")
public class ChatServlet extends HttpServlet {

    private final ChatProvider provider = new MockChatProvider();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/pages/chat.jsp").forward(req, resp);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String message = req.getParameter("message");
        String reply = provider.reply(message);

        HttpSession session = req.getSession(true);
        List<Map<String, String>> history =
                (List<Map<String, String>>) session.getAttribute("chatHistory");
        if (history == null) {
            history = new ArrayList<>();
            session.setAttribute("chatHistory", history);
        }
        history.add(Map.of("role", "user", "text", message != null ? message : ""));
        history.add(Map.of("role", "bot", "text", reply));
        while (history.size() > 20) {
            history.remove(0);
        }

        String accept = req.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            try (PrintWriter out = resp.getWriter()) {
                out.print("{\"reply\":\"" + escapeJson(reply) + "\"}");
            }
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/chat");
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
