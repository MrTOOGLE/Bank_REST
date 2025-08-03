-- Создание таблиц для системы управления банковскими картами

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role ENUM('ADMIN', 'USER') NOT NULL DEFAULT 'USER'
    );

CREATE TABLE IF NOT EXISTS cards (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    number VARCHAR(16) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    validity_period DATE NOT NULL,
    status ENUM('ACTIVE', 'BLOCKED', 'EXPIRED') NOT NULL DEFAULT 'ACTIVE',
    balance DECIMAL(19,2) NOT NULL DEFAULT 0.00,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
    );
