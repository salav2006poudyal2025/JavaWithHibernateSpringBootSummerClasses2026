# MySpringWeb

> A full-stack Java Spring Boot application built to demonstrate enterprise web development with authentication, REST APIs, database persistence, email integration, cloud image storage, containerization, and cloud deployment.

[![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Security-Spring%20Security-green?style=flat-square&logo=springsecurity)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/Auth-JWT-purple?style=flat-square)](https://jwt.io/)
[![Docker](https://img.shields.io/badge/Container-Docker-blue?style=flat-square&logo=docker)](https://www.docker.com/)
[![Deployment](https://img.shields.io/badge/Deploy-Render-46E3B7?style=flat-square)](https://render.com/)

---

## 📌 Overview

**MySpringWeb** is a full-stack Java web application developed as part of an **Enterprise Web Systems Development** project.

The application combines a browser-based interface with REST APIs and demonstrates how different enterprise technologies work together in a layered architecture.

### What it includes

- User registration, login, and logout
- Session-based authentication
- BCrypt password hashing
- JWT authentication and protected REST APIs
- Spring Security
- Spring MVC and Spring Boot
- JPA/Hibernate ORM
- MySQL, TiDB, and H2 database support
- Gmail SMTP email integration
- Cloudinary image uploads
- Thymeleaf frontend
- Docker containerization
- Render deployment
- Postman API testing

---

## ✨ Features

### 🔐 Authentication & Security

- Session-based login and logout
- BCrypt password hashing
- JWT generation and validation
- Protected REST API endpoints
- Spring Security integration
- Environment-based secret configuration

### 👤 User Management

- Create users
- View users
- Update users
- Delete users
- Database persistence through JPA/Hibernate

### 📧 Email Service

- Gmail SMTP integration
- Registration/welcome emails
- Asynchronous email sending through the configured mail service

### 🖼️ Image Management

The project demonstrates two image-storage approaches:

**Local database gallery**
- Upload images
- Store Base64 image data
- Display images in the gallery

**Cloudinary gallery**
- Upload images to Cloudinary
- Store secure image URLs
- Display uploaded images

### 🌐 REST API

- JWT-protected endpoints
- CRUD operations
- `ResponseEntity` based HTTP responses
- HTTP status handling
- Exception handling

### 🚀 Deployment

- Docker support
- Render deployment
- Environment variable configuration
- GitHub-based deployment workflow

---

## 🏗️ Architecture

The application follows a layered/N-tier architecture that separates responsibilities between controllers, services, repositories, and the database.

```text
                    ┌─────────────────────┐
                    │   Browser / Postman │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │     Controllers     │
                    │ MVC / REST APIs     │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │      Services       │
                    │   Business Logic    │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │    Repositories     │
                    │    JPA / Hibernate  │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │      Database       │
                    │ MySQL / TiDB / H2  │
                    └─────────────────────┘
```

This structure improves **separation of concerns, maintainability, scalability, and code organization**.

---

## 🛠️ Technology Stack

| Area | Technologies |
|---|---|
| Language | Java 17 |
| Backend | Spring Boot, Spring MVC |
| Security | Spring Security, BCrypt, JWT |
| ORM | Spring Data JPA, Hibernate |
| Database | MySQL, TiDB, H2 |
| Frontend | HTML, CSS, Thymeleaf |
| Email | Gmail SMTP |
| Image Storage | Cloudinary |
| Build Tool | Maven |
| API Testing | Postman |
| Version Control | Git, GitHub |
| Containerization | Docker |
| Deployment | Render |

---

## 📁 Project Structure

```text
MySpringWeb/
│
├── pom.xml
├── Dockerfile
├── render.yaml
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── Configuration/
│   │   │   ├── Controller/
│   │   │   ├── RController/
│   │   │   ├── Service/
│   │   │   ├── Repository/
│   │   │   ├── Model/
│   │   │   ├── Exception/
│   │   │   └── MySpringWebApplication
│   │   │
│   │   └── resources/
│   │       ├── templates/
│   │       ├── static/
│   │       └── application.properties
│   │
│   └── test/
│
└── README.md
```

---

## 🗄️ Database Design

### UserTable

Stores:

- User ID
- Username
- Email
- BCrypt password

### ImageTable

Stores locally uploaded image data, including Base64 encoded content.

Relationship:

```text
UserTable
    │
    └── One To Many
             │
             ▼
        ImageTable
```

### ImageTable2

Stores Cloudinary image URLs.

Relationship:

```text
UserTable
    │
    └── One To Many
             │
             ▼
        ImageTable2
```

---

## 🔑 Authentication

### Session Authentication

```text
User Login
    │
    ▼
Verify Username
    │
    ▼
Verify Password
    │
    ▼
Create Session
    │
    ▼
Store Username
    │
    ▼
Access Protected Page
```

**Status:** ✅ Implemented

Current areas identified for improvement:

- Route protection
- Session fixation protection
- CSRF protection

### BCrypt Password Security

Passwords are encoded before being stored in the database.

```text
Raw Password
     │
     ▼
BCrypt Encoder
     │
     ▼
Hashed Password
     │
     ▼
Database
```

During login, the supplied password is checked against the stored BCrypt hash.

**Status:** ✅ Implemented

---

## 🪪 JWT Authentication

JWT is used for stateless authentication of REST APIs.

### JWT Flow

```text
Login Request
     │
     ▼
Validate User
     │
     ▼
Generate JWT
     │
     ▼
Return Token
     │
     ▼
Protected API Request
     │
     ▼
Validate JWT
     │
     ▼
Allow Access
```

### Main Components

- `AuthRestController`
- `JWUtil`
- `JwtAuthenticationFilter`
- `SecurityConfig`

**Status:** ✅ Implemented

### Possible Improvements

- Role-based authorization
- Refresh tokens
- Token revocation

---

## 📧 Email Integration

### Registration Email Flow

```text
Register User
     │
     ▼
Save User
     │
     ▼
Trigger Email
     │
     ▼
JavaMailSender
     │
     ▼
Gmail SMTP
```

### Features

- Welcome/registration email
- Asynchronous email sending

**Status:** ✅ Implemented

---

## ☁️ Cloudinary Integration

Images can be uploaded to Cloudinary and the resulting secure URL is stored in the database.

```text
Upload Image
     │
     ▼
Controller
     │
     ▼
Cloudinary
     │
     ▼
secure_url
     │
     ▼
Database
     │
     ▼
Gallery Display
```

**Status:** ✅ Implemented

### Possible Improvements

- File type and size validation
- Ownership validation
- Image deletion support

---

## 🔌 REST API

### Authentication

#### Login

```http
POST /api/auth/login
```

Example response:

```json
{
  "token": "jwt-token"
}
```

### Users

| Method | Endpoint | Purpose |
|---|---|---|
| `GET` | `/api/users` | Get all users |
| `GET` | `/api/users/{id}` | Get a user by ID |
| `POST` | `/api/users` | Create a user |
| `PUT` | `/api/users/{id}` | Update a user |
| `DELETE` | `/api/users/{id}` | Delete a user |

### Authentication Header

Protected endpoints expect:

```http
Authorization: Bearer <token>
```

---

## 📡 HTTP Status Codes

| Code | Meaning |
|---:|---|
| `200` | OK |
| `201` | Created |
| `204` | No Content |
| `401` | Unauthorized |
| `404` | Not Found |
| `500` | Internal Server Error |

---

## ⚙️ Setup & Configuration

### Prerequisites

Make sure the following are installed:

- Java 17
- Maven
- Git
- MySQL or another supported database
- Docker (optional)
- Postman (recommended for API testing)

### 1. Clone the Repository

```bash
git clone <your-repository-url>
cd MySpringWeb
```

### 2. Configure Environment Variables

#### Database

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

#### Email

```text
MAIL_USERNAME
MAIL_PASSWORD
```

#### JWT

```text
JWT_SECRET
```

#### Cloudinary

```text
CLOUDINARY_CLOUD_NAME
CLOUDINARY_API_KEY
CLOUDINARY_API_SECRET
```

> Keep secrets out of GitHub. Use environment variables or a local configuration file that is excluded from version control.

---

## 📬 Gmail SMTP Setup

1. Enable two-factor authentication on the Gmail account.
2. Generate a Gmail App Password.
3. Configure the mail credentials:

```text
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
```

4. Register a test user.
5. Verify that the email is delivered successfully.

---

## ☁️ Cloudinary Setup

1. Create a Cloudinary account.
2. Obtain your Cloudinary credentials.
3. Configure:

```text
CLOUDINARY_CLOUD_NAME
CLOUDINARY_API_KEY
CLOUDINARY_API_SECRET
```

4. Start the application.
5. Upload a test image and verify that the image URL is stored and displayed.

---

## 🔐 JWT Setup

Configure a strong JWT secret:

```text
JWT_SECRET=LongRandomSecretAtLeast32Bytes
```

Then test the authentication flow:

1. Log in.
2. Receive the JWT.
3. Send the token with protected API requests.

```http
Authorization: Bearer <token>
```

---

## 🐳 Docker

The project includes Docker support for consistent application packaging and deployment.

The Docker configuration is intended to:

- Use a Java 17 environment
- Install/build the application with Maven
- Copy the project into the image
- Expose port `8080`
- Run the generated JAR

Example build/run workflow:

```bash
docker build -t myspringweb .
docker run -p 8080:8080 myspringweb
```

---

## 🚀 Render Deployment

The project includes Render deployment configuration.

### Required Environment Variables

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

### Deployment Steps

1. Push the project to GitHub.
2. Create a Render Blueprint/Web Service.
3. Connect the GitHub repository.
4. Configure the required environment variables.
5. Deploy the application.
6. Verify the public application URL and API endpoints.

Render provides HTTPS, a public URL, GitHub integration, and automatic redeployment.

---

## 🧪 Postman Testing

### Step 1 — Login

```http
POST /api/auth/login
```

Store the returned JWT.

### Step 2 — Call a Protected API

```http
GET /api/users
```

Add:

```http
Authorization: Bearer <token>
```

### Step 3 — Test CRUD Operations

- Create User
- Read User
- Update User
- Delete User

---

## 📊 Feature Status

| Feature | Status |
|---|---|
| Registration | ✅ Implemented |
| Login | ✅ Implemented |
| Logout | ✅ Implemented |
| Session Authentication | ⚠️ Partial |
| BCrypt Passwords | ✅ Implemented |
| JWT Generation | ✅ Implemented |
| JWT Validation | ✅ Implemented |
| Spring Security | ⚠️ Partial |
| REST APIs | ✅ Implemented |
| CRUD Operations | ✅ Implemented |
| Database Connectivity | ✅ Configured |
| JPA Relationships | ⚠️ Partial |
| SMTP Email | ✅ Implemented |
| Cloudinary Upload | ✅ Implemented |
| Thymeleaf | ✅ Implemented |
| Docker | ✅ Implemented |
| Render Deployment | ✅ Implemented |

---

## 🛡️ Security Review

### Implemented

- ✅ BCrypt password hashing
- ✅ JWT authentication
- ✅ Spring Security
- ✅ Environment variable secrets
- ✅ Protected API endpoints

### Known Issues

- ❌ CSRF is disabled
- ❌ No role-based authorization
- ❌ No ownership checks
- ❌ Potential password-hash exposure through REST responses
- ❌ Request validation is incomplete

---

## 🔎 Known Issues & Improvements

### Critical

- Protect browser routes completely.
- Prevent password hashes from being returned by REST APIs.
- Review CSRF configuration for session-based browser authentication.

### High Priority

- Add a DTO layer.
- Add request/input validation.
- Add unique database constraints where required.
- Implement ownership-based authorization.
- Improve error responses.

### Medium Priority

- Store Cloudinary ownership information.
- Avoid parsing JWTs multiple times where possible.
- Add upload file type/size restrictions.

### Low Priority

- Replace field injection with constructor injection.
- Remove unused files.
- Perform general project cleanup.

---

## 🎯 Recommended Improvements Before Submission

The following items should be addressed before treating the application as production-ready:

1. Remove password hashes from API responses.
2. Add DTOs for API request/response models.
3. Review and enable appropriate CSRF protection for browser forms.
4. Protect all browser routes.
5. Add validation for incoming requests.
6. Add appropriate unique constraints.
7. Test all authentication and external integrations end-to-end.

---

## 📚 Learning Outcomes

This project demonstrates practical experience with:

- Java Enterprise Development
- Spring Boot
- Spring MVC
- Spring Security
- Session Management
- BCrypt Password Hashing
- JWT Authentication
- REST API Design
- JPA/Hibernate ORM
- SMTP Email Integration
- Cloudinary File Storage
- Docker Containerization
- Git/GitHub
- Maven
- Cloud Deployment with Render

---

## 📈 Project Summary

MySpringWeb brings together multiple enterprise technologies in one application:

```text
Java 17
   │
   ▼
Spring Boot
   │
   ├── Spring MVC ───────► Thymeleaf UI
   │
   ├── Spring Security ──► Session + JWT
   │
   ├── JPA/Hibernate ────► Database
   │
   ├── JavaMailSender ───► Gmail SMTP
   │
   └── Cloudinary ───────► Image Storage
             │
             ▼
          Docker
             │
             ▼
           Render
```

The core functionality is implemented and the project demonstrates the major concepts expected from an enterprise Java web application. Before production use or final submission, the main focus should be on **security hardening, route protection, validation, DTO implementation, and complete end-to-end testing**.

---

## 👨‍💻 Author

**Sulav Poudyal**

Built as an **Enterprise Web Systems Development** project.

---

⭐ If this project helped you learn Spring Boot and enterprise web development, consider giving the repository a star.
