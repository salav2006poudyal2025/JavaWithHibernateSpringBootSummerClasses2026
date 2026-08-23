# MySpringWeb Project Details

## Overview

MySpringWeb is a Java 17 Spring Boot 4.1.0 application created as an Enterprise Web Systems Development project. It demonstrates a layered web application with two interfaces:

- A server-rendered browser interface built with Spring MVC and Thymeleaf.
- A REST API protected by Spring Security and signed JWTs.

The application uses JPA/Hibernate for persistence, a MySQL-compatible database in normal operation, H2 for tests, Gmail SMTP for welcome email, and Cloudinary for external image storage.

## Objectives

The project was built to demonstrate:

- Spring Boot application structure and dependency injection.
- MVC request handling and server-rendered views.
- Service and repository separation.
- JPA entity and relationship mapping.
- BCrypt password hashing.
- HTTP-session browser authentication.
- JWT authentication for REST clients.
- Multipart file processing and two image-storage approaches.
- Email and cloud-service integration.
- Maven, Docker, environment configuration, and Render deployment.

## All Features

### Implemented

✅ User registration with nonblank field checks  
✅ BCrypt password hashing before persistence  
✅ Browser login and logout using an HTTP session  
✅ Browser user listing, editing, and deletion  
✅ JWT login at `/api/auth/login`  
✅ JWT-protected REST user CRUD  
✅ Database image upload using Base64 text  
✅ Cloudinary image upload using a returned secure URL  
✅ Gmail SMTP welcome email  
✅ Asynchronous email execution with `@Async`  
✅ JPA/Hibernate persistence  
✅ H2 test configuration  
✅ Dockerfile with build and runtime stages  
✅ Render deployment descriptor

### Partially Implemented

🟡 Automated testing covers application startup and the email service only.  
🟡 Browser access control is split between Spring Security rules and controller session checks.  
🟡 Cloudinary records have a user relationship, but the current upload path does not assign it.  
🟡 REST controllers return persistence entities rather than dedicated DTOs.

### Planned

🚧 Roles and authorization policies  
🚧 Refresh tokens and token revocation  
🚧 DTO-based API responses and password redaction  
🚧 Comprehensive validation and upload security  
🚧 Database migrations and CI/CD

## Registration Flow

1. The browser submits the signup form to `POST /signup`.
2. The controller checks that username, email, and password are not blank.
3. The service checks for an existing username.
4. The password is encoded with BCrypt.
5. The user is saved through `UserRepository`.
6. An asynchronous welcome email is submitted to `EmailService`.
7. The browser is redirected to `/login`.

The current flow does not enforce database-level username uniqueness and does not consistently validate email format at the controller boundary.

## Login Flow

1. The browser submits username and password to `POST /login`.
2. The controller checks for blank values.
3. `UserServiceImpl` finds the user by username.
4. `PasswordEncoder.matches` compares the submitted password with the stored BCrypt hash.
5. On success, the username is stored in the HTTP session.
6. The browser is redirected to `/home`.
7. Failed authentication returns the login view with an error message.

This is a manual session flow. It does not create a Spring Security authenticated principal for browser users.

## Logout Flow

The browser sends `GET /logout`. The controller invalidates the current HTTP session and redirects to `/login`.

This works in the current application, but a state-changing logout request would be better implemented with a protected POST request.

## JWT Flow

1. A client sends username and password as URL-encoded parameters to `POST /api/auth/login`.
2. The authentication service verifies the credentials with BCrypt.
3. `JWUtil` creates a signed token with the username as subject, issued-at time, and expiration time.
4. The controller returns `{"token":"..."}`.
5. The client sends the token in `Authorization: Bearer <token>`.
6. `JwtAuthenticationFilter` extracts and validates the token before the username-password filter.
7. A valid token creates an authenticated security context with an empty authority list.
8. The protected REST controller handles the request.

The configured expiration is `86400000` milliseconds, approximately 24 hours. The current implementation does not provide roles, refresh tokens, revocation, issuer validation, audience validation, or post-login user-status checks.

## Image Upload Flow

### Database Gallery

1. The browser submits a multipart file to `POST /gallery`.
2. The controller reads the file bytes.
3. The bytes are converted to Base64 text.
4. An `ImageTable` record is created.
5. The current session user is associated when available.
6. The image record is saved through `ImageRepository`.
7. The gallery view displays the stored image data.

`ImageTable.image` is a `@Lob` with a `MEDIUMBLOB` column definition. Base64 increases storage size compared with the original binary file.

### Current Upload Limits

- Maximum file size: `20MB`.
- Maximum request size: `50MB`.

The application does not currently validate MIME types, file signatures, image dimensions, malware, or ownership comprehensively.

## Cloudinary Flow

1. The browser submits a multipart file to `POST /gallery2`.
2. The controller sends the file bytes to Cloudinary.
3. Cloudinary returns a secure HTTPS URL.
4. An `ImageTable2` record stores that URL.
5. The gallery reloads the stored records and renders them.

The current controller does not assign the logged-in user to the `ImageTable2` relationship. Public IDs and deletion operations are not stored or implemented.

## Email System

`EmailService` uses Spring Boot Mail and `JavaMailSender` with Gmail SMTP:

- Host: `smtp.gmail.com`.
- Port: `587`.
- SMTP authentication: enabled.
- STARTTLS: enabled.
- Connection, read, and write timeouts: `10000` milliseconds.

Registration triggers a personalized welcome email after the user is saved. The email operation runs asynchronously. Blank recipients are skipped, and mail failures are logged without rolling back a successful registration.

A Gmail app password should be used instead of a normal account password when required by Google account security.

## Service Layer Explanation

### `UserService`

Defines user operations including `saveUser`, `registerUser`, `findById`, `findByUsername`, `findAllUsers`, `deleteUser`, and `authenticate`.

### `UserServiceImpl`

Implements user business logic:

- Encodes new passwords with BCrypt.
- Avoids encoding values that already begin with `$2`.
- Finds users by username and ID.
- Compares passwords through `PasswordEncoder.matches`.
- Sends registration email after persistence.
- Delegates persistence to repositories.

The service does not enforce database-level username uniqueness and does not catch missing-user errors during every deletion path.

### `EmailService`

Builds and sends registration messages asynchronously through `JavaMailSender`. It logs successful sends, skips blank addresses, and logs failures.

## Controller Layer Explanation

### MVC Controllers

- `MappingClass` renders the initial and next pages.
- `SignupController` handles registration form display and submission.
- `UserController` handles home, edit, update, and delete operations.
- `GalleryController` handles database and Cloudinary image pages and uploads.
- `MailController` renders the mail page after a session check.

### REST Controllers

- `AuthRestController` exposes API login and returns JWTs.
- `RControllerClass` exposes protected connectivity and user CRUD endpoints.

Controllers currently contain some validation and session decisions that could be centralized in a stronger security and validation design.

## Repository Layer Explanation

- `UserRepository` extends `JpaRepository<UserTable, Integer>` and provides `findByUsername`.
- `ImageRepository` extends `JpaRepository<ImageTable, Integer>`.
- `Image2Repository` extends `JpaRepository<ImageTable2, Integer>`.

Repositories provide the persistence operations used by services and controllers. `UserRepository` also contains an unused `existsByUsernameAndPassword` method; comparing raw passwords in a repository would not be appropriate for BCrypt authentication.

## Database Layer Explanation

The application uses Spring Data JPA and Hibernate. Runtime configuration uses the MySQL Connector/J driver and values supplied by environment variables. Hibernate is currently configured with:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Tests use an in-memory H2 database configured in test resources. No Flyway or Liquibase migration system is currently present.

## Security Design

`SecurityConfig` currently:

- Disables CSRF globally.
- Permits `/`, `/login`, `/signup`, static assets, and `/api/auth/**`.
- Requires authentication for `/api/**`.
- Places `JwtAuthenticationFilter` before `UsernamePasswordAuthenticationFilter`.
- Permits other routes at the Spring Security request-rule level.

Browser authentication is implemented separately using a session attribute named `username`. As a result, `/home`, `/deleteUser`, `/editUser`, `/updateUser`, and `/gallery2` are not consistently protected by a session check, while `/gallery` and `/mail` perform controller-level checks.

## Password Handling

During registration, the plain password is passed to a BCrypt password encoder and the resulting hash is persisted. During login, `PasswordEncoder.matches` compares the submitted password with the stored hash. Plain passwords are not intentionally stored.

The REST API currently serializes `UserTable` entities directly in some responses. Because the entity contains a password property, this creates a potential password exposure risk and is a major production limitation.

## Session Authentication

Successful browser login stores the username in the current `HttpSession`. Selected controllers use that value to allow or deny page access. Logout invalidates the session.

This approach demonstrates session handling but is not a complete form-login integration with Spring Security. Session fixation protection, secure cookie settings, timeouts, concurrency controls, and consistent route authorization are not documented as configured features.

## JWT Authentication

JWTs use HMAC signing with HS256. The username is the subject. Issued-at and expiration claims are included. The signing secret comes from `JWT_SECRET`; the configured lifetime is approximately 24 hours.

The filter accepts a Bearer token, validates it with `JWUtil`, and places an authentication object in the security context. Authorities are empty. Invalid token parsing is logged to standard output by the current filter.

## Package Architecture

```text
+ io.herald.MySpringWeb
  + Configuration   SecurityConfig, JwtAuthenticationFilter, JWUtil, CloudinaryConfig
  + Controller      MVC controllers and browser routes
  + Exception       Custom exception and global handler
  + Model           UserTable, ImageTable, ImageTable2
  + RController     REST controllers
  + Repository      Spring Data JPA repositories
  + Service         User and email services
```

## Data Models

### `UserTable`

- `id`: generated integer identifier.
- `username`: user name, marked `@NotBlank`.
- `email`: email value, marked `@NotBlank` and `@Email`.
- `password`: BCrypt password value, marked `@NotBlank`.
- `images`: one-to-many relationship with `ImageTable`.
- `cloudImages`: one-to-many relationship with `ImageTable2`.

### `ImageTable`

- `id`: generated integer identifier.
- `image`: Base64 image content stored as a large object.
- `user`: many-to-one relationship to `UserTable` using `user_id`.

### `ImageTable2`

- `id`: generated integer identifier.
- `imageUrl`: URL returned by Cloudinary.
- `user`: many-to-one relationship to `UserTable` using `user_id`.

## Entity Relationships

```text
UserTable 1 ---- many ImageTable
UserTable 1 ---- many ImageTable2
```

Both user collections use `mappedBy = "user"` and `cascade = CascadeType.ALL`. The database gallery associates the current user when possible. The Cloudinary upload path currently leaves its user field unset.

## Persistence Details

- Repository base type: `JpaRepository`.
- Runtime database: MySQL-compatible database.
- Test database: H2 in-memory database.
- Schema strategy: Hibernate `update`.
- Database driver: MySQL Connector/J.
- Image database content: Base64 text in a `MEDIUMBLOB` column.
- Cloudinary content: remote URL stored in the application database.

## Validation Rules

Implemented validation includes:

- Nonblank checks for signup username, email, and password.
- Nonblank checks for browser login username and password.
- Nonblank checks for profile-update username and email.
- HTML `required` attributes and an email input type.
- Entity annotations `@NotBlank` and `@Email`.

Not currently implemented consistently:

- `@Valid` or `@Validated` at REST controller boundaries.
- Password strength rules.
- Username format and length rules.
- Server-side email format enforcement for all browser paths.
- Multipart MIME, signature, empty-file, and dimension validation.
- Ownership checks for edits, deletes, and image access.

## API Documentation

Base path: `/api`.

API login uses `application/x-www-form-urlencoded` request parameters rather than a JSON request body.

```http
POST /api/auth/login
Content-Type: application/x-www-form-urlencoded

username=alice&password=your-password
```

Successful response:

```json
{
  "token": "jwt-token"
}
```

Protected request header:

```http
Authorization: Bearer <token>
```

## Endpoint Documentation

| Method | Endpoint | Authentication | Current result |
| --- | --- | --- | --- |
| `POST` | `/api/auth/login` | Public | `200` with token or `401` |
| `GET` | `/api/hello` | JWT required | `200` with `Hello World` |
| `GET` | `/api/users` | JWT required | `200` with users or `204` if empty |
| `GET` | `/api/users/{id}` | JWT required | `200` with entity or `404` |
| `POST` | `/api/users` | JWT required | `201` with `Saved Successfully` |
| `PUT` | `/api/users/{id}` | JWT required | `200` with updated entity or `404` |
| `DELETE` | `/api/users/{id}` | JWT required | `204` or `404` |

The API returns JPA entities directly for some operations. This means API output is coupled to persistence models and may expose sensitive or recursive fields.

## Example Requests

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -d "username=alice&password=your-password"
```

### List users

```bash
curl -H "Authorization: Bearer your-jwt-token" \
  http://localhost:8080/api/users
```

### Create a user

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer your-jwt-token" \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","email":"alice@example.com","password":"your-password"}'
```

## Example Responses

### Login success

```json
{
  "token": "eyJ..."
}
```

### Create success

```text
Saved Successfully
```

### Protected greeting

```text
Hello World
```

### Common statuses

- `200 OK`: successful read, update, login, or greeting.
- `201 Created`: REST user creation.
- `204 No Content`: empty user list or successful deletion.
- `401 Unauthorized`: invalid or missing API authentication.
- `404 Not Found`: requested user does not exist.
- `500 Internal Server Error`: unhandled server exception; the current fallback may include the raw exception message.

## Environment Variables

| Variable | Used for |
| --- | --- |
| `DB_URL` | JDBC database URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `MAIL_USERNAME` | Gmail SMTP username |
| `MAIL_PASSWORD` | Gmail SMTP password or app password |
| `JWT_SECRET` | JWT signing secret |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary cloud name |
| `CLOUDINARY_API_KEY` | Cloudinary API key |
| `CLOUDINARY_API_SECRET` | Cloudinary API secret |

Additional application settings include port `8080`, JWT expiration `86400000`, multipart max file size `20MB`, and multipart max request size `50MB`.

## Gmail Configuration

Set `MAIL_USERNAME` and `MAIL_PASSWORD`. The application uses Gmail SMTP on port `587` with authentication and STARTTLS. A Gmail app password is recommended where Google requires one. Email delivery is asynchronous and a mail failure does not undo a saved registration.

## Cloudinary Configuration

Set the Cloudinary cloud name, API key, and API secret environment variables. `CloudinaryConfig` creates the Cloudinary client. The upload flow stores the returned secure URL in `ImageTable2`. The current implementation does not store a Cloudinary public ID or assign the owning user.

## Docker Details

The Dockerfile uses two stages:

1. `maven:3.9-eclipse-temurin-17` copies `pom.xml` and `src`, then runs `mvn clean package -DskipTests`.
2. `eclipse-temurin:17-jre-alpine` receives the generated JAR and starts it with `java -jar app.jar`.

Port `8080` is exposed. A local container can be run with:

```bash
docker build -t myspringweb .
docker run --env-file .env -p 8080:8080 myspringweb
```

The Docker build currently skips tests; CI should verify tests before publishing an image.

## Render Deployment Details

`render.yaml` defines a Docker web service:

- Name: `my-spring-web`.
- Service type: web.
- Environment: Docker.
- Plan: free.
- Branch: `main`.
- Environment values: all nine application variables listed above, configured with `sync: false`.

Render deployment requires the repository to be connected and all environment variables to be supplied in the Render service settings. Secrets should not be committed to the YAML file.

## Testing

Run the test suite with the Maven Wrapper:

```powershell
.\mvnw.cmd test
```

Or on macOS/Linux:

```bash
./mvnw test
```

## Existing Tests

### `MySpringWebApplicationTests`

Loads the Spring application context using the H2 test configuration.

### `EmailServiceTest`

Covers welcome email behavior and blank-recipient handling using mocked mail components.

The current suite does not cover controllers, JWT filters, authorization, repositories, uploads, Cloudinary calls, or complete end-to-end flows.

## Project Limitations

- The project is educational and is not presented as fully production-ready.
- `ddl-auto=update` is used instead of versioned migrations.
- CSRF is disabled globally.
- Browser route protection is inconsistent.
- REST entities are exposed directly.
- Password fields may be exposed through entity serialization.
- REST validation is incomplete.
- Upload validation and ownership enforcement are incomplete.
- Cloudinary ownership is not assigned.
- The Docker build skips tests.
- No CI/CD workflow is included.

## Known Issues

- The unused `existsByUsernameAndPassword` repository method models raw-value comparison and should not be used for authentication.
- The broad fallback exception handling can expose raw exception messages.
- JWT keys use `secret.getBytes()` with the platform default charset.
- Invalid JWT parsing is logged to standard output.
- Browser pages such as `/home` and `/gallery2` are not consistently session-protected.
- Bidirectional entity serialization can create recursion or persistence-model coupling.

## Operational Notes

- Use a long, random `JWT_SECRET` in deployed environments.
- Keep database, SMTP, and Cloudinary secrets in environment configuration.
- Use a Gmail app password rather than a normal password when required.
- Treat Base64 database image storage as suitable for demonstration or small workloads.
- Monitor database, email, Cloudinary, and JWT failures before production use.
- Configure HTTPS and secure deployment settings outside this repository.

## Security Review

### Positive controls

- BCrypt is used for password hashing.
- JWTs are signed and have an expiration time.
- API routes require authentication after login.
- Secrets are externalized through environment variables.
- Multipart request size limits are configured.

### Security gaps

- CSRF is disabled.
- Browser authorization is not centralized.
- API entities can expose password fields.
- Roles and authorities are absent.
- There is no refresh-token rotation or revocation.
- Upload type, content, ownership, and malware checks are absent.
- Error responses may expose internal exception text.
- Logout uses GET.

## Scalability Discussion

The layered architecture provides a reasonable base for a small application. Spring Data repositories and external Cloudinary delivery avoid serving every image from the application database. However, Base64 image storage increases database size and transfer overhead. The current application also has no pagination, filtering, connection-pool tuning documentation, external session store, background job reliability policy, or metrics. These concerns become more important as user and image volume increases.

## Production Readiness Review

Current readiness: suitable for learning, demonstration, and controlled development environments; not yet suitable for unreviewed production workloads.

Before production use, prioritize password redaction, consistent authorization, CSRF and logout design, upload validation, ownership checks, migrations, structured errors, secure headers and cookies, monitoring, backups, dependency scanning, and automated security testing.

## Learning Outcomes

The project provides practical experience with:

- Java 17 and object-oriented application structure.
- Spring Boot auto-configuration and dependency injection.
- MVC controllers and Thymeleaf templates.
- REST controllers and HTTP status handling.
- Spring Security filters and JWTs.
- BCrypt and session authentication.
- JPA entities, repositories, and relationships.
- Multipart files, Base64 data, and external media storage.
- Gmail SMTP and asynchronous services.
- Maven, Docker, environment variables, and Render.
- Unit and application-context testing.

## Lessons Learned

- Authentication mechanisms need a clear boundary between browser sessions and API tokens.
- Password hashing must always be handled through a password encoder, never raw repository comparisons.
- External media storage reduces database pressure but still requires ownership and lifecycle tracking.
- Entity models are convenient internally but should not define a long-term public API contract.
- Environment variables protect credentials only when the deployment platform and local files are also handled carefully.
- Tests should cover security and user workflows, not only application startup.

## Application Workflows

### Browser workflow

```text
Signup form -> SignupController -> UserService -> BCrypt -> UserRepository
           -> Database -> Async EmailService -> Login

Login form -> UserService authentication -> HttpSession -> Home

Authenticated page -> MVC controller -> Service -> Repository -> Thymeleaf view
```

### REST workflow

```text
API login -> AuthRestController -> credential check -> JWUtil -> JWT

Bearer request -> JwtAuthenticationFilter -> SecurityContext
               -> REST controller -> Service -> Repository -> response
```

### Media workflow

```text
Multipart upload -> database Base64 record -> gallery view
Multipart upload -> Cloudinary secure URL -> ImageTable2 -> gallery view
```

## Complete Architecture

```text
+---------------------+       +----------------------+
| Browser              |       | REST client          |
+----------+----------+       +----------+-----------+
           |                             |
           v                             v
+---------------------+       +----------------------+
| MVC controllers     |       | REST controllers     |
| Thymeleaf views     |       | JWT authentication   |
+----------+----------+       +----------+-----------+
           \                             /
            v                           v
              +-----------------------+
              | Service layer        |
              | User and email logic |
              +-----------+-----------+
                          v
              +-----------------------+
              | JPA repositories     |
              +-----------+-----------+
                          v
              +-----------------------+
              | MySQL-compatible DB  |
              | H2 during tests      |
              +-----------------------+

         +----------------+  +----------------+
         | Gmail SMTP     |  | Cloudinary     |
         +----------------+  +----------------+
```

## Deployment Architecture

```text
Git repository -> Render Docker build -> Java 17 runtime
                                      |
                                      +-> MySQL-compatible database
                                      +-> Gmail SMTP
                                      +-> Cloudinary
```

The Docker image contains the packaged Spring Boot JAR. Runtime configuration is supplied through environment variables rather than source-controlled secrets.

## Feature Matrix

| Area | Status | Notes |
| --- | --- | --- |
| Spring Boot application | ✅ Implemented | Context loads successfully |
| Thymeleaf web interface | ✅ Implemented | Server-rendered pages and forms |
| User registration | ✅ Implemented | Blank checks and BCrypt |
| Browser login/logout | ✅ Implemented | Manual HTTP session flow |
| Browser authorization | 🟡 Partially Implemented | Not consistent across routes |
| User CRUD | ✅ Implemented | MVC and REST paths |
| JWT authentication | ✅ Implemented | HS256, approximately 24 hours |
| API authorization | ✅ Implemented | `/api/**` requires authentication |
| Database image gallery | ✅ Implemented | Base64 and `MEDIUMBLOB` |
| Cloudinary gallery | ✅ Implemented | URL persistence |
| Cloudinary ownership | 🟡 Partially Implemented | User is not assigned on upload |
| Gmail welcome email | ✅ Implemented | Async SMTP path |
| Automated testing | 🟡 Partially Implemented | Three current tests |
| Docker | ✅ Implemented | Tests skipped in image build |
| Render descriptor | ✅ Implemented | Environment values not committed |
| DTO API contract | 🚧 Planned | Entities currently exposed |
| Role authorization | 🚧 Planned | No roles or authorities |
| Refresh tokens | 🚧 Planned | Not implemented |
| Database migrations | 🚧 Planned | `ddl-auto=update` remains |
| CI/CD | 🚧 Planned | No workflow present |

## Changelog Summary

- Reorganized project documentation into a concise public README and this detailed technical reference.
- Documented implemented behavior separately from limitations and planned work.
- Recorded current MVC routes, REST endpoints, environment configuration, tests, Docker setup, and Render setup.

## Future Roadmap

### Security Improvements

🚧 Centralize browser authorization.  
🚧 Reconsider CSRF and use POST logout.  
🚧 Add roles, secure cookies, session controls, issuer/audience checks, and token revocation.

### API Improvements

🚧 Add request and response DTOs.  
🚧 Remove password fields from responses.  
🚧 Apply validation, consistent error bodies, pagination, and API versioning.

### Testing Improvements

🚧 Add controller, JWT, authorization, repository, upload, Cloudinary, and end-to-end tests.

### Reliability Improvements

🚧 Add migrations, structured logging, health checks, metrics, retries, timeouts, and correlation IDs.

### DevOps Improvements

🚧 Run tests and security scans in CI before image publication.  
🚧 Automate dependency and container vulnerability checks.

### Infrastructure Improvements

🚧 Add backups, restore procedures, HTTPS guidance, scaling guidance, and external session storage for multiple instances.

### User Experience Improvements

🚧 Improve validation messages, responsive behavior, upload previews, upload errors, and image lifecycle controls.

### Long-Term Improvements

🚧 Add account verification, password reset, audit-friendly administration, quotas, thumbnails, and production media lifecycle management.
