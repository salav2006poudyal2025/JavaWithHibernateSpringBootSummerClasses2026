# 🚀 MySpringWeb

<p align="center">
  <strong>A full-stack Spring Boot application combining a server-rendered web interface with a JWT-protected REST API.</strong>
</p>

<p align="center">
  Authentication • JPA/Hibernate • MySQL • JWT • Thymeleaf • Email • Cloudinary • Docker • Render
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk" alt="Java 17">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen?style=for-the-badge&logo=springboot" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Spring%20Security-JWT-blue?style=for-the-badge&logo=springsecurity" alt="Spring Security">
  <img src="https://img.shields.io/badge/MySQL-Compatible-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/Docker-Containerized-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker">
  <img src="https://img.shields.io/badge/Render-Deployed-46E3B7?style=for-the-badge&logo=render&logoColor=black" alt="Render">
</p>

<p align="center">
  <a href="#-overview">Overview</a> •
  <a href="#-features">Features</a> •
  <a href="#-architecture">Architecture</a> •
  <a href="#-rest-api">REST API</a> •
  <a href="#-setup">Setup</a> •
  <a href="#-deployment">Deployment</a>
</p>

---

## 📌 Project Status

> 🟢 **Core application functionality is implemented.**
>
> The production-hardening items listed in the [Future Scope](#-future-scope) section are intentionally documented as **future improvements**, not completed features.

---

# 📖 Overview

**MySpringWeb** is a full-stack **Spring Boot** application developed as an **Enterprise Web Systems Development** project.

The project demonstrates how common enterprise application components work together in a layered Java application, including:

* 🔐 Authentication and authorization
* 🔑 BCrypt password hashing
* 🎟️ JWT-based REST authentication
* 🌐 Server-rendered Thymeleaf web pages
* 🗄️ JPA/Hibernate database persistence
* 👤 User CRUD operations
* 🖼️ Database-backed image storage
* ☁️ Cloudinary image storage
* 📧 Gmail SMTP email delivery
* 🐳 Docker containerization
* 🚀 Render deployment
* 🧪 Automated testing
* ⚙️ Environment-based configuration

The application provides **two complementary interfaces**:

| Interface          | Technology             | Purpose                   |
| ------------------ | ---------------------- | ------------------------- |
| 🌐 Web Application | Spring MVC + Thymeleaf | Browser-based interaction |
| 🔌 REST API        | Spring REST + JWT      | Programmatic/API access   |

It also demonstrates **two different image-storage strategies**:

| Storage       | Implementation | Purpose                       |
| ------------- | -------------- | ----------------------------- |
| 🗄️ Database  | `ImageTable`   | Stores Base64 image data      |
| ☁️ Cloudinary | `ImageTable2`  | Stores secure Cloudinary URLs |

This makes MySpringWeb both a working web application and a practical demonstration of different enterprise architecture and storage approaches.

---

# 🎯 Objectives

The project was designed to demonstrate:

* How Spring Boot starts and configures an enterprise Java application
* How Spring MVC maps browser requests to controllers and templates
* How Spring Data JPA maps Java entities to relational tables
* How BCrypt protects stored passwords
* How session authentication and stateless JWT authentication can coexist
* How services isolate reusable business logic from controllers
* How email and external cloud services are integrated
* How multipart uploads are accepted and persisted
* How environment variables keep deployment credentials outside source code
* How a Maven project can be packaged into a Docker image
* How a Dockerized Spring Boot application can be deployed to Render

---

# ✨ Features

## 👤 User Registration

The application provides a browser-based registration workflow.

* Registration using username, email, and password
* Jakarta validation
* Required-field validation
* Duplicate username detection
* BCrypt password hashing
* Passwords are not intentionally stored as plain text
* Automatic welcome email
* Asynchronous email delivery using `@Async`
* Redirect to the login page after successful registration

### Registration Flow

```text
Browser Signup Form
        │
        ▼
SignupController
        │
        ▼
Request Validation
        │
        ▼
UserService
        │
        ▼
Duplicate Username Check
        │
        ▼
BCrypt Password Encoding
        │
        ▼
UserRepository
        │
        ▼
Database
        │
        ▼
Async EmailService
        │
        ▼
Redirect to /login
```

---

## 🔐 Browser Login & Logout

The browser interface supports session-based authentication.

### Login

* Username/password login
* Credential verification
* BCrypt password comparison
* Username stored in `HttpSession`
* Redirect to `/home` after successful authentication

### Logout

* HTTP session invalidation
* Current browser session is cleared

```text
POST /login
     │
     ▼
Find User
     │
     ▼
Compare Password with BCrypt
     │
     ▼
Store Username in HttpSession
     │
     ▼
Redirect to /home
```

---

## 👥 User Administration

The browser interface supports:

* Viewing all users
* Opening a user for editing
* Updating username
* Updating email
* Keeping the active session username synchronized after username changes
* Deleting users by ID

The REST API provides corresponding CRUD functionality.

---

# 🖼️ Database Image Gallery

The first gallery demonstrates storing image data directly in the application database.

### Workflow

```text
Multipart File
      │
      ▼
Read File
      │
      ▼
Base64 Encoding
      │
      ▼
ImageTable
      │
      ▼
Database
      │
      ▼
Browser Gallery
```

The image is:

1. Submitted through a multipart form.
2. Read by the application.
3. Converted into Base64 text.
4. Stored in `ImageTable`.
5. Associated with the current session user when available.
6. Loaded and displayed by the gallery page.

### ⚠️ Storage Consideration

Base64 encoding increases the amount of data stored compared with the original binary file.

Therefore, this approach is useful for:

* Demonstration
* Learning database persistence
* Small applications

But it is **not ideal for a large production media library**.

---

# ☁️ Cloudinary Image Gallery

The second gallery demonstrates external cloud-based media storage using Cloudinary.

### Workflow

```text
Multipart File
      │
      ▼
Cloudinary Upload
      │
      ▼
secure_url
      │
      ▼
ImageTable2
      │
      ▼
Browser Gallery
```

The process is:

1. A multipart file is submitted.
2. The controller uploads the file to Cloudinary.
3. Cloudinary returns a `secure_url`.
4. The URL is stored in `ImageTable2`.
5. The gallery displays the remote image.

### Benefits

Cloudinary provides a more scalable approach for image delivery and can support:

* CDN delivery
* Image transformations
* Image optimization
* Responsive image generation
* External media management

---

# 📧 Email Integration

The project integrates Gmail SMTP through Spring Boot Mail.

### Current Configuration

* Gmail SMTP
* SMTP host: `smtp.gmail.com`
* SMTP port: `587`
* Authentication enabled
* STARTTLS enabled
* Personalized welcome messages
* Asynchronous execution using `@Async`

The email service is designed so that an email delivery failure is logged instead of automatically undoing a successfully saved registration.

---

# 🔌 REST API

The REST API provides user management through HTTP and JSON-oriented endpoints.

It includes:

* JWT login
* Bearer-token authentication
* Protected API endpoints
* User CRUD
* HTTP status handling
* Validation support
* Exception handling

---

# 🔄 Application Workflows

## 📝 Registration Workflow

```text
┌────────────────────────┐
│ Browser Signup Form    │
└────────────┬───────────┘
             │
             ▼
┌────────────────────────┐
│ SignupController       │
│ Request Validation     │
└────────────┬───────────┘
             │
             ▼
┌────────────────────────┐
│ UserService            │
│ Duplicate Check        │
└────────────┬───────────┘
             │
             ▼
┌────────────────────────┐
│ PasswordEncoder        │
│ BCrypt Hash            │
└────────────┬───────────┘
             │
             ▼
┌────────────────────────┐
│ UserRepository         │
│ Save User              │
└────────────┬───────────┘
             │
             ▼
┌────────────────────────┐
│ Async EmailService     │
└────────────┬───────────┘
             │
             ▼
       Redirect /login
```

---

## 🔐 Browser Authentication Workflow

```text
POST /login
     │
     ▼
Look up username
     │
     ▼
Compare submitted password
with BCrypt hash
     │
     ▼
Store username in HttpSession
     │
     ▼
Redirect to /home
```

---

## 🎟️ JWT Workflow

```text
POST /api/auth/login
          │
          ▼
Validate username/password
          │
          ▼
Generate signed JWT
          │
          ▼
Return token
          │
          ▼
Client sends:
Authorization: Bearer <token>
          │
          ▼
JwtAuthenticationFilter
          │
          ▼
Validate JWT
          │
          ▼
Establish authentication
          │
          ▼
Protected Controller
```

---

## 🖼️ Image Workflows

### Database Gallery

```text
multipart file
      ↓
Base64 content
      ↓
ImageTable
      ↓
browser gallery
```

### Cloudinary Gallery

```text
multipart file
      ↓
Cloudinary
      ↓
secure_url
      ↓
ImageTable2
      ↓
browser gallery
```

---

# 🏗️ Architecture

MySpringWeb follows a conventional **layered / N-tier architecture**.

```text
                         ┌──────────────────────┐
                         │   Browser / Postman  │
                         └──────────┬───────────┘
                                    │
                    ┌───────────────┴───────────────┐
                    │                               │
                    ▼                               ▼
           ┌─────────────────┐             ┌─────────────────┐
           │ Thymeleaf Web   │             │    REST API     │
           │      Flow       │             │      Flow       │
           └────────┬────────┘             └────────┬────────┘
                    │                               │
                    ▼                               ▼
           ┌─────────────────┐             ┌─────────────────┐
           │ MVC Controllers │             │ REST Controllers│
           └────────┬────────┘             └────────┬────────┘
                    │                               │
                    └───────────────┬───────────────┘
                                    ▼
                         ┌──────────────────────┐
                         │    Service Layer    │
                         │                      │
                         │ Authentication      │
                         │ Users                │
                         │ Email                │
                         │ Uploads              │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │ Spring Data JPA      │
                         │ Repositories         │
                         └──────────┬───────────┘
                                    │
                                    ▼
                         ┌──────────────────────┐
                         │ MySQL-Compatible DB  │
                         └──────────────────────┘

                    External Integrations
                    ┌──────────┐   ┌────────────┐
                    │  Gmail   │   │ Cloudinary │
                    │   SMTP   │   │   Images   │
                    └──────────┘   └────────────┘
```

---

# 📦 Package Architecture

## ⚙️ Configuration Package

Contains security and external-service configuration.

### `SecurityConfig`

Responsible for Spring Security configuration and password encoding.

### `JwtAuthenticationFilter`

Processes incoming Bearer tokens and establishes API authentication.

### `JWUtil`

Responsible for creating and validating JWT tokens.

### `CloudinaryConfig`

Creates/configures the Cloudinary client.

---

## 🌐 Controller Package

Contains browser-facing MVC controllers responsible for:

* Page rendering
* Forms
* Registration
* Login
* User administration
* Gallery functionality
* Mail-related pages

---

## 🔌 RController Package

Contains REST controllers for:

* API authentication
* User CRUD operations
* Protected API endpoints

---

## 🧠 Service Package

Contains reusable business logic.

### `UserService`

Defines user-related operations.

### `UserServiceImpl`

Implements:

* Registration
* User lookup
* Authentication
* Password hashing
* Persistence behavior

### `EmailService`

Responsible for:

* Creating welcome emails
* Sending emails
* Asynchronous email processing

---

## 🗄️ Repository Package

Contains Spring Data JPA repositories for:

* Users
* Database images
* Cloudinary image records

These repositories provide database access without requiring handwritten SQL for the current operations.

---

## ⚠️ Exception Package

Contains custom application exceptions and centralized exception handling.

### `UserNotFoundException`

Used when a requested user cannot be found.

### `GlobalExceptionHandler`

Converts common application exceptions into appropriate HTTP responses.

---

# 🛠️ Technology Stack

| Area            | Technology                    | Purpose                               |
| --------------- | ----------------------------- | ------------------------------------- |
| ☕ Language      | Java 17                       | Application implementation            |
| 🌱 Framework    | Spring Boot 4.1.0             | Application startup and configuration |
| 🌐 Web          | Spring MVC                    | HTTP request mapping                  |
| 🎨 Views        | Thymeleaf                     | Server-rendered HTML                  |
| 🔐 Security     | Spring Security               | Request security                      |
| 🔑 Passwords    | BCrypt                        | One-way password hashing              |
| 🎟️ Tokens      | JJWT 0.11.5                   | JWT creation and parsing              |
| 🗄️ Persistence | Spring Data JPA               | Data access                           |
| 🧩 ORM          | Hibernate                     | Object-relational mapping             |
| 🐬 Database     | MySQL-compatible DB           | Production persistence                |
| 🧪 Test DB      | H2                            | Test database                         |
| ✅ Validation    | Spring Boot Validation        | Jakarta validation                    |
| 📧 Email        | Spring Boot Mail              | Gmail SMTP                            |
| ☁️ Media        | Cloudinary HTTP5 Client 2.4.0 | External image storage                |
| 🎨 Frontend     | HTML, CSS, Thymeleaf          | Browser UI                            |
| 📦 Build        | Maven                         | Dependency/build management           |
| 🐳 Packaging    | Docker                        | Containerization                      |
| 🚀 Deployment   | Render                        | Cloud deployment                      |
| 🧪 Testing      | JUnit / Spring Test           | Automated testing                     |
| 🔬 API Testing  | Postman                       | Manual REST testing                   |

---

# 📁 Project Structure

```text
MySpringWeb/
│
├── pom.xml
├── mvnw
├── mvnw.cmd
├── Dockerfile
├── render.yaml
├── README.md
├── LICENSE
├── To_Do.txt
├── Test.java
│
└── src/
    │
    ├── main/
    │   │
    │   ├── java/io/herald/MySpringWeb/
    │   │   │
    │   │   ├── MySpringWebApplication.java
    │   │   ├── Configuration/
    │   │   ├── Controller/
    │   │   ├── Exception/
    │   │   ├── Model/
    │   │   ├── RController/
    │   │   ├── Repository/
    │   │   └── Service/
    │   │
    │   └── resources/
    │       ├── application.properties
    │       ├── static/
    │       │   └── css/
    │       │       └── styles.css
    │       └── templates/
    │
    └── test/
        ├── java/io/herald/MySpringWeb/
        └── resources/
            └── application.properties
```

---

# 🗄️ Data Model

## `UserTable`

Stores application users.

### Main fields

* Generated integer ID
* Username
* Email address
* BCrypt-encoded password

### Relationships

```text
UserTable
   │
   ├─────────────── 1:N ────────────────> ImageTable
   │
   └─────────────── 1:N ────────────────> ImageTable2
```

The model uses validation annotations such as:

* `@NotBlank`
* `@Email`

User relationships are configured with cascading delete behavior.

---

## `ImageTable`

Represents database-backed image storage.

| Field         | Description                               |
| ------------- | ----------------------------------------- |
| ID            | Generated image ID                        |
| Image content | Base64 encoded image                      |
| User          | `ManyToOne` relationship with `UserTable` |

The image content is stored as a string in a `MEDIUMBLOB` column.

---

## `ImageTable2`

Represents Cloudinary-backed image storage.

| Field     | Description                               |
| --------- | ----------------------------------------- |
| ID        | Generated image ID                        |
| Image URL | Cloudinary URL                            |
| User      | `ManyToOne` relationship with `UserTable` |

> ⚠️ The current Cloudinary upload path saves the URL but does not populate the user relationship. This is documented as a future data-ownership improvement.

---

## Persistence Behavior

* Spring Data repositories handle database access.
* Hibernate manages ORM behavior.
* `spring.jpa.hibernate.ddl-auto=update` is currently used.
* Production configuration uses a MySQL JDBC driver.
* Database connection values are supplied through environment variables.
* H2 is included for tests rather than production.

---

# 🌐 Web Application Routes

The following routes are implemented by the browser-facing MVC controllers.

| Method | Route         | Behavior                                            |
| ------ | ------------- | --------------------------------------------------- |
| `GET`  | `/`           | Renders the first page                              |
| `GET`  | `/nextPage`   | Renders the next page                               |
| `GET`  | `/login`      | Renders login form                                  |
| `POST` | `/login`      | Authenticates user and stores username in session   |
| `GET`  | `/signup`     | Renders signup form                                 |
| `POST` | `/signup`     | Validates/registers user and triggers welcome email |
| `GET`  | `/home`       | Displays users                                      |
| `GET`  | `/logout`     | Invalidates HTTP session                            |
| `POST` | `/deleteUser` | Deletes user by ID                                  |
| `POST` | `/editUser`   | Loads user for editing                              |
| `POST` | `/updateUser` | Updates username and email                          |
| `GET`  | `/gallery`    | Displays database-backed images                     |
| `POST` | `/gallery`    | Stores uploaded image as Base64                     |
| `GET`  | `/gallery2`   | Displays Cloudinary images                          |
| `POST` | `/gallery2`   | Uploads image to Cloudinary                         |
| `GET`  | `/mail`       | Renders mail page after session check               |

The browser UI is implemented using:

```text
src/main/resources/templates/
src/main/resources/static/css/styles.css
```

---

# 🔌 REST API

The REST API is available under:

```text
/api
```

---

## 🔐 API Authentication

### Login

```http
POST /api/auth/login
Content-Type: application/x-www-form-urlencoded

username=alice&password=your-password
```

A successful request returns:

```json
{
  "token": "jwt-token"
}
```

Invalid credentials return:

```text
401 Unauthorized
```

---

## 🎟️ Authentication Header

Protected endpoints require:

```http
Authorization: Bearer <token>
```

---

## 📋 Available Endpoints

| Method   | Endpoint          | Authentication | Purpose                          |
| -------- | ----------------- | -------------- | -------------------------------- |
| `POST`   | `/api/auth/login` | Public         | Authenticate and receive JWT     |
| `GET`    | `/api/hello`      | Required       | Protected API connectivity check |
| `GET`    | `/api/users`      | Required       | Retrieve all users               |
| `GET`    | `/api/users/{id}` | Required       | Retrieve one user                |
| `POST`   | `/api/users`      | Required       | Create a user                    |
| `PUT`    | `/api/users/{id}` | Required       | Update a user                    |
| `DELETE` | `/api/users/{id}` | Required       | Delete a user                    |

---

## 🧪 Example API Request

After obtaining a token:

```bash
curl -H "Authorization: Bearer <token>" \
     http://localhost:8080/api/users
```

---

## 📦 Example User Request

```http
POST /api/users
Authorization: Bearer <token>
Content-Type: application/json
```

Example body:

```json
{
  "username": "alice",
  "email": "alice@example.com",
  "password": "your-password"
}
```

---

## 📌 Current API Response Behavior

The current API uses application entity models directly in several responses.

This makes the API simple for demonstration and testing, but it is **not yet an ideal public API contract**.

Future improvements include:

* DTOs
* Password-field redaction
* Dedicated request models
* Consistent error response bodies
* API versioning
* Pagination

---

# 🔐 Security Design

## 🔑 Password Security

Passwords are not intended to be stored as plain text.

During registration:

```text
Plain Password
      ↓
BCrypt
      ↓
Password Hash
      ↓
Database
```

During login:

```text
Submitted Password
      ↓
PasswordEncoder.matches()
      ↓
Stored BCrypt Hash
      ↓
Authentication Result
```

---

## 🍪 Browser Sessions

The browser login flow stores the authenticated username inside the HTTP session.

Selected controllers check the session value before allowing certain page access or user actions.

---

## 🎟️ JWT Authentication

The application currently uses:

* Signed JWT tokens
* HS256
* Username as JWT subject
* 24-hour token expiration
* Bearer-token authentication
* `JwtAuthenticationFilter`

The configured expiration is:

```text
86,400,000 milliseconds
```

Equivalent to:

```text
24 hours
```

---

## 🌐 Public Routes

The security configuration explicitly permits routes for:

* Home entry
* Login
* Signup
* Static resources
* `/api/auth/**`

The remaining API routes require authentication.

Some browser pages currently depend on controller-level session checks rather than a completely centralized Spring Security form-login architecture.

---

# ⚙️ Configuration

The application uses environment variables for secrets and deployment-specific values.

## 🔐 Environment Variables

| Variable                | Purpose                              |
| ----------------------- | ------------------------------------ |
| `DB_URL`                | JDBC connection URL                  |
| `DB_USERNAME`           | Database username                    |
| `DB_PASSWORD`           | Database password                    |
| `MAIL_USERNAME`         | Gmail account                        |
| `MAIL_PASSWORD`         | Gmail app password / SMTP credential |
| `JWT_SECRET`            | JWT signing secret                   |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary cloud name                |
| `CLOUDINARY_API_KEY`    | Cloudinary API key                   |
| `CLOUDINARY_API_SECRET` | Cloudinary API secret                |

---

## 📝 Application Settings

Important application settings include:

```properties
server.port=8080

spring.jpa.hibernate.ddl-auto=update

spring.mail.host=smtp.gmail.com
spring.mail.port=587

jwt.expiration=86400000

spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=50MB
```

---

# 📧 Gmail Setup

To enable registration emails:

### 1. Enable 2-Step Verification

Enable two-factor authentication on the Gmail account.

### 2. Create an App Password

Create a Gmail app password for SMTP access.

### 3. Configure Credentials

Set:

```text
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-gmail-app-password
```

### 4. Register a User

Create a test account through:

```text
/signup
```

### 5. Verify Delivery

Check the recipient inbox and application logs.

> ⚠️ Do not use your normal Gmail password as the SMTP password when Google requires an app password.

---

# ☁️ Cloudinary Setup

To enable Cloudinary image uploads:

### 1. Create a Cloudinary Account

Create an account and open the dashboard.

### 2. Obtain Credentials

You will need:

```text
Cloud Name
API Key
API Secret
```

### 3. Set Environment Variables

```text
CLOUDINARY_CLOUD_NAME=your-cloud-name
CLOUDINARY_API_KEY=your-api-key
CLOUDINARY_API_SECRET=your-api-secret
```

### 4. Start the Application

Run the application normally.

### 5. Test Uploads

Open:

```text
/gallery2
```

and upload an image.

---

# 🔒 Security of Configuration

Never commit the following to Git:

```text
Database passwords
Gmail passwords
Gmail app passwords
JWT secrets
Cloudinary API secrets
Production environment files
```

Use environment variables or a secure secret-management solution instead.

---

# 💻 Local Development Setup

## 📋 Prerequisites

Before running the project, install:

* ☕ Java Development Kit 17
* 🔧 Git
* 🐬 MySQL-compatible database
* 📦 Maven or Maven Wrapper
* 🐳 Docker — optional
* 🧪 Postman — optional
* 📧 Gmail SMTP credentials
* ☁️ Cloudinary credentials

---

# 📥 Clone the Project

```bash
git clone <your-repository-url>

cd MySpringWeb
```

---

# ⚙️ Configure Environment Variables

Set the variables described in the [Configuration](#-configuration) section.

---

## 🪟 Windows PowerShell

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

Use a database URL appropriate for your database provider and TLS configuration.

---

# ▶️ Running the Application

## Using Maven Wrapper — Windows

```powershell
.\mvnw.cmd spring-boot:run
```

## Using Maven

```bash
mvn spring-boot:run
```

---

# 🌍 Application URL

Once started, the application runs on:

```text
http://localhost:8080
```

---

## 🔗 Useful Pages

| Page                  | URL                              |
| --------------------- | -------------------------------- |
| 🏠 Home               | `http://localhost:8080/`         |
| 🔐 Login              | `http://localhost:8080/login`    |
| 📝 Signup             | `http://localhost:8080/signup`   |
| 🖼️ Database Gallery  | `http://localhost:8080/gallery`  |
| ☁️ Cloudinary Gallery | `http://localhost:8080/gallery2` |
| 📧 Mail Page          | `http://localhost:8080/mail`     |

---

# 🧪 Testing

Run the test suite with the Maven Wrapper.

## Windows

```powershell
.\mvnw.cmd test
```

## Maven

```bash
mvn test
```

---

## Current Tests

The current visible tests include:

### `MySpringWebApplicationTests`

Verifies that the Spring application context loads successfully.

### `EmailServiceTest`

Tests:

* Welcome email composition
* Blank-recipient behavior

---

# 🔬 Manual REST API Testing

Postman can be used for manual API verification.

### Step 1 — Login

Send:

```http
POST /api/auth/login
```

with:

```text
username
password
```

as request parameters.

### Step 2 — Copy Token

Copy the returned JWT.

### Step 3 — Add Authorization

Add:

```http
Authorization: Bearer <token>
```

### Step 4 — Test API

Exercise:

```text
GET
POST
PUT
DELETE
```

under:

```text
/api/users
```

---

## ⚠️ Testing Limitations

The repository does not currently show comprehensive tests for:

* Controllers
* JWT authentication
* Authorization
* Repositories
* Upload functionality
* Cloudinary integration
* End-to-end workflows

These are included in the future testing roadmap.

---

# 🐳 Docker

The project uses a **multi-stage Docker build**.

## Build Stage

The build stage uses:

```text
maven:3.9-eclipse-temurin-17
```

It:

1. Copies `pom.xml`
2. Copies application source
3. Builds the application
4. Packages the JAR

## Runtime Stage

The runtime image uses:

```text
eclipse-temurin:17-jre-alpine
```

The generated JAR is copied into the runtime image.

The application exposes:

```text
8080
```

and starts using:

```bash
java -jar app.jar
```

---

# 🐳 Build Docker Image

```bash
docker build -t myspringweb .
```

---

# ▶️ Run Docker Container

```bash
docker run --env-file .env -p 8080:8080 myspringweb
```

Your `.env` file should remain local.

> ⚠️ Never commit `.env` if it contains real credentials.

Add it to `.gitignore`:

```gitignore
.env
```

---

# 🚀 Render Deployment

The project includes:

```text
render.yaml
```

The configuration defines a Docker web service named:

```text
my-spring-web
```

---

## Render Configuration

| Setting          | Value                  |
| ---------------- | ---------------------- |
| Environment      | Docker                 |
| Plan             | Free                   |
| Branch           | `main`                 |
| Application Port | `8080`                 |
| Database         | Environment configured |
| Email            | Environment configured |
| JWT              | Environment configured |
| Cloudinary       | Environment configured |

---

# ☁️ Render Deployment Steps

### 1. Push to GitHub

Push the project repository to GitHub.

```bash
git add .
git commit -m "Prepare application for deployment"
git push origin main
```

### 2. Create Render Service

Create or connect a Render Blueprint/web service.

### 3. Select Repository

Connect the GitHub repository containing MySpringWeb.

### 4. Confirm Branch

Use:

```text
main
```

### 5. Configure Environment Variables

Add all required variables:

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

### 6. Deploy

Start the deployment.

### 7. Monitor Logs

Check deployment logs for:

* Database connection problems
* Mail configuration errors
* Cloudinary errors
* JWT configuration problems
* Application startup failures

### 8. Verify

Test:

* Public web pages
* Registration
* Login
* User management
* Image galleries
* REST API authentication

> 🔐 Do not put actual secrets directly inside `render.yaml`. Environment variables are configured securely through Render.

---

# 🩺 Operational Notes & Limitations

The following points describe the current implementation honestly and define the boundary between the working demonstration and production readiness.

### Spring Boot Version

The Maven project currently declares:

```text
Spring Boot 4.1.0
```

Older references to Spring Boot 3.x should not be used to describe the current build.

### Database

Production database configuration uses the MySQL JDBC driver.

TiDB may work because of MySQL compatibility, but there is no separate TiDB-specific configuration file.

### Hibernate Schema Management

The project currently uses:

```properties
spring.jpa.hibernate.ddl-auto=update
```

This is convenient during development.

For production, controlled database migrations should be introduced.

Recommended future tools:

* Flyway
* Liquibase

### CSRF

CSRF is currently disabled globally.

This should be reconsidered, especially for browser-based state-changing requests.

### Browser Security

Some browser routes rely on manual session checks while Spring Security configuration handles API authentication.

A fully hardened production application should use a more consistent security architecture.

### Logout

The current implementation uses:

```text
GET /logout
```

A safer state-changing logout design should use an appropriate protected request method such as POST.

### File Uploads

Multipart size limits exist:

```text
Maximum file size: 20MB
Maximum request size: 50MB
```

However, the application does not yet provide a complete:

* MIME-type validation system
* File-signature validation
* Ownership validation
* Malware scanning pipeline

### Base64 Image Storage

Base64 database storage increases image size and can approach database column limits for large files.

### Cloudinary Ownership

Cloudinary image records are currently saved without assigning the owning user relationship.

### REST Entity Exposure

Some REST endpoints return JPA entity objects directly.

This may expose fields such as the password property or create serialization coupling with persistence relationships.

DTOs are recommended for a production API.

### Validation

REST request bodies are not consistently annotated with `@Valid` at the controller boundary.

### JWT

The current JWT implementation does not yet include:

* Roles
* Refresh tokens
* Token revocation
* Full user-status validation after token creation

### Error Handling

The global fallback exception handler may include raw exception messages.

Production APIs should return safe and stable error messages rather than internal exception details.

### Docker Testing

The Docker build currently skips tests.

Tests should run in CI before publishing a Docker image.

### CI/CD

No GitHub Actions workflow is currently visible in the repository structure.

---

# 🔮 Future Scope

## 🔐 Security Hardening

* [ ] Protect every browser route consistently through Spring Security.
* [ ] Replace manual session checks with a coherent authentication and authorization model.
* [ ] Re-enable and configure CSRF protection where appropriate.
* [ ] Use POST for logout and protect state-changing operations.
* [ ] Add session fixation protection.
* [ ] Configure secure cookie settings.
* [ ] Add session timeouts.
* [ ] Add concurrency controls.
* [ ] Introduce roles such as `USER` and `ADMIN`.
* [ ] Add method-level or route-level authorization.
* [ ] Enforce ownership checks for user content.
* [ ] Add rate limiting.
* [ ] Add account lockout or progressive delays for repeated login failures.
* [ ] Validate JWT issuer.
* [ ] Validate JWT audience.
* [ ] Validate JWT algorithm.
* [ ] Validate token expiration.
* [ ] Validate user status after JWT authentication.
* [ ] Add refresh-token rotation.
* [ ] Add refresh-token revocation.
* [ ] Rotate deployment secrets securely.

---

## 🔌 API Quality

* [ ] Introduce request DTOs.
* [ ] Introduce response DTOs.
* [ ] Never return password fields.
* [ ] Apply `@Valid` consistently.
* [ ] Create a consistent JSON error format.
* [ ] Add pagination to `GET /api/users`.
* [ ] Add filtering.
* [ ] Add sorting.
* [ ] Add user search.
* [ ] Introduce API versioning such as `/api/v1`.
* [ ] Add OpenAPI/Swagger documentation.
* [ ] Add example API request collections.
* [ ] Use `201 Created` where appropriate.
* [ ] Use `204 No Content` where appropriate.
* [ ] Add optimistic locking.
* [ ] Add concurrent update conflict handling.

---

## 🎨 User Experience

* [ ] Improve form validation messages.
* [ ] Add accessible form labels.
* [ ] Add duplicate-email handling.
* [ ] Add email/account verification.
* [ ] Add password strength requirements.
* [ ] Add password change.
* [ ] Add password reset.
* [ ] Add account deletion.
* [ ] Add pagination or lazy loading.
* [ ] Add image previews.
* [ ] Add upload progress.
* [ ] Improve upload failure messages.
* [ ] Add image deletion.
* [ ] Improve responsive design.
* [ ] Improve navigation state.
* [ ] Add an administrator dashboard.
* [ ] Add audit-friendly administrative actions.

---

## 🖼️ Image & Media Management

* [ ] Validate MIME types.
* [ ] Validate file signatures.
* [ ] Validate extensions.
* [ ] Validate image dimensions.
* [ ] Enforce per-user storage quotas.
* [ ] Enforce server-side upload limits.
* [ ] Link every Cloudinary record to its owning user.
* [ ] Store Cloudinary public IDs.
* [ ] Add image deletion.
* [ ] Add image replacement.
* [ ] Generate thumbnails.
* [ ] Generate responsive image variants.
* [ ] Move high-volume media fully to object storage.
* [ ] Add malware scanning where required.
* [ ] Strip metadata where appropriate.

---

## 🧱 Reliability & Maintainability

* [ ] Replace `ddl-auto=update` with Flyway or Liquibase.
* [ ] Add structured logging.
* [ ] Add correlation IDs.
* [ ] Add health endpoints.
* [ ] Add readiness endpoints.
* [ ] Add application metrics.
* [ ] Monitor database failures.
* [ ] Monitor email failures.
* [ ] Monitor JWT failures.
* [ ] Monitor upload failures.
* [ ] Monitor request latency.
* [ ] Create local/test/staging/production profiles.
* [ ] Add retry policies for external services.
* [ ] Add timeout policies.
* [ ] Review database indexes.
* [ ] Review database queries as data grows.
* [ ] Add dependency update automation.
* [ ] Add vulnerability scanning.
* [ ] Document release procedures.

---

# 🧪 Testing Roadmap

## Controller Testing

* [ ] Add controller tests for every browser route.
* [ ] Add MockMvc tests for login.
* [ ] Add MockMvc tests for logout.
* [ ] Add registration tests.
* [ ] Add session-access tests.

## Security Testing

* [ ] Test JWT generation.
* [ ] Test JWT expiration.
* [ ] Test malformed JWTs.
* [ ] Test missing JWTs.
* [ ] Test protected routes.
* [ ] Test unauthorized requests.
* [ ] Test forbidden requests.

## Repository Testing

* [ ] Add repository tests using H2.
* [ ] Add integration tests using a containerized MySQL-compatible database.

## Upload Testing

* [ ] Test empty files.
* [ ] Test oversized files.
* [ ] Test invalid file types.
* [ ] Test invalid image content.
* [ ] Test valid uploads.
* [ ] Test upload ownership.

## Integration Testing

* [ ] Mock Cloudinary.
* [ ] Mock email services.
* [ ] Test REST contracts.
* [ ] Automate Postman/Newman tests.

## End-to-End Testing

* [ ] Test registration.
* [ ] Test login.
* [ ] Test user CRUD.
* [ ] Test database gallery.
* [ ] Test Cloudinary gallery.
* [ ] Test logout.
* [ ] Test complete application workflows.

---

# 🚀 Deployment & Scaling Roadmap

* [ ] Use a managed production database.
* [ ] Configure automated database backups.
* [ ] Introduce production migrations.
* [ ] Tune database connection pools.
* [ ] Use a production-grade email provider.
* [ ] Use CDN-based media delivery.
* [ ] Optimize Cloudinary transformations.
* [ ] Enforce HTTPS.
* [ ] Add custom domains.
* [ ] Configure secure HTTP headers.
* [ ] Add horizontal scaling guidance.
* [ ] Add external session storage for multiple instances.
* [ ] Create CI/CD pipeline.
* [ ] Run automated tests before deployment.
* [ ] Run security scanning before deployment.
* [ ] Run Docker image vulnerability scanning.
* [ ] Document backup procedures.
* [ ] Document restore procedures.
* [ ] Document incident response.
* [ ] Document rollback procedures.

---

# 📚 Learning Outcomes

This project demonstrates practical experience with:

### ☕ Java

* Java 17 application development
* Object-oriented programming
* Layered application design

### 🌱 Spring Boot

* Spring Boot application structure
* Auto-configuration
* Dependency injection
* Spring MVC

### 🌐 Web Development

* HTTP request handling
* MVC controllers
* Thymeleaf templates
* HTML forms
* CSS

### 🔐 Security

* Spring Security
* BCrypt password hashing
* HTTP sessions
* JWT authentication
* Bearer tokens
* Authentication filters

### 🗄️ Persistence

* Spring Data JPA
* Hibernate
* Entity relationships
* Cascading operations
* MySQL-compatible databases
* H2 testing database

### 📧 External Services

* Gmail SMTP
* Asynchronous email services
* Cloudinary
* External API integration

### 🖼️ File Handling

* Multipart file uploads
* Base64 encoding
* Database image storage
* Cloud image storage

### 🔌 REST APIs

* REST controllers
* CRUD operations
* `ResponseEntity`
* HTTP status codes
* JWT-protected endpoints

### 🐳 DevOps

* Maven
* Maven Wrapper
* Docker
* Multi-stage Docker builds
* Environment variables
* Render deployment

### 🧪 Testing

* Spring context testing
* Service testing
* Email service testing
* Manual REST testing with Postman

---

# 📊 Feature Summary

| Feature                      |  Status  |
| ---------------------------- | :------: |
| User Registration            |     ✅    |
| BCrypt Password Hashing      |     ✅    |
| Browser Login                |     ✅    |
| Browser Logout               |     ✅    |
| Session Authentication       |     ✅    |
| User CRUD                    |     ✅    |
| JWT Authentication           |     ✅    |
| Protected REST API           |     ✅    |
| Database Image Gallery       |     ✅    |
| Cloudinary Gallery           |     ✅    |
| Gmail SMTP                   |     ✅    |
| Async Email                  |     ✅    |
| JPA/Hibernate                |     ✅    |
| MySQL-Compatible DB          |     ✅    |
| H2 Test Database             |     ✅    |
| Docker                       |     ✅    |
| Render Configuration         |     ✅    |
| Automated Tests              | 🟡 Basic |
| Comprehensive Security Tests |    🔜    |
| DTO-Based REST API           |    🔜    |
| Role-Based Authorization     |    🔜    |
| Database Migrations          |    🔜    |
| CI/CD Pipeline               |    🔜    |
| Production Hardening         |    🔜    |

---

# 🧭 Quick Start

If you just want to get the project running:

```bash
# Clone
git clone <your-repository-url>

# Enter project
cd MySpringWeb

# Configure environment variables

# Run application
./mvnw spring-boot:run
```

Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Then open:

```text
http://localhost:8080
```

---

# 🧪 Quick API Test

### 1. Login

```http
POST http://localhost:8080/api/auth/login
```

Parameters:

```text
username=your-username
password=your-password
```

### 2. Copy JWT

```json
{
  "token": "your-jwt-token"
}
```

### 3. Call Protected Endpoint

```bash
curl -H "Authorization: Bearer your-jwt-token" \
     http://localhost:8080/api/users
```

---

# 🗺️ Application Map

```text
                     MySpringWeb
                          │
        ┌─────────────────┴─────────────────┐
        │                                   │
        ▼                                   ▼
   🌐 Web Interface                     🔌 REST API
        │                                   │
        ▼                                   ▼
 Thymeleaf + MVC                    JWT + Spring Security
        │                                   │
        └─────────────────┬─────────────────┘
                          │
                          ▼
                   🧠 Service Layer
                          │
             ┌────────────┼────────────┐
             │            │            │
             ▼            ▼            ▼
           Users        Email       Uploads
             │            │            │
             ▼            ▼            ▼
         Database       Gmail      Cloudinary
             │
             ▼
       Spring Data JPA
```

---

# ⚠️ Important Notes

This project is primarily an **educational enterprise web application**.

It demonstrates real-world concepts, but it should not be considered fully production-hardened.

Before using it for a production workload, review:

* Authentication architecture
* Authorization
* CSRF protection
* File upload security
* API DTO design
* Password exposure
* JWT lifecycle management
* Database migrations
* Monitoring
* Logging
* Automated security testing
* CI/CD
* Backup and recovery

The [Future Scope](#-future-scope) section documents these improvements in detail.

---

# 👨‍💻 Author

## **Sulav Poudyal**

Built as an **Enterprise Web Systems Development** project.

---

# 📄 License

See [`LICENSE`](LICENSE) for the project license information.

---

# ⭐ Final Note

If this project helped you understand Spring Boot, JWT authentication, JPA, Docker, Cloudinary, or enterprise application architecture, consider giving the repository a ⭐.

**Thank you for checking out MySpringWeb! 🚀**
