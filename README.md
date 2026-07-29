# Blogger Platform API

Production-ready REST API for a blogging platform.  
Migrated from NestJS/TypeScript to **Java 21 + Spring Boot 3.3** to demonstrate backend architecture skills, security patterns, and clean code practices.

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 3.3 |
| Database | MongoDB |
| Auth | JWT (jjwt 0.12) + Basic Auth |
| Email | Spring Mail (SMTP via Mail.ru) |
| Docs | SpringDoc OpenAPI / Swagger UI |
| Config | spring-dotenv (`.env` support) |

## Architecture

**Package-by-Layer** structure — classic layered architecture:

```
src/main/java/com/example/newshowcase/
├── config/         — Security, JWT, Web, OpenAPI configuration
├── controller/     — REST controllers (Auth, Blogs, Posts, Users)
├── dto/            — request/response models with validation
├── exception/      — global error handling (@RestControllerAdvice)
├── filter/         — JWT auth filter, rate limiting filter
├── interceptor/    — request/response logging
├── model/          — MongoDB documents (entities)
├── repository/     — Spring Data + custom query repositories (CQRS-lite)
└── service/        — business logic layer
```

## Key Features & Patterns Demonstrated

### Authentication & Security
- **JWT-based auth** — stateless access tokens (HS256), configurable expiration
- **Basic Auth** — for admin endpoints (`/users`)
- **Registration flow** — signup -> email confirmation code -> account activation
- **Rate limiting** — IP-based throttling (5 req / 10 sec) on sensitive endpoints

### Email Integration
- SMTP email sending (Mail.ru) with HTML templates
- Registration confirmation with expirable codes (24h TTL)
- Resend confirmation endpoint with rate limiting

### API Design
- RESTful resource naming
- Pagination with configurable page size, sort field, sort direction
- Search/filter support (by name, login, email)
- Validation with custom regex patterns and Jakarta Bean Validation
- Consistent error response format (`errorsMessages[]`)

### Data Layer
- **CQRS-lite pattern** — separate read repositories (`QueryRepository` with `MongoTemplate`) and write repositories (`MongoRepository`)
- Flexible dynamic queries with criteria builder

### Observability
- Structured logging (SLF4J) at service level — auth events, CRUD operations, errors
- Request/response time logging via interceptor
- Global exception handler with catch-all for unexpected errors

## API Endpoints

### Auth (`/auth`)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/auth/login` | Login, returns JWT | - |
| POST | `/auth/registration` | Register + send confirmation email | - |
| POST | `/auth/registration-confirmation` | Confirm email with code | - |
| POST | `/auth/registration-email-resending` | Resend confirmation email | - |
| GET | `/auth/me` | Get current user info | Bearer JWT |

### Users (`/users`)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/users` | List users (paginated, searchable) | Basic |
| POST | `/users` | Create user | Basic |
| GET | `/users/{id}` | Get user by ID | Basic |
| DELETE | `/users/{id}` | Delete user | Basic |

### Blogs (`/blogs`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/blogs` | List blogs (paginated, searchable) |
| POST | `/blogs` | Create blog |
| GET | `/blogs/{id}` | Get blog by ID |
| PUT | `/blogs/{id}` | Update blog |
| DELETE | `/blogs/{id}` | Delete blog |
| GET | `/blogs/{id}/posts` | Get posts for blog |
| POST | `/blogs/{id}/posts` | Create post in blog |

### Posts (`/posts`)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/posts` | List posts (paginated) |
| POST | `/posts` | Create post |
| GET | `/posts/{id}` | Get post by ID |
| PUT | `/posts/{id}` | Update post |
| DELETE | `/posts/{id}` | Delete post |

## Getting Started

### Prerequisites

- Java 21+
- MongoDB (local or cloud)

### Configuration

Create `.env` in the project root:

```env
PORT=3000
MONGO_URI_CLOUD=mongodb://localhost:27017
DB_NAME=nest_base
ACCESS_JWT_SECRET=your-secret-min-32-chars-long-here
REFRESH_JWT_SECRET=your-secret-min-32-chars-long-here
MAIL_USERNAME=your-email@mail.ru
MAIL_PASSWORD=your-app-password
```

### Run

```bash
mvn spring-boot:run
```

### Swagger UI

```
http://localhost:3000/swagger-doc
```

Use the **Authorize** button to set JWT or Basic credentials for testing protected endpoints.

## Testing

```bash
mvn test
```

The `DELETE /testing/all-data` cleanup endpoint is isolated in `src/test` and excluded from production builds.
