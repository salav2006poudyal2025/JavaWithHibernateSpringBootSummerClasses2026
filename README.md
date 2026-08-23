# MySpringWeb

## Overview

**MySpringWeb** is a Java 17 Spring Boot web application that demonstrates a complete enterprise-style web system with:

- Spring MVC and Thymeleaf server-rendered pages.
- User registration, login, logout, and CRUD operations.
- BCrypt password hashing.
- HTTP session authentication for the web interface.
- REST APIs for user management.
- JWT-based authentication for protected REST APIs.
- Local database image storage using Base64 data.
- Cloudinary-based image storage.
- Asynchronous registration emails through Gmail SMTP.
- Centralized REST exception handling.
- Jakarta validation.
- JUnit 5 and Mockito tests.
- H2 database support for tests.
- Maven build management.
- Docker containerization.
- Render deployment configuration.
- Environment-variable-based configuration for sensitive credentials.

## Technology Stack

| Area | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4.1.0 |
| Web | Spring MVC / Spring Web MVC |
| Templates | Thymeleaf |
| ORM | Hibernate / Spring Data JPA |
| Database | MySQL-compatible database |
| Testing DB | H2 |
| Security | Spring Security |
| Password Hashing | BCrypt |
| API Authentication | JWT / JJWT |
| Email | Spring Mail / Gmail SMTP |
| Image Storage | Database + Cloudinary |
| Frontend | HTML, CSS, Bootstrap, Font Awesome |
| Build | Maven |
| Testing | JUnit 5 + Mockito |
| Container | Docker |
| Deployment | Render |

## Main Features

### Web Application

- Landing page.
- Signup and login pages.
- User registration.
- Duplicate username checking.
- BCrypt password hashing.
- HTTP session-based login.
- Logout with session invalidation.
- User listing.
- User editing.
- User deletion.
- Database image gallery.
- Cloudinary image gallery.
- Protected mail page.
- Shared Thymeleaf navigation.

### REST API

- `POST /api/auth/login` — REST login and JWT generation.
- `GET /api/hello` — API test endpoint.
- `GET /api/users` — Get all users.
- `GET /api/users/{id}` — Get a user.
- `POST /api/users` — Create a user.
- `PUT /api/users/{id}` — Update a user.
- `DELETE /api/users/{id}` — Delete a user.

The `/api/auth/**` routes are public while the remaining `/api/**` routes require JWT authentication.

## Authentication

The project intentionally demonstrates two authentication approaches:

```text
Browser / Thymeleaf
        |
        v
HTTP Session
```

and:

```text
REST Client
    |
    v
JWT Login
    |
    v
Authorization: Bearer <token>
    |
    v
JwtAuthenticationFilter
```

Passwords are encoded with BCrypt before being stored.

## Image Upload

Two storage approaches are implemented:

```text
/gallery
    Image
      -> bytes
      -> Base64
      -> ImageTable
      -> Database
```

and:

```text
/gallery2
    Image
      -> Cloudinary
      -> secure_url
      -> ImageTable2
      -> Database
```

The first gallery stores image data in the database. The second stores the image externally in Cloudinary and saves its URL.

## Email

After successful registration, `EmailService` sends a welcome email through Gmail SMTP.

The email operation is asynchronous using `@Async`, so registration does not have to wait for the email operation to finish.

## Testing

The project includes:

- Spring application-context testing with `@SpringBootTest`.
- Email-service unit tests using Mockito.
- H2 in-memory database configuration for tests.

The included tests cover successful application context startup, successful personalized registration email sending, and the blank-recipient case.

## Configuration

Sensitive values are supplied through environment variables instead of being hard-coded:

```text
DB_URL
DB_USERNAME
DB_PASSWORD

MAIL_USERNAME
MAIL_PASSWORD

JWT_SECRET

CLOUDINARY_CLOUD_NAME
CLOUDINARY_API_KEY
CLOUDINARY_API_SECRET
```

See `application.properties` for the application's configuration keys.

## Run Locally

### Prerequisites

- Java 17
- Maven or the included Maven Wrapper
- MySQL-compatible database
- Gmail SMTP/App Password if email functionality is required
- Cloudinary account if Cloudinary uploads are required

### Start the application

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

### Run tests

```bash
./mvnw test
```

### Build

```bash
./mvnw clean package
```

## Docker

Build:

```bash
docker build -t myspringweb .
```

Run:

```bash
docker run -p 8080:8080 myspringweb
```

The required environment variables must be supplied to the container.

## Project Structure

```text
src/main/java/io/herald/MySpringWeb/
├── Configuration/
├── Controller/
├── Exception/
├── Model/
├── RController/
├── Repository/
└── Service/

src/main/resources/
├── application.properties
├── static/css/
└── templates/

src/test/
├── java/
└── resources/
```

## Documentation

The detailed implementation documentation is in:

**[PROJECTDETAIL.md](PROJECTDETAIL.md)**

That document explains the project file-by-file and describes the controllers, services, repositories, models, security configuration, JWT flow, image handling, email system, testing, Docker, Render deployment, routes, database relationships, and other implementation details.

## Author

**Sulav Poudyal**
