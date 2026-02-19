# Spring Security Filter Chain — Internal Execution (Very Easy Explanation)

This guide explains EXACT internal working of Spring Security filter chain.

This is the MOST IMPORTANT internal flow.

This explains what happens when request comes.

---

# BIG IDEA FIRST

Every request goes through:

Security Filter Chain

Filter chain contains many filters.

Example filters:

• JwtAuthenticationFilter (your filter)  
• UsernamePasswordAuthenticationFilter  
• AuthorizationFilter  
• ExceptionTranslationFilter  

Each filter has specific job.

---

# REAL LIFE EXAMPLE

Think like airport security:

Passenger enters airport

↓

ID check

↓

Ticket check

↓

Security check

↓

Allowed to enter

Filter chain works same way.

---

# STEP 1: CLIENT SENDS REQUEST

Example request:

```
GET /api/test

Authorization: Bearer eyJhbGc...
```

Request goes to Spring Boot.

Spring does NOT send request directly to controller.

First, it goes to Security Filter Chain.

---

# STEP 2: REQUEST ENTERS SECURITY FILTER CHAIN

Spring calls:

```
FilterChain.doFilter()
```

Now filters execute one by one.

Order is important.

Example order:

```
SecurityContextPersistenceFilter
JwtAuthenticationFilter
UsernamePasswordAuthenticationFilter
AuthorizationFilter
```

---

# STEP 3: JwtAuthenticationFilter EXECUTES

This is YOUR custom filter.

Spring calls:

```
JwtAuthenticationFilter.doFilterInternal()
```

This filter does:

1. Read Authorization header
2. Extract token
3. Validate token
4. Authenticate user

---

# STEP 4: FILTER EXTRACTS TOKEN

Reads header:

```
Authorization: Bearer eyJhbGc...
```

Extracts:

```
eyJhbGc...
```

---

# STEP 5: FILTER CALLS JwtService

JwtService extracts username:

```
ishant
```

Now filter knows username.

---

# STEP 6: FILTER CALLS UserDetailsService

Filter calls:

```
CustomUserDetailsService.loadUserByUsername("ishant")
```

This loads user from database.

Database:

```
username: ishant
password: encrypted
role: USER
```

User returned.

---

# STEP 7: FILTER VALIDATES TOKEN

JwtService checks:

• token not expired  
• username matches  
• token valid  

If valid → continue

If invalid → reject request

---

# STEP 8: FILTER CREATES AUTHENTICATION OBJECT

Filter creates:

```
UsernamePasswordAuthenticationToken
```

This object represents logged-in user.

Contains:

• username  
• authorities  
• authentication status  

---

# STEP 9: FILTER STORES USER IN SECURITYCONTEXT

MOST IMPORTANT STEP

Filter calls:

```
SecurityContextHolder.getContext().setAuthentication(authentication)
```

Now Spring knows:

USER IS LOGGED IN

SecurityContext stores user.

Think like:

"Current logged-in user memory"

---

# STEP 10: FILTER CALLS NEXT FILTER

Filter calls:

```
filterChain.doFilter(request, response)
```

Now next filter executes.

Example:

AuthorizationFilter

---

# STEP 11: AUTHORIZATION FILTER CHECKS ACCESS

Spring checks:

Is user authenticated?

YES → allow access

NO → reject access

---

# STEP 12: REQUEST REACHES DISPATCHERSERVLET

DispatcherServlet sends request to controller.

Example:

```
TestController.test()
```

Controller executes.

Response created.

---

# STEP 13: RESPONSE RETURNS TO CLIENT

Client receives response.

Example:

```
Hello User
```

---

# COMPLETE FLOW SUMMARY (VERY IMPORTANT)

Client sends request

↓

Security Filter Chain starts

↓

JwtAuthenticationFilter executes

↓

Token extracted

↓

Username extracted

↓

User loaded from database

↓

Token validated

↓

Authentication object created

↓

Stored in SecurityContext

↓

AuthorizationFilter checks access

↓

Controller executes

↓

Response returned

---

# INTERNAL AUTHENTICATIONMANAGER FLOW (LOGIN ONLY)

During login:

Spring calls:

```
AuthenticationManager.authenticate()
```

AuthenticationManager calls:

```
ProviderManager
```

ProviderManager calls:

```
DaoAuthenticationProvider
```

DaoAuthenticationProvider calls:

```
UserDetailsService
```

User loaded.

Password verified using PasswordEncoder.

Authentication successful.

---

# SECURITYCONTEXT EXPLAINED SIMPLY

SecurityContext stores logged-in user.

Stored in:

```
SecurityContextHolder
```

Example stored data:

```
username: ishant
role: USER
authenticated: true
```

Controller can access current user.

---

# WHY JwtAuthenticationFilter IS REQUIRED

Spring Security does NOT understand JWT automatically.

JwtAuthenticationFilter tells Spring:

"This user is authenticated"

Without this filter → JWT authentication fails

---

# FILTER CHAIN VISUAL SIMPLIFIED

Client Request

↓

Security Filter Chain

↓

JwtAuthenticationFilter

↓

SecurityContextHolder

↓

AuthorizationFilter

↓

Controller

↓

Response

---

# VERY IMPORTANT INTERNAL COMPONENTS SUMMARY

JwtAuthenticationFilter → reads token

JwtService → validates token

UserDetailsService → loads user

AuthenticationManager → authenticates user

SecurityContextHolder → stores authenticated user

FilterChain → controls execution order

---

# FINAL ONE-LINE SUMMARY

JwtAuthenticationFilter authenticates user and stores user in SecurityContext, allowing controller access.

---

# MOST IMPORTANT INTERVIEW QUESTION

Q: How Spring Security authenticates JWT?

Answer:

JwtAuthenticationFilter extracts token, validates token, loads user, and sets authentication in SecurityContextHolder.

---

# FINAL MASTER SUMMARY

Request → FilterChain → JwtAuthenticationFilter → JwtService → UserDetailsService → SecurityContextHolder → Controller → Response
