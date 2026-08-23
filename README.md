# MySpringWeb

![Java 17](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk)
![Spring Boot 4.1.0](https://img.shields.io/badge/Spring%20Boot-4.1.0-brightgreen?style=for-the-badge&logo=springboot)
![Spring Security](https://img.shields.io/badge/Security-Spring%20Security-blue?style=for-the-badge&logo=springsecurity)
![Docker](https://img.shields.io/badge/Docker-Supported-2496ED?style=for-the-badge&logo=docker)

## 1. Project Title
**MySpringWeb** - Enterprise Web Systems Development Project

## 2. Project Overview
MySpringWeb is a robust, enterprise-grade full-stack Spring Boot web application. Designed to be scalable and secure, it elegantly bridges the gap between server-rendered browser interfaces (using Thymeleaf) and robust RESTful APIs (protected by JWT). It leverages a comprehensive suite of modern technologies, including Spring Data JPA, Cloudinary for external media, and Gmail SMTP for communication, all seamlessly packaged within a Docker container for effortless deployment.

## 3. Problem Statement
Modern enterprise applications require dual interfaces: a dynamic, user-friendly frontend for direct human interaction, and a secure, scalable API for third-party integrations and headless clients. Developing an application that successfully manages both aspects while adhering to robust security protocols, efficient media management, and scalable cloud deployment can be highly complex and challenging.

## 4. Solution
MySpringWeb delivers a comprehensive solution by providing a highly structured, layered web architecture. It perfectly orchestrates a Thymeleaf-powered frontend with a JWT-secured backend REST API. By integrating cloud services like Cloudinary for optimized media delivery and establishing a seamless Dockerized workflow, the application offers a unified, secure, and easily deployable ecosystem ready for enterprise demands.

## 5. Features
- 🔐 **Robust Authentication:** Dual security layers featuring HTTP session-based browser logins and JWT-secured REST endpoints.
- 👤 **Comprehensive User Management:** Secure registration with BCrypt password hashing, profile editing, and deletion.
- 🖼️ **Advanced Media Management:** Flexible image upload capabilities, supporting both Base64 database storage and secure Cloudinary cloud integration.
- 📧 **Automated Communications:** Asynchronous welcome emails powered by Gmail SMTP.
- 🚀 **RESTful API:** Fully functional API supporting CRUD operations for headless integrations.
- 🐳 **Containerized Deployment:** Docker support with multi-stage builds for consistent, environment-agnostic deployment.

## 6. Technology Stack
- **Backend:** Java 17, Spring Boot 4.1.0, Spring MVC, Spring Security
- **Frontend:** Thymeleaf, HTML5, CSS3
- **Database:** MySQL-Compatible DB (Production), H2 (Testing)
- **ORM / Persistence:** Hibernate, Spring Data JPA
- **Security:** BCrypt, JJWT (0.11.5)
- **Cloud Integrations:** Cloudinary (Media), Gmail SMTP (Email)
- **DevOps:** Maven, Docker, Render

## 7. Architecture Overview
MySpringWeb is built on a clean, layered architectural pattern:
- **Presentation Layer:** Handles incoming requests through MVC Controllers (for browser views) and REST Controllers (for API clients).
- **Security Layer:** Intercepts traffic to enforce authentication using Spring Security and JWT Filters.
- **Service Layer:** Encapsulates the core business logic (`UserService`, `EmailService`).
- **Data Access Layer:** Manages database interactions via Spring Data JPA Repositories.
- **External Services:** Integrates with third-party providers (Cloudinary, Gmail SMTP).

## 8. Workflow
1. **User Access:** The client (browser or API) sends a request.
2. **Security Gateway:** The request passes through the security filters (Session checks for web, JWT validation for APIs).
3. **Controller Routing:** The request is routed to the appropriate controller (MVC or REST).
4. **Business Logic Execution:** The Controller delegates the task to the Service Layer.
5. **Data Persistence:** The Service interacts with the Repository to fetch or persist data in MySQL.
6. **Response Generation:** The final response (a Thymeleaf view or JSON payload) is returned to the client.

## 9. Key Benefits
- ⚡ **High Performance:** Optimized backend and asynchronous processing ensure snappy response times.
- 🛡️ **Enterprise Security:** Industry-standard security practices protect user data.
- 📈 **Scalability:** The layered architecture and Docker containerization make it easy to scale.
- 🔄 **Flexibility:** Seamlessly serves both web users and API consumers.
- ☁️ **Cloud-Ready:** Native support for cloud databases, media storage, and hosting platforms.

## 10. Project Structure
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
└── test/                    # H2-backed Automated Tests
```

## 11. Installation
Ensure you have **JDK 17**, **Maven**, and a **MySQL-compatible database** installed.

1. **Clone the repository:**
   ```bash
   git clone https://github.com/yourusername/MySpringWeb.git
   cd MySpringWeb
   ```
2. **Configure Environment Variables:**
   Set the following variables: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, `MAIL_USERNAME`, `MAIL_PASSWORD`, `JWT_SECRET`, `CLOUDINARY_CLOUD_NAME`, `CLOUDINARY_API_KEY`, `CLOUDINARY_API_SECRET`.

3. **Build and Run:**
   ```bash
   ./mvnw spring-boot:run
   ```
   *For Windows, use `.\mvnw.cmd spring-boot:run`*

## 12. Usage
- **Web Interface:** Navigate to `http://localhost:8080` in your browser. Register, login, manage your profile, and explore the image galleries.
- **REST API:** Interact with the API at `http://localhost:8080/api`. Obtain a JWT via `POST /api/auth/login` (using `application/x-www-form-urlencoded`) and use it in the `Authorization: Bearer <token>` header for subsequent requests to `/api/users`.

## 13. Screenshots Placeholder
> *📸 (Insert screenshots of the login page, dashboard, and gallery interface here)*

## 14. Future Roadmap
- 🛡️ Centralize and enhance browser authorization.
- 📦 Introduce comprehensive DTOs for improved API data handling.
- 🧪 Expand automated test coverage for end-to-end flows.
- ☁️ Introduce CI/CD pipelines for automated deployment.
- 🔄 Add password reset and advanced account management features.

## 15. Contribution Guidelines
We welcome contributions! To contribute:
1. Fork the repository.
2. Create a new branch (`git checkout -b feature/AmazingFeature`).
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`).
4. Push to the branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

## 16. License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## 17. Acknowledgements
- Built as an Enterprise Web Systems Development project.
- **Author:** Sulav Poudyal
- Special thanks to the open-source community for Spring Boot, Thymeleaf, and Docker.