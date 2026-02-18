package com.example.Spring.Security.JWT.Token.Common.Implmentation.dto;


import com.example.Spring.Security.JWT.Token.Common.Implmentation.entity.Role;
import lombok.*;


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
