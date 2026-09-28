# FoodieGo — Online Food Delivery Application

A full-stack food delivery web app built with **Java 17 + Spring Boot** (backend/REST API),
**MySQL** (database), and **HTML5 + CSS3 + vanilla JavaScript** (frontend) — matching the
12-page design you provided (Home, Login/Signup, Browse/Menu, Restaurant Details, Cart,
Checkout, Order Tracking, Profile, Offers, About, Contact).

---

## 1. Project Structure

```
foodiego/
├── backend/                  Spring Boot REST API
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/foodiego/
│       │   ├── entity/       JPA entities (User, Restaurant, MenuItem, Order, ...)
│       │   ├── repository/   Spring Data JPA repositories
│       │   ├── service/      Business logic (Auth, Cart, Order)
│       │   ├── controller/   REST controllers
│       │   ├── security/     Lightweight token-auth helper
│       │   ├── config/       CORS + password encoder config
│       │   ├── dto/          Request/response objects
│       │   └── FoodieGoApplication.java
│       └── resources/
│           ├── application.properties       (MySQL config)
│           ├── application-h2.properties    (optional in-memory H2 profile)
│           └── data.sql                     (auto-seeds categories, restaurants, 150+ menu items, coupons)
├── database/
│   ├── schema.sql            Reference schema (for manual DB setup)
│   └── seed-data.sql         Same seed data as data.sql, for manual import
└── frontend/                 Static HTML/CSS/JS (12 pages)
    ├── index.html            1. Home Page
    ├── login.html            2. Login Page
    ├── signup.html           3. Sign Up Page
    ├── menu.html              4. Browse / Menu Page (All, Pizza, Burgers, Indian, Chinese, Healthy, Desserts, Beverages)
    ├── restaurant.html       5. Restaurant / Food Details Page
    ├── cart.html             6. Cart Page
    ├── checkout.html         7. Checkout / Payment Page
    ├── track-order.html      8. Order Tracking Page
    ├── profile.html          9. Profile / Account Page
    ├── offers.html           10. Offers & Coupons Page
    ├── about.html            11. About Us Page
    ├── contact.html          12. Contact Us Page
    ├── css/style.css
    └── js/{api.js, common.js}
```

---

## 2. Prerequisites

- **Java 17+** (JDK)
- **Maven 3.8+** (or use the included `mvnw` if you add the wrapper — this project uses plain Maven)
- **MySQL 8.x** running locally (or use the H2 in-memory profile for a zero-install test run)
- Any static file server / VS Code "Live Server" / or just opening the HTML files directly in a browser

---

## 3. Backend Setup (Spring Boot + MySQL)

### Step 1 — Create the database (optional — Spring Boot can auto-create it)
```sql
CREATE DATABASE foodiego_db;
```
Or just let the app do it: `application.properties` already has
`createDatabaseIfNotExist=true`.

### Step 2 — Configure DB credentials
Edit `backend/src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/foodiego_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Step 3 — Run the backend
```bash
cd backend
mvn spring-boot:run
```
The API starts at **http://localhost:8080**. On first run, Hibernate creates all tables
(`ddl-auto=update`) and `data.sql` automatically seeds:
- 7 categories (Pizza, Burgers, Indian, Chinese, Healthy, Desserts, Beverages)
- 21 restaurants (3 per category)
- **154 menu items** (22+ per category)
- 5 coupons (FLAT50, HUNGRY100, FREEDEL, SAVE20, WEEKEND75)

> **No MySQL installed?** Run with the built-in H2 in-memory database instead — no setup needed:
> ```bash
> mvn spring-boot:run -Dspring-boot.run.profiles=h2
> ```

### Quick test
Open http://localhost:8080/api/categories in your browser — you should see JSON output.

---

## 4. Frontend Setup

The frontend is plain static HTML/CSS/JS — no build step required.

1. Open `frontend/js/api.js` and confirm `API_BASE` points to your backend
   (default: `http://localhost:8080/api`).
2. Serve the `frontend` folder with any static server, e.g.:
   ```bash
   cd frontend
   python3 -m http.server 5500
   ```
   Then visit **http://localhost:5500/index.html**
   *(Opening the HTML files directly via `file://` also works, but a local server avoids
   browser CORS/file quirks.)*

---

## 5. How It All Works

- **Auth**: Signup/Login hits `/api/auth/signup` & `/api/auth/login`. Passwords are hashed
  with BCrypt. A random token is issued and stored in `localStorage`; every protected request
  sends it as `Authorization: Bearer <token>`.
- **Browse/Menu**: `/api/categories`, `/api/restaurants?category=`, `/api/menu-items?category=`
  power the sidebar filters and grids — each category has 22+ real menu items.
- **Restaurant details**: `/api/restaurants/{id}` + `/api/restaurants/{id}/menu`.
- **Cart**: `/api/cart` (GET/POST/PUT/DELETE) is tied to the logged-in user server-side.
- **Checkout**: `/api/orders/checkout` computes item total, ₹30 delivery fee, ₹10 platform fee,
  applies coupon discounts, creates the `Order` + `OrderItem` rows, and clears the cart.
- **Order tracking**: `/api/orders/{id}` simulates live status progression
  (Placed → Preparing → Out for Delivery → Delivered) based on elapsed time.
- **Profile**: `/api/profile` (view/update) + `/api/addresses` (list/add/delete).
- **Offers**: `/api/coupons` lists all active coupons, shown as colored cards.
- **Contact**: `/api/contact` stores messages in the `contact_messages` table.

---

## 6. Default Test Flow

1. Go to **Sign Up**, create an account.
2. Browse **Menu**, pick a category (e.g. Pizza), open a restaurant, add items to cart.
3. Go to **Cart** → **Checkout** → add a delivery address → pick payment method → apply a
   coupon (try `FLAT50` or `FREEDEL`) → **Place Order**.
4. You're redirected to **Track Order**, which shows live progress.
5. Check **Profile** → **My Orders** to see order history, and **Offers** for all coupons.

---

## 7. Notes & Next Steps

- This project uses a simple opaque-token auth (not JWT) to keep the codebase easy to read and
  extend — swap in Spring Security + JWT if you need production-grade auth.
- Images are pulled from Unsplash URLs for demo purposes — replace with your own hosted images
  for production.
- To reset seed data, drop the tables (or the DB) and restart the app with
  `spring.sql.init.mode=always` (already set).
