const PRODUCTS = [
  { id: 1, name: "Wireless Bluetooth Headphones", cat: "Electronics", price: 1499, stars: 4.5, stock: 25 },
  { id: 2, name: "Cotton T-Shirt (Unisex)", cat: "Clothing", price: 399, stars: 4.2, stock: 50 },
  { id: 3, name: "Java Programming Book", cat: "Books", price: 599, stars: 4.8, stock: 15 },
  { id: 4, name: "Stainless Steel Water Bottle", cat: "Home", price: 799, stars: 4.3, stock: 40 },
  { id: 5, name: "USB-C Fast Charger 65W", cat: "Electronics", price: 1299, stars: 4.6, stock: 30 },
  { id: 6, name: "Desk Organizer Set", cat: "Home", price: 449, stars: 4.0, stock: 20 },
  { id: 7, name: "Running Sports Shoes", cat: "Clothing", price: 2199, stars: 4.4, stock: 12 },
  { id: 8, name: "Web Development Handbook", cat: "Books", price: 699, stars: 4.7, stock: 18 }
];
function getCart() {
  try { return JSON.parse(localStorage.getItem("nm_cart") || "[]"); } catch { return []; }
}
function saveCart(c) { localStorage.setItem("nm_cart", JSON.stringify(c)); updateCartBadge(); }
function addToCart(id, qty) {
  qty = qty || 1;
  const cart = getCart();
  const row = cart.find(x => x.id === id);
  if (row) row.qty += qty; else cart.push({ id, qty });
  saveCart(cart);
  alert("Added to cart");
}
function cartCount() { return getCart().reduce((s, x) => s + x.qty, 0); }
function updateCartBadge() {
  const el = document.getElementById("cartCount");
  if (el) el.textContent = cartCount();
}
function cartTotal() {
  return getCart().reduce((s, x) => {
    const p = PRODUCTS.find(p => p.id === x.id);
    return s + (p ? p.price * x.qty : 0);
  }, 0);
}
function stars(n) {
  const f = Math.floor(n);
  return "★".repeat(f) + (n % 1 >= 0.5 ? "☆" : "") + " " + n;
}
document.addEventListener("DOMContentLoaded", updateCartBadge);
