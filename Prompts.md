This document outlines the developer prompts used with GitHub Copilot to build a basic e-commerce backend in Spring Boot. 
It covers MySQL integration, JWT-based authentication and full CRUD operations for products.
Project Setup

1. Maven Project Setup
Prompt:
Create a Maven pom.xml file for Spring Boot project named 'productapp':
- Group ID: com.productapp
- Java 17, Spring Boot 3.1.0
- Dependencies: spring-boot-starter-web, spring-boot-starter-data-jpa, spring-boot-starter-security, mysql-connector-j, jjwt-api:0.11.5, jjwt-impl:0.11.5, jjwt-jackson:0.11.5
- Include spring-boot-maven-plugin


 JWT Security Implementation

2. JWT Service Class
Prompt:
Create JwtService class in com.productapp.security package:
- @Service annotation
- String secret key for JWT signing
- Method generateToken(String username) - creates JWT with 24 hour expiration
- Method extractUsername(String token) - extracts username from JWT
- Method validateToken(String token, String username) - validates JWT token
- Use io.jsonwebtoken.Jwts for JWT operations
3. JWT Authentication Filter
Prompt:
Create JwtAuthFilter class extending OncePerRequestFilter in com.productapp.security:
- Inject JwtService and UserDetailsService
- Override doFilterInternal method to extract JWT from Authorization header
- If valid token, set authentication in SecurityContext
- Add @Component annotation
4. Security Configuration
Prompt:
Create SecurityConfig class in com.productapp.security:
- @Configuration and @EnableWebSecurity annotations
- Configure SecurityFilterChain bean to permit /auth/** endpoints and secure others
- Disable CSRF, set session to STATELESS
- Add JwtAuthFilter before UsernamePasswordAuthenticationFilter
- Create PasswordEncoder bean (BCryptPasswordEncoder)
- Create AuthenticationManager bean

 User Management
5. User Entity
Prompt:
Create User entity in com.productapp.entity:
- @Entity and @Table(name = "users")
- Fields: Long id, String name, String email, String password
- @Id @GeneratedValue for id
- @Column(unique = true) for email
- Include constructors, getters, setters
6. User Repository
Prompt:
Create UserRepository interface in com.productapp.repository:
- Extend JpaRepository<User, Long>
- Method: Optional<User> findByEmail(String email)
- @Repository annotation
7. Custom UserDetailsService
Prompt:
Create CustomUserDetailsService in com.productapp.security implementing UserDetailsService:
- @Service annotation
- Inject UserRepository
- Override loadUserByUsername method to find user by email
- Return User.builder().username(user.getEmail()).password(user.getPassword()).authorities("USER").build()
8. Authentication DTOs
Prompt:
Create in com.productapp.dto package:
1. LoginRequest class with String email and String password fields
2. RegisterRequest class with String name, String email, String password fields  
3. JwtResponse class with String token field
Include constructors, getters, setters for all
9. Auth Controller
Prompt:
Create AuthController in com.productapp.controller:
- @RestController and @RequestMapping("/auth")
- Inject UserRepository, PasswordEncoder, AuthenticationManager, JwtService
- POST /register endpoint - save user with encoded password
- POST /login endpoint - authenticate user and return JWT token
- Return appropriate ResponseEntity responses


 Product CRUD Implementation

10. Product Entity
Prompt:
Create Product entity in com.productapp.entity:
- @Entity and @Table(name = "products")
- Fields: Long id, String name, String category, Double price, String description
- @Id @GeneratedValue for id
- Include constructors, getters, setters
11. Product Repository
Prompt:
Create ProductRepository interface in com.productapp.repository:
- Extend JpaRepository<Product, Long>
- @Repository annotation
12. Product Service
Prompt:
Create ProductService class in com.productapp.service:
- @Service annotation
- Inject ProductRepository
- Methods: getAllProducts(), getProductById(Long id), saveProduct(Product product), updateProduct(Long id, Product product), deleteProduct(Long id)
- Include exception handling for product not found
13. Product Controller
Prompt:
Create ProductController in com.productapp.controller:
- @RestController and @RequestMapping("/products")
- Inject ProductService
- Endpoints: GET /, GET /{id}, POST /, PUT /{id}, DELETE /{id}
- All endpoints require authentication (JWT token)
- Return appropriate ResponseEntity responses

Configuration

14. Application Properties
Prompt:
Create application.properties file:
spring.datasource.url=jdbc:mysql://localhost:3306/productdb
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080
15. Main Application Class
Prompt:
Create ProductAppApplication class in com.productapp:
- @SpringBootApplication annotation
- Main method with SpringApplication.run(ProductAppApplication.class, args)
