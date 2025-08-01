This document outlines the developer prompts used with GitHub Copilot to build a basic e-commerce backend in Spring Boot. 
It covers MySQL integration, JWT-based authentication and full CRUD operations for products.

1. Initial Project Setup
Create a new Spring Boot application with these dependencies:

spring-boot-starter-web

spring-boot-starter-data-jpa

spring-boot-starter-security

spring-boot-starter-validation

spring-boot-starter-test

mysql-connector-j

lombok

Configure application.properties:

text
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
Create the package structure:

text
src/main/java/com/yourorg/ecommerce/
├── config
├── controller
├── dto
├── entity
├── repository
├── security
└── service
2. User Registration & Authentication
2.1. Entity & Repository
User entity:

Fields: id, name, email, phone, password

Annotations: @Entity, @Table

Use Lombok: @Getter, @Setter, @NoArgsConstructor, @AllArgsConstructor

UserRepository:

java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
2.2. DTOs
RegisterRequest:

java
public class RegisterRequest {
    private String name;
    private String email;
    private String phone;
    private String password;
}
LoginRequest:

java
public class LoginRequest {
    private String email;
    private String password;
}
JwtResponse:

java
public class JwtResponse {
    private String jwtToken;
}
3. JWT Token Service
Create JwtService with methods:

java
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000;

    public String generateToken(UserDetails userDetails) { /* … */ }
    public String extractUsername(String token) { /* … */ }
    public boolean isTokenValid(String token, UserDetails userDetails) { /* … */ }
}
Use HS256 algorithm via io.jsonwebtoken.Jwts.

Load secret from application.properties or environment.

Set 24-hour expiration.

4. Authentication Controller
java
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    private final AuthenticationManager authManager;
    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) { /* … */ }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest req) { /* … */ }
}
/register: Check existsByEmail, encode password, save user.

/login: Authenticate via UsernamePasswordAuthenticationToken, generate JWT.

5. Spring Security Configuration
java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
    @Bean AuthenticationManager authManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtFilter) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
6. JWT Authentication Filter
java
@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsSvc;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        // Extract token, validate, set Authentication in SecurityContext
    }
}
7. Custom UserDetailsService
CustomUserDetails implements UserDetails:

java
public class CustomUserDetails implements UserDetails { /* wrap User entity */ }
CustomUserDetailsService:

java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // fetch User by email and return CustomUserDetails
    }
}
8. Product Entity & Repository
Product entity:

Fields: id, name, description, price, quantity

Validation: @NotBlank, @Size, @Positive

Annotations: @Entity, Lombok annotations

ProductRepository:

java
public interface ProductRepository extends JpaRepository<Product, Long> { }
9. Product Service & Controller
9.1. ProductService
java
@Service
public class ProductService {
    // createProduct, getAllProducts, getProductById, updateProduct, deleteProduct
}
9.2. ProductController
java
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    // @PostMapping("/"), @GetMapping("/"), @GetMapping("/{id}"),
    // @PutMapping("/{id}"), @DeleteMapping("/{id}") — secured endpoints
}
Require valid JWT for all /api/products/** routes.

10. Postman Test Plan
Register New User
POST http://localhost:8080/auth/register

json
{
  "name": "User One",
  "email": "user1@gmail.com",
  "phone": "1234567890",
  "password": "user123"
}
Login to Obtain JWT
POST http://localhost:8080/auth/login

json
{
  "email": "user1@gmail.com",
  "password": "user123"
}
Use Token

text
Authorization: Bearer <jwtToken>
Create Product
POST http://localhost:8080/api/products

json
{
  "name": "Laptop",
  "description": "High-end gaming laptop",
  "price": 150000,
  "quantity": 5
}
Get All Products
GET http://localhost:8080/api/products

Get Product by ID
GET http://localhost:8080/api/products/1

Update Product
PUT http://localhost:8080/api/products/1

json
{
  "name": "Laptop Pro",
  "description": "Upgraded model",
  "price": 180000,
  "quantity": 4
}
Delete Product
DELETE http://localhost:8080/api/products/1
