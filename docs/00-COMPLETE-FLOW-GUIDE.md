# COMPLETE SPRING SECURITY + JWT FLOW GUIDE (VERY EASY EXPLANATION)

This guide explains FULL FLOW from:

Client → Filter → Security → Service → Database → Response

This is EXACT internal working of Spring Security + JWT.

---

# BIG PICTURE OVERVIEW

Full flow:

Client Request  
→ Security Filter Chain  
→ JwtAuthenticationFilter  
→ AuthenticationManager  
→ UserDetailsService  
→ Database  
→ SecurityContext  
→ Controller  
→ Response  

---

# PART 1: APPLICATION STARTUP FLOW

When Spring Boot starts:

Spring creates these objects:

• SecurityConfig  
• JwtAuthenticationFilter  
• JwtService  
• CustomUserDetailsService  
• AuthService  
• AuthController  

Spring also builds:

Security Filter Chain

This chain contains filters.

Important filter:

JwtAuthenticationFilter (YOUR filter)

This filter runs for every request.

---

# PART 2: SECURITY FILTER CHAIN EXPLAINED

Security Filter Chain is like security gate.

Every request must pass through this chain.

Example:

Client sends request:

```
GET /api/test
```

Request goes through:

Security Filter Chain  
→ JwtAuthenticationFilter  
→ Authentication  
→ Controller  

---

# PART 3: REGISTER FLOW (STEP BY STEP)

Client sends request:

```
POST /auth/register
{
  "username": "ishant",
  "password": "1234",
  "role": "USER"
}
```

Step 1:
Request enters Security Filter Chain

JwtAuthenticationFilter runs

But /auth/register is public

So filter skips authentication

---

Step 2:
Request goes to AuthController

AuthController.register() called

---

Step 3:
AuthController calls:

```
AuthService.register()
```

---

Step 4:
AuthService creates User object

Password is encrypted using PasswordEncoder

Example:

```
1234 → encrypted password
```

---

Step 5:
User saved in database

Database:

```
id | username | password | role
1  | ishant   | encrypted| USER
```

---

Step 6:
JwtService generates token

Example:

```
eyJhbGc...
```

---

Step 7:
Token returned to client

Client stores token

---

REGISTER FLOW COMPLETE

---

# PART 4: LOGIN FLOW (STEP BY STEP)

Client sends request:

```
POST /auth/login
{
  "username": "ishant",
  "password": "1234"
}
```

---

Step 1:
Request enters Security Filter Chain

JwtAuthenticationFilter runs

Login endpoint is public

So authentication skipped

---

Step 2:
Request goes to AuthController

AuthController.login() called

---

Step 3:
AuthService.login() called

---

Step 4:
AuthenticationManager.authenticate() called

AuthenticationManager is main authentication engine

---

Step 5:
AuthenticationManager calls:

DaoAuthenticationProvider

---

Step 6:
DaoAuthenticationProvider calls:

CustomUserDetailsService.loadUserByUsername()

---

Step 7:
CustomUserDetailsService loads user from database

---

Step 8:
PasswordEncoder compares password

Input password:
```
1234
```

Database password:
```
encrypted password
```

If match → success

---

Step 9:
JwtService generates token

---

Step 10:
Token returned to client

---

LOGIN FLOW COMPLETE

---

# PART 5: PROTECTED ENDPOINT FLOW (MOST IMPORTANT)

Client sends request:

```
GET /api/test

Authorization: Bearer eyJhbGc...
```

This is protected endpoint.

---

Step 1:
Request enters Security Filter Chain

---

Step 2:
JwtAuthenticationFilter runs

This is YOUR custom filter

---

Step 3:
Filter reads Authorization header

```
Bearer eyJhbGc...
```

---

Step 4:
Filter extracts token

```
eyJhbGc...
```

---

Step 5:
JwtService extracts username

Example:

```
username = ishant
```

---

Step 6:
Filter calls:

CustomUserDetailsService.loadUserByUsername()

User loaded from database

---

Step 7:
JwtService validates token

Checks:

• token not expired  
• username matches  
• token valid  

---

Step 8:
Filter creates Authentication object

```
UsernamePasswordAuthenticationToken
```

This object represents logged-in user

---

Step 9:
Filter sets authentication in SecurityContext

IMPORTANT LINE:

```
SecurityContextHolder.getContext().setAuthentication(authToken)
```

This tells Spring:

USER IS LOGGED IN

---

Step 10:
Request continues

Controller executes

---

Step 11:
Controller returns response

---

PROTECTED FLOW COMPLETE

---

# PART 6: SECURITYCONTEXT EXPLAINED

SecurityContext stores logged-in user.

Think like:

"Current logged-in user memory"

Stored in:

```
SecurityContextHolder
```

Example:

```
username: ishant
role: USER
```

Spring uses this to allow access.

---

# PART 7: AUTHENTICATIONMANAGER FLOW

AuthenticationManager is main authentication engine.

Flow:

AuthenticationManager  
→ DaoAuthenticationProvider  
→ CustomUserDetailsService  
→ UserRepository  
→ Database  

Password verified.

Authentication successful.

---

# PART 8: DAOAUTHENTICATIONPROVIDER FLOW

DaoAuthenticationProvider does:

Load user from database  
Compare password  
Return authenticated user  

Uses:

UserDetailsService  
PasswordEncoder  

---

# PART 9: JWT TOKEN FLOW

JWT token contains:

Header  
Payload  
Signature  

Example payload:

```
username: ishant
expiration: tomorrow
```

JwtService creates token.

JwtAuthenticationFilter validates token.

---

# PART 10: COMPLETE REQUEST FLOW SUMMARY

Client sends request

↓

Security Filter Chain runs

↓

JwtAuthenticationFilter executes

↓

Extract token

↓

Validate token

↓

Load user from database

↓

Set authentication in SecurityContext

↓

Controller executes

↓

Response returned

---

# PART 11: VISUAL FLOW SIMPLIFIED

Client  
↓  
Filter Chain  
↓  
JwtAuthenticationFilter  
↓  
JwtService  
↓  
UserDetailsService  
↓  
Database  
↓  
SecurityContext  
↓  
Controller  
↓  
Response  

---

# PART 12: WHY FILTER IS REQUIRED

Spring Security does NOT understand JWT automatically.

Filter tells Spring:

"This user is authenticated"

Without filter → authentication fails

---

# PART 13: WHY SECURITYCONTEXT IS REQUIRED

SecurityContext stores logged-in user.

Controller can access current user.

---

# FINAL SIMPLE SUMMARY

Spring Security flow:

Request comes

↓

JwtAuthenticationFilter checks token

↓

User loaded from database

↓

User stored in SecurityContext

↓

Controller executes

↓

Response returned

---

# FINAL ONE-LINE SUMMARY

JwtAuthenticationFilter authenticates user and SecurityContext stores authenticated user.
