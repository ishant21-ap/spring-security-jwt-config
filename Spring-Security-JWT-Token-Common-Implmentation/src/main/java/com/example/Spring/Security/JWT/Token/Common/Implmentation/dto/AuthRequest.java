package com.example.Spring.Security.JWT.Token.Common.Implmentation.dto;

import lombok.*;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthRequest {

    private String username;
    private String password;
}
