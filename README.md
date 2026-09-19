# NetraaMart

E-commerce Marketplace (Buyer / Seller / Admin) built with Java Servlet + JSP + H2.

**Checkpoint target:** Full Build + Deploy – **21 September 2026**

## Tech Stack

- Java 17
- Maven (WAR packaging)
- Jakarta Servlet 6 + JSP + JSTL
- H2 Database (file-based, AUTO_SERVER mode)
- BCrypt password hashing
- Session-based authentication + AuthFilter

## Features Status (F1–F8)

| ID | Feature | Status |
|----|---------|--------|
| F1 | Register / Login (Buyer + Seller, seeded Admin) | Done |
| F2 | Seller product CRUD | Done |
| F3 | Buyer browse / search / filter | Done |
| F4 | Cart | Done |
| F5 | Checkout (mock payment) | Done |
| F6 | Order history | Done |
| F7 | Admin panel | Done |
| F8 | Reviews & ratings | Done |

## Quick Start (Local)

### Prerequisites
- JDK 17+
- Maven 3.8+
- Tomcat 9 or 10

### Build
```bash
mvn clean package
# Deploy target/NetraaMart.war to Tomcat
```

### Default Admin
- Email: admin@netraamart.com
- Password: admin123

### Health Endpoint
GET /api/v1/health → {"status":"UP","db":"UP"}

## Security Checklist

- [x] Passwords hashed with BCrypt
- [x] PreparedStatements for all queries
- [x] Session regenerated on login
- [x] AuthFilter protecting private routes
- [x] Role checks (BUYER / SELLER / ADMIN)
- [x] Soft-delete for products
- [x] Input validation on forms

## Demo User Journeys

1. Register as Seller → Add products → Seller dashboard
2. Register as Buyer → Browse → Cart → Checkout (mock) → Orders
3. Login as Admin → Moderate users / products / orders
4. Leave a review on a product

## Deployment (Sep 21)

1. mvn clean package
2. Deploy NetraaMart.war to Tomcat
3. Verify live URL + /api/v1/health
4. Record 2-3 min backup demo video
5. Tag v1.0.0

---
NetraaMart – Full Build ready for review.
