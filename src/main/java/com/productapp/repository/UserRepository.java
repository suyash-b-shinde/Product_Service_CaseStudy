package com.productapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.productapp.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
