package com.yournamemart.dao;

import com.yournamemart.model.Product;
import com.yournamemart.util.DBUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO {

    public long create(Product p) {
        String sql = """
            INSERT INTO products (seller_id, name, description, price, stock_qty, category, image_url)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, p.getSellerId());
            ps.setString(2, p.getName().trim());
            ps.setString(3, p.getDescription() != null ? p.getDescription().trim() : null);
            ps.setBigDecimal(4, p.getPrice());
            ps.setInt(5, p.getStockQty());
            ps.setString(6, p.getCategory() != null ? p.getCategory().trim() : null);
            ps.setString(7, p.getImageUrl() != null ? p.getImageUrl().trim() : null);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getLong(1);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean update(Product p) {
        String sql = """
            UPDATE products
            SET name = ?, description = ?, price = ?, stock_qty = ?,
                category = ?, image_url = ?, updated_at = CURRENT_TIMESTAMP
            WHERE id = ? AND seller_id = ? AND active = TRUE
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, p.getName().trim());
            ps.setString(2, p.getDescription() != null ? p.getDescription().trim() : null);
            ps.setBigDecimal(3, p.getPrice());
            ps.setInt(4, p.getStockQty());
            ps.setString(5, p.getCategory() != null ? p.getCategory().trim() : null);
            ps.setString(6, p.getImageUrl() != null ? p.getImageUrl().trim() : null);
            ps.setLong(7, p.getId());
            ps.setLong(8, p.getSellerId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean softDelete(long productId, long sellerId) {
        String sql = "UPDATE products SET active = FALSE, updated_at = CURRENT_TIMESTAMP WHERE id = ? AND seller_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, productId);
            ps.setLong(2, sellerId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Optional<Product> findById(long id) {
        String sql = """
            SELECT p.*, u.full_name AS seller_name
            FROM products p
            JOIN users u ON p.seller_id = u.id
            WHERE p.id = ? AND p.active = TRUE
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Product> findBySeller(long sellerId) {
        List<Product> list = new ArrayList<>();
        String sql = """
            SELECT p.*, u.full_name AS seller_name
            FROM products p
            JOIN users u ON p.seller_id = u.id
            WHERE p.seller_id = ? AND p.active = TRUE
            ORDER BY p.updated_at DESC
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
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

    public List<Product> findAllActive() {
        List<Product> list = new ArrayList<>();
        String sql = """
            SELECT p.*, u.full_name AS seller_name
            FROM products p
            JOIN users u ON p.seller_id = u.id
            WHERE p.active = TRUE
            ORDER BY p.created_at DESC
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> search(String keyword, String category) {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("""
            SELECT p.*, u.full_name AS seller_name
            FROM products p
            JOIN users u ON p.seller_id = u.id
            WHERE p.active = TRUE
            """);
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?)");
            String like = "%" + keyword.trim().toLowerCase() + "%";
            params.add(like);
            params.add(like);
        }
        if (category != null && !category.isBlank()) {
            sql.append(" AND LOWER(p.category) = ?");
            params.add(category.trim().toLowerCase());
        }
        sql.append(" ORDER BY p.created_at DESC");

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
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

    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setSellerId(rs.getLong("seller_id"));
        p.setSellerName(rs.getString("seller_name"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStockQty(rs.getInt("stock_qty"));
        p.setCategory(rs.getString("category"));
        p.setImageUrl(rs.getString("image_url"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        p.setActive(rs.getBoolean("active"));
        return p;
    }
}
