# MySpringWeb

MySpringWeb is a full-stack Spring Boot application that combines a server-rendered web interface with a JWT-protected REST API. It was developed as an Enterprise Web Systems Development project to demonstrate how authentication, persistence, validation, email delivery, image storage, containerization, and cloud deployment fit together in a layered Java application.

The application currently supports:

- Browser-based user registration, login, logout, and user administration
- BCrypt password hashing and credential verification
- A session-based browser workflow
- JWT creation and Bearer-token authentication for REST endpoints
- CRUD operations for users through both the web interface and REST API
- A database-backed image gallery that stores Base64 image data
- A Cloudinary-backed image gallery that stores secure image URLs
- Asynchronous registration emails through Gmail SMTP
- JPA/Hibernate persistence with a MySQL-compatible database
- Thymeleaf templates and static CSS for the web interface
- Docker packaging and Render deployment configuration
- Automated tests for application startup and email service behavior

> **Project status:** The core application functionality is implemented. The production-hardening items listed in [Future Scope](#future-scope) are intentionally documented as next steps, not as completed features.

## Table of Contents

- [Project Overview](#project-overview)
- [Objectives](#objectives)
- [Implemented Features](#implemented-features)
- [Application Workflows](#application-workflows)
- [Architecture](#architecture)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [Data Model](#data-model)
- [Web Application Routes](#web-application-routes)
- [REST API](#rest-api)
- [Security Design](#security-design)
- [Configuration](#configuration)
- [Local Development Setup](#local-development-setup)
- [Running the Application](#running-the-application)
- [Testing](#testing)
- [Docker](#docker)
- [Render Deployment](#render-deployment)
- [Operational Notes and Limitations](#operational-notes-and-limitations)
- [Future Scope](#future-scope)
- [Learning Outcomes](#learning-outcomes)
- [Author](#author)

## Project Overview

MySpringWeb demonstrates a conventional layered/N-tier architecture:

1. A browser or API client sends a request.
2. A controller receives the request and selects the appropriate page or API response.
3. Services perform authentication, registration, password handling, email triggering, and business operations.
4. Spring Data repositories communicate with the database.
5. Thymeleaf renders browser pages, while REST controllers return HTTP responses and JSON data.

The project intentionally contains two complementary interfaces:

- **Web interface:** server-rendered Thymeleaf pages for people using the application in a browser.
- **REST interface:** JSON-oriented user operations protected with JWT authentication for tools such as Postman or another client application.

It also contains two image-storage demonstrations:

- `ImageTable` stores encoded image content in the database.
- `ImageTable2` stores URLs returned by Cloudinary.

This makes the application useful as both a working web system and a practical comparison of local database storage versus managed cloud media storage.

## Objectives

The project was designed to demonstrate:

- How Spring Boot starts and configures an enterprise Java application
- How Spring MVC maps browser requests to controllers and templates
- How Spring Data JPA maps Java entities to relational tables
- How BCrypt protects stored passwords
- How session authentication and stateless JWT authentication can coexist
- How services isolate reusable business logic from controllers
- How email and external cloud services are integrated
- How multipart uploads are accepted and persisted
- How environment variables keep deployment credentials outside source code
- How a Maven project can be packaged into a Docker image and deployed to Render

## Implemented Features

### User registration

- A signup page collects a username, email, and password.
- Required values are validated using Jakarta validation annotations on the user model.
- Duplicate usernames are rejected before a new user is saved.
- Passwords are encoded with BCrypt before persistence.
- A welcome email is triggered after registration.
- The email operation is asynchronous, so a mail delivery problem does not have to block the registration flow.
- After successful registration, the browser is redirected to the login page.

### Browser login and logout

- The login page accepts a username and password.
- Credentials are checked against the stored BCrypt password hash.
- On successful login, the username is stored in the HTTP session.
- Successful login redirects the user to the home page.
- Logout invalidates the current HTTP session.

### User administration

The browser interface supports:

- Viewing all users on the home page
- Opening a user for editing
- Updating a user's username and email
- Keeping the active session username synchronized after a username update
- Deleting a user by ID

The REST interface exposes corresponding create, read, update, and delete operations.

### Database image gallery

The first gallery demonstrates storing image content in the application database:

1. A multipart file is submitted from the gallery page.
2. The file is read and encoded as Base64 text.
3. The encoded value is stored in `ImageTable`.
4. The image is associated with the current session user when one is available.
5. The gallery loads stored records and displays the image data in the browser.

This approach is useful for demonstrating persistence, but Base64 increases the stored size and is not ideal for a large production media library.

### Cloudinary image gallery

The second gallery demonstrates managed external media storage:

1. A multipart file is submitted from the Cloudinary gallery page.
2. The controller uploads the file to Cloudinary.
3. Cloudinary returns a `secure_url`.
4. The URL is stored in `ImageTable2`.
5. The gallery displays the stored remote image URL.

### Email integration

- Gmail SMTP is configured through Spring Boot Mail.
- SMTP uses port `587`, authentication, and STARTTLS.
- Registration sends a personalized welcome message.
- The email service is asynchronous through `@Async`.
- Email failures are logged by the service rather than being allowed to undo the saved registration.

### REST API

- REST login returns a signed JWT when credentials are valid.
- The JWT contains the username as its subject.
- A request filter reads `Authorization: Bearer <token>` headers.
- Protected user endpoints require authentication.
- Controllers use `ResponseEntity` and HTTP status codes for API responses.
- Missing users are converted into `404 Not Found` responses.
- Validation failures are converted into `400 Bad Request` responses where validation is applied.

### Deployment support

- The project includes Maven Wrapper scripts for repeatable Maven execution.
- The Dockerfile uses a multi-stage build.
- The build stage uses Maven with Eclipse Temurin Java 17.
- The runtime stage uses a smaller Eclipse Temurin Java 17 JRE Alpine image.
- Render is configured as a Docker web service on the `main` branch.

## Application Workflows

### Registration workflow

```text
Browser submits signup form
        |
        v
SignupController validates request
        |
        v
UserService checks duplicate username
        |
        v
PasswordEncoder creates BCrypt hash
        |
        v
UserRepository saves UserTable
        |
        v
EmailService sends welcome email asynchronously
        |
        v
Browser redirects to /login
```

### Browser authentication workflow

```text
POST /login
        |
        v
Look up username
        |
        v
Compare submitted password with BCrypt hash
        |
        v
Store username in HttpSession
        |
        v
Redirect to /home
```

### JWT workflow

```text
POST /api/auth/login
        |
        v
Validate username and password
        |
        v
Generate signed JWT
        |
        v
Return token to API client
        |
        v
Client sends Authorization: Bearer <token>
        |
        v
JwtAuthenticationFilter validates token
        |
        v
Protected controller is allowed to execute
```

### Image workflows

```text
Database gallery:
multipart file -> Base64 content -> ImageTable -> browser gallery

Cloudinary gallery:
multipart file -> Cloudinary -> secure_url -> ImageTable2 -> browser gallery
```

## Architecture

```text
                         Browser / Postman
                                  |
                 +----------------+----------------+
                 |                                 |
          Thymeleaf web flow                 REST API flow
                 |                                 |
                 v                                 v
           MVC Controllers                 REST Controllers
                 |                                 |
                 +----------------+----------------+
                                  v
                            Service Layer
                 authentication, users, email, uploads
                                  |
                                  v
                         Spring Data Repositories
                                  |
                                  v
                    MySQL-compatible relational database

                 External integrations:
                 Gmail SMTP       Cloudinary
```

### Configuration package

Contains security and external-service configuration:

- `SecurityConfig` configures Spring Security and the password encoder.
- `JwtAuthenticationFilter` processes Bearer tokens.
- `JWUtil` creates and validates JWTs.
- `CloudinaryConfig` creates the Cloudinary client.

### Controller package

Contains browser-facing MVC controllers for page rendering, forms, user administration, galleries, and mail-related pages.

### RController package

Contains REST controllers for authentication and user CRUD operations.

### Service package

Contains reusable application logic:

- `UserService` defines user operations.
- `UserServiceImpl` implements registration, lookup, authentication, hashing, and persistence behavior.
- `EmailService` composes and sends welcome email messages asynchronously.

### Repository package

Contains Spring Data JPA repositories for users and both image entities. They provide persistence access without requiring handwritten SQL for the current operations.

### Exception package

Contains the custom `UserNotFoundException` and `GlobalExceptionHandler`, which translates common failures into HTTP responses.

## Technology Stack

| Area | Technology | Purpose |
|---|---|---|
| Language | Java 17 | Application implementation |
| Framework | Spring Boot 4.1.0 | Application startup and auto-configuration |
| Web | Spring MVC | HTTP request mapping and web controllers |
| Views | Thymeleaf | Server-rendered HTML templates |
| Security | Spring Security | Request security and password encoding |
| Passwords | BCrypt | One-way password hashing |
| Tokens | JJWT 0.11.5 | JWT creation and parsing |
| Persistence | Spring Data JPA and Hibernate | Object-relational mapping |
| Database driver | MySQL Connector/J | MySQL-compatible database connectivity |
| Test database | H2 | Test-scope database dependency |
| Validation | Spring Boot Starter Validation | Jakarta validation support |
| Email | Spring Boot Starter Mail | Gmail SMTP integration |
| Media | Cloudinary HTTP5 client 2.4.0 | External image storage |
| Frontend | HTML, CSS, Thymeleaf | Browser user interface |
| Build | Maven and Maven Wrapper | Dependency management and packaging |
| Packaging | Docker | Container image creation |
| Deployment | Render | Cloud web-service deployment |
| Testing tool | Postman | Manual REST API testing |

## Project Structure

```text
MySpringWeb/
├── pom.xml
├── mvnw
├── mvnw.cmd
├── Dockerfile
├── render.yaml
├── README.md
├── LICENSE
├── To_Do.txt
├── Test.java                         # Standalone root utility, not the app test suite
└── src/
    ├── main/
    │   ├── java/io/herald/MySpringWeb/
    │   │   ├── MySpringWebApplication.java
    │   │   ├── Configuration/
    │   │   ├── Controller/
    │   │   ├── Exception/
    │   │   ├── Model/
    │   │   ├── RController/
    │   │   ├── Repository/
    │   │   └── Service/
    │   └── resources/
    │       ├── application.properties
    │       ├── static/css/styles.css
    │       └── templates/
    └── test/
        ├── java/io/herald/MySpringWeb/
        └── resources/application.properties
```

## Data Model

### `UserTable`

Stores application users:

- Generated integer ID
- Username
- Email address
- BCrypt-encoded password
- One-to-many relationship with locally stored images
- One-to-many relationship with Cloudinary image records

The model includes `@NotBlank` and `@Email` constraints for appropriate fields. The user relationships are configured with cascading delete behavior.

### `ImageTable`

Represents database-backed image storage:

- Generated image ID
- Base64 image content stored as a string in a `MEDIUMBLOB` column
- Many-to-one relationship to `UserTable`

### `ImageTable2`

Represents Cloudinary-backed image storage:

- Generated image ID
- Cloudinary image URL
- Many-to-one relationship to `UserTable`

The current Cloudinary upload path saves the URL but does not populate the user relationship. This is recorded as a future data-ownership improvement below.

### Persistence behavior

- Spring Data repositories handle database access.
- Hibernate is configured with `spring.jpa.hibernate.ddl-auto=update`.
- The production configuration uses a MySQL JDBC driver and environment-provided connection details.
- H2 is included for tests, not as the production database.

## Web Application Routes

The following routes are implemented by the browser-facing MVC controllers.

| Method | Route | Behavior |
|---|---|---|
| `GET` | `/` | Renders the first page |
| `GET` | `/nextPage` | Renders the next page |
| `GET` | `/login` | Renders the login form |
| `POST` | `/login` | Authenticates the user and stores the username in the session |
| `GET` | `/signup` | Renders the signup form |
| `POST` | `/signup` | Validates and registers a user, then triggers a welcome email |
| `GET` | `/home` | Displays users |
| `GET` | `/logout` | Invalidates the HTTP session |
| `POST` | `/deleteUser` | Deletes a user by ID |
| `POST` | `/editUser` | Loads a user for editing |
| `POST` | `/updateUser` | Updates username and email |
| `GET` | `/gallery` | Displays database-backed images |
| `POST` | `/gallery` | Stores an uploaded image as Base64 database content |
| `GET` | `/gallery2` | Displays Cloudinary image URLs |
| `POST` | `/gallery2` | Uploads an image to Cloudinary and saves its URL |
| `GET` | `/mail` | Renders the mail page after a session check |

The browser UI is implemented with templates in `src/main/resources/templates` and shared styling in `src/main/resources/static/css/styles.css`.

## REST API

The REST API is available under `/api`.

### API authentication

#### Login

```http
POST /api/auth/login
Content-Type: application/x-www-form-urlencoded

username=alice&password=your-password
```

The current controller accepts `username` and `password` as request parameters. A successful request returns a token:

```json
{
  "token": "jwt-token"
}
```

Invalid credentials return `401 Unauthorized`.

### Authentication header

Send the token with protected requests:

```http
Authorization: Bearer <token>
```

### Available endpoints

| Method | Endpoint | Authentication | Purpose |
|---|---|---|---|
| `POST` | `/api/auth/login` | Public | Authenticate and receive a JWT |
| `GET` | `/api/hello` | Required | Protected API connectivity check |
| `GET` | `/api/users` | Required | Retrieve all users |
| `GET` | `/api/users/{id}` | Required | Retrieve one user |
| `POST` | `/api/users` | Required | Create a user |
| `PUT` | `/api/users/{id}` | Required | Update a user |
| `DELETE` | `/api/users/{id}` | Required | Delete a user |

### Example protected request

```bash
curl -H "Authorization: Bearer <token>" http://localhost:8080/api/users
```

### Current API response behavior

The current API uses the application entity model directly in several responses. This makes the API straightforward for demonstration and testing, but it is not yet an ideal public contract. DTOs, password redaction, explicit request models, and consistent error bodies are future improvements.

## Security Design

### Password security

Passwords are never intended to be stored as plain text. During registration, `UserServiceImpl` encodes the supplied password with BCrypt. During login, the supplied password is compared with the stored hash using `PasswordEncoder.matches`.

### Browser sessions

The browser login flow stores the authenticated username in the HTTP session. Selected controllers check that session value before allowing page access or user actions.

### JWT authentication

- JWTs are signed with the configured secret.
- HS256 is used by the JWT utility.
- The username is stored as the token subject.
- The configured expiration is `86,400,000` milliseconds, equivalent to 24 hours.
- `JwtAuthenticationFilter` reads Bearer tokens and establishes authentication for protected API requests.
- `/api/**` requires authentication through Spring Security.

### Public routes

The security configuration explicitly permits the home entry route, login and signup pages, static resources, and `/api/auth/**`. API routes outside authentication are protected. Some browser pages depend on controller-level session checks rather than a complete Spring Security form-login flow.

## Configuration

The application reads secrets and deployment-specific values from environment variables. The required variables are:

| Variable | Purpose |
|---|---|
| `DB_URL` | JDBC connection URL |
| `DB_USERNAME` | Database username |
| `DB_PASSWORD` | Database password |
| `MAIL_USERNAME` | Gmail account used by SMTP |
| `MAIL_PASSWORD` | Gmail app password or SMTP credential |
| `JWT_SECRET` | Secret used to sign JWTs |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary cloud name |
| `CLOUDINARY_API_KEY` | Cloudinary API key |
| `CLOUDINARY_API_SECRET` | Cloudinary API secret |

Additional application settings currently include:

```properties
server.port=8080
spring.jpa.hibernate.ddl-auto=update
spring.mail.host=smtp.gmail.com
spring.mail.port=587
jwt.expiration=86400000
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=50MB
```

### Gmail requirements

1. Enable two-factor authentication for the Gmail account.
2. Create a Gmail app password.
3. Set `MAIL_USERNAME` to the sender address.
4. Set `MAIL_PASSWORD` to the app password.
5. Register a test user and verify delivery.

### Cloudinary requirements

1. Create a Cloudinary account.
2. Obtain the cloud name, API key, and API secret.
3. Export the three Cloudinary variables.
4. Start the application.
5. Upload a test image through `/gallery2`.

Keep all credentials out of source control. Do not commit real database passwords, Gmail credentials, JWT secrets, or Cloudinary secrets.

## Local Development Setup

### Prerequisites

- Java Development Kit 17
- Git
- A MySQL-compatible database, such as MySQL or TiDB
- Maven, or use the included Maven Wrapper
- Docker, optional for container execution
- Postman, optional for manual API testing
- Gmail SMTP credentials, required for working email delivery
- Cloudinary credentials, required for the Cloudinary gallery

### Clone the project

```bash
git clone <your-repository-url>
cd MySpringWeb
```

### Configure the environment

Set the variables listed in [Configuration](#configuration) in the shell or IDE run configuration. The application does not provide safe production defaults for the database, email, JWT, or Cloudinary credentials.

On Windows PowerShell, variables can be set for the current terminal session like this:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/myspringweb"
$env:DB_USERNAME="your-database-user"
$env:DB_PASSWORD="your-database-password"
$env:MAIL_USERNAME="your-email@gmail.com"
$env:MAIL_PASSWORD="your-gmail-app-password"
$env:JWT_SECRET="replace-with-a-long-random-secret"
$env:CLOUDINARY_CLOUD_NAME="your-cloud-name"
$env:CLOUDINARY_API_KEY="your-api-key"
$env:CLOUDINARY_API_SECRET="your-api-secret"
```

Use a database URL appropriate for the database provider and TLS requirements in your environment.

## Running the Application

Using the Maven Wrapper on Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Using Maven directly:

```bash
mvn spring-boot:run
```

The application listens on:

```text
http://localhost:8080
```

Useful starting points include:

- `http://localhost:8080/` for the first page
- `http://localhost:8080/login` for browser login
- `http://localhost:8080/signup` for registration
- `http://localhost:8080/gallery` for database image storage
- `http://localhost:8080/gallery2` for Cloudinary image storage

## Testing

Run the test suite with the Maven Wrapper:

```powershell
.\mvnw.cmd test
```

The current visible tests include:

- `MySpringWebApplicationTests`, which verifies that the Spring application context loads.
- `EmailServiceTest`, which tests welcome email composition and blank-recipient behavior.

Manual REST testing can be performed in Postman:

1. Submit `POST /api/auth/login` with `username` and `password` request parameters.
2. Copy the returned token.
3. Add `Authorization: Bearer <token>` to protected requests.
4. Exercise `GET`, `POST`, `PUT`, and `DELETE` operations under `/api/users`.

The repository does not currently show comprehensive controller, JWT, authorization, repository, upload, or end-to-end tests. Those are included in the future testing roadmap.

## Docker

The Dockerfile uses a two-stage build:

1. The build stage uses `maven:3.9-eclipse-temurin-17`, copies `pom.xml` and `src`, and packages the application with tests skipped.
2. The runtime stage uses `eclipse-temurin:17-jre-alpine`, copies the generated JAR, exposes port `8080`, and starts the application with `java -jar app.jar`.

Build and run locally:

```bash
docker build -t myspringweb .
docker run --env-file .env -p 8080:8080 myspringweb
```

The `.env` file should remain local and should never contain values that are committed to the repository.

## Render Deployment

`render.yaml` defines a Docker web service named `my-spring-web`:

- Environment: Docker
- Plan: Free
- Branch: `main`
- Application port: `8080`
- Required environment variables: database, mail, JWT, and Cloudinary settings

Typical deployment process:

1. Push the repository to GitHub.
2. Create or connect a Render Blueprint or web service.
3. Confirm the service uses the `main` branch.
4. Add every required environment variable in Render.
5. Deploy the service.
6. Check the deployment logs for database, mail, and external-service configuration errors.
7. Verify the public web routes and authenticate against the REST API.

Do not put secrets in `render.yaml`; its environment variables are configured with `sync: false` so their values can be entered securely in the Render dashboard.

## Operational Notes and Limitations

The following points describe the current implementation honestly and define the boundary between the working demonstration and production readiness:

- The Maven project currently declares Spring Boot `4.1.0`; older references to Spring Boot 3.x should not be used to describe the current build.
- Production database properties use the MySQL JDBC driver. TiDB may work when configured through its MySQL-compatible connection, but there is no separate TiDB configuration file.
- `spring.jpa.hibernate.ddl-auto=update` is convenient for development, but schema migrations should be used for controlled production changes.
- CSRF is disabled globally. This should be reconsidered, especially for browser-based state-changing requests.
- Some browser routes rely on manual session checks, while the Spring Security configuration permits more routes than a fully hardened application would.
- Logout currently uses `GET /logout`; a state-changing logout action should use a safer, deliberate design.
- File uploads have multipart size limits, but there is no complete MIME-type, content-signature, ownership, or malware validation pipeline.
- Base64 database storage increases image size and can approach database column limits for large uploads.
- Cloudinary image records are currently saved without assigning the owning user relationship.
- The REST API returns entity objects in places, which can expose fields such as the password property or create serialization coupling with persistence relationships.
- REST request bodies are not consistently annotated with `@Valid` at the controller boundary.
- JWT authentication does not currently implement roles, refresh tokens, revocation, or a user-existence check after token validation.
- The global fallback exception handler can include raw exception messages in responses; production APIs should return safe, stable error messages.
- The Docker build skips tests. Tests should run in CI before an image is published.
- No GitHub Actions workflow is currently visible in the repository structure.

## Future Scope

### Security hardening

- Protect every browser route consistently through Spring Security.
- Replace manual session checks with a coherent authentication and authorization model.
- Re-enable and configure CSRF protection for browser workflows where appropriate.
- Use POST for logout and protect state-changing operations.
- Add session fixation protection, secure cookie settings, session timeouts, and concurrency controls.
- Introduce roles such as `USER` and `ADMIN` with method- or route-level authorization.
- Enforce ownership checks before viewing, editing, deleting, or uploading user content.
- Add rate limiting and account lockout or progressive delay for repeated login failures.
- Validate JWT issuer, audience, algorithm, expiration, and user status.
- Add refresh-token rotation and revocation for long-lived client sessions.
- Rotate secrets through deployment configuration without exposing them in logs.

### API quality

- Introduce request and response DTOs instead of exposing JPA entities.
- Never return password fields in API responses.
- Apply `@Valid` to create and update request models.
- Return one consistent JSON error format for validation, authentication, authorization, and server failures.
- Add pagination, filtering, sorting, and search to `GET /api/users`.
- Add API versioning, for example `/api/v1`, before making the API a public contract.
- Add OpenAPI/Swagger documentation and example request collections.
- Use appropriate status codes consistently, including `201 Created` and `204 No Content` where applicable.
- Add optimistic locking or conflict handling for concurrent updates.

### User experience

- Add clear validation messages and accessible form labels.
- Add duplicate-email handling and account verification.
- Add password strength rules, password change, password reset, and account deletion workflows.
- Add pagination or lazy loading to user and image lists.
- Add image previews, upload progress, better upload failure messages, and delete controls.
- Add responsive design improvements and consistent navigation state.
- Add an administrator dashboard with audit-friendly actions.

### Image and media management

- Validate MIME type, file signature, extension, and dimensions.
- Enforce per-user storage quotas and server-side limits.
- Link every Cloudinary record to its owning user.
- Store Cloudinary public IDs so images can be deleted or transformed later.
- Add image deletion and replacement operations.
- Generate thumbnails and responsive image variants.
- Move large or high-volume media fully to object storage rather than database BLOB columns.
- Add malware scanning and metadata stripping where the deployment context requires it.

### Reliability and maintainability

- Replace `ddl-auto=update` with Flyway or Liquibase migrations.
- Add structured logging, correlation IDs, and health/readiness endpoints.
- Add metrics and monitoring for database, email, JWT failures, uploads, and request latency.
- Add centralized configuration profiles for local, test, staging, and production environments.
- Add retry and timeout policies for external email and Cloudinary calls.
- Add database indexes and query review as user and image volume grows.
- Run tests, security checks, and image vulnerability scans in CI before deployment.
- Add dependency update automation and a documented release process.

### Testing roadmap

- Add controller tests for every browser route.
- Add MockMvc tests for login, logout, registration, and session access.
- Add JWT generation, expiration, malformed-token, and protected-route tests.
- Add security tests for unauthorized and forbidden requests.
- Add repository tests using H2 or a containerized MySQL-compatible database.
- Add upload tests for empty, oversized, invalid, and valid files.
- Add Cloudinary and mail integration tests using mocks or test services.
- Add REST contract tests and Postman/Newman automation.
- Add end-to-end tests for registration, login, CRUD, and gallery workflows.
- Ensure the CI pipeline runs the complete test suite before Docker packaging.

### Deployment and scaling

- Add a managed production database with backups, migrations, and connection-pool tuning.
- Add a production-grade email provider or transactional email service.
- Use a CDN and optimized Cloudinary transformations for media delivery.
- Add HTTPS enforcement, custom domains, and secure headers.
- Add horizontal-scaling guidance and external session storage if multiple instances are used.
- Add a CI/CD workflow for build, test, security scanning, and Render deployment.
- Document backup, restore, incident response, and rollback procedures.

## Learning Outcomes

This project demonstrates practical work with:

- Java 17 application development
- Spring Boot application structure
- Spring MVC request mapping
- Thymeleaf server-side rendering
- Spring Security configuration
- BCrypt password hashing
- HTTP sessions
- JWT authentication and filters
- REST API design with `ResponseEntity`
- Spring Data JPA and Hibernate
- Entity relationships and cascading persistence
- Jakarta validation
- Gmail SMTP and asynchronous service execution
- Multipart file uploads
- Database image storage and Cloudinary integration
- Maven dependency management and wrappers
- Docker multi-stage builds
- Environment-based configuration
- Render cloud deployment
- Unit testing and Spring context testing
- Manual API verification with Postman

## Author

**Sulav Poudyal**

Built as an Enterprise Web Systems Development project.

## License

See [LICENSE](LICENSE) for the project license information.
