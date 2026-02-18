package com.example.Spring.Security.JWT.Token.Common.Implmentation.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    // This method runs for every request
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        //Get AuthoriZATION header
        final String authHeader = request.getHeader("Authorization");

        // If header is missing or doesn't start with Bearer, skip
        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract Token
        final String token = authHeader.substring(7);


        // Extract username from token
        final String username = jwtService.extractUsername(token);


        // If username exists and not already authenticated
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user from database
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Validate token
            if(jwtService.isTokenValid(token, userDetails.getUsername())){

                // Create Authentication object
                UsernamePasswordAuthenticationToken authToken =
                        new  UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // set authentication context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }




}
