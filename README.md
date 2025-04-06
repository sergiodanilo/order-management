# 📦 Order Management API

A Spring Boot project for managing orders in a sales system. The application supports CRUD operations for Clients, Resales, Products, and Orders, with integration to an unstable external API using a Circuit Breaker pattern for resilience.

## 🚀 Technologies Used
- Java 17
- Spring Boot
- Spring Data JPA
- Spring Web
- Spring Retry & Resilience4j (Circuit Breaker)
- Maven
- H2 Database
- Lombok
- Jakarta Validation

## 🛠️ Getting Started
### Prerequisites
- Java 17+
- Maven 3.8+

### Running the Application
```
# Clone the repository
git clone https://github.com/your-org/your-project.git

cd your-project

# Build the application
mvn clean install

# Run the Spring Boot application
mvn spring-boot:run
```
The application will start at: http://localhost:8080

## 🌱 Mock Data Initialization
You can call the following endpoint to create mock data in your database:

### `GET /api/startup`

This will automatically generate:
- Client
- Resale
- Products
- Order with multiple products

You can customize the logic in StartupController to generate more data if needed.