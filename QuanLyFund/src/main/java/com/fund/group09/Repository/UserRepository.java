package com.fund.group09.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fund.group09.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
