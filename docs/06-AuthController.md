# AuthController.java — Complete Explanation in Very Easy Language

This class is responsible for handling HTTP requests.

Simple meaning:

This class receives requests from client (Postman, frontend, browser)

Then it calls AuthService.

Then it returns response.

Controller is entry point of application.

Client → Controller → Service → Database

---

# Real Life Example

Think like this:

You go to bank counter.

You talk to bank employee.

Employee talks to backend system.

Employee gives you response.

Here:

Client = You  
Controller = Bank employee  
Service = Backend system  

---

# Full Code

```java
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) {

        return ResponseEntity.ok(
                authService.register(request)
        );

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthRequest request
    ) {

        return ResponseEntity.ok(
                authService.login(request)
        );

    }

}
```

---

# PART 1: @RestController

```java
@RestController
```

This tells Spring:

"This class handles HTTP requests"

Spring allows this class to receive requests.

Without this annotation, controller will not work.

---

# PART 2: @RequestMapping("/auth")

```java
@RequestMapping("/auth")
```

This sets base URL.

All endpoints will start with:

```
/auth
```

Example:

```
/auth/register
/auth/login
```

---

# PART 3: @RequiredArgsConstructor

```java
@RequiredArgsConstructor
```

Spring automatically injects AuthService.

This creates constructor automatically:

```java
public AuthController(AuthService authService)
```

Spring injects AuthService.

---

# PART 4: AuthService field

```java
private final AuthService authService;
```

Controller calls this service.

Service handles logic.

Controller does not handle logic.

Controller only receives and sends data.

---

# PART 5: register endpoint

```java
@PostMapping("/register")
```

This creates endpoint:

```
POST /auth/register
```

Client calls this endpoint to register.

---

# PART 6: @RequestBody

```java
@RequestBody RegisterRequest request
```

This receives JSON data from client.

Example client request:

```
POST /auth/register

{
    "username": "ishant",
    "password": "1234",
    "role": "USER"
}
```

Spring converts JSON into RegisterRequest object.

---

# PART 7: Call AuthService

```java
authService.register(request)
```

Controller calls service.

Service handles registration.

Service:

• encrypts password  
• saves user  
• generates JWT  

---

# PART 8: Return response

```java
return ResponseEntity.ok(
        authService.register(request)
);
```

Returns response to client.

Example response:

```
{
    "token": "eyJhbGc..."
}
```

Client receives token.

---

# COMPLETE REGISTER FLOW

Client sends:

```
POST /auth/register
{
    "username": "ishant",
    "password": "1234",
    "role": "USER"
}
```

Flow:

AuthController receives request

AuthService.register called

User saved in database

JWT token generated

Controller returns token

Client receives token

---

# PART 9: login endpoint

```java
@PostMapping("/login")
```

Creates endpoint:

```
POST /auth/login
```

Client calls this endpoint to login.

---

# PART 10: Receive request body

```java
@RequestBody AuthRequest request
```

Example client request:

```
POST /auth/login

{
    "username": "ishant",
    "password": "1234"
}
```

Spring converts JSON into AuthRequest object.

---

# PART 11: Call AuthService login

```java
authService.login(request)
```

Service:

• verifies username  
• verifies password  
• generates JWT  

---

# PART 12: Return response

```java
return ResponseEntity.ok(
        authService.login(request)
);
```

Returns JWT token.

Example response:

```
{
    "token": "eyJhbGc..."
}
```

---

# COMPLETE LOGIN FLOW

Client sends:

```
POST /auth/login
{
    "username": "ishant",
    "password": "1234"
}
```

Flow:

Controller receives request

AuthService.login called

AuthenticationManager verifies user

JWT generated

Controller returns token

Client receives token

---

# What is ResponseEntity

```java
ResponseEntity.ok(...)
```

This sends HTTP response.

Status code:

```
200 OK
```

Response body:

```
token
```

---

# Why controller needed

Controller connects client and service.

Without controller, client cannot access application.

---

# Real Example

Client → POST /auth/login

Controller receives request

Service verifies user

Controller returns token

---

# Simple Summary

Controller receives request.

Controller calls service.

Controller returns response.

---

# Interview Questions

Q: What is Controller?

Answer:

Controller handles HTTP requests.

---

Q: Why use @RestController?

Answer:

To receive HTTP requests.

---

Q: Why use @RequestBody?

Answer:

To receive JSON data.

---

# Final Summary

AuthController is entry point of application.

It receives login and register requests and returns JWT token.
