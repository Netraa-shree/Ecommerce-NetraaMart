package com.yournamemart.chat;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Canned FAQ chatbot — no external network required.
 * Covers common marketplace questions for demo / evaluation.
 */
public class MockChatProvider implements ChatProvider {

    private final Map<String, String> faq = new LinkedHashMap<>();

    public MockChatProvider() {
        faq.put("hello", "Hi! I'm the NetraaMart assistant. Ask me about orders, products, sellers, or returns.");
        faq.put("hi", "Hello! How can I help you today?");
        faq.put("help", "You can ask about: how to register, how to sell, cart, checkout, orders, reviews, or admin.");
        faq.put("register", "Click Register, choose Buyer or Seller, enter name, email and password (min 6 characters).");
        faq.put("login", "Use your email and password on the Login page. Admin: admin@netraamart.com / admin123");
        faq.put("sell", "Register as a Seller, then open Seller Dashboard to add, edit or delete products.");
        faq.put("product", "Sellers list products with name, price, stock, category and optional image URL. Buyers can browse and search.");
        faq.put("cart", "Open a product, set quantity, click Add to Cart. View Cart to update quantities or remove items.");
        faq.put("checkout", "From Cart, go to Checkout and confirm the mock payment. No real money is charged.");
        faq.put("order", "After checkout, open Orders to see your order history and payment reference.");
        faq.put("payment", "Payment is simulated (mock). You get a payment reference like MOCK-XXXXXXXX after placing an order.");
        faq.put("review", "On a product page, log in and submit a 1–5 star rating with an optional comment.");
        faq.put("admin", "Admin can view users, moderate products and see all orders from the Admin panel.");
        faq.put("return", "For this demo project, returns are not processed online. Contact the seller offline if needed.");
        faq.put("delivery", "This is a coursework marketplace demo — shipping is not tracked. Orders are marked PLACED after mock payment.");
        faq.put("password", "Passwords are stored with BCrypt hashing. Demo admin/seller password is admin123.");
    }

    @Override
    public String reply(String userMessage) {
        if (userMessage == null || userMessage.isBlank()) {
            return "Please type a question. Try: \"How do I register?\" or \"How does checkout work?\"";
        }

        String q = userMessage.toLowerCase(Locale.ROOT).trim();

        for (Map.Entry<String, String> e : faq.entrySet()) {
            if (q.contains(e.getKey())) {
                return e.getValue();
            }
        }

        if (q.contains("thank")) {
            return "You're welcome! Happy shopping on NetraaMart.";
        }

        return "I'm not sure about that yet. Try asking about register, sell, cart, checkout, orders, reviews, or admin.";
    }
}
