package com.yournamemart.dao;

import com.yournamemart.model.CartItem;
import com.yournamemart.model.Order;
import com.yournamemart.model.OrderItem;
import com.yournamemart.util.DBUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class OrderDAO {

    private final CartDAO cartDAO = new CartDAO();
    private final ProductDAO productDAO = new ProductDAO();

    /**
     * Places an order from the current cart (mock payment).
     * Returns the new order id, or -1 on failure.
     */
    public long placeOrder(long buyerId) {
        List<CartItem> cartItems = cartDAO.findByUser(buyerId);
        if (cartItems.isEmpty()) {
            return -1;
        }

        // Validate stock
        for (CartItem item : cartItems) {
            if (item.getQuantity() > item.getStockQty()) {
                return -1; // insufficient stock
            }
        }

        BigDecimal total = cartDAO.getCartTotal(buyerId);
        String paymentRef = "MOCK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // 1. Insert order
            String orderSql = "INSERT INTO orders (buyer_id, total_amount, status, payment_ref) VALUES (?, ?, 'PLACED', ?)";
            long orderId;
            try (PreparedStatement ps = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setLong(1, buyerId);
                ps.setBigDecimal(2, total);
                ps.setString(3, paymentRef);
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        conn.rollback();
                        return -1;
                    }
                    orderId = keys.getLong(1);
                }
            }

            // 2. Insert order items + reduce stock
            String itemSql = """
                INSERT INTO order_items (order_id, product_id, seller_id, quantity, unit_price)
                VALUES (?, ?, (SELECT seller_id FROM products WHERE id = ?), ?, ?)
                """;
            String stockSql = "UPDATE products SET stock_qty = stock_qty - ? WHERE id = ? AND stock_qty >= ?";

            try (PreparedStatement itemPs = conn.prepareStatement(itemSql);
                 PreparedStatement stockPs = conn.prepareStatement(stockSql)) {

                for (CartItem item : cartItems) {
                    itemPs.setLong(1, orderId);
                    itemPs.setLong(2, item.getProductId());
                    itemPs.setLong(3, item.getProductId());
                    itemPs.setInt(4, item.getQuantity());
                    itemPs.setBigDecimal(5, item.getUnitPrice());
                    itemPs.addBatch();

                    stockPs.setInt(1, item.getQuantity());
                    stockPs.setLong(2, item.getProductId());
                    stockPs.setInt(3, item.getQuantity());
                    stockPs.addBatch();
                }
                itemPs.executeBatch();
                int[] stockResults = stockPs.executeBatch();
                for (int r : stockResults) {
                    if (r == 0) {
                        conn.rollback();
                        return -1; // stock race condition
                    }
                }
            }

            // 3. Clear cart
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM cart_items WHERE user_id = ?")) {
                ps.setLong(1, buyerId);
                ps.executeUpdate();
            }

            conn.commit();
            return orderId;

        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ignored) {}
            }
            return -1;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ignored) {}
            }
        }
    }

    public Optional<Order> findById(long orderId) {
        String sql = """
            SELECT o.*, u.full_name AS buyer_name
            FROM orders o
            JOIN users u ON o.buyer_id = u.id
            WHERE o.id = ?
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(findItems(orderId));
                    return Optional.of(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Order> findByBuyer(long buyerId) {
        List<Order> list = new ArrayList<>();
        String sql = """
            SELECT o.*, u.full_name AS buyer_name
            FROM orders o
            JOIN users u ON o.buyer_id = u.id
            WHERE o.buyer_id = ?
            ORDER BY o.created_at DESC
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, buyerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(findItems(order.getId()));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Order> findIncomingForSeller(long sellerId) {
        List<Order> list = new ArrayList<>();
        String sql = """
            SELECT DISTINCT o.*, u.full_name AS buyer_name
            FROM orders o
            JOIN users u ON o.buyer_id = u.id
            JOIN order_items oi ON oi.order_id = o.id
            WHERE oi.seller_id = ?
            ORDER BY o.created_at DESC
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapOrder(rs);
                    // Only items belonging to this seller
                    order.setItems(findItemsForSeller(order.getId(), sellerId));
                    list.add(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        String sql = """
            SELECT o.*, u.full_name AS buyer_name
            FROM orders o
            JOIN users u ON o.buyer_id = u.id
            ORDER BY o.created_at DESC
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order order = mapOrder(rs);
                order.setItems(findItems(order.getId()));
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<OrderItem> findItems(long orderId) {
        List<OrderItem> list = new ArrayList<>();
        String sql = """
            SELECT oi.*, p.name AS product_name
            FROM order_items oi
            JOIN products p ON oi.product_id = p.id
            WHERE oi.order_id = ?
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapItem(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<OrderItem> findItemsForSeller(long orderId, long sellerId) {
        List<OrderItem> list = new ArrayList<>();
        String sql = """
            SELECT oi.*, p.name AS product_name
            FROM order_items oi
            JOIN products p ON oi.product_id = p.id
            WHERE oi.order_id = ? AND oi.seller_id = ?
            """;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            ps.setLong(2, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapItem(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setBuyerId(rs.getLong("buyer_id"));
        o.setBuyerName(rs.getString("buyer_name"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setStatus(rs.getString("status"));
        o.setPaymentRef(rs.getString("payment_ref"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        return o;
    }

    private OrderItem mapItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getLong("id"));
        item.setOrderId(rs.getLong("order_id"));
        item.setProductId(rs.getLong("product_id"));
        item.setSellerId(rs.getLong("seller_id"));
        item.setProductName(rs.getString("product_name"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        return item;
    }
}
