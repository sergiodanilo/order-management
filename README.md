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
git clone https://github.com/sergiodanilo/order-management.git

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
- 1 Client
- 1 Resale
- 10 Products
- 1 Order with 5 products

### Send a batch of orders to API

You can use the following endpoint to send a batch of orders to the external API (mocked):
### `POST /api/resale-orders`
The payload is a list of order IDs:
```json
[1, 2, 3]
```

You can customize the logic in StartupController to generate more data if needed.

## H2 Console
To access the H2 Console, navigate to:
```http://localhost:8080/h2-console```

Don't forget to change the JDBC URL to: 
```jdbc:h2:mem:testdb```