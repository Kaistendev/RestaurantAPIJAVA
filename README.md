# 🍽️ Restaurant API

![Java](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.0-6DB33F?style=for-the-badge&logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-4169E1?style=for-the-badge&logo=postgresql)
![Redis](https://img.shields.io/badge/Redis-7-DC382D?style=for-the-badge&logo=redis)
![Docker](https://img.shields.io/badge/Docker-Enabled-2496ED?style=for-the-badge&logo=docker)

A mission-critical, high-performance RESTful API designed for comprehensive restaurant management. Built with cutting-edge Java 25 and Spring Boot 4 technologies, focusing on scalability, security, and developer experience.

---

## 🚀 Key Features

### 🛡️ Security & Access Control

- **Stateless Authentication**: Robust JWT implementation with **Refresh Token** flow.
- **Granular RBAC**: Role-Based Access Control (CLIENT, RESTAURANT) managed via Spring Security 7.
- **Rate Limiting**: Protection against brute-force and abuse using **Bucket4j** (10 req/min per IP).
- **Security Audit**: Automated tracking of `createdAt`, `updatedAt`, `createdBy`, and `lastModifiedBy`.

### ⚡ Performance & Reliability

- **Redis Caching**: Intelligent caching for high-demand resources like Dishes, Menus, and Restaurants.
- **Soft Delete**: Logical deletion implementation using `@SQLDelete`, ensuring data recoverability.
- **Validation**: Strict Bean Validation and custom validators (e.g., `@UniqueEmail`) for early failure detection.

### 📖 Infrastructure & DX

- **Interactive API Docs**: Fully configured **Swagger UI** with JWT authorization support at `/swagger-ui.html`.
- **Database Versioning**: Deterministic schema management powered by **Flyway**.
- **Observability**: Production-ready monitoring via **Spring Boot Actuator** and structured **JSON Logging**.

---

## 🛠️ Technology Stack

| Category         | Technology                                   |
| :--------------- | :------------------------------------------- |
| **Backend**      | Java 25, Spring Boot 4.0+, Spring Security 7 |
| **Persistencia** | Hibernate, Spring Data JPA, PostgreSQL 17    |
| **Cache**        | Redis 7                                      |
| **Migraciones**  | Flyway                                       |
| **Mapeo**        | MapStruct 1.6+                               |
| **Utilidades**   | Lombok, Bucket4j, JJWT                       |
| **Testing**      | JUnit 5, Testcontainers, Mockito             |

---

## 🚦 Getting Started

### Prerequisites

- Docker & Docker Compose
- Java 25 (if running locally without Docker)
- Gradle 9

### 🐳 Quick Start with Docker

The entire stack (App, DB, Redis) is containerized for professional deployment.

1. **Clone the repository**:

   ```bash
   git clone https://github.com/Kaistendev/RestaurantAPIJAVA.git
   cd RestaurantAPIJAVA
   ```

2. **Spin up the environment**:

   ```bash
   docker-compose up --build
   ```

3. **Access the API**:
   - **Swagger UI**: [http://localhost:9191/swagger-ui.html](http://localhost:9191/swagger-ui.html)
   - **Health Check**: [http://localhost:9191/actuator/health](http://localhost:9191/actuator/health)

---

## 🧪 Testing & Quality

We maintain a high level of parity between development and production.
Integration tests use **Testcontainers** to spin up real instances of PostgreSQL and Redis during the build process.

```bash
./gradlew test
```

---
