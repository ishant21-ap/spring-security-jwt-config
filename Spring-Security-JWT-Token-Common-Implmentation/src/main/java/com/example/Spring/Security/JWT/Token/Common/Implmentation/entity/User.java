package com.example.Spring.Security.JWT.Token.Common.Implmentation.entity;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

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


    @Column(nullable = false, unique = true)
    private String username;


    @Column(nullable = false)
    private String password;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;



    /*
       Spring Security uses this method to get user roles
    */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(
                () -> role.name()
        );

    }

    /*
        Spring Security uses this password for authentication
     */
    @Override
    public String getPassword() {
        return password;
    }

    /*
        Spring Security uses this username for authentication
     */
    @Override
    public String getUsername() {
        return username;
    }

    /*
        Account status checks
        We keep all true for simplicity
     */

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
