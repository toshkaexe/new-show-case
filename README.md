# Blogger Platform API

REST API for a blogging platform. The project demonstrates building a production-ready backend with Java 21 + Spring Boot 3.3, featuring full authentication flow, email confirmation, and pagination.

Originally written in NestJS/TypeScript, then rewritten in Java to demonstrate proficiency with the Spring ecosystem.

**Swagger UI:** http://localhost:3000/swagger-doc

## About the Project

This is an API for a platform where users can manage blogs and publish posts. Main scenarios:

1. **Registration** — user signs up, receives a confirmation code via email, activates the account
2. **Authentication** — login with email/login + password, receive a JWT token
3. **Password Recovery** — request a recovery code sent to email
4. **Blog Management** — create, edit, delete blogs
5. **Post Publishing** — posts are linked to blogs, support likes
6. **Administration** — user management via Basic Auth

## Tech Stack

- **Java 21** — records, text blocks, pattern matching
- **Spring Boot 3.3** — Web, Security, Data MongoDB, Validation, Mail
- **MongoDB** — document database
- **JWT (jjwt 0.12)** — stateless authentication
- **Spring Mail** — email sending via SMTP (Mail.ru)
- **SpringDoc OpenAPI** — Swagger UI documentation
- **spring-dotenv** — automatic `.env` file loading

## Project Structure

Uses **Package-by-Layer** — classic separation by technical layers:

```
src/main/java/com/example/newshowcase/
│
├── config/             Configuration
│   ├── SecurityConfig      — Spring Security (JWT + Basic Auth)
│   ├── WebConfig           — interceptors
│   ├── JwtProperties       — token settings from application.yml
│   └── OpenApiConfig       — Swagger UI with authorization
│
├── controller/         REST Controllers
│   ├── AuthController      — registration, login, confirmation, recovery
│   ├── BlogsController     — blog CRUD + blog posts
│   ├── PostsController     — post CRUD
│   ├── UsersController     — user management (Basic Auth)
│   └── WelcomePageController
│
├── dto/                Request/Response Models
│   ├── *Request            — input validation (Jakarta Validation)
│   └── *OutputModel        — API responses
│
├── exception/          Error Handling
│   ├── BadRequestException     — business errors (400)
│   └── GlobalExceptionHandler  — unified error format for the entire API
│
├── filter/             Servlet Filters
│   ├── JwtAuthFilter       — JWT extraction and validation from header
│   └── RateLimitFilter     — IP-based request throttling (5 req / 10 sec)
│
├── interceptor/        Interceptors
│   └── LoggingInterceptor  — request processing time logging
│
├── model/              MongoDB Entities (@Document)
│   ├── User                — user (with confirmation/recovery codes)
│   ├── Blog                — blog
│   ├── Post                — post
│   └── ExtendedLikesInfo, NewestLike — embedded objects
│
├── repository/         Data Layer
│   ├── *Repository         — Spring Data MongoDB (CRUD operations)
│   └── *QueryRepository    — custom queries via MongoTemplate
│
└── service/            Business Logic
    ├── AuthService         — registration, login, confirmation
    ├── JwtService          — token generation and validation
    ├── EmailService        — email sending (confirmation, recovery)
    ├── BlogsService        — blog logic
    ├── PostsService        — post logic
    └── UsersService        — user logic
```

### Why Two Repositories (Repository + QueryRepository)?

This is the **CQRS-lite** pattern — separating reads from writes:

- `BlogsRepository` (interface) — extends `MongoRepository`, provides standard `save()`, `findById()`, `delete()`
- `BlogsQueryRepository` (class) — uses `MongoTemplate` for complex queries: dynamic filters, pagination, sorting, DTO mapping

## How to Run

### Prerequisites

- Java 21+
- MongoDB (local or cloud cluster)

### 1. Clone the repository

```bash
git clone git@github.com:toshkaexe/new-show-case.git
cd new-show-case
```

### 2. Create `.env` file in the project root

```env
PORT=3000
MONGO_URI_CLOUD=mongodb://localhost:27017
DB_NAME=nest_base
ACCESS_JWT_SECRET=your-secret-key-minimum-32-characters
REFRESH_JWT_SECRET=your-secret-key-minimum-32-characters
MAIL_USERNAME=your-email@mail.ru
MAIL_PASSWORD=your-app-password
```

> JWT secrets must be at least 32 characters (HS256 requirement).  
> For Mail.ru use an app password, not the mailbox password.

### 3. Start MongoDB

```bash
# Docker
docker run -d -p 27017:27017 mongo

# Or local installation
mongod
```

### 4. Run the application

```bash
mvn spring-boot:run
```

### 5. Open Swagger UI

```
http://localhost:3000/swagger-doc
```

Use the **Authorize** button to enter a JWT token or Basic credentials for testing protected endpoints.

## API Endpoints

### Auth
| Method | URL | Description |
|--------|-----|-------------|
| POST | `/auth/registration` | Register + send confirmation email |
| POST | `/auth/registration-confirmation` | Confirm email with code |
| POST | `/auth/registration-email-resending` | Resend confirmation code |
| POST | `/auth/login` | Login, returns JWT |
| GET | `/auth/me` | Current user info (JWT) |
| POST | `/auth/password-recovery` | Send recovery code to email |

### Users (Basic Auth)
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/users` | List users (pagination, search) |
| POST | `/users` | Create user |
| GET | `/users/{id}` | Get by ID |
| DELETE | `/users/{id}` | Delete |

### Blogs
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/blogs` | List blogs (pagination, name search) |
| POST | `/blogs` | Create blog |
| GET | `/blogs/{id}` | Get by ID |
| PUT | `/blogs/{id}` | Update |
| DELETE | `/blogs/{id}` | Delete |
| GET | `/blogs/{id}/posts` | Blog posts |
| POST | `/blogs/{id}/posts` | Create post in blog |

### Posts
| Method | URL | Description |
|--------|-----|-------------|
| GET | `/posts` | List posts (pagination) |
| POST | `/posts` | Create post |
| GET | `/posts/{id}` | Get by ID |
| PUT | `/posts/{id}` | Update |
| DELETE | `/posts/{id}` | Delete |
