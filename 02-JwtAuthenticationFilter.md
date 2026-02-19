# JwtAuthenticationFilter.java — Complete Explanation in Very Easy Language

This is the MOST IMPORTANT class in JWT authentication.

This class is responsible for:

• Reading JWT token from request  
• Validating JWT token  
• Logging in the user automatically  
• Telling Spring Security that user is authenticated  

Without this class, JWT authentication will NOT work.

---

# Real Life Example

Think like this:

You go to airport.

Security guard checks your ticket.

If ticket is valid → you can enter.

If ticket is invalid → you cannot enter.

Here:

Security guard = JwtAuthenticationFilter  
Ticket = JWT token  
Airport = Your API  

---

# Full Code

```java
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;

        }

        final String token = authHeader.substring(7);

        final String username = jwtService.extractUsername(token);

        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(username);

            if (jwtService.isTokenValid(token, userDetails.getUsername())) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);

            }

        }

        filterChain.doFilter(request, response);

    }

}
```

---

# PART 1: @Component

```java
@Component
```

Meaning:

This tells Spring:

"Create object of this class automatically"

Spring creates this filter during startup.

Without this, filter will not work.

---

# PART 2: @RequiredArgsConstructor

```java
@RequiredArgsConstructor
```

This creates constructor automatically.

Spring uses constructor to inject:

• JwtService  
• UserDetailsService  

---

# PART 3: extends OncePerRequestFilter

```java
extends OncePerRequestFilter
```

Meaning:

This filter runs ONCE for EVERY request.

Example:

User sends request:

```
GET /api/test
```

This filter runs.

User sends another request:

```
GET /api/products
```

This filter runs again.

It runs for EVERY request.

---

# PART 4: JwtService field

```java
private final JwtService jwtService;
```

This service is used to:

• extract username from token  
• validate token  

Example:

Token:
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
```

JwtService extracts username.

---

# PART 5: UserDetailsService field

```java
private final UserDetailsService userDetailsService;
```

This service loads user from database.

Example:

Username = ishant

This service loads user from database.

---

# PART 6: doFilterInternal method

```java
protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain
)
```

This method runs for EVERY request.

This is the main logic.

---

# PART 7: Read Authorization Header

```java
final String authHeader =
        request.getHeader("Authorization");
```

This reads Authorization header.

Example request:

```
GET /api/test

Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
```

authHeader value becomes:

```
Bearer eyJhbGc...
```

---

# PART 8: Check if token exists

```java
if (authHeader == null ||
    !authHeader.startsWith("Bearer ")) {

    filterChain.doFilter(request, response);
    return;

}
```

Meaning:

If Authorization header does not exist

OR

Does not start with "Bearer "

Then skip authentication.

Continue request.

Example:

Public endpoint:

```
/auth/login
```

No JWT needed.

So filter skips.

---

# PART 9: Extract token

```java
final String token = authHeader.substring(7);
```

This removes "Bearer "

Example:

Before:
```
Bearer eyJhbGc...
```

After:
```
eyJhbGc...
```

Now we have actual JWT token.

---

# PART 10: Extract username from token

```java
final String username =
        jwtService.extractUsername(token);
```

This reads username from token.

Example:

Token contains:
```
username = ishant
```

username variable becomes:
```
ishant
```

---

# PART 11: Check if user not already authenticated

```java
if (username != null &&
    SecurityContextHolder.getContext()
        .getAuthentication() == null)
```

Meaning:

If username exists

AND

User not already logged in

Then authenticate user.

SecurityContextHolder stores logged-in user.

---

# PART 12: Load user from database

```java
UserDetails userDetails =
        userDetailsService.loadUserByUsername(username);
```

Loads user from database.

Example:

Username = ishant

Database:

```
username: ishant
password: encrypted
role: USER
```

---

# PART 13: Validate token

```java
if (jwtService.isTokenValid(
        token,
        userDetails.getUsername()
))
```

Checks:

• token not expired  
• username matches  
• token valid  

If valid → authenticate user

---

# PART 14: Create Authentication Object

```java
UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
```

This creates authentication object.

This object tells Spring:

"This user is logged in"

---

# PART 15: Set request details

```java
authToken.setDetails(
    new WebAuthenticationDetailsSource()
        .buildDetails(request)
);
```

Adds request details.

Not critical, but useful.

---

# PART 16: Set user as authenticated

```java
SecurityContextHolder
    .getContext()
    .setAuthentication(authToken);
```

THIS IS THE MOST IMPORTANT LINE.

This tells Spring:

"User is authenticated"

Now user can access protected endpoints.

Without this line → user is not authenticated.

---

# PART 17: Continue request

```java
filterChain.doFilter(request, response);
```

Continue request to next filter.

Then controller executes.

---

# COMPLETE FLOW EXAMPLE

User sends request:

```
GET /api/test

Authorization: Bearer eyJhbGc...
```

Flow:

Filter runs

Extract token

Extract username

Load user from database

Validate token

Set authentication

Controller executes

Response returned

---

# What happens WITHOUT this filter

User sends request:

```
GET /api/test
```

Spring rejects request.

Because user not authenticated.

---

# What happens WITH this filter

User sends request with token.

Filter authenticates user.

Spring allows request.

---

# Simple Summary

This filter is security guard.

It checks JWT token.

If token valid → user logged in.

If token invalid → user not allowed.

---

# Interview Questions

Q: Why JwtAuthenticationFilter needed?

Answer:

To authenticate user using JWT.

---

Q: Why extends OncePerRequestFilter?

Answer:

To run filter once per request.

---

Q: What is SecurityContextHolder?

Answer:

Stores logged-in user.

---

# Final Summary

JwtAuthenticationFilter is the core of JWT authentication.

It reads token, validates token, and logs in user automatically.
