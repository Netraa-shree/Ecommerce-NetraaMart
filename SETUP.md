# Setup & Deploy – NetraaMart

## Local development

1. Install **JDK 17** and **Maven 3.8+**
2. Clone this repository
3. Build:
   ```bash
   mvn clean package
   ```
4. Deploy `target/NetraaMart.war` to **Tomcat 9 or 10**
5. Open: `http://localhost:8080/NetraaMart/`

H2 database files are created under `./data/` relative to the server working directory.

## Demo accounts

| Role   | Email                 | Password  |
|--------|-----------------------|-----------|
| Admin  | admin@netraamart.com  | admin123  |
| Seller | seller@netraamart.com | admin123  |

Register new Buyer or Seller accounts from the Register page.

## Health check

```
GET /NetraaMart/api/v1/health
```

Expected response:
```json
{"status":"UP","db":"UP"}
```

## Full Build + Deploy checklist (Sep 21)

- [ ] All F1–F8 features working end-to-end
- [ ] Live URL reachable
- [ ] `/api/v1/health` returns UP
- [ ] Security checklist complete (see SECURITY.md)
- [ ] CI green (GitHub Actions)
- [ ] README + SETUP complete
- [ ] 2–3 minute backup demo video recorded
- [ ] Release tagged `v1.0.0`

## Smoke-test journeys

1. Admin login → view users / products / orders
2. Seller login → add product → see it on Browse
3. Buyer register → search → add to cart → checkout → view order
4. Leave a star review on a product
