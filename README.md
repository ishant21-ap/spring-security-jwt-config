# Spring Security + JWT Template (Spring Boot 3 + PostgreSQL)

This project is a **production‑ready reusable template** for implementing **Spring Security with JWT authentication** using **Spring Boot 3+, Spring Security 6+, and PostgreSQL**.

This repository is designed to be reused in any future project where authentication and authorization are required.

---

# Table of Contents

1. Project Goal
2. Architecture Overview
3. Authentication Flow (Step‑by‑Step)
4. Project Structure
5. Step‑by‑Step File Creation Order
6. Detailed Code Explanation (File by File)
7. Request Flow Internally
8. Security Flow Internally
9. JWT Flow Internally
10. Database Structure
11. How to Run Project
12. How to Test Using Postman
13. How to Reuse This Template
14. Interview Questions and Answers

---

# 1. Project Goal

The goal of this project is to provide:

• Stateless authentication using JWT
• Secure password storage using BCrypt
• Role‑based authentication support
• Clean and reusable architecture
• Production‑ready Spring Security configuration

---

# 2. Architecture Overview

Client → Controller → Service → Repository → Database

Security Flow:

Client → JWT Filter → SecurityContext → Controller

---

# 3. Authentication Flow (Step‑by‑Step)

## Register Flow

Step 1: Client sends register request

Step 2: Controller receives request

Step 3: Service encodes password

Step 4: Service saves user in database

Step 5: JWT token generated

Step 6: Token returned to client

---

## Login Flow

Step 1: Client sends username and password

Step 2: AuthenticationManager authenticates user

Step 3: CustomUserDetailsService loads user from DB

Step 4: PasswordEncoder verifies password

Step 5: JWT token generated

Step 6: Token returned

---

## Protected Request Flow

Step 1: Client sends request with JWT token

Step 2: JwtAuthenticationFilter intercepts request

Step 3: Token validated

Step 4: Authentication stored in SecurityContext

Step 5: Controller executes

---

# 4. Project Structure

```
com.example.securityjwt
│
├── config
│     SecurityConfig.java
│
├── controller
│     AuthController.java
│     TestController.java
│
├── dto
│     AuthRequest.java
│     AuthResponse.java
│     RegisterRequest.java
│
├── entity
│     User.java
│     Role.java
│
├── repository
│     UserRepository.java
│
├── security
│     JwtService.java
│     JwtAuthenticationFilter.java
│     CustomUserDetailsService.java
│
├── service
│     AuthService.java
│
└── application.yml
```

---

# 5. File Creation Order and Why

Order is important due to dependencies.

Step 1: Role.java
Defines roles (USER, ADMIN)

Step 2: User.java
Defines database entity and authentication user

Step 3: UserRepository.java
Provides database access

Step 4: DTO classes
Provides request and response objects

Step 5: JwtService.java
Handles token generation and validation

Step 6: CustomUserDetailsService.java
Loads user from database

Step 7: JwtAuthenticationFilter.java
Intercepts every request

Step 8: SecurityConfig.java
Configures Spring Security

Step 9: AuthService.java
Handles business logic

Step 10: AuthController.java
Handles HTTP requests

---

# 6. Detailed Code Explanation

---

# Role.java

```
public enum Role {
    USER,
    ADMIN
}
```

Explanation:

Enum defines fixed roles.

Spring Security uses roles for authorization.

---

# User.java

```
@Entity
```

Marks class as database entity.

```
@Table(name = "users")
```

Maps entity to users table.

```
@Id
@GeneratedValue
```

Defines primary key.

```
private String username;
```

Stores login username.

```
private String password;
```

Stores encrypted password.

```
implements UserDetails
```

Allows Spring Security to use this entity for authentication.

Spring Security internally works only with UserDetails.

---

# UserRepository.java

```
extends JpaRepository<User, Long>
```

Provides CRUD operations.

```
Optional<User> findByUsername(String username);
```

Spring Security uses this method to find user.

---

# DTO Classes

AuthRequest

Used for login.

AuthResponse

Used to return JWT token.

RegisterRequest

Used for registration.

DTO prevents exposing entity directly.

---

# JwtService.java

Purpose:

Generate token
Validate token
Extract username

Important line:

```
.signWith(signingKey)
```

Signs token using secret key.

```
.extractUsername(token)
```

Reads username from token.

---

# CustomUserDetailsService.java

```
implements UserDetailsService
```

Spring Security calls this class.

```
loadUserByUsername
```

Loads user from database.

---

# JwtAuthenticationFilter.java

Most important class.

Runs for every request.

```
String authHeader = request.getHeader("Authorization");
```

Reads token from request.

```
jwtService.extractUsername(token);
```

Extract username.

```
SecurityContextHolder.getContext().setAuthentication(authToken);
```

Marks user as authenticated.

---

# SecurityConfig.java

Controls entire Spring Security.

```
.sessionCreationPolicy(STATELESS)
```

Disables session.

```
.addFilterBefore(jwtAuthenticationFilter)
```

Registers JWT filter.

```
.requestMatchers("/auth/**").permitAll()
```

Allows public access.

---

# AuthService.java

Handles business logic.

Register:

Encode password
Save user
Generate token

Login:

Authenticate user
Generate token

---

# AuthController.java

Handles HTTP requests.

Endpoints:

POST /auth/register
POST /auth/login

---

# 7. Security Flow Internally

```
Request
 ↓
JWT Filter
 ↓
Validate token
 ↓
Set SecurityContext
 ↓
Controller
```

---

# 8. JWT Token Structure

JWT contains:

Header.Payload.Signature

Payload contains:

Username
Expiration

---

# 9. Database Structure

Table: users

Columns:

id
username
password
role

---

# 10. application.yml

```
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/security_jwt_db

jwt:
  secret: mysecretkeymysecretkeymysecretkey12
  expiration: 86400000
```

---

# 11. How to Run

Step 1: Create database

Step 2: Run Spring Boot

Step 3: Test endpoints

---

# 12. Postman Testing

Register:

POST /auth/register

Login:

POST /auth/login

Protected:

GET /api/test

Add Header:

Authorization: Bearer TOKEN

---

# 13. How to Reuse

Copy these packages:

config
security
service
repository
entity

Update entity fields as needed.

---

# 14. Interview Questions

Q: Why JWT?

Stateless authentication.

Q: Why UserDetailsService?

Loads user from database.

Q: Why PasswordEncoder?

Encrypt password.

Q: Why filter?

Intercept requests.

---

# Conclusion

This template provides complete JWT authentication system.

Reusable in any Spring Boot project.

Secure, scalable, and production ready.

---

End of README
