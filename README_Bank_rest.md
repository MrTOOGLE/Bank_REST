# 🏦 Система Управления Банковскими Картами

REST API для управления банковскими картами с JWT аутентификацией.

## 🚀 Быстрый старт

```bash
# Клонирование и сборка
git clone https://github.com/MrTOOGLE/Bank_REST.git
cd Bank_REST
./gradlew clean build

# Запуск через Docker
docker-compose up -d
```

**Приложение доступно:** http://localhost:8080  
**Swagger UI:** http://localhost:8080/swagger-ui.html

## 🔐 Тестовые данные

После запуска можно зарегистрироваться через `/api/v1/auth/register` или использовать миграции для создания тестовых пользователей.

## 📚 Подробная документация

См. [docs/README_Docs.md](docs/README_Docs.md)

## 🛠 Технологии

Java 17, Spring Boot, Spring Security, JWT, MySQL 8.0, Liquibase, Docker