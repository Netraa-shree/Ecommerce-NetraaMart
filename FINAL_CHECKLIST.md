

## Features

- [x] F1 Authentication (Buyer / Seller / Admin)
- [x] F2 Seller product listings
- [x] F3 Browse / search
- [x] F4 Cart
- [x] F5 Checkout (mock payment)
- [x] F6 Order history
- [x] F7 Admin panel
- [x] F8 Reviews & ratings
- [x] O4 AI chatbot (mock FAQ provider)

## Security

- [x] BCrypt passwords
- [x] PreparedStatements only
- [x] AuthFilter + role checks
- [x] Session regenerated on login
- [x] Soft-delete products

## Deploy readiness

- [ ] WAR built (`mvn clean package`)
- [ ] Live URL up
- [ ] Health endpoint UP on live server
- [ ] Smoke-test all journeys on live URL
- [ ] Backup 2–3 min demo video
- [ ] README / SETUP / SECURITY complete
- [ ] Tag `v1.0.0` when green

## Smoke tests

1. Register buyer → browse → cart → checkout → orders  
2. Register seller → add product → see on browse  
3. Admin login → users / products / orders  
4. Leave a review on a product  
5. Open `/chat` and ask “How do I checkout?”
