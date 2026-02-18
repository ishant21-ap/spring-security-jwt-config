package com.example.Spring.Security.JWT.Token.Common.Implmentation.repository;

import com.example.Spring.Security.JWT.Token.Common.Implmentation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
