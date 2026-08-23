# MySpringWeb — Project Details

> This document contains the detailed implementation documentation for MySpringWeb. The root `README.md` is intentionally kept shorter and acts as the project entry point.

## 1. Project Overview

**MySpringWeb** is a Java-based enterprise web application developed with **Spring Boot**. The project combines a server-rendered web application, database persistence, user authentication, REST APIs, JWT-based API security, email notifications, image uploading, Cloudinary integration, automated testing, Docker containerization, and cloud deployment configuration.

The application was developed as a practical Spring/Enterprise Web Systems project to demonstrate how the different layers of a modern Java web application work together.

The main functionality implemented in this project includes:

- User registration with username, email, and password.
- BCrypt password hashing before passwords are stored in the database.
- User login using server-side HTTP sessions for the Thymeleaf web interface.
- User logout and session invalidation.
- User listing from the database.
- User editing and deletion from the web interface.
- User validation using Jakarta Validation annotations in the entity.
- REST API endpoints for user CRUD operations.
- JWT generation for REST API authentication.
- A custom JWT authentication filter.
- Protected `/api/**` endpoints.
- A public REST authentication endpoint at `/api/auth/login`.
- A local image gallery that stores uploaded image data in the database as Base64 text.
- A second image gallery using Cloudinary for external cloud image storage.
- Automatic welcome emails after successful user registration.
- Asynchronous email processing so registration does not have to wait for the email operation.
- Centralized exception handling for REST API errors.
- Unit testing for the email service.
- Spring application context testing.
- Maven-based build and dependency management.
- Docker multi-stage containerization.
- Render deployment configuration.
- Environment-variable-based configuration for database, email, JWT, and Cloudinary credentials.

---

## 2. Main Technologies Used

| Area | Technology |
|---|---|
| Programming Language | Java 17 |
| Application Framework | Spring Boot 4.1.0 |
| Web Framework | Spring MVC / Spring Web MVC |
| Template Engine | Thymeleaf |
| ORM | Hibernate |
| Persistence | Spring Data JPA |
| Database | MySQL-compatible database |
| Testing Database | H2 |
| Security | Spring Security |
| Password Hashing | BCrypt |
| API Authentication | JSON Web Token (JWT) |
| JWT Library | JJWT 0.11.5 |
| Image Storage | Database + Cloudinary |
| Email | Spring Mail / Gmail SMTP |
| Frontend Styling | HTML5, CSS3, Bootstrap, Font Awesome |
| Build Tool | Maven |
| Code Generation | Lombok |
| Containerization | Docker |
| Deployment Configuration | Render |
| Testing | JUnit 5 + Mockito |
| Version Control | Git |

---

# 3. What I Built

The project is not only a basic Spring Boot CRUD application. It contains several different parts that demonstrate how an enterprise-style application can be developed and connected together.

The application has two main ways of interacting with the backend:

1. **Browser-based web application**
   - Uses Spring MVC controllers.
   - Uses Thymeleaf templates.
   - Uses HTTP sessions for the browser login flow.
   - Provides pages for signup, login, home, user editing, galleries, and logout.

2. **REST API**
   - Uses `@RestController`.
   - Provides CRUD endpoints for users.
   - Uses JWT tokens for stateless API authentication.
   - Uses `ResponseEntity` to control HTTP response status codes.
   - Uses centralized exception handling.

This allows the same application to demonstrate both traditional server-rendered Spring MVC and REST API development.

---

# 4. Application Architecture

The application follows a layered/N-tier architecture.

```text
                         Browser / REST Client
                                  |
                    +-------------+-------------+
                    |                           |
              Web MVC Layer                REST API Layer
              (@Controller)              (@RestController)
                    |                           |
                    +-------------+-------------+
                                  |
                             Service Layer
                          (Business Logic)
                                  |
                            Repository Layer
                          (Spring Data JPA)
                                  |
                             Database
```

Additional infrastructure is connected to the application:

```text
                         MySpringWeb
                              |
       +----------------------+----------------------+
       |                      |                      |
   MySQL/TiDB            Cloudinary              Gmail SMTP
   User/Image DB         Image Storage           Email Service
```

### Controller Layer

The controller layer receives browser requests and API requests.

The project separates normal web controllers from REST controllers:

- `Controller/` contains Thymeleaf web controllers.
- `RController/` contains REST API controllers.

### Service Layer

The service layer contains the main user-related business logic.

For example:

- Saving users.
- Hashing passwords.
- Registering users.
- Authenticating users.
- Finding users.
- Deleting users.
- Triggering registration emails.

### Repository Layer

The repository layer uses Spring Data JPA to communicate with the database.

The repositories extend `JpaRepository`, which provides common database operations such as:

- Save
- Find by ID
- Find all
- Delete
- Other generated/custom queries

### Model Layer

The model classes are JPA entities. They represent database tables and relationships between users and uploaded images.

---

# 5. Project Entry Point

The application starts from:

```text
src/main/java/io/herald/MySpringWeb/MySpringWebApplication.java
```

The main class is:

```java
@SpringBootApplication
@EnableAsync
public class MySpringWebApplication
```

### `@SpringBootApplication`

This enables the main Spring Boot application configuration and component scanning.

### `@EnableAsync`

This enables asynchronous method execution.

It is important for the email functionality because the registration email method is marked with `@Async`.

The application starts with:

```java
SpringApplication.run(MySpringWebApplication.class, args);
```

---

# 6. User Registration

User registration is handled through:

```text
/signup
```

The registration page is:

```text
src/main/resources/templates/signup.html
```

The controller responsible for signup is:

```text
Controller/SignupController.java
```

## Registration flow

The user enters:

- Username
- Email
- Password

The form sends a `POST` request to:

```text
/signup
```

The controller:

1. Reads the submitted username.
2. Reads the submitted email.
3. Reads the submitted password.
4. Checks that all required values are present.
5. Checks whether the username already exists.
6. Creates a `UserTable` object.
7. Passes the user to `UserService`.
8. The service hashes the password.
9. The user is saved to the database.
10. A registration email is triggered asynchronously.
11. The browser is redirected to the login page.
12. A success message is displayed.

The username duplicate check is performed with:

```java
userService.findByUsername(username)
```

This prevents a second account from being created with the same username.

---

# 7. Password Security with BCrypt

Passwords are not intentionally stored as plain text.

The application defines a `PasswordEncoder` bean in:

```text
Configuration/SecurityConfig.java
```

using:

```java
new BCryptPasswordEncoder()
```

The actual password processing is performed in:

```text
Service/UserServiceImpl.java
```

When a user is saved, the service checks whether the password already appears to be a BCrypt hash.

If it is not already hashed, it is encoded before being saved.

```java
if (user.getPassword() != null && !user.getPassword().startsWith("$2")) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
}
```

This is important during updates because an existing BCrypt password should not be hashed again.

---

# 8. User Login

The browser login page is:

```text
/login
```

The login template is:

```text
src/main/resources/templates/login.html
```

The login logic is in:

```text
Controller/MappingClass.java
```

The login process is:

```text
Username + Password
        |
        v
MappingClass
        |
        v
UserService.authenticate()
        |
        v
UserRepository.findByUsername()
        |
        v
BCrypt password comparison
        |
        +---- Invalid ---> Login page with error
        |
        +---- Valid ----> Create HTTP session
                              |
                              v
                           /home
```

The actual password comparison uses:

```java
passwordEncoder.matches(password, user.getPassword())
```

This compares the raw password entered by the user with the stored BCrypt hash.

---

# 9. HTTP Session Authentication for the Web Interface

After successful browser login, the application creates an HTTP session.

The username is stored using:

```java
session.setAttribute("username", username);
```

The navigation bar displays the current session username.

The session is also used by some controllers to determine whether a user is logged in.

For example, the gallery and mail pages check:

```java
session.getAttribute("username")
```

If there is no username in the session, the user is sent back to the login page.

## Logout

The logout route is:

```text
GET /logout
```

The controller obtains the current session and invalidates it:

```java
session.invalidate();
```

The user is then redirected to:

```text
/login
```

---

# 10. User Model / Database Entity

The main user entity is:

```text
Model/UserTable.java
```

It represents the user record stored in the database.

The entity contains:

- `id`
- `username`
- `email`
- `password`
- `images`
- `cloudImages`

The ID is generated automatically:

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private int id;
```

The username and email fields contain validation annotations:

```java
@NotBlank
@Email
```

The password also has a `@NotBlank` constraint.

---

# 11. User-to-Image Relationships

A user can have multiple images.

The `UserTable` entity contains:

```java
@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
private List<ImageTable> images;
```

and:

```java
@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
private List<ImageTable2> cloudImages;
```

This represents a one-to-many relationship.

Conceptually:

```text
One User
   |
   +---- Many database images
   |
   +---- Many Cloudinary image records
```

`CascadeType.ALL` is configured for these relationships so that operations on the user can cascade to its associated image records.

---

# 12. Local Database Image Gallery

The project contains a gallery that stores image data directly in the database.

The page is:

```text
/gallery
```

The template is:

```text
templates/galleryPage.html
```

The controller is:

```text
Controller/GalleryController.java
```

The entity used is:

```text
Model/ImageTable.java
```

## Upload process

The form uses:

```html
enctype="multipart/form-data"
```

The controller receives the file as:

```java
MultipartFile image
```

The uploaded file is converted to bytes:

```java
byte[] imgBytes = image.getBytes();
```

Those bytes are then converted into a Base64 string:

```java
String imgString = Base64.getEncoder().encodeToString(imgBytes);
```

The Base64 value is stored in `ImageTable`.

The entity uses:

```java
@Lob
@Column(columnDefinition = "MEDIUMBLOB")
private String image;
```

The uploaded image is also linked to the currently logged-in user when a matching username exists in the session.

The application then retrieves the stored images and exposes them to the Thymeleaf page.

The page displays them using a data URL:

```text
data:image/jpeg;base64,...
```

This demonstrates one approach to storing image data inside the application database.

---

# 13. Cloudinary Image Gallery

The project also implements a second image storage approach using Cloudinary.

The page is:

```text
/gallery2
```

The template is:

```text
templates/galleryPage2.html
```

The entity is:

```text
Model/ImageTable2.java
```

Instead of storing the complete image content in the database, this entity stores:

```java
private String imageUrl;
```

## Cloudinary upload process

The controller receives the uploaded `MultipartFile`.

The file bytes are sent to Cloudinary:

```java
cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
```

Cloudinary returns an upload result.

The application extracts:

```text
secure_url
```

The returned HTTPS URL is then stored in `ImageTable2`.

The browser later displays the image using that URL.

The flow is:

```text
Browser
   |
   | Upload image
   v
GalleryController
   |
   v
Cloudinary
   |
   | secure_url
   v
ImageTable2
   |
   v
Database
   |
   v
Thymeleaf Gallery
```

### Difference between the two galleries

The project intentionally demonstrates two approaches:

**Local database gallery**

```text
Image file
   -> Base64
   -> Database
   -> Browser
```

**Cloudinary gallery**

```text
Image file
   -> Cloudinary
   -> secure URL
   -> Database stores URL
   -> Browser loads image from Cloudinary
```

The local gallery links uploaded images to the logged-in user.

The current Cloudinary upload implementation stores the Cloudinary image record but does not assign the `UserTable` relationship during upload.

---

# 14. Cloudinary Configuration

Cloudinary is configured in:

```text
Configuration/CloudinaryConfig.java
```

The configuration reads:

- Cloud name
- API key
- API secret

from application properties.

A `Cloudinary` object is registered as a Spring bean:

```java
@Bean
public Cloudinary cloudinary()
```

The configuration also enables secure HTTPS URLs.

This allows `GalleryController` to inject and use the configured Cloudinary instance.

---

# 15. Email System

The project includes an email service:

```text
Service/EmailService.java
```

It uses:

```text
Spring Mail
JavaMailSender
Gmail SMTP
```

The main email use case implemented in the project is the **registration welcome email**.

After a user successfully registers, the service calls:

```java
emailService.sendRegistrationEmail(...)
```

The email contains:

- A personalized greeting using the username.
- A registration success message.
- A closing from the MySpringWeb team.

The subject is:

```text
Welcome to MySpringWeb
```

---

# 16. Asynchronous Email Sending

The email method is annotated with:

```java
@Async
```

The application enables asynchronous execution with:

```java
@EnableAsync
```

This means the registration process can complete without making the browser wait for the email operation to finish.

The flow is:

```text
User Registration
      |
      v
Save User
      |
      v
Registration Complete
      |
      +--------------------+
      |                    |
      v                    v
Redirect to Login     Send Email Async
```

The email service also checks whether the recipient address is blank.

If it is blank, the email is skipped and a warning is logged.

Email failures are caught and logged so that a failure in the email provider does not undo an already completed user registration.

---

# 17. Mail Page

The application contains a mail page:

```text
/mail
```

and:

```text
templates/mailPage.html
```

The `MailController` checks whether a user has a valid web session before displaying the page.

The current mail page is primarily a frontend page/route. The actual implemented email sending functionality is handled by `EmailService`, particularly during user registration.

---

# 18. REST API

The project includes a separate REST API layer.

The main REST controller is:

```text
RController/RControllerClass.java
```

The API base path is:

```text
/api
```

A separate authentication controller is:

```text
RController/AuthRestController.java
```

with base path:

```text
/api/auth
```

---

# 19. REST API Endpoints

## Health Check

```http
GET /api/hello
```

Returns:

```text
Hello World
```

---

## Get All Users

```http
GET /api/users
```

Returns all registered users.

If there are no users, the controller returns:

```text
204 NO CONTENT
```

Otherwise it returns:

```text
200 OK
```

with the user list.

---

## Get User by ID

```http
GET /api/users/{id}
```

Example:

```http
GET /api/users/1
```

The controller searches for the requested user.

If found:

```text
200 OK
```

If the user does not exist, it throws:

```text
UserNotFoundException
```

which is converted into:

```text
404 NOT FOUND
```

by the global exception handler.

---

## Create User

```http
POST /api/users
```

The request body is mapped to `UserTable`.

The service handles saving the user, including password hashing and the registration email process.

The successful response is:

```text
201 CREATED
```

with:

```text
Saved Successfully
```

---

## Update User

```http
PUT /api/users/{id}
```

The controller:

1. Finds the existing user.
2. Updates the username.
3. Updates the email.
4. Updates the password only when a new password is provided.
5. Sends the updated entity through the service.
6. The service hashes a newly supplied raw password.
7. Returns the updated user.

If the user does not exist:

```text
404 NOT FOUND
```

---

## Delete User

```http
DELETE /api/users/{id}
```

The controller first checks whether the user exists.

If found:

```text
204 NO CONTENT
```

If not found:

```text
404 NOT FOUND
```

---

# 20. REST Authentication with JWT

The REST API uses JWT-based authentication.

The login endpoint is:

```http
POST /api/auth/login
```

The endpoint receives:

- username
- password

The credentials are verified through:

```text
UserService.authenticate()
```

If authentication succeeds, the application generates a JWT.

The response contains:

```json
{
  "token": "..."
}
```

If the credentials are invalid, the response is:

```text
401 UNAUTHORIZED
```

---

# 21. JWT Utility

JWT functionality is implemented in:

```text
Configuration/JWUtil.java
```

The class is responsible for:

- Creating signing keys.
- Generating JWTs.
- Extracting the username.
- Extracting expiration information.
- Extracting claims.
- Checking token expiration.
- Validating a token against a username.

The token uses:

```text
HS256
```

for signing.

The username is stored as the JWT subject.

The token also contains:

- Issued-at time.
- Expiration time.

The configured expiration is:

```text
86400000 milliseconds
```

which corresponds to:

```text
24 hours
```

---

# 22. JWT Authentication Filter

The custom filter is:

```text
Configuration/JwtAuthenticationFilter.java
```

It extends:

```java
OncePerRequestFilter
```

This means the filter is designed to run once for each request dispatch.

## Authentication process

The filter reads:

```text
Authorization
```

from the HTTP request header.

It expects:

```text
Authorization: Bearer <JWT>
```

The filter:

1. Checks whether the Authorization header exists.
2. Checks whether it starts with `Bearer `.
3. Removes the `Bearer ` prefix.
4. Extracts the username from the JWT.
5. Checks whether authentication already exists.
6. Validates the JWT.
7. Creates a Spring Security authentication object.
8. Places the authentication into the `SecurityContext`.
9. Continues the filter chain.

---

# 23. Spring Security Configuration

Security configuration is located in:

```text
Configuration/SecurityConfig.java
```

The application creates a `SecurityFilterChain`.

CSRF is disabled:

```java
http.csrf(csrf -> csrf.disable());
```

This configuration is appropriate for the project's REST/JWT approach, although the browser portion of the application still uses session-based login.

## Public routes

The following routes/resources are explicitly permitted:

```text
/
/login
/signup
/css/**
/js/**
/images/**
/api/auth/**
```

## Protected REST routes

All other:

```text
/api/**
```

routes require authentication.

The custom JWT filter is inserted before:

```text
UsernamePasswordAuthenticationFilter
```

This allows the application to establish JWT authentication before the rest of the Spring Security filter chain processes the request.

---

# 24. Two Authentication Approaches in the Project

An important part of this project is that it demonstrates two authentication mechanisms.

## Browser Web Application

The Thymeleaf frontend uses:

```text
HTTP Session
```

After successful login:

```text
username -> HttpSession
```

The browser uses that session while navigating through the web application.

## REST API

The REST API uses:

```text
JWT
```

The client first logs in and receives a token.

The client then sends:

```text
Authorization: Bearer <token>
```

for protected API requests.

Therefore:

```text
Web UI
  -> Session authentication

REST API
  -> JWT authentication
```

---

# 25. Exception Handling

The project implements centralized REST exception handling using:

```text
Exception/GlobalExceptionHandler.java
```

The class uses:

```java
@ControllerAdvice
```

This allows errors from controllers to be handled centrally.

---

## UserNotFoundException

A custom exception is defined:

```text
Exception/UserNotFoundException.java
```

It extends:

```java
RuntimeException
```

It is used when a requested user does not exist.

The global handler converts it into:

```text
404 NOT FOUND
```

---

## Validation Errors

The global exception handler also handles:

```text
ConstraintViolationException
```

and returns:

```text
400 BAD REQUEST
```

with the validation messages.

---

## Generic Errors

A fallback handler catches:

```text
Exception
```

and returns:

```text
500 INTERNAL SERVER ERROR
```

This gives the REST API a centralized error-response mechanism instead of putting the same exception logic into every controller method.

---

# 26. User CRUD from the Web Interface

The browser application provides basic user management.

The home page:

```text
/home
```

displays users retrieved from the database.

For each user, the page provides:

- User ID.
- Username.
- Edit action.
- Delete action.

## Edit

The edit process uses:

```text
POST /editUser
```

The controller finds the user by ID and displays:

```text
templates/editPage.html
```

The update form sends:

```text
POST /updateUser
```

The current web edit form updates:

- Username
- Email

It preserves the existing password and image relationships.

If the currently logged-in user's username is changed, the session username is also updated.

## Delete

The web interface sends:

```text
POST /deleteUser
```

The user is deleted through the service layer.

---

# 27. Repository Layer

The project has three main repositories.

## UserRepository

```text
Repository/UserRepository.java
```

Extends:

```java
JpaRepository<UserTable, Integer>
```

It provides standard user CRUD operations.

It also defines:

```java
findByUsername(String username)
```

which is used extensively for authentication and user lookup.

There is also an `existsByUsernameAndPassword(...)` query method in the repository.

---

## ImageRepository

```text
Repository/ImageRepository.java
```

Extends:

```java
JpaRepository<ImageTable, Integer>
```

It manages the locally stored image records.

---

## Image2Repository

```text
Repository/Image2Repository.java
```

Extends:

```java
JpaRepository<ImageTable2, Integer>
```

It manages Cloudinary image URL records.

---

# 28. Service Layer

The service interface is:

```text
Service/UserService.java
```

The implementation is:

```text
Service/UserServiceImpl.java
```

The interface defines operations for:

- Saving a user.
- Registering a user.
- Finding a user by ID.
- Finding a user by username.
- Finding all users.
- Deleting a user.
- Authenticating a user.

The implementation connects the controllers to the repository and keeps the business logic outside the controllers.

This makes the project easier to maintain because controllers do not need to directly contain database and password-processing logic.

---

# 29. Thymeleaf Frontend

The frontend is built with HTML and Thymeleaf.

The templates are located in:

```text
src/main/resources/templates/
```

The project contains the following pages:

| Template | Purpose |
|---|---|
| `firstPage.html` | Main landing page |
| `nextPage.html` | Additional navigation/test page |
| `login.html` | User login form |
| `login2.html` | Additional login template |
| `signup.html` | User registration form |
| `home.html` | User dashboard/list |
| `editPage.html` | User editing form |
| `galleryPage.html` | Database image gallery |
| `galleryPage2.html` | Cloudinary image gallery |
| `mailPage.html` | Mail-related frontend page |
| `navbar.html` | Shared navigation bar |

The templates use Thymeleaf expressions such as:

```text
th:href
th:text
th:if
th:each
th:action
th:src
```

These allow server-side data to be rendered into the HTML.

---

# 30. Frontend Styling

The main custom stylesheet is:

```text
src/main/resources/static/css/styles.css
```

The project also uses external frontend resources including:

- Bootstrap.
- Font Awesome.
- Google Fonts on the landing page.

The landing page contains a custom visual design with:

- Gradient background.
- Glass-style container.
- Animated decorative shapes.
- Responsive button layout.
- Login and signup navigation.
- Font Awesome icons.

The login and signup pages use a Bootstrap-based card layout and the project's custom stylesheet.

---

# 31. Shared Navigation Bar

The navigation is implemented in:

```text
templates/navbar.html
```

It provides links to:

- Home
- Database Gallery
- Cloudinary Gallery
- Mail
- Logout

It also displays the currently logged-in username using the session:

```text
session.username
```

The navigation template is inserted into several pages using Thymeleaf.

---

# 32. Application Configuration

The main configuration file is:

```text
src/main/resources/application.properties
```

The application uses environment variables for sensitive configuration.

The database settings use:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

This means credentials do not have to be hard-coded directly into the source code.

---

# 33. Database Configuration

The application is configured for a MySQL-compatible database.

The JDBC driver is:

```text
com.mysql.cj.jdbc.Driver
```

The connection values come from environment variables.

Hibernate is configured with:

```text
spring.jpa.hibernate.ddl-auto=update
```

This allows Hibernate to update the database schema based on the entity mappings.

SQL logging is disabled with:

```text
spring.jpa.show-sql=false
```

The project comment in `application.properties` references using a TiDB database, while the application connects through the standard MySQL-compatible JDBC driver.

---

# 34. Email Configuration

The application uses Gmail SMTP:

```text
smtp.gmail.com
```

Port:

```text
587
```

SMTP authentication and STARTTLS are enabled.

The following environment variables are used:

```text
MAIL_USERNAME
MAIL_PASSWORD
```

The password is intended to be a Gmail App Password rather than a normal Gmail account password.

---

# 35. JWT Configuration

JWT configuration uses:

```text
JWT_SECRET
```

The token expiration is configured as:

```text
jwt.expiration=86400000
```

The secret is supplied through an environment variable so that the signing key is not stored directly in source code.

---

# 36. Cloudinary Configuration

Cloudinary uses three environment variables:

```text
CLOUDINARY_CLOUD_NAME
CLOUDINARY_API_KEY
CLOUDINARY_API_SECRET
```

The values are mapped to:

```text
cloudinary.cloud-name
cloudinary.api-key
cloudinary.api-secret
```

inside the Spring configuration.

---

# 37. File Upload Limits

The application configures multipart upload limits:

```text
spring.servlet.multipart.max-file-size=20MB
spring.servlet.multipart.max-request-size=50MB
```

This allows individual uploaded files up to 20 MB while allowing a total multipart request size of up to 50 MB.

---

# 38. Maven Configuration

The project uses Maven.

The main Maven configuration is:

```text
pom.xml
```

The project uses Java:

```text
17
```

Important dependencies include:

- Spring Data JPA
- Spring Web MVC
- Spring Validation
- Spring Thymeleaf
- Spring Security
- Spring Mail
- MySQL Connector
- Cloudinary
- JJWT
- Lombok
- H2
- Spring Boot testing dependencies

The Spring Boot Maven plugin is used to build and package the application.

The Maven compiler plugin is configured with Lombok annotation processing.

---

# 39. Testing

The project contains automated tests under:

```text
src/test/
```

There are two main test classes.

## Application Context Test

```text
MySpringWebApplicationTests.java
```

This test uses:

```java
@SpringBootTest
```

and checks that the Spring application context can load successfully.

The included test report shows:

```text
Tests run: 1
Failures: 0
Errors: 0
Skipped: 0
```

---

# 40. Email Service Unit Tests

The email service is tested in:

```text
Service/EmailServiceTest.java
```

Mockito is used to create a mock:

```java
JavaMailSender
```

The tests verify that the email service behaves correctly without requiring a real Gmail SMTP connection.

### Test 1: Personalized registration email

The test verifies:

- Sender address.
- Recipient address.
- Subject.
- Personalized username in the email body.
- That `JavaMailSender.send()` is called.

### Test 2: Blank recipient

The test verifies that no email is attempted when the recipient address is blank.

The included test report shows:

```text
Tests run: 2
Failures: 0
Errors: 0
Skipped: 0
```

Together with the application context test, the included test results show:

```text
3 tests
0 failures
0 errors
```

---

# 41. H2 Database for Tests

The test configuration is:

```text
src/test/resources/application.properties
```

The tests use an in-memory H2 database:

```text
jdbc:h2:mem:testdb
```

This avoids requiring the real production database during tests.

The test configuration also supplies dummy values for:

- JWT secret.
- Cloudinary credentials.
- Mail credentials.

This allows the Spring application context to resolve required configuration placeholders during testing.

The test database uses:

```text
create-drop
```

so the test schema is created for the test run and removed afterward.

---

# 42. Docker Containerization

The project contains a:

```text
Dockerfile
```

It uses a multi-stage Docker build.

## Build stage

The build stage uses:

```text
maven:3.9-eclipse-temurin-17
```

The project is copied into the container and Maven builds the application:

```text
mvn clean package -DskipTests
```

## Runtime stage

The final image uses:

```text
eclipse-temurin:17-jre-alpine
```

Only the generated JAR is copied from the build stage.

The application exposes:

```text
8080
```

and starts with:

```text
java -jar app.jar
```

### Why a multi-stage build is used

The Maven build environment is only needed while compiling and packaging the application.

The final runtime container only needs the Java runtime and the generated JAR.

This keeps the runtime image more focused than putting the complete Maven build environment into the final container.

---

# 43. Render Deployment

The project contains:

```text
render.yaml
```

The deployment is configured as a Render web service using Docker.

The service is configured to use the:

```text
main
```

branch.

The environment variables configured for Render are:

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

These are marked as values that should be supplied separately rather than committed as plain-text secrets.

The deployment therefore follows this general flow:

```text
GitHub Repository
       |
       v
Render
       |
       v
Docker Build
       |
       v
Spring Boot Container
       |
       +---- Database
       |
       +---- Gmail SMTP
       |
       +---- Cloudinary
```

---

# 44. Running the Project Locally

## Prerequisites

Install:

- Java 17
- Git
- A MySQL-compatible database
- Maven, or use the included Maven Wrapper

For the external services, configure:

- Gmail SMTP/App Password.
- Cloudinary account.

---

## Environment Variables

Before running the application, provide:

```text
DB_URL=jdbc:mysql://localhost:3306/myspringweb
DB_USERNAME=your_database_username
DB_PASSWORD=your_database_password

MAIL_USERNAME=your_gmail_address
MAIL_PASSWORD=your_gmail_app_password

JWT_SECRET=your_long_random_jwt_secret

CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret
```

The exact database URL depends on the database provider being used.

Do not commit real passwords, API secrets, JWT secrets, or email credentials into the repository.

---

# 45. Run with Maven Wrapper

On Linux/macOS:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

---

# 46. Build the Application

The project can be packaged with Maven:

```bash
./mvnw clean package
```

On Windows:

```powershell
.\mvnw.cmd clean package
```

The generated JAR will be placed inside:

```text
target/
```

---

# 47. Run Tests

Run the tests with:

```bash
./mvnw test
```

On Windows:

```powershell
.\mvnw.cmd test
```

The tests use the H2 test database and mocked email sender where appropriate.

---

# 48. Docker Commands

Build the Docker image:

```bash
docker build -t myspringweb .
```

Run the container:

```bash
docker run -p 8080:8080 myspringweb
```

Because the application depends on external configuration, the required environment variables must also be provided when running the container.

---

# 49. Web Application Routes

The main browser routes implemented by the project are:

| Method | Route | Purpose |
|---|---|---|
| GET | `/` | Landing page |
| GET | `/nextPage` | Additional page |
| GET | `/login` | Display login page |
| POST | `/login` | Authenticate web user |
| GET | `/signup` | Display registration page |
| POST | `/signup` | Register new user |
| GET | `/home` | Display users/dashboard |
| GET | `/logout` | Invalidate session and logout |
| POST | `/editUser` | Open user edit page |
| POST | `/updateUser` | Update username/email |
| POST | `/deleteUser` | Delete a user |
| GET | `/gallery` | Database image gallery |
| POST | `/gallery` | Upload image to database |
| GET | `/gallery2` | Cloudinary gallery |
| POST | `/gallery2` | Upload image to Cloudinary |
| GET | `/mail` | Mail page |

---

# 50. REST API Route Summary

| Method | Endpoint | Purpose | Authentication |
|---|---|---|---|
| POST | `/api/auth/login` | Authenticate and receive JWT | Public |
| GET | `/api/hello` | API health/test endpoint | JWT |
| GET | `/api/users` | Get all users | JWT |
| GET | `/api/users/{id}` | Get user by ID | JWT |
| POST | `/api/users` | Create user | JWT |
| PUT | `/api/users/{id}` | Update user | JWT |
| DELETE | `/api/users/{id}` | Delete user | JWT |

The security configuration explicitly permits `/api/auth/**`, while the remaining `/api/**` routes require authentication.

---

# 51. Example JWT API Workflow

A REST client can follow this process:

### Step 1: Login

```http
POST /api/auth/login
```

Provide the username and password.

### Step 2: Receive the token

The server returns:

```json
{
  "token": "YOUR_JWT_TOKEN"
}
```

### Step 3: Use the token

For a protected request:

```http
GET /api/users
Authorization: Bearer YOUR_JWT_TOKEN
```

### Step 4: JWT filter validates the request

The custom filter extracts and validates the token.

If valid, the request is authenticated and can continue to the protected API controller.

---

# 52. HTTP Status Codes Implemented

The REST API uses meaningful HTTP status codes.

| Status | Meaning | Example |
|---|---|---|
| `200 OK` | Successful request | Get/update user |
| `201 CREATED` | New resource created | Create user |
| `204 NO CONTENT` | Successful request with no body | Empty user list/delete |
| `400 BAD REQUEST` | Validation error | Constraint violation |
| `401 UNAUTHORIZED` | Invalid authentication | Invalid API login |
| `404 NOT FOUND` | Requested user does not exist | Unknown user ID |
| `500 INTERNAL SERVER ERROR` | Unexpected server error | Unhandled exception |

`ResponseEntity` is used in the REST controllers to control these responses.

---

# 53. Important Security Decisions

Several security-related decisions were implemented in the project:

### Password hashing

Passwords are stored using BCrypt rather than raw passwords.

### JWT secret

The JWT signing secret comes from an environment variable.

### Database credentials

Database credentials come from environment variables.

### Mail credentials

Gmail credentials come from environment variables.

### Cloudinary credentials

Cloudinary API credentials come from environment variables.

### Protected API

The `/api/**` endpoints require authentication except for the authentication endpoints under `/api/auth/**`.

### Session logout

The browser session is explicitly invalidated during logout.

---

# 54. Validation

The `UserTable` entity uses Jakarta Validation annotations:

```java
@NotBlank(message = "Username is required")
```

```java
@Email(message = "Invalid email format")
```

```java
@NotBlank(message = "Email is required")
```

```java
@NotBlank(message = "Password is required")
```

These annotations define the expected validity rules for user data.

The global exception handler also contains support for `ConstraintViolationException` and converts validation violations into a `400 BAD REQUEST` response.

The browser signup controller additionally performs explicit checks for blank username, email, and password values before creating the user.

---

# 55. Error Handling in the Web Application

The web controllers also provide user-friendly redirect behavior for common situations.

Examples include:

- Missing login credentials.
- Invalid username/password.
- Missing signup fields.
- Username already in use.
- User not found during editing.
- Invalid user update fields.

Messages are passed through Spring's `RedirectAttributes` so they can be displayed after redirects.

For example, after a successful signup the application redirects to the login page and displays:

```text
Account created. Please sign in.
```

---

# 56. Separation of Responsibilities

One of the main architectural goals of the project is to avoid placing all functionality in one class.

The responsibilities are divided as follows:

```text
Model
  -> Represents data

Repository
  -> Handles database persistence

Service
  -> Handles business logic

Controller
  -> Handles web requests

REST Controller
  -> Handles API requests

Configuration
  -> Configures security/JWT/Cloudinary

Exception
  -> Handles application errors

Templates
  -> Provides web pages

Tests
  -> Verifies application behavior
```

This separation makes the application easier to understand, test, maintain, and extend.

---

# 57. Complete Project Structure

```text
MySpringWeb/
│
├── pom.xml
├── Dockerfile
├── render.yaml
├── README.md
├── LICENSE
├── ToDo.md
├── mvnw
├── mvnw.cmd
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── io/herald/MySpringWeb/
│   │   │       │
│   │   │       ├── MySpringWebApplication.java
│   │   │       │
│   │   │       ├── Configuration/
│   │   │       │   ├── CloudinaryConfig.java
│   │   │       │   ├── JWUtil.java
│   │   │       │   ├── JwtAuthenticationFilter.java
│   │   │       │   └── SecurityConfig.java
│   │   │       │
│   │   │       ├── Controller/
│   │   │       │   ├── GalleryController.java
│   │   │       │   ├── MailController.java
│   │   │       │   ├── MappingClass.java
│   │   │       │   ├── SignupController.java
│   │   │       │   └── UserController.java
│   │   │       │
│   │   │       ├── Exception/
│   │   │       │   ├── GlobalExceptionHandler.java
│   │   │       │   └── UserNotFoundException.java
│   │   │       │
│   │   │       ├── Model/
│   │   │       │   ├── ImageTable.java
│   │   │       │   ├── ImageTable2.java
│   │   │       │   └── UserTable.java
│   │   │       │
│   │   │       ├── RController/
│   │   │       │   ├── AuthRestController.java
│   │   │       │   └── RControllerClass.java
│   │   │       │
│   │   │       ├── Repository/
│   │   │       │   ├── Image2Repository.java
│   │   │       │   ├── ImageRepository.java
│   │   │       │   └── UserRepository.java
│   │   │       │
│   │   │       ├── Service/
│   │   │       │   ├── EmailService.java
│   │   │       │   ├── UserService.java
│   │   │       │   └── UserServiceImpl.java
│   │   │       │
│   │   │       └── test/
│   │   │           └── maptest.java
│   │   │
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       │   └── css/
│   │       │       └── styles.css
│   │       └── templates/
│   │           ├── editPage.html
│   │           ├── firstPage.html
│   │           ├── galleryPage.html
│   │           ├── galleryPage2.html
│   │           ├── home.html
│   │           ├── login.html
│   │           ├── login2.html
│   │           ├── mailPage.html
│   │           ├── navbar.html
│   │           ├── nextPage.html
│   │           └── signup.html
│   │
│   └── test/
│       ├── java/
│       │   └── io/herald/MySpringWeb/
│       │       ├── MySpringWebApplicationTests.java
│       │       └── Service/
│       │           └── EmailServiceTest.java
│       │
│       └── resources/
│           └── application.properties
│
└── target/
    └── Maven build/test output
```

---

# 58. File-by-File Responsibility

## Configuration

### `CloudinaryConfig.java`

Creates and configures the Cloudinary Spring bean using environment-based credentials.

### `JWUtil.java`

Creates, parses, and validates JWT tokens.

### `JwtAuthenticationFilter.java`

Reads Bearer tokens from HTTP requests and establishes Spring Security authentication when the JWT is valid.

### `SecurityConfig.java`

Configures password encoding, endpoint permissions, CSRF behavior, session/security behavior, and JWT filter ordering.

---

## Controllers

### `MappingClass.java`

Handles:

- Landing page.
- Next page.
- Login page.
- Web login.
- Home page.
- Logout.

### `SignupController.java`

Handles:

- Signup page.
- Signup form.
- Duplicate username check.
- User creation.
- Registration success redirect.

### `UserController.java`

Handles:

- User deletion.
- Opening the edit page.
- Updating username/email.
- Updating the active session username when required.

### `GalleryController.java`

Handles:

- Local database gallery.
- Local image upload.
- Cloudinary gallery.
- Cloudinary image upload.

### `MailController.java`

Handles the protected mail page.

---

## REST Controllers

### `AuthRestController.java`

Handles REST login and JWT generation.

### `RControllerClass.java`

Handles REST user CRUD and the `/api/hello` endpoint.

---

## Models

### `UserTable.java`

Represents application users and their relationships with image records.

### `ImageTable.java`

Stores locally uploaded image data.

### `ImageTable2.java`

Stores Cloudinary image URLs.

---

## Repositories

### `UserRepository.java`

Provides user database operations and username lookup.

### `ImageRepository.java`

Provides database operations for local image records.

### `Image2Repository.java`

Provides database operations for Cloudinary image records.

---

## Services

### `UserService.java`

Defines the business operations available for users.

### `UserServiceImpl.java`

Implements:

- Password hashing.
- User registration.
- Authentication.
- User lookup.
- User deletion.
- User persistence.
- Registration email triggering.

### `EmailService.java`

Builds and sends registration emails asynchronously through Gmail SMTP.

---

## Exceptions

### `UserNotFoundException.java`

Represents a requested user that does not exist.

### `GlobalExceptionHandler.java`

Converts application exceptions into consistent REST HTTP responses.

---

# 59. Development Features Demonstrated

This project demonstrates practical use of several Spring concepts:

- Spring Boot application startup.
- Dependency injection.
- Spring MVC.
- REST controllers.
- Spring Data JPA.
- Hibernate ORM.
- Entity mapping.
- Repository abstraction.
- Service interfaces and implementations.
- Thymeleaf server-side rendering.
- Multipart file uploads.
- Spring Security.
- BCrypt.
- HTTP sessions.
- JWT authentication.
- Custom servlet filters.
- Security filter ordering.
- `ResponseEntity`.
- Exception handling with `@ControllerAdvice`.
- Validation annotations.
- External API/service integration.
- Asynchronous execution.
- SMTP email sending.
- Cloud image storage.
- Maven.
- JUnit.
- Mockito.
- H2 testing database.
- Docker.
- Render deployment configuration.
- Environment-based configuration.

---

# 60. External Services Used

## Gmail SMTP

Used for sending registration welcome emails.

Configuration:

```text
smtp.gmail.com:587
```

## Cloudinary

Used for the second image gallery, where image files are uploaded to Cloudinary and the resulting secure URLs are stored in the database.

## MySQL-Compatible Database

Used for persistent storage of:

- Users.
- Local image data.
- Cloudinary image URLs.

## H2

Used as an in-memory database for testing.

## Render

Configured as the cloud deployment platform using the Docker container.

---

# 61. Important Project Design Choices

### Database image storage and Cloudinary storage are both demonstrated

The project does not rely on only one image-storage approach. It demonstrates the difference between storing image data in the database and storing image files externally while keeping their URLs in the database.

### Session and JWT authentication are both demonstrated

The browser interface uses HTTP sessions, while the REST API uses JWT authentication.

### Email is separated from registration logic

The actual email functionality is placed in `EmailService` rather than inside the signup controller.

### Database access is separated into repositories

Controllers and services do not need to manually write SQL for normal CRUD operations.

### Business logic is separated into services

The password hashing and authentication logic is handled by `UserServiceImpl`.

### External configuration is used for secrets

Database passwords, email credentials, JWT secrets, and Cloudinary credentials are not intended to be hard-coded.

---

# 62. Current Scope and Implementation Notes

The following points describe what is actually implemented in the current project:

- Browser login uses an HTTP session.
- REST API authentication uses JWT.
- The REST API requires JWT authentication for `/api/**`, except `/api/auth/**`.
- The database gallery stores image content as Base64 text in an entity column configured as `MEDIUMBLOB`.
- The Cloudinary gallery stores the returned secure image URL in `ImageTable2`.
- Local database gallery images are associated with the logged-in user during upload.
- The current Cloudinary upload method does not set the `UserTable` relationship for `ImageTable2`.
- The mail page exists as a protected frontend route, while actual email sending is implemented in `EmailService`.
- The current browser edit form changes username and email, not password.
- REST user updates can also accept a new password, which is then hashed by the service layer.
- The project includes validation annotations on `UserTable`.
- The REST exception handler supports validation violations, missing users, and generic exceptions.
- The project includes both application-context testing and email-service unit testing.
- Docker is configured as the deployment packaging method.
- Render is configured through `render.yaml`.

These details are documented to reflect the implementation in the project rather than describing features that are not currently present.

---

# 63. Final Project Workflow

The major application flow can be summarized as:

```text
                     USER
                      |
          +-----------+-----------+
          |                       |
       Sign Up                  Login
          |                       |
          v                       v
   SignupController        MappingClass
          |                       |
          v                       v
     UserService           UserService
          |                       |
          v                       v
     BCrypt Hash             BCrypt Match
          |                       |
          v                       v
     UserRepository          HTTP Session
          |                       |
          v                       v
       Database                 /home
          |
          +---------> Async Welcome Email
                           |
                           v
                       Gmail SMTP
```

For the REST API:

```text
REST Client
    |
    v
POST /api/auth/login
    |
    v
UserService.authenticate()
    |
    v
BCrypt verification
    |
    v
JWUtil.generateToken()
    |
    v
JWT returned to client
    |
    v
Authorization: Bearer <JWT>
    |
    v
JwtAuthenticationFilter
    |
    v
JWT validation
    |
    v
SecurityContext
    |
    v
Protected /api/** endpoint
    |
    v
UserService
    |
    v
UserRepository
    |
    v
Database
```

For image uploads:

```text
                  IMAGE UPLOAD
                       |
              +--------+--------+
              |                 |
              v                 v
        /gallery            /gallery2
              |                 |
              v                 v
          Base64           Cloudinary
              |                 |
              v                 v
        ImageTable        secure_url
              |                 |
              v                 v
          Database         ImageTable2
                                |
                                v
                             Database
```

---

# 64. Overall Result

MySpringWeb is a full Spring Boot web application that brings together the major concepts demonstrated in the project:

- Full server-side web application using Spring MVC and Thymeleaf.
- User registration and login.
- BCrypt password protection.
- HTTP session management.
- User CRUD operations.
- Spring Data JPA and Hibernate persistence.
- RESTful API design.
- JWT-based REST authentication.
- Custom JWT security filter.
- Centralized exception handling.
- Validation support.
- Local database image storage.
- Cloudinary image storage.
- Gmail SMTP registration emails.
- Asynchronous email processing.
- Automated tests with JUnit and Mockito.
- H2 database for testing.
- Maven build and dependency management.
- Docker containerization.
- Render deployment configuration.
- Environment-variable-based secret management.

The project therefore demonstrates the complete flow from the frontend request, through Spring controllers and business services, into database persistence and external services, while also providing a separate secured REST API and a deployable Docker-based application.

---

## Author

**Sulav Poudyal**
