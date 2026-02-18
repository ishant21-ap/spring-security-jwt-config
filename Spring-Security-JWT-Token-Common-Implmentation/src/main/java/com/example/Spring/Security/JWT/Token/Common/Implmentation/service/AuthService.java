package com.example.Spring.Security.JWT.Token.Common.Implmentation.service;


import com.example.Spring.Security.JWT.Token.Common.Implmentation.dto.AuthRequest;
import com.example.Spring.Security.JWT.Token.Common.Implmentation.dto.AuthResponse;
import com.example.Spring.Security.JWT.Token.Common.Implmentation.dto.RegisterRequest;
import com.example.Spring.Security.JWT.Token.Common.Implmentation.entity.User;
import com.example.Spring.Security.JWT.Token.Common.Implmentation.repository.UserRepository;
import com.example.Spring.Security.JWT.Token.Common.Implmentation.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /*
        Register new user
     */
    public AuthResponse register(RegisterRequest request) {

        // Create user object
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        // Save to database
        userRepository.save(user);

        // Generate token
        String token = jwtService.generateToken(user.getUsername());

        return AuthResponse.builder()
                .token(token)
                .build();

    }

    /*
        Login user
     */
    public AuthResponse login(AuthRequest request) {

        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        // If authentication successful, generate token
        String token = jwtService.generateToken(request.getUsername());

        return AuthResponse.builder()
                .token(token)
                .build();

    }

}

