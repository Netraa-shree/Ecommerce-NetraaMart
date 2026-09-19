package com.netraamart.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Database utility - H2 embedded for development.
 * For production/deployment use server mode: jdbc:h2:tcp://localhost/~/netraamart
 */
public class DBUtil {

    // Embedded mode (file-based, persists across restarts)
    private static final String JDBC_URL = "jdbc:h2:./data/netraamart;AUTO_SERVER=TRUE;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("org.h2.Driver");
            initDatabase();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("H2 Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    private static void initDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Users table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    email VARCHAR(255) NOT NULL UNIQUE,
                    password_hash VARCHAR(255) NOT NULL,
                    full_name VARCHAR(150) NOT NULL,
                    role VARCHAR(20) NOT NULL CHECK (role IN ('BUYER', 'SELLER', 'ADMIN')),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    active BOOLEAN DEFAULT TRUE
                )
                """);

            // Products table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS products (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    seller_id BIGINT NOT NULL,
                    name VARCHAR(200) NOT NULL,
                    description CLOB,
                    price DECIMAL(12,2) NOT NULL,
                    stock_qty INT NOT NULL DEFAULT 0,
                    category VARCHAR(100),
                    image_url VARCHAR(500),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    active BOOLEAN DEFAULT TRUE,
                    FOREIGN KEY (seller_id) REFERENCES users(id)
                )
                """);

            // Cart items (session-based but persisted for simplicity)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS cart_items (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    user_id BIGINT NOT NULL,
                    product_id BIGINT NOT NULL,
                    quantity INT NOT NULL DEFAULT 1,
                    added_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (user_id) REFERENCES users(id),
                    FOREIGN KEY (product_id) REFERENCES products(id),
                    UNIQUE(user_id, product_id)
                )
                """);

            // Orders
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS orders (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    buyer_id BIGINT NOT NULL,
                    total_amount DECIMAL(12,2) NOT NULL,
                    status VARCHAR(30) NOT NULL DEFAULT 'PLACED',
                    payment_ref VARCHAR(100),
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (buyer_id) REFERENCES users(id)
                )
                """);

            // Order items
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS order_items (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    order_id BIGINT NOT NULL,
                    product_id BIGINT NOT NULL,
                    seller_id BIGINT NOT NULL,
                    quantity INT NOT NULL,
                    unit_price DECIMAL(12,2) NOT NULL,
                    FOREIGN KEY (order_id) REFERENCES orders(id),
                    FOREIGN KEY (product_id) REFERENCES products(id),
                    FOREIGN KEY (seller_id) REFERENCES users(id)
                )
                """);

            // Reviews
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS reviews (
                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                    product_id BIGINT NOT NULL,
                    user_id BIGINT NOT NULL,
                    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
                    comment CLOB,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    FOREIGN KEY (product_id) REFERENCES products(id),
                    FOREIGN KEY (user_id) REFERENCES users(id),
                    UNIQUE(product_id, user_id)
                )
                """);

            // Seed Admin if not exists
            stmt.execute("""
                MERGE INTO users (id, email, password_hash, full_name, role, active)
                KEY(email)
                VALUES (1, 'admin@netraamart.com',
                        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
                        'System Admin', 'ADMIN', TRUE)
                """);
            // Password for admin: admin123

            // Seed demo seller (password: seller123)
            stmt.execute("""
                MERGE INTO users (id, email, password_hash, full_name, role, active)
                KEY(email)
                VALUES (2, 'seller@netraamart.com',
                        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
                        'Demo Seller', 'SELLER', TRUE)
                """);

            // Seed sample products if table is empty
            var rs = stmt.executeQuery("SELECT COUNT(*) AS c FROM products");
            rs.next();
            if (rs.getInt("c") == 0) {
                stmt.execute("""
                    INSERT INTO products (seller_id, name, description, price, stock_qty, category, image_url) VALUES
                    (2, 'Wireless Bluetooth Headphones', 'Comfortable over-ear headphones with 20h battery life.', 1499.00, 25, 'Electronics', NULL),
                    (2, 'Cotton T-Shirt (Unisex)', 'Soft cotton tee available in multiple sizes.', 399.00, 50, 'Clothing', NULL),
                    (2, 'Java Programming Book', 'Beginner-friendly guide to Java and web apps.', 599.00, 15, 'Books', NULL),
                    (2, 'Stainless Steel Water Bottle', '1 litre insulated bottle keeps drinks cold 24h.', 799.00, 40, 'Home', NULL),
                    (2, 'USB-C Fast Charger 65W', 'Compact GaN charger for laptop and phone.', 1299.00, 30, 'Electronics', NULL)
                    """);
                System.out.println("[DBUtil] Seeded sample products.");
            }

            System.out.println("[DBUtil] Database initialized successfully.");

        } catch (SQLException e) {
            System.err.println("[DBUtil] Failed to initialize database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
