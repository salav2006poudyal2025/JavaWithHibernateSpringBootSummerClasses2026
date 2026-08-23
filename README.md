# MySpringWeb

![Java 17](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Spring Boot 4.1.0](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen?style=for-the-badge&logo=springboot)
![Spring Security](https://img.shields.io/badge/Security-Spring%20Security-blue?style=for-the-badge&logo=springsecurity)
![Docker](https://img.shields.io/badge/Docker-Supported-2496ED?style=for-the-badge&logo=docker)

## 1. Project Overview
MySpringWeb is a robust, enterprise-grade full-stack Spring Boot web application. Designed to be highly scalable and secure, it elegantly bridges the gap between server-rendered browser interfaces (using Thymeleaf) and robust RESTful APIs (protected by JWT). It leverages a comprehensive suite of modern technologies, including Spring Data JPA, Cloudinary for external media, and Gmail SMTP for communication, all seamlessly packaged within a Docker container for effortless deployment.

## 2. Key Achievements & Capabilities
- **Dual Authentication System:** Implemented a sophisticated dual security layer featuring HTTP session-based browser logins for end-users and JWT-secured REST endpoints for headless clients.
- **Advanced Media Management:** Successfully integrated Cloudinary for robust cloud image storage alongside Base64 database storage, offering highly flexible media handling capabilities.
- **Asynchronous Communications:** Engineered an automated, non-blocking email notification system using Gmail SMTP to send instant welcome emails to new users.
- **Containerized DevOps Workflow:** Packaged the entire application and its dependencies into a Docker image, ensuring a consistent and environment-agnostic deployment process, currently optimized for Render.
- **Clean Architecture:** Built upon an N-Tier architecture (Controller, Service, Repository, Database), promoting exceptional separation of concerns, scalability, and clean code.

## 3. Technology Stack
- **Backend:** Java 17, Spring Boot 4.1.0, Spring MVC, Spring Security
- **Frontend:** Thymeleaf, HTML5, CSS3
- **Database:** MySQL-Compatible DB (Production), H2 (Testing)
- **ORM / Persistence:** Hibernate, Spring Data JPA
- **Security:** BCrypt Password Hashing, JJWT (0.11.5)
- **Cloud Integrations:** Cloudinary (Media), Gmail SMTP (Email)
- **DevOps:** Maven, Docker, Render

## 4. Architecture Overview
MySpringWeb is built on a clean, layered architectural pattern:
- **Presentation Layer:** Handles incoming requests through MVC Controllers (for browser views) and REST Controllers (for API clients).
- **Security Layer:** Intercepts traffic to enforce authentication using Spring Security and JWT Filters.
- **Service Layer:** Encapsulates the core business logic (`UserService`, `EmailService`).
- **Data Access Layer:** Manages database interactions via Spring Data JPA Repositories.
- **External Services:** Integrates with third-party providers (Cloudinary, Gmail SMTP).

## 5. Installation and Setup

### Prerequisites
- JDK 17
- Maven
- MySQL-compatible database

### Step-by-Step Configuration

#### 1. Database Credentials
Ensure your database is running and create a schema. You will need:
- `DB_URL` (e.g., `jdbc:mysql://localhost:3306/myspringweb`)
- `DB_USERNAME`
- `DB_PASSWORD`

#### 2. Gmail SMTP Setup (For Email Notifications)
To allow the application to send automated emails, you must configure a Gmail App Password:
1. Log in to your Google Account.
2. Navigate to **Manage your Google Account** > **Security**.
3. Ensure **2-Step Verification** is turned ON.
4. Go to **App passwords** (you might need to search for it in the settings search bar).
5. Select "Mail" for the app and "Other (Custom name)" for the device. Name it "MySpringWeb".
6. Click **Generate**. You will be given a 16-character password.
7. Save this password. In your environment variables, set:
   - `MAIL_USERNAME` = Your full Gmail address
   - `MAIL_PASSWORD` = The 16-character generated App Password (without spaces)

#### 3. Cloudinary Setup (For Image Uploads)
To enable cloud image storage, you need Cloudinary API credentials:
1. Go to [Cloudinary](https://cloudinary.com/) and create a free account.
2. Navigate to the **Dashboard**.
3. Copy the following credentials from your Account Details:
   - **Cloud Name**
   - **API Key**
   - **API Secret**
4. Set these in your environment variables:
   - `CLOUDINARY_CLOUD_NAME`
   - `CLOUDINARY_API_KEY`
   - `CLOUDINARY_API_SECRET`

#### 4. JWT Secret
Generate a strong, random 256-bit string to sign your JWTs securely.
- `JWT_SECRET` = Your random string (e.g., `aVeryLongAndSecureRandomSecretKeyThatIsAtLeast256Bits`)

### Running the Application

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/MySpringWeb.git
   cd MySpringWeb
   ```
2. **Set the Environment Variables** in your IDE, terminal, or `.env` file matching the names above.
3. **Build and Run:**
   ```bash
   ./mvnw spring-boot:run
   ```
   *For Windows, use `.\mvnw.cmd spring-boot:run`*

## 6. Usage
- **Web Interface:** Navigate to `http://localhost:8080` in your browser. Register, login, manage your profile, and explore the image galleries.
- **REST API:** Interact with the API at `http://localhost:8080/api`. Obtain a JWT via `POST /api/auth/login` and use it in the `Authorization: Bearer <token>` header for subsequent requests.

## 7. Project Structure
```text
src/
├── main/
│   ├── java/io/herald/MySpringWeb/
│   │   ├── Configuration/   # Security, JWT, Cloudinary settings
│   │   ├── Controller/      # Thymeleaf Web Controllers
│   │   ├── Exception/       # Global Error Handling
│   │   ├── Model/           # JPA Entities (UserTable, ImageTable)
│   │   ├── RController/     # RESTful API Controllers
│   │   ├── Repository/      # Database Interfaces
│   │   └── Service/         # Core Business Logic
│   └── resources/
│       ├── static/          # CSS, JS, Assets
│       ├── templates/       # Thymeleaf HTML Views
│       └── application.properties
└── test/                    # Automated Tests
```

## 8. License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## 9. Acknowledgements
- **Author:** Sulav Poudyal
- Built as a comprehensive Enterprise Web Systems Development project.