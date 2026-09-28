// ===================== FoodieGo shared helpers =====================

function isLoggedIn() {
  return !!localStorage.getItem("fg_token");
}

function getUserName() {
  return localStorage.getItem("fg_name") || "";
}

function saveSession(auth) {
  localStorage.setItem("fg_token", auth.token);
  localStorage.setItem("fg_userId", auth.userId);
  localStorage.setItem("fg_name", auth.fullName);
  localStorage.setItem("fg_email", auth.email);
}

function logout() {
  localStorage.removeItem("fg_token");
  localStorage.removeItem("fg_userId");
  localStorage.removeItem("fg_name");
  localStorage.removeItem("fg_email");
  window.location.href = "login.html";
}

function requireLogin() {
  if (!isLoggedIn()) {
    window.location.href = "login.html";
    return false;
  }
  return true;
}

function showToast(message) {
  let toast = document.getElementById("fg-toast");
  if (!toast) {
    toast = document.createElement("div");
    toast.id = "fg-toast";
    toast.className = "toast";
    document.body.appendChild(toast);
  }
  toast.textContent = message;
  toast.classList.add("show");
  clearTimeout(toast._timer);
  toast._timer = setTimeout(() => toast.classList.remove("show"), 2500);
}

function formatMoney(v) {
  return "₹" + Number(v).toFixed(0);
}

function initials(name) {
  if (!name) return "U";
  const parts = name.trim().split(" ");
  return (parts[0][0] + (parts[1] ? parts[1][0] : "")).toUpperCase();
}

// Renders the logged-in state into the top navbar (call on DOMContentLoaded on pages that have a navbar)
async function refreshNavCartBadge() {
  const badgeEls = document.querySelectorAll(".cart-badge");
  if (!badgeEls.length) return;
  if (!isLoggedIn()) {
    badgeEls.forEach(b => b.textContent = "0");
    return;
  }
  try {
    const cart = await Api.getCart();
    const count = cart.reduce((sum, c) => sum + c.quantity, 0);
    badgeEls.forEach(b => b.textContent = count);
  } catch (e) { /* silently ignore */ }
}

function bindNavAccountLink() {
  const el = document.getElementById("nav-account-link");
  if (!el) return;
  el.href = isLoggedIn() ? "profile.html" : "login.html";
}

document.addEventListener("DOMContentLoaded", () => {
  refreshNavCartBadge();
  bindNavAccountLink();
});
