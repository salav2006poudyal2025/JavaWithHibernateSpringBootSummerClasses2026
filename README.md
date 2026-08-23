# MySpringWeb

![Java 17](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Spring Boot 4.1.0](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen?style=for-the-badge&logo=springboot)
![Spring Security](https://img.shields.io/badge/Security-Spring%20Security-blue?style=for-the-badge&logo=springsecurity)
![Docker](https://img.shields.io/badge/Docker-Supported-2496ED?style=for-the-badge&logo=docker)

## Overview

MySpringWeb is an educational full-stack Spring Boot application built to demonstrate a layered enterprise web system. It combines a Thymeleaf web interface with a JWT-protected REST API and integrates JPA/Hibernate, MySQL-compatible persistence, Gmail SMTP, Cloudinary, Docker, and Render.

## Key Features

- User registration with field checks and BCrypt password hashing
- Browser login and logout with HTTP sessions
- User listing, editing, and deletion through the web interface
- JWT login and protected REST user CRUD endpoints
- Database image uploads stored as Base64 data
- Cloudinary uploads with stored secure URLs
- Asynchronous welcome emails through Gmail SMTP
- H2-backed automated tests
- Docker image and Render deployment configuration

## Architecture Overview

```text
Browser / API Client
        |
        +--> MVC Controllers --> Services --> JPA Repositories --> Database
        |          |                 |                |
        |       Thymeleaf       Email / Uploads    MySQL or H2
        |
        +--> REST Controllers --> JWT Filter --> Services
                                      |
                             Spring Security
```

## Technology Stack

| Area | Technology |
| --- | --- |
| Backend | Java 17, Spring Boot 4.1.0, Spring MVC |
| Frontend | Thymeleaf, HTML, CSS |
| Database | MySQL-compatible database; H2 for tests |
| Persistence | Spring Data JPA and Hibernate |
| Security | Spring Security, BCrypt, JJWT 0.11.5 |
| Cloud services | Gmail SMTP, Cloudinary |
| DevOps | Maven, Docker, Render |
| Testing | JUnit, Spring Boot Test, Mockito |

## Project Structure

```text
src/
├── main/
│   ├── java/io/herald/MySpringWeb/
│   │   ├── Configuration/   Security, JWT, and Cloudinary configuration
│   │   ├── Controller/      Browser-facing MVC controllers
│   │   ├── Exception/       Exceptions and global handling
│   │   ├── Model/           JPA entities
│   │   ├── RController/     REST controllers
│   │   ├── Repository/      Spring Data repositories
│   │   └── Service/         Business logic
│   └── resources/
│       ├── application.properties
│       ├── static/css/
│       └── templates/
└── test/
    ├── java/
    └── resources/
```

## Data Model

- `UserTable` stores the user ID, username, email, BCrypt password, and image relationships.
- `ImageTable` stores Base64 image content in a `MEDIUMBLOB` column and belongs to a user.
- `ImageTable2` stores a Cloudinary image URL and has a user relationship that is not populated by the current upload flow.
- User-to-image relationships are one-to-many with cascade delete configured on the user entity.

## REST API

The API is available under `/api`. Login is public; the other endpoints require `Authorization: Bearer <token>`.

| Method | Endpoint | Status |
| --- | --- | --- |
| `POST` | `/api/auth/login` | `200` with a token, or `401` |
| `GET` | `/api/hello` | `200` |
| `GET` | `/api/users` | `200` or `204` |
| `GET` | `/api/users/{id}` | `200` or `404` |
| `POST` | `/api/users` | `201` |
| `PUT` | `/api/users/{id}` | `200` or `404` |
| `DELETE` | `/api/users/{id}` | `204` or `404` |

## Environment Variables

`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `MAIL_USERNAME`, `MAIL_PASSWORD`, `JWT_SECRET`, `CLOUDINARY_CLOUD_NAME`, `CLOUDINARY_API_KEY`, and `CLOUDINARY_API_SECRET` are used by the application. Keep real credentials outside source control.

## Installation and Local Running

Requirements: JDK 17, a MySQL-compatible database, and Maven or the included Maven Wrapper.

Set the environment variables, then run:

```powershell
.\mvnw.cmd spring-boot:run
```

The application starts at `http://localhost:8080`.

Run tests with:

```powershell
.\mvnw.cmd test
```

## Docker

```bash
docker build -t myspringweb .
docker run --env-file .env -p 8080:8080 myspringweb
```

The Docker build uses a Maven build stage and a Java 17 runtime stage. It currently skips tests during image creation.

## Deployment

The repository includes [render.yaml](render.yaml) for a Docker-based Render web service. Configure every required environment variable in Render before deployment. Do not put secrets in `render.yaml`.

## Screenshots

Add screenshots of the main pages here when publishing the project:

| Page | Screenshot |
| --- | --- |
| Login | `docs/screenshots/login.png` |
| User home | `docs/screenshots/home.png` |
| Database gallery | `docs/screenshots/gallery.png` |
| Cloudinary gallery | `docs/screenshots/gallery2.png` |

## Current Status

### Completed

✅ Web pages and form workflows
✅ Session-based browser login and logout
✅ BCrypt password hashing
✅ JWT authentication and protected REST API
✅ User CRUD
✅ Database and Cloudinary image upload paths
✅ Gmail SMTP welcome email path
✅ Docker and Render configuration

### Partially Implemented

🟡 Automated testing covers application startup and the email service, but not the full application.
🟡 Browser authorization is handled by a mixture of Spring Security rules and controller session checks.
🟡 Cloudinary image records are saved without assigning the current user.

## Future Improvements

- Add DTOs and redact password fields from REST responses.
- Add consistent validation, error responses, and ownership checks.
- Add roles, refresh tokens, and stronger JWT validation.
- Add upload validation, database migrations, and broader automated tests.
- Add CI/CD and production monitoring.

## Author

**Sulav Poudyal**

Built as an Enterprise Web Systems Development project.

## License

See [LICENSE](LICENSE) for license information.