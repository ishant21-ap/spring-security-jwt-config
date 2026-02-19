# DTO Classes — Complete Explanation in Very Easy Language

DTO means:

Data Transfer Object

Simple meaning:

DTO is used to send and receive data between client and server.

DTO protects your internal database structure.

Client NEVER directly uses User entity.

Client only uses DTO.

---

# Why DTO needed

Example:

User entity has:

id  
username  
password  
role  

But client should NOT see password.

DTO allows controlling what client sees.

---

# DTO Classes in this project

We have 3 DTO classes:

1. RegisterRequest
2. AuthRequest
3. AuthResponse

---

# 1. RegisterRequest.java

Full Code:

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    private String username;

    private String password;

    private Role role;

}
```

---

# Purpose

This DTO receives data from client during registration.

Example client request:

```
POST /auth/register

{
    "username": "ishant",
    "password": "1234",
    "role": "USER"
}
```

Spring converts this JSON into RegisterRequest object.

---

# Line-by-line explanation

```java
@Getter
@Setter
```

Creates getters and setters automatically.

Example:

getUsername()

setUsername()

---

```java
@NoArgsConstructor
@AllArgsConstructor
```

Creates constructors automatically.

---

```java
@Builder
```

Allows object creation using builder.

Example:

```java
RegisterRequest request =
    RegisterRequest.builder()
        .username("ishant")
        .password("1234")
        .role(USER)
        .build();
```

---

```java
private String username;
```

Stores username from client.

Example:

```
ishant
```

---

```java
private String password;
```

Stores password.

Example:

```
1234
```

---

```java
private Role role;
```

Stores role.

Example:

```
USER
```

---

# 2. AuthRequest.java

Full Code:

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequest {

    private String username;

    private String password;

}
```

---

# Purpose

This DTO receives login data.

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

# Fields explanation

```java
private String username;
```

Stores username.

---

```java
private String password;
```

Stores password.

---

# 3. AuthResponse.java

Full Code:

```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;

}
```

---

# Purpose

This DTO sends JWT token to client.

Example response:

```
{
    "token": "eyJhbGc..."
}
```

Client stores this token.

Client uses token for future requests.

---

# Flow using DTO

Register Flow:

Client sends RegisterRequest

Controller receives RegisterRequest

Service creates user

Service returns AuthResponse

Client receives token

---

Login Flow:

Client sends AuthRequest

Controller receives AuthRequest

Service verifies user

Service returns AuthResponse

Client receives token

---

# Why not use User entity directly

Bad practice:

```java
public User register(User user)
```

This exposes internal database structure.

Good practice:

```java
public AuthResponse register(RegisterRequest request)
```

This protects internal structure.

---

# Real Life Example

Think like this:

Bank account = User entity

ATM machine = DTO

ATM only shows limited information.

DTO hides internal data.

---

# Simple Summary

DTO is used to send and receive data safely.

DTO protects internal database structure.

DTO is used between client and server.

---

# Interview Questions

Q: What is DTO?

Answer:

DTO is used to transfer data between client and server.

---

Q: Why use DTO instead of entity?

Answer:

To protect internal database structure.

---

# Final Summary

DTO controls what data client sends and receives.

It makes application secure and clean.
