/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  lorraineb, seany
 * Created: Feb 28, 2026
 */
DROP database peso_track;
CREATE DATABASE IF NOT EXISTS peso_track;
USE peso_track;

-- Users

CREATE TABLE IF NOT EXISTS users(
    user_id     INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    first_name  VARCHAR(64) NOT NULL,
    last_name   VARCHAR(64) NOT NULL,
    email       VARCHAR(64) UNIQUE NOT NULL,
    password    VARCHAR(255) NOT NULL,
    created_at  DATE NOT NULL DEFAULT (CURRENT_DATE),
    INDEX idx_email (email)
);

-- Accounts

CREATE TABLE IF NOT EXISTS accounts (
    id          INT UNSIGNED    AUTO_INCREMENT PRIMARY KEY,
    user_id     INT UNSIGNED    NOT NULL,
    type        ENUM('Cash', 'Savings', 'Credit Card', 'Other') NOT NULL,
    balance     DECIMAL(15, 2)  NOT NULL DEFAULT 0.00,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY uq_user_type (user_id, type),
    INDEX idx_user (user_id)
);

-- Categories

CREATE TABLE IF NOT EXISTS categories (
    id          SMALLINT UNSIGNED   AUTO_INCREMENT PRIMARY KEY,
    user_id     INT UNSIGNED        NULL,
    name        VARCHAR(100)        NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user (user_id)
);

INSERT INTO categories (id, user_id, name) VALUES
    (1,  NULL, 'Uncategorized'),
    (2,  NULL, 'Salary'),
    (3,  NULL, 'Business'),
    (4,  NULL, 'Freelance'),
    (5,  NULL, 'Other Income'),
    (6,  NULL, 'Food & Dining'),
    (7,  NULL, 'Transportation'),
    (8,  NULL, 'Housing'),
    (9,  NULL, 'Health'),
    (10, NULL, 'Shopping'),
    (11, NULL, 'Entertainment'),
    (12, NULL, 'Utilities'),
    (13, NULL, 'Subscriptions'),
    (14, NULL, 'Other Expense'),
    (15, NULL, 'Transfer');

-- Transactions

CREATE TABLE IF NOT EXISTS transactions (
    id              BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    user_id         INT UNSIGNED NOT NULL,
    account_id      INT UNSIGNED NOT NULL,
    category_id     SMALLINT UNSIGNED NOT NULL DEFAULT 1,
    type            ENUM('income','expense','transfer') NOT NULL,
    amount          DECIMAL(15,2) NOT NULL,
    date            DATE NOT NULL,
    description     VARCHAR(255) NOT NULL,
    created_at      DATE NOT NULL DEFAULT (CURRENT_DATE), 
 
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE RESTRICT,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET DEFAULT,

    INDEX idx_user (user_id),
    INDEX idx_account (account_id),
    INDEX idx_category (category_id),
    INDEX idx_date (date),
    INDEX idx_type (type)
);


-- USE peso_track;
-- drop database peso_track;
SELECT * FROM users;
SELECT * FROM categories;
SELECT * FROM accounts;
SELECT * FROM transactions;