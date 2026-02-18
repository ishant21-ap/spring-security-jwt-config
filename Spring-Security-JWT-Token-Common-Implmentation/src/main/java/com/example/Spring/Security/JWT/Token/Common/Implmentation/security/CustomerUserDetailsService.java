package com.example.Spring.Security.JWT.Token.Common.Implmentation.security;

import com.example.Spring.Security.JWT.Token.Common.Implmentation.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // This method is called by Spring Security during authentication

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.
                findByUsername(username).
                orElseThrow(() -> new UsernameNotFoundException("User not found" + username));
    }
}
