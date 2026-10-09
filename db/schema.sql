-- ==========================================================
--  RECSYS DATABASE SCHEMA
-- ==========================================================
CREATE DATABASE IF NOT EXISTS recsys_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE recsys_db;

SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS recommendation_feedback;
DROP TABLE IF EXISTS recommendations;
DROP TABLE IF EXISTS interactions;
DROP TABLE IF EXISTS user_preferences;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS system_settings;

SET FOREIGN_KEY_CHECKS=1;

-- ----------------------------------------------------------
--  users
-- ----------------------------------------------------------
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(60) NOT NULL,
    role ENUM('ADMIN','USER') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    INDEX idx_user_email (email),
    INDEX idx_user_role (role)
) ENGINE=InnoDB COMMENT='Application users';

-- ----------------------------------------------------------
--  categories
-- ----------------------------------------------------------
CREATE TABLE categories (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    INDEX idx_category_name (name)
) ENGINE=InnoDB COMMENT='Product categories';

-- ----------------------------------------------------------
--  products
-- ----------------------------------------------------------
CREATE TABLE products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    description TEXT,
    category_id BIGINT NOT NULL,
    tags VARCHAR(255),
    price DECIMAL(10,2) NULL,
    image_url VARCHAR(255),
    popularity_score DOUBLE NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id)
        REFERENCES categories(id) ON DELETE CASCADE,
    INDEX idx_product_category (category_id),
    INDEX idx_product_popularity (popularity_score),
    INDEX idx_product_created (created_at)
) ENGINE=InnoDB COMMENT='Available products';

-- ----------------------------------------------------------
--  user_preferences
-- ----------------------------------------------------------
CREATE TABLE user_preferences (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    weight TINYINT NOT NULL CHECK (weight BETWEEN 1 AND 5),
    CONSTRAINT fk_pref_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_pref_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
    UNIQUE KEY uq_user_category (user_id, category_id),
    INDEX idx_pref_user (user_id),
    INDEX idx_pref_category (category_id)
) ENGINE=InnoDB COMMENT='Per‑user category interest weights';

-- ----------------------------------------------------------
--  interactions
-- ----------------------------------------------------------
CREATE TABLE interactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    type ENUM('VIEW','LIKE','CLICK','PURCHASE','DISLIKE') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_inter_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_inter_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_inter_user (user_id),
    INDEX idx_inter_product (product_id),
    INDEX idx_inter_type (type),
    INDEX idx_inter_created (created_at)
) ENGINE=InnoDB COMMENT='User‑product interaction events';

-- ----------------------------------------------------------
--  recommendations
-- ----------------------------------------------------------
CREATE TABLE recommendations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    score DECIMAL(6,3) NOT NULL,
    algorithm VARCHAR(20) NOT NULL,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_rec_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_rec_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    INDEX idx_rec_user (user_id),
    INDEX idx_rec_product (product_id),
    INDEX idx_rec_algo (algorithm),
    INDEX idx_rec_generated (generated_at)
) ENGINE=InnoDB COMMENT='Generated recommendations';

-- ----------------------------------------------------------
--  recommendation_feedback (logs)
-- ----------------------------------------------------------
CREATE TABLE recommendation_feedback (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recommendation_id BIGINT NOT NULL,
    shown_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    clicked BOOLEAN NOT NULL DEFAULT FALSE,
    converted BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_feedback_rec FOREIGN KEY (recommendation_id) REFERENCES recommendations(id) ON DELETE CASCADE,
    INDEX idx_feedback_rec (recommendation_id),
    INDEX idx_feedback_shown (shown_at)
) ENGINE=InnoDB COMMENT='Feedback on shown recommendations';

-- ----------------------------------------------------------
--  system_settings
-- ----------------------------------------------------------
CREATE TABLE system_settings (
    setting_key VARCHAR(50) PRIMARY KEY,
    setting_value VARCHAR(255) NOT NULL,
    updated_by BIGINT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_setting_updater FOREIGN KEY (updated_by) REFERENCES users(id) ON DELETE SET NULL
) ENGINE=InnoDB COMMENT='Global configuration values';
