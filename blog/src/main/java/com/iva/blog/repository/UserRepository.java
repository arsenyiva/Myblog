package com.iva.blog.repository;

import com.iva.blog.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String name);

    Optional<User> findByEmail(String emailFromForm);
}
