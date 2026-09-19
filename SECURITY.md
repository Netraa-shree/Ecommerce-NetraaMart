# Security Notes – NetraaMart

## Implemented

| Item | Detail |
|------|--------|
| Password hashing | BCrypt (cost factor 10), never store plain text |
| SQL injection | All queries use PreparedStatement |
| Session fixation | Session invalidated and recreated on login |
| Authorization | AuthFilter + role checks (BUYER / SELLER / ADMIN) |
| XSS mitigation | Prefer JSTL `c:out`; HtmlUtil.escape available |
| Product deletion | Soft-delete (active flag) instead of hard delete |
| Admin protection | Seeded admin (id=1) cannot be deactivated |

## Checklist for Full Build review

- [x] No credentials in source code
- [x] No MD5/SHA1 for passwords
- [x] AuthFilter on protected routes
- [x] Input validation on registration and product forms
- [x] Stock checked before order placement
- [ ] HTTPS (enable on production server)
- [ ] CSRF tokens (optional enhancement)

## Demo accounts

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@netraamart.com | admin123 |
| Seller | seller@netraamart.com | admin123 |

Change these passwords before any public deployment.
