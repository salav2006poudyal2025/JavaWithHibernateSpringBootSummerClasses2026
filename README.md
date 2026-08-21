# MySpringWeb

MySpringWeb is a Java 17 Spring Boot web application developed as a practical full-stack project. It demonstrates how a modern Java web application can combine server-side web pages, database persistence, authentication, REST APIs, email services, image uploads, cloud storage, Docker, and cloud deployment.

The application provides two main interfaces:

1. **Browser-based web application** using Thymeleaf and HTTP sessions.
2. **JWT-protected REST API** for clients such as Postman, JavaScript applications, or mobile applications.

> **Project status:** This README describes the current implementation of the project. It also documents known implementation limitations and recommended improvements. Some areas should be strengthened before using the application in a production environment.

---

## Table of Contents

1. [Project Overview](#1-project-overview)
2. [Problems the Project Solves](#2-problems-the-project-solves)
3. [Main Features](#3-main-features)
4. [Technology Stack](#4-technology-stack)
5. [Project Architecture](#5-project-architecture)
6. [Project Structure](#6-project-structure)
7. [How the Application Works](#7-how-the-application-works)
8. [User Registration](#8-user-registration)
9. [User Login and Session Management](#9-user-login-and-session-management)
10. [User Management](#10-user-management)
11. [Password Security](#11-password-security)
12. [JWT Authentication](#12-jwt-authentication)
13. [Email Integration](#13-email-integration)
14. [Image Upload and Storage](#14-image-upload-and-storage)
15. [Database Design](#15-database-design)
16. [REST API Documentation](#16-rest-api-documentation)
17. [Browser Routes](#17-browser-routes)
18. [Exception Handling](#18-exception-handling)
19. [Environment Variables](#19-environment-variables)
20. [Local Development Setup](#20-local-development-setup)
21. [Docker Setup](#21-docker-setup)
22. [Render Deployment](#22-render-deployment)
23. [Testing](#23-testing)
24. [Security Considerations](#24-security-considerations)
25. [Current Implementation Limitations](#25-current-implementation-limitations)
26. [Future Improvements](#26-future-improvements)
27. [Course Concepts Demonstrated](#27-course-concepts-demonstrated)
28. [Project Achievements](#28-project-achievements)
29. [Conclusion](#29-conclusion)

---

# 1. Project Overview

MySpringWeb is a practical Spring Boot application that brings together several important concepts of Java web development.

The application allows users to create accounts, authenticate themselves, manage user records, receive registration emails, and upload images.

It also provides REST APIs secured with JSON Web Tokens (JWT).

At a high level, the system works like this:

```text
                         CLIENTS
                  /                    \
                 /                      \
            Browser                  REST Client
               |                         |
               v                         v
       Thymeleaf Controllers       REST Controllers
               |                         |
               +------------+------------+
                            |
                            v
                      Service Layer
                            |
                            v
                    Repository Layer
                            |
                            v
                     Database Layer
                            |
                 +----------+----------+
                 |                     |
                 v                     v
             MySQL/TiDB          Image Records

External integrations:
        |
        +------> Gmail SMTP
        |
        +------> Cloudinary
```

The project follows a layered architecture so that each part of the application has a clear responsibility.

---

# 2. Problems the Project Solves

MySpringWeb demonstrates how to build an application that can:

* Register new users.
* Authenticate users securely.
* Store users in a relational database.
* Protect passwords using BCrypt.
* Maintain browser login sessions.
* Send registration emails.
* Upload images.
* Store images in a database.
* Store images using Cloudinary.
* Expose REST APIs.
* Protect REST APIs using JWT.
* Handle REST API exceptions centrally.
* Package the application using Docker.
* Deploy the application using Render.

The project is especially useful for learning because concepts that are often studied separately are connected together in one application.

---

# 3. Main Features

## 3.1 User Registration

Users can create accounts by providing:

* Username
* Email
* Password

The password is hashed using BCrypt before being stored.

---

## 3.2 User Login

Users can log in using their username and password.

The application retrieves the stored BCrypt hash and uses BCrypt's `matches()` operation to verify the password.

If authentication succeeds, the username is stored in the HTTP session.

---

## 3.3 Logout

The `/logout` route invalidates the current browser session.

This removes the session-based authentication information.

---

## 3.4 User Management

The browser interface provides functionality to:

* List users
* Open a user's edit page
* Update username
* Update email
* Delete users

---

## 3.5 Browser Session Authentication

The browser interface uses an HTTP session.

After login:

```text
username
    |
    v
HTTP Session
    |
    v
Browser requests
```

The session can then be used by controllers to determine whether a browser user is logged in.

---

## 3.6 JWT Authentication

The REST API uses JWT authentication.

The process is:

```text
Username + Password
        |
        v
/api/auth/login
        |
        v
JWT generated
        |
        v
Authorization: Bearer <token>
        |
        v
Protected API
```

---

## 3.7 Email Notification

After successful registration, the application starts an asynchronous email task.

The email is sent using:

```text
JavaMailSender
       |
       v
SMTP Server
       |
       v
User's Email
```

---

## 3.8 Local Image Gallery

The `/gallery` feature stores uploaded image data in the database.

The image is converted to Base64 before being stored.

---

## 3.9 Cloudinary Image Gallery

The `/gallery2` feature uploads image data to Cloudinary.

Cloudinary returns a secure HTTPS URL, and the application stores that URL in the database.

Instead of storing the entire image in the database, the database stores something similar to:

```text
https://res.cloudinary.com/.../image/upload/...
```

---

## 3.10 REST API

The project provides REST endpoints for:

* Authentication
* Health/demo testing
* Listing users
* Finding a user
* Creating a user
* Updating a user
* Deleting a user

---

## 3.11 Docker Support

The application includes a multi-stage Dockerfile.

The first stage builds the application using Maven.

The second stage runs the generated JAR using a Java 17 runtime.

---

## 3.12 Render Deployment

The project includes `render.yaml` for Render Blueprint deployment.

Secrets are provided through environment variables instead of being committed to Git.

---

# 4. Technology Stack

| Technology        | Purpose                                   |
| ----------------- | ----------------------------------------- |
| Java 17           | Main programming language                 |
| Spring Boot 4.1.0 | Main application framework                |
| Spring MVC        | HTTP request and response handling        |
| Spring Security   | Authentication and security configuration |
| Thymeleaf         | Server-side HTML rendering                |
| Spring Data JPA   | Database persistence                      |
| Hibernate         | ORM implementation                        |
| MySQL / TiDB      | Relational database                       |
| BCrypt            | Password hashing                          |
| JJWT              | JWT creation and validation               |
| JavaMailSender    | Email sending                             |
| Cloudinary        | Cloud image storage                       |
| Maven             | Dependency management and project build   |
| Docker            | Application containerization              |
| Render            | Cloud deployment                          |
| Lombok            | Boilerplate code generation               |

---

# 5. Project Architecture

MySpringWeb follows a simple **N-tier/layered architecture**.

```text
Browser / Postman / REST Client
              |
              | HTTP Request
              v
       +----------------+
       |  Controller    |
       +----------------+
              |
              v
       +----------------+
       |    Service     |
       +----------------+
              |
              v
       +----------------+
       |   Repository   |
       +----------------+
              |
              v
       +----------------+
       |    Database    |
       +----------------+
```

Each layer has a different responsibility.

---

## 5.1 Controller Layer

The controller layer receives HTTP requests.

Responsibilities include:

* Receiving form submissions.
* Reading request parameters.
* Reading path variables.
* Calling services.
* Returning Thymeleaf pages.
* Returning JSON responses.
* Redirecting users.

Main packages:

```text
Controller/
RController/
```

---

## 5.2 Service Layer

The service layer contains business logic.

Examples include:

* Password hashing.
* Password verification.
* User registration.
* User authentication.
* Email triggering.
* User updates.

Main files:

```text
Service/UserService.java
Service/UserServiceImpl.java
Service/EmailService.java
```

---

## 5.3 Repository Layer

The repository layer communicates with the database through Spring Data JPA.

Main repositories:

```text
UserRepository
ImageRepository
Image2Repository
```

The repository provides operations such as:

```text
save()
findAll()
findById()
delete()
```

---

## 5.4 Model Layer

The model layer contains JPA entities.

Main entities:

```text
UserTable
ImageTable
ImageTable2
```

These classes represent data stored in the database.

---

## 5.5 Configuration Layer

The configuration layer contains:

* Spring Security configuration
* JWT utilities
* JWT authentication filter
* Cloudinary configuration

Main files:

```text
SecurityConfig.java
JWUtil.java
JwtAuthenticationFilter.java
CloudinaryConfig.java
```

---

# 6. Project Structure

```text
MySpringWeb/
│
├── src/
│   ├── main/
│   │   ├── java/io/herald/MySpringWeb/
│   │   │
│   │   ├── Configuration/
│   │   │   ├── SecurityConfig.java
│   │   │   ├── JWUtil.java
│   │   │   ├── JwtAuthenticationFilter.java
│   │   │   └── CloudinaryConfig.java
│   │   │
│   │   ├── Controller/
│   │   │   ├── SignupController.java
│   │   │   ├── MappingClass.java
│   │   │   ├── UserController.java
│   │   │   ├── GalleryController.java
│   │   │   └── MailController.java
│   │   │
│   │   ├── RController/
│   │   │   ├── AuthRestController.java
│   │   │   └── RControllerClass.java
│   │   │
│   │   ├── Exception/
│   │   │   ├── GlobalExceptionHandler.java
│   │   │   └── UserNotFoundException.java
│   │   │
│   │   ├── Model/
│   │   │   ├── UserTable.java
│   │   │   ├── ImageTable.java
│   │   │   └── ImageTable2.java
│   │   │
│   │   ├── Repository/
│   │   │   ├── UserRepository.java
│   │   │   ├── ImageRepository.java
│   │   │   └── Image2Repository.java
│   │   │
│   │   ├── Service/
│   │   │   ├── UserService.java
│   │   │   ├── UserServiceImpl.java
│   │   │   └── EmailService.java
│   │   │
│   │   └── MySpringWebApplication.java
│   │
│   └── resources/
│       ├── templates/
│       │   ├── login.html
│       │   ├── signup.html
│       │   ├── home.html
│       │   ├── editPage.html
│       │   ├── galleryPage.html
│       │   ├── galleryPage2.html
│       │   └── navbar.html
│       │
│       ├── static/
│       │   └── css/
│       │       └── styles.css
│       │
│       └── application.properties
│
├── src/test/
│   ├── java/
│   └── resources/
│
├── Dockerfile
├── render.yaml
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

Some additional files such as `Test.java`, `test/maptest.java`, and `templates/login2.html` are present for learning or legacy experimentation and are not part of the primary application flow.

---

# 7. How the Application Works

The complete application can be understood as a series of connected workflows.

```text
User
 |
 v
Browser
 |
 v
Controller
 |
 v
Service
 |
 v
Repository
 |
 v
Database
```

For external integrations:

```text
Application
 |
 +----> Gmail SMTP
 |
 +----> Cloudinary
```

For the REST API:

```text
REST Client
    |
    v
JWT Login
    |
    v
JWT Token
    |
    v
Authorization Header
    |
    v
JWT Filter
    |
    v
Spring Security
    |
    v
REST Controller
```

---

# 8. User Registration

The registration page is available at:

```text
/signup
```

The user provides:

* Username
* Email
* Password

The process is:

```text
User
 |
 v
signup.html
 |
 v
SignupController
 |
 |-- Check required fields
 |
 |-- Check duplicate username
 |
 v
UserServiceImpl
 |
 |-- Hash password using BCrypt
 |
 v
UserRepository
 |
 v
Database
 |
 +----> EmailService
              |
              v
          SMTP Server
```

### Step-by-step explanation

### Step 1: User submits the form

The browser sends the registration data to the signup controller.

### Step 2: Controller checks the input

The controller performs basic checks, including duplicate username detection.

### Step 3: Service processes the user

The service receives the user and hashes the password.

### Step 4: Password is stored securely

Instead of storing:

```text
mypassword123
```

the database stores a BCrypt hash similar to:

```text
$2a$10$...
```

### Step 5: User is saved

The repository saves the user into the database.

### Step 6: Welcome email is started

The email service creates a welcome message and sends it asynchronously.

### Step 7: User is redirected

After registration, the user can continue to the login page.

---

# 9. User Login and Session Management

The browser login page is:

```text
/login
```

The login process is:

```text
User enters username/password
             |
             v
       MappingClass
             |
             v
       UserServiceImpl
             |
             v
      Find username
             |
             v
     BCrypt.matches()
             |
       +-----+-----+
       |           |
     Valid       Invalid
       |           |
       v           v
 Session        Login Error
       |
       v
   /home
```

When authentication succeeds, the application stores:

```text
session.username
```

The browser can then access session-dependent pages.

---

## Logout

The logout route is:

```text
/logout
```

The application invalidates the HTTP session.

```text
Logged-in Session
       |
       v
    /logout
       |
       v
Session Invalidated
       |
       v
Login Page
```

---

# 10. User Management

The application provides browser-based user management.

The home page can list registered users.

The application supports:

```text
List
 |
 +----> Edit
 |
 +----> Update
 |
 +----> Delete
```

The normal update process changes fields such as:

* Username
* Email

The existing password and image relationships are retained when the normal edit flow updates the user.

---

# 11. Password Security

Password security is one of the important parts of this project.

The application uses:

```text
BCryptPasswordEncoder
```

## During registration

```text
Raw Password
     |
     v
BCrypt Encoder
     |
     v
Password Hash
     |
     v
Database
```

## During login

```text
Entered Password
       |
       v
BCrypt.matches()
       |
       v
Stored BCrypt Hash
       |
       v
true / false
```

The application does **not** intentionally compare passwords using:

```java
rawPassword.equals(storedPassword)
```

Instead, BCrypt is used to verify the password.

The `existsByUsernameAndPassword` repository method exists in the project but is not used for authentication. The service correctly performs authentication through BCrypt.

---

# 12. JWT Authentication

JWT stands for **JSON Web Token**.

The project uses JWT to authenticate REST API clients.

The basic flow is:

```text
POST /api/auth/login
        |
        v
Username + Password
        |
        v
BCrypt Verification
        |
        v
JWT Generated
        |
        v
Client receives token
        |
        v
Authorization: Bearer <token>
        |
        v
Protected API
```

---

## 12.1 JWT Login

The client sends:

```text
POST /api/auth/login
```

with:

```text
username
password
```

If the credentials are valid, the application generates a signed JWT.

The token contains the username as its subject.

---

## 12.2 JWT Secret

The JWT is signed using:

```text
JWT_SECRET
```

This value must remain private.

A long, random secret should be used.

---

## 12.3 JWT Expiration

The current JWT expiration is:

```text
86,400,000 milliseconds
```

which is approximately:

```text
24 hours
```

---

## 12.4 JWT Filter

When the client sends a protected request:

```http
Authorization: Bearer YOUR_TOKEN
```

the `JwtAuthenticationFilter`:

1. Reads the Authorization header.
2. Extracts the Bearer token.
3. Validates the JWT.
4. Checks the signature.
5. Checks expiration.
6. Extracts the username.
7. Places an authenticated principal into the Spring Security context.

The request can then continue to the protected REST controller.

---

## 12.5 Current JWT Limitations

The project currently does not implement:

* Refresh tokens
* Token revocation
* Role-based authorization
* Token blacklist
* Advanced JWT error responses
* A defined browser-side JWT storage strategy

These are potential future improvements.

---

# 13. Email Integration

The application sends a welcome email after registration.

The process is:

```text
Successful Registration
          |
          v
UserServiceImpl
          |
          v
EmailService
          |
          v
SimpleMailMessage
          |
          v
JavaMailSender
          |
          v
SMTP Server
          |
          v
User's Email
```

The application uses:

```java
JavaMailSender
```

and:

```java
SimpleMailMessage
```

The email method is asynchronous using:

```java
@Async
```

This means the application does not have to wait for the entire email operation before continuing with the registration flow.

If email delivery fails, the current implementation catches the error and prints information to standard error.

For Gmail SMTP, an **App Password** should be used instead of a normal Gmail password.

---

# 14. Image Upload and Storage

The application supports two different image-storage methods.

---

## 14.1 Local Database Gallery

The local gallery is available through:

```text
/gallery
```

The process is:

```text
User selects image
       |
       v
MultipartFile
       |
       v
Read image bytes
       |
       v
Base64 Encoding
       |
       v
ImageTable
       |
       v
Database
       |
       v
Thymeleaf
       |
       v
Image displayed
```

The image is stored as a Base64 representation.

The page can display it using a data URL similar to:

```text
data:image/jpeg;base64,...
```

---

## 14.2 Cloudinary Gallery

The Cloudinary gallery is available through:

```text
/gallery2
```

The process is:

```text
User selects image
       |
       v
MultipartFile
       |
       v
GalleryController
       |
       v
Cloudinary SDK
       |
       v
Cloudinary
       |
       v
secure_url
       |
       v
ImageTable2
       |
       v
Database
       |
       v
galleryPage2.html
       |
       v
Image displayed
```

The database stores the Cloudinary URL rather than the entire image.

This keeps the database record significantly smaller.

---

# 15. Database Design

The project uses a MySQL-compatible relational database.

TiDB can also be used because it provides MySQL compatibility.

The main entities are:

```text
UserTable
ImageTable
ImageTable2
```

---

## 15.1 UserTable

The `UserTable` entity represents application users.

Main fields:

| Field      | Description           |
| ---------- | --------------------- |
| `id`       | Generated primary key |
| `username` | User's username       |
| `email`    | User's email          |
| `password` | BCrypt password hash  |

Validation includes:

```text
@NotBlank
@Email
```

where appropriate.

---

## 15.2 ImageTable

`ImageTable` stores locally uploaded image information.

Main fields include:

```text
id
image
user_id
```

The image is stored as a large database object/Base64 representation.

---

## 15.3 ImageTable2

`ImageTable2` stores Cloudinary image information.

Main fields include:

```text
id
imageUrl
user_id
```

The `imageUrl` contains the HTTPS URL returned by Cloudinary.

The user relationship exists in the model, but the current Cloudinary upload flow does not set the user relationship.

---

# 16. Database Relationships

The relationships can be understood as:

```text
                 UserTable
                    |
          +---------+---------+
          |                   |
          v                   v
      ImageTable         ImageTable2
```

One user can have multiple image records.

Therefore:

```text
UserTable 1 ---- * ImageTable

UserTable 1 ---- * ImageTable2
```

This represents:

* One-to-many relationship from user to images.
* Many-to-one relationship from images to user.

The user image collections use `CascadeType.ALL`.

This means JPA is configured to cascade relevant operations from the user to associated images.

---

# 17. REST API Documentation

The default local base URL is:

```text
http://localhost:8080
```

All `/api/**` endpoints require JWT authentication except:

```text
/api/auth/**
```

---

## 17.1 Authentication

### Login

```http
POST /api/auth/login
```

Authentication:

```text
Not required
```

Request type:

```text
application/x-www-form-urlencoded
```

Parameters:

```text
username
password
```

Successful response:

```json
{
  "token": "YOUR_JWT_TOKEN"
}
```

---

## 17.2 Health/Demo Endpoint

```http
GET /api/hello
```

Authentication:

```text
JWT required
```

Example response:

```text
Hello World
```

---

## 17.3 Get All Users

```http
GET /api/users
```

Authentication:

```text
JWT required
```

Possible responses:

```text
200 OK
```

with users, or:

```text
204 No Content
```

when no users exist.

---

## 17.4 Get User by ID

```http
GET /api/users/{id}
```

Example:

```http
GET /api/users/5
```

Authentication:

```text
JWT required
```

Possible responses:

```text
200 OK
404 Not Found
```

---

## 17.5 Create User

```http
POST /api/users
```

Authentication:

```text
JWT required
```

Content type:

```text
application/json
```

Example:

```json
{
  "username": "bob",
  "email": "bob@example.com",
  "password": "choose-a-password"
}
```

Successful response:

```text
201 Created
```

---

## 17.6 Update User

```http
PUT /api/users/{id}
```

Authentication:

```text
JWT required
```

Example:

```http
PUT /api/users/5
```

Request body:

```json
{
  "username": "newusername",
  "email": "newemail@example.com",
  "password": "new-password"
}
```

Possible responses:

```text
200 OK
404 Not Found
```

---

## 17.7 Delete User

```http
DELETE /api/users/{id}
```

Authentication:

```text
JWT required
```

Possible responses:

```text
204 No Content
404 Not Found
```

---

# 18. API Examples

## Get JWT Token

```bash
curl -X POST "http://localhost:8080/api/auth/login" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "username=alice&password=your-password"
```

---

## Use JWT Token

```bash
curl "http://localhost:8080/api/users" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

---

## Create User

```bash
curl -X POST "http://localhost:8080/api/users" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"username":"bob","email":"bob@example.com","password":"choose-a-password"}'
```

> Never publish real passwords, JWT tokens, database passwords, SMTP credentials, or Cloudinary secrets in GitHub, screenshots, reports, or documentation.

---

# 19. Browser Routes

The browser-based application provides the following routes.

| Method     | Route         | Purpose                      |
| ---------- | ------------- | ---------------------------- |
| GET        | `/`           | Landing page                 |
| GET / POST | `/signup`     | Registration                 |
| GET / POST | `/login`      | Login                        |
| GET        | `/home`       | User list/home page          |
| POST       | `/editUser`   | Open edit form               |
| POST       | `/updateUser` | Update user                  |
| POST       | `/deleteUser` | Delete user                  |
| GET        | `/logout`     | Logout                       |
| GET        | `/mail`       | Mail page                    |
| GET / POST | `/gallery`    | Local database image gallery |
| GET / POST | `/gallery2`   | Cloudinary gallery           |
| GET        | `/nextPage`   | Auxiliary page               |

---

# 20. Exception Handling

The application uses:

```java
@ControllerAdvice
```

through the `GlobalExceptionHandler`.

This allows exceptions to be handled centrally rather than repeating error-handling code in every controller.

---

## User Not Found

If a REST request tries to access a user that does not exist:

```text
UserNotFoundException
        |
        v
GlobalExceptionHandler
        |
        v
404 Not Found
```

---

## Validation Error

Constraint violations can produce:

```text
400 Bad Request
```

with validation messages.

---

## Unexpected Error

Unhandled exceptions can result in:

```text
500 Internal Server Error
```

The current implementation returns the exception message, which should be improved for production so internal information is not exposed.

---

# 21. Environment Variables

Sensitive configuration values are not intended to be stored directly in the source code.

The application reads configuration through environment variables.

| Variable                | Purpose                           |
| ----------------------- | --------------------------------- |
| `DB_URL`                | MySQL/TiDB JDBC connection URL    |
| `DB_USERNAME`           | Database username                 |
| `DB_PASSWORD`           | Database password                 |
| `MAIL_USERNAME`         | SMTP sender email                 |
| `MAIL_PASSWORD`         | SMTP app password/provider secret |
| `JWT_SECRET`            | JWT signing secret                |
| `CLOUDINARY_CLOUD_NAME` | Cloudinary cloud name             |
| `CLOUDINARY_API_KEY`    | Cloudinary API key                |
| `CLOUDINARY_API_SECRET` | Cloudinary API secret             |

These values should never be committed to Git.

---

# 22. Local Development Setup

## Prerequisites

Install:

* JDK 17
* MySQL or TiDB
* Maven-compatible internet connection
* SMTP account
* Cloudinary account

The project includes the Maven Wrapper, so Maven does not necessarily need to be installed globally.

---

## Step 1: Clone the Repository

```bash
git clone <YOUR_REPOSITORY_URL>
cd MySpringWeb
```

---

## Step 2: Create Database

Create an empty database.

Example:

```text
myspringweb
```

Example JDBC URL:

```text
jdbc:mysql://localhost:3306/myspringweb
```

---

## Step 3: Configure Environment Variables

### Windows PowerShell

```powershell
$env:DB_URL = "jdbc:mysql://localhost:3306/myspringweb"
$env:DB_USERNAME = "your_database_user"
$env:DB_PASSWORD = "your_database_password"

$env:MAIL_USERNAME = "your_sender@example.com"
$env:MAIL_PASSWORD = "your_smtp_app_password"

$env:JWT_SECRET = "a-long-random-secret-with-at-least-32-bytes"

$env:CLOUDINARY_CLOUD_NAME = "your_cloud_name"
$env:CLOUDINARY_API_KEY = "your_api_key"
$env:CLOUDINARY_API_SECRET = "your_api_secret"
```

### macOS/Linux

```bash
export DB_URL='jdbc:mysql://localhost:3306/myspringweb'
export DB_USERNAME='your_database_user'
export DB_PASSWORD='your_database_password'

export MAIL_USERNAME='your_sender@example.com'
export MAIL_PASSWORD='your_smtp_app_password'

export JWT_SECRET='a-long-random-secret-with-at-least-32-bytes'

export CLOUDINARY_CLOUD_NAME='your_cloud_name'
export CLOUDINARY_API_KEY='your_api_key'
export CLOUDINARY_API_SECRET='your_api_secret'
```

---

# 23. Build and Run

## Windows

Run tests:

```powershell
.\mvnw.cmd test
```

Build:

```powershell
.\mvnw.cmd clean package
```

Run:

```powershell
.\mvnw.cmd spring-boot:run
```

---

## macOS/Linux

Run tests:

```bash
./mvnw test
```

Build:

```bash
./mvnw clean package
```

Run:

```bash
./mvnw spring-boot:run
```

---

## Run JAR Directly

After building:

```bash
java -jar target/MySpringWebGrp1-0.0.1-SNAPSHOT.jar
```

Then open:

```text
http://localhost:8080
```

---

# 24. Database Schema Generation

The application currently uses:

```properties
spring.jpa.hibernate.ddl-auto=update
```

This allows Hibernate to automatically create or update database tables during development.

This is convenient for development but should generally be replaced with a proper migration system such as:

* Flyway
* Liquibase

for production environments.

---

# 25. Docker Setup

The project includes a multi-stage Dockerfile.

The build process is:

```text
Stage 1
Maven + Java 17
       |
       v
Build Spring Boot JAR
       |
       v
Stage 2
Java 17 JRE
       |
       v
Run JAR
```

This produces a smaller runtime image because the final container does not need the full Maven build environment.

---

## Build Docker Image

From the project root:

```bash
docker build -t my-spring-web .
```

---

## Run Docker Container

```bash
docker run --rm -p 8080:8080 \
  -e DB_URL='jdbc:mysql://host.docker.internal:3306/myspringweb' \
  -e DB_USERNAME='your_database_user' \
  -e DB_PASSWORD='your_database_password' \
  -e MAIL_USERNAME='your_sender@example.com' \
  -e MAIL_PASSWORD='your_smtp_app_password' \
  -e JWT_SECRET='a-long-random-secret-with-at-least-32-bytes' \
  -e CLOUDINARY_CLOUD_NAME='your_cloud_name' \
  -e CLOUDINARY_API_KEY='your_api_key' \
  -e CLOUDINARY_API_SECRET='your_api_secret' \
  my-spring-web
```

Then open:

```text
http://localhost:8080
```

For Linux, make sure the database hostname is reachable from the container.

---

# 26. Render Deployment

The repository contains:

```text
render.yaml
```

This allows the application to be deployed using Render Blueprint.

## Deployment Steps

### 1. Push to GitHub

Push the project to a GitHub repository.

Make sure the repository contains:

```text
Dockerfile
render.yaml
pom.xml
src/
```

Never push real secrets.

---

### 2. Open Render

Sign in to Render and choose:

```text
New → Blueprint
```

---

### 3. Connect GitHub

Select the repository containing MySpringWeb.

---

### 4. Configure Environment Variables

Add:

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

---

### 5. Deploy

Render builds the Docker image and starts the application.

After successful deployment, Render provides a public URL such as:

```text
https://your-service.onrender.com
```

---

## Deployment Checklist

Before deploying, verify:

* `main` branch contains the latest code.
* Database is reachable from Render.
* Database connection settings are correct.
* SMTP credentials work.
* Cloudinary credentials work.
* JWT secret is configured.
* No secrets are committed to GitHub.
* Application logs do not expose credentials.

---

# 27. Testing

The current automated test is:

```text
MySpringWebApplicationTests.contextLoads
```

The test verifies that the Spring application context can load successfully.

Test configuration uses H2 and dummy values so the test does not depend on production credentials.

---

## Run Tests

Windows:

```powershell
.\mvnw.cmd test
```

macOS/Linux:

```bash
./mvnw test
```

---

# 28. Manual Testing

| Test                        | Expected Result                 |
| --------------------------- | ------------------------------- |
| Register user               | Account is created              |
| Register duplicate username | Duplicate username is rejected  |
| Login                       | User is redirected to home      |
| Logout                      | Session is invalidated          |
| Email                       | Welcome email is sent           |
| Local image upload          | Image is saved and displayed    |
| Cloudinary upload           | Image is uploaded and URL saved |
| JWT login                   | JWT token is returned           |
| Protected API without token | Request is rejected             |
| Protected API with token    | Request succeeds                |
| Get non-existing user       | `404 Not Found`                 |
| Delete user                 | User is removed                 |

---

# 29. Postman Testing

A simple Postman workflow is:

## Step 1: Login

Create:

```text
POST /api/auth/login
```

Choose:

```text
Body → x-www-form-urlencoded
```

Add:

```text
username
password
```

---

## Step 2: Copy JWT

The response will contain:

```json
{
  "token": "..."
}
```

Copy the token.

---

## Step 3: Configure Authorization

For protected requests:

```text
Authorization
    |
    v
Bearer Token
    |
    v
Paste JWT
```

---

## Step 4: Test API

Try:

```text
GET /api/hello
```

Then:

```text
GET /api/users
```

---

## Step 5: Test User Creation

Use:

```text
POST /api/users
```

with:

```json
{
  "username": "bob",
  "email": "bob@example.com",
  "password": "password"
}
```

---

# 30. Security Configuration

Spring Security is configured using:

```text
SecurityConfig.java
```

The general security flow is:

```text
Incoming Request
       |
       v
Spring Security Filter Chain
       |
       v
JwtAuthenticationFilter
       |
       v
Is JWT valid?
       |
   +---+---+
   |       |
  Yes      No
   |       |
   v       v
Authenticated
   |
   v
Authorization Rules
   |
   v
Controller
```

---

## Public Routes

The following routes are public:

```text
/
 /login
/signup
/css/**
/js/**
/images/**
/api/auth/**
```

---

## Protected API Routes

Every:

```text
/api/**
```

route requires authentication except:

```text
/api/auth/**
```

---

## Browser Routes

Most non-API routes are currently permitted by Spring Security.

Some controllers perform their own session checks, such as:

```text
/mail
/gallery
```

However, other browser pages do not consistently perform session checks.

This should be improved before production use.

---

# 31. CSRF

The current security configuration disables CSRF.

This can be reasonable for stateless JWT APIs.

However, the browser application uses HTTP sessions and server-side forms.

Therefore, CSRF protection should be reconsidered and properly configured before production deployment.

---

# 32. Role-Based Authorization

The current application does not implement roles such as:

```text
ADMIN
USER
```

The JWT authentication establishes an authenticated user but does not provide role-based authorization.

Therefore, the application currently focuses on:

```text
Authenticated
       vs
Not Authenticated
```

rather than:

```text
ADMIN
USER
GUEST
```

---

# 33. Current Implementation Limitations

The project is functional, but several areas should be improved.

## 33.1 Browser Route Security

Not every browser-only page currently checks the session consistently.

For example:

* `/mail` checks the session.
* `/gallery` checks the session.
* `/home` does not currently perform the same controller-level check.
* `/gallery2` does not currently perform the same session check.

Consistent authorization should be added.

---

## 33.2 Cloudinary Image Ownership

`ImageTable2` contains a user relationship.

However, the current Cloudinary upload implementation does not set that relationship.

Therefore:

```text
Cloudinary Image
      |
      v
ImageTable2
      |
      v
user_id may be NULL
```

This should be fixed so every uploaded image belongs to the authenticated user.

---

## 33.3 Image Validation

The current image upload implementation does not strongly validate:

* File type
* File size
* Empty files
* Invalid image content

Production applications should validate uploads before processing them.

---

## 33.4 Database Constraints

Username uniqueness is currently checked by application logic.

It should also be enforced at the database level.

For example:

```text
username UNIQUE
email UNIQUE
```

depending on the application's requirements.

---

## 33.5 REST Validation

The entities contain validation annotations such as:

```java
@NotBlank
@Email
```

However, REST request bodies are not consistently annotated with:

```java
@Valid
```

Therefore, automatic validation should be strengthened.

---

## 33.6 Password Exposure

Returning JPA entities directly from REST APIs can expose fields that should not be returned, particularly password hashes.

DTOs should be used instead.

For example:

```text
UserEntity
     |
     v
UserResponseDTO
     |
     v
REST API
```

---

## 33.7 JWT Management

The application does not currently support:

* Refresh tokens
* Token revocation
* Token blacklist
* Advanced logout handling
* Role-based permissions

These features can be added later.

---

# 34. Future Improvements

The following improvements would make the application more suitable for production.

### 1. Improve browser security

Require authentication consistently for browser-only pages.

---

### 2. Enable appropriate CSRF protection

Especially for session-based browser forms.

---

### 3. Add Spring Security `UserDetailsService`

Use Spring Security's standard authentication model instead of relying entirely on custom authentication logic.

---

### 4. Add roles

Introduce roles such as:

```text
ADMIN
USER
```

and restrict sensitive operations.

For example:

```text
ADMIN → Can manage users

USER → Can manage own profile/images
```

---

### 5. Associate Cloudinary images with users

Every uploaded Cloudinary image should be connected to the authenticated user.

---

### 6. Validate uploaded files

Check:

* MIME type
* File extension
* File size
* Empty files
* Upload errors

---

### 7. Use DTOs

Avoid exposing database entities directly through REST APIs.

---

### 8. Add database-level constraints

Enforce important constraints such as username/email uniqueness.

---

### 9. Improve validation

Use:

```java
@Valid
```

and stronger validation rules in REST and browser controllers.

---

### 10. Improve exception responses

Do not expose internal exception messages in production.

Instead, return safe responses such as:

```json
{
  "status": 500,
  "message": "An unexpected error occurred"
}
```

---

### 11. Add structured logging

Replace simple console printing with a proper logging framework and appropriate log levels.

---

### 12. Add password reset

Implement:

* Password reset request
* Email verification
* Temporary reset token
* New password creation

---

### 13. Add email verification

Users could be required to verify their email address before accessing certain functionality.

---

### 14. Improve JWT security

Add:

* Refresh tokens
* Token revocation
* Better expiration management
* Secure token storage
* Improved JWT error handling

---

### 15. Add automated tests

Expand testing to include:

* Unit tests
* Service tests
* Controller tests
* Repository tests
* Integration tests
* Security tests
* REST API tests

---

### 16. Use database migrations

Replace:

```properties
spring.jpa.hibernate.ddl-auto=update
```

with a migration solution such as:

```text
Flyway
```

or:

```text
Liquibase
```

---

### 17. Add CI/CD

A CI/CD pipeline could automatically:

```text
Git Push
   |
   v
Build
   |
   v
Test
   |
   v
Docker Build
   |
   v
Deploy
```

---

# 35. Course Concepts Demonstrated

The project covers many common Java and Spring Boot course topics.

| Course Concept        | Project Implementation                                            |
| --------------------- | ----------------------------------------------------------------- |
| Java fundamentals     | Classes, interfaces, collections, exceptions, streams, `Optional` |
| Spring Boot           | `@SpringBootApplication`, starters, configuration                 |
| Spring MVC            | Controllers, request mappings, redirects, models                  |
| Thymeleaf             | Server-side HTML rendering                                        |
| JPA                   | Entities, relationships, repositories                             |
| Hibernate             | ORM and database mapping                                          |
| Spring Security       | Security filter chain and authentication                          |
| BCrypt                | Password hashing and verification                                 |
| JWT                   | Token generation and bearer authentication                        |
| REST                  | CRUD APIs and HTTP status codes                                   |
| Email                 | SMTP and `JavaMailSender`                                         |
| Cloudinary            | Cloud image upload and URL storage                                |
| Exception Handling    | `@ControllerAdvice`                                               |
| Maven                 | Dependency management and build                                   |
| Docker                | Multi-stage container build                                       |
| Render                | Cloud deployment configuration                                    |
| Environment Variables | External configuration and secrets                                |
| Async Processing      | Asynchronous email sending                                        |

---

# 36. Important Concepts for Viva/Presentation

If this project is being presented in a viva or academic demonstration, the following concepts are particularly important.

## Why use a Controller?

The controller handles HTTP requests and determines what response should be returned.

```text
HTTP Request
     |
     v
Controller
```

---

## Why use a Service?

The service separates business logic from HTTP handling.

```text
Controller
     |
     v
Service
```

This makes the code easier to maintain and test.

---

## Why use Repository?

The repository handles database operations.

```text
Service
   |
   v
Repository
   |
   v
Database
```

---

## Why use BCrypt?

Passwords should not be stored as plain text.

BCrypt creates a one-way password hash that can be verified later without storing the original password.

---

## Why use JWT?

JWT allows REST clients to authenticate using a signed token rather than sending their password with every request.

---

## Why use HTTP Session?

The browser-based application needs to remember that a user has logged in.

The HTTP session provides that state.

---

## Why use Cloudinary?

Storing large image files directly inside a relational database can increase database size and storage requirements.

Cloudinary allows the application to store the image externally and keep only the URL in the database.

---

## Why use Docker?

Docker packages the application and its runtime environment into a consistent container.

This reduces differences between development and deployment environments.

---

## Why use environment variables?

Sensitive values such as:

```text
Database password
JWT secret
SMTP password
Cloudinary secret
```

should not be committed to source control.

Environment variables allow these values to be configured separately.

---

# 37. Project Achievements

The project successfully demonstrates:

* A Spring Boot web application.
* Layered architecture.
* User registration.
* BCrypt password protection.
* Browser authentication using sessions.
* Login and logout.
* User CRUD functionality.
* JPA/Hibernate database integration.
* Local database image storage.
* Cloudinary image storage.
* SMTP email integration.
* JWT authentication.
* Protected REST APIs.
* Centralized exception handling.
* Maven build and testing.
* Docker containerization.
* Render deployment configuration.
* Environment-based secret management.

---

# 38. Complete System Flow

The entire project can be summarized using the following architecture:

```text
                         USER
                          |
                          v
                  +---------------+
                  |    Browser    |
                  +---------------+
                          |
                          v
                  +---------------+
                  |  Controllers  |
                  +---------------+
                          |
                          v
                  +---------------+
                  |    Services   |
                  +---------------+
                     /     |      \
                    /      |       \
                   v       v        v
              Database   Email   Cloudinary
                 |        |          |
                 v        v          v
              MySQL     SMTP     Image Storage


REST CLIENT
     |
     v
/api/auth/login
     |
     v
JWT Generation
     |
     v
Bearer Token
     |
     v
JwtAuthenticationFilter
     |
     v
Spring Security
     |
     v
REST Controller
     |
     v
Service
     |
     v
Repository
     |
     v
Database
```

---

# 39. Final Conclusion

MySpringWeb is a complete educational Spring Boot application that demonstrates how the different components of a modern Java web system work together.

The application provides a browser-based interface for registration, login, user management, and image galleries while also exposing JWT-protected REST APIs.

The most important architectural concept in the project is the **separation of responsibilities**:

```text
Controller
    ↓
Handles HTTP requests

Service
    ↓
Contains business logic

Repository
    ↓
Handles database operations

Model
    ↓
Represents application data

Configuration
    ↓
Handles security and external integrations

Thymeleaf
    ↓
Provides the browser interface
```

The project also demonstrates important real-world integrations:

```text
Spring Boot
    |
    +---- MySQL/TiDB
    |
    +---- Spring Security
    |
    +---- BCrypt
    |
    +---- JWT
    |
    +---- Gmail SMTP
    |
    +---- Cloudinary
    |
    +---- Docker
    |
    +---- Render
```

Overall, MySpringWeb provides a strong practical demonstration of how a Java Spring Boot application can be designed, secured, connected to a database, integrated with external services, tested, containerized, and deployed.

It is suitable as an educational project and provides a good foundation for further development into a production-ready application.

