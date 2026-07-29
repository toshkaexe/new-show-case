# New Show Case — Blogger API

REST API for a blogging platform built with Java 21 + Spring Boot 3.3.

## Tech Stack

- Java 21
- Spring Boot 3.3 (Web, Security, Data MongoDB, Validation)
- MongoDB
- JWT (jjwt 0.12)
- SpringDoc OpenAPI (Swagger UI)

## Features

- **Auth** — registration, login, JWT authentication
- **Users** — CRUD with pagination and search
- **Blogs** — CRUD with pagination and name search
- **Posts** — CRUD, linked to blogs, likes system

## Getting Started

### Prerequisites

- Java 21+
- MongoDB (local or cloud URI)

### Environment Variables

| Variable | Default | Description |
|---|---|---|
| `MONGO_URI_CLOUD` | `mongodb://localhost:27017` | MongoDB connection URI |
| `DB_NAME` | `nest_base` | Database name |
| `PORT` | `3000` | Server port |
| `ACCESS_JWT_SECRET` | `defaultAccessSecret` | Access token secret |
| `REFRESH_JWT_SECRET` | `defaultRefreshSecret` | Refresh token secret |

### Running the Application

```bash
./mvnw spring-boot:run
```

Or with Maven:

```bash
mvn spring-boot:run
```

## API Documentation

Swagger UI is available at:

```
http://localhost:3000/swagger-doc
```

## Project Structure

```
src/main/java/com/example/newshowcase/
├── config/         — configuration (Security, JWT, Web)
├── controller/     — REST controllers
├── dto/            — request/response models
├── exception/      — error handling
├── filter/         — JWT filter
├── interceptor/    — request logging
├── model/          — MongoDB entities
├── repository/     — repositories (Spring Data + Query)
└── service/        — business logic
```

## Testing

```bash
mvn test
```

The `DELETE /testing/all-data` endpoint is only available in the test context for database cleanup.
