package com.yournamemart.dao;

import com.yournamemart.model.Review;
import com.yournamemart.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReviewDAO {

    public boolean addOrUpdate(long productId, long userId, int rating, String comment) {
        if (rating < 1 || rating > 5) return false;

        // Upsert: one review per user per product
        String checkSql = "SELECT id FROM reviews WHERE product_id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection()) {
            Long existingId = null;
            try (PreparedStatement ps = conn.prepareStatement(checkSql)) {
                ps.setLong(1, productId);
                ps.setLong(2, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) existingId = rs.getLong("id");
                }
            }

            if (existingId != null) {
                String updateSql = "UPDATE reviews SET rating = ?, comment = ?, created_at = CURRENT_TIMESTAMP WHERE id = ?";
                try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                    ps.setInt(1, rating);
                    ps.setString(2, comment != null ? comment.trim() : null);
                    ps.setLong(3, existingId);
                    return ps.executeUpdate() > 0;
                }
            } else {
                String insertSql = "INSERT INTO reviews (product_id, user_id, rating, comment) VALUES (?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                    ps.setLong(1, productId);
                    ps.setLong(2, userId);
                    ps.setInt(3, rating);
                    ps.setString(4, comment != null ? comment.trim() : null);
                    return ps.executeUpdate() > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Review> findByProduct(long productId) {
        List<Review> list = new ArrayList<>();
        String sql = """
            SELECT r.*, u.full_name AS user_name
            FROM reviews r
            JOIN users u ON r.user_id = u.id
            WHERE r.product_id = ?
            ORDER BY r.created_at DESC
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Optional<Review> findByUserAndProduct(long userId, long productId) {
        String sql = """
            SELECT r.*, u.full_name AS user_name
            FROM reviews r
            JOIN users u ON r.user_id = u.id
            WHERE r.user_id = ? AND r.product_id = ?
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public double getAverageRating(long productId) {
        String sql = "SELECT COALESCE(AVG(rating), 0) AS avg_rating FROM reviews WHERE product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getDouble("avg_rating");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getReviewCount(long productId) {
        String sql = "SELECT COUNT(*) AS cnt FROM reviews WHERE product_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("cnt");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Review mapRow(ResultSet rs) throws SQLException {
        Review r = new Review();
        r.setId(rs.getLong("id"));
        r.setProductId(rs.getLong("product_id"));
        r.setUserId(rs.getLong("user_id"));
        r.setUserName(rs.getString("user_name"));
        r.setRating(rs.getInt("rating"));
        r.setComment(rs.getString("comment"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        return r;
    }
}
