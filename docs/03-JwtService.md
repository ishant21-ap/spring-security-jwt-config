# JwtService.java — Complete Explanation in Very Easy Language

This class is responsible for handling JWT tokens.

It does 3 main things:

• Generate JWT token  
• Extract username from JWT token  
• Validate JWT token  

Think of this class as:

"JWT Manager"

It creates tokens and checks tokens.

---

# Real Life Example

Think JWT like an ID card.

When user logs in → system gives ID card (JWT)

Later when user comes again → system checks ID card

If valid → allow entry  
If invalid → reject  

JwtService creates and checks this ID card.

---

# Full Code

```java
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    private Key signingKey;

    @PostConstruct
    public void init() {

        signingKey =
                Keys.hmacShaKeyFor(secret.getBytes());

    }

    public String generateToken(String username) {

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + jwtExpiration
                        )
                )
                .signWith(signingKey)
                .compact();

    }

    public String extractUsername(String token) {

        return extractClaim(
                token,
                Claims::getSubject
        );

    }

    public boolean isTokenValid(
            String token,
            String username
    ) {

        final String extractedUsername =
                extractUsername(token);

        return extractedUsername.equals(username)
                && !isTokenExpired(token);

    }

    private boolean isTokenExpired(String token) {

        return extractClaim(
                token,
                Claims::getExpiration
        ).before(new Date());

    }

    private <T> T extractClaim(
            String token,
            Function<Claims, T> resolver
    ) {

        final Claims claims =
                extractAllClaims(token);

        return resolver.apply(claims);

    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

    }

}
```

---

# PART 1: @Service

```java
@Service
```

Meaning:

This tells Spring:

"Create object of this class"

Spring automatically creates JwtService.

Other classes can use it.

---

# PART 2: Secret Key

```java
@Value("${jwt.secret}")
private String secret;
```

This reads secret key from application.yml

Example:

```yaml
jwt:
  secret: mysecretkeymysecretkeymysecretkey12
```

Secret key is used to sign token.

This makes token secure.

Without secret key → anyone can create fake token.

---

# PART 3: Expiration Time

```java
@Value("${jwt.expiration}")
private long jwtExpiration;
```

This reads expiration time.

Example:

```yaml
jwt:
  expiration: 86400000
```

86400000 ms = 24 hours

After 24 hours → token expires.

---

# PART 4: Signing Key

```java
private Key signingKey;
```

This stores secret key in secure format.

Used to sign and verify tokens.

---

# PART 5: @PostConstruct

```java
@PostConstruct
public void init()
```

@PostConstruct runs after Spring creates object.

It runs only once.

---

```java
signingKey =
        Keys.hmacShaKeyFor(secret.getBytes());
```

This converts secret string into secure key.

Example:

Secret:
```
mysecretkey
```

Converted into secure key.

This key signs JWT.

---

# PART 6: generateToken method

```java
public String generateToken(String username)
```

This creates JWT token.

Example input:

```
username = ishant
```

Output:

```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

---

```java
return Jwts.builder()
```

Creates JWT builder.

Builder creates token.

---

```java
.subject(username)
```

Stores username inside token.

Example:

```
subject = ishant
```

---

```java
.issuedAt(new Date())
```

Stores current time.

Example:

```
issuedAt = now
```

---

```java
.expiration(
    new Date(
        System.currentTimeMillis()
        + jwtExpiration
    )
)
```

Sets expiration time.

Example:

Now = 10:00 AM  
Expiration = 10:00 AM + 24 hours  

Token expires tomorrow.

---

```java
.signWith(signingKey)
```

Signs token using secret key.

This prevents hacking.

---

```java
.compact();
```

Converts token into string.

Final token:

```
eyJhbGc...
```

---

# PART 7: extractUsername method

```java
public String extractUsername(String token)
```

Extracts username from token.

Example:

Token contains:

```
username = ishant
```

This method returns:

```
ishant
```

---

```java
return extractClaim(
        token,
        Claims::getSubject
);
```

Reads subject field from token.

Subject = username.

---

# PART 8: isTokenValid method

```java
public boolean isTokenValid(
    String token,
    String username
)
```

Checks if token is valid.

---

```java
final String extractedUsername =
        extractUsername(token);
```

Extract username from token.

---

```java
return extractedUsername.equals(username)
    && !isTokenExpired(token);
```

Checks:

• username matches  
• token not expired  

If both true → token valid.

---

# PART 9: isTokenExpired method

```java
private boolean isTokenExpired(String token)
```

Checks if token expired.

---

```java
extractClaim(
    token,
    Claims::getExpiration
)
```

Gets expiration date.

---

```java
.before(new Date())
```

Checks if expiration date is before current time.

If yes → expired.

---

# PART 10: extractClaim method

```java
private <T> T extractClaim(
    String token,
    Function<Claims, T> resolver
)
```

This is generic method.

It extracts any field from token.

Example:

Username  
Expiration  

---

```java
final Claims claims =
        extractAllClaims(token);
```

Gets all token data.

---

```java
return resolver.apply(claims);
```

Returns requested field.

---

# PART 11: extractAllClaims method

```java
private Claims extractAllClaims(String token)
```

Reads entire token.

---

```java
return Jwts.parser()
```

Creates parser.

---

```java
.verifyWith(signingKey)
```

Verifies token using secret key.

If secret key wrong → token invalid.

---

```java
.parseSignedClaims(token)
```

Parses token.

---

```java
.getPayload();
```

Gets token data.

Example payload:

```
username: ishant
expiration: tomorrow
```

---

# COMPLETE FLOW EXAMPLE

User logs in:

JwtService.generateToken("ishant")

Token created.

User sends request:

JwtService.extractUsername(token)

Returns ishant.

JwtService.isTokenValid(token, "ishant")

Returns true.

---

# Token Structure

JWT token looks like this:

```
header.payload.signature
```

Example:

```
eyJhbGc.header.payload.signature
```

Payload contains:

```
username
expiration
```

---

# Simple Summary

JwtService creates and validates tokens.

It is responsible for:

• generating token  
• extracting username  
• validating token  

---

# Interview Questions

Q: Why JwtService needed?

Answer:

To generate and validate JWT tokens.

---

Q: What is secret key?

Answer:

Used to sign and verify token.

---

Q: What is expiration?

Answer:

Token validity time.

---

# Final Summary

JwtService is the class that manages JWT tokens.

It creates secure tokens and validates them.
