# Spring Security + JWT Internal Flow Guide (Deep Explanation)

This document explains the COMPLETE INTERNAL FLOW of Spring Security with JWT authentication.

This guide focuses on:

• What happens internally
• Which class executes first
• Which class executes next
• How Spring Security works internally
• How JWT filter integrates into Spring Security

This is critical for:

• Interviews
• Debugging
• Deep understanding
• Production usage

---

# SECTION 1: Application Startup Flow

When Spring Boot starts:

Step 1: Spring scans all classes

Annotations scanned:

@Configuration
@Service
@Repository
@Component
@RestController

Spring creates beans for:

SecurityConfig
JwtService
JwtAuthenticationFilter
CustomUserDetailsService
AuthService
AuthController

These beans are stored in Spring Container.

---

# SECTION 2: Security Initialization Flow

SecurityConfig is initialized.

Spring executes:

```
@Bean
SecurityFilterChain
```

Spring creates Security Filter Chain.

Filter Chain contains multiple filters:

Important filters:

SecurityContextHolderFilter
JwtAuthenticationFilter (your custom filter)
UsernamePasswordAuthenticationFilter
AuthorizationFilter

Your JWT filter is inserted BEFORE UsernamePasswordAuthenticationFilter.

This is critical.

---

# SECTION 3: Register Flow (Complete Internal Execution)

Client sends request:

POST /auth/register

Step 1:

Request reaches DispatcherServlet.

DispatcherServlet is front controller of Spring.

Step 2:

Request goes through Security Filter Chain.

JwtAuthenticationFilter executes.

Since this endpoint is permitted (/auth/**), authentication not required.

Step 3:

Request reaches AuthController.

Spring calls:

AuthController.register()

Step 4:

AuthService.register() executes.

Step 5:

PasswordEncoder.encode(password)

Password is converted to hash.

Example:

1234 → $2a$10$hashvalue

Step 6:

UserRepository.save(user)

Hibernate executes SQL:

INSERT INTO users

Step 7:

JwtService.generateToken()

JWT token created.

Step 8:

Token returned to client.

---

# SECTION 4: Login Flow (Complete Internal Execution)

Client sends:

POST /auth/login

Step 1:

DispatcherServlet receives request.

Step 2:

Request goes through Security Filter Chain.

Step 3:

AuthController.login() called.

Step 4:

AuthService.login() executes.

Step 5:

AuthenticationManager.authenticate() executes.

AuthenticationManager delegates to:

DaoAuthenticationProvider

Step 6:

DaoAuthenticationProvider calls:

CustomUserDetailsService.loadUserByUsername()

Step 7:

UserRepository.findByUsername()

User loaded from database.

Step 8:

PasswordEncoder.matches()

Compares entered password and database password.

If matches → success

Step 9:

JwtService.generateToken()

Token returned.

---

# SECTION 5: Protected Request Flow (MOST IMPORTANT)

Client sends:

GET /api/test
Authorization: Bearer JWT_TOKEN

Step 1:

DispatcherServlet receives request.

Step 2:

Security Filter Chain starts.

Step 3:

JwtAuthenticationFilter executes.

This is FIRST important step.

Step 4:

Filter extracts header:

Authorization: Bearer TOKEN

Step 5:

JwtService.extractUsername(token)

Username extracted.

Step 6:

CustomUserDetailsService.loadUserByUsername()

User loaded from database.

Step 7:

JwtService.isTokenValid()

Token validated.

Step 8:

UsernamePasswordAuthenticationToken created.

This object represents authenticated user.

Step 9:

SecurityContextHolder.getContext().setAuthentication()

This is MOST IMPORTANT LINE.

This marks user as authenticated.

Step 10:

Request continues to controller.

Step 11:

Controller executes successfully.

---

# SECTION 6: SecurityContext Explained

SecurityContext stores authentication.

Think of it as:

"Current logged in user"

Stored in:

SecurityContextHolder

You can access anywhere using:

Authentication authentication

---

# SECTION 7: Why Filter is Required

Spring Security does NOT know JWT automatically.

Filter tells Spring Security:

"This user is authenticated"

Without filter:

All protected requests fail.

---

# SECTION 8: AuthenticationManager Internal Flow

AuthenticationManager

→ DaoAuthenticationProvider

→ UserDetailsService

→ PasswordEncoder

→ Authentication success

---

# SECTION 9: JWT Creation Flow

JwtService.generateToken()

Steps:

Create header

Create payload

Add username

Add expiration

Sign using secret key

Return token

---

# SECTION 10: JWT Validation Flow

Extract username

Check expiration

Check signature

If valid → authenticated

---

# SECTION 11: Complete Execution Order Summary

Application starts

SecurityConfig initialized

Filters registered

Client sends request

Filter executes

Authentication happens

SecurityContext set

Controller executes

Response returned

---

# FINAL SUMMARY

Spring Security does NOT authenticate automatically.

Your JWT filter performs authentication.

SecurityContext stores authentication.

Controller executes only after authentication.

---

END OF INTERNAL FLOW GUIDE
