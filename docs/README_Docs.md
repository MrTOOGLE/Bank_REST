# 🏦 Система Управления Банковскими Картами

REST API для управления банковскими картами с JWT аутентификацией и ролевым доступом.

## 📋 Описание

Система позволяет:
- **Создавать и управлять** банковскими картами
- **Делать переводы** между своими картами
- **Управлять пользователями** (для администраторов)
- **Просматривать карты** с маскированными номерами
- **Блокировать карты** и управлять статусами

## 🚀 Технологии

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** + JWT токены (JJWT 0.11.5)
- **Spring Data JPA** + Hibernate
- **Spring Validation** (Jakarta Validation)
- **MySQL 8.0** + MySQL Connector
- **Liquibase** для миграций БД
- **MapStruct 1.5.5** для маппинга DTO
- **Lombok** для упрощения кода
- **SpringDoc OpenAPI** (Swagger UI 2.2.0)
- **Docker Compose** для развертывания
- **JUnit 5** + Spring Security Test для тестирования

## ⚡ Быстрый старт

### 🐳 Запуск через Docker (рекомендуется)

```bash
# Клонирование репозитория
git clone https://github.com/MrTOOGLE/Bank_REST.git
cd Bank_REST

# Сборка приложения
./gradlew clean build

# Запуск контейнеров
docker-compose up -d

# Проверка статуса
docker-compose ps
```

Приложение будет доступно по адресу: **http://localhost:8080**

### 💻 Локальный запуск

1. **Запустить MySQL локально:**
```bash
# Создать базу данных
mysql -u root -p
CREATE DATABASE bankcards_db;
CREATE USER 'bankuser'@'localhost' IDENTIFIED BY 'bankpass';
GRANT ALL PRIVILEGES ON bankcards_db.* TO 'bankuser'@'localhost';
```

2. **Изменить application.yml:**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bankcards_db
```

3. **Запустить приложение:**
```bash
./gradlew bootRun
```

## 📖 API Документация

### Swagger UI
**http://localhost:8080/swagger-ui.html** - интерактивная документация API

### Основные эндпоинты

#### 🔐 Аутентификация
- `POST /api/v1/auth/register` - Регистрация
- `POST /api/v1/auth/login` - Вход в систему

#### 💳 Управление картами
- `GET /api/v1/cards` - Просмотр карт (с пагинацией)
- `GET /api/v1/cards/my/full` - Свои карты с полными номерами
- `GET /api/v1/cards/{id}/balance` - Баланс карты
- `POST /api/v1/cards` - Создание карты (только ADMIN)
- `PUT /api/v1/cards/{id}/status` - Изменение статуса (только ADMIN)
- `PUT /api/v1/cards/{id}/block` - Блокировка карты
- `DELETE /api/v1/cards/{id}` - Удаление карты (только ADMIN)

#### 💸 Переводы
- `POST /api/v1/transfers` - Перевод между своими картами

#### 👥 Управление пользователями (только ADMIN)
- `GET /api/v1/users` - Список пользователей
- `POST /api/v1/users` - Создание пользователя
- `PUT /api/v1/users/{id}/role` - Изменение роли
- `DELETE /api/v1/users/{id}` - Удаление пользователя

## 👤 Роли пользователей

### 🔨 ADMIN
- Создание, удаление, блокировка карт
- Управление пользователями (создание, удаление, изменение ролей)
- Просмотр всех карт в системе
- Изменение статусов карт

### 👨‍💼 USER
- Просмотр своих карт
- Переводы между своими картами
- Запрос блокировки своих карт
- Просмотр баланса

## 🔒 Безопасность

- **JWT токены** для аутентификации
- **Маскирование номеров карт** (**** **** **** 1234)
- **Ролевой доступ** (ADMIN/USER)
- **Шифрование паролей** (BCrypt)
- **Валидация входных данных**
- **Защита от SQL инъекций** (JPA)

## 🗃️ База данных

### Схема
- **users** - пользователи системы
- **cards** - банковские карты

### Миграции
Автоматические Liquibase миграции при запуске приложения.

## 🏗️ Архитектура проекта

```
src/main/java/com/example/bankcards/
├── config/          # Конфигурация (Security, etc)
├── controller/      # REST контроллеры
├── dto/            # Data Transfer Objects
├── entity/         # JPA сущности
├── exception/      # Обработка исключений
├── mapper/         # MapStruct мапперы
├── repository/     # JPA репозитории
├── security/       # JWT и Spring Security
├── service/        # Бизнес-логика
└── util/           # Утилиты
```

## 📦 Docker

### Сервисы
- **MySQL 8.0** - база данных (порт 3306)
- **Java приложение** - REST API (порт 8080)

### Volumes
- `mysql_data` - персистентные данные MySQL

## 📚 Дополнительная информация

- **Пагинация**: `?page=0&size=10&sort=validityPeriod,desc`
- **Валидация**: Автоматическая валидация DTO с Jakarta Validation
- **Обработка ошибок**: Глобальный exception handler
- **Логирование**: Логи SQL запросов включены

## 👨‍💻 Разработчик

Проект создан как тестовое задание для демонстрации навыков разработки на Spring Boot.