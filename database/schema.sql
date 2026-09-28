-- =====================================================
-- FoodieGo Database Schema (MySQL)
-- This is created automatically by Hibernate (ddl-auto=update)
-- when the Spring Boot app runs. This file is provided for
-- reference / manual setup if you prefer to create it yourself.
-- =====================================================

CREATE DATABASE IF NOT EXISTS foodiego_db;
USE foodiego_db;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(30),
    password_hash VARCHAR(255) NOT NULL,
    auth_token VARCHAR(255),
    created_at DATETIME
);

CREATE TABLE IF NOT EXISTS addresses (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    label VARCHAR(100),
    address_line VARCHAR(500) NOT NULL,
    city VARCHAR(100),
    state VARCHAR(100),
    pincode VARCHAR(20),
    is_default BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    icon VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS restaurants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cuisine VARCHAR(255),
    rating DOUBLE,
    review_count INT,
    delivery_time VARCHAR(50),
    free_delivery BOOLEAN DEFAULT TRUE,
    image_url VARCHAR(500),
    category_id BIGINT,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS menu_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id BIGINT NOT NULL,
    category_id BIGINT,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    price DOUBLE NOT NULL,
    image_url VARCHAR(500),
    recommended BOOLEAN DEFAULT FALSE,
    veg BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id),
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

CREATE TABLE IF NOT EXISTS cart_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    menu_item_id BIGINT NOT NULL,
    quantity INT DEFAULT 1,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

CREATE TABLE IF NOT EXISTS coupons (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(255),
    description VARCHAR(500),
    discount_type VARCHAR(30),
    discount_value DOUBLE,
    min_order_value DOUBLE DEFAULT 0,
    badge_color VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    address_id BIGINT,
    status VARCHAR(30) DEFAULT 'PLACED',
    item_total DOUBLE,
    delivery_fee DOUBLE,
    platform_fee DOUBLE,
    discount DOUBLE DEFAULT 0,
    total_payable DOUBLE,
    payment_method VARCHAR(50),
    coupon_code VARCHAR(50),
    placed_at DATETIME,
    preparing_at DATETIME,
    out_for_delivery_at DATETIME,
    delivered_at DATETIME,
    delivery_partner_name VARCHAR(100),
    delivery_partner_phone VARCHAR(30),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (address_id) REFERENCES addresses(id)
);

CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    item_name VARCHAR(255),
    price DOUBLE,
    quantity INT,
    FOREIGN KEY (order_id) REFERENCES orders(id)
);

CREATE TABLE IF NOT EXISTS contact_messages (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    email VARCHAR(255),
    message VARCHAR(2000),
    created_at DATETIME
);
