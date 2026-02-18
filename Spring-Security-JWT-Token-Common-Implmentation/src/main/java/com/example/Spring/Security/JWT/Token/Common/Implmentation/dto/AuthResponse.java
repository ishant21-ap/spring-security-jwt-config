package com.example.Spring.Security.JWT.Token.Common.Implmentation.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String token;
}
