# Complete Study Notes: Java + Spring + Hibernate + Security + JWT + Email + Cloudinary + Deployment

## 1. Course Overview

**Enterprise Web Systems Development combines:**
- Java Programming Language
- Enterprise Application Development
- Spring Framework (Spring MVC / Spring Boot)
- Hibernate ORM / JPA

**Purpose:**
Build scalable, secure, maintainable, production-ready web applications using industry-standard architecture.

**Technology Stack:**
- Backend: Spring MVC / Spring Boot
- ORM: Hibernate / JPA
- Database: MySQL / PostgreSQL
- Frontend: HTML, CSS, JavaScript, Thymeleaf
- Tools: Maven, Git, GitHub, Postman
- Security: Spring Security, BCrypt, JWT
- Cloud Services: Cloudinary, Render

## 2. Java Fundamentals

### Java Is Platform Independent

Write Once Run Anywhere (WORA).

**Flow:**
1. Write Java Source Code (.java)
2. Compile using javac
3. Generate Bytecode (.class)
4. JVM executes bytecode

**Classes and Inheritance:**
- One class extends another.
- Child inherits parent's methods and fields.
- Java supports single inheritance.

**Interfaces:**
- Define behavior contracts.
- Classes implement interfaces.
- Supports multiple interface implementation.

**Inheritance vs Interface:**

**Inheritance:**
- Reuse existing code.
- One parent class only.

**Interface:**
- Define required methods.
- Multiple interfaces possible.

## 3. Exception Handling

**Purpose:**
Handle runtime errors gracefully.

**Keywords:**
1. try
2. catch
3. finally
4. throw
5. throws

**try:**
Contains code that may generate an exception.

**catch:**
Handles exceptions.

**finally:**
Always runs.
Used for cleanup.

**throw:**
Manually creates an exception.

**throws:**
Declares potential exceptions.

**Common Exceptions:**
- ArithmeticException
- NullPointerException
- NumberFormatException
- ArrayIndexOutOfBoundsException
- InputMismatchException

**User Defined Exception:**
Create custom exception classes by extending Exception.

## 4. Byte Streams and Character Streams

**Byte Streams:**
- Work with binary data.
- Images
- Audio
- Videos
- Files

**Classes:**
- FileInputStream
- FileOutputStream

**Character Streams:**
- Work with text.
- Handle encoding automatically.

## 5. N-Tier Architecture in Spring

**Controller Layer:**
- Receives HTTP requests.
- Sends HTTP responses.

**Service Layer:**
- Contains business logic.

**Repository Layer:**
- Interacts with database.

**Benefits:**
- Separation of concerns
- Maintainability
- Scalability
- Clean code

## 6. Hibernate and JPA

**Purpose:**
Map Java Objects to Database Tables.

**Features:**
- CRUD Operations
- Entity Mapping
- Relationship Mapping

**Relationships:**
- One to One
- One to Many
- Many to Many

**Annotations:**
- @Entity
- @Id
- @GeneratedValue

## 7. REST Controllers

`@RequestMapping("/api")`

Used at controller level.
All routes start with /api.

`@PathVariable`

Extract values from URL.
**Example:**
`/api/users/12`

12 becomes parameter value.

**Problem:**
If ID not found, generic errors may appear.

**Solution:**
ResponseEntity.

## 8. ResponseEntity

**Provides:**
- HTTP Status Control
- Custom Response Bodies
- Error Handling

### HTTP Status Codes

**200 OK**
Request successful.

**201 CREATED**
Resource created.
Usually POST.

**204 NO CONTENT**
Success but no response body.

**400 BAD REQUEST**
Invalid client input.

**401 UNAUTHORIZED**
Login required.

**403 FORBIDDEN**
Permission denied.

**404 NOT FOUND**
Resource does not exist.

**500 INTERNAL SERVER ERROR**
Unexpected server problem.

## 9. Spring Security

**Dependency:**
Spring Security Starter.

**SecurityConfig:**
Creates SecurityFilterChain.

**SecurityFilterChain:**
Controls security rules for the entire application.

**Default Behavior:**
- Username: user
- Password generated in console.

**Development Configuration:**
Disable CSRF.
Permit all requests.

**Lambda Usage:**
`http.csrf(csrf -> csrf.disable())`

## 10. BCrypt Password Encoder

**Purpose:**
Hash passwords securely.

**Implementation:**
Create PasswordEncoder Bean.
Return BCryptPasswordEncoder.

**Signup:**
1. Receive password.
2. Encode password.
3. Store encoded password.

**Login:**
1. Find user.
2. Compare raw password with encoded password.
3. Authenticate.
4. Create session.

**Benefits:**
- Passwords never stored as plain text.
- Strong security.

## 11. Session Authentication

**Flow:**
1. User logs in.
2. Credentials validated.
3. Session created.
4. Session stored on server.
5. User accesses protected pages.

## 12. Java Email Service

**Purpose:**
Send automated emails.

**Setup:**
1. Add Java Mail Sender dependency.
2. Reload Maven.
3. Configure application.properties.
4. Use Gmail App Password.
5. Autowire JavaMailSender.

**Use Cases:**
- Registration confirmation
- Login alerts
- Notifications
- Password reset emails

## 13. JWT Authentication

JWT = JSON Web Token

**Dependencies:**
- jjwt-api
- jjwt-impl
- jjwt-jackson

**Purpose:**
Stateless authentication.

**Token Generation:**
User logs in.
Server generates token.
Client stores token.

**Subsequent Requests:**
Client sends token.
Server validates token.

**JWT Utility Responsibilities:**
- Generate Token
- Extract Username

Token contains user identity information.

## 14. JWT Filter

JwtAuthenticationFilter extends OncePerRequestFilter.

Runs for every request.

**Process:**
1. Read token header.
2. Check Bearer token.
3. Extract token.
4. Extract username.
5. Validate token.
6. Set authentication.
7. Continue filter chain.

**Header Format:**
`Token: Bearer <jwt-token>`

'Bearer ' including space equals 7 characters.
Token extracted using substring(7).

## 15. JWT Security Configuration

**Security Rules:**
- Allow login endpoints.
- Protect /api/** endpoints.

**Filter Ordering:**
```java
http.addFilterBefore(
jwtAuthFilter,
UsernamePasswordAuthenticationFilter.class
)
```

**Flow:**
1. JWT Filter executes first.
2. Token validated.
3. Authentication added.
4. Remaining security filters execute.

## 16. Cloudinary Image Upload

**Purpose:**
Store images in cloud.

**Flow:**
1. User uploads image.
2. Controller receives MultipartFile.
3. Upload sent to Cloudinary.
4. Cloudinary stores image.
5. secure_url returned.
6. URL saved in database.
7. Display image using stored URL.

**Important Classes:**
- Model(Entity)
- Repository
- Controller
- CloudinaryConfig

**Annotations:**
`@Entity`
`@Repository`
`@Controller`
`@Configuration`
`@Bean`
`@Autowired`

**Multipart Upload Form:**
`enctype="multipart/form-data"`

## 17. Spring Configuration and Beans

**`@Configuration`:**
Defines configuration class.

**`@Bean`:**
Registers object into Spring Container.

**Why Needed:**
Allows Spring to manage third-party libraries such as Cloudinary.

## 18. API Testing with Postman

**Used For:**
- GET Requests
- POST Requests
- PUT Requests
- DELETE Requests
- JWT Testing
- Response Verification

## 19. Git and GitHub

Version Control System.

**Best Practices:**
- Create repository.
- Commit frequently.
- Write meaningful messages.
- Push regularly.

**Common Operations:**
- Clone
- Commit
- Push
- Pull

## 20. Maven Build Process

**Purpose:**
Build project artifacts.

**Lifecycle:**
1. Compile
2. Test
3. Package

**Command:**
package

**Output:**
Generated JAR inside target folder.

## 21. Docker

**Purpose:**
Containerize applications.

**Benefits:**
- Consistent environments
- Easy deployment
- Portable execution

**Dockerfile Tasks:**
- Use Java 17 image
- Install Maven
- Copy project
- Build project
- Expose port 8080
- Run JAR file

## 22. Render Deployment

**Purpose:**
Deploy Spring applications to cloud.

**Steps:**
1. Push code to GitHub.
2. Open Render.
3. Create Web Service.
4. Connect repository.
5. Select Docker runtime.
6. Deploy.

**Benefits:**
- HTTPS
- Public URL
- Automatic redeployment
- GitHub integration

## 23. Final Course Competencies

**After completing the course a student should be able to:**

- Build enterprise web systems.
- Design N-tier architectures.
- Create REST APIs.
- Use Spring MVC.
- Use Hibernate/JPA.
- Implement authentication.
- Use BCrypt hashing.
- Use JWT authorization.
- Send emails using SMTP.
- Upload images using Cloudinary.
- Test APIs with Postman.
- Manage source code with Git.
- Package applications with Maven.
- Containerize applications using Docker.
- Deploy applications to Render.
- Build complete full-stack Java web applications.
