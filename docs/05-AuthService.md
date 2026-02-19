# AuthService.java — Complete Explanation in Very Easy Language

This class is responsible for:

• Registering new user  
• Logging in user  
• Generating JWT token  

Simple meaning:

This class handles login and register logic.

Controller calls this class.

This class talks to database and JwtService.

---

# Real Life Example

Think like this:

User wants to create account → register()

User wants to login → login()

AuthService handles both.

---

# Full Code

```java
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(
            RegisterRequest request
    ) {

        User user =
                User.builder()
                        .username(request.getUsername())
                        .password(
                                passwordEncoder.encode(
                                        request.getPassword()
                                )
                        )
                        .role(request.getRole())
                        .build();

        userRepository.save(user);

        String token =
                jwtService.generateToken(
                        user.getUsername()
                );

        return AuthResponse.builder()
                .token(token)
                .build();

    }

    public AuthResponse login(
            AuthRequest request
    ) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        String token =
                jwtService.generateToken(
                        request.getUsername()
                );

        return AuthResponse.builder()
                .token(token)
                .build();

    }

}
```

---

# PART 1: @Service

```java
@Service
```

This tells Spring:

"Create object of this class"

Spring automatically creates AuthService.

Controller uses this service.

---

# PART 2: @RequiredArgsConstructor

```java
@RequiredArgsConstructor
```

Spring injects dependencies automatically.

Spring injects:

• UserRepository  
• PasswordEncoder  
• JwtService  
• AuthenticationManager  

---

# PART 3: UserRepository field

```java
private final UserRepository userRepository;
```

Used to save user in database.

Example:

```java
userRepository.save(user);
```

This inserts user into database.

---

# PART 4: PasswordEncoder field

```java
private final PasswordEncoder passwordEncoder;
```

Used to encrypt password.

Example:

Input password:
```
1234
```

Stored password:
```
$2a$10$encryptedpassword
```

Never store plain password.

---

# PART 5: JwtService field

```java
private final JwtService jwtService;
```

Used to generate JWT token.

Example:

```java
jwtService.generateToken(username)
```

Returns token.

---

# PART 6: AuthenticationManager field

```java
private final AuthenticationManager authenticationManager;
```

Used to authenticate user during login.

Spring Security verifies:

• username  
• password  

---

# PART 7: register method

```java
public AuthResponse register(
        RegisterRequest request
)
```

This method registers new user.

Example request:

```
username: ishant
password: 1234
role: USER
```

---

# PART 8: Create User object

```java
User user =
        User.builder()
```

Creates new User object.

---

```java
.username(request.getUsername())
```

Sets username.

Example:

```
ishant
```

---

```java
.password(
    passwordEncoder.encode(
        request.getPassword()
    )
)
```

Encrypts password.

Example:

Input:
```
1234
```

Stored:
```
$2a$10$encrypted
```

---

```java
.role(request.getRole())
```

Sets role.

Example:

```
USER
```

---

```java
.build();
```

Creates User object.

---

# PART 9: Save user in database

```java
userRepository.save(user);
```

Inserts user into database.

Database table example:

```
id | username | password | role
1  | ishant   | encrypted| USER
```

---

# PART 10: Generate token

```java
String token =
        jwtService.generateToken(
                user.getUsername()
        );
```

Creates JWT token.

Example:

```
eyJhbGc...
```

---

# PART 11: Return response

```java
return AuthResponse.builder()
        .token(token)
        .build();
```

Returns token to user.

User receives token.

---

# COMPLETE REGISTER FLOW

User sends request:

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

User object created

Password encrypted

User saved in database

JWT token generated

Token returned

---

# PART 12: login method

```java
public AuthResponse login(
        AuthRequest request
)
```

This method logs in user.

Example request:

```
username: ishant
password: 1234
```

---

# PART 13: Authenticate user

```java
authenticationManager.authenticate(
    new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
    )
);
```

This verifies:

• username exists  
• password correct  

Spring internally calls:

CustomUserDetailsService

PasswordEncoder

If correct → success

If wrong → error

---

# PART 14: Generate JWT token

```java
String token =
        jwtService.generateToken(
                request.getUsername()
        );
```

Creates JWT token.

---

# PART 15: Return response

```java
return AuthResponse.builder()
        .token(token)
        .build();
```

Returns token to user.

---

# COMPLETE LOGIN FLOW

User sends request:

```
POST /auth/login
{
    "username": "ishant",
    "password": "1234"
}
```

Flow:

AuthController receives request

AuthService.login called

AuthenticationManager verifies user

Password checked

JWT token generated

Token returned

---

# What happens if password wrong

AuthenticationManager throws exception.

Login fails.

---

# Why AuthenticationManager used

AuthenticationManager verifies user credentials securely.

---

# Simple Summary

AuthService handles:

• Register user  
• Login user  
• Generate JWT token  

---

# Interview Questions

Q: Why AuthService needed?

Answer:

To handle login and register logic.

---

Q: Why PasswordEncoder used?

Answer:

To encrypt password.

---

Q: Why AuthenticationManager used?

Answer:

To authenticate user.

---

# Real Example

Register:

User created

Password encrypted

Token generated

Login:

User verified

Token generated

---

# Final Summary

AuthService connects controller, database, and JwtService.

It handles login and registration securely.
