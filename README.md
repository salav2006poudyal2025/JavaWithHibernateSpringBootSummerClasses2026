# MySpringWeb

A full-stack Java Spring Boot web application demonstrating Enterprise Web Development concepts including:

- User Registration & Login
- Session-Based Authentication
- BCrypt Password Security
- JWT Authentication & Authorization
- REST APIs with ResponseEntity
- Spring Security
- JPA/Hibernate ORM
- MySQL/TiDB Persistence
- SMTP Email Service
- Cloudinary Image Upload
- Thymeleaf Frontend
- Docker Containerization
- Render Deployment

This project was developed as part of Enterprise Web Systems Development and combines multiple enterprise technologies into a single real-world application.

---

# Project Overview

MySpringWeb provides both:

### Browser-Based Application
- Signup
- Login
- Logout
- User Management
- Image Gallery
- Email Services

### REST API Application
- JWT Login
- Protected CRUD APIs
- JSON Responses
- ResponseEntity Status Management

The system follows a layered architecture:

```text
Client (Browser/Postman)
          │
          ▼
     Controllers
          │
          ▼
       Services
          │
          ▼
    Repositories
          │
          ▼
      Database
```

---

# Main Features

## Authentication & Security

- Session-Based Login
- Session Logout
- JWT Authentication
- JWT Protected APIs
- Spring Security
- BCrypt Password Encryption

## User Management

- Create User
- View User
- Update User
- Delete User

## Email Integration

- Gmail SMTP
- Registration Email
- Asynchronous Email Sending

## Image Management

### Local Database Gallery

- Upload Images
- Store Base64 Data
- Display Gallery

### Cloudinary Gallery

- Upload to Cloudinary
- Store Secure URLs
- Display Uploaded Images

## REST APIs

- Secure Endpoints
- ResponseEntity Support
- Proper HTTP Status Codes
- Exception Handling

## Deployment

- Docker Support
- Render Deployment
- Environment Variable Configuration

---

# Technology Stack

## Backend

- Java 17
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Hibernate

## Database

- MySQL
- TiDB
- H2 Database

## Frontend

- HTML
- CSS
- Thymeleaf

## Security

- BCrypt
- JWT

## Third-Party Integrations

- Gmail SMTP
- Cloudinary

## DevOps

- Maven
- Docker
- Render
- GitHub

---

# Project Structure

```text
MySpringWeb
│
├── pom.xml
├── Dockerfile
├── render.yaml
│
├── src
│   ├── main
│   │   ├── java
│   │   │
│   │   ├── Configuration
│   │   ├── Controller
│   │   ├── RController
│   │   ├── Service
│   │   ├── Repository
│   │   ├── Model
│   │   ├── Exception
│   │   └── MySpringWebApplication
│   │
│   └── resources
│       ├── templates
│       ├── static
│       └── application.properties
│
└── test
```

---

# Database Design

## UserTable

Stores:

- User ID
- Username
- Email
- BCrypt Password

## ImageTable

Stores:

- Local Image Data
- Base64 Encoded Image

Relationship:

```text
UserTable
     │
     └── One To Many
              │
              ▼
         ImageTable
```

## ImageTable2

Stores:

- Cloudinary URL

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

# Authentication System

## Session Authentication

### Flow

```text
User Login
    │
    ▼
Verify Username
    │
Verify Password
    │
    ▼
Create Session
    │
Store Username
    │
Access Protected Page
```

### Current Status

✅ Implemented

### Improvement Needed

- Route Protection
- Session Fixation Protection
- CSRF Protection

---

# BCrypt Password Security

### Registration

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

### Login

```text
User Password
      │
      ▼
BCrypt Match
      │
      ▼
Authentication Result
```

### Status

✅ Implemented

---

# JWT Authentication

## JWT Flow

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

### Components

- AuthRestController
- JWUtil
- JwtAuthenticationFilter
- SecurityConfig

### Status

✅ Implemented

### Improvements

- Role-Based Authorization
- Refresh Tokens
- Token Revocation

---

# Email Service

## Registration Email Flow

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

- Welcome Email
- Async Sending

### Status

✅ Implemented

### Manual Testing Required

- Generate Gmail App Password
- Verify Delivery

---

# Cloudinary Integration

## Upload Flow

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

### Status

✅ Implemented

### Improvements

- File Validation
- Ownership Validation
- Delete Feature

---

# REST API Documentation

## Authentication

### Login

```http
POST /api/auth/login
```

Response:

```json
{
  "token": "jwt-token"
}
```

---

## Users

### Get All Users

```http
GET /api/users
```

### Get User By ID

```http
GET /api/users/{id}
```

### Create User

```http
POST /api/users
```

### Update User

```http
PUT /api/users/{id}
```

### Delete User

```http
DELETE /api/users/{id}
```

---

# HTTP Status Codes Used

| Code | Meaning |
|--------|--------|
| 200 | OK |
| 201 | Created |
| 204 | No Content |
| 401 | Unauthorized |
| 404 | Not Found |
| 500 | Internal Server Error |

---

# Security Implementation

## Current Security Features

✅ BCrypt Passwords

✅ JWT Authentication

✅ Spring Security

✅ Environment Variable Secrets

✅ Protected API Endpoints

## Current Security Issues

❌ CSRF Disabled

❌ No Roles

❌ No Ownership Checks

❌ Password Hash Exposure Risk

❌ Missing Request Validation

---

# Setup Guide

## 2. Configure Environment Variables

### Database

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

### Email

```text
MAIL_USERNAME
MAIL_PASSWORD
```

### JWT

```text
JWT_SECRET
```

### Cloudinary

```text
CLOUDINARY_CLOUD_NAME
CLOUDINARY_API_KEY
CLOUDINARY_API_SECRET
```

---

# Gmail SMTP Setup

1. Enable Two-Factor Authentication
2. Generate Gmail App Password
3. Configure:

```text
MAIL_USERNAME=your_email@gmail.com

MAIL_PASSWORD=your_app_password
```

4. Register User
5. Verify Email Delivery

---

# Cloudinary Setup

1. Create Cloudinary Account
2. Get Credentials

```text
CLOUDINARY_CLOUD_NAME

CLOUDINARY_API_KEY

CLOUDINARY_API_SECRET
```

3. Configure Environment Variables
4. Upload Test Image

---

# JWT Setup

Generate secure secret:

```text
JWT_SECRET=LongRandomSecretAtLeast32Bytes
```

Test:

1. Login
2. Get JWT
3. Call API with:

```http
Authorization: Bearer <token>
```

---

# Render Deployment

## Required Variables

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

## Steps

1. Push Code to GitHub
2. Create Render Blueprint
3. Connect Repository
4. Configure Variables
5. Deploy
6. Verify Application

---

# Postman Testing

## Step 1

Login:

```http
POST /api/auth/login
```

Store JWT.

---

## Step 2

Call Protected API:

```http
GET /api/users
```

Header:

```http
Authorization: Bearer <token>
```

---

## Step 3

Verify CRUD

- Create User
- Read User
- Update User
- Delete User

---

# Feature Validation Report

| Feature | Status |
|----------|----------|
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

# Error Analysis Report

## Critical

- Browser routes not fully protected
- Password hashes may be exposed through REST responses
- CSRF disabled while using sessions

## High

- Missing DTO layer
- Missing validation
- Missing unique constraints
- No ownership authorization
- Generic error messages

## Medium

- Cloudinary ownership not saved
- JWT parsed multiple times
- Upload restrictions missing

## Low

- Field Injection
- Unused Files
- Cleanup Needed

---

# Learning Outcomes

This project demonstrates:

- Java Enterprise Development
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
- Cloud Deployment using Render

---

# Must Fix Before Submission

1. Remove password hashes from API responses.
2. Add DTO layer.
3. Enable CSRF for browser forms.
4. Protect all browser routes.
5. Add validation.
6. Add unique constraints.

---

# Final Conclusion

MySpringWeb successfully demonstrates a complete Java Enterprise Web Application using Spring Boot, Spring Security, Hibernate, JWT, SMTP Email, Cloudinary, Docker, and Render Deployment.

The core functionality is implemented and educational objectives are achieved. Before production use or final submission, focus on security hardening, route protection, input validation, DTO implementation, and full end-to-end testing of all external integrations.