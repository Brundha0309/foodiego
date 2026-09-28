// ===================== FoodieGo API client =====================
// Set the base URL of your running Spring Boot backend here.
const API_BASE = "http://localhost:8080/api";

const Api = {
  async request(path, options = {}) {
    const token = localStorage.getItem("fg_token");
    const headers = { "Content-Type": "application/json", ...(options.headers || {}) };
    if (token) headers["Authorization"] = "Bearer " + token;

    const res = await fetch(API_BASE + path, { ...options, headers });
    let data = null;
    try { data = await res.json(); } catch (e) { /* no body */ }

    if (!res.ok) {
      const message = (data && data.message) ? data.message : "Something went wrong. Please try again.";
      throw new Error(message);
    }
    return data;
  },

  get(path) { return this.request(path, { method: "GET" }); },
  post(path, body) { return this.request(path, { method: "POST", body: JSON.stringify(body) }); },
  put(path, body) { return this.request(path, { method: "PUT", body: JSON.stringify(body) }); },
  del(path) { return this.request(path, { method: "DELETE" }); },

  // ---- Auth ----
  signup(data) { return this.post("/auth/signup", data); },
  login(data) { return this.post("/auth/login", data); },

  // ---- Catalog ----
  getCategories() { return this.get("/categories"); },
  getRestaurants(category) {
    const q = category && category !== "all" ? `?category=${encodeURIComponent(category)}` : "";
    return this.get("/restaurants" + q);
  },
  searchRestaurants(query) { return this.get(`/restaurants?search=${encodeURIComponent(query)}`); },
  getRestaurant(id) { return this.get(`/restaurants/${id}`); },
  getRestaurantMenu(id) { return this.get(`/restaurants/${id}/menu`); },
  getMenuItems(category) {
    const q = category && category !== "all" ? `?category=${encodeURIComponent(category)}` : "";
    return this.get("/menu-items" + q);
  },

  // ---- Cart ----
  getCart() { return this.get("/cart"); },
  addToCart(menuItemId, quantity = 1) { return this.post("/cart/add", { menuItemId, quantity }); },
  updateCartItem(cartItemId, quantity) { return this.put(`/cart/${cartItemId}`, { quantity }); },
  removeCartItem(cartItemId) { return this.del(`/cart/${cartItemId}`); },

  // ---- Orders ----
  checkout(payload) { return this.post("/orders/checkout", payload); },
  getOrderHistory() { return this.get("/orders"); },
  trackOrder(id) { return this.get(`/orders/${id}`); },

  // ---- Address ----
  getAddresses() { return this.get("/addresses"); },
  addAddress(data) { return this.post("/addresses", data); },
  deleteAddress(id) { return this.del(`/addresses/${id}`); },

  // ---- Coupons ----
  getCoupons() { return this.get("/coupons"); },

  // ---- Profile ----
  getProfile() { return this.get("/profile"); },
  updateProfile(data) { return this.put("/profile", data); },

  // ---- Contact ----
  sendContact(data) { return this.post("/contact", data); },
};
