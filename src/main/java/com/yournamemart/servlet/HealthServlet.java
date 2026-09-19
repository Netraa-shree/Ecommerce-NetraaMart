package com.yournamemart.servlet;

import com.yournamemart.util.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

@WebServlet("/api/v1/health")
public class HealthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String dbStatus = "DOWN";
        try (Connection conn = DBUtil.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                dbStatus = "UP";
            }
        } catch (Exception e) {
            dbStatus = "DOWN";
        }

        String json = String.format(
                "{\"status\":\"%s\",\"db\":\"%s\"}",
                "UP".equals(dbStatus) ? "UP" : "DOWN",
                dbStatus
        );

        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }
}
