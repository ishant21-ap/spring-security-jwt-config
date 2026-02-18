package com.example.Spring.Security.JWT.Token.Common.Implmentation.controller;


import com.example.Spring.Security.JWT.Token.Common.Implmentation.dto.AuthRequest;
import com.example.Spring.Security.JWT.Token.Common.Implmentation.dto.AuthResponse;
import com.example.Spring.Security.JWT.Token.Common.Implmentation.dto.RegisterRequest;
import com.example.Spring.Security.JWT.Token.Common.Implmentation.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /*
        Register endpoint
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @RequestBody RegisterRequest request
    ) {

        return ResponseEntity.ok(authService.register(request));

    }

    /*
        Login endpoint
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody AuthRequest request
    ) {

        return ResponseEntity.ok(authService.login(request));

    }

}
