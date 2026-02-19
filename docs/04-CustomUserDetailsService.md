# CustomUserDetailsService.java — Complete Explanation in Very Easy Language

This class is responsible for loading user from database.

Spring Security uses this class during login and JWT authentication.

Simple meaning:

This class tells Spring Security:

"How to find user in database"

Without this class, Spring Security cannot find users.

---

# Real Life Example

Think like this:

User tries to login.

Spring asks:

"Find this user in database"

CustomUserDetailsService answers:

"Here is the user"

---

# Full Code

```java
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(
            String username
    ) throws UsernameNotFoundException {

        return userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"
                        )
                );

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

Spring automatically creates this class.

Other classes can use it.

Example:

SecurityConfig uses it.

JwtAuthenticationFilter uses it.

---

# PART 2: @RequiredArgsConstructor

```java
@RequiredArgsConstructor
```

This automatically creates constructor.

Spring injects UserRepository into this class.

Example constructor created automatically:

```java
public CustomUserDetailsService(
    UserRepository userRepository
) {
    this.userRepository = userRepository;
}
```

---

# PART 3: implements UserDetailsService

```java
implements UserDetailsService
```

This is VERY IMPORTANT.

UserDetailsService is interface provided by Spring Security.

Spring Security calls this interface when authentication happens.

Spring Security does this internally:

```java
userDetailsService.loadUserByUsername(username)
```

This method must be implemented.

---

# PART 4: UserRepository field

```java
private final UserRepository userRepository;
```

This is used to access database.

This repository finds user using username.

Example database table:

```
id | username | password | role
1  | ishant   | encrypted| USER
```

UserRepository fetches user.

---

# PART 5: loadUserByUsername method

```java
@Override
public UserDetails loadUserByUsername(
        String username
)
```

This is MOST IMPORTANT method.

Spring Security calls this method during:

• Login  
• JWT authentication  

Example:

User tries login:

```
username: ishant
password: 1234
```

Spring calls:

```java
loadUserByUsername("ishant")
```

---

# PART 6: Find user from database

```java
return userRepository
        .findByUsername(username)
```

This searches database.

Example:

Database contains:

```
username: ishant
```

UserRepository returns User object.

---

# PART 7: orElseThrow

```java
.orElseThrow(() ->
        new UsernameNotFoundException(
                "User not found"
        )
);
```

If user does NOT exist in database,

Throw exception.

Example:

User tries login:

```
username: abc
```

Database does NOT contain abc.

Exception thrown:

```
User not found
```

Spring rejects login.

---

# PART 8: Return UserDetails

UserRepository returns User object.

Our User entity implements UserDetails.

Example:

```java
public class User implements UserDetails
```

So User object itself is UserDetails.

Spring Security accepts it.

---

# COMPLETE LOGIN FLOW USING THIS CLASS

User sends login request:

```
POST /auth/login
{
    "username": "ishant",
    "password": "1234"
}
```

Flow:

AuthController receives request

AuthService calls AuthenticationManager

AuthenticationManager calls AuthenticationProvider

AuthenticationProvider calls:

```java
loadUserByUsername("ishant")
```

CustomUserDetailsService finds user

Returns user

Spring verifies password

Login successful

---

# COMPLETE JWT FLOW USING THIS CLASS

User sends request:

```
GET /api/test
Authorization: Bearer eyJhbGc...
```

Flow:

JwtAuthenticationFilter extracts username

Calls:

```java
loadUserByUsername("ishant")
```

User loaded from database

Token validated

User authenticated

---

# Why this class is needed

Spring Security does NOT know your database.

This class tells Spring Security:

"How to find user in database"

Without this class → login fails.

---

# What happens WITHOUT this class

Spring cannot find user.

Login will fail.

Error example:

```
No UserDetailsService found
```

---

# Simple Summary

This class loads user from database.

Spring Security uses it to authenticate users.

---

# Interview Questions

Q: Why CustomUserDetailsService needed?

Answer:

Spring Security uses it to load user from database.

---

Q: What method must be implemented?

Answer:

loadUserByUsername()

---

Q: What happens if user not found?

Answer:

UsernameNotFoundException thrown.

---

# Real World Example

Database:

```
username: ishant
password: encrypted
```

Spring calls:

```
loadUserByUsername("ishant")
```

This class returns user.

Spring verifies password.

User logged in.

---

# Final Summary

CustomUserDetailsService connects Spring Security with database.

It loads user from database during authentication.
