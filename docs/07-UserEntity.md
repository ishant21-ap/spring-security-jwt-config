# User.java (User Entity) — Complete Explanation in Very Easy Language

This class represents the User table in database.

Simple meaning:

This class stores user data.

Example user data:

• username  
• password  
• role  

This class connects Java code with database table.

This class is also used by Spring Security to authenticate users.

---

# Real Life Example

Think like this:

Database table = Excel sheet

User.java = structure of that Excel sheet

Each row = one user

Example:

| id | username | password | role |
|----|----------|----------|------|
| 1  | ishant   | encrypted| USER |

---

# Full Code

```java
@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                new SimpleGrantedAuthority(
                        role.name()
                )
        );

    }

    @Override
    public String getPassword() {

        return password;

    }

    @Override
    public String getUsername() {

        return username;

    }

    @Override
    public boolean isAccountNonExpired() {

        return true;

    }

    @Override
    public boolean isAccountNonLocked() {

        return true;

    }

    @Override
    public boolean isCredentialsNonExpired() {

        return true;

    }

    @Override
    public boolean isEnabled() {

        return true;

    }

}
```

---

# PART 1: @Entity

```java
@Entity
```

This tells Spring:

"This class is database entity"

Spring creates table using this class.

Table name will be:

```
users
```

---

# PART 2: @Table(name = "users")

```java
@Table(name = "users")
```

This tells Spring:

Table name is "users"

Database table:

```
users
```

---

# PART 3: Lombok annotations

```java
@Getter
@Setter
```

Automatically creates getters and setters.

Example:

```java
getUsername()
setUsername()
```

---

```java
@Builder
```

Allows object creation like this:

```java
User user =
    User.builder()
        .username("ishant")
        .password("1234")
        .role(USER)
        .build();
```

---

```java
@NoArgsConstructor
@AllArgsConstructor
```

Creates constructors automatically.

---

# PART 4: implements UserDetails

```java
public class User implements UserDetails
```

VERY IMPORTANT.

Spring Security requires UserDetails.

This allows Spring Security to use this class.

Spring Security works only with UserDetails.

Without this, authentication will not work.

---

# PART 5: id field

```java
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;
```

@Id means primary key.

@GeneratedValue means auto increment.

Example database:

```
id = 1
id = 2
id = 3
```

---

# PART 6: username field

```java
@Column(unique = true, nullable = false)
private String username;
```

Stores username.

Example:

```
ishant
```

unique = true → username must be unique

nullable = false → cannot be empty

---

# PART 7: password field

```java
@Column(nullable = false)
private String password;
```

Stores encrypted password.

Example:

```
$2a$10$encryptedpassword
```

Never store plain password.

---

# PART 8: role field

```java
@Enumerated(EnumType.STRING)
private Role role;
```

Stores role.

Example:

```
USER
ADMIN
```

Stored as string in database.

Example database:

```
role = USER
```

---

# PART 9: getAuthorities method

```java
@Override
public Collection<? extends GrantedAuthority> getAuthorities()
```

Spring Security calls this method.

This returns user role.

---

```java
return List.of(
        new SimpleGrantedAuthority(
                role.name()
        )
);
```

Example:

Role = USER

Spring Security receives:

```
USER
```

Spring uses this for authorization.

---

# PART 10: getPassword method

```java
@Override
public String getPassword() {

    return password;

}
```

Spring calls this method.

Spring compares password during login.

---

# PART 11: getUsername method

```java
@Override
public String getUsername() {

    return username;

}
```

Spring calls this method.

Spring identifies user using username.

---

# PART 12: isAccountNonExpired

```java
@Override
public boolean isAccountNonExpired() {

    return true;

}
```

This means account is valid.

If false → account expired.

---

# PART 13: isAccountNonLocked

```java
@Override
public boolean isAccountNonLocked() {

    return true;

}
```

Means account is not locked.

---

# PART 14: isCredentialsNonExpired

```java
@Override
public boolean isCredentialsNonExpired() {

    return true;

}
```

Means password is valid.

---

# PART 15: isEnabled

```java
@Override
public boolean isEnabled() {

    return true;

}
```

Means account is enabled.

If false → account disabled.

---

# Database Table Created

Table: users

Columns:

```
id
username
password
role
```

Example:

```
1 | ishant | encrypted | USER
```

---

# How Spring Security uses this class

Login flow:

Spring calls:

```
loadUserByUsername("ishant")
```

User returned.

Spring calls:

```
getPassword()
getUsername()
getAuthorities()
```

Spring verifies user.

---

# Why implements UserDetails needed

Spring Security only understands UserDetails.

This allows Spring Security to use User entity.

---

# Simple Summary

This class represents database user.

It stores username, password, role.

Spring Security uses this class to authenticate user.

---

# Interview Questions

Q: Why @Entity used?

Answer:

To create database table.

---

Q: Why implements UserDetails?

Answer:

Spring Security requires UserDetails.

---

Q: Why password encrypted?

Answer:

For security.

---

# Final Summary

User entity connects database and Spring Security.

It stores user data and allows authentication.
