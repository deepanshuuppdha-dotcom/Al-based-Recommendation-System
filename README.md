# AI-Based Recommendation System

Java web app (Servlets, JSP, JDBC, MySQL) that suggests products based on user preferences and behavior. Backend is 100% Java. Frontend is JSP, HTML, CSS and JavaScript.

## Requirements
JDK 17, Maven 3.9+, MySQL 8

## Setup
1. Run db/schema.sql then db/seed.sql in MySQL.
2. Copy src/main/resources/db.properties.example to src/main/resources/db.properties and fill in your MySQL user and password.
3. Run: mvn clean package cargo:run
4. Open http://localhost:8080/login

## Demo logins
- Admin: admin@recsys.com / Admin@123
- User: alice@example.com / User@123

## Project structure
- src/main/java/com/recsys: model, dao, dao/impl, service, filter, servlet, util, exception
- src/main/webapp/WEB-INF/views: JSP pages
- db: schema.sql and seed.sql
